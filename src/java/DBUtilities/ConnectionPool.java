package DBUtilities;

import java.sql.*;
import java.util.*;
import JavaUtil.EBANProperties;

/** A class for preallocating, recycling, and managing
 *  JDBC connections.
*/

public class ConnectionPool
{
  private String driver, url, username, password,serverVersion;
  private int maxConnections;
  private boolean waitIfBusy;
  private Vector availableConnections, busyConnections;
  private boolean connectionPending = false;
  protected static String database;
  protected static String Message;

  public ConnectionPool(String URL,
			String Driver,
                        int initialConnections,
                        int maxConnections,
                        boolean waitIfBusy)
  throws SQLException
  {

      /***********************************************/
      /*oracle driver*/
      /***********************************************/
      //this.driver = "oracle.jdbc.driver.OracleDriver";
      //this.url = "jdbc:oracle:thin:@localhost:1521:"+DB;

      /* end of code for oracle driver*/

      /***********************************************/
      /*This is the i-net driver*/
      /***********************************************/
      //this.driver = "com.inet.tds.TdsDriver";
      //this.url = "jdbc:inetdae7:localHost:1433";

      /***********************************************/
      /* This is the Net Direct driver*/
      /***********************************************/
      //this.driver = "com.jnetdirect.jsql.JSQLDriver";
      //this.url = "jdbc:JSQLConnect://127.0.0.1:1433/sqlVersion=6";

      /***********************************************/
      /* This is the Microsoft SQL Server 2000 driver*/
      /***********************************************/

      //this.driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
      //this.url = "jdbc:microsoft:sqlserver://127.0.0.1:1433";

      /* End of code for the Microsoft SQL Server 2000  */

      /***********************************************/
      /*This is the code for the ATI 6.5/7.0/2000 driver*/
      /***********************************************/

      //this.driver="net.avenir.jdbcdriver7.Driver";
      //this.driver="net.avenir.jdbc2.Driver";

      //Local
      //this.serverVersion = "Sql2000";
      //this.url="jdbc:AvenirDriver://127.0.0.1:1433";

      //Local System Test
      //this.serverVersion = "Sql2000";
      //this.url="jdbc:AvenirDriver://xtpc005:1433";

      //UAT production
      //this.serverVersion = "Sql2000";
      this.serverVersion = "Sql6.5";
      //this.url="jdbc:AvenirDriver://127.0.0.1:1433";

     /*End of code for the ATI 6.5/7.0/2000 driver*/
      this.url=URL;
      this.driver=Driver;
      this.username = "ewfuser";
      //this.password = "edify";
      this.password = "*3wfus3r*";
      //this.username = "Danet5";
      //this.password = "*20Password05*";
      this.maxConnections = maxConnections;
      this.waitIfBusy = waitIfBusy;
      if (initialConnections > maxConnections)
      {
        initialConnections = maxConnections;
      }
      availableConnections = new Vector(initialConnections);
      busyConnections = new Vector();
      for(int i=0; i<initialConnections; i++)
      {
        availableConnections.addElement(makeNewConnection());
      }
  }
  public ConnectionPool(String ServerName,
			int environment,
                        int initialConnections,
                        int maxConnections,
                        boolean waitIfBusy)
  throws SQLException
  {
    switch (environment)
    {
      case 1: //local JBuilder
        this.url = "jdbc:AvenirDriver://"+ServerName+":1433";
        this.driver = "net.avenir.jdbcdriver7.Driver";
        this.username = "ewfuser";
        this.password = "*3wfus3r*";
        break;
      case 2: //Danet Tomcat
        this.url = "jdbc:sqlserver://"+ServerName+":1433";
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.username = "ewfuser";
        this.password = "*3wfus3r*";
        break;
      case 3: //C&W Tomcat
        this.url = "jdbc:sqlserver://"+ServerName+":1433";
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.username = "Danet5";
        this.password = "*20Password05*";
        break;
      case 4: //batch oracle test
        database = EBANProperties.getEBANProperty(EBANProperties.ORACLEDBNAME);
        this.url = "jdbc:oracle:thin:@"+ServerName+":1521:"+database;
        this.driver = "oracle.jdbc.driver.OracleDriver";
        this.username = "test";
        this.password = "test";
        break;
      case 5: //batch oracle
        database = EBANProperties.getEBANProperty(EBANProperties.ORACLEDBNAME);
        this.url = "jdbc:oracle:thin:@"+ServerName+":1521:"+database;
        this.driver = "oracle.jdbc.driver.OracleDriver";
        this.username = "admiin";
        this.password = "admin";
        break;
      case 6: //Danet Tomcat jtds jdbc:jtds:<server_type>://<server>[:<port>][/<database>][;<property>=<value>[;...]]
        this.url = "jdbc:jtds:sqlserver://"+ServerName+":1433";
        this.driver = "net.sourceforge.jtds.jdbc.Driver";
        this.username = "ewfuser";
        this.password = "*3wfus3r*";
        break;
      case 7: //C&W Tomcat jtds
        this.url = "jdbc:jtds:sqlserver://"+ServerName+":1433";
        this.driver = "net.sourceforge.jtds.jdbc.Driver";
        this.username = "Danet5";
        this.password = "*20Password05*";
        break;
      case 8: //Danet multi
        this.url = EBANProperties.getEBANProperty("driverURLPre")+ServerName+
          EBANProperties.getEBANProperty("driverURLSuf");
        this.driver = EBANProperties.getEBANProperty("driverClasspath");
        this.username = "ewfuser";
        this.password = "*3wfus3r*";
        break;
      case 9: //C&W multi
        this.url = EBANProperties.getEBANProperty("driverURLPre")+ServerName+
          EBANProperties.getEBANProperty("driverURLSuf");
        this.driver = EBANProperties.getEBANProperty("driverClasspath");
        this.username = "Danet5";
        this.password = "*20Password05*";
        break;
      case 10: //danet oracle goldfish
        database = EBANProperties.getEBANProperty(EBANProperties.ORACLEDBNAME);
        this.url = "jdbc:oracle:thin:@"+ServerName+":1521:"+database;
        this.driver = "oracle.jdbc.driver.OracleDriver";
        this.username = "Congomgr";
        this.password = "Congomgr";
        break;
      case 11: //C&W FSA Tomcat
        this.url = "jdbc:sqlserver://"+ServerName+":1433";
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.username = "GCB_Service_Account";
        this.password = "*Devoteam2011*";
        break;
      case 12: //C&W FSA Tomcat Test
        this.url = "jdbc:sqlserver://"+ServerName+":1433";
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.username = EBANProperties.getEBANProperty("tun");
        this.password = EBANProperties.getEBANProperty("tup");
        break;
      case 13: //C&W FSA Tomcat Test
        this.url = EBANProperties.getEBANProperty("turl");
        this.driver = EBANProperties.getEBANProperty("td");
        this.username = EBANProperties.getEBANProperty("tun");
        this.password = EBANProperties.getEBANProperty("tup");
        break;
      }

      this.maxConnections = maxConnections;
      this.waitIfBusy = waitIfBusy;
      if (initialConnections > maxConnections)
      {
        initialConnections = maxConnections;
      }
      availableConnections = new Vector(initialConnections);
      busyConnections = new Vector();
      for(int i=0; i<initialConnections; i++)
      {
        availableConnections.addElement(makeNewConnection());
      }
  }
  public synchronized Connection getConnection()
      throws SQLException
  {
    if (!availableConnections.isEmpty()) {
      Connection existingConnection =
        (Connection)availableConnections.lastElement();
      int lastIndex = availableConnections.size() - 1;
      availableConnections.removeElementAt(lastIndex);
      // If connection on available list is closed (e.g.,
      // it timed out), then remove it from available list
      // and repeat the process of obtaining a connection.
      // Also wake up threads that were waiting for a
      // connection because maxConnection limit was reached.
      if (existingConnection.isClosed()) {
        notifyAll(); // Freed up a spot for anybody waiting
        return(getConnection());
      } else {
        busyConnections.addElement(existingConnection);
        return(existingConnection);
      }
    } else {

      // Three possible cases:
      // 1) You haven't reached maxConnections limit. So
      //    establish one in the background if there isn't
      //    already one pending, then wait for
      //    the next available connection (whether or not
      //    it was the newly established one).
      // 2) You reached maxConnections limit and waitIfBusy
      //    flag is false. Throw SQLException in such a case.
      // 3) You reached maxConnections limit and waitIfBusy
      //    flag is true. Then do the same thing as in second
      //    part of step 1: wait for next available connection.

      if ((totalConnections() < maxConnections) &&
          !connectionPending) {
        makeBackgroundConnection();
      } else if (!waitIfBusy) {
        throw new SQLException("Connection limit reached");
      }
      // Wait for either a new connection to be established
      // (if you called makeBackgroundConnection) or for
      // an existing connection to be freed up.
      try {
        wait();
      } catch(InterruptedException ie) {}
      // Someone freed up a connection, so try again.
      return(getConnection());
    }
  }

