package DBUtilities;
import java.sql.*;
import java.io.*;
import java.util.*;
import DBUtilities.ConnectionPool;
import JavaUtil.*;
import oracle.sql.*;
import oracle.jdbc.driver.OracleTypes;

public class DBAccess
{
	//Private Data
	private String SQL = "";
	private String Grid = "<tr><td nowrap align=right>&nbsp;&nbsp<font color=blue><b>No Data found</tr></td>";
	protected String DB;
	protected String ListDataSource;
	private String Table;
	private int Columns;
	private int RowCount;
	private java.sql.ResultSet RS;
	protected  Connection Conn = null;
	protected ConnectionPool Pool = null;
	protected ConnectionPool P5Pool = null;
	protected java.sql.Statement Stmt;
	protected String Message="";
	private String[] Key;
	protected String Qualifier;
	private String[] Fields;
	private Enumeration Options;
	private String Header;
	private int FromPos;
	private int maxAccounts;
        private String User_Id;
	private String Account_Id;
	private final char quote = '"';
	private final int READ = 1;
	protected  final int PREPARE = 2;
	protected  final int WRITE = 3;
	protected  final int NORESULT = 4;
	protected  String P3ServerName="",P5ServerName="",P4ServerName="";
	protected  int P3Connections=5,P5Connections=5,P4Connections=5;
	private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
	private JavaUtil.FileUtil FU = new JavaUtil.FileUtil();
	protected String Name,Value,KeyValue,OrderBy,DBTable,Alert,GroupBy;
	private static final String P3 = "P3";
	private static final String P5 = "P5";
	private static final String P4 = "P4";
	private static final String GOLDFISH = "GOLDFISH";
	private String sharedPath;
        protected int selectSize;
        protected String selectStyle;
        protected boolean addBlankLine;
        private String extraQualifierValue;
        private String extraQualifierValue2;
        private int tabIndex;
	private String Quote="\"";
        private int environment;
        private int connections;
        private String serverName;
        private int environmentGF;
        private int connectionsGF;
        private String serverNameGF;
        private double vatRate = 1 +
          (Double.parseDouble(EBANProperties.getEBANProperty(EBANProperties.VATRATE))/100);
        private String arborControlFileName;
        private ResultSet rsExt = null;
        private CallableStatement cstmtExt = null;

