package DB_Connect;
// a singleton class
import java.sql.SQLException;

  public class P5ConnectionPool extends ConnectionPool
  {
    private static String URL="";
    private static final String Driver="net.avenir.jdbcdriver7.Driver";

    private static P5ConnectionPool pool = null;

    private P5ConnectionPool() throws SQLException
    {
	//call parent constructor
        super(URL,Driver,5,10,false);
    }
    public static synchronized P5ConnectionPool getInstance()
    {
      if (pool == null)
      {
	try
	{
	  pool = new P5ConnectionPool();
	} catch(SQLException sqle) {Message=sqle.getMessage();};
      }
      return(pool);
    }
    public static synchronized P5ConnectionPool getInstance(String ServerName)
    {
      URL="jdbc:AvenirDriver://"+ServerName+":1433";
      return getInstance();
    }
  }