  // You can't just make a new connection in the foreground
  // when none are available, since this can take several
  // seconds with a slow network connection. Instead,
  // start a thread that establishes a new connection,
  // then wait. You get woken up either when the new connection
  // is established or if someone finishes with an existing
  // connection.

  private void makeBackgroundConnection() {
    connectionPending = true;
    try {
      Thread connectThread = new Thread();
      connectThread.start();
    } catch(OutOfMemoryError oome) {
      // Give up on new connection
    }
  }

  public void run()
  {
    try
    {
      Connection connection = makeNewConnection();
        synchronized(this)
	{
          availableConnections.addElement(connection);
          connectionPending = false;
          notifyAll();
        }
    } catch(Exception e) { // SQLException or OutOfMemory
      // Give up on new connection and wait for existing one
      // to free up.
    }
  }

  // This explicitly makes a new connection. Called in
  // the foreground when initializing the ConnectionPool,
  // and called in the background when running.

  private Connection makeNewConnection()
      throws SQLException
  {
    Properties x = new Properties();
    x.put("user",username);
    x.put("password",password);
    //Next line ONLY for versions other than 2000
    //x.put("serverVersion",serverVersion);

    try
    {
      // Load database driver if not already loaded
      Class.forName(driver);
      // Establish network connection to database
      Connection connection =
        //DriverManager.getConnection(url,x);
	DriverManager.getConnection(url,username,password);

      return(connection);
    } catch(ClassNotFoundException cnfe)
      {
        // Simplify try/catch blocks of people using this by
        // throwing only one exception type.
        throw new SQLException("Can't find class for driver: " +
                             driver);
      }
  }

