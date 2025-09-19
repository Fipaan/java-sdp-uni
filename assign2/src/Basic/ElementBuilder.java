public class ElementBuilder<T extends Element> implements Builder<T> {
    T element;
    public ElementBuilder(T element) { this.element = element; }
    public ElementBuilder<T> setType(String type) {
        this.element.setType(type);
        return this;
    }
    public ElementBuilder<T> setId(long id) {
        this.element.setId(id);
        return this;
    }
    public ElementBuilder<T> setPos(Vector2 pos) {
        this.element.setPos(pos);
        return this;
    }
    public ElementBuilder<T> setSize(Vector2 size) {
        this.element.setSize(size);
        return this;
    }
    public ElementBuilder<T> setBackgroundColor(Color backgroundColor) {
        this.element.setBackgroundColor(backgroundColor);
        return this;
    }
    public ElementBuilder<T> setForegroundColor(Color foregroundColor) {
        this.element.setForegroundColor(foregroundColor);
        return this;
    }
    public ElementBuilder<T> setEnabled(boolean enabled) {
        this.element.setEnabled(enabled);
        return this;
    }
    public T build() {
        if (this.element.getType() == null) {
            throw new Error("type was not provided");
        }
        if (this.element.getPos() == null) {
            throw new Error("pos was not provided");
        }
        if (this.element.getSize() == null) {
            throw new Error("size was not provided");
        }
        if (this.element.getBackgroundColor() == null) {
            throw new Error("backgroundColor was not provided");
        }
        if (this.element.getForegroundColor() == null) {
            throw new Error("foregroundColor was not provided");
        }
        return this.element;
    }
}
