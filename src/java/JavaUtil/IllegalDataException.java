package JavaUtil;


/**
 * Exception to indicate an error in Base64 encoded data
 */
public class IllegalDataException
      extends Exception
{
  private IllegalDataException() { }

  IllegalDataException(String s) {
    super(s);
  }

  IllegalDataException(String s, int offset) {
    this(s + " after offset " + offset + ".");
  }
}
