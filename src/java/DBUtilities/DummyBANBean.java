package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class DummyBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long itemId;
    private String screenItem;
    private java.sql.Date screenDateh;
    private int sDay;
    private int sMonth;
    private int sYear;

  public DummyBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("");
    errored.addElement("");
    itemId = 0;
    screenDateh = new java.sql.Date(new java.util.Date().getTime());
    screenItem = "";
    sDay = 0;
    sMonth = 0;
    sYear = 0;
  }

  public void Reset()
  {
    super.Reset();
    itemId = 0;
    screenDateh = new java.sql.Date(new java.util.Date().getTime());
    screenItem = "";
    sDay = 0;
    sMonth = 0;
    sYear = 0;
  }
/*set Methods*/
  public void setScreenItem(String value)
  {
    ScreenData.put("screenItem",value);
    screenItem = SU.isNull(value,"");
  }
  public void setSDay(String value)
  {
    try
    {
      setSDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sDay", "0");
    }
  }
  public void setSDay(int value)
  {
    ScreenData.put("sDay",String.valueOf(value));
    sDay = value;
  }
  public void setSMonth(String value)
  {
    try
    {
      setSMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sMonth","0");
    }
  }
  public void setSMonth(int value)
  {
    ScreenData.put("sMonth",String.valueOf(value));
    sMonth = value;
  }
  public void setSYear(String value)
  {
    try
    {
      setSYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sYear","0");
    }
  }
  public void setSYear(int value)
  {
    ScreenData.put("sYear",String.valueOf(value));
    sYear = value;
  }
  public void setScreenDate(String value)
  {
    ScreenData.put("screenDateh",value);
    screenDateh = SU.toJDBCDate(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(screenDateh==null?(new java.util.Date())
      :screenDateh);
    sDay=cal.get(cal.DATE);
    sMonth=cal.get(cal.MONTH)+1;
    sYear=cal.get(cal.YEAR);
    /*if (billingCeaseDateh == null)
    {
      bcDay=0;
      bcMonth=0;
      bcYear=0;
    }
    else
    {
      cal.setTime(billingCeaseDateh);
      bcDay=cal.get(cal.DATE);
      bcMonth=cal.get(cal.MONTH)+1;
      bcYear=cal.get(cal.YEAR);
    }*/
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("screenItem"))
    {
      setScreenItem(value);
    }
    else if (name.equals("screenDateDay"))
    {
      setSDay(value);
    }
    else if (name.equals("screenDateMonth"))
    {
      setSMonth(value);
    }
    else if (name.equals("screenDateYear"))
    {
      setSYear(value);
    }
    else if (name.equals("screenStartDateh"))
    {
      setScreenDate(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getScreenItem()
  {
    return screenItem;
  }
  public int getSDay()
  {
    return sDay;
  }
  public int getSMonth()
  {
    return sMonth;
  }
  public int getSYear()
  {
    return sYear;
  }
  public String getScreenDate()
  {
    if (screenDateh != null)
    {
      return SU.reformatDate(screenDateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getSelectMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getSelectMode();
    }
  }
  public String getInputMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getInputMode();
    }
  }
  public String getMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "READONLY";
    }
    else
    {
      return InputMode;
    }
  }
  /*******************************************************************************/
public String getClass(String Item)
{
        if (!errored.isEmpty())
	{
	  if (errored.contains(Item))
	    return "errored";
	  else
	  {
	    if (mandatory.contains(Item))
              return "mandatory";
	    else
	      return "optional";
	  }
    	}
	else
	{
	  if (mandatory.contains(Item))
            return "mandatory";
	  else
	    return "optional";
	}
}
/*******************************************************************************/
 public boolean getFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call db..Get_Stuff_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,itemId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          screenItem = RS.getString(1);
          screenDateh = RS.getDate(2);
          setDates();
          getFromDB = true;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getFromDB;
    }
 }
/*******************************************************************************/
public int updateDB()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call db..Update_Stuff_eBAN(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,itemId);
      cstmt.setString(2,screenItem);
      cstmt.setDate(3,screenDateh);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update thing</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Thing updated</b></font>";
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ret;
  }
}
/*******************************************************************************/
public int createDBEntry()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call db..Create_Stuff_eBAN(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,screenItem);
      cstmt.setDate(2,screenDateh);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new stuff</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New stuff created for " +
              screenItem +"</b></font>";
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ret;
  }
}
/******************************************************************************/
  public boolean isValid(String ButtonPressed)
  {
    Enumeration FormFields=mandatory.elements();
    setErrored("clear");
    Message = "<font color=red><b>";
    String FormField="";
    String FieldName;
    String value="";

    if (ButtonPressed.startsWith("Submit"))
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if ((FormField.equals("")) || (SU.isSpaces(FormField)))
        {
          setErrored(FieldName);
        }
        /*else if (FieldName.equals("billingRefPrefix"))
        {
          if ((FormField.length() < 4) || (FormField.length() > 8))
          {
            setErrored("billingRefPrefix");
            Message = "<font color=red><b>Billing Reference Prefix must be between 4 and 8 characters in length";
            return false;
          }
        }*/
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      /*if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
      {
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }*/
      return true;
    }
  }
}

