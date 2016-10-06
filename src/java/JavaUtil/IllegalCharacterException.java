package JavaUtil;


/**
 * Exception to indicate illegal characters in Base64 encoded data
 */
class IllegalCharacterException
      extends Exception
{
  IllegalCharacterException(String s) {
    super(s);
  }
}
