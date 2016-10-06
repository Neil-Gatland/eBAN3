package JavaUtil;

public class ConglomLocalReportDataDescriptor {

  private String accountNo;
  private String description;
  private String referenceItem;
  private String billingNo;
  private String referenceType;

  public ConglomLocalReportDataDescriptor(String accountNo, String description,
    String referenceItem, String billingNo, String referenceType)
  {
    this.accountNo = accountNo;
    this.description = description;
    this.referenceItem = referenceItem;
    this.billingNo = billingNo;
    this.referenceType = referenceType;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getDescription()
  {
    return description;
  }

  public String getReferenceItem()
  {
    return referenceItem;
  }

  public String getBillingNo()
  {
    return billingNo;
  }

  public String getReferenceType()
  {
    return referenceType;
  }

}