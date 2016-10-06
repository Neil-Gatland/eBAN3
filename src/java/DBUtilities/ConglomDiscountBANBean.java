package DBUtilities;

import java.sql.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;


public class ConglomDiscountBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long conglomCustomerId;
    private long discountId;
    private String billedProduct;
    private String discountType;
    private double discountPct;
    private double appliedPct;
    private String leadAccount;
    private String checkDigit;
    private java.sql.Date effectiveFromh;
    private java.sql.Date effectiveToh;
    private int efDay;
    private int efMonth;
    private int efYear;
    private int etDay;
    private int etMonth;
    private int etYear;
    private String feedSource;
    private String sourceConglomId;
    private String sourceSystemId;
    private String discountTypeDesc;
    private String billedProductDesc;

  public ConglomDiscountBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("conglomDiscountType");
    mandatory.addElement("discountPct");
    mandatory.addElement("conglomBilledProduct3");
    mandatory.addElement("appliedPct");
    mandatory.addElement("effectiveFromh");
    errored.clear();
    errored.addElement("");
    discountId = 0;
    effectiveFromh = new java.sql.Date(new java.util.Date().getTime());
    effectiveToh = null;
    billedProduct = "";
    discountType = "";
    discountPct = 0;
    appliedPct = 100;
    leadAccount = "";
    checkDigit = "";
    efDay = 0;
    efMonth = 0;
    efYear = 0;
    etDay = 0;
    etMonth = 0;
    etYear = 0;
    feedSource = null;
    sourceConglomId = null;
    sourceSystemId = null;
    discountTypeDesc = "";
    billedProductDesc = "";
  }

  public void Reset()
  {
    super.Reset();
    discountId = 0;
    effectiveFromh = new java.sql.Date(new java.util.Date().getTime());
    effectiveToh = null;
    billedProduct = "";
    discountType = "";
    discountPct = 0;
    appliedPct = 100;
    leadAccount = "";
    checkDigit = "";
    efDay = 0;
    efMonth = 0;
    efYear = 0;
    etDay = 0;
    etMonth = 0;
    etYear = 0;
    errored.clear();
    feedSource = null;
    sourceConglomId = null;
    sourceSystemId = null;
    discountTypeDesc = "";
    billedProductDesc = "";
  }
