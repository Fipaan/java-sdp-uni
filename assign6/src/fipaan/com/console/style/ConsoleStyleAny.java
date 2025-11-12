package fipaan.com.console.style;

import fipaan.com.errors.FError;
import fipaan.com.utils.TypeUtils;
import fipaan.com.generic.Pair;

public class ConsoleStyleAny {
    public static final ConsoleStyleAny RESET_COLORS = new ConsoleStyleAny()
            .add(ConsoleColor16.ForeDefault)
            .add(ConsoleColor16.BackDefault);
    public ConsoleStyleAnyType type;
    private Object obj;

    public Object getRaw() { return obj; }

    private ConsoleStyleAny(ConsoleStyleAnyType type, Object obj) { this.type = type; this.obj = obj; }
    private ConsoleStyleAny(ConsoleStyleAny style) { this(style.type, style.obj); }
    public ConsoleStyleAny(ConsoleStyle    style) { this(ConsoleStyleAnyType.Style,    style); }
    public ConsoleStyleAny(ConsoleColor16  color) { this(ConsoleStyleAnyType.Color16,  color); }
    public ConsoleStyleAny(ConsoleColor256 color) { this(ConsoleStyleAnyType.Color256, color); }
    public ConsoleStyleAny(ConsoleColorRGB color) { this(ConsoleStyleAnyType.ColorRGB, color); }
    public ConsoleStyleAny(ConsoleStyleAny head, ConsoleStyleAny tail) { this(ConsoleStyleAnyType.Compound, new Pair<>(head, tail)); }
    public ConsoleStyleAny(Pair<ConsoleStyleAny, ConsoleStyleAny> pair) { this(ConsoleStyleAnyType.Compound, pair); }
    public ConsoleStyleAny() { this((ConsoleStyleAnyType) null, null); }

    public ConsoleStyleAny add(ConsoleStyleAny style) {
        if (obj != null) {
            ConsoleStyleAny head = new ConsoleStyleAny(this);
            type = ConsoleStyleAnyType.Compound;
            Pair<ConsoleStyleAny, ConsoleStyleAny> pair = new Pair<>(head, null);
            obj = pair;
            pair.second = style;
        } else {
            type = style.type;
            obj = style.getRaw();
        }
        return this;
    }
    public ConsoleStyleAny add(ConsoleStyle style) {
        return add(new ConsoleStyleAny(style));
    }
    public ConsoleStyleAny add(ConsoleColor16 color) {
        return add(new ConsoleStyleAny(color));
    }
    public ConsoleStyleAny add(ConsoleColor256 color) {
        return add(new ConsoleStyleAny(color));
    }
    public ConsoleStyleAny add(ConsoleColorRGB color) {
        return add(new ConsoleStyleAny(color));
    }
    public ConsoleStyleAny add(ConsoleStyleAny head, ConsoleStyleAny tail) {
        return add(new ConsoleStyleAny(head, tail));
    }
    public ConsoleStyleAny add(Pair<ConsoleStyleAny, ConsoleStyleAny> pair) {
        return add(new ConsoleStyleAny(pair));
    }

    public ConsoleStyle getStyle() {
        ConsoleStyleAnyType.Style.expect(type);
        return TypeUtils.<Object, ConsoleStyle>cast(obj);
    }
    public ConsoleColor16 getColor16() {
        ConsoleStyleAnyType.Color16.expect(type);
        return TypeUtils.<Object, ConsoleColor16>cast(obj);
    }
    public ConsoleColor256 getColor256() {
        ConsoleStyleAnyType.Color256.expect(type);
        return TypeUtils.<Object, ConsoleColor256>cast(obj);
    }
    public ConsoleColorRGB getColorRGB() {
        ConsoleStyleAnyType.ColorRGB.expect(type);
        return TypeUtils.<Object, ConsoleColorRGB>cast(obj);
    }
    public ConsoleStyleAny head() {
        ConsoleStyleAnyType.Compound.expect(type);
        return TypeUtils.<Object, Pair<ConsoleStyleAny, ConsoleStyleAny>>cast(obj).first;
    }
    public ConsoleStyleAny tail() {
        ConsoleStyleAnyType.Compound.expect(type);
        return TypeUtils.<Object, Pair<ConsoleStyleAny, ConsoleStyleAny>>cast(obj).second;
    }
    public String valueString() {
        switch (type) {
            case ConsoleStyleAnyType.Style:    return getStyle().valueString();
            case ConsoleStyleAnyType.Color16:  return getColor16().valueString();
            case ConsoleStyleAnyType.Color256: return getColor256().valueString();
            case ConsoleStyleAnyType.ColorRGB: return getColorRGB().valueString();
            case ConsoleStyleAnyType.Compound: return head().valueString() + ";" + tail().valueString();
            default: throw FError.New("Unknown type: %s", type.toString());
        }
    }
    @Override public String toString() {
        return String.format("fipaan.ConsoleStyleAny{value: %s}", valueString());
    }
}
