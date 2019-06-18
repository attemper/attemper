package com.github.attemper.sys.util;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordUtil {

    public static void main(String[] args) {
        System.out.println(encode("admin@sse", "attemper"));
    }

    public static String encode(String source, String salt) {
        ByteSource bytes = ByteSource.Util.bytes(salt);
        SimpleHash hash = new Sha256Hash(source, bytes, 1024);
        return hash.toBase64();
    }

}
