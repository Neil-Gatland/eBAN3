package JavaUtil;


public class SuspendedDataDescriptor {

  private String providerName;
  private String masterAccount;
  private String accountNumber;
  private String accountName;
  private String product;
  private String suspenseReason;
  private String recordCount;
  private String writtenOff;
  private String productCode;
  private String suspended;

  public SuspendedDataDescriptor(String providerName, String masterAccount,
    String accountNumber, String accountName, String product,
    String suspenseReason, String recordCount, String writtenOff,
    String productCode, String suspended)
  {
    this.providerName = providerName;
    this.masterAccount = masterAccount;
    this.accountNumber = accountNumber;
    this.accountName = accountName;
    this.product = product;
    this.suspenseReason = suspenseReason;
    this.recordCount = recordCount;
    this.writtenOff = writtenOff;
    this.productCode = productCode;
    this.suspended = suspended;
  }

  public String getProviderName()
  {
    return providerName==null?"&nbsp;":providerName;
  }

  public String getMasterAccount()
  {
    return masterAccount==null?"&nbsp;":masterAccount;
  }

  public String getAccountNumber()
  {
    return accountNumber==null?"&nbsp;":accountNumber;
  }

  public String getAccountName()
  {
    return accountName==null?"&nbsp;":accountName;
  }

  public String getProduct()
  {
    return product==null?"&nbsp;":product;
  }

  public String getProductCode()
  {
    return productCode==null?"&nbsp;":productCode;
  }

  public String getSuspenseReason()
  {
    return suspenseReason==null?"&nbsp;":suspenseReason;
  }

  public String getRecordCount()
  {
    return recordCount==null?"&nbsp;":recordCount;
  }

  public String getWrittenOff()
  {
    return writtenOff==null?"&nbsp;":writtenOff;
  }

  public String getSuspended()
  {
    return suspended==null?"&nbsp;":suspended;
  }
}