package JavaUtil;

import java.util.*;
import java.text.*;

/**
 * A simple class to store a date and time value with millisecond resolution.
 *
 * <P>This is a read-only implementation that does not allow changing the time value.</P>
 *
 * <P>Copyright (c) 2002  Danet Ltd</P>
 *
 * @author Werner Vollmer
 * @version 1.0
 */

public class DateTime extends Date {

  /** the format string for string representation of dates */
  public static String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

  /**
   * Formatter to parse <TT>DateTime</TT> objects from strings and to format <TT>DateTime</TT>
   * objects as strings.
   */
  private static DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

  /**
   * Cache field for string-formatted <TT>DateTime</TT> object.
   */
  private transient String dateTimeStr = null;



  /**
   * Create a new <TT>DateTime</TT> object representing the date and time given in milliseconds
   * since 01/01/1970 00:00:00 GMT.
   * @param  dateTime date and time in milliseconds since 01/01/1970 00:00:00 GMT
   */
  public DateTime(long dateTime)
  {
    super(dateTime);
  }


  /**
   * Create a new <TT>DateTime</TT> object representing the current system date and time.
   */
  public DateTime()
  {
    this(System.currentTimeMillis());
  }


  /**
   * Create a new <TT>DateTime</TT> object representing the date and time given in a
   * {@link Date} object.
   *
   * @param  date the {@link Date} object to take date and time from
   */
  public DateTime(Date date)
  {
    this(date.getTime());
  }


  /**
   * Create a new <TT>DateTime</TT> object representing the date and time given in the
   * parameter string.
   * @param  dateTime   the date/time string to parse in the format "yyyy-MM-dd hh:mm:ss"
   * @throws  ParseException  if the date/time string is not in the specified format
   */
  public DateTime(String dateTime)
        throws ParseException
  {
    this(formatter.parse(dateTime).getTime());
  }


  /**
   * Create a new <TT>DateTime</TT> object representing the date and time given in the
   * parameter string.
   * @param  dateTime   the date/time string to parse
   * @param  format     the  format to use
   * @throws  ParseException  if the date/time string is not in the specified format
   */
  public DateTime(String dateTime, String format)
        throws ParseException
  {
    this(new SimpleDateFormat(format).parse(dateTime).getTime());
  }


  /**
   * Create a new <TT>DateTime</TT> object representing the date and time given in the
   * parameter string.
   *
   * <P>The date/time string has to be in the locale's {@link DateFormat#SHORT} format.</P>
   *
   * @param  dateTime  the date/time string to parse in the format "yyyy-MM-dd hh:mm:ss"
   * @param  locale    the locale the date string format conforms to
   * @throws  ParseException  if the date/time string is not in the specified format
   */
  public DateTime(String dateTime, Locale locale)
        throws ParseException
  {
    this(DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                        DateFormat.SHORT, locale).parse(dateTime).getTime());
  }



  /**
   * This method has been overridden to prevent its use. It will always
   * throw an IllegalArgumentException.
   * @param  time  the time in milliseconds since 01/01/1970 00:00:00
   * @throws  IllegalArgumentException  always
   * @deprecated
   */
  public void setTime(long time)
  {
    throw new IllegalArgumentException("The method setTime(long) is not allowed "
                                     + "in the class ebill.util.DateTime.");
  }


  /**
   * Returns a string representation of the object.
   * @return   the date/time in the format "yyyy-MM-dd hh:mm:ss"
   */
  public String toString()
  {
    return formatter.format(this);
  }


  /**
   * Returns a string representation of the object.
   *
   * <P>The date/time is formatted with {@link DateFormat#SHORT}
   * @param  locale  the locale to format the date/time for
   * @return   the date/time in the format "yyyy-MM-dd hh:mm:ss"
   */
  public String toString(Locale locale)
  {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(this);
  }

}