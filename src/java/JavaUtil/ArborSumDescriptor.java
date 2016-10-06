package JavaUtil;

public class ArborSumDescriptor
{

  private String id;
  private long totalCalls;
  private long totalDuration;
  private long totalVolume;
  private long totalRated;
  private long totalBilled;

  public ArborSumDescriptor(String id, long calls, long duration, long volume, long rated, long billed)
  {
    this.id = id;
    this.totalCalls = calls;
    this.totalDuration = duration;
    this.totalVolume = volume;
    this.totalRated = rated;
    this.totalBilled = billed;
  }

  public String getId()
  {
    return id;
  }

  public long getTotalCalls()
  {
    return totalCalls;
  }

  public long getTotalDuration()
  {
    return totalDuration;
  }

  public long getTotalVolume()
  {
    return totalVolume;
  }

  public long getTotalRated()
  {
    return totalRated;
  }

  public long getTotalBilled()
  {
    return totalBilled;
  }

  public void incrementTotalCalls(long calls)
  {
    this.totalCalls = totalCalls + calls;
  }

  public void incrementTotalDuration(long duration)
  {
    this.totalDuration = totalDuration + duration;
  }

  public void incrementTotalVolume(long volume)
  {
    this.totalVolume = totalVolume + volume;
  }

  public void incrementTotalRated(long rated)
  {
    this.totalRated = totalRated + rated;
  }

  public void incrementTotalBilled(long billed)
  {
    this.totalBilled = totalBilled + billed;
  }

}