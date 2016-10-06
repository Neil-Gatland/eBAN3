package JavaUtil;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.*;

//import java.sql.Date;

public class StringUtil {

  //Private Data
  private String Message;
  private final int NOT_INT=-2147483648;
  private static final char[] alphaNum = {'A','B','C','D','E','F','G','H','I','J',
    'K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1',
    '2','3','4','5','6','7','8','9',' '};
  private static final char[] unfriendly = {'\'','"','{','}','^','|',',','[',
    ']','\n','\r','\t','~','¬'};

  //Private Methods

  //Public Methods
//  var pattern = /["^\{\}\[\]'<>\\|`¦¬&]/g;

  public StringUtil()
  {
  }
/****************************************************************************/
  public String before(String string)
  {
   if (string != null)
   {
     StringTokenizer st = new StringTokenizer(string);
     if (st.hasMoreTokens())
     {
         return st.nextToken();
     }
     else
     {
      return string;
     }
   }
   else
   {
     return "";
   }
  }
/****************************************************************************/
  public String after(String string)
  {
    String temp=string;
    int pos=0;
    if (string != null)
    {
      pos=temp.indexOf(" ");

      if (pos+1<temp.length())
      {
	temp=temp.substring(pos+1);
      }
      else
      {
	temp="";
      }
    }
    else
    {
      temp="";
    }
    return temp;
  }
/****************************************************************************/
  public String Extract(String string,int StringNumber)
  {
    String temp=string;
    int TokenNumber=0;
       if (string != null)
    {
      //add a space to the string to force at least one token
      StringTokenizer st = new StringTokenizer(string+" ");
      while ((st.hasMoreTokens()) && (TokenNumber < StringNumber))
      {
	temp=st.nextToken();
	TokenNumber++;
      }
      if (TokenNumber==StringNumber)
      {
        return temp;
      }
      else
      {
	return "";
      }
    }
    else
    {
      return "";
    }
  }
/*************************************************************************************/
  public String getAbbreviation(String string,int Length)
  {
    String Abbreviation="",NamePart="";

    if (string != null)
    {
      if (string.length()<=Length)
      {
        return string;
      }
      else
      {//add a space to the string to force at least one token
        StringTokenizer st = new StringTokenizer(string+" ");
          while ((st.hasMoreTokens()) && (Abbreviation.length() < Length))
          {
            NamePart=st.nextToken();
            Abbreviation+=NamePart.substring(0,1);
          }
          if (Abbreviation.length()<Length)
	  {
	    if (NamePart.length()>Length-Abbreviation.length())
	    {
	      Abbreviation+=NamePart.substring(1,1+Length-Abbreviation.length());
	    }
	    else if (NamePart.length()==Length-Abbreviation.length())
	    {
	      Abbreviation+=NamePart.substring(0,Length-Abbreviation.length());
	    }
	  }
	  return Abbreviation;
      }
    }
    else
    {
      return "";
    }
  }
/*************************************************************/
public String DateToString(Date date)
{
   SimpleDateFormat StringDate = new SimpleDateFormat("dd-MMM-yyyy");
   //String sDate="";
   try
   {
      return StringDate.format(date);
   }
   catch(java.lang.NullPointerException se){return null;}

}
/*************************************************************/
private int actualDecimalPlaces(double r)
{
  String s = String.valueOf(r);
  if (s.indexOf(".") == -1)
  {
    return 0;
  }
  else
  {
    String d = s.substring(s.indexOf(".") + 1);
    return d.length();
  }
}
/*************************************************************/
private double padWithZeros(double r, int dp)
{
  String s = String.valueOf(r);
  StringBuffer sb = new StringBuffer(s);
  int already = 0;
  if (s.indexOf(".") == -1)
  {
    sb.append(".");
  }
  else
  {
    String d = s.substring(s.indexOf(".") + 1);
    already = d.length();
  }
  for (int i=already; i<dp; i++)
  {
    sb.append("0");
  }
  return Double.parseDouble(sb.toString());
}
/*************************************************************/
public String reformatLongDuration(long duration)
{
  long hh = duration/3600;
  long rem = duration%3600;
  long mm = rem/60;
  long ss = rem%60;
  return (hh<10?"0":"") + hh + ":" + (mm<10?"0":"") + mm +  ":" +
    (ss<10?"0":"") + ss;
}
/*************************************************************/
public String padWithZeros(String s, int dp)
{
  StringBuffer sb = new StringBuffer(s);
  int already = 0;
  if (s.indexOf(".") == -1)
  {
    sb.append(".");
  }
  else
  {
    String d = s.substring(s.indexOf(".") + 1);
    already = d.length();
  }
  for (int i=already; i<dp; i++)
  {
    sb.append("0");
  }
  return sb.toString();
}
/*************************************************************/
private double removeTrailingZeroes(double r, int dp)
{
  String s = String.valueOf(r);
  if ((s.indexOf(".") == -1) || (s.charAt(s.length()-1) != '0'))
  {
    return r;
  }
  else
  {
    String d = s.substring(s.indexOf(".") + 1);
    int soFar = d.length();
    if (soFar == dp)
    {
      return r;
    }
    else if (soFar < dp)
    {
      return padWithZeros(r, dp);
    }
    else
    {
      while ((soFar > dp) && (s.charAt(s.length()-1) == '0'))
      {
        s = s.substring(0, s.length()-1);
      }
      return Double.parseDouble(s);
    }
  }
}
/*************************************************************/
public double roundUp(double r, int dp)
{
    /*int actual = actualDecimalPlaces(r);
    if (actual == dp)
    {
      return r;
    }
    else if (actual < dp)
    {
      return padWithZeros(r, dp);
    }
    else
    {
      double d = removeTrailingZeroes(r, dp);
      actual = actualDecimalPlaces(d);
      if (actual == dp)
      {
        return d;
      }
      else
      {*/
        BigDecimal bd = new BigDecimal(r);
        //BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(dp,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
      /*}
    }*/
}
/*************************************************************/
public String roundToString(double r, int dp)
{
  String s = String.valueOf(roundUp(r, dp));
  return padWithZeros(s, dp);
}
/*************************************************************/
public static String roundToStringSt(double r, int dp)
{
    BigDecimal bd = new BigDecimal(r);
    bd = bd.setScale(dp,BigDecimal.ROUND_HALF_UP);
    return String.valueOf(bd.doubleValue());
}

/*************************************************************/
public String roundToString(float r, int dp)
{
    BigDecimal bd = new BigDecimal(r);
    bd = bd.setScale(dp,BigDecimal.ROUND_HALF_UP);
    String value = String.valueOf(bd.floatValue());
    if (((value.length() - value.indexOf(".")) > (dp + 1)) &&
      (value.endsWith("0")))
    {
      value = value.substring(0, value.length() - 1);
    }
    return value;
}

/*************************************************************/
public String DateToString(Date date, String format)
{
   SimpleDateFormat StringDate = new SimpleDateFormat(format);
   //String sDate="";
   try
   {
      return StringDate.format(date);
   }
   catch(java.lang.NullPointerException se){return null;}

}
/***********************************************************************************/
  public String isNull(String oldString,String newString)
  {
    SimpleDateFormat now = new SimpleDateFormat("dd-MMM-yyyy");
    Date date = new Date();
    //DateFormat df = DateFormat.getDateInstance();
    date.getTime();
    String Today = now.format(date);

     if (oldString !=null)
     {
         return oldString ;
     }
     else
     {
      if (newString.compareToIgnoreCase("Today") == 0)
      {
	return  Today;
      }
      else if (newString.compareToIgnoreCase("FirstOfMonth") == 0)
      {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(cal.DATE, 1);
        Date first = cal.getTime();
        String fom = now.format(first);
	return  fom;
      }
      else
      {
        return newString;
      }
     }
  }
/***********************************************************************************/
  public String isNull2(String oldString,String newString)
  {

    String retValue = "";
     if (oldString !=null)
     {
       if (!oldString.equals(" "))
         retValue = oldString ;
     }
     return retValue ;
  }
/***********************************************************************************/
  public String hasValue(String oldString,String newString)
  {
     if ((oldString==null) || (oldString.compareTo("")==0))
     {
         return "";
     }
     else
     {
        return newString;
     }
  }
/***********************************************************************************/
  public String hasNoValue(String oldString,String newString)
  {
     if ((oldString==null) || (oldString.equals("")) || (isSpaces(oldString)))
     {
         return newString;
     }
     else
     {
        return oldString;
     }
  }
/***********************************************************************************/
  public boolean hasNoValue(String oldString)
  {
     if ((oldString==null) || (oldString.equals("")) || (isSpaces(oldString)))
     {
         return true;
     }
     else
     {
        return false;
     }
  }
  /*************************************************************/
  public String isBlank(String oldString,String newString)
  {
    if (isNull(oldString,"")=="")
    {
      return newString;
    }
    else
    {
      return oldString;
    }
  }
 /*************************************************************/
  public java.sql.Date toJDBCDate(String DateString)
  {
    java.sql.Date sqlDate = null;
    java.util.Date uDate;
    String JDBCDate;
    SimpleDateFormat sdfinput = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat sdfoutput = new SimpleDateFormat("yyyy-MM-dd");
    try {

      if (DateString.equalsIgnoreCase("Today"))
      {
        uDate=new Date();
      }
      else
      {
        uDate=sdfinput.parse(DateString);
      }
      JDBCDate=sdfoutput.format(uDate);
      //sdf.format()
    }
    catch(java.text.ParseException pe){
      Message=pe.getMessage();
      return null;}
    return sqlDate.valueOf(JDBCDate);
  }
 /*************************************************************/
  public java.sql.Date toJDBCDate(String DateString,String Format)
  {
    java.sql.Date sqlDate = null;
    java.util.Date uDate;
    String JDBCDate;
    SimpleDateFormat sdfinput = new SimpleDateFormat(Format);
    SimpleDateFormat sdfoutput = new SimpleDateFormat("yyyy-MM-dd");
    try {

      if (DateString.equalsIgnoreCase("Today"))
      {
        uDate=new Date();
      }
      else
      {
        uDate=sdfinput.parse(DateString);
      }
      JDBCDate=sdfoutput.format(uDate);
      //sdf.format()
    }
    catch(java.text.ParseException pe){
      Message=pe.getMessage();
      return null;}
    return sqlDate.valueOf(JDBCDate);
  }
 /*************************************************************/
  public java.util.Date toDate(String DateString,String Format)
  {
    java.util.Date uDate;
    SimpleDateFormat sdfinput = new SimpleDateFormat(Format);
    try {

      if (DateString.equalsIgnoreCase("Today"))
      {
        uDate=new Date();
      }
      else
      {
        uDate=sdfinput.parse(DateString);
      }
    }
    catch(java.text.ParseException pe){
      Message=pe.getMessage();
      return null;}
    return uDate;
  }
/*************************************************************/
  public String reformatDate(String DateString)
  {
    java.util.Date uDate;
    String ddmmyyyDate="";

    if (DateString != null)
    {
      SimpleDateFormat sdfinput = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat sdfoutput = new SimpleDateFormat("dd-MMM-yyyy");
      try {

        uDate=sdfinput.parse(DateString);
        ddmmyyyDate=sdfoutput.format(uDate);
      }
      catch(java.text.ParseException pe){Message=pe.getMessage();return null;}
    }
    return ddmmyyyDate;
  }
/*************************************************************************************/
  public String reformatDate(String DateString,String Format)
  {
    java.util.Date uDate;
    String ddmmyyyDate="";

    if ((DateString != null) && (DateString.compareTo("") !=0))
    {
      SimpleDateFormat sdfinput = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat sdfoutput = new SimpleDateFormat(Format);
      try {

        uDate=sdfinput.parse(DateString);
        ddmmyyyDate=sdfoutput.format(uDate);
      }
      catch(java.text.ParseException pe){Message=pe.getMessage();return null;}
    }
    return ddmmyyyDate;
  }
/*************************************************************************************/
  public String reformatDate(String DateString,String FromFormat, String ToFormat)
  {
    java.util.Date uDate;
    String newDate="";
    SimpleDateFormat now = new SimpleDateFormat(ToFormat);
    Date date = new Date();

    if (DateString.compareToIgnoreCase("now")==0)
    {
      date.getTime();
      return now.format(date);
    }
    if ((DateString != null) && (DateString.compareTo("") !=0))
    {
      SimpleDateFormat sdfinput = new SimpleDateFormat(FromFormat);
      SimpleDateFormat sdfoutput = new SimpleDateFormat(ToFormat);
      try {

        uDate=sdfinput.parse(DateString);
        newDate=sdfoutput.format(uDate);
      }
      catch(java.text.ParseException pe){Message=pe.getMessage();return null;}
    }
    return newDate;
  }
/*************************************************************************************/
  public String getMessage()
  {
    return Message;
  }
/*************************************************************************************/
  public float toFloat(String FloatString)
  {
    float newFloat;
    try
    {
      newFloat=Float.parseFloat(FloatString);
    }
    catch (NumberFormatException ne)
    {
      return -999999999;
    }
    return newFloat;
  }
 /*************************************************************************************/
 public int toInt(String intString)
  {
    int intl=0;
    if (intString.compareTo("") != 0)
    {
      try
      {
        intl=Integer.parseInt(intString);
      }
      catch (java.lang.NumberFormatException ne){return NOT_INT;}
    }
    return intl;
  }
/*************************************************************************************/
public Enumeration toEnumeration(String sArray[])
{
  int i;
  Vector V=new Vector();

  for (i=0;i<sArray.length;i++)
  {
    V.addElement(sArray[i]);
  }
  return V.elements();
}
/*************************************************************************************/
public String get_DB_Data(Enumeration Results,int Columns)
{
  if (Results.hasMoreElements())
  {
    return isNull((String)Results.nextElement(),"&nbsp");

  }
  else
  {
    return "&nbsp";
  }
}
/*************************************************************************************/
public Enumeration unpackTextArea(String TextArea)
{
  Vector v=new Vector();
  StringTokenizer st = new StringTokenizer(TextArea+"\n","\n");

  while (st.hasMoreTokens())
  {
    v.addElement(st.nextToken());
  }
  return v.elements();
}
/*************************************************************************************/
public Enumeration unpackTextArea(String TextArea, String tokenDelimiter)
{
  Vector v=new Vector();
  StringTokenizer st = new StringTokenizer(TextArea+tokenDelimiter,tokenDelimiter);

  while (st.hasMoreTokens())
  {
    v.addElement(st.nextToken());
  }
  return v.elements();
}
/*************************************************************************************/
public String packTextArea(String Lines[])
{
  Enumeration e=toEnumeration(Lines);
  //e=Lines.elements();
  StringBuffer TextArea=new StringBuffer("");

  if (e.hasMoreElements())
  {
    TextArea.append(e.nextElement());
  }

  while (e.hasMoreElements())
  {
    //TextArea.append("\n");
    TextArea.append(e.nextElement());
  }

  return TextArea.toString();
}
/*************************************************************/
public String packTextArea(String Lines[], String tokenDelimiter)
{
  Enumeration e=toEnumeration(Lines);
  //e=Lines.elements();
  StringBuffer TextArea=new StringBuffer("");

  if (e.hasMoreElements())
  {
    TextArea.append(e.nextElement());
  }

  while (e.hasMoreElements())
  {
    String line = (String)e.nextElement();
    if (line != null)
    {
      if (!line.equals(""))
      {
        TextArea.append(tokenDelimiter);
        TextArea.append(line);
      }
    }
  }

  return TextArea.toString();
}
/*************************************************************/
public String getSystem(String URL)
{
  //Derive System from URL
  if (URL != null)
  {
    try
    {
      String lCaseUrl = URL.toLowerCase();
      int endPos = lCaseUrl.indexOf("eban");
      int startPos = lCaseUrl.lastIndexOf("/", endPos) + 1;
      return URL.substring(startPos, endPos);
      /*if (URL.indexOf("localhost") != -1)
        return "OSS";
      else
        return URL.substring(URL.indexOf("8080/")+5,URL.indexOf("eBAN"));*/
    }
    catch (IndexOutOfBoundsException ieb)
    {
      return "";
    }
  }
  else
  {
    return "";
  }
}
/*************************************************************/
public String getHost(String URL)
{
  //Derive Hostname from URL
  if (URL != null)
  {
    try
    {
      return URL.substring(URL.indexOf("http://")+7,URL.indexOf(":8080"));
    }
    catch (IndexOutOfBoundsException ieb)
    {
      return "";
    }
  }
  else
  {
    return "";
  }
}
/**************************************************************/
public String getEdifyServer(String System)
{
  BufferedReader inifile;
  String inirecord="";
  JavaUtil.FileUtil FU = new JavaUtil.FileUtil();

  inifile=FU.openFile("c:\\JDBC\\"+System+".ini");

  inirecord=FU.readNext(inifile);

  while ((inirecord.compareTo("EOF") != 0) && (!inirecord.startsWith("EOF")))
  {
    if (before(inirecord).compareToIgnoreCase("EDIFYSERVERNAME") == 0)
    {
      return after(inirecord);
    }
    inirecord=FU.readNext(inifile);
  }
  return "127.0.0.1";
}
/*************************************************************/
public String getEdifyServerName(String URL)
{
  String Host="";
  //Derive Hostname from URL
  if (URL != null)
  {
    try
    {
      Host=URL.substring(URL.indexOf("http://")+7,URL.indexOf(":8080"));

      if ((Host.compareTo("127.0.0.1")==0) || (Host.compareToIgnoreCase("localhost")==0))
      {
	return "127.0.0.1";
      }
      else if ((Host.compareToIgnoreCase("dlpc023")==0) || (Host.compareToIgnoreCase("dlpc005")==0))
      {
	return "xtpc005";
      }
      else if ((Host.compareToIgnoreCase("SWI-BCC-P10")==0) || (Host.compareTo("148.185.19.13")==0))
      {
	return "SWI-BCC-P10";
      }
      else if ((Host.compareToIgnoreCase("SWI-BCC-P5")==0) || (Host.compareTo("148.185.19.4")==0))
      {
	 return "SWI-BCC-GCBW1";
      }
      else
      {
	return "127.0.0.1";
      }
    }
    catch (IndexOutOfBoundsException ieb)
    {
      return "";
    }
  }
  else
  {
    return "";
  }
}
/*************************************************************/
  public boolean isSpaces(String string)
  {
    String temp=string;
    int pos=0;
    if (string != null)
    {
      while (pos<string.length())
      {
	temp=string.substring(pos,pos+1);
	if (temp.compareTo(" ")!=0)
	{
	  return false;
	}
	pos++;
      }
    }
    else
    {
      return false;
    }
    return true;
  }
  public void table ()
  {
    String SiteSplit[]={"","",""};
    String SiteSplitx[]={"","",""};
    String p="",s="",d="";
    Vector v=new Vector();
    Enumeration e=null;

    SiteSplit[0]="a";
    SiteSplit[1]="b";
    SiteSplit[2]="c";

    v.addElement(SiteSplit);
    e=v.elements();

    while (e.hasMoreElements())
    {
      SiteSplitx=(String[])e.nextElement();
      p=SiteSplitx[0];
    }
  }
/*************************************************************************************/
  public static boolean validDate(String dt, String format)
  {
    boolean isValid = false;
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      formatter.setLenient(false);
      formatter.parse(dt);
      isValid = true;
    }
    catch (Exception ex)
    {
    }
    finally
    {
      return isValid;
    }

  }
