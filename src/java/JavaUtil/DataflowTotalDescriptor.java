package JavaUtil;

public class DataflowTotalDescriptor {

  private String id;
  private String description;
  private String productCode;
  //private double cost;
  private long cost;
  private long totalCalls;
  private long duration;

  public DataflowTotalDescriptor(String id, String description, long totalCalls,
    long duration, /*double*/long cost)
  {
    this.id = id;
    this.description = description;
    this.totalCalls = totalCalls;
    this.duration = duration;
    this.cost = cost;
  }

  public DataflowTotalDescriptor(String id, String description, long duration,
    /*double*/long cost)
  {
    this.id = id;
    this.description = description;
    this.totalCalls = 1;
    this.duration = duration;
    this.cost = cost;
  }

  public DataflowTotalDescriptor(String id, String description,
    String productCode, long duration, /*double*/long cost)
  {
    this.id = id;
    this.description = description;
    this.productCode = productCode;
    this.totalCalls = 1;
    this.duration = duration;
    this.cost = cost;
  }

  public String getId()
  {
    return id;
  }

  public String getDescription()
  {
    return description;
  }

  public String getProductCode()
  {
    return productCode;
  }

  public /*double*/long getCost()
  {
    return cost;
  }

  public String getCostAsString()
  {
    String ret = null;
    String temp = String.valueOf(cost);
    int pos = 0;
    int len = temp.length();
    if (len < 4)
    {
      if (len == 1)
        ret = "0.00" + temp;
      else if (len == 2)
        ret = "0.0" + temp;
      else //if (len == 3)
        ret = "0." + temp;
    }
    else
    {
      pos = temp.length() - 3;
      ret = temp.substring(0, pos) + "." + temp.substring(pos);
    }
    return ret;
  }

  public void incrementCost(/*double*/long cost)
  {
    this.cost += cost;
  }

  public long getTotalCalls()
  {
    return totalCalls;
  }

  public void incrementTotalCalls(long totalCalls)
  {
    this.totalCalls += totalCalls;
  }

  public void incrementTotalCalls()
  {
    this.totalCalls++;
  }

  public long getDuration()
  {
    return duration;
  }

  public void incrementDuration(long duration)
  {
    this.duration += duration;
  }

}