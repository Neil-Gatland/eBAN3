package JavaUtil;

import java.io.*;

/**
 * Encoder and Decoder for Base64 Encoding
 *
 * <P>The coder object can be obtained by calling the {@link #getCoder()} method.</P>
 * <P>For details on Base64 encoding see <A HREF="http://alternic.net/rfcs/rfc2000/rfc2045.txt">
 * RFC 2045</A></P>
 *
 * <P>Copyright (c) 2002  Danet Ltd</P>
 *
 * @author Werner Vollmer
 * @version 1.0
 */

public class Base64Coder {

  /** Translation table for encoding */
  private char[] BASE64_TRANSLATE = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',   'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',   'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',   'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
    'w', 'x', 'y', 'z', '0', '1', '2', '3',   '4', '5', '6', '7', '8', '9', '+', '/'
  };

  /** Translation table for decoding */
  private int[] BASE64_RETRANSLATE = new int[128];

  /** The coder object */
  private static Base64Coder coder = null;


  static {
    coder = new Base64Coder();
  }



  /**
   * Returns the coder object
   *
   * @return   the coder object for Base64 encoding and decoding
   */
  public static Base64Coder getCoder()
  {
    return coder;
  }


  /**
   * Construct the coder class.
   */
  private Base64Coder()
  {
    for (int i = 0; i < BASE64_RETRANSLATE.length; BASE64_RETRANSLATE[i++] = -1);
    for (int i = 0; i < BASE64_TRANSLATE.length; i++) {
      BASE64_RETRANSLATE[BASE64_TRANSLATE[i]] = i;
    }
  }


  /**
   * Encode a byte array.
   *
   * @param  bb  an array of bytes to encode
   * @param  multiLine  if <TT>true</TT>, data is splitted over multiple lines
   * @return   a <TT>String</TT> containing the Base64 encoded byte array
   */
  public String encode(byte[] bb, boolean multiLine)
  {
    return encode(bb, 0, bb.length, multiLine);
  }


  /**
   * Encode a byte array.
   *
   * @param  bb  an array of bytes to encode
   * @return   a <TT>String</TT> containing the Base64 encoded byte array
   */
  public String encode(byte[] bb)
  {
    return encode(bb, 0, bb.length, false);
  }


  /**
   * Encode a byte array subrange.
   *
   * @param  bb  an array of bytes to encode a subrange of
   * @param  offset  start offset for encoding
   * @param  length  number of bytes to encode
   * @return   a <TT>String</TT> containing the Base64 encoded byte array
   */
  public String encode(byte[] bb, int offset, int length)
  {
    return encode(bb, offset, length);
  }


  /**
   * Encode a byte array subrange.
   *
   * @param  bb  an array of bytes to encode a subrange of
   * @param  offset  start offset for encoding
   * @param  length  number of bytes to encode
   * @param  multiLine  if <TT>true</TT>, data is splitted over multiple lines
   * @return   a <TT>String</TT> containing the Base64 encoded byte array
   */
  public String encode(byte[] bb, int offset, int length, boolean multiLine)
  {
    StringBuffer sb = new StringBuffer();

    int blocksLength = (length / 3) * 3;
    for (int pos = 0; pos < blocksLength; pos += 3) {
      if (multiLine && pos % 57 == 0 && pos > 0) {
        sb.append("\r\n");
      }
      int x = ((bb[pos + offset + 0] << 16) & 0xFF0000)
            + ((bb[pos + offset + 1] <<  8) & 0x00FF00)
            + ((bb[pos + offset + 2] <<  0) & 0x0000FF);
      sb.append(BASE64_TRANSLATE[(x & 0xFC0000) >> 18]);
      sb.append(BASE64_TRANSLATE[(x & 0x03F000) >> 12]);
      sb.append(BASE64_TRANSLATE[(x & 0x000FC0) >> 6]);
      sb.append(BASE64_TRANSLATE[(x & 0x00003F)]);
    }

    switch (length - blocksLength) {
      case 1: {
          int x = ((bb[blocksLength + offset + 0] << 16) & 0xFF0000);
          sb.append(BASE64_TRANSLATE[(x & 0xFC0000) >> 18]);
          sb.append(BASE64_TRANSLATE[(x & 0x03F000) >> 12]);
          sb.append("==");
          break;
        }
      case 2: {
          int x = ((bb[blocksLength + offset + 0] << 16) & 0xFF0000)
                + ((bb[blocksLength + offset + 1] <<  8) & 0x00FF00);
          sb.append(BASE64_TRANSLATE[(x & 0xFC0000) >> 18]);
          sb.append(BASE64_TRANSLATE[(x & 0x03F000) >> 12]);
          sb.append(BASE64_TRANSLATE[(x & 0x000FC0) >> 6]);
          sb.append("=");
          break;
        }
      case 0:
    }
    if (multiLine) {
      sb.append("\r\n");
    }

    return sb.toString();
  }



  /**
   * Read <TT>byte</TT>s from an <TT>InputStream</TT>, convert them to Base64 format
   * and write this to a <TT>Writer</TT>
   *
   * <P>The input stream is read until it returns an <TT>EndOfFile</TT> (<TT>-1</TT>).
   *
   * @param  in   the <TT>InputStream</TT> to read from
   * @param  out  the <TT>Writer</TT> to write output to
   * @return  the number of bytes that have been read and encoded
   * @throws  IOException  if an input or output stream I/O error occures
   */
  public int encode(InputStream in, Writer out)
        throws IOException
  {
    return encode(in, out, false);
  }


  /**
   * Read <TT>byte</TT>s from an <TT>InputStream</TT>, convert them to Base64 format
   * and write this to a <TT>Writer</TT>
   *
   * <P>The input stream is read until it returns an <TT>EndOfFile</TT> (<TT>-1</TT>).
   *
   * @param  in   the <TT>InputStream</TT> to read from
   * @param  out  the <TT>Writer</TT> to write output to
   * @param  multiLine  if <TT>true</TT>, data is splitted over multiple lines
   * @return  the number of bytes that have been read and encoded
   * @throws  IOException  if an input or output stream I/O error occures
   */
  public int encode(InputStream in, Writer out, boolean multiLine)
        throws IOException
  {
    int numBytes = 0;
    byte[] buf = new byte[3];
    int bufBytes = 0;
    BufferedInputStream inp = in instanceof BufferedInputStream
                              ? (BufferedInputStream)in : new BufferedInputStream(in);
    BufferedWriter outp = out instanceof BufferedWriter
                          ? (BufferedWriter)out : new BufferedWriter(out);

    while (true) {
      bufBytes = 0;

      if (multiLine && numBytes % 57 == 0 && numBytes > 0) {
        outp.write("\r\n");
      }

      if ((buf[0] = (byte)inp.read()) == -1) {
        break;
      } else {
        if ((buf[1] = (byte)inp.read()) == -1) {
          int x = (buf[0] << 4);
          outp.write(BASE64_TRANSLATE[(x & 0xFC0) >> 6]);
          outp.write(BASE64_TRANSLATE[x & 0x3F]);
          outp.write("==");
          numBytes++;
          break;
        } else {
          if ((buf[2] = (byte)inp.read()) == -1) {
            int x = (buf[0] << 10) + (buf[1] << 2);
            outp.write(BASE64_TRANSLATE[(x & 0x3F000) >> 12]);
            outp.write(BASE64_TRANSLATE[(x & 0xFC0) >> 6]);
            outp.write(BASE64_TRANSLATE[x & 0x3F]);
            outp.write("=");
            numBytes += 2;
            break;
          } else {
            int x = (buf[0] << 16) + (buf[1] << 8) + buf[2];
            outp.write(BASE64_TRANSLATE[(x & 0xFC0000) >> 18]);
            outp.write(BASE64_TRANSLATE[(x & 0x3F000) >> 12]);
            outp.write(BASE64_TRANSLATE[(x & 0xFC0) >> 6]);
            outp.write(BASE64_TRANSLATE[x & 0x3F]);
            numBytes += 3;
          }
        }
      }
    }

    if  (multiLine) {
      outp.write("\r\n");
    }
    outp.flush();

    return numBytes;
  }



  /**
   * Retranslate a single character.
   *
   * @param  c  the character to retranlate
   * @return  the retranslated character (in the range of 0 to 63)
   * @throws IllegalCharacterException   if the character is not a valid Base64 encoding character
   */
  private final int retrans(int c)
        throws IllegalCharacterException
  {
    int r = 0;
    if (c < 32 || c > 127 || (r = BASE64_RETRANSLATE[c]) == -1) {
      throw new IllegalCharacterException("Illegal Character #" + c);
    }
    return r;
  }


  /**
   * Decode a <TT>String</TT> containing Base64 encoded data.
   *
   * @param  s  a <TT>String</TT> containing nothing but Base64 encoded data
   * @return  a <TT>byte</TT> array containing the decoded data
   * @throws IllegalDataException   if the input data does not contain valid Base64 encoded data
   */
  public byte[] decode(String s)
        throws IllegalDataException
  {
    ByteBuffer bb = new ByteBuffer((s.length() * 3) / 4);
    int pos = 0;
    try {
      char[] enc = s.toCharArray();
      while (pos < enc.length && Character.isWhitespace(enc[pos])) {
        pos++;
      }
      while (pos < enc.length) {
        if (enc[pos + 3] == '=') {
          if (enc[pos + 2] == '=') {
            int x = (retrans(enc[pos + 0]) << 6) + retrans(enc[pos + 1]);
            bb.append((byte)((x >> 4) & 0xFF));
          } else {
            int x = (retrans(enc[pos + 0]) << 12) + (retrans(enc[pos + 1]) << 6) + retrans(enc[pos + 2]);
            bb.append((byte)((x >> 10) & 0xFF));
            bb.append((byte)((x >> 2) & 0xFF));
          }
        } else {
          int x = (retrans(enc[pos + 0]) << 18) + (retrans(enc[pos + 1]) << 12)
                  + (retrans(enc[pos + 2]) << 6) + retrans(enc[pos + 3]);
          bb.append((byte)((x >> 16) & 0xFF));
          bb.append((byte)((x >> 8) & 0xFF));
          bb.append((byte)(x & 0xFF));
        }
        pos += 4;
        while (pos < enc.length && Character.isWhitespace(enc[pos])) {
          pos++;
        }
      }
    } catch (IllegalCharacterException ice) {
      throw new IllegalDataException(ice.getMessage(), pos);
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      throw new IllegalDataException("Unexpected end of data", pos);
    }
    return bb.toByteArray();
  }




