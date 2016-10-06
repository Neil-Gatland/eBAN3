package JavaUtil;

import java.math.BigDecimal;

public class TeleBusinessSummaryDescriptor {

  private long numberOfCalls;
  private long duration;
  private String product;
  private String calledNumber;
  private String callDate;
  private BigDecimal cost;

  public TeleBusinessSummaryDescriptor(String product, String calledNumber, String callDate)
  {
    this.product = product;
    this.calledNumber = calledNumber;
    this.callDate = callDate;
    numberOfCalls = 0;
    duration = 0;
    cost = new BigDecimal(0);
  }

  public TeleBusinessSummaryDescriptor(String callDate)
  {
    this.callDate = callDate;
    numberOfCalls = 0;
    duration = 0;
    cost = new BigDecimal(0);
  }

  public long getNumberOfCalls()
  {
    return numberOfCalls;
  }

  public void incrementNumberOfCalls(long addOn)
  {
    numberOfCalls += addOn;
  }

  public long getDuration()
  {
    return duration;
  }

  public String getDurationPrint()
  {
    long hh = duration/3600;
    long rem = duration%3600;
    long mm = rem/60;
    long ss = rem%60;
    return (hh<10?"0":"") + hh + ":" + (mm<10?"0":"") + mm +  ":" +
      (ss<10?"0":"") + ss;
  }

  public void incrementDuration(long addOn)
  {
    duration += addOn;
  }

  public String getProduct()
  {
    return product;
  }

  public BigDecimal getCost()
  {
    return cost;
  }

  public String getCostPrint()
  {
    return cost.toString();
  }

  public void incrementCost(BigDecimal addTo)
  {
    cost = cost.add(addTo);
  }

  public String getCalledNumber()
  {
    return calledNumber;
  }

  public String getCallDate()
  {
    return callDate;
  }

}