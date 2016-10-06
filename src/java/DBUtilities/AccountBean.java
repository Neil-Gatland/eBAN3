package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class AccountBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long accountId;
    private long masterAccountId;
    private long providerId;
    private String providerName;
    private String masterAccountNumber;
    private String masterAccountName;
    private String accountNumber;
    private String accountName;
    private String invoiceInd;
    private String accountProductList;
    private String productCode;
    private String productDescription;

  public AccountBean ()
  {
    mandatory.clear();
    mandatory.addElement("accountNumber");
    mandatory.addElement("accountName");
    mandatory.addElement("invoiceInd");
    errored.addElement("");
    accountId = -1;
    masterAccountId = -1;
    providerId = -1;
    providerName = "";
    masterAccountNumber = "";
    masterAccountName = "";
    accountNumber = "";
    accountName = "";
    invoiceInd = "";
    accountProductList = "";
    productCode = "";
  }

  public void Reset()
  {
    super.Reset();
    accountId = -1;
    masterAccountId = -1;
    providerId = -1;
    providerName = "";
    masterAccountNumber = "";
    masterAccountName = "";
    accountNumber = "";
    accountName = "";
    invoiceInd = "";
    accountProductList = "";
    productCode = "";
  }
/*set Methods*/
  public void setAccountId (String value)
  {
    accountId  = Long.parseLong(value);
  }
  public void setMasterAccountId (String value)
  {
    masterAccountId  = Long.parseLong(value);
  }
  public void setProviderId (String value)
  {
    providerId  = Long.parseLong(value);
  }
  public void setMasterAccountNumber(String value)
  {
    masterAccountNumber = value;
  }
  public void setMasterAccountName(String value)
  {
    ScreenData.put("masterAccountName",value);
  }
  public void setAccountNumber(String value)
  {
    ScreenData.put("accountNumber",value);
    accountNumber = value;
  }
  public void setAccountName(String value)
  {
    ScreenData.put("accountName",value);
    accountName = value;
  }
  public void setInvoiceInd(String value)
  {
    ScreenData.put("invoiceInd",value);
    invoiceInd = SU.isNull(value,"");
  }
  public void setAccountProductList(String value)
  {
    accountProductList = SU.isNull(value,"");
  }
  public void setProductCode(String value)
  {
    productCode = SU.isNull(value,"");
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("accountNumber"))
    {
      setAccountNumber(value);
    }
    else if (name.equals("accountName"))
    {
      setAccountName(value);
    }
    else if (name.equals("invoiceInd"))
    {
      setInvoiceInd(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getMasterAccountId ()
  {
    return Long.toString(masterAccountId);
  }
  public String getAccountId ()
  {
    return Long.toString(accountId);
  }
  public String getProviderId ()
  {
    return Long.toString(providerId);
  }
  public String getMasterAccountNumber()
  {
    return masterAccountNumber;
  }
  public String getMasterAccountName()
  {
    return masterAccountName;
  }
  public String getAccountNumber()
  {
    return accountNumber;
  }
  public String getAccountName()
  {
    return accountName;
  }
  public String getInvoiceInd()
  {
    return invoiceInd ;
  }
  public String getAccountProductList()
  {
    return accountProductList;
  }
  public String getProductCode()
  {
    return productCode;
  }
  public String getSelectMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getSelectMode();
    }
  }
  public String getInputMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getInputMode();
    }
  }
  public String getMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      ((Mode.equals("Amend")) && (FieldName.equals("accountNumber"))))
    {
      return "READONLY";
    }
    else
    {
      return InputMode;
    }
  }
  /*******************************************************************************/
public String getClass(String Item)
{
        if (!errored.isEmpty())
	{
	  if (errored.contains(Item))
	    return "errored";
	  else
	  {
	    if (mandatory.contains(Item))
              return "mandatory";
	    else
	      return "optional";
	  }
    	}
	else
	{
	  if (mandatory.contains(Item))
            return "mandatory";
	  else
	    return "optional";
	}
}
/*******************************************************************************/
 public boolean getMasterAccountFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Master_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          providerId = RS.getLong(1);
          masterAccountNumber = RS.getString(2);
          masterAccountName = RS.getString(3);
          getFromDB = true;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getFromDB;
    }
 }
/*******************************************************************************/
 public boolean getAccountFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,accountId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          masterAccountId = RS.getLong(1);
          setAccountNumber(RS.getString(2));
          accountName = RS.getString(3);
          invoiceInd = RS.getString(4);
          getFromDB = true;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getFromDB;
    }
 }
/*******************************************************************************/
 private boolean accountExists()
 {
    boolean exists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Account_Number(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,accountNumber);
        cstmt.execute();
        RS = cstmt.getResultSet();

        exists = RS.next();
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return exists;
    }
 }
