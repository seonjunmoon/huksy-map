package autocomplete;

import java.util.List;

public interface Autocomplete {

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    List<Term> allMatches(String prefix);
}
