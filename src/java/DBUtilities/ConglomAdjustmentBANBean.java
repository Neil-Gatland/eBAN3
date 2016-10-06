package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class ConglomAdjustmentBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long conglomCustomerId;
    private long adjustmentId;
    private String screenItem;
    private int adjDay;
    private int adjMonth;
    private int adjYear;
    private DecimalFormat df2;
    private java.sql.Date adjustmentDateh;
    private String billedProduct;
    private String adjustmentCRDE;
    private String docketText;
    private String docketNumber;
    private String adjustmentDescription;
    private String sourceInvoice;
    private String dummyInvoice;
    private double netAmount;
    private double vatAmount;
    private double totalAmount;
    private String billPeriodRef;

  public ConglomAdjustmentBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("adjustmentDescription");
    mandatory.addElement("adjustmentAmount");
    mandatory.addElement("adjustmentCRDE");
    mandatory.addElement("conglomBilledProduct3");
    mandatory.addElement("docketNumber");
    mandatory.addElement("associateInvoice");
    mandatory.addElement("adjustmentDateh");
    errored.addElement("");
    adjustmentId = 0;
    screenItem = "";
    adjDay = 0;
    adjMonth = 0;
    adjYear = 0;
    df2 = new DecimalFormat( "#,###,###,##0.00" );
    adjustmentDateh = new java.sql.Date(new java.util.Date().getTime());
    billedProduct = "";
    adjustmentCRDE = "";
    docketText = "Id";
    docketNumber = "";
    adjustmentDescription = "";
    sourceInvoice = "";
    dummyInvoice = "";
    netAmount = 0;
    vatAmount = 0;
    totalAmount = 0;
    conglomCustomerId = -1;
    billPeriodRef = "";
  }

  public void Reset()
  {
    super.Reset();
    adjustmentId = 0;
    screenItem = "";
    adjDay = 0;
    adjMonth = 0;
    adjYear = 0;
    df2 = new DecimalFormat( "#,###,###,##0.00" );
    adjustmentDateh = new java.sql.Date(new java.util.Date().getTime());
    billedProduct = "";
    adjustmentCRDE = "";
    docketText = "Id";
    docketNumber = "";
    adjustmentDescription = "";
    sourceInvoice = "";
    dummyInvoice = "";
    netAmount = 0;
    vatAmount = 0;
    totalAmount = 0;
    conglomCustomerId = -1;
    billPeriodRef = "";
  }
