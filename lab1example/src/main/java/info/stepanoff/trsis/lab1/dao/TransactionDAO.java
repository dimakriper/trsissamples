/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.dao;

import info.stepanoff.trsis.lab1.model.Transaction;
import info.stepanoff.trsis.lab1.model.TransactionType;
import info.stepanoff.trsis.lab1.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *
 * @author Pavel.Stepanov
 */
public class TransactionDAO {
    private static final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong();
    
    static {
        // Add some sample data
        CategoryDAO categoryDAO = new CategoryDAO();
        Category food = categoryDAO.findById(1L);
        Category transport = categoryDAO.findById(2L);
        
        addTransaction(new Transaction(idCounter.incrementAndGet(), "Grocery shopping", 
            new BigDecimal("50.75"), LocalDate.now(), food, TransactionType.EXPENSE));
        addTransaction(new Transaction(idCounter.incrementAndGet(), "Bus ticket", 
            new BigDecimal("2.50"), LocalDate.now(), transport, TransactionType.EXPENSE));
        addTransaction(new Transaction(idCounter.incrementAndGet(), "Salary", 
            new BigDecimal("2000.00"), LocalDate.now(), null, TransactionType.INCOME));
    }
    
    public static List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
    
    public static Transaction findById(Long id) {
        return transactions.get(id);
    }
    
    public static Transaction addTransaction(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(idCounter.incrementAndGet());
        }
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }
    
    public static boolean updateTransaction(Transaction transaction) {
        if (transaction.getId() == null || !transactions.containsKey(transaction.getId())) {
            return false;
        }
        transactions.put(transaction.getId(), transaction);
        return true;
    }
    
    public static boolean deleteTransaction(Long id) {
        return transactions.remove(id) != null;
    }
    
    public static List<Transaction> getTransactionsByType(TransactionType type) {
        return transactions.values().stream()
            .filter(t -> t.getType() == type)
            .collect(Collectors.toList());
    }
    
    public static BigDecimal getTotalByType(TransactionType type) {
        return transactions.values().stream()
            .filter(t -> t.getType() == type)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public static List<Transaction> getTransactionsByCategory(Long categoryId) {
        return transactions.values().stream()
            .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        return transactions.values().stream()
            .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
            .collect(Collectors.toList());
    }
}