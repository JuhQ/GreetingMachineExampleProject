package test.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GreetingMachineUnitTests {

    @Test
    void generateGreetingContainsNameAndSalutationForFormal() {
        GreetingMachine gm = new GreetingMachine();
        User user = new User("Alice", "Dr.", "en");
        String greeting = gm.generateGreeting(user, GreetingType.FORMAL, "en", "Welcome to our system.");
        assertTrue(greeting.contains("Alice"));
        assertTrue(greeting.contains("Dr."));
        assertTrue(greeting.contains("Welcome to our system."));
        assertTrue(greeting.contains(Localizer.get("salutation", "en")));
    }

    @Test
    void generateGreetingWithNullUserThrowsException() {
        GreetingMachine gm = new GreetingMachine();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                gm.generateGreeting(null, GreetingType.CASUAL, "en", "test"));
        assertEquals("user cannot be null", exception.getMessage());
    }

    @Test
    void nullTypeDefaultsToCasualGreetingFormat() {
        GreetingMachine gm = new GreetingMachine();
        User user = new User("Bob", null, "en");
        String greeting = gm.generateGreeting(user, null, "en", "Nice to see you!");
        assertTrue(greeting.contains("Hey Bob!"));
        assertTrue(greeting.contains("Nice to see you!"));
    }

    @Test
    void nullLocaleFallsBackToEnglishSalutation() {
        GreetingMachine gm = new GreetingMachine();
        User user = new User("Eve", "Ms.", null);
        String greeting = gm.generateGreeting(user, GreetingType.FORMAL, null, "Welcome!");
        assertTrue(greeting.contains("Eve"));
        assertTrue(greeting.contains("Ms."));
        assertTrue(greeting.contains(Localizer.get("salutation", "en")));
    }

    @Test
    void emptyCustomMessageIsHandledGracefully() {
        GreetingMachine gm = new GreetingMachine();
        User user = new User("John", null, "en");
        String greeting = gm.generateGreeting(user, GreetingType.CASUAL, "en", "");
        assertTrue(greeting.contains("Hey John!"));
        assertFalse(greeting.contains("null"));
    }

    @Test
    void registerTemplateWithNullTypeThrowsException() {
        GreetingMachine gm = new GreetingMachine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gm.registerTemplate(null, "Template"));
        assertEquals("type and template cannot be null", ex.getMessage());
    }

    @Test
    void registerTemplateWithNullTemplateThrowsException() {
        GreetingMachine gm = new GreetingMachine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> gm.registerTemplate(GreetingType.CUSTOM, null));
        assertEquals("type and template cannot be null", ex.getMessage());
    }

    @Test
    void customTemplateRegistrationAndRenderingProducesExpectedContent() {
        GreetingMachine gm = new GreetingMachine();
        gm.registerTemplate(GreetingType.CUSTOM, "[CUSTOM] {salutation} {name}: {customMessage}");
        User user = new User("Alice", null, "en");
        String out = gm.generateGreeting(user, GreetingType.CUSTOM, "en", "Announcement");
        assertTrue(out.contains("[CUSTOM]"));
        assertTrue(out.contains("Alice"));
        assertTrue(out.contains("Announcement"));
        assertTrue(out.contains(Localizer.get("salutation", "en")));
    }

    @Test
    void nullTypeWithCustomMessageUsesCasualFallback() {
        GreetingMachine gm = new GreetingMachine();
        User user = new User("Zara", null, "en");
        String greeting = gm.generateGreeting(user, null, "en", "Fallback message.");
        assertTrue(greeting.contains("Hey Zara!"));
        assertTrue(greeting.contains("Fallback message."));
    }
}