/*set Methods*/
  public void setAdjustmentId(long value)
  {
    adjustmentId = value;
  }
  public void setTotalAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setTotalAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("totalAmount","0.00");
      totalAmount = 0.00;
    }
  }
  public void setTotalAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("totalAmount",String.valueOf(dd2dec));
    totalAmount = dd2dec;
    totalAmount = setAmountSign(totalAmount);
  }
  public void setVATAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setVATAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("vatAmount","0.00");
      vatAmount = 0.00;
    }
  }
  public void setVATAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("vatAmount",String.valueOf(dd2dec));
    vatAmount = dd2dec;
    vatAmount = setAmountSign(vatAmount);
  }
  public void setNetAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setNetAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("netAmount","0.00");
      netAmount = 0.00;
    }
  }
  public void setNetAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("netAmount",String.valueOf(dd2dec));
    netAmount = dd2dec;
    netAmount = setAmountSign(netAmount);
  }
  private void resetAmountSign()
  {
    netAmount = setAmountSign(netAmount);
    vatAmount = setAmountSign(vatAmount);
    totalAmount = setAmountSign(totalAmount);
  }
  private double setAmountSign(double amount)
  {
    if (SU.isNull(adjustmentCRDE,"").equals("CRED"))
    {
      if (amount > 0)
      {
        amount *= -1;
      }
    }
    else
    {
      if (amount < 0)
      {
        amount *= -1;
      }
    }
    return amount;
  }
  public void setDummyInvoice(String value)
  {
    ScreenData.put("dummyInvoice",value);
    dummyInvoice = SU.isNull(value,"");
  }
  public void setSourceInvoice(String value)
  {
    ScreenData.put("sourceInvoice",value);
    sourceInvoice = SU.isNull(value,"");
  }
  public void setDocketNumber(String value)
  {
    ScreenData.put("docketNumber",value);
    docketNumber = SU.isNull(value,"");
  }
  private void setDocketText()
  {
    if (adjustmentCRDE.equals("CRED"))
      docketText = "Credit Number";
    else if (adjustmentCRDE.equals("DEBT"))
      docketText = "Docket Number";
    else if (adjustmentCRDE.equals("SUND"))
      docketText = "Sundry Id";
    else
      docketText = "Id";
  }
  public void setBilledProduct(String value)
  {
    ScreenData.put("conglomBilledProduct3",value);
    billedProduct = SU.isNull(value,"");
  }
  public void setAdjustmentCRDE(String value)
  {
    ScreenData.put("adjustmentCRDE",value);
    adjustmentCRDE = SU.isNull(value,"");
    setDocketText();
    resetAmountSign();
  }
  public void setAdjustmentDescription(String value)
  {
    ScreenData.put("adjustmentDescription",value);
    adjustmentDescription = SU.isNull(value,"");
  }
  public void setAdjDay(String value)
  {
    try
    {
      setAdjDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("adjDay", "0");
    }
  }
  public void setAdjDay(int value)
  {
    ScreenData.put("adjDay",String.valueOf(value));
    adjDay = value;
  }
  public void setAdjMonth(String value)
  {
    try
    {
      setAdjMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("adjMonth","0");
    }
  }
  public void setAdjMonth(int value)
  {
    ScreenData.put("adjMonth",String.valueOf(value));
    adjMonth = value;
  }
  public void setAdjYear(String value)
  {
    try
    {
      setAdjYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("adjYear","0");
    }
  }
  public void setAdjYear(int value)
  {
    ScreenData.put("adjYear",String.valueOf(value));
    adjYear = value;
  }
  public void setAdjustmentDate(String value)
  {
    ScreenData.put("adjustmentDateh",value);
    adjustmentDateh = SU.toJDBCDate(value);
  }
  public void setConglomCustomerId(long value)
  {
    conglomCustomerId = value;
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(adjustmentDateh==null?(new java.util.Date())
      :adjustmentDateh);
    adjDay=cal.get(cal.DATE);
    adjMonth=cal.get(cal.MONTH)+1;
    adjYear=cal.get(cal.YEAR);
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("adjustmentDateDay"))
    {
      setAdjDay(value);
    }
    else if (name.equals("adjustmentDateMonth"))
    {
      setAdjMonth(value);
    }
    else if (name.equals("adjustmentDateYear"))
    {
      setAdjYear(value);
    }
    else if (name.equals("adjustmentDateh"))
    {
      setAdjustmentDate(value);
    }
    else if (name.equals("dummyInvoice"))
    {
      setDummyInvoice(value);
    }
    else if (name.equals("sourceInvoice"))
    {
      setSourceInvoice(value);
    }
    else if (name.equals("docketNumber"))
    {
      setDocketNumber(value);
    }
    else if (name.equals("conglomBilledProduct3"))
    {
      setBilledProduct(value);
    }
    else if (name.equals("adjustmentCRDE"))
    {
      setAdjustmentCRDE(value);
    }
    else if (name.equals("adjustmentDescription"))
    {
      setAdjustmentDescription(value);
    }
    else if (name.equals("netAmount"))
    {
      setNetAmount(value);
    }
    else if (name.equals("vatAmount"))
    {
      setVATAmount(value);
    }
    else if (name.equals("totalAmount"))
    {
      setTotalAmount(value);
    }
  }
