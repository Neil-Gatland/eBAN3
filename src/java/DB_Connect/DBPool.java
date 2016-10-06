package DB_Connect;

/**
 * Title:
 * Description: Generic database pool, accessed by the Connect(int Mode,DataSource) method
 *              for connections to a single datasource
 */

import java.sql.SQLException;

  public class DBPool extends ConnectionPool
  {// a singleton class
    private static String URL="";
    private static final String Driver="net.avenir.jdbcdriver7.Driver";

    private static DBPool pool = null;

    private DBPool() throws SQLException
    {
	//call parent constructor
        super(URL,Driver,20,30,false);
    }
    public static synchronized DBPool getInstance()
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
    /*****************************************************/
/*    public static void main (String[] args)
    {
      //DBPool DBP = new DBPool();
      DBPool.getInstance();
    }
*/
  }
