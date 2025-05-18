/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.model;

/**
 *
 * @author Pavel.Stepanov
 */
public class Category {
    private Long id;
    private String name;
    private String color;
    
    // Constructors
    public Category() {}
    
    public Category(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}