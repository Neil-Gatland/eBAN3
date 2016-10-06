package DBUtilities;

import java.sql.*;
import java.util.Date;
import JavaUtil.StringUtil;
import java.util.Enumeration;
import java.math.BigInteger;
import java.util.Vector;
import JavaUtil.EBANProperties;

public class GCTBBean  extends BANBean
{
    protected String SQL;

  protected DBAccess DBA = new DBUtilities.DBAccess();
  protected static final String P5 = "P5";
  protected String Message="";


  public GCTBBean()
  {
  }
  protected void Reset()
  {
  }
/*****************************************************************************************/
  public boolean insertConnectionLog(String status, String userId)
  {
    boolean insertLog = false;
    try
    {
      SQL = "INSERT INTO givn_ref..Connection_Log (Logon_Id, Connection_Status) " +
        "VALUES('" + userId + "','" + status + "') ";
      insertLog = DBA.Connect(DBA.NORESULT,P5);
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      DBA.Close();
      return insertLog;
    }

  }
/*****************************************************************************/
  public  Enumeration getUser(String UserId,String Password)
  {
        SQL="select Billing_Team,Logon_Id,Rights1,Rights2,Rights3,Rights4," +
          "Rights5,Rights6,Delete_Check from Givn_Ref..Logon where Logon_Id = '" +
          UserId + "' and Logon_Password = '" + Password + "' ";
	Vector Result = new Vector();
      try
      {
        if (DBA.Connect(DBA.PREPARE,P5))
        {
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.execute();
          RS = cstmt.getResultSet();
          if (RS.next())
          {
            Result.addElement(RS.getString(1));
            Result.addElement(RS.getString(2));
            Result.addElement(RS.getString(2));
            Result.addElement(RS.getString(3));
            Result.addElement(RS.getString(4));
            Result.addElement(RS.getString(5));
            Result.addElement(RS.getString(6));
            Result.addElement(RS.getString(7));
            Result.addElement(Integer.toString(RS.getInt(8)));
          }
          DBA.Close();
	  if (!Result.isEmpty())
	  {
	    Message = "";
	  }
	  else
	  {
	    Message = "<font color=red><b>Invalid User Name or Password</font>";
	  }
	}
	else
	{
	  //Connection failed
	  //Result.addElement("<font color=red<b>"+Message);
	  //Result.addElement("");
	  Message = "<font color=red><b>"+Message;
	}
      }
      catch(java.sql.SQLException se)
      {
        Result.addElement(se.getMessage());
        DBA.Close();
      }
      return Result.elements();
  }
}