  public synchronized void free(Connection connection) {
    busyConnections.removeElement(connection);
    availableConnections.addElement(connection);
    // Wake up threads that are waiting for a connection
    notifyAll();
  }

  public synchronized int totalConnections() {
    return(availableConnections.size() +
           busyConnections.size());
  }

  /** Close all the connections. Use with caution:
   *  be sure no connections are in use before
   *  calling. Note that you are not required to
   *  call this when done with a ConnectionPool, since
   *  connections are guaranteed to be closed when
   *  garbage collected. But this method gives more control
   *  regarding when the connections are closed.
   */

  public synchronized void closeAllConnections() {
    closeConnections(availableConnections);
    availableConnections = new Vector();
    closeConnections(busyConnections);
    busyConnections = new Vector();
  }

  private void closeConnections(Vector connections) {
    try {
      for(int i=0; i<connections.size(); i++) {
        Connection connection =
          (Connection)connections.elementAt(i);
        if (!connection.isClosed()) {
          connection.close();
        }
      }
    } catch(SQLException sqle) {
      // Ignore errors; garbage collect anyhow
    }
  }

  public synchronized String toString() {
    String info =
      "ConnectionPool(" + url + "," + username + ")" +
      ", available=" + availableConnections.size() +
      ", busy=" + busyConnections.size() +
      ", max=" + maxConnections;
    return(info);
  }
/***********************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
	  String Message;
  try
     {
          ConnectionPool CP1 = new ConnectionPool("eBan","net.avenir.jdbcdriver7.Driver",1,1,false);
     }
      catch(SQLException sqle)
	  {Message=sqle.getMessage();}
  }
}
/***********************************************************************************/


