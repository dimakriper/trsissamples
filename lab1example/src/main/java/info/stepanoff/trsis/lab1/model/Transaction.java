/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Pavel.Stepanov
 */
public class Transaction {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Category category;
    private TransactionType type;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(Long id, String description, BigDecimal amount, 
                      LocalDate date, Category category, TransactionType type) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}