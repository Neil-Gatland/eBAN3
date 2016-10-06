package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class NTSBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String ntsPrefix;
    private String ntsPrefixList;
    private String productCode;
    private String productGroup;

  public NTSBean ()
  {
    mandatory.clear();
    mandatory.addElement("rsProduct");
    mandatory.addElement("ntsPrefix");
    mandatory.addElement("rsProductGroup");
    errored.addElement("");
    ntsPrefix = "";
    ntsPrefixList = "";
    productCode = "";
    productGroup = "";
  }

  public void Reset()
  {
    super.Reset();
    ntsPrefix = "";
    ntsPrefixList = "";
    productCode = "";
    productGroup = "";
  }
/*set Methods*/
  public void setNTSPrefix(String value)
  {
    ScreenData.put("ntsPrefix",value);
    ntsPrefix = value;
  }
  public void setNTSPrefixList(String value)
  {
    ntsPrefixList = SU.isNull(value,"");
  }
  public void setProductCode(String value)
  {
    ScreenData.put("rsProduct",value);
    productCode = SU.isNull(value,"");
  }
  public void setProductGroup(String value)
  {
    ScreenData.put("rsProductGroup",value);
    productGroup = SU.isNull(value,"");
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("ntsPrefix"))
    {
      setNTSPrefix(value);
    }
    else if (name.equals("rsProductGroup"))
    {
      setProductGroup(value);
    }
    else if (name.equals("rsProduct"))
    {
      setProductCode(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getNTSPrefix()
  {
    return ntsPrefix;
  }
  public String getNTSPrefixList()
  {
    return ntsPrefixList;
  }
  public String getProductCode()
  {
    return productCode;
  }
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
 public boolean productNTSPrefixExists()
 {
    boolean productNTSPrefixExists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Product_NTS_Prefix(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,productCode);
        cstmt.setString(2,ntsPrefix);
        cstmt.execute();
        RS = cstmt.getResultSet();
        productNTSPrefixExists = RS.next();
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
      return productNTSPrefixExists;
    }
 }
/*******************************************************************************/
public boolean deleteProductNTSPrefix()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Product_NTS_Prefix(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productCode);
      cstmt.setString(2,ntsPrefix);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Product NTS Prefix</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product NTS Prefix " +
              productCode + "/" + ntsPrefix + " deleted.</b></font>";
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
public boolean createProductNTSPrefix()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Product_NTS_Prefix(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,productCode);
      cstmt.setString(2,ntsPrefix);
      cstmt.setString(3,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          long ret = RS.getLong(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new Product NTS Prefix</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Product NTS Prefix " +
              productCode + "/" + ntsPrefix + " created.</b></font>";
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
        if (productNTSPrefixExists())
        {
          setErrored("rsProduct");
          setErrored("ntsPrefix");
          Message = "<font color=red><b>This Product NTS Prefix combination exists already.";
          return false;
        }
      }
      return true;
    }
  }
/*******************************************************************************/
  public String populateNTSPrefixList()
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

        SQL = "{call RevShare..Get_Product_NTS_Prefix_List()}";
	cstmt = DBA.Conn.prepareCall(SQL);

	columns=4;

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
            String thisProductCode = RS.getString(counter);
            String thisNTSPrefix = RS.getString(++counter);
            String thisProductGroup = RS.getString(++counter);
            /*boolean canDelete = RS.getString(++counter).equals("true");*/
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<a name=\"" + thisProductCode + thisNTSPrefix + "\"></a>");
            grid.append("<input class=listbutton type=button value=\"D\" " +
              "onClick=\"sendSelected('" + thisProductGroup + "','" +
              thisProductCode + "','" + thisNTSPrefix + "','Delete')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no items to display</b></font>";
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
        setNTSPrefixList(grid.toString());
        return grid.toString();
      }
      else
      {
        setNTSPrefixList("");
        return Message;
      }
    }
  }
}

