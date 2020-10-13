package deque.palindrome;

import java.util.Random;

public class DNABasePair implements CharacterComparator {
    private final Random random;

    public DNABasePair() {
        random = new Random();
    }

    public DNABasePair(long seed) {
        random = new Random(seed);
    }

    @Override
    public boolean equalChars(char x, char y) {
        return complement(x) == y;
    }

    /** Returns a palindrome of the given length using the instance's random
     *  number generator.
     */
    public String randomPalindrome(int length) {
        char[] chars = new char[length];
        if (length == 0) {
            return "";
        } else if (length == 1) {
            chars[0] = base(random.nextInt(4));
        } else {
            int middle = length / 2;
            if (length % 2 != 0) {
                middle = (length / 2) + 1;
            }
            for (int i = 0; i < middle; i++) {
                chars[i] = base(random.nextInt(4));
            }
            for (int i = length - 1; middle <= i; i--) {
                char base = chars[length - 1 - i];
                chars[i] = complement(base);
            }
        }
        return new String(chars);
    }

    /** Returns a non-palindrome of the given length by generating a palindrome
     *  and then randomly substituting base pairs.
     */
    public String randomNearPalindrome(int length, int numSubstitutions) {
        char[] palindrome = randomPalindrome(length).toCharArray();
        while (numSubstitutions > 0) {
            int index = random.nextInt(length);
            palindrome[index] = complement(palindrome[index]);
            numSubstitutions -= 1;
        }
        return new String(palindrome);
    }

    private char base(int index) {
        if (index == 0) {
            return 'A';
        } else if (index == 1) {
            return 'T';
        } else if (index == 2) {
            return 'C';
        } else if (index == 3) {
            return 'G';
        } else {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    private char complement(char base) {
        if (base == 'A') {
            return 'T';
        } else if (base == 'T') {
            return 'A';
        } else if (base == 'C') {
            return 'G';
        } else if (base == 'G') {
            return 'C';
        } else {
            throw new IllegalArgumentException("Invalid base: " + base);
        }
    }
}

