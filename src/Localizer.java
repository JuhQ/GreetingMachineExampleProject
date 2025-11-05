import java.util.HashMap;
import java.util.Map;

/**
 * Minimal localizer with a small in-memory dictionary for demo purposes.
 * Keys used: salutation, time.morning, time.afternoon, time.evening
 */
public class Localizer {
    private static final Map<String, Map<String, String>> DICT = new HashMap<>();

    static {
        // English
        Map<String, String> en = new HashMap<>();
        en.put("salutation", "Dear");
        en.put("time.morning", "morning");
        en.put("time.afternoon", "afternoon");
        en.put("time.evening", "evening");
        DICT.put("en", en);

        // Spanish
        Map<String, String> es = new HashMap<>();
        es.put("salutation", "Estimado");
        es.put("time.morning", "mañana");
        es.put("time.afternoon", "tarde");
        es.put("time.evening", "noche");
        DICT.put("es", es);

        // French (very limited)
        Map<String, String> fr = new HashMap<>();
        fr.put("salutation", "Cher");
        fr.put("time.morning", "matin");
        fr.put("time.afternoon", "après-midi");
        fr.put("time.evening", "soir");
        DICT.put("fr", fr);
    }

    public static String get(String key, String localeTag) {
        if (key == null) return null;
        if (localeTag == null) localeTag = "en";
        Map<String, String> dict = DICT.get(localeTag);
        if (dict != null && dict.containsKey(key)) return dict.get(key);
        // fallback to language-only (e.g., en-US -> en)
        int dash = localeTag.indexOf('-');
        if (dash > 0) {
            String lang = localeTag.substring(0, dash);
            dict = DICT.get(lang);
            if (dict != null && dict.containsKey(key)) return dict.get(key);
        }
        // fallback to English
        Map<String, String> en = DICT.get("en");
        return en.getOrDefault(key, key);
    }
}

