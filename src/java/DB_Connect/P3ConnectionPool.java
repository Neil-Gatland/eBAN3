package DB_Connect;

import java.sql.SQLException;

  public class P3ConnectionPool extends ConnectionPool
  {// a singleton class
    private static String URL="";
    private static final String Driver="net.avenir.jdbcdriver7.Driver";

    private static P3ConnectionPool pool = null;

    private P3ConnectionPool() throws SQLException
    {
        super(URL,Driver,5,10,false); //call parent constructor
    }
    public static synchronized P3ConnectionPool getInstance()
    {
      if (pool == null)
      {
	try
	{
	  pool = new P3ConnectionPool();
	} catch(SQLException sqle) {Message=sqle.getMessage();};
      }
      return(pool);
    }
    public static synchronized P3ConnectionPool getInstance(String ServerName)
    {
      URL="jdbc:AvenirDriver://"+ServerName+":1433";
      return getInstance();
    }
  }