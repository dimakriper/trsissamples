/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.servlets;

import info.stepanoff.trsis.lab1.dao.TransactionDAO;
import info.stepanoff.trsis.lab1.html.HTMLBuilder;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Pavel.Stepanov
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private static final String CSS = "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }\n" +
            ".header { display: flex; justify-content: space-between; align-items: center; }\n" +
            ".summary-cards { display: flex; gap: 20px; margin: 20px 0; }\n" +
            ".card { padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }\n" +
            ".income { background-color: #e6ffe6; }\n" +
            ".expense { background-color: #ffe6e6; }\n" +
            ".balance { background-color: #e6f2ff; }\n" +
            "table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n" +
            "th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }\n" +
            "th { background-color: #f2f2f2; }\n" +
            ".action-links { display: flex; gap: 10px; }\n" +
            "a { text-decoration: none; color: #0066cc; }\n" +
            "a:hover { text-decoration: underline; }\n" +
            ".nav { display: flex; gap: 20px; margin-bottom: 20px; }";

    private static final String JS = "function deleteTransaction(id) {\n" +
            "    if (confirm('Are you sure you want to delete this transaction?')) {\n" +
            "        fetch('/transaction/' + id, {\n" +
            "            method: 'DELETE'\n" +
            "        }).then(response => {\n" +
            "            if (response.ok) {\n" +
            "                window.location.reload();\n" +
            "            } else {\n" +
            "                alert('Failed to delete transaction');\n" +
            "            }\n" +
            "        });\n" +
            "    }\n" +
            "}";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get summary data
        BigDecimal totalIncome = TransactionDAO.getTotalByType(TransactionType.INCOME);
        BigDecimal totalExpense = TransactionDAO.getTotalByType(TransactionType.EXPENSE);
        BigDecimal balance = totalIncome.subtract(totalExpense);
        List<Transaction> recentTransactions = TransactionDAO.getAllTransactions();
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        HTMLBuilder html = new HTMLBuilder()
            .doctype()
            .html(html1 -> html1
                .head(head -> head
                    .meta("UTF-8")
                    .title("Budget Dashboard")
                    .style(CSS)
                )
                .body(body -> {
                    body.div("header", header -> header
                        .h1("Budget Dashboard")
                        .div("nav", nav -> {
                            nav.a("/dashboard", "Dashboard");
                            nav.a("/transactions", "Transactions");
                            nav.a("/categories", "Categories");
                            nav.a("/transaction", "New Transaction");
                        })
                    );
                    
                    body.div("summary-cards", cards -> {
                        cards.div("card income", card -> {
                            card.h3("Total Income");
                            card.h2("$" + totalIncome);
                        });
                        cards.div("card expense", card -> {
                            card.h3("Total Expenses");
                            card.h2("$" + totalExpense);
                        });
                        cards.div("card balance", card -> {
                            card.h3("Current Balance");
                            card.h2("$" + balance);
                        });
                    });
                    
                    body.h2("Recent Transactions");
                    body.table(table -> {
                        table.thead(thead -> thead.tr(tr -> {
                            tr.th("Date");
                            tr.th("Description");
                            tr.th("Category");
                            tr.th("Type");
                            tr.th("Amount");
                            tr.th("Actions");
                        }));
                        
                        table.tbody(tbody -> {
                            for (Transaction transaction : recentTransactions) {
                                tbody.tr(tr -> {
                                    tr.td(transaction.getDate().format(dateFormatter));
                                    tr.td(transaction.getDescription());
                                    tr.td(transaction.getCategory() != null ? transaction.getCategory().getName() : "N/A");
                                    tr.td(transaction.getType().toString());
                                    
                                    String amountColor = transaction.getType() == TransactionType.INCOME ? "green" : "red";
                                    tr.tdWithStyle("$" + transaction.getAmount(), "color: " + amountColor);
                                    
                                    tr.td("<a href=\"/transaction/" + transaction.getId() + "\">Edit</a> | " +
                                         "<a href=\"#\" onclick=\"deleteTransaction(" + transaction.getId() + ")\">Delete</a>");
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