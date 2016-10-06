package DBUtilities;

import java.sql.*;
import java.math.BigInteger;
import java.util.*;
import java.text.DecimalFormat;
import JavaUtil.EBANProperties;

public class ConglomCustomerBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long conglomCustomerId;
    private String customerName;
    private String customerNameOrig;
    private String dob;
    private String billingAddress[] = {"", "", "", "", "", "", ""};
    private String billingRefPrefix;
    private String billingFrequency;
    private String zeroBal;
    private String vatExempt;
    private String invoiceTypeCode;
    private String conMPR;
    private String cwContactTitle;
    private String cwContactName;
    private String softBillReq;
    private String fao;
    private int bsDay;
    private int bsMonth;
    private int bsYear;
    private int bcDay;
    private int bcMonth;
    private int bcYear;
    private java.sql.Date billingStartDateh;
    private java.sql.Date billingCeaseDateh;
    private java.sql.Date monthlyBillStartDate;
    private boolean hasTrialInvoice;
    private long billedProductId;
    private String billedProductExtractFreq;
    private String billedProductFeedSource;
    private String billPeriodRef;
    private String quarterlyBillCode;
    private ArrayList billedProducts = new ArrayList();
    private String globalCustomerId;

  public ConglomCustomerBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("customerName");
    mandatory.addElement("billingStartDateh");
    mandatory.addElement("billingRefPrefix");
    mandatory.addElement("zeroBal");
    mandatory.addElement("billingAddress1");
    mandatory.addElement("billingAddress2");
    mandatory.addElement("billingAddress3");
    mandatory.addElement("fao");
    mandatory.addElement("invoiceTypeCode");
    errored.addElement("");
    customerName = "";
    customerNameOrig = "";
    dob = "";
/*    billingAddress[0] = "";
    billingAddress[1] = "";
    billingAddress[2] = "";
    billingAddress[3] = "";
    billingAddress[4] = "";
    billingAddress[5] = "";
    billingAddress[6] = "";*/
    billingRefPrefix = "";
    billingFrequency = "";
    zeroBal = "";
    vatExempt = "";
    invoiceTypeCode = "";
    conMPR = "";
    cwContactTitle = "";
    cwContactName = "";
    softBillReq = "";
    fao = "";
    bsDay = 0;
    bsMonth = 0;
    bsYear = 0;
    bcDay = 0;
    bcMonth = 0;
    bcYear = 0;
    billingStartDateh = new java.sql.Date(new java.util.Date().getTime());
    billingCeaseDateh = null;
    conglomCustomerId = 0;
    hasTrialInvoice = false;
    billedProductId = 0;
    billedProductExtractFreq = null;
    billedProductFeedSource = null;
    monthlyBillStartDate = null;
    billPeriodRef = null;
    quarterlyBillCode = null;
    billedProducts.clear();
    globalCustomerId = "";
  }

  public void Reset()
  {
    super.Reset();
    customerName = "";
    customerNameOrig = "";
    dob = "";
    billingAddress[0] = "";
    billingAddress[1] = "";
    billingAddress[2] = "";
    billingAddress[3] = "";
    billingAddress[4] = "";
    billingAddress[5] = "";
    billingAddress[6] = "";
    billingRefPrefix = "";
    billingFrequency = "";
    zeroBal = "";
    vatExempt = "";
    invoiceTypeCode = "";
    conMPR = "";
    cwContactTitle = "";
    cwContactName = "";
    softBillReq = "";
    fao = "";
    bsDay = 0;
    bsMonth = 0;
    bsYear = 0;
    bcDay = 0;
    bcMonth = 0;
    bcYear = 0;
    billingStartDateh = new java.sql.Date(new java.util.Date().getTime());
    billingCeaseDateh = null;
    conglomCustomerId = 0;
    hasTrialInvoice = false;
    billedProductId = 0;
    billedProductExtractFreq = null;
    billedProductFeedSource = null;
    monthlyBillStartDate = null;
    billPeriodRef = null;
    quarterlyBillCode = null;
    billedProducts.clear();
    globalCustomerId = "";
  }
