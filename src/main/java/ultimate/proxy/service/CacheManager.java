package ultimate.proxy.service;

import org.springframework.stereotype.Component;
import ultimate.proxy.domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Comparator.comparing;

@Component
public class CacheManager {

    private final HashMap<String, List<Transaction>> transactions;

    public CacheManager() {
        transactions = new HashMap<>();
    }

    public void save(String serviceName, Transaction transaction) {
        transactions.putIfAbsent(serviceName, new ArrayList<>());
        transactions.get(serviceName).add(transaction);
    }

    public List<Transaction> get(String serviceName) {
        List<Transaction> transactions = this.transactions.get(serviceName);
        transactions.sort(comparing(Transaction::getStartTime));
        return transactions;
    }

    public HashMap<String, List<Transaction>> getAll() {
        HashMap<String, List<Transaction>> sortedTransactions = new HashMap<>();
        transactions
                .keySet()
                .forEach(serviceName -> sortedTransactions.put(serviceName, get(serviceName)));
        return sortedTransactions;
    }
}
