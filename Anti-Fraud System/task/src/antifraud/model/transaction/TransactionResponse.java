package antifraud.model.transaction;

import javax.persistence.Entity;
import java.util.stream.Collectors;

public class TransactionResponse {

    private TransactionResponseStatus result;

    private String info;

    public TransactionResponse() {

    }

    public TransactionResponse(TransactionProcessResult result) {
        this.result = result.getStatus();

        if (result.getInfos().size() > 1) {
            result.getInfos().remove(TransactionInfoEnum.NONE);
        }

        this.info = result.getInfos().stream()
                .map(TransactionInfoEnum::getText)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    public TransactionResponseStatus getResult() {
        return result;
    }

    public void setResult(TransactionResponseStatus result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
