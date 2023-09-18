public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        char[] letters = word.toCharArray();
        LinkedListDeque<Character> wordLLD = new LinkedListDeque<>();
        for (char letter: letters) {
            wordLLD.addLast(letter);
        }
        return wordLLD;
    }

    public boolean isPalindrome(String word) {
        LinkedListDeque<Character> wordLLD = (LinkedListDeque)wordToDeque(word);
        return isPalindromeHelper(wordLLD);
    }

    private boolean isPalindromeHelper(Deque<Character> wordD) {
        if (wordD.isEmpty() || wordD.size() == 1) {
            return true;
        }
        char front = wordD.removeFirst();
        char last = wordD.removeLast();
        if (front != last) {
            return false;
        }
        return isPalindromeHelper(wordD);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        LinkedListDeque<Character> wordD = (LinkedListDeque)wordToDeque(word);
        int halfSize = wordD.size() / 2;
        for (int i = 0; i < halfSize; i++) {
            if (!cc.equalChars(wordD.removeFirst(), wordD.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
