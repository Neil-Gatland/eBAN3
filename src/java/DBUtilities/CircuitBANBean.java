package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class CircuitBANBean extends BANBean
{
    private String SQL;
    private String Product_Type="",Speed="",Customer_Reference="",Circuit_Description="";
    private String FromEnd_Site_Reference="",FromEnd_Site_Address="",FromEnd_City="",FromEnd_Country="",FromEnd_Site_Address_Lines[]={"","","","",""};
    private String ToEnd_Site_Reference="",ToEnd_Site_Address="",ToEnd_City="",ToEnd_Country="",ToEnd_Site_Address_Lines[]={"","","","",""};
    private String GCB_Product_Type="",Circuit_Status="L",Circuit_Int_Designator="",Carrier="";
    private String Contract_Number="",C00_Number="",Initial_Period_Days="",Initial_Period_Months="";
    private String Fixed_Charge_Period="",Invoice_Option="",Split_Billing="N",VAT_Code="SR",Bill_Option="";
    private String Billing_Frequency="Quarterly",Following_Invoice_Type="",Next_Invoice_Type="";
    private String Billed_By="",General_Ledger_Code="",Account_Text_Override="",C03_Number="";
    protected Vector mandatory=new Vector();
    protected  final int NORESULT = 4;
    private float Local_Tax_Rate=0;
    private java.sql.Date Billing_Start_Dateh,Billing_End_Dateh,Contract_Dateh;
    private Hashtable ScreenData=new Hashtable();
    private int BEDays, BEMonths, BEYears, BiSDays, BiSMonths, BiSYears, BiEDays, BiEMonths, BiEYears;
    private String c3RefNo;
    private String vatRate = JavaUtil.EBANProperties.getEBANProperty("vatRate");

  public CircuitBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("GCB_Product_Type");
    mandatory.addElement("Circuit_Reference");
    mandatory.addElement("Circuit_Description");
    mandatory.addElement("FromEnd_Site_Reference");
    mandatory.addElement("Billing_Start_Dateh");
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("GCD_Id");
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    BiSDays=0;
    BiSMonths=0;
    BiSYears=0;
    BiEDays=0;
    BiEMonths=0;
    BiEYears=0;
    c3RefNo="";

    errored.addElement("");
  }
  public void Reset()
  {
    super.Reset();
    Circuit_Reference="";
    Product_Type="";
    Speed="";
    Customer_Reference="";
    Circuit_Description="";
    FromEnd_Site_Reference="";
    FromEnd_Site_Address="";
    FromEnd_City="";
    FromEnd_Country="";
    FromEnd_Site_Address_Lines[0]=FromEnd_Site_Address_Lines[1]=FromEnd_Site_Address_Lines[2]=FromEnd_Site_Address_Lines[3]=FromEnd_Site_Address_Lines[4]="";
    ToEnd_Site_Reference="";
    ToEnd_Site_Address="";
    ToEnd_City="";
    ToEnd_Country="";
    ToEnd_Site_Address_Lines[0]=ToEnd_Site_Address_Lines[1]=ToEnd_Site_Address_Lines[2]=ToEnd_Site_Address_Lines[3]=ToEnd_Site_Address_Lines[4]="";
    GCB_Product_Type="";
    Circuit_Status="L";
    Circuit_Int_Designator="";
    Carrier="";
    Contract_Number="";
    C00_Number="";
    Initial_Period_Days="0";
    Initial_Period_Months="0";
    Fixed_Charge_Period="0";
    Invoice_Option="";
    Split_Billing="N";
    VAT_Code="SR";
    Bill_Option="";
    Billing_Frequency="Quarterly";
    Following_Invoice_Type="";
    Next_Invoice_Type="";
    Billed_By="";
    General_Ledger_Code="";
    Account_Text_Override="";
    C03_Number="";
    Local_Tax_Rate=0;
    Billing_Start_Dateh=null;
    Billing_End_Dateh=null;
    Contract_Dateh=null;
    setErrored("clear");
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    BiSDays=0;
    BiSMonths=0;
    BiSYears=0;
    BiEDays=0;
    BiEMonths=0;
    BiEYears=0;
    c3RefNo="";
  }

