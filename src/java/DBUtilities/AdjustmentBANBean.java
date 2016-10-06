package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class AdjustmentBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String customer;
    private String invoiceRegion;
    private String invoiceNumber;
    private String adjustmentDescription;
    private String adjustmentCurrency;
    private BigDecimal adjustmentAmount;
    private String adjustmentCRDE;
    private String adjustmentType;
    private String revenueType;
    private String revenueDescription;
    private String netOrFull;
    private String rootCause;
    private String chargeEntity;
    private String purchaseOrderRef;
    private String invNoCredited;
    private int adjDay;
    private int adjMonth;
    private int adjYear;
    private java.sql.Date adjustmentDateh;
    private BigDecimal taxAmount;
    private String taxType;
    private String prevTaxType;
    private String taxTypeDescription;
    private String taxDescription;
    private double taxRate;
    private double prevTaxRate;
    private boolean hasPrevTaxRate;
    private String taxTypeDescriptionEDC;
    private String taxTypeEDC;
    private String taxDescriptionEDC;
    private BigDecimal taxAmountEDC;
    private String taxTypeDescriptionHEC;
    private String taxTypeHEC;
    private String taxDescriptionHEC;
    private BigDecimal taxAmountHEC;
    private double additionalTaxRate;
    private double additionalTaxRateEDC;
    private double additionalTaxRateHEC;
    private String sapTaxCode;
    private String prevSAPTaxCode;
    private String accountId;
    private String adjustmentList;
    private long adjustmentId;
    private BigDecimal invoiceAdjustmentTotal;
    private BigDecimal totalTaxAmount;
    private String reportRequired;

  public AdjustmentBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("adjustmentDescription");
    mandatory.addElement("adjustmentCurrency");
    mandatory.addElement("adjustmentAmount");
    mandatory.addElement("adjustmentCRDE");
    mandatory.addElement("revenueType");
    mandatory.addElement("chargeEntity");
    mandatory.addElement("adjustmentDateh");
    errored.addElement("");
    customer = "";
    invoiceRegion = "";
    invoiceNumber = "";
    adjustmentDescription = "";
    adjustmentCurrency = "";
    adjustmentAmount = new BigDecimal(new BigInteger("0"), 2);
    adjustmentCRDE = "";
    adjustmentType = "";
    revenueType = "";
    revenueDescription = "";
    netOrFull = "";
    rootCause = "";
    chargeEntity = "C&W";
    purchaseOrderRef = "";
    invNoCredited = "";
    adjDay = 0;
    adjMonth = 0;
    adjYear = 0;
    adjustmentDateh = new java.sql.Date(new java.util.Date().getTime());
    taxAmount = new BigDecimal(new BigInteger("0"), 2);
    taxType = "None";
    prevTaxType = "None";
    taxTypeDescription = "None";
    taxRate = 0;
    prevTaxRate = 0;
    hasPrevTaxRate = false;
    taxDescription = "None";
    additionalTaxRate = 0;
    sapTaxCode = "None";
    prevSAPTaxCode = "None";
    accountId = "";
    adjustmentList = "";
    adjustmentId = 0;
    invoiceAdjustmentTotal = new BigDecimal(new BigInteger("0"), 2);
    taxTypeDescriptionEDC = "None";
    taxTypeEDC = "None";
    taxAmountEDC = new BigDecimal(new BigInteger("0"), 2);
    taxDescriptionEDC = "None";
    taxTypeDescriptionHEC = "None";
    taxTypeHEC = "None";
    taxAmountHEC = new BigDecimal(new BigInteger("0"), 2);
    taxDescriptionHEC = "None";
    additionalTaxRateEDC = 0;
    additionalTaxRateHEC = 0;
    totalTaxAmount = new BigDecimal(new BigInteger("0"), 2);
    reportRequired = "N";
  }

  public void Reset()
  {
    super.Reset();
    customer = "";
    invoiceRegion = "";
    invoiceNumber = "";
    adjustmentDescription = "";
    adjustmentCurrency = "";
    adjustmentAmount = new BigDecimal(new BigInteger("0"), 2);
    adjustmentCRDE = "";
    adjustmentType = "";
    revenueType = "";
    revenueDescription = "";
    netOrFull = "";
    rootCause = "";
    chargeEntity = "C&W";
    purchaseOrderRef = "";
    invNoCredited = "";
    adjDay = 0;
    adjMonth = 0;
    adjYear = 0;
    setErrored("clear");
    adjustmentDateh = new java.sql.Date(new java.util.Date().getTime());
    taxAmount = new BigDecimal(new BigInteger("0"), 2);
    taxType = "None";
    prevTaxType = "None";
    taxTypeDescription = "None";
    taxRate = 0;
    prevTaxRate = 0;
    hasPrevTaxRate = false;
    taxDescription = "None";
    additionalTaxRate = 0;
    sapTaxCode = "None";
    prevSAPTaxCode = "None";
    accountId = "";
    adjustmentList = "";
    adjustmentId = 0;
    invoiceAdjustmentTotal = new BigDecimal(new BigInteger("0"), 2);
    taxTypeDescriptionEDC = "None";
    taxAmountEDC = new BigDecimal(new BigInteger("0"), 2);
    taxDescriptionEDC = "None";
    taxTypeDescriptionHEC = "None";
    taxAmountHEC = new BigDecimal(new BigInteger("0"), 2);
    taxDescriptionHEC = "None";
    additionalTaxRateEDC = 0;
    additionalTaxRateHEC = 0;
    totalTaxAmount = new BigDecimal(new BigInteger("0"), 2);
    taxTypeEDC = "None";
    taxTypeHEC = "None";
    reportRequired = "N";
  }
