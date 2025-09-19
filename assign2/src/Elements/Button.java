public class Button extends ElementBasic implements Element {
    // Custom fields
    private String text;
    private Runnable action;

    // Custom get/set methods
    public String getText()                { return text; }
    public void setText(String text)       { this.text = text; }
    public void setAction(Runnable action) { this.action = action; }

    // Base methods
    public void render() {
        Engine.DrawRectangle(pos, size, backgroundColor);
        Engine.DrawText(pos, size, foregroundColor, text);
    }
    public void update() {}
    public void onClick() {
        this.action.run();
    }
    @Override
    public String toString() {
        return super.toString() + String.format(" [\n" +
                "    text: \"%s\"\n" +
                "]", text);
    }
}
