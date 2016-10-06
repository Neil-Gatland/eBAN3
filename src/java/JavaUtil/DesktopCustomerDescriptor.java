package JavaUtil;


public class DesktopCustomerDescriptor {

  private String customerName;
  private String period;
  private String frequency;
  private String dob;
  private String accounts;
  private String status;
  private String autoClose;
  private String gcId;
  private String analyst;
  private String products;
  private String exceptions;
  private long conglomCustId;
  private String billClosedDate;

  public DesktopCustomerDescriptor(String customerName, String frequency,
    String period, String status, String dob, String products,
    String exceptions, long conglomCustId)
  {
    this.customerName = customerName;
    this.period = period;
    this.frequency = frequency;
    this.dob = dob;
    this.products = products;
    this.status = status;
    this.exceptions = exceptions;
    this.conglomCustId = conglomCustId;
  }

  public DesktopCustomerDescriptor(String customerName, String period,
    String frequency, String dob, String accounts, String status,
    String autoClose, String gcId, String analyst)
  {
    this.customerName = customerName;
    this.period = period;
    this.frequency = frequency;
    this.dob = dob;
    this.accounts = accounts;
    this.status = status;
    this.autoClose = autoClose;
    this.gcId = gcId;
    this.analyst = analyst;
  }

  public DesktopCustomerDescriptor(String customerName, String period,
    String frequency, String dob, String accounts, String status,
    String autoClose, String gcId, String analyst, String billClosedDate)
  {
    this.customerName = customerName;
    this.period = period;
    this.frequency = frequency;
    this.dob = dob;
    this.accounts = accounts;
    this.status = status;
    this.autoClose = autoClose;
    this.gcId = gcId;
    this.analyst = analyst;
    this.billClosedDate = billClosedDate;
  }

  public String getCustomerName()
  {
    return customerName;
  }

  public String getPeriod()
  {
    return period;
  }

  public String getFrequency()
  {
    return frequency;
  }

  public String getDOB()
  {
    return dob;
  }

  public String getAccounts()
  {
    return accounts;
  }

  public String getStatus()
  {
    return status;
  }

  public String getAutoClose()
  {
    return autoClose;
  }

  public String getGCID()
  {
    return gcId;
  }

  public String getAnalyst()
  {
    return analyst;
  }

  public String getProducts()
  {
    return products;
  }

  public String getExceptions()
  {
    return exceptions;
  }

  public String getConglomCustId()
  {
    return Long.toString(conglomCustId);
  }

  public String getBillClosedDate()
  {
    return billClosedDate;
  }

}