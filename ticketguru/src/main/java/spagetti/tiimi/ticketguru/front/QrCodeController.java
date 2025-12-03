package spagetti.tiimi.ticketguru.front;

import java.awt.image.BufferedImage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/qr")
public class QrCodeController {

  @GetMapping(value = "/{ticketCode}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> generateQr(@PathVariable("ticketCode") String ticketCode)
      throws Exception {

    String url = ticketCode;

    QRCodeWriter writer = new QRCodeWriter();
    BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

    return new ResponseEntity<>(
        MatrixToImageWriter.toBufferedImage(bitMatrix),
        HttpStatus.OK);
  }

  @GetMapping("/qrreader")
  public String qrReaderPage() {
    return "qr-check";
  }

}
