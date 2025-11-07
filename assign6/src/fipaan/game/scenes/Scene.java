package fipaan.game.scenes;

import fipaan.com.has.*;

public abstract class Scene implements HasLocation<Scene> {
    public String name;
    public Scene(String name) {  this.name = name; }

    protected Context context;
    public Scene applyContext(Context ctx) { context = ctx; return this; }

    public abstract Scene onClick();
    public abstract Scene onResize();
    public abstract Scene beginDrawing();
    public abstract Scene endDrawing();

    public int getX() { return context.getX(); }
    public int getY() { return context.getY(); }
    public Scene setX(int x) {
        if (x < 1) x = 1;
        else if (x + 1>= context.getWidth()) x = context.getWidth() - 2;
        context.setX(x);
        return this;
    }
    public Scene setY(int y) {
        if (y < 1) y = 1;
        else if (y + 1 >= context.getHeight()) y = context.getHeight() - 2;
        context.setY(y);
        return this;
    }
}
