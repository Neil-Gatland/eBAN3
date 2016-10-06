package JavaUtil;


public class StandardInvoiceChargeDescriptor {

  private String invGCDId;
  private String invGCDTotal;

  public StandardInvoiceChargeDescriptor(String invGCDId, String invGCDTotal)
  {
    this.invGCDId = invGCDId;
    this.invGCDTotal = invGCDTotal;
  }

  public String getInvGCDTotal()
  {
    return invGCDTotal;
  }

  public String getInvGCDId()
  {
    return invGCDId;
  }

}