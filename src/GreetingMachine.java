import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GreetingMachine {
    private final Map<GreetingType, String> templates = new EnumMap<>(GreetingType.class);

    public GreetingMachine() {
        // default templates (use placeholders: {salutation},{title},{name},{timeOfDay},{customMessage})
        templates.put(GreetingType.FORMAL, "{salutation} {title} {name}, good {timeOfDay}. {customMessage}");
        templates.put(GreetingType.CASUAL, "Hey {name}! {timeOfDay} — {customMessage}");
        templates.put(GreetingType.FRIENDLY, "Hi {name}, hope you're having a lovely {timeOfDay}! {customMessage}");
        templates.put(GreetingType.HUMOROUS, "Yo {name}! It's {timeOfDay} — stay awesome. {customMessage}");
        templates.put(GreetingType.CUSTOM, "{customMessage}");
    }

    public void registerTemplate(GreetingType type, String template) {
        if (type == null || template == null) throw new IllegalArgumentException("type and template cannot be null");
        templates.put(type, template);
    }

    public String generateGreeting(User user, GreetingType type, String localeTag, String customMessage) {
        if (user == null) throw new IllegalArgumentException("user cannot be null");
        if (type == null) type = GreetingType.CASUAL;
        String template = templates.getOrDefault(type, "{customMessage}");

        // determine time of day (simple)
        String timeOfDayKey = resolveTimeOfDayKey(LocalTime.now());

        // build variables map
        Map<String, String> vars = new HashMap<>();
        // salutation from Localizer
        String salutation = Localizer.get("salutation", localeTag);
        vars.put("salutation", salutation != null ? salutation : "Hello");
        vars.put("title", user.getTitle() != null ? user.getTitle() : "");
        vars.put("name", user.getName() != null ? user.getName() : "there");
        vars.put("timeOfDay", Localizer.get(timeOfDayKey, localeTag));
        vars.put("customMessage", customMessage != null ? customMessage : "");

        // render
        return TemplateEngine.render(template, vars);
    }

    private String resolveTimeOfDayKey(LocalTime now) {
        int hour = now.getHour();
        if (hour < 12) return "time.morning";
        if (hour < 17) return "time.afternoon";
        return "time.evening";
    }

    // Small demo that exercises the system in many ways
    public static void main(String[] args) {
        GreetingMachine gm = new GreetingMachine();

        // register an extra custom template showing advanced placeholders
        gm.registerTemplate(GreetingType.CUSTOM, "[CUSTOM GREETING] {salutation} {name}! ({timeOfDay}) -> {customMessage}");

        List<User> users = Arrays.asList(
                new User("Alice", "Dr." , "en"),
                new User("Carlos", null, "es"),
                new User("François", "M.", "fr"),
                new User(null, null, "en")
        );

        System.out.println("=== Greeting Machine Demo ===");

        for (User u : users) {
            System.out.println("\n-- For user: " + (u.getName() != null ? u.getName() : "<unknown>") + " (locale=" + u.getLocaleTag() + ") --");

            // Formal
            String g1 = gm.generateGreeting(u, GreetingType.FORMAL, u.getLocaleTag(), "Welcome to our system.");
            System.out.println("Formal: " + g1);

            // Casual
            String g2 = gm.generateGreeting(u, GreetingType.CASUAL, u.getLocaleTag(), "Nice to see you!");
            System.out.println("Casual: " + g2);

            // Friendly with empty custom message
            String g3 = gm.generateGreeting(u, GreetingType.FRIENDLY, u.getLocaleTag(), "");
            System.out.println("Friendly: " + g3);

            // Humorous
            String g4 = gm.generateGreeting(u, GreetingType.HUMOROUS, u.getLocaleTag(), "Don't forget your coffee.");
            System.out.println("Humorous: " + g4);

            // Custom
            String g5 = gm.generateGreeting(u, GreetingType.CUSTOM, u.getLocaleTag(), "This is a one-off announcement.");
            System.out.println("Custom: " + g5);
        }

        // Show behavior with explicit locale override
        System.out.println("\n-- Locale override example --");
        User bob = new User("Bob", "Mr.", "en");
        String spanishFormal = gm.generateGreeting(bob, GreetingType.FORMAL, "es", "Bienvenido!");
        System.out.println("Bob formal in Spanish: " + spanishFormal);

        // Show error handling
        System.out.println("\n-- Error handling examples --");
        try {
            gm.generateGreeting(null, GreetingType.CASUAL, "en", "test");
        } catch (IllegalArgumentException ex) {
            System.out.println("Expected error: " + ex.getMessage());
        }

        // Show time stamped greeting
        String stamped = gm.generateGreeting(new User("TimeLord", null, "en"), GreetingType.CASUAL, "en", "Current time is " + DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now()));
        System.out.println("\nTime stamped greeting: " + stamped);

        System.out.println("\n=== Demo finished ===");
    }
}