/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.servlets;

import info.stepanoff.trsis.lab1.dao.CategoryDAO;
import info.stepanoff.trsis.lab1.dao.TransactionDAO;
import info.stepanoff.trsis.lab1.html.HTMLBuilder;
import info.stepanoff.trsis.lab1.model.Category;
import info.stepanoff.trsis.lab1.model.Transaction;
import info.stepanoff.trsis.lab1.model.TransactionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pavel.Stepanov
 */
@WebServlet(name = "TransactionServlet", urlPatterns = {"/transaction", "/transaction/*"})
public class TransactionServlet extends HttpServlet {

    private static final String CSS = "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }\n" +
            ".header { display: flex; justify-content: space-between; align-items: center; }\n" +
            ".form-group { margin-bottom: 15px; }\n" +
            "label { display: block; margin-bottom: 5px; }\n" +
            "input, select { width: 100%; padding: 8px; box-sizing: border-box; }\n" +
            "button { background-color: #0066cc; color: white; padding: 10px 15px; " +
            "         border: none; border-radius: 4px; cursor: pointer; }\n" +
            ".nav { display: flex; gap: 20px; margin-bottom: 20px; }";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        final Transaction transaction;
        
        // Check if we're editing an existing transaction
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                transaction = TransactionDAO.findById(id);
                if (transaction == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else {
            transaction = null;
        }
        
        final boolean isEditing = transaction != null;
        final List<Category> categories = CategoryDAO.getAllCategories();
        
        HTMLBuilder html = new HTMLBuilder()
            .doctype()
            .html(html1 -> html1
                .head(head -> head
                    .meta("UTF-8")
                    .title(isEditing ? "Edit Transaction" : "New Transaction")
                    .style(CSS)
                )
                .body(body -> {
                    body.div("header", header -> header
                        .h1(isEditing ? "Edit Transaction" : "New Transaction")
                        .div("nav", nav -> {
                            nav.a("/dashboard", "Dashboard");
                            nav.a("/transactions", "Transactions");
                            nav.a("/categories", "Categories");
                            nav.a("/transaction", "New Transaction");
                        })
                    );
                    
                    body.form(isEditing ? "/transaction/" + transaction.getId() : "/transaction", "post", form -> {
                        form.div("form-group", group -> {
                            group.label("description", "Description");
                            group.input("text", "description", 
                                isEditing ? transaction.getDescription() : "", "Enter description");
                        });
                        
                        form.div("form-group", group -> {
                            group.label("amount", "Amount");
                            group.input("number", "amount", 
                                isEditing ? transaction.getAmount().toString() : "", "0.00");
                        });
                        
                        form.div("form-group", group -> {
                            group.label("date", "Date");
                            group.input("date", "date", 
                                isEditing ? transaction.getDate().toString() : LocalDate.now().toString(), null);
                        });
                        
                        form.div("form-group", group -> {
                            group.label("category", "Category");
                            group.select("categoryId", select -> {
                                select.option("", "-- Select Category --", false);
                                for (final Category category : categories) {
                                    boolean selected = isEditing && 
                                        transaction.getCategory() != null && 
                                        category.getId().equals(transaction.getCategory().getId());
                                    select.option(category.getId().toString(), category.getName(), selected);
                                }
                            });
                        });
                        
                        form.div("form-group", group -> {
                            group.label("type", "Type");
                            group.select("type", select -> {
                                boolean incomeSelected = isEditing && transaction.getType() == TransactionType.INCOME;
                                boolean expenseSelected = isEditing && transaction.getType() == TransactionType.EXPENSE;
                                select.option(TransactionType.INCOME.toString(), "Income", incomeSelected);
                                select.option(TransactionType.EXPENSE.toString(), "Expense", expenseSelected);
                            });
                        });
                        
                        form.button("submit", isEditing ? "Update Transaction" : "Add Transaction");
                    });
                })
            );
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(html.build());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        boolean isEditing = pathInfo != null && pathInfo.length() > 1;
        
        String description = request.getParameter("description");
        String amountStr = request.getParameter("amount");
        String dateStr = request.getParameter("date");
        String categoryIdStr = request.getParameter("categoryId");
        String typeStr = request.getParameter("type");
        
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            LocalDate date = LocalDate.parse(dateStr);
            TransactionType type = TransactionType.valueOf(typeStr);
            
            Category category = null;
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                Long categoryId = Long.parseLong(categoryIdStr);
                category = CategoryDAO.findById(categoryId);
            }
            
            Transaction transaction;
            if (isEditing) {
                Long id = Long.parseLong(pathInfo.substring(1));
                transaction = TransactionDAO.findById(id);
                if (transaction == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                
                transaction.setDescription(description);
                transaction.setAmount(amount);
                transaction.setDate(date);
                transaction.setCategory(category);
                transaction.setType(type);
                
                TransactionDAO.updateTransaction(transaction);
            } else {
                transaction = new Transaction(null, description, amount, date, category, type);
                TransactionDAO.addTransaction(transaction);
            }
            
            response.sendRedirect(request.getContextPath() + "/transactions");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid form data: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean deleted = TransactionDAO.deleteTransaction(id);
            
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}