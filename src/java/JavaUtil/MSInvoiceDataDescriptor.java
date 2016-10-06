package JavaUtil;
import java.math.BigDecimal;

public class MSInvoiceDataDescriptor {

  private String invGlobalCustomerId;
  private String invInvoiceNo;
  private String invBillingCustomerName;
  private String invBillingAddress1;
  private String invBillingAddress2;
  private String invBillingAddress3;
  private String invBillingAddress4;
  private String invBillingAddress5;
  private String invBillingAddress6;
  private String invAccountId;
  private String invTaxPointDate;
  private String invCustomerVATRegNo;
  private String invCustomerVATRegLiteral;
  private String invCustomerVATRegText;
  private String invNotes;
  private String invChargePeriodStartDate;
  private String invChargePeriodEndDate;
  private String invCustomerData1;
  private String invCustomerData2;
  private String invChargeDescriptionCode;
  private String invChargeAmount;
  private String invOutgoingExchangeRate;
  private String invTaxTotal;
  private String invAdjTotal;
  private String invTaxType;
  private String invOutgoingCurrencyCode;
  private String invDecimalPlaces;
  private String invTaxRate;
  private String invJurisdictionCurrencyCode;
  private String invJurisdictionCurrencyDesc;
  private String invJurisdictionDecimalPlaces;
  private String invJurisdictionCurrencyExRate;
  private String invOutgoingCurrencyDesc;
  private String invSectionInd;
  private String invChargeId;
  private String invCustomerContactPoint;
  private String invCompanyName;
  private String invCompanyDetails1;
  private String invCompanyDetails2;
  private long invChargeAmountNum;
  private long invTaxTotalNum;
  private BigDecimal invChargeAmountBD;
  private BigDecimal invTaxTotalBD;

  public MSInvoiceDataDescriptor(String invGlobalCustomerId, String invInvoiceNo,
    String invBillingCustomerName, String invBillingAddress1,
    String invBillingAddress2, String invBillingAddress3,
    String invBillingAddress4, String invBillingAddress5,
    String invBillingAddress6, String invAccountId, String invTaxPointDate,
    String invCustomerVATRegNo, String invCustomerVATRegLiteral,
    String invCustomerVATRegText, String invNotes, String invChargePeriodStartDate,
    String invChargePeriodEndDate, String invCustomerData1, String invCustomerData2,
    String invChargeDescriptionCode, String invChargeAmount,
    String invOutgoingExchangeRate, String invTaxTotal, String invAdjTotal,
    String invTaxType, String invOutgoingCurrencyCode, String invDecimalPlaces,
    String invTaxRate, String invJurisdictionCurrencyCode,
    String invJurisdictionCurrencyDesc, String invJurisdictionDecimalPlaces,
    String invJurisdictionCurrencyExRate, String invOutgoingCurrencyDesc,
    String invSectionInd, String invChargeId, String invCustomerContactPoint,
    String invCompanyName, String invCompanyDetails1, String invCompanyDetails2)
  {
    this.invGlobalCustomerId = invGlobalCustomerId;
    this.invInvoiceNo = invInvoiceNo;
    this.invBillingCustomerName = invBillingCustomerName;
    this.invBillingAddress1 = invBillingAddress1;
    this.invBillingAddress2 = invBillingAddress2;
    this.invBillingAddress3 = invBillingAddress3;
    this.invBillingAddress4 = invBillingAddress4;
    this.invBillingAddress5 = invBillingAddress5;
    this.invBillingAddress6 = invBillingAddress6;
    this.invAccountId = invAccountId;
    this.invTaxPointDate = invTaxPointDate;
    this.invCustomerVATRegNo = invCustomerVATRegNo;
    this.invCustomerVATRegLiteral = invCustomerVATRegLiteral;
    this.invCustomerVATRegText = invCustomerVATRegText;
    this.invNotes = invNotes;
    this.invChargePeriodStartDate = invChargePeriodStartDate;
    this.invChargePeriodEndDate = invChargePeriodEndDate;
    this.invCustomerData1 = invCustomerData1;
    this.invCustomerData2 = invCustomerData2;
    this.invChargeDescriptionCode = invChargeDescriptionCode;
    this.invChargeAmount = invChargeAmount;
    this.invOutgoingExchangeRate = invOutgoingExchangeRate;
    this.invTaxTotal = invTaxTotal;
    this.invAdjTotal = invAdjTotal;
    this.invTaxType = invTaxType;
    this.invOutgoingCurrencyCode = invOutgoingCurrencyCode;
    this.invDecimalPlaces = invDecimalPlaces;
    this.invTaxRate = invTaxRate;
    this.invJurisdictionCurrencyCode = invJurisdictionCurrencyCode;
    this.invJurisdictionCurrencyDesc = invJurisdictionCurrencyDesc;
    this.invJurisdictionDecimalPlaces = invJurisdictionDecimalPlaces;
    this.invJurisdictionCurrencyExRate = invJurisdictionCurrencyExRate;
    this.invOutgoingCurrencyDesc = invOutgoingCurrencyDesc;
    this.invSectionInd = invSectionInd;
    this.invChargeId = invChargeId;
    this.invCustomerContactPoint = invCustomerContactPoint;
    this.invCompanyName = invCompanyName;
    this.invCompanyDetails1 = invCompanyDetails1;
    this.invCompanyDetails2 = invCompanyDetails2;
  }

