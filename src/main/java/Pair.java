import java.util.Objects;

public class Pair {
    private int key;
    private int value;

    public Pair(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return getKey() == pair.getKey();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return getKey() == pair.getKey() && getValue() == pair.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }
}
