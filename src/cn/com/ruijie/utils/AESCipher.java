package cn.com.ruijie.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 * AES加解密算法
 * 
 * @note 若要使用192、256位密匙 需要到Sun下载jce_policy-6.zip 并解压安装</p>
 * @author arix04
 * 
 */

public class AESCipher {
	private static Logger Log = LoggerFactory.getLogger(AESCipher.class);

	private final static int KEY_SIZE = 128; // 128, 192, 256, 密钥长度
	private final static String ALGORITHM = "AES/CBC/PKCS5Padding";

	private Cipher encryptCipher;
	private Cipher decryptCipher;

	public AESCipher(String key, String iv) {
		key = key.substring(0, KEY_SIZE / 8);
		iv = iv.substring(0, KEY_SIZE / 8);

		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

		try {
			encryptCipher = Cipher.getInstance(ALGORITHM);
			encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			decryptCipher = Cipher.getInstance(ALGORITHM);
			decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String source) {
		try {
			byte[] encrypted = encryptCipher.doFinal(source.getBytes());
			String result = Base64.encodeBytes(encrypted, Base64.DONT_BREAK_LINES | Base64.URL_SAFE);
			int len = result.length();
			if (result.charAt(len - 2) == '=') {
				result = result.substring(0, len - 2);
			} else if (result.charAt(len - 1) == '=') {
				result = result.substring(0, len - 1);
			}
			return result;
		} catch (Exception e) {
			Log.error("加密数据失败, " + e);
			return null;
		}
	}

	// 解密
	public String decrypt(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		try {
			int len = string.length() % 3;

			if (len == 1) {
				string += "==";
			} else if (len == 2) {
				string += "=";
			}

			byte[] encrypted = Base64.decode(string, Base64.URL_SAFE);
			byte[] result = decryptCipher.doFinal(encrypted);

			return new String(result);
		} catch (Exception e) {
			Log.error("解密数据失败, " + e);
			return null;
		}
	}

	public static void test(AESCipher cipher, String s) {
		// 加密
		String enString = cipher.encrypt(s);
		System.out.println("加密后的字串是：" + enString);

		// 解密
		String DeString = cipher.decrypt(enString);
                System.out.println("解密后的字串是：" + DeString);
		//System.out.println("解密后的字串是：" + DeString.replace('\0', ',').split(",")[1]);
	}

	public static void main(String[] args) throws Exception {

		final String passwordKey = "40188a09c06ae535dbd120aeb2fc8cfdf8f0c156";
		final String passwordIV = "c7db6e30edec5c3724e3bbb1e3215ec713bd4db6";

		AESCipher cipher = new AESCipher(passwordKey, passwordIV);

		test(cipher, "luye"+"\0"+"123123yp");
		test(cipher, "这就是密码.");
		test(cipher, "hi, 这就是密码.");

		String s = "a" + "\0" + "b";
		System.out.println(s.length());
		System.out.println(s);
	}
}