/*************************************************************************************/
  public static boolean isNumeric(String in)
  {
    boolean isValid = false;
    try
    {
      long num = Long.parseLong(in);
      isValid = true;
    }
    catch (NumberFormatException ex)
    {
    }
    finally
    {
      return isValid;
    }

  }
/*************************************************************************************/
  public String getLongAs2dpString(long value)
  {
    StringBuffer ret = new StringBuffer((value<0?"-":""));
    String temp = String.valueOf(Math.abs(value));
    int pos = 0;
    int len = temp.length();
    if (len < 3)
    {
      if (len == 1)
        ret.append("0.0" + temp);
      else
        ret.append("0." + temp);
    }
    else
    {
      pos = temp.length() - 2;
      ret.append(temp.substring(0, pos) + "." + temp.substring(pos));
    }
    return ret.toString();
  }
/*************************************************************************************/
  public String removeChar(String s, char c)
  {
    StringBuffer r = new StringBuffer();
    for (int i = 0; i < s.length(); i ++)
    {
      if (s.charAt(i) != c)
      {
        r.append(s.charAt(i));
      }
    }
    return r.toString();
  }
/*************************************************************************************/
  public static String alphaNumericOnly(String sIn)
  {
    String s = sIn.toUpperCase();
    StringBuffer r = new StringBuffer();
    boolean found = false;
    for (int i = 0; i < s.length(); i++)
    {
      found = false;
      for (int j = 0; j < alphaNum.length; j++)
      {
        if (s.charAt(i) == alphaNum[j])
        {
          r.append(s.charAt(i));
          found = true;
          break;
        }
      }
      if (!found)
      {
        r.append(" ");
      }
    }
    return r.toString();
  }