/*******************************************************************************/
public boolean updateAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Account(?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,accountId);
      cstmt.setLong(2,masterAccountId);
      cstmt.setString(3,accountNumber);
      cstmt.setString(4,accountName);
      cstmt.setString(5,invoiceInd);
      cstmt.setString(6,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Account " +
              accountName + "/" + accountNumber + " (" + ret +
              ") updated.</b></font>";
            ok = true;
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ok;
  }
}
/*******************************************************************************/
public boolean deleteAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Account(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,accountId);
      cstmt.setString(2,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Account " +
              accountName + "/" + accountNumber + " (" + ret +
              ") deleted.</b></font>";
            ok = true;
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ok;
  }
}
/*******************************************************************************/
public boolean createAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Account(?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,masterAccountId);
      cstmt.setString(2,accountNumber);
      cstmt.setString(3,accountName);
      cstmt.setString(4,invoiceInd);
      cstmt.setString(5,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          long ret = RS.getLong(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New Account " +
              accountName + "/" + accountNumber + " (" + ret +
              ") created.</b></font>";
            accountId = ret;
            ok = true;
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ok;
  }
}
/*******************************************************************************/
public boolean createAccountProduct()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Account_Product(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,accountId);
      cstmt.setString(2,productCode);
      cstmt.setString(3,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to add Product to this Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product " + productDescription +
              " " + productCode + " added to Account " + accountName + "/" +
              accountNumber + " (" + accountId + ").</b></font>";
            ok = true;
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ok;
  }
}
/*******************************************************************************/
public boolean deleteAccountProduct()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Account_Product(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,accountId);
      cstmt.setString(2,productCode);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Product from this Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product " + productDescription +
              " " + productCode + " deleted from Account " + accountName + "/" +
              accountNumber + " (" + accountId + ").</b></font>";
            ok = true;
          }
        }
      }
      catch(java.sql.SQLException se)
      {
        Message=se.getMessage();
      }
    }
  }
  catch(java.sql.SQLException se)
  {
    Message=se.getMessage();
  }
  catch(java.lang.NullPointerException se)
  {
    Message="<font color=red><b>"+se.getMessage();
  }//message set in underlying code
  finally
  {
    close();
    return ok;
  }
}
/******************************************************************************/
  public boolean isValid(String ButtonPressed)
  {
    Enumeration FormFields=mandatory.elements();
    setErrored("clear");
    Message = "<font color=red><b>";
    String FormField="";
    String FieldName;
    String value="";

    if (ButtonPressed.startsWith("Submit"))
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if ((FormField.equals("")) || (SU.isSpaces(FormField)))
        {
          setErrored(FieldName);
        }
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      if (Mode.equals("Create"))
      {
        if (accountExists())
        {
          setErrored("accountNumber");
          Message = "<font color=red><b>This Account Number exists already.";
          return false;
        }
        else if (accountNumber.length() != 7)
        {
          setErrored("accountNumber");
          Message = "<font color=red><b>Account Number must have seven digits.";
          return false;
        }
        else if (accountNumber.indexOf(" ") != -1)
        {
          setErrored("accountNumber");
          Message = "<font color=red><b>Account Number cannot contain spaces.";
          return false;
        }
        else
        {
          try
          {
            Long.parseLong(accountNumber);
          }
          catch (NumberFormatException nfe)
          {
            setErrored("accountNumber");
            Message = "<font color=red><b>Account Number must be numeric.";
            return false;
          }
        }
      }
      /*else if ((Mode.equals("Create")) && (deletedInd.equals("Y")))
      {
        setErrored("deletedInd");
        Message = "<font color=red><b>Master Account cannot be created with deleted ind set to 'Y'.";
        return false;
      }
      else if ((Mode.equals("Amend")) && (!canDelete()))
      {
        setErrored("deletedInd");
        Message = "<font color=red><b>Master Account cannot be deleted until all of its accounts have been deleted";
        return false;
      }*/
      return true;
    }
  }
/*******************************************************************************/
 public boolean getProviderFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Provider(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,providerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          providerName = RS.getString(1);
          getFromDB = true;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getFromDB;
    }
 }
/*******************************************************************************/
 public boolean canDelete()
 {
    boolean canDelete = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Account_Product_For_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,accountId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        canDelete = !RS.next();
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return canDelete;
    }
 }
/*******************************************************************************/
 public boolean accountProductExists()
 {
    boolean accountProductExists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Account_Product(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,accountId);
        cstmt.setString(2,productCode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        accountProductExists = RS.next();
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return accountProductExists;
    }
 }
/*******************************************************************************/
  public String populateAccountProductList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Account_Product_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,accountId);

	columns=3;

        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            rowcount++;
            rows = BigInteger.valueOf(rowcount);
            //Alternate row colours
            if (rows.testBit(0))
            {//An odd number
              gridClass="grid1 ";
            }
            else
            {
              gridClass="grid2 ";
            }
            String value = "";
            for(counter=1;counter<columns+1;counter++)
            {
              grid.append("<td class=");
              grid.append(gridClass);
              value = RS.getString(counter);
              grid.append(value);
              grid.append("</td>");
            }
            //Add the extra generated column for the buttons
            String thisProductId = RS.getString(counter);
            /*boolean canDelete = RS.getString(++counter).equals("true");*/
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");/*
            //grid.append("<input class=listbutton type=button value=\"V\" " +
              //"onClick=\"sendSelected('" + thisProductId + "','ProductView')\">");
            if (canDelete)
            {*/
              grid.append("<input class=listbutton type=button value=\"D\" " +
                "onClick=\"sendSelected('" + thisProductId + "','ProductDelete')\">");
            /*}
            else
            {
              grid.append("<input class=listbutton type=button value=\" \" " +
                "onClick=\"\">");
            }*/
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Products for this Product Group</b></font>";
          }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      if (Message.equals(""))
      {
        setAccountProductList(grid.toString());
        return grid.toString();
      }
      else
      {
        setAccountProductList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public boolean getProductFromDB()
  {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Product(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productCode);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          productDescription = RS.getString(1);
          //productDescription = RS.getString(1);
          getFromDB = true;
        }
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getFromDB;
    }
  }
}

