package JavaUtil;

public class ebillzSummaryDescriptor {

  private String id;
  private String accountNumber;
  private String period;
  private String callCategory;
  private String timeOfDay;
  private long totalDuration;
  private long totalAmount;
  private long totalCalls;

  public ebillzSummaryDescriptor
    (String id, String accountNumber, String period, String callCategory,
     String timeOfDay,long duration, long amount, long calls)
  {
    this.id = id;
    this.accountNumber = accountNumber;
    this.period = period;
    this.callCategory = callCategory;
    this.timeOfDay = timeOfDay;
    this.totalAmount = amount;
    this.totalDuration = duration;
    this.totalCalls = calls;
  }

  public String getId()
  {
    return id;
  }

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public String getPeriod()
  {
    return period;
  }

  public String getCallCategory()
  {
    return callCategory;
  }

  public String getTimeOfDay()
  {
    return timeOfDay;
  }

  public long getTotalAmount()
  {
    return totalAmount;
  }

  public long getTotalDuration()
  {
    return totalDuration;
  }

  public long getTotalCalls()
  {
    return totalCalls;
  }

  public void incrementTotalCalls(long calls)
  {
    this.totalCalls = totalCalls + calls;
  }

  public void incrementTotalDuration(long duration)
  {
    this.totalDuration = totalDuration + duration;
  }

  public void incrementTotalAmount(long amount)
  {
    this.totalAmount = totalAmount + amount;
  }

}