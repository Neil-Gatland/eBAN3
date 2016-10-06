package JavaUtil;

public class SSBSTotalCLIDescriptor {

  private String id;
  private long totalDuration;
  private long totalAmount;
  private long totalCalls;
  private String cli;
  private String productCode;
  private String costCentre;

  public SSBSTotalCLIDescriptor
    (String id, long Duration, long Amount, long Calls,
     String cli, String productCode, String costCentre)
  {
    this.id = id;
    this.totalAmount = Amount;
    this.totalDuration = Duration;
    this.totalCalls = Calls;
    this.cli = cli;
    this.productCode = productCode;
    this.costCentre = costCentre;
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

  public String getCLI()
  {
    return cli;
  }

  public String getProductCode()
  {
    return productCode;
  }

  public String getCostCentre()
  {
    return costCentre;
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