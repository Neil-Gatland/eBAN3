package DBUtilities;

import java.sql.*;
import java.util.*;
import java.io.*;
import JavaUtil.*;
import java.math.*;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

public class BANBean {
    protected String SQL;

    protected String banIdentifier;
    protected String banType;
    protected String BANs_to_List,Global_Customer_Id_for_List="All",BAN_Type_for_List="All";
    protected String User_for_List="All",PG_for_List="All",Invoice_Region_for_List="All";
    protected String ChargeDateHeading,ChargeFieldVisibility,CRDB,Currency_Desc="",Account_Id="";
    protected String banStatus="Draft",site,banCreatedBy,siteDisplay,action="",Invoice_Region="",Global_Customer_Name="";
    protected String Return_BAN_To_Name="",BAN_Summary="",BAN_Reason="",Mode="",Division_Id="";
    protected String Service_Reference="",C2_Ref="",Global_Customer_Id="",Message="",Carrier_Name="";
    protected String chargeTypeDisplay,chargeType,Last_Updated_By="",Circuit_Reference="";
    protected String SelectMode="",ProcessMode="",InputMode="",Calendar="";
    protected String Product="",Product_Name="",Account_Name="",Account_Filter="",
      Nostro_User_Id="",Billing_Source="";
    protected int Payment_Group_Id=0;
    protected String User_Staff_Number="", Surname_Filter="", CLI="", CLI_Filter, CLI_Id="";
    protected String CLI_Details_Customer_Name="";
    protected String CLI_Details_Staff_Number="";
    protected String CLI_Details_User_Name="";
    protected String CLI_Details_CLI="";
    protected String CLI_Details_Charge_From="";
    protected String CLI_Details_Charge_To="";
    protected String CLI_Details_Call_Charge="";
    protected String CLI_Details_Calls_Charge="";
    protected String CLI_Details_Management_Charge="";
    protected String CLI_Details_Markup_Charge="";
    protected String Credit_Amount="";
    protected String Credit_Description="";
    protected String CR_DR="";
    protected int VDay=0, VMonth=0, VYear=0;
    protected String Credit_Id="";
    protected String Credit_Details_Customer_Name="";
    protected String Credit_Details_Staff_Number="";
    protected String Credit_Details_User_Name="";
    protected String Credit_Details_CLI="";
    protected String Credit_Details_Charge_From="";
    protected String Credit_Details_Charge_To="";
    protected String Credit_Details_Call_Charge="";
    protected String Credit_Details_Calls_Charge="";
    protected String Credit_Details_Management_Charge="";
    protected String Credit_Details_Markup_Charge="";
    protected String Credit_Details_Total_Credit="";
    protected String Credit_Details_Calls_Credit="";
    protected String Credit_Details_Management_Credit="";
    protected String Credit_Details_Markup_Credit="";
    protected String Credit_Details_Credit_Date="";
    protected String Credit_Details_Credit_Description="";
    protected String Credit_Details_CR_DR="";
    protected String Division_Name="";

    protected java.sql.Date Required_BAN_Effective_Dateh,proposedBanDate;
    protected String banAuthorisedBy,userId,bansToList,rejectReason="",OrderBy="Status";
    protected final int READ = 1;
    protected final int WRITE = 3;
    protected final int PREPARE = 2;
    protected java.sql.ResultSet RS;
    protected CallableStatement cstmt;
    protected java.sql.Statement Stmt;
    protected boolean select;
    protected String header;
    protected int border=0;
    protected int columns;
    protected Vector errored=new Vector();
    protected JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    protected DBAccess DBA = null;
    protected final int NOT_INT=-2147483648;
    protected static final String P3 = "P3";
    protected static final String P4 = "P4";
    protected static final String P5 = "P5";
    protected  Connection Conn = null;
    protected boolean isDirect = false;

    protected String Status_for_List="All",Account_for_List="All",GSR_for_List="All";
    protected String Division_for_List="All",Month_for_List="All",Charge_Type_for_List="All";
    protected String Invoice_for_List="All",Visit_Month_for_List="All",Created_Month_for_List="All";

    protected String showFilters="hidden";
    protected String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);

    protected String Business_Unit_for_Report="All";
    protected String Segment_for_Report="All";
    protected String Customer_for_Report="All";
    protected String groupName = "";
    protected boolean edifySetCustomer = false;
    protected String siteVisibility = "hidden";
    protected String c2RefNo = "";
//variables for new desktop
    private String billingTeam;
    private String lastDOB;
    private String sapDate;
    private String billMonth;
    private String actAsLogon;
    private String logonGroup;
    private String desktopSortOrder;
    private String jobQueue;
    private String customerGrid;
    private boolean refreshCustomerGrid;
    private String exceptionTypeForList;
    private String exceptionStatusForList;
    private String iRINForList;
    private String billPeriodStartDate;
    private String billPeriodEndDate;
    private int exceptionId;
    private int exceptionType;
    private String exceptionIRIN;
    private String exceptionStatus;
    private String accDetsCustomer;
    private String accDetsPeriod;
    private String accDetsDOB;
    private int accDetsAccounts;
    private String accDetsStatus;
    private String accDetsExceptions;
    private String customerContact;
    private String companyAddress1;
    private String companyAddress2;
    private String companyAddress3;
    private String companyAddress4;
    private String companyAddress5;
    private String companyAddress6;
    private String companyAddress7;
    private String companyDetails1;
    private String companyDetails2;
    private String companyName;
    private String billingContact;
    private String billingAddress1;
    private String billingAddress2;
    private String billingAddress3;
    private String billingAddress4;
    private String billingAddress5;
    private String billingAddress6;
    private String billingAddress7;
    private String bankAddress1;
    private String bankAddress2;
    private String bankAddress3;
    private String bankAddress4;
    private String bankAddress5;
    private String bankAddress6;
    private String bankAddress7;
    private String altBankAddress1;
    private String altBankAddress2;
    private String altBankAddress3;
    private String altBankAddress4;
    private String altBankAddress5;
    private String altBankAddress6;
    private String altBankAddress7;
    private String billingCustomerName;
    private String accountId;
    private String taxReference;
    private String taxRate;
    private String taxType;
    private String taxReferenceLiteral;
    private String outgoingCurrencyCode;
    private String outgoingCurrencyDesc;
    private String taxPayable;
    private String mbLogGrid;
    private String runType;
    private String runTime;
    private String backdates;
    private boolean scheduleCrystal;
    private String billJobId;
    private String discountPlan;
    private String invoiceNo;
    private String servicePartnerName;
    private long servicePartnerId;
    private String gsoBillingPeriod;
    private String gsoStatus;
    private String gsoRejects;
    private String gsoExceptions;
    private String gsoJobId;
    private String gsoMessageDate;
    private String gsoSubmitMode;
    private String gsoTrialButtonStatus;
    private String gsoCloseButtonStatus;
    private String gsoExtractButtonStatus;
    private long conglomCustomerId;
    private String conglomCustomerName;
    private String conglomFrequency;
    private String conglomPeriod;
    private long conglomProductCount;
    private String conglomInvoiceRef;
    private String conglomInvTotalExc;
    private String conglomInvTax;
    private String conglomInvTotalInc;
    private String periodForList;
    private String invoiceDocketNoForList;
    private String billedProductForList;
    private String invoiceDocketNo;
    private String geocodes;
    private String analysts;
    private String billPeriodRef;
    private double conglomDiscountNetAmount;
    private String itemTypeForList;
    private double conglomBillProdBilledAmtTotal;
    private double conglomBillProdVATTotal;
    private double conglomBillProdInvoiceAmtTotal;
    private int conglomBillProdItemNo;
    private String conglomBillProdGrid;
    private String conglomItemStatusGrid;
    private String conglomCRDocketGrid;
    private ArrayList msInvoiceCharges;
    private String invoiceRegion;
    private String customerRadius;
    private String billingRegion;
    private String totalCharges;
    private String companyAddressId;
    private ArrayList customerList;
    private String searchResults;
    private String closedInvoiceGrid;
    private String loginId;
    private boolean adAuthenticated;
    private String adEmailAddress;

  public BANBean()
  {
    billingTeam = null;
    lastDOB = null;
    sapDate = null;
    billMonth = null;
    actAsLogon = null;
    logonGroup = null;
    desktopSortOrder = "Customer";
    jobQueue = null;
    customerGrid = null;
    refreshCustomerGrid = true;
    Message="";
    exceptionTypeForList = "All";
    exceptionStatusForList = "All";
    iRINForList = "All";
    billPeriodStartDate = "Unknown";
    billPeriodEndDate = "Unknown";
    exceptionId = -1;
    exceptionType = -1;
    exceptionIRIN = null;
    exceptionStatus = null;
    accDetsCustomer = null;
    accDetsPeriod = null;
    accDetsDOB = null;
    accDetsAccounts = -1;
    accDetsStatus = null;
    accDetsExceptions = null;
    customerContact = null;
    companyAddress1 = null;
    companyAddress2 = null;
    companyAddress3 = null;
    companyAddress4 = null;
    companyAddress5 = null;
    companyAddress6 = null;
    companyAddress7 = null;
    companyDetails1 = null;
    companyDetails2 = null;
    companyName = "";
    billingContact = null;
    billingAddress1 = null;
    billingAddress2 = null;
    billingAddress3 = null;
    billingAddress4 = null;
    billingAddress5 = null;
    billingAddress6 = null;
    billingAddress7 = null;
    bankAddress1 = null;
    bankAddress2 = null;
    bankAddress3 = null;
    bankAddress4 = null;
    bankAddress5 = null;
    bankAddress6 = null;
    bankAddress7 = null;
    altBankAddress1 = null;
    altBankAddress2 = null;
    altBankAddress3 = null;
    altBankAddress4 = null;
    altBankAddress5 = null;
    altBankAddress6 = null;
    altBankAddress7 = null;
    billingCustomerName = null;
    accountId = null;
    taxReference = null;
    taxReferenceLiteral = null;
    outgoingCurrencyCode = null;
    outgoingCurrencyDesc = null;
    taxPayable = null;
    taxRate = null;
    taxType = null;
    mbLogGrid = "";
    runType = null;
    runTime = null;
    backdates = null;
    scheduleCrystal = false;
    billJobId = null;
    discountPlan = null;
    invoiceNo = null;
    servicePartnerName = null;
    servicePartnerId = -1;
    gsoBillingPeriod = null;
    gsoStatus = null;
    gsoRejects = null;
    gsoExceptions = null;
    gsoJobId = "";
    gsoMessageDate = "";
    gsoSubmitMode = "";
    gsoTrialButtonStatus = "";
    gsoCloseButtonStatus = "";
    gsoExtractButtonStatus = "";
    conglomCustomerId = -1;
    conglomCustomerName = null;
    conglomFrequency = null;
    conglomPeriod = null;
    conglomProductCount = 0;
    conglomInvoiceRef = null;
    conglomInvTotalExc = null;
    conglomInvTax = null;
    conglomInvTotalInc = null;
    periodForList = "All";
    invoiceDocketNoForList = "All";
    billedProductForList = "All";
    invoiceDocketNo = null;
    geocodes = "";
    analysts = "";
    billPeriodRef = null;
    conglomDiscountNetAmount = 0;
    itemTypeForList = "All";
    conglomBillProdBilledAmtTotal = 0;
    conglomBillProdVATTotal = 0;
    conglomBillProdInvoiceAmtTotal = 0;
    conglomBillProdItemNo = 0;
    conglomBillProdGrid = "";
    conglomItemStatusGrid = "";
    conglomCRDocketGrid = "";
    msInvoiceCharges = new ArrayList();
    invoiceRegion = null;
    customerRadius = null;
    billingRegion = null;
    totalCharges = "0.00";
    companyAddressId = null;
    customerList = new ArrayList();
    DBA = new DBUtilities.DBAccess();
    searchResults = "";
    closedInvoiceGrid = "";
    loginId = "";
    adAuthenticated = false;
    adEmailAddress = null;
  }
  protected void Reset()
  {
    Currency_Desc="";
    Return_BAN_To_Name="";
    BAN_Summary="";
    BAN_Reason="";
    Message="";
    Last_Updated_By="";
    banIdentifier="";
    banStatus="Draft";
    banCreatedBy="";
    isDirect = false;
    groupName = "";
    edifySetCustomer = false;
    Division_Name="";
    siteVisibility = "hidden";
    billingTeam = null;
    lastDOB = null;
    sapDate = null;
    billMonth = null;
    actAsLogon = null;
    logonGroup = null;
    desktopSortOrder = "Customer";
    jobQueue = null;
    customerGrid = null;
    refreshCustomerGrid = true;
    exceptionTypeForList = "All";
    exceptionStatusForList = "All";
    iRINForList = "All";
    GSR_for_List = "All";
    billPeriodStartDate = "Unknown";
    billPeriodEndDate = "Unknown";
    exceptionId = -1;
    exceptionType = -1;
    exceptionIRIN = null;
    exceptionStatus = null;
    accDetsCustomer = null;
    accDetsPeriod = null;
    accDetsDOB = null;
    accDetsAccounts = -1;
    accDetsStatus = null;
    accDetsExceptions = null;
    customerContact = null;
    companyAddress1 = null;
    companyAddress2 = null;
    companyAddress3 = null;
    companyAddress4 = null;
    companyAddress5 = null;
    companyAddress6 = null;
    companyAddress7 = null;
    companyDetails1 = null;
    companyDetails2 = null;
    companyName = "";
    billingContact = null;
    billingAddress1 = null;
    billingAddress2 = null;
    billingAddress3 = null;
    billingAddress4 = null;
    billingAddress5 = null;
    billingAddress6 = null;
    billingAddress7 = null;
    bankAddress1 = null;
    bankAddress2 = null;
    bankAddress3 = null;
    bankAddress4 = null;
    bankAddress5 = null;
    bankAddress6 = null;
    bankAddress7 = null;
    altBankAddress1 = null;
    altBankAddress2 = null;
    altBankAddress3 = null;
    altBankAddress4 = null;
    altBankAddress5 = null;
    altBankAddress6 = null;
    altBankAddress7 = null;
    billingCustomerName = null;
    accountId = null;
    taxReference = null;
    taxReferenceLiteral = null;
    outgoingCurrencyCode = null;
    outgoingCurrencyDesc = null;
    taxPayable = null;
    taxRate = null;
    taxType = null;
    mbLogGrid = "";
    runType = null;
    runTime = null;
    backdates = null;
    scheduleCrystal = false;
    billJobId = null;
    discountPlan = null;
    invoiceNo = null;
    servicePartnerName = null;
    servicePartnerId = -1;
    gsoBillingPeriod = null;
    gsoStatus = null;
    gsoRejects = null;
    gsoExceptions = null;
    gsoJobId = "";
    gsoMessageDate = "";
    gsoSubmitMode = "";
    gsoTrialButtonStatus = "";
    gsoCloseButtonStatus = "";
    gsoExtractButtonStatus = "";
    conglomCustomerId = -1;
    conglomCustomerName = null;
    conglomFrequency = null;
    conglomPeriod = null;
    conglomProductCount = 0;
    conglomInvoiceRef = null;
    conglomInvTotalExc = null;
    conglomInvTax = null;
    conglomInvTotalInc = null;
    periodForList = "All";
    invoiceDocketNoForList = "All";
    billedProductForList = "All";
    invoiceDocketNo = null;
    geocodes = "";
    analysts = "";
    billPeriodRef = null;
    conglomDiscountNetAmount = 0;
    itemTypeForList = "All";
    conglomBillProdBilledAmtTotal = 0;
    conglomBillProdVATTotal = 0;
    conglomBillProdInvoiceAmtTotal = 0;
    conglomBillProdItemNo = 0;
    conglomBillProdGrid = "";
    conglomItemStatusGrid = "";
    conglomCRDocketGrid = "";
    msInvoiceCharges = new ArrayList();
    invoiceRegion = null;
    customerRadius = null;
    billingRegion = null;
    totalCharges = "0.00";
    companyAddressId = null;
    customerList = new ArrayList();
    DBA = new DBUtilities.DBAccess();
    searchResults = "";
    closedInvoiceGrid = "";
  }
  public void setLoginId(String value)
  {
    loginId = value;
  }
  public String getLoginId()
  {
    return loginId;
  }
  public String getCompanyAddressId()
  {
    return companyAddressId;
  }
  public String getTotalCharges()
  {
    return totalCharges;
  }
  public void setTotalCharges(String value)
  {
    totalCharges = value; 
  }
  public String getCustomerRadius()
  {
    return customerRadius;
  }
  public void setCustomerRadius(String value)
  {
    customerRadius = value;
  }
  public String getBillingRegion()
  {
    return billingRegion;
  }
  public void setBillingRegion(String value)
  {
    billingRegion = value;
  }
  public String getInvoiceRegionName()
  {
    return invoiceRegion;
  }
  public void setInvoiceRegion(String value)
  {
    invoiceRegion = value;
  }
  public double getConglomDiscountNetAmount()
  {
    return conglomDiscountNetAmount;
  }
  public String getBillPeriodRef()
  {
    return billPeriodRef;
  }
  public String getGeocodes()
  {
    return geocodes;
  }
  public void setGeocodes(String value)
  {
    geocodes = value;
  }
  public String getAnalysts()
  {
    return analysts;
  }
  public void setAnalysts(String value)
  {
    analysts = value;
  }
  public void setInvoiceDocketNo(String value)
  {
    invoiceDocketNo = value;
  }
  public String getInvoiceDocketNo()
  {
    return invoiceDocketNo;
  }
  public void setItemTypeForList(String value)
  {
    itemTypeForList = value;
  }
  public String getItemTypeForList()
  {
    return itemTypeForList;
  }
  public void setPeriodForList(String value)
  {
    periodForList = value;
  }
  public String getPeriodForList()
  {
    return periodForList;
  }
  public void setInvoiceDocketNoForList(String value)
  {
    invoiceDocketNoForList = value;
  }
  public String getInvoiceDocketNoForList()
  {
    return invoiceDocketNoForList;
  }
  public void setBilledProductForList(String value)
  {
    billedProductForList = value;
  }
  public String getBilledProductForList()
  {
    return billedProductForList;
  }
  public long getConglomCustomerId()
  {
    return conglomCustomerId;
  }
  public void setConglomCustomerId(long value)
  {
    conglomCustomerId = value;
  }
  public long getConglomProductCount()
  {
    return conglomProductCount;
  }
  public void setConglomProductCount(long value)
  {
    conglomProductCount = value;
  }
  public String getConglomInvTotalInc()
  {
    return conglomInvTotalInc;
  }
  public int getConglomBillProdItemNo()
  {
    return conglomBillProdItemNo;
  }
  public String getConglomBillProdBilledAmtTotal()
  {
    return reformatAmount(conglomBillProdBilledAmtTotal);
  }
  public String getConglomBillProdVATTotal()
  {
    return reformatAmount(conglomBillProdVATTotal);
  }
  public String getConglomBillProdInvoiceAmtTotal()
  {
    return reformatAmount(conglomBillProdInvoiceAmtTotal);
  }
  private String reformatAmount(double amt)
  {
    BigDecimal bd = new BigDecimal(Double.toString(amt));
    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    //return bd.doubleValue();
    StringBuffer sb = new StringBuffer(Double.toString(bd.doubleValue()));
    if (sb.charAt(sb.length()-2) == '.')
    {
      sb.append("0");
    }
    return sb.toString();
  }
  public void setConglomInvTotalInc(String value)
  {
    conglomInvTotalInc = value;
  }
  public String getConglomInvTax()
  {
    return conglomInvTax;
  }
  public void setConglomInvTax(String value)
  {
    conglomInvTax = value;
  }
  public String getConglomInvTotalExc()
  {
    return conglomInvTotalExc;
  }
  public void setConglomInvTotalExc(String value)
  {
    conglomInvTotalExc = value;
  }
  public String getConglomInvoiceRef()
  {
    return conglomInvoiceRef;
  }
  public void setConglomInvoiceRef(String value)
  {
    conglomInvoiceRef = value;
  }
  public String getConglomPeriod()
  {
    return conglomPeriod;
  }
  public void setConglomPeriod(String value)
  {
    conglomPeriod = value;
  }
  public String getConglomFrequency()
  {
    return conglomFrequency;
  }
  public void setConglomFrequency(String value)
  {
    conglomFrequency = value;
  }
  public String getConglomCustomerName()
  {
    return conglomCustomerName;
  }
  public void setConglomCustomerName(String value)
  {
    conglomCustomerName = value;
  }
  public String getGSOButtonChecked(String button)
  {
    return gsoSubmitMode.equals(button)?"checked":"";
  }
  public String getGSOSubmitMode()
  {
    return gsoSubmitMode;
  }
  public void setGSOSubmitMode(String value)
  {
    gsoSubmitMode = value;
  }
  public String getGSOTrialButtonStatus()
  {
    return gsoTrialButtonStatus;
  }
  public String getGSOCloseButtonStatus()
  {
    return gsoCloseButtonStatus;
  }
  public String getGSOExtractButtonStatus()
  {
    return gsoExtractButtonStatus;
  }
  public void setGSOJobId(String value)
  {
    gsoJobId = value;
  }
  public void setGSOMessageDate(String value)
  {
    gsoMessageDate = value;
  }
  public String getGSOJobId()
  {
    return gsoJobId;
  }
  public String getGSOMessageDate()
  {
    return gsoMessageDate;
  }
  public String getGSOBillingPeriod()
  {
    return gsoBillingPeriod;
  }
  public String getGSOStatus()
  {
    return gsoStatus;
  }
  public String getGSORejects()
  {
    return gsoRejects;
  }
  public String getGSOExceptions()
  {
    return gsoExceptions;
  }
  public String getServicePartnerName()
  {
    return servicePartnerName;
  }
  public String getSPName()
  {
    return SU.hasNoValue(servicePartnerName, "");
  }
  public void setServicePartnerName(String value)
  {
    servicePartnerName = value;
  }
  public long getServicePartnerId()
  {
    return servicePartnerId;
  }
  public void setServicePartnerId(long value)
  {
    servicePartnerId = value;
  }
  public String getInvoiceNo()
  {
    return invoiceNo;
  }
  public void setInvoiceNo(String value)
  {
    invoiceNo = value;
  }
