package fipaan.com.console.style;

import fipaan.com.errors.FError;
import fipaan.com.utils.TypeUtils;
import fipaan.com.generic.Pair;

public class ConsoleStyleAny {
    public ConsoleStyleAnyType type;
    private Object obj;
    
    private ConsoleStyleAny(ConsoleStyleAnyType type, Object obj) { this.type = type; this.obj = obj; }
    private ConsoleStyleAny(ConsoleStyleAny style) { this(style.type, style.obj); }
    public ConsoleStyleAny(ConsoleStyle    style) { this(ConsoleStyleAnyType.Style,    style); }
    public ConsoleStyleAny(ConsoleColor16  color) { this(ConsoleStyleAnyType.Color16,  color); }
    public ConsoleStyleAny(ConsoleColor256 color) { this(ConsoleStyleAnyType.Color256, color); }
    public ConsoleStyleAny(ConsoleColorRGB color) { this(ConsoleStyleAnyType.ColorRGB, color); }
    public ConsoleStyleAny(ConsoleStyleAny head, ConsoleStyleAny tail) { this(ConsoleStyleAnyType.Compound, new Pair<>(head, tail)); }
    public ConsoleStyleAny(Pair<ConsoleStyleAny, ConsoleStyleAny> pair) { this(ConsoleStyleAnyType.Compound, pair); }

    private Pair<ConsoleStyleAny, ConsoleStyleAny> prepareDecorator() {
        ConsoleStyleAny head = new ConsoleStyleAny(this);
        type = ConsoleStyleAnyType.Compound;
        Pair<ConsoleStyleAny, ConsoleStyleAny> pair = new Pair<>(head, null);
        obj = pair;
        return pair;
    }
    public ConsoleStyleAny add(ConsoleStyle style) {
        prepareDecorator().second = new ConsoleStyleAny(style);
        return this;
    }
    public ConsoleStyleAny add(ConsoleColor16 color) {
        prepareDecorator().second = new ConsoleStyleAny(color);
        return this;
    }
    public ConsoleStyleAny add(ConsoleColor256 color) {
        prepareDecorator().second = new ConsoleStyleAny(color);
        return this;
    }
    public ConsoleStyleAny add(ConsoleColorRGB color) {
        prepareDecorator().second = new ConsoleStyleAny(color);
        return this;
    }
    public ConsoleStyleAny add(ConsoleStyleAny head, ConsoleStyleAny tail) {
        prepareDecorator().second = new ConsoleStyleAny(head, tail);
        return this;
    }
    public ConsoleStyleAny add(Pair<ConsoleStyleAny, ConsoleStyleAny> pair) {
        prepareDecorator().second = new ConsoleStyleAny(pair);
        return this;
    }
    public ConsoleStyleAny add(ConsoleStyleAny style) {
        prepareDecorator().second = style;
        return this;
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
            case ConsoleStyleAnyType.Style:    return getStyle().toString();
            case ConsoleStyleAnyType.Color16:  return getColor16().toString();
            case ConsoleStyleAnyType.Color256: return getColor256().toString();
            case ConsoleStyleAnyType.ColorRGB: return getColorRGB().toString();
            case ConsoleStyleAnyType.Compound: return head().toString() + ";" + tail().toString();
            default: throw FError.New("Unknown type: %s", type.toString());
        }
    }
    @Override public String toString() {
        return String.format("fipaan.ConsoleStyleAny{value: %s}", valueString());
    }
}
