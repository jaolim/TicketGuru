package spagetti.tiimi.ticketguru;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SellTicketE2ETest {

    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl = "https://ticket-guru-ticketguru-postgres.2.rahtiapp.fi";

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "25OhPoAdmin##");
        return headers;
    }

    private String extractFirstId(String json, String fieldName) {
        Pattern p = Pattern.compile("\\\"" + fieldName + "\\\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        throw new AssertionError("Field '" + fieldName + "' not found in response body: " + json);
    }

    @Test
    void sellTicket_endToEndFlow() {

        ResponseEntity<String> events = rest.exchange(
                baseUrl + "/events",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class
        );

        assert events.getStatusCode() == HttpStatus.OK;
        assert events.getBody().contains("Event1");

        ResponseEntity<String> sales = rest.exchange(
                baseUrl + "/sales",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class
        );
        assert sales.getStatusCode() == HttpStatus.OK;
        assert sales.getBody().contains("\"saleid\"");
        String saleid = extractFirstId(sales.getBody(), "saleid");

        String ticketJson = """
        {
            "redeemed": false,
            "cost": {"costid": 1},
            "sale": {"saleid": %s}
        }
        """.formatted(saleid);

        HttpHeaders headers = authHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> createdTicket = rest.exchange(
                baseUrl + "/tickets",
                HttpMethod.POST,
                new HttpEntity<>(ticketJson, headers),
                String.class
        );

        assert createdTicket.getStatusCode() == HttpStatus.CREATED;

        ResponseEntity<String> tickets = rest.exchange(
                baseUrl + "/tickets",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                String.class
        );

        assert tickets.getStatusCode() == HttpStatus.OK;
        assert tickets.getBody().contains("ticketid");
        assert tickets.getBody().contains("cost");
    }
}
