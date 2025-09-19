public class Vector2 {
    float x, y;
    Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    static Vector2 Zero() { return new Vector2(0.0f, 0.0f); }
    static Vector2 One()  { return new Vector2(1.0f, 1.0f); }
    Vector2 add(float x, float y)  { this.x += x; this.y += y; return this; }
    Vector2 sub(float x, float y)  { this.x -= x; this.y -= y; return this; }
    Vector2 mult(float x, float y) { this.x *= x; this.y *= y; return this; }
    Vector2 scale(float k)         { this.x *= k; this.y *= k; return this; }
    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
