package ultimate.proxy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ultimate.proxy.api.model.PerformanceTransactionResponse;
import ultimate.proxy.api.model.ServiceTransactionResponse;
import ultimate.proxy.domain.Transaction;
import ultimate.proxy.service.CacheManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transactions")
public class TransactionAPI {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping
    public ResponseEntity getAllTransactions() {
        return ResponseEntity.ok().body(cacheManager.getAll());
    }

    @GetMapping("/service")
    public ResponseEntity getServiceTransactions() {
        List<Object> collect = cacheManager.getAll()
                .entrySet()
                .stream()
                .map(this::mapToServiceTransaction)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @GetMapping("/performance")
    public ResponseEntity getPerformanceTransactions() {
        List<Object> collect = cacheManager.getAll()
                .entrySet()
                .stream()
                .map(this::mapToPerformanceTransaction)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    private ServiceTransactionResponse mapToServiceTransaction(Map.Entry<String, List<Transaction>> entry) {
        return ServiceTransactionResponse.from(entry.getKey(), entry.getValue());
    }

    private PerformanceTransactionResponse mapToPerformanceTransaction(Map.Entry<String, List<Transaction>> entry) {
        return PerformanceTransactionResponse.from(entry.getKey(), entry.getValue());
    }
}
