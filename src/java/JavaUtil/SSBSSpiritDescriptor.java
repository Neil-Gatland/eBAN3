package JavaUtil;

public class SSBSSpiritDescriptor {

  private String id;
  private String cli;
  private long cost;

  public SSBSSpiritDescriptor
    (String id, String cli, long cost)
  {
    this.id = id;
    this.cli = cli;
    this.cost = cost;
  }

  public String getId()
  {
    return id;
  }

  public String getCLI()
  {
    return cli;
  }

  public long getCost()
  {
    return cost;
  }

  public void incrementCost(long Cost)
  {
    this.cost = cost + Cost;
  }

}