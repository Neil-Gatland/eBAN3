package JavaUtil;

public class RSAccountDescriptor {

  private long accountId;
  private String accountName;
  private String accountNo;
  private String invoiceInd;

  public RSAccountDescriptor(long accountId, String accountName,
    String accountNo, String invoiceInd)
  {
    this.accountId = accountId;
    this.accountName = accountName;
    this.accountNo = accountNo;
    this.invoiceInd = invoiceInd;
  }

  public long getAccountId()
  {
    return accountId;
  }

  public String getAccountName()
  {
    return accountName;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getInvoiceInd()
  {
    return invoiceInd;
  }

}