/*set Methods*/
  public void setFromEnd_Site_Reference(String value)
  {
   FromEnd_Site_Reference = value;
   ScreenData.put("FromEnd_Site_Reference",FromEnd_Site_Reference);
  }
  public void setFromEnd_Site_Address(String value)
  {
   FromEnd_Site_Address = SU.isNull(value,"");
   ScreenData.put("FromEnd_Site_Address",FromEnd_Site_Address);
  }
  public void setFromEnd_City(String value)
  {
   FromEnd_City = value;
   ScreenData.put("FromEnd_City",FromEnd_City);
  }
  public void setFromEnd_Country(String value)
  {
   FromEnd_Country = value;
   ScreenData.put("FromEnd_Country",FromEnd_Country);
  }
  public void setToEnd_Site_Reference(String value)
  {
   ToEnd_Site_Reference = value;
   ScreenData.put("ToEnd_Site_Reference",ToEnd_Site_Reference);
  }
  public void setToEnd_Site_Address(String value)
  {
   ToEnd_Site_Address = value;
   ScreenData.put("ToEnd_Site_Address",ToEnd_Site_Address);
  }
  public void setToEnd_City(String value)
  {
   ToEnd_City = value;
   ScreenData.put("FromEnd_City",FromEnd_City);
  }
  public void setToEnd_Country(String value)
  {
   ToEnd_Country = value;
   ScreenData.put("ToEnd_Country",ToEnd_Country);
  }
  public void setGCB_Product_Type(String value)
  {
    GCB_Product_Type = value;
    ScreenData.put("GCB_Product_Type",GCB_Product_Type);
    if (Invoice_Option.compareToIgnoreCase("")==0)
    {
      if (value.compareToIgnoreCase("OSSP")==0)
      {
        Invoice_Option="Participating";
      }
      else
      {
        Invoice_Option="Coordinating";
      }
    }
    if (GCB_Product_Type.equals("LLUF"))
    {
      if (mandatory.indexOf("Circuit_Speed") == -1)
      {
        mandatory.add("Circuit_Speed");
      }
      if (mandatory.indexOf("C2_Ref_No") == -1)
      {
        mandatory.add("C2_Ref_No");
      }
    }
    else
    {
      if (mandatory.indexOf("Circuit_Speed") != -1)
        mandatory.remove("Circuit_Speed");
      if (mandatory.indexOf("C2_Ref_No") != -1)
        mandatory.remove("C2_Ref_No");
    }
  }
  public void setProduct_Type(String value)
  {
    StringBuffer SQLBuffer = new StringBuffer("");
    Enumeration Results;
    Product_Type = SU.isNull(value,"");
    ScreenData.put("Product_Type",value);

/*   if ((Product_Type.compareTo("")!=0) &&
      (Fixed_Charge_Period.compareTo("")==0) &&
      (Initial_Period_Days.compareTo("")==0) &&
      (Initial_Period_Months.compareTo("")==0))
   {
      SQLBuffer.append("Select Initial_Period,Fixed_Period from OSS..Product_Type");
      SQLBuffer.append(" where GCB_Product_Type = '").append(GCB_Product_Type);
      SQLBuffer.append("' and Tariff_Code = '").append(Product_Type).append("'");
      DBA.setDB("OSS");
      Results=DBA.getResults(SQLBuffer.toString());
      if(Results.hasMoreElements())
      {
        setInitial_Period_Days((String)Results.nextElement());
        if(Results.hasMoreElements())
        {
          setFixed_Charge_Period((String)Results.nextElement());
        }
      }
    }*/
  }
  public String getC3RefNo()
  {
    return c3RefNo;
  }
  public String getLLUC2RefNo()
  {
    return c2RefNo;
  }
  public void setC3RefNo(String value)
  {
    c3RefNo = value;
   ScreenData.put("C3_Ref_No",value);
  }
  public void setSpeed(String value)
  {
   Speed = value;
   ScreenData.put("Circuit_Speed",value);
  }
  public void setCircuit_Status(String value)
  {
   Circuit_Status = value;
   ScreenData.put("Circuit_Status",value);
  }
  public void setBilling_Start_Date(String value)
  {
    Billing_Start_Dateh = SU.toJDBCDate(value);
    ScreenData.put("Billing_Start_Dateh",value);
  }
  public void setBilling_End_Date(String value)
  {
    Billing_End_Dateh = SU.toJDBCDate(value);
    ScreenData.put("Billing_End_Date",value);
  }
  public void setCustomer_Reference(String value)
  {
   Customer_Reference = SU.removeUnfriendlyChars(SU.isNull(value,""), false);
   ScreenData.put("Customer_Reference",Customer_Reference);
  }
  public void setCircuit_Int_Designator(String value)
  {
    Circuit_Int_Designator=value;
    ScreenData.put("Circuit_Int_Designator",value);
  }
  public void setCarrier(String value)
  {
    Carrier=value;
    ScreenData.put("Carrier",value);
  }
  public void setCircuit_Description(String value)
  {
   Circuit_Description = SU.removeUnfriendlyChars(SU.isNull(value,""), false);
   ScreenData.put("Circuit_Description",value);
  }
  public void setContract_Number(String value)
  {
   Contract_Number = SU.isNull(value,"");
   ScreenData.put("Contract_Number",value);
  }
  public void setC00_Number(String value)
  {
   C00_Number = SU.isNull(value,"");
   ScreenData.put("C00_Number",value);
  }
  public void setBilled_By(String value)
  {
   Billed_By = SU.isNull(value,"");
   ScreenData.put("Billed_By",value);
  }
  public void setInitial_Period_Days(String value)
  {
   Initial_Period_Days = SU.isNull(value,"");
   ScreenData.put("Initial_Period_Days",value);
  }
  public void setInitial_Period_Months(String value)
  {
   Initial_Period_Months = SU.isNull(value,"");
   ScreenData.put("Initial_Period_Months",value);
  }
  public void setFixed_Charge_Period(String value)
  {
   Fixed_Charge_Period = SU.isNull(value,"");
   ScreenData.put("Fixed_Charge_Period",Fixed_Charge_Period);
  }
  public void setContract_Date(String value)
  {
    Contract_Dateh = SU.toJDBCDate(value);
    ScreenData.put("Contract_Date",value);
  }
  public void setVAT_Code(String value)
  {
   VAT_Code = SU.isNull(value,"");
   ScreenData.put("VAT_Code",value);
  }
  public void setInvoice_Option(String value)
  {
   Invoice_Option = SU.isNull(value,"");
   ScreenData.put("Invoice_Option",value);
  }
  public void setSplit_Billing(String value)
  {
   Split_Billing = SU.isNull(value,"");
   ScreenData.put("Split_Billing",Split_Billing);
  }
  public void setBill_Option(String value)
  {
   Bill_Option = SU.isNull(value,"");
   ScreenData.put("Bill_Option",Bill_Option);
  }
  public void setBilling_Frequency(String value)
  {
    Billing_Frequency=SU.isNull(value,"");
    ScreenData.put("Billing_Frequency",Billing_Frequency);
  }
  public void setC03_Number(String value)
  {
    C03_Number=SU.isNull(value,"");
    ScreenData.put("C03_Number",C03_Number);
  }
  public void setCircuitRefFromBAN(String BAN_Id)
  {
    StringBuffer SQLBuffer = new StringBuffer("");

    if (BAN_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Circuit_Reference from eban..OSS_Charge_BAN");
      SQLBuffer.append(" where BAN_Identifier = '").append(BAN_Id);
      SQLBuffer.append("'");

      setCircuit_Reference(DBA.getValue(SQLBuffer.toString(),P5));
      //Datasource changes setCircuit_Reference(DBA.getValue(SQLBuffer.toString()));
    }
  }
  /*set BAN values from form*/
