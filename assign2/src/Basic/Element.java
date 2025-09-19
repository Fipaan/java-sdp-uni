public interface Element {
    String getType();
    void setType(String type);
    long getId();
    void setId(long id);
    Vector2 getPos();
    void setPos(Vector2 pos);
    Vector2 getSize();
    void setSize(Vector2 size);
    Color getBackgroundColor();
    void setBackgroundColor(Color backgroundColor);
    Color getForegroundColor();
    void setForegroundColor(Color foregroundColor);
    
    boolean isEnabled();
    void    setEnabled(boolean enabled);
    void    show();
    void    hide();

    void render();
    void update();
    void onClick();
}