/*
*/
/*******************************************************************************/
/*get Methods*/
  public long getAdjustmentId()
  {
    return adjustmentId;
  }
  public double getNetAmount()
  {
    return netAmount;
  }
  public double getVATAmount()
  {
    return vatAmount;
  }
  public double getTotalAmount()
  {
    return totalAmount;
  }
  public String getDummyInvoice()
  {
    return dummyInvoice;
  }
  public String getSourceInvoice()
  {
    return sourceInvoice;
  }
  public String getDocketNumber()
  {
    return docketNumber;
  }
  public String getDocketText()
  {
    return docketText;
  }
  public String getBilledProduct()
  {
    return billedProduct;
  }
  public String getAdjustmentCRDE()
  {
    return adjustmentCRDE;
  }
  public String getAdjustmentDescription()
  {
    return adjustmentDescription;
  }
  public int getAdjDay()
  {
    return adjDay;
  }
  public int getAdjMonth()
  {
    return adjMonth;
  }
  public int getAdjYear()
  {
    return adjYear;
  }
  public String getAdjustmentDate()
  {
    if (adjustmentDateh != null)
    {
      return SU.reformatDate(adjustmentDateh.toString());
    }
    else
    {
      return null;
    }
  }
  public long getConglomCustomerId()
  {
    return conglomCustomerId;
  }
  public String getSelectMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      (Mode.equals("Amend")))
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
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      (Mode.equals("Amend")))
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
    else if ((Mode.equals("Amend")) && (FieldName.equals("sourceInvoice") ||
      FieldName.equals("dummyInvoice")))
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
/*Select Source_Account_No,
Source_Acct_Check_Digit,
Source_Invoice_No,
Adjustment_Type,
Date_Raised,
Docket_Number,
Description,
Net_Amount,
Vat_Amount,
Total_Amount,
Billed_Product_Id
******************************************************************************/
 public boolean getAdjustment()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call Conglomerate..Get_Adjustment_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,adjustmentId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          sourceInvoice = RS.getString(1);
          setAdjustmentCRDE(RS.getString(2));
          adjustmentDateh = RS.getDate(3);
          docketNumber = RS.getString(4);
          adjustmentDescription = RS.getString(5);
          netAmount = RS.getDouble(6);
          vatAmount = RS.getDouble(7);
          totalAmount = RS.getDouble(8);
          setBilledProduct(RS.getString(9));
          setDates();
          setDocketText();
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
public int updateAdjustment()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Update_Adjustment_eBAN(?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,sourceInvoice);
      cstmt.setString(3,adjustmentCRDE);
      cstmt.setString(4,billedProduct);
      cstmt.setDouble(5,netAmount);
      cstmt.setDouble(6,vatAmount);
      cstmt.setDouble(7,totalAmount);
      cstmt.setString(8,docketNumber);
      cstmt.setString(9,adjustmentDescription);
      cstmt.setString(10,userId);
      cstmt.setLong(11,adjustmentId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update adjustment</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment updated successfully" +
              "</b></font>";
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
public int createAdjustment()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Add_Adjustment_eBAN(?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,sourceInvoice.equals("")?" ":sourceInvoice);
      cstmt.setString(3,adjustmentCRDE);
      cstmt.setString(4,billedProduct);
      cstmt.setDouble(5,netAmount);
      cstmt.setDouble(6,vatAmount);
      cstmt.setDouble(7,totalAmount);
      cstmt.setDate(8,adjustmentDateh);
      cstmt.setString(9,docketNumber);
      cstmt.setString(10,adjustmentDescription);
      cstmt.setString(11,userId);
      cstmt.setString(12,dummyInvoice);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new adjustment</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New adjustment created successfully" +
              "</b></font>";
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
/*****************************************************************************/
  public boolean deleteAdjustment()
  {
    boolean success = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call Conglomerate..Delete_Adjustment_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,adjustmentId);
        cstmt.execute();
        if (RS.next())
        {
          if (RS.getInt(1) == 0)
          {
            success = true;
            Message = "<font color=blue><b>Adjustment deleted</b></font>";
          }
          else
          {
            success = false;
            Message = "<font color=red><b>Unable to delete adjustment</b></font>";
          }
        }
      }
      else
      {
        success = false;
        Message = "<font color=red><b>Unable to delete adjustment</b></font>";
      }
    }
    catch(Exception ex)
    {
      Message="<font color=red><b>"+ex.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return success;
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

    if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        if (FieldName.equals("adjustmentAmount"))
        {
          if ((SU.hasNoValue((String)ScreenData.get("netAmount"))) ||
            (SU.hasNoValue((String)ScreenData.get("vatAmount"))) ||
            (SU.hasNoValue((String)ScreenData.get("totalAmount"))))
          {
            setErrored(FieldName);
          }
        }
        else if (FieldName.equals("associateInvoice"))
        {
          if ((SU.hasNoValue((String)ScreenData.get("sourceInvoice"))) &&
            (SU.hasNoValue((String)ScreenData.get("dummyInvoice"))))
          {
            setErrored(FieldName);
          }
        }
        else
        {
          if (SU.hasNoValue((String)ScreenData.get(FieldName)))
          {
            setErrored(FieldName);
          }
        }
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else if ((action.equals("Add")) &&
      (checkDocketNumberExists()))
    {
      setErrored("docketNumber");
      Message = "<font color=red><b>" + docketText + " exists already</b></font>";
      return false;
    }
    else if ((action.equals("Add")) &&
      (SU.hasNoValue((String)ScreenData.get("sourceInvoice"))) &&
      (checkGeneratedSourceInvoiceNumberExists()))
    {
      setErrored("docketNumber");
      Message = "<font color=red><b>The Source Invoice Number which will be " +
        " generated from this " + docketText + " (D" + docketNumber + ") " +
        "exists already</b></font>";
      return false;
    }
    else
    {
      double netPlusVat = SU.roundUp(netAmount + vatAmount, 2);
      if (totalAmount != netPlusVat)
      {
        setErrored("adjustmentAmount");
        Message = "<font color=red><b>Total amount must equal net plus vat";
        return false;
      }
      else if (!checkSourceInvoice())
      {
        setErrored("sourceInvoice");
        Message = "<font color=red><b>Source invoice does not exist for this customer and product";
        return false;
      }
      return true;
    }
  }
