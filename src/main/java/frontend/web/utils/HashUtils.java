/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package frontend.web.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Dolphin
 */
public class HashUtils {
  public static String hash(String password) {
    StringBuilder sb = new StringBuilder();
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA");
      byte[] bs;
      bs = messageDigest.digest(password.getBytes());
      for (int i = 0; i < bs.length; i++) {
        String hexVal = Integer.toHexString(0xFF & bs[i]);
        if (hexVal.length() == 1) {
          sb.append("0");
        }
        sb.append(hexVal);
      }
    } catch (NoSuchAlgorithmException ex) {
    }
    return sb.toString();
  }
}
