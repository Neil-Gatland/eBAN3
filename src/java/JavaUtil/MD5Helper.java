package JavaUtil;

import java.io.*;
import java.security.*;


/**
 * Helper Class for generation of MD5 hashes
 *
 * <P>Copyright (c) 2002  Danet Ltd</P>
 *
 * @author Werner Vollmer
 * @version 1.0
 */


public class MD5Helper {

  static MessageDigest md5 = null;

  static {
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException nsae) {
      throw new RuntimeException("Message digest algorithm 'MD5' is unknown.");
    }
  }


  /**
   * Create an MD5 hash for the provided input data.
   *
   * @param  bb  the input data
   * @return  an array of 16 <TT>byte</TT>s containing the MD5 hash
   */
  public static byte[] createMD5(byte[] bb)
  {
    byte[] hash = null;
    synchronized(md5) {
      md5.reset();
      md5.update(bb);
      hash = md5.digest();
    }
    return hash;
  }


  /**
   * Create an MD5 hash for the provided input data.
   *
   * @param  bb  the <TT>byte</TT> array containing the input data
   * @param  offset  the input data start offset
   * @param  length  the input data length
   * @return  an array of 16 <TT>byte</TT>s containing the MD5 hash
   */
  public static byte[] createMD5(byte[] bb, int offset, int length)
  {
    byte[] hash = null;
    synchronized(md5) {
      md5.reset();
      md5.update(bb, offset, length);
      hash = md5.digest();
    }
    return hash;
  }


  /**
   * Create a Base64-encoded MD5 hash for the provided input string.
   *
   * <P>The input string is UTF-8-encoded into a byte array and the MD5 hash is calculated
   *    from this.</P>
   *
   * @param  s  the input data string
   * @return  a string of characters containing the Base64-encoded MD5 hash
   */
  public static String createMD5(String s)
  {
    byte[] data = null;
    try {
      data = s.getBytes("UTF8");
    } catch(UnsupportedEncodingException uee) {
      throw new RuntimeException("Encoding 'UTF8' is not supported on this system!");
    }
    return Base64Coder.getCoder().encode(createMD5(data));
  }


  /**
   * Print the MD5 hash for each invocation parameter.
   */
  public static void main(String args[]) {
    for (int i = 0; i < args.length; i++) {
      System.out.println("'" + args[i] + "': '" + createMD5(args[i]) + "'");
    }
  }

}
