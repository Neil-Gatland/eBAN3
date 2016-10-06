package JavaUtil;


public class StandardInvoiceAdjustmentDescriptor {

  private String invAdjustmentDescription;
  private String invAdjustmentTotal;

  public StandardInvoiceAdjustmentDescriptor(String invAdjustmentDescription,
    String invAdjustmentTotal)
  {
    this.invAdjustmentDescription = invAdjustmentDescription;
    this.invAdjustmentTotal = invAdjustmentTotal;
  }

  public String getInvAdjustmentTotal()
  {
    return invAdjustmentTotal;
  }

  public String getInvAdjustmentDescription()
  {
    return invAdjustmentDescription;
  }

}