package JavaUtil;

public class RSRatingSchemeDescriptor {

  private String productCode;
  private String ratingType;
  private String fromCallMonth;
  private String rangeStart;
  private String rangeEnd;
  private String dayRate;
  private String eveningRate;
  private String weekendRate;
  private String duration;

  public RSRatingSchemeDescriptor(String productCode, String ratingType,
    String fromCallMonth, String rangeStart, String rangeEnd, String dayRate,
    String eveningRate, String weekendRate, String duration)
  {
    this.productCode = productCode;
    this.ratingType = ratingType;
    this.fromCallMonth = fromCallMonth;
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
    this.dayRate = dayRate;
    this.eveningRate = eveningRate;
    this.weekendRate = weekendRate;
    this.duration = duration;
  }

  public String getProductCode()
  {
    return productCode;
  }

  public String getRatingType()
  {
    return ratingType;
  }

  public String getFromCallMonth()
  {
    return fromCallMonth;
  }

  public String getRangeStart()
  {
    return rangeStart;
  }

  public String getRangeEnd()
  {
    return rangeEnd;
  }

  public String getDayRate()
  {
    return dayRate;
  }

  public String getEveningRate()
  {
    return eveningRate;
  }

  public String getWeekendRate()
  {
    return weekendRate;
  }

  public String getDuration()
  {
    return duration;
  }

}