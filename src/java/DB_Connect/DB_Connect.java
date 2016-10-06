package DB_Connect;

import java.sql.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import JavaUtil.StringUtil;
import JavaUtil.FileUtil;
import java.util.StringTokenizer;

public class DB_Connect
{
	//Private Data
	private String SQL = "";
	private String Grid = "<tr><td nowrap align=right>&nbsp;&nbsp<font color=blue><b>No Data found</tr></td>";
	private String DB;
	private String ListDataSource;
	private String Table;
	private int Columns;
	private int RowCount;
	public java.sql.ResultSet RS;
	protected  Connection Conn = null;
	//protected ConnectionPool Pool = null;
	protected ConnectionPool P3Pool = null;
	protected ConnectionPool P4Pool = null;
	protected ConnectionPool P5Pool = null;
	private java.sql.Statement Stmt;
	public CallableStatement cstmt;
	private Enumeration Options;
	protected String Message="";
	private static String Qualifier;
	private String Header;
	private int FromPos;
        private String User_Id;
	private String Account_Id;
	private final char quote = '"';
	private final int READ = 1;
	protected  final int PREPARE = 2;
	protected  final int WRITE = 3;
	protected  final int NORESULT = 4;
	protected  final int CONNECT = 5;
	protected  String P3ServerName="",P5ServerName="",P4ServerName="";
	private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
	private JavaUtil.FileUtil FU = new JavaUtil.FileUtil();
	private String Name,Value,OrderBy,DBTable,Alert;
	private static final String P3 = "P3";
	private static final String P5 = "P5";
	private static final String P4 = "P4";
	//private boolean Transaction=false;

/***************************************************************************/
public Connection Connect(int MODE,String DataSource)
{
  //Is this the first call?
  if (P3ServerName.compareTo("")==0)
  {//First time
    //if being invoked in batch mode, the datasource locations will not have been set
    //The screens set these by calling DBStart

    getServerNames("BAR");
  }
  try
  {
    if (DataSource.compareTo("P3")==0)
    {
      P3Pool = P3ConnectionPool.getInstance(P3ServerName);
      Conn = P3Pool.getConnection();
    }
    else if (DataSource.compareTo("P4")==0)
    {
      P4Pool = P4ConnectionPool.getInstance(P4ServerName);
      Conn = P4Pool.getConnection();
    }
    else
    {
      P5Pool = P5ConnectionPool.getInstance(P5ServerName);
      Conn = P5Pool.getConnection();
    }
  }
  catch(java.sql.SQLException se){Message=se.getMessage(); return null;}
  catch(java.lang.NullPointerException se)
  {
    if (DataSource.compareTo("P3")==0)
    {
      Message=P3ConnectionPool.Message;
      return null;
    }
    else if (DataSource.compareTo("P4")==0)
    {
      Message=P4ConnectionPool.Message;
      return null;
    }
    else
    {
      Message=P5ConnectionPool.Message;
      return null;
    }
  }
 try
  {
    //Conn = Pool.getConnection();
    /*if (COMMIT)
    {
      Conn = Pool.getConnection();
    }
    else if (Conn == null)
    {
      Conn = Pool.getConnection();
      Conn.setAutoCommit(false);
    }*/
    if (Conn != null)
    {
      if (MODE==READ)
      {//Run with Query in SQL string
	Stmt = Conn.createStatement();
	RS = Stmt.executeQuery(SQL);
      }
      else if (MODE==NORESULT)
      {//No result so open execute and close
	Stmt = Conn.createStatement();
	Stmt.execute(SQL);
	/**///Pool.free(Conn);
      }
      else
      {//Connect only
	return Conn;
      }
    }
    else
    {
      return null;
    }
  }
  catch(java.sql.SQLException se){Message=se.getMessage(); return null;}
  catch(java.lang.NullPointerException se)
  {
    if (DataSource.compareTo("P3")==0)
    {
      Message=P3ConnectionPool.Message;
      return null;
    }
    else if (DataSource.compareTo("P4")==0)
    {
      Message=P4ConnectionPool.Message;
      return null;
    }
    else
    {
      Message=P5ConnectionPool.Message;
      return null;
    }
  }//message set in underlying code
  return Conn;
}
/**************************************************************************************************/
public Connection BeginTransaction(String DataSource)
{
  Connection Conn;

  Conn=Connect(CONNECT,DataSource);
  if (Conn !=null)
  {
    try
    {
      Conn.setAutoCommit(true);
    }
  catch(java.sql.SQLException se){Message=se.getMessage(); return null;}
  }
  return Conn;
}
public boolean CommitTransaction (Connection Conn)
{
try
  {
    Conn.commit();
  }
  catch(java.sql.SQLException se){Message=se.getMessage(); return false;}
  return true;
}
public void Close (String DataSource)
{
  try
  {
    if (RS != null)
    {//Updates do not have a result set
      RS.close();
    }
    Stmt.close();
    if (Conn.getAutoCommit())
    {
      if (DataSource.compareTo("P3")==0)
      {
	P3Pool=P3ConnectionPool.getInstance("");
	P3Pool.free(Conn);
      }
      else if (DataSource.compareTo("P4")==0)
      {
	P4Pool=P4ConnectionPool.getInstance("");
	P4Pool.free(Conn);
      }
      else
      {
	P5Pool=P5ConnectionPool.getInstance(P5ServerName);
	P5Pool.free(Conn);
      }
    }
  }
  catch(java.sql.SQLException se){Message=se.getMessage();}
  catch(java.lang.NullPointerException ne){Message=ne.getMessage();}
}
public void free (Connection Conn)
{
    String DataSource="";

    try
    {
      DataSource=getDataSourcefromURL(Conn);
      if (Conn.getAutoCommit())
      {
	if (DataSource.compareTo("P3")==0)
	{
	  P3Pool=P3ConnectionPool.getInstance(P3ServerName);
	  P3Pool.free(Conn);
	}
	else if (DataSource.compareTo("P4")==0)
	{
	  P4Pool=P4ConnectionPool.getInstance(P4ServerName);
	  P4Pool.free(Conn);
	}
	else
	{
	  P5Pool=P5ConnectionPool.getInstance(P5ServerName);
	  P5Pool.free(Conn);
	}
      }
    }
    catch(java.lang.NullPointerException ne){Message=ne.getMessage();}
    catch(java.sql.SQLException se){Message=se.getMessage();}
}
  public  void setSQL(String newSQL)
  {
    SQL = newSQL;
  }
  protected  void setMessage(String newMessage)
  {
      Message = newMessage;
  }
  /*public void setTransaction(boolean value)
  {
    Transaction=value;
  }*/
  public  int getRowCount()
  {
	  return RowCount;
  }
  public  String getMessage()
  {
	  return Message;
  }
  private void setListBoxMetaData(String TableName,String QualifierValue)
  {
      if (TableName.compareTo("Invoice_Region") == 0)
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "convert(varchar(35),Invoice_Region)";
	Value = "Invoice_Region";
	Qualifier = " and Global_Customer_Id = '"+QualifierValue+"'";
	Alert = "'Please Select a Customer first'";
	OrderBy = Name;
	DBTable=TableName;
      }
      else if (TableName.compareTo("Barclays_Account") == 0)
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Account_Id";
	Value = "Account_Id";
	Qualifier = " and (Global_Customer_Id = '"+QualifierValue+"' or '"+QualifierValue+"' = 'All')";
	Alert = "";
	OrderBy = Name;
	DBTable=TableName;
      }
      else if (TableName.compareTo("Barclays_Customer") == 0)
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = "";
	OrderBy = "Global_Customer_Name";
	DBTable=TableName;
      }
      else if (TableName.compareTo("Inactive_Global_Customer") == 0)
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Global_Customer_Name";
	Value = "Global_Customer_Id";
	Qualifier = " and reference_ind = 'N'";
	OrderBy = "Global_Customer_Name";
	DBTable="Global_Customer";
      }
      else if (TableName.compareTo("Global_Service_Reference") == 0)
      {
	DB="gcd";
	ListDataSource=P4;
	Name = "Service_Reference";
	Value = "Service_Reference";
	Qualifier = " and Global_Customer_Division_Id in (select distinct Global_Customer_Division_Id ";
	Qualifier += " from gcd..global_customer_division (nolock) where Global_Customer_Id = '"+QualifierValue+"')";
	DBTable = "Global_Customer_Billing";
	OrderBy = Name;
	Alert="'Please select an Account first'";
      }
      else if (TableName.compareTo("Barclays_Service") == 0)
      {
	DB="Barclays";
	ListDataSource=P4;
	Name = "CMDB_Service_Reference";
	Value = "CMDB_Service_Reference";
	//Qualifier = " and substring(CMDB_Service_Reference,1,3) = '"+QualifierValue+"'";
	Qualifier = "";
	DBTable = "Service";
	OrderBy = Name;
	Alert="'Please select an Account first'";
      }
      else if (TableName.compareTo("Currency_Desc") == 0)
      {
	DB="givn";
	ListDataSource=P3;
	Name = "Currency_Desc";
	Value = "Currency_Code";
	Qualifier = "";
	OrderBy = Name;
	DBTable=TableName;
	Alert="";
      }
      else if ((TableName.compareTo("Site") == 0) ||
	       (TableName.compareTo("From_End") == 0) ||
	       (TableName.compareTo("To_End") == 0))
      {
	DB="givn_ref";
	ListDataSource=P3;
	Name = "Site_Id+ ' ' + isnull(Site_Name,'')";
	Value = "Site_Id";
	Qualifier = "";
	OrderBy = Name;
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.compareTo("Barclays_Site") == 0)
      {
	DB="Barclays";
	ListDataSource=P4;
	Name = "Site_Id+ ' ' + isnull(Site_Name,'')";
	Value = "Site_Id";
	Qualifier = "";
	OrderBy = Name;
	DBTable="Site";
	Alert="";
      }
      else if (TableName.compareTo("Revenue_Reason") == 0)
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
       else if (TableName.compareTo("Revenue_Type") == 0)
      {
      	DB="gcd";
	ListDataSource="P4";
	Name = "distinct Revenue_Type_Code + ' ' + Revenue_Type_Description";
	Value = "Revenue_Type_Code + ' ' + Revenue_Type_Description";
	Qualifier = " and Charge_Type_Code = '"+QualifierValue+"'";
	OrderBy = "Revenue_Type_Code + ' ' + Revenue_Type_Description";
	DBTable="Charge_Type";
	Alert="'Please select a Charge Type first'";
      }
      else if (TableName.compareTo("Charge_Frequency") == 0)
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Frequency";
	Value = "Frequency";
	Qualifier = "";
	OrderBy = Name;
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.compareTo("Ban_Status") == 0)
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Status";
	Value = "Status";
	Qualifier = "";
	OrderBy = Name;
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.compareTo("BAN_Type") == 0)
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "BAN_Type";
	Value = "BAN_Type";
	Qualifier = "";
	OrderBy = Value;
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.compareTo("List_Month") == 0)
      {
	DB="eBAN";
	ListDataSource=P5;
	Name = "Name";
	Value = "Name";
	Qualifier = "";
	OrderBy = "null";
	DBTable=TableName;
	Alert="";
      }
      else if (TableName.compareTo("Service_Type") == 0)
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
      else if (TableName.compareTo("Product_Category") == 0)
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Description";
	Value = "Product_Type";
	Qualifier = "";
	OrderBy = Value;
	DBTable="Product_Type_Description";
	Alert="";
      }
      else if (TableName.compareTo("Product_Type") == 0)
      {
	DB="OSS";
	ListDataSource=P5;
	Name = "Description";
	Value = "Tariff_Code";
	Qualifier = "and Product_Category = '"+QualifierValue+"'";
	OrderBy = Name;
	DBTable="Product_Type";
	Alert="'Please select a Product Category first'";
      }
      else if ((TableName.compareTo("Split_Billing") == 0) ||
	      (TableName.compareTo("Standard_Bill_Indicator") == 0) ||
	      (TableName.compareTo("VAT_Indicator") == 0))
      {
	DBTable="YN_Ind";
	Qualifier = "";
	Alert="";
	DB="eBAN";
	ListDataSource=P5;
      }
    else
      {
	DBTable="YN_Ind";
	Qualifier = "";
	Alert="";
	DB="eBAN";
	ListDataSource=P5;
      }
  }
  public String getDataSource(String DataSourceSQL)
  {
   return P5;
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
  public  Enumeration getUser(String System,String UserId,String Password)
  {
	SQL="select Group_Name,User_Name from eBan..eBan_User where Login_Id = '"+UserId;
        SQL=SQL+"' and Password = '"+Password + "' and System = '"+System+"'";

	Vector Result = new Vector();
	if (Connect(READ,P5) != null)
	{
	  try
	  {
	    while (RS.next())
	    {
	      Result.addElement(RS.getString(1));
	      Result.addElement(RS.getString(2));
	    }
	    Close(P5);
	  }
	  catch(java.sql.SQLException se)
	  {
	    Result.addElement(se.getMessage());
	    Close(P5);
	  }
	  if (!Result.isEmpty())
	  {
	    Message = "";
	  }
	  else
	  {
	    Message = "No Data Found for " + SQL;
	  }
	}
	else
	{
	  //Connection failed
	  Result.addElement("<font color=red<b>"+Message);
	  Result.addElement("");
	}
	return Result.elements();
  }
  /*****************************************************************************/
  public Enumeration getResults(String QuerySQL,String DataSource)
  {//Two columns, one row
    SQL=QuerySQL;

    int i,cols=countColumns();

    Vector Row = new Vector();

    if (Connect(READ,DataSource)!= null)
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
	Close(DataSource);
      }
      catch(java.sql.SQLException se)
      {
	Row.addElement(se.getMessage());
	Close(DataSource);
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
      //ad out so that resultset has the expected number of entries
      for (i=2;i<=cols;i++)
      {
	Row.addElement("");
      }
    }
    return Row.elements();
  }
  /*****************************************************************************/
  public String getValue(String QuerySQL,String DataSource)
  {//single value always a string

    SQL=QuerySQL;
    String value="";
    if (Connect(READ,DataSource) != null)
	{
	  try
	  {
	    if (RS.next())
	    {
	      value=RS.getString(1);
	    }
	    else
	    {
	      value= "No Data Found for " + SQL;
	    }
	    Close(DataSource);
	    return value;
	  }
	  catch(java.sql.SQLException se)
	  {
	    Close(DataSource);
	    return se.getMessage();}
	}
	else
	{
	  //Connection failed
	  return "<font color=red<b>"+Message;
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
  public  String getMenu(String System,String Group_Name,String User_Id)
  {
    /****sTANDARDISE*/
    StringBuffer MenuSQL = new StringBuffer("");

    MenuSQL.append("Select Form_Name,Menu_Option,Sequence from eBan..User_Group_Menu_Option where Menu_Name = 'Main' and Group_Name = '");
    MenuSQL.append(Group_Name).append("' and system = '").append(System).append("' ");
    MenuSQL.append("UNION Select Form_Name,Menu_Option,Sequence from eBan..User_Group_Menu_Option where Menu_Name = 'Main' and Group_Name = 'Returned'");
    MenuSQL.append(" and exists (select 1 from OSS_Charge_BAN where BAN_Created_By = '");
    MenuSQL.append(User_Id).append("' and BAN_Status_Code = 'Returned' union select 1 from eBan..Circuit_BAN where BAN_Created_By = '").append(User_Id);
    MenuSQL.append("' and BAN_Status_Code = 'Returned') order by sequence");

    SQL=MenuSQL.toString();
	int Counter;
	Conn=Connect(READ,P5);
	if (Conn != null)
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
				Grid=Grid+"<tr><td><a href='"+RS.getString(1)+"'><img src='/shared/nr/cw/newimages/menu_brown.gif'</a></td>";
				Grid=Grid+"<td class=menu_bold><a href='"+RS.getString(1)+"' class=navchild>"+RS.getString(2)+"</a></td></tr>";
			}
			//End the table
			Grid=Grid+"</table>";
			// Clean up after ourselves
			Close(P5);
		}
		catch(java.sql.SQLException se){Grid=se.getMessage();
		Close(P5);}
		//return Grid;;}
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
  public String getListBox(String Table,String Mode,String FirstEntry,String QualifierValue)
  {
    boolean selected=false;
    StringBuffer ListSQL = new StringBuffer("");
    String Option;
    StringBuffer ListHTML = new StringBuffer("");
    StringBuffer ListHTML2 = new StringBuffer("");

    setListBoxMetaData(Table,QualifierValue);

    if (DBTable.compareTo("YN_Ind") !=0)
    {
      ListSQL.append("Select ").append( Name).append(",").append(Value).append(" from ");
      ListSQL.append(DB).append("..").append(DBTable).append(" (nolock) where ").append(Value).append(" is not null ");
      ListSQL.append(Qualifier);
      if (OrderBy.compareTo("") !=0)
      {
        ListSQL.append(" order by ").append(OrderBy);
      }
    }
    else if (DBTable.compareTo("Invalid") !=0)
    {
      return "<font color=red>Table "+DBTable+" does not exist";
    }
    SQL=ListSQL.toString();

    if ((Qualifier != "")&&(QualifierValue == ""))
    {
      ListHTML.append("<SELECT NAME='").append(Table).append("' size=1 onClick=\"");
      ListHTML.append("alert(").append(Alert).append(")\">");
      if (FirstEntry != "")
      {
	ListHTML.append("<OPTION VALUE='").append(FirstEntry).append("' selected>");
	ListHTML.append(FirstEntry).append("</OPTION>");
      }
      else
      {
	ListHTML.append("<OPTION VALUE='' selected></OPTION></SELECT>");
      }
      return ListHTML.toString();
    }
    if (Connect(READ,ListDataSource) !=null)
    {
      ListHTML.append("<SELECT NAME='").append(Table).append("' size=1 ");

      if (Mode.compareTo("submit")==0)
      {
	ListHTML.append(" onChange='this.form.submit()'>");
      }
      else if (Mode.compareTo("READONLY")==0)
      {
	ListHTML.append(" DISABLED>");
      }
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
	while (RS.next())
	{
	  RowCount++;
	  Option=RS.getString(2);
	  ListHTML2.append("<OPTION VALUE='").append(Option).append("'");
	  if ((FirstEntry != null) && (Option.compareToIgnoreCase(FirstEntry)!=0))
	  {
	  }
	  else
	  {
	    selected=true;
	    ListHTML2.append(" SELECTED");
	  }
	  ListHTML2.append(">").append(RS.getString(1)).append("</OPTION>");
	}
	if (selected)
        {
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
	    else if ((RowCount == 1) && (Mode.compareTo("submit")!=0))
	    {//only one option, so don't add null first entry
	      ListHTML.append(ListHTML2.toString());
	    }
	    else
	    {//null first entry
	      ListHTML.append("<OPTION VALUE=\"\" SELECTED>&nbsp</OPTION>");
	      ListHTML.append(ListHTML2.toString());
	    }
	  }
	}
	ListHTML.append("</SELECT>");
	// Clean up after ourselves
	Close(ListDataSource);
      }
      catch(java.sql.SQLException se)
      {
	Close(ListDataSource);
	return se.getMessage();
      }
    }
    else
    {
        //Connection failed
        Grid="<tr><td nowrap align=right class=allaire>&nbsp;&nbsp<font color=red<b>"+Message+"</tr></td>";
        return Grid;
    }
    return ListHTML.toString();
  }

  /***********************************************************************************/
  public  String getTextArea(String IdField,String DataSource)
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

    SQL+=" from " + DB + ".." + Table + " where "+ Id + "='"+IdField+"'";

    if (Connect(READ,DataSource) != null)
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
	Close(DataSource);
      }
      catch(java.sql.SQLException se)
      {
	HTML=se.getMessage();
	Close(DataSource);
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
  public  boolean getExists(String ExistsSQL,String DataSource)
  {
        SQL=ExistsSQL;
	if(Connect(READ,DataSource) != null)
	{
		try
		{
			if (RS.next())
			{
				return true;
			}
			else
			{
				Message="No data found";
			}
			Close(DataSource);
		}
		catch(java.sql.SQLException se)
		{
		  Message=se.getMessage();
		  Close(DataSource);
		  return false;
		}

	}
	else
	{
		//Message="Database connection failure";
		return false;
	}
	return false;
  }
  /***********************************************************************************/
  public  boolean NoResult(String inSQL,String DataSource)
  {
	SQL=inSQL;
	if(Connect(NORESULT,DataSource) != null)
	{
	    //Close();
	    return true;
	}
	else
	{
	  return false;
	}
   }
/***********************************************************************************/
public boolean startDB(String System)
{
  if (getServerNames(System))
  {
    if (P5ServerName.compareTo("") !=0)
    {
      P5ConnectionPool.getInstance(P5ServerName);
    }
    if (P3ServerName.compareTo("") !=0)
    {
      P3ConnectionPool.getInstance(P3ServerName);
    }
    if (P4ServerName.compareTo("") !=0)
    {
      P4ConnectionPool.getInstance(P4ServerName);
    }
    return true;
  }
  else
  {
    return false;
  }
}
private boolean getServerNames(String System)
{
  BufferedReader inifile;
  String inirecord="";

  inifile=FU.openFile("c:\\JDBC\\"+System+".ini");

  if (inifile == null)
  {
    return false;
  }

  inirecord=FU.readNext(inifile);

  while ((inirecord.compareTo("EOF") != 0) && (!inirecord.startsWith("EOF")))
  {
    if (SU.before(inirecord).compareToIgnoreCase("P3SERVERNAME") == 0)
    {
      P3ServerName=SU.after(inirecord);
    }
    else if (SU.before(inirecord).compareToIgnoreCase("P4SERVERNAME") == 0)
    {
      P4ServerName=SU.after(inirecord);
    }
    else if (SU.before(inirecord).compareToIgnoreCase("P5SERVERNAME") == 0)
    {
      P5ServerName=SU.after(inirecord);
    }
    inirecord=FU.readNext(inifile);
  }
  return true;
}
/*********************************************************************************/
private String getDataSourcefromURL(Connection Conn)
{
  String URL="";
  try
  {
      URL=Conn.getMetaData().getURL();
  }
  catch(java.lang.NullPointerException ne){Message=ne.getMessage();}
  catch(java.sql.SQLException se){Message=se.getMessage();}

  if  (URL.endsWith("localhost:1433"))
  {
    return P3;
  }
  else if  (URL.endsWith("127.0.0.1:1433"))
  {
    return P4;
  }
  else
  {
    return P5;
  }
}
/*************************************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    StringUtil SU = new StringUtil();
    DB_Connect DBA = new DB_Connect();
    String Host;
    Host=SU.getHost("http://dlpc023:8080/OSSeBAN/pages/eBAN.jsp");
    DBA.startDB(Host);
    DBA.getUser("OSS","judge","jules");
  }
}