/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.dao;

import info.stepanoff.trsis.lab1.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Pavel.Stepanov
 */
public class CategoryDAO {
    private static final Map<Long, Category> categories = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong();
    
    static {
        // Add some sample categories
        addCategory(new Category(idCounter.incrementAndGet(), "Food", "#FF5733"));
        addCategory(new Category(idCounter.incrementAndGet(), "Transport", "#33FF57"));
        addCategory(new Category(idCounter.incrementAndGet(), "Entertainment", "#3357FF"));
        addCategory(new Category(idCounter.incrementAndGet(), "Utilities", "#F3FF33"));
        addCategory(new Category(idCounter.incrementAndGet(), "Housing", "#FF33F3"));
    }
    
    public static List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }
    
    public static Category findById(Long id) {
        return categories.get(id);
    }
    
    public static Category findByName(String name) {
        return categories.values().stream()
            .filter(c -> c.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    public static Category addCategory(Category category) {
        if (category.getId() == null) {
            category.setId(idCounter.incrementAndGet());
        }
        categories.put(category.getId(), category);
        return category;
    }
    
    public static boolean updateCategory(Category category) {
        if (category.getId() == null || !categories.containsKey(category.getId())) {
            return false;
        }
        categories.put(category.getId(), category);
        return true;
    }
    
    public static boolean deleteCategory(Long id) {
        return categories.remove(id) != null;
    }
}