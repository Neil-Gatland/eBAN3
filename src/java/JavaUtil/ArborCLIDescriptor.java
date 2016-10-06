package JavaUtil;

public class ArborCLIDescriptor
{

  private String id;
  private long totalCalls;
  private long totalDuration;
  private long totalVolume;
  private long totalRated;
  private long totalBilled;
  private String cli;
  private String serviceType;
  private String costCentre;

  public ArborCLIDescriptor
    (String id, long calls, long duration, long volume, long rated, long billed, String cli, String serviceType, String costCentre)
  {
    this.id = id;
    this.totalCalls = calls;
    this.totalDuration = duration;
    this.totalVolume = volume;
    this.totalRated = rated;
    this.totalBilled = billed;
    this.cli=cli;
    this.serviceType=serviceType;
    this.costCentre=costCentre;
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

  public String getCli()
  {
    return cli;
  }

  public String getServiceType()
  {
    return serviceType;
  }

  public String getCostCentre()
  {
    return costCentre;
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