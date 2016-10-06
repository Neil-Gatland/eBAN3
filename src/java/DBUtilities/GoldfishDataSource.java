package DBUtilities;
// a singleton class
import java.sql.SQLException;

  public class GoldfishDataSource extends ConnectionPool
  {
    private static String server;
    private static int connectionCount;
    private static int envType;

    private static GoldfishDataSource gfPool = null;

    private GoldfishDataSource() throws SQLException
    {
        super(server,envType,connectionCount,connectionCount,false); //call parent constructor
    }
    public static synchronized GoldfishDataSource getInstance()
    {
      if (gfPool == null)
      {
	try
	{
	  gfPool = new GoldfishDataSource();
	}
        catch(SQLException sqle)
        {
          Message=sqle.getMessage();
        }
      }
      return(gfPool);
    }
    public static synchronized GoldfishDataSource getInstance(String serverName,
      int connections, int environment)
    {
      connectionCount=connections;
      envType=environment;
      server=serverName;
      return getInstance();
    }
  }