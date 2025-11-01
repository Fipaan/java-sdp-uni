package fipaan.com.printf;

import fipaan.com.printf.*;
import fipaan.com.utils.*;

public class FormattedObj {
    private FormattedObjType type;
    private Object obj;
    private FormattedObj(FormattedObjType type, Object obj) {
        this.type = type;
        this.obj  = obj;
    }
    public FormattedObj(Integer obj) { this(FormattedObjType.Integer, obj); }
    public FormattedObj(Double  obj) { this(FormattedObjType.Double,  obj); }
    public FormattedObj(String  obj) { this(FormattedObjType.String,  obj); }
    public Integer getInt() {
        FormattedObjType.Integer.expect(type);
        return TypeUtils.<Object, Integer>cast(obj);
    }
    public Double getDouble() {
        FormattedObjType.Double.expect(type);
        return TypeUtils.<Object, Double>cast(obj);
    }
    public String getString() {
        FormattedObjType.String.expect(type);
        return TypeUtils.<Object, String>cast(obj);
    }
}
