package DBUtilities;
// a singleton class
import java.sql.SQLException;

  public class P4DataSource extends ConnectionPool
  {
    private static String URL="";
    //private static final String Driver="net.avenir.jdbcdriver7.Driver";
    private static final String Driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static int ConnectionCount;

    private static P4DataSource P4pool = null;

    private P4DataSource() throws SQLException
    {
	//call parent constructor
        super(URL,Driver,ConnectionCount,ConnectionCount,false);
    }
    public static synchronized P4DataSource getInstance()
    {
      if (P4pool == null)
      {
	try
	{
	  P4pool = new P4DataSource();
	} catch(SQLException sqle) {Message=sqle.getMessage();};
      }
      return(P4pool);
    }
    public static synchronized P4DataSource getInstance(String ServerName,int Connections)
    {
      //URL="jdbc:AvenirDriver://"+ServerName+":1433";
      URL="jdbc:sqlserver://"+ServerName+":1433";
      ConnectionCount=Connections;
      return getInstance();
    }
  }