/*************************************************************************************/
  public static String alphaNumericOnly(String sIn, boolean replaceWithBlank)
  {
    String s = sIn.toUpperCase();
    StringBuffer r = new StringBuffer();
    boolean found = false;
    for (int i = 0; i < s.length(); i++)
    {
      found = false;
      for (int j = 0; j < alphaNum.length; j++)
      {
        if (s.charAt(i) == alphaNum[j])
        {
          r.append(s.charAt(i));
          found = true;
          break;
        }
      }
      if ((!found) && replaceWithBlank)
      {
        r.append(" ");
      }
    }
    return r.toString();
  }
/*************************************************************************************/
  public static boolean isAlphaNumericOnly(String sIn)
  {
    String s = sIn.toUpperCase();
    boolean found = false;
    for (int i = 0; i < s.length(); i++)
    {
      found = false;
      for (int j = 0; j < alphaNum.length; j++)
      {
        if (s.charAt(i) == alphaNum[j])
        {
          found = true;
          break;
        }
      }
      if (!found)
      {
        return false;
      }
    }
    return true;
  }
/*************************************************************************************/
  public static boolean containsUnfriendlyChars(String sIn)
  {
    String s = sIn.toUpperCase();
    for (int i = 0; i < s.length(); i++)
    {
      for (int j = 0; j < unfriendly.length; j++)
      {
        if (s.charAt(i) == unfriendly[j])
        {
          return true;
        }
      }
    }
    return false;
  }