/*
int[] adjItems = setDateItems(adjustmentDateh);
adjDay = adjItems[0];
adjMonth = adjItems[1];
adjYear = adjItems[2];
*/
/*set Methods*/
  public void setReportRequired(String value)
  {
    reportRequired = SU.hasNoValue(value,"N");
  }
  public void setAdjustmentId(long value)
  {
    adjustmentId = value;
  }
  public void setAdjustmentList(String value)
  {
    adjustmentList = SU.isNull(value,"");
  }
  public void setAccountId(String value)
  {
    accountId = SU.isNull(value,"");
  }
  public void setCustomer(String value)
  {
    customer = SU.isNull(value,"");
  }
  public void setInvoiceRegion(String value)
  {
    invoiceRegion = SU.isNull(value,"");
  }
  public void setInvoiceNumber(String value)
  {
    invoiceNumber = SU.isNull(value,"");
  }
  public void setAdjustmentDescription(String value)
  {
    adjustmentDescription = SU.removeChar(SU.isNull(value,""), '\'');
    ScreenData.put("adjustmentDescription",value);
  }
  public void setAdjustmentCurrency(String value)
  {
    ScreenData.put("adjustmentCurrency",value);
    adjustmentCurrency = SU.isNull(value,"");
  }
  public void setAdjustmentAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      adjustmentAmount = new BigDecimal(SU.hasNoValue(tidy, "0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
      setAdjustmentAmountSign();
      ScreenData.put("adjustmentAmount",adjustmentAmount.toString());
      //setAdjustmentAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("adjustmentAmount","0.00");
      adjustmentAmount = new BigDecimal(new BigInteger("0"), 2);
    }
  }
