package JavaUtil;

import java.io.*;

/**
 * A buffer class to compose byte arrays.
 *
 * <P>Bytes or byte arrays can be appended while the length of the buffer is
 * extended automatically to take up the appended data.</P>
 *
 * <P>Copyright (c) 2002  Danet Ltd</P>
 *
 * @author Werner Vollmer
 * @version 1.0
 */

public class ByteBuffer {

  /** The default buffer length */
  public static final int DEFAULT_LENGTH = 1024;


  /** The byte array that takes up all data */
  private byte[] bytes = null;

  /** Number of bytes currently "in use" in <TT>bytes</TT> */
  private int size = 0;


  /**
   * Create a <TT>ByteBuffer</TT> with default length.
   */
  public ByteBuffer()
  {
    this(DEFAULT_LENGTH);
  }


  /**
   * Create a <TT>ByteBuffer</TT> with the given length.
   *
   * @param  length  the number of bytes the buffer can take up without extending
   */
  public ByteBuffer(int length)
  {
    init(length);
  }


  /**
   * Initialize the buffer.
   *
   * <P>The size, i.e. the number of bytes currently in the buffer, is set to <TT>0</TT> and
   * a new buffer is allocated if the given length exceeds the current length. </P>
   *
   * @param length   the minimum number of bytes the buffer can take up without extending
   */
  public void init(int length)
  {
    if (bytes == null || bytes.length < length) {
      bytes = new byte[length];
    }
    size = 0;
  }


  /**
   * Initialize the buffer.
   *
   * <P>The size, i.e. the number of bytes currently in the buffer, is set to <TT>0</TT>. </P>
   */
  public void init()
  {
    size = 0;
  }


  /**
   * Expand the buffer to at least the given size.
   *
   * <P>The buffer length is doubled until it is bigger or equal to the given minimum size. </P>
   *
   * @param length   the minimum number of bytes the buffer can take up without expanding
   */
  private void expand(int length)
  {
    byte[] oldBytes = bytes;
    int newSize = bytes.length;
    while(newSize < length) {
      newSize *= 2;
    }
    bytes = new byte[newSize];
    System.arraycopy(oldBytes, 0, bytes, 0, size);
  }


  /**
   * Return the buffer size.
   *
   * @return  the number of bytes currently in the buffer
   */
  public int getSize()
  {
    return size;
  }


  /**
   * Append a single <TT>byte</TT> to the buffer.
   *
   * @param  b  the byte to append
   */
  public void append(byte b)
  {
    if (bytes.length < size + 1) {
      expand(size + 1);
    }
    bytes[size++] = b;
  }


  /**
   * Append a <TT>byte</TT> array to the buffer.
   *
   * @param  bb   the <TT>byte</TT> array to append
   */
  public void append(byte[] bb)
  {
    if (bytes.length < size + bb.length) {
      expand(size + bb.length);
    }
    System.arraycopy(bb, 0, bytes, size, bb.length);
    size += bb.length;
  }



  /**
   * Append a <TT>byte</TT> array subrange to the buffer.
   *
   * @param  bb   the <TT>byte</TT> array to append a subrange from
   * @param  offset  the start offset in <TT>bb</TT>
   * @param  length  the number of bytes to append
   */
  public void append(byte[] bb, int offset, int length)
  {
    if (bytes.length < size + length) {
      expand(size + length);
    }
    System.arraycopy(bb, offset, bytes, size, length);
    size += length;
  }


  /**
   * Return the buffer contents.
   *
   * @return   a byte array representing the buffer contents
   */
  public byte[] toByteArray()
  {
    byte[] bb = new byte[size];
    System.arraycopy(bytes, 0, bb, 0, size);
    return bb;
  }


  /**
   * Return the buffer contents as a string.
   *
   * <P>This makes only sense with text data!</P>
   *
   * @param  encoding  the encoding used for <TT>byte</TT> to <TT>char</TT> conversion
   * @return  a <TT>String</TT> constructed from the buffer contents
   * @throws  UnsupportedEncodingException   if the given encoding does not exist
   */
  public String toString(String encoding)
        throws UnsupportedEncodingException
  {
    return new String(bytes, 0, size, encoding);
  }

}
