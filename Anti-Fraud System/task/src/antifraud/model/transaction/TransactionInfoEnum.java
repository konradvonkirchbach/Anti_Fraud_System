package antifraud.model.transaction;

public enum TransactionInfoEnum {
    NONE("none"),
    AMOUNT("amount"),
    IP("ip"),
    NUMBER("card-number"),
    IP_CORRELATION("ip-correlation"),
    REGION_CORRELATION("region-correlation");

    private String text;

    TransactionInfoEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