/*************************************************************************************/
  public static String removeUnfriendlyChars(String sIn, boolean replaceWithBlank)
  {
    String s = sIn.toUpperCase();
    StringBuffer r = new StringBuffer();
    boolean found = false;
    for (int i = 0; i < s.length(); i++)
    {
      found = false;
      for (int j = 0; j < unfriendly.length; j++)
      {
        if (s.charAt(i) == unfriendly[j])
        {
          found = true;
          break;
        }
      }
      if (!found)
      {
        r.append(s.charAt(i));
      }
      else if (replaceWithBlank)
      {
        r.append(" ");
      }
    }
    return r.toString();
  }
/*************************************************************************************/
  public static String getRandomString (String charset, int length) {
    Random rnd = new Random (System.currentTimeMillis ());;
    StringBuffer sb = new StringBuffer (length);
    int nChars = charset.length ();
    int rndIdx;
    for (int i = 0; i < length; i++) {
      rndIdx = rnd.nextInt (nChars);
      sb.append (charset.charAt(rndIdx));
    }
    return sb.toString ();
  }
/*************************************************************************************/
  public static String getRandomString (int length) {
    return getRandomString
      ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"£$%^&*()-_=+[]{};:'@#~|,.<>/?", length);
  }
/*************************************************************************************/
  public static String getRandomLowerCaseString (int length) {
    return getRandomString ("abcdefghijklmnopqrstuvwxyz", length);
  }