//  /** Test program */
//  public static void main(String[] args) {
//    String[] testData = new String[] { "ADMIN"
//        , "1", "12", "123", "1234", "12345", "123456", "1234567"
//        , "abcdefghijklmnop"
//        , "fgnfkjhgskfhdalkfhkjlahfsjklahf euifhiafh ofdiohfkslajf jkfshakjlsfh lkjhkjlsakjsladhfk sa",
//        "sfsal;fkjl;sasdfjghdfghkl'pokt03065ujjpju0pwsftesafsgjdkl;jkl;asfj;slajfl;jljgdgsgfdg"
//      + "lskdfjl;dfsgfdgdsgksjfskfjs;lj;lrwrwriopupsafsaf;jl;ertet';l;kl;'l;;;;;;;;;;;;;safsfsa"
//      + "lskdfjl;dfsgfdgdsgksjfskfjs;lj;lrwrwriopupsafsaf;jl;ertet';l;kl;'l;;;;;;;;;;;;;safsfsa"
//      + "sfsal;fkjl;sasdfjghdfghkl'pokt03065ujjpju0pwsftesafsgjdkl;jkl;asfj;slajfl;jljgdgsgfdg"
//    };
//    Base64Coder coder = getCoder();
//    for (int i = 0; i < testData.length; i++) {
//      String s = testData[i];
//      System.out.print("Test String '" + s + "':   ");
//      try {
//        long startTime = System.currentTimeMillis();
//        byte[] sBytes = s.getBytes("UTF8");
//        String enc = coder.encode(sBytes, false);
//        byte[] dBytes = coder.decode(enc);
//        String dec = new String(dBytes, "UTF8");
//        String grox = new String(s.getBytes("UTF8"), "UTF8");
//        long endTime = System.currentTimeMillis();
//        System.out.print((s.equals(dec) ? "" : "NO ") + "MATCH   ");
//        System.out.print("Decoded length: " + s.length() + " characters    ");
//        System.out.print("Encoded length: " + enc.length() + " characters    ");
//        System.out.println("Coding time: " + (endTime - startTime) + " ms");
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      }
//    }
//
//  }

}