package DBUtilities;
// a singleton class
import java.sql.SQLException;

  public class P5DataSource extends ConnectionPool
  {
    private static String URL="";
    //private static final String Driver="net.avenir.jdbcdriver7.Driver";
    private static final String Driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static int ConnectionCount;

    private static P5DataSource P5pool = null;

    private P5DataSource() throws SQLException
    {
	//call parent constructor
        super(URL,Driver,ConnectionCount,ConnectionCount,false);
    }
    public static synchronized P5DataSource getInstance()
    {
      if (P5pool == null)
      {
	try
	{
	  P5pool = new P5DataSource();
	} catch(SQLException sqle) {Message=sqle.getMessage();};
      }
      return(P5pool);
    }
    public static synchronized P5DataSource getInstance(String ServerName,int Connections)
    {
      //URL="jdbc:AvenirDriver://"+ServerName+":1433";
      URL="jdbc:sqlserver://"+ServerName+":1433";
      ConnectionCount=Connections;
      return getInstance();
    }
  }