package JavaUtil;

public class RSThresholdDescriptor {

  private String type;
  private String prsFlag;
  private String minAmount;
  private String minMinutes;

  public RSThresholdDescriptor(String type,
    String prsFlag, String minAmount, String minMinutes)
  {
    this.type = type;
    this.prsFlag = prsFlag;
    this.minAmount = minAmount;
    this.minMinutes = minMinutes;
  }

  public String getType()
  {
    return type;
  }

  public String getPRSFlag()
  {
    return prsFlag;
  }

  public String getMinAmount()
  {
    return minAmount;
  }

  public String getMinMinutes()
  {
    return minMinutes;
  }

}