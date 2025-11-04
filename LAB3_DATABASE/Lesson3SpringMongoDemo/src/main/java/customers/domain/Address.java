package customers.domain;

public record Address(String street, String city, String state, String zipCode) {
    @Override
    public String street() {
        return street;
    }

    @Override
    public String city() {
        return city;
    }

    @Override
    public String state() {
        return state;
    }

    @Override
    public String zipCode() {
        return zipCode;
    }
}