/*  public void setAdjustmentAmount(double value)
  {
    //double dd2dec = new Double(df2.format(SU.roundUp(value,2))).doubleValue();
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("adjustmentAmount",SU.roundToString(dd2dec,2));
    adjustmentAmount = dd2dec;
    setAdjustmentAmountSign();
  }
  private void setAdjustmentAmountSign()
  {
    if (SU.isNull(adjustmentCRDE,"").equals("Credit"))
    {
      if (adjustmentAmount > 0)
      {
        adjustmentAmount *= -1;
      }
    }
    else
    {
      if (adjustmentAmount < 0)
      {
        adjustmentAmount *= -1;
      }
    }
  }*/
  private void setAdjustmentAmountSign()
  {
    if (SU.isNull(adjustmentCRDE,"").equals("Credit"))
    {
      if (adjustmentAmount.doubleValue() > 0)
      {
        adjustmentAmount = adjustmentAmount.multiply(new BigDecimal("-1"));
      }
    }
    else
    {
      if (adjustmentAmount.doubleValue() < 0)
      {
        adjustmentAmount = adjustmentAmount.multiply(new BigDecimal("-1"));
      }
    }
  }
  /*public void setAdjustmentCRDE(String value)
  {
    ScreenData.put("adjustmentCRDE",value);
    if (!adjustmentCRDE.equals(value))
    {
      if (SU.isNull(value,"").equals("Credit"))
      {
        mandatory.addElement("rootCause");
        mandatory.addElement("revenueDescription");
        mandatory.addElement("netOrFull");
      }
      else
      {
        mandatory.removeElement("rootCause");
        mandatory.removeElement("revenueDescription");
        mandatory.removeElement("netOrFull");
      }
    }
    adjustmentCRDE = SU.isNull(value,"");
    setAdjustmentAmountSign();
  }*/
  public void setAdjustmentCRDE(String value)
  {
    ScreenData.put("adjustmentCRDE",value);
    adjustmentCRDE = SU.isNull(value,"");
    if (value.equals("Credit"))
    {
      if (!mandatory.contains("rootCause")) mandatory.addElement("rootCause");
      if (!mandatory.contains("revenueDescription")) mandatory.addElement("revenueDescription");
      if (!mandatory.contains("netOrFull")) mandatory.addElement("netOrFull");
    }
    else
    {
      if (mandatory.contains("rootCause")) mandatory.removeElement("rootCause");
      if (mandatory.contains("revenueDescription")) mandatory.removeElement("revenueDescription");
      if (mandatory.contains("netOrFull")) mandatory.removeElement("netOrFull");
    }
    setAdjustmentAmountSign();
  }
  public void setAdjustmentType(String value)
  {
    ScreenData.put("adjustmentType",value);
    adjustmentType = SU.isNull(value,"");
  }
  public void setRevenueType(String value)
  {
    ScreenData.put("revenueType",value);
    revenueType = SU.isNull(value,"");
  }
  public void setRevenueDescription(String value)
  {
    ScreenData.put("revenueDescription",value);
    revenueDescription = SU.isNull(value,"");
  }
  public void setNetOrFull(String value)
  {
    ScreenData.put("netOrFull",value);
    netOrFull = SU.isNull(value,"");
  }
  public void setRootCause(String value)
  {
    ScreenData.put("rootCause",value);
    rootCause = SU.isNull(value,"");
  }
  public void setChargeEntity(String value)
  {
    ScreenData.put("chargeEntity",value);
    chargeEntity = SU.isNull(value,"");
  }
  public void setPurchaseOrderRef(String value)
  {
    ScreenData.put("purchaseOrderRef",value);
    purchaseOrderRef = SU.isNull(value,"");
  }
  public void setInvNoCredited(String value)
  {
    ScreenData.put("invNoCredited",value);
    invNoCredited = SU.isNull(value,"");
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
    if (name.equals("customer"))
    {
      setCustomer(value);
    }
    else if (name.equals("invoiceRegion"))
    {
      setInvoiceRegion(value);
    }
    else if (name.equals("invoiceNumber"))
    {
      setInvoiceNumber(value);
    }
    else if (name.equals("adjustmentDescription"))
    {
      setAdjustmentDescription(value);
    }
    else if (name.equals("adjustmentCurrency"))
    {
      setAdjustmentCurrency(value);
    }
    else if (name.equals("adjustmentAmount"))
    {
      setAdjustmentAmount(value);
    }
    else if (name.equals("adjustmentCRDE"))
    {
      setAdjustmentCRDE(value);
    }
    else if (name.equals("adjustmentType"))
    {
      setAdjustmentType(value);
    }
    else if (name.equals("revenueType"))
    {
      setRevenueType(value);
    }
    else if (name.equals("revenueDescription"))
    {
      setRevenueDescription(value);
    }
    else if (name.equals("netOrFull"))
    {
      setNetOrFull(value);
    }
    else if (name.equals("rootCause"))
    {
      setRootCause(value);
    }
    else if (name.equals("chargeEntity"))
    {
      setChargeEntity(value);
    }
    else if (name.equals("purchaseOrderRef"))
    {
      setPurchaseOrderRef(value);
    }
    else if (name.equals("invNoCredited"))
    {
      setInvNoCredited(value);
    }
    else if (name.equals("adjustmentDateDay"))
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
    else if (name.equals("reportRequired"))
    {
      setReportRequired(value);
    }
}
/*******************************************************************************/
/*get Methods*/
  public String getReportRequired()
  {
    return reportRequired;
  }
  public long getAdjustmentId()
  {
    return adjustmentId;
  }
  public String getAdjustmentList()
  {
    return adjustmentList;
  }
  public String getAccountId()
  {
    return accountId;
  }
  public String getCustomer()
  {
    return customer;
  }
  public String getInvoiceRegion()
  {
    return invoiceRegion;
  }
  public String getInvoiceNumber()
  {
    return invoiceNumber;
  }
  public String getAdjustmentDescription()
  {
    return adjustmentDescription;
  }
  public String getAdjustmentCurrency()
  {
    return adjustmentCurrency;
  }
  public BigDecimal getAdjustmentAmount()
  {
    return adjustmentAmount;
  }
  public BigDecimal getTaxAmount()
  {
    return taxAmount;
  }
  public BigDecimal getTotalTaxAmount()
  {
    return totalTaxAmount;
  }
  public BigDecimal getInvoiceAdjustmentTotal()
  {
    return invoiceAdjustmentTotal;
  }
  public String getAdjustmentCRDE()
  {
    return adjustmentCRDE;
  }
  public String getAdjustmentType()
  {
    return adjustmentType;
  }
  public String getTaxType()
  {
    return taxType;
  }
  public String getTaxTypeDescription()
  {
    return taxTypeDescription;
  }
  public String getRevenueType()
  {
    return revenueType;
  }
  public String getRevenueDescription()
  {
    return revenueDescription;
  }
  public String getNetOrFull()
  {
    return netOrFull;
  }
  public String getRootCause()
  {
    return rootCause;
  }
  public String getChargeEntity()
  {
    return chargeEntity;
  }
  public String getPurchaseOrderRef()
  {
    return purchaseOrderRef;
  }
  public String getInvNoCredited()
  {
    return invNoCredited;
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
  public String getInputMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else if (FieldName.equals("netOrFull"))
    {
      if (adjustmentCRDE.equals("Debit"))
      {
        return "DISABLED";
      }
      else
      {
        return super.getInputMode();
      }
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

    /*if ((Mode.compareTo("Amend")==0) && (FieldName.compareTo("Site_Reference")== 0))
    {
      return "READONLY";
    }
    if (action.compareTo("Authorise") == 0)
    {
      return "READONLY";
    }
    if ((FieldName.compareTo("BAN_Summary") ==0) ||
	    (FieldName.compareTo("BAN_Reason") ==0) ||
	    (FieldName.compareTo("Effective_Date") ==0))

    {
      if ((action.compareTo("View") !=0 ) &&
	  (action.compareTo("History") !=0 ) &&
	  (action.compareTo("Authorise") !=0 ))
	  return "";
      else
        return InputMode;
    }
    else
    {
      return InputMode;
    }*/
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
/******************************************************************************/
public boolean calculateTax()
{
  totalTaxAmount = new BigDecimal(new BigInteger("0"), 2);
  taxAmount = new BigDecimal(new BigInteger("0"), 2);
  taxAmountEDC = new BigDecimal(new BigInteger("0"), 2);
  taxAmountHEC = new BigDecimal(new BigInteger("0"), 2);
  /*if (chargeEntity.equalsIgnoreCase("carrier"))
  {
    //no tax calculation
    return true;
  }
  else
  {*/
    boolean ok = false;
    try
    {
      SQL = "select distinct td.Tax_Type + ' ' + td.Tax_Description  Tax_Type_Desc, " +
      "td.Tax_Description, td.Tax_type, tr.Tax_Rate, tr.Additional_Tax_Rate, " +
      "tr.sap_tax_code " +
      "from givn_ref..tax_requirement tr (nolock), givn_ref..tax_description td (nolock) " +
      "where tr.global_customer_Id = '" + customer + "' " +
      "and tr.Invoice_Region = '" + invoiceRegion + "' " +
      "and tr.Start_Date <= '" + SU.reformatDate(adjustmentDateh.toString()) + "' " +
      "and (tr.End_Date >= '" + SU.reformatDate(adjustmentDateh.toString()) + "' " +
      "or tr.End_Date is null) " +
      "and td.Tax_Type = tr.Tax_Type ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        BigDecimal tTA = new BigDecimal(new BigInteger("0"), 2);
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          taxTypeDescription = RS.getString(1);
          taxDescription = RS.getString(2);
          taxType = RS.getString(3);
          taxRate = RS.getDouble(4);
          additionalTaxRate = RS.getDouble(5);
          sapTaxCode = RS.getString(6);
          if ((hasPrevTaxRate) && (prevTaxType.equals(taxType)) &&
            (prevTaxRate != taxRate) && (prevTaxRate != -1))
          {
            taxRate = prevTaxRate;
            sapTaxCode = prevSAPTaxCode;
          }
          BigDecimal tA;
          if (taxType.equalsIgnoreCase("WHD"))
          {
            //tA = (adjustmentAmount.doubleValue()/(1 - taxRate)) - adjustmentAmount.doubleValue();
            //taxAmount = SU.roundUp(tA,2);
            tA = adjustmentAmount.divide(new BigDecimal(1 - taxRate), BigDecimal.ROUND_HALF_UP);
            taxAmount = tA.subtract(adjustmentAmount);
          }
          else if (taxType.equalsIgnoreCase("EDC"))
          {
            /*tA = adjustmentAmount.doubleValue() * taxRate * additionalTaxRate;
            taxAmountEDC = SU.roundUp(tA,2);*/
            tA = adjustmentAmount.multiply(new BigDecimal(taxRate * additionalTaxRate));
            taxAmountEDC = tA.setScale(2, BigDecimal.ROUND_HALF_UP);
            taxTypeDescriptionEDC = taxTypeDescription;
            taxTypeEDC = taxType;
            taxDescriptionEDC = taxDescription;
            additionalTaxRateEDC = additionalTaxRate;

          }
          else if (taxType.equalsIgnoreCase("HEC"))
          {
            /*tA = adjustmentAmount.doubleValue() * taxRate * additionalTaxRate;
            taxAmountHEC = SU.roundUp(tA,2);*/
            tA = adjustmentAmount.multiply(new BigDecimal(taxRate * additionalTaxRate));
            taxAmountHEC = tA.setScale(2, BigDecimal.ROUND_HALF_UP);
            taxTypeDescriptionHEC = taxTypeDescription;
            taxTypeHEC = taxType;
            taxDescriptionHEC = taxDescription;
            additionalTaxRateHEC = additionalTaxRate;
          }
          else
          {
            /*tA = adjustmentAmount.doubleValue() * taxRate;
            taxAmount = SU.roundUp(tA,2);*/
            tA = adjustmentAmount.multiply(new BigDecimal(taxRate));
            taxAmount = tA.setScale(2, BigDecimal.ROUND_HALF_UP);
          }
          tTA = tTA.add(tA);
        }
        totalTaxAmount = tTA.setScale(2, BigDecimal.ROUND_HALF_UP);
        ok = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return ok;
    }
  //}
}
/*****************************************************************************/
  public boolean deleteAdjustment()
  {
    boolean success = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Delete_Adjustment_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,userId);
        cstmt.setLong(2,adjustmentId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          if (RS.getInt(1) == 0)
          {
            success = true;
            Message = "<font color=blue><b>Adjustment deleted from invoice " +
              invoiceNumber +"</b></font>";
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
/*******************************************************************************/
 public boolean getAdjustment(boolean archived)
 {
    boolean getAdjustment = false;
    totalTaxAmount = new BigDecimal(new BigInteger("0"), 2);
    taxAmount = new BigDecimal(new BigInteger("0"), 2);
    taxAmountEDC = new BigDecimal(new BigInteger("0"), 2);
    taxAmountHEC = new BigDecimal(new BigInteger("0"), 2);
//StringBuffer debug = new StringBuffer("a");
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..get_Adjustment_" + (archived?"Archive_":"") + "eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,adjustmentId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          getAdjustment = true;
          reportRequired = archived?"N":RS.getString(20);
          adjustmentCRDE = RS.getString(19);
          adjustmentDateh = RS.getDate(1);
          adjustmentCurrency = RS.getString(2);
          //getBigDecimal(int, int) causes deprecation warnings, but it's the only one that works here. TA - 25/02/2010
          adjustmentAmount = RS.getBigDecimal(3, 2);
          adjustmentType = RS.getString(4);
          adjustmentDescription = RS.getString(5);
          netOrFull = RS.getString(6);
          rootCause = RS.getString(7);
          revenueType = RS.getString(8);
          revenueDescription = RS.getString(9);
          chargeEntity = RS.getString(10);
          purchaseOrderRef = RS.getString(11);
          invNoCredited = RS.getString(12);
          String thisTaxType = SU.hasNoValue(RS.getString(13), "None");
          taxRate = RS.getDouble(14);
//debug.append("b");
          if (thisTaxType.equals("EDC"))
          {
//debug.append("c");
            //getBigDecimal(int, int) causes deprecation warnings, but it's the only one that works here. TA - 25/02/2010
            taxAmountEDC = RS.getBigDecimal(15, 2);
            taxTypeEDC = thisTaxType;
            taxDescriptionEDC = SU.hasNoValue(RS.getString(16), "None");
            taxTypeDescriptionEDC = taxTypeEDC.equals("None")?"None":
              (taxTypeEDC + " " + taxDescriptionEDC);
            additionalTaxRateEDC = RS.getDouble(17);
//debug.append("d");
          }
          else if (thisTaxType.equals("HEC"))
          {
//debug.append("e");
            //getBigDecimal(int, int) causes deprecation warnings, but it's the only one that works here. TA - 25/02/2010
            taxAmountHEC = RS.getBigDecimal(15, 2);
            taxTypeHEC = thisTaxType;
            taxDescriptionHEC = SU.hasNoValue(RS.getString(16), "None");
            taxTypeDescriptionHEC = taxTypeHEC.equals("None")?"None":
              (taxTypeHEC + " " + taxDescriptionHEC);
            additionalTaxRateHEC = RS.getDouble(17);
//debug.append("f");
          }
          else
          {
//debug.append("g");
            //getBigDecimal(int, int) causes deprecation warnings, but it's the only one that works here. TA - 25/02/2010
            taxAmount = RS.getBigDecimal(15, 2);
            taxType = thisTaxType;
            taxDescription = SU.hasNoValue(RS.getString(16), "None");
            taxTypeDescription = taxType.equals("None")?"None":
              (taxType + " " + taxDescription);
            additionalTaxRate = RS.getDouble(17);
//debug.append("h");
          }
//debug.append("i");
          sapTaxCode = SU.hasNoValue(RS.getString(18), "None");
        }
//debug.append("j");
        totalTaxAmount = taxAmount.add(taxAmountHEC.add(taxAmountEDC)).setScale(2, BigDecimal.ROUND_HALF_UP);
        setDates();
//debug.append("k");
        //adjustmentCRDE = (adjustmentAmount.doubleValue() < 0)?"Credit":"Debit";
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
/*    catch(Exception ex)
    {
      Message=debug.toString();
    }
*/  finally
    {
      close();
      return getAdjustment;
    }
 }
/*******************************************************************************/
public int createAdjustment()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      //java.util.Calendar cal = java.util.Calendar.getInstance();
      //cal.set(year, month, day);
      SQL = "{call gcd..Create_Adjustment_eBAN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,customer);
      cstmt.setString(2,invoiceNumber);
      cstmt.setDate(3,adjustmentDateh);
      cstmt.setString(4,adjustmentCurrency);
      cstmt.setBigDecimal(5,adjustmentAmount);
      cstmt.setString(6,adjustmentType);
      cstmt.setString(7,adjustmentDescription);
      cstmt.setString(8,"");
      cstmt.setString(9,userId);
      cstmt.setString(10,"");
      cstmt.setString(11,invoiceRegion);
      cstmt.setString(12,"GM Billing");
      cstmt.setString(13,SU.hasNoValue(netOrFull, "Z"));
      cstmt.setString(14,SU.hasNoValue(rootCause, "Z"));
      cstmt.setString(15,revenueType);
      cstmt.setString(16,SU.hasNoValue(revenueDescription, "Z"));
      cstmt.setString(17,chargeEntity);
      cstmt.setString(18,accountId);
      cstmt.setString(19,purchaseOrderRef);
      cstmt.setString(20,invNoCredited);
      cstmt.setBigDecimal(21,taxAmount);
      cstmt.setString(22,taxType);
      cstmt.setDouble(23,taxRate);
      cstmt.setString(24,taxDescription);
      cstmt.setDouble(25,additionalTaxRate);
      cstmt.setString(26,sapTaxCode);
      cstmt.setString(27,taxTypeEDC);
      cstmt.setBigDecimal(28,taxAmountEDC);
      cstmt.setString(29,taxDescriptionEDC);
      cstmt.setDouble(30,additionalTaxRateEDC);
      cstmt.setString(31,taxTypeHEC);
      cstmt.setBigDecimal(32,taxAmountHEC);
      cstmt.setString(33,taxDescriptionHEC);
      cstmt.setDouble(34,additionalTaxRateHEC);
      cstmt.setString(35,reportRequired);
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
            Message = "<font color=blue><b>New adjustment created for invoice " +
              invoiceNumber +"</b></font>";
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
public int updateAdjustment()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      //java.util.Calendar cal = java.util.Calendar.getInstance();
      //cal.set(year, month, day);
      SQL = "{call gcd..Update_Adjustment_eBAN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,customer);
      cstmt.setString(2,invoiceNumber);
      cstmt.setDate(3,adjustmentDateh);
      cstmt.setString(4,adjustmentCurrency);
      cstmt.setDouble(5,adjustmentAmount.doubleValue());
      cstmt.setString(6,adjustmentType);
      cstmt.setString(7,adjustmentDescription);
      cstmt.setString(8,"");
      cstmt.setString(9,userId);
      cstmt.setString(10,"");
      cstmt.setString(11,invoiceRegion);
      cstmt.setString(12,"GM Billing");
      cstmt.setString(13,SU.hasNoValue(netOrFull, "Z"));
      cstmt.setString(14,SU.hasNoValue(rootCause, "Z"));
      cstmt.setString(15,revenueType);
      cstmt.setString(16,SU.hasNoValue(revenueDescription, "Z"));
      cstmt.setString(17,chargeEntity);
      cstmt.setString(18,accountId);
      cstmt.setString(19,purchaseOrderRef);
      cstmt.setString(20,invNoCredited);
      cstmt.setBigDecimal(21,taxAmount);
      cstmt.setString(22,taxType);
      cstmt.setDouble(23,taxRate);
      cstmt.setString(24,taxDescription);
      cstmt.setDouble(25,additionalTaxRate);
      cstmt.setString(26,sapTaxCode);
      cstmt.setLong(27,adjustmentId);
      cstmt.setString(28,taxTypeEDC);
      cstmt.setBigDecimal(29,taxAmountEDC);
      cstmt.setString(30,taxDescriptionEDC);
      cstmt.setDouble(31,additionalTaxRateEDC);
      cstmt.setString(32,taxTypeHEC);
      cstmt.setBigDecimal(33,taxAmountHEC);
      cstmt.setString(34,taxDescriptionHEC);
      cstmt.setDouble(35,additionalTaxRateHEC);
      cstmt.setString(36,reportRequired);
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
            Message = "<font color=blue><b>Adjustment updated for invoice " +
              invoiceNumber +"</b></font>";
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
      else if (FieldName.equals("adjustmentAmount"))
      {
        try
        {
          double temp = Double.parseDouble(FormField);
          /*if (temp == 0)
          {
            setErrored("adjustmentAmount");
            Message = "<font color=red><b>Adjustment Amount cannot be zero";
            return false;
          }*/
        }
        catch (java.lang.NumberFormatException NE)
        {
          setErrored("adjustmentAmount");
          Message = "<font color=red><b>Adjustment Amount must be numeric";
          return false;
        }
      }
      else if (FieldName.equals("adjustmentCRDE"))
      {
        if (FormField.equals("Credit"))
        {
          if (!adjustmentType.equals("1"))
          {
            setErrored("adjustmentCRDE");
            setErrored("adjustmentType");
            Message = "<font color=red><b>Adjustment Type must be 'Credit Adjustment' if Credit/Debit is 'Credit' ";
            return false;
          }
        }
        else
        {
          if (adjustmentType.equals("1"))
          {
            setErrored("adjustmentCRDE");
            setErrored("adjustmentType");
            Message = "<font color=red><b>Adjustment Type cannot be 'Credit Adjustment' if Credit/Debit is 'Debit' ";
            return false;
          }
        }
      }
    }
  }
  if ((!errored.isEmpty()) && (errored.size() > 0))
  {
    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
    return false;
  }
  else if (SU.hasNoValue(invNoCredited))
  {
    hasPrevTaxRate = false;
  }
  else
  {
    hasPrevTaxRate = findPrevTaxRate();
    /*if (!hasPrevTaxRate)
    {
      setErrored("invNoCredited");
      Message = "<font color=red><b>Unable to find a record for this invoice number";
      return false;
    }*/
  }
  return true;
}
/*******************************************************************************/
  private boolean findPrevTaxRate()
  {
    boolean found = false;
    try
    {
      SQL = "select distinct isnull(tax_rate, -1), isnull(tax_type, 'None'), " +
      "isnull(sap_tax_code, 'None') " +
      "from gcd..adjustments_tax_archive (nolock) " +
      "where invoice_no = '" + invNoCredited + "' " +
      "union " +
      "select distinct tax_rate, tax_type, sap_tax_code " +
      "from gcd..tax_payable_archive (nolock) " +
      "where invoice_no = '" + invNoCredited + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          found = true;
          prevTaxRate = RS.getDouble(1);
          prevTaxType = RS.getString(2);
          prevSAPTaxCode = RS.getString(3);
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
      return found;
    }
  }
