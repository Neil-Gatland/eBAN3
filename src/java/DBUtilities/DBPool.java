package DBUtilities;

/**
 * Title:
 * Description: Generic database pool, accessed by the Connect(int Mode) method
 *              for connections to a single datasource
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

// a singleton class
import java.sql.SQLException;

  public class DBPool extends ConnectionPool
  {
    private static String URL="";
    private static final String Driver="net.avenir.jdbcdriver7.Driver";

    private static DBPool pool = null;

    private DBPool() throws SQLException
    {
	//call parent constructor
        super(URL,Driver,20,30,false);
    }
    private static synchronized DBPool getInstance()
    {
      if (pool == null)
      {
	try
	{
	  pool = new DBPool();
	} catch(SQLException sqle) {Message=sqle.getMessage();};
      }
      return(pool);
    }
  public static synchronized DBPool getInstance(String ServerName)
    {
      URL="jdbc:AvenirDriver://"+ServerName+":1433";
      return getInstance();
    }
  }
