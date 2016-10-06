package DBUtilities;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class ConglomBilledProductBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long conglomCustProductId;
    private String billedProductExtractFreq;
    private String billedProductFeedSource;
    private String billedProductId;
    private int bsDay;
    private int bsMonth;
    private int bsYear;
    private int bcDay;
    private int bcMonth;
    private int bcYear;
    private java.sql.Date billingStartDateh;
    private java.sql.Date billingCeaseDateh;
    private String leadAccount;
    private String checkDigit;
    private String goldfishConglomId;

  public ConglomBilledProductBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("conglomBilledProduct");
    mandatory.addElement("billedProductExtractFreq");
    mandatory.addElement("billingStartDateh");
    errored.addElement("");
    billingStartDateh = new java.sql.Date(new java.util.Date().getTime());
    billingCeaseDateh = null;
    bsDay = 0;
    bsMonth = 0;
    bsYear = 0;
    bcDay = 0;
    bcMonth = 0;
    bcYear = 0;
    conglomCustProductId = 0;
    billedProductExtractFreq = "";
    billedProductFeedSource = "none";
    billedProductId = "none";
    leadAccount = "";
    checkDigit = "";
    goldfishConglomId = "";
  }

  public void Reset()
  {
    super.Reset();
    billingStartDateh = new java.sql.Date(new java.util.Date().getTime());
    billingCeaseDateh = null;
    bsDay = 0;
    bsMonth = 0;
    bsYear = 0;
    bcDay = 0;
    bcMonth = 0;
    bcYear = 0;
    conglomCustProductId = 0;
    billedProductExtractFreq = "";
    billedProductFeedSource = "none";
    billedProductId = "none";
    leadAccount = "";
    checkDigit = "";
    goldfishConglomId = "";
    if (mandatory.contains("goldfishConglomId"))
    {
      mandatory.removeElement("goldfishConglomId");
    }
  }
