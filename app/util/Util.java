package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class Util {

	public static String encodeString(String msg) throws Exception {
		String resp = null;
			if(msg != null && !msg.isEmpty()) {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] hash = md.digest(msg.getBytes(StandardCharsets.UTF_8));
				resp = DatatypeConverter.printHexBinary(hash);
			}
		return resp;
	}
}
