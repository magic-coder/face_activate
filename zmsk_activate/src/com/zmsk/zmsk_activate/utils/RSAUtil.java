package com.zmsk.zmsk_activate.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import android.util.Base64;

/***
 * RSA������
 * 
 * @author warrior
 *
 */
public class RSAUtil {

	/**
	 * �����㷨RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * ǩ���㷨
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	/**
	 * ���ݱ��뷽ʽ
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * ��ȡ��Կ��key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * ��ȡ˽Կ��key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA���������Ĵ�С
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA���������Ĵ�С
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * Ĭ�Ϲ�Կ
	 */
	private static final String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF2PQbdYtmsifN4n+RoCYAxW73494vkhDcs9oXipdwRovmySRgQKtA8NAVzRIb/R3YAn1XnyizNV2/RBUkbrHLNIlZwBIpNLfpvqlmdqzpLkmVkL7JM3a0mayE4InLH6vjmpBDTYjIFy1MyTEhe77B3wH8zldeLRpITw55feQUMwIDAQAB";

	/**
	 * Ĭ��˽Կ
	 */
	private static final String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMXY9Bt1i2ayJ83if5GgJgDFbvfj3i+SENyz2heKl3BGi+bJJGBAq0Dw0BXNEhv9HdgCfVefKLM1Xb9EFSRuscs0iVnAEik0t+m+qWZ2rOkuSZWQvskzdrSZrITgicsfq+OakENNiMgXLUzJMSF7vsHfAfzOV14tGkhPDnl95BQzAgMBAAECgYEAjifJlaVKqVpe0UbqJEdupuU1S3X14jZQWVP84ydYknqN8SAbO/GbWwjsao2zclrXQ6reRsP8KW+x0Ujo7AmBQt4uSqRTdu5s9GpufFncEWjECPsZdikVgFwxnI4hoVUhnsPVw2mLjw7e8emeLl4tobTDaU4WX6XOZOJe1E0qTLkCQQDhoPUwmkLZIWNZEQ600JBWhnDjEyRheZ7B8XaB+tW9FRMH+QJRyteCbHy+/JUjFXAT7NW/h+UOtIe1TZnZ5XY1AkEA4HqrKqA5+5n9KSyZA+rjZK4DTu/TwZPevAKTKFmV9C9FfkWO2VAK9OoJ9n3w9TumOyfdW5Yt3C+Z1YeJVgKNxwJAKjDuCJOgEngy2rHc3STvCK8FJwEqWWvjqwKbFX0xQLVTJLeEnoTevc3JmfEjdjcJCUDNS45+37wUcGu9bEiDTQJAKg/SMtKDCtn3zddFdK52nU7d39SgYQ1MFv6EhHME3hRdeSOfeKi+5NVVuJIwrELZCwyVNawWO8PPl2smGK+x0QJAc7kvFWBKfyBeesyHgFoucZQDZBe5kl4EpOM94pI1buz9dIAjXQ27HZGlkDh96sTICkdIhWK8sTMfzIXjYq5+Bg==";

	/**
	 * <p>
	 * ��˽Կ����Ϣ��������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @return
	 * @throws Exception
	 */
	public static String sign(String data) throws Exception {
		return sign(data, PRIVATEKEY);
	}

	/**
	 * <p>
	 * ��˽Կ����Ϣ��������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String sign(String data, String privateKey) throws Exception {
		return sign(data.getBytes(CHARSET), privateKey);
	}

	/**
	 * <p>
	 * ��˽Կ����Ϣ��������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Base64.encodeToString(signature.sign(), Base64.DEFAULT);
	}

	/**
	 * <p>
	 * У������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @param sign
	 *            ����ǩ��
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign) throws Exception {
		return verify(data, sign, PUBLICKEY);
	}

	/**
	 * <p>
	 * У������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @param sign
	 *            ����ǩ��
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign, String publicKey) throws Exception {
		return verify(data.getBytes(CHARSET), sign, publicKey);
	}

	/**
	 * <p>
	 * У������ǩ��
	 * </p>
	 * 
	 * @param data
	 *            �Ѽ�������
	 * @param sign
	 *            ����ǩ��
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String sign, String publicKey) throws Exception {
		byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64.decode(sign, Base64.DEFAULT));
	}

	/**
	 * <p>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String data) throws Exception {
		return encryptByPrivateKey(data, PRIVATEKEY);
	}

	/**
	 * <p>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
		return encryptByPrivateKey(data.getBytes(CHARSET), privateKey);
	}

	/**
	 * <p>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// �����ݷֶμ���
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return new String(Base64.encode(encryptedData, Base64.DEFAULT));
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data) throws Exception {
		return encryptByPublicKey(data, PUBLICKEY);
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, String publicKey) throws Exception {
		return encryptByPublicKey(data.getBytes(CHARSET), publicKey);
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// �����ݼ���
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// �����ݷֶμ���
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return new String(Base64.encode(encryptedData, Base64.DEFAULT));
	}

	/**
	 * <P>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encryptedData) throws Exception {
		return decryptByPrivateKey(encryptedData, PRIVATEKEY);
	}

	/**
	 * <P>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String encryptedData, String privateKey) throws Exception {
		return decryptByPrivateKey(Base64.decode(encryptedData, Base64.DEFAULT), privateKey);
	}

	/**
	 * <P>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// �����ݷֶν���
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData, CHARSET);
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String encryptedData) throws Exception {
		return decryptByPublicKey(encryptedData, PUBLICKEY);
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String encryptedData, String publicKey) throws Exception {
		return decryptByPublicKey(Base64.decode(encryptedData, Base64.DEFAULT), publicKey);
	}

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// �����ݷֶν���
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData, CHARSET);
	}

	/**
	 * <p>
	 * ������Կ��(��Կ��˽Կ)
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * <p>
	 * ��ȡ˽Կ
	 * </p>
	 * 
	 * @param keyMap
	 *            ��Կ��
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
	}

	/**
	 * <p>
	 * ��ȡ��Կ
	 * </p>
	 * 
	 * @param keyMap
	 *            ��Կ��
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(Base64.encode(key.getEncoded(),Base64.DEFAULT));
	}

	public static void main(String[] args) throws Exception {
		String data = "�Ұ��й�";
		System.out.println("ԭ���֣�\r\n" + data);

		String encodedData = encryptByPrivateKey(data);
		System.out.println("˽Կ���ܺ�\r\n" + encodedData);

		String decodedData = decryptByPublicKey(encodedData);
		System.out.println("��Կ���ܺ�: \r\n" + decodedData);

		String encodedData1 = encryptByPublicKey(data);
		System.out.println("��Կ���ܺ�\r\n" + encodedData1);

		String decodedData1 = decryptByPrivateKey(encodedData1);
		System.out.println("˽Կ���ܺ�: \r\n" + decodedData1);

		String sign = sign(encodedData);
		System.out.println("˽Կǩ��:\r" + sign);

		boolean status = verify(encodedData, sign);
		System.out.println("��Կ��֤���:\r" + status);
	}

}
