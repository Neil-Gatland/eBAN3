package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class ConglomManualInvoiceBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long conglomCustomerId;
    private long invoiceId;
    private String billedProduct;
    private int bDay;
    private int bMonth;
    private int bYear;
    private String billPeriodRef;
    private String sourceAccountNo;
    private String checkDigit;
    private String sourceInvoiceNo;
    private java.sql.Date billDateh;
    private java.sql.Date monthlyBillStartDate;
    private String billMonth;
    private String quarterlyBillCode;
    private double oneOffCharges;
    private double recurringCharges;
    private double usageCharges;
    private double miscCharges;
    private double sourceDiscTotal;
    private double invoiceNetAmount;
    private double invoiceVATAmount;
    private double invoiceTotalAmount;
    private double installCharges;
    private double rentalCharges;
    private double callinkCharges;
    private double vpnCharges;
    private double callCharges;
    private double authCodeCharges;
    private double easyUsageCharges;
    private double easyQtrlyCharges;
    private double sundryCharges;
    private double specialCharges;
    private double conglomDiscountNetAmount;
    private String feedSource;
    private String sourceConglomId;
    private String sourceSystemId;
    private boolean overrideVAT;
    private double vatRate;

  public ConglomManualInvoiceBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("sourceAccountNo");
    mandatory.addElement("conglomBilledProduct3");
    mandatory.addElement("sourceInvoiceNo");
    mandatory.addElement("billDateh");
    errored.clear();
    errored.addElement("");
    invoiceId = 0;
    bDay = 0;
    bMonth = 0;
    bYear = 0;
    billedProduct = "";
    billPeriodRef = "";
    sourceAccountNo = "";
    checkDigit = "";
    sourceInvoiceNo = "";
    billDateh = new java.sql.Date(new java.util.Date().getTime());
    billMonth = "";
    oneOffCharges = 0;
    recurringCharges = 0;
    usageCharges = 0;
    miscCharges = 0;
    sourceDiscTotal = 0;
    invoiceNetAmount = 0;
    invoiceVATAmount = 0;
    invoiceTotalAmount = 0;
    installCharges = 0;
    rentalCharges = 0;
    callinkCharges = 0;
    vpnCharges = 0;
    callCharges = 0;
    authCodeCharges = 0;
    easyUsageCharges = 0;
    easyQtrlyCharges = 0;
    sundryCharges = 0;
    specialCharges = 0;
    conglomDiscountNetAmount = 0;
    feedSource = "";
    sourceConglomId = "";
    sourceSystemId = "";
    overrideVAT = false;
    findVATRate();
    //Mode = "";
  }

  public void Reset()
  {
    super.Reset();
    invoiceId = 0;
    bDay = 0;
    bMonth = 0;
    bYear = 0;
    billedProduct = "";
    billPeriodRef = "";
    sourceAccountNo = "";
    checkDigit = "";
    sourceInvoiceNo = "";
    billDateh = new java.sql.Date(new java.util.Date().getTime());
    billMonth = "";
    oneOffCharges = 0;
    recurringCharges = 0;
    usageCharges = 0;
    miscCharges = 0;
    sourceDiscTotal = 0;
    invoiceNetAmount = 0;
    invoiceVATAmount = 0;
    invoiceTotalAmount = 0;
    installCharges = 0;
    rentalCharges = 0;
    callinkCharges = 0;
    vpnCharges = 0;
    callCharges = 0;
    authCodeCharges = 0;
    easyUsageCharges = 0;
    easyQtrlyCharges = 0;
    sundryCharges = 0;
    specialCharges = 0;
    conglomDiscountNetAmount = 0;
    errored.clear();
    feedSource = "";
    sourceConglomId = "";
    sourceSystemId = "";
    overrideVAT = false;
    findVATRate();
    //Mode = "";
  }
