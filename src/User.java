public class User {
    private final String name;
    private final String title;
    private final String localeTag;

    public User(String name, String title, String localeTag) {
        this.name = name;
        this.title = title;
        this.localeTag = localeTag != null ? localeTag : "en";
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLocaleTag() {
        return localeTag;
    }
}