/*******************************************************************************/
  public String populateAdjustmentList(boolean invoiceClosed)
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    invoiceAdjustmentTotal = new BigDecimal(new BigInteger("0"), 2);

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call gcd..Get_Adjustment_List_eBAN(?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,customer);
        cstmt.setString(2,invoiceRegion);
        cstmt.setString(3,invoiceNumber);

	columns=4;

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
              if (counter == 3)
              {//new BigDecimal(new BigInteger("0"), 2);
                //invoiceAdjustmentTotal += Double.parseDouble(value.substring(value.indexOf(">")+1));
                invoiceAdjustmentTotal =
                  invoiceAdjustmentTotal.add(new BigDecimal(Double.parseDouble(value.substring(value.indexOf(">")+1)))).setScale(2, BigDecimal.ROUND_HALF_UP);
              }
            }
            //Add the extra generated column for the buttons
            String thisAdjId = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            String archived = value.substring(value.indexOf(">")+1).equals("Billed")?"true":"false";
            /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisAdjId + "','AdjustmentView','" + archived + "')\">");*/
            grid.append("<input class=listbutton type=button value=\"V\" " +
              "onClick=\"sendSelected('" + thisAdjId + "','AdjustmentView','" +
              archived + "')\">");
            if ((invoiceClosed) || (archived.equals("true")))
            {
              grid.append("<input class=listbutton type=button value=\"\">");
              grid.append("<input class=listbutton type=button value=\"\">");
              /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22>");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22>");*/
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\"A\" " +
                "onClick=\"sendSelected('" + thisAdjId + "','AdjustmentAmend','" +
                archived + "')\">");
              grid.append("<input class=listbutton type=button value=\"D\" " +
                "onClick=\"sendSelected('" + thisAdjId + "','AdjustmentDelete','" +
                archived + "')\">");
              /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonA.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisAdjId + "','AdjustmentAmend','" + archived + "')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisAdjId + "','AdjustmentDelete','" + archived + "')\">");*/
            }
            if (invoiceClosed)
            {
              grid.append("<input class=listbutton type=button value=\"\">");
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\"I\" " +
                "onClick=\"sendSelected('" + thisAdjId + "','AdjustmentInvoice','" +
                archived + "','" + invoiceNumber + "','" + 
                RS.getString(++counter) + "','" + invoiceRegion + "','" + 
                RS.getString(++counter) + "')\">");
            }
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no adjustments for this invoice</b></font>";
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
        setAdjustmentList(grid.toString());
        return grid.toString();
      }
      else
      {
        setAdjustmentList("");
        return Message;
      }
    }
  }
}

