package JavaUtil;

public class DataflowInputFileDescriptor {

  private long inputId;
  private long controlId;
  private java.util.Date billingDate;
  private String accountNo;
  private double cost;
  private long totalCalls;
  private long durationMins;
  private long durationSecs;
  private String controlFilename;

  public DataflowInputFileDescriptor(long controlId, long inputId,
    java.util.Date billingDate)
  {
    this.controlId = controlId;
    this.inputId = inputId;
    this.billingDate = billingDate;
  }

  public DataflowInputFileDescriptor(long controlId, long inputId,
    java.util.Date billingDate, String accountNo, long totalCalls,
    long durationMins, long durationSecs, double cost, String controlFilename)
  {
    this.controlId = controlId;
    this.inputId = inputId;
    this.billingDate = billingDate;
    this.accountNo = accountNo;
    this.totalCalls = totalCalls;
    this.durationMins = durationMins;
    this.durationSecs = durationSecs;
    this.cost = cost;
    this.controlFilename = controlFilename;
  }

  public long getControlId()
  {
    return controlId;
  }

  public long getInputId()
  {
    return inputId;
  }

  public java.util.Date getBillingDate()
  {
    return billingDate;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getControlFilename()
  {
    return controlFilename;
  }

  public double getCost()
  {
    return cost;
  }

  public long getTotalCalls()
  {
    return totalCalls;
  }

  public long getDurationMins()
  {
    return durationMins;
  }

  public long getDurationSecs()
  {
    return durationSecs;
  }

}