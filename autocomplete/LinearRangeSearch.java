package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LinearRangeSearch implements Autocomplete {
    private final List<Term> terms;

    /**
     * Validates and stores the given terms.
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public LinearRangeSearch(Collection<Term> terms) {
        if (terms == null) {
            throw new IllegalArgumentException("Collection is null or collection contains null");
        }
        for (Term t : terms) {
            if (t == null) {
                throw new IllegalArgumentException("Collection is null or collection contains null");
            }
        }
        this.terms = new ArrayList<>(terms);
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
        ArrayList<Term> list = new ArrayList<>();
        for (Term t : terms) {
            if (t.queryPrefix(prefix.length()).compareTo(prefix) == 0) {
                list.add(t);
            }
        }
        Collections.sort(list, Term.byReverseWeightOrder());
        return list;
    }
}
