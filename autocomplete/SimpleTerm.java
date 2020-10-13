package autocomplete;

import java.util.Objects;

public class SimpleTerm implements Term {
    private final String query;
    private final long weight;

    public SimpleTerm(String query, long weight) {
        this.query = query;
        if (weight < 0) {
            throw new IllegalArgumentException("weight is negative");
        }
        this.weight = weight;
    }

    public String queryPrefix(int r) {
        if (r > query.length()) {
            return query;
        } else {
            char[] chars = new char[r];
            for (int i = 0; i < r; i++) {
                chars[i] = query.charAt(i);
            }
            String result = new String(chars);
            return result;
        }
    }

    public String query() {
        return query;
    }

    public long weight() {
        return weight;
    }

    @Override
    public String toString() {
        return "SimpleTerm{" +
                "query='" + query + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleTerm)) {
            return false;
        }
        SimpleTerm that = (SimpleTerm) o;
        return weight() == that.weight() &&
                Objects.equals(query(), that.query());
    }

    @Override
    public int hashCode() {
        return Objects.hash(query(), weight());
    }
}
