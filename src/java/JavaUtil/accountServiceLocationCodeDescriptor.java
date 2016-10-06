package JavaUtil;

public class accountServiceLocationCodeDescriptor {

  private String code;
  private String description;

  public accountServiceLocationCodeDescriptor
    (String code, String description)
  {
    this.code = code;
    this.description = description;
  }

  public String getCode()
  {
    return code;
  }

  public String getDescription()
  {
    return description;
  }

}