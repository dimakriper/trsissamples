/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.servlets;

import info.stepanoff.trsis.lab1.dao.CategoryDAO;
import info.stepanoff.trsis.lab1.html.HTMLBuilder;
import info.stepanoff.trsis.lab1.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Pavel.Stepanov
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {"/category", "/category/*"})
public class CategoryServlet extends HttpServlet {

    private static final String CSS = "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }\n" +
            ".header { display: flex; justify-content: space-between; align-items: center; }\n" +
            ".form-group { margin-bottom: 15px; }\n" +
            "label { display: block; margin-bottom: 5px; }\n" +
            "input { width: 100%; padding: 8px; box-sizing: border-box; }\n" +
            "button { background-color: #0066cc; color: white; padding: 10px 15px; " +
            "         border: none; border-radius: 4px; cursor: pointer; }\n" +
            ".nav { display: flex; gap: 20px; margin-bottom: 20px; }";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        final Category category;
        
        // Check if we're editing an existing category
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                category = CategoryDAO.findById(id);
                if (category == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else {
            category = null;
        }
        
        final boolean isEditing = category != null;
        
        HTMLBuilder html = new HTMLBuilder()
            .doctype()
            .html(html1 -> html1
                .head(head -> head
                    .meta("UTF-8")
                    .title(isEditing ? "Edit Category" : "New Category")
                    .style(CSS)
                )
                .body(body -> {
                    body.div("header", header -> header
                        .h1(isEditing ? "Edit Category" : "New Category")
                        .div("nav", nav -> {
                            nav.a("/dashboard", "Dashboard");
                            nav.a("/transactions", "Transactions");
                            nav.a("/categories", "Categories");
                            nav.a("/category", "New Category");
                        })
                    );
                    
                    body.form(isEditing ? "/category/" + category.getId() : "/category", "post", form -> {
                        form.div("form-group", group -> {
                            group.label("name", "Name");
                            group.input("text", "name", 
                                isEditing ? category.getName() : "", "Enter category name");
                        });
                        
                        form.div("form-group", group -> {
                            group.label("color", "Color");
                            group.input("color", "color", 
                                isEditing ? category.getColor() : "#33CEFF", null);
                        });
                        
                        form.button("submit", isEditing ? "Update Category" : "Add Category");
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
        
        String name = request.getParameter("name");
        String color = request.getParameter("color");
        
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }
            
            Category category;
            if (isEditing) {
                Long id = Long.parseLong(pathInfo.substring(1));
                category = CategoryDAO.findById(id);
                if (category == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                
                category.setName(name);
                category.setColor(color);
                
                CategoryDAO.updateCategory(category);
            } else {
                category = new Category(null, name, color);
                CategoryDAO.addCategory(category);
            }
            
            response.sendRedirect(request.getContextPath() + "/categories");
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
            boolean deleted = CategoryDAO.deleteCategory(id);
            
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