/*public void setBANvalues(HttpServletRequest request)
{
  Enumeration FormFields;
  FormFields=request.getParameterNames();
  String FormField;

  while (FormFields.hasMoreElements())
  {
    FormField=(String)FormFields.nextElement();
    setParameter(FormField,request.getParameter(FormField));
  }
}*/
  public void setCircuit_Reference(String value)
  {
   super.setCircuit_Reference(SU.removeUnfriendlyChars(value, false));
   ScreenData.put("Circuit_Reference",Circuit_Reference);
  }
  public void setRequired_BAN_Effective_Date(String value)
  {
    super.setRequired_BAN_Effective_Date(value);
    ScreenData.put("Required_BAN_Effective_Dateh",value);
  }
    public void setBAN_Summary(String value)
  {
    super.setBAN_Summary(value);
    ScreenData.put("BAN_Summary",BAN_Summary);
  }
  public void setMode(String value)
  {
    super.setMode(value);
    if (Mode.compareTo("Cease")==0)
    {
	Circuit_Status="Cancelled";
    }
  }
  /*public void setAction(String value)
  {
    super.setAction(value);
    //if (value.compareTo("Create")==0)
    //{
     // Reset();
      //banIdentifier="";
    //}
  }*/
  /****************************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.startsWith("BAN_Summary"))
    {
      setBAN_Summary(value);
    }
    else if (name.startsWith("BAN_Reason"))
    {
     setBAN_Reason(value);
    }
    else if (name.startsWith("Required_BAN_Effective_Dateh"))
    {
      setRequired_BAN_Effective_Date(value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateDay"))
    {
      setBANEffectiveDateDays(value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateMonth"))
    {
      setBANEffectiveDateMonths(value);
    }
    else if (name.startsWith("Required_BAN_Effective_DateYear"))
    {
      setBANEffectiveDateYears(value);
    }
    else if (name.startsWith("Product_Type"))
    {
      setProduct_Type(value);
    }
    else if (name.startsWith("GCB_Product_Type"))
    {
      setGCB_Product_Type(value);
    }
    else if (name.startsWith("FromEnd_Site_Reference"))
    {
      setFromEnd_Site_Reference(value);
    }
    else if (name.startsWith("FromEnd_Site_Address"))
    {
      setFromEnd_Site_Address(value);
    }
     else if (name.startsWith("FromEnd_City"))
    {
      setFromEnd_City(value);
    }
     else if (name.startsWith("FromEnd_Country"))
    {
      setFromEnd_Country(value);
    }
    else if (name.startsWith("ToEnd_Site_Reference"))
    {
      setToEnd_Site_Reference(value);
    }
   else if (name.startsWith("ToEnd_Site_Address"))
    {
      setToEnd_Site_Address(value);
    }
     else if (name.startsWith("ToEnd_City"))
    {
      setToEnd_City(value);
    }
     else if (name.startsWith("ToEnd_Country"))
    {
      setToEnd_Country(value);
    }
    else if (name.startsWith("Billing_Start_Dateh"))
    {
      setBilling_Start_Date(value);
    }
    else if (name.startsWith("Billing_Start_DateDay"))
    {
      setBillingStartDateDays(value);
    }
    else if (name.startsWith("Billing_Start_DateMonth"))
    {
      setBillingStartDateMonths(value);
    }
    else if (name.startsWith("Billing_Start_DateYear"))
    {
      setBillingStartDateYears(value);
    }
    else if (name.startsWith("Billing_End_Dateh"))
    {
      setBilling_End_Date(value);
    }
    else if (name.startsWith("Billing_End_DateDay"))
    {
      setBillingEndDateDays(value);
    }
    else if (name.startsWith("Billing_End_DateMonth"))
    {
      setBillingEndDateMonths(value);
    }
    else if (name.startsWith("Billing_End_DateYear"))
    {
      setBillingEndDateYears(value);
    }
    else if (name.startsWith("BAN_Summary"))
    {
      setBAN_Summary(value);
    }
    else if (name.startsWith("BAN_Reason"))
    {
      setBAN_Reason(value);
    }
    else if (name.startsWith("Circuit_Speed"))
    {
      setSpeed(value);
    }
    else if (name.startsWith("Circuit_Reference"))
    {
      setCircuit_Reference(value);
    }
    else if (name.startsWith("Circuit_Description"))
    {
      setCircuit_Description(value);
    }
    else if (name.startsWith("Carrier"))
    {
      setCarrier(value);
    }
    else if (name.startsWith("Contract_Number"))
    {
      setContract_Number(value);
    }
    else if (name.startsWith("C00_Number"))
    {
      setC00_Number(value);
    }
    else if (name.startsWith("Billed_By"))
    {
      setBilled_By(value);
    }
    else if (name.startsWith("Initial_Period_Days"))
    {
      setInitial_Period_Days(value);
    }
    else if (name.startsWith("Initial_Period_Months"))
    {
      setInitial_Period_Months(value);
    }
    else if (name.startsWith("Fixed_Charge_Period"))
    {
      setFixed_Charge_Period(value);
    }
    else if (name.startsWith("Contract_Date"))
    {
      setContract_Date(value);
    }
    else if (name.startsWith("Invoice_Option"))
    {
      setInvoice_Option(value);
    }
    else if (name.startsWith("Split_Billing"))
    {
      setSplit_Billing(value);
    }
    else if (name.startsWith("VAT_Code"))
    {
      setVAT_Code(value);
    }
    else if (name.startsWith("Customer_Reference"))
    {
      setCustomer_Reference(value);
    }
    else if (name.startsWith("Circuit_Int_Designator"))
    {
      setCircuit_Int_Designator(value);
    }
    else if (name.startsWith("Bill_Option"))
    {
      setBill_Option(value);
    }
    else if (name.startsWith("GCD_Id"))
    {
      ScreenData.put("GCD_Id",value);
      setDivision_Id(value);
    }
    else if (name.startsWith("Billing_Frequency"))
    {
      setBilling_Frequency(value);
    }
    else if (name.startsWith("Circuit_Status"))
    {
      setCircuit_Status(value);
    }
    else if (name.startsWith("C03_Number"))
    {
      setC03_Number(value);
    }
    else if (name.equals("C2_Ref_No"))
    {
      setC2RefNo(value);
      ScreenData.put("C2_Ref_No",value);
    }
    else if (name.equals("C3_Ref_No"))
    {
      setC3RefNo(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getBilling_Start_Date()
  {
     if (Billing_Start_Dateh != null)
    {
      return SU.reformatDate(Billing_Start_Dateh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getBilling_End_Date()
  {
     if (Billing_End_Dateh != null)
    {
      return SU.reformatDate(Billing_End_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
  public String getFromEnd_Site_Reference()
  {
    return FromEnd_Site_Reference;
  }
  public String getFromEnd_Site_Address()
  {
    return FromEnd_Site_Address;
  }
  public String getFromEnd_Country()
  {
    return SU.isNull(FromEnd_Country,"");
  }
  public String getFromEnd_City()
  {
    return FromEnd_City;
  }
  public String getToEnd_Site_Reference()
  {
    return ToEnd_Site_Reference;
  }
  public String getToEnd_Site_Address()
  {
    return ToEnd_Site_Address;
  }
  public String getToEnd_Country()
  {
    return SU.isNull(ToEnd_Country,"");
  }
  public String getToEnd_City()
  {
    return ToEnd_City;
  }
  public String getGCB_Product_Type()
  {
    return SU.isNull(GCB_Product_Type,"");
}
  public String getProduct_Type()
  {
    return SU.isNull(Product_Type,"");
  }
  public String getCircuit_Status()
  {
    return SU.isNull(Circuit_Status,"");
  }
  public String getSpeed()
  {
    return Speed;
  }
  public String getCircuit_Description()
  {
    return Circuit_Description;
  }
  public String getCircuit_Int_Designator()
  {
    return Circuit_Int_Designator;
  }
  public String getCarrier()
  {
    return Carrier;
  }
   public String getContract_Number()
  {
   return Contract_Number;
  }
  public String getC00_Number()
  {
   return C00_Number;
  }
  public String getBilled_By()
  {
   return Billed_By;
  }
  public String getInitial_Period_Days()
  {
   return Initial_Period_Days;
  }
  public String getInitial_Period_Months()
  {
   return Initial_Period_Months;
  }
  public String getFixed_Charge_Period()
  {
   return Fixed_Charge_Period;
  }
  public String getContract_Date()
  {
     if (Contract_Dateh != null)
    {
      return SU.reformatDate(Contract_Dateh.toString());
    }
    else
    {
      return "";
    }
  }
  public String getInvoice_Option()
  {
   return Invoice_Option;
  }
  public String getSplit_Billing_Ind()
  {
   return Split_Billing;
  }
  public String getVAT_Code()
  {
   return VAT_Code;
  }
  public String getBill_Option()
  {
   return Bill_Option;
  }
  public String getCustomer_Reference()
  {
   return SU.isNull(Customer_Reference,"");
  }
  public String getBilling_Frequency()
  {
   return Billing_Frequency;
  }
  public String getC03_Number()
  {
   return C03_Number;
  }
  public String getLocal_Tax_Rate(String Charge_Entity)
  {
    if (Charge_Entity.compareTo("C&W") == 0)
        if (VAT_Code.compareToIgnoreCase("SR") == 0)
	{
          return vatRate;
	}
	else
	{
	  return "0.0";
	}
    else
        return String.valueOf(Local_Tax_Rate);
  }
  public String getMode(String FieldName)
  {
    if (FieldName.compareTo("Circuit_Status") ==0)
    {
      if (Mode.compareTo("Create")==0)
	  return "READONLY";
      else
	  return SelectMode;
    }
    if (FieldName.compareTo("Billed_By") ==0)
    {
      if (Mode.compareTo("Create")==0)
	  return "READONLY";
      else
	  return InputMode;
    }
    if (FieldName.compareTo("Circuit_Reference") ==0)
    {
      if (Mode.compareTo("Create")==0)
	  return "";
      else
	  return "READONLY";
    }
    if (FieldName.compareTo("Billing_End_Date") ==0)
    {
      if (Mode.compareTo("Cease") == 0)
	  return "";
      else
	  return "no";
    }
    if (FieldName.compareTo("C03_Number") ==0)
    {
      if (Mode.compareTo("Cease") == 0)
	  return "";
      else
	  return "READONLY";
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
    else
    {
      return InputMode;
    }
  }
  /***************************************************************************/