/************************************************************************************************/
  public boolean checkSourceInvoice()
  {
    boolean found = false;
    if (SU.hasNoValue(sourceInvoice))
    {
      return true;
    }
    else
    {
      try
      {
        SQL = "select Source_Acct_Check_Digit from Conglomerate..Source_Invoice (nolock) " +
          "where Conglom_Cust_Id = " + conglomCustomerId + " " +
          "and Billed_Product_Id = '" + billedProduct + "' " +
          "and Source_Invoice_No = '" + sourceInvoice + "' ";

        DBA.setSQL(SQL);
        if (DBA.Connect(READ,P5))
        {
          RS = DBA.getResultsSet();
          found = RS.next();
        }
      }
      catch(Exception ex)
      {
        Message=ex.getMessage();
      }
      finally
      {
        close();
        return found;
      }
    }
  }
/************************************************************************************************/
  public boolean checkDocketNumberExists()
  {
    boolean found = false;
    try
    {
      SQL = "select Docket_Number from Conglomerate..Conglom_Adjustment (nolock) " +
        "where Docket_Number = '" + docketNumber + "' ";

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        found = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return found;
    }
  }
/************************************************************************************************/
  public boolean checkGeneratedSourceInvoiceNumberExists()
  {
    boolean found = false;
    try
    {
      SQL = "select Source_Invoice_No from Conglomerate..Source_Invoice (nolock) " +
        "where Source_Invoice_No = 'D" + docketNumber + "' ";

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        found = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return found;
    }
  }
}

