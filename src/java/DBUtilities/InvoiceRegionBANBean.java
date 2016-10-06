package DBUtilities;

import java.sql.*;
import java.math.BigInteger;
import java.util.*;

public class InvoiceRegionBANBean extends BANBean
{
    private String SQL;
    private String Billing_Customer_Name,currentRegion;
    private String[] Billing_Address;
    private String Currency_Desc,Tax_Description,Tax_Reference;
    private String Billing_Contact,Billing_Entity_Address,Cheque_Remittance_Address,EFT_Remittance_Address;
    private String globalAccountManager, customerContact,
      customerBillingName, customerBillingAddress, companyAddressId,
      banTaxRequirement, multipleBillingRegions, customerBillingCountry,
      sapCustomerNo, customerVatRegNo, billingRegions, countryLiteral,
      customerContactPoint, strategicReports, restrictionType;
    private int BEDays, BEMonths, BEYears;
    protected Vector mandatory=new Vector();
    private Hashtable ScreenData=new Hashtable();
    private String tokenDelimiter = "\r\n";
    private static final String TEN_ZEROS = "0000000000";
    private boolean isMANS;
    private String origInvoiceRegionName;
    private String arborBar;
    private String pcbBar;
    private String cmpls;
    
    
  public InvoiceRegionBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("Invoice_Region");
    mandatory.addElement("BANEffectiveDateh");
    mandatory.addElement("Global_Account_Manager");
    //mandatory.addElement("SAP_Customer_No");
    mandatory.addElement("Company_Address_Id");
    mandatory.addElement("Customer_Contact");
    mandatory.addElement("Customer_Billing_Name");
    //mandatory.addElement("Customer_VAT_Reg_No");
    mandatory.addElement("BAN_Tax_Requirement");
    mandatory.addElement("Customer_Billing_Address");
    mandatory.addElement("Customer_Billing_Country");
    mandatory.addElement("Currency_Desc");
    mandatory.addElement("Multiple_Billing_Regions");
    mandatory.addElement("Customer_Contact_Point");
    Billing_Address=new String[8];
    isMANS = false;
    origInvoiceRegionName = "";
    restrictionType = null;
    arborBar = "";
    pcbBar = "";
    cmpls = "";
  }
  public void Reset()
  {
    super.Reset();
    globalAccountManager = "";
    customerContact = "";
    customerBillingName = "";
    customerBillingAddress = "";
    companyAddressId = "";
    banTaxRequirement = " ";
    multipleBillingRegions = "N";
    customerBillingCountry = "GB";
    sapCustomerNo = "";
    customerVatRegNo = "";
    billingRegions = "";
    countryLiteral = "";
    Invoice_Region = "";
    errored.clear();
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    Message="";
    Billing_Address=new String[8];
    isMANS = false;
    customerContactPoint = "";
    strategicReports = "N";
    origInvoiceRegionName = "";
    restrictionType = null;
    arborBar = "";
    pcbBar = "";
    cmpls = "";
  }
