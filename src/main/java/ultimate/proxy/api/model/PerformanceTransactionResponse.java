package ultimate.proxy.api.model;

import lombok.Data;
import ultimate.proxy.domain.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PerformanceTransactionResponse {
    private final String serviceName;
    private final List<HashMap> transactions;

    public PerformanceTransactionResponse(String serviceName, List<HashMap> transactions) {
        this.serviceName = serviceName;
        this.transactions = transactions;
    }

    public static PerformanceTransactionResponse from(String serviceName, List<Transaction> transactions) {
        List<HashMap> performanceMatrix = transactions.stream()
                .map(PerformanceTransactionResponse::mapMatrix)
                .collect(Collectors.toList());
        return new PerformanceTransactionResponse(serviceName, performanceMatrix);
    }

    private static HashMap mapMatrix(Transaction transaction) {
        HashMap matrix = new HashMap();
        matrix.put("executionTime", transaction.getExecutionTime());
        matrix.put("startTime", transaction.getStartTime());
        return matrix;
    }
}
