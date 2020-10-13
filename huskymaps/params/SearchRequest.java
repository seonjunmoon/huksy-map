package huskymaps.params;

import java.util.Objects;

/** Represents a search request received from the browser. */
public class SearchRequest {

    /** The search query. */
    public final String term;
    /** Whether this search requires locations or just a list of matches. */
    public final boolean full;

    public SearchRequest(String term, boolean full) {
        this.term = term;
        this.full = full;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchRequest that = (SearchRequest) o;
        return full == that.full &&
                Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, full);
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "term='" + term + '\'' +
                ", full=" + full +
                '}';
    }
}
