/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.lab1.html;

import java.util.function.Consumer;

/**
 *
 * @author Pavel.Stepanov
 */
public class HTMLBuilder {
    private final StringBuilder html = new StringBuilder();
    
    public HTMLBuilder doctype() {
        html.append("<!DOCTYPE html>\n");
        return this;
    }
    
    public HTMLBuilder html(Consumer<HTMLBuilder> content) {
        html.append("<html>\n");
        content.accept(this);
        html.append("</html>\n");
        return this;
    }
    
    public HTMLBuilder head(Consumer<HTMLBuilder> content) {
        html.append("<head>\n");
        content.accept(this);
        html.append("</head>\n");
        return this;
    }
    
    public HTMLBuilder title(String title) {
        html.append("<title>").append(title).append("</title>\n");
        return this;
    }
    
    public HTMLBuilder meta(String charset) {
        html.append("<meta charset=\"").append(charset).append("\">\n");
        return this;
    }
    
    public HTMLBuilder style(String css) {
        html.append("<style>\n").append(css).append("\n</style>\n");
        return this;
    }
    
    public HTMLBuilder script(String js) {
        html.append("<script>\n").append(js).append("\n</script>\n");
        return this;
    }
    
    public HTMLBuilder body(Consumer<HTMLBuilder> content) {
        html.append("<body>\n");
        content.accept(this);
        html.append("</body>\n");
        return this;
    }
    
    public HTMLBuilder div(String className, Consumer<HTMLBuilder> content) {
        html.append("<div class=\"").append(className).append("\">\n");
        content.accept(this);
        html.append("</div>\n");
        return this;
    }
    
    public HTMLBuilder h1(String text) {
        html.append("<h1>").append(text).append("</h1>\n");
        return this;
    }
    
    public HTMLBuilder h2(String text) {
        html.append("<h2>").append(text).append("</h2>\n");
        return this;
    }
    
    public HTMLBuilder h3(String text) {
        html.append("<h3>").append(text).append("</h3>\n");
        return this;
    }
    
    public HTMLBuilder p(String text) {
        html.append("<p>").append(text).append("</p>\n");
        return this;
    }
    
    public HTMLBuilder a(String href, String text) {
        html.append("<a href=\"").append(href).append("\">").append(text).append("</a>\n");
        return this;
    }
    
    public HTMLBuilder table(Consumer<HTMLBuilder> content) {
        html.append("<table>\n");
        content.accept(this);
        html.append("</table>\n");
        return this;
    }
    
    public HTMLBuilder thead(Consumer<HTMLBuilder> content) {
        html.append("<thead>\n");
        content.accept(this);
        html.append("</thead>\n");
        return this;
    }
    
    public HTMLBuilder tbody(Consumer<HTMLBuilder> content) {
        html.append("<tbody>\n");
        content.accept(this);
        html.append("</tbody>\n");
        return this;
    }
    
    public HTMLBuilder tr(Consumer<HTMLBuilder> content) {
        html.append("<tr>\n");
        content.accept(this);
        html.append("</tr>\n");
        return this;
    }
    
    public HTMLBuilder th(String text) {
        html.append("<th>").append(text).append("</th>\n");
        return this;
    }
    
    public HTMLBuilder td(String text) {
        html.append("<td>").append(text).append("</td>\n");
        return this;
    }
    
    public HTMLBuilder tdWithStyle(String text, String style) {
        html.append("<td style=\"").append(style).append("\">").append(text).append("</td>\n");
        return this;
    }
    
    public HTMLBuilder form(String action, String method, Consumer<HTMLBuilder> content) {
        html.append("<form action=\"").append(action).append("\" method=\"").append(method).append("\">\n");
        content.accept(this);
        html.append("</form>\n");
        return this;
    }
    
    public HTMLBuilder input(String type, String name, String value, String placeholder) {
        html.append("<input type=\"").append(type).append("\" name=\"").append(name).append("\"");
        if (value != null) {
            html.append(" value=\"").append(value).append("\"");
        }
        if (placeholder != null) {
            html.append(" placeholder=\"").append(placeholder).append("\"");
        }
        html.append(">\n");
        return this;
    }
    
    public HTMLBuilder select(String name, Consumer<HTMLBuilder> content) {
        html.append("<select name=\"").append(name).append("\">\n");
        content.accept(this);
        html.append("</select>\n");
        return this;
    }
    
    public HTMLBuilder option(String value, String text, boolean selected) {
        html.append("<option value=\"").append(value).append("\"");
        if (selected) {
            html.append(" selected");
        }
        html.append(">").append(text).append("</option>\n");
        return this;
    }
    
    public HTMLBuilder label(String forAttribute, String text) {
        html.append("<label for=\"").append(forAttribute).append("\">").append(text).append("</label>\n");
        return this;
    }
    
    public HTMLBuilder button(String type, String text) {
        html.append("<button type=\"").append(type).append("\">").append(text).append("</button>\n");
        return this;
    }
    
    public HTMLBuilder raw(String html) {
        this.html.append(html).append("\n");
        return this;
    }
    
    public String build() {
        return html.toString();
    }
}