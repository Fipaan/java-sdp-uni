package fipaan.com.is;

public interface IsReflector<Self extends IsReflector<Self>> {
    default Self self() { return (Self)this; }
}