/************************************************************************************************/
  public void setInvoiceNoAcc(String value)
  {
    invoiceNo = value;
    resetAccountId();
  }
  private void resetAccountId()
  {
    try
    {
      SQL = "SELECT Account_Id FROM gcd..Monthly_Billing_Invoice_Region " +
        "WHERE Invoice_No = '" + invoiceNo + "' " +
        "AND Global_Customer_Id = '" + Global_Customer_Id + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Account_Id = RS.getString(1);
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
/************************************************************************************************/
  public String getDiscountPlan()
  {
    return discountPlan;
  }
  public void setDiscountPlan(String value)
  {
    discountPlan = value;
  }
  public String getSheduleCrystalChecked()
  {
    return scheduleCrystal?"checked":"";
  }
  public boolean getSheduleCrystal()
  {
    return scheduleCrystal;
  }
  public void setScheduleCrystal(boolean value)
  {
    scheduleCrystal = value;
  }
  public String getBackdatesChecked(String value)
  {
    return backdates.equals(value)?"checked":"";
  }
  public String getBackdates()
  {
    return backdates;
  }
  public void setBackdates(String value)
  {
    backdates = value;
  }
  public String getRunTimeChecked(String value)
  {
    return runTime.equals(value)?"checked":"";
  }
  public String getRunTime()
  {
    return runTime;
  }
  public void setRunTime(String value)
  {
    runTime = value;
  }
  public String getRunTypeChecked(String value)
  {
    return runType.equals(value)?"checked":"";
  }
  public String getRunType()
  {
    return runType;
  }
  public void setRunType(String value)
  {
    runType = value;
  }
  public String getMBLogGrid()
  {
    return mbLogGrid;
  }
  public void setMBLogGrid(String value)
  {
    mbLogGrid = value;
  }
  public String getTaxPayable()
  {
    return taxPayable;
  }
  public String getTaxRate()
  {
    return taxRate;
  }
  public String getTaxType()
  {
    return taxType;
  }
  public String getOutgoingCurrencyCode()
  {
    return outgoingCurrencyCode;
  }
  public String getOutgoingCurrencyDesc()
  {
    return outgoingCurrencyDesc;
  }
  public String getTaxReferenceLiteral()
  {
    return taxReferenceLiteral;
  }
  public String getTaxReference()
  {
    return taxReference;
  }
  public String getAccountId()
  {
    return accountId;
  }
  public void setAccountId(String value)
  {
    accountId = value;
  }
  public String getBillingCustomerName()
  {
    return billingCustomerName;
  }
  public String getBillingContact()
  {
    return billingContact;
  }
  public String getCompanyName()
  {
    return companyName;
  }
  public String getCompanyDetails1()
  {
    return SU.hasNoValue(companyDetails1,"&nbsp;");
  }
  public String getCompanyDetails2()
  {
    return SU.hasNoValue(companyDetails2,"&nbsp;");
  }
  public String[] getBillingAddress()
  {
    String addr[] = {SU.hasNoValue(billingAddress1,"&nbsp;"),
      SU.hasNoValue(billingAddress2,"&nbsp;"),
      SU.hasNoValue(billingAddress3,"&nbsp;"),
      SU.hasNoValue(billingAddress4,"&nbsp;"),
      SU.hasNoValue(billingAddress5,"&nbsp;"),
      SU.hasNoValue(billingAddress6,"&nbsp;"),
      SU.hasNoValue(billingAddress7,"&nbsp;")};
    if ((addr[3].equals(addr[5])) && (!addr[5].equals("&nbsp;")))
    {
      addr[5] = "&nbsp;";
    }
    if ((addr[4].equals(addr[6])) && (!addr[6].equals("&nbsp;")))
    {
      addr[6] = "&nbsp;";
    }
    return addr;
  }
  public String[] getBankAddress()
  {
    String addr[] = {SU.hasNoValue(bankAddress1,"&nbsp;"),
      SU.hasNoValue(bankAddress2,"&nbsp;"),
      SU.hasNoValue(bankAddress3,"&nbsp;"),
      SU.hasNoValue(bankAddress4,"&nbsp;"),
      SU.hasNoValue(bankAddress5,"&nbsp;"),
      SU.hasNoValue(bankAddress6,"&nbsp;"),
      SU.hasNoValue(bankAddress7,"&nbsp;")};
    return addr;
  }
  public String[] getAltBankAddress()
  {
    String addr[] = {SU.hasNoValue(altBankAddress1,"&nbsp;"),
      SU.hasNoValue(altBankAddress2,"&nbsp;"),
      SU.hasNoValue(altBankAddress3,"&nbsp;"),
      SU.hasNoValue(altBankAddress4,"&nbsp;"),
      SU.hasNoValue(altBankAddress5,"&nbsp;"),
      SU.hasNoValue(altBankAddress6,"&nbsp;"),
      SU.hasNoValue(altBankAddress7,"&nbsp;")};
    return addr;
  }
  public String[] getCompanyAddress(boolean removeDuplicateName)
  {
    String addr[] = {SU.hasNoValue(companyAddress1,"&nbsp;"),
      SU.hasNoValue(companyAddress2,"&nbsp;"),
      SU.hasNoValue(companyAddress3,"&nbsp;"),
      SU.hasNoValue(companyAddress4,"&nbsp;"),
      SU.hasNoValue(companyAddress5,"&nbsp;"),
      SU.hasNoValue(companyAddress6,"&nbsp;"),
      SU.hasNoValue(companyAddress7,"&nbsp;")};
    if ((removeDuplicateName) && (companyName.equals(addr[0])))
    {
      for (int i=1; i<7; i++)
      {
        addr[i-1] = addr[i];
      }
      addr[6] = "&nbsp;";
    }
    return addr;
  }
  public String getCustomerContact()
  {
    return customerContact;
  }
  public String getAccDetsCustomer()
  {
    return accDetsCustomer;
  }
  public String getAccDetsPeriod()
  {
    return accDetsPeriod;
  }
  public String getAccDetsDOB()
  {
    return accDetsDOB;
  }
  public int getAccDetsAccounts()
  {
    return accDetsAccounts;
  }
  public String getAccDetsStatus()
  {
    return accDetsStatus;
  }
  public String getAccDetsExceptions()
  {
    return accDetsExceptions;
  }
  public String getExceptionStatus()
  {
    return exceptionStatus;
  }
  public void setExceptionStatus(String value)
  {
    exceptionStatus = value;
  }
  public String getExceptionIRIN()
  {
    return exceptionIRIN;
  }
  public void setExceptionIRIN(String value)
  {
    exceptionIRIN = value;
  }
  public int getExceptionType()
  {
    return exceptionType;
  }
  public void setExceptionType(int value)
  {
    exceptionType = value;
  }
  public int getExceptionId()
  {
    return exceptionId;
  }
  public void setExceptionId(int value)
  {
    exceptionId = value;
  }
  public String getBPSD()
  {
    return billPeriodStartDate;
  }
  public void setBPSD(String value)
  {
    billPeriodStartDate = value;
  }
  public String getBPED()
  {
    return billPeriodEndDate;
  }
  public void setBPED(String value)
  {
    billPeriodEndDate = value;
  }
  public String getIRINForList()
  {
    return iRINForList;
  }
  public void setIRINForList(String value)
  {
    iRINForList = value;
  }
  public String getExceptionStatusForList()
  {
    return exceptionStatusForList;
  }
  public void setExceptionStatusForList(String value)
  {
    exceptionStatusForList = value;
  }
  public String getExceptionTypeForList()
  {
    return exceptionTypeForList;
  }
  public void setExceptionTypeForList(String value)
  {
    exceptionTypeForList = value;
  }
  public boolean getRefreshCustomerGrid()
  {
    return refreshCustomerGrid;
  }
  public void setRefreshCustomerGrid(boolean value)
  {
    refreshCustomerGrid = value;
  }
  public String getJobQueue()
  {
    return jobQueue;
  }
  public void setJobQueue(String value)
  {
    jobQueue = value;
  }
  public String getBillingTeam()
  {
    return billingTeam;
  }
  public void setBillingTeam(String value)
  {
    billingTeam = value;
  }
  public String getLastDOB()
  {
    return lastDOB;
  }
  public void setLastDOB(String value)
  {
    lastDOB = value;
  }
  public String getSapDate()
  {
    return sapDate;
  }
  public void setSapDate(String value)
  {
    sapDate = value;
  }
  public String getBillMonth()
  {
    return billMonth;
  }
  public void setBillMonth(String value)
  {
    billMonth = value;
  }
  public String getActAsLogon()
  {
    return actAsLogon;
  }
  public void setActAsLogon(String value)
  {
    actAsLogon = value;
  }
  public String getLogonGroup()
  {
    return logonGroup;
  }
  public void setLogonGroup(String value)
  {
    logonGroup = value;
  }
  public String getDesktopSortOrder()
  {
    return desktopSortOrder;
  }
  public void setDesktopSortOrder(String value)
  {
    desktopSortOrder = value;
  }

  public String getSiteVisibility()
  {
    return siteVisibility;
  }
  public String getC2RefNo()
  {
    return c2RefNo;
  }
  public void setC2RefNo(String value)
  {
    c2RefNo = value;
  }
  public void setSiteVisibility(String value)
  {
    siteVisibility = value;
  }
  public String getNostroUserId()
  {
    return Nostro_User_Id;
  }
  public String getBillingSource()
  {
    return Billing_Source;
  }
  public void setNostroUserId(String value)
  {
    Nostro_User_Id=value;
  }
  public void setBillingSource(String value)
  {
    Billing_Source = value;
  }
  public void setBillingSource()
  {
    findBillingSource();
  }
  public void setGroupName(String value)
  {
    groupName=value;
  }
  public String getPaymentGroupId()
  {
    return Integer.toString(Payment_Group_Id);
  }
  public void setPaymentGroupId(String pgId)
  {
    Payment_Group_Id=pgId.equals("")?0:Integer.parseInt(pgId);
  }
  public void setEdifySetCustomer(boolean value)
  {
    edifySetCustomer = value;
  }
  public boolean getEdifySetCustomer()
  {
    return edifySetCustomer;
  }
  public String getBanIdentifier()
  {
   return SU.isNull(banIdentifier,"");
  }
  public String getGlobalCustomerId()
  {
    return Global_Customer_Id;
  }
  public String getProduct()
  {
    return Product;
  }
  public String getAccountName()
  {
    return Account_Name;
  }
  public String getAccountFilter()
  {
    return Account_Filter;
  }
  public String getSurname_Filter()
  {
    return Surname_Filter;
  }
  public String getCLI_Filter()
  {
    return CLI_Filter;
  }
  public String getCLI_Id()
  {
    return CLI_Id;
  }
  public String getCR_DR()
  {
    return CR_DR;
  }
  public String getCLI()
  {
    return CLI;
  }
  public String getCredit_Amount()
  {
    float cr_amount=Float.parseFloat(Credit_Amount);
    Credit_Amount=Float.toString(cr_amount);
    return Credit_Amount;
  }
  public String getCredit_Description()
  {
    return Credit_Description;
  }
  public String getCLI_Details_Customer_Name()
  {
    return CLI_Details_Customer_Name;
  }
  public String getCLI_Details_Staff_Number()
  {
    return CLI_Details_Staff_Number;
  }
  public String getCLI_Details_User_Name()
  {
    return CLI_Details_User_Name;
  }
  public String getCLI_Details_CLI()
  {
    return CLI_Details_CLI;
  }
  public String getCLI_Details_Charge_From()
  {
    return CLI_Details_Charge_From;
  }
  public String getCLI_Details_Charge_To()
  {
    return CLI_Details_Charge_To;
  }
  public String getCLI_Details_Call_Charge()
  {
    return CLI_Details_Call_Charge;
  }
  public String getCLI_Details_Calls_Charge()
  {
    return CLI_Details_Calls_Charge;
  }
  public String getCLI_Details_Management_Charge()
  {
    return CLI_Details_Management_Charge;
  }
  public String getCLI_Details_Markup_Charge()
  {
    return CLI_Details_Markup_Charge;
  }
  public String getCredit_Id()
  {
    return Credit_Id;
  }
  public String getCredit_Details_Customer_Name()
  {
    return Credit_Details_Customer_Name;
  }
  public String getCredit_Details_Staff_Number()
  {
    return Credit_Details_Staff_Number;
  }
  public String getCredit_Details_User_Name()
  {
    return Credit_Details_User_Name;
  }
  public String getCredit_Details_CLI()
  {
    return Credit_Details_CLI;
  }
  public String getCredit_Details_Charge_From()
  {
    return Credit_Details_Charge_From;
  }
  public String getCredit_Details_Charge_To()
  {
    return Credit_Details_Charge_To;
  }
  public String getCredit_Details_Call_Charge()
  {
    return Credit_Details_Call_Charge;
  }
  public String getCredit_Details_Calls_Charge()
  {
    return Credit_Details_Calls_Charge;
  }
  public String getCredit_Details_Management_Charge()
  {
    return Credit_Details_Management_Charge;
  }
  public String getCredit_Details_Markup_Charge()
  {
    return Credit_Details_Markup_Charge;
  }
  public String getCredit_Details_Total_Credit()
  {
    return Credit_Details_Total_Credit;
  }
  public String getCredit_Details_Calls_Credit()
  {
    return Credit_Details_Calls_Credit;
  }
  public String getCredit_Details_Management_Credit()
  {
    return Credit_Details_Management_Credit;
  }
  public String getCredit_Details_Markup_Credit()
  {
    return Credit_Details_Markup_Credit;
  }
  public String getCredit_Details_Credit_Date()
  {
    return Credit_Details_Credit_Date;
  }
  public String getCredit_Details_Credit_Description()
  {
    return Credit_Details_Credit_Description;
  }
  public String getCredit_Details_CR_DR()
  {
    return Credit_Details_CR_DR;
  }
  public void setCredit_Id(String value)
  {
    Credit_Id=SU.isNull(value,"");
  }
  public String getProductName()
  {
    return Product_Name;
  }
  public String getService_Reference()
  {
    return SU.isNull(Service_Reference,"N/A");
  }
  public String getC2Ref()
  {
    return SU.isNull(C2_Ref,"N/A");
  }
  public String getCircuit_Reference()
  {
    return Circuit_Reference;
  }
  public String getDivision_Id()
  {
    return Division_Id;
  }
  public String getDivisionName()
  {
    if (Division_Name == null)
    {
      return DBA.getDivisionName(Division_Id);
    }
    else if (Division_Name.equals(""))
    {
      return DBA.getDivisionName(Division_Id);
    }
    else
    {
      return Division_Name;
    }
  }
  public String getGlobalCustomerName()
  {
    if (Global_Customer_Name == null)
    {
      return DBA.getGlobalCustomerName(Global_Customer_Id);
    }
    else if (Global_Customer_Name.equals(""))
    {
      return DBA.getGlobalCustomerName(Global_Customer_Id);
    }
    else
    {
      return Global_Customer_Name;
    }
  }
  public String getInvoice_Region()
  {
    return SU.isNull(Invoice_Region,"");
  }
  public String getBanStatus()
  {
    return banStatus;
  }
  public String getBanAuthorisedBy()
  {
    return banAuthorisedBy;
  }
  public String getReturn_BAN_To_Name()
  {
    return Return_BAN_To_Name;
  }
  public String getRequired_BAN_Effective_Date()
  {
    if (Required_BAN_Effective_Dateh != null)
    {
      return SU.reformatDate(Required_BAN_Effective_Dateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getBanCreatedBy()
  {
    return banCreatedBy;
  }
  public String getBAN_Reason()
  {
    return BAN_Reason;
  }
  public String getBAN_Summary()
  {
    return BAN_Summary;
  }
  public String getMessage()
  {
    String temp=SU.isNull(Message,"");
    if ((temp.length() > 0) && (!temp.startsWith("<font")))
      temp = "<font color=red><b>ERROR! Please contact Systems Support with " +
        "the following information: </b></font>" +
        temp;
    return temp;
  }
  public int getRecordCount()
  {
    return 0;
  }
  public String getBansToList()
  {
    return bansToList;
  }
   public String getAction()
  {
    return action;
  }
  public String getProposedBanDate()
  {
    if (proposedBanDate != null)
    {
      return SU.reformatDate(proposedBanDate.toString());
    }
    else
    {
      return "";
    }
  }
  public String getchargeDateHeading()
  {
    return ChargeDateHeading;
  }
  public String getSite()
  {
    return SU.isNull(site,"");
  }
  public String getSiteDisplay()
  {
    return siteDisplay;
  }
  public String getBANs_to_List()
  {
    return SU.isNull(BANs_to_List,"");
  }
  public String getGSR_for_List()
  {
    return GSR_for_List;
  }
  public String getDivision_for_List()
  {
    return Division_for_List;
  }
  public String getAccount_for_List()
  {
    return Account_for_List;
  }
  public String getInvoice_Region_for_List()
  {
    return Invoice_Region_for_List;
  }
  public String getGlobal_Customer_Id_for_List()
  {
    return Global_Customer_Id_for_List;
  }
  public String getUser_for_List()
  {
    return User_for_List;
  }
  public String getUser_Staff_Number()
  {
    return User_Staff_Number;
  }
  public String getPG_for_List()
  {
    return PG_for_List;
  }
  public String getStatus_for_List ()
  {
    return Status_for_List;
  }
  public String getBAN_Month_for_List ()
  {
    return Month_for_List;
  }
  public String getInvoice_for_List ()
  {
    return Invoice_for_List;
  }
  public String getVisit_Month_for_List ()
  {
    return Visit_Month_for_List;
  }
  public String getCreated_Month_for_List ()
  {
    return Created_Month_for_List;
  }
  public String getAccount_Id()
  {
    return Account_Id;
  }
  public String getMode()
  {
   return Mode;
  }
  public String getRejectReason()
  {
    return rejectReason;
  }
  public String getRejectVisibility()
  {
    if ((banStatus.compareTo("Rejected")!=0) && (banStatus.compareTo("Returned")!=0))
    {
      return "hidden";
    }
    else
    {
      return "visible";
    }
  }
  public String getSelectMode()
  {
    return SelectMode;
  }
  public String getProcessMode()
  {
    return ProcessMode;
  }
  public String getInputMode()
  {
    return InputMode;
  }
  public String getCalendar()
  {
    return Calendar;
  }
  public String getCurrency_Desc()
  {
    return SU.isNull(Currency_Desc,"");
  }
  public String getCarrier_Name()
  {
    return SU.isNull(Carrier_Name,"");
  }
  public int getVDay()
  {
    return VDay;
  }
  public int getVMonth()
  {
    return VMonth;
  }
  public int getVYear()
  {
    return VYear;
  }
  public String getGroupName()
  {
    return groupName;
  }
  /***********************************************************************/
  public String getHeader(String System)
  {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");
    String imagename1,imagename2;
    String pos = System.equals("OSS")?"265":"182";
    if (OrderBy.compareTo("StatusSortAsc")==0)
    {
      imagename1="sorted_by.gif";
      imagename2="sort_by.gif";
    }
    else
    {
      imagename2="sorted_by.gif";
      imagename1="sort_by.gif";
    }
    if (BANs_to_List.startsWith("Invoice"))
    {
      HB.append("<br><table border=0><tr class=gridHeader>");
      HB.append("<td class=gridHeader NOWRAP width=110>BAN Id</td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Customer</td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Invoice Region<br><image name=\"Region\" onclick=\"sendOrderBy('Region')\" align=right width=13 height=8 src=\"/shared/nr/cw/newimages/").append(imagename1).append("\"></td>");
      HB.append("<td class=gridHeader NOWRAP width=120>Billing Customer Name<br><image onclick=\"sendOrderBy('Customer')\" name=\"Billing\" valign=bottom align=right width=13 height=8 src=\"/shared/nr/cw/newimages/").append(imagename2).append("\"></td>");
      HB.append("<td class=gridHeader NOWRAP width=90>Currency</td><td class=gridHeader NOWRAP width=45>Select</td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr></table>");
      columns=8;
    }
    else
    {//Main List
      if (System.compareTo("OSS")==0)
      {
        HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer/Carrier</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Circuit</button></td>");
      }
      else if (System.compareTo("Nostro")==0)
      {
        HB.append("<td class=gridHeader NOWRAP valign=top width=130><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=130><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('Payment_Group')\">Payment Group</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=130><button class=grid_menu onclick=\"Toggle_Menu('User')\">User</button></td>");
      }
      else if (System.compareTo("AdHoc")==0)
      {
        HB.append("<td class=gridHeader NOWRAP valign=top width=130><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=130><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Account</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('Invoice')\">Invoice Id</button></td>");
      }
      else if (System.compareTo("Credit")==0)
      {
        HB.append("<td class=gridHeader NOWRAP valign=top width=120><button class=grid_menu onclick=\"Toggle_Menu('Staff No')\">Staff No</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=170><button class=grid_menu onclick=\"Toggle_Menu('User Name')\">User Name</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=140><button class=grid_menu onclick=\"Toggle_Menu('CLI')\">CLI</button></td>");
      }
      else
      {
        HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Account_List')\">Division/Region</button></td>");
        HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Service</button></td>");
      }
      HB.append("<td class=gridHeader NOWRAP valign=top width=75><button id=StatusSort url=\"StatusSort\" class=grid_menu onclick=\"Toggle_Menu('Status')\">Status");
      HB.append("</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type");
      HB.append("</button></td>");
      if (System.compareTo("AdHoc")!=0)
        HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Month')\">BAN Date</button></td>");
      else
        HB.append("<td class=gridHeader NOWRAP valign=top width=100><button class=grid_menu onclick=\"Toggle_Menu('Invoice_Month')\">C" +
          (action.equals("Processed")?"omple":"rea") + "ted Date</button></td>");
      if ((System.compareTo("Nostro")!=0) && (System.compareTo("AdHoc")!=0))
        HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      if (System.compareTo("AdHoc")!=0)
        HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>BAN Id</button></td>");
      else
        HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Visit_Month')\">Visit Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Now add the filters
      HB.append("<TR><TD class=grid1><SPAN id=Customer ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 300px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBA.getListBox("BAN_Customers","submit",Global_Customer_Id_for_List,"",1,"style=\"width:300px\"",true));
      }
      else if (System.compareTo("Nostro")==0)
      {
        HB.append(DBA.getListBox("Nostro_BAN_Customers","submit",Global_Customer_Id_for_List,""));
      }
      else if (System.compareTo("AdHoc")==0)
      {
        HB.append(DBA.getListBox("Ad_Hoc_Customers","submit",Global_Customer_Id_for_List,action));
      }
      else
      {
        HB.append(DBA.getListBox("GCB_BAN_Customers","submit",Global_Customer_Id_for_List,""));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=\"Account_List\" ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBA.getListBox("BAN_Accounts","submit",Account_for_List,Global_Customer_Id_for_List));
      }
      else if (System.compareTo("Nostro")==0)
      {
        HB.append(DBA.getListBox("Nostro_BAN_Accounts","submit",Account_for_List,Global_Customer_Id_for_List));
      }
      else if (System.compareTo("AdHoc")==0)
      {
        HB.append(DBA.getListBox("Ad_Hoc_Accounts","submit",Account_for_List,action));
      }
      else
      {
	HB.append(DBA.getListBox("BAN_Invoice_Regions","submit",Account_for_List,Global_Customer_Id_for_List));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id="+(System.equals("Nostro")?"Payment_Group ":System.equals("AdHoc")?"Invoice ":"GSR "));
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBA.getListBox("BAN_Circuits","submit",GSR_for_List,Global_Customer_Id_for_List+"|"+Account_for_List));
      }
      else if (System.compareTo("AdHoc")==0)
      {
        HB.append(DBA.getListBox("Ad_Hoc_Invoices","submit",Invoice_for_List,action));
      }
      else if (System.compareTo("Nostro")==0)
      {
        HB.append(DBA.getListBox("Nostro_BAN_Payment_Groups","submit",PG_for_List,Global_Customer_Id_for_List));
        HB.append("</tr></td></table></SPAN></TD>");
        HB.append("<TD colspan=1 class=grid1><SPAN id=User ");
        HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: "+pos+"\">");
        HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
        HB.append("<TR><TD class=grid1>");
        HB.append(DBA.getListBox("Nostro_BAN_Users","submit",User_for_List,Global_Customer_Id_for_List));
      }
      else
      {
	HB.append(DBA.getListBox("BAN_Services","submit",GSR_for_List,Global_Customer_Id_for_List));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Status ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if ((action.equals("View")) || (action.equals("Active")) ||
        (action.equals("Processed")) || ((System.equals("AdHoc")) && (action.equals("Amend"))))
      {
        if (System.equals("AdHoc"))
        {
          HB.append(DBA.getListBox("Ad_Hoc_Ban_Status","submit",Status_for_List,action));
        }
        else
        {
          HB.append(DBA.getListBox("Ban_Status","submit",Status_for_List,""));
        }
      }
      else
	HB.append(DBA.getListBox("Ban_Status","DISABLED",Status_for_List,""));

      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Type");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      String banType = System.equals("AdHoc")?"Ad_Hoc_Type":((System.equals("Nostro")?"Nostro_":"") + "BAN_Type");
      String qV = System.equals("Nostro")?"Nostro%":"";
      HB.append(DBA.getListBox(banType,"submit",BAN_Type_for_List,qV));
      HB.append("</tr></td></table></SPAN></TD>");

      String colspan = System.equals("Nostro")?"3":System.equals("AdHoc")?"1":"4";
      HB.append("<TD colspan="+colspan+" class=grid1><SPAN id="+(System.equals("AdHoc")?"I_":"")+"Month");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 130px; POSITION: absolute; TOP: "+pos+"\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("AdHoc")==0)
        HB.append(DBA.getListBox("Invoice_Month","submit",Created_Month_for_List,"", 1, " style=\"WIDTH: 130px;\"", false));
      else
        HB.append(DBA.getListBox("List_Month","submit",Month_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      if (System.compareTo("AdHoc")==0)
      {
        HB.append("<TD class=grid1><SPAN id=V_Month");
        HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 130px; POSITION: absolute; TOP: "+pos+"\">");
        HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
        HB.append("<TR><TD class=grid1>");
        HB.append(DBA.getListBox("Visit_Month","submit",Visit_Month_for_List,"", 1, " style=\"WIDTH: 130px;\"", false));
        HB.append("</tr></td></table></SPAN></TD>");
        HB.append("<TD class=grid1></TD>");
      }
      HB.append("</TR></TABLE>");

    }
    return HB.toString();
  }
  /***********************************************************************/
  public String getGCBHeader()
  {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");
    String imagename1,imagename2;
    if (OrderBy.compareTo("StatusSortAsc")==0)
    {
      imagename1="sorted_by.gif";
      imagename2="sort_by.gif";
    }
    else
    {
      imagename2="sorted_by.gif";
      imagename1="sort_by.gif";
    }
    HB.append("<td class=gridHeader NOWRAP valign=top width=200><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=200><button class=grid_menu onclick=\"Toggle_Menu('Invoice_Region')\">Invoice Region</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=75><button id=StatusSort url=\"StatusSort\" class=grid_menu onclick=\"Toggle_Menu('Status')\">Status");
    HB.append("</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type");
    HB.append("</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=100><button class=grid_menu onclick=\"Toggle_Menu('BAN_Month')\">C" +
      (action.equals("Processed")?"omple":"rea") + "ted Date</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>BAN Id</button></td>");
    HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

    //Now add the filters
    HB.append("<TR><TD class=grid1><SPAN id=\"Customer\" ");
    HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 300px; POSITION: absolute; TOP: 170\">");
    HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
    HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("GCB_BAN_Customers","submit",Global_Customer_Id_for_List,""));
    HB.append("</tr></td></table></SPAN></TD>");

    HB.append("<TD class=grid1><SPAN id=\"Invoice_Region\" ");
    HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 170\">");
    HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
    HB.append("<TR><TD class=grid1>");
    HB.append(DBA.getListBox("GCB_BAN_Invoice_Regions","submit",Invoice_Region_for_List,""));
    HB.append("</tr></td></table></SPAN></TD>");


    HB.append("<TD colspan=1 class=grid1><SPAN id=Status ");
    HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 170\">");
    HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
    HB.append("<TR><TD class=grid1>");
    if ((action.equals("View")) || (action.equals("Active")) ||
      (action.equals("Processed")))
    {
        HB.append(DBA.getListBox("Ban_Status","submit",Status_for_List,""));
    }
    else
      HB.append(DBA.getListBox("Ban_Status","DISABLED",Status_for_List,""));

    HB.append("</tr></td></table></SPAN></TD>");

    HB.append("<TD colspan=1 class=grid1><SPAN id=Type");
    HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 170\">");
    HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
    HB.append("<TR><TD class=grid1>");
    String banType = "New_BAN_Type";
    String qV = "GCB";
    HB.append(DBA.getListBox(banType,"submit",BAN_Type_for_List,qV));
    HB.append("</tr></td></table></SPAN></TD>");

    String colspan = "3";
    HB.append("<TD colspan="+colspan+" class=grid1><SPAN id=BAN_Month");
    HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 130px; POSITION: absolute; TOP: 170\">");
    HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
    HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("List_Month","submit",Month_for_List,""));
    HB.append("</tr></td></table></SPAN></TD>");

    HB.append("</TR></TABLE>");

    return HB.toString();
  }
