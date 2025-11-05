import java.util.Map;

public class TemplateEngine {
    /**
     * Very small templating: replaces {key} with value from vars. If a value is missing, replaces with empty string.
     * Supports escaping of braces by doubling them: {{ and }} => { and }.
     */
    public static String render(String template, Map<String, String> vars) {
        if (template == null) return "";
        // simple escape handling
        template = template.replace("{{", "\u0000LEFT_BRACE\u0000").replace("}}", "\u0000RIGHT_BRACE\u0000");
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < template.length()) {
            char c = template.charAt(i);
            if (c == '{') {
                int end = template.indexOf('}', i);
                if (end == -1) {
                    out.append(c); // no matching brace
                    i++;
                } else {
                    String key = template.substring(i + 1, end).trim();
                    String val = vars.getOrDefault(key, "");
                    out.append(val);
                    i = end + 1;
                }
            } else {
                out.append(c);
                i++;
            }
        }
        String result = out.toString().replace("\u0000LEFT_BRACE\u0000", "{").replace("\u0000RIGHT_BRACE\u0000", "}");
        return result;
    }
}