/*set Methods*/
  public void setDiscountId(long value)
  {
    discountId = value;
  }
  public void setConglomCustomerId(long value)
  {
    conglomCustomerId = value;
  }
  public void setBilledProduct(String value)
  {
    ScreenData.put("conglomBilledProduct3",value);
    if (!billedProduct.equals(SU.isNull(value,"")))
    {
      billedProduct = SU.isNull(value,"");
      String[] details = DBA.getConglomSourceDetails(conglomCustomerId,
        billedProduct);
      feedSource = details[0];
      sourceConglomId = details[1];
      sourceSystemId = details[2];
    }
    else
    {
      billedProduct = SU.isNull(value,"");
    }
  }
  public void setDiscountType(String value)
  {
    ScreenData.put("conglomDiscountType",value);
    discountType = SU.isNull(value,"");
  }
  public void setDiscountTypeDesc(String value)
  {
    discountTypeDesc = SU.isNull(value,"");
  }
  public void setBilledProductDesc(String value)
  {
    billedProductDesc = SU.isNull(value,"");
  }
  public void setLeadAccount(String value)
  {
    ScreenData.put("leadAccount",value);
    leadAccount = SU.isNull(value,"");
  }
  public void setCheckDigit(String value)
  {
    ScreenData.put("checkDigit",value);
    checkDigit = value;
  }
  public void setDiscountPct(String value)
  {
    try
    {
      setDiscountPct(Double.parseDouble(SU.hasNoValue(value, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("discountPct",null);
    }
  }
  public void setDiscountPct(double value)
  {
    ScreenData.put("discountPct",String.valueOf(value));
    discountPct = value;
  }
  public void setAppliedPct(String value)
  {
    try
    {
      setAppliedPct(Double.parseDouble(SU.hasNoValue(value, "0.00")));
    }
    catch (java.lang.NumberFormatException NE)
    {
	    ScreenData.put("appliedPct",null);
    }
  }
  public void setAppliedPct(double value)
  {
    ScreenData.put("appliedPct",String.valueOf(value));
    appliedPct = value;
  }
  public void setEFDay(String value)
  {
    try
    {
      setEFDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("efDay", "0");
    }
  }
  public void setEFDay(int value)
  {
    ScreenData.put("efDay",String.valueOf(value));
    efDay = value;
  }
  public void setEFMonth(String value)
  {
    try
    {
      setEFMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("efMonth","0");
    }
  }
  public void setEFMonth(int value)
  {
    ScreenData.put("efMonth",String.valueOf(value));
    efMonth = value;
  }
  public void setEFYear(String value)
  {
    try
    {
      setEFYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("efYear","0");
    }
  }
  public void setEFYear(int value)
  {
    ScreenData.put("efYear",String.valueOf(value));
    efYear = value;
  }
  public void setEffectiveFrom(String value)
  {
    ScreenData.put("effectiveFromh",value);
    effectiveFromh = SU.toJDBCDate(value);
  }
  public void setETDay(String value)
  {
    try
    {
      setETDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("etDay", "0");
    }
  }
  public void setETDay(int value)
  {
    ScreenData.put("etDay",String.valueOf(value));
    etDay = value;
  }
  public void setETMonth(String value)
  {
    try
    {
      setETMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("etMonth","0");
    }
  }
  public void setETMonth(int value)
  {
    ScreenData.put("etMonth",String.valueOf(value));
    etMonth = value;
  }
  public void setETYear(String value)
  {
    try
    {
      setETYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("etYear","0");
    }
  }
  public void setETYear(int value)
  {
    ScreenData.put("etYear",String.valueOf(value));
    etYear = value;
  }
  public void setEffectiveTo(String value)
  {
    ScreenData.put("effectiveToh",value);
    effectiveToh = SU.toJDBCDate(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(effectiveFromh==null?(new java.util.Date())
      :effectiveFromh);
    efDay=cal.get(cal.DATE);
    efMonth=cal.get(cal.MONTH)+1;
    efYear=cal.get(cal.YEAR);
    if (effectiveToh == null)
    {
      etDay=0;
      etMonth=0;
      etYear=0;
    }
    else
    {
      cal.setTime(effectiveToh);
      etDay=cal.get(cal.DATE);
      etMonth=cal.get(cal.MONTH)+1;
      etYear=cal.get(cal.YEAR);
    }
  }
  /*  public void setBilledProduct(String value)
  {
    ScreenData.put("conglomBilledProduct3",value);
    billedProduct = SU.isNull(value,"");
  }
  public void setDiscountType(String value)
  {
    ScreenData.put("discountType",value);
    discountType = SU.isNull(value,"");
  }
  public void setLeadAccount(String value)
  {
    ScreenData.put("leadAccount",value);
    leadAccount = SU.isNull(value,"");
  }
  public void setCheckDigit(String value)
  {
    ScreenData.put("checkDigit",value);
    checkDigit = value;
  }
  public void setDiscountPct(String value)
****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("conglomBilledProduct3"))
    {
      setBilledProduct(value);
    }
    else if (name.equals("conglomDiscountType"))
    {
      setDiscountType(value);
    }
    else if (name.equals("conglomBilledProduct3Desc"))
    {
      setBilledProductDesc(value);
    }
    else if (name.equals("conglomDiscountTypeDesc"))
    {
      setDiscountTypeDesc(value);
    }
    else if (name.equals("leadAccount"))
    {
      setLeadAccount(value);
    }
    else if (name.equals("checkDigit"))
    {
      setCheckDigit(value);
    }
    else if (name.equals("discountPct"))
    {
      setDiscountPct(value);
    }
    else if (name.equals("appliedPct"))
    {
      setAppliedPct(value);
    }
    else if (name.equals("effectiveFromDay"))
    {
      setEFDay(value);
    }
    else if (name.equals("effectiveFromDay"))
    {
      setEFDay(value);
    }
    else if (name.equals("effectiveFromMonth"))
    {
      setEFMonth(value);
    }
    else if (name.equals("effectiveFromYear"))
    {
      setEFYear(value);
    }
    else if (name.equals("effectiveFromh"))
    {
      setEffectiveFrom(value);
    }
    else if (name.equals("effectiveToDay"))
    {
      setETDay(value);
    }
    else if (name.equals("effectiveToMonth"))
    {
      setETMonth(value);
    }
    else if (name.equals("effectiveToYear"))
    {
      setETYear(value);
    }
    else if (name.equals("effectiveToh"))
    {
      setEffectiveTo(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public long getDiscountId()
  {
    return discountId;
  }
  public long getConglomCustomerId()
  {
    return conglomCustomerId;
  }
  public double getDiscountPct()
  {
    return discountPct;
  }
  public double getAppliedPct()
  {
    return appliedPct;
  }
  public String getBilledProduct()
  {
    return billedProduct;
  }
  public String getDiscountType()
  {
    return discountType;
  }
  public String getBilledProductDesc()
  {
    return billedProductDesc;
  }
  public String getDiscountTypeDesc()
  {
    return discountTypeDesc;
  }
  public String getLeadAccount()
  {
    return leadAccount;
  }
  public String getCheckDigit()
  {
    return checkDigit;
  }
  public int getEFDay()
  {
    return efDay;
  }
  public int getEFMonth()
  {
    return efMonth;
  }
  public int getEFYear()
  {
    return efYear;
  }
  public String getEffectiveFrom()
  {
    if (effectiveFromh != null)
    {
      return SU.reformatDate(effectiveFromh.toString());
    }
    else
    {
      return null;
    }
  }
  public int getETDay()
  {
    return etDay;
  }
  public int getETMonth()
  {
    return etMonth;
  }
  public int getETYear()
  {
    return etYear;
  }
  public String getEffectiveTo()
  {
    if (effectiveToh != null)
    {
      return SU.reformatDate(effectiveToh.toString());
    }
    else
    {
      return null;
    }
  }
  public String getSelectMode()
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      (Mode.equals("Amend")))
    {
      return "DISABLED";
    }
    else
    {
      return super.getSelectMode();
    }
  }
  public String getSelectMode(String listName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")) ||
      (Mode.equals("Amend")))
    {
      return "DISABLED";
    }
    else
    {
      return "onChange=\"saveText('" + listName + "')\"";
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
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
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
/*select Discount_Item_Code,
Eff_From_Date,
Eff_To_Date,
Whole_Value_Percent,
Discount_Rate,
Lead_Account_Id,
Lead_Acct_Check_Digit,
Conglom_Cust_Id
******************************************************************************/
 public boolean getDiscount()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call Conglomerate..Get_Discount_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,discountId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          discountType = RS.getString(1);
          effectiveFromh = RS.getDate(2);
          effectiveToh = RS.getDate(3);
          appliedPct = RS.getDouble(4);
          discountPct = RS.getDouble(5);
          leadAccount = SU.isNull(RS.getString(6), "");
          checkDigit = SU.isNull(RS.getString(7), "");
          setBilledProduct(RS.getString(8));
          discountTypeDesc = RS.getString(10);
          billedProductDesc = RS.getString(11);
          setDates();
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
public int updateDiscount()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Update_Discount_eBAN(?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,discountType);
      cstmt.setDate(4,effectiveToh);
      cstmt.setDouble(5,appliedPct);
      cstmt.setDouble(6,discountPct);
      cstmt.setString(7,leadAccount.equals("")?null:leadAccount);
      cstmt.setString(8,checkDigit.equals("")?null:checkDigit);
      cstmt.setString(9,userId);
      cstmt.setLong(10,discountId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update discount</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Discount updated successfully" +
              "</b></font>";
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
    return ret;
  }
}
/*******************************************************************************/
public int createDiscount()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Add_Discount_eBAN(?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,discountType);
      cstmt.setDate(4,effectiveFromh);
      cstmt.setDate(5,effectiveToh);
      cstmt.setDouble(6,appliedPct);
      cstmt.setDouble(7,discountPct);
      cstmt.setString(8,leadAccount.equals("")?null:leadAccount);
      cstmt.setString(9,checkDigit.equals("")?null:checkDigit);
      cstmt.setString(10,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new discount</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New discount created successfully" +
              "</b></font>";
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
    return ret;
  }
}
/*******************************************************************************/
public int deleteDiscount()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Delete_Discount_eBAN(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,discountType);
      cstmt.setLong(4,discountId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete discount</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Discount deleted successfully" +
              "</b></font>";
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
    return ret;
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
      if ((effectiveToh != null) && (effectiveToh.before(effectiveFromh)))
      {
        Message = "<font color=red><b>Effective To Date cannot be before Effective From Date";
        setErrored("effectiveFromh");
        setErrored("effectiveToh");
        return false;
      }
      else if ((discountPct <= 0) || (discountPct > 100))
      {
        Message = "<font color=red><b>Discount Percentage must be a numeric " +
          "value greater than 0 and less than or equal to 100</b></font>";
        setErrored("discountPct");
        return false;
      }
      else if ((appliedPct <= 0) || (appliedPct > 100))
      {
        Message = "<font color=red><b>Percentage Applied To must be a numeric " +
          "value greater than 0 and less than or equal to 100</b></font>";
        setErrored("appliedPct");
        return false;
      }
      return true;
    }
  }
/*******************************************************************************/
  public boolean validateAccountToExclude(String accountNo)
  {
    boolean valid = true;
    if ((feedSource.equalsIgnoreCase("GOLDFISH")) &&
      (!DBA.goldfishAccountExists(accountNo, sourceSystemId, sourceConglomId)))
    {
      Message = "<font color=red><b>Goldfish account does not exist on " +
        "T_Accounts for this Customer/Product</b></font>";
      valid = false;
    }
    else if (DBA.conglomAccountExcluded(conglomCustomerId, billedProduct,
      discountType, accountNo))
    {
      Message = "<font color=red><b>This account is already excluded " +
        "for this Customer/Product/Discount</b></font>";
      valid = false;
    }
    return valid;
  }
/*******************************************************************************/
public int removeExcludedAccount(long exclId)
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Remove_Excluded_Disc_Account_eBAN(?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,exclId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to remove excluded account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Excluded account removed" +
              "</b></font>";
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
    return ret;
  }
}
/*******************************************************************************/
public int excludeAccount(String accountNo)
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call Conglomerate..Exclude_Disc_Account_eBAN(?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,conglomCustomerId);
      cstmt.setString(2,billedProduct);
      cstmt.setString(3,discountType);
      cstmt.setString(4,accountNo);
      cstmt.setString(5,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to exclude account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Account excluded" +
              "</b></font>";
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
    return ret;
  }
}
/*******************************************************************************/
  public String getConglomDiscountExclusionList()
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

        SQL = "{call conglomerate..Get_Discount_Exclusion_List_eBAN(?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustomerId);
        cstmt.setString(2,billedProduct);
        cstmt.setString(3,discountType);

	columns=1;

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
              grid.append(RS.getString(counter));
              grid.append("</td>");
            }
            //Add the extra generated column for the buttons
            long thisId = RS.getLong(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<img src="+sharedPath+"/nr/cw/newimages/buttonD.gif " +
              "align=bottom border=0 width=24 height=22 onClick=\"sendSelected('" +
              thisId + "','Delete')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          /*if (rowcount == 0)
          {
            Message="<font color=blue>There are no products for this customer</font>";
          }*/
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
        return grid.toString();
      }
      else
      {
        return Message;
      }
    }
  }
}

