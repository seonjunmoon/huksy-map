package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BinaryRangeSearch implements Autocomplete {
    private final List<Term> terms;

    /**
     * Validates and stores the given terms.
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public BinaryRangeSearch(Collection<Term> terms) {

        if (terms == null) {
            throw new IllegalArgumentException("Collection is null or collection contains null");
        }
        for (Term t : terms) {
            if (t == null) {
                throw new IllegalArgumentException("Collection is null or collection contains null");
            }
        }
        this.terms = new ArrayList<>(terms);
        Collections.sort(this.terms);
    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public List<Term> allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix is null");
        }
        if (prefix == "") {
            List<Term> list = new ArrayList<>(terms);
            Collections.sort(list, Term.byReverseWeightOrder());
            return list;
        }
        int lo = 0;
        int hi = terms.size() - 1;
        int lo2 = 0;
        int hi2 = terms.size() - 1;
        while (lo < hi) { // invariant
            int mid = (lo + hi) / 2;
            if (terms.get(mid).queryPrefix(prefix.length()).compareTo(prefix) == 0) {
                hi = mid;
            } else if (terms.get(mid).queryPrefix(prefix.length()).compareTo(prefix) < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        while (lo2 < hi2) { // invariant
            int mid = (lo2 + hi2) / 2;
            if (terms.get(mid).queryPrefix(prefix.length()).compareTo(prefix) == 0) {
                lo2 = mid + 1;
            } else if (terms.get(mid).queryPrefix(prefix.length()).compareTo(prefix) < 0) {
                lo2 = mid + 1;
            } else {
                hi2 = mid;
            }
        }
        int beg = lo;
        int end = lo2;

        if (this.terms.get(beg).queryPrefix(prefix.length()).compareTo(prefix) != 0 || beg == terms.size()) {
            return new ArrayList<>();
        }

        ArrayList<Term> list = new ArrayList<>();
        for (int i = beg; i < end; i++) {
            list.add(terms.get(i));
        }
        Collections.sort(list, Term.byReverseWeightOrder());
        return list;
    }
}
