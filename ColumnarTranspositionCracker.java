import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ColumnarTranspositionCracker {

    private static final String ENGLISH_FREQS = "etaoinshrdlcumwfgypbvkjxqz";
    private static Map<String, Double> BIGRAM_FREQS = loadBigramFrequencies();

    public static String crack(String ciphertext) {
        int keyLength = findKeyLength(ciphertext);
        String plaintext = decrypt(ciphertext, keyLength);
        System.out.println("Key Length: " + keyLength);
        return plaintext;
    }

    private static int findKeyLength(String ciphertext) {
        int bestKeyLength = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int keyLength = 2; keyLength <= Math.sqrt(ciphertext.length()); keyLength++) {
            if (ciphertext.length() % keyLength == 0) {
                String decrypted = decrypt(ciphertext, keyLength);
                double score = evaluateDecryptedText(decrypted);
                if (score > bestScore) {
                    bestScore = score;
                    bestKeyLength = keyLength;
                }
            }
        }
        return bestKeyLength;
    }

    private static String decrypt(String ciphertext, int keyLength) {
        // Implement decryption logic here
        // This is a simplified version; you may need to adjust based on your specific implementation
        StringBuilder plaintext = new StringBuilder();
        int rows = ciphertext.length() / keyLength;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < keyLength; j++) {
                int index = i + j * rows;
                if (index < ciphertext.length()) {
                    plaintext.append(ciphertext.charAt(index));
                }
            }
        }
        return plaintext.toString();
    }

    private static double evaluateDecryptedText(String text) {
        double score = 0;
        for (char c : text.toCharArray()) {
            if (ENGLISH_FREQS.indexOf(c) != -1) {
                score += 1 / (ENGLISH_FREQS.indexOf(c) + 1); // Higher frequency letters contribute more
            }
        }
        // Add bigram analysis here if needed
        for (int i = 0; i < text.length() - 1; i++) {
            String bigram = text.substring(i, i + 2);
            if (BIGRAM_FREQS.containsKey(bigram)) {
                score += BIGRAM_FREQS.get(bigram);
            }
        }
        return score;
    }

    private static Map<String, Double> loadBigramFrequencies() {
        Map<String, Double> bigramFrequencies = new HashMap<>();
        try {
            Scanner scan = new Scanner(new File("bigrams.txt"));
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String bigram = line.substring(0, 2);
                String freq = line.substring(2).strip();
                Double frequency = Double.parseDouble(freq);
                bigramFrequencies.put(bigram, frequency);
            }
            scan.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            throw new RuntimeException(ioEx);
        }
        return bigramFrequencies;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ciphertext:");
        String ciphertext = scanner.nextLine();
        scanner.close();

        String plaintext = crack(ciphertext);
        System.out.println("Plaintext: " + plaintext);
    }
}