/*************************************************************************************/
  public static String getRandomUpperCaseString (int length) {
    return getRandomString ("ABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
  }
/*************************************************************************************/
  public static String getRandomMixedString (int length)
  {
    StringBuffer sb = new StringBuffer(getRandomString
      ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"£$%^&*()-_=+[]{};:'@#~|,.<>/?", length));
    //check it has at least one upper case letter
    if (!hasAtleastOne(sb.toString(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
    {
      sb.append(getRandomUpperCaseString(1));
    }
    //check it has at least one symbol
    if (!hasAtleastOne(sb.toString(), "!\"£$%^&*()-_=+[]{};:'@#~|,.<>/?"))
    {
      sb.append(getRandomString("!\"£$%^&*()-_=+[]{};:'@#~|,.<>/?", 1));
    }
    //check it has at least one number
    if (!hasAtleastOne(sb.toString(), "0123456789"))
    {
      sb.append(getRandomString("0123456789", 1));
    }
    return sb.toString();
  }
/*************************************************************************************/
  public static boolean hasAtleastOne(String sIn, String sTest)
  {
    for (int i = 0; i < sIn.length(); i++)
    {
      for (int j = 0; j < sTest.length(); j++)
      {
        if (sIn.charAt(i) == sTest.charAt(j))
        {
          return true;
        }
      }
    }
    return false;
  }
/*************************************************************************************/
  public String replaceString(String input, String oldPattern,
    String newPattern)
  {
    if ((input.equals("")) || (oldPattern.equals("")))
    {
      return input;
    }

    StringBuffer result = new StringBuffer();
    int startIdx = 0;
    int idxOld = 0;
    while ((idxOld = input.indexOf(oldPattern, startIdx)) >= 0)
    {
      result.append( input.substring(startIdx, idxOld) );
      result.append( newPattern );

      startIdx = idxOld + oldPattern.length();
    }
    result.append( input.substring(startIdx) );
    return result.toString();
  }
/*************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    int value;
    StringUtil SU = new StringUtil();
    SU.table();
   }
 /*************************************************************************************/
}
