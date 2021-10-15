package gimgut.util;

public class Triplet<U, T, K> {
    private U u;
    private T t;
    private K k;

    public Triplet(U u, T t, K k) {
        this.u = u;
        this.t = t;
        this.k = k;
    }

    public U getFirst() {
        return u;
    }

    public T getSecond() {
        return t;
    }

    public K getThird() {
        return k;
    }
}
