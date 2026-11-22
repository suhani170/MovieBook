package util;

/**
 * Small helper to safely print user/database text inside HTML.
 * Prevents XSS (e.g. a movie title like <script>...</script>).
 */
public class HtmlUtil {

    public static String esc(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
