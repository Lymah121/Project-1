import java.util.Scanner;

public class PolyAlphabeticCipher {

    private String alphabet;
    private String key;
    private char[][] betaMatrix;

    public PolyAlphabeticCipher(String alphabet, String key) {
        this.alphabet = alphabet;
        this.key = key;
        generateBetaMatrix(key);
    }

    public PolyAlphabeticCipher(String alphabet, char[][] betaMatrix) {
        this.alphabet = alphabet;
        this.betaMatrix = betaMatrix;
    }

    private void generateBetaMatrix(String key) {
        betaMatrix = new char[key.length()][alphabet.length()];
        for (int i = 0; i < key.length(); i++) {
            int shift = alphabet.indexOf(key.charAt(i));
            for (int j = 0; j < alphabet.length(); j++) {
                betaMatrix[i][j] = alphabet.charAt((j + shift) % alphabet.length());
            }
        }
    }

    public char[][] getBeta() {
        return betaMatrix;
    }

    public String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        int keyIndex = 0;
        for (char c : plaintext.toCharArray()) {
            if (alphabet.indexOf(c) == -1) {
                ciphertext.append(c);
                continue;
            }
            int col = alphabet.indexOf(c);
            char encryptedChar = betaMatrix[keyIndex % betaMatrix.length][col];
            ciphertext.append(encryptedChar);
            keyIndex++;
        }
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        int keyIndex = 0;
        for (char c : ciphertext.toCharArray()) {
            if (alphabet.indexOf(c) == -1) {
                plaintext.append(c);
                continue;
            }
            int row = keyIndex % betaMatrix.length;
            int col = -1;
            for (int i = 0; i < betaMatrix[row].length; i++) {
                if (betaMatrix[row][i] == c) {
                    col = i;
                    break;
                }
            }
            char decryptedChar = alphabet.charAt(col);
            plaintext.append(decryptedChar);
            keyIndex++;
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        System.out.println("Enter the key: ");
        String key = scanner.nextLine();
        PolyAlphabeticCipher cipher = new PolyAlphabeticCipher(alphabet, key);

        System.out.println("Enter the plaintext: ");
        String plaintext = scanner.nextLine();

        String ciphertext = cipher.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        String decrypted = cipher.decrypt(ciphertext);
        System.out.println("Decrypted: " + decrypted);

        char[][] betaMatrix = cipher.getBeta();
        System.out.println("Beta Matrix:");
        for (char[] row : betaMatrix) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
}
