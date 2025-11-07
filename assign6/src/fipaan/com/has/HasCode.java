package fipaan.com.has;

import fipaan.com.is.IsReflector;

public interface HasCode<Self extends HasCode<Self>> extends IsReflector<Self> {
    int getCode();
    Self setCode(int code);
}

