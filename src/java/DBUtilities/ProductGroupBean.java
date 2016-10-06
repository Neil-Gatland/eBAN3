package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class ProductGroupBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String productGroup;
    private String origProductGroup;
    private String productList;

  public ProductGroupBean ()
  {
    mandatory.clear();
    mandatory.addElement("productGroup");
    errored.addElement("");
    productGroup = "";
    productList = "";
  }

  public void Reset()
  {
    super.Reset();
    productGroup = "";
    productList = "";

  }
/*set Methods*/
  public void setProductGroup(String value)
  {
    ScreenData.put("productGroup",value);
    productGroup = value;
  }
  public void setProductList(String value)
  {
    productList = SU.isNull(value,"");
  }

  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("productGroup"))
    {
      setProductGroup(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getProductGroup()
  {
    return productGroup;
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
      ((Mode.equals("Amend")) && (FieldName.equals("productGroup"))))
    {
      return "READONLY";
    }
    else
    {
      return InputMode;
    }
  }
  public String getProductList()
  {
    return productList;
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
 public boolean getProductGroupFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Product_Group(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productGroup);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          productGroup = RS.getString(1);
          origProductGroup = productGroup;
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
 private boolean productGroupExists()
 {
    boolean exists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Product_Group(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productGroup);
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
public boolean updateProductGroup()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Product_Group(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productGroup);
      cstmt.setString(2,origProductGroup);
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
            Message = "<font color=red><b>Unable to update Product Group</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product Group " + productGroup +
              " updated</b></font>";
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
public boolean deleteProductGroup()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Product_Group(?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productGroup);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Product Group</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product Group " + productGroup +
              " deleted</b></font>";
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
public boolean createProductGroup()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Product_Group(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productGroup);
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
            Message = "<font color=red><b>Unable to create new Product Group</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New Product Group " + productGroup +
              " created. To create a new Product for " +
              "this Product Group, select 'Add New Product' " +
              "from the 'Options' menu above.</b></font>";
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
        /*else if (FieldName.equals("billingRefPrefix"))
        {
          if ((FormField.length() < 4) || (FormField.length() > 8))
          {
            setErrored("billingRefPrefix");
            Message = "<font color=red><b>Billing Reference Prefix must be between 4 and 8 characters in length";
            return false;
          }
        }*/
      }
    }
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      if ((Mode.equals("Create")) && (productGroupExists()))
      {
        setErrored("productGroup");
        Message = "<font color=red><b>This Product Group exists already.";
        return false;
      }
      return true;
    }
  }
/*******************************************************************************/
  public String populateProductList()
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

        SQL = "{call RevShare..Get_Product_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productGroup);

	columns=2;

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
            boolean canDelete = RS.getString(++counter).equals("true");
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            //grid.append("<input class=listbutton type=button value=\"V\" " +
              //"onClick=\"sendSelected('" + thisProductId + "','ProductView')\">");
            if (canDelete)
            {
              grid.append("<input class=listbutton type=button value=\"D\" " +
                "onClick=\"sendSelected('" + thisProductId + "','ProductDelete')\">");
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\" \" " +
                "onClick=\"\">");
            }
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
        setProductList(grid.toString());
        return grid.toString();
      }
      else
      {
        setProductList("");
        return Message;
      }
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
        SQL = "{call RevShare..Find_Product_For_Product_Group(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productGroup);
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
}

