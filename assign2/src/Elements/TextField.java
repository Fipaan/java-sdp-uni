public class TextField extends ElementBasic implements Element {
    private boolean focused;
    private String text = "";
    private int cursor = 0;
    
    public boolean getFocused() { return focused; }
    public void setFocused(boolean focused) { this.focused = focused; }
    
    public void render() {
        Engine.ScissorsBegin(pos, size);
        Engine.DrawText(pos, size, foregroundColor, text.substring(cursor));
        Engine.ScissorsEnd();
    }
    public void update() {
        if (!focused) return;
        String input = Engine.GetInputAsString();
        if (input.length() == 0) return;
        text = text.substring(0, cursor) + input + text.substring(cursor);
        cursor += input.length();
    }
    public void onClick() {
        focused = true;
    }
    @Override
    public String toString() {
        String tmpTxt = "";
        assert cursor <= text.length();
        if (cursor > 0) {
            tmpTxt += text.substring(0, cursor);
            if (cursor < text.length()) {
                 tmpTxt += "|" + text.charAt(cursor);
            }
            if (cursor + 1 < text.length()) {
                tmpTxt += "|" + text.substring(cursor + 1);
            }
        }
        return super.toString() + String.format(
                    " [\n" +
                    "    focused: %s,\n" +
                    "    text: \"%s\"\n" +
                    "]",
                    focused ? "true" : "false",
                    tmpTxt
        );
    }
}
