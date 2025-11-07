package fipaan.com.console.style;

import fipaan.com.errors.FThrow;

public enum ConsoleStyleAnyType {
    Style,
    Color16,
    Color256,
    ColorRGB,
    Compound;
    
    public void expect(ConsoleStyleAnyType val) {
        if (this != val) {
             FThrow.New("Expected %s, got %s", this.toString(), val.toString());
        }
    }
}