/*set Methods*/
  public void setAction(String value)
  {
    super.setAction(value);
    if (value.equals("Add"))
    {
      if (!mandatory.contains("conglomCustomer"))
      {
        mandatory.add("conglomCustomer");
      }
    }
    else
    {
      if (mandatory.contains("conglomCustomer"))
      {
        mandatory.remove("conglomCustomer");
      }
    }
  }
  public void setBilledProductId(long value)
  {
    billedProductId = value;
  }
  public void setBillPeriodRef(String value)
  {
    billPeriodRef = SU.isNull(value,"");
  }
  public void setBilledProductExtractFreq(String value)
  {
    //ScreenData.put("billedProductExtractFreq",value);
    billedProductExtractFreq = SU.isNull(value,"");
  }
  public void setBilledProductFeedSource(String value)
  {
    //ScreenData.put("billedProductFeedSource",value);globalCustomerId
    billedProductFeedSource = SU.isNull(value,"");
  }
  public void setConglomCustomerId(long value)
  {
    conglomCustomerId = value;
  }
  public void setGlobalCustomerId(String value)
  {
    ScreenData.put("conglomCustomer",value);
    globalCustomerId = value;
  }
  public void setFAO(String value)
  {
    ScreenData.put("fao",value);
    fao = SU.isNull(value,"");
  }
  public void setSoftBillReq(String value)
  {
    ScreenData.put("softBillReq",value);
    softBillReq = SU.isNull(value,"");
  }
  public void setCWContactName(String value)
  {
    ScreenData.put("cwContactName",value);
    cwContactName = SU.isNull(value,"");
  }
  public void setCWContactTitle(String value)
  {
    ScreenData.put("cwContactTitle",value);
    cwContactTitle = SU.isNull(value,"");
  }
  public void setConMPR(String value)
  {
    ScreenData.put("conMPR",value);
    conMPR = SU.isNull(value,"");
  }
  public void setInvoiceTypeCode(String value)
  {
    ScreenData.put("invoiceTypeCode",value);
    invoiceTypeCode = SU.isNull(value,"");
  }
  public void setVATExempt(String value)
  {
    ScreenData.put("vatExempt",value);
    vatExempt = SU.isNull(value,"");
  }
  public void setZeroBal(String value)
  {
    ScreenData.put("zeroBal",value);
    zeroBal = SU.isNull(value,"");
  }
  public void setBillingFrequency(String value)
  {
    ScreenData.put("conglomBillingFrequency",value);
    billingFrequency = SU.isNull(value,"");
  }
  public void setBillingRefPrefix(String value)
  {
    ScreenData.put("billingRefPrefix",value);
    billingRefPrefix = SU.isNull(value,"");
  }
  public void setCustomerName(String value)
  {
    ScreenData.put("customerName",value);
    customerName = SU.isNull(value,"");
  }
  public void setDob(String value)
  {
    ScreenData.put("conglomDOB",value);
    dob = SU.isNull(value,"");
  }
  public void setBillingAddress1(String value)
  {
    ScreenData.put("billingAddress1",value);
    billingAddress[0] = SU.isNull(value,"");
  }
  public void setBillingAddress2(String value)
  {
    ScreenData.put("billingAddress2",value);
    billingAddress[1] = SU.isNull(value,"");
  }
  public void setBillingAddress3(String value)
  {
    ScreenData.put("billingAddress3",value);
    billingAddress[2] = SU.isNull(value,"");
  }
  public void setBillingAddress4(String value)
  {
    ScreenData.put("billingAddress4",value);
    billingAddress[3] = SU.isNull(value,"");
  }
  public void setBillingAddress5(String value)
  {
    ScreenData.put("billingAddress5",value);
    billingAddress[4] = SU.isNull(value,"");
  }
  public void setBillingAddress6(String value)
  {
    ScreenData.put("billingAddress6",value);
    billingAddress[5] = SU.isNull(value,"");
  }
  public void setBillingAddress7(String value)
  {
    ScreenData.put("billingAddress7",value);
    billingAddress[6] = SU.isNull(value,"");
  }
  public void setBillingAddressN(String value, int n)
  {
    ScreenData.put("billingAddress"+n,value);
    billingAddress[n-1] = SU.isNull(value,"");
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
    if (name.equals("customerName"))
    {
      setCustomerName(value);
    }
    else if (name.equals("conglomDOB"))
    {
      setDob(value);
    }
    else if (name.equals("conglomCustomer"))
    {
      setGlobalCustomerId(value);
    }
    else if (name.equals("billingAddress1"))
    {
      setBillingAddressN(value, 1);
    }
    else if (name.equals("billingAddress2"))
    {
      setBillingAddressN(value, 2);
    }
    else if (name.equals("billingAddress3"))
    {
      setBillingAddressN(value, 3);
    }
    else if (name.equals("billingAddress4"))
    {
      setBillingAddressN(value, 4);
    }
    else if (name.equals("billingAddress5"))
    {
      setBillingAddressN(value, 5);
    }
    else if (name.equals("billingAddress6"))
    {
      setBillingAddressN(value, 6);
    }
    else if (name.equals("billingAddress7"))
    {
      setBillingAddressN(value, 7);
    }
    else if (name.equals("billingRefPrefix"))
    {
      setBillingRefPrefix(value);
    }
    else if (name.equals("conglomBillingFrequency"))
    {
      setBillingFrequency(value);
    }
    else if (name.equals("zeroBal"))
    {
      setZeroBal(value);
    }
    else if (name.equals("vatExempt"))
    {
      setVATExempt(value);
    }
    else if (name.equals("invoiceTypeCode"))
    {
      setInvoiceTypeCode(value);
    }
    else if (name.equals("conMPR"))
    {
      setConMPR(value);
    }
    else if (name.equals("cwContactTitle"))
    {
      setCWContactTitle(value);
    }
    else if (name.equals("cwContactName"))
    {
      setCWContactName(value);
    }
    else if (name.equals("softBillReq"))
    {
      setSoftBillReq(value);
    }
    else if (name.equals("fao"))
    {
      setFAO(value);
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
  public long getBilledProductId()
  {
    return billedProductId;
  }
  public String getBilledProductExtractFreq()
  {
    return billedProductExtractFreq;
  }
  public String getBillPeriodRef()
  {
    return billPeriodRef;
  }
  public String getBilledProductFeedSource()
  {
    return billedProductFeedSource ;
  }
  public long getConglomCustomerId()
  {
    return conglomCustomerId;
  }
  public String getGlobalCustomerId()
  {
    return globalCustomerId;
  }
  public String getFAO()
  {
    return fao;
  }
  public String getSoftBillReq()
  {
    return softBillReq;
  }
  public String getCWContactName()
  {
    return cwContactName;
  }
  public String getCWContactTitle()
  {
    return cwContactTitle;
  }
  public String getConMPR()
  {
    return conMPR;
  }
  public String getInvoiceTypeCode()
  {
    return invoiceTypeCode;
  }
  public String getVATExempt()
  {
    return vatExempt;
  }
  public String getZeroBal()
  {
    return zeroBal;
  }
  public String getBillingFrequency()
  {
    return billingFrequency;
  }
  public String getBillingRefPrefix()
  {
    return billingRefPrefix;
  }
  public String getCustomerName()
  {
    return customerName;
  }
  public String getDob()
  {
    return dob;
  }
  public String[] getBillingAddress()
  {
    return billingAddress;
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
    if ((FieldName.equals("billingRefPrefix")) && (hasTrialInvoice))
    {
      return "READONLY";
    }
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
/*******************************************************************************/
 public boolean getCustomer()
 {
    boolean getCustomer = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Customer_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          customerName = RS.getString(1);
          dob = RS.getString(2);
          billingAddress[0] = RS.getString(3);
          billingAddress[1] = RS.getString(4);
          billingAddress[2] = RS.getString(5);
          billingAddress[3] = RS.getString(6);
          billingAddress[4] = RS.getString(7);
          billingAddress[5] = RS.getString(8);
          billingAddress[6] = RS.getString(9);
          billingRefPrefix = RS.getString(10).trim();
          billingFrequency = RS.getString(11);
          zeroBal = RS.getString(12);
          vatExempt = RS.getString(13);
          invoiceTypeCode = RS.getString(14);
          conMPR = RS.getString(15);
          cwContactTitle = RS.getString(16);
          cwContactName = RS.getString(17);
          softBillReq = RS.getString(18);
          fao = RS.getString(19);
          billingStartDateh = RS.getDate(20);
          billingCeaseDateh = RS.getDate(21);
          setDates();
          getCustomer = true;
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
      return getCustomer;
    }
 }
/*******************************************************************************/
 public boolean checkTrialInvoice()
 {
    hasTrialInvoice = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Check_For_Trial_Invoice_eBAN	(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billingRefPrefix);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          hasTrialInvoice = RS.getLong(1)==1;
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
      return hasTrialInvoice;
    }
 }
/*******************************************************************************/
public int updateCustomer()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      //java.util.Calendar cal = java.util.Calendar.getInstance();
      //cal.set(year, month, day);
      SQL = "{call conglomerate..Update_Customer_eBAN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,customerName);
      cstmt.setString(3,dob);
      cstmt.setString(4,billingAddress[0]);
      cstmt.setString(5,billingAddress[1]);
      cstmt.setString(6,billingAddress[2]);
      cstmt.setString(7,billingAddress[3]);
      cstmt.setString(8,billingAddress[4]);
      cstmt.setString(9,billingAddress[5]);
      cstmt.setString(10,billingAddress[6]);
      cstmt.setString(11,billingRefPrefix);
      cstmt.setString(12,billingFrequency);
      cstmt.setString(13,zeroBal);
      cstmt.setString(14,vatExempt);
      cstmt.setString(15,invoiceTypeCode);
      cstmt.setString(16,conMPR);
      cstmt.setString(17,cwContactTitle);
      cstmt.setString(18,cwContactName);
      cstmt.setString(19,softBillReq);
      cstmt.setString(20,fao);
      cstmt.setDate(21,billingStartDateh);
      cstmt.setDate(22,billingCeaseDateh);
      cstmt.setString(23,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update customer</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Customer updated</b></font>";
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
public long createCustomer()
{
  long ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      //java.util.Calendar cal = java.util.Calendar.getInstance();
      //cal.set(year, month, day);
      SQL = "{call conglomerate..Create_Customer_eBAN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,globalCustomerId);
      cstmt.setString(2,customerName);
      cstmt.setString(3,dob);
      cstmt.setString(4,billingAddress[0]);
      cstmt.setString(5,billingAddress[1]);
      cstmt.setString(6,billingAddress[2]);
      cstmt.setString(7,billingAddress[3]);
      cstmt.setString(8,billingAddress[4]);
      cstmt.setString(9,billingAddress[5]);
      cstmt.setString(10,billingAddress[6]);
      cstmt.setString(11,billingRefPrefix);
      cstmt.setString(12,billingFrequency);
      cstmt.setString(13,zeroBal);
      cstmt.setString(14,vatExempt);
      cstmt.setString(15,invoiceTypeCode);
      cstmt.setString(16,conMPR);
      cstmt.setString(17,cwContactTitle);
      cstmt.setString(18,cwContactName);
      cstmt.setString(19,softBillReq);
      cstmt.setString(20,fao);
      cstmt.setDate(21,billingStartDateh);
      cstmt.setDate(22,billingCeaseDateh);
      cstmt.setString(23,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getLong(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create customer</b></font>";
          }
          else
          {
            conglomCustomerId = ret;
            Message = "<font color=blue><b>Customer created. 'Add Product' " +
              "is available on the 'Options' menu above.  You will also need " +
              "to contact Systems Support before you will be able to see this " +
              "customer in the customer list.</b></font>";
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

    if ((ButtonPressed.startsWith("Submit")) ||
      (ButtonPressed.startsWith("Update")))
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if ((FormField.equals("")) || (SU.isSpaces(FormField)))
        {
          setErrored(FieldName);
        }
        else if (FieldName.equals("billingRefPrefix"))
        {
          if ((FormField.length() < 4) || (FormField.length() > 8))
          {
            setErrored("billingRefPrefix");
            Message = "<font color=red><b>Billing Reference Prefix must be between 4 and 8 characters in length";
            return false;
          }
        }
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
      {
        setErrored("billingStartDateh");
        setErrored("billingCeaseDateh");
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }
      else if (((action.equals("Add")) && (checkCustomerExists())) ||
        ((action.equals("Amend")) && (!customerName.equals(customerNameOrig)) &&
        (checkCustomerExists())))
      {
        setErrored("conglomCustomer");
        setErrored("customerName");
        Message = "<font color=red><b>Customer Name exists already for this Global Customer";
        return false;
      }
      return true;
    }
  }
/*******************************************************************************/
  public String getBilledProductList(String sortOrder)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    RadioButton="width=45 align=center><img src=\"/shared/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        grid.append("<table border=0>");

        rowcount = 0;

        SQL = "{call conglomerate..Get_Billed_Products_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);

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
            for(counter=1;counter<columns+1;counter++)
            {
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(RS.getString(counter));
              grid.append("</td>");
            }
            //Add the extra generated column for the buttons
            String thisId = RS.getString(counter);
            String thisFreq = RS.getString(++counter);
            String thisSource = RS.getString(++counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            if (Mode.equals("Confirm"))
            {
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"\">");
            }
            else
            {
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisId + "','Update','" + thisFreq + "','" + thisSource + "')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonL.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisId + "','LD','" + thisFreq + "','" + thisSource + "')\">");
            }
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
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
        return grid.toString();
      }
      else
      {
        return Message;
      }
    }
  }
