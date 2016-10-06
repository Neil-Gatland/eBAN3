package DBUtilities;
// a singleton class
import java.sql.SQLException;

  public class P3DataSource extends ConnectionPool
  {
    private static String URL="";
    //private static final String Driver="net.avenir.jdbcdriver7.Driver";
    private static final String Driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static int ConnectionCount;

    private static P3DataSource P3pool = null;

    private P3DataSource() throws SQLException
    {
        super(URL,Driver,ConnectionCount,ConnectionCount,false); //call parent constructor
    }
    private P3DataSource(String ServerName,
      int Connections, int environment) throws SQLException
    {
        super(ServerName,environment,ConnectionCount,ConnectionCount,false); //call parent constructor
    }
    public static synchronized P3DataSource getInstance()
    {
      if (P3pool == null)
      {
	try
	{
	  P3pool = new P3DataSource();
	}
        catch(SQLException sqle)
        {
          Message=sqle.getMessage();
        }
      }
      return(P3pool);
    }
    public static synchronized P3DataSource getInstance(String ServerName,
      int Connections, int environment)
    {
      if (P3pool == null)
      {
	try
	{
	  P3pool = new P3DataSource(ServerName, Connections, environment);
	}
        catch(SQLException sqle)
        {
          Message=sqle.getMessage();
        }
      }
      return(P3pool);
    }
    public static synchronized P3DataSource getInstance(String ServerName,int Connections)
    {
      //URL="jdbc:AvenirDriver://"+ServerName+":1433";
      URL="jdbc:sqlserver://"+ServerName+":1433";
      ConnectionCount=Connections;
      return getInstance();
    }
    /*public static synchronized P3DataSource getInstance(String ServerName,
      int Connections, int environment)
    {
      ConnectionCount=Connections;
      return getInstance();
    }*/
  }