/******************************************************************************/
  public String getBanType()
  {
    return SU.isNull(banType,"Unknown");
  }
  public void setChargeType(String newchargeType)
  {
    chargeTypeDisplay=(SU.after(newchargeType));
    chargeType = SU.before(newchargeType);
    if (chargeType.compareTo("01")==0)
    {
      ChargeDateHeading="Charge Valid From Date";
      ChargeFieldVisibility="visible";
      CRDB="Debit";
    }
    else if (chargeType.compareTo("02")==0)
    {
      ChargeDateHeading="Charge Due Date";
      ChargeFieldVisibility="hidden";
      CRDB="Debit";
    }
    else if ((chargeType.compareTo("03")==0) || (chargeType.compareTo("04")==0))
    {
      ChargeDateHeading="Credit Payable Date";
      ChargeFieldVisibility="hidden";
      CRDB="Credit";
    }
    else if (chargeType.compareTo("05")==0)
    {
      ChargeDateHeading="Adjustment Date";
      ChargeFieldVisibility="hidden";
    }
    else
    {
      ChargeDateHeading="Charge Date";
      ChargeFieldVisibility="hidden";
    }
  }
  public void setGSR_for_List(String value)
  {
    GSR_for_List=SU.isNull(value,"All");
  }
  public void setAccount_for_List(String value)
  {
    Account_for_List=SU.isNull(value,"All");
  }
  public void setInvoice_Region_for_List(String value)
  {
    Invoice_Region_for_List=SU.isNull(value,"All");
  }
  public void setDivision_for_List(String value)
  {
    Division_for_List=SU.isNull(value,"All");
  }
  public void setGlobal_Customer_Id_for_List(String value)
  {
    Global_Customer_Id_for_List=SU.isNull(value,"All");
  }
  public void setUser_for_List(String value)
  {
    User_for_List=SU.isNull(value,"All");
  }
  public void setUser_Staff_Number(String value)
  {
    User_Staff_Number=value;
  }
  public void setPG_for_List(String value)
  {
    PG_for_List=SU.isNull(value,"All");
  }
  public void setInvoice_for_List(String value)
  {
    Invoice_for_List=SU.isNull(value,"All");
  }
  public void setVisit_Month_for_List(String value)
  {
    Visit_Month_for_List=SU.isNull(value,"All");
  }
  public void setCreated_Month_for_List(String value)
  {
    Created_Month_for_List=SU.isNull(value,"All");
  }
  public void setBAN_Type_for_List(String value)
  {
    BAN_Type_for_List=SU.isNull(value,"All");
  }
  public void setStatus_for_List(String value)
  {
    Status_for_List=SU.isNull(value,"All");
  }
  public void setBAN_Month_for_List(String value)
  {
    Month_for_List=SU.isNull(value,"All");
  }
  public void setCharge_Type_for_List(String value)
  {
    Charge_Type_for_List=SU.isNull(value,"All");
  }
  public void setBusiness_Unit_for_Report(String value)
  {
    Business_Unit_for_Report=SU.isNull(value,"All");
    //Segment_for_Report="All";
    //Customer_for_Report="All";
  }
  public String getBusiness_Unit_for_Report()
  {
    return Business_Unit_for_Report;
  }
  public void setSegment_for_Report(String value)
  {
    Segment_for_Report=SU.isNull(value,"All");
    //Customer_for_Report="All";
  }
  public String getSegment_for_Report()
  {
    return Segment_for_Report;
  }
  public void setCustomer_for_Report(String value)
  {
    Customer_for_Report=SU.isNull(value,"All");
  }
  public String getCustomer_for_Report()
  {
    return Customer_for_Report;
  }
  public void resetReportFilters()
  {
    Business_Unit_for_Report="All";
    Segment_for_Report="All";
    Customer_for_Report="All";
  }
  public void setMRUValues()
  {
      Global_Customer_Id_for_List=SU.isNull(Global_Customer_Id,"All");
      Account_for_List=SU.isNull(Invoice_Region,"All");
      GSR_for_List=SU.isNull(Service_Reference,"All");
      BAN_Type_for_List=SU.isNull(chargeType,"All");
      Status_for_List=SU.isNull(banStatus,"All");
      Division_for_List=SU.isNull(banStatus,"All");
      Invoice_for_List=SU.isNull(Invoice_for_List,"All");
      Created_Month_for_List=SU.isBlank(SU.reformatDate(SU.isNull(getCreated_Month_for_List(),""),"MMMMM yyyy"),"All");
      Visit_Month_for_List=SU.isBlank(SU.reformatDate(SU.isNull(getVisit_Month_for_List(),""),"MMMMM yyyy"),"All");

      Month_for_List=SU.isBlank(SU.reformatDate(SU.isNull(getRequired_BAN_Effective_Date(),""),"MMMMM yyyy"),"All");
  }
  public void setdefaultFieldModes()
  {
    if ((action.compareTo("Authorise") == 0) ||
        (action.compareTo("View") == 0) ||
	(action.compareTo("History") == 0) ||
	(action.compareTo("Active") == 0) ||
	(action.compareTo("Processed") == 0) ||
        (Mode.compareTo("Cease") ==0) ||
        (Mode.compareTo("Delete") == 0) ||
        ((action.equals("direct")) && (banStatus.equals("Implemented")))
        )
    {
      SelectMode="DISABLED";
      ProcessMode="DISABLED";
      InputMode="READONLY";
      Calendar="no";
    }
    else
    {
      SelectMode="submit";
      ProcessMode="process";
      InputMode="";
      Calendar="";
    }
  }
  /*********************************************************************/
  public void setBanIdentifier(String newBanIdentifier)
  {
   banIdentifier=newBanIdentifier;
  }
  public void setCustomerfromList(String newCustomer)
  {
   Global_Customer_Id=SU.before(newCustomer);
  }
   public void setGlobalCustomerName(String newCustomer)
  {
   Global_Customer_Name = newCustomer;
  }
   public void setProduct(String newProduct)
  {
   Product = newProduct;
   getNewProductName();
  }
  public void setGlobalCustomerId(String value)
  {
    /*if (edifySetCustomer)
    {
      return;
    }*/
    Enumeration e,e2;
    if (Global_Customer_Id.compareTo(value) == 0)
    {
      return;
    }
    Global_Customer_Id = value;
    Account_Id="";
    Circuit_Reference="";

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name, Customer_Radius from givn..Global_Customer");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");

      e=DBA.getResults(SQLBuffer.toString(),P3);

      if (e.hasMoreElements())
      {
        Global_Customer_Name=(String)e.nextElement();
        if (e.hasMoreElements())
        {
          customerRadius=(String)e.nextElement();
        }
      }
      //Usually only one account, so find it
      StringBuffer SQLBuffer2 = new StringBuffer("");
      SQLBuffer2.append("Select Account_Id,OutGoing_Currency_Code from givn_ref..Invoice_Region");
      SQLBuffer2.append(" where Global_Customer_Id = '").append(Global_Customer_Id).append("'");

      e2=DBA.getResults(SQLBuffer2.toString(),P3);
      //Datasource changes e2=DBA.getResults(SQLBuffer2.toString());

      if (e2.hasMoreElements())
      {
        Account_Id=SU.isNull((String)e2.nextElement(),"");
        if (e2.hasMoreElements())
        {
	  Currency_Desc=SU.isNull((String)e2.nextElement(),"");
        }
      }

    }
  }
  public void inheritGlobalCustomerId(String value)
  {
    if (Global_Customer_Id.compareTo(value) == 0)
    {
      return;
    }
    Global_Customer_Id = value;

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer (nolock)");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");

      Global_Customer_Name=DBA.getValue(SQLBuffer.toString(),P3);
      //Datasource changes Global_Customer_Name=DBA.getValue(SQLBuffer.toString());
    }
  }

  public void setCustomerFromAccount(String value)
  {
    Enumeration e;
    Global_Customer_Id = value;

    StringBuffer SQLBuffer = new StringBuffer("");

    if (Global_Customer_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer (nolock)");
      SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
      SQLBuffer.append("'");

      Global_Customer_Name=DBA.getValue(SQLBuffer.toString(),P3);
      //Datasource changes Global_Customer_Name=DBA.getValue(SQLBuffer.toString());
    }
  }
  public void setInvoice_Region(String newInvoice_Region)
  {
   Enumeration e;
   StringBuffer SQLBuffer = new StringBuffer("");
   Invoice_Region = SU.isNull(newInvoice_Region,"");

   if ((Invoice_Region.compareTo("")!=0) && (Mode.compareToIgnoreCase("Create") != 0))
   {
    SQLBuffer.append("Select Account_Id,OutGoing_Currency_Code from givn_ref..Invoice_Region (nolock)");
    SQLBuffer.append(" where Global_Customer_Id = '").append(Global_Customer_Id);
    SQLBuffer.append("' and Invoice_Region = '").append(Invoice_Region).append("'");

    //DBA.setSQL(SQLBuffer.toString());

      e=DBA.getResults(SQLBuffer.toString(),P3);
      //Datasource Changes e=DBA.getResults(SQLBuffer.toString());

    if (e.hasMoreElements())
    {
      Account_Id=SU.isNull((String)e.nextElement(),"");
        if (e.hasMoreElements())
        {
            Currency_Desc=SU.isNull((String)e.nextElement(),"");
        }
    }
   }
  }

  public void setAccount_Id(String value)
  {
    String This_Customer_Id="";

    if (value.compareTo(Account_Id) !=0)
    {
      Account_Id = SU.isNull(value,"");

      Enumeration e;
      StringBuffer SQLBuffer = new StringBuffer("");

      if (Account_Id.compareTo("")!=0)
      {
	SQLBuffer.append("Select Invoice_Region,OutGoing_Currency_Code from givn_ref..Invoice_Region (nolock)");
	SQLBuffer.append(" where Account_Id = '").append(Account_Id).append("'");

	e=DBA.getResults(SQLBuffer.toString(),P3);

	if (e.hasMoreElements())
	{
	  Invoice_Region=SU.isNull((String)e.nextElement(),"");
	  Currency_Desc=SU.isNull((String)e.nextElement(),"");
	}
      }
    }
  }
  public void setAccount_Id(String value,boolean ripple)
  {
    String This_Customer_Id="";

    if (value.compareTo(Account_Id) !=0)
    {
      Account_Id = SU.isNull(value,"");

      Enumeration e;
      StringBuffer SQLBuffer = new StringBuffer("");

      if ((ripple) && (Account_Id.compareTo("")!=0))
      {//ripple up value from account to customer
	SQLBuffer.append("Select upper(Global_Customer_Id),Invoice_Region,OutGoing_Currency_Code from givn_ref..Invoice_Region (nolock)");
	SQLBuffer.append(" where Account_Id = '").append(Account_Id).append("'");

	e=DBA.getResults(SQLBuffer.toString(),P3);

	if (e.hasMoreElements())
	{
	  This_Customer_Id=SU.isNull((String)e.nextElement(),"");
	  Invoice_Region=SU.isNull((String)e.nextElement(),"");
	  Currency_Desc=SU.isNull((String)e.nextElement(),"");
	  if (Global_Customer_Id.compareTo(This_Customer_Id) !=0)
	  {
	    setCustomerFromAccount(This_Customer_Id);
	  }
	}
      }
    }
  }
  public void setBanStatus(String newbanStatus)
  {
   banStatus = newbanStatus;
  }
  public void setService_Reference(String newService_Reference)
  {
    Service_Reference = newService_Reference;
  }
  public void setC2Ref(String newC2_Ref)
  {
    C2_Ref = newC2_Ref;
  }
  public void setCircuit_Reference(String value)
  {
    String This_Account_Id,This_Status;

    if (value.compareTo(Circuit_Reference) !=0)
    {
      Circuit_Reference = SU.isNull(value,"");

    }
  }
  public void setCircuit_Reference(String value,boolean ripple)
  {
    String This_Account_Id,This_Status;

    if (value.compareTo(Circuit_Reference) !=0)
    {
      Circuit_Reference = SU.isNull(value,"");

      if (ripple)
      {  //need to ripple up to Account

        StringBuffer SQLBuffer = new StringBuffer("");
        Enumeration e;

        if (Circuit_Reference.compareTo("")!=0)
        {
	  SQLBuffer.append("Select Account_Id from OSS..Circuit (nolock)");
	  SQLBuffer.append(" where CW_Circuit_Ref = '").append(Circuit_Reference).append("'");
	  SQLBuffer.append(" and Circuit_Status <> 'X'");

	  //Datasource changes This_Account_Id=DBA.getValue(SQLBuffer.toString(),P5);
	  This_Account_Id=DBA.getValue(SQLBuffer.toString(),P5);

	  if (This_Account_Id.indexOf("No Data Found") == -1)
	  {
	    if (This_Account_Id.compareTo(Account_Id) !=0)
	    {
	      setAccount_Id(This_Account_Id,true);
	    }
	  }
	  else
	  {
	    Message = "<font color=red><b>Invalid Circuit Reference";
	    Global_Customer_Id="";
	    Account_Id="";
	  }
        }
      }
    }
  }
  public void setIsDirect(boolean inVal)
  {
    isDirect = inVal;
  }
  public boolean getIsDirect()
  {
    return isDirect;
  }
  public void setBanCreatedBy(String newbanCreatedBy)
  {
    banCreatedBy = newbanCreatedBy;
  }
  public void setReturn_BAN_To_Name(String newReturn_BAN_To_Name)
  {
    Return_BAN_To_Name = newReturn_BAN_To_Name;
  }
  public void setBAN_Summary(String newBAN_Summary)
  {
    BAN_Summary = newBAN_Summary;
  }
  public void setBAN_Reason(String newBAN_Reason)
  {
   BAN_Reason = newBAN_Reason;
  }
  public void setRequired_BAN_Effective_Date(String newRequired_BAN_Effective_Date)
  {
    Required_BAN_Effective_Dateh = SU.toJDBCDate(newRequired_BAN_Effective_Date);
  }
  public void setProposedBanDate(String newproposedBanDate)
  {
    proposedBanDate = SU.toJDBCDate(newproposedBanDate);
  }
  public void setUserId(String newuserId)
  {
     userId = newuserId;
  }
  public void setRejectReason(String newrejectReason)
  {
    rejectReason = newrejectReason;
  }
  public void setOrderBy(String newOrderBy)
  {
    OrderBy = newOrderBy;
  }
  public void setCurrency_Desc(String value)
  {
   Currency_Desc = SU.before(SU.isNull(value,""));
  }
  public void setCarrier_Name(String value)
  {
   Carrier_Name = SU.isNull(value,"");
  }
  public void setAccountName(String value)
  {
   Account_Name = SU.isNull(value,"");
  }
  public void setAccountFilter(String value)
  {
   Account_Filter = SU.isNull(value,"");
  }
  public void setSurname_Filter(String value)
  {
   Surname_Filter = SU.isNull(value,"");
  }
  public void setCLI_Filter(String value)
  {
   CLI_Filter = SU.isNull(value,"");
  }
  public void setCLI(String value)
  {
   CLI = SU.isNull(value,"");
  }
  public void setCredit_Amount(String value)
  {
   Credit_Amount = SU.isNull(value,"");
  }
  public void setCredit_Description(String value)
  {
   Credit_Description = SU.isNull(value,"");
  }
  public void setCLI_Id(String value)
  {
   CLI_Id = SU.isNull(value,"");
  }
  public void setCR_DR(String value)
  {
   CR_DR = SU.isNull(value,"");
  }
  public void setDivision_Id(String value)
  {
    Division_Id=value;
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setMessage(String newMessage)
  {
    Message=newMessage;
  }
  public void setMode(String value)
  {
    Mode=value;
    setdefaultFieldModes();
  }
  public void setSite(String newSite)
  {//site can contain spaces
    siteDisplay=newSite;
    if (newSite.length() > 8)
    {
      site = newSite.substring(0,8);
    }
    else
    {
      site=newSite;
    }
  }
  public void setBANs_to_List(String value)
  {
    BANs_to_List=value;
    Message="Please select the required filter values";
    if (BANs_to_List.compareTo("All BANs")==0)
    {
      Global_Customer_Id_for_List="All";
      Account_for_List="All";
      Invoice_Region_for_List="All";
      GSR_for_List="All";
      User_for_List="All";
      PG_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
      Division_for_List="All";
    }
    else if (BANs_to_List.compareTo("All Invoices")==0)
    {
      Global_Customer_Id_for_List="All";
      Account_for_List="All";
      Charge_Type_for_List="All";
      Invoice_for_List="All";
      Visit_Month_for_List="All";
      Created_Month_for_List="All";
      Status_for_List="All";
    }
    else if  (BANs_to_List.startsWith("BANs"))
    {
      Account_for_List="All";
      GSR_for_List="All";
      User_for_List="All";
      PG_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
      Division_for_List="All";
    }
    else if (BANs_to_List.endsWith("Account"))
    {
      GSR_for_List="All";
      User_for_List="All";
      PG_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
      Charge_Type_for_List="All";
      Invoice_for_List="All";
      Visit_Month_for_List="All";
      Created_Month_for_List="All";
    }
    else if (BANs_to_List.endsWith("Payment Group"))
    {
      Account_for_List="All";
      GSR_for_List="All";
      User_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
    else if (BANs_to_List.endsWith("User"))
    {
      GSR_for_List="All";
      PG_for_List="All";
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
    else if (BANs_to_List.endsWith("Service"))
    {
      BAN_Type_for_List="All";
      Status_for_List="All";
      Month_for_List="All";
    }
  }
  public void setShowFilters(String value)
  {
    showFilters=value;
  }
  public void setVDay(int value)
  {
    VDay=value;
  }
  public void setVMonth(int value)
  {
    VMonth=value;
  }
  public void setVYear(int value)
  {
    VYear=value;
  }
/****************************************************************************/
  public void setAction(String newaction)
  {
    action = newaction;
    if (action == null)
    {
      action="View";
    }
    if (action.equals("Amend"))
    {
      bansToList="'Draft','Proposed','Authorised'";
    }
    else if (action.equals("Authorise"))
    {
      bansToList="'Proposed'";
    }
    else if (action.equals("Accept"))
    {
      bansToList="'Authorised'";
    }
    else if (action.equals("Active"))
    {
      bansToList="'Draft','Proposed','Provisional','Returned'";
    }
    else if (action.equals("Processed"))
    {
      bansToList="'Implemented','Exception','Rejected'";
    }
    else
    {
      bansToList="'Draft','Proposed','Authorised','Accepted','Complete','Rejected','Cancelled','Submitted'";
    }
  }
/*******************************************************************************/
  public boolean errors()
  {
    if(errored.isEmpty())
      return false;
    else
      return true;
  }
/******************************************************************************/
public void close ()
{
    try
    {
      RS.close();
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){};
    try
    {
      cstmt.close();
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){};
    try
    {
      Stmt.close();
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){}

    finally {DBA.Pool.free(DBA.Conn);}
}
/******************************************************************************/
public void close (boolean ReleaseConnection)
{
    try
    {
      if (RS != null)
      {
        RS.close();
      }
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){};

    try
    {
      if (cstmt != null)
      {
        cstmt.close();
      }
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){};

    try
    {
      if (Stmt !=null)
      {
        Stmt.close();
      }
    }
    catch(java.sql.SQLException se)
    {
      /*if (!se.toString().endsWith("already Closed"))
      {
	  System.out.println(se);
      }*/
    }
    catch(java.lang.NullPointerException se){}

    finally
    {
      if (ReleaseConnection)
      {
        DBA.Pool.free(DBA.Conn);
      }
    }
}
/*******************************************************************************/
public void close (Connection lConn,boolean release)
{
    try
    {
      RS.close();
    }
    catch(java.sql.SQLException se){System.out.println(se);}
    catch(java.lang.NullPointerException se){};
    try
    {
      cstmt.close();
    }
    catch(java.sql.SQLException se){System.out.println(se);}
    catch(java.lang.NullPointerException se){};
    try
    {
      Stmt.close();
    }
    catch(java.sql.SQLException se){System.out.println(se);}
    catch(java.lang.NullPointerException se){}
    finally
    {
      if (release)
      {
        DBA.Pool.free(lConn);
      }
    }
}
/******************************************************************************/
public void closeupdate ()
{
  try
  {
    if (cstmt != null)
    {
      cstmt.close();
    }

  }
  catch(java.sql.SQLException se)
  {
    //System.out.println(se);
  }
  try
  {
    if (Stmt !=null)
    {
      Stmt.close();
    }
  }
  catch(java.sql.SQLException se)
  {
    //System.out.println(se);
  }
  finally {DBA.Pool.free(DBA.Conn);}
}

/*******************************************************************************/
  public String getBanList(String System)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    RadioButton="width=45 align=center><img src=\""+sharedPath+"/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");
	rowcount = 0;

	if (System.compareTo("OSS") ==0)
	{
	  if (action.compareTo("History") !=0)
	  {
	    SQL = groupName.equals("MANS")?"{call eban..listMANSBANs "
              :"{call eban..listOSSBANs ";
	  }
	  else
	  {
    	    SQL = groupName.equals("MANS")
              ?"{call eban..List_Archived_MANS_BANs "
              :"{call eban..List_Archived_OSS_BANs ";
	  }
	}
	else if (System.compareTo("GCB") ==0)
	{
	  SQL = "{call eban..listGCBBANs ";
	}
	else if (System.compareTo("Nostro") ==0)
	{
	  SQL = "{call eban..listNostroBANs ";
	}

        columns=8;
	if (System.equals("Nostro"))
        {
          SQL += "(?,?,?,?,?,?,?,?,?)}";
        }
        else if (System.equals("GCB"))
        {
          columns=6;
          SQL += "(?,?,?,?,?,?,?)}";
        }
        else
        {
          SQL += "(?,?,?,?,?,?,?,?)}";
        }


	cstmt = DBA.Conn.prepareCall(SQL);
	if (System.equals("GCB"))
        {
          cstmt.setString(1,action);
          cstmt.setString(2,OrderBy);
          cstmt.setString(3,Global_Customer_Id_for_List);
          cstmt.setString(4,Invoice_Region_for_List);
          cstmt.setString(5,BAN_Type_for_List);
          cstmt.setString(6,Status_for_List);
          cstmt.setString(7,Month_for_List);
        }
        else
        {
          cstmt.setString(1,action);
          cstmt.setString(2,OrderBy);
          cstmt.setString(3,Global_Customer_Id_for_List);
          cstmt.setString(4,Account_for_List);
          if (System.equals("Nostro"))
          {
            cstmt.setString(5,PG_for_List);
            cstmt.setString(6,User_for_List);
            cstmt.setString(7,BAN_Type_for_List);
            cstmt.setString(8,Status_for_List);
            cstmt.setString(9,Month_for_List);
          }
          else
          {
            cstmt.setString(5,GSR_for_List);
            cstmt.setString(6,BAN_Type_for_List);
            cstmt.setString(7,Status_for_List);
            cstmt.setString(8,Month_for_List);
          }
        }

        cstmt.executeQuery();

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
	    //Add the extra generated column for the radio button
	    grid.append("<td width=45 class=");
	    grid.append(gridClass);
	    grid.append(RadioButton);
	    grid.append(RS.getString(counter));
	    grid.append("')\"></td></tr>");
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
        return grid.toString();
      else
        return Message;
    }
  }
/*******************************************************************************/
  public boolean findBanType()
  {
    boolean findBanType = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
	SQL = "{call eban..Get_BAN_Type ";
	SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
	    banType=RS.getString(1);
            findBanType = true;
	  }
        }
	catch(java.sql.SQLException se)
	{
	  Message=se.getMessage();
	}
      }
      else
      {
	Message="<font color=red><b>"+DBA.getMessage();
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
      return findBanType;
    }
  }