  public MSInvoiceDataDescriptor(String invNotes, String invChargePeriodStartDate,
    String invChargePeriodEndDate, String invChargeDescriptionCode,
    String invChargeAmount, long invChargeAmountNum, long invTaxTotalNum,
    String invCompanyDetails1)
  {
    this.invNotes = invNotes;
    this.invChargePeriodStartDate = invChargePeriodStartDate;
    this.invChargePeriodEndDate = invChargePeriodEndDate;
    this.invChargeDescriptionCode = invChargeDescriptionCode;
    this.invChargeAmount = invChargeAmount;
    this.invChargeAmountNum = invChargeAmountNum;
    this.invTaxTotalNum = invTaxTotalNum;
    this.invCompanyDetails1 = invCompanyDetails1;
  }

  public MSInvoiceDataDescriptor(String invNotes, String invChargePeriodStartDate,
    String invChargePeriodEndDate, String invChargeDescriptionCode,
    String invChargeAmount, BigDecimal invChargeAmountBD, BigDecimal invTaxTotalBD,
    String invCompanyDetails1)
  {
    this.invNotes = invNotes;
    this.invChargePeriodStartDate = invChargePeriodStartDate;
    this.invChargePeriodEndDate = invChargePeriodEndDate;
    this.invChargeDescriptionCode = invChargeDescriptionCode;
    this.invChargeAmount = invChargeAmount;
    this.invChargeAmountBD = invChargeAmountBD;
    this.invTaxTotalBD = invTaxTotalBD;
    this.invCompanyDetails1 = invCompanyDetails1;
  }

  public String getInvGlobalCustomerId()
  {
    return invGlobalCustomerId;
  }

  public String getInvInvoiceNo()
  {
    return invInvoiceNo;
  }

  public String getInvBillingCustomerName()
  {
    return invBillingCustomerName;
  }

  public String getInvBillingAddress1()
  {
    return invBillingAddress1;
  }

  public String getInvBillingAddress2()
  {
    return invBillingAddress2;
  }

  public String getInvBillingAddress3()
  {
    return invBillingAddress3;
  }

  public String getInvBillingAddress4()
  {
    return invBillingAddress4;
  }

  public String getInvBillingAddress5()
  {
    return invBillingAddress5;
  }

  public String getInvBillingAddress6()
  {
    return invBillingAddress6;
  }

  public String getInvAccountId()
  {
    return invAccountId;
  }

  public String getInvTaxPointDate()
  {
    return invTaxPointDate;
  }

  public String getInvCustomerVATRegNo()
  {
    return invCustomerVATRegNo;
  }

  public String getInvCustomerVATRegLiteral()
  {
    return invCustomerVATRegLiteral;
  }

  public String getInvCustomerVATRegText()
  {
    return invCustomerVATRegText;
  }

  public String getInvNotes()
  {
    return invNotes;
  }

  public String getInvChargePeriod()
  {
    return invChargePeriodStartDate +
      (invChargePeriodEndDate.equals("")?"":(" - " + invChargePeriodEndDate));
  }

  public String getInvChargePeriodStartDate()
  {
    return invChargePeriodStartDate;
  }

  public String getInvChargePeriodEndDate()
  {
    return invChargePeriodEndDate;
  }

  public String getInvCustomerData1()
  {
    return invCustomerData1;
  }

  public String getInvCustomerData2()
  {
    return invCustomerData2;
  }

  public String getInvChargeDescriptionCode()
  {
    return invChargeDescriptionCode;
  }

  public String getInvChargeAmount()
  {
    return invChargeAmount;
  }

  public long getInvChargeAmountNum()
  {
    return invChargeAmountNum;
  }

  public BigDecimal getInvChargeAmountBD()
  {
    return invChargeAmountBD;
  }

  public String getInvOutgoingExchangeRate()
  {
    return invOutgoingExchangeRate;
  }

  public String getInvTaxTotal()
  {
    return invTaxTotal;
  }

  public long getInvTaxTotalNum()
  {
    return invTaxTotalNum;
  }

  public BigDecimal getInvTaxTotalBD()
  {
    double dTemp = invTaxTotalBD.doubleValue();
    String sTemp = invTaxTotalBD.toString();
    return invTaxTotalBD;
  }

  public String getInvAdjTotal()
  {
    return invAdjTotal;
  }

  public String getInvTaxType()
  {
    return invTaxType;
  }

  public String getInvOutgoingCurrencyCode()
  {
    return invOutgoingCurrencyCode;
  }

  public String getInvDecimalPlaces()
  {
    return invDecimalPlaces;
  }

  public String getInvTaxRate()
  {
    return invTaxRate;
  }

  public String getInvJurisdictionCurrencyCode()
  {
    return invJurisdictionCurrencyCode;
  }

  public String getInvJurisdictionCurrencyDesc()
  {
    return invJurisdictionCurrencyDesc;
  }

  public String getInvJurisdictionDecimalPlaces()
  {
    return invJurisdictionDecimalPlaces;
  }

  public String getInvJurisdictionCurrencyExRate()
  {
    return invJurisdictionCurrencyExRate;
  }

  public String getInvOutgoingCurrencyDesc()
  {
    return invOutgoingCurrencyDesc;
  }

  public String getInvSectionInd()
  {
    return invSectionInd;
  }

  public String getInvChargeId()
  {
    return invChargeId;
  }

  public String getInvCustomerContactPoint()
  {
    return invCustomerContactPoint;
  }

  public String getInvCompanyName()
  {
    return invCompanyName;
  }

  public String getInvCompanyDetails1()
  {
    return invCompanyDetails1;
  }

  public String getInvCompanyDetails2()
  {
    return invCompanyDetails2;
  }

}
