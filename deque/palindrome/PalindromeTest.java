package deque.palindrome;

import deque.Deque;
import org.junit.Test;

import static org.junit.Assert.*;

public class PalindromeTest {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("anna"));
        assertFalse(palindrome.isPalindrome("Anna"));
        assertTrue(palindrome.isPalindrome("bob"));
        assertFalse(palindrome.isPalindrome("cindy"));
        assertTrue(palindrome.isPalindrome("dnd"));
    }

    @Test
    public void testIsPalindrome2() {
        CharacterComparator offByOne = new OffByOne();

        assertTrue(palindrome.isPalindrome("boc", offByOne));
        assertFalse(palindrome.isPalindrome("Boc", offByOne));
        assertFalse(palindrome.isPalindrome("aoa", offByOne));
        assertTrue(palindrome.isPalindrome("1u2", offByOne));
    }
}