/*************************************************************************************/
private void getNewProductName()
{
    try{
      SQL = "SELECT Text FROM eban..BAN_Reference_Data " +
        "WHERE Value='" + Product + "' " +
        "AND Datatype='Product'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          Product_Name=RS.getString(1);
        }
        else
          Product_Name="";
      }
    }
    catch(Exception ex)
    {
      Product_Name="";
    }
    finally
    {
      close();
    }
  }
/*************************************************************************************/
public boolean customerInitiated()
{
  boolean init = false;
    try{
      SQL = "SELECT Global_Customer_Id FROM givn..Global_Customer_Application " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' " +
        "AND Application_Id='11'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          init = true;
        }
      }
    }
    catch(Exception ex)
    {
    }
    finally
    {
      close();
      return init;
    }
  }
/*************************************************************************************/
public String getNostroBANList()
{
  int counter=0;
  String RadioButton=
    "width=45 align=center><img src=\""+sharedPath+
    "/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";

  int rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid=new StringBuffer("");
  StringBuffer sb = new StringBuffer();
  columns=8;
  Message="";
  try
  {
    sb.append("select 'width=130 NOWRAP>'+Customer," +
      "'width=130 NOWRAP>'+Account," +
      "'width=110 NOWRAP>'+Payment_Group," +
      "'width=130 NOWRAP>'+User_Name," +
      "'width=75 NOWRAP>' +" +
      "case Status when  'Draft' then '<font color=green><b>'" +
      "when  'Proposed' then '<font color=blue><b>'" +
      "when  'Implemented' then '<font color=magenta><b>'" +
              "when  'Rejected' then '<font color=red><b>'" +
              "else '' end + Status," +
      "'width=90 NOWRAP>' +BAN_Type," +
      "'width=80 NOWRAP>' +Effective_Date," +
      "'width=150 NOWRAP>' + BAN_Identifier," +
      "BAN_Identifier " +
      "from eban..Nostro_BAN_List_View BLV " +
      "where Status in (select BAN_Status_Code FROM eban..BAN_List where Action = '" +
      action + "') ");
    if (!Global_Customer_Id_for_List.equals("All"))
      sb.append("and Global_Customer_Id = '" + Global_Customer_Id_for_List + "' ");
    if (!Account_for_List.equals("All"))
      sb.append("and Account = '" + Account_for_List + "' ");
    if (!PG_for_List.equals("All"))
      sb.append("and Payment_Group = '" + PG_for_List + "' ");
    if (!User_for_List.equals("All"))
      sb.append("and User_Name = '" + User_for_List + "' ");
    if (!Status_for_List.equals("All"))
      sb.append("and Status = '" + Status_for_List + "' ");
    if (!BAN_Type_for_List.equals("All"))
      sb.append("and BAN_Type = '" + BAN_Type_for_List + "' ");
    if (!Month_for_List.equals("All"))
      sb.append("and (datename(month,Effective_Date) + ' ' + " +
      "convert(char(4),datepart(year,Effective_Date)) = '" + Month_for_List +
      "') ");
    SQL=sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P5))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0>");

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
          //Add the extra generated column for the radio button
          grid.append("<td width=45 class=");
          grid.append(gridClass);
          grid.append(RadioButton);
          grid.append(RS.getString(counter));
          grid.append("')\"></td></tr>");
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
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/*************************************************************************************/
public String getAdHocList()
{
  int counter=0;
  String RadioButton=
    "width=45 align=center><img src=\""+sharedPath+
    "/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";

  int rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid=new StringBuffer("");
  StringBuffer sb = new StringBuffer();
  columns=7;
  Message="";
  try
  {
    String viewNo = null;
    if (action.equals("Processed"))
      viewNo = "_2";
    else if (action.equals("Print"))
      viewNo = "_3";
    else
      viewNo = "";

    sb.append("select 'width=130 NOWRAP>'+Customer," +
      "'width=130 NOWRAP>'+case Account when '' then '&nbsp;' else Account end," +
      "'width=110 NOWRAP>'+Invoice_Id," +
      "'width=75 NOWRAP>' +" +
      "case Status when  'Draft' then '<font color=green><b>'" +
      "when  'Provisional' then '<font color=orange><b>'" +
      "when  'Exception' then '<font color=maroon><b>'" +
      "when  'Proposed' then '<font color=blue><b>'" +
      "when  'Implemented' then '<font color=magenta><b>'" +
              "when  'Returned' then '<font color=purple><b>'" +
              "when  'Rejected' then '<font color=red><b>'" +
              "else '' end + Status," +
      "'width=90 NOWRAP>' +Type," +
      "'width=100 NOWRAP>' +Created_Date," +
      "'width=80 NOWRAP>' +Visit_Date, " +
      "Invoice_Id " +
      "from eban..Ad_Hoc_Invoice_List_View" + viewNo + " CLV " +
      "where Status in (select BAN_Status_Code FROM eban..BAN_List where Action = '" +
      action + "') ");
    if (!Global_Customer_Id_for_List.equals("All"))
      sb.append("and Customer_Id = " + Global_Customer_Id_for_List + " ");
    if (!Account_for_List.equals("All"))
      sb.append("and Account = '" + Account_for_List + "' ");
    if (!Invoice_for_List.equals("All"))
      sb.append("and Invoice_Id = '" + Invoice_for_List + "' ");
    if (!Status_for_List.equals("All"))
      sb.append("and Status = '" + Status_for_List + "' ");
    if (!BAN_Type_for_List.equals("All"))
      sb.append("and Type = '" + BAN_Type_for_List + "' ");
    if (!Visit_Month_for_List.equals("All"))
      sb.append("and (datename(month,Visit_Date) + ' ' + " +
      "convert(char(4),datepart(year,Visit_Date)) = '" + Visit_Month_for_List +
      "') ");
    if (!Created_Month_for_List.equals("All"))
      sb.append("and (datename(month,Created_Date) + ' ' + " +
      "convert(char(4),datepart(year,Created_Date)) = '" + Created_Month_for_List +
      "') ");
    sb.append(" order by Invoice_Id desc");
    SQL=sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P5))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0>");

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
          //Add the extra generated column for the radio button
          grid.append("<td width=45 class=");
          grid.append(gridClass);
          grid.append(RadioButton);
          grid.append(RS.getString(counter));
          grid.append("')\"></td></tr>");
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
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/*************************************************************************************/
public String AddCredit(String Credit_Date)
{
  String Message=CR_DR+" added successfully";
  String Connection_Failure_Message="Failed to connect";
  String Failure_Message="";

  float Calls_Credit=0, Management_Credit, Markup_Credit;
  float Supplied_Credit=SU.toFloat(Credit_Amount);
  float Call_Charge=SU.toFloat(CLI_Details_Call_Charge);
  float Calls_Charge=SU.toFloat(CLI_Details_Calls_Charge);
  float Management_Charge=SU.toFloat(CLI_Details_Management_Charge);
  float Markup_Charge=SU.toFloat(CLI_Details_Markup_Charge);

  if (Supplied_Credit==Call_Charge)
  {
    Calls_Credit=Calls_Charge;
    Management_Credit=Management_Charge;
    Markup_Credit=Markup_Charge;
  }
  else
  {
    Calls_Credit=Supplied_Credit*(Calls_Charge/Call_Charge);
    Management_Credit=Supplied_Credit*(Management_Charge/Call_Charge);
    Markup_Credit=Supplied_Credit*(Markup_Charge/Call_Charge);
  }

  if (CR_DR=="credit")
  {
    Calls_Credit*=-1;
    Management_Credit*=-1;
    Calls_Credit*=-1;
  }

  String Insert_Credit [][] = new String [10][3];

  //column names
  Insert_Credit[0][0]="Global_Customer_Id";
  Insert_Credit[1][0]="Created_Date";
  Insert_Credit[2][0]="CLI_Id";
  Insert_Credit[3][0]="Credit_Description";
  Insert_Credit[4][0]="Credit_Date";
  Insert_Credit[5][0]="Calls_Credit";
  Insert_Credit[6][0]="Management_Credit";
  Insert_Credit[7][0]="Markup_Credit";
  Insert_Credit[8][0]="Last_Update_Date";
  Insert_Credit[9][0]="Last_Update_Id";
  //values
  Insert_Credit[0][1]=Global_Customer_Id;
  Insert_Credit[1][1]="GETDATE()";
  Insert_Credit[2][1]=CLI_Id;
  Insert_Credit[3][1]=Credit_Description;
  Insert_Credit[4][1]=Credit_Date;
  if (CR_DR.equals("credit"))
  {
    Insert_Credit[5][1]="ROUND("+Calls_Credit+"*-1,2)";
    Insert_Credit[6][1]="ROUND("+Management_Credit+"*-1,2)";
    Insert_Credit[7][1]="ROUND("+Markup_Credit+"*-1,2)";
  }
  else
  {
    Insert_Credit[5][1]="ROUND("+Calls_Credit+",2)";
    Insert_Credit[6][1]="ROUND("+Management_Credit+",2)";
    Insert_Credit[7][1]="ROUND("+Markup_Credit+",2)";
  }
  Insert_Credit[8][1]="GETDATE()";
  Insert_Credit[9][1]="Create AMEX Credit";
  //character indicator
  Insert_Credit[0][2]="C";
  Insert_Credit[1][2]="";
  Insert_Credit[2][2]="";
  Insert_Credit[3][2]="C";
  Insert_Credit[4][2]="C";
  Insert_Credit[5][2]="";
  Insert_Credit[6][2]="";
  Insert_Credit[7][2]="";
  Insert_Credit[8][2]="";
  Insert_Credit[9][2]="C";

  if (DBA.DBWrite("gcd..AMEX_Credit",Insert_Credit,P4))
  {
    return Message;
  }
  else
  {
    return Connection_Failure_Message;
  }

}
/**************************************************************************************/
public String getCLI_Details()
{

  try
  {

    StringBuffer sb= new StringBuffer();
    String Message="";

    sb.append("select Customer_Name, "+
              "Staff_Number, "+
              "User_Name, "+
              "CLI, "+
              "Charge_From, "+
             "Charge_To, "+
             "Call_Charge, "+
             "Calls_Charge, "+
             "Management_Charge, "+
             "Markup_Charge "+
             "from gcd..Formatted_CLI "+
             "where CLI_Id ="+CLI_Id);

    SQL=sb.toString();
    DBA.setSQL(SQL);

    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();

      try
      {

        while (RS.next())
        {
          CLI_Details_Customer_Name=RS.getString(RS.findColumn("Customer_Name"));
          CLI_Details_Staff_Number=RS.getString(RS.findColumn("Staff_Number"));
          CLI_Details_User_Name=RS.getString(RS.findColumn("User_Name"));
          CLI_Details_CLI=RS.getString(RS.findColumn("CLI"));
          CLI_Details_Charge_From=RS.getString(RS.findColumn("Charge_From"));
          CLI_Details_Charge_To=RS.getString(RS.findColumn("Charge_To"));
          CLI_Details_Call_Charge=RS.getString(RS.findColumn("Call_Charge"));
          CLI_Details_Calls_Charge=RS.getString(RS.findColumn("Calls_Charge"));
          CLI_Details_Management_Charge=RS.getString(RS.findColumn("Management_Charge"));
          CLI_Details_Markup_Charge=RS.getString(RS.findColumn("Markup_Charge"));
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  finally
  {
    close();
    if (Message.equals(""))
      return "";
    else
      return Message;
  }

}/**************************************************************************************/
public String getCredit_Details()
{

  try
  {

    StringBuffer sb= new StringBuffer();
    String Message="";

    sb.append("select Customer_Name, "+
              "Staff_Number, "+
              "User_Name, "+
              "CLI, "+
              "Charge_From, "+
             "Charge_To, "+
             "Call_Charge, "+
             "Calls_Charge, "+
             "Management_Charge, "+
             "Markup_Charge, "+
             "Total_Credit, "+
             "Calls_Credit, "+
             "Management_Credit, "+
             "Markup_Credit, "+
             "Credit_Date, "+
             "Credit_Description, "+
             "CR_DR=CASE WHEN Total_Credit LIKE '-%' THEN 'Credit' ELSE 'Debit' END "+
             "from gcd..Formatted_Credit "+
             "where Credit_Id ="+Credit_Id);

    SQL=sb.toString();
    DBA.setSQL(SQL);

    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();

      try
      {

        while (RS.next())
        {
          Credit_Details_Customer_Name=RS.getString(RS.findColumn("Customer_Name"));
          Credit_Details_Staff_Number=RS.getString(RS.findColumn("Staff_Number"));
          Credit_Details_User_Name=RS.getString(RS.findColumn("User_Name"));
          Credit_Details_CLI=RS.getString(RS.findColumn("CLI"));
          Credit_Details_Charge_From=RS.getString(RS.findColumn("Charge_From"));
          Credit_Details_Charge_To=RS.getString(RS.findColumn("Charge_To"));
          Credit_Details_Call_Charge=RS.getString(RS.findColumn("Call_Charge"));
          Credit_Details_Calls_Charge=RS.getString(RS.findColumn("Calls_Charge"));
          Credit_Details_Management_Charge=RS.getString(RS.findColumn("Management_Charge"));
          Credit_Details_Markup_Charge=RS.getString(RS.findColumn("Markup_Charge"));
          Credit_Details_Total_Credit=RS.getString(RS.findColumn("Total_Credit"));
          Credit_Details_Calls_Credit=RS.getString(RS.findColumn("Calls_Credit"));
          Credit_Details_Management_Credit=RS.getString(RS.findColumn("Management_Credit"));
          Credit_Details_Markup_Credit=RS.getString(RS.findColumn("Markup_Credit"));
          Credit_Details_Credit_Date=RS.getString(RS.findColumn("Credit_Date"));
          Credit_Details_Credit_Description=RS.getString(RS.findColumn("Credit_Description"));
          Credit_Details_CR_DR=RS.getString(RS.findColumn("CR_DR"));
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  finally
  {
    close();
    if (Message.equals(""))
      return "";
    else
      return Message;
  }

}
/*************************************************************************************/
public String getCLIList()
{

  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String RadioButton=
    "width=45 align=center><img src=\""+sharedPath+
    "/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  StringBuffer sb= new StringBuffer();
  String Test_GCID="";
  columns=8;
  Message="";

  try
  {

    if ((User_Staff_Number.compareTo("")==0) &&
        (Surname_Filter.compareTo("")==0) &&
        (CLI.compareTo("")==0))
    {
      Test_GCID = "";
    }
    else
    {
      Test_GCID = Global_Customer_Id;
    }

    sb.append("select 'width=120 NOWRAP>'+Staff_Number, "+
      "'width=170 NOWRAP>'+User_Name, " +
      "'width=140  NOWRAP>'+CLI, "+
      "'width=90  NOWRAP>'+Charge_From, "+
      "'width=90  NOWRAP>'+Charge_To, "+
      "'width=80  align=right NOWRAP>'+Call_Charge, "+
      "'width=80  align=right NOWRAP>'+Call_Volume, "+
      "'width=80  align=right NOWRAP>'+Call_Duration, "+
      "CLI_Id " +
      "from gcd..Formatted_CLI "+
      "where Global_Customer_Id ='"+ Test_GCID +"' ");

    if (User_Staff_Number.compareTo("")!=0)
    {
      sb.append("and Staff_Number = '"+User_Staff_Number+"' ");
    }

    if (Surname_Filter.compareTo("")!=0)
    {
      sb.append("and User_Surname like '"+Surname_Filter.replace('*','%')+"' ");
    }

    if (CLI.compareTo("")!=0)
    {
      sb.append("and CLI like '"+CLI.replace('*','%')+"' ");
    }

    SQL=sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0><tr>");

      try
      {
        while (RS.next())
        {
          rowcount++;
          rows = BigInteger.valueOf(rowcount);
          //Alternate row colours
          if (rows.testBit(0))
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          //add extra generated column for the radio button
          grid.append("<td width=45 class=");
          grid.append(gridClass);
          grid.append(RadioButton);
          grid.append(RS.getString(counter));
          grid.append("')\"></td></tr>");
          //end the table
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/*************************************************************************************/
public String getLLUHistoryList()
{

  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  StringBuffer sb= new StringBuffer();
  columns=4;
  Message="";

  try
  {

    sb.append("select 'width=168 NOWRAP>'+Speed, "+
      "'width=80 NOWRAP>'+CONVERT(VARCHAR(11),BAN_Effective_Date,113), " +
      "'width=100 NOWRAP>'+Last_Update_Id, "+
      "'width=80 NOWRAP>'+CONVERT(VARCHAR(11),Last_Update_Date,113) "+
      "from eban..LLU_Charge_BAN "+
      "where global_service_reference ='"+ Service_Reference +"' "+
      "order by BAN_Effective_Date DESC ");

    SQL=sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0><tr>");

      try
      {
        while (RS.next())
        {
          rowcount++;
          rows = BigInteger.valueOf(rowcount);
          //Alternate row colours
          if (rows.testBit(0))
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          grid.append("</tr>");
          //end the table
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/*************************************************************************************/
public String getCreditList()
{

  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String RadioButton=
    "width=45 align=center><img src=\""+sharedPath+
    "/nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  StringBuffer sb= new StringBuffer();
  String Test_GCID="";
  columns=9;
  Message="";

  try
  {

    if ((User_Staff_Number.compareTo("")==0) &&
        (Surname_Filter.compareTo("")==0) &&
        (CLI.compareTo("")==0))
    {
      Test_GCID = "";
    }
    else
    {
      Test_GCID = Global_Customer_Id;
    }

    sb.append("select 'width=120 NOWRAP>'+Staff_Number, "+
      "'width=170 NOWRAP>'+User_Name, " +
      "'width=140  NOWRAP>'+CLI, "+
      "'width=90  NOWRAP>'+Charge_From, "+
      "'width=90  NOWRAP>'+Charge_To, "+
      "'width=80  align=right NOWRAP>'+Total_Credit, "+
      "'width=80  align=right NOWRAP>'+Call_Charge, "+
      "'width=80  align=right NOWRAP>'+Call_Volume, "+
      "'width=80  align=right NOWRAP>'+Call_Duration, "+
      "Credit_Id " +
      "from gcd..Formatted_Credit "+
      "where Global_Customer_Id ='"+ Test_GCID +"' ");

    if (User_Staff_Number.compareTo("")!=0)
    {
      sb.append("and Staff_Number = '"+User_Staff_Number+"' ");
    }

    if (Surname_Filter.compareTo("")!=0)
    {
      sb.append("and User_Surname like '"+Surname_Filter.replace('*','%')+"' ");
    }

    if (CLI.compareTo("")!=0)
    {
      sb.append("and CLI like '"+CLI.replace('*','%')+"' ");
    }

    SQL=sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0><tr>");

      try
      {
        while (RS.next())
        {
          rowcount++;
          rows = BigInteger.valueOf(rowcount);
          //Alternate row colours
          if (rows.testBit(0))
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          //add extra generated column for the radio button
          grid.append("<td width=45 class=");
          grid.append(gridClass);
          grid.append(RadioButton);
          grid.append(RS.getString(counter));
          grid.append("')\"></td></tr>");
          //end the table
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}

public String getAdHocActive()
{
  int counter=0;
  int rowcount=0;
  int row=0;
  String gridClass = "";
  StringBuffer grid=new StringBuffer("");
  String[] rowArray = new String[4];
  columns=7;
  Message="";
  String rowstart1 =  "<tr><td class=grid1 NOWRAP>";
  String rowend1 =  "</td><td class=grid1 NOWRAP>Active</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td></tr>";
  String rowstart2 =  "<tr><td class=grid2 NOWRAP>";
  String rowend2 =  "</td><td class=grid2 NOWRAP>Active</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td></tr>";
  try
  {
    StringBuffer sb = new StringBuffer("select distinct s.status 'State'," +
      "'Active' 'Status'," +
      "count(*) 'Total'," +
      "sum(case when s2.Created_Date > DATEADD(month, -1, getdate()) " +
        "then 1 else 0 end ) as '<1'," +
      "sum(case when s2.Created_Date between DATEADD(month, -2, getdate()) and DATEADD(month, -1, getdate()) " +
        "then 1 else 0 end ) as '1-2'," +
      "sum(case when s2.Created_Date between DATEADD(month, -3, getdate()) and DATEADD(month, -2, getdate()) " +
        "then 1 else 0 end ) as '2-3'," +
      "sum(case when s2.Created_Date < DATEADD(month, -3, getdate()) " +
        "then 1 else 0 end ) as '>3' " +
      "From eban..Ad_Hoc_Charge_Status_History s, " +
      "eban..Ad_Hoc_Charge_Status_History s2 " +
      "Where s.Charge_Id = s2.Charge_Id " +
      "and s.Created_Date in (select max(Created_Date) from eban..ad_hoc_charge_status_history where status <> 'Printed' group by charge_id) " +
      "and s2.Created_Date in (select min(Created_Date) from eban..ad_hoc_charge_status_history group by charge_id) " +
      "and s.Status in (select Ban_Status_Code from eban..Ban_List where Action = 'Active') ");
    if ((!Business_Unit_for_Report.equals("All")) ||
      (!Segment_for_Report.equals("All")) ||
      (!Customer_for_Report.equals("All")))
    {
      sb.append("and s.Charge_Id in (select Charge_Id from eban..Assure_Charge a, " +
        "eban..Ad_Hoc_Customer c where a.Customer_Id = c.Customer_Id ");
      if (!Business_Unit_for_Report.equals("All"))
        sb.append("and c.Business_Unit = '"+Business_Unit_for_Report+"' ");
      if (!Segment_for_Report.equals("All"))
        sb.append("and c.Segment = '"+Segment_for_Report+"' ");
      if (!Customer_for_Report.equals("All"))
        sb.append("and c.Customer_Id = "+Customer_for_Report+" ");
      sb.append(") ");
    }
    sb.append("group by s.status");
    SQL = sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P5))
    {
      RS = DBA.getResultsSet();

      try
      {
        while (RS.next())
        {
          rowcount++;
          grid=new StringBuffer("");
          grid.append("<tr>");
          for(counter=1;counter<columns+1;counter++)
          {
            String data = RS.getString(counter);
            if (counter == 1)
            {
              if (data.equals("Draft"))
              {
                row = 0;
                gridClass="grid2 ";
              }
              else if (data.equals("Provisional"))
              {
                row = 1;
                gridClass="grid1 ";
              }
              else if (data.equals("Proposed"))
              {
                row = 2;
                gridClass="grid2 ";
              }
              else if (data.equals("Returned"))
              {
                row = 3;
                gridClass="grid1 ";
              }
            }
            grid.append("<td class=");
            grid.append(gridClass);
            if (counter > 2)
              grid.append(" align=\"center\"");
            grid.append(" NOWRAP>");
            grid.append(data);
            grid.append("</td>");
          }
          grid.append("</tr>");
          rowArray[row] = grid.toString();
        }
        grid=new StringBuffer("");
        for (row=0; row<4; row++)
        {
          if ((rowArray[row] == null) || (rowArray[row].equals("")))
          {
            switch (row)
            {
              case 0 :
                rowArray[0] = rowstart2 + "Draft" + rowend2;
                break;
              case 1 :
                rowArray[1] = rowstart1 + "Provisional" + rowend1;
                break;
              case 2 :
                rowArray[2] = rowstart2 + "Proposed" + rowend2;
                break;
              case 3 :
                rowArray[3] = rowstart1 + "Returned" + rowend1;
                break;
            }
          }
          grid.append(rowArray[row]);
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/****************************************************************************************/
  protected int[] setDateItems(java.sql.Date inDate)
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(inDate==null?(new java.util.Date()):inDate);
    int[] dateItems = {cal.get(cal.DATE), cal.get(cal.MONTH)+1, cal.get(cal.YEAR)};
    return dateItems;
  }
/*************************************************************************************/
public String getAdHocProcessed()
{
  int counter=0;
  int rowcount=0;
  int row=0;
  String gridClass = "";
  StringBuffer grid=new StringBuffer("");
  String[] rowArray = new String[3];
  columns=7;
  Message="";
  String rowstart1 =  "<tr><td class=grid1 NOWRAP>";
  String rowend1 =  "</td><td class=grid1 NOWRAP>Processed</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid1 align=\"center\" NOWRAP>0</td></tr>";
  String rowstart2 =  "<tr><td class=grid2 NOWRAP>";
  String rowend2 =  "</td><td class=grid2 NOWRAP>Processed</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td>" +
  "<td class=grid2 align=\"center\" NOWRAP>0</td></tr>";
  try
  {
    StringBuffer sb = new StringBuffer("select distinct s.status 'State'," +
      "'Processed' 'Status'," +
      "sum(case when DATEDIFF(mm, s.Created_Date, getdate()) = 0 " +
        "then 1 else 0 end ) as 'this'," +
      "sum(case when DATEDIFF(mm, s.Created_Date, getdate()) = 1 " +
        "then 1 else 0 end ) as '-1'," +
      "sum(case when DATEDIFF(mm, s.Created_Date, getdate()) = 2 " +
        "then 1 else 0 end ) as '-2'," +
      "sum(case when DATEDIFF(mm, s.Created_Date, getdate()) = 3 " +
        "then 1 else 0 end ) as '-3'," +
      "sum(case when DATEDIFF(mm, s.Created_Date, getdate()) = 4 " +
        "then 1 else 0 end ) as '-4' " +
      "From eban..Ad_Hoc_Charge_Status_History s " +
      "Where s.Created_Date in (select max(Created_Date) from eban..ad_hoc_charge_status_history where status <> 'Printed' group by charge_id) " +
      "and s.Status in (select Ban_Status_Code from eban..Ban_List where Action = 'Processed') ");
    if ((!Business_Unit_for_Report.equals("All")) ||
      (!Segment_for_Report.equals("All")) ||
      (!Customer_for_Report.equals("All")))
    {
      sb.append("and s.Charge_Id in (select Charge_Id from eban..Assure_Charge a, " +
        "eban..Ad_Hoc_Customer c where a.Customer_Id = c.Customer_Id ");
      if (!Business_Unit_for_Report.equals("All"))
        sb.append("and c.Business_Unit = '"+Business_Unit_for_Report+"' ");
      if (!Segment_for_Report.equals("All"))
        sb.append("and c.Segment = '"+Segment_for_Report+"' ");
      if (!Customer_for_Report.equals("All"))
        sb.append("and c.Customer_Id = "+Customer_for_Report+" ");
      sb.append(") ");
    }
    sb.append("group by s.status");
    SQL = sb.toString();
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P5))
    {
      RS = DBA.getResultsSet();

      try
      {
        while (RS.next())
        {
          rowcount++;
          grid=new StringBuffer("");
          grid.append("<tr>");
          for(counter=1;counter<columns+1;counter++)
          {
            String data = RS.getString(counter);
            if (counter == 1)
            {
              if (data.equals("Implemented"))
              {
                row = 0;
                gridClass="grid2 ";
              }
              else if (data.equals("Exception"))
              {
                row = 1;
                gridClass="grid1 ";
              }
              else if (data.equals("Rejected"))
              {
                row = 2;
                gridClass="grid2 ";
              }
            }
            grid.append("<td class=");
            grid.append(gridClass);
            if (counter > 2)
              grid.append(" align=\"center\"");
            grid.append(" NOWRAP>");
            grid.append(data);
            grid.append("</td>");
          }
          grid.append("</tr>");
          rowArray[row] = grid.toString();
        }
        grid=new StringBuffer("");
        for (row=0; row<3; row++)
        {
          if ((rowArray[row] == null) || (rowArray[row].equals("")))
          {
            switch (row)
            {
              case 0 :
                rowArray[0] = rowstart2 + "Implemented" + rowend2;
                break;
              case 1 :
                rowArray[1] = rowstart1 + "Exception" + rowend1;
                break;
              case 2 :
                rowArray[2] = rowstart2 + "Rejected" + rowend2;
                break;
            }
          }
          grid.append(rowArray[row]);
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    if (Message.equals(""))
      return grid.toString();
    else
      return Message;
  }
}
/*************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    BANBean BAN=new BANBean();

    BAN.setAction("Amend");
    BAN.setOrderBy("Status");
    BAN.setGlobal_Customer_Id_for_List("All");
    BAN.setAccount_for_List("All");
    BAN.setGSR_for_List("All");
    BAN.setBAN_Type_for_List("All");
    BAN.setStatus_for_List("All");
    BAN.setBAN_Month_for_List("All");

    DBAccess DBA = new DBAccess();
    DBA.startDB("GCB");

    BAN.getBanList("GCB");
  }
/*****************************************************************************************/
  protected boolean canAuthorise()
  {
    boolean canAuthorise = false;
    try{
      SQL = "SELECT Group_Name from eban..eban_user (nolock) " +
       "WHERE Login_Id = '" + userId + "' " +
       "AND Group_Name = 'Authorise' "
       ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          canAuthorise = true;
        }
        else
        {
          Message="<font color=red><b>You do not have sufficient authority to authorise this BAN";
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
      return canAuthorise;
    }
  }
/*******************************************************************************/
  public String testGetCustomerList(boolean withTable)
  {
    StringBuffer grid = new StringBuffer();
    if (withTable)
      grid.append("<table border=0>");
    for (int i = 1; i < 1001; i++)
    {
      grid.append(
"<tr><td class=grid1 width=397 nowrap>"+i+" AAA aaasdasd                                                                                            </td>" +
"<td class=grid1 width=65 nowrap>February</td><td class=grid1 width=65 nowrap>Monthly  </td>" +
"<td class=grid1 width=55 nowrap>WD03</td><td class=grid1 width=55 nowrap>0</td>" +
"<td class=grid1 width=60 nowrap><font color=red>Not Run</font></td>" +
"<td class=grid1 width=60 nowrap>N</td><td class=grid1 >" +
"<img src=../nr/cw/newimages/buttonA.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Account')\">" +
"<img src=../nr/cw/newimages/buttonD.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','DataBilling')\">" +
"<img src=../nr/cw/newimages/buttonE.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Exceptions')\">" +
"<img src=../nr/cw/newimages/buttonS.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Submit')\">" +
"<img src=../nr/cw/newimages/buttonV.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','ViewBillLog')\">" +
"<img src=../nr/cw/newimages/buttonG.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Extract')\"></td>" +
"</tr>");
    }
    if (withTable)
      grid.append("</table>");
    return grid.toString();
  }
/*******************************************************************************/
  public Collection testGetCustomerList3(String loginId, String orderBy,
    boolean calldb, boolean usedb)
  {
GregorianCalendar now = new GregorianCalendar();
ArrayList al = new ArrayList();
      int counter=0;
      String RadioButton;
      int rowcount;
      BigInteger rows = new BigInteger("0");
      String gridClass;
      //StringBuffer grid=new StringBuffer("");
      Message="";

if (calldb)
{
      try{
        if (DBA.Connect(PREPARE,P5))
        {
          //grid.append("<table border=0>");

          rowcount = 0;

now = new GregorianCalendar();
System.out.println("Time before query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));

          SQL = "{call gcd..Get_Desktop_List_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

          columns=7;

          cstmt.execute();
          RS = cstmt.getResultSet();
now = new GregorianCalendar();
System.out.println("Time after query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
          try
          {
if (usedb)
{
            while (RS.next())
            {
              StringBuffer sb = new StringBuffer("");
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
              sb.append("<tr>");
              for(counter=1;counter<columns+1;counter++)
              {
                sb.append("<td class=");
                sb.append(gridClass);
                sb.append(RS.getString(counter));
                sb.append("</td>");
              }
              //Add the extra generated column for the buttons
              String thisGCID = RS.getString(counter);
              sb.append("<td class=");
              sb.append(gridClass);
              sb.append(">");
              if (loginId.equals("All"))
              {
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
              }
              else
              {
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonA.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Account')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','DataBilling')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonE.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Exceptions')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonS.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Submit')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','ViewBillLog')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonG.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Extract')\">");
              }
              sb.append("</td></tr>");
              //End the table
              al.add(sb.toString());
            }
}
else
{
   for (int i = 1; i < 1001; i++)
    {
      al.add(
"<tr><td class=grid1 width=397 nowrap>"+i+" AAA aaasdasd                                                                                            </td>" +
"<td class=grid1 width=65 nowrap>February</td><td class=grid1 width=65 nowrap>Monthly  </td>" +
"<td class=grid1 width=55 nowrap>WD03</td><td class=grid1 width=55 nowrap>0</td>" +
"<td class=grid1 width=60 nowrap><font color=red>Not Run</font></td>" +
"<td class=grid1 width=60 nowrap>N</td><td class=grid1 >" +
"<img src=../nr/cw/newimages/buttonA.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Account')\">" +
"<img src=../nr/cw/newimages/buttonD.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','DataBilling')\">" +
"<img src=../nr/cw/newimages/buttonE.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Exceptions')\">" +
"<img src=../nr/cw/newimages/buttonS.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Submit')\">" +
"<img src=../nr/cw/newimages/buttonV.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','ViewBillLog')\">" +
"<img src=../nr/cw/newimages/buttonG.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Extract')\"></td>" +
"</tr>");
    }
}
            //grid.append("</table>");
now = new GregorianCalendar();
System.out.println("Time after rs loop:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
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
      }
}
else
{
    for (int i = 1; i < 1001; i++)
    {
      al.add(
"<tr><td class=grid1 width=397 nowrap>"+i+" AAA aaasdasd                                                                                            </td>" +
"<td class=grid1 width=65 nowrap>February</td><td class=grid1 width=65 nowrap>Monthly  </td>" +
"<td class=grid1 width=55 nowrap>WD03</td><td class=grid1 width=55 nowrap>0</td>" +
"<td class=grid1 width=60 nowrap><font color=red>Not Run</font></td>" +
"<td class=grid1 width=60 nowrap>N</td><td class=grid1 >" +
"<img src=../nr/cw/newimages/buttonA.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Account')\">" +
"<img src=../nr/cw/newimages/buttonD.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','DataBilling')\">" +
"<img src=../nr/cw/newimages/buttonE.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Exceptions')\">" +
"<img src=../nr/cw/newimages/buttonS.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Submit')\">" +
"<img src=../nr/cw/newimages/buttonV.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','ViewBillLog')\">" +
"<img src=../nr/cw/newimages/buttonG.gif align=bottom border=0 width=24 height=22 onClick=\"sendSelected('AAA','Extract')\"></td>" +
"</tr>");
    }

}
now = new GregorianCalendar();
System.out.println("Time last thing:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
      return al;
  }
/*******************************************************************************/
  public Collection testGetCustomerList2(String loginId, String orderBy)
  {
GregorianCalendar now = new GregorianCalendar();
ArrayList al = new ArrayList();
      int counter=0;
      String RadioButton;
      int rowcount;
      BigInteger rows = new BigInteger("0");
      String gridClass;
      //StringBuffer grid=new StringBuffer("");
      Message="";

      try{
        if (DBA.Connect(PREPARE,P5))
        {
          //grid.append("<table border=0>");

          rowcount = 0;

          SQL = "{call gcd..Get_Desktop_List_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

          columns=7;
now = new GregorianCalendar();
System.out.println("Time before query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));

          cstmt.execute();
          RS = cstmt.getResultSet();
now = new GregorianCalendar();
System.out.println("Time after query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
          try
          {
            while (RS.next())
            {
              StringBuffer sb = new StringBuffer("");
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
              sb.append("<tr>");
              for(counter=1;counter<columns+1;counter++)
              {
                sb.append("<td class=");
                sb.append(gridClass);
                sb.append(RS.getString(counter));
                sb.append("</td>");
              }
              //Add the extra generated column for the buttons
              String thisGCID = RS.getString(counter);
              sb.append("<td class=");
              sb.append(gridClass);
              sb.append(">");
              if (loginId.equals("All"))
              {
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
              }
              else
              {
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonA.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Account')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','DataBilling')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonE.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Exceptions')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonS.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Submit')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','ViewBillLog')\">");
                sb.append("<img src="+sharedPath+"/nr/cw/newimages/buttonG.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Extract')\">");
              }
              sb.append("</td></tr>");
              //End the table
              al.add(sb.toString());
            }
            //grid.append("</table>");
now = new GregorianCalendar();
System.out.println("Time after rs loop:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
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
      }
now = new GregorianCalendar();
System.out.println("Time last thing:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));
      return al;
  }
/*******************************************************************************/
  public String getCustomerList(String loginId, String orderBy)
  {
    if (refreshCustomerGrid)
    {
      int counter=0;
      String RadioButton;
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

          SQL = "{call gcd..Get_Desktop_List_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

          columns=7;
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
              grid.append("<tr>");
              for(counter=1;counter<columns+1;counter++)
              {
                grid.append("<td class=");
                grid.append(gridClass);
                grid.append(RS.getString(counter));
                grid.append("</td>");
              }
              //Add the extra generated column for the buttons
              String thisGCID = RS.getString(counter);
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(">");
              if (loginId.equals("All"))
              {
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                  "align=bottom border=0 width=24 height=22>");
              }
              else
              {
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonA.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Account')\">");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','DataBilling')\">");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonE.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Exceptions')\">");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonS.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Submit')\">");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','ViewBillLog')\">");
                grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonG.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisGCID + "','Extract')\">");
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
          customerGrid = grid.toString();
          return customerGrid;
        }
        else
        {
          return Message;
        }
      }
    }
    else
    {
      return customerGrid;
    }
  }
/*******************************************************************************/
  public Collection getCustomerList2(String loginId, String orderBy)
  {
//GregorianCalendar now = new GregorianCalendar();
    if (refreshCustomerGrid)
    {
      customerList = new ArrayList();
      try{
        if (DBA.Connect(PREPARE,P5))

        {
          SQL = "{call gcd..Get_Desktop_List_eBAN2(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

/*now = new GregorianCalendar();
System.out.println("Time before query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/

          cstmt.execute();
          RS = cstmt.getResultSet();
/*now = new GregorianCalendar();
System.out.println("Time after query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
          try
          {
            while (RS.next())
            {
              customerList.add(new DesktopCustomerDescriptor(RS.getString(1),
                RS.getString(2), RS.getString(3), RS.getString(4),
                RS.getString(5), RS.getString(6), RS.getString(7),
                RS.getString(8), RS.getString(9), RS.getString(10)));
            }
/*now = new GregorianCalendar();
System.out.println("Time after rs loop:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
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
/*now = new GregorianCalendar();
System.out.println("Time last thing:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
      }
    }
    return customerList;
  }
/*******************************************************************************/
  public synchronized Collection getCustomerList2Iso(String loginId, String orderBy)
  {
    Vector thisV = null;
    Connection thisConn = null;
    ResultSet thisRS = null;
    CallableStatement thisCstmt = null;
    String thisSQL = null;
//GregorianCalendar now = new GregorianCalendar();
    //if (refreshCustomerGrid)
    //{
      ArrayList thisCustomerList = new ArrayList();
      try{
        thisV = DBA.ConnectIso(PREPARE,P5,null);
        if (thisV != null)
        {
          thisConn = (Connection)thisV.get(0);
          thisSQL = "{call gcd..Get_Desktop_List_eBAN2(?,?)}";
          thisCstmt = thisConn.prepareCall(thisSQL);
          thisCstmt.setString(1,loginId);
          thisCstmt.setString(2,orderBy);
/*now = new GregorianCalendar();
System.out.println("Time before query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/

          thisCstmt.execute();
          thisRS = thisCstmt.getResultSet();
/*now = new GregorianCalendar();
System.out.println("Time after query:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
          try
          {
            while (thisRS.next())
            {
              thisCustomerList.add(new DesktopCustomerDescriptor(thisRS.getString(1),
                thisRS.getString(2), thisRS.getString(3), thisRS.getString(4),
                thisRS.getString(5), thisRS.getString(6), thisRS.getString(7),
                thisRS.getString(8), thisRS.getString(9), thisRS.getString(10)));
            }
/*now = new GregorianCalendar();
System.out.println("Time after rs loop:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
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
        DBA.CloseIso(null, thisCstmt, thisRS, thisConn);
/*now = new GregorianCalendar();
System.out.println("Time last thing:"+now.get(now.HOUR)+"."+now.get(now.MINUTE)+"."+
now.get(now.SECOND)+"."+now.get(now.MILLISECOND));*/
      }
    //}
    return thisCustomerList;
  }
/*******************************************************************************/
  public String getGSOCustomerList(String loginId, String orderBy)
  {
    if (refreshCustomerGrid)
    {
      int counter=0;
      String RadioButton;
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

          SQL = "{call gso..Get_Desktop_List_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

          columns=5;

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
              String thisSPId = RS.getString(counter);
              String thisSPName = RS.getString(++counter);
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonA.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisSPId + "','" +thisSPName + "','AdHoc')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisSPId + "','" +thisSPName + "','Detail')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonE.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisSPId + "','" +thisSPName + "','Exceptions')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonS.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisSPId + "','" +thisSPName + "','Submit')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisSPId + "','" +thisSPName + "','ViewBillLog')\">");
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
          customerGrid = grid.toString();
          return customerGrid;
        }
        else
        {
          return Message;
        }
      }
    }
    else
    {
      return customerGrid;
    }
  }
/*******************************************************************************/
  public String getGSORequestList(String orderBy)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        grid.append("<table border=0>");

        rowcount = 0;

        SQL = "{call gso..Get_Request_List_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,servicePartnerName);
        //cstmt.setString(2,orderBy);

        columns=9;

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
            grid.append("</tr>");
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
  public String getGSOExceptionList(String orderBy)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        grid.append("<table border=0>");

        rowcount = 0;

        SQL = "{call gso..Get_Exception_List_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,servicePartnerName);
        //cstmt.setString(2,orderBy);

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
            for(counter=1;counter<columns+1;counter++)
            {
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(RS.getString(counter));
              grid.append("</td>");
            }
            grid.append("</tr>");
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
  public String getGSOBillingLogList(String orderBy)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try
    {
      grid.append("<table border=0>");

      rowcount = 0;

      SQL = "SELECT 'width=67 nowrap>' + ISNULL(Job_Id, '&nbsp;'), " +
        "'width=90 nowrap>' + ISNULL(CONVERT(char(11),Message_Date,106), '&nbsp;'), " +
        "'width=760 nowrap>' + ISNULL(Message, '&nbsp;') " +
        "FROM gso..Monthly_Billing_Log " +
        "WHERE Global_Customer_Id = '" + servicePartnerName + "' " +
        (gsoJobId.equals("")?" ":("AND Job_Id = '" + gsoJobId + "' ")) +
        (gsoMessageDate.equals("")?" ":("AND CONVERT(char(8),message_date,112) = '" + gsoMessageDate + "' "))
        ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        columns=3;
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
          grid.append("</tr>");
          //End the table
        }
        grid.append("</table>");
        if (rowcount == 0)
        {
          Message="<font color=blue><b>There are no billing logs for this selection</b></font>";
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
  public String getGSORejectList(String orderBy)
  {
    int counter=0;
    String RadioButton;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        grid.append("<table border=0>");

        rowcount = 0;

        SQL = "{call gso..Get_Reject_List_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,servicePartnerId);
        //cstmt.setString(2,orderBy);

        columns=5;

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
            long thisId = RS.getLong(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Detail')\">");
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
  public Collection getConglomCustomerList2(String loginId, String orderBy)
  {
    if (refreshCustomerGrid)
    {
      customerList = new ArrayList();
      try{
        if (DBA.Connect(PREPARE,P5))
        {
          SQL = "{call conglomerate..Get_Desktop_List_eBAN2(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);
          cstmt.execute();
          RS = cstmt.getResultSet();
          try
          {
            while (RS.next())
            {
              customerList.add(new DesktopCustomerDescriptor(RS.getString(1),
                RS.getString(2), RS.getString(3), RS.getString(4),
                RS.getString(5), RS.getString(6), RS.getString(7),
                RS.getLong(8)));
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
      }
    }
    return customerList;
  }
/*******************************************************************************/
  public String getConglomCustomerList(String loginId, String orderBy)
  {
    if (refreshCustomerGrid)
    {
      int counter=0;
      String RadioButton;
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

          SQL = "{call conglomerate..Get_Desktop_List_eBAN(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,loginId);
          cstmt.setString(2,orderBy);

          columns=7;

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
              String thisGCID = RS.getString(counter);
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonP.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','Product')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonB.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','Billing')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonE.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','Exceptions')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonS.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','Submit')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','View')\">");
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonPr.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisGCID + "','BillProfile')\">");
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
          customerGrid = grid.toString();
          return customerGrid;
        }
        else
        {
          return Message;
        }
      }
    }
    else
    {
      return customerGrid;
    }
  }
/*******************************************************************************/
  public String getExceptionList()
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

        SQL = "{call gcd..Get_Data_Exceptions_eBAN(?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,exceptionTypeForList);
        cstmt.setString(3,exceptionStatusForList);
        cstmt.setString(4,iRINForList);
        cstmt.setString(5,GSR_for_List);

        columns=7;

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
            String thisExId = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisExId + "','D')\">");*/
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisExId + "','Update','" + RS.getInt(++counter) + "','" +
              RS.getString(++counter) + "','" + RS.getString(++counter) + "')\">");
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
  public String getConglomExceptionList()
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

        SQL = "{call conglomerate..Get_Exceptions_eBAN(?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,exceptionTypeForList);
        cstmt.setString(3,exceptionStatusForList);
        cstmt.setString(4,periodForList);
        cstmt.setString(5,invoiceDocketNoForList);
        cstmt.setString(6,billedProductForList);

        columns=6;

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
            String thisExId = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisExId + "','Display')\">");*/
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              //thisExId + "','Update')\">");
              thisExId + "','Update','" + RS.getString(++counter) + "','" +
              RS.getString(++counter) + "')\">");
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
  public String getAccountList(String accountType)
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

        SQL = "{call gcd..Get_Account_List_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,accountType);

        columns=accountType.equals("i")?11:9;

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
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            String thisIRId = RS.getString(counter);
            if (thisIRId.equals("-1"))
            {
              grid.append("<input class=listbutton type=button value=\"\">");
              /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22>");*/
              //grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                //"align=bottom border=0 width=24 height=22>");
            }
            else
            {
              if (accountType.equals("i"))
              {
                grid.append("<input class=listbutton type=button value=\"I\" " +
                  "onClick=\"sendSelected('" + thisIRId + "','" + 
                  RS.getString(++counter) + "','" + RS.getString(++counter) + "','" + 
                  RS.getString(++counter) + "','ViewInvoice')\">");
                /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonV.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisIRId + "','ViewInvoice')\">");*/
              }
              else
              {
                grid.append("<input class=listbutton type=button value=\"B\" " +
                  "onClick=\"sendSelected('" + thisIRId + "','BillProfile')\">");
                /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonB.gif " +
                  "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                  thisIRId + "','BillProfile')\">");*/
              }
            }
            grid.append("</td>");
            grid.append("</tr>");
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
  public String getInvoiceReportList()
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

        SQL = "{call givn_ref..Get_Report_List_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);

        columns=3;

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
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            String fileName = RS.getString(counter);
            String status = RS.getString(++counter);
            if (status.equals("Completed"))
            {
              grid.append("<input class=listbutton type=button value=\"I\" " +
                "onClick=\"sendSelected('" + fileName + /*"','" + 
                RS.getString(++counter) + "','" + RS.getString(++counter) + "','" + 
                RS.getString(++counter) +*/ "','ViewReport')\">");
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\"\">");
            }
            grid.append("</td>");
            grid.append("</tr>");
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
  public void getBillProfile()
  {
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call givn..Bill_Profile_MBIR_eBAN(?,?)}";
        //SQL = "{call givn..Bill_Profile_IR_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);
        //cstmt.setString(2,Account_Id);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            customerContact = RS.getString(1);
            companyAddress1 = RS.getString(2);
            companyAddress2 = RS.getString(3);
            companyAddress3 = RS.getString(4);
            companyAddress4 = RS.getString(5);
            companyAddress5 = RS.getString(6);
            companyAddress6 = RS.getString(7);
            companyAddress7 = RS.getString(8);
            companyDetails1 = RS.getString(9);
            companyDetails2 = RS.getString(10);
            companyName = RS.getString(11);
            billingContact = RS.getString(12);
            billingAddress1 = RS.getString(13);
            billingAddress2 = RS.getString(14);
            billingAddress3 = RS.getString(15);
            billingAddress4 = RS.getString(16);
            billingAddress5 = RS.getString(17);
            bankAddress1 = RS.getString(18);
            bankAddress2 = RS.getString(19);
            bankAddress3 = RS.getString(20);
            bankAddress4 = RS.getString(21);
            bankAddress5 = RS.getString(22);
            bankAddress6 = RS.getString(23);
            bankAddress7 = RS.getString(24);
            altBankAddress1 = RS.getString(25);
            altBankAddress2 = RS.getString(26);
            altBankAddress3 = RS.getString(27);
            altBankAddress4 = RS.getString(28);
            altBankAddress5 = RS.getString(29);
            altBankAddress6 = RS.getString(30);
            altBankAddress7 = RS.getString(31);
            billingCustomerName = RS.getString(32);
            accountId = RS.getString(33);
            taxReference = RS.getString(34);
            taxReferenceLiteral = RS.getString(35);
            outgoingCurrencyCode = RS.getString(36);
            outgoingCurrencyDesc = RS.getString(37);
            billingAddress6 = RS.getString(38);
            billingAddress7 = RS.getString(39);
            invoiceRegion = RS.getString(40);
            companyAddressId = RS.getString(41);
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
    }
  }
/*******************************************************************************/
  public void getBillProfile(boolean useInvoice)
  {
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = useInvoice?"{call givn..Bill_Profile_MBIR_eBAN(?,?)}"
          :"{call givn..Bill_Profile_IR_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,useInvoice?invoiceNo:Account_Id);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            customerContact = RS.getString(1);
            companyAddress1 = RS.getString(2);
            companyAddress2 = RS.getString(3);
            companyAddress3 = RS.getString(4);
            companyAddress4 = RS.getString(5);
            companyAddress5 = RS.getString(6);
            companyAddress6 = RS.getString(7);
            companyAddress7 = RS.getString(8);
            companyDetails1 = RS.getString(9);
            companyDetails2 = RS.getString(10);
            companyName = RS.getString(11);
            billingContact = RS.getString(12);
            billingAddress1 = RS.getString(13);
            billingAddress2 = RS.getString(14);
            billingAddress3 = RS.getString(15);
            billingAddress4 = RS.getString(16);
            billingAddress5 = RS.getString(17);
            bankAddress1 = RS.getString(18);
            bankAddress2 = RS.getString(19);
            bankAddress3 = RS.getString(20);
            bankAddress4 = RS.getString(21);
            bankAddress5 = RS.getString(22);
            bankAddress6 = RS.getString(23);
            bankAddress7 = RS.getString(24);
            altBankAddress1 = RS.getString(25);
            altBankAddress2 = RS.getString(26);
            altBankAddress3 = RS.getString(27);
            altBankAddress4 = RS.getString(28);
            altBankAddress5 = RS.getString(29);
            altBankAddress6 = RS.getString(30);
            altBankAddress7 = RS.getString(31);
            billingCustomerName = RS.getString(32);
            accountId = RS.getString(33);
            taxReference = RS.getString(34);
            taxReferenceLiteral = RS.getString(35);
            outgoingCurrencyCode = RS.getString(36);
            outgoingCurrencyDesc = RS.getString(37);
            billingAddress6 = RS.getString(38);
            billingAddress7 = RS.getString(39);
            invoiceRegion = RS.getString(40);
            companyAddressId = RS.getString(41);
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
    }
  }
/*******************************************************************************/
  public Collection getMSInvoiceCharges()
  {
    return msInvoiceCharges;
  }
/*******************************************************************************/
  public Collection getMSInvoiceDetails()
  {
    Message="";
    msInvoiceCharges = new ArrayList();
    ArrayList al = new ArrayList();
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..MS_Invoice_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          while (RS.next())
          {
            al.add(new MSInvoiceDataDescriptor(RS.getString(1), RS.getString(2),
              RS.getString(3), RS.getString(4), RS.getString(5),
              //getBigDecimal(int, int) causes deprecation warnings, but it's the only one that works here. TA - 25/02/2010
              RS.getBigDecimal(6, 2), RS.getBigDecimal(7, 2), RS.getString(8)));
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
    catch(java.lang.NullPointerException ne)
    {
      Message="<font color=red><b>"+ne.getMessage();
    }//message set in underlying code
    catch(java.lang.Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      msInvoiceCharges.addAll(al);
      return al;
    }
  }
/*******************************************************************************/
  public void getConglomBillProfile()
  {
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Bill_Profile_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            customerContact = RS.getString(1);
            companyAddress1 = RS.getString(2);
            companyAddress2 = RS.getString(3);
            companyAddress3 = RS.getString(4);
            companyAddress4 = RS.getString(5);
            companyAddress5 = RS.getString(6);
            companyAddress6 = RS.getString(7);
            companyAddress7 = RS.getString(8);
            companyDetails1 = RS.getString(9);
            companyDetails2 = RS.getString(10);
            companyName = RS.getString(11);
            billingContact = RS.getString(12);
            billingAddress1 = RS.getString(13);
            billingAddress2 = RS.getString(14);
            billingAddress3 = RS.getString(15);
            billingAddress4 = RS.getString(16);
            billingAddress5 = RS.getString(17);
            billingAddress6 = RS.getString(18);
            billingAddress7 = RS.getString(19);
            bankAddress1 = RS.getString(20);
            bankAddress2 = RS.getString(21);
            bankAddress3 = RS.getString(22);
            bankAddress4 = RS.getString(23);
            bankAddress5 = RS.getString(24);
            bankAddress6 = RS.getString(25);
            bankAddress7 = RS.getString(26);
            altBankAddress1 = RS.getString(27);
            altBankAddress2 = RS.getString(28);
            altBankAddress3 = RS.getString(29);
            altBankAddress4 = RS.getString(30);
            altBankAddress5 = RS.getString(31);
            altBankAddress6 = RS.getString(32);
            altBankAddress7 = RS.getString(33);
            billingCustomerName = RS.getString(34);
            accountId = RS.getString(35);
            taxReference = RS.getString(36);
            taxReferenceLiteral = RS.getString(37);
            outgoingCurrencyCode = RS.getString(38);
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
    }
  }
/*******************************************************************************/
  public String getExceptionDetails()
  {
    StringBuffer grid = new StringBuffer("");
    StringBuffer exSQL = new StringBuffer("");
    Message="";
    try
    {
      SQL = "select Display_Name, Field_Name " +
        "from gcd..Exception_Report_Field ERF (nolock), gcd..Data_Billing_Exceptions DBE (nolock) " +
        "where ERF.Exception_Type = DBE.Exception_Type " +
        "and DBE.Exception_Id = " + exceptionId + " " +
        "order by Display_Sequence "
       ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          switch (exceptionType)
          {
            case 6:
            case 9:
              exSQL.append("Select distinct " + RS.getString(1) + ", "  +
                "isnull(" + RS.getString(2) + ",'&nbsp;') Value " +
                "from gcd..Data_Billing_Exceptions DBE (nolock) " +
                "INNER JOIN gcd..Exception_Type ET ON DBE.Exception_Type = ET.Exception_Type " +
                "LEFT OUTER JOIN gcd..Exception_Note EN ON DBE.Exception_Id = EN.Exception_Id " +
                "LEFT OUTER JOIN gcd..Single_Charge C ON DBE.Charge_Id = C.Charge_Id " +
                "where DBE.Exception_Id = " + exceptionId);
              break;
            case 12:
              exSQL.append("Select distinct " + RS.getString(1) + ", "  +
                "isnull(" + RS.getString(2) + ",'&nbsp;') Value " +
                "from gcd..Data_Billing_Exceptions DBE (nolock) " +
                "INNER JOIN gcd..Exception_Type ET ON DBE.Exception_Type = ET.Exception_Type " +
                "LEFT OUTER JOIN gcd..Exception_Note EN ON DBE.Exception_Id = EN.Exception_Id " +
                "LEFT OUTER JOIN gcd..Adjustments C ON DBE.Charge_Id = C.Adjustment_Id " +
                "where DBE.Exception_Id = " + exceptionId);
              break;
            default:
              exSQL.append("Select distinct " + RS.getString(1) + ", "  +
                "isnull(" + RS.getString(2) + ",'&nbsp;') Value " +
                "from gcd..Data_Billing_Exceptions DBE (nolock) " +
                "INNER JOIN gcd..Exception_Type ET ON DBE.Exception_Type = ET.Exception_Type " +
                "LEFT OUTER JOIN gcd..Exception_Note EN ON DBE.Exception_Id = EN.Exception_Id " +
                "LEFT OUTER JOIN gcd..Single_Charge C ON DBE.Charge_Id = C.Charge_Id " +
                "where DBE.Exception_Id = " + exceptionId);
          }
          exSQL.append(" UNION ALL ");
        }
        close();
        SQL = exSQL.replace(exSQL.length() - 11, exSQL.length(), " ").toString();
        DBA.setSQL(SQL);
//System.out.println(SQL);
        if (DBA.Connect(READ,P5))
        {
          RS = DBA.getResultsSet();
          while (RS.next())
          {
            grid.append("<tr><td class=grid1>" + RS.getString(1) + "</td><td class=grid1>" +
              RS.getString(2) + "</td></tr>");
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
    catch(Exception ex)
    {
      Message=ex.getMessage();
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
  public String getConglomExceptionDetails()
  {
    StringBuffer grid = new StringBuffer("");
    StringBuffer exSQL = new StringBuffer("");
    Message="";
    try
    {
      SQL = "select distinct Display_Name, Field_Name, Display_Sequence  " +
        "from conglomerate..Exception_Report_Field ERF (nolock), conglomerate..Conglom_Exception CBE (nolock) " +
        "where ERF.Exception_Type = CBE.Exception_Type " +
        "and CBE.Exception_Id = " + exceptionId + " " +
        "order by Display_Sequence "
       ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          /*exSQL.append("Select distinct " + RS.getString(1) + ", "  +
            "isnull(" + RS.getString(2) + ",'&nbsp;') Value " +
            "from conglomerate..Conglom_Exception CBE (nolock), " +
            "conglomerate..Exception_Type ET (nolock), " +
            "conglomerate..Exception_Note EN (nolock) " +
            "where CBE.Conglom_Cust_Id = " + conglomCustomerId + " " +
            "and  CBE.Exception_Id = " + exceptionId + " " +
            "and CBE.Exception_Type = ET.Exception_Type " +
            "and CBE.Exception_Id *= EN.Exception_Id ");*/
          exSQL.append("Select distinct " + RS.getString(1) + ", "  +
            "isnull(" + RS.getString(2) + ",'&nbsp;') Value " +
            "from conglomerate..Conglom_Exception CBE (nolock) " + 
            "join conglomerate..Exception_Type ET (nolock) " +
	    "on CBE.Exception_Type = ET.Exception_Type " + 
            "left outer join conglomerate..Exception_Note EN (nolock) " + 
	    "on CBE.Exception_Id = EN.Exception_Id " + 
            "where CBE.Conglom_Cust_Id = " + conglomCustomerId + " " +
            "and  CBE.Exception_Id = " + exceptionId);
          exSQL.append(" UNION ALL ");
        }
        close();
        SQL = exSQL.replace(exSQL.length() - 11, exSQL.length(), " ").toString();
        DBA.setSQL(SQL);
//System.out.println(SQL);
        if (DBA.Connect(READ,P5))
        {
          RS = DBA.getResultsSet();
          while (RS.next())
          {
            grid.append("<tr><td class=grid1>" + RS.getString(1) + "</td><td class=grid1>" +
              RS.getString(2) + "</td></tr>");
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
    catch(Exception ex)
    {
      Message=ex.getMessage();
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
  public String getGSOInvoiceCurrency(String servicePartner)
  {
    String currency = "";
    Message="";
    try
    {
      SQL = "select Outgoing_Currency_Code " +
        "from givn_ref..Invoice_Region (nolock) " +
        "where Invoice_Region = '" + servicePartner + "' " +
        "and Account_Id like '8%' "
       ;
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          currency = RS.getString(1);
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
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return currency;
    }
  }
/*******************************************************************************/
  public String getGIVNJobQueue(boolean setMessage)
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    String thisMessage="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call givn..Get_Job_Queue_eBAN()}";
	cstmt = DBA.Conn.prepareCall(SQL);

	columns=5;

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
            grid.append("</tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            if (setMessage)
            {
              thisMessage="<font color=blue>There are no jobs in the queue</font>";
            }
            else
            {
              grid.append("<font color=blue>There are no jobs in the queue</font>");
            }
          }
	}
	catch(java.sql.SQLException se)
        {
	  thisMessage=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      thisMessage=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      thisMessage="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (thisMessage.equals(""))
      {
        setJobQueue(grid.toString());
        return grid.toString();
      }
      else
      {
        setJobQueue("");
        Message = thisMessage;
        return thisMessage;
      }
    }
  }
/*******************************************************************************/
  public String getLogMessages(boolean isConglom)
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

        if (isConglom)
        {
          SQL = "{call conglomerate..Get_Billing_Log_eBAN(?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setLong(1,conglomCustomerId);
        }
        else
        {
          SQL = "{call gcd..Get_MB_Log_eBAN(?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,Global_Customer_Id);
        }

	columns=3;

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
            grid.append("</tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue>There are no log entries for this customer</font>";
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
        setMBLogGrid(grid.toString());
        return grid.toString();
      }
      else
      {
        return Message;
      }
    }
  }
/************************************************************************************************/
  private void findBillingSource()
  {
    Billing_Source = "Unknown";
    if (!Account_Id.equals(""))
    {
      try
      {
        SQL = "SELECT Billing_Source FROM eban..Ad_Hoc_Account (nolock) " +
          "WHERE Account_No='" + Account_Id + "' ";
        DBA.setSQL(SQL);
        if (DBA.Connect(READ,P5))
        {
          RS = DBA.getResultsSet();
          if (RS.next())
          {
            Billing_Source = RS.getString(1);
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
/************************************************************************************************/
  public String findCDSinglesCustomer()
  {
    String custId = null;
    try
    {
      SQL = "SELECT GCID FROM gcd..CD_Singles_Running ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          custId = RS.getString(1);
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
      return custId;
    }
  }
/************************************************************************************************/
  public void getBillPeriodStartDate()
  {
    billPeriodStartDate = "Unknown";
    try
    {
      SQL = "SELECT convert(varchar(26),max(Bill_Period_Start_Date),106) " +
        "FROM gcd..Monthly_Billing (nolock) " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          billPeriodStartDate = RS.getString(1);
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
/************************************************************************************************/
  public void getInvoiceBillDetails()
  {
    billPeriodStartDate = "Unknown";
    billPeriodEndDate = "Unknown";
    totalCharges = "0.00";
    try
    {
      SQL = "SELECT convert(varchar, min(Bill_Period_Start_Date), 103), " +
        "convert(varchar, max(Bill_Period_End_Date), 103), " +
        "convert(varchar,sum(isnull(Gross_Amount_Currency,0) * " +
        "case Charge_Type_Code when '03' then -1 else 1 end)) " +
        "FROM gcd..Monthly_Billing_All_View (nolock) " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' " +
        "AND Invoice_No='" + invoiceNo + "' " +
        "GROUP BY Global_Customer_Division_Id ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          billPeriodStartDate = RS.getString(1);
          billPeriodEndDate = RS.getString(2);
          totalCharges = RS.getString(3);
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
//************************************************************************************************/
  public Collection getInvoiceChargeTotals()
  {
    ArrayList al = new ArrayList();
    try
    {
      SQL = "SELECT case when Region_Id is null or " +
        "replace(Region_Id, 'x', '') = '' then Global_Customer_Division_Id else " +
        "Region_Id + ' / ' + Global_Customer_Division_Id end, " +
        "convert(varchar,sum(isnull(Gross_Amount_Outgoing,0) * " +
        "case Charge_Type_Code when '03' then -1 else 1 end)) " +
        "FROM gcd..Monthly_Billing (nolock) " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' " +
        "AND Invoice_No='" + invoiceNo + "' " +
        "GROUP BY Region_Id, Global_Customer_Division_Id ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          al.add(new StandardInvoiceChargeDescriptor(RS.getString(1),
            RS.getString(2)));
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
      return al;
    }
  }
/************************************************************************************************/
  public Collection getInvoiceAdjustments()
  {
    ArrayList al = new ArrayList();
    try
    {
      SQL = "SELECT Adjustment_Description, " +
        "convert(varchar,isnull(Adjustment_Amount,0)) " +
        "FROM gcd..Adjustments (nolock) " +
        "WHERE Global_Customer_Id='" + Global_Customer_Id + "' " +
        "AND Invoice_No='" + invoiceNo + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          al.add(new StandardInvoiceAdjustmentDescriptor(RS.getString(1),
            RS.getString(2)));
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
      return al;
    }
  }
/*******************************************************************************/
  public String[] getCustomerVATDetails()
  {
    Message="";
    String[] details = new String[3];
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Get_Customer_VAT_Details_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            for (int i = 0; i < 3; i++)
            {
              details[i] = RS.getString(i+1);
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
      return details;
    }
  }
/*******************************************************************************/
  public String[] getStandardInvoiceDetails()
  {
    Message="";
    String[] details = new String[7];
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Get_Standard_Invoice_Totals_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            for (int i = 0; i < 7; i++)
            {
              details[i] = RS.getString(i+1);
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
      return details;
    }
  }
/*******************************************************************************/
  public String[] getStrategicInvoiceDetails()
  {
    Message="";
    String[] details = new String[13];
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Get_Strategic_Invoice_Details_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,invoiceNo);

        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            for (int i = 0; i < 13; i++)
            {
              details[i] = RS.getString(i+1);
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
      return details;
    }
  }
/************************************************************************************************/
  public void getAccountTaxPayable()
  {
    taxPayable = "&nbsp;";
    //billPeriodStartDate = "Unknown";
    try
    {
      /*SQL = "select TD.Tax_Description + ' @ ' + " +
        "convert(varchar(25),convert(numeric(8,2),TR.Tax_Rate*100)) + '%<br>' " +
        "from givn_ref..Invoice_Region_Invoice_Numbers IRIN (nolock), " +
        "givn_ref..Tax_Requirement TR (nolock), " +
        "givn_ref..Tax_Description TD (nolock) " +
        "where IRIN.Global_Customer_Id = '" + Global_Customer_Id + "' " +
        //"and IRIN.Invoice_Region = '" + Invoice_Region + "' " +
        "and IRIN.Invoice_No = '" + invoiceNo + "' " +
        "and TR.Global_Customer_Id = IRIN.Global_Customer_Id " +
        "and TR.Invoice_Region = IRIN.Invoice_Region " +
        "and TR.Tax_Type = TD.Tax_Type ";
*/
      SQL = "select distinct TD.Tax_Description + ' @ ' + " +
        "convert(varchar(25),convert(numeric(8,2),tp.Tax_Rate*100)) + '%<br>', " +
        "TD.Tax_Type, convert(varchar(25),convert(numeric(8,1),tp.Tax_Rate*100)) + '%' " +
        "from  gcd..tax_payable tp (nolock), " +
        "givn_ref..Tax_Description TD (nolock) " +
        "where TP.Invoice_No = '" + invoiceNo + "' " +
        "and TP.Tax_Type = TD.Tax_Type " +
        "UNION " +
        "select distinct TD.Tax_Description + ' @ ' + " +
        "convert(varchar(25),convert(numeric(8,2),at.Tax_Rate*100)) + '%<br>', " +
        "TD.Tax_Type, convert(varchar(25),convert(numeric(8,1),at.Tax_Rate*100)) + '%' " +
        "from  gcd..adjustments_tax at(nolock), " +
        "givn_ref..Tax_Description TD(nolock) " +
        "where at.Invoice_No = '" + invoiceNo + "' " +
        "and at.Tax_Type = TD.Tax_Type ";
//System.out.println(SQL);
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          taxPayable = SU.isNull(RS.getString(1), "&nbsp;");
          taxType = SU.isNull(RS.getString(2), "&nbsp;");
          taxRate = SU.isNull(RS.getString(3), "&nbsp;");
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
/************************************************************************************************/
  private Enumeration getEmailAddress(String userId)
  {
    Vector result = new Vector();
    try
    {
      SQL = "select Email_Address, " +
        "case when datediff(day, Date_Last_Accessed, getdate()) > 90 then 'sus' else 'ok' end " +
        "where Login_Id = '" + userId + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          result.addElement(RS.getString(1));
          result.addElement(RS.getString(2));
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
      return result.elements();
    }
  }
/************************************************************************************************/
  private boolean createPwdEmail(String userId, String email, String password)
  {
    boolean done = false;
    try
    {
      File tokenDir = new File(EBANProperties.getEBANProperty("tokenDir"));
      String fileName = "monitor-readfromfile-" + userId + ".txt";
      File tokenFile = new File(tokenDir, fileName);
      if (tokenFile.exists())
      {
        tokenFile.delete();
      }
      tokenFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(tokenFile));
      bw.write("addr:" + email);
      bw.newLine();
      bw.write("title:Global Customer Billing from Cable & Wireless");
      bw.newLine();
      bw.write("message:" + password);
      bw.newLine();
      bw.write("message:");
      bw.newLine();
      bw.write("message:The password shown above is the new temporary password you have");
      bw.newLine();
      bw.write("message:recently requested from Cable & Wireless GCB. Please use this");
      bw.newLine();
      bw.write("message:exactly as it appears here together with your assigned User Name and");
      bw.newLine();
      bw.write("message:then amend it to a password of your choice when prompted.");
      bw.newLine();
      bw.write("message:");
      bw.newLine();
      bw.write("message:**********************************************************************");
      bw.newLine();
      bw.write("message:This message may contain information which is confidential or privileged.");
      bw.newLine();
      bw.write("message:If you are not the intended recipient, please advise the sender immediately");
      bw.newLine();
      bw.write("message:by reply e-mail and delete this message and any attachments");
      bw.newLine();
      bw.write("message:without retaining a copy");
      bw.newLine();
      bw.write("message:");
      bw.newLine();
      bw.write("message:**********************************************************************");
      bw.newLine();

      bw.close();
      done = true;
    }
    catch (Exception ex)
    {
      Message = ex.getMessage();
    }
    finally
    {
      return done;
    }
  }
/************************************************************************************************/
  public void resetPassword(String userId)
  {
    Enumeration emailDetails = getEmailAddress(userId);
    if (!emailDetails.hasMoreElements())
    {
      Message = "<font color=red>Cannot find email address for this user. " +
        "Please contact Systems Support.</font>";
    }
    else
    {
      String emailAddr = (String)emailDetails.nextElement();
      String loginStatus = (String)emailDetails.nextElement();
      if (loginStatus.equals("sus"))
      {
        Message = "<font color=red>This user has been suspended. " +
          "Please contact Systems Support.</font>";
      }
      else
      {
        String tempPwd = SU.getRandomMixedString(8);
        if (updatePassword(userId, null, tempPwd, true))
        {
          if (createPwdEmail(userId, emailAddr, tempPwd))
          {
            Message = "<font color=blue>A temporary password has been sent.</font>";
          }
        }
        else
        {
          Message = "<font color=red>Unable to reset password. " +
            "Please contact Systems Support.</font>";
        }
      }
    }
  }
/************************************************************************************************/
  public  Enumeration getGivnRefUserSecure(String UserId,String Password)
  {
    Vector result = new Vector();
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Get_User(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,UserId.trim());
        cstmt.setString(2,MD5Helper.createMD5(Password.trim()));

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          result.addElement(RS.getString(1));
          result.addElement(RS.getString(2));
          result.addElement(RS.getString(3));
          result.addElement(RS.getString(4));
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
      return result.elements();
    }
  }
/*****************************************************************************
  public  Enumeration getGivnRefUserSecure(String UserId,String Password)
  {
        SQL = "select Billing_Team,User_Firstname + ' ' + User_Surname, " +
          "isnull(Logon_Group, 'none'), " +
          "case when date_last_changed is null then 'a' when datediff(day, date_last_changed, getdate()) > 90 then 'b' else 'c' end " +
          "from givn_ref..logon l (nolock), " +
          "eban..eban_password p (nolock) " +
          "where l.Logon_Id = '"+ UserId.trim() + "' " +
          "and l.Logon_Id = p.Login_Id " +
          "and p.Current_Password = '"+ MD5Helper.createMD5(Password.trim()) + "'";
          //"and p.Current_Password = '"+ Password.trim() + "'";
          //"and Logon_Password = '"+ MD5Helper.createMD5(Password.trim()) + "'";

	Vector Result = new Vector();
	if (DBA.Connect(READ,P5))
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	      Result.addElement(RS.getString(4));
	    }
	    DBA.Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    DBA.Close();
	  }
	  if (!Result.isEmpty())
	  {
	    Message = "";
	  }
	  else
	  {
	    Message = "<font color=red><b>Invalid User Name or Password</font>";
	  }
	}
	else
	{
	  //Connection failed
	  //Result.addElement("<font color=red<b>"+Message);
	  //Result.addElement("");
	  Message = "<font color=red><b>"+Message;
	}
	return Result.elements();
  }
/************************************************************************************************/
  public boolean updatePassword(String userId, String oldPassword,
    String newPassword, boolean systemChange)
  {
    Message = "<font color=red>Unable to update password</font>";
    boolean upd = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Update_Password(?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,userId.trim());
        cstmt.setString(2,oldPassword==null?null:MD5Helper.createMD5(oldPassword.trim()));
        cstmt.setString(3,MD5Helper.createMD5(newPassword.trim()));
        cstmt.setString(4,systemChange?"y":"n");

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          switch (ret)
          {
            case 0:
              Message = "<font color=blue>Password updated successfully</font>";
              upd = true;
              break;
            case -5:
              Message = "<font color=red>Password has already been changed today</font>";
              break;
            case -6:
              Message = "<font color=red>New password has been used recently</font>";
              break;
            default:
              Message = "<font color=red>Unable to update password</font>";
          }
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
      return upd;
    }
  }
/************************************************************************************************/
  public void updatePassword(String userId, String oldPassword, String newPassword)
  {
    Message = "<font color=red>Unable to update password</font>";
    try
    {
      SQL = "select Logon_Id " +
        "from givn_ref..logon (nolock) " +
        "WHERE Logon_Id = '" + userId + "' " +
        "AND Logon_Password = '" + oldPassword + "'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          SQL = "UPDATE givn_ref..logon " +
            "SET Logon_Password = '" + newPassword + "' " +
            "WHERE Logon_Id = '" + userId + "' " +
            "AND Logon_Password = '" + oldPassword + "'";
          DBA.setSQL(SQL);
          if (DBA.Connect(DBA.NORESULT,P5))
          {
            SQL = "select Logon_Id " +
              "from givn_ref..logon (nolock) " +
              "WHERE Logon_Id = '" + userId + "' " +
              "AND Logon_Password = '" + newPassword + "'";
            DBA.setSQL(SQL);
            if (DBA.Connect(READ,P5))
            {
              RS = DBA.getResultsSet();
              if (RS.next())
              {
                Message = "<font color=blue>Password updated successfully</font>";
              }
            }
          }
        }
        else
        {
          Message = "<font color=red>Original login details do not match</font>";
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
/************************************************************************************************/
  public void updateSessionId(String sessionId)
  {
    try
    {
      SQL = "UPDATE eBAN..eBan_User " +
        "SET Session_Id = '" + sessionId + "' " +
        "WHERE Login_Id = '" + loginId + "' ";
      DBA.setSQL(SQL);
      if (!DBA.Connect(DBA.NORESULT,P5))
      {
        Message="<font color=red>Unable to store session id. Please contact systems support.</font>";
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
/************************************************************************************************/
  public String getSessionId()
  {
    String sessionId = "";
    try
    {
      SQL = "select isnull(Session_Id,'') " +
        "from eBAN..eBan_User (nolock) " +
        "where Login_Id = '" + loginId + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          sessionId = RS.getString(1);
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
      return sessionId;
    }
  }
/************************************************************************************************/
  public void updateException(String status, String note, String userId,
    boolean isConglom)
  {
    Message = "<font color=red>Unable to update exception</font>";
    try
    {
      SQL = "{call " + (isConglom?"conglomerate..Update_Exception_eBAN":"gcd..Update_Exception") + "(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setInt(1,exceptionId);
      cstmt.setString(2,status);
      cstmt.setString(3,note);
      cstmt.setString(4,userId);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        if(RS.getInt(1) == 0)
        {
          Message = "<font color=blue>Exception updated successfully</font>";
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
/*******************************************************************************/
  public void getAccountSummary()
  {
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Get_Account_Summary(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
						accDetsCustomer = RS.getString(1);
						accDetsPeriod = RS.getString(2);
						accDetsDOB = RS.getString(3);
						accDetsAccounts = RS.getInt(4);
						accDetsStatus = RS.getString(5);
						accDetsExceptions = RS.getString(6);
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
    }
  }
/*******************************************************************************/
  public String checkInvoiceForFinance(String invoiceNo)
  {
    Message="";
    String ret = "";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Check_Invoice(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNo);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            accountId = RS.getString(1);
            Account_Name = RS.getString(2);
            Global_Customer_Id = RS.getString(3);
            boolean restricted = RS.getString(4).equalsIgnoreCase("restricted");
            totalCharges = RS.getString(5);
            ret = restricted?"restricted":"ok";
            this.invoiceNo = invoiceNo;
          }
          else
          {
            ret = "not found"; 
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
  public int insertCDSinglesQueue()
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Insert_CD_Singles_Queue(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getInt(1);
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
  public boolean checkBPRCQueue(boolean overnight)
  {
    boolean inQueue = false;
    try
    {
      SQL = "Select Global_Customer_Id from givn..BPRC_" +
        (overnight?"Overnight_":"") + "Queue (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and Jobname = 'GM Billing'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        inQueue = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return inQueue;
    }
  }
/************************************************************************************************/
  public boolean allowBillSubmission()
  {
    boolean allowed = true;
    try
    {
      SQL = "Select Text from eban..BAN_Reference_Data (nolock) " +
        "where Datatype = 'Allow Bill Submission' " +
        "and Value = 'N'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        allowed = !RS.next();
      }
    }
    catch(Exception ex)
    {
      allowed = false;
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return allowed;
    }
  }
/************************************************************************************************/
 public int allowBillSubmission2()
  {
    int allowed = 1;
    try
    {
      SQL = "Select Text from eban..BAN_Reference_Data (nolock) " +
        "where Datatype = 'Allow Bill Submission' " +
        "and Value = 'N'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
         allowed = RS.next()?0:1;
      }
    }
    catch(Exception ex)
    {
      allowed = -1;
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return allowed;
    }
  }
/************************************************************************************************/
  public boolean checkInvoiceExists()
  {
    boolean exists = false;
    try
    {
      SQL = "Select Global_Customer_Id from gcd..Monthly_Billing (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and Invoice_No = '" + invoiceNo + "' " +
        "UNION Select Global_Customer_Id from gcd..Adjustments (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and Invoice_No = '" + invoiceNo + "' ";
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
/************************************************************************************************/
  public boolean checkTrialBills()
  {
    boolean exists = false;
    try
    {
      SQL = "Select Global_Customer_Id from gcd..Monthly_Billing (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and Invoice_Region not like '4%'";
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
/************************************************************************************************/
  public boolean checkInvoiceTax(String invoiceNo)
  {
    boolean exists = false;
    try
    {
      SQL = "Select Invoice_No from gcd..Monthly_Billing_Tax_Archive " +
        "where Invoice_No = '" + invoiceNo + "' ";
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
/************************************************************************************************/
  public boolean checkCanClose()
  {
    boolean canClose = false;
    try
    {
      SQL = "Select Job_Id from gcd..Monthly_Billing_Log (nolock) " +
        "where Message = 'Processing has ended due to errors' " +
        "and Job_Id = (select max(Job_Id) from gcd..Monthly_Billing_Log (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "') " +
        "and Job_Id Not In (Select Job_Id from gcd..Monthly_Billing_Log (nolock) " +
        "where Message Like 'One or more exceptions exist for Customer%with " +
        "a Prevent Close status. These must be resolved before Close processing can occur.' " +
        "and Job_Id = (select max(Job_Id) from gcd..Monthly_Billing_Log (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "')) ";

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        canClose = !RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return canClose;
    }
  }
/************************************************************************************************/
  public boolean billJobRunning()
  {
    boolean running = false;
    try
    {
      SQL = "Select distinct job_id from gcd..Monthly_Billing_Log " +
        "where job_id = '" + billJobId + "' " +
        "and global_customer_id = '" + Global_Customer_Id + "' " +
        "and Message <> 'Submitted'";

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        running = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return running;
    }
  }
/************************************************************************************************/
  public boolean reportsRunning()
  {
    boolean running = false;
    try
    {
      SQL = "Select distinct trial_ind from givn_ref..Report_Request " +
        "where global_customer_id = '" + Global_Customer_Id + "' " +
        "and (request_status = 'Requested' or request_status = 'In Progress')";

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        running = RS.next();
        if (running)
        {    
            Message = "<font color=red><b>Trial report creation is currently running for this customer. " +
            "You will not be able to submit a trial or close request until this has completed. Please " +
            "wait a minute before re-submitting your request.</b></font>";
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
      return running;
    }
  }
/************************************************************************************************/
  public int countBillJobsRunning()
  {
    int running = -1;
    try
    {
      SQL = "Select count(*) " +
        "from givn..Billing_Period_Run_Control " +
        "where jobname = 'GM Billing' " +
        "and Active_Ind = 'Y' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
          running = RS.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return running;
    }
  }
/************************************************************************************************/
  public String determineSubmissionMessage()
  {
    String confirmMsg = null;
    if (runType.equals("close"))
    {
      confirmMsg = "Warning - You are closing the bill for customer " +
        Global_Customer_Id + ". You will not be able to re-open the bill " +
        "after this run. Please confirm you wish to proceed by pressing 'OK'. " +
        "Press 'Cancel' to exit without submitting.";
    }
    else if ((runType.equals("trial")) && (checkAutoClose()))
    {
      confirmMsg = "Warning - " + Global_Customer_Id + " is an auto close customer. " +
        "Please confirm you wish to proceed by pressing 'OK'. " +
        "Press 'Cancel' to exit without submitting.";
    }
    return confirmMsg;
  }
/************************************************************************************************/
  public int submitBillJob()
  {
    int seqNo = -1;
    billJobId = SU.DateToString(new java.util.Date(), "yyyyMMddHHmmss");
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call givn.." +
          (runTime.equals("overnight")?"Insert_Overnight_BPRC":"Insert_BPRC_Queue") +
          "(?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,"GM Billing");
        cstmt.setString(3,runType.equals("close")?"Y":"N");
        cstmt.setString(4,backdates);
        cstmt.setString(5,billJobId);
        cstmt.setString(6,scheduleCrystal?"Y":"N");
        cstmt.setString(7,loginId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          seqNo = RS.getInt(1);
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
      return seqNo;
    }
  }
/************************************************************************************************/
  public boolean crystalToSetup()
  {
    boolean crystalToSetup = false;
    try
    {
      SQL = "select invoice_region from givn_ref..invoice_region (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and crystal_setup = 'N'";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        crystalToSetup = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return crystalToSetup;
    }
  }
/************************************************************************************************/
  public boolean checkCDSinglesQueue()
  {
    boolean inQueue = false;
    try
    {
      SQL = "Select Global_Customer_Id from eban..CD_Singles_Queue (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        inQueue = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return inQueue;
    }
  }
/************************************************************************************************/
  public boolean checkAutoClose()
  {
    boolean autoClose = false;
    try
    {
      SQL = "Select Global_Customer_Id from givn..Global_Customer_Application (nolock) " +
        "where Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "and Application_Id = '01' " +
        "and Auto_Close_Ind = 'Y' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        autoClose = DBA.getResultsSet().next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return autoClose;
    }
  }
/************************************************************************************************/
  public boolean checkConglomGoldfishQueue()
  {
    boolean inQueue = false;
    try
    {
      SQL = "select conglom_cust_id from conglomerate..job_queue " +
        "where jobname = 'Goldfish Extract' " +
        "and conglom_cust_id = " + conglomCustomerId + " " +
        "union select conglom_cust_id from " +
        "conglomerate..billing_period_run_control " +
        "where jobname = 'Goldfish Extract' and active_ind = 'Y' " +
        "and conglom_cust_id = " + conglomCustomerId + " ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        inQueue = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return inQueue;
    }
  }
/************************************************************************************************/
  public String checkForCurrentConglomInvoice()
  {
    String invRef = "none";
    try
    {
      SQL = "select conglom_invoice_ref from conglomerate..conglom_invoice " +
        "where conglom_cust_id = " + conglomCustomerId + " " +
        "and bill_period_ref = substring(convert(char(8),getdate(),2),1,2) + " +
        "substring(convert(char(8),getdate(),2),4,2) ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          invRef = RS.getString(1);
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
      return invRef;
    }
  }
/************************************************************************************************/
  public String getMaxConglomBillPeriod()
  {
    String invRef = "none";
    try
    {
      SQL = "select period from conglomerate..billing_items " +
        "where conglom_cust_id = " + conglomCustomerId + " " +
        "and bill_period_ref = (select max(bill_period_ref) " +
        "from conglomerate..billing_items " +
        "where conglom_cust_id = " + conglomCustomerId + ") ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          invRef = RS.getString(1);
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
      return invRef;
    }
  }
/************************************************************************************************/
  public String checkForTrialConglomInvoice()
  {
    String invRef = "none";
    try
    {
      SQL = "select conglom_invoice_ref + '|' + bill_period_ref " +
        "from conglomerate..conglom_invoice " +
        "where conglom_cust_id = " + conglomCustomerId + " " +
        "and status = 'TRIAL' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          invRef = RS.getString(1);
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
      return invRef;
    }
  }
/*************************************************************************************/
  public boolean insertMBLog(int seqNo)
  {
    boolean success = false;
    try
    {
      SQL = "INSERT INTO gcd..Monthly_Billing_Log " +
        "(Job_Id, Message_Date, Message, Global_Customer_Id, Message_Sequence, " +
        "Job_Sequence, Job_Name) values('" + billJobId + "', getdate(), 'Submitted', " +
        "'" + Global_Customer_Id + "', 0, " + seqNo + ",'GM Billing')";
      DBA.setSQL(SQL);
      if(DBA.Connect(DBA.NORESULT,P5))
      {
        success = true;
      }
      else
      {
        success = false;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return success;
    }
  }
/*******************************************************************************/
  public int generateAdHocInvoiceNo(int day, int month, int year)
  {
    int invNo = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //cal.set(year, month, day);
        SQL = "{call givn_ref..Generate_Invoice_No_eBAN(?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,Invoice_Region);
        cstmt.setString(3, year + "-" + (month<10?"0":"") + month + "-" + day);
        cstmt.setString(4,"GM Billing");
        cstmt.setString(5,actAsLogon);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            invNo = RS.getInt(1);
            if (invNo < 0)
            {
              Message = "<font color=red><b>Unable to generate new invoice number</b></font>";
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
      return invNo;
    }
  }
/*******************************************************************************/
  public int createAdHocInvoice(String invoiceNo, int bpsDay, int bpsMonth,
    int bpsYear, int bpeDay, int bpeMonth, int bpeYear)
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //cal.set(year, month, day);
        SQL = "{call gcd..Create_Ad_Hoc_Invoice_eBAN(?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,Invoice_Region);
        cstmt.setString(3, bpsYear + "-" + (bpsMonth<10?"0":"") + bpsMonth +
          "-" + (bpsDay<10?"0":"") + bpsDay);
        cstmt.setString(4, bpeYear + "-" + (bpeMonth<10?"0":"") + bpeMonth +
          "-" + (bpeDay<10?"0":"") + bpeDay);
        cstmt.setString(5,invoiceNo);
        cstmt.setString(6,actAsLogon);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret == -1)
            {
              Message = "<font color=red><b>No site found for invoice region</b></font>";
            }
            else if (ret < 0)
            {
              Message = "<font color=red><b>Unable to create new invoice</b></font>";
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
  public String generateOneOffInvoice(int bpsDay, int bpsMonth,
    int bpsYear, int bpeDay, int bpeMonth, int bpeYear)
  {
    String ret = "0";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Prepare_One_Off_Invoice_eBAN(?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2, bpsYear + "-" + (bpsMonth<10?"0":"") + bpsMonth +
          "-" + (bpsDay<10?"0":"") + bpsDay);
        cstmt.setString(3, bpeYear + "-" + (bpeMonth<10?"0":"") + bpeMonth +
          "-" + (bpeDay<10?"0":"") + bpeDay);
        //cstmt.setString(5,"GM Billing");
        cstmt.setString(4,actAsLogon);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getString(1);
            if (ret.startsWith("-"))
            {
              if (ret.equals("-1"))
              {
                StringBuffer sb = new StringBuffer("<font color=red>Please close " +
                  "invoice(s) ");
                while (RS.next())
                {
                  sb.append("'" + RS.getString(1) + "', ");
                }
                sb.setLength(sb.length() - 2);
                sb.append(" before proceeding.");
                Message = sb.toString();
              }
              else if (ret.equals("-2"))
              {
                StringBuffer sb = new StringBuffer("<font color=red>Customer " +
                  "ready for bill period " + (bpsDay<10?"0":"") + bpsDay + "/" +
                  (bpsMonth<10?"0":"") + bpsMonth + "/" + bpsYear + " to " +
                  (bpeDay<10?"0":"") + bpeDay + "/" + (bpeMonth<10?"0":"") +
                  bpeMonth + "/" + bpeYear+ ". WARNING: this bill period has " +
                  "been issued already on invoice(s) ");
                while (RS.next())
                {
                  sb.append("'" + RS.getString(1) + "', ");
                }
                sb.setLength(sb.length() - 2);
                sb.append(".");
                Message = sb.toString();
              }
              else
              {
                Message = "<font color=red><b>Unable to create new one-off " +
                "invoice, return code: " + ret + "</b></font>";
              }
            }
            else
            {
              Message = "<font color=blue>Customer ready for bill period " +
                (bpsDay<10?"0":"") + bpsDay + "/" + (bpsMonth<10?"0":"") +
                bpsMonth + "/" + bpsYear + " to " + (bpeDay<10?"0":"") + bpeDay +
                "/" + (bpeMonth<10?"0":"") + bpeMonth + "/" + bpeYear;
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
  public int generateAdHocInvoice(int bpsDay, int bpsMonth,
    int bpsYear, int bpeDay, int bpeMonth, int bpeYear)
  {
    int ret = 0;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Generate_Ad_Hoc_Invoice_eBAN(?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,Invoice_Region);
        cstmt.setString(3, bpsYear + "-" + (bpsMonth<10?"0":"") + bpsMonth +
          "-" + (bpsDay<10?"0":"") + bpsDay);
        cstmt.setString(4, bpeYear + "-" + (bpeMonth<10?"0":"") + bpeMonth +
          "-" + (bpeDay<10?"0":"") + bpeDay);
        cstmt.setString(5,"GM Billing");
        cstmt.setString(6,actAsLogon);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getInt(1);
            if (ret == -6)
            {
              Message = "<font color=red><b>No site found for invoice region</b></font>";
            }
            else if ((ret == -9) || (ret == -10) || (ret == -11))
            {
              Message = "<font color=red><b>Problem with Running_Ind. Please contact Systems Support</b></font>";
            }
            else if (ret < 0)
            {
              Message = "<font color=red><b>Unable to create new invoice, return code: " +
                ret + "</b></font>";
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
  public long createGSOAdHocInvoice(String spName, String spId,
    String endCustomerName, double amount, String creditDebit, int tpDay,
    int tpMonth, int tpYear, String bpsDate, String description)
  {
    long ret = 0;
/*@Service_Partner_Name VARCHAR(50),
@Service_Partner_Id integer,
@End_Customer_Name varchar(100),
@Amount	NUMERIC(20,5),
@Credit_Debit char(1),
@BPSD DATETIME,
@Tax_Point_Date	DATETIME,
@Description varchar
*/
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //cal.set(year, month, day);
        SQL = "{call gso..Create_Ad_Hoc_Invoice_eBAN(?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1, spName);
        cstmt.setString(2, spId);
        cstmt.setString(3, endCustomerName);
        cstmt.setDouble(4, amount);
        cstmt.setString(5, creditDebit);
        cstmt.setString(6, bpsDate.substring(0,4) + "-" + bpsDate.substring(4,6) +
          "-" + bpsDate.substring(6,8));
        cstmt.setString(7, tpYear + "-" + (tpMonth<10?"0":"") + tpMonth +
          "-" + (tpDay<10?"0":"") + tpDay);
        cstmt.setString(8, description);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getLong(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to create new invoice</b></font>";
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
  public int closeAdHocInvoice(String invoiceNo)
  {
    int ret = 0;

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Archive_Manual_Invoice_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNo);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            /*ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to create new invoice</b></font>";
            }*/
            String status = RS.getString(1);
            long adjustments = RS.getLong(2);
            long mbRecs = RS.getLong(3);
            if (status.equals("OK"))
            {
              Message = "<font color=blue><b>Invoice " +
                invoiceNo + " closed</b></font>";
            }
            else if (status.equals("Already"))
            {
              Message = "<font color=blue><b>Invoice " +
                invoiceNo + " has been closed already</b></font>";
            }
            else if (status.equals("sap"))
            {
              Message = "<font color=red><b>Adjustments have more than one " +
                "SAP tax code. Invoice " + invoiceNo + " cannot be closed.</b></font>";
            }
            else if (status.equals("rate"))
            {
              Message = "<font color=red><b>Adjustments have more than one " +
                "tax rate. Invoice " + invoiceNo + " cannot be closed.</b></font>";
            }
            else if (status.equals("mismatch"))
            {
              Message = "<font color=red><b>Adjustment SAP tax code and tax " +
                "rate do not match. Invoice " + invoiceNo + " cannot be closed.</b></font>";
            }
            else if (status.equals("Ebill"))
            {
              Message = "<font color=red><b>This invoice cannot be closed as " +
                "the account is not set up on e-billing or SAP yet. Setup runs at 4pm daily.</b></font>";
            }
            else
            {
              Message = "<font color=red><b>Unable to close invoice " +
                invoiceNo + "</b></font>";
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
  public boolean getConglomCustomerSummary()
  {
    boolean ret = false;

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Customer_Summary_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = true;
            conglomCustomerName = RS.getString(1);
            conglomFrequency = RS.getString(2);
            conglomPeriod = RS.getString(3);
            conglomProductCount = RS.getLong(4);
            conglomInvoiceRef = RS.getString(5);
            conglomInvTotalExc = RS.getString(6);
            conglomInvTax = RS.getString(7);
            conglomInvTotalInc = RS.getString(8);
            Global_Customer_Id = RS.getString(9);
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
  public boolean getGSOServicePartnerStatus()
  {
    boolean found = false;
    try
    {
      SQL = "Select Billing_Period, Status, Rejects, Exceptions " +
        "from gso..View_GSO_Service_Partners (nolock) " +
        "where SP_Id = " + servicePartnerId + " ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          found = true;
          gsoBillingPeriod = RS.getString(1);
          gsoStatus = RS.getString(2);
          gsoRejects = RS.getString(3);
          gsoExceptions = RS.getString(4);
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
/************************************************************************************************/
  public int getGSOExceptionCount()
  {
    int count = -1;
    try
    {
      SQL = "select count(*) " +
        "from gso..GSO_Exceptions (nolock) " +
        "where SP_Name = '" + servicePartnerName + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          count = RS.getInt(1);
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
      return count;
    }
  }
/************************************************************************************************/
  public String determineGSOSubmissionOptions()
  {
    gsoSubmitMode = "";
    gsoTrialButtonStatus = "disabled";
    gsoCloseButtonStatus = "disabled";
    gsoExtractButtonStatus = "disabled";
    Message = "";
    String msg = "";
    try
    {
      SQL = "Select Job_Request_Seq_Id, Request_Type, " +
        "Request_Status, Completed_On, Completion_Status, " +
        "Monthly_Billing_Mode " +
        "from gso..Job_Request (nolock) " +
        "where Job_Request_Seq_Id = " +
        "(select max(Job_Request_Seq_Id) " +
        "from gso..Job_Request (nolock) " +
        "where SP_Name = '" + servicePartnerName + "' " +
        "and Bill_Period_Start_Date = (" +
        "select Billing_Period_Start_Date " +
        "from givn..Billing_Period_Run_Control (nolock) " +
        "where Global_Customer_Id = '" + servicePartnerName + "' " +
        "and Jobname = 'GSO Billing')) ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          String gsoRequestType = RS.getString(2);
          String gsoRequestStatus = RS.getString(3);
          String gsoCompletionStatus = RS.getString(5);
          String gsoMonthlyBillingMode = RS.getString(6);
          if (gsoRequestType.equals("Daily"))
	  {
            if (gsoRequestStatus.equals("Completed"))
	    {
              if (gsoCompletionStatus.equals("OK"))
	      {
                gsoSubmitMode = "Trial";
                gsoTrialButtonStatus = "";
                gsoCloseButtonStatus = "disabled";
                gsoExtractButtonStatus = "";
              }
              else
              {
                gsoSubmitMode = "GCD Extract";
                gsoTrialButtonStatus = "disabled";
                gsoCloseButtonStatus = "disabled";
                gsoExtractButtonStatus = "";
              }
            }
            else if (gsoRequestStatus.equals("Running"))
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>GCD Extract running</font>";
            }
            else
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>GCD Extract requested</font>";
            }
          }
          else if (gsoMonthlyBillingMode.equals("Trial"))
          {
            if (gsoRequestStatus.equals("Completed"))
            {
              if (gsoCompletionStatus.equals("OK"))
              {
                if (getGSOExceptionCount() == 0)
                {
                  gsoSubmitMode = "Trial";
                  gsoTrialButtonStatus = "";
                  gsoCloseButtonStatus = "";
                  gsoExtractButtonStatus = "";
                }
                else
                {
                  gsoSubmitMode = "Trial";
                  gsoTrialButtonStatus = "";
                  gsoCloseButtonStatus = "disabled";
                  gsoExtractButtonStatus = "";
                }
              }
            }
            else if (gsoRequestStatus.equals("Running"))
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>Monthly Trial running</font>";
            }
            else
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>Monthly Trial requested</font>";
            }
          }
          else
          {
            if (gsoRequestStatus.equals("Completed"))
            {
              if (gsoCompletionStatus.equals("OK"))
              {
                gsoSubmitMode = "";
                gsoTrialButtonStatus = "disabled";
                gsoCloseButtonStatus = "disabled";
                gsoExtractButtonStatus = "disabled";
              }
              else
              {
                gsoSubmitMode = "Close";
                gsoTrialButtonStatus = "";
                gsoCloseButtonStatus = "";
                gsoExtractButtonStatus = "";
	      }
            }
            else if (gsoRequestStatus.equals("Running"))
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>Monthly Close running</font>";
            }
            else
            {
              gsoSubmitMode = "";
              gsoTrialButtonStatus = "disabled";
              gsoCloseButtonStatus = "disabled";
              gsoExtractButtonStatus = "disabled";
              msg = "<font color=red>Monthly Close requested</font>";
            }
          }
        }
        else
        {
          gsoSubmitMode = "GCD Extract";
          gsoTrialButtonStatus = "disabled";
          gsoCloseButtonStatus = "disabled";
          gsoExtractButtonStatus = "";
        }
      }
    }
    catch(Exception ex)
    {
      msg=ex.getMessage();
    }
    finally
    {
      close();
      return msg;
    }
  }
/*******************************************************************************/
  public long submitGSOJobRequest(String mode)
  {
    long ret = -99;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gso..Insert_Job_Request_eBAN(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1, servicePartnerName);
        cstmt.setString(2, mode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            ret = RS.getLong(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to submit GSO job request</b></font>";
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
  public long applyConglomDiscount(String userId, String type, String invoiceNo)
  {
    long ret = -99;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Apply_Discount_eBAN(?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1, conglomCustomerId);
        cstmt.setString(2, billedProductForList);
        cstmt.setString(3, userId);
        cstmt.setString(4, type);
        cstmt.setString(5, invoiceNo);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            double dRet = RS.getDouble(1);
            if (dRet < 0)
            {
              ret = (long)dRet;
            }
            else
            {
              conglomDiscountNetAmount = dRet;
              ret = 0;
            }
            //ret = RS.getLong(1);
            if (ret == 0)
            {
              Message = "<font color=blue><b>" + type + " discount applied successfully</b></font>";
            }
            else if (ret == -2)
            {
              Message = "<font color=red><b>No source invoices found with status of 'OPEN' or 'TRIAL'</b></font>";
            }
            else if (ret == -23)
            {
              Message = "<font color=red><b>Unable to apply all discount items</b></font>";
            }
            else if (ret == -24)
            {
              Message = "<font color=red><b>Unable to apply all lead discount items</b></font>";
            }
            else if (ret == -25)
            {
              Message = "<font color=red><b>No discounts in effect</b></font>";
            }
            else
            {
              Message = "<font color=red><b>Unable to apply " + type.toLowerCase() +
                " discount</b></font>";
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
  public String getConglomProductList(String sortOrder)
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

        SQL = "{call conglomerate..Get_Product_List_eBAN(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);

	columns=5;

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
            grid.append("</tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
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
  public String getConglomJobQueue()
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

        SQL = "{call conglomerate..Get_Job_Queue_eBAN()}";
	cstmt = DBA.Conn.prepareCall(SQL);

	columns=5;

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
            grid.append("</tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue>There are no customers in the Goldfish Extract queue</font>";
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
        setJobQueue(grid.toString());
        return grid.toString();
      }
      else
      {
        return Message;
      }
    }
  }
/*************************************************************************************/
public String findGeocodes(String city, String state, String zipcode)
{

  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  columns=5;
  Message="";
  StringBuffer where = new StringBuffer("where datatype = 'US States' and value = state ");
  if (!SU.hasNoValue(city))
  {
    where.append("and city like '%" + city + "%' ");
  }
  if (!SU.hasNoValue(state))
  {
    where.append("and state = '" + state + "' ");
  }
  if (!SU.hasNoValue(zipcode))
  {
    where.append("and zip_code8 like '" + zipcode + "%' ");
  }
  try
  {

    SQL="select 'width=157 NOWRAP>'+city, "+
      "'width=100 NOWRAP>'+county, " +
      "'width=320 NOWRAP>'+text+' ('+state+')', " +
      "'width=80 NOWRAP>'+ case when state='CN' then zip_code8 else zip_code end, "+
      "'width=80 NOWRAP>'+geocode "+
      "from eban..geocode (nolock), eban..ban_reference_data (nolock) " +
      where.toString() +
      "order by city, state ";

    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0><tr>");

      try
      {
        while (RS.next())
        {
          rowcount++;
          rows = BigInteger.valueOf(rowcount);
          //Alternate row colours
          if (rows.testBit(0))
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          grid.append("</tr>");
          //end the table
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      geocodes = grid.toString();

    else
      geocodes = Message;
    return geocodes;
  }
}
/*************************************************************************************/
public String findAnalysts(String gcId)
{
  return findAnalysts(gcId, false);
}
/*************************************************************************************/
public String findAnalysts(String gcId, boolean isConglom)
{
  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  columns=1;
  Message="";
  try
  {

    SQL="select 'width=397 NOWRAP>'+ L.User_Firstname + ' ' + L.User_Surname " +
      "from GIVN_REF..Logon L (nolock), " +
      "GIVN..Global_Customer GC (nolock), " +
      "GIVN..Global_Customer_Application GCA (nolock), " +
      "GIVN..Customer_Access CA (nolock) " +
      "where GC.Global_Customer_Id = " + (isConglom
      ?"(select distinct global_customer_id from conglomerate..conglom_customer (nolock) " +
      "where conglom_cust_id = " + gcId + ")"
      :"'" + gcId + "'") + " " +
      "and CA.Global_Customer_Id = GC.Global_Customer_Id " +
      "and ((CA.Logon_Id like 'w%' or CA.Logon_Id like 'y%') " +
      "and CA.Logon_Id " + (isConglom?"":"not ") + "like '%c') " +
      "and not CA.Logon_Id like 'y12850%' " +      
      "and L.Logon_Id = CA.Logon_Id " +
      "and GCA.Global_Customer_Id = GC.Global_Customer_Id " +
      "and GCA.Application_Id = '0" + (isConglom?"9":"1") + "' " +
      "order by L.User_Firstname + ' ' + L.User_Surname ";

    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      grid.append("<table border=0><tr>");

      try
      {
        while (RS.next())
        {
          rowcount++;
          rows = BigInteger.valueOf(rowcount);
          //Alternate row colours
          if (rows.testBit(0))
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          grid.append("</tr>");
          //end the table
        }
        if (rowcount == 0)
        {
          grid.append("<td class=grid1 width=397 NOWRAP>None Assigned</td></tr>");
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      analysts = grid.toString();

    else
      analysts = Message;
    return analysts;
  }
}
/*************************************************************************************/
public String dataSearch(String searchData, String searchType)
{
  int counter=0, rowcount=0;
  BigInteger rows = new BigInteger("0");
  String gridClass;
  StringBuffer grid= new StringBuffer("");
  Message="";
  try
  {
    if ((searchType.equals("gcid")) || (searchType.equals("acc")) ||
      (searchType.equals("c2ref")) || (searchType.equals("c3ref")))
    {
      columns = 5;
    }
    else //if (searchType.equals("gsr"))
    {
      columns = 3;
    }

    if (DBA.Connect(PREPARE,P5))
    {
      grid.append("<table border=0>");

      rowcount = 0;

      SQL = "{call gcd..Get_Data_Search_List_eBAN(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,searchData);
      cstmt.setString(2,searchType);

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
          {//odd number
            gridClass="grid1 ";
          }
          else
          {//even number
            gridClass="grid2 ";
          }
          grid.append("<tr>");
          for(counter=1;counter<columns+1;counter++)
          {
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(RS.getString(counter));
            grid.append("</td>");
          }
          grid.append("</tr>");
          //end the table
        }
        if (rowcount == 0)
        {
          grid.append("<td class=grid1 colspan= " + columns +
            " width=737 NOWRAP>No data found</td></tr>");
        }
        grid.append("</table>");
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }

  }

  catch(Exception se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }
  finally
  {
    close();
    if (Message.equals(""))
      searchResults = grid.toString();

    else
      searchResults = Message;
    return searchResults;
  }
}
/*************************************************************************************/
public String getSearchResults()
{
  return searchResults;
}
/*************************************************************************************/
public void setSearchResults(String sr)
{
  searchResults = sr;
}
/*******************************************************************************/
  public boolean checkConglomSubmitEligibility(boolean close)
  {
    boolean eligible = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate.." + (close?"Close":"Trial") +
          "_Submit_Check_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.equals("OK"))
          {
            billPeriodRef = RS.getString(2);
            eligible = true;
          }
          else
          {
            Message = ret;
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
      return eligible;
    }
  }
/*******************************************************************************/
  public String getConglomCustomerAdjustmentList()
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

        SQL = "{call conglomerate..Get_Adjustment_List_eBAN(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billedProductForList);

	columns=6;

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
            long thisId = RS.getLong(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Update')\">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Delete')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
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
  public String getConglomProductDiscountList()
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

        SQL = "{call conglomerate..Get_Discount_List_eBAN(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billedProductForList);

	columns=7;

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
            long thisId = RS.getLong(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Update')\">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Delete')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
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
  public String getConglomManualInvoiceList()
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

        SQL = "{call conglomerate..Get_Manual_Invoice_List_eBAN(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billedProductForList);

	columns=5;

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
            long thisId = RS.getLong(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Update')\">");
            /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Delete')\">");*/
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
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
  public String getConglomCRDocketGrid()
  {
    return conglomCRDocketGrid;
  }
/*******************************************************************************/
  public String getConglomCRDocketList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    String message="";
    conglomCRDocketGrid = "";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call conglomerate..Get_CR_Docket_Item_List_eBAN(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);

	columns=9;

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
            long thisId = RS.getLong(counter);
            String product = RS.getString(++counter);
            String sourceAccount = RS.getString(++counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Update','" + product + "','" + sourceAccount + "')\">");
            /*grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Delete')\">");*/
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            message="<font color=blue>No Conglom Discounts exist without Credit Notes for this customer</font>";
          }
	}
	catch(java.sql.SQLException se)
        {
	  message=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      conglomCRDocketGrid = grid.toString();
      if (message.equals(""))
      {
        return conglomCRDocketGrid;
      }
      else
      {
        return message;
      }
    }
  }
/*******************************************************************************/
  public String getConglomItemStatusList(String itemId)
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    String message="";
    conglomItemStatusGrid = "";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call conglomerate..Get_Bill_Prod_Item_Status_List_eBAN(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,Long.parseLong(itemId));

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
            grid.append("</tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
	}
	catch(java.sql.SQLException se)
        {
	  message=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (message.equals(""))
      {
        conglomItemStatusGrid = grid.toString();
        return conglomItemStatusGrid;
      }
      else
      {
        return message;
      }
    }
  }
/*******************************************************************************/
  public String getConglomItemStatusGrid()
  {
    return conglomItemStatusGrid;
  }
/*******************************************************************************/
  public String getConglomBillProdGrid()
  {
    return conglomBillProdGrid;
  }
/*******************************************************************************/
  public String getConglomBillProdList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    String thisMessage = "";
    StringBuffer grid=new StringBuffer("");
    //Message="";
    conglomBillProdBilledAmtTotal = 0;
    conglomBillProdVATTotal = 0;
    conglomBillProdInvoiceAmtTotal = 0;
    conglomBillProdItemNo = 0;
    conglomBillProdGrid = "";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call conglomerate..Get_Bill_Prod_Item_List_eBAN(?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billedProductForList);
        cstmt.setString(3,periodForList);
        cstmt.setString(4,itemTypeForList);
        cstmt.setString(5,userId);

	columns=11;

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
            long thisId = RS.getLong(counter);
            int sourceTableSeqNo = RS.getInt(++counter);
            boolean isConglomAdjustment = sourceTableSeqNo == 3;
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            if (isConglomAdjustment)
            {
              counter+=3;
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonBlank.gif " +
                "align=bottom border=0 width=24 height=22 alt=\"This is a " +
                "conglom adjustment which cannot be updated here. Please use " +
                "the adjustment screens\">");
            }
            else
            {
              grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonU.gif " +
                "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
                thisId + "','Update','"+ RS.getString(++counter) + "','" + RS.getString(++counter) + "','" + RS.getString(++counter) + "')\">");
            }
            grid.append("</td></tr>");
            //sum totals
            conglomBillProdBilledAmtTotal += RS.getDouble(++counter);
            conglomBillProdVATTotal += RS.getDouble(++counter);
            conglomBillProdInvoiceAmtTotal += RS.getDouble(++counter);
          }
          //End the table
          grid.append("</table>");
          conglomBillProdItemNo = rowcount;
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
	}
	catch(java.sql.SQLException se)
        {
	  thisMessage=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      thisMessage=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      thisMessage="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (thisMessage.equals(""))
      {
        conglomBillProdGrid = grid.toString();
        return conglomBillProdGrid;
      }
      else
      {
        Message = thisMessage;
        return Message;
      }
    }
  }
/************************************************************************************************/
  public boolean updateCRDocket(String interimNo, String actualNo)
  {
		boolean done = false;
    Message = "<font color=red><b>Unable to update CR/Docket number</b></font>";
    try
    {
      SQL = "{call conglomerate..Update_CR_Docket_eBAN(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setLong(2,Long.parseLong(interimNo));
      cstmt.setString(3,actualNo);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        if(RS.getInt(1) == 0)
        {
          done = true;
          Message = "<font color=blue><b>Conglom Discount updated with credit note number " +
						actualNo + "</b></font>";
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
      return done;
    }
  }
/************************************************************************************************/
  public boolean updateConglomBillItemStatus(String itemId, String newStatus,
    String statusChangeReason, String userId)
  {
		boolean done = false;
    Message = "<font color=red><b>Unable to update Bill Item Status</b></font>";
    try
    {
      SQL = "{call conglomerate..Update_Bill_Item_Status_eBAN(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,Long.parseLong(itemId));
      cstmt.setString(2,newStatus);
      cstmt.setString(3,statusChangeReason);
      cstmt.setString(4,userId);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        if(RS.getInt(1) == 0)
        {
          done = true;
          Message = "<font color=blue><b>Bill Item Status updated</b></font>";
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
      return done;
    }
  }
/************************************************************************************************/
  public boolean reactivateCustomer(String gcId)
  {
    boolean done = false;
    try
    {
      SQL = "{call givn..Reassign_Customer_eBAN(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,gcId);
      cstmt.setString(2,actAsLogon);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        if(RS.getInt(1) == 0)
        {
          done = true;
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
      return done;
    }
  }
/************************************************************************************************/
  public String getGCBReport(String reportId, String dateParam, String param1,
    String param2)
  {
    StringBuffer sb = new StringBuffer("");
    try
    {
      SQL = "{call eBAN..Get_GCB_Report_eBAN(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,reportId);
      cstmt.setString(2,dateParam);
      cstmt.setString(3,param1);
      cstmt.setString(4,param2);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS == null)
      {
        sb.append("n/a");
      }
      else
      {
        ResultSetMetaData rsmd = RS.getMetaData();
        int numberOfColumns = rsmd.getColumnCount() + 1;
        boolean firstTime = true;
        while (RS.next())
        {
          if (firstTime)
          {
            sb.append("<TABLE BORDER=1>");
            // get column names
            sb.append("<THEAD><TR>");
            for (int i = 1; i<numberOfColumns; i++)
            {
               sb.append("<TH>");
               sb.append(rsmd.getColumnName(i));
               sb.append("</TH>");
            }
            sb.append("</TR></THEAD>");
            firstTime = false;
          }
          sb.append("<TR>");
          for (int i = 1; i<numberOfColumns; i++)
          {
            sb.append("<TD>");
            // if the column is a TIMESTAMP type, convert to date to get rid of the time
            if ((rsmd.getColumnType(i)+2) == Types.TIMESTAMP)
            {
              sb.append(RS.getDate(i));
            }
            else
            {
            try
            {
              String temp = RS.getString(i);
              sb.append(temp);
            }
            catch (Exception e)
            {
              sb.append(RS.getString(i));
            }
            sb.append("</TD>");
            }
          }
          sb.append("</TR>");
        }
        sb.append("</TABLE>");
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return sb.toString();
    }
  }
/************************************************************************************************/
  public ResultSet getGCBReportRS(String reportId, String dateParam, String param1,
    String param2)
  {
    RS = null;
    try
    {
      SQL = "{call eBAN..Get_GCB_Report_eBAN(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,reportId);
      cstmt.setString(2,dateParam);
      cstmt.setString(3,param1);
      cstmt.setString(4,param2);

      cstmt.execute();
      RS = cstmt.getResultSet();
    }
    catch(Exception ex)
    {
      close();
      Message=ex.getMessage();
    }
    finally
    {
      return RS;
    }
  }
/*******************************************************************************/
  public void testExec()
  {
    try
    {
      Runtime runtime = Runtime.getRuntime();
      Process proc = runtime.exec("C:\\test\\copyfile.bat");
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
  }
/*******************************************************************************/
  public String getClosedInvoiceGrid()
  {
    return closedInvoiceGrid;
  }
/*******************************************************************************/
  public void setClosedInvoiceGrid(String value)
  {
    closedInvoiceGrid = value;
  }
/*******************************************************************************/
  public String getClosedInvoiceList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    String thisMessage = "";
    closedInvoiceGrid = "";
    StringBuffer grid=new StringBuffer("");

    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        grid.append("<table border=0>");

        rowcount = 0;

        SQL = "{call gcd..Get_Closed_Invoice_List_eBAN(?,?)}";
      	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,accountId);

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
            for(counter=1;counter<columns+1;counter++)
            {
              grid.append("<td class=");
              grid.append(gridClass);
              grid.append(RS.getString(counter));
              grid.append("</td>");
            }
            //Add the extra generated column for the buttons
            String thisId = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonR.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','" + RS.getString(++counter) + "')\">");
            grid.append("</td></tr>");
          }
          //End the table
          grid.append("</table>");
          if (rowcount == 0)
          {
            thisMessage="<font color=blue>There are no closed invoices for this account</font>";
          }
	}
	catch(java.sql.SQLException se)
        {
	  thisMessage=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      thisMessage=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      thisMessage="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (thisMessage.equals(""))
      {
        closedInvoiceGrid = grid.toString();
        return closedInvoiceGrid;
      }
      else
      {
        //Message = thisMessage;
        return thisMessage;
      }
    }
  }
/************************************************************************************************/
  public void reOpenBill()
  {
    try
    {
      SQL = "{call gcd..Reopen_Customer_Bill_eBAN(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,Global_Customer_Id);
      cstmt.setString(2,actAsLogon);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        String ret = RS.getString(1);
        if (ret.startsWith("-"))
        {
          if (ret.equals("-1"))
          {
            StringBuffer sb = new StringBuffer("<font color=red>Please close " +
              "invoice(s) ");
            while (RS.next())
            {
              sb.append("'" + RS.getString(1) + "', ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(" before proceeding.");
            Message = sb.toString();
          }
          else
          {
            Message = "<font color=red>Unable to re-open customer " + Global_Customer_Id +
              " - error code: " + ret;
          }
        }
        else
        {
          Message = "<font color=blue>Customer " + Global_Customer_Id  +
            " has been re-opened ";
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
/************************************************************************************************/
  public void reverseInvoice(String invoiceNo)
  {
    try
    {
      SQL = "{call gcd..Reverse_Invoice_eBAN(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,accountId);
      cstmt.setString(2,invoiceNo);
      cstmt.setString(3,actAsLogon);

      cstmt.execute();
      RS = cstmt.getResultSet();
      if (RS.next())
      {
        String ret = RS.getString(1);
        if (ret.startsWith("-"))
        {
          Message = "<font color=red><b>Unable to reverse invoice " + invoiceNo +
            " - error code: " + ret + "</b></font>";
        }
        else
        {
          Message = "<font color=blue><b>Invoice " + invoiceNo +
            " has been reversed by new invoice " + ret +
            ". The trial pdf will be available for viewing via the Desktop 'A' button shortly.</b></font>";
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
/*****************************************************************************/
  public  Enumeration getDOBInfo()
  {
	Vector Result = new Vector();
        if ((billingTeam.equals("Global")) || (billingTeam.equals("Conglom")))
        {
          if (billingTeam.equals("Global"))
          {
            SQL = "select max(dob_code), convert(char(11),SAP_Close_Date,106) + ' 18:00' SAP_Close_Date " +
              "from givn_ref..dob_billing_cycle (nolock) " +
              "where billing_team = '" + billingTeam + "' " +
              "and date_of_billing = " +
              "(select max(date_of_billing) from givn_ref..dob_billing_cycle " +
              "(nolock) where billing_team = '" + billingTeam + "' " +
              "and date_of_billing < getdate()) " +
              "group by SAP_Close_Date ";
          }
          else //if (billingTeam.equals("Conglom"))
          {
            SQL = "select max(dob_cycle_id), 'N/A' SAP_Close_Date " +
              "from conglomerate..conglom_billing_schedule (nolock) " +
              "where extract_run_date = " +
              "(select max(extract_run_date) from conglomerate..conglom_billing_schedule " +
              "(nolock) where extract_run_date < getdate()) ";
          }
          try
          {
            DBA.setSQL(SQL);
            if (DBA.Connect(READ,P5))
            {
              RS = DBA.getResultsSet();
              boolean found = false;
              while (RS.next())
              {
                Result.addElement(RS.getString(1));
                Result.addElement(RS.getString(2));
                found = true;
              }
              if (!found)
              {
                Result.addElement("Not Yet Available");
                Result.addElement("Not Yet Available");
              }
              close();
            }
            else
            {
              //Connection failed
              Result.addElement("Not Yet Available");
              Result.addElement("Not Yet Available");
              Message = "<font color=red><b>"+DBA.Message;
            }
          }
          catch(java.sql.SQLException se)
          {
            Result.addElement(se.getMessage());
            close();
          }
          if (!Result.isEmpty())
          {
            Message = "";
          }
          else
          {
            Message = "<font color=red><b>DOB data not found</font>";
          }
        }
        else
        {
          Result.addElement("N/A");
          Result.addElement("N/A");
        }
	return Result.elements();
  }
  
  public boolean adAuthenticate(String adLoginId, String password, 
          String adDomain) {
    adAuthenticated = false;
    String thisDomain = adDomain!=null?adDomain:
            EBANProperties.getEBANProperty(EBANProperties.LDAPDOMAIN);
    String ldapEnvironment = EBANProperties.getEBANProperty(EBANProperties.LDAPENVIRONMENT);
    try {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
            EBANProperties.getEBANProperty(EBANProperties.LDAPINITIALCONTEXTFACTORY));
        env.put(Context.PROVIDER_URL, 
                EBANProperties.getEBANProperty(EBANProperties.LDAPPROVIDERURL));
        env.put(Context.SECURITY_AUTHENTICATION, 
                EBANProperties.getEBANProperty(EBANProperties.LDAPSECURITYAUTHENTICATION));
        if (ldapEnvironment.equals("local")) {
            env.put(Context.SECURITY_PRINCIPAL, EBANProperties.getEBANProperty(EBANProperties.LDAPLOCALSECURITYPRINCIPAL));
        } else {
            env.put(Context.SECURITY_PRINCIPAL, thisDomain + "\\" + adLoginId);
        }    
        env.put(Context.SECURITY_CREDENTIALS, password);

        // Create the initial context
        DirContext ctx = new InitialDirContext(env);
        if (ldapEnvironment.equals("local")) {
            attributeSearch(ctx, EBANProperties.getEBANProperty(EBANProperties.LDAPLOCALUID));
        } else {
            attributeSearch(ctx, adLoginId);
        }    
        adAuthenticated = true;

    } catch (AuthenticationException ae) {
        //Message = "<font color=red><b>" + ae.getMessage() + "</b></font>";
        Message = "<font color=red><b>Invalid credentials entered</b></font>";
    } catch (NamingException ne) {
        Message = "<font color=red><b>" + ne.getMessage() + "</b></font>";
    } catch (Exception e) {
        Message = "<font color=red><b>" + e.getMessage() + "</b></font>";
    } finally {
        return adAuthenticated;
    }
  }
  
  public boolean adAuthenticateMultiDomain(String adLoginId, String password) {
    adAuthenticated = false;
    Properties adDomains = EBANProperties.getEBANProperties(EBANProperties.LDAPDOMAINLIST);
    Enumeration e = adDomains.elements();
    while (e.hasMoreElements()) {
        String adDomain = (String)e.nextElement();
        if (adAuthenticate(adLoginId, password, adDomain)) {
            adAuthenticated = true;
            break;
        }
    }
    if (!adAuthenticated) {
        Message = "<font color=red><b>Invalid credentials entered</b></font>";
    }
    return adAuthenticated;
  }
  
  public boolean isADAuthenticated() {
      //adAuthenticated = true;
      return adAuthenticated;
  }
  
  public String getADEmailAddress() {
      //adAuthenticated = true;
      return adEmailAddress;
  }
  
    public void attributeSearch(DirContext ctx, String accountName) 
            throws NamingException, Exception {
        adEmailAddress = null;
        String ldapSearchBase = EBANProperties.getEBANProperty(EBANProperties.LDAPSEARCHBASE);
        String searchFilter = "(&(objectClass=" +
                EBANProperties.getEBANProperty(EBANProperties.LDAPSEARCHOBJECTCLASS) + ")(" +
                EBANProperties.getEBANProperty(EBANProperties.LDAPSEARCHIDENTIFIER) +
                "=" + accountName + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

        SearchResult searchResult = null;
        if(results.hasMoreElements()) {
            searchResult = (SearchResult) results.nextElement();
            Attributes attr = searchResult.getAttributes();
            try {
                adEmailAddress = attr.get(EBANProperties.getEBANProperty(EBANProperties.LDAPMAILIDENTIFIER)).get(0).toString();
            } catch (Exception ex) {
                adEmailAddress = "unknown";
            }    
            //make sure there is not another item available, there should be only 1 match
            if(results.hasMoreElements()) {
                throw new Exception("Matched multiple users for the accountName: " + accountName);
            }
        }
    }
  
}

