public class User {
    private String name;
    String getName() { return name; }
    void setName(String name) { this.name = name; }

    User(String name) { this.name = name; }
    User()            { this(String.format("User%d", 100_000 + Global.rand.nextInt(999_999 - 100_000 + 1))); }
}
