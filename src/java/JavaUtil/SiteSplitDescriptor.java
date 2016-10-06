package JavaUtil;

public class SiteSplitDescriptor {

  private String siteName;
  private String siteId;
  private float percentageSplit;

  public SiteSplitDescriptor(String siteName, String siteId,
    float percentageSplit)
  {
    this.siteName = siteName;
    this.siteId = siteId;
    this.percentageSplit = percentageSplit;
  }

  public String getSiteName()
  {
    return siteName;
  }

  public String getSiteId()
  {
    return siteId;
  }

  public float getPercentageSplit()
  {
    return percentageSplit;
  }

}