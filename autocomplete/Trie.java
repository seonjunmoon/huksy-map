package autocomplete;

import java.util.Collection;
import java.util.List;

/**
 * Tries are tree data structures that efficiently store strings. Each node
 * represents a letter and traversal of the tree starting at the root and
 * ending at a specially marked terminal node determines a word.
 */
public class Trie implements Autocomplete {
    // TODO: add fields as necessary

    /**
     * Creates a trie containing all of the given terms.
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public Trie(Collection<Term> terms) {
        // TODO: replace this with your code
        throw new UnsupportedOperationException("Not implemented yet: replace this with your code.");
    }

    @Override
    public List<Term> allMatches(String prefix) {
        // TODO: replace this with your code
        throw new UnsupportedOperationException("Not implemented yet: replace this with your code.");
    }
}
