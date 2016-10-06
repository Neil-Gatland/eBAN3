package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class AdjustmentBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long providerId;
    private long masterAccountId;
    private String invoiceNumber;
    private String adjustmentType;
    private String adjustmentDescription;
    private String adjustmentLineDescription;
    private java.sql.Date taxPointDateh;
    private int tpDay;
    private int tpMonth;
    private int tpYear;
    private BigDecimal adjustmentAmount;
    private BigDecimal adjustmentLineAmount;
    private BigDecimal netAmount;
    private BigDecimal vatAmount;
    private BigDecimal grossAmount;
    private BigDecimal vatRate;
    private String lineAction;
    private String lineMode;
    private String latestCallMonth;
    private String adjustmentLineList;
    private long adjustmentLineNo;

  public AdjustmentBean ()
  {
    mandatory.clear();
    mandatory.addElement("providerId");
    mandatory.addElement("masterAccountId");
    mandatory.addElement("adjustmentCRDE");
    mandatory.addElement("adjustmentDescription");
    mandatory.addElement("adjustmentAmount");
    errored.clear();
    errored.addElement("");
    providerId = 0;
    masterAccountId = 0;
    invoiceNumber = "";
    adjustmentType = "";
    adjustmentDescription = "";
    taxPointDateh = new java.sql.Date(new java.util.Date().getTime());
    setDates();
    adjustmentAmount = new BigDecimal(0);
    netAmount = new BigDecimal(0);
    vatAmount = new BigDecimal(0);
    grossAmount = new BigDecimal(0);
    vatRate = new BigDecimal(0);
    lineAction = "";
    lineMode = "";
    latestCallMonth = "";
    adjustmentLineList = "";
    adjustmentLineNo = 0;
  }

  public void Reset()
  {
    super.Reset();
    errored.clear();
    errored.addElement("");
    providerId = 0;
    masterAccountId = 0;
    invoiceNumber = "";
    adjustmentType = "";
    adjustmentDescription = "";
    taxPointDateh = new java.sql.Date(new java.util.Date().getTime());
    setDates();
    adjustmentAmount = new BigDecimal(0);
    netAmount = new BigDecimal(0);
    vatAmount = new BigDecimal(0);
    grossAmount = new BigDecimal(0);
    vatRate = new BigDecimal(0);
    lineAction = "";
    lineMode = "";
    latestCallMonth = "";
    adjustmentLineList = "";
    adjustmentLineNo = 0;
  }
