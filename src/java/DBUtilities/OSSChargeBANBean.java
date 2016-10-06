package DBUtilities;

import java.sql.*;
import java.util.*;
import java.math.BigInteger;
import JavaUtil.SiteSplitDescriptor;
import JavaUtil.EBANProperties;

public class OSSChargeBANBean extends BANBean
{
    private String Charge_Description="",Charge_Category="",Charge_CategoryDisplay="";
    private java.sql.Date Charge_Valid_From_Date=null,Charge_Valid_To_Date, storedToDate,
      Price_Change_Date, Charge_Period_Start_Date, Charge_Period_End_Date;
    private String Charge_Frequency="Monthly",VAT_Indicator="Y",Revenue_Reason_Code="";
    private String OSS_Charge_Type="",Standard_Bill_Indicator="N",Charge_Entity="C&W Charge";
    private String Currency_Display="<font color=black> (UK Pounds Sterling)";
    private double Gross_Amount,VAT_Amount=0,Total_Amount=0, oldGrossAmount;
    protected Vector mandatory=new Vector();
    private int Charge_Id, splits;
    private Hashtable ScreenData=new Hashtable();
    private Vector siteSplits=new Vector();
    private String Charge_Currency;
    private int BEDays, BEMonths, BEYears, FCDays, FCMonths, FCYears, TCDays,
      TCMonths, TCYears, PCDays, PCMonths, PCYears, PSDays, PSMonths, PSYears,
      PEDays, PEMonths, PEYears;
    private String siteReference, siteReference2, siteReference3;
    private int unitQuantity;
    private boolean cdcsRequired;
    private double split, split2, split3;
    private String chargeDescriptionCode;
    private String discountsApplicable;
    private String revenueType;
    private String revenueDescription;
    private String revenueNetOrFull;
    private String revenueRootCause;
    private int chargeId;
    private boolean chargeBilled;
    private boolean chargeTrialled;
    private boolean toDateNullable;
    private String trialDelete;
    private String Charge_Description_for_List;
    private boolean archived;
    private String c3RefNo;
    private boolean reprovide;
    private String reopenBill;
    private String c2RefNo_for_List;
    private long chargeIdCheck;
    private String cdcAuthority;
    private String circuitBillStartDate;
    private String toEnd;



