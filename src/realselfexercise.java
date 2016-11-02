import java.util.ArrayList;
import java.util.HashMap;

public class realselfexercise {

    public static final boolean IGNORE_CASE = true; // Treat upper/lower case
                                                    // letters the same

    public static final String[] TEST_WORDS = { "hello", "llama", "",
            "aabbccddeeff", "apple", "Llama" }; // returns h, m, null, null, a,
                                                // [L/l]

    public static void main(String[] args) {
        for (String word : TEST_WORDS) {
            System.out.printf("%s : %s%n", word,
                    findFirstNonRepeatedLetter(word));
        }
    }

    /**
     * Finds the first non-repeated letter within a string.
     * 
     * @param input - the string to search for first non-repeated letter within
     * @return the first non-repeated letter
     */
    public static Character findFirstNonRepeatedLetter(String input) {

        if (input.isEmpty()) {
            return null;
        }

        HashMap<Character,Integer> seenLetters = new HashMap<Character,Integer>();
        ArrayList<Character> letters = new ArrayList<Character>();

        // Pass 1: Consume input O(N)
        for (int i = 0; i < input.length(); i++) {
            Character currChar = input.charAt(i); // O(1)

            if (IGNORE_CASE) {
                currChar = Character.toLowerCase(currChar);
            }

            if (!seenLetters.containsKey(currChar)) {
                seenLetters.put(currChar, i); // Add to seen letters, O(1)
                letters.add(currChar); // Add to letters O(1)
            } else {
                int lastIndex = seenLetters.put(currChar, i); // Kicks out old
                                                              // value O(1)
                letters.set(lastIndex, null); // clears duplicate letters O(1)
                letters.add(null); // null place holder to keep indices correct
                                   // O(1)
            }
        }

        // Pass 2: Find first non-null character in letters O(N)
        for (Character c : letters) {
            if (c != null) {
                return c;
            }
        }

        // no non-duplicated characters found
        return null;
    }

}