/*
  public String getHeader(String Type)
  {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");
    String imagename1,imagename2;
    if (OrderBy.compareTo("StatusSortAsc")==0)
    {
      imagename1="sorted_by.gif";
      imagename2="sort_by.gif";
    }
    else
    {
      imagename2="sorted_by.gif";
      imagename1="sort_by.gif";
    }
    if (Type.startsWith("Circuit"))
    {
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Customer')\">Customer</button></td>");
      //onmouseover=\"Close_Menus();Open_Menu('Customer')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu onclick=\"Toggle_Menu('Account')\">Account</button></td>");
      //onmouseover=\"Close_Menus();Open_Menu('Account')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=110><button class=grid_menu onclick=\"Toggle_Menu('GSR')\">Circuit</button></td>");
      // onmouseover=\"Close_Menus();Open_Menu('GSR')\"\"
      HB.append("<td class=gridHeader NOWRAP valign=top width=75><button id=StatusSort url=\"StatusSort\" class=grid_menu onclick=\"Toggle_Menu('Status')\">Status");
      HB.append("</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=90><button class=grid_menu onclick=\"Toggle_Menu('Type')\">Type");
      HB.append("</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=80><button class=grid_menu onclick=\"Toggle_Menu('Month')\">Effective Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>CR/DB</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Now add the filters
      HB.append("<TR><TD class=grid1><SPAN id=Customer ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Global_Customer",true,Global_Customer_Id_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD class=grid1><SPAN id=Account ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Invoice_Region",true,Account_for_List,Global_Customer_Id_for_List));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=GSR ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      if (System.compareTo("OSS")==0)
      {
        HB.append(DBA.getListBox("BAN_Circuits",true,GSR_for_List,Global_Customer_Id_for_List));
      }
      else
      {
	HB.append(DBA.getListBox("Global_Service_Reference",true,GSR_for_List,Global_Customer_Id_for_List));
      }
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Status ");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("Ban_Status",true,Status_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=1 class=grid1><SPAN id=Type");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("BAN_Type",true,BAN_Type_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("<TD colspan=4 class=grid1><SPAN id=Month");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 100px; POSITION: absolute; TOP: 235px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("BAN_Month",true,BAN_Month_for_List,""));
      HB.append("</tr></td></table></SPAN></TD>");

      HB.append("</TR></TABLE>");

    }
    return HB.toString();
  }*/
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
	  if ((mandatory.contains(Item)) || ((Item.compareTo("Billing_End_Date")== 0)
					      && (Mode.compareTo("Cease") == 0)))
            return "mandatory";
	  else
	    return "optional";
	}
}
/*******************************************************************************/
public boolean isCircuitValid(String ButtonPressed)
{
	Enumeration FormFields=mandatory.elements();
	setErrored("clear");
        Message = "<font color=red><b>";
	String FormField="";
	String FieldName;
	String value="";

	if ((ButtonPressed.startsWith("Submit")) && (Mode.compareTo("Cease") !=0))
	{
	  while (FormFields.hasMoreElements())
	  {
	    FieldName=(String)FormFields.nextElement();
	    FormField=SU.isNull((String)ScreenData.get(FieldName),"");
	    if(FormField.compareTo("") == 0)
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

	//Fixed Charge period Numeric?
	/*if ((SU.isNull(Fixed_Charge_Period,"").compareTo("") != 0) &&
	    (SU.toInt(Fixed_Charge_Period) == NOT_INT))
	{
	  setErrored("Fixed_Charge_Period");
          Message = "<font color=red><b>Fixed charge period must be numeric";
	  return false;
	}
	//Contract Number Numeric?
	if (Contract_Number.compareTo("") != 0)
	{
	  try
	  {
	    Integer.parseInt(Contract_Number);
	  }
	  catch(java.lang.NumberFormatException NE)
	  {
	    setErrored("Contract_Number");
	    Message = "<font color=red><b>Contract Number must be numeric";
	    return false;
	  }
	}
	//Initial Period numeric??
	if (Initial_Period_Days.compareTo("")!=0)
	{
	  try
	  {
	    Integer.parseInt(Initial_Period_Days);
	  }
	  catch(java.lang.NumberFormatException NE)
	  {
	    setErrored("Initial_Period_Days");
	    Message = "<font color=red><b>Initial_Period_Days must be numeric";
	    return false;
	  }
	}
	try
	{
	  Integer.parseInt(Fixed_Charge_Period);
	}
	catch(java.lang.NumberFormatException NE)
	{
	  setErrored("Fixed_Charge_Period");
	  Message = "<font color=red><b>Fixed_Charge_Period must be numeric";
	  return false;
	}*/
	//Circuit Ref already exists for a 'Create'?
	if ((Mode.compareTo("Create")==0) && (CircuitExists()))
	{
	  setErrored("Circuit_Reference");
	  Message = "<font color=red><b>A circuit with this Circuit Reference already exists";
	  return false;
	}
	//Billed elsewhere but no Billed By/
	/*if ((Circuit_Status.compareTo("B")==0) && (Billed_By.compareTo("")==0))
	{
	  setErrored("Billed_By");
	  Message = "<font color=red><b>If the Circuit Status is 'Billed Elsewhere' you must supply a value for 'Billed By'";
	  return false;
	}
	//No Billing End Date for a 'Cease'?
	if ((Mode.compareTo("Cease")==0) && (SU.isNull((String)ScreenData.get("Billing_End_Date"),"").compareTo("")==0))
	{
	  setErrored("Billing_End_Date");
	  Message = "<font color=red><b>You must supply a Billing End Date for a Cease";
	  return false;
	}
	if ((Mode.compareTo("Cease")==0) && (SU.isNull((String)ScreenData.get("C03_Number"),"").compareTo("")==0))
	{
	  setErrored("C03_Number");
	  Message = "<font color=red><b>You must supply a C03 Number for a Cease";
	  return false;
	}*/
	//Circuit Ref space filled for a 'Create'?
	if ((Mode.compareTo("Create")==0) && (SU.isSpaces(Circuit_Reference)))
	{
	  setErrored("Circuit_Reference");
	  Message = "<font color=red><b>Circuit Reference must not be all spaces";
	  return false;
	}
	if (ToEnd_Site_Reference.equals(FromEnd_Site_Reference))
	{
	  setErrored("FromEnd_Site_Reference");
	  setErrored("ToEnd_Site_Reference");
	  Message = "<font color=red><b>From and To Sites must be different";
	  return false;
	}

	return true;
}
/*******************************************************************************/
public boolean getCircuitBAN()
{
    boolean getCircuitBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Circuit_BAN ";
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
            Mode=RS.getString(2);
	    banStatus=RS.getString(3);
	    BAN_Summary=RS.getString(4);
	    Required_BAN_Effective_Dateh=RS.getDate(5);
	    banCreatedBy=RS.getString(6);
	    FromEnd_Site_Reference=RS.getString(7);
	    ToEnd_Site_Reference=RS.getString(8);
	    Circuit_Description=RS.getString(9);
	    Speed=RS.getString(10);
	    Customer_Reference=RS.getString(11);
    	    Billing_Start_Dateh=RS.getDate(12);
    	    Billing_End_Dateh=RS.getDate(13);
	    Carrier=RS.getString(14);
	    GCB_Product_Type=RS.getString(15);
	    Circuit_Reference=RS.getString(16);
	    Division_Id=RS.getString(17);
            Bill_Option=RS.getString(18);
            setDates();
	    getCircuitBAN = true;
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
      return getCircuitBAN;
    }
}
/*****************************************************************************************/
      /* As a result of problems with the callable statement syntax in the JDBC driver */
      /* The callable statement syntax has now been replaced with a straight sql string execution*/
/*public boolean createCircuitBAN()
{
  int i=0;
  Enumeration FromEnd_Site_Address_UnPacked;
  Enumeration ToEnd_Site_Address_UnPacked;

    try
    {
      if (FromEnd_Site_Address.compareTo("") !=0 )
      {
	  FromEnd_Site_Address_UnPacked = SU.unpackTextArea(FromEnd_Site_Address);

	  while ((FromEnd_Site_Address_UnPacked.hasMoreElements()) && (i < 5))
	  {
	    FromEnd_Site_Address_Lines[i]=(String)FromEnd_Site_Address_UnPacked.nextElement();
	    i++;
	  }
      }
      while (i < 5)
      {
	FromEnd_Site_Address_Lines[i]="";
	i++;
      }

      ToEnd_Site_Address_UnPacked = SU.unpackTextArea(ToEnd_Site_Address);
      i=0;
      while ((ToEnd_Site_Address_UnPacked.hasMoreElements()) && (i < 5))
      {
	  ToEnd_Site_Address_Lines[i]=(String)ToEnd_Site_Address_UnPacked.nextElement();
	  i++;
      }

      while (i < 5)
      {
	ToEnd_Site_Address_Lines[i]="";
	i++;
      }
      StringBuffer SQLBuffer = new StringBuffer("");

      SQLBuffer.append("exec eban.dbo.Create_Circuit_BAN '");
      SQLBuffer.append(Global_Customer_Id).append("','"); //1
      SQLBuffer.append(Invoice_Region).append("','"); //2
      SQLBuffer.append(BAN_Summary).append("','");
      SQLBuffer.append(BAN_Reason).append("','");
      SQLBuffer.append(SU.DateToString(Required_BAN_Effective_Dateh)).append("','");
      SQLBuffer.append(userId).append("','");
      SQLBuffer.append(SU.DateToString(Billing_Start_Dateh)).append("','");
      SQLBuffer.append(SU.DateToString(Billing_End_Dateh)).append("','");
      SQLBuffer.append(GCB_Product_Type).append("','");
      SQLBuffer.append(Product_Type).append("','");
      SQLBuffer.append(Speed).append("','");
      SQLBuffer.append(FromEnd_Site_Reference).append("','");
      SQLBuffer.append(FromEnd_Site_Address_Lines[0]).append("','");
      SQLBuffer.append(FromEnd_Site_Address_Lines[1]).append("','");
      SQLBuffer.append(FromEnd_Site_Address_Lines[2]).append("','");
      SQLBuffer.append(FromEnd_Site_Address_Lines[3]).append("','");
      SQLBuffer.append(FromEnd_Site_Address_Lines[4]).append("','");
      SQLBuffer.append(FromEnd_Country).append("','");
      SQLBuffer.append(FromEnd_City).append("','");
      SQLBuffer.append(ToEnd_Site_Reference).append("','");
      SQLBuffer.append(ToEnd_Site_Address_Lines[0]).append("','");
      SQLBuffer.append(ToEnd_Site_Address_Lines[1]).append("','");
      SQLBuffer.append(ToEnd_Site_Address_Lines[2]).append("','");
      SQLBuffer.append(ToEnd_Site_Address_Lines[3]).append("','");
      SQLBuffer.append(ToEnd_Site_Address_Lines[4]).append("','");
      SQLBuffer.append(ToEnd_Country).append("','");
      SQLBuffer.append(ToEnd_City).append("','");
      SQLBuffer.append(Circuit_Description).append("','");
      SQLBuffer.append(Circuit_Int_Designator).append("','");
      SQLBuffer.append(Customer_Reference).append("','");
      SQLBuffer.append(Carrier).append("','");
      SQLBuffer.append(VAT_Code).append("','");
      SQLBuffer.append(Billing_Frequency).append("','");
      SQLBuffer.append(Contract_Number).append("','");
      SQLBuffer.append(SU.DateToString(Contract_Dateh)).append("','");
      SQLBuffer.append(Invoice_Option).append("','");
      SQLBuffer.append(Following_Invoice_Type).append("','");
      SQLBuffer.append(Next_Invoice_Type).append("','");
      SQLBuffer.append(Split_Billing).append("',");
      SQLBuffer.append(Fixed_Charge_Period).append(",'");
      SQLBuffer.append(Billed_By).append("','");
      SQLBuffer.append(C00_Number).append("','");
      SQLBuffer.append(Bill_Option).append("','");
      SQLBuffer.append(Account_Text_Override).append("','");
      SQLBuffer.append(banStatus).append("','");
      SQLBuffer.append(Circuit_Reference).append("','");
      SQLBuffer.append(Account_Id).append("','");
      SQLBuffer.append(Mode).append("','");
      SQLBuffer.append(Circuit_Status).append("','");
      SQLBuffer.append(C03_Number).append("'");

      banIdentifier=DBA.getValue(SQLBuffer.toString(),P5);
      close();

      if (banIdentifier.startsWith("<font"))
      {
	Message=banIdentifier;
	return false;
      }
      else
      {
	Reset();
      }
    }
    catch(java.lang.NullPointerException ne){Message="<font color=red><b>"+ne.getMessage();return false;}
    Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
    return true;
  }*/
/******************************************************************************************/
/* This function replaced by above due to problems with callable statement*/
public boolean createCircuitBAN()
{
  boolean createCircuitBAN = false;
  int i=0;

    try
    {
      if (DBA.Connect(WRITE,P5))
      {

        SQL = "{call eban.dbo.Create_Circuit_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);

	cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,userId);
	cstmt.setDate(5,Billing_Start_Dateh);
	cstmt.setDate(6,Billing_End_Dateh);
	cstmt.setString(7,GCB_Product_Type);
	cstmt.setString(8,Speed);
	cstmt.setString(9,FromEnd_Site_Reference);
	cstmt.setString(10,ToEnd_Site_Reference);
	cstmt.setString(11,Circuit_Description);
	cstmt.setString(12,Customer_Reference);
	cstmt.setString(13,Carrier);
	cstmt.setString(14,Bill_Option);
	cstmt.setString(15,banStatus);
	cstmt.setString(16,Circuit_Reference);
	cstmt.setString(17,Mode);
	cstmt.setString(18,Division_Id);
	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
	    banIdentifier=RS.getString(1);
            createCircuitBAN = true;
            Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
	if (banIdentifier.compareTo("0") == 0)
	{
	  Message="Insert Failed";
          createCircuitBAN = false;
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
      return createCircuitBAN;
    }
  }
  /**********************************************************************************************/
public boolean authoriseCircuitBAN()
{
  boolean authoriseCircuitBAN = false;
  int i=0;

    try
    {
      if (DBA.Connect(WRITE,P5))
      {

        SQL = "{call eban.dbo.Authorise_Circuit_BAN " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);

	cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,userId);
	cstmt.setDate(5,Billing_Start_Dateh);
	cstmt.setDate(6,Billing_End_Dateh);
	cstmt.setString(7,GCB_Product_Type);
	cstmt.setString(8,Speed);
	cstmt.setString(9,FromEnd_Site_Reference);
	cstmt.setString(10,ToEnd_Site_Reference);
	cstmt.setString(11,Circuit_Description);
	cstmt.setString(12,Customer_Reference);
	cstmt.setString(13,Carrier);
	cstmt.setString(14,Bill_Option);
	cstmt.setString(15,banStatus);
	cstmt.setString(16,Circuit_Reference);
	cstmt.setString(17,Mode);
	cstmt.setString(18,Division_Id);
	cstmt.setString(19,c2RefNo);
	cstmt.setString(20,c3RefNo);
	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
            String ret = RS.getString(1);
            if (ret.startsWith("-"))
            {
              authoriseCircuitBAN = false;
              Message = "<font color=red><b>Unable to authorise BAN: " +
                (ret.equals("-99")?"a bill job is currently running for this customer."
                :("return code " + ret)) + "</b></font>";

            }
            else
            {
              authoriseCircuitBAN = true;
              banIdentifier = ret;
              setBanStatus("Implemented");
              Message = "<font color=blue><b>BAN Id:-" + banIdentifier +
                " authorised and circuit " + (Mode.equals("Create")?"created":"updated");
            }
	  }
	}
	catch(java.sql.SQLException se)
        {
	  Message=se.getMessage();
        }
	if (banIdentifier.compareTo("0") == 0)
	{
	  Message="Insert Failed";
          authoriseCircuitBAN = false;
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
      return authoriseCircuitBAN;
    }
  }
  /**********************************************************************************************/
  public boolean updateCircuitBAN()
  {
    boolean updateCircuitBAN = false;
    int rowcount=0,i=0;
    Enumeration FromEnd_Site_Address_UnPacked;
    Enumeration ToEnd_Site_Address_UnPacked;

    try
    {
      if (DBA.Connect(WRITE,P5))
      {
	SQL = "{call eban..update_Circuit_BAN ";
	SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	cstmt = DBA.Conn.prepareCall(SQL);

	cstmt.setString(1,Global_Customer_Id);
        cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,userId);
	cstmt.setDate(5,Billing_Start_Dateh);
	cstmt.setDate(6,Billing_End_Dateh);
	cstmt.setString(7,GCB_Product_Type);
	cstmt.setString(8,Speed);
	cstmt.setString(9,FromEnd_Site_Reference);
	cstmt.setString(10,ToEnd_Site_Reference);
	cstmt.setString(11,Circuit_Description);
	cstmt.setString(12,Customer_Reference);
	cstmt.setString(13,Carrier);
	cstmt.setString(14,Bill_Option);
	cstmt.setString(15,banStatus);
	cstmt.setString(16,Circuit_Reference);
	cstmt.setString(17,Mode);
	cstmt.setString(18,Division_Id);
	cstmt.setString(19,banIdentifier);

	cstmt.execute();
        String tempId = banIdentifier;
        //Reset();
        updateCircuitBAN = true;
        Message = "<font color=blue><b>BAN Id:-"+tempId+" updated";
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
      closeupdate();
      return updateCircuitBAN;
    }
  }
