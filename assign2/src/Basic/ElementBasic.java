public class ElementBasic {
    protected String type;
    protected long id;
    protected Vector2 pos, size;
    protected Color backgroundColor, foregroundColor;
    protected boolean enabled;

    ElementBasic() {}
    ElementBasic(Element element) {
        this.type            = element.getType();
        this.id              = element.getId();
        this.pos             = element.getPos();
        this.size            = element.getSize();
        this.backgroundColor = element.getBackgroundColor();
        this.foregroundColor = element.getForegroundColor();
        this.enabled         = element.isEnabled();
    }
    
    public String  getType()                                 { return type; }
    public void    setType(String type)                      { this.type = type; }
    public long    getId()                                   { return id; }
    public void    setId(long id)                            { this.id = id; }
    public Vector2 getPos()                                  { return pos; }
    public void    setPos(Vector2 pos)                       { this.pos = pos; }
    public Vector2 getSize()                                 { return size; }
    public void    setSize(Vector2 size)                     { this.size = size; }
    public Color   getBackgroundColor()                      { return backgroundColor; }
    public void    setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
    public Color   getForegroundColor()                      { return foregroundColor; }
    public void    setForegroundColor(Color foregroundColor) { this.foregroundColor = foregroundColor; }
    
    public boolean isEnabled()              { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void show()                      { enabled = true; }
    public void hide()                      { enabled = false; }

    @Override
    public String toString() {
        return String.format(
                "%s(%d) [\n" +
                "    pos: %s,\n" +
                "    size: %s,\n" +
                "    backgroundColor: %s,\n" +
                "    foregroundColor: %s,\n" +
                "    enabled: %s\n" +
                "]",
            type, id,
            pos.toString(), size.toString(),
            backgroundColor.toString(), foregroundColor.toString(),
            enabled ? "true" : "false"
        );
    }
}