/*set Methods*/
  public void setOverrideVAT(String value)
  {
    overrideVAT = value != null;
  }
  public void setConglomDiscountNetAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setConglomDiscountNetAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("conglomDiscountNetAmount",null);
    }
  }
  public void setConglomDiscountNetAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("conglomDiscountNetAmount",SU.roundToString(dd2dec,2));
    conglomDiscountNetAmount = dd2dec;
  }
  public void setSpecialCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setSpecialCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("specialCharges",null);
    }
  }
  public void setSpecialCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("specialCharges",SU.roundToString(dd2dec,2));
    specialCharges = dd2dec;
  }
  public void setSundryCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setSundryCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("sundryCharges",null);
    }
  }
  public void setSundryCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("sundryCharges",SU.roundToString(dd2dec,2));
    sundryCharges = dd2dec;
  }
  public void setEasyQtrlyCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setEasyQtrlyCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("easyQtrlyCharges",null);
    }
  }
  public void setEasyQtrlyCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("easyQtrlyCharges",SU.roundToString(dd2dec,2));
    easyQtrlyCharges = dd2dec;
  }
  public void setEasyUsageCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setEasyUsageCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("easyUsageCharges",null);
    }
  }
  public void setEasyUsageCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("easyUsageCharges",SU.roundToString(dd2dec,2));
    easyUsageCharges = dd2dec;
  }
  public void setAuthCodeCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setAuthCodeCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("authCodeCharges",null);
    }
  }
  public void setAuthCodeCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("authCodeCharges",SU.roundToString(dd2dec,2));
    authCodeCharges = dd2dec;
  }
  public void setCallCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setCallCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("callCharges",null);
    }
  }
  public void setCallCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("callCharges",SU.roundToString(dd2dec,2));
    callCharges = dd2dec;
  }
  public void setVPNCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setVPNCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("vpnCharges",null);
    }
  }
  public void setVPNCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("vpnCharges",SU.roundToString(dd2dec,2));
    vpnCharges = dd2dec;
  }
  public void setCallinkCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setCallinkCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("callinkCharges",null);
    }
  }
  public void setCallinkCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("callinkCharges",SU.roundToString(dd2dec,2));
    callinkCharges = dd2dec;
  }
  public void setRentalCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setRentalCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("rentalCharges",null);
    }
  }
  public void setRentalCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("rentalCharges",SU.roundToString(dd2dec,2));
    rentalCharges = dd2dec;
  }
  public void setInvoiceTotalAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setInvoiceTotalAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("invoiceTotalAmount",null);
    }
  }
  public void setInvoiceTotalAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("invoiceTotalAmount",SU.roundToString(dd2dec,2));
    invoiceTotalAmount = dd2dec;
  }
  public void setInvoiceVATAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setInvoiceVATAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("invoiceVATAmount",null);
    }
  }
  public void setInvoiceVATAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("invoiceVATAmount",SU.roundToString(dd2dec,2));
    invoiceVATAmount = dd2dec;
  }
  public void setInvoiceNetAmount(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setInvoiceNetAmount(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("invoiceNetAmount",null);
    }
  }
  public void setInvoiceNetAmount(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("invoiceNetAmount",SU.roundToString(dd2dec,2));
    invoiceNetAmount = dd2dec;
  }
  public void setSourceDiscTotal(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setSourceDiscTotal(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("sourceDiscTotal",null);
    }
  }
  public void setSourceDiscTotal(double value)
  {
    if (value > 0)
    {
      value *= -1;
    }
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("sourceDiscTotal",SU.roundToString(dd2dec,2));
    sourceDiscTotal = dd2dec;
  }
  public void setMiscCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setMiscCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("miscCharges",null);
    }
  }
  public void setMiscCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("miscCharges",SU.roundToString(dd2dec,2));
    miscCharges = dd2dec;
  }
  public void setUsageCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setUsageCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("usageCharges",null);
    }
  }
  public void setUsageCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("usageCharges",SU.roundToString(dd2dec,2));
    usageCharges = dd2dec;
  }
  public void setRecurringCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setRecurringCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("recurringCharges",null);
    }
  }
  public void setRecurringCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("recurringCharges",SU.roundToString(dd2dec,2));
    recurringCharges = dd2dec;
  }
  public void setOneOffCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setOneOffCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("oneOffCharges",null);
    }
  }
  public void setOneOffCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("oneOffCharges",SU.roundToString(dd2dec,2));
    oneOffCharges = dd2dec;
  }
  public void setInstallCharges(String value)
  {
    String tidy = SU.removeUnfriendlyChars(value, false);
    try
    {
      setInstallCharges(Double.parseDouble(SU.hasNoValue(tidy, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("installCharges",null);
    }
  }
  public void setInstallCharges(double value)
  {
    double dd2dec = SU.roundUp(value,2);
    ScreenData.put("installCharges",SU.roundToString(dd2dec,2));
    installCharges = dd2dec;
  }
  public void setBillMonth(String value)
  {
    ScreenData.put("billMonth",value);
    billMonth = SU.isNull(value,"");
  }
  public void setSourceInvoiceNo(String value)
  {
    ScreenData.put("sourceInvoiceNo",value);
    sourceInvoiceNo = SU.isNull(value,"");
  }
  public void setCheckDigit(String value)
  {
    ScreenData.put("checkDigit",value);
    checkDigit = value;
  }
  public void setSourceAccountNo(String value)
  {
    ScreenData.put("sourceAccountNo",value);
    sourceAccountNo = SU.isNull(value,"");
  }
  public void setInvoiceId(long value)
  {
    invoiceId = value;
  }
  public void setConglomCustomerId(long value)
  {
    conglomCustomerId = value;
  }
  public void setBilledProduct(String value)
  {
    ScreenData.put("conglomBilledProduct3",value);
    if (!billedProduct.equals(SU.isNull(value,"")))
    {
      billedProduct = SU.isNull(value,"");
      String[] details = DBA.getConglomSourceDetails(conglomCustomerId,
        billedProduct);
      feedSource = details[0];
      sourceConglomId = details[1];
      sourceSystemId = details[2];
    }
    else
    {
      billedProduct = SU.isNull(value,"");
    }
    resetCharges();
  }
  public void resetCharges()
  {
    setOneOffCharges("0");
    setRecurringCharges("0");
    setUsageCharges("0");
    setMiscCharges("0");
    setSourceDiscTotal("0");
    setInvoiceNetAmount("0");
    setInvoiceVATAmount("0");
    setInvoiceTotalAmount("0");
    setInstallCharges("0");
    setRentalCharges("0");
    setCallinkCharges("0");
    setVPNCharges("0");
    setCallCharges("0");
    setAuthCodeCharges("0");
    setEasyUsageCharges("0");
    setEasyQtrlyCharges("0");
    setSundryCharges("0");
    setSpecialCharges("0");
    setConglomDiscountNetAmount("0");
    setInvoiceNetAmount("0");
    setInvoiceTotalAmount("0");
  }
  public void setBillPeriodRef(String value)
  {
    ScreenData.put("billPeriodRef",value);
    billPeriodRef = SU.isNull(value,"");
  }
  public void setBDay(String value)
  {
    try
    {
      setBDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bDay", "0");
    }
  }
  public void setBDay(int value)
  {
    ScreenData.put("bDay",String.valueOf(value));
    bDay = value;
  }
  public void setBMonth(String value)
  {
    try
    {
      setBMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bMonth","0");
    }
  }
  public void setBMonth(int value)
  {
    ScreenData.put("bMonth",String.valueOf(value));
    bMonth = value;
  }
  public void setBYear(String value)
  {
    try
    {
      setBYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("bYear","0");
    }
  }
  public void setBYear(int value)
  {
    ScreenData.put("bYear",String.valueOf(value));
    bYear = value;
  }
  public void setBillDate(String value)
  {
    ScreenData.put("billDateh",value);
    billDateh = SU.toJDBCDate(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(billDateh==null?(new java.util.Date())
      :billDateh);
    bDay=cal.get(cal.DATE);
    bMonth=cal.get(cal.MONTH)+1;
    bYear=cal.get(cal.YEAR);
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
    if (name.equals("billPeriodRef"))
    {
      setBillPeriodRef(value);
    }
    else if (name.equals("billDateDay"))
    {
      setBDay(value);
    }
    else if (name.equals("billDateMonth"))
    {
      setBMonth(value);
    }
    else if (name.equals("billDateYear"))
    {
      setBYear(value);
    }
    else if (name.equals("billDateh"))
    {
      setBillDate(value);
    }
    else if (name.equals("conglomBilledProduct3"))
    {
      setBilledProduct(value);
    }
    /*else if (name.equals("conglomDiscountNetAmount"))
    {
      setConglomDiscountNetAmount(value);
    }*/
    else if (name.equals("specialCharges"))
    {
      setSpecialCharges(value);
    }
    else if (name.equals("sundryCharges"))
    {
      setSundryCharges(value);
    }
    else if (name.equals("easyQtrlyCharges"))
    {
      setEasyQtrlyCharges(value);
    }
    else if (name.equals("easyUsageCharges"))
    {
      setEasyUsageCharges(value);
    }
    else if (name.equals("installCharges"))
    {
      setInstallCharges(value);
    }
    else if (name.equals("authCodeCharges"))
    {
      setAuthCodeCharges(value);
    }
    else if (name.equals("callCharges"))
    {
      setCallCharges(value);
    }
    else if (name.equals("vpnCharges"))
    {
      setVPNCharges(value);
    }
    else if (name.equals("callinkCharges"))
    {
      setCallinkCharges(value);
    }
    else if (name.equals("rentalCharges"))
    {
      setRentalCharges(value);
    }
    /*else if (name.equals("invoiceTotalAmount"))
    {
      setInvoiceTotalAmount(value);
    }*/
    else if (name.equals("invoiceVATAmount"))
    {
      setInvoiceVATAmount(value);
    }
    /*else if (name.equals("invoiceNetAmount"))
    {
      setInvoiceNetAmount(value);
    }*/
    else if (name.equals("sourceDiscTotal"))
    {
      setSourceDiscTotal(value);
    }
    else if (name.equals("miscCharges"))
    {
      setMiscCharges(value);
    }
    else if (name.equals("usageCharges"))
    {
      setUsageCharges(value);
    }
    else if (name.equals("recurringCharges"))
    {
      setRecurringCharges(value);
    }
    else if (name.equals("oneOffCharges"))
    {
      setOneOffCharges(value);
    }
    else if (name.equals("billMonth"))
    {
      setBillMonth(value);
    }
    else if (name.equals("sourceInvoiceNo"))
    {
      setSourceInvoiceNo(value);
    }
    else if (name.equals("checkDigit"))
    {
      setCheckDigit(value);
    }
    else if (name.equals("sourceAccountNo"))
    {
      setSourceAccountNo(value);
    }
    else if (name.equals("overrideVAT"))
    {
      setOverrideVAT(value);
    }
  }

  private String reformatAmount(double amt)
  {
    return SU.roundToString(amt, 2);
  }
/*******************************************************************************/
/*get Methods*/
  public String getOverrideVAT()
  {
    return overrideVAT?"checked":"";
  }
  public String getConglomDiscountNetAmountStr()
  {
    return reformatAmount(conglomDiscountNetAmount);
  }
  public String getSpecialCharges()
  {
    return reformatAmount(specialCharges);
  }
  public String getSundryCharges()
  {
    return reformatAmount(sundryCharges);
  }
  public String getEasyQtrlyCharges()
  {
    return reformatAmount(easyQtrlyCharges);
  }
  public String getEasyUsageCharges()
  {
    return reformatAmount(easyUsageCharges);
  }
  public String getUsageCharges()
  {
    return reformatAmount(usageCharges);
  }
  public String getAuthCodeCharges()
  {
    return reformatAmount(authCodeCharges);
  }
  public String getCallCharges()
  {
    return reformatAmount(callCharges);
  }
  public String getVPNCharges()
  {
    return reformatAmount(vpnCharges);
  }
  public String getCallinkCharges()
  {
    return reformatAmount(callinkCharges);
  }
  public String getRentalCharges()
  {
    return reformatAmount(rentalCharges);
  }
  public String getInstallCharges()
  {
    return reformatAmount(installCharges);
  }
  public String getInvoiceTotalAmount()
  {
    return reformatAmount(invoiceTotalAmount);
  }
  public String getInvoiceVATAmount()
  {
    return reformatAmount(invoiceVATAmount);
  }
  public String getInvoiceNetAmount()
  {
    return reformatAmount(invoiceNetAmount);
  }
  public String getSourceDiscTotal()
  {
    return reformatAmount(sourceDiscTotal);
  }
  public String getMiscCharges()
  {
    return reformatAmount(miscCharges);
  }
  public String getRecurringCharges()
  {
    return reformatAmount(recurringCharges);
  }
  public String getOneOffCharges()
  {
    return reformatAmount(oneOffCharges);
  }
  public String getBillMonth()
  {
    return billMonth;
  }
  public String getCheckDigit()
  {
    return checkDigit;
  }
  public String getSourceInvoiceNo()
  {
    return sourceInvoiceNo;
  }
  public String getSourceAccountNo()
  {
    return sourceAccountNo;
  }
  public long getInvoiceId()
  {
    return invoiceId;
  }
  public long getConglomCustomerId()
  {
    return conglomCustomerId;
  }
  public String getBilledProduct()
  {
    return billedProduct;
  }
  public String getBillPeriodRef()
  {
    return billPeriodRef;
  }
  public int getBDay()
  {
    return bDay;
  }
  public int getBMonth()
  {
    return bMonth;
  }
  public int getBYear()
  {
    return bYear;
  }
  public String getBillDate()
  {
    if (billDateh != null)
    {
      return SU.reformatDate(billDateh.toString());
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
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) || (Mode.equals("Delete")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getInputMode();
    }
  }
  public String getSelectMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) || (Mode.equals("Delete")))
    {
      return "DISABLED";
    }
    else if ((FieldName.equals("conglomBilledProduct3")) && (!action.equals("Add")))
    {
      return "DISABLED";
    }
    else
    {
      return "submit";
    }
  }
  public String getMode(String FieldName)
  {
    if ((FieldName.equals("invoiceNetAmount")) ||
      (FieldName.equals("invoiceTotalAmount")) ||
      (FieldName.equals("conglomDiscountNetAmount")))
    {
      return "READONLY";
    }
    else if ((FieldName.equals("invoiceVATAmount")) && (!overrideVAT))
    {
      return "READONLY";
    }
    else if ((Mode.equals("Confirm")) || (Mode.equals("View")))
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
 public boolean getInvoice()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Source_Invoice_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,invoiceId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          billedProduct = RS.getString(1);
          billPeriodRef = RS.getString(2);
          sourceAccountNo = RS.getString(3);
          checkDigit = RS.getString(4);
          sourceInvoiceNo = RS.getString(5);
          billDateh = RS.getDate(6);
          billMonth = RS.getString(7);
          oneOffCharges = RS.getDouble(8);
          recurringCharges = RS.getDouble(9);
          usageCharges = RS.getDouble(10);
          miscCharges = RS.getDouble(11);
          sourceDiscTotal = RS.getDouble(12);
          invoiceNetAmount = RS.getDouble(13);
          invoiceVATAmount = RS.getDouble(14);
          invoiceTotalAmount = RS.getDouble(15);
          installCharges = RS.getDouble(16);
          rentalCharges = RS.getDouble(17);
          callinkCharges = RS.getDouble(18);
          vpnCharges = RS.getDouble(19);
          callCharges = RS.getDouble(20);
          authCodeCharges = RS.getDouble(21);
          easyUsageCharges = RS.getDouble(22);
          easyQtrlyCharges = RS.getDouble(23);
          sundryCharges = RS.getDouble(24);
          specialCharges = RS.getDouble(25);
          conglomDiscountNetAmount = RS.getDouble(26);
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
public int deleteInvoice()
{
  return -1;
}
/*******************************************************************************/
public int createInvoice()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Create_Source_Invoice_eBAN(?,?,?,?,?,?, " +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,billPeriodRef);
      cstmt.setString(4,sourceInvoiceNo);
      cstmt.setString(5,sourceAccountNo);
      cstmt.setString(6,checkDigit);
      cstmt.setDate(7,billDateh);
      cstmt.setDouble(8,invoiceNetAmount);
      cstmt.setDouble(9,invoiceVATAmount);
      cstmt.setDouble(10,invoiceTotalAmount);
      cstmt.setDouble(11,invoiceNetAmount);
      cstmt.setDouble(12,invoiceVATAmount);
      cstmt.setDouble(13,invoiceTotalAmount);
      cstmt.setDouble(14,oneOffCharges);
      cstmt.setDouble(15,recurringCharges);
      cstmt.setDouble(16,usageCharges);
      cstmt.setDouble(17,miscCharges);
      cstmt.setDouble(18,sourceDiscTotal);
      cstmt.setDouble(19,conglomDiscountNetAmount); // for one type only
      cstmt.setDouble(20,installCharges);
      cstmt.setDouble(21,rentalCharges);
      cstmt.setDouble(22,callinkCharges);
      cstmt.setDouble(23,vpnCharges);
      cstmt.setDouble(24,callCharges);
      cstmt.setDouble(25,authCodeCharges);
      cstmt.setDouble(26,easyUsageCharges);
      cstmt.setDouble(27,easyQtrlyCharges);
      cstmt.setDouble(28,sundryCharges);
      cstmt.setDouble(29,specialCharges);
      cstmt.setDouble(30,0);
      cstmt.setDouble(31,0);
      cstmt.setDouble(32,0);
      cstmt.setString(33,sourceConglomId);
      cstmt.setDate(34,monthlyBillStartDate);
      cstmt.setString(35,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new source invoice</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New source invoice " +
              sourceInvoiceNo +" created</b></font>";
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
public int updateInvoice()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Update_Source_Invoice_eBAN(?,?,?,?,?,?, " +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,billPeriodRef);
      cstmt.setString(4,sourceInvoiceNo);
      cstmt.setString(5,sourceAccountNo);
      cstmt.setString(6,checkDigit);
      cstmt.setDate(7,billDateh);
      cstmt.setDouble(8,invoiceNetAmount);
      cstmt.setDouble(9,invoiceVATAmount);
      cstmt.setDouble(10,invoiceTotalAmount);
      cstmt.setDouble(11,invoiceNetAmount);
      cstmt.setDouble(12,invoiceVATAmount);
      cstmt.setDouble(13,invoiceTotalAmount);
      cstmt.setDouble(14,oneOffCharges);
      cstmt.setDouble(15,recurringCharges);
      cstmt.setDouble(16,usageCharges);
      cstmt.setDouble(17,miscCharges);
      cstmt.setDouble(18,sourceDiscTotal);
      cstmt.setLong(19,invoiceId);
      cstmt.setDouble(20,installCharges);
      cstmt.setDouble(21,rentalCharges);
      cstmt.setDouble(22,callinkCharges);
      cstmt.setDouble(23,vpnCharges);
      cstmt.setDouble(24,callCharges);
      cstmt.setDouble(25,authCodeCharges);
      cstmt.setDouble(26,easyUsageCharges);
      cstmt.setDouble(27,easyQtrlyCharges);
      cstmt.setDouble(28,sundryCharges);
      cstmt.setDouble(29,specialCharges);
      cstmt.setDouble(30,0);
      cstmt.setDouble(31,0);
      cstmt.setDouble(32,0);
      cstmt.setString(33,sourceConglomId);
      cstmt.setDate(34,monthlyBillStartDate);
      cstmt.setString(35,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update source invoice</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Source invoice " +
              sourceInvoiceNo +" updated</b></font>";
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

    //sumCharges();
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
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing</b></font>";
      return false;
    }
    else if (invoiceNetAmount == 0)
    {
      setErrored("invoiceNetAmount");
      setErrored("invoiceTotalAmount");
      Message = "<font color=red><b>Invoice cannot have zero value</b></font>";
      return false;
    }
    else if (invoiceVATAmount > 999999.99)
    {
      setErrored("invoiceVATAmount");
      Message = "<font color=red><b>Invoice VAT amount must be less than or equal to 999999.99</b></font>";
      return false;
    }
    else if (Math.abs(sourceDiscTotal) > 999999.99)
    {
      setErrored("sourceDiscTotal");
      Message = "<font color=red><b>Source discount amount must be less than or equal to 999999.99</b></font>";
      return false;
    }
    else if (usageCharges > 999999.99)
    {
      setErrored("usageCharges");
      Message = "<font color=red><b>Usage charges must be less than or equal to 999999.99</b></font>";
      return false;
    }
    else if ((action.equals("Add")) &&
      (DBA.checkConglomSourceInvoiceExists(conglomCustomerId, billedProduct,
        sourceInvoiceNo)))
    {
      setErrored("sourceInvoiceNo");
      Message = "<font color=red><b>Source Invoice Number exists already</b></font>";
      return false;
    }
    else
    {
      return true;
    }
  }
  public void sumCharges()
  {
    invoiceNetAmount = oneOffCharges + recurringCharges + usageCharges +
      miscCharges + installCharges + rentalCharges + callinkCharges +
      vpnCharges + callCharges + authCodeCharges + easyUsageCharges +
      easyQtrlyCharges + sundryCharges + specialCharges + sourceDiscTotal;
    if (!overrideVAT)
    {
      invoiceVATAmount = SU.roundUp(invoiceNetAmount * vatRate, 2);
    }
    invoiceTotalAmount = invoiceNetAmount + invoiceVATAmount;
  }
/*******************************************************************************/
 public void getConglomMonthlyBillStartDate()
 {
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Mth_Bill_Start_Date_eBAN (?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          monthlyBillStartDate = RS.getDate(1);
          billPeriodRef = RS.getString(2);
          quarterlyBillCode = RS.getString(3);
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
    }
  }
/*******************************************************************************/
  private void findVATRate()
  {
    try
    {
      SQL = "select distinct tax_rate " +
      "from eban..ban_tax_requirement (nolock) " +
      "where tax_type = 'VAT' " +
      "and sap_tax_code = 'VUK' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          vatRate = RS.getDouble(1);
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

