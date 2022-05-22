package antifraud.model.transaction;

public enum RegionCode {
    EAP("East Asia and Pacific"),
    ECA("Europe and Central Asia"),
    HIC("High-Income countries"),
    LAC("Latin America and the Caribbean"),
    MENA("The Middle East and North Africa"),
    SA("South Asia"),
    SSA("Sub-Saharan Africa");

    private String text;

    RegionCode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