/*******************************************************************************/
 public String getExtractDatesListBox(String selectedDate, boolean fullExtract,
  String billedProductId, boolean disabled)
 {
    StringBuffer html = new StringBuffer("<select name=\"conglomExtractDate\" " +
      "size=1 " + (disabled?"disabled":"") + "><option value=\" \"> </option>");
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Select_Billing_Dates_eBAN(?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setDate(1,monthlyBillStartDate);
        cstmt.setString(2,billedProductId);
        cstmt.setString(3,fullExtract?"F":"P");
        cstmt.execute();
        RS = cstmt.getResultSet();

        while (RS.next())
        {
          String text = RS.getString(1);
          String value = RS.getString(2);
          html.append("<option value=\"" + value + "\" " +
            (value.equals(selectedDate)?"selected":"") + ">" + text +
            "</option>");
        }
        html.append("</select>");
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
      return html.toString();
    }
 }
/*******************************************************************************/
 public String getPriorPeriodsListBox(String selectedPriorPeriod, boolean disabled)
 {
    StringBuffer html = new StringBuffer("<select name=\"conglomPriorPeriods\" " +
      "size=1 " + (disabled?"disabled":"") + "><option value=\" \"> </option>");
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Prior_Periods_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        while (RS.next())
        {
          String pp = RS.getString(1);
          html.append("<option value=\"" + pp + "\" " +
            (pp.equals(selectedPriorPeriod)?"selected":"") + ">" + pp +
            "</option>");
        }
        html.append("</select>");
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
      return html.toString();
    }
 }
