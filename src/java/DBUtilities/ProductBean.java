package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class ProductBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String productCode;
    private String productDescription;
    private String productGroup;
    private String returnTo;

  public ProductBean ()
  {
    mandatory.clear();
    mandatory.addElement("productCode");
    errored.addElement("");
    productCode = "";
    productDescription = "";
    productGroup = "";
    returnTo = "";
  }

  public void Reset()
  {
    super.Reset();
    productCode = "";
    productDescription = "";
    productGroup = "";
    returnTo = "";
  }
/*set Methods*/
  public void setProductCode(String value)
  {
    ScreenData.put("productCode",value);
    productCode = value;
  }
  public void setProductDescription(String value)
  {
    ScreenData.put("productDescription",value);
    productDescription = value;
  }
  public void setProductGroup(String value)
  {
    productGroup = value;
  }
  public void setReturnTo(String value)
  {
    returnTo = value;
  }

  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("productCode"))
    {
      setProductCode(value);
    }
    else if (name.equals("productDescription"))
    {
      setProductDescription(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getProductCode()
  {
    return productCode;
  }
  public String getProductDescription()
  {
    return productDescription;
  }
  public String getProductGroup()
  {
    return productGroup;
  }
  public String getReturnTo()
  {
    return returnTo;
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
      ((Mode.equals("Amend")) && (FieldName.equals("productCode"))))
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
/*******************************************************************************/
  public boolean deleteProduct()
  {
    boolean success = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Delete_Product(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productCode);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          if (RS.getInt(1) == 0)
          {
            success = true;
            Message = "<font color=blue><b>Product " + productCode + " " +
              productDescription + " deleted." +
              (returnTo.equals("productGroupMaint.jsp")?" To " +
              "create a new Product for this Product Group, select 'Add New " +
              "Product' from the 'Options' menu above.":"") + "</b></font>";
          }
          else
          {
            success = false;
            Message = "<font color=red><b>Unable to delete Product.</b></font>";
          }
        }
      }
      else
      {
        success = false;
        Message = "<font color=red><b>Unable to delete Product.</b></font>";
      }
    }
    catch(Exception ex)
    {
      Message="<font color=red><b>"+ex.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return success;
    }
  }
/*******************************************************************************/
public boolean updateProduct()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Product_Group(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productCode);
      cstmt.setString(2,productDescription);
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
            Message = "<font color=red><b>Unable to update Product</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product " + productCode + " " +
              productDescription + " updated</b></font>";
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
public boolean createProduct()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Product(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productCode);
      cstmt.setString(2,productDescription);
      cstmt.setString(3,productGroup);
      cstmt.setString(4,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new Product</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New Product " + productCode + " " +
              productDescription + " created." +
              (returnTo.equals("productGroupMaint.jsp")?" To " +
              "create another new Product for this Product Group, select " +
              "'Add New Product' from the 'Options' menu above.":"") +
              "</b></font>";
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
  private boolean productExists()
  {
    boolean exists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Product(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productCode);
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
      if ((Mode.equals("Create")) && (productExists()))
      {
        setErrored("productCode");
        Message = "<font color=red><b>A Product with this Product Code exists already.";
        return false;
      }
      return true;
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
        SQL = "{call RevShare..Find_Account_Product_For_Product(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productCode);
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