/********************************************************************/
public boolean getCircuit_Header()
{
    boolean getCircuit_Header = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Circuit_Header ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Circuit_Reference);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    Global_Customer_Id=RS.getString(1);
	    Invoice_Region=RS.getString(2);
	    GCB_Product_Type=RS.getString(3);
	    Product_Type=RS.getString(4);
	    Speed=RS.getString(5);
	    Circuit_Description=RS.getString(6);
	    Circuit_Int_Designator=RS.getString(7);
	    Customer_Reference=RS.getString(8);
	    Carrier=RS.getString(9);
	    VAT_Code=RS.getString(10);
	    Billing_Frequency=RS.getString(11);
	    Contract_Number=RS.getString(12);
	    Contract_Dateh=RS.getDate(13);
	    Invoice_Option=RS.getString(14);
	    Bill_Option=RS.getString(15);
	    Circuit_Status=RS.getString(16);
            getCircuit_Header = true;
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
      return getCircuit_Header;
    }
}
/*******************************************************************************/
public void getCircuit(String BAN_Id)
{
    StringBuffer SQLBuffer = new StringBuffer("");
    String Circuit_Ref="";

    if (BAN_Id.compareTo("")!=0)
    {
      SQLBuffer.append("Select Circuit_Reference from eban..OSS_Charge_BAN");
      SQLBuffer.append(" where BAN_Identifier = '").append(BAN_Id);
      SQLBuffer.append("'");

      Circuit_Ref=DBA.getValue(SQLBuffer.toString(),P5);
      //Datasource changes Circuit_Ref=DBA.getValue(SQLBuffer.toString());
      if (Circuit_Ref.compareTo("") !=0)
      {
        setCircuit_Reference(Circuit_Ref);
      }
    }
    getCircuit();
}
/*******************************************************************************/
public String LLUBanUpdate(
                String DeleteTrial,
                String eBANSummary,
                String User,
                String GCID,
                String Service_Reference,
                String Effective_Date,
                String Speed)
{

  String BAN="";
  Message="";
  boolean Deleted=true;
  eBANSummary=eBANSummary.trim();

  //if there is a trial bill delete the original record first
  if (DeleteTrial=="Y")
  {
    SQL = "DELETE FROM gcd..LLU_Circuit_Speed "+
          "WHERE Service_Reference = '"+Service_Reference+"' "+
          "AND DATEPART(mm,Effective_Start_Date) = "+
          "DATEPART(mm,CONVERT(DATETIME,'"+Effective_Date+"')) "+
          "AND DATEPART(yy,Effective_Start_Date) = "+
          "DATEPART(yy,CONVERT(DATETIME,'"+Effective_Date+"')) ";
    DBA.setSQL(SQL);
    if (DBA.Connect(NORESULT,P4))
      {
        Deleted=true;
      }
      else
      {
        Deleted=false;
      }
    close();
  }

  if (Deleted)
  {
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Update_LLU_Circuit_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,"Implemented");
	cstmt.setString(2,eBANSummary);
	cstmt.setString(3,User);
	cstmt.setString(4,Effective_Date);
	cstmt.setString(5,Service_Reference);
	cstmt.setString(6,GCID);
	cstmt.setString(7,"Speed");
	cstmt.setString(8,Speed);
	cstmt.setString(9,DeleteTrial);
        cstmt.execute();
	RS = cstmt.getResultSet();

        try
        {
          if (RS.next())
          {
            BAN="Circuit speed successfully updated : BAN "+RS.getString(1);
          }
          else
          {
            Message="No results returned by eban..Update_LLU_Circuit_BAN";
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
    close();
  }

  if (Message!="")
  {
    BAN="Update has failed : "+Message;
  }

  return BAN;

}
/*******************************************************************************/
public String LLUChangeAllowed
        (String Service_Reference,
         String Effective_Date,
         String Speed,
         String GlobalCustomerId)
{
  //At start assume change is not allowed
  String test="X",BPSD="",OldSpeed="";
  boolean BillCheck=false;
  int ChangeLimit=30, ChangeCount=0;

  //Check to see if the Service has been changed this month
  SQL = "SELECT Global_Service_Reference, Speed "+
        "FROM eban..LLU_Charge_Ban (nolock) "+
        "WHERE Global_Service_Reference = '"+Service_Reference+"' "+
        "AND DATEPART(mm,BAN_Effective_Date) = "+
        "DATEPART(mm,CONVERT(DATETIME,'"+Effective_Date+"')) "+
        "AND DATEPART(yy,BAN_Effective_Date) = "+
        "DATEPART(yy,CONVERT(DATETIME,'"+Effective_Date+"')) ";
  DBA.setSQL(SQL);
  if (DBA.Connect(READ,P4))
  {
  RS = DBA.getResultsSet();
  try
  {
    if (RS.next())
    {
      test="Y";
      OldSpeed=SU.isNull(RS.getString(2),"");
    }
    else
    {
      test="N";
    }
  }
  catch(java.sql.SQLException se)
  {
    test=se.getMessage();
  }
    close();
  }

  //for a change in this month determine if it has been billed
  if (test=="Y")
  {
    BillCheck=false;
    SQL = "SELECT CONVERT(VARCHAR,MAX(BPSD_Orig),106) "+
          "FROM gcd..Monthly_Billing_Archive (nolock) "+
          "WHERE Service_Reference = '"+Service_Reference+"' ";
    DBA.setSQL(SQL);
    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      try
      {
        if (RS.next())
        {
          //check to see if it has been billed this month
          BillCheck = true;
          BPSD=SU.isNull(RS.getString(1),"");
        }
        else
        {
          //reset to ok as this circuit has never been billed
          test="N";
        }
      }
      catch(java.sql.SQLException se)
      {
        test=se.getMessage();
      }
      close();

      if (BillCheck)
      {
        SQL = "SELECT Speed "+
              "FROM gcd..Monthly_Billing_Archive (nolock) "+
              "WHERE Service_Reference = '"+Service_Reference+"' "+
              "AND BPSD_Orig = '"+BPSD+"' "+
              "AND Speed = '"+OldSpeed+"' ";
        //System.out.print(SQL);
        DBA.setSQL(SQL);
        if (DBA.Connect(READ,P4))
        {
          RS = DBA.getResultsSet();
          try
          {
            if (RS.next())
            {
              test="X";
            }
            else
            {
              test="Y";
            }
          }
          catch(java.sql.SQLException se)
          {
            test=se.getMessage();
          }
          close();
        }
      }
    }
  }

  // For the month of the change make sure that the limit for the month
  // has not been breached
  SQL = "SELECT Changes_Per_Month "+
        "FROM   gcd..LLU_Customer_Change_Value "+
        "WHERE  Global_Customer_Id = '"+GlobalCustomerId+"'";
  DBA.setSQL(SQL);
  if (DBA.Connect(READ,P4))
  {
  RS = DBA.getResultsSet();
  try
  {
    if (RS.next())
    {
      ChangeLimit=RS.getInt(1);
    }
  }
  catch(java.sql.SQLException se)
  {
    test=se.getMessage();
  }
    close();
  }

  SQL = "SELECT COUNT(*) "+
        "FROM   eban..LLU_Charge_Ban "+
        "WHERE  Global_Customer_Id = '"+GlobalCustomerId+"' "+
        "AND DATEPART(mm,BAN_Effective_Date) = "+
        "DATEPART(mm,CONVERT(DATETIME,'"+Effective_Date+"')) "+
        "AND DATEPART(yy,BAN_Effective_Date) = "+
        "DATEPART(yy,CONVERT(DATETIME,'"+Effective_Date+"')) ";
  System.out.print(SQL);
  DBA.setSQL(SQL);
  if (DBA.Connect(READ,P4))
  {
  RS = DBA.getResultsSet();
  try
  {
    if (RS.next())
    {
      ChangeCount=RS.getInt(1);
      if (ChangeCount >= ChangeLimit)
      {
        test="C";
      }
      else
      {
        test="Y";
      }
    }
    else
    {
      test="Y";
    }
  }
  catch(java.sql.SQLException se)
  {
    test=se.getMessage();
  }
    close();
  }

  return test;
}
/*******************************************************************************/
public String getFullFromDets(String Service_Reference)
{
    String getFullFromDets="";

    SQL = "SELECT  From_End_Code + ' : ' + From_End "+
          "FROM gcd..Global_Customer_Billing "+
          "WHERE Service_Reference = '"+Service_Reference+"' ";
    DBA.setSQL(SQL);

    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      try
      {
        if (RS.next())
        {
          getFullFromDets=SU.isNull(RS.getString(1),"");
        }
      }
      catch(java.sql.SQLException se)
      {
        getFullFromDets=se.getMessage();
      }
  }
  close();
  return getFullFromDets;
}/*******************************************************************************/
public String getFullToDets(String Service_Reference)
{
    String getFullToDets="";

    SQL = "SELECT  To_End_Code + ' : ' + To_End "+
          "FROM gcd..Global_Customer_Billing "+
          "WHERE Service_Reference = '"+Service_Reference+"' ";
    DBA.setSQL(SQL);

    if (DBA.Connect(READ,P4))
    {
      RS = DBA.getResultsSet();
      try
      {
        if (RS.next())
        {
          getFullToDets=SU.isNull(RS.getString(1),"");
        }
      }
      catch(java.sql.SQLException se)
      {
        getFullToDets=se.getMessage();
      }
  }
  close();
  return getFullToDets;
}
/*******************************************************************************/
public boolean getCircuit()
{
    boolean getCircuit = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..get_GSR ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Circuit_Reference);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
            Customer_Reference=SU.isNull(RS.getString(2),"");
	    Circuit_Description=SU.isNull(RS.getString(3),"");
	    Speed=SU.isNull(RS.getString(4),"");
	    Division_Id=SU.isNull(RS.getString(5),"");
	    GCB_Product_Type=SU.isNull(RS.getString(6),"");
	    FromEnd_Site_Reference=SU.isNull(RS.getString(7),"");
	    ToEnd_Site_Reference=SU.isNull(RS.getString(8),"");
    	    Billing_Start_Dateh=RS.getDate(9);
    	    Billing_End_Dateh=RS.getDate(10);
	    Bill_Option=SU.isNull(RS.getString(11),"");
	    Carrier=SU.isNull(RS.getString(12),"");
	    c2RefNo=SU.isNull(RS.getString(13),"");
	    c3RefNo=SU.isNull(RS.getString(14),"");
            setDates();
	    getCircuit = true;
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
      return getCircuit;
    }
}
/***************************************************/
public boolean getSpecifiedCircuit(String Circuit)
{
    boolean getSpecifiedCircuit = false;
    Circuit_Reference=Circuit;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call OSS..get_Circuit ";
        SQL += "(?)}";

	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Circuit_Reference);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    //Global_Customer_Id=RS.getString(1);
	    //Invoice_Region=RS.getString(2);
	    Account_Id=RS.getString(3);
    	    Billing_Start_Dateh=RS.getDate(4);
    	    Billing_End_Dateh=RS.getDate(5);
	    GCB_Product_Type=RS.getString(6);
	    Product_Type=RS.getString(7);
	    Speed=RS.getString(8);
	    FromEnd_Site_Reference=RS.getString(9);
	    FromEnd_Site_Address=RS.getString(10);
	    FromEnd_Country=RS.getString(11);
	    FromEnd_City=RS.getString(12);
	    ToEnd_Site_Reference=RS.getString(13);
	    ToEnd_Site_Address=RS.getString(14);
	    ToEnd_Country=RS.getString(15);
	    ToEnd_City=RS.getString(16);
	    Circuit_Description=RS.getString(17);
	    Circuit_Int_Designator=RS.getString(18);
	    Customer_Reference=RS.getString(19);
	    Carrier=RS.getString(20);
	    VAT_Code=RS.getString(21);
	    Billing_Frequency=RS.getString(22);
	    setContract_Number(RS.getString(23));
	    Contract_Dateh=RS.getDate(24);
	    Invoice_Option=RS.getString(25);
	    Following_Invoice_Type=RS.getString(26);
	    Next_Invoice_Type=RS.getString(27);
	    Split_Billing=RS.getString(28);
	    Fixed_Charge_Period=RS.getString(29);
	    Bill_Option=RS.getString(30);
	    Account_Text_Override=RS.getString(31);
	    Circuit_Status=RS.getString(32);
	    Local_Tax_Rate=(RS.getFloat(33));
	    Billed_By=RS.getString(34);
	    setC00_Number(RS.getString(35));

	    ScreenData.put("Billing_Start_Dateh",SU.DateToString(Billing_Start_Dateh));
            getSpecifiedCircuit = true;
  	    //FromEnd_Site_Address=SU.packTextArea(FromEnd_Site_Address_Lines);
	    //ToEnd_Site_Address=SU.packTextArea(ToEnd_Site_Address_Lines);
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
      return getSpecifiedCircuit;
    }
}
/*********************************************************************/
public boolean ImplementCircuitBAN()
{

  if (Mode.compareTo("Create") ==0)
  {
    SQL = "exec eban..Create_GSR '"+ banIdentifier+ "'";
  }
  else //if (Mode.compareTo("Amend") ==0)
  {
    SQL = "exec eban..Update_GSR '"+ banIdentifier+ "'";
  }
/*  else if (Mode.compareTo("Cease") ==0)
  {
    SQL = "exec eban..Cease_Circuit '"+ banIdentifier+ "'";
  }*/

  if(DBA.NoResult(SQL,P5))
  {
    setBanStatus("Implemented");
    return true;
  }
  else
  {
    Message=DBA.getMessage();
    return false;
  }
}
/********************************************************************/
public boolean CircuitExists()
{

  SQL = "Select 'Found' from OSS..Circuit where CW_Circuit_Ref = '"+ Circuit_Reference+ "'";

  if(DBA.getExists(SQL,P5))
  //Datasource changes if(DBA.getExists(SQL))
  {
    return true;
  }
  else
  {
    return false;
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
/************************************************************************************************/
  public int getBillingEndDateDays()
  {
    return BiEDays;
  }
/************************************************************************************************/
  public void setBillingEndDateDays(String value)
  {
    BiEDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBillingEndDateMonths()
  {
    return BiEMonths;
  }
/************************************************************************************************/
  public void setBillingEndDateMonths(String value)
  {
    BiEMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBillingEndDateYears()
  {
    return BiEYears;
  }
/************************************************************************************************/
  public void setBillingEndDateYears(String value)
  {
    BiEYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBillingStartDateDays()
  {
    return BiSDays;
  }
/************************************************************************************************/
  public void setBillingStartDateDays(String value)
  {
    BiSDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBillingStartDateMonths()
  {
    return BiSMonths;
  }
/************************************************************************************************/
  public void setBillingStartDateMonths(String value)
  {
    BiSMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getBillingStartDateYears()
  {
    return BiSYears;
  }
/************************************************************************************************/
  public void setBillingStartDateYears(String value)
  {
    BiSYears = value.equals("")?0:Integer.parseInt(value);
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
    cal.setTime(Billing_Start_Dateh==null?(new java.util.Date())
      :Billing_Start_Dateh);
    BiSDays=cal.get(cal.DATE);
    BiSMonths=cal.get(cal.MONTH)+1;
    BiSYears=cal.get(cal.YEAR);
    if (Billing_End_Dateh!=null)
    {
      cal.setTime(Billing_End_Dateh);
      BiEDays=cal.get(cal.DATE);
      BiEMonths=cal.get(cal.MONTH)+1;
      BiEYears=cal.get(cal.YEAR);
    }
  }
 /********************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    boolean OK;
    BANBean BAN=new BANBean();
    CircuitBANBean ctBAN=new CircuitBANBean();
    BAN.setCircuit_Reference("34T5345");
    ctBAN.createCircuitBAN();

 }
}