/*set Methods*/
  public void setIsMANS(boolean value)
  {
    isMANS = value;
    if (isMANS)
    {
      if (!Mode.equals("Amend"))
      {
        companyAddressId = "12";
      }
      if (mandatory.indexOf("Customer_Contact") != -1)
      {
        mandatory.remove("Customer_Contact");
      }
    }
    else
    {
      if (mandatory.indexOf("Customer_Contact") == -1)
      {
        mandatory.add("Customer_Contact");
      }
    }
  }
  public void setCMPLS(String value)
  {
   cmpls = SU.isNull(value,"");
   ScreenData.put("CMPLS",cmpls);
  }
  public void setArborBar(String value)
  {
   arborBar = SU.isNull(value,"");
   ScreenData.put("Arbor_Bar",arborBar);
  }
  public void setPCBBar(String value)
  {
   pcbBar = SU.isNull(value,"");
   ScreenData.put("PCB_Bar",pcbBar);
  }
  public void setCustomerContactPoint(String value)
  {
   customerContactPoint = value;
  }
  public void setCurrency_Desc(String newCurrency_Desc)
  {
   Currency_Desc = newCurrency_Desc;
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
  public void setGlobal_Account_Manager(String newGlobal_Account_Manager)
  {
   globalAccountManager = SU.isNull(newGlobal_Account_Manager,"");
  }
  public void setCustomer_Contact(String newCustomer_Contact)
  {
   customerContact = SU.isNull(newCustomer_Contact,"");
  }
  public void setCustomer_Billing_Name(String newCustomer_Billing_Name)
  {
   customerBillingName = SU.isNull(newCustomer_Billing_Name,"");
  }
  public void setCustomer_Billing_Address(String newCustomer_Billing_Address)
  {
   customerBillingAddress = SU.isNull(newCustomer_Billing_Address,"");
  }
  public void setCustomer_Billing_Country(String newCustomer_Billing_Country)
  {
   customerBillingCountry = SU.isNull(newCustomer_Billing_Country,"");
  }
  public void setCompany_Address_Id(String newCompany_Address_Id)
  {
    companyAddressId = SU.isNull(newCompany_Address_Id,"");
    /*if ((companyAddressId.equals("3")) || (companyAddressId.equals("12")) ||
    (companyAddressId.equals("27")) || (companyAddressId.equals("42")))
    {
      if (mandatory.indexOf("SAP_Customer_No") != -1)
      {
        mandatory.remove("SAP_Customer_No");
      }
    }
    else
    {
      if (mandatory.indexOf("SAP_Customer_No") == -1)
        mandatory.add("SAP_Customer_No");
    }*/
  }
  public void setBAN_Tax_Requirement(String newBAN_Tax_Requirement)
  {
   banTaxRequirement = SU.isNull(newBAN_Tax_Requirement,"");
  }
  public void setMultiple_Billing_Regions(String newMultiple_Billing_Regions)
  {
   multipleBillingRegions = SU.isNull(newMultiple_Billing_Regions,"");
  }
  public void setStrategicReports(String newStrategicReports)
  {
   strategicReports = SU.isNull(newStrategicReports,"N");
  }
  public void setSAP_Customer_No(String newSAP_Customer_No)
  {
    if ((mandatory.indexOf("SAP_Customer_No") == -1) && (Mode.equals("Create")))
      sapCustomerNo = "";
    else
      sapCustomerNo = SU.isNull(newSAP_Customer_No,"");
  }
  public void setCustomer_VAT_Reg_No(String newCustomer_VAT_Reg_No)
  {
   customerVatRegNo = SU.isNull(newCustomer_VAT_Reg_No,"");
  }
  public void setBillingRegions(String newBillingRegions)
  {
   billingRegions = SU.isNull(newBillingRegions,"");
  }
  public void setCountryLiteral(String newCountryLiteral)
  {
   countryLiteral = SU.isNull(newCountryLiteral,"");
  }
  public void setInvoice_Region(String newInvoice_Region)
  {
    Invoice_Region = newInvoice_Region.replace(',', ' ');
  }
/*set BAN values from form*/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("Invoice_Region")==0)
    {
      //retain the old value in case the user updates the invoice_region
      currentRegion=Invoice_Region;
      setInvoice_Region(value);
      ScreenData.put("Invoice_Region",value.replace(',', ' '));
    }
    else if (name.compareTo("BANEffectiveDateh")==0)
    {
      setRequired_BAN_Effective_Date(value);
      ScreenData.put("BANEffectiveDateh",value);
    }
    else if (name.compareTo("Billing_Contact")==0)
    {
      setBilling_Contact(value);
      ScreenData.put("Billing_Contact",value);
    }
    else if (name.compareTo("Global_Account_Manager")==0)
    {
      setGlobal_Account_Manager(value);
      ScreenData.put("Global_Account_Manager",value);
    }
    else if (name.compareTo("SAP_Customer_No")==0)
    {
      setSAP_Customer_No(value);
      ScreenData.put("SAP_Customer_No",value);
    }
    else if (name.compareTo("Company_Address_Id")==0)
    {
      setCompany_Address_Id(value);
      ScreenData.put("Company_Address_Id",value);
    }
    else if (name.compareTo("Customer_Contact")==0)
    {
      setCustomer_Contact(value);
      ScreenData.put("Customer_Contact",value);
    }
    else if (name.compareTo("Customer_Billing_Name")==0)
    {
      setCustomer_Billing_Name(value);
      ScreenData.put("Customer_Billing_Name",value);
    }
    else if (name.compareTo("Customer_VAT_Reg_No")==0)
    {
      setCustomer_VAT_Reg_No(value);
      ScreenData.put("Customer_VAT_Reg_No",value);
    }
    else if (name.compareTo("BAN_Tax_Requirement")==0)
    {
      setBAN_Tax_Requirement(value);
      ScreenData.put("BAN_Tax_Requirement",value);
    }
    else if (name.compareTo("Customer_Billing_Address")==0)
    {
      setCustomer_Billing_Address(value);
      ScreenData.put("Customer_Billing_Address",value);
    }
    else if (name.compareTo("Customer_Billing_Country")==0)
    {
      setCustomer_Billing_Country(value);
      ScreenData.put("Customer_Billing_Country",value);
    }
    else if (name.compareTo("Currency_Desc")==0)
    {
      setCurrency_Desc(value);
      ScreenData.put("Currency_Desc",value);
    }
    else if (name.compareTo("Multiple_Billing_Regions")==0)
    {
      setMultiple_Billing_Regions(value);
      ScreenData.put("Multiple_Billing_Regions",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("BillingRegions")==0)
    {
      setBillingRegions(value);
      ScreenData.put("BillingRegions",value);
    }
    else if (name.compareTo("Country_Literal")==0)
    {
      setCountryLiteral(value);
      ScreenData.put("Country_Literal",value);
    }
    else if (name.compareTo("Customer_Contact_Point")==0)
    {
      setCustomerContactPoint(value);
      ScreenData.put("Customer_Contact_Point",value);
    }
    else if (name.compareTo("Strategic_Reports")==0)
    {
      setStrategicReports(value);
      ScreenData.put("Strategic_Reports",value);
    }
    else if (name.equals("Arbor_Bar"))
    {
      setArborBar(value);
    }
    else if (name.equals("PCB_Bar"))
    {
      setPCBBar(value);
    }
    else if (name.equals("CMPLS"))
    {
      setCMPLS(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getMode(String FieldName)
  {//This defines whether or not a field is enterable

    if (action.compareTo("Authorise") == 0)
    {
      return "READONLY";
    }
    if ((FieldName.equals("Customer_Billing_Country")) ||
      (FieldName.equals("BAN_Tax_Requirement")))
    {
      if (Mode.equals("Amend"))
        return "READONLY";
      else
        return ProcessMode;
    }

    if (FieldName.equals("Company_Address_Id"))
    {
      if (Mode.equals("Amend"))
        return "READONLY";
      else
        return SelectMode;
    }

    if (Mode.equals("Amend"))
    {
      if ((!FieldName.equals("Customer_Contact")) &&
        (!FieldName.equals("Global_Account_Manager")) &&
        (!FieldName.equals("Customer_Billing_Name")) &&
        (!FieldName.equals("BAN_Summary")) &&
        (!FieldName.equals("Currency_Desc")) &&
        (!FieldName.equals("SAP_Customer_No")) &&
        (!FieldName.equals("Customer_Contact_Point")) &&
        (!FieldName.equals("Invoice_Region")) &&
        (!FieldName.equals("Customer_Billing_Address")))
        return "READONLY";
      else
        return InputMode;
    }
    if ((FieldName.compareTo("BAN_Summary") ==0) ||
	    (FieldName.compareTo("BAN_Reason") ==0) ||
	    (FieldName.compareTo("Effective_Date") ==0))
    {
      if ((action.compareTo("View") !=0 ) &&
	  (action.compareTo("History") !=0 ) &&
	  (action.compareTo("Authorise") !=0 ))
	  return "";
      else
        return InputMode;
    }
    /*else if (FieldName.equals("SAP_Customer_No"))
    {
      if (mandatory.indexOf("SAP_Customer_No") == -1)
        return "READONLY";
      else
        return InputMode;
    }*/
    else
    {
      return InputMode;
    }
  }

  public boolean getIsMANS()
  {
    return isMANS;
  }
  public String getBilling_Customer_Name()
  {
    return SU.isNull(Billing_Customer_Name,"");
  }
  public String getBilling_Contact()
  {
    return Billing_Contact;
  }
  public String getCurrency_Desc()
  {
    return Currency_Desc;
  }
  public String getCustomerContactPoint()
  {
    return customerContactPoint;
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
  public String getBansToList()
  {
    return bansToList;
  }
  public String getGlobal_Account_Manager()
  {
   return globalAccountManager;
  }
  public String getCustomer_Contact()
  {
   return customerContact;
  }
  public String getCountryLiteral()
  {
   return countryLiteral;
  }
  public String getCustomer_Billing_Name()
  {
   return customerBillingName;
  }
  public String getCustomer_Billing_Address()
  {
   return customerBillingAddress;
  }
  public String getCustomer_Billing_Country()
  {
   return customerBillingCountry;
  }
  public String getCompany_Address_Id()
  {
   return companyAddressId;
  }
  public String getBAN_Tax_Requirement()
  {
   return banTaxRequirement;
  }
  public String getMultiple_Billing_Regions()
  {
   return multipleBillingRegions;
  }
  public String getStrategicReports()
  {
   return strategicReports;
  }
  public String getSAP_Customer_No()
  {
   return sapCustomerNo;
  }
  public String getCustomer_VAT_Reg_No()
  {
   return customerVatRegNo;
  }
  public String getBilling_Regions()
  {
   return billingRegions;
  }
  public String getCMPLS()
  {
   return cmpls;
  }
  public String getArborBar()
  {
   return arborBar;
  }
  public String getPCBBar()
  {
   return pcbBar;
  }
  public String getBillingRegionsListBox()
  {
    String listBox = null;
    listBox = DBA.getListBox("BAN_Billing_Region",InputMode,"", banIdentifier,
      3,"style=\"height:80;width:400\"",false);
    if (listBox.indexOf("</SELECT>") == -1)
    {
      StringBuffer sb = new StringBuffer(listBox);
      int start = 0;
      int pos = billingRegions.indexOf(",");
      while (pos > 0)
      {
        sb.append("<option value=\"");
        sb.append(billingRegions.substring(start, pos));
        sb.append("\">");
        sb.append(billingRegions.substring(start, pos));
        sb.append("</option>");
        start = ++pos;
        pos = billingRegions.indexOf(",", start);
      }
      sb.append("</SELECT>");
      listBox = sb.toString();
    }
    return listBox;
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
public boolean isValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";
        String mbr=SU.isNull((String)ScreenData.get("Multiple_Billing_Regions"),"");

	if ((ButtonPressed.startsWith("Submit")) &&
          (Mode.compareTo("Cease") !=0))
	{
	  while (FormFields.hasMoreElements())
	  {
	    FieldName=(String)FormFields.nextElement();
	    FormField=SU.isNull((String)ScreenData.get(FieldName),"");
	    if(FormField.compareTo("") == 0)
	    {
              if ((FieldName.equals("New_Billing_Region")) ||
                (FieldName.equals("BAN_Billing_Region")))
              {
                if (mbr.equals("Y"))
                  setErrored(FieldName);
              }
              else
	        setErrored(FieldName);
	    }
            else
            {
              if (FieldName.equals("Customer_Billing_Address"))
              {
                int count = 0;
                Enumeration Billing_Address_Lines =
                  SU.unpackTextArea(FormField, tokenDelimiter);
                while (Billing_Address_Lines.hasMoreElements())
                {
                  Object temp = Billing_Address_Lines.nextElement();
                  count++;
                }
                if (count > 5)
                {
                  setErrored(FieldName);
                  Message = "<font color=red><b>Please enter no more than 5 address lines";
                  return false;
                }
              }
              else if (FieldName.equals("Invoice_Region"))
              {
                if (Mode.equals("Add"))
                {
                  String ir = FormField.trim();
                  if (!ir.equals(FormField))
                  {
                    setErrored(FieldName);
                    Message = "<font color=red><b>Invoice Region name cannot " +
                      "start or end with spaces. Spaces removed.";
                    setInvoice_Region(ir);
                    ScreenData.put("Invoice_Region",ir);
                    return false;
                  }
                  if (!SU.isAlphaNumericOnly(ir.substring(0,1)))
                  {
                    setErrored(FieldName);
                    Message = "<font color=red><b>Invoice Region name cannot " +
                      "start with non-alphanumeric character. Character removed.";
                    setInvoice_Region(ir.substring(1));
                    ScreenData.put("Invoice_Region",ir.substring(1));
                    return false;
                  }
                  if (SU.containsUnfriendlyChars(ir))
                  {
                    setErrored(FieldName);
                    Message = "<font color=red><b>Invoice Region name contains " +
                      "non-alphanumeric characters which could cause database problems. Characters removed.";
                    setInvoice_Region(SU.removeUnfriendlyChars(ir, false));
                    ScreenData.put("Invoice_Region",SU.removeUnfriendlyChars(ir, false));
                    return false;
                  }
                  if (DBA.invoiceRegionExists(Global_Customer_Id, ir))
                  {
                    setErrored(FieldName);
                    Message = "<font color=red><b>Invoice Region exists already for this customer";
                    return false;
                  }
                }
              }
            }
	  }
        }

	if ((!errored.isEmpty()) && (errored.size() > 0))
	{
	    Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
	    return false;
	}
	else if (!DBA.invoiceRegionRefDataExists(Currency_Desc, companyAddressId))
        {
          setErrored("Currency_Desc");
          setErrored("Company_Address_Id");
          Message = "<font color=red><b>This combination of Billing Currency and Company Address Id is invalid";
          return false;
	}
	else
        {
	  FormField=SU.isNull((String)ScreenData.get("SAP_Customer_No"),"");
          if (!FormField.equals(""))
          {
            if ((FormField.length() < 6) || (FormField.length() > 10))
            {
              setErrored("SAP_Customer_No");
              Message = "<font color=red><b>SAP Customer No must be between 6 and 10 characters";
              return false;
            }
            else if (!SU.isNumeric(FormField))
            {
              setErrored("SAP_Customer_No");
              Message = "<font color=red><b>SAP Customer No must be numeric";
              return false;
            }
          }
	  return true;
        }
}
/*******************************************************************************/
public boolean getInvoiceRegionBAN()
{
    boolean getInvoiceRegionBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Invoice_Region_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Global_Customer_Id=RS.getString(1);
	    Invoice_Region=RS.getString(2);
	    customerContact=RS.getString(3);
	    globalAccountManager=RS.getString(4);
	    sapCustomerNo=RS.getString(5);
	    companyAddressId=RS.getString(6);
	    customerBillingName=RS.getString(7);
    	    customerVatRegNo=RS.getString(8);
	    banTaxRequirement=RS.getString(9);
	    Billing_Address[0]=RS.getString(10);
	    Billing_Address[1]=RS.getString(11);
	    Billing_Address[2]=RS.getString(12);
	    Billing_Address[3]=RS.getString(13);
	    Billing_Address[4]=RS.getString(14);
	    Billing_Address[5]=RS.getString(15);
	    Billing_Address[6]=RS.getString(16);
	    customerBillingCountry=RS.getString(17);
	    Currency_Desc=RS.getString(18);
	    multipleBillingRegions=RS.getString(19);
	    Required_BAN_Effective_Dateh=RS.getDate(20);
	    BAN_Summary=RS.getString(21);
	    Mode=RS.getString(22);
	    banStatus=(RS.getString(23));
	    customerContactPoint=RS.getString(24);
            restrictionType = RS.getString(25);
            origInvoiceRegionName = RS.getString(26);
            arborBar = RS.getString(27);
            pcbBar = RS.getString(28);
            cmpls = RS.getString(29);
            strategicReports = restrictionType==null?"N":restrictionType.equals("Strategic")?"Y":"N";
            setDates();
            /*for (int i=6; i>=0; i--)
            {
              if (!Billing_Address[i].equals(" "))
              {
                countryLiteral = Billing_Address[i];
                Billing_Address[i] = "";
                break;
              }
              else
              {
                Billing_Address[i] = "";
              }
            }*/
	    customerBillingAddress=SU.packTextArea(Billing_Address, tokenDelimiter);
            if (multipleBillingRegions.equals("Y"))
            {
              getInvoiceRegionBAN = getBillingRegionsFromTable(true);
            }
            else
            {
              getInvoiceRegionBAN = true;
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
      return getInvoiceRegionBAN;
    }
}
/*******************************************************************************/
public boolean getInvoiceRegion()
{
    boolean getInvoiceRegion = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call givn_ref..get_account (?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Account_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Invoice_Region=RS.getString(1);
            origInvoiceRegionName = Invoice_Region;
	    globalAccountManager=RS.getString(2);
	    customerBillingName=RS.getString(3);
	    customerContact=RS.getString(4);
	    Billing_Address[0]=RS.getString(5);
	    Billing_Address[1]=RS.getString(6);
	    Billing_Address[2]=RS.getString(7);
	    Billing_Address[3]=RS.getString(8);
	    Billing_Address[4]=RS.getString(9);
	    Currency_Desc=RS.getString(10);
            ScreenData.put("Currency_Desc",Currency_Desc);
	    setCompany_Address_Id(RS.getString(11));
            ScreenData.put("Company_Address_Id",companyAddressId);
	    sapCustomerNo=RS.getString(12).trim();
    	    customerVatRegNo=RS.getString(13);
	    banTaxRequirement=RS.getString(19);
            ScreenData.put("BAN_Tax_Requirement",banTaxRequirement);
	    customerBillingCountry=RS.getString(15);
	    customerContactPoint=RS.getString(16);
            restrictionType = RS.getString(17);
            multipleBillingRegions = RS.getInt(18)>1?"Y":"N";
            arborBar = RS.getString(20);
            pcbBar = RS.getString(21);
            cmpls = RS.getString(22);
            strategicReports = restrictionType==null?"N":restrictionType.equals("Strategic")?"Y":"N";
            setDates();
	    customerBillingAddress=SU.packTextArea(Billing_Address, tokenDelimiter);
            ScreenData.put("Customer_Billing_Country",customerBillingCountry);
            //if (multipleBillingRegions.equals("Y"))
            //{
              getInvoiceRegion = getBillingRegionsFromTable(false);
            /*}
            else
            {
              getInvoiceRegion = true;
            }*/
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
public boolean createInvoiceRegionBAN()
{
  boolean createInvoiceRegionBAN = false;
  int i=0;
  Enumeration Billing_Address_Lines;
    try
    {
      Billing_Address = new String[8];
      Billing_Address_Lines = SU.unpackTextArea(customerBillingAddress, tokenDelimiter);

      while ((Billing_Address_Lines.hasMoreElements()) && (i < 7))
      {
	  Billing_Address[i]=(String)Billing_Address_Lines.nextElement();
	  i++;
      }
      /*if (i < 7)
      {
	Billing_Address[i]=countryLiteral;
	i++;
        while (i < 7)
        {
          Billing_Address[i]="";
          i++;
        }
      }
      else
      {
	Billing_Address[6]=countryLiteral;
      }*/
      if (!Mode.equals("Amend"))
      {
        origInvoiceRegionName = Invoice_Region;
      }
      /*if ((multipleBillingRegions.equals("Y")) &&
        (billingRegions.indexOf(",") == billingRegions.length()-1))
      {
        multipleBillingRegions = "N";
      }*/
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Invoice_Region_BAN " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
	cstmt.setString(2,Global_Customer_Id);
        cstmt.setString(3,Invoice_Region);
	cstmt.setString(4,customerContact);
	cstmt.setString(5,globalAccountManager);
	cstmt.setString(6,sapCustomerNo);
	cstmt.setString(7,companyAddressId);
	cstmt.setString(8,customerBillingName);
	cstmt.setString(9,customerVatRegNo);
	cstmt.setString(10,banTaxRequirement);
	cstmt.setString(11,Billing_Address[0]);
	cstmt.setString(12,Billing_Address[1]);
	cstmt.setString(13,Billing_Address[2]);
	cstmt.setString(14,Billing_Address[3]);
	cstmt.setString(15,Billing_Address[4]);
	cstmt.setString(16,Billing_Address[5]);
	cstmt.setString(17,Billing_Address[6]);
	cstmt.setString(18,customerBillingCountry);
	cstmt.setString(19,Currency_Desc);
	cstmt.setString(20,multipleBillingRegions);
	cstmt.setString(21,billingRegions);
	cstmt.setString(22,BAN_Summary);
        cstmt.setDate(23,Required_BAN_Effective_Dateh);
        cstmt.setString(24,Mode);
	cstmt.setString(25,banCreatedBy);
	cstmt.setString(26,customerContactPoint);
        if ((strategicReports.equals("Y")) &&
          ((restrictionType == null) || (!restrictionType.equals("Strategic"))))
        {
          restrictionType = "Strategic";
        }
	cstmt.setString(27,restrictionType);
	cstmt.setString(28,origInvoiceRegionName);
	cstmt.setString(29,arborBar);
	cstmt.setString(30,pcbBar);
	cstmt.setString(31,cmpls);

        try
        {
          cstmt.execute();
          RS = cstmt.getResultSet();
          if (RS.next())
          {
            banIdentifier=RS.getString(1);
            createInvoiceRegionBAN = true;
            StringBuffer msg = new StringBuffer("<font color=blue><b>BAN Id:-" +
              banIdentifier + " created.");
            if ((Mode.equals("Amend")) && (!Invoice_Region.equals(origInvoiceRegionName)))
            {
              msg.append(" Any existing trial bills will need to be rerun to reflect " +
              "the invoice region name change.");
              if (multipleBillingRegions.equals("Y"))
              {
                msg.append(" Please check Billing Region names for relevance.");
              }
            }
            Message = msg.toString();
          }
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
      return createInvoiceRegionBAN;
    }
  }
/**********************************************************************************************/
  public boolean updateInvoiceRegionBAN()
  {
    boolean updateInvoiceRegionBAN = false;
    int rowcount=0,i=0;
    Enumeration Billing_Address_Lines;

    try
    {
      Billing_Address = new String[8];
      Billing_Address_Lines = SU.unpackTextArea(customerBillingAddress, tokenDelimiter);

      while ((Billing_Address_Lines.hasMoreElements()) && (i < 7))
      {
	  Billing_Address[i]=(String)Billing_Address_Lines.nextElement();
	  i++;
      }
      if (i < 7)
      {
	Billing_Address[i]=customerBillingCountry;
	i++;
        while (i < 7)
        {
          Billing_Address[i]="";
          i++;
        }
      }
      else
      {
	Billing_Address[6]=customerBillingCountry;
      }
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_Invoice_Region_BAN " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,Global_Customer_Id);
        cstmt.setString(4,Invoice_Region);
	cstmt.setString(5,customerContact);
	cstmt.setString(6,globalAccountManager);
	cstmt.setString(7,sapCustomerNo);
	cstmt.setString(8,companyAddressId);
	cstmt.setString(9,customerBillingName);
	cstmt.setString(10,customerVatRegNo);
	cstmt.setString(11,banTaxRequirement);
	cstmt.setString(12,Billing_Address[0]);
	cstmt.setString(13,Billing_Address[1]);
	cstmt.setString(14,Billing_Address[2]);
	cstmt.setString(15,Billing_Address[3]);
	cstmt.setString(16,Billing_Address[4]);
	cstmt.setString(17,Billing_Address[5]);
	cstmt.setString(18,Billing_Address[6]);
	cstmt.setString(19,Currency_Desc);
	cstmt.setString(20,multipleBillingRegions);
	cstmt.setString(21,billingRegions);
	cstmt.setString(22,BAN_Summary);
        cstmt.setDate(23,Required_BAN_Effective_Dateh);
        cstmt.setString(24,rejectReason);
	cstmt.setString(25,banCreatedBy);
	cstmt.setString(26,customerContactPoint);
        if ((strategicReports.equals("Y")) &&
          ((restrictionType == null) || (!restrictionType.equals("Strategic"))))
        {
          restrictionType = "Strategic";
        }
	cstmt.setString(27,restrictionType);
	cstmt.setString(28,arborBar);
	cstmt.setString(29,pcbBar);
	cstmt.setString(30,cmpls);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
        if (rowcount != 1)
        {
          Message="Unexpected! - "+Integer.toString(rowcount) + " Rows Updated";
        }
        else
        {
          Message="<font color=blue><b>Invoice Region BAN amended" +
              ((Mode.equals("Amend")&&!Invoice_Region.equals(origInvoiceRegionName))
              ?". Any existing trial bills will need to be rerun to reflect the invoice region name change.":"");
          updateInvoiceRegionBAN = true;
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
      return updateInvoiceRegionBAN;
    }
  }
 /*********************************************************************/
public boolean AuthoriseInvoiceRegionBAN()
{
  boolean AuthoriseInvoiceRegionBAN = false;
  int i=0;
  Enumeration Billing_Address_Lines;
  try
  {
    if (Mode.equals("Create"))
    {
      Billing_Address = new String[8];
      Billing_Address_Lines = SU.unpackTextArea(customerBillingAddress, tokenDelimiter);

      while ((Billing_Address_Lines.hasMoreElements()) && (i < 7))
      {
          Billing_Address[i]=(String)Billing_Address_Lines.nextElement();
          i++;
      }
      /*if (i < 7)
      {
        Billing_Address[i]=countryLiteral;
        i++;
        while (i < 7)
        {
          Billing_Address[i]="";
          i++;
        }
      }
      else
      {
        Billing_Address[6]=countryLiteral;
      }*/
    }
    if (DBA.Connect(WRITE,P3))
    {
      if (Mode.equals("Add"))
      {
        //SQL = "{call givn_ref..Create_Invoice_Region " +
        SQL = "{call givn_ref..Create_Invoice_Region " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
        cstmt.setString(3,Global_Customer_Id);
        cstmt.setString(4,Invoice_Region.replace(',', ' '));
        cstmt.setString(5,customerContact);
        cstmt.setString(6,globalAccountManager);
        cstmt.setString(7,sapCustomerNo);
        cstmt.setString(8,companyAddressId);
        cstmt.setString(9,customerBillingName);
        cstmt.setString(10,customerVatRegNo);
        cstmt.setString(11,banTaxRequirement);
        cstmt.setString(12,Billing_Address[0]);
        cstmt.setString(13,Billing_Address[1]);
        cstmt.setString(14,Billing_Address[2]);
        cstmt.setString(15,Billing_Address[3]);
        cstmt.setString(16,Billing_Address[4]);
        cstmt.setString(17,Billing_Address[5]);
        cstmt.setString(18,Billing_Address[6]);
        cstmt.setString(19,customerBillingCountry);
        cstmt.setString(20,Currency_Desc);
        cstmt.setString(21,multipleBillingRegions);
        cstmt.setString(22,billingRegions);
        cstmt.setString(23,BAN_Summary);
        cstmt.setDate(24,Required_BAN_Effective_Dateh);
        cstmt.setString(25,Mode);
        cstmt.setString(26,banCreatedBy);
        cstmt.setString(27,customerContactPoint);
        if ((strategicReports.equals("Y")) &&
          ((restrictionType == null) || (!restrictionType.equals("Strategic"))))
        {
          restrictionType = "Strategic";
        }
	cstmt.setString(28,restrictionType);
	cstmt.setString(29,arborBar);
	cstmt.setString(30,pcbBar);
	cstmt.setString(31,cmpls);
      }
      else
      {
        SQL = "{call givn_ref..Update_Invoice_Region (?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
      }

      try
      {
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          //int retcode=RS.getInt(1);
          //if (retcode == -1)
          String retcode=RS.getString(1);
          if (retcode.equals("-2"))
          {
            Message = "<font color=red><b>BAN Id:-"+banIdentifier+" created " +
                "but at least one Billing Region selected for deletion is used " +
                "by a site and cannot be deleted.";
          }
          else if (retcode.equals("-99"))
          {
            Message = "<font color=red><b>Unable to implement BAN. " +
              "A bill job is currently running for this customer.";
          }
          else if (retcode.startsWith("-"))
          {
            Message = "<font color=red><b>Unable to implement BAN. " +
              "Please contact Systems Support.";
          }
          else
          {
            AuthoriseInvoiceRegionBAN = true;
            //String accId = Integer.toString(retcode);
            //String accountId = TEN_ZEROS.substring(0, (10-accId.length())) + accId;
            StringBuffer msg = new StringBuffer("<font color=blue><b>You " +
              "have successfully implemented BAN Id :-" + banIdentifier +
              " Account Id :-" + retcode + ".");
            if ((Mode.equals("Amend")) && (!Invoice_Region.equals(origInvoiceRegionName)))
            {
              msg.append("</b></font><font color=red><b> Any existing trial " +
                "bills will need to be rerun and any adhoc invoices recreated " +
                "to reflect the invoice region name change.");
              if (multipleBillingRegions.equals("Y"))
              {
                msg.append(" Please also check Billing Region names for relevance.");
              }
            }
            Message = msg.toString();
          }
        }
        /*String accountId = DBA.getAccountIdForBAN(banIdentifier);
        if (accountId == null)
        {
          Message = "<font color=red><b>Unable to implement BAN. " +
            "Please contact Systems Support.";
        }
        else
        {
          AuthoriseInvoiceRegionBAN = true;
          Message = "<font color=blue><b>You have successfully implemented BAN Id :-"+
            banIdentifier + " Account Id :-" + accountId +
            (Mode.equals("Amend")?" updated":" created");
        }*/
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
    return AuthoriseInvoiceRegionBAN;
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
/************************************************************************************************/
  public int getBANEffectiveDateDays()
  {
    return BEDays;
  }
/************************************************************************************************/
  public void setBANEffectiveDateDays(String value)
  {
    BEDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBANEffectiveDateMonths()
  {
    return BEMonths;
  }
/************************************************************************************************/
  public void setBANEffectiveDateMonths(String value)
  {
    BEMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBANEffectiveDateYears()
  {
    return BEYears;
  }
/************************************************************************************************/
  public void setBANEffectiveDateYears(String value)
  {
    BEYears = value.equals("")?0:Integer.parseInt(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(Required_BAN_Effective_Dateh==null?(new java.util.Date())
      :Required_BAN_Effective_Dateh);
    BEDays=cal.get(cal.DATE);
    BEMonths=cal.get(cal.MONTH)+1;
    BEYears=cal.get(cal.YEAR);
  }
/*************************************************************************************/
  private boolean getBillingRegionsFromTable(boolean fromBAN)
  {
    boolean getBillingRegionsFromTable = false;
    StringBuffer sb = new StringBuffer();
    try
    {
      SQL = fromBAN?("SELECT Billing_Region FROM eban..IR_BAN_Billing_Region " +
        "WHERE BAN_Identifier='" + banIdentifier + "' "):
        ("SELECT Billing_Region FROM givn_ref..Billing_Region " +
        "where Account_Id = '" + Account_Id + "' ");
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          sb.append(RS.getString(1)+",");
        }
        billingRegions=sb.toString();
        getBillingRegionsFromTable = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
      billingRegions="";
    }
    finally
    {
      close();
      return getBillingRegionsFromTable;
    }
  }
/*************************************************************************************/
  //for debugging
  /*public static void main (String[] args)
  {
    boolean OK;
    //HttpServletRequest request = null;
    InvoiceRegionBANBean iBAN=new InvoiceRegionBANBean();
    CustomerBANBean cBAN=new CustomerBANBean();
    cBAN.setBanIdentifier("nmb");
    //cBAN.isValid();
 }
 */
}
