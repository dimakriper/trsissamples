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
import java.util.List;

/**
 *
 * @author Pavel.Stepanov
 */
@WebServlet(name = "CategoryListServlet", urlPatterns = {"/categories"})
public class CategoryListServlet extends HttpServlet {

    private static final String CSS = "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }\n" +
            ".header { display: flex; justify-content: space-between; align-items: center; }\n" +
            "table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n" +
            "th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }\n" +
            "th { background-color: #f2f2f2; }\n" +
            ".action-links { display: flex; gap: 10px; }\n" +
            "a { text-decoration: none; color: #0066cc; }\n" +
            "a:hover { text-decoration: underline; }\n" +
            ".nav { display: flex; gap: 20px; margin-bottom: 20px; }\n" +
            ".color-swatch { width: 24px; height: 24px; display: inline-block; border-radius: 4px; }\n" +
            ".button { background-color: #0066cc; color: white; padding: 10px 15px; " +
            "          border: none; border-radius: 4px; cursor: pointer; }";

    private static final String JS = "function deleteCategory(id) {\n" +
            "    if (confirm('Are you sure you want to delete this category?')) {\n" +
            "        fetch('/category/' + id, {\n" +
            "            method: 'DELETE'\n" +
            "        }).then(response => {\n" +
            "            if (response.ok) {\n" +
            "                window.location.reload();\n" +
            "            } else {\n" +
            "                alert('Failed to delete category');\n" +
            "            }\n" +
            "        });\n" +
            "    }\n" +
            "}";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = CategoryDAO.getAllCategories();
        
        HTMLBuilder html = new HTMLBuilder()
            .doctype()
            .html(html1 -> html1
                .head(head -> head
                    .meta("UTF-8")
                    .title("Categories")
                    .style(CSS)
                )
                .body(body -> {
                    body.div("header", header -> header
                        .h1("Categories")
                        .div("nav", nav -> {
                            nav.a("/dashboard", "Dashboard");
                            nav.a("/transactions", "Transactions");
                            nav.a("/categories", "Categories");
                            nav.a("/category", "New Category");
                        })
                    );
                    
                    body.table(table -> {
                        table.thead(thead -> thead.tr(tr -> {
                            tr.th("Name");
                            tr.th("Color");
                            tr.th("Actions");
                        }));
                        
                        table.tbody(tbody -> {
                            for (Category category : categories) {
                                tbody.tr(tr -> {
                                    tr.td(category.getName());
                                    tr.td("<div class=\"color-swatch\" style=\"background-color: " + 
                                        category.getColor() + ";\"></div> " + category.getColor());
                                    tr.td("<a href=\"/category/" + category.getId() + "\">Edit</a> | " +
                                         "<a href=\"#\" onclick=\"deleteCategory(" + category.getId() + ")\">Delete</a>");
                                });
                            }
                        });
                    });
                    
                    body.script(JS);
                })
            );
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(html.build());
        }
    }
}