   public OSSChargeBANBean ()
  {
    cdcAuthority = EBANProperties.getEBANProperty("cdcAuthority");
    mandatory.clear();
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("Circuit_Reference");
    mandatory.addElement("Site_Reference");
    mandatory.addElement("Splits");
    mandatory.addElement("Gross_Amount");
    mandatory.addElement("Unit_Quantity");
    mandatory.addElement("Charge_Currency");
    mandatory.addElement("Charge_Frequency");
    mandatory.addElement("Charge_Description");
    mandatory.addElement("From_Charge_Valid_Dateh");
    mandatory.addElement("Charge_Entity");
    mandatory.addElement("Discounts_Applicable");
    mandatory.addElement("Revenue_Type");
    mandatory.addElement("C2_Ref_No");

    siteSplits.clear();
    Charge_Currency = "USD";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    FCDays=0;
    FCMonths=0;
    FCYears=0;
    TCDays=0;
    TCMonths=0;
    TCYears=0;
    PSDays=0;
    PSMonths=0;
    PSYears=0;
    PEDays=0;
    PEMonths=0;
    PEYears=0;
    splits=0;
    siteReference="";
    siteReference2="";
    siteReference3="";
    unitQuantity=1;
    cdcsRequired=false;
    split=100;
    split2=0;
    split3=0;
    chargeDescriptionCode="";
    discountsApplicable="N";
    ScreenData.put("Discounts_Applicable",discountsApplicable);
    revenueType="1";
    revenueDescription="";
    revenueNetOrFull="";
    revenueRootCause="";
    chargeId=0;
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    Price_Change_Date=null;
    Charge_Period_Start_Date=null;
    Charge_Period_End_Date=null;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    PCDays=cal.get(cal.DATE);
    PCMonths=cal.get(cal.MONTH)+1;
    PCYears=cal.get(cal.YEAR);
    chargeBilled=false;
    chargeTrialled=false;
    toDateNullable=true;
    trialDelete="N";
    storedToDate=null;
    Charge_Description_for_List="All";
    c2RefNo_for_List="All";
    archived=false;
    c2RefNo="";
    c3RefNo="";
    reprovide=false;
    reopenBill="N";
    oldGrossAmount=0;
    chargeIdCheck = -1;
    circuitBillStartDate = "";
    toEnd = null;
  }
  public void Reset()
  {
    super.Reset();
    mandatory.clear();
    mandatory.addElement("Required_BAN_Effective_Dateh");
    mandatory.addElement("Circuit_Reference");
    mandatory.addElement("Site_Reference");
    mandatory.addElement("Splits");
    mandatory.addElement("Gross_Amount");
    mandatory.addElement("Unit_Quantity");
    mandatory.addElement("Charge_Currency");
    mandatory.addElement("Charge_Frequency");
    mandatory.addElement("Charge_Description");
    mandatory.addElement("From_Charge_Valid_Dateh");
    mandatory.addElement("Charge_Entity");
    mandatory.addElement("Discounts_Applicable");
    mandatory.addElement("Revenue_Type");
    mandatory.addElement("C2_Ref_No");

    Charge_Description="";
    Charge_Category="";
    Charge_Valid_From_Date=null;
    Charge_Valid_To_Date=null;
    Charge_Frequency="Monthly";
    Revenue_Reason_Code="";
    OSS_Charge_Type="";
    Standard_Bill_Indicator="";
    Charge_Entity="C&W";
    VAT_Indicator="Y";
    Currency_Display="<font color=black> (UK Pounds Sterling)";
    Gross_Amount=0;
    VAT_Amount=0;
    Total_Amount=0;
    ScreenData.clear();
    setErrored("clear");
    setRequired_BAN_Effective_Date("Today");
    siteSplits.clear();
    Charge_Currency = "USD";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    FCDays=0;
    FCMonths=0;
    FCYears=0;
    TCDays=0;
    TCMonths=0;
    TCYears=0;
    PSDays=0;
    PSMonths=0;
    PSYears=0;
    PEDays=0;
    PEMonths=0;
    PEYears=0;
    Charge_Period_Start_Date=null;
    Charge_Period_End_Date=null;
    Price_Change_Date=null;
    java.util.Calendar cal = java.util.Calendar.getInstance();
    PCDays=cal.get(cal.DATE);
    PCMonths=cal.get(cal.MONTH)+1;
    PCYears=cal.get(cal.YEAR);
    splits=0;
    siteReference="";
    siteReference2="";
    siteReference3="";
    unitQuantity=1;
    cdcsRequired=false;
    split=100;
    split2=0;
    split3=0;
    chargeDescriptionCode="";
    discountsApplicable="N";
    ScreenData.put("Discounts_Applicable",discountsApplicable);
    revenueType="1";
    revenueDescription="";
    revenueNetOrFull="";
    revenueRootCause="";
    chargeId=0;
    chargeBilled=false;
    chargeTrialled=false;
    toDateNullable=true;
    trialDelete="N";
    storedToDate=null;
    Charge_Description_for_List="All";
    c2RefNo_for_List="All";
    archived=false;
    c2RefNo="";
    c3RefNo="";
    reprovide=false;
    reopenBill="N";
    oldGrossAmount=0;
    chargeIdCheck = -1;
    circuitBillStartDate = "";
    toEnd = null;
  }
  public String getCharge_Description_for_List()
  {
    return Charge_Description_for_List;
  }
  public void setCharge_Description_for_List(String value)
  {
    Charge_Description_for_List = SU.hasNoValue(value,"All");
    if ((Charge_Description_for_List.equalsIgnoreCase("All")) &&
      (!Charge_Description_for_List.equals("All")))
      Charge_Description_for_List = "All";
  }
  public void setGSR_for_List(String value)
  {
    GSR_for_List=SU.hasNoValue(value,"All");
  }
  public Collection getSiteSplits()
  {
    return siteSplits;
  }
  public void addSiteSplit(SiteSplitDescriptor ssd)
  {
    siteSplits.add(ssd);
  }
  public void removeSiteSplit(SiteSplitDescriptor ssd)
  {
    siteSplits.remove(ssd);
  }
  public boolean checkSiteSplitTotal()
  {
    float total = 0;
    for (Iterator it = siteSplits.iterator(); it.hasNext(); )
    {
      SiteSplitDescriptor ssd = (SiteSplitDescriptor)it.next();
      total += ssd.getPercentageSplit();
    }
    return total==100;
  }
  public String getSite_Reference()
  {
    return siteReference;
  }
  public String getDiscountsApplicable()
  {
    return discountsApplicable;
  }
  public String getSite_Reference2()
  {
    return siteReference2;
  }
  public String getSite_Reference3()
  {
    return siteReference3;
  }
  private void isSiteValid(String siteId, String fieldName)
  {
    if (DBA.siteValid(siteId))
    {
      setErrored("clear");
      Message = "<font color=red><b>";
    }
    else
    {
      setErrored(fieldName);
      Message = "<font color=red><b>This site has no billing region assigned and cannot be used";
    }
  }
  private void setCDCsRequired()
  {
    if (!Mode.equals("Cease / "))
    {
      cdcsRequired = DBA.cdcsRequired(siteReference, siteReference2, siteReference3);
      if (cdcsRequired)
      {
        if (!mandatory.contains("Charge_Description_Code"))
        {
          mandatory.addElement("Charge_Description_Code");
        }
      }
      else
      {
        if (mandatory.contains("Charge_Description_Code"))
        {
          mandatory.removeElement("Charge_Description_Code");
        }
      }
    }
  }
  public void setSite_Reference(String value)
  {
    ScreenData.put("Site_Reference",value);
    siteReference = value;
    setCDCsRequired();
    isSiteValid(value, "Site_Reference");
  }
  public void setSite_Reference2(String value)
  {
    ScreenData.put("Site_Reference2",value);
    siteReference2 = value;
    setCDCsRequired();
    isSiteValid(value, "Site_Reference2");
  }
  public void setSite_Reference3(String value)
  {
    ScreenData.put("Site_Reference3",value);
    siteReference3 = value;
    setCDCsRequired();
    isSiteValid(value, "Site_Reference3");
  }
  public void setCircuit_Reference(String value)
  {
    ScreenData.put("Circuit_Reference",value);
    Circuit_Reference = value;
  }
  public String getCharge_Category()
  {
    return Charge_Category;
  }
  public String getReopenBill()
  {
    return reopenBill;
  }
  public String getCharge_CategoryDisplay()
  {
    return SU.isNull(Charge_CategoryDisplay," ");
  }
  public String getCharge_Description()
  {
    return Charge_Description;
  }
  public String getRevenueType()
  {
    return revenueType;
  }
  public String getRevenueDescription()
  {
    return revenueDescription;
  }
  public String getRevenueNetOrFull()
  {
    return revenueNetOrFull;
  }
  public String getRevenueRootCause()
  {
    return revenueRootCause;
  }
  public String getChargeDescriptionCode()
  {
    return chargeDescriptionCode;
  }
  public String getUnit_Quantity()
  {
    if ((String)ScreenData.get("Unit_Quantity") != null)
    {
      return (String)ScreenData.get("Unit_Quantity");
    }
    else
    {
      return String.valueOf(unitQuantity);
    }
  }
  public String getSplits()
  {
    String screenDataSplits = (String)ScreenData.get("Splits");
    if ((screenDataSplits != null) && (!screenDataSplits.equals("")))
    {
      return screenDataSplits;
    }
    else
    {
      return String.valueOf(splits);
    }
  }
  public String getSplit()
  {
    if ((String)ScreenData.get("Split") != null)
    {
      return (String)ScreenData.get("Split");
    }
    else
    {
      return String.valueOf(split);
    }
  }
  public String getSplit2()
  {
    if ((String)ScreenData.get("Split2") != null)
    {
      return (String)ScreenData.get("Split2");
    }
    else
    {
      return String.valueOf(split2);
    }
  }
  public String getSplit3()
  {
    if ((String)ScreenData.get("Split3") != null)
    {
      return (String)ScreenData.get("Split3");
    }
    else
    {
      return String.valueOf(split3);
    }
  }
  public String getOldGrossAmount()
  {
    return String.valueOf(oldGrossAmount);
  }
  public String getGross_Amount()
  {
    if ((String)ScreenData.get("Gross_Amount") != null)
    {
      return (String)ScreenData.get("Gross_Amount");
    }
    else
    {
      return String.valueOf(Gross_Amount);
    }
  }
  public String getCharge_Valid_From_Date()
  {
    if (Charge_Valid_From_Date==null)
      return null;
    else
      return SU.reformatDate(Charge_Valid_From_Date.toString());
  }
  public String getCharge_Period_Start_Date()
  {
    if (Charge_Period_Start_Date==null)
      return null;
    else
      return SU.reformatDate(Charge_Period_Start_Date.toString());
  }
  public String getCharge_Period_End_Date()
  {
    if (Charge_Period_End_Date==null)
      return null;
    else
      return SU.reformatDate(Charge_Period_End_Date.toString());
  }
  public String getPrice_Change_Date()
  {
    if (Price_Change_Date==null)
      return null;
    else
      return SU.reformatDate(Price_Change_Date.toString());
  }
  public String getCharge_Valid_To_Date()
  {
     if (Charge_Valid_To_Date != null)
    {
      return SU.reformatDate(Charge_Valid_To_Date.toString());
    }
    else
    {
      return "";
    }
  }
   public String getCRDB()
  {
    return SU.isNull(CRDB,"");
  }
  public String getCharge_Frequency()
  {
    return SU.isNull(Charge_Frequency,"");
  }
  public String getCharge_Currency()
  {
    return Charge_Currency;
  }
  public String getOSS_Charge_Type()
  {
    return OSS_Charge_Type;
  }
  public String getStandard_Bill_Indicator()
  {
    return Standard_Bill_Indicator;
  }
  public String getCharge_Entity()
  {
    return Charge_Entity;
  }
  public String getVAT_Indicator()
  {
    return VAT_Indicator;
  }
  public String getVATAmount()
  {
    if ((String)ScreenData.get("VAT_Amount") != null)
    {
      return (String)ScreenData.get("VAT_Amount");
    }
    else
    {
      return String.valueOf(VAT_Amount);
    }
  }
  public String getTotal_Amount()
  {
	return SU.roundToString(Gross_Amount+VAT_Amount,2);
  }
  public String getRevenue_Reason_Code()
  {
    return SU.isNull(Revenue_Reason_Code,"");
  }
  public String getCurrency_Display()
  {
    return Currency_Display;
  }
  public String getChargeFieldVisibility(String value)
  {
/*     if ((value.compareTo("Charge_Entity")==0) || (value.compareTo("Charge_Frequency")==0))
    {
      if (Charge_Category.compareTo("05")!=0)
        return "visible";
      else
	return "hidden";
    }
    if (value.compareTo("VAT")==0)
    {
      if (Charge_Category.compareTo("05")==0)
	return "visible";
      else
	return "visible";
    }*/
    if ((value.equals("ToDate")) || (value.equals("Charge_Frequency")))
    {
      if (Charge_Category.equals("01"))
	return "visible";
      else
	return "hidden";
    }
    if (value.equals("Unit_Quantity"))
    {
      if ((Charge_Category.equals("01")) && (!Mode.equals("Cease / ")))
	return "visible";
      else
	return "hidden";
    }
    if (value.equals("Repro"))
    {
      if (!Mode.equals("Cease / "))
        return "visible";
      else
        return "hidden";
    }
    if ((value.equals("Revenue_Description")) || (value.equals("Revenue_Net_Or_Full")) ||
      (value.equals("Revenue_Root_Cause")))
    {
      if (Charge_Category.equals("03"))
	return "visible";
      else
	return "hidden";
    }
    if (value.compareTo("Circuit_Ends")==0)
    {
      if (cdcsRequired)
	  return "visible";
      else
          return "hidden";
    }
    if (value.compareTo("CRDB")==0)
    {
      if (Charge_Category.compareTo("05")==0)
	  return "visible";
      else
          return "hidden";
    }
    else
    {
	return "visible";
    }
  }
  public String getC3RefNo()
  {
    return c3RefNo;
  }
  public String getC2RefNo_for_List()
  {
    return c2RefNo_for_List;
  }
  public void setC2RefNo_for_List(String value)
  {
    c2RefNo_for_List=SU.isBlank(value,"All");
  }
/*  public String getLLUC2RefNo()
  {
    return c2RefNo;
  }*/
  public void setC3RefNo(String value)
  {
    c3RefNo = value;
   ScreenData.put("C3_Ref_No",value);
  }
  public boolean disableDate(String FieldName, boolean disableAnyway)
  {
    if (disableAnyway)
      return true;
    else
    {
      if (((FieldName.equals("From_Charge_Valid_Date")) && ((chargeBilled) ||
        (Mode.equals("Cease / ")))) ||
        ((FieldName.equals("To_Charge_Valid_Date")) && (Mode.equals("Cease / "))) ||
        (FieldName.equals("Charge_Period_Start_Date") && chargeBilled) ||
        (FieldName.equals("Charge_Period_End_Date") && chargeBilled))
        return true;
      else
        return false;
    }
  }
  public String getMode(String FieldName)
  {
    if (archived)
        return "READONLY";
    if ((Mode.equals("Delete")) && (!FieldName.equals("BAN_Summary")))
        return "READONLY";
    if (action.compareTo("View") == 0)
    {
      if ((FieldName.endsWith("Date")) ||
          (FieldName.endsWith("Dateh")))
      {
        return "DISABLED";
      }
      else
      {
        return "READONLY";
      }
    }
    if (FieldName.compareTo("To_Charge_Valid_Date") ==0)
    {
      if (Mode.compareTo("Cease") == 0)
        return "";
      else
        return Calendar;
    }
    if (FieldName.compareTo("VAT_Amount") ==0)
    {
      if (((action.compareTo("Create") == 0) ||
       (action.compareTo("Amend") == 0)) &&
       (OSS_Charge_Type.compareTo("07") != 0))
        return "READONLY";
      else
        return InputMode;
    }
    if (FieldName.equals("Circuit_Reference"))
    {
      if ((Mode.equals("Amend")) || (Mode.equals("Cease / ")))
      {
        ScreenData.put("Circuit_Reference",Circuit_Reference);
        return "DISABLED";
      }
      else
        return SelectMode;
    }
    if (FieldName.equals("Site_Reference"))
    {
      if (Mode.equals("Cease / "))
      {
        ScreenData.put("Site_Reference",siteReference);
        return "DISABLED";
      }
      else
        return SelectMode;
    }
    if (FieldName.equals("Charge_Currency"))
    {
      if (Mode.equals("Cease / "))
      {
        ScreenData.put("Charge_Currency",Charge_Currency);
        return "DISABLED";
      }
      else
        return InputMode;
    }
    if (FieldName.equals("Charge_Frequency"))
    {
      if (chargeBilled)
        return "DISABLED";
      else if (Mode.equals("Cease / "))
      {
        ScreenData.put("Charge_Frequency",Charge_Frequency);
        return "DISABLED";
      }
      else
        return InputMode;
    }
     if (FieldName.equals("Charge_Entity"))
    {
      if (chargeBilled)
        return "DISABLED";
      else
        return InputMode;
    }
     if (FieldName.equals("Unit_Quantity"))
    {
      if (chargeBilled)
        return "READONLY";
    }
    /*if (FieldName.compareTo("Gross_Amount") ==0)
    {
      if (((action.compareTo("Create") == 0) ||
       (action.compareTo("Amend") == 0)) &&
       ((OSS_Charge_Type.compareTo("07")) != 0) && (OSS_Charge_Type.compareTo("") !=0))
        return InputMode;
      else
        return "READONLY";
    }*/
    if ((action.compareTo("Cancel") == 0) ||
          (action.compareTo("Cease") == 0) ||
          (Mode.compareTo("Cease") == 0))
    {
      if ((FieldName.compareTo("BAN_Summary") ==0)  ||
            (FieldName.compareTo("BAN_Reason") ==0))
      {
        return "";
      }
      else if (FieldName.compareTo("Effective_Date") ==0)
      {
        return "no";
      }
      else return InputMode;
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
/********************************************************************************************/
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
public void setParameter(String name,String value)
  {
    if (name.compareTo("Charge_Description")==0)
    {
      setCharge_Description(value);
    }
    else if (name.compareTo("Revenue_Type")==0)
    {
      setRevenueType(value);
    }
    else if (name.compareTo("Reopen_Bill")==0)
    {
      setReopenBill(value);
    }
    else if (name.compareTo("Revenue_Description")==0)
    {
      setRevenueDescription(value);
    }
    else if (name.compareTo("Revenue_Net_Or_Full")==0)
    {
      setRevenueNetOrFull(value);
    }
    else if (name.compareTo("Revenue_Root_Cause")==0)
    {
      setRevenueRootCause(value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
    }
    else if (name.compareTo("Discounts_Applicable")==0)
    {
      setDiscountsApplicable(value);
    }
    else if (name.startsWith("Required_BAN_Effective_Dateh"))
    {
      setRequired_BAN_Effective_Date(value);
      ScreenData.put("Required_BAN_Effective_Dateh",value);
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
    else if (name.compareTo("From_Charge_Valid_Dateh")==0)
    {
      setFrom_Charge_Valid_Date(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateDay"))
    {
      setFromChargeValidDateDays(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateMonth"))
    {
      setFromChargeValidDateMonths(value);
    }
    else if (name.startsWith("From_Charge_Valid_DateYear"))
    {
      setFromChargeValidDateYears(value);
    }
    else if (name.compareTo("To_Charge_Valid_Dateh")==0)
    {
      setTo_Charge_Valid_Date(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateDay"))
    {
      setToChargeValidDateDays(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateMonth"))
    {
      setToChargeValidDateMonths(value);
    }
    else if (name.startsWith("To_Charge_Valid_DateYear"))
    {
      setToChargeValidDateYears(value);
    }
    else if (name.equals("Price_Change_Dateh"))
    {
      setPrice_Change_Date(value);
    }
    else if (name.startsWith("Price_Change_DateDay"))
    {
      setPriceChangeDateDays(value);
    }
    else if (name.startsWith("Price_Change_DateMonth"))
    {
      setPriceChangeDateMonths(value);
    }
    else if (name.startsWith("Price_Change_DateYear"))
    {
      setPriceChangeDateYears(value);
    }
    else if (name.startsWith("Circuit_Reference"))
    {
      setCircuit_Reference(value);
    }
    else if (name.equals("Site_Reference"))
    {
      setSite_Reference(value);
    }
    else if (name.equals("Site_Reference2"))
    {
      setSite_Reference2(value);
    }
    else if (name.equals("Site_Reference3"))
    {
      setSite_Reference3(value);
    }
    else if (name.startsWith("Splits"))
    {
      setSplits(value);
    }
    else if (name.equals("Split"))
    {
      setSplit(value);
    }
    else if (name.equals("Split2"))
    {
      setSplit2(value);
    }
    else if (name.equals("Split3"))
    {
      setSplit3(value);
    }
    else if (name.compareTo("Gross_Amount")==0)
    {
      setGross_Amount(value);
    }
    else if (name.compareTo("Unit_Quantity")==0)
    {
      setUnit_Quantity(value);
    }
    else if (name.compareTo("Charge_Currency")==0)
    {
      setCharge_Currency(value);
    }
    else if (name.compareTo("Charge_Frequency")==0)
    {
      setCharge_Frequency(value);
    }
    else if (name.compareTo("Charge_Description_Code")==0)
    {
      setChargeDescriptionCode(value);
    }
    else if (name.compareTo("OSS_Charge_Type")==0)
    {
      setOSS_Charge_Type(value);
    }
    else if (name.compareTo("Standard_Bill_Indicator")==0)
    {
      setStandard_Bill_Indicator(value);
    }
    else if (name.compareTo("Charge_Entity")==0)
    {//if a 'Clear' action has been requested, set the Entity to 'C&W'
      if (value.compareTo("") == 0)
      {
        setCharge_Entity("C&W");
	ScreenData.put("Charge_Entity","C&W");
      }
      else
      {
        setCharge_Entity(value);
        ScreenData.put("Charge_Entity",value);
      }
    }
    else if (name.compareTo("RejectReason")==0)
    {
      setRejectReason(value);
    }
    else if (name.compareTo("VAT_Amounth")==0)
    {
      setVAT_Amount(value);
    }
    else if (name.compareTo("Revenue_Reason")==0)
    {
      setRevenue_Reason_Code(value);
    }
    else if (name.compareTo("Total_Amounth")==0)
    {
      setTotal_Amount(value);
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
    else if (name.equals("Charge_Period_End_Dateh"))
    {
      setChargePeriodEndDate(value);
    }
    else if (name.startsWith("Charge_Period_End_DateDay"))
    {
      setChargePeriodEndDateDays(value);
    }
    else if (name.startsWith("Charge_Period_End_DateMonth"))
    {
      setChargePeriodEndDateMonths(value);
    }
    else if (name.startsWith("Charge_Period_End_DateYear"))
    {
      setChargePeriodEndDateYears(value);
    }
    else if (name.equals("Charge_Period_Start_Dateh"))
    {
      setChargePeriodStartDate(value);
    }
    else if (name.startsWith("Charge_Period_Start_DateDay"))
    {
      setChargePeriodStartDateDays(value);
    }
    else if (name.startsWith("Charge_Period_Start_DateMonth"))
    {
      setChargePeriodStartDateMonths(value);
    }
    else if (name.startsWith("Charge_Period_Start_DateYear"))
    {
      setChargePeriodStartDateYears(value);
    }
  }
  /*********************************************************************/
  public void setRevenueType(String value)
  {
    revenueType = value;
    ScreenData.put("Revenue_Type",value);
  }
  public void setReopenBill(String value)
  {
    reopenBill = value;
    ScreenData.put("Reopen_Bill",value);
  }
  public void setRevenueDescription(String value)
  {
    revenueDescription = value;
    ScreenData.put("Revenue_Description",value);
  }
  public void setRevenueNetOrFull(String value)
  {
    revenueNetOrFull = value;
    ScreenData.put("Revenue_Net_Or_Full",value);
  }
  public void setRevenueRootCause(String value)
  {
    revenueRootCause = value;
    ScreenData.put("Revenue_Root_Cause",value);
  }
  public void setCharge_Description(String value)
  {
    String noApostrophe = SU.removeChar(SU.isNull(value,""), '\'');
    String noReturn = noApostrophe.replace('\n', ' ').replace('\r', ' ');
    Charge_Description = noReturn;
    ScreenData.put("Charge_Description",noReturn);
  }
  public void setChargeDescriptionCode(String value)
  {
    chargeDescriptionCode = value;
    ScreenData.put("Charge_Description_Code",value);
  }
  public void setUnit_Quantity(String value)
  {
    ScreenData.put("Unit_Quantity",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        unitQuantity = Integer.parseInt(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      unitQuantity=1;
    }
  }
  public void setSplits(String value)
  {
    ScreenData.put("Splits",value);
    try
    {
      splits = Integer.parseInt(value);
    }
    catch (java.lang.NumberFormatException NE)
    {
    }
    if (splits == 0)
    {
      setSplit("100");
      setSplit2("0");
      setSplit3("0");
      setSite_Reference2("");
      setSite_Reference3("");
    }
    else if (splits == 1)
    {
      setSplit3("0");
      setSite_Reference3("");
    }
  }
  public void setSplit(String value)
  {
    ScreenData.put("Split",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        split = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      split=0;
    }
  }
  public void setSplit2(String value)
  {
    ScreenData.put("Split2",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        split2 = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      split2=0;
    }
  }
  public void setSplit3(String value)
  {
    ScreenData.put("Split3",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        split3 = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      split3=0;
    }
  }
  public void setGross_Amount(String value)
  {
    if (!Charge_Category.equals("01"))
    {
      //get rid of negative if present
      if (value.indexOf('-') != -1)
        value = value.replace('-',' ').trim();
    }
    ScreenData.put("Gross_Amount",value);
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        Gross_Amount = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      Gross_Amount=0;
    }
  }
  public void setVAT_Amount(String value)
  {
    if ((value.compareTo("")!=0) && (value !=null))
    {
      ScreenData.put("VAT_Amount",value);
      try
      {
        VAT_Amount = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
      }
    }
    else
    {
      VAT_Amount=0;
    }
    Total_Amount=Gross_Amount+VAT_Amount;
  }
  public void setTotal_Amount(String value)
  {
    if ((value.compareTo("")!=0) && (value !=null))
    {
      try
      {
        Total_Amount = Double.parseDouble(value);
      }
      catch (java.lang.NumberFormatException NE)
      {
        Total_Amount=0;
      }
    }
    else
    {
      Total_Amount=0;
    }
  }
  public void setCRDB(String value)
  {
    CRDB=value;
    ScreenData.put("CRDB",value);
  }
  public void setVAT_Indicator(String Charge_Entity,String Local_Tax_Rate,String VAT_Code)
  {
    if (Charge_Entity.compareToIgnoreCase("C&W")==0)
    {
      if (VAT_Code.compareToIgnoreCase("SR")==0)
      {
	VAT_Indicator="Y";
      }
      else
      {
	VAT_Indicator="N";
      }
    }
    else if (SU.toFloat(Local_Tax_Rate) != 0)
    {
	VAT_Indicator="Y";
      }
      else
      {
	VAT_Indicator="N";
      }
  }
  public void setTo_Charge_Valid_Date(String value)
  {
    Charge_Valid_To_Date = SU.toJDBCDate(value);
    ScreenData.put("To_Charge_Valid_Dateh",value);
  }
  public void setFrom_Charge_Valid_Date(String value)
  {
    Charge_Valid_From_Date = SU.toJDBCDate(value);
    ScreenData.put("From_Charge_Valid_Dateh",value);
  }
  public void setPrice_Change_Date(String value)
  {
    Price_Change_Date = SU.toJDBCDate(value);
    ScreenData.put("Price_Change_Dateh",value);
  }
  public void setChargePeriodStartDate(String value)
  {
    Charge_Period_Start_Date = SU.toJDBCDate(value);
    ScreenData.put("Charge_Period_Start_Dateh",value);
  }
  public void setChargePeriodEndDate(String value)
  {
    Charge_Period_End_Date = SU.toJDBCDate(value);
    ScreenData.put("Charge_Period_End_Dateh",value);
  }
  public void setCharge_Currency(String value)
  {
    Charge_Currency=value;
    ScreenData.put("Charge_Currency",value);
  }
  public void setDiscountsApplicable(String value)
  {
    discountsApplicable=value;
    ScreenData.put("Discounts_Applicable",value);
  }
  public void setCharge_Frequency(String value)
  {
    Charge_Frequency=value;
    ScreenData.put("Charge_Frequency",value);
  }
  public void setOSS_Charge_Type(String value)
  {
    OSS_Charge_Type=value;
    ScreenData.put("OSS_Charge_Type",value);
  }
  public void setStandard_Bill_Indicator(String value)
  {
    Standard_Bill_Indicator=value;
    ScreenData.put("Standard_Bill_Indicator",value);
  }

  public void setMode(String value)
  {
    Mode=value;
    setdefaultFieldModes();
    if (Mode.equals("Cease"))
    {
      if (!mandatory.contains("To_Charge_Valid_Date"))
      {
        mandatory.addElement("To_Charge_Valid_Date");
      }
    }
    else
    {
      if (mandatory.contains("To_Charge_Valid_Date"))
      {
        mandatory.removeElement("To_Charge_Valid_Date");
      }
    }

    if (Mode.equals("Cease / "))
    {
      if (mandatory.contains("Unit_Quantity"))
      {
        mandatory.removeElement("Unit_Quantity");
      }
      if (mandatory.contains("Discounts_Applicable"))
      {
        mandatory.removeElement("Discounts_Applicable");
      }
      if (mandatory.contains("Revenue_Type"))
      {
        mandatory.removeElement("Revenue_Type");
      }
      if (mandatory.contains("Revenue_Description"))
      {
        mandatory.removeElement("Revenue_Description");
      }
      if (mandatory.contains("Revenue_Net_Or_Full"))
      {
        mandatory.removeElement("Revenue_Net_Or_Full");
      }
      if (mandatory.contains("Revenue_Root_Cause"))
      {
        mandatory.removeElement("Revenue_Root_Cause");
      }
      if (mandatory.contains("Splits"))
      {
        mandatory.removeElement("Splits");
      }
      if (mandatory.contains("Charge_Entity"))
      {
        mandatory.removeElement("Charge_Entity");
      }
      if (mandatory.contains("Charge_Description_Code"))
      {
        mandatory.removeElement("Charge_Description_Code");
      }
      if (!mandatory.contains("Price_Change_Dateh"))
      {
        mandatory.addElement("Price_Change_Dateh");
      }
      if (!mandatory.contains("Reopen_Bill"))
      {
        mandatory.addElement("Reopen_Bill");
      }
    }
    else
    {
      if (mandatory.contains("Price_Change_Dateh"))
      {
        mandatory.removeElement("Price_Change_Dateh");
      }
      if (mandatory.contains("Reopen_Bill"))
      {
        mandatory.removeElement("Reopen_Bill");
      }
      if (!mandatory.contains("Unit_Quantity"))
      {
        mandatory.addElement("Unit_Quantity");
      }
      if (!mandatory.contains("Discounts_Applicable"))
      {
        mandatory.addElement("Discounts_Applicable");
      }
      if (!mandatory.contains("Revenue_Type"))
      {
        mandatory.addElement("Revenue_Type");
      }
      if (!mandatory.contains("Splits"))
      {
        mandatory.addElement("Splits");
      }
      if (!mandatory.contains("Charge_Entity"))
      {
        mandatory.addElement("Charge_Entity");
      }
    }
  }

  /**********************************************************************************/
  public void setCharge_Entity(String value)
  {
    Charge_Entity=SU.isNull(value,"");

    ScreenData.put("Charge_Entity",value);
    if (Charge_Entity.compareTo("C&W")==0)
    {
      Currency_Desc="GBP";
      Currency_Display="<font color=black> (UK Pounds Sterling)";
    }
    else
    {
      Enumeration e;
      StringBuffer SQLBuffer2 = new StringBuffer("");
      SQLBuffer2.insert(0,"\0");
      SQLBuffer2.append("select CA.carrier_currency_code,CD.currency_desc from oss..carrier CA,givn..Currency_Desc CD,OSS..circuit C");
      SQLBuffer2.append(" where C.CW_Circuit_Ref = '").append(Circuit_Reference);
      SQLBuffer2.append("' and C.Foreign_Carrier_Name = CA.Carrier_Name and CD.Currency_Code =* CA.Carrier_Currency_Code");

      e=DBA.getResults(SQLBuffer2.toString(),P5);
      //Datasource changes e=DBA.getResults(SQLBuffer2.toString());

      if (e.hasMoreElements())
      {
	Currency_Desc=SU.isNull((String)e.nextElement(),"");
	if (e.hasMoreElements())
	{
	  Currency_Display="<font color=black> ("+SU.isNull((String)e.nextElement(),"")+")";
	}
	else
	{
	  Currency_Display="<font color=black> ("+Currency_Desc+")";
	}
      }
      else
      {
	Currency_Display=" (No valid Currency Code on Carrier)";
      }

    }
    ScreenData.put("Currency_Desc",Currency_Desc);
  }
  public void setCharge_Category(String value)
  {
    boolean resetSBI = false;
    if (Standard_Bill_Indicator.equals(""))
    {
      Standard_Bill_Indicator="N";
      resetSBI = true;
    }
    Charge_CategoryDisplay=(SU.after(value));
    Charge_Category = SU.before(value);
    if (Mode.equals("Cease / "))
    {
      if (mandatory.contains("Unit_Quantity"))
        mandatory.removeElement("Unit_Quantity");
      if (mandatory.contains("Charge_Entity"))
        mandatory.removeElement("Charge_Entity");
      if (mandatory.contains("Revenue_Type"))
        mandatory.removeElement("Revenue_Type");
    }
    else
    {
      if (Charge_Category.equals("01"))
      {
        if (!mandatory.contains("Unit_Quantity"))
          mandatory.addElement("Unit_Quantity");
        if (!mandatory.contains("Charge_Frequency"))
          mandatory.addElement("Charge_Frequency");
        if (mandatory.contains("Revenue_Description"))
          mandatory.removeElement("Revenue_Description");
        if (mandatory.contains("Revenue_Net_Or_Full"))
          mandatory.removeElement("Revenue_Net_Or_Full");
        if (mandatory.contains("Revenue_Root_Cause"))
          mandatory.removeElement("Revenue_Root_Cause");
      }
      else if (Charge_Category.equals("02"))
      {
        if (mandatory.contains("Unit_Quantity"))
          mandatory.removeElement("Unit_Quantity");
        if (mandatory.contains("Charge_Frequency"))
          mandatory.removeElement("Charge_Frequency");
        if (mandatory.contains("Revenue_Description"))
          mandatory.removeElement("Revenue_Description");
        if (mandatory.contains("Revenue_Net_Or_Full"))
          mandatory.removeElement("Revenue_Net_Or_Full");
        if (mandatory.contains("Revenue_Root_Cause"))
          mandatory.removeElement("Revenue_Root_Cause");
        revenueDescription = DBA.getNotApplicable("Description");
        revenueNetOrFull = DBA.getNotApplicable("Net_Or_Full");
        revenueRootCause = DBA.getNotApplicable("Root_Cause");
      }
      else
      {
        if (mandatory.contains("Unit_Quantity"))
          mandatory.removeElement("Unit_Quantity");
        if (mandatory.contains("Charge_Frequency"))
          mandatory.removeElement("Charge_Frequency");
        if (!mandatory.contains("Revenue_Description"))
          mandatory.addElement("Revenue_Description");
        if (!mandatory.contains("Revenue_Net_Or_Full"))
          mandatory.addElement("Revenue_Net_Or_Full");
        if (!mandatory.contains("Revenue_Root_Cause"))
          mandatory.addElement("Revenue_Root_Cause");
      }
    }
/*    if (Charge_Category.compareTo("01")==0)
    {
      ChargeDateHeading="Charge Valid From Date";
      ChargeFieldVisibility="visible";
      CRDB="Debit";
      if (resetSBI)
        Standard_Bill_Indicator="Y";
    }
    else if (Charge_Category.compareTo("02")==0)
    {
      ChargeDateHeading="Charge Due Date";
      ChargeFieldVisibility="hidden";
      CRDB="Debit";
    }
    else if ((Charge_Category.compareTo("03")==0) || (Charge_Category.compareTo("04")==0))
    {
      ChargeDateHeading="Credit Payable Date";
      ChargeFieldVisibility="hidden";
      CRDB="Credit";
    }
    else if (Charge_Category.compareTo("05")==0)
    {
      ChargeDateHeading="Valid From Date";
      ChargeFieldVisibility="hidden";
    }
    else
    {
      ChargeDateHeading="Charge Date";
      ChargeFieldVisibility="hidden";
    }*/
  }
  public void setCharge_Id(String value)
  {
    Charge_Id=SU.toInt(value);
  }
  public long getChargeIdCheck()
  {
    return chargeIdCheck;

  }
  public void setChargeIdCheck(long value)
  {
    chargeIdCheck = value;
  }
  public void setRevenue_Reason_Code(String value)
  {
    Revenue_Reason_Code=value;
  }
  /*public void setMode(String value)
  {
    super.setMode(value);
    if (Mode.compareTo("Create")==0)
    {
      //Reset();
      //banIdentifier="";
    }
  }*/
  /*public void setAction(String value)
  {
    super.setAction(value);
    if (value.compareTo("Create")==0)
    {
      Reset();
      banIdentifier="";
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
  //StringBuffer sb = new StringBuffer();

  //First set of checks do not apply to draft saves
  if (ButtonPressed.startsWith("Submit"))
  {
    if (Mode.equals("Cease / "))
    {
      if (Price_Change_Date.compareTo(Charge_Valid_From_Date) < 0)
      {
        setErrored("From_Charge_Valid_Dateh");
        setErrored("Price_Change_Dateh");
        Message = "<font color=red><b>Effective Date of Price Change must not be before Valid From Date";
        return false;
      }
      if (Charge_Valid_To_Date != null)
      {
        if (Price_Change_Date.compareTo(Charge_Valid_To_Date) >= 0)
        {
          setErrored("From_Charge_Valid_Dateh");
          setErrored("Price_Change_Dateh");
          Message = "<font color=red><b>Effective Date of Price Change must be before Valid To Date";
          return false;
        }
      }
    }
    if (Mode.equals("Cease"))
    {
      if (Charge_Valid_To_Date == null)
      {
        setErrored("To_Charge_Valid_Date");
        Message = "<font color=red><b>You must enter a value for Valid To Date";
        return false;
      }
      if(BAN_Summary.compareTo("") == 0)
      {
        setErrored("BAN_Summary");
        Message = "<font color=red><b>You must enter a value for BAN Summary";
        return false;
      }
    }
    else
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if(FormField.equals(""))
        {
          String fieldMode = getMode(FieldName);
          if ((!fieldMode.equalsIgnoreCase("DISABLED")) &&
            (!fieldMode.equalsIgnoreCase("READONLY")))
          {
            setErrored(FieldName);
            //sb.append(" " + FieldName);
          }
        }
        else if (FieldName.startsWith("Site_Reference"))
        {
          if (!DBA.siteValid(FormField))
          {
            setErrored(FieldName);
            Message = "<font color=red><b>This site has no billing region assigned and cannot be used";
            return false;
          }
        }
      }
      if ((!errored.isEmpty()) && (errored.size() > 0))
      {
          Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
          // + sb.toString();
          //Message=(String)errored.firstElement();
          return false;
      }

    }
  }
  //These checks apply to all saves
  if ((Charge_Valid_To_Date != null) && (Charge_Valid_From_Date != null))
  {
    if (Charge_Valid_To_Date.before(Charge_Valid_From_Date))
    {
      setErrored("From_Charge_Valid_Dateh");
      setErrored("To_Charge_Valid_Dateh");
      Message = "<font color=red><b>Valid To Date cannot be less than Valid From Date";
      return false;
    }
  }
  if (!Charge_Category.equals("01"))
  {
    if ((Charge_Period_Start_Date != null) && (Charge_Period_End_Date != null))
    {
      if (Charge_Period_End_Date.before(Charge_Period_Start_Date))
      {
        setErrored("Charge_Period_Start_Dateh");
        setErrored("Charge_Period_End_Dateh");
        Message = "<font color=red><b>Period End Date cannot be less than Period Start Date";
        return false;
      }
    }
    if (((Charge_Period_Start_Date == null) && (Charge_Period_End_Date != null)) ||
      ((Charge_Period_Start_Date != null) && (Charge_Period_End_Date == null)))
    {
      setErrored("Charge_Period_Start_Dateh");
      setErrored("Charge_Period_End_Dateh");
      Message = "<font color=red><b>Period Start Date and Period End Date must be entered together";
      return false;
    }
  }
  if ((Mode.equals("Amend")) && (Charge_Category.equals("01")))
  {
    if (Charge_Valid_To_Date == null)
    {
      if ((storedToDate != null) && (!toDateChangeable()))
      {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Charge_Valid_To_Date = storedToDate;
        cal.setTime(Charge_Valid_To_Date);
        TCDays=cal.get(cal.DATE);
        TCMonths=cal.get(cal.MONTH)+1;
        TCYears=cal.get(cal.YEAR);
        setErrored("To_Charge_Valid_Date");
        Message = "<font color=red><b>Valid To Date cannot be changed as charge has been billed";
        return false;
      }
    }
    else
    {
      if ((!Charge_Valid_To_Date.equals(storedToDate)) && (!toDateChangeable()))
      {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Charge_Valid_To_Date = storedToDate;
        cal.setTime(Charge_Valid_To_Date);
        TCDays=cal.get(cal.DATE);
        TCMonths=cal.get(cal.MONTH)+1;
        TCYears=cal.get(cal.YEAR);
        setErrored("To_Charge_Valid_Date");
        Message = "<font color=red><b>Valid To Date cannot be changed as charge has been billed";
        return false;
      }
    }
  }
  if (!checkBillingStartDate())
  {
    setErrored("From_Charge_Valid_Dateh");
    Message = "<font color=red><b>Valid From Date cannot be before " +
      "circuit billing start date - " + circuitBillStartDate;
    return false;
  }
  if (cdcsRequired && !chargeDescriptionCode.equals("") && !checkToEnd())
  {
    setErrored("Charge_Description_Code");
    setErrored("Circuit_Reference");
    Message = "<font color=red><b>This charge description code is invalid " +
      "as the GSR has no to end present ";
    return false;
  }
  //Amounts must be numeric
  try
  {
    Gross_Amount = Double.parseDouble((String)ScreenData.get("Gross_Amount"));
  }
  catch (java.lang.NumberFormatException NE)
  {
    setErrored("Gross_Amount");
    Message = "<font color=red><b>Unit Charge Amount must be numeric";
    return false;
  }
  if (!Mode.equals("Cease / "))
  {
    try
    {
      unitQuantity = Integer.parseInt((String)ScreenData.get("Unit_Quantity"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      setErrored("Unit_Quantity");
      Message = "<font color=red><b>Unit Quantity must be numeric";
      return false;
    }
    if ((unitQuantity < 1) || (unitQuantity > 999))
    {
      setErrored("Unit_Quantity");
      Message = "<font color=red><b>Unit Quantity must be between 1 and 999 inclusive";
      return false;
    }
  }
  if (splits == 1)
  {
    if (siteReference.equals("") || siteReference2.equals(""))
    {
      setErrored("Site_Reference");
      Message = "<font color=red><b>Please select two Sites";
      return false;
    }
    else if (siteReference.equals(siteReference2))
    {
      setErrored("Site_Reference");
      Message = "<font color=red><b>Each Site must be different";
      return false;
    }
    else if ((split == 0) || (split2 == 0))
    {
      setErrored("Site_Reference");
      setErrored("Splits");
      Message = "<font color=red><b>Both Split Percentages must be greater than zero and add up to 100";
      return false;
    }

  }
  if (splits == 2)
  {
    if (siteReference.equals("") || siteReference2.equals("") ||
      siteReference3.equals(""))
    {
      setErrored("Site_Reference");
      Message = "<font color=red><b>Please select three Sites";
      return false;
    }
    else if ((siteReference.equals(siteReference2)) ||
      (siteReference.equals(siteReference3)) ||
      (siteReference2.equals(siteReference3)))
    {
      setErrored("Site_Reference");
      Message = "<font color=red><b>Each Site must be different";
      return false;
    }
    else if ((split == 0) || (split2 == 0) || (split3 == 0))
    {
      setErrored("Site_Reference");
      setErrored("Splits");
      Message = "<font color=red><b>All Split Percentages must be greater than zero and add up to 100";
      return false;
    }

  }
  if ((split + split2 + split3) != 100)
  {
    setErrored("Site_Reference");
    setErrored("Splits");
    Message = "<font color=red><b>Split Percentages must add up to 100";
    return false;
  }


  return true;
}
/****************************************************************************************/
  public String getChargeHeader()
 {
    StringBuffer HB=new StringBuffer("<br><table border=0><tr class=gridHeader>");

    //Column Headings

      HB.append("<tr><td class=gridHeader NOWRAP valign=top width=450><button class=grid_menu>Description</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=60><button class=grid_menu>Amount</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=198><button class=grid_menu>" +
        (Charge_Category.equals("01")?"From":"Charge Payable") + " Date</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=200><button class=grid_menu>" +
        (Charge_Category.equals("01")?"To Date":"") + "</button></td>");
      HB.append("<td class=gridHeader NOWRAP valign=top width=45><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>");

      //Filters

/*      HB.append("<TR><TD class=grid1>");//<SPAN id=Desc");
      //HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 450px; POSITION: absolute; TOP: 199px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append("<iframe width=445px scrolling=no hspace=0 marginheight=0 marginwidth=0 src=\"" +
        "filterInput.jsp?name=Description&value=" + Charge_Description_for_List +
        "\" style=\"height:18px;\"></iframe>");
      HB.append("</tr></td></table></TD>");
      //HB.append("</tr></td></table></SPAN></TD>");
      HB.append("<TD class=grid1><SPAN id=Amount");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 60px; POSITION: absolute; TOP: 177px\"></TD>");
      HB.append("<TD class=grid1>");//<SPAN id=From");
      //HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 200px; POSITION: absolute; TOP: 199px\">");
      HB.append("<TABLE cellSpacing=0 cellPadding=0 border=0>\n");
      HB.append("<TR><TD class=grid1>");
      HB.append(DBA.getListBox("List_Month2","submit",Month_for_List,"",1,"style=\"width:196px\"",true));
      HB.append("</tr></td></table></TD>");
      //HB.append("</tr></td></table></SPAN></TD>");
      HB.append("<TD class=grid1><SPAN id=To");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 200px; POSITION: absolute; TOP: 177px\"></TD>");
      HB.append("<TD class=grid1><SPAN id=Select");
      HB.append(" style=\"VISIBILITY: ").append(showFilters).append("; WIDTH: 45px; POSITION: absolute; TOP: 177px\"></TD>");
*/
      HB.append("</TR></TABLE>");

    return HB.toString();
  }
/*****************************************************************************************/
  public boolean createChargeBAN()
  {
    boolean createChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Create_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
	cstmt.setString(2,BAN_Summary);
	cstmt.setString(3,banCreatedBy);
	cstmt.setDate(4,Required_BAN_Effective_Dateh);
        cstmt.setString(5,Circuit_Reference);
	cstmt.setDouble(6,Gross_Amount);
	cstmt.setString(7,chargeDescriptionCode);
	cstmt.setDate(8,Charge_Valid_From_Date);
	cstmt.setDate(9,Charge_Valid_To_Date);
	cstmt.setString(10,Charge_Description);
        cstmt.setInt(11,splits);
        cstmt.setString(12,siteReference);
        cstmt.setString(13,siteReference2);
        cstmt.setString(14,siteReference3);
        cstmt.setDouble(15,split);
        cstmt.setDouble(16,split2);
        cstmt.setDouble(17,split3);
	cstmt.setString(18,Charge_Frequency);
	cstmt.setString(19,Charge_Currency);
	cstmt.setString(20,revenueType);
	cstmt.setString(21,"Z");
	cstmt.setString(22,"Z");
	cstmt.setString(23,"Z");
        cstmt.setInt(24,unitQuantity);
        cstmt.setString(25,Global_Customer_Id);
	cstmt.setString(26,Mode);
	cstmt.setString(27,Charge_Entity);

	try
	{
          cstmt.execute();
	  RS = cstmt.getResultSet();
	  if (RS.next())
	  {
	    banIdentifier=RS.getString(1);
            createChargeBAN = true;
            Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
	  }
	  ScreenData.clear();
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
      return createChargeBAN;
    }

  }
/*****************************************************************************************/
  public boolean authoriseChargeBAN()
  {
    boolean authoriseChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        if (Charge_Category.equals("01"))
        {
          SQL = "{call eban.dbo.Authorise_Charge_BAN " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,banStatus);
          cstmt.setString(2,BAN_Summary);
          cstmt.setString(3,banCreatedBy);
          cstmt.setDate(4,Required_BAN_Effective_Dateh);
          cstmt.setString(5,Circuit_Reference);
          cstmt.setDouble(6,Gross_Amount);
          cstmt.setString(7,chargeDescriptionCode.equals("")?null:chargeDescriptionCode);
          cstmt.setDate(8,Charge_Valid_From_Date);
          cstmt.setDate(9,Charge_Valid_To_Date);
          cstmt.setString(10,Charge_Description);
          cstmt.setInt(11,splits);
          cstmt.setString(12,siteReference);
          cstmt.setString(13,siteReference2);
          cstmt.setString(14,siteReference3);
          cstmt.setDouble(15,split);
          cstmt.setDouble(16,split2);
          cstmt.setDouble(17,split3);
          cstmt.setString(18,Charge_Frequency);
          cstmt.setString(19,Charge_Currency);
          cstmt.setString(20,revenueType);
          cstmt.setString(21,"Z");
          cstmt.setString(22,"Z");
          cstmt.setString(23,"Z");
          cstmt.setInt(24,unitQuantity);
          cstmt.setString(25,Global_Customer_Id);
          cstmt.setString(26,Mode);
          cstmt.setString(27,Charge_Entity);
          cstmt.setString(28,c2RefNo);
          cstmt.setString(29,c3RefNo);
       }
        else
        {
          SQL = "{call eban.dbo.Authorise_" +
            (Charge_Category.equals("02")?"One_Off_Charge":"Credit") + "_BAN " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,banStatus);
          cstmt.setString(2,BAN_Summary);
          cstmt.setString(3,banCreatedBy);
          cstmt.setDate(4,Required_BAN_Effective_Dateh);
          cstmt.setString(5,Circuit_Reference);
          cstmt.setDouble(6,Gross_Amount);
          cstmt.setString(7,chargeDescriptionCode.equals("")?null:chargeDescriptionCode);
          cstmt.setDate(8,Charge_Valid_From_Date);
          cstmt.setString(9,Charge_Description);
          cstmt.setInt(10,splits);
          cstmt.setString(11,siteReference);
          cstmt.setString(12,siteReference2);
          cstmt.setString(13,siteReference3);
          cstmt.setDouble(14,split);
          cstmt.setDouble(15,split2);
          cstmt.setDouble(16,split3);
          cstmt.setString(17,Charge_Currency);
          cstmt.setString(18,revenueType);
          cstmt.setString(19,revenueDescription);
          cstmt.setString(20,revenueNetOrFull);
          cstmt.setString(21,revenueRootCause);
          cstmt.setString(22,Global_Customer_Id);
          cstmt.setString(23,Mode);
          cstmt.setString(24,Charge_Entity);
          cstmt.setString(25,c2RefNo);
          cstmt.setString(26,c3RefNo);
          cstmt.setDate(27,Charge_Period_Start_Date);
          cstmt.setDate(28,Charge_Period_End_Date);
        }

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            authoriseChargeBAN = false;
            Message = "<font color=red><b>Unable to authorise BAN: " +
              (ret.equals("-99")?"a bill job is currently running for this customer."
              :("return code " + ret)) + "</b></font>";

          }
          else
          {
            authoriseChargeBAN = true;
            banIdentifier = ret;
            Message = "<font color=blue><b>BAN Id:-" + banIdentifier +
              " authorised and " +
                (Charge_Category.equals("03")?"credit":"charge") + " created";
          }
        }
        ScreenData.clear();
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
      return authoriseChargeBAN;
    }

  }
/*****************************************************************************************/
  public boolean updateChargeBANCR()
  {
    boolean updateChargeBANCR = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban.dbo.Update_Charge_BAN_CR " +
          "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banStatus);
        cstmt.setString(2,BAN_Summary);
        cstmt.setString(3,banCreatedBy);
        cstmt.setDate(4,Price_Change_Date);
        cstmt.setString(5,Circuit_Reference);
        cstmt.setDouble(6,Gross_Amount);
        cstmt.setDate(7,Charge_Valid_From_Date);
        cstmt.setString(8,Charge_Description);
        cstmt.setString(9,Charge_Currency);
        cstmt.setString(10,Global_Customer_Id);
        cstmt.setString(11,"CR");
        cstmt.setInt(12,Charge_Id);
        cstmt.setString(13,c2RefNo);
        cstmt.setString(14,reopenBill);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String ret = RS.getString(1);
          Message = "<font color=blue><b>BAN Id:-" + ret +
            " authorised and charge updated";
          if (ret.startsWith("-"))
          {
            updateChargeBANCR = false;
            Message = "<font color=red><b>Unable to authorise BAN: " +
              (ret.equals("-99")?"a bill job is currently running for this customer."
              :("return code " + ret)) + "</b></font>";

          }
          else
          {
            updateChargeBANCR = true;
            banIdentifier = ret;
            Message = "<font color=blue><b>BAN Id:-" + banIdentifier +
              " authorised and charge updated";
          }
        }
        ScreenData.clear();
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
      return updateChargeBANCR;
    }

  }
/*****************************************************************************************/
  public boolean updateChargeBAN()
  {
    boolean updateChargeBAN = false;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        if (Charge_Category.equals("01"))
        {
          if ((storedToDate != null) && (Charge_Valid_To_Date == null))
          {
            trialDelete = DBA.trialDelete(Charge_Id, Charge_Category)?"Y":"N";
          }
          SQL = "{call eban.dbo." + (Mode.equals("Delete")?"Delete":"Update") +
            "_Charge_BAN " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,banStatus);
          cstmt.setString(2,BAN_Summary);
          cstmt.setString(3,banCreatedBy);
          cstmt.setDate(4,Required_BAN_Effective_Dateh);
          cstmt.setString(5,Circuit_Reference);
          cstmt.setDouble(6,Gross_Amount);
          cstmt.setString(7,chargeDescriptionCode.equals("")?null:chargeDescriptionCode);
          cstmt.setDate(8,Charge_Valid_From_Date);
          cstmt.setDate(9,Charge_Valid_To_Date);
          cstmt.setString(10,Charge_Description);
          cstmt.setInt(11,splits);
          cstmt.setString(12,siteReference);
          cstmt.setString(13,siteReference2);
          cstmt.setString(14,siteReference3);
          cstmt.setDouble(15,split);
          cstmt.setDouble(16,split2);
          cstmt.setDouble(17,split3);
          cstmt.setString(18,Charge_Frequency);
          cstmt.setString(19,Charge_Currency);
          cstmt.setString(20,revenueType);
          cstmt.setString(21,"Z");
          cstmt.setString(22,"Z");
          cstmt.setString(23,"Z");
          cstmt.setInt(24,unitQuantity);
          cstmt.setString(25,Global_Customer_Id);
          cstmt.setString(26,Mode);
          cstmt.setString(27,Charge_Entity);
          cstmt.setInt(28,Charge_Id);
          cstmt.setString(29,trialDelete);
          cstmt.setString(30,c2RefNo);
          cstmt.setString(31,c3RefNo);
        }
        else
        {
          trialDelete = DBA.trialDelete(Charge_Id, Charge_Category)?"Y":"N";
          SQL = "{call eban.dbo." + (Mode.equals("Delete")?"Delete":"Update") +
            "_Single_Charge_BAN " +
            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,banStatus);
          cstmt.setString(2,BAN_Summary);
          cstmt.setString(3,banCreatedBy);
          cstmt.setDate(4,Required_BAN_Effective_Dateh);
          cstmt.setString(5,Circuit_Reference);
          cstmt.setDouble(6,Gross_Amount);
          cstmt.setString(7,chargeDescriptionCode.equals("")?null:chargeDescriptionCode);
          cstmt.setDate(8,Charge_Valid_From_Date);
          cstmt.setString(9,Charge_Description);
          cstmt.setInt(10,splits);
          cstmt.setString(11,siteReference);
          cstmt.setString(12,siteReference2);
          cstmt.setString(13,siteReference3);
          cstmt.setDouble(14,split);
          cstmt.setDouble(15,split2);
          cstmt.setDouble(16,split3);
          cstmt.setString(17,Charge_Currency);
          cstmt.setString(18,revenueType);
          cstmt.setString(19,revenueDescription);
          cstmt.setString(20,revenueNetOrFull);
          cstmt.setString(21,revenueRootCause);
          cstmt.setString(22,Global_Customer_Id);
          cstmt.setString(23,Mode);
          cstmt.setString(24,Charge_Entity);
          cstmt.setInt(25,Charge_Id);
          cstmt.setString(26,Charge_Category);
          cstmt.setString(27,trialDelete);
          cstmt.setString(28,c2RefNo);
          cstmt.setString(29,c3RefNo);
          cstmt.setDate(30,Charge_Period_Start_Date);
          cstmt.setDate(31,Charge_Period_End_Date);
        }

        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          String ret = RS.getString(1);
          if (ret.startsWith("-"))
          {
            updateChargeBAN = false;
            Message = "<font color=red><b>Unable to authorise BAN: " +
              (ret.equals("-99")?"a bill job is currently running for this customer."
              :("return code " + ret)) + "</b></font>";

          }
          else
          {
            updateChargeBAN = true;
            banIdentifier = ret;
            Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" authorised " +
              "and " + (Charge_Category.equals("03")?"credit":"charge") +
              (Mode.equals("Delete")?" deleted":" updated");
          }
        }
        ScreenData.clear();
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
      return updateChargeBAN;
    }

  }
/**********************************************************************************************/
  public boolean updateChargeBanold()
  {
    boolean updateChargeBan = false;
    int rowcount=0;

    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..update_OSS_Charge_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	//if we are only doing a partial update (e.g. Authorise)
	//we need to make sure the values of composite fields
	//i.e. fields made up from id and description are set
	//to the id only
        cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
	cstmt.setString(2,BAN_Summary);
	cstmt.setDate(3,Required_BAN_Effective_Dateh);
	cstmt.setString(4,BAN_Reason);
	cstmt.setString(5,userId);
	cstmt.setString(6,SU.before(Charge_Category));
	cstmt.setDouble(7,Gross_Amount);
	cstmt.setString(8,Charge_Description);
	cstmt.setString(9,Currency_Desc);
	cstmt.setString(10,Charge_Entity);
	cstmt.setString(11,Standard_Bill_Indicator);
	cstmt.setString(12,OSS_Charge_Type);
	cstmt.setString(13,banStatus);
	cstmt.setString(14,rejectReason);
	cstmt.setString(15,CRDB);
	cstmt.setString(16,Charge_Frequency);
	cstmt.setDate(17,Charge_Valid_From_Date);
	cstmt.setDate(18,Charge_Valid_To_Date);
	cstmt.setString(19,VAT_Indicator);
	cstmt.setDouble(20,VAT_Amount);
	cstmt.setString(21,Revenue_Reason_Code);

        cstmt.execute();
        String tempId = banIdentifier;
        Reset();
        updateChargeBan = true;
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
      return updateChargeBan;
    }
  }
/*********************************************************************/
public boolean ImplementChargeBAN()
{

  SQL = "exec eban..Implement_OSS_Charge_BAN '"+ banIdentifier+ "','" +
    userId + "'";

  if(DBA.NoResult(SQL,P5))
  {
    return true;
  }
  else
  {
    Message=DBA.getMessage();
    return false;
  }
}
/****************************************************************************************/
public boolean getChargeBan()
{
    boolean getChargeBan = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Get_Charge_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    banStatus=RS.getString(1);
	    BAN_Summary=RS.getString(2);
	    banCreatedBy=RS.getString(3);
	    Required_BAN_Effective_Dateh=RS.getDate(4);
	    Circuit_Reference=RS.getString(5);
            Gross_Amount=RS.getDouble(6);
            chargeDescriptionCode=RS.getString(7);
	    Charge_Valid_From_Date=RS.getDate(8);
	    Charge_Valid_To_Date=RS.getDate(9);
            Charge_Description=RS.getString(10);
            splits=RS.getInt(11);
            Charge_Frequency=RS.getString(12);
            Charge_Currency=RS.getString(13);
            revenueType=RS.getString(14);
            unitQuantity=RS.getInt(15);
	    Global_Customer_Id=RS.getString(16);
	    Mode=RS.getString(17);
	    Charge_Entity=RS.getString(18);
            chargeId=RS.getInt(19);
	  }
          getChargeBan = true;
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
      if (getChargeBan == true)
      {
        getChargeBan = getChargeSiteBan();
      }
      return getChargeBan;
    }
}
/****************************************************************************************/
public boolean getChargeSiteBan()
{
    boolean getChargeSiteBan = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..Get_Charge_Site_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
            siteReference=RS.getString(1);
            split=RS.getDouble(2);
	  }
	  if (RS.next())
	  {
            siteReference2=RS.getString(1);
            split2=RS.getDouble(2);
	  }
          else
          {
            siteReference2="";
            split2=0;
          }
	  if (RS.next())
	  {
            siteReference3=RS.getString(1);
            split3=RS.getDouble(2);
	  }
          else
          {
            siteReference3="";
            split3=0;
          }
          getChargeSiteBan = true;

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
      return getChargeSiteBan;
    }
}
/****************************************************************************************/
public boolean getCharge()
{
    /*Random generator = new Random();
    int msPause = generator.nextInt(900) + 100;
    try
    {
      Thread.currentThread().sleep((long)msPause);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }*/

    boolean getCharge = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call gcd..Get_" + (Charge_Category.equals("01")?"":"Single_") +
         "Charge (?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setInt(1,Charge_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
	  if (RS.next())
	  {
            if (Charge_Category.equals("01"))
            {
              siteReference=RS.getString(1);
              split=RS.getDouble(2);
              Gross_Amount=RS.getDouble(3);
              unitQuantity=RS.getInt(4);
              splits=RS.getInt(5);
              Charge_Frequency=RS.getString(6);
              Charge_Description=RS.getString(7);
              Charge_Valid_From_Date=RS.getDate(8);
              Charge_Valid_To_Date=RS.getDate(9);
              storedToDate = Charge_Valid_To_Date;
              chargeDescriptionCode=SU.isNull(RS.getString(10), "");
              Charge_Entity=RS.getString(11);
              revenueType=RS.getString(12);
              Charge_Currency=RS.getString(13);
              Circuit_Reference=RS.getString(14);
	      c2RefNo=SU.isNull(RS.getString(15),"");
	      c3RefNo=SU.isNull(RS.getString(16),"");
              chargeIdCheck=RS.getLong(17);
            }
            else
            {
              siteReference=RS.getString(1);
              split=RS.getDouble(2);
              Gross_Amount=RS.getDouble(3);
              splits=RS.getInt(4);
              Charge_Description=RS.getString(5);
              Charge_Valid_From_Date=RS.getDate(6);
              chargeDescriptionCode=SU.isNull(RS.getString(7), "");
              Charge_Entity=RS.getString(8);
              revenueType=RS.getString(9);
              revenueDescription=RS.getString(10);
              revenueRootCause=RS.getString(11);
              revenueNetOrFull=RS.getString(12);
              Charge_Currency=RS.getString(13);
              Circuit_Reference=RS.getString(14);
              archived=RS.getInt(15)==1;
	      c2RefNo=SU.isNull(RS.getString(16),"");
	      c3RefNo=SU.isNull(RS.getString(17),"");
              chargeIdCheck=RS.getLong(18);
              Charge_Period_Start_Date=RS.getDate(19);
              Charge_Period_End_Date=RS.getDate(20);
            }
	  }
	  if (RS.next())
	  {
	    siteReference2=RS.getString(1);
            split2=RS.getDouble(2);
	  }
	  if (RS.next())
	  {
	    siteReference3=RS.getString(1);
            split3=RS.getDouble(2);
	  }
          setDates();
          setCDCsRequired();
          if (Mode.equals("Cease / "))
          {
            oldGrossAmount = Gross_Amount;
            Gross_Amount = 0;
          }
          getCharge = true;
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
      return getCharge;
    }
}
/*******************************************************************************/
  public String getChargeList()
  {
	int counter=0;
	String RadioButton;
	int rowcount;
	BigInteger rows = new BigInteger("0");
	String gridClass;
        StringBuffer grid=new StringBuffer("");
        Message="";
	String chargeDescription = Charge_Description_for_List.equalsIgnoreCase("All")
          ?"All":("%"+Charge_Description_for_List+"%");

	RadioButton="width=45 align=center><img src=\"../nr/cw/newimages/icon_edit.gif\" width=20 onClick=\"sendSelected('";
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call gcd..List_Charges" + (Mode.equals("Cease / ")?"_CR":"") +
          " (?,?,?,?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,action);
	cstmt.setString(2,OrderBy);
	cstmt.setString(3,GSR_for_List);
	cstmt.setString(4,Month_for_List);
	cstmt.setString(5,Charge_Category);
	cstmt.setString(6,chargeDescription);
	cstmt.setString(7,Global_Customer_Id_for_List);
	cstmt.setString(8,c2RefNo_for_List);

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
	    for(counter=1;counter<columns+1;counter++)
	    {
	      grid.append("<td class=");
	      grid.append(gridClass);
              StringBuffer value = new StringBuffer(RS.getString(counter));
              if (value.toString().endsWith("NOWRAP>"))
                value.append("&nbsp;");
	      grid.append(value.toString());
	      grid.append("</td>");
	    }
	    //Add the extra generated column for the radio button
	    grid.append("<td width=45 class=");
	    grid.append(gridClass);
	    grid.append(RadioButton);
	    grid.append(RS.getString(counter));
	    grid.append("')\"></td></tr>");
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
/************************************************************************************************/
  public int getFromChargeValidDateDays()
  {
    return FCDays;
  }
/************************************************************************************************/
  public void setFromChargeValidDateDays(String value)
  {
    FCDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getFromChargeValidDateMonths()
  {
    return FCMonths;
  }
/************************************************************************************************/
  public void setFromChargeValidDateMonths(String value)
  {
    FCMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getFromChargeValidDateYears()
  {
    return FCYears;
  }
/************************************************************************************************/
  public void setFromChargeValidDateYears(String value)
  {
    FCYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateDays()
  {
    return TCDays;
  }
/************************************************************************************************/
  public void setToChargeValidDateDays(String value)
  {
    TCDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateMonths()
  {
    return TCMonths;
  }
/************************************************************************************************/
  public void setToChargeValidDateMonths(String value)
  {
    TCMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getToChargeValidDateYears()
  {
    return TCYears;
  }
/************************************************************************************************/
  public void setToChargeValidDateYears(String value)
  {
    TCYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getPriceChangeDateDays()
  {
    return PCDays;
  }
/************************************************************************************************/
  public void setPriceChangeDateDays(String value)
  {
    PCDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getPriceChangeDateMonths()
  {
    return PCMonths;
  }
/************************************************************************************************/
  public void setPriceChangeDateMonths(String value)
  {
    PCMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getPriceChangeDateYears()
  {
    return PCYears;
  }
/************************************************************************************************/
  public void setPriceChangeDateYears(String value)
  {
    PCYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodStartDateDays()
  {
    return PSDays;
  }
/************************************************************************************************/
  public void setChargePeriodStartDateDays(String value)
  {
    PSDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodStartDateMonths()
  {
    return PSMonths;
  }
/************************************************************************************************/
  public void setChargePeriodStartDateMonths(String value)
  {
    PSMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodStartDateYears()
  {
    return PSYears;
  }
/************************************************************************************************/
  public void setChargePeriodStartDateYears(String value)
  {
    PSYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodEndDateDays()
  {
    return PEDays;
  }
/************************************************************************************************/
  public void setChargePeriodEndDateDays(String value)
  {
    PEDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodEndDateMonths()
  {
    return PEMonths;
  }
/************************************************************************************************/
  public void setChargePeriodEndDateMonths(String value)
  {
    PEMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getChargePeriodEndDateYears()
  {
    return PEYears;
  }
/************************************************************************************************/
  public void setChargePeriodEndDateYears(String value)
  {
    PEYears = value.equals("")?0:Integer.parseInt(value);
  }
/***********************************************************************************/
  public String getCDCListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, int listBoxSize, String listBoxStyle,
    boolean blankLine)
  {
    String html = "";
    if (cdcsRequired)
    {
      String mode = Mode.equals("")?"onChange=\"checkCDC()\"":Mode;
      html = DBA.getListBox(Table, mode, FirstEntry, QualifierValue,
        listBoxSize, listBoxStyle, blankLine);
    }
    else
    {
      html = "<SELECT " + listBoxStyle +" NAME='notapplicable' size=" +
        listBoxSize + " DISABLED><OPTION VALUE='n/a' selected>" +
	"Not Applicable</OPTION></SELECT>" +
        "<input type=\"hidden\" name=\"" + Table + "\" value=\"\">";
    }
    return html;
  }
/****************************************************************************************/
  public void setChargeBilled()
  {
    chargeBilled = chargeBilled(Charge_Id, Charge_Category, false);
    if (chargeBilled)
    {
      if ((Charge_Category.equals("01")) && (Mode.equals("Delete")))
        archived = true;
    }
    else
    {
      chargeTrialled = chargeBilled(Charge_Id, Charge_Category, true);
      if (chargeTrialled)
      {
        Message = "<font color=red>Warning - this charge exists on a trial bill. " +
          "You must rerun the trial after updating the charge.";
      }
    }
  }
/****************************************************************************************/
  public void setToDateNullable()
  {
    if (Charge_Valid_To_Date == null)
      toDateNullable = true;
    else
      toDateNullable = false;//toDateNullable(Charge_Id, Global_Customer_Id);
  }
/****************************************************************************************/
  public boolean getToDateNullable()
  {
    return toDateNullable;
  }
/****************************************************************************************/
  public String getSelectMode()
  {
    if (archived)
      return "DISABLED";
    else
      return super.getSelectMode();
  }
/****************************************************************************************/
  public String getInputMode()
  {
    if (archived)
      return "READONLY";
    else
      return super.getInputMode();
  }
/****************************************************************************************/
  public String[] getCircuitEnds()
  {
    if (cdcsRequired)
    {
      return DBA.getCircuitEnds(Circuit_Reference);
    }
    else
    {
      return new String[] {"N/A", "N/A"};
    }
  }
/****************************************************************************************/
  public boolean isChargeArchived()
  {
    return archived;
  }
/****************************************************************************************/
  public boolean getCDCsRequired()
  {
    return cdcsRequired;
  }
/****************************************************************************************/
  public String getCDCAuthority()
  {
    return cdcAuthority;
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
    cal.setTime(Charge_Valid_From_Date==null?(new java.util.Date())
      :Charge_Valid_From_Date);
    FCDays=cal.get(cal.DATE);
    FCMonths=cal.get(cal.MONTH)+1;
    FCYears=cal.get(cal.YEAR);
    if (Charge_Valid_To_Date!=null)
    {
      cal.setTime(Charge_Valid_To_Date);
      TCDays=cal.get(cal.DATE);
      TCMonths=cal.get(cal.MONTH)+1;
      TCYears=cal.get(cal.YEAR);
    }
    if (Charge_Period_Start_Date!=null)
    {
      cal.setTime(Charge_Period_Start_Date);
      PSDays=cal.get(cal.DATE);
      PSMonths=cal.get(cal.MONTH)+1;
      PSYears=cal.get(cal.YEAR);
    }
    if (Charge_Period_End_Date!=null)
    {
      cal.setTime(Charge_Period_End_Date);
      PEDays=cal.get(cal.DATE);
      PEMonths=cal.get(cal.MONTH)+1;
      PEYears=cal.get(cal.YEAR);
    }
  }
/********************************************************************/
  public void setReprovide(boolean value)
  {
    reprovide = value;
    if (reprovide)
    {
    }
    else
    {
    }
  }
/************************************************************************************************/
  private boolean checkBillingStartDate()
  {
    boolean bsdOK = false;
    try
    {
      SQL = "select billing_start_date, convert(varchar, billing_start_date, 103), " +
        "to_end_code " +
        "from gcd..global_customer_billing (nolock) " +
        "where service_reference = '" + Circuit_Reference + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          java.sql.Date bsd = RS.getDate(1);
          circuitBillStartDate = RS.getString(2);
          toEnd = RS.getString(3);
          bsdOK = !bsd.after(Charge_Valid_From_Date);
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return bsdOK;
    }
  }
/************************************************************************************************/
  private boolean checkToEnd()
  {
    boolean toEndOK = false;
    try
    {
      SQL = "select vj.to_end_required " +
        "from givn_ref..Vertex_Jurisdiction vj (nolock) " +
        "where vj.Charge_Description_Code = '" + chargeDescriptionCode + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          boolean toEndRequired = RS.getString(1).equals("Y");
          if (toEndRequired && SU.hasNoValue(toEnd))
          {
            toEndOK = false;
          }
          else
          {
            toEndOK = true;
          }
        }
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return toEndOK;
    }
  }
/*****************************************************************************/
  private boolean chargeBilled(int chargeId, String chargeTypeCode, boolean trial)
  {
    boolean cB = false;
    try
    {
      SQL = "SELECT bpsd_orig " +
        "FROM gcd..Monthly_Billing" + (trial?"":"_Archive") + " (nolock) " +
        "WHERE Charge_Type_Code = '" + chargeTypeCode + "' " +
        "AND Charge_Id = " + Integer.toString(chargeId);

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        cB = RS.next();
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      DBA.Close();
      return cB;
    }
  }
/*****************************************************************************/
  private boolean toDateChangeable()
  {
    boolean tDN = false;
    if (!chargeBilled(chargeId, "01", false)) //&&
      //(!chargeBilled(chargeId, chargeTypeCode, true)))
    {
      tDN = true;
    }
    else
    {
      SQL = "SELECT MAX(bpsd_orig) " +
        "FROM gcd..Monthly_Billing_Archive (nolock) " +
        "WHERE Charge_Id = " + Integer.toString(chargeId) + " " +
        "AND Global_Customer_Id = '" + Global_Customer_Id + "' " +
        "AND not Service_Reference = 'dummy' "
        ;

      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        try
        {
          RS = DBA.getResultsSet();
          if (RS.next())
          {
            if (RS.getDate(1) != null)
            {
              tDN = true;
            }
          }
        }
        catch(java.sql.SQLException se)
        {
          Message = se.getMessage();
        }
        finally
        {
          DBA.Close();
        }
      }
    }
    return tDN;
  }
/********************************************************************/
  //for debugging
  public static void main (String[] args)
  {
    String Currency;
    OSSChargeBANBean chBAN = new OSSChargeBANBean();
    chBAN.setCircuit_Reference("345345");
    chBAN.setCharge_Entity("C&W");
    chBAN.setCharge_Entity("Carrier");
    Currency=chBAN.getCurrency_Desc();
    //BAN.getBanList();
 }
}
