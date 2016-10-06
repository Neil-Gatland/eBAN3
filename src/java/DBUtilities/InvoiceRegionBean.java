package DBUtilities;

import java.sql.*;
import java.util.Date;
//import JavaUtil.StringUtil;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;

public class InvoiceRegionBean extends BANBean
{
    private String SQL;
    private String Billing_Customer_Name,Billing_Address[]={"","","","","","","",""},currentRegion;
    private String Billing_Customer_Name_and_Address,Currency_Desc,Tax_Description,Tax_Reference,SAP_Customer_No;
    private String Billing_Contact,Billing_Entity_Address,Cheque_Remittance_Address,EFT_Remittance_Address;
    protected Vector mandatory=new Vector();

  public InvoiceRegionBean ()
  {
    mandatory.clear();
    mandatory.addElement("Invoice_Region");
    mandatory.addElement("Currency_Desc");
    mandatory.addElement("Billing_Customer_Name_and_Address");
    mandatory.addElement("Billing_Contact");
    mandatory.addElement("Billing_Entity_Address");
    mandatory.addElement("Cheque_Remittance_Address");
    mandatory.addElement("EFT_Remittance_Address");
    mandatory.addElement("Tax_Description");
    mandatory.addElement("Tax_Reference");
    errored.addElement("");
  }
/*set Methods*/
  public void setCurrency_Desc(String newCurrency_Desc)
  {
   Currency_Desc = newCurrency_Desc;
  }
  public void setBilling_Customer_Name_and_Address(String newBilling_Customer_Name_and_Address)
  {
   Billing_Customer_Name_and_Address = newBilling_Customer_Name_and_Address;
  }
  public void setBilling_Contact(String newBilling_Contact)
  {
   Billing_Contact = newBilling_Contact;
  }
  public void setBilling_Entity_Address(String newBilling_Entity_Address)
  {
   Billing_Entity_Address = SU.isNull(newBilling_Entity_Address,"");
  }
  public void setCheque_Remittance_Address(String newCheque_Remittance_Address)
  {
   Cheque_Remittance_Address = SU.isNull(newCheque_Remittance_Address,"");
  }
  public void setEFT_Remittance_Address(String newEFT_Remittance_Address)
  {
   EFT_Remittance_Address = SU.isNull(newEFT_Remittance_Address,"");
  }
  public void setTax_Description(String newTax_Description)
  {
   Tax_Description = SU.isNull(newTax_Description,"");
  }
  public void setTax_Reference(String newTax_Reference)
  {
   Tax_Reference = SU.isNull(newTax_Reference,"");
  }
  public void setSAP_Customer_No(String newSAP_Customer_No)
  {
   SAP_Customer_No = SU.isNull(newSAP_Customer_No,"");
  }
/*set BAN values from form*/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Invoice_Region")==0)
    {
      //retain the old value in case the user updates the invoice_region
      currentRegion=Invoice_Region;
      setInvoice_Region(value);
    }
    else if (name.compareTo("Currency_Desc")==0)
    {
      setCurrency_Desc(value);
    }
    else if (name.compareTo("Billing_Customer_Name_and_Address")==0)
    {
      setBilling_Customer_Name_and_Address(value);
    }
    else if (name.compareTo("Billing_Contact")==0)
    {
      setBilling_Contact(value);
    }
    else if (name.compareTo("Billing_Entity_Address")==0)
    {
      setBilling_Entity_Address(value);
    }
    else if (name.compareTo("Cheque_Remittance_Address")==0)
    {
      setCheque_Remittance_Address(value);
    }
    else if (name.compareTo("EFT_Remittance_Address")==0)
    {
      setEFT_Remittance_Address(value);
    }
    else if (name.compareTo("Tax_Description")==0)
    {
      setTax_Description(value);
    }
    else if (name.compareTo("Tax_Reference")==0)
    {
      setTax_Reference(value);
    }
    else if (name.compareTo("SAP_Customer_No")==0)
    {
      setSAP_Customer_No(value);
    }
  }
/*******************************************************************************/
/*get Methods*/

  public String getBilling_Customer_Name_and_Address()
  {
    return SU.isNull(Billing_Customer_Name_and_Address,"");
  }
  public String getBilling_Contact()
  {
    return Billing_Contact;
  }
  public String getCurrency_Desc()
  {
    return Currency_Desc;
  }
  public String getBilling_Entity_Address()
  {
    return Billing_Entity_Address;
  }
  public String getCheque_Remittance_Address()
  {
    return Cheque_Remittance_Address;
  }
  public String getEFT_Remittance_Address()
  {
    return SU.isNull(EFT_Remittance_Address,"");
  }
  public String getTax_Description()
  {
    return Tax_Description;
  }
  public String getTax_Reference()
  {
    return Tax_Reference;
  }
  public String getSAP_Customer_No()
  {
    return SAP_Customer_No;
  }
  public String getBansToList()
  {
    return bansToList;
  }
