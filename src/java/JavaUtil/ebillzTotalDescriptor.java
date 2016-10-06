package JavaUtil;

public class ebillzTotalDescriptor {

  private String id;
  private long totalDuration;
  private long totalAmount;
  private long totalCalls;

  public ebillzTotalDescriptor
    (String id, long Duration, long Amount, long Calls)
  {
    this.id = id;
    this.totalAmount = Amount;
    this.totalDuration = Duration;
    this.totalCalls = Calls;
  }

  public String getId()
  {
    return id;
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

  public void incrementTotalCalls(long Calls)
  {
    this.totalCalls = totalCalls + Calls;
  }

  public void incrementTotalDuration(long Duration)
  {
    this.totalDuration = totalDuration + Duration;
  }

  public void incrementTotalAmount(long Amount)
  {
    this.totalAmount = totalAmount + Amount;
  }

}