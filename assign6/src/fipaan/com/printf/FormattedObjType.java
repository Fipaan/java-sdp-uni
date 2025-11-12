package fipaan.com.printf;

import fipaan.com.errors.*;

public enum FormattedObjType {
    Integer,
    Float,
    String;

    public void expect(FormattedObjType val) {
        if (this != val) {
             throw FError.New("Expected %s, got %s", this.toString(), val.toString());
        }
    }
    @Override public String toString() {
        switch (this) {
            case FormattedObjType.Integer: return "Integer";
            case FormattedObjType.Float:   return "Float";
            case FormattedObjType.String:  return "String";
        }
        throw FError.UNREACHABLE("unknown type");
    }
}