/*set Methods*/
  public void setAdjustmentLineNo(String value)
  {
    try
    {
      adjustmentLineNo = Long.parseLong(SU.hasNoValue(value, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      adjustmentLineNo = 0;
    }
  }
  public void setAdjustmentLineList(String value)
  {
    adjustmentLineList = SU.isNull(value,"");
  }
  public void setLatestCallMonth(String value)
  {
    latestCallMonth = SU.isNull(value,"");
  }
  public void setLineAction(String value)
  {
    lineAction = SU.isNull(value,"");
  }
  public void setLineMode(String value)
  {
    lineMode = SU.isNull(value,"");
  }
  public void setAction(String value)
  {
    action = SU.isNull(value,"");
    if (action.equals("Amend"))
    {
      if (mandatory.contains("adjustmentDescription"))
      {
        mandatory.remove("adjustmentDescription");
        mandatory.remove("adjustmentAmount");
      }
    }
    else
    {
      if (!mandatory.contains("adjustmentDescription"))
      {
        mandatory.add("adjustmentDescription");
        mandatory.add("adjustmentAmount");
      }
    }
  }
  public void setInvoiceNumber(String value)
  {
    ScreenData.put("invoiceNumber",value);
    invoiceNumber = SU.isNull(value,"");
  }
  public void setAdjustmentType(String value)
  {
    ScreenData.put("adjustmentCRDE",value);
    adjustmentType = SU.isNull(value,"");
    recalculateAmounts();
  }
  public void setAdjustmentDescription(String value)
  {
    ScreenData.put("adjustmentDescription",value);
    adjustmentDescription = SU.isNull(value,"");
  }
  public void setAdjustmentLineDescription(String value)
  {
    adjustmentLineDescription = SU.isNull(value,"");
  }
  public void setProviderId(String value)
  {
    try
    {
      setProviderId(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("providerId", "0");
    }
  }
  public void setProviderId(long value)
  {
    if (providerId != value)
    {
      ScreenData.put("providerId",String.valueOf(value));
      providerId = value;
      getProviderVATRate();
      recalculateAmounts();
    }
  }
  public void setMasterAccountId(String value)
  {
    try
    {
      setMasterAccountId(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("masterAccountId", "0");
    }
  }
  public void setMasterAccountId(long value)
  {
    ScreenData.put("masterAccountId",String.valueOf(value));
    masterAccountId = value;
  }
  public void setTPDay(String value)
  {
    try
    {
      setTPDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("tpDay", "0");
    }
  }
  public void setTPDay(int value)
  {
    ScreenData.put("tpDay",String.valueOf(value));
    tpDay = value;
  }
  public void setTPMonth(String value)
  {
    try
    {
      setTPMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("tpMonth","0");
    }
  }
  public void setTPMonth(int value)
  {
    ScreenData.put("tpMonth",String.valueOf(value));
    tpMonth = value;
  }
  public void setTPYear(String value)
  {
    try
    {
      setTPYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("tpYear","0");
    }
  }
  public void setTPYear(int value)
  {
    ScreenData.put("tpYear",String.valueOf(value));
    tpYear = value;
  }
  public void setTaxPointDate(String value)
  {
    ScreenData.put("taxPointDateh",value);
    taxPointDateh = SU.toJDBCDate(value);
  }
  public void setAdjustmentLineAmount(String value)
  {
    try
    {
      adjustmentLineAmount = new BigDecimal(SU.hasNoValue(value, "0"));
      if (adjustmentType.equals("Credit"))
      {
        if (adjustmentLineAmount.compareTo(new BigDecimal(0)) == 1)
        {
          adjustmentLineAmount = adjustmentLineAmount.negate();
        }
      }
      else
      {
        if (adjustmentLineAmount.compareTo(new BigDecimal(0)) == -1)
        {
          adjustmentLineAmount = adjustmentLineAmount.negate();
        }
      }
    }
    catch (java.lang.NumberFormatException NE)
    {
      adjustmentLineAmount = new BigDecimal(0);
    }
  }
  public void setAdjustmentAmount(String value)
  {
    try
    {
      adjustmentAmount = new BigDecimal(SU.hasNoValue(value, "0"));
      ScreenData.put("adjustmentAmount", SU.hasNoValue(value, "0"));
      recalculateAmounts();
    }
    catch (java.lang.NumberFormatException NE)
    {
      adjustmentAmount = new BigDecimal(0);
      ScreenData.put("adjustmentAmount", "0");
      recalculateAmounts();
    }
  }
  private void recalculateAmounts()
  {
    if (adjustmentType.equals("Credit"))
    {
      if (adjustmentAmount.compareTo(new BigDecimal(0)) == 1)
      {
        adjustmentAmount = adjustmentAmount.negate();
      }
    }
    else
    {
      if (adjustmentAmount.compareTo(new BigDecimal(0)) == -1)
      {
        adjustmentAmount = adjustmentAmount.negate();
      }
    }
    ScreenData.put("adjustmentAmount", adjustmentAmount.toString());
    netAmount = adjustmentAmount;
    ScreenData.put("netAmount", netAmount.toString());
    vatAmount = netAmount.multiply(vatRate).setScale(2,BigDecimal.ROUND_HALF_UP);
    ScreenData.put("vatAmount", vatAmount.toString());
    grossAmount = netAmount.add(vatAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
    ScreenData.put("grossAmount", grossAmount.toString());
  }
  public void setNetAmount(String value)
  {
    try
    {
      netAmount = new BigDecimal(SU.hasNoValue(value, "0"));
      ScreenData.put("netAmount", SU.hasNoValue(value, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("netAmount", "0");
    }
  }
  public void setVATAmount(String value)
  {
    try
    {
      vatAmount = new BigDecimal(SU.hasNoValue(value, "0"));
      ScreenData.put("vatAmount", SU.hasNoValue(value, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("vatAmount", "0");
    }
  }
  public void setGrossAmount(String value)
  {
    try
    {
      grossAmount = new BigDecimal(SU.hasNoValue(value, "0"));
      ScreenData.put("grossAmount", SU.hasNoValue(value, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("grossAmount", "0");
    }
  }
  public void setVATRate(String value)
  {
    try
    {
      vatRate = new BigDecimal(SU.hasNoValue(value, "0"));
      ScreenData.put("vatRate", SU.hasNoValue(value, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("vatRate", "0");
    }
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(taxPointDateh==null?(new java.util.Date())
      :taxPointDateh);
    tpDay=cal.get(cal.DATE);
    tpMonth=cal.get(cal.MONTH)+1;
    tpYear=cal.get(cal.YEAR);
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("providerId"))
    {
      setProviderId(value);
    }
    else if (name.equals("masterAccountId"))
    {
      setMasterAccountId(value);
    }
    else if (name.equals("invoiceNumber"))
    {
      setInvoiceNumber(value);
    }
    else if (name.equals("adjustmentCRDE"))
    {
      setAdjustmentType(value);
    }
    else if (name.equals("adjustmentDescription"))
    {
      setAdjustmentDescription(value);
    }
    else if (name.equals("adjustmentAmount"))
    {
      setAdjustmentAmount(value);
    }
    /*else if (name.equals("netAmount"))
    {
      setNetAmount(value);
    }
    else if (name.equals("vatAmount"))
    {
      setVATAmount(value);
    }
    else if (name.equals("grossAmount"))
    {
      setGrossAmount(value);
    }
    else if (name.equals("vatRate"))
    {
      setVATRate(value);
    }
    else if (name.equals("taxPointDateDay"))
    {
      setTPDay(value);
    }
    else if (name.equals("taxPointDateMonth"))
    {
      setTPMonth(value);
    }
    else if (name.equals("taxPointDateYear"))
    {
      setTPYear(value);
    }
    else if (name.equals("taxPointStartDateh"))
    {
      setTaxPointDate(value);
    }*/
  }
/*******************************************************************************/
/*get Methods*/
  public String getAdjustmentLineList()
  {
    return adjustmentLineList;
  }
  public String getProviderId()
  {
    return providerId<=0?"":Long.toString(providerId);
  }
  public String getMasterAccountId()
  {
    return masterAccountId<=0?"":Long.toString(masterAccountId);
  }
  public String getInvoiceNumber()
  {
    return invoiceNumber;
  }
  public String getAdjustmentType()
  {
    return adjustmentType;
  }
  public String getAdjustmentDescription()
  {
    return adjustmentDescription;
  }
  public String getAdjustmentAmount()
  {
    return adjustmentAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public String getAdjustmentLineDescription()
  {
    return adjustmentLineDescription;
  }
  public String getAdjustmentLineAmount()
  {
    return adjustmentLineAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public String getNetAmount()
  {
    return netAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public String getVATAmount()
  {
    return vatAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public String getGrossAmount()
  {
    return grossAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public String getVATRate()
  {
    return vatRate.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
  }
  public int getTPDay()
  {
    return tpDay;
  }
  public int getTPMonth()
  {
    return tpMonth;
  }
  public int getTPYear()
  {
    return tpYear;
  }
  public String getTaxPointDate()
  {
    if (taxPointDateh != null)
    {
      return SU.reformatDate(taxPointDateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getLineAction()
  {
    return lineAction;
  }
  public String getLineMode()
  {
    return lineMode;
  }
  public String getLatestCallMonth()
  {
    return latestCallMonth;
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
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      (!action.equals("Add")))
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
      (!action.equals("Add")) || (FieldName.equals("invoiceNumber")) ||
      (FieldName.equals("netAmount")) || (FieldName.equals("vatAmount")) ||
      (FieldName.equals("vatRate")) || (FieldName.equals("grossAmount")))
    {
      return "READONLY";
    }
    else if ((FieldName.equals("providerId")) || (FieldName.equals("adjustmentCRDE")))
    {
      return "submit";
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
 public boolean getProviderVATRate()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Provider_VAT_Rate(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,providerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          vatRate = RS.getBigDecimal(1,3);
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
 public boolean getAdjustmentInvoiceFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Adjustment_Invoice(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          taxPointDateh = RS.getDate(2);
          masterAccountId = RS.getLong(3);
          adjustmentType = RS.getString(5);
          adjustmentDescription = RS.getString(7);
          netAmount = RS.getBigDecimal(8,2);
          vatAmount = RS.getBigDecimal(9,2);
          grossAmount = RS.getBigDecimal(10,2);
          vatRate = RS.getBigDecimal(11,3);
          providerId = RS.getLong(19);
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
 public boolean getAdjustmentInvoiceLine()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Adjustment_Invoice_Line(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.setLong(2,adjustmentLineNo);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          adjustmentLineDescription = RS.getString(1);
          adjustmentLineAmount = RS.getBigDecimal(2,2);
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
 public boolean updateAdjustmentInvoiceAmounts()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Update_Adjustment_Invoice_Amounts(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.setString(2,userId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          netAmount = RS.getBigDecimal(1,2);
          vatAmount = RS.getBigDecimal(2,2);
          grossAmount = RS.getBigDecimal(3,2);
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
public boolean updateAdjustmentInvoice()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Adjustment_Invoice(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,invoiceNumber);
      cstmt.setString(2,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Adjustment Invoice</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment Invoice " + invoiceNumber +
              " updated</b></font>";
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
public boolean createAdjustmentInvoice()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Adjustment_Invoice(?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,latestCallMonth);
      cstmt.setLong(2,masterAccountId);
      cstmt.setString(3,adjustmentType);
      cstmt.setString(4,adjustmentDescription);
      cstmt.setBigDecimal(5,adjustmentAmount);
      cstmt.setBigDecimal(6,netAmount);
      cstmt.setBigDecimal(7,vatAmount);
      cstmt.setBigDecimal(8,grossAmount);
      cstmt.setBigDecimal(9,vatRate);
      cstmt.setString(10,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            Message = "<font color=red><b>Unable to create Adjustment Invoice</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment Invoice " + ret +
              " created</b></font>";
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
public boolean createAdjustmentInvoiceLine()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Adjustment_Invoice_Line(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,invoiceNumber);
      cstmt.setString(2,adjustmentLineDescription);
      cstmt.setBigDecimal(3,adjustmentLineAmount);
      cstmt.setString(4,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            Message = "<font color=red><b>Unable to create Adjustment Invoice Line</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment Invoice Line created</b></font>";
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
public boolean updateAdjustmentInvoiceLine()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Adjustment_Invoice_Line(?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,invoiceNumber);
      cstmt.setLong(2,adjustmentLineNo);
      cstmt.setString(3,adjustmentLineDescription);
      cstmt.setBigDecimal(4,adjustmentLineAmount);
      cstmt.setString(5,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            Message = "<font color=red><b>Unable to update Adjustment Invoice Line</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment Invoice Line updated</b></font>";
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
public boolean deleteAdjustmentInvoice()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Adjustment_Invoice(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,invoiceNumber);
      cstmt.setString(2,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Adjustment Invoice" +
              invoiceNumber + "</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Adjustment Invoice " + invoiceNumber +
              " deleted</b></font>";
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
/*******************************************************************************/
  public String populateAdjustmentLineList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Adjustment_Line_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);

	columns=2;

        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            rowcount++;
            rows = BigInteger.valueOf(rowcount);
            //Alternate row colours
            if (rows.testBit(0))
            {//An odd number
              gridClass="grid1 ";
            }
            else
            {
              gridClass="grid2 ";
            }
            String value = "";
            for(counter=1;counter<columns+1;counter++)
            {
              grid.append("<td class=");
              grid.append(gridClass);
              value = RS.getString(counter);
              grid.append(value);
              grid.append("</td>");
            }
            //Add the extra generated column for the buttons
            String invoiceNo = RS.getString(counter);
            long lineNo = RS.getLong(++counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<input class=listbutton type=button value=\"U\" " +
              "onClick=\"sendSelected('" + invoiceNo + "','" + lineNo + "','Update')\">");
            grid.append("<input class=listbutton type=button value=\"D\" " +
              "onClick=\"sendSelected('" + invoiceNo + "','" + lineNo + "','Delete')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Adjustment Lines</b></font>";
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
      if (Message.equals(""))
      {
        setAdjustmentLineList(grid.toString());
        return grid.toString();
      }
      else
      {
        setAdjustmentLineList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public boolean deleteAdjustmentInvoiceLine()
  {
    boolean ok = false;

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Delete_Adjustment_Invoice_Line(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.setLong(2,adjustmentLineNo);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            int ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to delete Adjustment Invoice Line</b></font>";
            }
            else
            {
              Message = "<font color=blue><b>Adjustment Invoice Line deleted</b></font>";
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
  public boolean canDeleteInvoiceLine()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Adjustment_Line_Count(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret <= 0)
          {
            Message = "<font color=red><b>Unable to determine Adjustment Line count</b></font>";
          }
          else if (ret == 1)
          {
            Message = "<font color=blue><b>This is the only Adjustment Line. It cannot be deleted.</b></font>";
          }
          else
          {
            ok = true;
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
      return ok;
    }
  }
}

