package LoadXML;

import DB_Connect.DB_Connect;
import java.sql.Connection;
import JavaUtil.StringUtil;

public class Load_XML
{
  private static boolean DEBUG=false;
  private static String FileType="",FileName="",Run_Id=null;
  private static DB_Connect DBC=new DB_Connect();
  private static String Barclays="P4";

  public static void main (String args[])
  {
    int i;
    Connection Conn;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();

    Conn=DBC.BeginTransaction("P4");
    if (Conn == null)
    {
      System.out.println("Failed to connect to the database with error:- "+DBC.getMessage());
      return;
    }
    Run_Id=SU.reformatDate("now","","yyyyMMddhhmmss");


    if (DEBUG)
    {
	FileName="c:\\Barclays\\XML\\ServiceItems.xml";
  	Load_Service_Items L=new Load_Service_Items();
	FileType = "Services";
        writeLog("Start of Load");
	L.LoadXML(FileName,Conn,Run_Id);
	writeLog("End of Load");
    }
    else
    {
      FileType=args[0];
      FileName=args[1];
      System.out.println("Loading " + FileType + " data from '" + FileName+"'");
      writeLog("Start of Load");

      if (FileType.compareToIgnoreCase("Profile")==0)
      {
	Load_Profile LP=new Load_Profile();
	LP.LoadXML(args[1],Conn,Run_Id);
      }
      else if (FileType.compareToIgnoreCase("Services")==0)
      {
	Load_Service_Items LS=new Load_Service_Items();
	if (LS.LoadXML(args[1],Conn,Run_Id))
	{
	}
	else
	{
	  writeLog(LS.getMessage());
	}
      }
      else if (FileType.compareToIgnoreCase("Audit")==0)
      {
	Load_Audit LA=new Load_Audit();
	LA.LoadXML(args[1],Conn,Run_Id);
      }
      else
      {
	writeLog("Invalid Feed parameter. Permissible values are - Services,Audit or Profile");
	System.out.println("Invalid parameter 1 value. Permissible values are - Services,Audit or Profile");
      }
      writeLog("End of Load");
    }
    //DBC.CommitTransaction(Conn);
  }
  private static void writeLog(String Message)
  {
    StringBuffer InsertSQL = new StringBuffer("");

    InsertSQL.append("Insert into Barclays..Load_Log values('");
    InsertSQL.append(FileType).append("','").append(FileName).append("','").append(Run_Id);
    InsertSQL.append("',getdate(),'").append(Message).append("')");

    DBC.NoResult(InsertSQL.toString(),Barclays);

  }

}
