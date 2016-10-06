package JavaUtil;

import java.util.*;

public class kpi2Descriptor {

  private String accountNo;
  private String fileDate;
  private String cdrDate;
  private long cdrCount;
  private long totalDuration;
  private long totalCost;

  public kpi2Descriptor
    (String accountNo, String fileDate, String cdrDate, long cdrCount, long totalDuration, long totalCost)
  {
    this.accountNo = accountNo;
    this.fileDate = fileDate;
    this.cdrDate = cdrDate;
    this.cdrCount = cdrCount;
    this.totalDuration = totalDuration;
    this.totalCost = totalCost;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public String getFileDate()
  {
    return fileDate;
  }

  public String getCdrDate()
  {
    return cdrDate;
  }

  public long getCdrCount()
  {
    return cdrCount;
  }

  public long getTotalDuration()
  {
    return totalDuration;
  }

  public long getTotalCost()
  {
    return totalCost;
  }

  public void incrementTotalCalls(long Calls)
  {
    this.cdrCount = cdrCount + Calls;
  }

  public void incrementTotalDuration(long Duration)
  {
    this.totalDuration = totalDuration + Duration;
  }

  public void incrementTotalAmount(long Cost)
  {
    this.totalCost = totalCost + Cost;
  }

}