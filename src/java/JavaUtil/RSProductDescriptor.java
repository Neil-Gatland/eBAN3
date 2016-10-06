package JavaUtil;

public class RSProductDescriptor {

  private String productCode;
  private String productDescription;
  private String productGroup;

  public RSProductDescriptor(String productCode,
    String productDescription, String productGroup)
  {
    this.productCode = productCode;
    this.productDescription = productDescription;
    this.productGroup = productGroup;
  }

  public String getProductCode()
  {
    return productCode;
  }

  public String getProductDescription()
  {
    return productDescription;
  }

  public String getProductGroup()
  {
    return productGroup;
  }

}