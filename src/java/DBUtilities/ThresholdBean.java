package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class ThresholdBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private BigDecimal amountPRS;
    private BigDecimal amountNonPRS;
    private long minutesPRS;
    private long minutesNonPRS;
    private long masterAccountId;
    private String productGroup;
    private String thresholdType;
    private String masterAccountNumber;
    private String masterAccountName;
    private String productDescription;

  public ThresholdBean ()
  {
    mandatory.clear();
    /*mandatory.addElement("amountPRS");
    mandatory.addElement("amountNonPRS");
    mandatory.addElement("minutesPRS");
    mandatory.addElement("minutesNonPRS");*/
    errored.addElement("");
    minutesPRS = 0;
    minutesNonPRS = 0;
    amountPRS = new BigDecimal(new BigInteger("0"), 2);
    amountNonPRS = new BigDecimal(new BigInteger("0"), 2);
    masterAccountId = -1;
    productGroup = null;
    thresholdType = "";
    masterAccountNumber = "";
    masterAccountName = "";
    productDescription = "";
  }

  public void Reset()
  {
    super.Reset();
    minutesPRS = 0;
    minutesNonPRS = 0;
    amountPRS = new BigDecimal(new BigInteger("0"), 2);
    amountNonPRS = new BigDecimal(new BigInteger("0"), 2);
    masterAccountId = -1;
    productGroup = null;
    thresholdType = "";
    masterAccountNumber = "";
    masterAccountName = "";
    productDescription = "";
  }
/*set Methods*/
  public void setMasterAccountId(String value)
  {
    masterAccountId = Long.parseLong(value);
  }
  public void setProductGroup(String value)
  {
    productGroup = value;
  }
  public void setThresholdType(String value)
  {
    thresholdType = value;
    mandatory.clear();
    mandatory.addElement("amountPRS");
    mandatory.addElement("minutesPRS");
    if (thresholdType.equalsIgnoreCase("invoice"))
    {
      mandatory.addElement("amountNonPRS");
      mandatory.addElement("minutesNonPRS");
    }
  }
  public void setMinutesPRS(String value)
  {
    try
    {
      setMinutesPRS(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("minutesPRS", "0");
    }
  }
  public void setMinutesPRS(long value)
  {
    ScreenData.put("minutesPRS",String.valueOf(value));
    minutesPRS = value;
  }
  public void setMinutesNonPRS(String value)
  {
    try
    {
      setMinutesNonPRS(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("minutesNonPRS", "0");
    }
  }
  public void setMinutesNonPRS(long value)
  {
    ScreenData.put("minutesNonPRS",String.valueOf(value));
    minutesNonPRS = value;
  }
  public void setAmountPRS(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      amountPRS = new BigDecimal(SU.hasNoValue(tidy, "0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
      ScreenData.put("amountPRS",amountPRS.toString());
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("amountPRS","0.00");
      amountPRS = new BigDecimal(new BigInteger("0"), 2);
    }
  }
  public void setAmountNonPRS(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      amountNonPRS = new BigDecimal(SU.hasNoValue(tidy, "0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
      ScreenData.put("amountNonPRS",amountNonPRS.toString());
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("amountNonPRS","0.00");
      amountNonPRS = new BigDecimal(new BigInteger("0"), 2);
    }
  }

  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("amountPRS"))
    {
      setAmountPRS(value);
    }
    else if (name.equals("amountNonPRS"))
    {
      setAmountNonPRS(value);
    }
    else if (name.equals("minutesPRS"))
    {
      setMinutesPRS(value);
    }
    else if (name.equals("minutesNonPRS"))
    {
      setMinutesNonPRS(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getProductGroupDisplay()
  {
    if (productGroup != null)
    {
      return productGroup;
    }
    else
    {
      return "";
    }
  }
  public String getMasterAccountDisplay()
  {
    if (masterAccountId != -1)
    {
      return masterAccountName + "/" + masterAccountNumber + " (" +
        masterAccountId + ")";
    }
    else
    {
      return "";
    }
  }
  public String getThresholdType()
  {
    return thresholdType;
  }
  public String getThresholdTypeDisplay()
  {
    return thresholdType + (thresholdType.equals("Product")?" Group":"");
  }
  public String getMinutesNonPRS()
  {
    return Long.toString(minutesNonPRS);
  }
  public String getMinutesPRS()
  {
    return Long.toString(minutesPRS);
  }
  public String getAmountPRS()
  {
    return amountPRS.toString();
  }
  public String getAmountNonPRS()
  {
    return amountNonPRS.toString();
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
 public boolean getThresholdFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Threshold(?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,thresholdType);
        cstmt.setLong(2,masterAccountId);
        cstmt.setString(3,productGroup);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          getFromDB = true;
          amountPRS = RS.getBigDecimal(1, 2);
          minutesPRS = RS.getLong(2);
          if (RS.next())
          {
            amountNonPRS = RS.getBigDecimal(1, 2);
            minutesNonPRS = RS.getLong(2);
           }
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
public boolean updateThreshold()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Threshold(?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,thresholdType);
      cstmt.setLong(2,masterAccountId);
      cstmt.setString(3,productGroup);
      cstmt.setBigDecimal(4,amountPRS);
      cstmt.setBigDecimal(5,amountNonPRS);
      cstmt.setLong(6,minutesPRS);
      cstmt.setLong(7,minutesNonPRS);
      cstmt.setString(8,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Threshold</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Threshold updated</b></font>";
            ok = true;
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
    return ok;
  }
}
/*******************************************************************************/
public boolean deleteThreshold()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Threshold(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,thresholdType);
      cstmt.setLong(2,masterAccountId);
      cstmt.setString(3,productGroup);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Threshold</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Threshold deleted</b></font>";
            ok = true;
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
    return ok;
  }
}
/*******************************************************************************/
public boolean createThreshold()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Threshold(?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,thresholdType);
      cstmt.setLong(2,masterAccountId);
      cstmt.setString(3,productGroup);
      cstmt.setBigDecimal(4,amountPRS);
      cstmt.setBigDecimal(5,amountNonPRS);
      cstmt.setLong(6,minutesPRS);
      cstmt.setLong(7,minutesNonPRS);
      cstmt.setString(8,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create Threshold</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Threshold created</b></font>";
            ok = true;
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
    return ok;
  }
}
/*******************************************************************************/
/*public int createDBEntry()
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
      if ((minutesPRS != 0) && (amountPRS.doubleValue() != 0))
      {
        setErrored("minutesPRS");
        setErrored("amountPRS");
        Message = "<font color=red><b>Only one of Minimum Amount and Minimum Duration can have a non-zero value";
        return false;
      }
      else if ((minutesNonPRS != 0) && (amountNonPRS.doubleValue() != 0))
      {
        setErrored("minutesNonPRS");
        setErrored("amountNonPRS");
        Message = "<font color=red><b>Only one of Minimum Amount and Minimum Duration can have a non-zero value";
        return false;
      }
      /*if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
    minutesPRS = 0;
    minutesNonPRS = 0;
    amountPRS = new BigDecimal(new BigInteger("0"), 2);
    amountNonPRS = new BigDecimal(new BigInteger("0"), 2);
      {
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }*/
      return true;
    }
  }
/*******************************************************************************/
  public boolean getMasterAccountFromDB()
  {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Master_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          masterAccountNumber = RS.getString(2);
          masterAccountName = RS.getString(3);
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
/*  public boolean getProductFromDB()
  {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Product(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productGroup);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          productDescription = RS.getString(1);
          //productDescription = RS.getString(1);
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
  }*/
}

