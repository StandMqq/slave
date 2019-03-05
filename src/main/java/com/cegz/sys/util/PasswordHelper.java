package com.cegz.sys.util;

import java.security.SecureRandom;

public class PasswordHelper {

	public static final String ALGORITHM = "SHA-1";

	public static final int HASHITERATIONS = 2;

	private static final int SALT_SIZE = 22;

	private static SecureRandom random = new SecureRandom();

//    public static String generateSalt(){
//        byte[] salt = generateSalt(SALT_SIZE);
//        return encodeHex(salt);
//    }

//    public static String encryptPassword(Users user) {
//        String newPassword = new SimpleHash(
//                ALGORITHM,
//                user.getPassword(),
//                ByteSource.Util.bytes(user.getCredentialsSalt()),
//                HASHITERATIONS).toHex();
//        return  newPassword;
//    }

//    /**
//     * 生成指定为数的随机的Byte[]作为salt.
//     * @param numBytes
//     * @return
//     */
//    public static byte[] generateSalt(int numBytes) {
//        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
//
//        byte[] bytes = new byte[numBytes];
//        random.nextBytes(bytes);
//        return bytes;
//    }

//    public static String encodeHex(byte[] input) {
//        return new String(Hex.encodeHex(input));
//    }

}