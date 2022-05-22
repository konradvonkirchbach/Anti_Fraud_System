package antifraud.model.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionProcessResult {

    private TransactionResponseStatus status;

    private final Set<TransactionInfoEnum> infos = new HashSet<>();

    public TransactionResponseStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionResponseStatus status) {
        this.status = status;
    }

    public Set<TransactionInfoEnum> getInfos() {
        return infos;
    }
}
