package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class InvoiceThresholdBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private BigDecimal invoiceThresholdPRS;
    private BigDecimal invoiceThresholdNonPRS;
    private long minDurationPRS;
    private long minDurationNonPRS;

  public InvoiceThresholdBean ()
  {
    mandatory.clear();
    mandatory.addElement("invoiceThresholdPRS");
    mandatory.addElement("invoiceThresholdNonPRS");
    mandatory.addElement("minDurationPRS");
    mandatory.addElement("minDurationNonPRS");
    errored.addElement("");
    minDurationPRS = 0;
    minDurationNonPRS = 0;
    invoiceThresholdPRS = new BigDecimal(new BigInteger("0"), 2);
    invoiceThresholdNonPRS = new BigDecimal(new BigInteger("0"), 2);
  }

  public void Reset()
  {
    super.Reset();
    minDurationPRS = 0;
    minDurationNonPRS = 0;
    invoiceThresholdPRS = new BigDecimal(new BigInteger("0"), 2);
    invoiceThresholdNonPRS = new BigDecimal(new BigInteger("0"), 2);

  }
/*set Methods*/
  public void setMinDurationPRS(String value)
  {
    try
    {
      setMinDurationPRS(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("minDurationPRS", "0");
    }
  }
  public void setMinDurationPRS(long value)
  {
    ScreenData.put("minDurationPRS",String.valueOf(value));
    minDurationPRS = value;
  }
  public void setMinDurationNonPRS(String value)
  {
    try
    {
      setMinDurationNonPRS(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("minDurationNonPRS", "0");
    }
  }
  public void setMinDurationNonPRS(long value)
  {
    ScreenData.put("minDurationNonPRS",String.valueOf(value));
    minDurationNonPRS = value;
  }
  public void setInvoiceThresholdPRS(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      invoiceThresholdPRS = new BigDecimal(SU.hasNoValue(tidy, "0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
      ScreenData.put("invoiceThresholdPRS",invoiceThresholdPRS.toString());
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("invoiceThresholdPRS","0.00");
      invoiceThresholdPRS = new BigDecimal(new BigInteger("0"), 2);
    }
  }
  public void setInvoiceThresholdNonPRS(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      invoiceThresholdNonPRS = new BigDecimal(SU.hasNoValue(tidy, "0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
      ScreenData.put("invoiceThresholdNonPRS",invoiceThresholdNonPRS.toString());
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("invoiceThresholdNonPRS","0.00");
      invoiceThresholdNonPRS = new BigDecimal(new BigInteger("0"), 2);
    }
  }

  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("invoiceThresholdPRS"))
    {
      setInvoiceThresholdPRS(value);
    }
    else if (name.equals("invoiceThresholdNonPRS"))
    {
      setInvoiceThresholdNonPRS(value);
    }
    else if (name.equals("minDurationPRS"))
    {
      setMinDurationPRS(value);
    }
    else if (name.equals("minDurationNonPRS"))
    {
      setMinDurationNonPRS(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getMinDurationNonPRS()
  {
    return Long.toString(minDurationNonPRS);
  }
  public String getMinDurationPRS()
  {
    return Long.toString(minDurationPRS);
  }
  public String getInvoiceThresholdPRS()
  {
    return invoiceThresholdPRS.toString();
  }
  public String getInvoiceThresholdNonPRS()
  {
    return invoiceThresholdNonPRS.toString();
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
 public boolean getInvoiceThreshold()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Invoice_Threshold()}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          invoiceThresholdPRS = RS.getBigDecimal(1, 2);
          minDurationPRS = RS.getLong(2);
          if (RS.next())
          {
            invoiceThresholdNonPRS = RS.getBigDecimal(1, 2);
            minDurationNonPRS = RS.getLong(2);
            getFromDB = true;
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
public int updateInvoiceThreshold()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Invoice_Threshold(?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setBigDecimal(1,invoiceThresholdPRS);
      cstmt.setBigDecimal(2,invoiceThresholdNonPRS);
      cstmt.setLong(3,minDurationPRS);
      cstmt.setLong(4,minDurationNonPRS);
      cstmt.setString(5,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Invoice Threshold</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Invoice Threshold updated</b></font>";
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
      /*if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
      {
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }*/
      return true;
    }
  }
}

