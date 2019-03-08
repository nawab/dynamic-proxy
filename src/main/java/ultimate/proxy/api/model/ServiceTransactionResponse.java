package ultimate.proxy.api.model;

import lombok.Data;
import ultimate.proxy.domain.Transaction;

import java.util.List;

@Data
public class ServiceTransactionResponse {

    public static ServiceTransactionResponse from(String key, List<Transaction> value) {
        return null;
    }
}
