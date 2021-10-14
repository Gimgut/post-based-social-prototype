package gimgut.util;

public class Pair<U, T> {
    private U u;
    private T t;

    public Pair(U u, T t) {
        this.u = u;
        this.t = t;
    }

    public U getFirst() {
        return u;
    }

    public T getSecond() {
        return t;
    }
}
