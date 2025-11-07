package fipaan.com.generic;

@FunctionalInterface
public interface InOutPosFunc<Arr> {
    void accept(Arr in, Arr out, int inX, int inY, int outX, int outY);
}