/***************************************************************************/
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
public boolean isValid()
{
	setErrored("clear");
        Message = "<font color=red><b>";

        if (SU.isNull(Invoice_Region,"").compareTo("")==0)
        {
          Message = Message+ "You must enter an Invoice Region Name";
	  setErrored("Invoice_Region");
        }
        if (SU.isNull(Currency_Desc," ").compareTo(" ")==0)
        {
          Message = Message+ "You must supply a Currency";
	  setErrored("Currency_Desc");
        }
        if (SU.isNull(Billing_Customer_Name_and_Address,"").compareTo("")==0)
        {
          Message = Message+ "You must supply a Billing Customer Name and Address";
	  setErrored("Billing_Customer_Name_and_Address");
        }
        if (SU.isNull(Billing_Contact,"").compareTo("")==0)
        {
          Message = Message+ "You must supply a Billing Contact";
	  setErrored("Billing_Contact");
        }
        if (SU.isNull(Billing_Entity_Address," ").compareTo(" ")==0)
        {
          Message = Message+ "You must enter a Billing Entity Address";
	  setErrored("Billing_Entity_Address");
        }
        if (SU.isNull(Cheque_Remittance_Address," ").compareTo(" ")==0)
        {
          Message = Message+ "You must enter a Cheque Remittance Address";
	  setErrored("Cheque_Remittance_Address");
        }
        else if (SU.isNull(EFT_Remittance_Address," ").compareTo(" ")==0)
        {
          Message = Message+ "You must enter a value for EFT Remittance Address";
	  setErrored("EFT_Remittance_Address");
        }
	else if (SU.isNull(Tax_Description,"").compareTo("")==0)
        {
          Message = Message+ "You must enter a value for Tax Description";
	  setErrored("Tax_Description");
        }
	else if (SU.isNull(Tax_Reference,"").compareTo("")==0)
        {
          Message = Message+ "You must enter a value for Tax Reference";
	  setErrored("Tax_Reference");
        }
	if (errored.isEmpty())
	  return true;
	else
	{
	  if (errored.size() > 1)
	  {
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	  }
	  return false;
	}
}
/*******************************************************************************/
public boolean getInvoiceRegion()
{
    boolean getInvoiceRegion = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "exec eban..get_Invoice_Region_BAN ";
        SQL += "?,?";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.setString(2,Invoice_Region);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Invoice_Region=RS.getString(1);
	    Currency_Desc=RS.getString(2);
	    Billing_Address[0]=RS.getString(3);
	    Billing_Address[1]=RS.getString(4);
	    Billing_Address[2]=RS.getString(5);
	    Billing_Address[3]=RS.getString(6);
	    Billing_Address[4]=RS.getString(7);
	    Billing_Address[5]=RS.getString(8);
    	    Billing_Address[6]=RS.getString(9);
	    Billing_Address[7]=RS.getString(10);
	    Billing_Contact=RS.getString(11);
	    Billing_Entity_Address=RS.getString(12);
	    Cheque_Remittance_Address=RS.getString(13);
	    EFT_Remittance_Address=RS.getString(14);
	    Tax_Description=RS.getString(15);
	    Tax_Reference=RS.getString(16);
	    SAP_Customer_No=RS.getString(17);
            getInvoiceRegion = true;
	    Billing_Customer_Name_and_Address=SU.packTextArea(Billing_Address);
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
      return getInvoiceRegion;
    }
}
/*****************************************************************************************/
public boolean createInvoiceRegion()
{
  boolean createInvoiceRegion = false;
  int i=0;
  Enumeration Billing_Address_Lines;
    try
    {
      Billing_Address_Lines = SU.unpackTextArea(Billing_Customer_Name_and_Address);

      while ((Billing_Address_Lines.hasMoreElements()) && (i < 8))
      {
	  Billing_Address[i]=(String)Billing_Address_Lines.nextElement();
	  i++;
      }
      while (i < 8)
      {
	Billing_Address[i]="";
	i++;
      }
      if (DBA.Connect(WRITE,P5))
      {
         SQL = "exec eban..Create_Invoice_Region_BAN ";
        SQL += "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,Invoice_Region);
	cstmt.setString(3,Currency_Desc);
	cstmt.setString(4,Billing_Address[0]);
	cstmt.setString(5,Billing_Address[1]);
	cstmt.setString(6,Billing_Address[2]);
	cstmt.setString(7,Billing_Address[3]);
	cstmt.setString(8,Billing_Address[4]);
	cstmt.setString(9,Billing_Address[5]);
	cstmt.setString(10,Billing_Address[6]);
	cstmt.setString(11,Billing_Address[7]);
	cstmt.setString(12,Billing_Contact);
	cstmt.setString(13, Billing_Entity_Address);
	cstmt.setString(14,Cheque_Remittance_Address);
	cstmt.setString(15,EFT_Remittance_Address);
	cstmt.setString(16,Tax_Description);
	cstmt.setString(17,Tax_Reference);
	cstmt.setString(18,SAP_Customer_No);

	try
	{
          cstmt.execute();
          createInvoiceRegion = true;
          Message="Invoice Region created";
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
      }
      else
      { //Failed to connect - message set in underlying code
	Message=DBA.Message;
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
      return createInvoiceRegion;
    }
  }
/**********************************************************************************************/
  public boolean updateInvoiceRegion()
  {
    boolean updateInvoiceRegion = false;
    int rowcount=0,i=0;
    Enumeration Billing_Address_Lines;

    Billing_Address_Lines = SU.unpackTextArea(Billing_Customer_Name_and_Address);

    while ((Billing_Address_Lines.hasMoreElements()) && (i < 8))
    {
      Billing_Address[i]=(String)Billing_Address_Lines.nextElement();
      i++;
    }
    while (i < 8)
    {
      Billing_Address[i]="";
      i++;
    }

    try
    {
	if (DBA.Connect(WRITE,P5))
	{
	  SQL = "exec eban..update_Invoice_Region_BAN ";
	  SQL += "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

	  cstmt = DBA.Conn.prepareCall(SQL);
	  cstmt.setString(1,banIdentifier);
	  cstmt.setString(2,currentRegion);
	  cstmt.setString(3,Currency_Desc);
	  cstmt.setString(4,Billing_Address[0]);
	  cstmt.setString(5,Billing_Address[1]);
	  cstmt.setString(6,Billing_Address[2]);
	  cstmt.setString(7,Billing_Address[3]);
	  cstmt.setString(8,Billing_Address[4]);
	  cstmt.setString(9,Billing_Address[5]);
	  cstmt.setString(10,Billing_Address[6]);
	  cstmt.setString(11,Billing_Address[7]);
	  cstmt.setString(12,Billing_Contact);
	  cstmt.setString(13, Billing_Entity_Address);
	  cstmt.setString(14,Cheque_Remittance_Address);
	  cstmt.setString(15,EFT_Remittance_Address);
	  cstmt.setString(16,Tax_Description);
	  cstmt.setString(17,Tax_Reference);
	  cstmt.setString(18,SAP_Customer_No);
	  cstmt.setString(19,Invoice_Region);
	  //cstmt.setString(19,userId);
	  //cstmt.setString(20,action);
	  //cstmt.setDate(19,proposedBanDate);
	  //cstmt.setString(20,rejectReason);

	  cstmt.execute();
	  rowcount=cstmt.getUpdateCount();
	  if (rowcount != 1)
	  {
	    Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
	  }
          else
          {
            Message="Invoice Region amended";
            updateInvoiceRegion = true;
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
        return updateInvoiceRegion;
      }
  }
 /********************************************************************/
 public boolean banSearch()
 {
  return false;
 }
 /*****************************************************************************/
  public void setInvoiceHeader()
  {
    String imagename1,imagename2;
    if (OrderBy=="Status")
    {
      imagename1="sorted_by.gif";
      imagename2="sort_by.gif";
    }
    else
    {
      imagename2="sorted_by.gif";
      imagename1="sort_by.gif";
    }
        String[] head ={"BAN Id","Customer","Invoice Region","Billing Customer Name","Currency"};
	header="<table border="+border+"><tr class=gridHeader>";
	header+="<td class=gridHeader NOWRAP width=110>BAN Id</td>";
	header+="<td class=gridHeader NOWRAP width=120>Customer</td>";
	header+="<td class=gridHeader NOWRAP width=120>Invoice Region<br><image name=\"Region\" onclick=\"sendOrderBy(2)\" align=right width=13 height=8 src=\"../nr/cw/newimages/"+imagename1+"\"></td>";
	header+="<td class=gridHeader NOWRAP width=120>Billing Customer Name<br><image onclick=\"sendOrderBy(3)\" name=\"Billing\" valign=bottom align=right width=13 height=8 src=\"../nr/cw/newimages/"+imagename2+"\"></td>";
	header+="<td class=gridHeader NOWRAP width=90>Currency</td><td class=gridHeader NOWRAP width=45>Select</td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr></table>";
	columns=5;
}
/*************************************************************************************/
  public String getInvoiceRegionList()
  {
	int counter=0;
	StringBuffer grid = new StringBuffer();
	String RadioButton;
	int rowcount;
	BigInteger rows = new BigInteger("0");
	String gridClass;
        Message="";

	//setHeader();
	columns=5;
	RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border ="+border+">");
	rowcount = 0;

        SQL = "exec eban..list_Invoice_Region_BANs ";
        SQL += "?,?";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.setString(2,OrderBy);
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
	    for(counter=1;counter<columns+1;counter++)
	    {
	      grid.append("<td class="+gridClass+RS.getString(counter)+"</td>");
	    }
	    //Add the extra generated column for the radio button
	    grid.append("<td class="+gridClass+RadioButton+RS.getString(counter)+"')\"></td></tr>");
	    //End the table
	  }
	  grid.append("</table>");
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
        return grid.toString();
      else
        return Message;
    }
  }
/*************************************************************************************/
  //for debugging
  /*public static void main (String[] args)
  {
    boolean OK;
    //HttpServletRequest request = null;
    InvoiceRegionBean iBAN=new InvoiceRegionBean();
    CustomerBANBean cBAN=new CustomerBANBean();
    cBAN.setBanIdentifier("nmb");
    //cBAN.isValid();
 }
 */
}