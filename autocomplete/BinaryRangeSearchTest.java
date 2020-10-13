package autocomplete;

import edu.princeton.cs.algs4.In;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryRangeSearchTest {

    private static Autocomplete linearAuto;
    private static Autocomplete binaryAuto;
    private static Collection<Term> terms;

    private static final String INPUT_FILENAME = "data/cities.txt";

    /**
     * Creates LinearRangeSearch and BinaryRangeSearch instances based on the data from cities.txt
     * so that they can easily be used in tests.
     */
    @Before
    public void setUp() {
        if (terms != null) {
            return;
        }

        In in = new In(INPUT_FILENAME);
        int n = in.readInt();
        terms = new ArrayList<>(n);
        for (int i = 0; i < n; i += 1) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms.add(new SimpleTerm(query, weight));
        }

        linearAuto = new LinearRangeSearch(terms);
        binaryAuto = new BinaryRangeSearch(terms);
    }

    @Test
    public void testSimpleExample() {
        Collection<Term> moreTerms = List.of(
            new SimpleTerm("hello", 0),
            new SimpleTerm("world", 0),
            new SimpleTerm("welcome", 0),
            new SimpleTerm("to", 0),
            new SimpleTerm("autocomplete", 0),
            new SimpleTerm("me", 0)
        );
        BinaryRangeSearch brs = new BinaryRangeSearch(moreTerms);
        List<Term> expected = List.of(new SimpleTerm("autocomplete", 0));
        assertEquals(expected, brs.allMatches("auto"));
        System.out.println(binaryAuto.allMatches("Orme"));
    }

    // Write more unit tests below.

}
