package com.rmd.wms.app.util;

import yao.util.encrypter.Base64;
import yao.util.md5.MD5Util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {

    private final SecretKeySpec key;

    public AESCipher(String strKey) {
        try {
            this.key = new SecretKeySpec(MD5Util.getMD516(strKey).toLowerCase().getBytes("UTF-8"), "AES");
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private static final String IV_STRING = "8*kq)2A5$k/S9Fd5";

    public String encrypt(String content) throws Exception {

        byte[] byteContent = content.getBytes("UTF-8");

        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
//		byte[] enCodeFormat = key.getBytes();
//	    SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = IV_STRING.getBytes("UTF-8");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(byteContent);

        // 同样对加密后数据进行 base64 编码
        return Base64.encode(encryptedBytes);
    }

    public String decrypt(String content) throws Exception {

        // base64 解码
        byte[] encryptedBytes = Base64.decode(content);

//		byte[] enCodeFormat = key.getBytes();
//	    SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = IV_STRING.getBytes("UTF-8");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

        byte[] result = cipher.doFinal(encryptedBytes);

        return new String(result, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
//		String string = AESCipher.encrypt("IAmThePlainText", "16BytesLengthKey");
//		System.out.println(string);
//		System.out.println(AESCipher.decrypt(string, "16BytesLengthKey"));
//		AESCipher aes = new AESCipher(ConstantEncryption.APP_RANDOM);
//		String str = aes.encrypt("{\"account\": \"admin01\",\"password\": \"000000\",\"versionCode\": \"zh\"}");
//		System.out.println(str);
//		System.out.println(aes.decrypt("RNsMcC39IY3e4qYHDj6lBmX0L6RM73p+JVDpFBW/OtDIxThM6crBzGwXpcqRrhVx"));
    }
}