	//Private Methods
protected boolean Connect(int MODE,String DataSource)
{
try{
  if (DataSource.equals(GOLDFISH))
  {
    Pool = GoldfishDataSource.getInstance(serverNameGF,connectionsGF,environmentGF);
  }
  else
  {
    Pool = UniDataSource.getInstance(serverName,connections,environment);
  }
  try
  {
    Conn = Pool.getConnection();
    if (Conn != null)
    {
      if (MODE==READ)
      {//Connect and Run with Query in SQL string
        Stmt = Conn.createStatement();
        RS = Stmt.executeQuery(SQL);
      }
      else if (MODE==NORESULT)
      {//No result so open execute and close
        Stmt = Conn.createStatement();
        Stmt.execute(SQL);
        Close();
      }
      else
      {//Connect only
        return true;
      }
    }
    else
    {
       return false;
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
    System.out.println(Message);
    return false;
  }
  catch(java.lang.NullPointerException se)
  {
    Message=Pool.Message;
    System.out.println(Message);
    return false;
  }//message set in underlying code
  return true;
}
catch (Exception ex)
{
  Message = ex.getMessage();
  return false;
}

}
public void closeExt()
{
  try
  {
    if (rsExt != null)
    {
      rsExt.close();
      rsExt = null;
    }
    cstmtExt.close();
    cstmtExt = null;
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException ne)
  {
    Message=ne.getMessage();
  }
  finally {Pool.free(Conn);}
}

public Connection connectExt()
{
  Connection ext = null;
  try
  {
    Pool = UniDataSource.getInstance(serverName,connections,environment);
    ext = Pool.getConnection();
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
    System.out.println(Message);
  }
  finally
  {
    return ext;
  }
}
public void freeConnectExt(Connection ext)
{
  Pool.free(ext);
}
public ResultSet execSQLServerStoredProc(String database, String storedProc,
  Collection parameters)
{
  //don't forget to invoke closeExt() after you've finished with the
  //ResultSet returned by this method
  StringBuffer sql = new StringBuffer("{call " + database + ".." +storedProc + "(");
  for (int i = 0; i < parameters.size(); i++)
  {
    sql.append("?,");
  }
  if (parameters.size() == 0)
  {
    sql.append(")");
  }
  else
  {
    sql.setCharAt(sql.length()-1,')');
  }
  sql.append("}");
  try
  {
    if (Connect(PREPARE,P5))
    {
      cstmtExt = Conn.prepareCall(sql.toString());
      for (Iterator it = parameters.iterator(); it.hasNext(); )
      {
        StoredProcParamDescriptor sppd = (StoredProcParamDescriptor)it.next();
        switch (sppd.getTypeNumber())
        {
          case 0: cstmtExt.setString(sppd.getPosition(), sppd.getStringValue());
          break;
          case 1: cstmtExt.setLong(sppd.getPosition(), sppd.getLongValue());
          break;
          case 2: cstmtExt.setInt(sppd.getPosition(), sppd.getIntValue());
          break;
          case 3: cstmtExt.setBoolean(sppd.getPosition(), sppd.getBooleanValue());
          break;
          case 4: cstmtExt.setFloat(sppd.getPosition(), sppd.getFloatValue());
          break;
          case 5: cstmtExt.setDouble(sppd.getPosition(), sppd.getDoubleValue());
          break;
          default: throw new Exception("Unknown stored procedure parameter type: " + sppd.getType());
        }
      }
      cstmtExt.execute();
      rsExt = cstmtExt.getResultSet();
    }
  }
  catch(Exception ex)
  {
    Message=ex.getMessage();
  }
  finally
  {
    return rsExt;
  }
}
/**************************************************************************************************/
protected Vector ConnectIso(int MODE,String DataSource, String isoSQL)
{
  Connection isoConn = null;
  java.sql.ResultSet isoRS = null;
  java.sql.Statement isoStmt = null;
  //CallableStatement isoCstmt = null;
  Vector ret = new Vector(2);

//System.out.println("Connect 1");
  if (DataSource.equals(GOLDFISH))
  {
    Pool = GoldfishDataSource.getInstance(serverNameGF,connectionsGF,environmentGF);
  }
  else
  {
    Pool = UniDataSource.getInstance(serverName,connections,environment);
  }
//System.out.println("Connect 2");

  try
  {
//System.out.println("Connect 3");
    isoConn = Pool.getConnection();
//System.out.println("Connect 4");
    if (isoConn != null)
    {
//System.out.println("Connect 5");
      if (MODE==READ)
      {//Connect and Run with Query in SQL string
//System.out.println("Connect 6");
        isoStmt = isoConn.createStatement();
        isoRS = Stmt.executeQuery(isoSQL);
        ret.add(isoStmt);
        ret.add(isoRS);
//System.out.println("Connect 7");
      }
      else if (MODE==NORESULT)
      {//No result so open execute and close
        isoStmt = isoConn.createStatement();
        isoStmt.execute(SQL);
        CloseIso(isoStmt, null, isoRS, isoConn);
      }
      else
      {//Connect only
        ret.add(isoConn);
        return ret;
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
//System.out.println("Connect 9: " + Message);
  }
  catch(java.lang.NullPointerException se)
  {
    Message=Pool.Message;
//System.out.println("Connect 10: " + Message);
  }//message set in underlying code
  finally
  {
    return ret;
  }
}
/**************************************************************************************************/
protected void CloseIso(Statement isoStmt, CallableStatement isoCstmt,
  ResultSet isoRs, Connection isoConn)
{
	try
	{
	  if (isoRs != null)
	  {
            try
            {
              isoRs.close();
            }
            catch (java.sql.SQLException se)
            {
            }
            finally
            {
              isoRs = null;
            }
	  }
          if (isoStmt != null)
          {
    	    isoStmt.close();
          }
          if (isoCstmt != null)
          {
    	    isoCstmt.close();
          }
	}
	catch(java.sql.SQLException se)
        {
          //Message=se.getMessage();
        }
	catch(java.lang.NullPointerException ne)
        {
          //Message=ne.getMessage();
        }
        finally
        {
          if (isoConn != null)
          {
            Pool.free(isoConn);
          }
        }
}
/**************************************************************************************************/
protected void Close ()
{
	try
	{
	  if (RS != null)
	  {
	    RS.close();
	  }
	  Stmt.close();

	}
	catch(java.sql.SQLException se)
        {
          //Message=se.getMessage();
        }
	catch(java.lang.NullPointerException ne)
        {
          Message=ne.getMessage();
        }
        finally {Pool.free(Conn);}
}
/**************************************************************************************************/
private Vector Connect(String thisSQL, String dataSource)
{
        java.sql.Statement thisStmt;
        java.sql.ResultSet thisRs;
        Vector ret = new Vector(2);

        if (dataSource.equals(GOLDFISH))
        {
          Pool = GoldfishDataSource.getInstance(serverNameGF,connectionsGF,environmentGF);
        }
        else
        {
          Pool = UniDataSource.getInstance(serverName,connections,environment);
        }

	try
	{
          Conn = Pool.getConnection();
	  if (Conn != null)
	  {
            thisStmt = Conn.createStatement();
            thisRs = thisStmt.executeQuery(thisSQL);
            ret.add(thisStmt);
            ret.add(thisRs);
	  }
	}
        catch(java.sql.SQLException se)
        {
          Message=se.getMessage();
        }
	catch(java.lang.NullPointerException se)
	{
          Message=Pool.Message;
	}//message set in underlying code
  return ret;
}
private void Close (java.sql.Statement thisStmt, java.sql.ResultSet thisRs)
{
	try
	{
	  if (thisRs != null)
	  {
            try
            {
              thisRs.close();
            }
            catch (java.sql.SQLException se)
            {
            }
            finally
            {
              thisRs = null;
            }
	  }
          if (thisStmt != null)
          {
    	    thisStmt.close();
          }
	}
	catch(java.sql.SQLException se)
        {
          //Message=se.getMessage();
        }
	catch(java.lang.NullPointerException ne)
        {
          //Message=ne.getMessage();
        }
        finally {Pool.free(Conn);}
}
protected void cstmtClose (CallableStatement cstmt)
{
	try
	{
	  cstmt.close();

	}
	catch(java.sql.SQLException se)
        {
          //Message=se.getMessage();
        }
	catch(java.lang.NullPointerException ne)
        {
          //Message=ne.getMessage();
        }
        finally {Pool.free(Conn);}
}
public DBAccess()
{
  sharedPath = EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  maxAccounts = Integer.parseInt(EBANProperties.getEBANProperty(EBANProperties.MAXACCOUNTS));
  selectSize = 1;
  selectStyle = "";
  GroupBy = "";
  addBlankLine = true;
  tabIndex = 0;
  environment = Integer.parseInt(EBANProperties.getEBANProperty(EBANProperties.ENVIRONMENT));
  connections = Integer.parseInt(EBANProperties.getEBANProperty(EBANProperties.CONNECTIONS));
  serverName = EBANProperties.getEBANProperty(EBANProperties.SERVERNAME);
  environmentGF = Integer.parseInt(EBANProperties.getEBANProperty("environmentGF",Integer.toString(environment)));
  connectionsGF = Integer.parseInt(EBANProperties.getEBANProperty("connectionsGF",Integer.toString(connections)));
  serverNameGF = EBANProperties.getEBANProperty("serverNameGF",serverName);
}
public  void setSQL(String newSQL)
{
  SQL = newSQL;
}
protected  void setMessage(String newMessage)
{
  Message = newMessage;
}
public  int getRowCount()
{
  return RowCount;
}
public  String getMessage()
{
  return Message;
}
public  void setKey(String[] newKey)
{
  Key=newKey;
}
private void setListBoxMetaData(String TableName,String QualifierValue)
{
      if (TableName.equals("GCB_Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region) + ' (' + Account_Id + ')'";
	Value = "Account_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Alert = "'Please Select a Customer first'";
	OrderBy = "1";
	DBTable="GCB_Accounts";
      }
      else if (TableName.equals("GCB_Account2"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region) + ' (' + Account_Id + ')'";
	Value = "Invoice_Region";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Alert = "'Please Select a Customer first'";
	OrderBy = "1";
	DBTable="GCB_Accounts";
      }
      else if (TableName.equals("Invoice_Number"))
      {
	/*DB="givn_ref";
	ListDataSource=P3;
	Name = "Invoice_No";// +'/'+ convert(char(11),Billing_Period_Start_Date,113)";
	Value = "Invoice_No";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+
          "' and Invoice_Region = '"+extraQualifierValue+"' ";
	Alert = "'Please Select an Invoice Region first'";
	OrderBy = "1";
	DBTable="IRIN_Manual";*/
	DB="gcd";
	ListDataSource=P3;
	Name = "Invoice_No";// +'/'+ convert(char(11),Billing_Period_Start_Date,113)";
	Value = "Invoice_No";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+
          "' and Invoice_Region = '"+extraQualifierValue+"' " +
          "and Invoice_No like '4%' ";
	Alert = "'Please Select an Invoice Region first'";
	OrderBy = "1";
	DBTable="Monthly_Billing";
      }
      else if (TableName.equals("Ad_Hoc_Invoice_Number"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Invoice_No";
	Value = "Invoice_No";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+
          "' and Invoice_Region = '"+extraQualifierValue+"' " +
          "and (Invoice_No like '4%' OR Invoice_No like '8%') ";
	Alert = "'Please Select an Invoice Region first'";
	OrderBy = "1 desc";
	DBTable="Monthly_Billing_Invoice_Region";
      }
      else if (TableName.equals("adjustmentCurrency"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Outgoing_Currency_Code";
	Value = "Outgoing_Currency_Code";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+
          "' and Invoice_Region = '"+extraQualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable="Invoice_Region";
      }
      else if (TableName.equals("adjustmentType"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Definition";
	Value = "Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Adjustment_Type";
      }
      else if (TableName.equals("revenueType"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Revenue_Type_Description";
	Value = "Revenue_Type_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Revenue_Type";
      }
      else if (TableName.equals("revenueDescription"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Revenue_Reason_Description";
	Value = "Revenue_Reason_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Revenue_Reason";
      }
      else if (TableName.equals("netOrFull"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Revenue_Net_Or_Full";
	Value = "Revenue_Net_Or_Full_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Revenue_Net_Or_Full";
      }
      else if (TableName.equals("rootCause"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "Revenue_Root_Cause";
	Value = "Revenue_Root_Cause_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Revenue_Root_Cause";
      }
      else if (TableName.equals("chargeEntity"))
      {
	DB="oss";
	ListDataSource=P3;
	Name = "Charge_Entity_Code";
	Value = "Charge_Entity_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Charge_Entity";
      }
      else if (TableName.equals("New_Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region)";
	Value = "Account_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' " +
          "and Billing_Region like 'Real Time Nostro Users%' and "+
          "Account_Id not in (select Account_Id from eban..Payment_Group_Account (nolock) "+
          "where Payment_Group_Id = "+extraQualifierValue+")";
	Alert = "";
	OrderBy = "1";
	DBTable="Billing_Region";
      }
      else if (TableName.equals("Existing_Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region)";
	Value = "Account_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' " +
          "and Billing_Region like 'Real Time Nostro Users%' and "+
          "Account_Id in (select Account_Id from eban..Payment_Group_Account (nolock) "+
          "where Payment_Group_Id = "+extraQualifierValue+")";
	Alert = "";
	OrderBy = "1";
	DBTable="Billing_Region";
      }
      else if (TableName.equals("Nostro_Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region)";
	Value = "Account_Id";
	//Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' " +
          "and Billing_Region like 'Real Time Nostro Users%'";
	Alert = "'Please Select a Customer first'";
	OrderBy = "1";
	//DBTable="Invoice_Region";
	DBTable="Billing_Region";
      }
      else if (TableName.equals("Ad_Hoc_Account"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "distinct(Account_No)";
	Value = "count(*)";
	Qualifier = " and Account_No like '"+QualifierValue.replace('*','%')+"' ";
	Alert = "'Please enter a value into the Account Filter first'";
	OrderBy = "";
	GroupBy = "Account_No";
	DBTable=TableName;
      }
      else if (TableName.equals("Ad_Hoc_Accounts"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "distinct(Account_No)";
	Value = "Account_No";
	Qualifier = " and status in (select ban_status_code from eban..ban_list (nolock) where action = '" +
          QualifierValue + "') ";
	OrderBy = "2";
	DBTable="Invoice_List_View";
      }
      else if (TableName.equals("Ad_Hoc_Party_Management"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Responsibility + ': ' + Name";
	Value = "Responsibility";
	Qualifier = " and Account_No = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Billing_Source"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Billing_Source";
	Value = "Billing_Source";
	Qualifier = " and Account_No = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable="Ad_Hoc_Account";
      }
      else if ((TableName.equals("conglomBilledProduct")) ||
        (TableName.equals("conglomBilledProduct2")) ||
        (TableName.equals("conglomBilledProduct3")) ||
        (TableName.equals("conglomBilledProduct4")))
      {
        if (extraQualifierValue.equals("all"))
        {
          DB="conglomerate";
          ListDataSource=P5;
          Name = "distinct b.Description";
          Value = "b.Billed_Product_Id";
          Qualifier = " and b.Billed_product_Id <> 'none' ";
          Alert = "";
          OrderBy = "b.Description";
          DBTable="Billed_Product b ";
        }
        else if (extraQualifierValue.equals("goldfish"))
        {
          DB="conglomerate";
          ListDataSource=P5;
          Name = "distinct b.Description";
          Value = "b.Billed_Product_Id";
          Qualifier = " and a.Billed_Product_Id = b.Billed_product_Id " +
            "and a.conglom_cust_id = " + QualifierValue + " " +
            "and a.bill_eff_from_date <= getdate() " +
            "and b.Billed_Product_Id IN ('SSVO','PCBL') " +
            "and (a.bill_eff_to_date > getdate() or a.bill_eff_to_date is null) ";
          Alert = "";
          OrderBy = "b.Description";
          DBTable="Conglom_Cust_Product a (nolock), conglomerate..Billed_Product b ";
        }
        else if (extraQualifierValue.equals("cust"))
        {
          if (TableName.equals("conglomBilledProduct4"))
          {
            DB="conglomerate";
            ListDataSource=P5;
            Name = "distinct b.Description";
            Value = "b.Billed_Product_Id";
            Qualifier = " and i.Billed_Product_Id = b.Billed_product_Id " +
              "and i.conglom_cust_id = " + QualifierValue + " ";
            Alert = "";
            OrderBy = "b.Description";
            DBTable="Billing_Items i (nolock), conglomerate..Billed_Product b ";
/*
	Select distinct p.Description, P.Billed_Product_Id
	from Billing_Items i, Billed_Product p
	where --i.Last_Update_Id = @User_Id
	 i.Conglom_Cust_Id = 54
	and p.Billed_Product_Id = i.Billed_Product_Id

*/
          }
          else
          {
            DB="conglomerate";
            ListDataSource=P5;
            Name = "distinct b.Description";
            Value = "b.Billed_Product_Id";
            Qualifier = " and a.Billed_Product_Id = b.Billed_product_Id " +
              "and a.conglom_cust_id = " + QualifierValue + " " +
              "and a.bill_eff_from_date <= getdate() " +
              "and (a.bill_eff_to_date > getdate() or a.bill_eff_to_date is null) ";
            Alert = "";
            OrderBy = "b.Description";
            DBTable="Conglom_Cust_Product a (nolock), conglomerate..Billed_Product b ";
          }
        }
        else
        {
          DB="conglomerate";
          ListDataSource=P5;
          Name = "distinct b.Description";
          Value = "b.Billed_Product_Id";
          Qualifier = " and a.Billed_Product_Id = b.Billed_product_Id " +
            "and b.feed_source = '" + QualifierValue + "' ";
          Alert = "";
          OrderBy = "b.Description";
          DBTable="Conglom_Cust_Product a (nolock), conglomerate..Billed_Product b ";
        }
      }
      else if (TableName.equals("Conglom_Exception_Type"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct Description";
	Value = "convert(char(3),ET.Exception_Type)";
	Qualifier = " and ET.Exception_Type = CBE.Exception_Type";
	Alert = "";
	OrderBy = "Description";
	DBTable="Exception_Type ET (nolock), conglomerate..Conglom_Exception CBE ";
      }
      else if (TableName.equals("Exception_Type"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Description";
	Value = "convert(char(3),Exception_Type)";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("Adjustment_Type"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Definition";
	Value = "Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Revenue_Type"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Revenue_Type_Description";
	Value = "Revenue_Type_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Revenue_Reason"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Revenue_Reason_Description";
	Value = "Revenue_Reason_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Revenue_Root_Cause"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Revenue_Root_Cause";
	Value = "Revenue_Root_Cause_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Revenue_Net_Or_Full"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Revenue_Net_Or_Full";
	Value = "Revenue_Net_Or_Full_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if (TableName.equals("Charge_Entity_Code"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Charge_Entity_Code";
	Value = "Charge_Entity_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if ((TableName.equals("Exception_Status")) ||
        (TableName.equals("Exception_Status2")))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Exception_Status";
	Value = "Exception_Status";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Exception_Status";
      }
      else if ((TableName.equals("Conglom_Exception_Status")) ||
        (TableName.equals("Conglom_Exception_Status2")))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "Exception_Status";
	Value = "Exception_Status";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable="Exception_Status";
      }
      else if (TableName.equals("Conglom_Inv_Doc"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct Docket_Invoice";
	Value = "Docket_Invoice";
	Qualifier = " and Conglom_Cust_Id = "+QualifierValue+" ";
	Alert = "";
	OrderBy = "2";
	DBTable="Conglom_Exception";
      }
      else if (TableName.equals("Conglom_Billed_Product"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct Billed_Product_Id";
	Value = "Billed_Product_Id";
	Qualifier = " and Conglom_Cust_Id = "+QualifierValue+" ";
	Alert = "";
	OrderBy = "2";
	DBTable="Conglom_Exception";
      }
      else if (TableName.equals("Conglom_Period"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct (substring(Bill_Period_Ref,3,2) + substring(Bill_period_Ref,1,2))";
	Value = "Bill_Period_Ref";
	Qualifier = " and Conglom_Cust_Id = "+QualifierValue+" ";
	Alert = "";
	OrderBy = "2";
	DBTable="Conglom_Exception";
      }
      else if (TableName.equals("GSR"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Global_SRV_Reference";
	Value = "Global_SRV_Reference";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable="Data_Billing_Exceptions";
      }
      else if (TableName.equals("IRIN"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "DISTINCT Invoice_Region + case when Invoice_No is not null then ' / ' + Invoice_No else '' end";
	Value = "Invoice_Region + case when Invoice_No is not null then ' / ' + Invoice_No else '' end";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "2";
	DBTable="Data_Billing_Exceptions";
      }
      else if (TableName.equals("Discount_Plan"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "dt.Discount_Description + ' (' + convert(varchar(11), dp.Plan_Start_Date, 106) " +
    "+ '-' + CASE WHEN dp.Plan_End_Date IS NULL THEN ' ' ELSE convert(varchar(11), " +
    "dp.Plan_End_Date, 106) END + ')'";
	Value = "dp.Discount_Id";
	Qualifier = " and dt.Discount_Type COLLATE DATABASE_DEFAULT = dp.Discount_Type COLLATE DATABASE_DEFAULT and dp.Account_Id = '"+QualifierValue+"' ";
	Alert = "'Please select an account first'";
	OrderBy = "2";
	DBTable="Discount_Type dt (nolock), Discount_Plan dp";
      }
      else if (TableName.equals("Discount_Type"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Discount_Description";
	Value = "Discount_Type";
	Qualifier = " and Discount_Type LIKE '"+QualifierValue+"%' ";
	Alert = "";
	OrderBy = "1";
	DBTable="Discount_Type";
      }
      else if (TableName.equals("User_Id"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Login_Id";
	Value = "Login_Id";
	Qualifier = " and System = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable="eBAN_User";
      }
      else if (TableName.equals("User_Group"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Group_Name";
	Value = "Group_Name";
	Qualifier = " and System = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("Nostro_Reference"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Sales_Order_Number";
	Value = "Sales_Order_Number";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Alert = "'Please Select a Customer first'";
	OrderBy = "1";
	DBTable="Customer_Sales_Order";
      }
      else if (TableName.equals("BAN_Billing_Region"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Billing_Region";
	Value = "Billing_Region";
	Qualifier = " and BAN_Identifier = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="IR_BAN_Billing_Region";
      }
      else if (TableName.equals("BAN_GCD"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "GCD_Name";// + ' (' + GSR_Prefix + ')'";
	Value = "GCD_Name + '|' + GSR_Prefix";
	Qualifier = " and BAN_Identifier = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="GCB_Cust_BAN_GCD";
      }
      else if (TableName.equals("GCB_Download_Report"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Report_Name";
	Value = "Report_Code";
	Qualifier = " and Display_Report = 'Y'";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("LLU_Global_Customer"))
      {
	DB="givn";
	ListDataSource=P5;
	Name = "distinct gc.Global_Customer_Name";
	Value = "gc.Global_Customer_Id";
	Qualifier = " and gc.global_customer_id = gcb.global_customer_id and " +
          "gcb.product_type = 'LLUF' ";
	OrderBy = "2";
	DBTable="Global_Customer gc (nolock), gcd..Global_Customer_Billing gcb ";
      }
      else if (TableName.equals("LLU_GSR"))
      {
	DB="gcd";
	ListDataSource=P5;
	Name = "distinct Service_Reference";
	Value = "Service_Reference";
	Qualifier = " and global_customer_id = '"+QualifierValue+"' and " +
          "product_type = 'LLUF' ";
	OrderBy = "2";
	DBTable="Global_Customer_Billing";
      }
      else if (TableName.equals("Billing_Region"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "Billing_Region";
	Value = "Billing_Region";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("Billing_Region_Site"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "Billing_Region+' (' + Account_Id + ')'";
	Value = "Billing_Region";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	OrderBy = "Billing_Region";
	DBTable="Billing_Region";
      }
      else if (TableName.equals("Splits"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Sequence_No";
	Value = "Sequence_No";
	Qualifier = " and Sequence_No >= " + QualifierValue +
          " and Sequence_No <= " + extraQualifierValue + " ";
	OrderBy = "1";
	DBTable="Sequence";
      }
      else if (TableName.equals("Nostro_User"))
      {
	DB="Nostro";
	ListDataSource=P5;
	Name = "User_Id";
	Value = "User_Id";
	Qualifier = " and Account_Id = '"+QualifierValue+"'";
	Alert = "'Please Select an Account first'";
	OrderBy = "Surname,First_Name";
	DBTable="Nostro_User";
      }
      else if (TableName.equals("Payment_Group"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Payment_Group_Name";
	Value = "Payment_Group_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Alert = "'Please Select a Customer first'";
	OrderBy = "1";
	DBTable="Nostro_Payment_Group";
      }
      else if (TableName.equals("Package"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Description";
	Value = "CONVERT(varchar,Package_Id)";
	Qualifier = " and Type = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "2";
	DBTable=TableName;
      }
      else if ((TableName.equals("Sales_Business_Unit")) ||
        (TableName.equals("Automated_Back_Billing")) ||
        (TableName.equals("DOB_Date")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Global_Billing_Analyst"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Logon_Id + ' - ' + User_Firstname + ' ' + User_Surname";
	Value = "Logon_Id";
	Qualifier = " and Logon_Id is not null ";
	Alert = "";
	OrderBy = "User_Surname, User_Firstname";
	DBTable="Logon";
      }
      else if (TableName.equals("Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Account_Id";
	Value = "Account_Id";
	Qualifier = " and (Global_Customer_Id = '"+QualifierValue+"' or '"+QualifierValue+"' = 'All')";
	Alert = "";
	OrderBy = "1";
	DBTable=extraQualifierValue.equals("MANS")?"MANS_Accounts":"OSS_Accounts";
      }
      else if (TableName.equals("Product"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("usState"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text + ' (' + Value + ')'";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if ((TableName.equals("Credit_Debit")) ||
				(TableName.equals("invoiceCRDE")) ||
				(TableName.equals("adjustmentCRDE")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Fixed_Charge_Type"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Product"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"'";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Ad_Hoc_Product"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Prime_Product_Name";
	Value = "Prime_Product_Code";
	Qualifier = "";
	Alert = "";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("GCD_Id"))
      {
	DB="GIVN";
	ListDataSource=P3;
	Name = "Global_Customer_Division_Id + '(' + Global_Srv_Reference_Prefix + ')'";
	Value = "Global_Customer_Division_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "'Please select a Customer first'";
	OrderBy = "Global_Customer_Division_Id";
	DBTable="Global_Customer_Division";
      }
      else if ((TableName.equals("FromEnd_Site_Reference")) ||
        (TableName.equals("ToEnd_Site_Reference")) ||
        (TableName.equals("Site_Reference")) ||
        (TableName.equals("Site_Reference2")) ||
        (TableName.equals("Site_Reference3")))
      {
	DB="GIVN_REF";
	ListDataSource=P3;
	Name = "'(' + ISNULL(Account_Id,'') + ') ' + RTRIM(Site_Id) + '/' + Site_Name";
	Value = "Site_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "Site_Id";
	DBTable="Site";
      }
      else if (TableName.equals("Fault_Code"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Product_Type"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("Charge_Description"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Text";
	Qualifier = " and Package_Id = "+QualifierValue+" and Type = '"+
          extraQualifierValue+"'";
	Alert = "'Please Select a Package first'";
	OrderBy = "Description_Id";
	DBTable="Description";
      }
      else if (TableName.equals("Account_Details"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Invoice_Region + '/' + Account_Id + '/' + Primary_Account_Ind";
	Value = "Account_Id";
	Qualifier = " and (Nostro_Account_Reference = '"+QualifierValue+"' or '"+QualifierValue+"' = 'All')";
	Alert = "'Please select a Nostro Reference first'";
	OrderBy = "Invoice_Region";
	DBTable="Invoice_Region";
      }
      else if (TableName.equals("Barclays_Account"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Account_Id";
	Value = "Account_Id";
	Qualifier = " and (Global_Customer_Id = '"+QualifierValue+"' or '"+QualifierValue+"' = 'All')";
	Alert = "";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("Global_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = QualifierValue.equals("MANS")?" and Global_Customer_Id in " +
          "(select distinct Global_Customer_Id from givn..Billing_Period_Run_Control " +
          "where DOB = 'adhoc') ":"";
	OrderBy = "Global_Customer_Name";
	DBTable=QualifierValue.equals("MANS")?"MANS_Customers":"OSS_Customers";
      }
      else if (TableName.equals("conglomCustomer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "distinct GC.Global_Customer_Id + ' ' + GC.Global_Customer_Name";
	Value = " GC.Global_Customer_Id";
	Qualifier = " and GC.Global_Customer_Id = CA.Global_Customer_Id and " +
          "GC.Global_Customer_Id = GCA.Global_Customer_Id and " +
          "GCA.Application_Id = A.Application_Id and " +
          "A.Billing_Type = 'Conglom' and " +
          "CA.Logon_Id = '" + QualifierValue + "' ";
	OrderBy = "1";
	DBTable = "Global_Customer GC (nolock), givn..Customer_Access CA (nolock), " +
          "givn..Global_Customer_Application GCA (nolock), givn_ref..Application A ";
      }
/*
from Global_Customer GC, Customer_Access CA,Global_Customer_Application GCA,givn_ref..application A
Where GC.Global_Customer_Id <> '",W_SelectedCustomer,"'
and GC.Global_Customer_Id = CA.Global_Customer_Id
and CA.Logon_Id = '", W_UserId, "'
and GC.Global_Customer_Id = GCA.Global_Customer_Id
and GCA.Application_Id = A.Application_Id
and A.Billing_Type = 'Conglom'")
*/

      else if (TableName.equals("GCB_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Id + ' ' + Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id is not null and " +
          "Global_Customer_Id in (select distinct Global_Customer_Id from " +
          "givn..customer_access(nolock) where logon_id = '" + QualifierValue + "') ";
	OrderBy = "Global_Customer_Id";
	DBTable="GCB_Customers";
      }
      else if (TableName.equals("GCB_Customer2"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Id + ' ' + Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id is not null and " +
          "Global_Customer_Id in (select distinct Global_Customer_Id from " +
          "givn..customer_access(nolock) where logon_id = '" + QualifierValue + "') ";
	OrderBy = "Global_Customer_Id";
	DBTable="GCB_Customers";
      }
      else if (TableName.equals("GCB_Adhoc_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Id + ' ' + Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id is not null and " +
          "Global_Customer_Id in (select distinct Global_Customer_Id from " +
          "givn..customer_access(nolock) where logon_id = '" + QualifierValue + "') ";
	OrderBy = "Global_Customer_Id";
	DBTable="GCB_Adhoc_Customers";
      }
      else if (TableName.equals("GCB_Customer3"))
      {
        if (QualifierValue.equals("true"))
        {
          DB="Conglomerate";
          ListDataSource=P3;
          Name = "Conglom_Cust_Name + ' (' + convert(varchar,Conglom_Cust_Id) + ')'";
          Value = "Conglom_Cust_Id";
          Qualifier = "";
          OrderBy = "Conglom_Cust_Name";
          DBTable="Conglom_Customer";
        }
        else
        {
          DB="givn";
          ListDataSource=P3;
          Name = "Global_Customer_Id + ' ' + Global_Customer_Name";
          Value = "Global_Customer_Id";
          Qualifier = "";
          OrderBy = "Global_Customer_Id";
          DBTable="GCB_Customers";
        }
      }
      else if (TableName.equals("Globaldial_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable="Globaldial_Customers";
      }
      else if (TableName.equals("gsoEndCustomer"))
      {
	DB="gso";
	ListDataSource=P3;
	Name = "distinct (End_Customer_Name)";
	Value = "End_Customer_Name";
	Qualifier = " and SP_Id = " + QualifierValue + " ";
	OrderBy = "2";
	DBTable="Invoice_Daily";
      }
      else if (TableName.equals("AMEX_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable="AMEX_Customers";
      }
      else if (TableName.equals("LLU_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable="LLU_Customers";
      }
      else if (TableName.equals("AMEX_Staff_Number"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(User_Staff_Number)";
	Value = "User_Staff_Number";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "'Please enter a value into the Customer Name filter first'";
	OrderBy = "2";
	DBTable="CLI";
      }
      else if (TableName.equals("C2_Ref"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(C2_Ref_No)";
	Value = "C2_Ref_No";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "'Please enter a value into the Customer Name filter first'";
	OrderBy = "2";
	DBTable="LLU_C2Ref_List";
      }
      else if (TableName.equals("LLU_Speed"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(Speed + REPLACE(REPLACE(Standard_Ind,'Y',''),'N',' (Non-Standard)'))";
	Value = "Speed";
	Qualifier = " and Speed <> '"+QualifierValue+"'";
	Alert = "Please select a value";
	OrderBy = "2";
	DBTable="LLU_Speed";
      }
      else if (TableName.equals("Circuit_Speed"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(Speed)";
	Value = "Speed";
	Qualifier = "";
	OrderBy = "2";
	DBTable="LLU_Speed";
      }
      else if (TableName.equals("Service_Reference"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(Service_Reference)";
	Value = "Service_Reference";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
        if (extraQualifierValue.length() > 0)
          {
            Qualifier = Qualifier +
                          " and C2_Ref_No = '"+extraQualifierValue+"' ";
          }
	Alert = "'Please enter a value into the Customer Name filter first'";
	OrderBy = "2";
	DBTable="LLU_C2Ref_List";
      }
/*      else if (TableName.equals("Service_Reference"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct(Global_SRV_Reference)";
	Value = "Global_SRV_Reference";
        StringBuffer sb = new StringBuffer();
        sb.append(" and Global_Customer_Id = '"+QualifierValue+"' ");
        if (extraQualifierValue.length() > 0)
        {
          sb.append(" and C2_Ref_No = '"+extraQualifierValue+"' ");
        }
	sb.append("UNION SELECT distinct Global_SRV_Reference, Global_SRV_Reference FROM gcd..Single_Charge (nolock) " +
          "WHERE Global_SRV_Reference IS NOT NULL AND Global_Customer_Id = '"+QualifierValue+"' ");
        if (extraQualifierValue.length() > 0)
        {
          sb.append(" and C2_Ref_No = '"+extraQualifierValue+"' ");
        }
        sb.append("UNION SELECT distinct Global_SRV_Reference, Global_SRV_Reference FROM gcd..Single_Charge_Archive (nolock) " +
          "WHERE Global_SRV_Reference IS NOT NULL AND Global_Customer_Id = '"+QualifierValue+"' ");
        if (extraQualifierValue.length() > 0)
        {
          sb.append(" and C2_Ref_No = '"+extraQualifierValue+"' ");
        }
        Qualifier = sb.toString();
	Alert = "'Please enter a value into the Customer Name filter first'";
	OrderBy = "Global_SRV_Reference";
	DBTable="Charge";
      }*/
      else if (TableName.equals("Product_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id in (select distinct(Global_Customer_Id) from eban..Customer_Product (nolock) where Product_Group='"+QualifierValue+"')";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.equals("All_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.equals("Barclays_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable=TableName;
      }
      else if (TableName.equals("Inactive_Global_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and reference_ind = 'N'";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.equals("Active_Global_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id in (select distinct(Global_Customer_Id) from givn..Global_Customer_Application (nolock))";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.equals("Unused_Global_Customer"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and Global_Customer_Id not in (select distinct(Global_Customer_Id) from givn..Global_Customer_Application (nolock))";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.equals("Ad_Hoc_Customer"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Party_Name";
	Value = "Customer_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
      }
      else if (TableName.equals("Ad_Hoc_Customers"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "distinct Party_Name";
	Value = "Customer_Id";
	Qualifier = " and status in (select ban_status_code from eban..ban_list (nolock) where action = '" +
          QualifierValue + "') ";
	OrderBy = "Party_Name";
	DBTable="Invoice_List_View";
      }
      else if (TableName.equals("Ad_Hoc_Invoices"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Charge_Id";
	Value = "Charge_Id";
	Qualifier = " and status in (select ban_status_code from eban..ban_list (nolock) where action = '" +
          QualifierValue + "') ";
	OrderBy = "1";
	DBTable="Invoice_List_View";
      }
      else if (TableName.equals("Business_Unit"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "distinct Business_Unit";
	Value = "Business_Unit";
	Qualifier = "";
	OrderBy = "2";
	DBTable="Ad_Hoc_Customer";
      }
      else if (TableName.equals("Segment"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "distinct Segment";
	Value = "Segment";
	Qualifier = " and Business_Unit = '"+QualifierValue+"' ";;
	OrderBy = "2";
	DBTable="Ad_Hoc_Customer";
      }
      else if (TableName.equals("Ad_Hoc_Customer2"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Party_Name";
	Value = "Customer_Id";
	Qualifier = " and Segment = '"+QualifierValue+"' ";;
	OrderBy = "1";
	DBTable="Ad_Hoc_Customer";
      }
      else if (TableName.equals("Service"))
      {
	DB="gcd";
	ListDataSource="P4";
	Name = "Service_Reference";
	Value = "Service_Reference";
	Qualifier = " and Global_Customer_Division_Id = '"+QualifierValue+"'";
	DBTable = "Global_Customer_Billing";
	OrderBy = "1";
	Alert="'Please select a Division first'";
      }
      else if (TableName.equals("Barclays_Service"))
      {
	DB="Barclays";
	ListDataSource=P4;
	Name = "CMDB_Service_Reference";
	Value = "CMDB_Service_Reference";
	Qualifier = " and substring(CMDB_Service_Reference,1,3) = '"+QualifierValue+"'";
	DBTable = "Service_Item";
	OrderBy = "1";
	Alert="'Please select an Account first'";
      }
      else if (TableName.equals("Division"))
      {
	DB="givn";
	ListDataSource="P3";
	Name = "Global_Customer_Division_Id";
	Value = "Global_Customer_Division_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	DBTable = "Global_Customer_Division";
	OrderBy = "1";
	Alert="'Please select a Customer first'";
      }
      else if ((TableName.equals("Currency_Desc")) ||
        (TableName.equals("Charge_Currency")) ||
        (TableName.equals("Invoice_Currency")) ||
        (TableName.equals("Account_Invoice_Currency")))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "distinct Currency_Desc";
	Value = "ISO_Currency_Code";
	Qualifier =
          TableName.equals("Invoice_Currency")?" and ISO_Currency_Code = '"+QualifierValue+"'":"";
	OrderBy = "Currency_Desc";
	DBTable="Currency_Desc";
	Alert="";
      }
      else if ((TableName.equals("GCB_Site")) ||
	       (TableName.equals("GCB_From_End")) ||
	       (TableName.equals("GCB_To_End")))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Site_Id+ ' ' + isnull(Site_Name,'')";
	Value = "Site_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="GCB_Site";
	Alert="'Please select a Customer first'";
      }
      else if ((TableName.equals("Site")) ||
	       (TableName.equals("From_End")) ||
	       (TableName.equals("To_End")))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Site_Id+ ' ' + isnull(Site_Name,'')";
	Value = "Site_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable=TableName;
	Alert="'Please select a Customer first'";
      }
      else if (TableName.equals("C2_Ref_No"))
      {
	DB="gcd";
	ListDataSource=P3;
	Name = "distinct C2_Ref_No";
	Value = "C2_Ref_No";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' " +
          "UNION SELECT distinct C2_Ref_No, C2_Ref_No FROM gcd..Single_Charge (nolock) " +
          "WHERE C2_Ref_No IS NOT NULL AND Global_Customer_Id = '"+QualifierValue+"' " +
          "UNION SELECT distinct C2_Ref_No, C2_Ref_No FROM gcd..Single_Charge_Archive (nolock) " +
          "WHERE C2_Ref_No IS NOT NULL AND Global_Customer_Id = '"+QualifierValue+"' "
        ;
	OrderBy = "2";
	DBTable="Charge";
	Alert="'Please select a Customer first'";
      }
      else if (TableName.equals("gsoBillingPeriod"))
      {
	DB="givn";
	ListDataSource=P3;
	Name = "convert(varchar(11),Billing_Period_Start_Date,106) + '-' + " +
    "convert(varchar(11),Billing_Period_End_Date,106)";
	Value = "convert(varchar(8),Billing_Period_Start_Date,112)";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' " +
          "UNION SELECT convert(varchar(11),Billing_Period_Start_Date,106) + '-' + " +
          "convert(varchar(11),Billing_Period_End_Date,106), " +
          "convert(varchar(8),Billing_Period_Start_Date,112) " +
          "FROM givn..Billing_Period_Run_Control_Arc (nolock) " +
          "WHERE Billing_Period_Start_Date IS NOT NULL AND Global_Customer_Id = '"+QualifierValue+"' "
        ;
	OrderBy = "2 desc";
	DBTable="Billing_Period_Run_Control";
	Alert="";
      }
      else if (TableName.startsWith("tableSplitSite"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Site_Id+ ' ' + isnull(Site_Name,'')";
	Value = "Site_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="Site";
	Alert="'Please select a Customer first'";
      }
      else if (TableName.equals("Revenue_Reason"))
       {
      	DB="OSS";
	ListDataSource=P5;
	Name = "Revenue_Reason_Description";
	Value = "Revenue_Reason_Code";
	Qualifier = " and Charge_Type_Code = '"+QualifierValue+"'";
	OrderBy = "Revenue_Reason_Description";
	DBTable="Revenue_Reason";
	Alert="";
      }
      else if (TableName.equals("GCB_Revenue_Reason"))
       {
      	DB="gcd";
	ListDataSource=P4;
	Name = "distinct Revenue_Reason_Description";
	Value = "Revenue_Reason_Code";
	Qualifier = " and Charge_Type_Code = '"+QualifierValue+"'";
	OrderBy = "Revenue_Reason_Description";
	DBTable="Charge_Type";
	Alert="";
      }
       else if (TableName.equals("GCB_Revenue_Type"))
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Type_Description";
	Value = "Revenue_Type_Code";
	Qualifier = " and Charge_Type_Code = '"+QualifierValue+"'";
	OrderBy = "Revenue_Type_Description";
	DBTable="Charge_Type";
	Alert="'Please select a Charge Type first'";
      }
       else if (TableName.equals("Revenue_Type"))
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Type_Description";
	Value = "Revenue_Type_Code";
	Qualifier = " and Revenue_Type_Code <> '"+QualifierValue+"' ";
	OrderBy = "Revenue_Type_Code";
	DBTable=TableName;
	Alert="";
      }
       else if (TableName.equals("Revenue_Description"))
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Reason_Description";
	Value = "Revenue_Reason_Code";
	Qualifier = " and Revenue_Reason_Code <> '"+QualifierValue+"' ";
	OrderBy = "Revenue_Reason_Code";
	DBTable="Revenue_Reason";
	Alert="";
      }
       else if (TableName.equals("Revenue_Net_Or_Full"))
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Net_Or_Full";
	Value = "Revenue_Net_Or_Full_Code";
	Qualifier = " and Revenue_Net_Or_Full_Code <> '"+QualifierValue+"' ";
	OrderBy = "2";
	DBTable=TableName;
	Alert="";
      }
       else if (TableName.equals("Revenue_Root_Cause"))
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Root_Cause";
	Value = "Revenue_Root_Cause_Code";
	Qualifier = " and Revenue_Root_Cause_Code <> '"+QualifierValue+"' ";
	OrderBy = "2";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Charge_Frequency"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Frequency";
	Value = "Frequency";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Ban_Status"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Status";
	Value = "Status";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Ad_Hoc_Ban_Status"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Ban_Status_Code";
	Value = "Ban_Status_Code";
	Qualifier = " AND Action = '"+QualifierValue+"' ";
	OrderBy = "1";
	DBTable="Ban_List";
	Alert="";
      }
      else if ((TableName.equals("BAN_Type"))
        || (TableName.equals("Nostro_BAN_Type")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "BAN_Type";
	Value = "BAN_Type";
	Qualifier = (TableName.equals("Nostro_BAN_Type")?" AND BAN_Type like '"+QualifierValue+"' ":"");
	OrderBy = "2";
	DBTable="BAN_Type";
	Alert="";
      }
      else if (TableName.equals("New_BAN_Type"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " AND Datatype = 'BAN Type' And Qualifier = '"+QualifierValue+"' ";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
	Alert="";
      }
      else if (TableName.equals("Customer_Contact_Point"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " AND Datatype = 'Customer Contact' And Qualifier = '" +
          QualifierValue + "' And Qualifier2 = '" + extraQualifierValue + "' ";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
	Alert = "'Please select Company Address Id first'";
      }
      else if (TableName.startsWith("conglomDataItem"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "LD_Description";
	Value = "LD_Description + '|' + LD_Ref_Type";
	Qualifier = " AND Conglom_Cust_Id = " + QualifierValue + " " +
          "And Billed_Product_Id = '" + extraQualifierValue + "' " +
          "And Report_Type = '" + extraQualifierValue2 + "' ";
	OrderBy = "1";
	DBTable="Cust_Local_Data_Ref_Item";
	Alert = "";
      }
      else if (TableName.equals("Ad_Hoc_Type"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = TableName;
	Value = TableName;
	Qualifier = "";
	OrderBy = "2";
	DBTable=TableName;
	Alert="";
      }
      else if ((TableName.equals("List_Month")) ||
        (TableName.equals("List_Month2"))  ||
        (TableName.equals("Visit_Month"))  ||
        (TableName.equals("Invoice_Month")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Name";
	Value = "Name";
	Qualifier = "";
	OrderBy = "null";
	DBTable="List_Month";
	Alert="";
      }
      else if (TableName.equals("Service_Type"))
      {
	DB="gcd";
	ListDataSource=P5;
	Name = "Code";
	Value = "Service_Type_Ref";
	Qualifier = "";
	OrderBy = "null";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Product_Category"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Description";
	Value = "Product_Type";
	Qualifier = "";
	OrderBy = "2";
	DBTable="Product_Type_Description";
	Alert="";
      }
      else if (TableName.equals("Product_Type"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Description";
	Value = "Tariff_Code";
	Qualifier = "and Product_Category = '"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="Product_Type";
	Alert="'Please select a Product Category first'";
      }
      /*else if (TableName.equals("GCB_Product_Type"))
      {
	DB="gcd";
	ListDataSource=P4;
	Name = "distinct Product_Type";
	Value = "Product_Type";
	Qualifier = "and Product_Type not in (select convert(varchar,Product_Type) " +
          "from gcd..PNN3_Product_Type) ";
	OrderBy = "2";
	DBTable="Global_Customer_Billing";
	Alert="";
      }*/
      else if (TableName.equals("GCB_Product_Type"))
      {
	DB="eban";
	ListDataSource=P4;
	Name = "distinct Text";
	Value = "Value";
	Qualifier = " and datatype = 'Product GSR Code'  ";
	OrderBy = "2";
	DBTable="Ban_Reference_Data";
	Alert="";
      }
      else if (TableName.equals("GCB_Product_Type"))
      {
	DB="eban";
	ListDataSource=P4;
	Name = "distinct Text";
	Value = "Value";
	Qualifier = " and datatype = 'Product GSR Code'  ";
	OrderBy = "2";
	DBTable="Ban_Reference_Data";
	Alert="";
      }
      else if (TableName.equals("invoiceTypeCode"))
      {
	DB="eban";
	ListDataSource=P4;
	Name = "distinct Text";
	Value = "Value";
	Qualifier = " and datatype = 'Conglom Invoice Type Code'  ";
	OrderBy = "2";
	DBTable="Ban_Reference_Data";
	Alert="";
      }
      else if (TableName.equals("conglomReportType"))
      {
	DB="eban";
	ListDataSource=P4;
	Name = "Text";
	Value = "Value";
	Qualifier = " and datatype = 'Conglom Report Type' ";
	OrderBy = "Display_Order";
	DBTable="Ban_Reference_Data";
	Alert="";
      }
      else if (TableName.startsWith("conglomSort"))
      {
	DB="eban";
	ListDataSource=P4;
	Name = "Text";
	Value = "Value";
	Qualifier = " and datatype = 'Conglom Report Sort Order' ";
	OrderBy = "Display_Order";
	DBTable="Ban_Reference_Data";
	Alert="";
      }
      else if (TableName.equals("Carrier"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Carrier_Name";
	Value = "Carrier_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Carrier";
	Alert="";
      }
      else if (TableName.equals("Invoice_Option"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Invoice_Option_Code";
	Value = "Invoice_Option_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Bill_Option"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Bill_Option_Code";
	Value = "Bill_Option_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if ((TableName.equals("FromEnd_Country")) ||
	      (TableName.equals("ToEnd_Country")) ||
	      (TableName.equals("Bill_Address_Country")) ||
	      (TableName.equals("Country")) ||
	      (TableName.equals("Carrier_Country")))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Country";
	Value = "Country";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Country_Zone";
	Alert="";
      }
      else if ((TableName.equals("Site_Country")) ||
        (TableName.equals("Customer_Billing_Country")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Country";
	Value = "ISO_2";
	Qualifier = " and System = '"+QualifierValue+"' ";
	OrderBy = "1";
	DBTable="Country_Currency";
	Alert="";
      }
      /*else if (TableName.equals("Customer_Billing_Country"))
      {
	DB="GIVN_REF";
	ListDataSource=P3;
	Name = "Country";
	Value = "Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Country_Codes";
	Alert="";
      }*/
      else if (TableName.equals("Billing_Frequency"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Billing_Frequency";
	//Value = "Billing_Frequency";
	Value = "Period";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("conglomBillingFrequency"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "Description";
	//Value = "Billing_Frequency";
	Value = "Frequency_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Frequency_Type";
	Alert="";
      }
      /*else if (TableName.equals("Charge_Description_Code"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "distinct(vdp.Charge_Description_Code)";
	Value = "vdp.Charge_Description_Code";
	Qualifier = "";
	/*Qualifier = " and vgp.Product_Type = vdp.Product_Type and " +
          "vgp.Product_GSR_Code = '" + QualifierValue + "' and " +
          "vdp.Charge_Description_Code <> 'TBA' ";*/
	/*OrderBy = "2";
	DBTable="Vertex_Data_Product vdp ";
	//DBTable="Vertex_Data_Product vdp (nolock), givn_ref..Vertex_GSR_Product vgp";
	Alert="";
      }*/
      else if (TableName.equals("Charge_Description_Code"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "distinct(vj.Charge_Description_Code)";
	Value = "vj.Charge_Description_Code";
	Qualifier = " and vcc.CDC_Category = vj.CDC_Category and " +
          "vcc.Product_GSR_Code = substring('" + QualifierValue + "',7,4) ";
	OrderBy = "2";
	DBTable="Vertex_Jurisdiction vj (nolock), givn_ref..Vertex_CDC_Category vcc ";
	Alert="";
      }
      else if (TableName.equals("MANS_Customer"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Circuit_BAN"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "CW_Circuit_Ref";
	Value = "CW_Circuit_Ref";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("BAN_Circuits"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "CW_Circuit_Ref";
	Value = "CW_Circuit_Ref";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("BAN_Services"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Service_Reference";
	Value = "Service_Reference";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("BAN_Accounts"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Account_Id";
	Value = "Account_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("Nostro_BAN_Users"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "User_Id";
	Value = "User_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Nostro_User_BAN";
	Alert="";
      }
      else if (TableName.equals("Nostro_BAN_Payment_Groups"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Payment_Group_Name";
	Value = "Payment_Group_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Nostro_Payment_Group_BAN";
	Alert="";
      }
      else if (TableName.equals("Nostro_BAN_Accounts"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Account_Name";
	Value = "Account_Name";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("BAN_Invoice_Regions"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Invoice_Region";
	Value = "Account_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("GCB_BAN_Invoice_Regions"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Invoice_Region";
	Value = "Invoice_Region";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Invoice_Region_BAN";
	Alert="";
      }
      else if ((TableName.equals("BAN_Customers")) || (TableName.equals("GCB_BAN_Customers"))
        || (TableName.equals("Nostro_BAN_Customers")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Customer";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
/*      else if (TableName.equals("Circuit"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "CW_Circuit_Ref";
	Value = "CW_Circuit_Ref";
	Qualifier += " and (Account_Id = '"+QualifierValue+"'  or '"+QualifierValue+"' = 'All') and Circuit_Status <> 'X'";
	OrderBy = "1";
	DBTable="Circuit";
	Alert="";
      }*/
      else if (TableName.equals("Circuit_Reference"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "Service_Reference";
	Value = "Service_Reference";
	Qualifier = " and Service_Reference is not null and " +
          "(Global_Customer_Id = '"+QualifierValue+"') and " +
          extraQualifierValue +
          "(billing_cessation_date is null or billing_cessation_date > getdate())";
	OrderBy = "1";
	DBTable="Global_Customer_Billing";
	Alert="'Please select a Division or a C2 Ref No first'";
      }
      else if (TableName.equals("Circuit"))
      {
	DB="GCD";
	ListDataSource=P5;
	Name = "distinct CASE WHEN gcb.Billing_Cessation_Date is not null " +
          "THEN gcb.Service_Reference + ' (' + isnull(convert(varchar(11),gcb.billing_cessation_date,106),'Live') + ')' " +
          "ELSE gcb.Service_reference END ";
	Value = "gcb.Service_Reference";
        if (extraQualifierValue.indexOf("C2_Ref_No") > 0)
        {
          Qualifier = " and gcb.Service_Reference is not null and " +
            extraQualifierValue +
            "(gcb.Global_Customer_Id = '"+QualifierValue+"') " +
            "and gcb.Service_Reference = c.Global_Srv_Reference ";
          DBTable="Global_Customer_Billing gcb (nolock), GCD..Charge_C2Ref c ";
        }
        else
        {
          Qualifier = " and gcb.Service_Reference is not null and " +
            extraQualifierValue +
            "(gcb.Global_Customer_Id = '"+QualifierValue+"') ";
          DBTable="Global_Customer_Billing gcb";
        }
	OrderBy = "2";
	Alert="'Please select a Division or a C2 Ref No first'";
      }
/*
Select case
When billing_cessation_date is not null
then Service_Reference + ' (' + isnull(convert(varchar(11),billing_cessation_date),'Live') + ')'
else Service_reference
End ,Service_Reference
from GCD..Global_Customer_Billing (nolock)
 where Service_Reference is not null  and Service_Reference is not null and (Global_Customer_Id = '@GCID')
order by Service_Reference
*/
      else if (TableName.equals("GCB_Charge_Type"))
      {
	DB="GCD";
	ListDataSource=P4;
	Name = "distinct Charge_Type_Description";
	Value = "Charge_Type_Code";
	Qualifier = "and Charge_Category_Code='"+QualifierValue+"'";
	OrderBy = "Charge_Type_Description";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("OSS_Charge_Type"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Charge_Description";
	Value = "Charge_Type_Code";
	Qualifier = "and Charge_Category_Code='"+QualifierValue+"'";
	OrderBy = "1";
	DBTable="Charge_Type";
	Alert="";
      }
      else if (TableName.equals("Charge_Entity"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Charge_Entity_Code";
	Value = "Charge_Entity_Code";
	Qualifier = "";// and Charge_Entity_Code <> case when '"+QualifierValue+"' <> 'OSSC' then 'Carrier' else '' end";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.equals("VAT_Code"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "VAT_Description";
	Value = "VAT_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable="VAT_Codes";
	Alert="";
      }
      else if (TableName.equals("Circuit_Status"))
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Circuit_Status_Description";
	Value = "Circuit_Status";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Circuit_Status";
	Alert="";
      }
      else if (TableName.equals("Tax_Precision"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Sequence_No";
	Value = "Sequence_No";
	Qualifier = "";
	OrderBy = "1";
	DBTable=TableName;
	Alert="";
      }
      else if ((TableName.equals("Split_Billing")) ||
	      (TableName.equals("Standard_Bill_Indicator")) ||
	      (TableName.equals("Primary_Account")) ||
	      (TableName.equals("Multiple_Billing_Regions")) ||
	      (TableName.equals("Discounts_Applicable")) ||
	      (TableName.equals("Auto_Close")) ||
	      (TableName.equals("Reopen_Bill")) ||
	      (TableName.equals("Multiple_GCDs")) ||
	      (TableName.equals("Strategic_Reports")) ||
	      (TableName.equals("zeroBal")) ||
	      (TableName.equals("vatExempt")) ||
	      (TableName.equals("conMPR")) ||
	      (TableName.equals("softBillReq")) ||
	      (TableName.equals("bankDetailsReturned")) ||
	      (TableName.equals("vatExemptInd")) ||
	      (TableName.equals("selfBillAgreement")) ||
	      (TableName.equals("customerContract")) ||
	      (TableName.equals("autoPrintInd")) ||
	      (TableName.equals("presentationInd")) ||
	      (TableName.equals("deletedInd")) ||
	      (TableName.equals("invoiceInd")) ||
	      (TableName.equals("rerateRQR09")) ||
	      (TableName.equals("sapUpload")) ||
	      (TableName.equals("reportRequired")) ||
	      (TableName.equals("VAT_Indicator")))
      {
	DBTable="YN_Ind";
	Name = "";
	Value = "";
	Qualifier = "";
	Alert="";
	DB="eBAN";
	ListDataSource=P5;
      }
      else if (TableName.equals("Tax_Type"))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Tax_Type";
	Value = "Tax_Type";
	Qualifier = " and Global_Customer_Id = '" + SU.Extract(QualifierValue,1)+ "'";
	Qualifier +=" and Site_Id = '" + SU.Extract(QualifierValue,2)+"'";
	OrderBy = "1";
	DBTable="Tax_Type_View";
	Alert="'Please select a Site first'";
      }
      else if (TableName.equals("Split_Sites"))
      {
	DB="eban";
	ListDataSource=P5;
	Name = "Sequence_No";
	Value = "Sequence_No";
	Qualifier = " and Sequence_No between 0 and " + QualifierValue;
	OrderBy = "1";
	DBTable="Sequence";
	Alert="";
      }
      else if (TableName.equals("Company_Address_Id"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("BAN_Tax_Requirement"))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "CASE ISNULL(Country_Code, '00') when '00' then Tax_Type + " +
          "'(' + CONVERT(varchar(10),CAST(Tax_Rate*100 AS DECIMAL(10,2))) + '%)' " +
          "else Tax_Type + '(' + country_code + ' - ' + " +
          "CONVERT(varchar(10),CAST(Tax_Rate*100 AS DECIMAL(10,2))) + '%)'  END " +
          " + CASE ISNULL(upper(Restricted), 'N') when 'Y' then '(r)' else '' END ";
	Value = "Tax_Type + '-' + CONVERT(varchar(10),Tax_Rate) + '-' + " +
          "SAP_Tax_Code + '-' + ISNULL(Country_Code, '00') ";
	Qualifier = " and Company_Address_Id = '"+QualifierValue+"' ";
	Alert = "'Please select Company Address Id first'";
	OrderBy = "Tax_Rate DESC";
	DBTable=TableName;
      }
      else if (TableName.equals("Current_BA"))
      {
	DB="givn";
	ListDataSource=P5;
	Name = "eu.user_name + '(' + ca.logon_id + ')' ";
	Value = "ca.logon_id ";
	Qualifier = " and ca.global_customer_id = '"+QualifierValue+"' " +
          "and ca.logon_id = eu.login_id and eu.system = 'GCB' " +
          "and (eu.login_id like 'w%' or eu.login_id like 'y%') " +
          "and eu.login_id not like 'y12850%' " +
          "and eu.login_id not like '%c' ";
	Alert = "";
	OrderBy = "eu.user_name";
	DBTable="customer_access ca (nolock), eban..eban_user eu ";
      }
      else if (TableName.equals("New_BA"))
      {
	DB="eban";
	ListDataSource=P5;
	Name = " distinct eu.user_name + ' (' + eu.login_id + ')' ";
	Value = "eu.login_id ";
	Qualifier = " and eu.system = 'GCB' " +
          "and (eu.login_id like 'w%' or eu.login_id like 'y%') " +
          "and eu.login_id not like 'y12850%' " +
          "and not eu.login_id like '%c' " +
          "and eu.login_id in (select logon_id from givn_ref..logon " +
          "where billing_team in ('global','interconnect','Ex Thus')) ";
	Alert = "";
	OrderBy = "1";
	DBTable="eban_user eu ";
      }
      else if (TableName.equals("gsoJobId"))
      {
	DB="gso";
	ListDataSource=P5;
	Name = "distinct Job_Id";
	Value = "Job_Id";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "2";
	DBTable="monthly_billing_log";
      }
      else if (TableName.equals("gsoMessageDate"))
      {
	DB="gso";
	ListDataSource=P5;
	Name = "distinct CONVERT(char(11),message_date,106)";
	Value = "CONVERT(char(8),message_date,112)";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "2";
	DBTable="monthly_billing_log";
      }
      /*else if (TableName.equals("Act_As_Logon"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "l.User_Firstname + ' ' + l.User_Surname";
	Value = "l.Logon_Id";
	Qualifier = " and vcc.CDC_Category = vj.CDC_Category and " +
          "vcc.Product_GSR_Code = substring('" + QualifierValue + "',7,4) ";
	OrderBy = "1";
	DBTable="Logon lg (nolock), givn_ref..Logon l ";
	Alert="";
      }*/
      else if (TableName.equals("Act_As_Logon"))
      {
	DB="givn_ref";
	ListDataSource=P5;
	Name = "User_Firstname + ' ' + User_Surname";
	Value = "Logon_Id";
	Qualifier = QualifierValue.equals("none")?
          (" and Logon_Id = '" + extraQualifierValue + "' "):
          (" and Logon_Group = '" + QualifierValue + "' ");
	OrderBy = "1";
	DBTable="Logon";
	Alert="";
      }
      else if (TableName.equals("conglomDOB"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "Description";
	Value = "DOB_Cycle_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable="DOB_Cycle";
	Alert="";
      }
      else if (TableName.startsWith("conglomSubLevel"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "Sub_Total_Level_Desc";
	Value = "Sub_Total_Level";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Cust_Report_Params_Desc";
	Alert="";
      }
      else if (TableName.startsWith("conglomDiscountType"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "Short_Description";
	Value = "Discount_Item_Code";
	Qualifier = " and  Billed_Product_Id = '" + QualifierValue + "' ";
	OrderBy = "1";
	DBTable="Conglom_Discount_Item";
	Alert="'Please select Billed Product first'";
      }
      else if (TableName.startsWith("conglomPeriod"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct Period";
	Value = "Period";
	Qualifier = " and Conglom_Cust_Id = " + QualifierValue + " "/* +
          "and Last_Update_Id = '"  + extraQualifierValue + "' "*/;
	OrderBy = "2";
	DBTable="Customer_Period";
	Alert="";
      }
      else if (TableName.startsWith("conglomItemStatus"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct Status";
	Value = "Status";
	Qualifier = " and Status <> 'TRIAL' ";
	OrderBy = "2";
	DBTable="Billing_Items";
	Alert="";
      }
      else if (TableName.startsWith("conglomItem"))
      {
	DB="conglomerate";
	ListDataSource=P5;
	Name = "distinct case " +
          "when Status = 'OPEN' then 'Items included in the bill' " +
          "when Status = 'TRIAL' then 'Items included in the bill' " +
          "when Status = 'DUPLICATE' then 'Items excluded from the bill' " +
          "when Status = 'REMOVED' then 'Items excluded from the bill' " +
          "when Status = 'SUSPENDED' then 'Items in suspension' " +
          "end";
	Value = "case " +
          "when Status = 'OPEN' then 'OPEN' " +
          "when Status = 'TRIAL' then 'OPEN' " +
          "when Status = 'DUPLICATE' then 'DUP' " +
          "when Status = 'REMOVED' then 'DUP' " +
          "when Status = 'SUSPENDED' then 'SUS' " +
          "end";
	Qualifier = " and Conglom_Cust_Id = " + QualifierValue + " "/* +
          "and Last_Update_Id = '"  + extraQualifierValue + "' "*/;
	OrderBy = "1";
	DBTable="Billing_Items";
	Alert="";
      }
      else if (TableName.equals("tReferenceTypes"))
      {
	DB="n/a";
	ListDataSource=GOLDFISH;
	Name = "Reference_Type";
	Value = "Description";
	Qualifier = "";
	OrderBy = "1";
	DBTable="T_Reference_Types";
	Alert="";
      }
      else if ((TableName.equals("rsProvider")) || (TableName.equals("providerId")) ||
        (TableName.equals("filterProvider")) || (TableName.equals("providerName")) ||
        (TableName.equals("providerName2")))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Provider_Name + ' (' + convert(varchar,Provider_Id) + ')'";
	Value = TableName.startsWith("providerName")?"Provider_Name":"Provider_Id";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Provider";
	Alert="";
      }
      else if ((TableName.equals("rsMasterAccount")) || (TableName.equals("masterAccountNumber")) ||
        (TableName.equals("masterAccountId")))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Master_Account_Name + ' ' + Master_Account_Number + ' (' + " +
          "convert(varchar,Master_Account_Id) + ')'";
	Value = TableName.equals("masterAccountNumber")?"Master_Account_Number":"Master_Account_Id";
	Qualifier = " and (Provider_Id = " + QualifierValue +
          //" or " + QualifierValue + " = -1" +
          ") and Deleted_Ind = 'N' ";
	OrderBy = "1";
	DBTable="Master_Account";
	Alert="'Please select a Provider first'";
      }
      /*else if (TableName.equals("masterAccountChannel"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Channel + ' - ' + Channel_Description";
	Value = "Channel";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Channel";
	Alert="";
      }*/
      else if (TableName.equals("rsAccount"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Account_Name + ' ' + Account_Number + ' (' + " +
          "convert(varchar,Account_Id) + ')'";
	Value = "Account_Id";
	Qualifier = " and Master_Account_Id = " + QualifierValue + " and Deleted_Ind = 'N' ";
	OrderBy = "1";
	DBTable="Account";
	Alert="'Please select a Master Account first'";
      }
      else if (TableName.equals("accountNumber"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "a.Account_Name + ' ' + a.Account_Number + ' (' + " +
          "convert(varchar,a.Account_Id) + ')'";
	Value = "a.Account_Number";
	Qualifier = " and m.Master_Account_Number = '" + QualifierValue + "' " +
          " and a.Deleted_Ind = 'N' and m.Master_Account_Id = a.Master_Account_Id ";
	OrderBy = "1";
	DBTable="Account a (nolock), RevShare..Master_Account m ";
	Alert="'Please select a Master Account first'";
      }
      else if ((TableName.equals("rsProductGroup")) || (TableName.equals("productGroup")))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "distinct Product_Group";
	Value = "Product_Group";
	Qualifier = "";
	OrderBy = "2";
	//DBTable="Master_Account_Product_Group";
	DBTable="Product_Group";
	Alert="";
      }
      else if ((TableName.equals("rsProduct")) || (TableName.equals("productCode")))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Product_Code + ' - ' + Product_Description";
	Value = "Product_Code";
	Qualifier = " and (Product_Group = '" + QualifierValue + "' or " +
    "'" + QualifierValue + "' = 'ALL') ";
	OrderBy = "1";
	DBTable="Product";
	Alert="'Please select a Product Group first'";
      }
      else if (TableName.equals("rsRatingScheme"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "distinct From_Call_Month + ' ' + isnull(Sign_Name, Master_Account_Number) + ' ' + Product_Code + ' ' + Rating_Type + (case when To_Call_Month is null then ' ' else ' (to ' + To_Call_Month + ')' end)";
	Value = "From_Call_Month + '|' + isnull(To_Call_Month,'open') + '|' + isnull(Sign_Name, Master_Account_Number) + '|' + Product_Code + '|' + Rating_Type";
	Qualifier = "";
	OrderBy = "2";
	DBTable="Rating_Scheme";
	Alert="";
      }
      else if (TableName.equals("frequencyCode"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Frequency_Code + ' - ' + Description";
	Value = "Frequency_Code";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Billing_Frequency_Code";
	Alert="";
      }
      else if (TableName.equals("signName"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Sign_Name";
	Value = "Sign_Name";
	Qualifier = "";
	OrderBy = "1";
	DBTable="Zodiac";
	Alert="";
      }
      else if ((TableName.equals("ratingType")) ||
        (TableName.equals("ratingDuration")) ||
        (TableName.equals("managedMasterAccount")))
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Text";
	Value = "Value";
	Qualifier = " and Datatype = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "Display_Order";
	DBTable="BAN_Reference_Data";
      }
      else if (TableName.equals("rsReport"))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Report_Name";
	Value = "Report_Code";
	Qualifier = " and Report_Type = '"+QualifierValue+"' ";
	Alert = "";
	OrderBy = "1";
	DBTable="Report_Download";
      }
      else if ((TableName.equals("fromCallMonth")) ||
        (TableName.equals("toCallMonth")) ||
        (TableName.equals("filterMonth")))
      {
	DB="RevShare";
	ListDataSource=P5;
	Name = "Call_Month";
	Value = "Call_Month";
	Qualifier = " and Call_Month > '"+QualifierValue+"' ";
	OrderBy = "1 desc";
	DBTable="Call_Month_View";
	Alert="";
      }
  }
  public String getDataSource(String DataSourceSQL)
  {
   return P5;
  }
  public  String[] getKey()
  {
	  return Key;
  }
  public  String getHeader()
  {
	  return Header;
  }
  public  String getGrid()
  {
	  return Grid;
  }
  public  void setUser_Id(String newUser_Id)
  {
    User_Id = newUser_Id;
  }
  public  String getUser_Id()
  {
   return User_Id;
  }
  public  java.sql.ResultSet getResultsSet ()
  {
    return RS;
  }
/*****************************************************************************/
  public String getGlobalCustomerName(String gcId)
  {
	SQL="select Global_Customer_Name from givn..Global_Customer " +
          "where Global_Customer_Id = '"+ gcId + "'";

	String gcName = "";
	if (Connect(READ,P3))
	{
	  try
	  {
	    if (RS.next())
	    {
	      gcName = RS.getString(1);
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Close();
	  }
	}
	return gcName;
  }
/*****************************************************************************/
  public boolean globalCustomerNameExists(String gcName)
  {
	SQL="select Global_Customer_Name from givn..Global_Customer " +
          "where ltrim(rtrim(upper(Global_Customer_Name))) = '"+ gcName.toUpperCase().trim() + "'";

	boolean exists = false;
	if (Connect(READ,P3))
	{
	  try
	  {
	    exists = RS.next();
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Close();
	  }
	}
	return exists;
  }
/*****************************************************************************/
  public String getDivisionName(String divisionId)
  {
	SQL="select Global_Customer_Division_Id + " +
          "'(' + Global_Srv_Reference_Prefix + ')' " +
          "from givn..Global_Customer_Division (nolock) " +
          "where Global_Customer_Division_Id = '"+ divisionId + "'";

	String divisionName = "";
	if (Connect(READ,P3))
	{
	  try
	  {
	    if (RS.next())
	    {
	      divisionName = RS.getString(1);
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Close();
	  }
	}
	return divisionName;
  }
/*****************************************************************************/
  public  Enumeration getUser(String System,String UserId,String Password)
  {
	if (System.startsWith("Query"))
        {
          SQL="SELECT Group_Name = 'Account Business', User_Name FROM Query..Query_User "+
            "WHERE User_Name = '"+UserId+"' and Password = '"+Password+"'";
        }
        else
        {
          SQL="select Group_Name,User_Name from eBan..eBan_User (nolock) where Login_Id = '"+UserId;
          SQL=SQL+"' and Password = '"+Password + "' and System = '"+System+"'";
        }

	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close();
	  }
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
	return Result.elements();
  }
/*****************************************************************************/
  public  Enumeration getGivnRefUser(String UserId,String Password)
  {
        SQL = "select Billing_Team,User_Firstname + ' ' + User_Surname, " +
          "isnull(Logon_Group, 'none') " +
          "from givn_ref..logon (nolock) " +
          "where Logon_Id = '"+UserId + "' " +
          "and Logon_Password = '"+Password + "'";
	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close();
	  }
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
	return Result.elements();
  }
/*****************************************************************************/
  public  Enumeration getGivnRefUserSecure(String UserId,String Password)
  {
        SQL = "select Billing_Team,User_Firstname + ' ' + User_Surname, " +
          "isnull(Logon_Group, 'none'), " +
          "case when date_last_changed is null then 'a' when datediff(day, date_last_changed, getdate()) > 90 then 'b' else 'c' end " +
          "from givn_ref..logon l (nolock), " +
          "eban..eban_password p (nolock) " +
          "where l.Logon_Id = '"+ UserId.trim() + "' " +
          "and l.Logon_Id = p.Login_Id " +
          "and p.Current_Password = '"+ MD5Helper.createMD5(Password.trim()) + "'";
          //"and p.Current_Password = '"+ Password.trim() + "'";
          //"and Logon_Password = '"+ MD5Helper.createMD5(Password.trim()) + "'";

	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	      Result.addElement(RS.getString(4));
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close();
	  }
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
	return Result.elements();
  }
/*****************************************************************************/
  public  Enumeration getGivnRefUser(String UserId)
  {
        SQL = "select Billing_Team,User_Firstname + ' ' + User_Surname, " +
          "isnull(Logon_Group, 'none') " +
          "from givn_ref..logon (nolock) " +
          "where Logon_Id = '"+UserId + "' " ;

	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close();
	  }
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
	return Result.elements();
  }
/*****************************************************************************/
  public  Enumeration getDOBInfo(String billingTeam)
  {
	Vector Result = new Vector();
        if ((billingTeam.equals("Global")) || (billingTeam.equals("Conglom")))
        {
          if (billingTeam.equals("Global"))
          {
            SQL = "select max(dob_code), convert(char(11),SAP_Close_Date,106) + ' 17:00' SAP_Close_Date " +
              "from givn_ref..dob_billing_cycle (nolock) " +
              "where billing_team = '" + billingTeam + "' " +
              "and date_of_billing = " +
              "(select max(date_of_billing) from givn_ref..dob_billing_cycle " +
              "(nolock) where billing_team = '" + billingTeam + "' " +
              "and date_of_billing < getdate()) " +
              "group by SAP_Close_Date ";
          }
          else //if (billingTeam.equals("Conglom"))
          {
            SQL = "select max(dob_cycle_id), 'N/A' SAP_Close_Date " +
              "from conglomerate..conglom_billing_schedule (nolock) " +
              "where extract_run_date = " +
              "(select max(extract_run_date) from conglomerate..conglom_billing_schedule " +
              "(nolock) where extract_run_date < getdate()) ";
          }
          if (Connect(READ,P5))
          {
            try
            {
              boolean found = false;
              while (RS.next())
              {
                Result.addElement(RS.getString(1));
                Result.addElement(RS.getString(2));
                found = true;
              }
              if (!found)
              {
                Result.addElement("Not Yet Available");
                Result.addElement("Not Yet Available");
              }
              Close();
            }
            catch(java.sql.SQLException se)
            {
              Result.addElement(se.getMessage());
              Close();
            }
            if (!Result.isEmpty())
            {
              Message = "";
            }
            else
            {
              Message = "<font color=red><b>DOB data not found</font>";
            }
          }
          else
          {
            //Connection failed
            Result.addElement("Not Yet Available");
            Result.addElement("Not Yet Available");
            Message = "<font color=red><b>"+Message;
          }
        }
        else
        {
          Result.addElement("N/A");
          Result.addElement("N/A");
        }
	return Result.elements();
  }
/*****************************************************************************/
  public  Enumeration getUser(String System,String UserId)
  {
	SQL = "select User_Name, Password, Group_Name from eBan..eBan_User (nolock) where Login_Id = '" +
        UserId + "' and System = '" + System + "'";

	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    if (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close();
	  }
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
	return Result.elements();
  }
  /*****************************************************************************/
  public Enumeration getResults(String QuerySQL,String DataSource)
  //**DataSource Changes  public  Enumeration getResults(String QuerySQL)
  {//Two columns, one row
    SQL=QuerySQL;

    int i,cols=countColumns();

    Vector Row = new Vector();

	if (Connect(READ,DataSource))
	//DataSource Changes if (Connect(READ))
	{
	  try
	  {
	    if (RS.next())
	    {
	      for (i=1;i<=cols;i++)
	      {
		Row.addElement(RS.getString(i));
	      }
	    }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Row.addElement(se.getMessage());
	    Close();
	  }

	  if (!Row.isEmpty())
	  {
	    Message = "";
	  }
	  else
	  {
	    //Result.addElement("No Data Found for " + SQL);
	    Message = "No Data Found for " + SQL;
	  }
	}
	else
	{
	  //Connection failed
	  Row.addElement("<font color=red<b>"+Message);
	  //pad out so that resultset has the expected number of entries
	  for (i=2;i<=cols;i++)
	  {
	    Row.addElement("");
	  }
	}
	return Row.elements();
  }
  /*****************************************************************************/
  public String getValue(String QuerySQL,String DataSource)
  //Datasource changes public String getValue(String QuerySQL)
  {//single value always a string

    SQL=QuerySQL;
    String value="";
	if (Connect(READ,DataSource))
	//Datasource changes if (Connect(READ))
	{
	  try
	  {
	    if (RS.next())
	    {
	      value=RS.getString(1);
	    }
	    else
	    {
	      value= "<font color=red<b>No Data Found for " + SQL;
	    }
	    Close();
	    return value;
	  }
	  catch(java.sql.SQLException se)
	  {
	    Close();
	    return "<font color=red<b>"+se.getMessage();}
	}
	else
	{
	  //Connection failed
          Close();
	  return "<font color=red<b>"+Message;
	}
  }
  /*****************************************************************************/
  private void setHeader(int Border,boolean Select)
  {
	String Head;
	char cHead[];
	char delimiter=',';
	int pos;
	String Name;

	Header="";
	Name="";
	Columns=1;

	if (SQL.toLowerCase().startsWith("select "))
	{
		Head=SQL.substring(7);
		cHead=Head.toCharArray();
		pos=0;
		Header="<table border=0><tr>";
		while ((pos < Head.length())&& (Head.substring(pos,pos+6).equals(" from ") == false))
		{
			Header=Header+"<td width=160><b><small>";
			//Iterate through the sql looking for column names or aliases
			//If an alias exists, this is used in place of a column name
			while ((cHead[pos] != delimiter) && (Head.substring(pos,pos+6).equals(" from ") == false) )
			{//Check for column alias (denoted by space in name)
				if (cHead[pos] == ' ')
				{//alias found, so start again
					Name="";
				}
				else if (cHead[pos] == '.')
				{//hierarchical name, so start again
					Name="";
				}
				else if ((cHead[pos] != '_') && (cHead[pos] != '"'))
				{//copy next character
					Name=Name+Head.substring(pos,pos+1);
				}
				else
				{//replace underscore with a space
					Name=Name+" ";
				}

				pos++;
			}
			Header=Header+Name+"</td>";
			Name="";
			if (Head.substring(pos,pos+6).equals(" from ") == false)
			{
				Columns++;
				pos++;
			}
			else
			{
				//save the postion of the 'from' column for later use
				FromPos=pos;
			}
		}
		if (Select)
		{//A Radio button selection is to be added, so include a header
			Header=Header+"<td width=100><b><small>Select?</td></tr></table>";
		}
		else
		{
			Header=Header+"<tr></table>";
		}
	}
}
/************************************************************/
  private int countColumns()
  {
	String Head;
	char delimiter=',';
	char cHead[];
	int pos;
	Columns=1;

	if (SQL.toLowerCase().startsWith("select "))
	{
		Head=SQL.substring(7);
		cHead=Head.toCharArray();
		pos=0;
		while ((pos < Head.length())&& (Head.substring(pos,pos+6).compareTo(" from ") != 0
		))
		{
			//Iterate through the sql looking for column names or aliases
			//If an alias exists, this is used in place of a column name
			while ((cHead[pos] != delimiter) && (Head.substring(pos,pos+6).compareTo(" from ") != 0) )
			{
			  pos++;
			}
			if (Head.substring(pos,pos+6).compareTo(" from ") != 0)
			{
				Columns++;
				pos++;
			}
		}
	}
	return Columns;
}
/*************************************************************************************/
  public  String getGridResults(int Border, boolean Select,String DataSource)
  {
	int Counter;
	String NewSQL;

	String RadioButton;

	Grid="";

	setHeader(Border,Select);

	if (Select)
	{
	  // Unpack Key field and add to sql
	  // FromPos marks the position of the from clause
	  NewSQL=SQL.substring(0,FromPos+7);

	  Counter=0;
	  NewSQL=NewSQL+","+Key[Counter];
	  Counter++;
	  while (Counter <= Key.length-1)
	  {
	    NewSQL=NewSQL+"+'|'+"+Key[Counter];
	    Counter++;
	  }
	  //Now add the remainder of the original SQL
	  SQL=NewSQL+SQL.substring(FromPos+7,SQL.length());
	  //RadioButton="<td width=100 bgcolor=lightblue ><INPUT type='radio' id='Select' name='Select' ";
	  RadioButton="<td width=50 bgcolor=\"#003399\" align=center><image border=0 src=\""+sharedPath+"/nr/cw/newimages/icon_edit.gif\"";
	}
	else
	{
		 RadioButton="";
	}
	if (Connect(READ,DataSource))
	{
		Grid="";
		Grid=Grid+"<table border ="+Border+">";
		RowCount = 0;
	  try
	  {
	    while (RS.next())
	    {
		RowCount++;
		Grid=Grid+"<tr>";
		for (Counter=1;Counter<=Columns;Counter++)
       		{
		  Grid=Grid+"<td width=160><small><small>&nbsp"+RS.getString(Counter)+"</td>";
		}
		if (Select)
		{
		  //Add the extra generated column for the radio button
		  //Counter++;
		  //Grid=Grid+RadioButton+" onClick=\"parent.document.forms(0).SelectedId.value='"+RS.getString(Counter)+"'\"></td></tr>";
		  Grid=Grid+RadioButton+" onClick=\"Send('"+RS.getString(Counter)+"')\"></td></tr>";
		}
	      }
	      //End the table
	      Grid=Grid+"</table>";
	      // Clean up after ourselves
	      Close();
	    }
	    catch(java.sql.SQLException se)
	    {
	      Grid=se.getMessage();
	      Close();
	      return Grid;
	    }
	}
	else
	{
	  //Connection failed
	  Grid="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
	  return Grid;
	}
	return Grid;
  }
/*************************************************************************************/
  public  String getMenu(String System,String Group_Name,String User_Id)
  {
    StringBuffer MenuSQL = new StringBuffer("");
    String jsStart = System.equals("OSS")?"javascript:welcomeMenuClick(\"":"";
    String jsEnd = System.equals("OSS")?"\")":"";

    MenuSQL.append("Select Form_Name,Menu_Option,Sequence from eBan..User_Group_Menu_Option (nolock) where Menu_Name = 'Main' and Group_Name = '");
    MenuSQL.append(Group_Name).append("' and system = '").append(System).append("' ");
    if ((System.equals("Nostro")) || (System.equals("AdHoc")) ||
      (System.equals("AMEX")) || (System.equals("GCB")))
    {
      MenuSQL.append(" order by sequence");
    }
    else
    {
      MenuSQL.append("UNION Select Form_Name,Menu_Option,Sequence from eBan..User_Group_Menu_Option (nolock) where Menu_Name = 'Main' and Group_Name = 'Returned'");
      MenuSQL.append(" and exists (select 1 from eBan..").append(System).append("_BAN_List_View (nolock) where BAN_Created_By = '");
      MenuSQL.append(User_Id).append("' and Status = 'Returned') order by sequence");
    }

    SQL=MenuSQL.toString();
    int Counter;
    if (Connect(READ,P5))
    //datasource changes if (Connect(READ))
    {
      Grid="";
      Grid=Grid+"<table>";
      RowCount = 0;
      try
      {
	while (RS.next())
	{
	  RowCount++;
            //String href = jsStart + RS.getString(1) + jsEnd;
	    Grid=Grid+"<tr><td><a href='"+RS.getString(1)+"'><img src='"+sharedPath+"/nr/cw/newimages/menu_brown.gif'</a></td>";
	    Grid=Grid+"<td class=menu_bold><a href='"+RS.getString(1)+"' class=navchild>"+RS.getString(2)+"</a></td></tr>";
	}
	//End the table
        /*if (System.equals("OSS"))
        {
          Grid=Grid+"<tr><td colspan=2 class=menu_bold><font color=#003399> " +
          "or enter BAN Id <input type=\"text\" id=\"banId\" " +
          "name=\"banId\"> and click</font> " + HB.getButton("here") + "</td></tr>";
        }*/
	Grid=Grid+"</table>";
	// Free the connection
	Close();
      }
      catch(java.sql.SQLException se){Grid=se.getMessage();
      Close();
      return Grid;}
    }
    else
    {
      //Connection failed
      Grid="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
      return Grid;
    }
  return Grid;
}
/***********************************************************************************/
  public  String getOptionList(String Table,String inSQL,boolean submit,String FirstEntry,String Alert)
  {
	int ColCount=0;
        SQL=inSQL;

	if ((Table != null) && (Table != "") && ((inSQL == "")  ||
	    (SQL ==  null)) && ((Alert == null) || (Alert=="")))
	{//No SQL, so construct from fields

	  SQL = "Select ";
	  SQL=SQL+Fields[ColCount];
	  ColCount++;
	  while (ColCount <= Fields.length-1)
	  {
	    SQL=SQL+","+Fields[ColCount];
	    ColCount++;
	  }
	  SQL=SQL+" from " + Table + " (nolock) where " + Fields[0] + " <> '" + FirstEntry + "' order by " + Fields[0];
	}
	if ((Alert !=null) && (Alert != ""))
	{
	  Grid="<SELECT NAME='"+Table+"' size=1 onClick="+quote+"alert('"+Alert+"')"+quote+">";
	  if (FirstEntry != "")
	  {
	    Grid=Grid+"<OPTION VALUE='"+FirstEntry+"' selected>" +FirstEntry+"</OPTION>";
	  }
	  else
	  {
	    Grid=Grid+"<OPTION VALUE='' selected></OPTION></SELECT>";
	  }
	  return Grid;
	}
	if (Connect(READ,P5))
	{
	  Grid="<SELECT NAME='"+Table+"' size=1 ";
	    if (submit)
	    {
	      Grid=Grid+" onChange='this.form.submit()'>";
	    }
	    else
	    {
	      Grid=Grid+">";
	      }
	      if (FirstEntry != "")
	      {
		Grid=Grid+"<OPTION VALUE='"+FirstEntry+"' selected>" +FirstEntry+"</OPTION>";
	      }
	      RowCount = 0;
	    try
	      {
		while (RS.next())
		{
		  RowCount++;
		  Grid=Grid+"<OPTION VALUE='"+RS.getString(2)+"'>"+RS.getString(1)+"</OPTION>";
		}
		Grid=Grid+"</SELECT>";
		Close();
	      }
	      catch(java.sql.SQLException se)
	      {
		Grid=se.getMessage();
		Close();
	        return Grid;
	      }
	}
	else
	{
		//Connection failed
		Grid="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
		return Grid;
	}
	return Grid;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, int ti)
  {
    tabIndex = ti;
    String ret = getListBox(Table, Mode, FirstEntry, QualifierValue);
    tabIndex = 0;
    return ret;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, String QualifierValue2, int listBoxSize,
    String listBoxStyle, boolean blankLine)
  {
    selectStyle = listBoxStyle;
    selectSize = listBoxSize;
    addBlankLine = blankLine;
    extraQualifierValue=QualifierValue2;
    String ret = getListBox(Table, Mode, FirstEntry, QualifierValue);
    selectSize = 1;
    selectStyle = "";
    addBlankLine = true;
    return ret;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, int listBoxSize, String listBoxStyle,
    boolean blankLine, int ti)
  {
    tabIndex = ti;
    String ret = getListBox(Table, Mode, FirstEntry, QualifierValue,
      listBoxSize, listBoxStyle, blankLine);
    tabIndex = 0;
    return ret;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, int listBoxSize, String listBoxStyle,
    boolean blankLine)
  {
    selectStyle = listBoxStyle;
    selectSize = listBoxSize;
    addBlankLine = blankLine;
    String ret = getListBox(Table, Mode, FirstEntry, QualifierValue);
    selectSize = 1;
    selectStyle = "";
    addBlankLine = true;
    return ret;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, boolean blankLine)
  {
    addBlankLine = blankLine;
    String ret = getListBox(Table, Mode, FirstEntry, QualifierValue);
    addBlankLine = true;
    return ret;
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,int FirstEntry,String QualifierValue)
  {
    return getListBox(Table,Mode,Integer.toString(FirstEntry),QualifierValue);
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    int QualifierValue,String QualifierValue2)
  {
    extraQualifierValue=QualifierValue2;
    return getListBox(Table,Mode,FirstEntry,QualifierValue==0?"":
      Integer.toString(QualifierValue));
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue,String QualifierValue2)
  {
    extraQualifierValue=QualifierValue2;
    return getListBox(Table,Mode,FirstEntry,QualifierValue);
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    int QualifierValue,String QualifierValue2,String QualifierValue3)
  {
    extraQualifierValue=QualifierValue2;
    extraQualifierValue2=QualifierValue3;
    return getListBox(Table,Mode,FirstEntry,QualifierValue==0?"":
      Integer.toString(QualifierValue));
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue,String QualifierValue2,String QualifierValue3)
  {
    extraQualifierValue=QualifierValue2;
    extraQualifierValue2=QualifierValue3;
    return getListBox(Table,Mode,FirstEntry,QualifierValue);
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,String QualifierValue)
  {
    java.sql.ResultSet thisRs = null;
    java.sql.Statement thisStmt = null;
    boolean selected=false;
    StringBuffer ListSQL = new StringBuffer("");
    String Option;
    StringBuffer ListHTML = new StringBuffer("");
    StringBuffer ListHTML2 = new StringBuffer("");
    String returnString = "";

    setListBoxMetaData(Table,QualifierValue);

    if (DBTable.equals("YN_Ind"))
    {
      ListSQL.append("Select 'Y' ,'Y' union select 'N' ,'N'");
    }
    else
    {
      if (DB.equals("n/a"))
      {
        ListSQL.append("Select " + Name + "," + Value + " from " + DBTable + " ");
      }
      else
      {
        ListSQL.append("Select " + Name + "," + Value + " from " + DB + ".." +
          DBTable + " (nolock) ");
      }
      ListSQL.append("where " + (Value.equals("count(*)")?"'x' = 'x' ":Value +
        " is not null ") + Qualifier);
      if (GroupBy.compareTo("") !=0)
      {
        ListSQL.append(" group by ").append(GroupBy);
        GroupBy = "";
      }
      if (OrderBy.compareTo("") !=0)
      {
        ListSQL.append(" order by ").append(OrderBy);
      }
    }
    String thisSQL=ListSQL.toString();
//if (thisSQL.indexOf("Billing_Items")>0)
//System.out.println(thisSQL);

    if ((Qualifier.compareTo("") !=0)&&(QualifierValue.compareTo("") ==0))
    {
      ListHTML.append("<SELECT " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
        selectStyle+" NAME='").append(Table).append("' size="+selectSize+" onClick=\"");
      if ((Alert !=null) && (Alert != ""))
      {
        ListHTML.append("alert(").append(Alert).append(")");
      }
      ListHTML.append("\">");
      if (FirstEntry != "")
      {
	ListHTML.append("<OPTION VALUE='").append(FirstEntry).append("' selected>");
	ListHTML.append(FirstEntry).append("</OPTION>");
      }
      else
      {
        if (addBlankLine)
          ListHTML.append("<OPTION VALUE='' selected></OPTION></SELECT>");
      }

      return ListHTML.toString();
    }
    try
    {
      Vector v = Connect(thisSQL, ListDataSource);
      if (v != null)
      //Datasource changes if (Connect(READ))
      {
        thisStmt = (java.sql.Statement)v.get(0);
        thisRs = (java.sql.ResultSet)v.get(1);
        ListHTML.append("<SELECT "+ (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
          selectStyle+" NAME='").append(Table).append("' size="+selectSize+" ");

        if (Mode.compareTo("submit")==0)
        {
          ListHTML.append(" onChange='this.form.submit()'>");
        }
        else if (Mode.compareTo("READONLY")==0)
        {
          ListHTML.append(" DISABLED>");
        }
        else if (Mode.compareTo("process")==0)
        {
          ListHTML.append(" onChange='"+Table+"_List_Process()'>");
        }
        else if (Table.equals("MANS_Customer"))
        {
          ListHTML.append(" onChange='storeCustomerName()'>");
        }
        /*else if (Table.equals("Multiple_GCDs"))
        {
          ListHTML.append(" onChange='checkGSRVis()'>");
        }*/
        else if (Mode.compareTo("")==0)
        {
          ListHTML.append(">");
        }
        else
        {
          ListHTML.append(" ").append(Mode).append(">");
        }
        RowCount = 0;

        try
        {
          while (thisRs.next())
          {
            RowCount++;
            String text=thisRs.getString(1);
            Option=thisRs.getString(2);
            ListHTML2.append("<OPTION VALUE='").append(Option).append("'");
            if ((FirstEntry != null) && (Option.compareToIgnoreCase(FirstEntry)!=0))
            {
              /*if (((Value.equals("count(*)")) ||
                (Table.equals("BAN_Tax_Requirement"))) &&
                (text.equals(FirstEntry)))*/
              //String feStart = FirstEntry.substring(0, FirstEntry.lastIndexOf("-"));
              if /*((Value != null) &&*/ ((Value.equals("count(*)") &&
                text.equals(FirstEntry)) ||
                (Table.equals("BAN_Tax_Requirement") &&
                FirstEntry.lastIndexOf("-") != -1 &&
                Option.startsWith(FirstEntry.substring(0, FirstEntry.lastIndexOf("-")))))//)
              {
                selected=true;
                ListHTML2.append(" SELECTED");
              }
            }
            else
            {
              selected=true;
              ListHTML2.append(" SELECTED");
            }
            ListHTML2.append(">").append(text).append("</OPTION>");
          }
          if (Table.equals("MANS_Customer"))
          {
            ListHTML2.append("<OPTION VALUE=\"\"");
            if ((FirstEntry != null) && (FirstEntry.equals("Completely New Customer")))
            {
              ListHTML2.append(" SELECTED");
            }
            ListHTML2.append(">Completely New Customer</OPTION>");
          }
          if (selected)
          {
            if ((Table.equals("List_Month2")) ||
              (Table.equals("Exception_Type")) ||
              (Table.equals("Exception_Status")) ||
              (Table.equals("Conglom_Billed_Product")) ||
              (Table.equals("Conglom_Period")) ||
              (Table.equals("Conglom_Inv_Doc")) ||
              (Table.equals("Conglom_Exception_Type")) ||
              (Table.equals("Conglom_Exception_Status")) ||
              (Table.equals("IRIN")) ||
              (Table.equals("GSR")) ||
              (Table.equals("conglomBilledProduct2")) ||
              (Table.equals("conglomBilledProduct4")) ||
              (Table.equals("conglomPeriod")) ||
              (Table.equals("filterMonth")) ||
              (Table.equals("filterProvider")) ||
              (Table.equals("providerName2")) ||
              (Table.equals("conglomItem")))
            {
              String temp = ListHTML.toString();
              int pos = temp.indexOf(">");
              ListHTML.insert(pos+1,"<OPTION VALUE=\"All\">All</OPTION>");
            }
            else if (addBlankLine)
            {
              ListHTML.append("<OPTION VALUE=\"\" SELECTED>&nbsp</OPTION>");
            }
            ListHTML.append(ListHTML2.toString());
          }
          else
          {
            if (FirstEntry != null)
            {
              if (FirstEntry.compareTo("All")==0)
              {
                ListHTML.append("<OPTION VALUE=\"All\" SELECTED>All</OPTION>");
                ListHTML.append(ListHTML2.toString());
              }
              else if ((RowCount == 1) && (Mode.compareTo("submit")!=0) &&
                (!Table.equals("Nostro_Account")) && (!Table.equals("New_BA")) &&
                (!Table.equals("conglomDiscountType")) &&
                (!Table.equals("Billing_Region_Site")) &&
                (!Table.equals("conglomBilledProduct3")) &&
                (!Table.equals("GCB_Account2")) &&
                (!Table.equals("accountNumber")) &&
                (!Table.equals("masterAccountNumber")) &&
                (Table.indexOf("Site_Reference") == -1) &&
                (!Table.startsWith("conglomDataItem")))
              {//only one option, so don't add null first entry
                ListHTML.append(ListHTML2.toString());

/*                (((!Table.equals("conglomBilledProduct3")) &&
                (!Table.equals("conglomDiscountType"))) &&
                (!Mode.startsWith("onChange")))) &&
*/
              }
              else
              {//null first entry
                if (Table.equals("Ad_Hoc_Account"))
                {
                  if (RowCount==0)
                  {
                    ListHTML = new StringBuffer("<SELECT " +
                      (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
                      selectStyle+" NAME='"+
                      Table+"' size="+selectSize+
                      " onClick=\"alert('No accounts match this filter')\">");
                  }
                  else if (RowCount > maxAccounts)
                  {
                    ListHTML = new StringBuffer("<SELECT " +
                      (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
                      selectStyle+" NAME='"+
                      Table+"' size="+selectSize+
                      " onClick=\"alert('More than " + Integer.toString(maxAccounts) +
                      "accounts match this filter. Please refine your search.')\">");
                    ListHTML2 = new StringBuffer();
                  }
                }
                if ((Table.equals("BAN_Tax_Requirement")) && (RowCount==0))
                {
                  ListHTML.append("<OPTION VALUE=\"N/A-0.00000-N/A-00\" SELECTED>N/A(0.00%)</OPTION>");
                }
                if (addBlankLine)
                  ListHTML.append("<OPTION VALUE=\"\" SELECTED>&nbsp</OPTION>");
                if (Table.equals("providerName2"))
                {
                  ListHTML.append("<OPTION VALUE=\"All\">All</OPTION>");
                }
                ListHTML.append(ListHTML2.toString());
              }
            }
          }
          ListHTML.append("</SELECT>");
          returnString = ListHTML.toString();
        }
        catch(java.sql.SQLException se)
        {
          returnString = se.getMessage();
        }
      }
      else
      {
          //Connection failed
          Grid="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
          returnString = Grid;
      }
    }
    catch(Exception ex)
    {
      returnString=ex.getMessage();
    }
    finally
    {
      Close(thisStmt, thisRs);
      return returnString;
    }
  }

  /***********************************************************************************/
  public  String getTextArea(String IdField,String DataSource)
  //datasource changes  public  String getTextArea(String IdField)
  {
    String HTML="",Name="",Id="";
    int ColCount=0,i;

    if (Options.hasMoreElements())
    {
      Id=(String)Options.nextElement();
    }

    if (Options.hasMoreElements())
    {
      Name=(String)Options.nextElement();
      ColCount++;
    }
    SQL="Select "+Name;
    while (Options.hasMoreElements())
    {
      Name=(String)Options.nextElement();
      SQL+=","+Name;
      ColCount++;
    }

    if (DB.equals("n/a"))
    {
      SQL+=" from " + Table + " where "+ Id + "='"+IdField+"'";
    }
    else
    {
      SQL+=" from " + DB + ".." + Table + " (nolock) where "+ Id + "='"+IdField+"'";
    }

    if (Connect(READ,DataSource))
    //datasource changes if (Connect(READ))
    {
      RowCount = 0;

      try
      {
	if (RS.next())
	{
	  i=1;
	  RowCount++;
	  //HTML="<textarea cols=25 rows="+Integer.toString(ColCount)+">";
	  HTML+=RS.getString(i);
	  for (i=2;i<ColCount;i++)
	  {
	    if (SU.isNull(RS.getString(i),"") != "")
	    {
	      HTML+="<br>"+RS.getString(i);
	    }
	  }
	}
	//HTML+="</textarea>";
	// Clean up after ourselves
	Close();
      }
      catch(java.sql.SQLException se)
      {
	HTML=se.getMessage();
	Close();
	return HTML;
      }
    }
    else
    {
      //Connection failed
      HTML="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
      return HTML;
    }
    return HTML;
  }

 /***********************************************************************************/
 public boolean getExists(String ExistsSQL,String DataSource)
 // datasource changes   public  boolean getExists(String ExistsSQL)
  {
    boolean found = false;
    SQL=ExistsSQL;
    try
    {
      if(Connect(READ,DataSource))
      {
        if (RS.next())
        {
          found = true;
        }
        else
        {
          Message="No data found";
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    finally
    {
      Close();
      return found;
    }
  }
  /*************************************************************************************************************/
  public String get_Site_Splits(String Global_Customer_Id,String Site_Id,int Split_Sites,String Mode,Hashtable inSplitSites,String Class)
  {
    String Splits_Table="",site,pct,desc;
    String SelectMode="";
    Hashtable SplitSites=inSplitSites;

    int Splits=1;
    StringBuffer Splits_Table_Buffer = new StringBuffer("");
    if ((Split_Sites==0) || (Global_Customer_Id.compareTo("")==0) || (Site_Id.compareTo("")==0))
    {
      return Splits_Table;
    }
    else
    {
      if (Mode.compareToIgnoreCase("READONLY")==0)
      {
	SelectMode="DISABLED";
      }
      Splits_Table_Buffer.append("<tr><td colspan=4 class=").append(Class).append(">Split Sites</td></tr><tr><tr><td colspan=4>");
      Splits_Table_Buffer.append("<table border=0><tr><td></td><td class=mandatory><b><small>%</td><td class=mandatory><small><b>Site</td>");
      Splits_Table_Buffer.append("<td><small><b>Description</td></tr>");
      Splits_Table_Buffer.append("<tr><td></td><td><input class=inp type=\"text\" name=\"tablepct0\" maxlength=\"5\" size=\"5\" value=\"").append(SU.isNull((String)SplitSites.get("tablepct0"),"")).append("\" ");
      Splits_Table_Buffer.append(Mode).append("</td><td>").append(getListBox("tableSplitSite0","DISABLED",Site_Id,Global_Customer_Id));
      Splits_Table_Buffer.append("</td><td><input class=inp type=\"text\" name=\"tabledesc0\" maxlength=\"50\" size=\"50\" value=\"").append(SU.isNull((String)SplitSites.get("tabledesc0"),"")).append("\" </td></tr>");
      //Note that 1 split = 2 rows, main site plus 1 other
      while (Splits<=Split_Sites)
      {
        site="tableSplitSite"+String.valueOf(Splits);
	pct="tablepct"+String.valueOf(Splits);
	desc="tabledesc"+String.valueOf(Splits);

	Splits_Table_Buffer.append("<tr><td></td><td><input class=inp type=\"text\" name=\"tablepct").append(Splits).append("\" maxlength=\"5\" size=\"5\" value=\"").append(SU.isNull((String)SplitSites.get(pct),"")).append("\" ");
        Splits_Table_Buffer.append(Mode).append("</td><td>").append(getListBox(site,Mode,SU.isNull((String)SplitSites.get(site),""),Global_Customer_Id));
        Splits_Table_Buffer.append("</td><td><input class=inp type=\"text\" name=\"tabledesc").append(Splits).append("\" maxlength=\"50\" size=\"50\" value=\"").append(SU.isNull((String)SplitSites.get(desc),"")).append("\" </td></tr>");
        Splits++;
      }
      Splits_Table_Buffer.append("</td></tr></table>");
      return Splits_Table_Buffer.toString();
    }
  }
  /***********************************************************************************/
  public  boolean DBWrite(String Table,String[][] Columns,String DataSource)
  {
	getSQLforInsert(Table,Columns);

	if(Connect(NORESULT,DataSource))
	{
          Close();
          return true;
	}
	else
	{
	  Close();
	  return false;
	}
   }
  /***********************************************************************************/
  public  boolean NoResult(String inSQL,String DataSource)
  {
	SQL=inSQL;
	if(Connect(NORESULT,DataSource))
	{
          Close();
          return true;
	}
	else
	{
          Close();
          return false;
	}
   }
  /***********************************************************************************/
  private void getSQLforInsert(String Table,String[][] Columns )
  {
	char delimiter=',';
	int pos;
	pos=0;

	SQL="Insert into "+Table+"( ";

        SQL=SQL+Columns[pos][0];
  	pos++;

	while ((pos < Columns.length) && (Columns[pos][0] != ""))
	{
	  SQL=SQL+delimiter;
	  SQL=SQL+Columns[pos][0];
  	  pos++;
	}

	SQL=SQL+") values(";

	pos=0;
        if (Columns[pos][2] == "C")
	{
	   SQL=SQL+"'";
	}
	SQL=SQL+Columns[pos][1];

	if (Columns[pos][2] == "C")
	{
	  SQL=SQL+"'";
	}
	pos++;
	while ((pos < Columns.length) && (Columns[pos][0] != ""))
	{
  	  SQL=SQL+delimiter;
	  if (Columns[pos][2] == "C")
	  {
	    SQL=SQL+"'";
	  }
	  SQL=SQL+Columns[pos][1];

	  if (Columns[pos][2] == "C")
	  {
	    SQL=SQL+"'";
	  }
	  pos++;
	}
	SQL=SQL+")";
  }
/*************************************************************************************/
private void getServerNames(String System)
{
  BufferedReader inifile;
  String inirecord="";

  inifile=FU.openFile("c:\\JDBC\\"+System+".ini");

  inirecord=FU.readNext(inifile);

  while ((inirecord.compareTo("EOF") != 0) && (!inirecord.startsWith("EOF")))
  {
    if (SU.before(inirecord).equalsIgnoreCase("P3SERVERNAME"))
    {
      P3ServerName=SU.Extract(inirecord,2);
      P3Connections=SU.toInt(SU.Extract(inirecord,3));
    }
    else if (SU.before(inirecord).equalsIgnoreCase("P4SERVERNAME"))
    {
      P4ServerName=SU.Extract(inirecord,2);
      P4Connections=SU.toInt(SU.Extract(inirecord,3));

    }
    else if (SU.before(inirecord).equalsIgnoreCase("P5SERVERNAME"))
    {
      P5ServerName=SU.Extract(inirecord,2);
      P5Connections=SU.toInt(SU.Extract(inirecord,3));
    }
    inirecord=FU.readNext(inifile);
  }
}
/*************************************************************************************/
  public void startDB()
  {
    /*P3ServerName = EBANProperties.getEBANProperty(EBANProperties.P3SERVERNAME);
    P3Connections = Integer.parseInt(
      EBANProperties.getEBANProperty(EBANProperties.P3CONNECTIONS));
    P4ServerName = EBANProperties.getEBANProperty(EBANProperties.P4SERVERNAME);
    P4Connections = Integer.parseInt(
      EBANProperties.getEBANProperty(EBANProperties.P4CONNECTIONS));
    P5ServerName = EBANProperties.getEBANProperty(EBANProperties.P5SERVERNAME);
    P5Connections = Integer.parseInt(
      EBANProperties.getEBANProperty(EBANProperties.P5CONNECTIONS));*/
    serverName = EBANProperties.getEBANProperty(EBANProperties.SERVERNAME);
    connections = Integer.parseInt(
      EBANProperties.getEBANProperty(EBANProperties.CONNECTIONS));
    environment = Integer.parseInt(
      EBANProperties.getEBANProperty(EBANProperties.ENVIRONMENT));
    UniDataSource.getInstance(serverName, connections, environment);
    environmentGF = Integer.parseInt(EBANProperties.getEBANProperty("environmentGF",Integer.toString(environment)));
    connectionsGF = Integer.parseInt(EBANProperties.getEBANProperty("connectionsGF",Integer.toString(connections)));
    serverNameGF = EBANProperties.getEBANProperty("serverNameGF",serverName);
    GoldfishDataSource.getInstance(serverNameGF, connectionsGF, environmentGF);

    /*P5DataSource.getInstance(P5ServerName,P5Connections);
    P3DataSource.getInstance(P3ServerName,P3Connections);
    P4DataSource.getInstance(P4ServerName,P4Connections);*/
  }

/*************************************************************************************/
  public void startDB(String System)
  {
    getServerNames(System);

    if (P5ServerName.compareTo("") !=0)
    {
      P5DataSource.getInstance(P5ServerName,P5Connections);
    }
    if (P3ServerName.compareTo("") !=0)
    {
      P3DataSource.getInstance(P3ServerName,P3Connections);
    }
    if (P4ServerName.compareTo("") !=0)
    {
      P4DataSource.getInstance(P4ServerName,P4Connections);
    }
   }
/*************************************************************************************/
  public Enumeration getAddress(String Account_No, String Billing_Source)
  {
	SQL = "select Contact_Name, Address_Line_1, Address_Line_2, Address_Line_3, "  +
          "Postcode, Country " +
          "from eBan..Ad_Hoc_Billing_Address (nolock) where Account_No = '" +
        Account_No + "' and Billing_Source = '" + Billing_Source + "'";

	Vector Result = new Vector();
	if (Connect(READ,P5))
	{
	  try
	  {
	    if (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	      Result.addElement(RS.getString(3));
	      Result.addElement(RS.getString(4));
	      Result.addElement(RS.getString(5));
	      Result.addElement(RS.getString(6));
	    }
            else
            {
	      Result.addElement("not found");
            }
	    Close();
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement("error - " + se.getMessage());
	    Close();
	  }
	}
	else
	{
          Result.addElement("error - " + Message);
	}
	return Result.elements();
  }
/*************************************************************************************/
  public String createAddress(String Account_No, String Billing_Source,
    String Contact_Name, String Address_Line_1, String Address_Line_2,
    String Address_Line_3, String Postcode, String Country, String Created_By,
    String type)
  {
    String returnMessage = "Address " + (type.equals("create")?"Created":"Amended") +
      " Successfully";
    try
    {
      if (type.equals("create"))
      {
        SQL = "SELECT Contact_Name FROM eBAN..Ad_Hoc_Billing_Address (nolock) " +
          "WHERE Account_No = '" + Account_No + "' " +
          "AND Billing_Source = '" + Billing_Source + "' ";

        if (!Connect(READ,P5))
        { //Failed to connect - message set in underlying code
          returnMessage=Message;
        }
        else
        {
          if (RS.next())
          {
            returnMessage="Address already exists for this account";
          }
          else
          {
            SQL = "INSERT INTO eBAN..Ad_Hoc_Billing_Address " +
              "VALUES ('" + Account_No + "','" + Billing_Source + "','" + Contact_Name +
              "','" + Address_Line_1 + "','" + Address_Line_2 + "','" + Address_Line_3 +
              "','" + Postcode + "','" + Country + "',getdate(),' " + Created_By +
              "')";
            if (!Connect(NORESULT,P5))
            { //Failed to connect - message set in underlying code
              returnMessage=Message;
            }
          }
        }
      }
      else
      {
        SQL = "UPDATE eBAN..Ad_Hoc_Billing_Address " +
          "SET Contact_Name = '" + Contact_Name + "', " +
          "Address_Line_1 = '" + Address_Line_1 + "', " +
          "Address_Line_2 = '" + Address_Line_2 + "', " +
          "Address_Line_3 = '" + Address_Line_3 + "', " +
          "Postcode = '" + Postcode + "', " +
          "Country = '" + Country + "', " +
          "Last_Update_Date = getdate(), " +
          "Last_Update_Id = '" + Created_By + "' " +
          "WHERE Account_No = '" + Account_No + "' " +
          "AND Billing_Source = '" + Billing_Source + "' ";
        if (!Connect(NORESULT,P5))
        { //Failed to connect - message set in underlying code
          returnMessage=Message;
        }
      }
    }
    catch(Exception ex)
    {
      returnMessage="<font color=red><b>"+ex.getMessage();
    }//message set in underlying code
    finally
    {
      Close();
      return returnMessage;
    }
  }
/*************************************************************************************/
  public boolean createAttachment(String location, String name, int id, String type,
                                  String attachmentDate, String downloadType,
                                  String itemType, String fileId)
  {
    boolean success = false;
    SQL = "INSERT INTO Attachment "+
      "(Location,Name,Id,Type,Attachment_Date,Download_Type,Item_Type,File_Id) "+
      "VALUES('"+location+"','"+name+"',"+id+",'"+type+"',TO_DATE('"+attachmentDate+
      "','DD-MON-YY'),'"+downloadType+"','"+itemType+"',"+fileId+") ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public int ebillzInvoiceWLR(int accountId, String attachmentDate)
  {
    int invoiceId = -1;
    SQL = "SELECT Invoice_Id "+
      "FROM Invoice "+
      "WHERE Issue_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') " +
      "AND Account_Id = " + accountId;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          invoiceId = RS.getInt(1);
        }
      }
      Close();
    }
    catch(java.sql.SQLException ex)
    {
      Close();
      System.out.println(ex.getErrorCode()+" : "+ex.getMessage());
    }
    return invoiceId;
  }
/*************************************************************************************/
  public String ebillzInvoiceNo(int invoiceId)
  {
    String invoiceNo = "";
    SQL = "SELECT Invoice_No FROM Invoice WHERE Invoice_Id = " + invoiceId;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          invoiceNo = RS.getString(1);
        }
      }
      Close();
    }
    catch(java.sql.SQLException ex)
    {
      Close();
      System.out.println(ex.getErrorCode()+" : "+ex.getMessage());
    }
    return invoiceNo;
  }
/*************************************************************************************/
  public long SSBSInvoice(String invoiceNo, long accountId,
    String taxPointDate, String total, String periodFrom, String periodTo)
  {
    long invoiceId = -1;
    String findSQL = "SELECT Invoice_Id FROM Invoice "+
      "WHERE Invoice_No = '"+invoiceNo+"' AND Account_Id = "+accountId;
    SQL = findSQL;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          // does exist so get id and update with current values
          invoiceId = RS.getLong(1);
          SQL = "UPDATE Invoice " +
            "SET Tax_Point_Date = TO_DATE('" + taxPointDate + "','YYYYMMDD'), " +
            "Invoice_Total = " + total + "/100, " +
            "Period_From_Date = TO_DATE('" +periodFrom + "','YYYYMMDD'), " +
            "Period_To_Date = TO_DATE('" +periodTo + "','YYYYMMDD') " +
            "WHERE Invoice_Id = " + invoiceId;
          if (!Connect(NORESULT,P5))
          {
            invoiceId = -1;
          }
        }
        else
        {
          // does not exist so create it and get id
          SQL = "INSERT INTO Invoice "+
                "(Invoice_Id,Account_Id, ISO_Currency_Code,Invoice_Type,"+
                "Invoice_No,Issue_Date,Tax_Point_Date,Period_From_Date,"+
                "Period_To_Date,Invoice_Total,Ebilled_Ind,Sent_Ind,"+
                "Last_Maintained_Date,Last_Maintained_By,History_Ind) "+
                "VALUES"+
                "(Invoice_Seq.NEXTVAL,"+accountId+",'GBP','Invoice','"+
                invoiceNo+"',TO_DATE('" + taxPointDate + "','YYYYMMDD'),"+
                "TO_DATE('" + taxPointDate + "','YYYYMMDD'),"+
                "TO_DATE('" + periodFrom + "','YYYYMMDD')," +
                "TO_DATE('" + periodTo + "','YYYYMMDD')," +
                total + "/100,'Ebilled','Unsent',SYSDATE," +
                "'loadSSBSInvoices','N')";
          if(Connect(NORESULT,P5))
          {
            SQL = findSQL;
            if (Connect(READ,P5))
            {
              if (RS.next())
              {
                invoiceId = RS.getLong(1);
              }
            }
          }
        }
      }
      else
      {

      }
      Close();
    }
    catch(java.sql.SQLException ex)
    {
      Close();
      System.out.println(ex.getErrorCode()+" : "+ex.getMessage());
    }
    return invoiceId;
  }
/*************************************************************************************/
  public long PCBInvoice(String invoiceNo, long accountId,
    String taxPointDate, String total, String periodFrom, String periodTo)
  {
    long invoiceId = -1;
    String findSQL =
      "SELECT Invoice_Id FROM Invoice "+
      "WHERE Invoice_No = '"+invoiceNo+"' AND Account_Id = "+accountId;
    SQL = findSQL;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          // does exist so get id and update with current values
          invoiceId = RS.getLong(1);
          SQL = "UPDATE Invoice " +
            "SET Tax_Point_Date = TO_DATE('" + taxPointDate + "','YYYYMMDD'), " +
            "Invoice_Total = " + total + "/100, " +
            "Period_From_Date = TO_DATE('" +periodFrom + "','YYYYMMDD'), " +
            "Period_To_Date = TO_DATE('" +periodTo + "','YYYYMMDD') " +
            "WHERE Invoice_Id = " + invoiceId;
          if (!Connect(NORESULT,P5))
          {
            invoiceId = -1;
          }
        }
        else
        {
          // does not exist so create it and get id
          SQL = "INSERT INTO Invoice "+
                "(Invoice_Id,Account_Id, ISO_Currency_Code,Invoice_Type,"+
                "Invoice_No,Issue_Date,Tax_Point_Date,Period_From_Date,"+
                "Period_To_Date,Invoice_Total,Ebilled_Ind,Sent_Ind,"+
                "Last_Maintained_Date,Last_Maintained_By,History_Ind) "+
                "VALUES"+
                "(Invoice_Seq.NEXTVAL,"+accountId+",'GBP','Invoice','"+
                invoiceNo+"',TO_DATE('" + taxPointDate + "','YYYYMMDD'),"+
                "TO_DATE('" + taxPointDate + "','YYYYMMDD'),"+
                "TO_DATE('" + periodFrom + "','YYYYMMDD')," +
                "TO_DATE('" + periodTo + "','YYYYMMDD')," +
                total + "/100,'Ebilled','Unsent',SYSDATE," +
                "'loadSSBSInvoices','N')";
          if(Connect(NORESULT,P5))
          {
            SQL = findSQL;
            if (Connect(READ,P5))
            {
              if (RS.next())
              {
                invoiceId = RS.getLong(1);
              }
            }
          }
        }
      }
      else
      {

      }
      Close();
    }
    catch(java.sql.SQLException ex)
    {
      Close();
      System.out.println(ex.getErrorCode()+" : "+ex.getMessage());
    }
    return invoiceId;
  }
/*************************************************************************************/
  public int ebillzInvoice(String invoiceNo, int accountId, String attachmentDate)
  {
    int invoiceId = -1;
    String findSQL = "";
    findSQL = "SELECT Invoice_Id "+
      "FROM Invoice "+
      "WHERE Invoice_No = '" + invoiceNo + "' " +
      "AND   Account_Id = " + accountId;
    SQL = findSQL;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          // does exist so get id
          invoiceId = RS.getInt(1);
        }
        else
        {
          String currency = "";
          SQL = "SELECT Invoice_Currency "+
            "FROM CDR_Format " +
            "WHERE Account_Id = " + accountId;
          if (RS.next())
          {
            currency = RS.getString(1);
          }
          // does not exist so create
          SQL = "INSERT INTO Invoice " +
            "(Invoice_Id,Account_Id,ISO_Currency_Code,Invoice_No,Invoice_Type," +
            "Issue_Date,Tax_Point_Date,Period_From_Date,Period_To_Date," +
            "Invoice_Total,Ebilled_Ind,Sent_Ind,Last_Maintained_Date,"+
            "Last_Maintained_By,History_Ind) VALUES(Invoice_Seq.NEXTVAL,'"+
            accountId+"','EUR','" +invoiceNo+"','Invoice',TO_DATE('"+attachmentDate+
            "','DD-MON-YY'),TO_DATE('"+attachmentDate+"','DD-MON-YY'),"+
            "ADD_MONTHS(TO_DATE('"+attachmentDate+"'),-1),"+
            "TO_DATE('"+attachmentDate+"')-1,"+
            "0,'Ebilled','Sent',SYSDATE,'ebillzControl','N')";
          if(Connect(NORESULT,P5))
          {
            SQL = findSQL;
            if (Connect(READ,P5))
            {
              if (RS.next())
              {
                invoiceId = RS.getInt(1);
              }
            }
          }
        }
      }
      Close();
    }
    catch(java.sql.SQLException ex)
    {
      Close();
      System.out.println(ex.getErrorCode()+" : "+ex.getMessage());
    }
    return invoiceId;
  }
/*************************************************************************************/
  public boolean updateAccountAttachment(String location, int accountId, String attachmentDate)
  {
    boolean success = false;
    SQL = "UPDATE Attachment "+
      "SET Location = '"+location+"' "+
      "WHERE Id = " + accountId + " " +
      "AND Type = 'account' " +
      "AND Item_Type = 'cdr' " +
      "AND Attachment_Date = '" + attachmentDate + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public boolean updateCDRInvoiceAttachment(String location, int invoiceId, String attachmentDate)
  {
    boolean success = false;
    SQL = "UPDATE Attachment "+
      "SET Location = '"+location+"' "+
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'cdr' " +
      "AND Name NOT LIKE ('%WLR%') " +
      "AND Attachment_Date = '" + attachmentDate + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public boolean updatePDFInvoiceAttachment(String location, int invoiceId, String attachmentDate)
  {
    boolean success = false;
    SQL = "UPDATE Attachment "+
      "SET Location = '"+location+"' "+
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Location NOT LIKE ('%Other%') " +
      "AND Attachment_Date = '" + attachmentDate + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public boolean updateOtherInvoiceAttachment(String location, int invoiceId, String attachmentDate)
  {
    boolean success = false;
    SQL = "UPDATE Attachment "+
      "SET Location = '"+location+"' "+
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Location LIKE ('%Other%') " +
      "AND Attachment_Date = '" + attachmentDate + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public boolean updateInvoiceAttachmentWLR(String location, int invoiceId, String attachmentDate)
  {
    boolean success = false;
    SQL = "UPDATE Attachment "+
      "SET Location = '"+location+"' "+
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'Invoice' " +
      "AND Item_Type = 'cdr' " +
      "AND Name LIKE ('%WLR%') " +
      "AND Attachment_Date = '" + attachmentDate + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*************************************************************************************/
  public boolean updateDIF(long id, boolean success)
  {
    boolean ok = false;
    SQL = "UPDATE Dataflow_Input_File ";
    if (success)
      SQL = SQL + "SET FTP_Status = 'Identified' ";
    else
      SQL = SQL + "SET FTP_Status = 'Request Failed' ";
    SQL = SQL + "WHERE Dataflow_Input_File_Id = " + id;
    if(Connect(NORESULT,P5))
      ok = true;
    else
    {
      System.out.println("Failed to update FTP Status for Dataflow_Input_File");
      System.out.println(SQL);
    }
    return ok;
  }
/*************************************************************************************/
  public String createUser(String User_Name, String User_Id,
    String Password, String User_Group, String System, String Created_By,
    String type)
  {
    String returnMessage = "User " + (type.equals("create")?"Created":"Updated") +
      " Successfully";
    try
    {
      if (type.equals("create"))
      {
        SQL = "SELECT User_Name FROM eBAN..eBAN_User (nolock) " +
          "Where Login_Id = '" + User_Id + "' " +
          "AND System = '" + System + "' ";

        if (!Connect(READ,P5))
        { //Failed to connect - message set in underlying code
          returnMessage=Message;
        }
        else
        {
          if (RS.next())
          {
            returnMessage="User Id already exists for this System";
          }
          else
          {
            SQL = "INSERT INTO eBAN..eBAN_User " +
              "VALUES ('" + User_Id + "','" + User_Name + "','" + Password +
              "','" + System + "','" + User_Group + "',getdate(),' " + Created_By +
              "')";
            if (!Connect(NORESULT,P5))
            { //Failed to connect - message set in underlying code
              returnMessage=Message;
            }
          }
        }
      }
      else
      {
        SQL = "UPDATE eBAN..eBAN_User " +
          "SET User_Name = '" + User_Name + "', " +
          "Password = '" + Password + "', " +
          "Group_Name = '" + User_Group + "', " +
          "Last_Update_Date = getdate(), " +
          "Last_Update_Id = '" + Created_By + "' " +
          "WHERE Login_Id = '" + User_Id + "' " +
          "AND System = '" + System + "' ";
        if (!Connect(NORESULT,P5))
        { //Failed to connect - message set in underlying code
          returnMessage=Message;
        }
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      returnMessage="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      Close();
      return returnMessage;
    }
  }
/*****************************************************************************/
  public Enumeration validateSource(String source)
  {
//System.out.println("validateSource 1");
    Vector result = new Vector();
    SQL="select Attachment_Type from givn_ref..EBilling_Attachment_Source (nolock) " +
      "where Billing_Source = '"+ source + "'";
//System.out.println("validateSource SQL: " + SQL);

    if (Connect(READ,P3))
    {
//System.out.println("validateSource 2");
      try
      {
//System.out.println("validateSource 3");
        while (RS.next())
        {
//System.out.println("validateSource 4");
          result.addElement(RS.getString(1));
        }
//System.out.println("validateSource 5");
        Close();
      }
      catch(java.sql.SQLException se)
      {
//System.out.println(se.getMessage());
        Close();
      }
    }
//System.out.println("validateSource 6");
    return result.elements();
  }
/*****************************************************************************/
  public Enumeration getAttatchmentTypes()
  {
    Vector result = new Vector();
    SQL="select Attachment_Type from givn_ref..EBilling_Attachment_Source (nolock) ";
    if (Connect(READ,P3))
    {
      try
      {
        while (RS.next())
        {
          result.addElement(RS.getString(1));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result.elements();
  }
/*****************************************************************************/
  public int getMaxAttatchmentTypeLength()
  {
    int result = 0;
    SQL="select max(len(Attachment_Type)) from givn_ref..EBilling_Attachment_Source (nolock) ";
    if (Connect(READ,P3))
    {
      try
      {
        if (RS.next())
        {
          result = RS.getInt(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public boolean validCustomer(String gcId)
  {
    boolean isValid = false;
    SQL="select Global_Customer_Id from givn..Global_Customer_Application (nolock) " +
      "where Global_Customer_Id = '"+ gcId + "'";

    if (Connect(READ,P3))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean validAccount(String gcId, String accountId)
  {
    boolean isValid = false;
    SQL="select Account_Id from givn_ref..EBilling_Accounts (nolock) " +
      "where Global_Customer_Id = '" + gcId + "' " +
      "and Account_Id = '" + accountId + "' ";

    if (Connect(READ,P3))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public String findInvoice(String gcId, String accountId)
  {
    String invoiceNo = null;
    SQL="select max(Invoice_No) from givn_ref..Invoice_Region_Invoice_Numbers (nolock) " +
      "where Global_Customer_Id = '" + gcId + "' " +
      "and Invoice_Region = (select Invoice_Region from givn_ref..Invoice_Region (nolock) " +
      "where Global_Customer_Id = '" + gcId + "' and Account_Id = '" +
      accountId + "') ";

    if (Connect(READ,P3))
    {
      try
      {
        if (RS.next())
          invoiceNo = RS.getString(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return invoiceNo;
  }
/*****************************************************************************/
  public String validAccount(String accountId)
  {
    String gcId = null;
    SQL="select Global_Customer_Id from givn_ref..Invoice_Region (nolock) " +
      "where Account_Id = '" + accountId + "' ";

    if (Connect(READ,P3))
    {
      try
      {
        if (RS.next())
          gcId = RS.getString(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return gcId;
  }
/*****************************************************************************/
  public boolean validateInvoice(String accountId, String invoiceId)
  {
    boolean isValid = false;
    SQL="select Invoice_No from givn_ref..EBilling_Invoices (nolock) " +
      "where Account_Id = '" + accountId + "' " +
      "and Invoice_No = '" + invoiceId + "' ";

    if (Connect(READ,P3))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public String getCountryCodeFromDescription(String description)
  {
    String code = null;
    SQL="select ISO_2 from eban..Country_Currency (nolock) " +
      "where Country = '" + description + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          code = RS.getString(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return code;
  }
/*****************************************************************************/
  public String getAccountName (int accountId)
  {
    String accountName = "";
    SQL="SELECT Account_Name FROM Account " +
          "WHERE Account_Id = " + accountId;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountName = RS.getString(1).trim();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account name for account id"+accountId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return accountName;
  }
/*****************************************************************************/
  public String getAccountName (long accountId)
  {
    String accountName = "";
    SQL="SELECT Account_Name FROM Account " +
          "WHERE Account_Id = " + accountId;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountName = RS.getString(1).trim();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account name for account id"+accountId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return accountName;
  }
/*****************************************************************************/
  public String getAccountNumber (long accountId)
  {
    String accountNumber = "";
    SQL="SELECT Account_Number FROM Account " +
          "WHERE Account_Id = " + accountId;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountNumber = RS.getString(1).trim();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account name for account id"+accountId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return accountNumber;
  }
/*****************************************************************************/
  public String getInvoiceAccountNumber (long invoiceId)
  {
    String accountNumber = "";
    SQL="SELECT Account_Number FROM Account, Invoice " +
          "WHERE Invoice_Id = " + invoiceId +" "+
          "AND Invoice.Account_Id = Account.Account_Id";
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountNumber = RS.getString(1).trim();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account name for invoice id"+invoiceId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return accountNumber;
  }
/*****************************************************************************/
  public int[] getAccountIds (String accountNumber)
  {
    int[] accountIds = new int[3];
    accountIds[0] = -1;
    SQL="select Account_Id, Account.Payment_Group_Id, Payment_Group.Customer_Id "+
        "from Account, Payment_Group " +
          "where Account_Number = '" + accountNumber + "' "+
          "and Payment_Group.Payment_Group_Id = Account.Payment_Group_Id";
          //System.out.println(SQL);

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountIds[0] = RS.getInt(1);
          accountIds[1] = RS.getInt(2);
          accountIds[2] = RS.getInt(3);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account id for account"+accountNumber+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }
    return accountIds;
  }/*****************************************************************************/
  public String getAccountIdForBAN(String banId)
  {
    String accountId = null;
    SQL="select Account_Id from givn_ref..Invoice_Region (nolock) " +
      "where Last_Update_Id = '" + banId + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountId = RS.getString(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return accountId;
  }
/*****************************************************************************/
  public boolean reengineerSSBSCDR(String accountNumber)
  {
    boolean result = false;
    String test = "";
    SQL = "SELECT Reformat_CDRS FROM Dataflow_Conversion_Mapping "+
          "WHERE New_Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          test = RS.getString(1);
          if (RS.getString(1).startsWith("Y"))
            result = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
  /*****************************************************************************/
  public boolean appendSLCDesc(String accountNumber)
  {
    boolean result = false;
    String test = "";
    SQL = "SELECT Update_CDRS FROM SLCD_Account "+
          "WHERE Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          test = RS.getString(1);
          if (RS.getString(1).startsWith("Y"))
            result = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getAttachments(String type, String noMonths, String billingSource, String locationType)
  {
    java.sql.ResultSet aRS = null;
    if (type.startsWith("Daily"))
    {
      SQL =
        "SELECT * FROM ATTACHMENT "+
        "WHERE Type = 'account' "+
        "AND Item_Type = 'cdr' "+
        "AND Id IN ( SELECT Account_Id FROM Account WHERE Billing_Source = '"+billingSource+"') "+
        "AND Attachment_Date < ADD_MONTHS( SYSDATE, -"+noMonths+" ) ";
      if (locationType.equals("archive"))
        SQL = SQL + "AND Location LIKE 'archive%'";
      else
        SQL = SQL + "AND Location NOT LIKE 'archive%'";
    }
    else
    {
      SQL =
        "SELECT * FROM ATTACHMENT "+
        "WHERE Type = 'invoice' "+
        "AND Item_Type = 'cdr' "+
        "AND Id IN ( SELECT Invoice_Id FROM Invoice, Account WHERE Billing_Source = '"+billingSource+"' "+
        "AND Invoice.Account_Id = Account.Account_Id ) "+
        "AND Attachment_Date < ADD_MONTHS( SYSDATE, -"+noMonths+" ) ";
      if (locationType.equals("archive"))
        SQL = SQL + "AND Location LIKE 'archive%'";
      else
        SQL = SQL + "AND Location NOT LIKE 'archive%'";
    }
    //System.out.println(SQL);
    if (Connect(READ,P5))
      aRS = RS;
    return aRS;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getAccountSLCDs(String accountNumber)
  {
    java.sql.ResultSet aRS = null;
    SQL = "SELECT * FROM Account_Service_Location_Code "+
          "WHERE Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
      aRS = RS;
    return aRS;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getInvoiceAttachments()
  {
    java.sql.ResultSet aRS = null;
    SQL =
      "SELECT i.invoice_id, a.account_number, a.billing_source, "+
      "REPLACE(at.location,'/','\\') location, location original_location, "+
      "NVL( Item_Type, 'NOTREPORT' ) Item_Type, Name "+
      "FROM invoice i, account a, attachment at "+
      "WHERE i.remove_ind = 'Y' "+
      "AND i.account_id = a.account_id "+
      "AND at.id = i.invoice_id "+
      "AND at.type = 'invoice'";
    if (Connect(READ,P5))
      aRS = RS;
    return aRS;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getInvoiceAttachments(long invoiceId)
  {
    java.sql.ResultSet aRS = null;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Get_Invoice_Attachments (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,OracleTypes.CURSOR);
        cstmt.setLong(2,invoiceId);
        cstmt.execute();
        aRS = (java.sql.ResultSet)cstmt.getObject(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      return aRS;
    }
  }
  /*****************************************************************************/
  public void updateEbanUserAudit(String loginId)
  {
    java.sql.ResultSet aRS = null;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{call eBAN..Update_eBan_User_Audit (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,loginId);
        cstmt.execute();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
  }
  /*****************************************************************************/
  public boolean checkEbanUserAudit(String loginId)
  {
    boolean active = false;  
    java.sql.ResultSet aRS = null;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{call eBAN..Check_eBan_User_Audit (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,loginId);
        cstmt.execute();
        aRS = cstmt.getResultSet();
        if (aRS.next()) {
            active = aRS.getString(1).equalsIgnoreCase("N");
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    } finally {
        return active;
    }
    //message set in underlying code
  }
  /*****************************************************************************/
  public java.sql.ResultSet getNonGCBCandidateInvoices(int noMonths)
  {
    java.sql.ResultSet aRS = null;
    SQL =
      "SELECT i.invoice_id, a.account_number, a.Billing_Source "+
      "FROM invoice i, account a "+
      "WHERE i.remove_ind <> 'Y' "+
      "AND i.account_id = a.account_id "+
      "AND a.Billing_Source IN('SSBS','ebillz','BASA','VBS','Arbor','MOBI') "+
      "AND i.Tax_Point_Date < ADD_MONTHS( SYSDATE, -"+noMonths+")";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      aRS = RS;
    else
      System.out.println("getNonGCBCandidateInvoices("+noMonths+") : "+Message);
    return aRS;
  }
  /*****************************************************************************/
  public boolean deleteAttachment(String location)
  {
    boolean result = false;
    String returnV = "";
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Delete_Attachment (?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
        cstmt.setString(2,location);
        cstmt.execute();
        returnV = cstmt.getString(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      if (returnV.equals("OK"))
        result = true;
      return result;
    }
  }
  /*****************************************************************************/
  public boolean removeInvoice(long invoiceId)
  {
    boolean result = false;
    String returnV = "";
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Remove_Invoice (?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
        cstmt.setLong(2,invoiceId);
        cstmt.execute();
        returnV = cstmt.getString(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      if (returnV.equals("OK"))
        result = true;
      return result;
    }
  }
/*****************************************************************************/
  public boolean BVreportSSBSCDR(String accountNumber)
  {
    boolean result = false;
    String test = "";
    SQL = "SELECT BV_report FROM Dataflow_Conversion_Mapping "+
          "WHERE New_Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          test = RS.getString(1);
          if (RS.getString(1).startsWith("Y"))
            result = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public boolean kpi2Account(String accountNumber)
  {
    boolean result = false;
    int count = 0;
    SQL = "SELECT COUNT(*) FROM KPI2_Account "+
          "WHERE Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          count = RS.getInt(1);
          if (count>0)
            result = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public String spiritTypeSSBSCDR(String accountNumber)
  {
    String result = "";
    SQL = "SELECT Spirit_Account FROM Dataflow_Conversion_Mapping "+
          "WHERE New_Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          result = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public int ftpRequestsCount()
  {
    int size = 0;
    SQL = "SELECT COUNT(*) FROM Dataflow_Input_File "+
          "WHERE FTP_Status IN ('Request','Request Failed')";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
          size = RS.getInt(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return size;
  }
/*****************************************************************************/
  public String[][] ftpRequests(int listSize)
  {
    String[][] requests = new String[listSize][2];
    int i = 0;
    SQL = "SELECT Filename, SSBS_Original_Filename FROM Dataflow_Input_File "+
          "WHERE FTP_Status IN ('Request','Request Failed')";
    if (Connect(READ,P5))
      try
      {
        while (RS.next())
        {
          requests[i][0] = RS.getString(1);
          requests[i][1] = RS.getString(2);
          i++;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    return requests;
  }
/*****************************************************************************/
  public int getSSBSDownloadListSize()
  {
    int size = 0;
    SQL = "SELECT COUNT(*) FROM Dataflow_Input_File "+
          "WHERE FTP_Status IN ('Identified','Failed')";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
          size = RS.getInt(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return size;
  }
/*****************************************************************************/
  public String[] getSSBSDownloadList(int listSize)
  {
    String[] downloadList = new String[listSize];
    int i = 0;
    SQL = "SELECT Filename FROM Dataflow_Input_File "+
          "WHERE FTP_Status IN ('Identified','Failed')";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          downloadList[i] = RS.getString(1);
          i++;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return downloadList;
  }
/*****************************************************************************/
  public boolean updateFTPStatus(String filename, String newStatus)
  {
    boolean success = false;
    SQL = "UPDATE Dataflow_Input_File "+
          "SET FTP_Status = '"+newStatus+"' "+
          "WHERE Filename = '"+filename+"'";
    if (Connect(NORESULT,P5))
    {
      success = true;
    }
    return success;
  }
/*****************************************************************************/
  public String[] getCircuitEnds(String gsr)
  {
    String[] ends = new String[2];
    SQL="select From_End_Code + '/' + From_End, To_End_Code + '/' + To_End " +
      "from gcd..Global_Customer_Billing (nolock) " +
      "where Service_Reference = '" + gsr + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
        {
          ends[0] = RS.getString(1);
          ends[1] = RS.getString(2);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return ends;
  }
/*****************************************************************************/
  public String[] getConglomSourceDetails(long conglomCustId,
    String billedProductId)
  {
    String[] details = new String[3];
    SQL="select Feed_Source, Source_Conglom_Id, Source_System_Id " +
      "from Conglomerate..View_Cust_Products (nolock) " +
      "where Conglom_Cust_Id = " + conglomCustId + " " +
      "and Billed_Product_Id = '" + billedProductId + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
        {
          details[0] = RS.getString(1);
          details[1] = RS.getString(2);
          details[2] = RS.getString(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return details;
  }
/*****************************************************************************/
  public String[] getSpiritInvoice(String accountNo, String billingDate)
  {
    String[] inv = new String[2];
    SQL="select Invoice_Id, Invoice_No " +
      "FROM Invoice " +
      "WHERE Invoice_No = " +
      "(SELECT Invoice_Number " +
      "FROM BV_Control " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND File_Name LIKE 'B%" + billingDate + "%Control%') "
    ;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
        {
          inv[0] = String.valueOf(RS.getLong(1));
          inv[1] = RS.getString(2);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return inv;
  }
/*****************************************************************************/
  public long[] getSpiritIds(String accountNo, String billingSource)
  {
    long[] ids = new long[3];
    SQL="select c.Customer_Id, a.Payment_Group_Id, a.Account_Id " +
      "FROM Customer c, Payment_Group p, Account a " +
      "WHERE c.Customer_Id = p.Customer_Id " +
      "AND p.Payment_Group_Id = a.Payment_Group_Id " +
      "AND a.Account_Number = '" + accountNo + "' " +
      "AND a.Billing_Source = '" + billingSource + "' "
    ;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
        {
          ids[0] = RS.getLong(1);
          ids[1] = RS.getLong(2);
          ids[2] = RS.getLong(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return ids;
  }
/*****************************************************************************/
  public boolean validInvoice(String accountId, String invoiceId)
  {
    boolean isValid = false;
    if (validInvoiceP3(accountId, invoiceId))
    {
      isValid = true;
    }
    else if (validInvoiceP4(accountId, invoiceId))
    {
      isValid = true;
    }
    else if (validInvoiceP5(accountId, invoiceId))
    {
      isValid = true;
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean validInvoiceP3(String accountId, String invoiceId)
  {
    boolean isValid = false;
    SQL="select Invoice_Id from givn_ref..EBilling_Invoices_P3 (nolock) " +
      "where Account_Id = '" + accountId + "' " +
      "and Invoice_Id = '" + invoiceId + "' ";

    if (Connect(READ,P3))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean validInvoiceP4(String accountId, String invoiceId)
  {
    boolean isValid = false;
    SQL="select Invoice_Id from givn_ref..EBilling_Invoices_P4 (nolock) " +
      "where Account_Id = '" + accountId + "' " +
      "and Invoice_Id = '" + invoiceId + "' ";

    if (Connect(READ,P4))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean validInvoiceP5(String accountId, String invoiceId)
  {
    boolean isValid = false;
    SQL="select Invoice_Number from oss..CW_Invoice_Archive (nolock) " +
      "where Account_Id = '" + accountId + "' " +
      "and Invoice_Number = '" + invoiceId + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean validAction(String action, String status)
  {
    boolean isValid = false;
    SQL="SELECT Action FROM eBAN..BAN_List (nolock) " +
      "WHERE Action = '" + action + "' " +
      "AND BAN_Status_Code = '" + status + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        isValid = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean cdcsRequired(Collection siteSplits)
  {
    boolean isValid = false;
    StringBuffer sb = new StringBuffer();

    for (Iterator it = siteSplits.iterator(); it.hasNext(); )
    {
      SiteSplitDescriptor ssd = (SiteSplitDescriptor)it.next();
      sb.append("'"+ssd.getSiteId()+"',");
    }
    sb.deleteCharAt(sb.length()-1);
    SQL = "SELECT COUNT(*) " +
      "FROM GIVN_REF..Tax_Requirement tr (nolock), GIVN_REF..Site s (nolock) " +
      "WHERE s.Billing_Region = tr.Billing_Region " +
      "AND s.Global_Customer_Id = tr.Global_Customer_Id " +
      "AND tr.Tax_Type = 'VTX' " +
      "AND s.Site_Id IN (" + sb.toString() + ") ";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            isValid = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean cdcsRequired(String siteId, String siteId2, String siteId3)
  {
    boolean isValid = false;
    SQL = "SELECT COUNT(*) " +
      "FROM GIVN_REF..Tax_Requirement tr (nolock), GIVN_REF..Site s (nolock) " +
      "WHERE s.Billing_Region = tr.Billing_Region " +
      "AND s.Global_Customer_Id = tr.Global_Customer_Id " +
      "AND tr.Tax_Type = 'VTX' " +
      "AND s.Site_Id IN ('" + siteId + "','" + siteId2 + "','" + siteId3 + "') ";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            isValid = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return isValid;
  }
/*****************************************************************************/
  public boolean chargeBilled(int chargeId, String chargeTypeCode, boolean trial)
  {
    boolean chargeBilled = false;
    SQL = "SELECT COUNT(*) " +
      "FROM gcd..Monthly_Billing" + (trial?"":"_Archive") + " (nolock) " +
      "WHERE Charge_Type_Code = '" + chargeTypeCode + "' " +
      "AND Charge_Id = " + Integer.toString(chargeId) +
      (trial?"":" UNION SELECT COUNT(*) " +
      "FROM gcd..Monthly_Billing_Archive_Pre2004 (nolock) " +
      "WHERE Charge_Type_Code = '" + chargeTypeCode + "' " +
      "AND Charge_Id = " + Integer.toString(chargeId));

    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            chargeBilled = true;
            break;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return chargeBilled;
  }
/*****************************************************************************/
  public boolean siteValid(String siteId)
  {
    boolean siteValid = true;
    SQL = "SELECT Site_Id " +
      "FROM givn_ref..Site (nolock) " +
      "WHERE Site_Id = '" +siteId + "' " +
      "AND (Billing_Region IS NULL " +
      "OR Billing_Region = 'tba' " +
      "OR LTRIM(RTRIM(Billing_Region)) = '') "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          siteValid = false;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return siteValid;
  }
/*****************************************************************************/
  public boolean customerHasCharges(String gcId)
  {
    boolean customerHasCharges = false;
    SQL = "SELECT Charge_Id " +
      "FROM gcd..Charge (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND To_Charge_Valid_Date IS NULL "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          customerHasCharges = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        customerHasCharges = true;
        Close();
      }
    }
    return customerHasCharges;
  }
/*****************************************************************************/
  public boolean customerHasSingleCharges(String gcId, String chargeTypeCode)
  {
    boolean customerHasSingleCharges = false;
    SQL = "SELECT Charge_Id " +
      "FROM gcd..Single_Charge (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND Charge_Type_Code = '" + chargeTypeCode + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          customerHasSingleCharges = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        customerHasSingleCharges = true;
        Close();
      }
    }
    return customerHasSingleCharges;
  }
/*****************************************************************************/
  public String customerHasTrialBill(String gcId)
  {
    StringBuffer sb = new StringBuffer();
    SQL = "SELECT DISTINCT Invoice_No " +
      "FROM gcd..Monthly_Billing (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          sb.append(RS.getString(1) + ",");
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        return "error";
      }
    }
    if (sb.length() > 0)
    {
      return sb.toString();
    }
    else
    {
      return "none";
    }
  }
/*****************************************************************************/
  public boolean customerHasAccounts(String gcId)
  {
    boolean customerHasAccounts = false;
    SQL = "SELECT Invoice_Region " +
      "FROM givn_ref..Invoice_Region (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "UNION SELECT Invoice_Region " +
      "FROM eban..Invoice_Region_BAN (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          customerHasAccounts = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return customerHasAccounts;
  }
/*****************************************************************************/
  public boolean invoiceRegionRefDataExists(String outgoingCurrencyCode, String companyAddressId)
  {
    boolean invoiceRegionRefDataExists = false;
    SQL = "SELECT Company_Address_Id " +
      "FROM givn_ref..Invoice_Region_Reference_Data (nolock) " +
      "WHERE Company_Address_Id = '" + companyAddressId + "' " +
      "AND Outgoing_Currency_Code = '" + outgoingCurrencyCode + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          invoiceRegionRefDataExists = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return invoiceRegionRefDataExists;
  }
/*****************************************************************************/
  public boolean invoiceRegionExists(String gcId, String invRegion)
  {
    boolean invoiceRegionExists = false;
    SQL = "SELECT Invoice_Region " +
      "FROM givn_ref..Invoice_Region (nolock) " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND Invoice_Region = '" + invRegion + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          invoiceRegionExists = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return invoiceRegionExists;
  }
/*****************************************************************************/
  public boolean toDateNullable(int chargeId, String gcId)
  {
    boolean toDateNullable = false;
    if (!chargeBilled(chargeId, "01", false)) //&&
      //(!chargeBilled(chargeId, chargeTypeCode, true)))
    {
      toDateNullable = true;
    }
    else
    {
      SQL = "SELECT bpsd_orig " +
        "FROM gcd..Monthly_Billing_Archive (nolock) " +
        "WHERE Charge_Id = " + Integer.toString(chargeId) + " " +
        "AND bpsd_orig IN (SELECT MAX(bpsd_orig) " +
        "FROM gcd..Monthly_Billing_Archive (nolock) " +
        "WHERE Global_Customer_Id = '" + gcId + "' " +
        "AND not Service_Reference = 'dummy') "
        ;

      if (Connect(READ,P5))
      {
        try
        {
          if (RS.next())
          {
            if (RS.getDate(1) != null)
            {
              toDateNullable = true;
            }
          }
          Close();
        }
        catch(java.sql.SQLException se)
        {
          Close();
        }
      }
    }
    return toDateNullable;
  }
/*****************************************************************************/
  public boolean trialDelete(int chargeId, String chargeTypeCode)
  {
    boolean trialDelete = false;
    SQL = "SELECT Charge_Id " +
      "FROM gcd..Monthly_Billing (nolock) " +
      "WHERE Charge_Id = " + Integer.toString(chargeId) + " " +
      "AND Charge_Type_Code = '" + chargeTypeCode + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          trialDelete = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return trialDelete;
  }
/*****************************************************************************/
  public String getNotApplicable(String type)
  {
    String na = "Z";
    String table = "Revenue_" + (type.equals("Description")?"Reason":type);
    SQL = "SELECT " + table + "_Code " +
      "FROM gcd.." + table + " (nolock) " +
      "WHERE Revenue_" + table +
      (table.equals("Revenue_Reason")?"_Description":"") + " = 'Not Applicable' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          na = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return na;
  }
/*****************************************************************************/
  public boolean updateRunControlStatus ( String billingSource,String processName, String status)
  {
    boolean success = false;
    SQL = "UPDATE Run_Control "+
          "SET Run_Status = '"+status+"' "+
          "WHERE Billing_Source = '"+billingSource+"' "+
          "AND Process_Name = '"+processName+"'";
    if (Connect(NORESULT,P5))
    {
      success = true;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public String getRunControlStatus ( String billingSource,String processName)
  {
    String status = "Not available";
    SQL = "SELECT Run_Status "+
          "FROM Run_Control "+
          "WHERE Billing_Source = '"+billingSource+"' "+
          "AND Process_Name = '"+processName+"'";
    //System.out.println(SQL);
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          status = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println("Error: "+se.getMessage());
        Close();
      }
    }
    return status;
  }
/*****************************************************************************/
  public boolean processAlreadyRunning(String billingSource, String process)
  {
    boolean running = false;
    SQL = "SELECT Run_Status " +
      "FROM Run_Control " +
      "WHERE UPPER(Billing_Source) = '" + billingSource.toUpperCase() + "' " +
      "AND UPPER(Process_Name) LIKE '%" + process.toUpperCase() + "%' " +
      "AND Run_Status = 'Active' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        running = RS.next();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
      }
      finally
      {
        Close();
      }
    }
    return running;
  }
/*************************************************************************************/
  public String updateRunControl(String processName, String runStatus,
    String billingSource)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Update_Run_Control (?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,processName.toUpperCase());
        cstmt.setString(3,runStatus);
        cstmt.setString(4,billingSource.toUpperCase());
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId <=0)
        {
          returnMessage = "Unexpected return code from " +
            "Update_Run_Control: " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*****************************************************************************/
  public Collection getDataflowArchiveFiles(int daysOld)
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT Dataflow_Input_File_Id, Dataflow_Account_Control_Id, " +
      "Dataflow_Output_File_Id, Location, Account_Number " +
      "FROM Attachment_Archive_View " +
      "WHERE Attachment_Date < SYSDATE - " + Integer.toString(daysOld)
      ;
    /*SQL = "SELECT c.Dataflow_Input_File_Id, c.Dataflow_Account_Control_Id, " +
      "c.Dataflow_Output_File_Id, a.Location, x.Account_Number " +
      "FROM Attachment a, Dataflow_Account_Control c, Account x " +
      "WHERE UPPER(a.Type) = 'ACCOUNT' " +
      "AND UPPER(a.Download_Type) = 'BATCH' " +
      "AND UPPER(a.item_Type) = 'CDR' " +
      "AND a.Attachment_Date < SYSDATE - " + Integer.toString(daysOld) + " " +
      "AND a.File_Id = c.Dataflow_Output_File_Id " +
      "AND a.Id = x.Account_Id "
      ;
      SQL = "SELECT Dataflow_Input_File_Id, Dataflow_Account_Control_Id, " +
      "Dataflow_Output_File_Id, Location, Account_Number " +
      "FROM Dataflow_Delete_View " +
      "WHERE Attachment_Date < SYSDATE - " + Integer.toString(daysOld) + " "
      ;*/

    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          al.add(new DataflowArchiveFileDescriptor(RS.getLong(1), RS.getLong(2),
            RS.getLong(3), RS.getString(4), RS.getString(5)));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return al;
  }
/*****************************************************************************/
  public Collection getGivnRefLogonFromAD(String adLogonId)
  {
    ArrayList al = new ArrayList();
    String result = "Failed";
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{call GIVN_REF..Get_Givn_Ref_Logon_From_AD (?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,adLogonId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        while (RS.next())
        {
            al.add(new GivnRefUser(RS.getString(1), RS.getString(2)));
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return al;
    }
  }
/*****************************************************************************/
  public Collection getGSORejectDetail(int rowId)
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT CASE WHEN Billing_Start_Date IS NULL THEN '&nbsp;' " +
      "ELSE CONVERT(varchar(11),Billing_Start_Date,106) END, " +
      "CASE WHEN SO_Ref IS NULL THEN '&nbsp;' ELSE SO_Ref END, " +
      "CASE WHEN GSO_Product_Code IS NULL THEN '&nbsp;' ELSE GSO_Product_Code END, " +
      "CASE WHEN Macspec1 IS NULL THEN '&nbsp;' ELSE Macspec1 END, " +
      "CASE WHEN Charge_Type IS NULL THEN '&nbsp;' ELSE CONVERT(varchar(20),Charge_Type) END, " +
      "ISNULL(CONVERT(varchar, Charge),'&nbsp;'), " +
      "ISNULL(Charge_Currency,'&nbsp;'), " +
      "ISNULL(End_Customer_Name,'&nbsp;'), " +
      "ISNULL(CONVERT(char(11),Service_Start_Date,106),'&nbsp;'), " +
      "ISNULL(Macjobnumber,'&nbsp;'), " +
      "ISNULL(Macactivitycode,'&nbsp;'), " +
      "ISNULL(Macspec2,'&nbsp;'), " +
      "ISNULL(Macponumber,'&nbsp;'), " +
      "ISNULL(A_End_Address,'&nbsp;'), " +
      "ISNULL(A_End_Desc,'&nbsp;') " +
      "from gso..si_rejects (nolock) where Row_Id = " + rowId + " ";

    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          for (int i=1; i<16; i++)
          {
            al.add(RS.getString(i));
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return al;
  }
/*****************************************************************************/
  public Collection getDataflowMonthlyArchiveFiles(int monthsOld, String archivePrefix)
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT Location " +
      "FROM Attachment " +
      "WHERE UPPER(Type) = 'INVOICE' " +
      "AND UPPER(Download_Type) = 'BATCH' " +
      //"AND UPPER(item_Type) = 'CDR' " +
      "AND MONTHS_BETWEEN(SYSDATE, Attachment_Date) > " + Integer.toString(monthsOld) + " " +
      "AND Location NOT LIKE '" + archivePrefix + "%' "
      ;

    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          al.add(RS.getString(1).replace('/', File.separatorChar));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return al;
  }
/*****************************************************************************/
  public Collection getDataflowMonthlyArchiveFilesSP(int monthsOld, String archivePrefix)
  {
    ArrayList al = new ArrayList();
    CallableStatement cstmt = null;

    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Get_Monthly_Attachment_Archive(?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.setInt(2,monthsOld);
        cstmt.setString(3,archivePrefix + '%');
        cstmt.execute();
        ResultSet rs = (ResultSet)cstmt.getObject(1);
        while (rs.next())
        {
          al.add(rs.getString(1).replace('/', File.separatorChar));
        }
      }
    }
    catch(Exception ex)
    {
        Message = "Error: " + ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
	    return al;
    }
  }
/*****************************************************************************/
  public boolean updateDataflowMonthlyForArchive(int monthsOld, String archivePrefix)
  {
    boolean success = false;
    SQL = "UPDATE Attachment " +
      "SET Location = '" + archivePrefix + "'||Location  " +
      "WHERE UPPER(Type) = 'INVOICE' " +
      "AND UPPER(Download_Type) = 'BATCH' " +
      //"AND UPPER(item_Type) = 'CDR' " +
      "AND MONTHS_BETWEEN(SYSDATE, Attachment_Date) > " + Integer.toString(monthsOld) + " " +
      "AND Location NOT LIKE '" + archivePrefix + "%' "
      ;
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateDataflowMonthlyForArchiveSingle(String location,
    String archivePrefix)
  {
    boolean success = false;
    SQL = "UPDATE Attachment " +
      "SET Location = '" + archivePrefix + location + "' " +
      "WHERE Location = '" + location + "' "
      ;
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public String getDataflowBandDescription(String id, String source)
  {
    String desc = "unknown";
    SQL = "SELECT Destination " +
      "FROM Dataflow_Band_Destination " +
      "WHERE Charge_Band = '" + id + "' " +
      "AND Source = '" + source + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          desc = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        desc = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      desc = "Error: " + Message;
    return desc;
  }
/*****************************************************************************/
  public long getDataflowInputFileId(String dataFileName, String controlFilename)
  {
    long id = -1;
    SQL = "SELECT Dataflow_Input_File_Id " +
      "FROM Dataflow_Input_File " +
      "WHERE Filename = '" + dataFileName + "' " +
      "AND Control_Filename = '" + controlFilename + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          id = RS.getLong(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    return id;
  }
/*****************************************************************************/
  public boolean existsDataflowInputFileRecord(String dataFileName,
    String controlFilename)
  {
    boolean exists = false;
    SQL = "SELECT Dataflow_Input_File_Id " +
      "FROM Dataflow_Input_File " +
      "WHERE Filename = '" + dataFileName + "' " +
      "AND Control_Filename = '" + controlFilename + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          exists = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    return exists;
  }
/*************************************************************************************/
  public long createDataflowInputRecord(String fileName, java.sql.Date billingDate,
    String controlFileName, String lastMaintainedBy, long cdrCount,
    long durationMins, long durationSecs)
  {
    CallableStatement cstmt = null;
    long dfInputFileId = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Dataflow_Input_File (?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,fileName);
        cstmt.setDate(3,billingDate);
        cstmt.setString(4,controlFileName);
        cstmt.setString(5,lastMaintainedBy);
        cstmt.setLong(6,cdrCount);
        cstmt.setLong(7,durationMins);
        cstmt.setLong(8,durationSecs);
        cstmt.execute();
        dfInputFileId = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return dfInputFileId;
    }
  }
/*************************************************************************************/
  public String updateDataflowInputRecord(long dfInputFileId,
    java.sql.Date billingDate, String successInd, String failureMessage,
    long cdrCount, long durationMins, long durationSecs, long totalCost,
    String lastMaintainedBy)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Update_Dataflow_Input_File (?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,dfInputFileId);
        cstmt.setString(3,successInd);
        cstmt.setString(4,failureMessage);
        cstmt.setLong(5,cdrCount);
        cstmt.setLong(6,durationMins);
        cstmt.setLong(7,durationSecs);
        cstmt.setLong(8,totalCost);
        cstmt.setDate(9,billingDate);
        cstmt.setString(10,lastMaintainedBy);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId <=0)
        {
          returnMessage = "Unexpected return code from " +
            "Update_Dataflow_Input_File: " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*************************************************************************************/
  public String deleteDataflowRecords(long[] inputFileIds, long[] outputFileIds,
    long[] controlFileIds)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Delete_Dataflow_Records (?,?,?)}";

        ArrayDescriptor descriptor =
          ArrayDescriptor.createDescriptor( "NUM_ARRAY", Conn );
        ARRAY inputIds = new ARRAY( descriptor, Conn, inputFileIds );
        ARRAY outputIds = new ARRAY( descriptor, Conn, outputFileIds );
        ARRAY controlIds = new ARRAY( descriptor, Conn, controlFileIds );

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setArray(2, inputIds);
        cstmt.setArray(3, outputIds);
        cstmt.setArray(4, controlIds);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId != 1)
        {
          returnMessage = "Unexpected return code from " +
            "Delete_Dataflow_Records(): " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*************************************************************************************/
  public String deleteDataflowRecordsSingle(long inputFileId, long outputFileId,
    long controlFileId)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Delete_Dataflow_Records_Single (?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2, inputFileId);
        cstmt.setLong(3, outputFileId);
        cstmt.setLong(4, controlFileId);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId != 1)
        {
          returnMessage = "Unexpected return code from " +
            "Delete_Dataflow_Records(): " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*************************************************************************************/
  public String deleteDataflowInputRecord(long dfInputFileId)
  {
    String returnMessage = null;
    SQL = "DELETE  " +
      "FROM Dataflow_Input_File " +
      "WHERE Dataflow_Input_File_Id = " + Long.toString(dfInputFileId) + " "
      ;

    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;
  }
/*************************************************************************************/
  public String deleteDataflowInputRecords(String dfInputFileIds)
  {
    String returnMessage = null;
    SQL = "DELETE  " +
      "FROM Dataflow_Input_File " +
      "WHERE Dataflow_Input_File_Id IN " + dfInputFileIds + " "
      ;

    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;
  }
/*************************************************************************************/
  public String deleteDataflowOutputRecords(String dfOutputFileIds)
  {
    String returnMessage = null;
    SQL = "DELETE  " +
      "FROM Dataflow_Output_File " +
      "WHERE Dataflow_Output_File_Id IN " + dfOutputFileIds + " "
      ;

    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;
  }
/*************************************************************************************/
  public String deleteDataflowControlRecords(String dfControlFileIds)
  {
    String returnMessage = null;
    SQL = "DELETE  " +
      "FROM Dataflow_Account_Control " +
      "WHERE Dataflow_Account_Control_Id IN " + dfControlFileIds + " "
      ;

    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;
  }
/*************************************************************************************/
  public String deleteDataflowAttachmentRecords(String dfAttachmentFileIds)
  {
    String returnMessage = null;
    SQL = "DELETE  " +
      "FROM Attachment " +
      "WHERE File_Id IN " + dfAttachmentFileIds + " "
      ;

    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;
  }
/*************************************************************************************/
  public String updateDataflowOutputRecord(long dfOutputFileId,
    String successInd, String failureMessage, String lastMaintainedBy)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Update_Dataflow_Output_File (?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,dfOutputFileId);
        cstmt.setString(3,successInd);
        cstmt.setString(4,failureMessage);
        cstmt.setString(5,lastMaintainedBy);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId <=0)
        {
          returnMessage = "Unexpected return code from " +
            "Update_Dataflow_Output_File: " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*************************************************************************************/
  public long createDataflowOutputRecord(String fileName,
    java.sql.Date billingDate, long cdrCount, long durationMins,
    long durationSecs, double totalCost, String lastMaintainedBy,
    String successInd, String failureMessage)
  {
    CallableStatement cstmt = null;
    long dfOutputFileId = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Dataflow_Output_File (?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,fileName);
        cstmt.setDate(3,billingDate);
        cstmt.setLong(4,cdrCount);
        cstmt.setLong(5,durationMins);
        cstmt.setLong(6,durationSecs);
        cstmt.setDouble(7,totalCost);
        cstmt.setString(8,lastMaintainedBy);
        cstmt.setString(9,successInd);
        cstmt.setString(10,failureMessage);
        cstmt.execute();
        dfOutputFileId = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return dfOutputFileId;
    }
  }
/*************************************************************************************/
  public long createDataflowAccountControl(long dfInputFileId,
    /*long dfOutputFileId,*/ long cdrCount, long durationMins, long durationSecs,
    double totalCost, String accountNumber, String lastMaintainedBy)
  {
    CallableStatement cstmt = null;
    long dfControlId = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Dataflow_Acc_Control (?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        //cstmt.setLong(2,dfInputFileId);
        //cstmt.setLong(3,dfOutputFileId);
        cstmt.setLong(2,cdrCount);
        cstmt.setLong(3,durationMins);
        cstmt.setLong(4,durationSecs);
        cstmt.setDouble(5,totalCost);
        cstmt.setString(6,accountNumber);
        cstmt.setString(7,lastMaintainedBy);
        cstmt.setLong(8,dfInputFileId);
        cstmt.execute();
        dfControlId = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return dfControlId;
    }
  }
/*************************************************************************************/
  public String updateDataflowAccountControl(long dfControlId, long dfIntputFileId,
    long dfOutputFileId, long cdrCount, long durationMins, long durationSecs,
    double totalCost, String lastMaintainedBy)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Update_Dataflow_Acc_Control (?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,dfControlId);
        cstmt.setLong(3,dfIntputFileId);
        cstmt.setLong(4,dfOutputFileId);
        cstmt.setLong(5,cdrCount);
        cstmt.setLong(6,durationMins);
        cstmt.setLong(7,durationSecs);
        cstmt.setDouble(8,totalCost);
        cstmt.setString(9,lastMaintainedBy);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId <=0)
        {
          returnMessage = "Unexpected return code from " +
            "Update_Dataflow_Acc_Control: " + rowId;
        }
      }
      else
      {
        returnMessage = Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*****************************************************************************/
  public DataflowInputFileDescriptor getDataflowControlRecord(String accountNo,
    String billingDate, String inputFileName)
  {
    DataflowInputFileDescriptor difd = null;
    String dateClause = billingDate==null?"AND i.Billing_Date IS NULL ":
      ("AND i.Billing_Date = TO_DATE('" + billingDate + "', 'YYYYMMDD') ");
    SQL = "SELECT a.Dataflow_Account_Control_Id, a.Dataflow_Input_File_Id, " +
      "i.Billing_Date, a.CDR_Count, a.Duration_Minutes, a.Duration_Seconds, " +
      "a.Total_Cost, i.Control_Filename " +
      "FROM Dataflow_Account_Control a, Dataflow_Input_File i " +
      "WHERE a.Account_Number = '" + accountNo + "' " +
      "AND a.Dataflow_Input_File_Id = i.Dataflow_Input_File_Id " +
      dateClause +
      "AND UPPER(i.Filename) = UPPER('" + inputFileName + "') "
      ;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          difd = new DataflowInputFileDescriptor(RS.getLong(1), RS.getLong(2),
            RS.getDate(3), accountNo, RS.getLong(4), RS.getLong(5), RS.getLong(6),
            RS.getFloat(7), RS.getString(8));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return difd;
  }
/*****************************************************************************/
/*  public DataflowInputFileDescriptor getDataflowControlRecord(String fileName,
    String accountNo)
  {
    DataflowInputFileDescriptor difd = null;
    SQL = "SELECT Dataflow_Input_File_Id, Billing_Date, CDR_Count, " +
      "Duration_Minutes, Duration_Seconds, Total_Cost " +
      "FROM Dataflow_Input_File " +
      "WHERE UPPER(Filename) = '" + fileName.toUpperCase() + "' " +
      "AND Account_Number = '" + accountNo + "' "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          difd = new DataflowInputFileDescriptor(RS.getLong(1), RS.getDate(2),
            accountNo, RS.getLong(3), RS.getLong(4), RS.getLong(5),
            RS.getFloat(6));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return difd;
  }*/
/*************************************************************************************/
  public String createSummaryMgtRecord(long dfControlId, String summaryType,
    String summaryValue, String lastMaintainedBy, long callCount,
    double cost, long duration)
  {
    CallableStatement cstmt = null;
    String returnMessage = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Summary_Mgt_Breakdown (?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,dfControlId);
        cstmt.setString(3,summaryType);
        cstmt.setString(4,summaryValue);
        cstmt.setLong(5,callCount);
        cstmt.setDouble(6,cost);
        cstmt.setLong(7,duration);
        cstmt.setString(8,lastMaintainedBy);
        cstmt.execute();
        int rowId = cstmt.getInt(1);
        if (rowId <=0)
        {
          returnMessage = "Unexpected return code from " +
            "Create_Summary_Mgt_Breakdown: " + rowId;
        }
      }
      else
      {
        returnMessage=Message;
      }
    }
    catch(Exception ex)
    {
      returnMessage=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return returnMessage;
    }
  }
/*****************************************************************************/
  public boolean oracleTest()
  {
    boolean success = false;
    SQL = "SELECT * FROM DUAL";

    if (Connect(READ,P5))
    {
      try
      {
        success = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getErrorCode());
        Close();
      }
    }
    return success;
  }
/*****************************************************************************/
  public String oracleTest2()
  {
    String result = "not found";
    SQL = "SELECT acco_account_number FROM t_credit_notes where acco_coca_id = 'CB00129'";

    if (Connect(READ,GOLDFISH))
    {
      try
      {
        if (RS.next())
	  result = RS.getString(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getErrorCode());
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public boolean conglomAccountExcluded(long conglomCustId, String billedProductId,
    String discountItemCode, String accountNo)
  {
    boolean exists = false;
    SQL = "SELECT Conglom_Cust_Id " +
      "FROM Conglomerate..Conglom_Disc_Excluded_Acc (nolock) " +
      "WHERE Conglom_Cust_Id = " + conglomCustId + " " +
      "AND Billed_Product_Id = '" + billedProductId + "' " +
      "AND Discount_Item_Code = '" + discountItemCode + "' " +
      "AND Source_Account_No = '" + accountNo + "' ";
    try
    {
      if (Connect(READ,P5))
      {
        exists = RS.next();
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      Close();
    }
    return exists;
  }
/*****************************************************************************/
  public boolean goldfishAccountExists(String accountNo, String sourceSystemId,
    String sourceConglomId)
  {
    boolean found = false;
    SQL = "SELECT acbs_short_name " +
      "FROM t_accounts " +
      "where account_number = '" + accountNo + "' " +
      "and acbs_short_name = '" + sourceSystemId + "' " +
      "and coca_id = '" + sourceConglomId + "' ";

    if (Connect(READ,GOLDFISH))
    {
      try
      {
        found = RS.next();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getErrorCode());
        Close();
      }
    }
    return found;
  }
/*****************************************************************************/
  public boolean insertSSBSCli(String id, String cli, String productCode,
    long duration, long cost, long count, String costCentre)
  {
    boolean success = false;
    SQL = "INSERT INTO SSBS_CLI " +
      "(Id, CLI, Product_Code, Cost_Centre, Duration, Cost, Count) " +
      "VALUES ('" + id + "','" + cli + "','" + productCode + "','" + costCentre +
      "'," + Long.toString(duration) + "," + Long.toString(cost) + "," +
      Long.toString(count) + ") ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean insertCli(String id, String description, String productCode,
    long duration, long cost, long count)
  {
    boolean success = false;
    SQL = "INSERT INTO Dataflow_CLI " +
      "(Id, Description, Product_Code, Duration, Cost, Count) " +
      "VALUES ('" + id + "','" + description + "','" + productCode +
      "'," + Long.toString(duration) + "," + Long.toString(cost) + "," +
      Long.toString(count) + ") ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean insertSpiritDetail(String houseCode, String cli,
    long cost, String source, String accountNo, String billingDate)
  {
    boolean success = false;
    SQL = "INSERT INTO Spirit_Detail " +
      "(House_Code, CLI, Billing_Date, Account_Number, Billing_Source, Cost) " +
      "VALUES ('" + houseCode + "','" + cli + "',TO_DATE('" + billingDate +
      "', 'YYYYMMDD'),'" + accountNo + "','" + source.toUpperCase() + "'," +
      Long.toString(cost) + ") ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean insertArborControl(String accountNo, String invoiceNo,
    String billingDate, String invoiceAmt, String currency, String fileName)
  {
    boolean success = false;
    SQL = "INSERT INTO Arbor_Control " +
      "(Account_Number, Invoice_Number, Billing_Date, Gross_Amount, Currency, " +
      "File_Name, Processed) " +
      "VALUES ('" + accountNo + "','" + invoiceNo + "',TO_DATE('" + billingDate +
      "', 'YYYYMMDD')," + invoiceAmt.substring(0, invoiceAmt.length()-2) + "." +
      invoiceAmt.substring(invoiceAmt.length()-2) + ",'" + currency + "','" +
      fileName + "','0') ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean insertArborControl(String accountNo, String accountName,
    String invoiceNo, String billingDate, String invoiceAmt, String currency,
    String fileName, String format, String controlFileName)
  {
    String decimalAmt = null;
    if (invoiceAmt.length() == 2)
    {
      decimalAmt = "0." + invoiceAmt;
    }
    else if (invoiceAmt.length() == 1)
    {
      decimalAmt = "0.0" + invoiceAmt;
    }
    else
    {
      decimalAmt = invoiceAmt.substring(0, invoiceAmt.length()-2) + "." +
        invoiceAmt.substring(invoiceAmt.length()-2);
    }
    boolean success = false;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Arbor_Control (?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,accountName);
        cstmt.setString(4,invoiceNo.replace('/','-'));
        cstmt.setString(5,billingDate);
        cstmt.setString(6,decimalAmt);
        cstmt.setString(7,currency);
        cstmt.setString(8,fileName);
        cstmt.setString(9,"0");
        cstmt.setString(10,format);
        cstmt.setString(11,controlFileName);
        cstmt.execute();
        if (cstmt.getLong(1) > 0)
        {
          success = true;
        }
        else
        {
          success = false;
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return success;
    }
  }
/*****************************************************************************/
  public boolean insertArborControl(String accountNo, String accountName,
    String invoiceNo, String billingDate, String invoiceAmt, String currency,
    String fileName, String format, String controlFileName, String processed)
  {
    boolean success = false;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Create_Arbor_Control (?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,accountName);
        cstmt.setString(4,invoiceNo.replace('/','-'));
        cstmt.setString(5,billingDate);
        cstmt.setString(6,invoiceAmt);
        cstmt.setString(7,currency);
        cstmt.setString(8,fileName);
        cstmt.setString(9,processed);
        cstmt.setString(10,format);
        cstmt.setString(11,controlFileName);
        cstmt.execute();
        long ret = cstmt.getLong(1);
//System.out.println("billingDate: " + billingDate);
//System.out.println("Create_Arbor_Control ret: " + Long.toString(ret));
        if (ret > 0)
        {
          success = true;
        }
        else
        {
          success = false;
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return success;
    }
  }
/*****************************************************************************/
  public boolean insertArborLoad(String accountNo)
  {
    boolean success = false;
    SQL = "INSERT INTO Arbor_Load " +
      "(Account_Number, Account_Name) " +
      "SELECT DISTINCT Account_Number, Account_Name " +
      "FROM Arbor_Control " +
      "WHERE Account_Number = '" + accountNo + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public String getArborControlFileName()
  {
    return arborControlFileName;
  }
/*****************************************************************************/
  public String checkArborControl (String accountNo, String invoiceNo,
    String billingDate, String invoiceAmt, String currency, String fileName,
    String format, boolean fromHeader)
  {
    arborControlFileName = null;
    String message = "";
    SQL="SELECT Billing_Date, Gross_Amount, Currency, Format, Control_File_Name " +
      "FROM Arbor_Control " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND Invoice_Number = '" + invoiceNo + "' " +
      "AND File_Name = '" + fileName + "' "
      ;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          java.util.Date tableDate = new java.util.Date(RS.getDate(1).getTime());
          double tableAmt = RS.getDouble(2);
          String tableCurrency  = RS.getString(3);
          String tableFormat = RS.getString(4);
          arborControlFileName = RS.getString(5);
          int dd = -1;
          int mm = -1;
          int yyyy = -1;
          if (fromHeader)
          {
            dd = Integer.parseInt(billingDate.substring(6, 8));
            mm = Integer.parseInt(billingDate.substring(4, 6));
            yyyy = Integer.parseInt(billingDate.substring(0, 4));
          }
          else
          {
            String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug",
              "Sep","Oct","Nov","Dec"};
            dd = Integer.parseInt(billingDate.substring(0,
              billingDate.indexOf("-")));
            yyyy = Integer.parseInt(billingDate.substring(billingDate.lastIndexOf("-")+1));
            String mon = billingDate.substring(billingDate.indexOf("-")+1,
              billingDate.lastIndexOf("-"));
            for (int i=0; i<12; i++)
            {
              if (mon.equalsIgnoreCase(months[i]))
              {
                mm = i + 1;
                break;
              }
            }
          }
          GregorianCalendar gc = new GregorianCalendar(yyyy, mm-1, dd);
          java.util.Date inDate = new java.util.Date(gc.getTime().getTime());
          double invAmt = fromHeader?Double.parseDouble(invoiceAmt):
            (Double.parseDouble(invoiceAmt.substring(0,
            invoiceAmt.length()-2) + "." +
            invoiceAmt.substring(invoiceAmt.length()-2)));
          if (inDate.compareTo(tableDate) != 0)
          {
            message = "Header/Control: Date mismatch found for account " + accountNo +
              ", invoice " + invoiceNo + " and file name " + fileName;
          }
          else if (invAmt != tableAmt)
          {
            message = "Header/Control: Total mismatch found for account " + accountNo +
              ", invoice " + invoiceNo + " and file name " + fileName;
          }
          else if (!currency.equals(tableCurrency))
          {
            message = "Header/Control: Currency mismatch found for account " + accountNo +
              ", invoice " + invoiceNo + " and file name " + fileName;
          }
          else if (!format.equals(tableFormat))
          {
            message = "Header/Control: Format mismatch found for account " + accountNo +
              ", invoice " + invoiceNo + " and file name " + fileName;
          }
          else
          {
            message = "ok";
          }
        }
        else
        {
          message = "Header/Control: No control record found for account " + accountNo +
            ", invoice " + invoiceNo + " and file name " + fileName;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        message = "Error getting control record for account " + accountNo +
          ", invoice " + invoiceNo + " and file name " + fileName + " - " +
          se.getErrorCode()+" : "+ se.getMessage();
        System.out.println(message);
        Close();
      }
    }

    return message;
  }
/*****************************************************************************/
  public String checkArborControlStatus(String accountNo, String invoiceNo,
    String fileName) throws Exception

  {
    String message = "";
    String ret = "";
    SQL="SELECT Processed " +
      "FROM Arbor_Control " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND (Invoice_Number = '" + invoiceNo.replace('/','-') + "' " +
      "OR Invoice_Number = '" + invoiceNo + "') " +
      "AND Control_File_Name = '" + fileName + "' "
      ;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          String status = RS.getString(1);
          if ((status.equals("Y")) || (status.equals("X")))
          {
            ret = status;
          }
          else if (status.equals("0"))
          {
            ret = "N";
          }
          else if (status.equals("2"))
          {
            ret = "X";
          }
          else if ((status.equals("3")) || (status.equals("5")))
          {
            ret = "F";
          }
          else //if ((status.equals("1")) || (status.equals("4")))
          {
            ret = "F";
          }
        }
        else
        {
          /*message = "No control record for account " + accountNo +
            ", invoice " + invoiceNo + " and file name " + fileName;
          System.out.println(message);
          throw new Exception(message);*/
          ret = "not found";
        }
      }
      catch(java.sql.SQLException se)
      {
        message = "Error getting control record for account " + accountNo +
          ", invoice " + invoiceNo + " and file name " + fileName + " - " +
          se.getErrorCode()+" : "+ se.getMessage();
        System.out.println(message);
        throw new Exception(message);
      }
      finally
      {
        Close();
      }
    }
    else
    {
      message = "Unable to connect to db - account " + accountNo +
        ", invoice " + invoiceNo + " and file name " + fileName;
      System.out.println(message);
      throw new Exception(message);
    }

    return ret;
  }
/*****************************************************************************/
  public String checkArborControlStatus(String accountNo, String invoiceNo)
    throws Exception

  {
    String message = "";
    String ret = "";
    SQL="SELECT Processed " +
      "FROM Arbor_Control " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND (Invoice_Number = '" + invoiceNo.replace('/','-') + "' " +
      "OR Invoice_Number = '" + invoiceNo.replace('-','/') + "' " +
      "OR Invoice_Number = '" + invoiceNo + "') "
      ;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          ret = RS.getString(1);
          /*String status = RS.getString(1);
          if (status.equals("Y"))
          {
            ret = status;
          }
          else if (status.equals("0"))
          {
            ret = "N";
          }
          else
          {
            ret = "X";
          }*/
        }
        else
        {
          message = "No control record for account " + accountNo +
            " and invoice " + invoiceNo;
          System.out.println(message);
          throw new Exception(message);
        }
      }
      catch(java.sql.SQLException se)
      {
        message = "Error getting control record for account " + accountNo +
          " and invoice " + invoiceNo + " - " +
          se.getErrorCode()+" : "+ se.getMessage();
        System.out.println(message);
        throw new Exception(message);
      }
      finally
      {
        Close();
      }
    }
    else
    {
      message = "Unable to connect to db - account " + accountNo +
        " and invoice " + invoiceNo;
      System.out.println(message);
      throw new Exception(message);
    }

    return ret;
  }
/*****************************************************************************/
  public boolean updateArborControl(String invoiceNo, String processed,
    boolean replace)
  {

    boolean success = false;
    SQL = "UPDATE Arbor_Control " +
      "SET Processed = '" + processed + "'" +
      (replace?(", Invoice_Number = '" + invoiceNo.replace('-','/') + "' "):" ") +
      "WHERE Invoice_Number = '" + invoiceNo + "' "
      ;
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateArborControl(String invoiceNo, String processed,
    boolean replace, String currentStatus)
  {

    boolean success = false;
    String thisStatus = null;
    if (processed.equals("Y"))
    {
      if (currentStatus.equals("0"))
      {
        thisStatus = processed; //all OK
      }
      else if (currentStatus.equals("2"))
      {
        thisStatus = "X"; //no control
      }
      else if (currentStatus.equals("3"))
      {
        thisStatus = processed; //control mismatch has been corrected
      }
      else if (currentStatus.equals("4"))
      {
        thisStatus = "X"; //no control but account now loaded
      }
      else if (currentStatus.equals("5"))
      {
        thisStatus = processed; //control mismatch has been corrected and account loaded
      }
    }
    else //processed.equals("1")
    {
      if ((processed.equals("1")) && (currentStatus.equals("0")))
      {
        thisStatus = processed; // send back as ?
      }
      else if ((processed.equals("1")) && (currentStatus.equals("2")))
      {
        thisStatus = "4"; //no control and account not found, send back as ?
      }
      else if ((processed.equals("1")) && (currentStatus.equals("3")))
      {
        thisStatus = "5"; //control mismatch and account not found, return as F
      }
    }
    SQL = "UPDATE Arbor_Control " +
      "SET Processed = '" + thisStatus + "'" +
      (replace?(", Invoice_Number = '" + invoiceNo.replace('-','/') + "' "):" ") +
      "WHERE Invoice_Number = '" + invoiceNo + "' "
      ;
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateArborControlProcessed(String accountNo, String invoiceNo,
    String fileName, String processed, boolean useCF)
  {

    boolean success = false;
    SQL = "UPDATE Arbor_Control " +
      "SET Processed = '" + processed + "' " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND " + (useCF?"Control_":"") + "File_Name = '" + fileName + "' " +
      "AND (Invoice_Number = '" + invoiceNo.replace('/','-') + "' " +
      "OR Invoice_Number = '" + invoiceNo.replace('-','/') + "' " +
      "OR Invoice_Number = '" + invoiceNo + "') "
      ;
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean insertAttachment(String type, long id, String name,
    String location, String itemType, String downloadType)
  {
    boolean success = false;
    SQL = "INSERT INTO Attachment (Type, Id, Name, Location, Item_Type, " +
      "Download_Type) VALUES ('" +
      type + "'," + id + ",'" + name + "','" + location + "','" + itemType +
      "','" + downloadType + "') ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public Collection getSpiritDetail()
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT House_Code, CLI, Account_Number, " +
      "Billing_Source, ROUND(((Cost/1000) * " + String.valueOf(vatRate) + "), 2)*100, " +
      "ROUND(((Cost/1000) * " + String.valueOf(vatRate-1) + "), 2)*100, Billing_Date " +
      "FROM Spirit_Detail " +
      "ORDER BY Account_Number, House_Code, CLI "
      ;

    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          al.add(new SpiritDescriptor(RS.getString(1), RS.getString(2),
            RS.getString(3), RS.getString(4), RS.getLong(5), RS.getLong(6),
            RS.getDate(7)));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return al;
  }
/*****************************************************************************/
  public boolean deleteSpiritDetail()
  {
    boolean success = false;
    SQL = "DELETE FROM Spirit_Detail ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public long[] getSpiritTotalForAccount(String accountNo,
    String billingSource)
  {
    long[] totals = new long[3];
    SQL = "SELECT COUNT(*)+1, ROUND(((SUM(Cost/1000) * " + String.valueOf(vatRate) + ")),2)*100, " +
      "ROUND(((SUM(Cost/1000) * " + String.valueOf(vatRate-1) + ")),2)*100 " +
      "FROM Spirit_Detail " +
      "WHERE Account_Number = '" + accountNo + "' " +
      "AND Billing_Source = '" + billingSource + "' "
      ;

    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          totals[0] = RS.getLong(1);
          totals[1] = RS.getLong(2);
          totals[2] = RS.getLong(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return totals;
  }
/*****************************************************************************/
  public boolean updateCli(String id, long duration, long cost)
  {
    boolean success = false;
    SQL = "UPDATE Dataflow_CLI " +
      "SET Duration =  Duration + " + Long.toString(duration) + ", " +
      "Cost = Cost + " + Long.toString(cost) + ", " +
      "Count = Count + 1 " +
      "WHERE Id = '" + id + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateSSBSCli(String id, long duration, long cost)
  {
    boolean success = false;
    SQL = "UPDATE SSBS_CLI " +
      "SET Duration = Duration + " + Long.toString(duration) + ", " +
      "Cost = Cost + " + Long.toString(cost) + ", " +
      "Count = Count + 1 " +
      "WHERE Id = '" + id + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateSSBSCli(String id, long duration, long cost, long count)
  {
    boolean success = false;
    SQL = "UPDATE SSBS_CLI " +
      "SET Duration = Duration + " + Long.toString(duration) + ", " +
      "Cost = Cost + " + Long.toString(cost) + ", " +
      "Count = Count + " + Long.toString(count) + " " +
      "WHERE Id = '" + id + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean updateCli(String id, long duration, long cost, long count)
  {
    boolean success = false;
    SQL = "UPDATE Dataflow_CLI " +
      "SET Duration = Duration + " + Long.toString(duration) + ", " +
      "Cost = Cost + " + Long.toString(cost) + ", " +
      "Count = Count + " + Long.toString(count) + " " +
      "WHERE Id = '" + id + "' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean deleteCli()
  {
    boolean success = false;
    SQL = "DELETE FROM Dataflow_CLI ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean deleteEbillzAccountSummaries(String accountNumber, String period)
  {
    boolean success = false;
    SQL = "DELETE FROM Ebillz_Account_Summary "+
          "WHERE Account_Number = '"+accountNumber+"' "+
          "AND Period = '"+period+"' ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public String removeAttachment(String location, long id, String type, long fileId)
  {
    String result = "Failed";
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Remove_Attachment (?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
        cstmt.setString(2,location);
        cstmt.setLong(3,id);
        cstmt.setString(4,type);
        cstmt.setLong(5,fileId);
        cstmt.execute();
        result = cstmt.getString(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      //if (result.equals("Failed"))
        //System.out.println(location+" : "+id+" : "+type+" : "+fileId);
      return result;
    }
  }
/*****************************************************************************/
  public boolean deleteSSBSCli()
  {
    boolean success = false;
    SQL = "DELETE FROM SSBS_CLI ";
    if(Connect(NORESULT,P5))
    {
      success = true;
    }
    else
    {
      success = false;
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean getCli(BufferedWriter bw)
  {
    String DQ = "\"";
    String SQ = "'";
    String DQCOMMADQ = "\",\"";
    boolean success = false;
    SQL = "SELECT Id, Description, Product_Code, Duration, Cost, Count " +
      "FROM Dataflow_CLI " +
      "ORDER BY id "
      ;

    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          String ret = null;
          String temp = String.valueOf(RS.getLong(5));
          int pos = 0;
          int len = temp.length();
          if (len < 4)
          {
            if (len == 1)
              ret = "0.00" + temp;
            else if (len == 2)
              ret = "0.0" + temp;
            else //if (len == 3)
              ret = "0." + temp;
          }
          else
          {
            pos = temp.length() - 3;
            ret = temp.substring(0, pos) + "." + temp.substring(pos);
          }

          bw.write(DQ + SQ + RS.getString(3) + DQCOMMADQ + SQ +
            RS.getString(1).substring(0,12) + DQCOMMADQ +
            Long.toString(RS.getLong(6)) + DQCOMMADQ + ret +
            DQCOMMADQ + Long.toString(RS.getLong(4)) + DQCOMMADQ + RS.getString(2) + DQ);
          bw.newLine();
        }
        Close();
        success = true;
      }
      catch(java.sql.SQLException se)
      {
        System.out.println("SQLException in getCli:"+se.getMessage());
        Close();
      }
      catch(java.io.IOException ioe)
      {
        System.out.println("IOException in getCli:"+ioe.getMessage());
        Close();
      }
    }
    return success;
  }
/*****************************************************************************/
  public boolean getSSBSCli(BufferedWriter bw)
  {
    String SQ = "'";
    String DQCOMMADQ = ",";
    boolean success = false;
    SQL = "SELECT Id, CLI, Product_Code, Duration, Cost, Count, Cost_Centre " +
      "FROM SSBS_CLI " +
      "ORDER BY Id "
      ;
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          String ret = null;
          String temp = String.valueOf(RS.getLong(5)/10);
          int pos = 0;
          int len = temp.length();
          if (len < 4)
          {
            if (len == 1)
              ret = "0.00" + temp;
            else if (len == 2)
              ret = "0.0" + temp;
            else //if (len == 3)
              ret = "0." + temp;
          }
          else
          {
            pos = temp.length() - 3;
            ret = temp.substring(0, pos) + "." + temp.substring(pos);
          }
          //ret = ret.substring(0,ret.length()-1);
          bw.write(RS.getString(3) + DQCOMMADQ + SQ +
            RS.getString(2) + DQCOMMADQ +
            Long.toString(RS.getLong(6)) + DQCOMMADQ + ret + DQCOMMADQ +
              Long.toString(RS.getLong(4)) + DQCOMMADQ + SQ + RS.getString(7));
          bw.newLine();
        }
        Close();
        success = true;
      }
      catch(java.sql.SQLException se)
      {
        System.out.println("SQLException in getCli:"+se.getMessage());
        Close();
      }
      catch(java.io.IOException ioe)
      {
        System.out.println("IOException in getCli:"+ioe.getMessage());
        Close();
      }
    }
    return success;
  }
/*****************************************************************************/
  public boolean checkCli(String id, String productCode)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Dataflow_CLI " +
      "WHERE Id = '" + id + "' " +
      "AND Product_Code = '" + productCode + "' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean checkConglomSourceInvoiceExists(long conglomCustId,
    String billedProduct, String invoiceNo)
  {
    boolean exists = false;
    SQL = "SELECT Source_Invoice_Id " +
      "FROM Conglomerate..Source_Invoice " +
      "WHERE Source_Invoice_No = '" + invoiceNo + "' ";
      /*"WHERE Conglom_Cust_Id = " + conglomCustId + " " +
      "AND Billed_Product_Id = '" + billedProduct + "' " +
      "AND Source_Invoice_No = '" + invoiceNo + "' ";*/
    try
    {
      if (Connect(READ,P5))
      {
        exists = RS.next();
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      Close();
    }
    return exists;
  }
/*****************************************************************************/
  public boolean checkInvoiceClosed(String gcId, String invoiceRegion,
    String invoiceNo)
  {
    boolean exists = false;
    /*SQL = "SELECT Global_Customer_Id " +
      "FROM givn_ref..IRIN_Manual " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND Invoice_Region = '" + invoiceRegion + "' " +
      "AND Invoice_No = '" + invoiceNo + "' " +
      "AND Status = 'Closed' ";*/
    SQL = "SELECT Global_Customer_Id " +
      "FROM gcd..Adjustments_Archive " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND Invoice_Region = '" + invoiceRegion + "' " +
      "AND Invoice_No = '" + invoiceNo + "' ";
    try
    {
      if (Connect(READ,P5))
      {
        exists = RS.next();
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      Close();
    }
    return exists;
  }
/*****************************************************************************/
  public boolean invoiceHasAdjustments(String gcId, String invoiceRegion,
    String invoiceNo)
  {
    boolean exists = false;
    SQL = "SELECT Adjustment_Id " +
      "FROM gcd..Adjustments " +
      "WHERE Global_Customer_Id = '" + gcId + "' " +
      "AND Invoice_Region = '" + invoiceRegion + "' " +
      "AND Invoice_No = '" + invoiceNo + "' " ;
    try
    {
      if (Connect(READ,P5))
      {
        exists = RS.next();
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      Close();
    }
    return exists;
  }
/*****************************************************************************/
  public boolean checkSSBSCli(String id, String productCode)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM SSBS_CLI " +
      "WHERE Id = '" + id + "' " +
      "AND Product_Code = '" + productCode + "' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean invoiceCDRAttachmentExists(int invoiceId, String attachmentDate)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Attachment " +
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'cdr' " +
      "AND Name NOT LIKE('%WLR%') " +
      "AND Attachment_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean invoicePDFAttachmentExists(int invoiceId, String attachmentDate)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Attachment " +
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Location NOT LIKE ('%Other%') " +
      "AND Attachment_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public long accountId(String accountNumber)
  {
    long id = 0;
    SQL =
      "SELECT Account_Id "+
      "FROM Account "+
      "WHERE Account_Number = '" + accountNumber + "'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
          id = RS.getLong(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return id;
  }
/*****************************************************************************/
  public long invoiceId(String invoiceNo)
  {
    long id = 0;
    SQL =
      "SELECT Invoice_Id "+
      "FROM Invoice "+
      "WHERE Invoice_No = '"+invoiceNo + "'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
          id = RS.getLong(1);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return id;
  }
/*****************************************************************************/
  public String cdrFormat(int accountId)
  {
    String format = "Default";
    SQL = "SELECT CDR_Format FROM CDR_Format "+
      "WHERE Account_Id = " + accountId;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          format = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return format;
  }
/*****************************************************************************/
  public boolean invoiceOtherAttachmentExists(int invoiceId, String attachmentDate)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Attachment " +
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Location LIKE ('%Other%') " +
      "AND Attachment_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean invoiceAttachmentExistsWLR(int invoiceId, String attachmentDate)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Attachment " +
      "WHERE Id = " + invoiceId + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'cdr' " +
      "AND Name LIKE('%WLR%') " +
      "AND Attachment_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean accountAttachmentExists(int accountId, String attachmentDate)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Attachment " +
      "WHERE Id = " + accountId + " " +
      "AND Type = 'account' " +
      "AND Item_Type = 'cdr' " +
      "AND Attachment_Date = TO_DATE('" + attachmentDate + "','DD-MON-YY') ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
/*****************************************************************************/
  public boolean endOfMonthTNBS(String filename)
  {
    boolean check = false;
    SQL = "SELECT Control_Filename " +
      "FROM Dataflow_Input_File " +
      "WHERE Filename = '" + filename + "'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          String controlFilename = RS.getString(1);
          String DOB = controlFilename.substring(7,8);
          if (DOB.startsWith("Z"))
            check = true;
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return check;
  }
/*****************************************************************************/
  public boolean accountExists(String accountNo)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
      "FROM Account " +
      "WHERE Account_Number = '" + accountNo + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getInt(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean SSBSAccountExists(String accountNumber)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
          "FROM Account " +
          "WHERE Account_Number = '" + accountNumber + "' "+
          "AND Billing_Source = 'SSBS'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getLong(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
/*****************************************************************************/
  public boolean thusAccount(String accountNumber)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) " +
          "FROM THUS_Account " +
          "WHERE Account_Number = '" + accountNumber + "'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          if (RS.getLong(1) > 0)
          {
            exists = true;
          }
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return exists;
  }
  /*****************************************************************************/
  public long SSBSInvoiceId(String accountNumber, String processingDate)
  {
    long id = 0;
    SQL = "SELECT Invoice_Id " +
          "FROM Invoice, Account " +
          "WHERE Account_Number = '" + accountNumber + "' "+
          "AND Billing_Source = 'SSBS' "+
          "AND Account.Account_Id = Invoice.Account_Id "+
          "AND Invoice_No NOT LIKE '%C' "+ // ignore credit notes  
          "AND TO_CHAR( Invoice.Tax_Point_Date, 'YYYYMM' ) = '"+
            processingDate.substring(0,6)+"'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          id = RS.getLong(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return id;
  }
  /*****************************************************************************/
  public String[] SSBSInvoiceData(long invoiceId)
  {
    String[] invoiceData = new String[3];
    SQL = "SELECT Invoice_No, "+
          "TO_CHAR( Tax_Point_Date, 'DD/MM/YYYY'), " +
          "TO_CHAR( ADD_MONTHS( Tax_Point_Date, 1 ) - 1, 'DD/MM/YYYY') " +
          "FROM Invoice " +
          "WHERE Invoice_Id = " + invoiceId;
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          invoiceData[0] = RS.getString(1);
          invoiceData[1] = RS.getString(2);
          invoiceData[2] = RS.getString(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return invoiceData;
  }
  /*****************************************************************************/
  public String SSBSAccountName(String accountNumber)
  {
    String name = "";
    SQL = "SELECT Account_Name " +
          "FROM Account " +
          "WHERE Account_Number = '" + accountNumber + "' "+
          "AND Billing_Source = 'SSBS' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          name = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return name;
  }
/*****************************************************************************/
  public String SSBSAccountConversionFlag(String accountNumber)
  {
    String result = "N";
    SQL = "SELECT CDR_Format.Energis_Conversion " +
          "FROM Account, CDR_Format " +
          "WHERE Account_Number = '" + accountNumber + "' "+
          "AND Billing_Source = 'SSBS' "+
          "AND Account.Account_Id = CDR_Format.Account_Id";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          result = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return result;
  }
/*****************************************************************************/
  public String[] getBIERInvoiceDetails(String invoiceNo)
  {
    String[] result = new String [5];
    result[0] = "Not found";
    SQL = "SELECT Tax_Point_Date, No_Pages, Total_Amount, Invoice_Start_Page,"+
          " Invoice_End_Page " +
          "FROM BIER_Invoice_Details " +
          "WHERE Invoice_No = '" + invoiceNo + "'";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          result[0] = RS.getString(1);
          result[1] = RS.getString(2);
          result[2] = RS.getString(3);
          result[3] = RS.getString(4);
          result[4] = RS.getString(5);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return result;
  }
/*****************************************************************************/
  public long[] SSBSAccountIds(String accountNo)
  {
    long[] Ids = new long[3];
    Ids[0] = -1;
    SQL = "SELECT Account.Account_Id, "+
      "Account.Payment_Group_Id, "+
      "Payment_Group.Customer_Id " +
      "FROM Account, Payment_Group " +
      "WHERE Account_Number = '" + accountNo + "' "+
      "AND Billing_Source = 'SSBS' "+
      "AND Account.Payment_Group_Id = Payment_Group.Payment_Group_Id";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          Ids[0] = RS.getLong(1);
          Ids[1] = RS.getLong(2);
          Ids[2] = RS.getLong(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return Ids;
  }
/*****************************************************************************/
  public long[] PCBAccountIds(String accountNo)
  {
    long[] Ids = new long[3];
    Ids[0] = -1;
    SQL = "SELECT Account.Account_Id, "+
      "Account.Payment_Group_Id, "+
      "Payment_Group.Customer_Id " +
      "FROM Account, Payment_Group " +
      "WHERE Account_Number = '" + accountNo + "' "+
      "AND Billing_Source = 'PCB' "+
      "AND Account.Payment_Group_Id = Payment_Group.Payment_Group_Id";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          Ids[0] = RS.getLong(1);
          Ids[1] = RS.getLong(2);
          Ids[2] = RS.getLong(3);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
        System.out.println(se.getErrorCode()+" : "+se.getMessage());
      }
    }
    return Ids;
  }
/*****************************************************************************/
  public boolean SSBSAttachment
  (String location, long id, String attachmentDate, String name)
  {
    boolean ok = false;
    SQL = "DELETE  " +
      "FROM Attachment " +
      "WHERE Id = " + id + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Name = '" + name + "'";
    if (Connect(NORESULT,P5))
    {
      SQL = "INSERT INTO Attachment " +
              "(Location,Name,Id,Type,Attachment_Date,Download_Type,Item_Type,File_Id) "+
              "VALUES " +
              "('" + location + "','" + name + "'," + id + ",'invoice'," +
              "TO_DATE('" + attachmentDate + "','YYYYMMDD')," +
              "'batch','report',NULL)";
      if (Connect(NORESULT,P5))
        ok = true;
    }
    Close();
    return ok;
  }
/*****************************************************************************/
  public boolean insertBIERDocument
  (String accountNumber, String documentDate, String invoiceNo,
   String totalAmount, String noPages, String lastMaintainedBy)
  {
    boolean ok = false;
    SQL = "INSERT INTO BIER_Document " +
          "(Account_Number, Document_Date, Invoice_No, "
          +"No_Pages, Total_Amount, "+""
          + "Last_Maintained_Date, Last_Maintained_By) "+
          "VALUES " +
          "('" + accountNumber + "','" + documentDate + "','" + invoiceNo +
          "'," + noPages + ",'" + totalAmount +
          "',SYSDATE,'" + lastMaintainedBy + "')";
    if (Connect(NORESULT,P5))
        ok = true;
    Close();
    return ok;
  }
/*****************************************************************************/
  public boolean insertBIERDocSectn
  (String accountNumber, String documentDate, String invoiceNo,
   String sectionType, String startPage, String endPage, String lastMaintainedBy)
  {
    boolean ok = false;
    SQL = "INSERT INTO BIER_Doc_Sectn " +
          "(Account_Number, Document_Date, Invoice_No, "
          +"Section_Type, Start_Page, End_Page, "
          + "Last_Maintained_Date, Last_Maintained_By) "+
          "VALUES " +
          "('" + accountNumber + "','" + documentDate + "','" + invoiceNo +
          "','"+sectionType+"'," + startPage + "," + endPage +
          ",SYSDATE,'" + lastMaintainedBy + "')";
    if (Connect(NORESULT,P5))
        ok = true;
    Close();
    return ok;
  }
/*****************************************************************************/
  public boolean PCBAttachment
  (String location, long id, String attachmentDate, String name)
  {
    boolean ok = false;
    SQL =
      "DELETE  " +
      "FROM Attachment " +
      "WHERE Id = " + id + " " +
      "AND Type = 'invoice' " +
      "AND Item_Type = 'report' " +
      "AND Name = '" + name + "'";
    if (Connect(NORESULT,P5))
    {
      SQL = "INSERT INTO Attachment " +
              "(Location,Name,Id,Type,Attachment_Date,Download_Type,Item_Type,File_Id) "+
              "VALUES " +
              "('" + location + "','" + name + "'," + id + ",'invoice'," +
              "TO_DATE('" + attachmentDate + "','YYYYMMDD')," +
              "'batch','report',NULL)";
      if (Connect(NORESULT,P5))
        ok = true;
    }
    Close();
    return ok;
  }
/*****************************************************************************/
  public boolean cleardownBIERTables()
  {
    boolean ok = false;
    SQL = "DELETE FROM BIER_Doc_Sectn";
    if (Connect(NORESULT,P5))
    {
      SQL = "DELETE FROM BIER_Document";
      if (Connect(NORESULT,P5))
        ok = true;
    }
    Close();
    return ok;
  }
/*****************************************************************************/
  public String getAttachmentLocation(String type, long id)
  {
    String location = null;
    SQL = "SELECT Location " +
      "FROM Attachment " +
      "WHERE Id = " + id + " " +
      "AND Type = '" + type + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          location = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return location;
  }
/*****************************************************************************/
  public long updateEbillzAccountSummary
    ( String period, String accountNumber, String category, String timeOfDay,
      long noCalls, long duration, long cost )
  {
    long result = 0;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Update_Ebillz_Account_Summary (?,?,?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,period);
        cstmt.setString(3,accountNumber);
        cstmt.setString(4,category);
        cstmt.setString(5,timeOfDay);
        cstmt.setLong(6,noCalls);
        cstmt.setLong(7,duration);
        cstmt.setLong(8,cost);
        cstmt.execute();
        result = cstmt.getLong(1);
      }

    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
public boolean iptInsertAttachment ( String location, long id, String datepart)
{
  boolean result = false;
  long retValue = -99;
  CallableStatement cstmt = null;
  try
  {
    if (Connect(PREPARE,P3))
    {
      SQL = "{ ? = call IPT.Insert_IPT_Attachment (?,?,?)}";
      cstmt = Conn.prepareCall(SQL);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2,location);
      cstmt.setLong(3,id);
      cstmt.setString(4,datepart);
      cstmt.execute();
      retValue = cstmt.getLong(1);
    }
  }
  catch(Exception ex)
  {
    Message=ex.getMessage();
    System.out.println(Message);
  }//message set in underlying code
  finally
  {
    cstmtClose(cstmt);
  }
  if (retValue==0)
    return true;
  else
    return false;
}

/*****************************************************************************/
public boolean iptInsertSumAttachment ( String location, long id, String datepart)
{
  boolean result = false;
  long retValue = -99;
  CallableStatement cstmt = null;
  try
  {
    if (Connect(PREPARE,P3))
    {
      SQL = "{ ? = call IPT.Insert_IPT_Sum_Attachment (?,?,?)}";
      cstmt = Conn.prepareCall(SQL);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2,location);
      cstmt.setLong(3,id);
      cstmt.setString(4,datepart);
      cstmt.execute();
      retValue = cstmt.getLong(1);
    }
  }
  catch(Exception ex)
  {
    Message=ex.getMessage();
    System.out.println(Message);
  }//message set in underlying code
  finally
  {
    cstmtClose(cstmt);
  }
  if (retValue==0)
    return true;
  else
    return false;
}

/*****************************************************************************/
public long iptArborInvoiceId ( String accountNo, String controlDate)
{
  long result = -99;
  CallableStatement cstmt = null;
  try
  {
    if (Connect(PREPARE,P3))
    {
      SQL = "{ ? = call IPT.CDR_Invoice_Id (?,?)}";
      cstmt = Conn.prepareCall(SQL);
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.setString(2,accountNo);
      cstmt.setString(3,controlDate);
      cstmt.execute();
      result = cstmt.getLong(1);
    }
  }
  catch(Exception ex)
  {
    Message=ex.getMessage();
    System.out.println(Message);
  }//message set in underlying code
  finally
  {
    cstmtClose(cstmt);
  }
  return result;
}

/*****************************************************************************/
  public long insertSSBSManagementSummary (String accountNo, String filename,
    String summaryType, String summaryValue,
    long noCalls, long duration, long cost)
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Insert_Management_Breakdown (?,?,?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,filename);
        cstmt.setString(4,summaryType);
        cstmt.setString(5,summaryValue);
        cstmt.setLong(6,noCalls);
        cstmt.setLong(7,duration);
        cstmt.setLong(8,cost);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public long deleteSSBSManagementSummary (String accountNo, String filename)
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Delete_Management_Breakdown (?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,filename);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public boolean clearFileKPI2Stats (String accountNo, String fileType, String fileDate)
  {
    CallableStatement cstmt = null;
    long result = 0;
    boolean OK = false;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call KPI.Clear_File_KPI2_Stats (?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,fileType);
        cstmt.setString(4,fileDate);
        cstmt.execute();
        result = cstmt.getLong(1);
        if (result>0)
          OK = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return OK;
    }
  }
/*****************************************************************************/
  public boolean insertKPI2
    (String accountNo, String fileType, String fileDate, String cdrDate, long cdrCount, long totalDuration, long totalCost )
  {
    CallableStatement cstmt = null;
    long result = 0;
    boolean OK = false;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call KPI.insert_KPI2 (?,?,?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNo);
        cstmt.setString(3,fileType);
        cstmt.setString(4,fileDate);
        cstmt.setString(5,cdrDate);
        cstmt.setLong(6,cdrCount);
        cstmt.setLong(7,totalDuration);
        cstmt.setLong(8,totalCost);
        cstmt.execute();
        result = cstmt.getLong(1);
        if (result==1)
          OK = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return OK;
    }
  }
/*****************************************************************************/
  public long insertSSBSCEReport (long invoiceId)
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Insert_CE_Report (?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,invoiceId);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public long insertSSBSFTP (String newFilename, String procDate, String reportFilename, String account)
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.FTP_Insert (?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,newFilename);
        cstmt.setString(3,procDate);
        cstmt.setString(4,reportFilename);
        cstmt.setString(5,account);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }/*****************************************************************************/
  public long insertSSBSFTPNew (String newFilename, String procDate, String reportFilename, String account, String ssbsFilename)
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.FTP_Insert_New (?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,newFilename);
        cstmt.setString(3,procDate);
        cstmt.setString(4,reportFilename);
        cstmt.setString(5,account);
        cstmt.setString(6,ssbsFilename);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public long summaryAttachment
    (String zipFilename, String procDate, String location, String name, long invoiceId )
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Summary_Attachment (?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,zipFilename);
        cstmt.setString(3,procDate);
        cstmt.setString(4,location);
        cstmt.setString(5,name);
        cstmt.setLong(6,invoiceId);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public long breakupAttachment
     (String location, long noFiles)
  {
    boolean success = false;
    long result = 0;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Breakup_Attachment (?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,location);
        cstmt.setLong(3,noFiles);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
    }
    return result;
  }
/*****************************************************************************/
  public long insertSSBSCDR
    (String filename, String zipFilename, String procDate, long cdrCount,
     long duration, long cost, String location, String name, long invoiceId )
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.CDR_Insert (?,?,?,?,?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,filename);
        cstmt.setString(3,zipFilename);
        cstmt.setString(4,procDate);
        cstmt.setLong(5,cdrCount);
        cstmt.setLong(6,duration);
        cstmt.setLong(7,cost);
        cstmt.setString(8,location);
        cstmt.setString(9,name);
        cstmt.setLong(10,invoiceId);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
/*****************************************************************************/
  public int updateAccountSLCD
    (String accountNumber, String serviceLocationCode, String description)
  {
    CallableStatement cstmt = null;
    int result = -1;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Update_SLCD (?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNumber);
        cstmt.setString(3,serviceLocationCode);
        cstmt.setString(4,description);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public String getSSBSProccessingDate(String DOB, String month)
  {
    String date = "";
    SQL = "SELECT TO_CHAR( DOB_DATE, 'YYYYMMDD' ) " +
      "FROM DOB " +
      "WHERE Billing_Cycle_Code = '" + DOB + "' " +
      "AND TO_CHAR( DOB_Date , 'YYMM' ) = '" + month + "' ";

    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          date = RS.getString(1);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Close();
      }
    }
    return date;
  }
/*************************************************************************************/
  public long testOraclePackage(String accountNumber)
  {
    CallableStatement cstmt = null;
    long accountId = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call AA_Load.Test (?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNumber);
        cstmt.execute();
        accountId = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return accountId;
    }
  }
/*************************************************************************************/
  public long loadArborArchive(String accountNumber, String invoiceNumber,
    java.sql.Date billingDate, String fileName)
  {
    CallableStatement cstmt = null;
    long ret = -1;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call AA_Load.Process_Archive (?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setString(2,accountNumber);
        cstmt.setString(3,invoiceNumber);
        cstmt.setDate(4,billingDate);
        cstmt.setString(5,fileName);
        cstmt.execute();
        ret = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return ret;
    }
  }
/*************************************************************************************/
  public long loadNewArbor(long customerId, long paymentGroupId, long accountId,
    String invoiceNumber, java.sql.Date billingDate, double grossAmt,
    String currency, String fileType, String fileName, boolean isSummary)
  {
    CallableStatement cstmt = null;
    long ret = -1;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call AA_Load.Process_Attachment (?,?,?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,customerId);
        cstmt.setLong(3,paymentGroupId);
        cstmt.setLong(4,accountId);
        cstmt.setString(5,invoiceNumber);
        cstmt.setDate(6,billingDate);
        cstmt.setDouble(7,grossAmt);
        cstmt.setString(8,currency);
        cstmt.setString(9,fileType);
        cstmt.setString(10,fileName);
        cstmt.setString(11,isSummary?"Y":"N");
        cstmt.execute();
        ret = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      if (ret == -99)
      {
        Message = "duplicate invoice found";
      }
      return ret;
    }
  }
/*****************************************************************************/
  public long[] getIdsForAccount (String accountNumber)
  {
    long[] accountIds = new long[3];
    accountIds[0] = -1;
    SQL="select Account_Id, Account.Payment_Group_Id, Payment_Group.Customer_Id "+
        "from Account, Payment_Group " +
          "where Account_Number = '" + accountNumber + "' "+
          "and Payment_Group.Payment_Group_Id = Account.Payment_Group_Id";
          //System.out.println(SQL);

    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountIds[0] = RS.getLong(1);
          accountIds[1] = RS.getLong(2);
          accountIds[2] = RS.getLong(3);
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account id for account "+accountNumber+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }
    return accountIds;
  }

  /**
   * Load descriptors for all PDF_Email_Request entries
   *
   * @return  an {@link ArrayList} containing the {@link EmailRequestDescriptor}
   *          objects
   */
  public Collection getAllPDFEmailRequests()
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT p.Login_Id, p.Invoice_Id, a.Name, " +
      "p.Last_Maintained_Date, p.Last_Maintained_By, l.Email_Address, " +
      "a.Location, i.Invoice_No, c.Account_Number, c.Account_Name, " +
      "c.Account_Id, p.Group_Id, p.Group_Type " +
      "FROM PDF_Email_Request p, Login l, Attachment a, invoice i, Account c " +
      "WHERE p.Login_Id = l.Login_Id " +
      "AND p.Invoice_Id = a.Id " +
      "AND p.Invoice_Id = i.Invoice_Id " +
      "AND i.Account_Id = c.Account_Id " +
      "AND a.Type = 'invoice' " +
      "AND a.Item_Type <> 'cdr' " +
      "AND p.Group_Type = (select max(p2.Group_Type) from PDF_Email_Request p2 " +
      "WHERE p2.Invoice_Id = p.Invoice_Id " +
      "AND p2.Login_Id = p.Login_Id) " +
      "ORDER BY 1, 2 "
      ;
    /*SQL = "SELECT p.Login_Id, p.Invoice_Id, p.Attachment_Name, " +
      "p.Last_Maintained_Date, p.Last_Maintained_By, l.Email_Address, " +
      "a.Location, i.Invoice_No, c.Account_Number, c.Account_Name, " +
      "c.Account_Id, p.Group_Id, p.Group_Type " +
      "FROM PDF_Email_Request p, Login l, Attachment a, invoice i, Account c " +
      "WHERE p.Login_Id = l.Login_Id " +
      "AND p.Invoice_Id = a.Id " +
      "AND p.Invoice_Id = i.Invoice_Id " +
      "AND i.Account_Id = c.Account_Id " +
      "AND a.Type = 'invoice' " +
      "AND p.Attachment_Name = a.Name " +
      "ORDER BY 1 "
      ;*/
    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          StringBuffer sb = new StringBuffer();
          if (RS.getString(9) != null)
          {
            sb.append(RS.getString(9));
          }
          if ((sb.length() != 0) && (!RS.getString(10).equals("")))
          {
            sb.append("/");
          }
          if (!RS.getString(10).equals(""))
          {
            sb.append(RS.getString(10));
          }
          al.add(new EmailRequestDescriptor(RS.getLong(1), RS.getLong(2),
            RS.getString(3), RS.getTimestamp(4), RS.getString(5),
            RS.getString(6), RS.getString(7), RS.getString(8), sb.toString(),
            RS.getLong(11), RS.getLong(12), RS.getString(13)));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
      Message = "Error: " + Message;
    return al;
  }

  /**
   * Load descriptors for all Notification_Email_Request entries
   *
   * @return  an {@link ArrayList} containing the {@link EmailRequestDescriptor}
   *          objects
   */
  public Collection getAllNotificationEmailRequests()
  {
    ArrayList al = new ArrayList();
    SQL = "SELECT Login_Id, Invoice_Id, Group_Id, Group_Type, " +
      "Last_Maintained_Date, Last_Maintained_By, " +
      "Email_Address, Invoice_No, Account_Number, Account_Name, " +
      "Account_Id, Notification_Type " +
      "FROM Notify_Email_Request_View " +
      "ORDER BY Login_Id, Account_Id "
      ;
    Message = "";
    if (Connect(READ,P5))
    {
      try
      {
        while (RS.next())
        {
          StringBuffer sb = new StringBuffer();
          if (RS.getString(9) != null)
          {
            sb.append(RS.getString(9));
          }
          if ((sb.length() != 0) && (!RS.getString(10).equals("")))
          {
            sb.append("/");
          }
          if (!RS.getString(10).equals(""))
          {
            sb.append(RS.getString(10));
          }
          al.add(new EmailRequestDescriptor(RS.getLong(1), RS.getLong(2),
            RS.getLong(3), RS.getString(4), RS.getTimestamp(5),
            RS.getString(6), RS.getString(7), RS.getString(8), sb.toString(),
            RS.getLong(11), RS.getString(12)));
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        Message = "Error: " + se.getMessage();
        Close();
      }
    }
    else
    {    
      Message = "Error: " + Message;
    }  
    return al;
  }

  public String removePDFEmailRequest(EmailRequestDescriptor erd)
  {
    long result = 0;
    String msg = null;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Remove_PDF_Email_Request (?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,erd.getLoginId());
        cstmt.setLong(3,erd.getInvoiceId());
        cstmt.setString(4,erd.getAttachmentName());
        cstmt.setString(5,erd.getLastMaintainedDate().toString());
        cstmt.setString(6,erd.getLastMaintainedBy());
        cstmt.execute();
        result = cstmt.getLong(1);
        if (result < 0)
        {
          msg = "Error in Remove_PDF_Email_Request, return code: " + result;
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      msg = Message;
    }
    finally
    {
      cstmtClose(cstmt);
      return msg;
    }
  }

  public String removeNotificationEmailRequest(EmailRequestDescriptor erd)
  {
    long result = 0;
    String msg = null;
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Remove_Notification_Email_Req (?,?,?,?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setLong(2,erd.getLoginId());
        cstmt.setLong(3,erd.getAccountId());
        cstmt.setLong(4,erd.getInvoiceId());
        cstmt.setString(5,erd.getNotificationType());
        cstmt.setLong(6,erd.getGroupId());
        cstmt.setString(7,erd.getGroupType());
        cstmt.setString(8,erd.getLastMaintainedDate().toString());
        cstmt.setString(9,erd.getLastMaintainedBy());
        cstmt.execute();
        result = cstmt.getLong(1);
        if (result < 0)
        {
          msg = "Error in Remove_Notification_Email_Req, return code: " + result;
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      msg = Message;
    }
    finally
    {
      cstmtClose(cstmt);
      return msg;
    }
  }

/*  public String removeNotificationEmailRequest(EmailRequestDescriptor erd)
  {
    String returnMessage = null;
    SQL = " DELETE FROM Notification_Email_Request " +
      "WHERE Login_Id = " + erd.getLoginId() + " " +
      (erd.getInvoiceId()>0?("AND Invoice_Id = " + erd.getInvoiceId()):
      ("AND Account_Id = " + erd.getAccountId())) + " " +
      "AND Notification_Type = '" + erd.getNotificationType() + "' " +
      "AND Group_Id = " + erd.getGroupId() + " " +
      "AND Group_Type = '" + erd.getGroupType() + "' " +
      "AND Last_Maintained_Date = TO_DATE('" + erd.getLastMaintainedDate().toString() +
      "','dd/MM/yyyy HH24:mi:ss') " +
      "AND Last_Maintained_By = '" + erd.getLastMaintainedBy() + "' "
      ;
    if (!Connect(NORESULT,P5))
    {
      returnMessage = Message;
    }
    return returnMessage;

  }*/
/*****************************************************************************/
  public String[] wadDetails(String accountNumber)
  {
    String[] result = new String [5];
    result[0] = "NOT WAD";
    SQL = "SELECT WAD_Customer_Name, "+
          "WAD_Address_Line_1, "+
          "WAD_Address_Line_2, "+
          "WAD_Address_Line_3, "+
          "WAD_Address_Line_4 "+
          "FROM WAD_Billing_Details "+
          "WHERE WAD_Account_Number = '"+accountNumber+"' ";
    if (Connect(READ,P5))
    {
      try
      {
        if (RS.next())
        {
          result[0] = RS.getString(1);
          result[1] = RS.getString(2);
          result[2] = RS.getString(3);
          result[3] = RS.getString(4);
          result[4] = RS.getString(5);
        }
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println("Error: "+se.getMessage());
        Close();
      }
    }
    return result;
  }
/*****************************************************************************/
  public int insertQuery(String gcId, String screenId, String userId,
    String queryText, String supplementary, String lastMaintainedBy,
    String billingTeam)
  {
    CallableStatement cstmt = null;
    int queryId = -1;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{call eBAN..Create_Query (?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,gcId);
        cstmt.setString(2,screenId);
        cstmt.setString(3,userId);
        cstmt.setString(4,queryText);
        cstmt.setString(5,supplementary);
        cstmt.setString(6,lastMaintainedBy);
        cstmt.setString(7,SU.isNull(billingTeam, "Data"));

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          queryId=RS.getInt(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return queryId;
    }
  }
/*****************************************************************************/
  public int insertQuery(String gcId, String screenId, String userId,
    String queryText, String supplementary, String lastMaintainedBy,
    String billingTeam, String adEmailAddress)
  {
    CallableStatement cstmt = null;
    int queryId = -1;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{call eBAN..Create_Query (?,?,?,?,?,?,?,?)}";

        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,gcId);
        cstmt.setString(2,screenId);
        cstmt.setString(3,userId);
        cstmt.setString(4,queryText);
        cstmt.setString(5,supplementary);
        cstmt.setString(6,lastMaintainedBy);
        cstmt.setString(7,SU.isNull(billingTeam, "Data"));
        cstmt.setString(8,adEmailAddress);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          queryId=RS.getInt(1);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }//message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return queryId;
    }
  }
/*****************************************************************************/
  public String getInvoiceNo (long invoiceId)
  {
    String invoiceNo = "";
    SQL="SELECT REPLACE(Invoice_no,'/','-') FROM Invoice " +
          "WHERE Invoice_Id = " + invoiceId ;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          invoiceNo = RS.getString(1).trim();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting invoice no for invoice id"+invoiceId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return invoiceNo;
  }
/*****************************************************************************/
  public String getInvoiceNoRaw (long invoiceId)
  {
    String invoiceNo = "";
    SQL="SELECT Invoice_No FROM Invoice " +
          "WHERE Invoice_Id = " + invoiceId ;
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          invoiceNo = RS.getString(1).trim();
        Close();
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting invoice no for invoice id"+invoiceId+" "+
          se.getErrorCode()+" : "+se.getMessage());
        Close();
      }
    }

    return invoiceNo;
  }
  /*****************************************************************************/
  public boolean attachmentToReportArchive
    (String location, String gcid, String reportName, String invoiceNo, String accountNumber)
  {
    boolean result = false;
    String returnV = "";
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call SSBS.Attachment_To_Report_Archive (?,?,?,?,?)}";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
        cstmt.setString(2,location);
        cstmt.setString(3,gcid);
        cstmt.setString(4,reportName);
        cstmt.setString(5,invoiceNo);
        cstmt.setString(6,accountNumber);
        cstmt.execute();
        returnV = cstmt.getString(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      if (returnV.equals("OK"))
        result = true;
      return result;
    }
  }
  /*****************************************************************************/
  public void setRBS09()
  {
    CallableStatement cstmt = null;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call SSBS.Set_RBS09 }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.execute();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
    }
  }
  /*****************************************************************************/
  public int arborControlFileExists(String controlFilename)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Control_File_Exists (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,controlFilename);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int arborCDRFileExists(String cdrFilename)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.CDR_File_Exists (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,cdrFilename);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int insertArborControlFile
    (String controlFilename,
     String type,
     String fileDate,
     String status,
     String statusDetail)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Create_Control_File (?,?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,controlFilename);
        cstmt.setString(3,type);
        cstmt.setString(4,fileDate);
        cstmt.setString(5,status);
        cstmt.setString(6,statusDetail);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int updateArborControlFile
    (long controlFileId,
     String status,
     String statusDetail)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Update_Control_File (?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setLong(2,controlFileId);
        cstmt.setString(3,status);
        cstmt.setString(4,statusDetail);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int insertArborAttachment
    (String location,
     String name,
     long id,
     String type,
     String attachmentDate,
     String filename,
     long count,
     long duration,
     long cost)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Insert_Attachment (?,?,?,?,?,?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,location);
        cstmt.setString(3,name);
        cstmt.setLong(4,id);
        cstmt.setString(5,type);
        cstmt.setString(6,attachmentDate);
        cstmt.setString(7,filename);
        cstmt.setLong(8,count);
        cstmt.setLong(9,duration);
        cstmt.setLong(10,cost);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int insertArborSumAttachment
    (String location,
     String name,
     long id,
     String attachmentDate)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Insert_Summary_Attachment (?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,location);
        cstmt.setString(3,name);
        cstmt.setLong(4,id);
        cstmt.setString(5,attachmentDate);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public int updateArborCDRFile
    (long cdrFileId,
     String status,
     String statusDetail)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Update_CDR_File (?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setLong(2,cdrFileId);
        cstmt.setString(3,status);
        cstmt.setString(4,statusDetail);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public ResultSet getIdentifiedArborCDRFiles()
  {
    ResultSet rs = null;
    CallableStatement cstmt = null;
    try
    {
      SQL = "{ ? = call Arbor.Get_Identified_CDR_Files () }";
      cstmt = Conn.prepareCall(SQL);
      cstmt.registerOutParameter(1,OracleTypes.CURSOR);
      cstmt.execute();
      rs = (ResultSet)cstmt.getObject(1);
    }
    catch(java.sql.SQLException ex)
    {
      String message=ex.getMessage();
      System.out.println(message);
    }
    finally
    {
      return rs;
    }
  }
  /*****************************************************************************/
  public int insertArborCDRFile
    (long controlFileId,
     String cdrFilename,
     String type,
     String fileDate,
     String accountNumber,
     String invoiceNo,
     long cdrCount,
     long totalDuration,
     long totalVolume,
     long totalRated,
     long totalBilled,
     String status,
     String statusDetail)
  {
    CallableStatement cstmt = null;
    int result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call Arbor.Create_CDR_File (?,?,?,?,?,?,?,?,?,?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setLong(3,controlFileId);
        cstmt.setString(2,cdrFilename);
        cstmt.setString(4,type);
        cstmt.setString(5,fileDate);
        cstmt.setString(6,accountNumber);
        cstmt.setString(7,invoiceNo);
        cstmt.setLong(8,cdrCount);
        cstmt.setLong(9,totalDuration);
        cstmt.setLong(10,totalVolume);
        cstmt.setLong(11,totalRated);
        cstmt.setLong(12,totalBilled);
        cstmt.setString(13,status);
        cstmt.setString(14,statusDetail);
        cstmt.execute();
        result = cstmt.getInt(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    String HTML;
    DBAccess DBA = new DBAccess();
    DBA.startDB("OSS");
    //HTML=DBA.get_Site_Splits("COU","Asia",2,"");

  }
/*****************************************************************************/
  public String getCurrentCallMonth()
  {
    String callMonth = "ERROR";
    SQL = "SELECT ISNULL(MAX(Call_Month),'EMPTY') "+
          "FROM RevShare..Process_Control (nolock) ";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          callMonth = RS.getString(1);
        }
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return callMonth;
  }
/*****************************************************************************/
  public boolean checkRequestProcessExists(String callMonth, String processName)
  {
    boolean exists = false;
    SQL = "SELECT COUNT(*) "+
          "FROM RevShare..Process (nolock) "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Name = '"+processName+"' "+
          "AND Process_Status = 'Requested'";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          long count = RS.getLong(1);
          if (count==1)
            exists = true;
        }
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return exists;
  }
/*****************************************************************************/
  public long getRequestProcessSeqNo(String callMonth, String processName)
  {
    long seqNo = 0;
    SQL = "SELECT Process_Seq_No "+
          "FROM RevShare..Process (nolock) "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Name = '"+processName+"' "+
          "AND Process_Status = 'Requested'";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
          seqNo = RS.getLong(1);
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return seqNo;
  }
/*****************************************************************************/
  public boolean setRequestProcessRunning(String callMonth, long processSeqNo, String updatedBy)
  {
    boolean success = false;
    SQL = "UPDATE RevShare..Process "+
          "SET Process_Status = 'Running', "+
          "Last_Updated_Date = GETDATE(), "+
          "Last_Updated_By = '"+updatedBy+"' "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Seq_No = "+processSeqNo;
    if (Connect(NORESULT,P5))
    {
      success = true;
      Close();
    }
    return success;
  }
/*****************************************************************************/
  public boolean setRequestProcessCompleted
    (String callMonth, String processMessagePrefix, long processSeqNo, String completedOK, String updatedBy)
  {
    boolean success = false;
    SQL = "UPDATE RevShare..Process "+
          "SET Process_Status = 'Completed', "+
          "Completed_OK = '"+completedOK+"', "+
          "Last_Updated_Date = GETDATE(), "+
          "Last_Updated_By = '"+updatedBy+"' "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Seq_No = "+processSeqNo;
    if (Connect(NORESULT,P5))
    {
      SQL = "UPDATE RevShare..Process_Control ";
      if (completedOK.startsWith("Y"))
      {
        SQL = SQL +
              "SET Current_Status = '"+processMessagePrefix+" completed', ";
        if (processMessagePrefix.endsWith("rating"))
          SQL = SQL + "Rating_Time = GETDATE(), ";
        if (processMessagePrefix.endsWith(" load"))
          SQL = SQL + "RQR09_Load_Time = GETDATE(), ";
        if (processMessagePrefix.startsWith("Invoicing"))
          SQL = SQL + "Invoicing_Time = GETDATE(), ";
        if (processMessagePrefix.startsWith("Upload"))
          SQL = SQL + "Ebilling_Upload_Time = GETDATE(), ";
        SQL = SQL +
              "Last_Updated_Date = GETDATE(), "+
              "Last_Updated_By = '"+updatedBy+"' ";
      }
      else
      {
        SQL = SQL +
              "SET Current_Status = '"+processMessagePrefix+" failure', "+
              "Last_Updated_Date = GETDATE(), "+
              "Last_Updated_By = '"+updatedBy+"' ";
      }
      SQL = SQL + "WHERE Call_Month = '"+callMonth+"' ";
      //System.out.println(SQL);
      if (Connect(NORESULT,P5))
      {
        success = true;
      }
    }
    Close();
    return success;
  }
/*****************************************************************************/
  public void insertProcessMessage(String callMonth, long processSeqNo, String message, String updatedBy)
  {
    boolean ok = true;
    long messageNo = 1;
    SQL = "SELECT ISNULL( MAX(Message_No) + 1, 1 ) "+
          "FROM RevShare..Process_Message (nolock) "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Seq_No = "+processSeqNo;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
          messageNo = RS.getLong(1);
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      ok = false;
    }
    if (ok)
    {
      SQL = "INSERT INTO RevShare..Process_Message "+
            "(Call_Month, Process_Seq_No, Message_No,"+
            " Message, Last_Updated_Date, Last_Updated_By) "+
            "VALUES( "+
            "'"+callMonth+"',"+processSeqNo+","+messageNo+",'"+
            message+"',GETDATE(),'"+updatedBy+"')";
      if (Connect(NORESULT,P5))
      {
        ok = true;
      }
    }
    Close();
  }
  /*****************************************************************************/
  public boolean insertRQR09
    (String processedCallMonth,
     String accountNumber,
     String providerName,
     String productCode,
     String productName,
     String callMonth,
     String source,
     String NTS,
     String dayCalls,
     String eveningCalls,
     String weekendCalls,
     String dayHours,
     String dayMinutes,
     String daySeconds,
     String dayCharge,
     String eveningHours,
     String eveningMinutes,
     String eveningSeconds,
     String eveningCharge,
     String weekendHours,
     String weekendMinutes,
     String weekendSeconds,
     String weekendCharge,
     String totalHours,
     String totalMinutes,
     String totalSeconds,
     String totalCharge,
     String totalCalls,
     String networkSource,
     String updatedBy,
     String duration)
  {
    boolean ok = false;
    SQL = "INSERT INTO RevShare..RQR09 "+
          "(Processed_Call_Month, Account_Number, Provider_Name,"+
          " Product_Code, Product_Name, Call_Month, Source,"+
          " NTS, Day_Calls, Evening_Calls, Weekend_Calls,"+
          " Day_Hours, Day_Minutes, Day_Seconds, Day_Charge,"+
          " Evening_Hours, Evening_Minutes, Evening_Seconds, Evening_Charge,"+
          " Weekend_Hours, Weekend_Minutes, Weekend_Seconds, Weekend_Charge,"+
          " Total_Hours, Total_Minutes, Total_Seconds, Total_Charge, Total_Calls,"+
          " Network_Source, Last_Updated_Date, Last_Updated_By, Duration) "+
          "VALUES( "+
          "'"+processedCallMonth+"','"+accountNumber+"','"+providerName+
          "','"+productCode+"','"+productName+"','"+callMonth+"','"+source+
          "','"+NTS+"',"+dayCalls+","+eveningCalls+","+weekendCalls+
          ","+dayHours+","+dayMinutes+","+daySeconds+","+dayCharge+
          ","+eveningHours+","+eveningMinutes+","+eveningSeconds+","+eveningCharge+
          ","+weekendHours+","+weekendMinutes+","+weekendSeconds+","+weekendCharge+
          ","+totalHours+","+totalMinutes+","+totalSeconds+","+totalCharge+","+totalCalls+
          ",'"+networkSource+"',GETDATE(),'"+updatedBy+"','"+duration+"')";
    //System.out.println(SQL);
    if (Connect(NORESULT,P5))
    {
      ok = true;
    }
    return ok;
  }
  /*****************************************************************************/
  public boolean insertRQR09
    (String processedCallMonth,
     String accountNumber,
     String providerName,
     String productCode,
     String productName,
     String callMonth,
     String source,
     String NTS,
     String dayCalls,
     String eveningCalls,
     String weekendCalls,
     String dayHours,
     String dayMinutes,
     String daySeconds,
     String dayCharge,
     String eveningHours,
     String eveningMinutes,
     String eveningSeconds,
     String eveningCharge,
     String weekendHours,
     String weekendMinutes,
     String weekendSeconds,
     String weekendCharge,
     String totalHours,
     String totalMinutes,
     String totalSeconds,
     String totalCharge,
     String totalCalls,
     String networkSource,
     String updatedBy,
     String duration,
     String usageCode,
     String serviceLevelCode)
  {
    boolean ok = false;
    SQL = "INSERT INTO RevShare..RQR09 "+
          "(Processed_Call_Month, Account_Number, Provider_Name,"+
          " Product_Code, Product_Name, Call_Month, Source,"+
          " NTS, Day_Calls, Evening_Calls, Weekend_Calls,"+
          " Day_Hours, Day_Minutes, Day_Seconds, Day_Charge,"+
          " Evening_Hours, Evening_Minutes, Evening_Seconds, Evening_Charge,"+
          " Weekend_Hours, Weekend_Minutes, Weekend_Seconds, Weekend_Charge,"+
          " Total_Hours, Total_Minutes, Total_Seconds, Total_Charge, Total_Calls,"+
          " Network_Source, Last_Updated_Date, Last_Updated_By, Duration,"+
          " Usage_Code, Service_Level_Code) "+
          "VALUES( "+
          "'"+processedCallMonth+"','"+accountNumber+"','"+providerName+
          "','"+productCode+"','"+productName+"','"+callMonth+"','"+source+
          "','"+NTS+"',"+dayCalls+","+eveningCalls+","+weekendCalls+
          ","+dayHours+","+dayMinutes+","+daySeconds+","+dayCharge+
          ","+eveningHours+","+eveningMinutes+","+eveningSeconds+","+eveningCharge+
          ","+weekendHours+","+weekendMinutes+","+weekendSeconds+","+weekendCharge+
          ","+totalHours+","+totalMinutes+","+totalSeconds+","+totalCharge+","+totalCalls+
          ",'"+networkSource+"',GETDATE(),'"+updatedBy+"','"+duration+
          "','"+usageCode+"','"+serviceLevelCode+"')";
    //System.out.println(SQL);
    if (Connect(NORESULT,P5))
    {
      ok = true;
    }
    return ok;
  }
/*****************************************************************************/
  public int cleardownRQR09(String callMonth)
  {
    boolean success = false;
    CallableStatement cstmt = null;
    int result = -999;
   try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Cleardown_RQR09 (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt("Return_Value");
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message="SQL Exception : "+ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int populateImportReport(String callMonth, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Populate_Import_Report (?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int populateBillableReport(String callMonth, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Populate_Billable_Report (?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int populateSuspenseReport(String callMonth, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Populate_Suspense_Report (?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int moveSuspendedRQR09(String mode, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        if (mode.equals("R"))
          SQL = "{ call RevShare..Restore_Rated_Suspense (?) }";
        else
          SQL = "{ call RevShare..Restore_Invoiced_Suspense (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int suspendIncorrectlyPostedRQR09(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -9990;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Suspense_Incorrectly_Posted (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int suspendOrphanAccountRQR09(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Suspense_Orphan_Accounts (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int suspendUnallocatedProductsRQR09(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Suspense_Unallocated_Products (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public String getRerateFlag(String callMonth, long processSeqNo)
  {
    String rerate = "N";
    SQL = "SELECT ISNULL( Rerate_RQR09, 'N' ) "+
          "FROM RevShare..Process (nolock) "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Seq_No = "+processSeqNo;
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
          rerate = RS.getString(1);
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return rerate;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getMasterAccountsForRating (String rerate)
  {
    java.sql.ResultSet masterAccountRS = null;
    SQL = "SELECT DISTINCT ma.Master_Account_Number, ma.Master_Account_Name, ma.Master_Account_Id "+
          "FROM RevShare..Master_Account ma (nolock), RevShare..Account a (nolock) "+
          "WHERE ma.Master_Account_Id = a.Master_Account_Id "+
          "AND ISNULL( ma.Deleted_Ind, 'N' ) = 'N' "+
          "AND a.Account_Number IN "+
          "( SELECT DISTINCT r.Account_Number "+
          "FROM RevShare..RQR09 r (nolock) "+
          "WHERE ISNULL( r.Invoiced, 'N' ) = 'N'";
    if (rerate.equals("N"))
      SQL = SQL +" AND ISNULL( r.Rated, 'N' ) = 'N'";
    SQL = SQL + ")";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      masterAccountRS = RS;
    else
      System.out.println(SQL);
    return masterAccountRS;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getProductCodeForRating (long masterAccountId, String rerate)
  {
    java.sql.ResultSet productCodeRS = null;
    SQL = "SELECT DISTINCT r.Product_Code "+
          "FROM RevShare..RQR09 r (nolock) "+
          "WHERE ISNULL( r.Invoiced, 'N' ) = 'N' "+
          "AND Account_Number IN "+
          "(SELECT a.Account_Number "+
          " FROM RevShare..Account a (nolock), Revshare..Master_Account ma (nolock) "+
          " WHERE ma.Master_Account_Id = "+masterAccountId+" "+
          " AND a.Master_Account_Id = ma.Master_Account_Id "+
          " AND ISNULL( a.Deleted_Ind, 'N' ) = 'N') ";
    if (rerate.equals("N"))
      SQL = SQL +" AND ISNULL( r.Rated, 'N' ) = 'N'";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      productCodeRS = RS;
    else
      System.out.println(SQL);
    return productCodeRS;
  }

/*****************************************************************************/
  public int rateMasterAccountProduct
    (String callMonth,
     String rerate,
     String updatedBy,
     long masterAccountId,
     String productCode)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Master_Account_Product_Rating (?,?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,rerate);
        cstmt.setString(3,updatedBy);
        cstmt.setLong(4,masterAccountId);
        cstmt.setString(5,productCode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
        //System.out.println("result = "+result+" : master_account_id = "+masterAccountId+" : product_code = "+productCode);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      Close();
      return result;
    }
  }
  /*****************************************************************************/
  public java.sql.ResultSet getMasterAccountsForInvoicing()
  {
    java.sql.ResultSet masterAccountRS = null;
    SQL = "SELECT DISTINCT ma.Master_Account_Number, ma.Master_Account_Name, ma.Master_Account_Id, "+
          "ma.Frequency_Code, ma.Invoiced "+
          "FROM RevShare..Master_Account ma (nolock), RevShare..Account a (nolock) "+
          "WHERE ma.Master_Account_Id = a.Master_Account_Id "+
          "AND ISNULL( ma.Deleted_Ind, 'N' ) = 'N' "+
          "AND ISNULL( a.Deleted_Ind, 'N' ) = 'N' "+
          "AND a.Account_Number IN "+
          "( SELECT DISTINCT r.Account_Number "+
          "FROM RevShare..RQR09 r (nolock) "+
          "WHERE ISNULL( r.Invoiced, 'N' ) = 'N' "+
          "AND ISNULL( r.Rated, 'N' ) = 'Y')";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      masterAccountRS = RS;
    else
      System.out.println(SQL);
    return masterAccountRS;
  }
/*****************************************************************************/
  public int cleardownRatingReports(String callMonth)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Cleardown_Rating_Reports(?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int cleardownInvoicingReports(String callMonth)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Cleardown_Invoicing_Reports(?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int cleardownInvoiceReport(String callMonth)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Cleardown_Invoice_Report(?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int populateInvoiceReport(String callMonth, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Populate_Invoice_Report (?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int suspendMasterAccountData(String callMonth, long masterAccountId, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Suspense_Master_Account_Data (?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setLong(2,masterAccountId);
        cstmt.setString(3,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int suspendAccountsToIgnore(long masterAccountId, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Suspense_Accounts_To_Ignore (?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int masterAccountPRSInvoicing(String callMonth, long masterAccountId, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..MA_PRS_Invoicing (?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setLong(2,masterAccountId);
        cstmt.setString(3,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int masterAccountNonPRSInvoicing(String callMonth, long masterAccountId, String updatedBy, String frequencyCode)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..MA_Non_PRS_Invoicing (?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setLong(2,masterAccountId);
        cstmt.setString(3,updatedBy);
        cstmt.setString(4,frequencyCode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int resetNotInvoicedPRS(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Reset_Not_Invoiced_PRS (?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public long getUploadCount()
  {
    long uploadCount = 0;
    SQL = "SELECT COUNT(*) "+
          "FROM RevShare..Invoice (nolock) "+
          "WHERE Produced = 'Y' "+
          "AND Invalid = 'N' "+
          "AND Uploaded_To_Ebilling = 'N'";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
          uploadCount = RS.getLong(1);
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return uploadCount;
  }
  /*****************************************************************************/
  public java.sql.ResultSet getInvoicesForUploading()
  {
    java.sql.ResultSet invoicesRS = null;
    SQL = "SELECT i.Invoice_Number, "+
          "PDF_Filename = ISNULL( i.PDF_Filename, 'N/S' ), "+
          "CSV_Filename = ISNULL( i.CSV_Filename, 'N/S' ), "+
          "ma.Presentation_Ind, "+
          "ma.Master_Account_Number, "+
          "i.Net_Amount, "+  
          "i.Vat_Amount, "+  
          "i.Gross_Amount "+  
          "FROM RevShare..Invoice i (nolock), "+
          "RevShare..Master_Account ma (nolock) "+
          "WHERE i.Produced = 'Y' "+
          "AND i.Invalid = 'N' "+
          "AND i.Uploaded_To_Ebilling = 'N' "+
          "AND i.Master_Account_Id = ma.Master_Account_Id";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      invoicesRS = RS;
    else
      System.out.println(SQL);
    return invoicesRS;
  }
/*****************************************************************************/
  public boolean updateInvoiceToUploaded(String invoiceNumber, String updatedBy)
  {
    boolean success = false;
    SQL = "UPDATE RevShare..Invoice "+
          "SET Uploaded_To_Ebilling = 'Y', "+
          "Last_Updated_Date = GETDATE(), "+
          "Last_Updated_By = '"+updatedBy+"' "+
          "WHERE Invoice_Number = '"+invoiceNumber+"'";
    //System.out.println(SQL);
    if (Connect(NORESULT,P5))
      success = true;
    Close();
    return success;
  }
/*****************************************************************************/
  public boolean checkProcessesRun(String callMonth)
  {
    boolean allPresent = false;
    long workSeqNo = -1;
    SQL = "SELECT MAX(Process_Seq_No) FROM RevShare..Process (nolock) "+
          "WHERE Call_Month = '"+callMonth+"' "+
          "AND Process_Status = 'Completed' "+
          "AND Completed_OK = 'Y' "+
          "AND Process_Name = 'Ebilling Upload'";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          workSeqNo = RS.getLong(1);
        }
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    if (workSeqNo>0)
    {
      SQL = "SELECT MAX(Process_Seq_No) FROM RevShare..Process (nolock) "+
            "WHERE Call_Month = '"+callMonth+"' "+
            "AND Process_Status = 'Completed' "+
            "AND Completed_OK = 'Y' "+
            "AND Process_Name = 'Master Account Invoicing' "+
            "AND Process_Seq_No < "+workSeqNo;
      workSeqNo = -1;
      try
      {
        if (Connect(READ,P5))
        {
          if (RS.next())
          {
            workSeqNo = RS.getLong(1);
          }
          Close();
        }
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getMessage());
        Close();
      }
    }
    if (workSeqNo>0)
    {
      SQL = "SELECT MAX(Process_Seq_No) FROM RevShare..Process (nolock) "+
            "WHERE Call_Month = '"+callMonth+"' "+
            "AND Process_Status = 'Completed' "+
            "AND Completed_OK = 'Y' "+
            "AND Process_Name = 'Summarised Call Data Rating' "+
            "AND Process_Seq_No < "+workSeqNo;
      workSeqNo = -1;
      try
      {
        if (Connect(READ,P5))
        {
          if (RS.next())
          {
            workSeqNo = RS.getLong(1);
          }
          Close();
        }
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getMessage());
        Close();
      }
    }
    if (workSeqNo>0)
    {
      SQL = "SELECT MAX(Process_Seq_No) FROM RevShare..Process (nolock) "+
            "WHERE Call_Month = '"+callMonth+"' "+
            "AND Process_Status = 'Completed' "+
            "AND Completed_OK = 'Y' "+
            "AND Process_Name = 'Load Summarised Call Data' "+
            "AND Process_Seq_No < "+workSeqNo;
      workSeqNo = -1;
      try
      {
        if (Connect(READ,P5))
        {
          if (RS.next())
          {
            workSeqNo = RS.getLong(1);
          }
          Close();
        }
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(se.getMessage());
        Close();
      }
    }
    if (workSeqNo>0)
      allPresent=true;
    return allPresent;
  }
/*****************************************************************************/
  public long countOutstandingUploads()
  {
    long workCount = 0;
    SQL = "SELECT COUNT(*) FROM RevShare..Invoice (nolock) "+
          "WHERE Produced = 'Y' AND Invalid = 'N' AND Uploaded_To_Ebilling = 'N'";
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          workCount = RS.getLong(1);
        }
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      Close();
    }
    return workCount;
  }
/*****************************************************************************/
  public int archiveWrittenOff()
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Archive_Written_Off () }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int archiveRQR09()
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Archive_RQR09 () }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public boolean insertNextProcessControl(String callMonth, String updatedBy)
  {
    boolean success = false;
    SQL = "INSERT INTO RevShare..Process_Control "+
          "(Call_Month,"+
          "Current_Status,RQR09_Load_Time,Rating_Time,Invoicing_Time,Ebilling_Upload_Time,Last_Updated_Date,Last_Updated_By) "+
          "VALUES ("+
          "LEFT(CONVERT(VARCHAR,DATEADD(month,1,'"+callMonth+"01'),112),6),"+
          "'Not yet started',NULL,NULL,NULL,NULL,GETDATE(),'"+updatedBy+"')";
    //System.out.println(SQL);
    if (Connect(NORESULT,P5))
    {
      success = true;
      Close();
    }
    return success;
  }
/*****************************************************************************/
  public long getRevShareAccountId (String accountNumber)
  {
    long accountId = 0;
    SQL = "SELECT Account_Id "+
          "FROM Account " +
          "WHERE Account_Number = '" + accountNumber + "' "+
          "AND Billing_Source = 'RevShare'";
    if (Connect(READ,P5))
    {
      try
      {
        if(RS.next())
          accountId= RS.getLong(1);
      }
      catch(java.sql.SQLException se)
      {
        System.out.println(
          "Error getting account id for RevShare account "+accountNumber+" "+
          se.getErrorCode()+" : "+se.getMessage());
      }
      finally
      {
        Close();
      }
    }
    return accountId;
  }
/****************************************************************************/
  public long insertRevShareInvoice
    (String invoiceNo, String taxPointDate, String total, long accountId, String type )
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call RevShare.Insert_Invoice (?,?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,invoiceNo);
        cstmt.setString(3,taxPointDate);
        cstmt.setString(4,total);
        cstmt.setLong(5,accountId);
        cstmt.setString(6,type);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/****************************************************************************/
  public long insertRevShareAttachment
    (String location, String type, long invoiceId, String taxPointDate  )
  {
    CallableStatement cstmt = null;
    long result = 0;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ ? = call RevShare.Insert_Attachment (?,?,?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
        cstmt.setString(2,location);
        cstmt.setString(3,type);
        cstmt.setLong(4,invoiceId);
        cstmt.setString(5,taxPointDate);
        cstmt.execute();
        result = cstmt.getLong(1);
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int identifyUnexpectedProductNTS(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Identify_Unexpected_Product_NTS(?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
/*****************************************************************************/
  public int ignoreInvoicesForSAP(String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Ignore_Invoices_For_SAP(?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
  /*****************************************************************************/
  public java.sql.ResultSet getSAPInvoicesToUpload()
  {
    java.sql.ResultSet invoicesRS = null;
    SQL = "SELECT i.Invoice_Number "+
          "FROM RevShare..Invoice i (nolock) "+
          "WHERE i.Sent_To_SAP = 'N' "+
          "AND i.Invalid = 'N' "+
          "AND i.Uploaded_To_Ebilling = 'Y' "+
          "ORDER BY Tax_Point_Date, Invoice_Number";
    //System.out.println(SQL);
    if (Connect(READ,P5))
      invoicesRS = RS;
    else
      System.out.println(SQL);
    return invoicesRS;
  }
  /*****************************************************************************/
  public String[] getJournalDetails(String invoiceNumber)
  {
    String[] journalDetails = new String[17];
    SQL = "SELECT Invoice_Number, "+
          "Document_Date, "+
          "Transaction_Type, "+
          "Document_Type, "+
          "Company_Code, "+
          "Posting_Date, "+
          "Period, "+
          "Reference, "+
          "Header_Text, "+
          "Tax_Amount, "+
          "Tax_Code, "+
          "Posting_Key_Line_1, "+
          "Posting_Key_Line_2, "+
          "Account_Line_1, "+
          "Account_Line_2, "+
          "Amount_Line_1, "+
          "Amount_Line_2 "+
          "FROM RevShare..SAP_Journal_View "+
          "WHERE Invoice_Number ='"+invoiceNumber+"'";
    //System.out.println(SQL);
    try
    {
      if (Connect(READ,P5))
      {
        if (RS.next())
        {
          journalDetails[0] = RS.getString(1);
          journalDetails[1] = RS.getString(2);
          journalDetails[2] = RS.getString(3);
          journalDetails[3] = RS.getString(4);
          journalDetails[4] = RS.getString(5);
          journalDetails[5] = RS.getString(6);
          journalDetails[6] = RS.getString(7);
          journalDetails[7] = RS.getString(8);
          journalDetails[8] = RS.getString(9);
          journalDetails[9] = RS.getString(10);
          journalDetails[10] = RS.getString(11);
          journalDetails[11] = RS.getString(12);
          journalDetails[12] = RS.getString(13);
          journalDetails[13] = RS.getString(14);
          journalDetails[14] = RS.getString(15);
          journalDetails[15] = RS.getString(16);
          journalDetails[16] = RS.getString(17);
        }
        Close();
      }
    }
    catch(java.sql.SQLException se)
    {
      System.out.println(se.getMessage());
      journalDetails[0] = "Failed";
      Close();
    }
    return journalDetails;
  }
  /*****************************************************************************/
  public boolean updateInvoicesSentToSAP(String invoiceNumberList, String SAPJournalFile, String updatedBy)
  {
    boolean success = false;
    SQL = "UPDATE RevShare..Invoice "+
          "SET Sent_To_SAP = 'Y', "+
          "SAP_Journal_File = '"+SAPJournalFile+"', "+
          "Last_Updated_Date = GETDATE(), "+
          "Last_Updated_By = '"+updatedBy+"' "+
          "WHERE Invoice_Number IN ("+invoiceNumberList+")";
    //System.out.println(SQL);
    if (Connect(NORESULT,P5))
      success = true;
    Close();
    return success;
  }
  /*****************************************************************************/
  public int populateArchiveReports(String callMonth, String updatedBy)
  {
    CallableStatement cstmt = null;
    int result = -999;
    try
    {
      if (Connect(PREPARE,P3))
      {
        SQL = "{ call RevShare..Populate_Archive_Reports(?,?) }";
        cstmt = Conn.prepareCall(SQL);
        cstmt.setString(1,callMonth);
        cstmt.setString(2,updatedBy);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
          result = RS.getInt(1);
      }
    }
    catch(java.sql.SQLException ex)
    {
      Message=ex.getMessage();
      System.out.println(Message);
    }
    //message set in underlying code
    finally
    {
      Close();
      cstmtClose(cstmt);
      return result;
    }
  }
}
