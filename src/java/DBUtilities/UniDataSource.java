package DBUtilities;
// a singleton class
import java.sql.SQLException;

  public class UniDataSource extends ConnectionPool
  {
    private static String server;
    private static int connectionCount;
    private static int envType;

    private static UniDataSource uniPool = null;

    private UniDataSource() throws SQLException
    {
        super(server,envType,connectionCount,connectionCount,false); //call parent constructor
    }
    public static synchronized UniDataSource getInstance()
    {
      if (uniPool == null)
      {
	try
	{
	  uniPool = new UniDataSource();
	}
        catch(SQLException sqle)
        {
          Message=sqle.getMessage();
        }
      }
      return(uniPool);
    }
    public static synchronized UniDataSource getInstance(String serverName,
      int connections, int environment)
    {
      connectionCount=connections;
      envType=environment;
      server=serverName;
      return getInstance();
    }
  }