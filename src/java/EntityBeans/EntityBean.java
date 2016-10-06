package EntityBeans;

import java.sql.*;
import java.util.Date;
import JavaUtil.StringUtil;
import java.util.Enumeration;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Vector;
import DB_Connect.DB_Connect;
public class EntityBean {

    protected String SQL;

    //Audit Data
    protected String AuditTrailId,RecordId,VersionNumber,ModifiedType,ModifiedBy,ModifiedOn="";

    //Run Data
    protected String Run_Id="",FileName="";

    protected String Invoice_Region="",Global_Customer_Name,Account_Id;
    protected String Global_Customer_Id="",Message="";
    protected String Feed="",Tag_Id="",Tag_Name="",Tag_Value="";
    protected final int READ = 1;
    protected final int WRITE = 3;
    protected final int PREPARE = 2;
    protected final int CONNECT = 5;
    protected java.sql.ResultSet RS;
    protected  Connection Conn = null;
    protected CallableStatement cstmt;
    protected PreparedStatement pstmt;
    protected boolean select;
    protected String header;
    protected int border=0;
    protected int columns;
    protected Vector errored=new Vector();
    protected JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    protected DB_Connect DBC = new DB_Connect();
    protected final int NOT_INT=-2147483648;
    protected static final String GIVN = "P3";
    protected static final String Barclays = "P4";
    //private boolean Transaction=false;
    protected String Status_for_List="All",Account_for_List="All",GSR_for_List="All",Month_for_List="All",Charge_Type_for_List="All";
    protected String showFilters="hidden";

  public EntityBean()
  {
  }
  protected void Reset()
  {
     AuditTrailId=RecordId=VersionNumber=ModifiedBy=ModifiedOn="";
  }
  public String getAuditTrailId()
  {
   return SU.isNull(AuditTrailId,"");
  }
  public String getRecordId()
  {
   return SU.isNull(RecordId,"");
  }
  public String getVersionNumber()
  {
   return SU.isNull(VersionNumber,"");
  }
  public String getModifiedBy()
  {
   return SU.isNull(ModifiedBy,"");
  }
  public String getModifiedOn()
  {
   return SU.isNull(ModifiedOn,"");
  }
  public String getGlobalCustomerId()
  {
    return Global_Customer_Id;
  }
  public String getGlobalCustomerName()
  {
    return SU.isNull(Global_Customer_Name,"");
  }
  public String getInvoice_Region()
  {
    return SU.isNull(Invoice_Region,"");
  }
  public String getAccount_Id()
  {
    return Account_Id;
  }
  public String getRun_Id()
  {
    return Run_Id;
  }
  public String getFileName()
  {
    return FileName;
  }
   /***********************************************************************/
  public void setModifiedBy(String value)
  {
    ModifiedBy=SU.isNull(value,"");
  }
  public void setModifiedOn(String value)
  {
    ModifiedOn=SU.isNull(value,"");
  }
  public void setModifiedType(String value)
  {
    ModifiedType=SU.isNull(value,"");
  }
  public void setAuditTrailId(String newAuditTrailId)
  {
   AuditTrailId=newAuditTrailId;
  }
  public void setRecordId(String value)
  {
   RecordId=value;
  }
  public void setVersionNumber(String value)
  {
   VersionNumber=value;
  }
   public void setGlobalCustomerName(String newCustomer)
  {
   Global_Customer_Name = newCustomer;
  }
  public void setErrored(String Item)
  {
    if (Item.startsWith("clear"))
      errored.clear();
    else
      errored.addElement(Item);
  }
  public void setMessage(String newMessage)
  {
    Message=newMessage;
  }
  public void setRun_Id(String value)
  {
    Run_Id=value;
  }
  public void setTag_Id(String value)
  {
    Tag_Id=value;
  }
  public void setTag_Name(String value)
  {
    Tag_Name=value;
  }
  public void setFileName(String value)
  {
    FileName=value;
  }
  public void setTag_value(String value)
  {
    Tag_Value=value;
  }

  /*******************************************************************************/
  public boolean errors()
  {
    if(errored.isEmpty())
      return false;
    else
      return true;
  }
/******************************************************************************/
public void close (Connection Conn)
{
	try
	{
	  if (RS != null)
	  {
		  RS.close();
		}
		if (cstmt != null)
		{
		  cstmt.close();
	        }
		DBC.free(Conn);
	}
	catch(java.sql.SQLException se){System.out.println(se);}
}
public void closeupdate (Connection Conn)
{
    DBC.free(Conn);
}
protected void writeReject(String Message)
{
    StringBuffer InsertSQL = new StringBuffer("");

    InsertSQL.append("Insert into Barclays.Reject_Data values('");
    InsertSQL.append(Feed).append("','").append(FileName).append("','").append(Run_Id);
    InsertSQL.append(Tag_Id).append("','").append(Tag_Id).append("','").append(Tag_Name);
    InsertSQL.append("','").append(Message).append("')");

    DBC.NoResult(InsertSQL.toString(),Barclays);
  }
/*******************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    StringBuffer SQLBuffer = new StringBuffer("");
    String gn;
      DB_Connect DBC = new DB_Connect();
      SQLBuffer.append("Select Global_Customer_Name from givn..Global_Customer");
      SQLBuffer.append(" where Global_Customer_Id = 'bvd'");
  }
}