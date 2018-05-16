package cn.com.ruijie.utils;

public class CipherHelper {

    private static final String AESKey = "40188a09c06ae535dbd120aeb2fc8cfdf8f0c156";
    private static final String AESIV = "c7db6e30edec5c3724e3bbb1e3215ec713bd4db6";
    private static final ThreadLocal<AESCipher[]> ciphers = new ThreadLocal<AESCipher[]>();

    public static AESCipher getCipher(String name) {
        int h = 0;

        for (int i = 0; i < name.length(); ++i) {
            h = 31 * h + name.charAt(i);
        }

        AESCipher[] cipher = ciphers.get();

        if (cipher == null) {
            cipher = new AESCipher[2];
            cipher[0] = new AESCipher(AESKey, AESIV);
            cipher[1] = new AESCipher(AESIV, AESKey);
            ciphers.set(cipher);
        }

        return cipher[Math.abs(h) % 2];
    }

    public static void main(String[] args) {
        String name = "xixi04";
        String mingwen = "654321";
        //String cryptedString = "hbB0DRBA5gqYIlLDDwIVrA";
        String cryptedString = CipherHelper.getCipher(name).encrypt(mingwen);
        System.out.println(cryptedString);
        String decryptedString = CipherHelper.getCipher(name).decrypt(cryptedString);
        System.out.println(decryptedString);
        //System.out.println(decryptedString.replace('\0' , ',').split(",")[1]);
    }
}
