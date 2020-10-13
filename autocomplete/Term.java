package autocomplete;

import java.util.Comparator;

public interface Term extends Comparable<Term> {
    /** Compares the two terms in lexicographic order by query. */
    default int compareTo(Term that) {
        return this.query().compareTo(that.query());
    }

    /** Compares to another term, in descending order by weight. */
    default int compareToByReverseWeightOrder(Term that) {
        return Long.compare(that.weight(), this.weight());
    }

    /**
     * Compares to another term in lexicographic order, but using only the first r characters
     * of each query. If r is greater than the length of any term's query, compares using the
     * term's full query.
     */
    default int compareToByPrefixOrder(Term that, int r) {
        if (r < 0) {
            throw new IllegalArgumentException("Number of prefix characters cannot be negative: " + r);
        }
        return this.queryPrefix(r).compareTo(that.queryPrefix(r));
    }

    /** Returns this term's query. */
    String query();

    /**
     * Returns the first r characters of this query.
     * If r is greater than the length of the query, returns the entire query.
     * @throws IllegalArgumentException if r < 0
     */
    String queryPrefix(int r);

    /** Returns this term's weight. */
    long weight();

    /** Returns a Comparator that orders terms in reverse weight order. */
    static Comparator<Term> byReverseWeightOrder() {
        return Term::compareToByReverseWeightOrder;
    }

    /** Returns a Comparator  that orders terms by prefix order. */
    static Comparator<Term> byPrefixOrder(int r) {
        return (t1, t2) -> t1.compareToByPrefixOrder(t2, r);
    }
}
