package spagetti.tiimi.ticketguru.domain;

public class Ticket {

    private String name;

    public Ticket () {

    }

    public Ticket (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ticket: [name: " + name + "]";
    }

}