/*******************************************************************************/
 public void getMonthlyBillStartDate()
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
  public int rePullDataFromGoldfish(String selectedDate, boolean fullExtract,
    String billedProductId)
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //cal.set(year, month, day);
        SQL = "{call conglomerate..Insert_Job_Queue_eBAN(?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,selectedDate);
        cstmt.setString(3,billedProductId);
        cstmt.setString(4,billPeriodRef);
        cstmt.setString(5,fullExtract?"F":"P");
        cstmt.setString(6,"Goldfish Extract");
        cstmt.setString(7,"N");
        cstmt.setString(8,quarterlyBillCode);
        cstmt.setString(9,dob);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret < 0)
            {
              switch (ret)
              {
                case -1:
                  Message = "<font color=red><b>Closed Bill already generated for this product for this period</b></font>";
                  break;
                case -2:
                  Message = "<font color=red><b>Source Invoices for this Product not due to be extracted</b></font>";
                  break;
                case -3:
                  Message = "<font color=red><b>Source Invoices for this Product are not available yet. " +
                    "Check Conglom_Billing_Schedule for DOB_Cycle.</b></font>";
                  break;
                default:
                  Message = "<font color=red><b>Unable to submit data extract</b></font>";
              }
            }
            else
            {
              Message = "<font color=blue><b>Data extract submitted. Click " +
                "'View Log' to monitor its progress.</b></font>";
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
  public int setUpPriorPeriod(String priorPeriod)
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //cal.set(year, month, day);
        SQL = "{call conglomerate..Set_Up_Prior_Period_eBAN(?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,priorPeriod);
        cstmt.setString(3,billingRefPrefix);
        cstmt.setString(4,billingFrequency);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret < 0)
            {
              switch (ret)
              {
                case -1:
                  Message = "<font color=red><b>Unable to insert record into Conglom_Invoice table</b></font>";
                  break;
                case -2:
                  Message = "<font color=red><b>Unable to insert record into Conglom_Prod_Inv_Line table</b></font>";
                  break;
                default:
                  Message = "<font color=red><b>Unable to set up prior period, code: " +
                    ret + "</b></font>";
              }
            }
            else
            {
              Message = "<font color=blue><b>Customer succesfully set up " +
                "for prior period " + priorPeriod + " (" + billingRefPrefix +
                "/" + priorPeriod.substring(2,4) + priorPeriod.substring(0,2) +
                ")</b></font>";
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
/************************************************************************************************/
  public Collection getBilledProducts()
  {
    return billedProducts;
  }
/************************************************************************************************/
  public Collection findBilledProducts()
  {
    billedProducts.clear();

    try
    {
      SQL = "Select b.Description " +
        "from conglomerate..Conglom_Cust_Product a (nolock), conglomerate..Billed_Product b " +
        "where a.Billed_Product_Id = b.Billed_product_Id " +
        "and a.conglom_cust_id = " + conglomCustomerId + " " +
        "and a.bill_eff_from_date <= getdate() " +
        "and (a.bill_eff_to_date > getdate() or a.bill_eff_to_date is null) ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          billedProducts.add(RS.getString(1));
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
      return billedProducts;
    }
  }
/*******************************************************************************/
  public Collection getBilledProductsHtml(boolean close)
  {
    billedProducts.clear();
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = close?"{call conglomerate..Get_Close_Bill_Data_Display_eBAN(?,?)}":
          "{call conglomerate..Get_Trial_Bill_Data_Display(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billPeriodRef);
        cstmt.execute();
        RS = cstmt.getResultSet();

        while (RS.next())
        {
          billedProducts.add("<tr><td>" + RS.getString(1) + "</td><td>" +
            RS.getString(2) + (close?"</td><td>" + RS.getString(3) + "</td><td>" +
            RS.getString(4):"") + "</td></tr>");
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
      return billedProducts;
    }
  }
/*******************************************************************************/
  public int generateBill(boolean close)
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        if (close)
        {
          SQL = "{call conglomerate..Generate_Close_Bill_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setLong(1,conglomCustomerId);
          cstmt.setString(2,billPeriodRef);
          cstmt.execute();
          RS = cstmt.getResultSet();
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to close trial bill, code: " +
                ret + "</b></font>";
            }
            else
            {
              Message = "<font color=blue><b>Trial bill data closed successfully</b></font>";
            }
          }
        }
        else
        {
          SQL = "{call conglomerate..Generate_Trial_Bill_eBAN(?,?,?,?,?,?,?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setLong(1,conglomCustomerId);
          cstmt.setString(2,billPeriodRef);
          cstmt.setString(3,billingRefPrefix);
          cstmt.setString(4,billingFrequency);
          cstmt.setString(5,this.getConglomInvoiceRef());
          cstmt.setString(6,quarterlyBillCode);
          cstmt.setString(7,userId);
          cstmt.setString(8,this.getGlobalCustomerId());
          cstmt.execute();
          RS = cstmt.getResultSet();
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret < 0)
            {
              switch (ret)
              {
                case -28:
                  Message = "<font color=red><b>Trial bill generated but with exceptions preventing closure</b></font>";
                  break;
                default:
                  Message = "<font color=red><b>Unable to generate trial bill, code: " +
                    ret + "</b></font>";
              }
            }
            else
            {
              //Runtime runtime = Runtime.getRuntime();
              //Process proc = runtime.exec(EBANProperties.getEBANProperty("crystalScheduling"));
              Message = "<font color=blue><b>Trial bill generated and reports scheduled</b></font>";
            }
          }
        }
      }
    }
    /*catch(java.io.IOException ie)
    {
      Message="<font color=red><b>"+ie.getMessage();
    }*/
    catch(java.sql.SQLException se)
    {
      Message="<font color=red><b>"+se.getMessage();
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
/************************************************************************************************/
  private boolean checkCustomerExists()
  {
    boolean exists = false;
    try
    {
      SQL = "Select Conglom_Cust_Id from conglomerate..Conglom_Customer (nolock) " +
        "where Global_Customer_Id = '" + globalCustomerId + "' " +
        "and Conglom_Cust_Name = '" + customerName + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        exists = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return exists;
    }
  }
}