/*set Methods*/
  public void setCheckDigit(String value)
  {
    ScreenData.put("checkDigit",value);
    checkDigit = value;
  }
  public void setLeadAccount(String value)
  {
    ScreenData.put("leadAccount",value);
    leadAccount = value;
  }
  public void setGoldfishConglomId(String value)
  {
    ScreenData.put("goldfishConglomId",value);
    goldfishConglomId = value;
  }
  public void setBilledProductId(String value)
  {
    ScreenData.put("conglomBilledProduct",value);
    billedProductId = value;
    if (action.equals("Add"))
    {
      getBilledProductDetails();
    }
  }
  public void setConglomCustProductId(long value)
  {
    conglomCustProductId = value;
  }
  public void setBilledProductExtractFreq(String value)
  {
    ScreenData.put("billedProductExtractFreq",value);
    billedProductExtractFreq = SU.isNull(value,"");
  }
  public void setBilledProductFeedSource(String value)
  {
    //ScreenData.put("billedProductFeedSource",value);
    billedProductFeedSource = SU.isNull(value,"");
    if (billedProductFeedSource.equalsIgnoreCase("GOLDFISH"))
    {
      if (!mandatory.contains("goldfishConglomId"))
      {
        mandatory.addElement("goldfishConglomId");
      }
    }
    else
    {
      goldfishConglomId = "";
      if (mandatory.contains("goldfishConglomId"))
      {
        mandatory.removeElement("goldfishConglomId");
      }
    }
  }
  public void setBSDay(String value)
  {
    try
    {
      setBSDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bsDay", "0");
    }
  }
  public void setBSDay(int value)
  {
    ScreenData.put("bsDay",String.valueOf(value));
    bsDay = value;
  }
  public void setBSMonth(String value)
  {
    try
    {
      setBSMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bsMonth","0");
    }
  }
  public void setBSMonth(int value)
  {
    ScreenData.put("bsMonth",String.valueOf(value));
    bsMonth = value;
  }
  public void setBSYear(String value)
  {
    try
    {
      setBSYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bsYear","0");
    }
  }
  public void setBSYear(int value)
  {
    ScreenData.put("bsYear",String.valueOf(value));
    bsYear = value;
  }
  public void setBillingStartDate(String value)
  {
    ScreenData.put("billingStartDateh",value);
    billingStartDateh = SU.toJDBCDate(value);
  }
  public void setBCDay(String value)
  {
    try
    {
      setBCDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bcDay", "0");
    }
  }
  public void setBCDay(int value)
  {
    ScreenData.put("bcDay",String.valueOf(value));
    bcDay = value;
  }
  public void setBCMonth(String value)
  {
    try
    {
      setBCMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bcMonth","0");
    }
  }
  public void setBCMonth(int value)
  {
    ScreenData.put("bcMonth",String.valueOf(value));
    bcMonth = value;
  }
  public void setBCYear(String value)
  {
    try
    {
      setBCYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bcYear","0");
    }
  }
  public void setBCYear(int value)
  {
    ScreenData.put("bcYear",String.valueOf(value));
    bcYear = value;
  }
  public void setBillingCeaseDate(String value)
  {
    ScreenData.put("billingCeaseDateh",value);
    billingCeaseDateh = SU.toJDBCDate(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(billingStartDateh==null?(new java.util.Date())
      :billingStartDateh);
    bsDay=cal.get(cal.DATE);
    bsMonth=cal.get(cal.MONTH)+1;
    bsYear=cal.get(cal.YEAR);
    if (billingCeaseDateh == null)
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
    }
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("conglomBilledProduct"))
    {
      setBilledProductId(value);
    }
    else if (name.equals("leadAccount"))
    {
      setLeadAccount(value);
    }
    else if (name.equals("checkDigit"))
    {
      setCheckDigit(value);
    }
    else if (name.equals("goldfishConglomId"))
    {
      setGoldfishConglomId(value);
    }
    else if (name.equals("billingStartDateDay"))
    {
      setBSDay(value);
    }
    else if (name.equals("billingStartDateMonth"))
    {
      setBSMonth(value);
    }
    else if (name.equals("billingStartDateYear"))
    {
      setBSYear(value);
    }
    else if (name.equals("billingStartDateh"))
    {
      setBillingStartDate(value);
    }
    else if (name.equals("billingCeaseDateDay"))
    {
      setBCDay(value);
    }
    else if (name.equals("billingCeaseDateMonth"))
    {
      setBCMonth(value);
    }
    else if (name.equals("billingCeaseDateYear"))
    {
      setBCYear(value);
    }
    else if (name.equals("billingCeaseDateh"))
    {
      setBillingCeaseDate(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getCheckDigit()
  {
    return checkDigit;
  }
  public String getGoldfishConglomId()
  {
    return goldfishConglomId;
  }
  public String getLeadAccount()
  {
    return leadAccount;
  }
  public String getBilledProductId()
  {
    return billedProductId;
  }
  public long getConglomCustProductId()
  {
    return conglomCustProductId;
  }
  public String getBilledProductExtractFreq()
  {
    return billedProductExtractFreq;
  }
  public String getBilledProductFeedSource()
  {
    return billedProductFeedSource;
  }
  public int getBSDay()
  {
    return bsDay;
  }
  public int getBSMonth()
  {
    return bsMonth;
  }
  public int getBSYear()
  {
    return bsYear;
  }
  public String getBillingStartDate()
  {
    if (billingStartDateh != null)
    {
      return SU.reformatDate(billingStartDateh.toString());
    }
    else
    {
      return null;
    }
  }
  public int getBCDay()
  {
    return bcDay;
  }
  public int getBCMonth()
  {
    return bcMonth;
  }
  public int getBCYear()
  {
    return bcYear;
  }
  public String getBillingCeaseDate()
  {
    if (billingCeaseDateh != null)
    {
      return SU.reformatDate(billingCeaseDateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getSelectMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else if (FieldName.equals("conglomBilledProduct"))
    {
      if (action.equals("Add"))
      {
        return "submit";
      }
      else
      {
        return "DISABLED";
      }
    }
    else
    {
      return super.getSelectMode();
    }
  }
  public String getInputMode(String FieldName)
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
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
       ((!billedProductFeedSource.equalsIgnoreCase("GOLDFISH")) &&
        (FieldName.equals("goldfishConglomId"))))
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
  public boolean checkBilledProductClosed()
  {
    boolean closed = false;
    if (billingCeaseDateh != null)
    {
      java.util.Calendar rightNow = java.util.Calendar.getInstance();
      rightNow.set(rightNow.HOUR, 0);
      rightNow.set(rightNow.MINUTE, 0);
      rightNow.set(rightNow.SECOND, 0);
      rightNow.set(rightNow.MILLISECOND, 0);
      java.sql.Date now = new java.sql.Date(rightNow.getTime().getTime());
      closed = !billingCeaseDateh.after(now);
    }
    return closed;
  }
/*******************************************************************************/
 public boolean getBilledProduct()
 {
    boolean getBilledProduct = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Customer_Product_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustProductId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          billingStartDateh = RS.getDate(1);
          billingCeaseDateh = RS.getDate(2);
          leadAccount = RS.getString(3);
          checkDigit = RS.getString(4);
          goldfishConglomId = RS.getString(5);
          billedProductId = RS.getString(6);
          ScreenData.put("conglomBilledProduct",billedProductId);
          setDates();
          getBilledProduct = true;
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
      return getBilledProduct;
    }
 }
/*******************************************************************************/
public int createCustomerProduct()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Create_Conglom_Cust_Product_eBAN(?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,this.getConglomCustomerId());
      cstmt.setString(2,billedProductId);
      cstmt.setDate(3,billingStartDateh);
      cstmt.setDate(4,billingCeaseDateh);
      cstmt.setString(5,leadAccount);
      cstmt.setString(6,checkDigit);
      cstmt.setString(7,goldfishConglomId);
      cstmt.setString(8,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret == -2)
          {
            Message = "<font color=red><b>Customer has this product already</b></font>";
          }
          else if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new customer product</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New product added to this customer</b></font>";
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
public int updateCustomerProduct()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Update_Conglom_Cust_Product_eBAN(?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustProductId);
      cstmt.setDate(2,billingStartDateh);
      cstmt.setDate(3,billingCeaseDateh);
      cstmt.setString(4,leadAccount);
      cstmt.setString(5,checkDigit);
      cstmt.setString(6,goldfishConglomId);
      cstmt.setString(7,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update this customer product</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Customer product updated</b></font>";
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
/*public int updateDB()
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
}*/
/******************************************************************************/
  public boolean isValid(String ButtonPressed)
  {
    Enumeration FormFields=mandatory.elements();
    setErrored("clear");
    Message = "<font color=red><b>";
    String FormField="";
    String FieldName;
    String value="";

    //if (ButtonPressed.startsWith("Submit"))
    //{
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
    //}
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
      {
        setErrored("billingStartDate");
        setErrored("billingCeaseDate");
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }
      if ((!billedProductFeedSource.equalsIgnoreCase("GOLDFISH")) &&
        (!goldfishConglomId.equals("")))
      {
        setErrored("goldfishConglomId");
        Message = "<font color=red><b>Goldfish Conglom Id can only be entered for Goldfish products";
        return false;
      }
      return true;
    }
  }
/************************************************************************************************/
  private void getBilledProductDetails()
  {
    try
    {
      SQL = "select Feed_Source, Extract_Frequency " +
        "from conglomerate..Billed_Product (nolock) " +
        "where Billed_Product_Id = '" + billedProductId + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          setBilledProductFeedSource(RS.getString(1));
          setBilledProductExtractFreq(RS.getString(2));
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
    }
  }
}

