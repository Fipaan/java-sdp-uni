public class Checkbox extends ElementBasic implements Element {
    // Custom fields
    private boolean checked;
    private Color checkedColor, uncheckedColor;

    // Custom get/set methods
    public boolean isChecked()                          { return this.checked; }
    public void setChecked(boolean checked)             { this.checked = checked; }
    public void toggleChecked()                         { this.checked = !this.checked; }
    public Color getCheckedColor()                      { return checkedColor; }
    public void setCheckedColor(Color checkedColor)     { this.checkedColor = checkedColor; }
    public Color getUncheckedColor()                    { return uncheckedColor; }
    public void setUncheckedColor(Color uncheckedColor) { this.uncheckedColor = uncheckedColor; }
    
    // Base methods
    public void render() {
        Engine.DrawRectangle(pos, size, checked ? checkedColor : uncheckedColor);
    }
    public void update() {}
    public void onClick() {
        toggleChecked();
    }
    @Override
    public String toString() {
        return super.toString() + String.format(" [\n" +
                    "    checked: %s,\n" +
                    "    checkedColor: %s,\n" +
                    "    uncheckedColor: %s\n" +
                    "]",
                    checked ? "true" : "false",
                    checkedColor.toString(),
                    uncheckedColor.toString()
        );
    }
}
