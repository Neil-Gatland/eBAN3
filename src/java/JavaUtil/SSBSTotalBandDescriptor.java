package JavaUtil;

public class SSBSTotalBandDescriptor {

  private String id;
  private long totalDuration;
  private long totalAmount;
  private long totalCalls;
  private String band;
  private String destination;

  public SSBSTotalBandDescriptor
    (String id, long Duration, long Amount, long Calls,
     String band, String destination)
  {
    this.id = id;
    this.totalAmount = Amount;
    this.totalDuration = Duration;
    this.totalCalls = Calls;
    this.band = band;
    this.destination = destination;
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

  public String getBand()
  {
    return band;
  }

  public String getDestination()
  {
    return destination;
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