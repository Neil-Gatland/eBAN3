package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;

public class RatingSchemeBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private String ratingSchemeId;
    private String productCode;
    private String signName;
    private String masterAccountNumber;
    private String accountNumber;
    private String ratingType;
    private String ratingDuration;
    private long originalRangeStart;
    private long rangeStart;
    private long rangeEnd;
    private BigDecimal dayRate;
    private BigDecimal eveningRate;
    private BigDecimal weekendRate;
    private String fromCallMonth;
    private String newFromCallMonth;
    private String toCallMonth;
    private String productGroup;
    private long providerId;
    private static final DecimalFormat dp10 = new DecimalFormat("#.##########");

  public RatingSchemeBean ()
  {
    mandatory.clear();
    mandatory.addElement("fromCallMonth");
    mandatory.addElement("productGroup");
    mandatory.addElement("nameOrNumber");
    mandatory.addElement("productCode");
    mandatory.addElement("ratingType");
    mandatory.addElement("ratingDuration");
    mandatory.addElement("rangeStart");
    mandatory.addElement("dayRate");
    mandatory.addElement("eveningRate");
    mandatory.addElement("weekendRate");
    errored.addElement("");
    ratingSchemeId = "";
    productCode = "";
    signName = "";
    masterAccountNumber = "";
    accountNumber = "";
    ratingType = "";
    ratingDuration = "";
    rangeStart = 1;
    rangeEnd = 0;
    dayRate = new BigDecimal(0);
    eveningRate = new BigDecimal(0);
    weekendRate = new BigDecimal(0);
    fromCallMonth = "";
    toCallMonth = "";
    newFromCallMonth = "";
    productGroup = "";
    providerId = -1;
  }

  public void Reset()
  {
    super.Reset();
    errored.clear();
    ratingSchemeId = "";
    productCode = "";
    signName = "";
    masterAccountNumber = "";
    accountNumber = "";
    ratingType = "";
    ratingDuration = "";
    rangeStart = 1;
    rangeEnd = 0;
    dayRate = new BigDecimal(0);
    eveningRate = new BigDecimal(0);
    weekendRate = new BigDecimal(0);
    fromCallMonth = "";
    toCallMonth = "";
    newFromCallMonth = "";
    productGroup = "";
    providerId = -1;
  }
/*set Methods*/
  public void setRatingSchemeId(String value)
  {
    ratingSchemeId = SU.isNull(value,"");
  }
  public void setFromCallMonth(String value)
  {
    ScreenData.put("fromCallMonth",value);
    fromCallMonth = SU.isNull(value,"");
  }
  public void setNewFromCallMonth(String value)
  {
    newFromCallMonth = SU.isNull(value,"");
  }
  public void setToCallMonth(String value)
  {
    ScreenData.put("toCallMonth",value);
    toCallMonth = SU.isNull(value,"");
  }
  public void setProductCode(String value)
  {
    ScreenData.put("productCode",value);
    productCode = SU.isNull(value,"");
  }
  public void setProductGroup(String value)
  {
    ScreenData.put("productGroup",value);
    productGroup = SU.isNull(value,"");
  }
  public void setSignName(String value)
  {
    signName = SU.isNull(value,"");
    ScreenData.put("signName",signName);
    if (!signName.equals(""))
    {
      setProviderId("0");
    }
  }
  public void setMasterAccountNumber(String value)
  {
    masterAccountNumber = SU.isNull(value,"");
    ScreenData.put("masterAccountNumber",masterAccountNumber);
    if (!masterAccountNumber.equals(""))
    {
      setSignName("");
    }
  }
  public void setRatingDuration(String value)
  {
    ScreenData.put("ratingDuration",value);
    ratingDuration = SU.isNull(value,"");
  }
  public void setRatingType(String value)
  {
    ScreenData.put("ratingType",value);
    ratingType = SU.isNull(value,"");
  }
  public void setSignNameProviderIdAndMasterAccountNumber(String signNameIn,
    String providerIn, String masterAccountNumberIn, boolean initial)
  {
    String sNI = SU.isNull(signNameIn,"");
    long pI = 0;
    try
    {
      pI = Long.parseLong(SU.hasNoValue(providerIn, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      pI = 0;
      ScreenData.put("providerId", "0");
    }
    String mANI = SU.isNull(masterAccountNumberIn,"");
    if (!signName.equals(sNI))
    {
      signName = sNI;
      ScreenData.put("signName",signName);
      if (!signName.equals(""))
      {
        providerId = 0;
        ScreenData.put("providerId","0");
        masterAccountNumber = "";
        ScreenData.put("masterAccountNumber","");
      }
    }
    else if (providerId != pI)
    {
      providerId = pI;
      ScreenData.put("providerId", String.valueOf(pI));
      masterAccountNumber = "";
      ScreenData.put("masterAccountNumber","");
      if (providerId > 0)
      {
        signName = "";
        ScreenData.put("signName","");
        if (initial)
        {
          masterAccountNumber = mANI;
          ScreenData.put("masterAccountNumber",masterAccountNumber);
        }
      }
    }
    else if (!masterAccountNumber.equals(mANI))
    {
      masterAccountNumber = mANI;
      ScreenData.put("masterAccountNumber",masterAccountNumber);
      if (!masterAccountNumber.equals(""))
      {
        signName = "";
        ScreenData.put("signName","");
      }
    }
  }
  public void setSignNameProviderIdAndAccountNumbers(String signNameIn,
    String providerIn, String masterAccountNumberIn, String accountNumberIn,
    boolean initial)
  {
    String sNI = SU.isNull(signNameIn,"");
    long pI = 0;
    try
    {
      pI = Long.parseLong(SU.hasNoValue(providerIn, "0"));
    }
    catch (java.lang.NumberFormatException NE)
    {
      pI = 0;
      ScreenData.put("providerId", "0");
    }
    String mANI = SU.isNull(masterAccountNumberIn,"");
    String aNI = SU.isNull(accountNumberIn,"");
    if (!signName.equals(sNI))
    {
      signName = sNI;
      ScreenData.put("signName",signName);
      if (!signName.equals(""))
      {
        providerId = 0;
        ScreenData.put("providerId","0");
        masterAccountNumber = "";
        ScreenData.put("masterAccountNumber","");
        accountNumber = "";
        ScreenData.put("accountNumber","");
      }
    }
    else if (providerId != pI)
    {
      providerId = pI;
      ScreenData.put("providerId", String.valueOf(pI));
      masterAccountNumber = "";
      ScreenData.put("masterAccountNumber","");
      accountNumber = "";
      ScreenData.put("accountNumber","");
      if (providerId > 0)
      {
        signName = "";
        ScreenData.put("signName","");
        if (initial)
        {
          masterAccountNumber = mANI;
          ScreenData.put("masterAccountNumber",masterAccountNumber);
          accountNumber = aNI;
          ScreenData.put("accountNumber",accountNumber);
        }
      }
    }
    else if (!masterAccountNumber.equals(mANI))
    {
      masterAccountNumber = mANI;
      ScreenData.put("masterAccountNumber",masterAccountNumber);
      if (!masterAccountNumber.equals(""))
      {
        signName = "";
        ScreenData.put("signName","");
        accountNumber = "";
        ScreenData.put("accountNumber","");
      }
    }
    else if (!accountNumber.equals(aNI))
    {
      accountNumber = aNI;
      ScreenData.put("accountNumber",accountNumber);
    }
  }
  public void setProviderId(String value)
  {
    try
    {
      setProviderId(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("providerId", "0");
    }
  }
  public void setProviderId(long value)
  {
    ScreenData.put("providerId",String.valueOf(value));
    providerId = value;
    if (providerId <= 0)
    {
      setMasterAccountNumber("");
    }
  }
  public void setRangeStart(String value)
  {
    try
    {
      setRangeStart(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("rangeStart", "0");
    }
  }
  public void setRangeStart(long value)
  {
    ScreenData.put("rangeStart",String.valueOf(value));
    rangeStart = value;
  }
  public void setRangeEnd(String value)
  {
    try
    {
      setRangeEnd(Long.parseLong(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("rangeEnd", "0");
    }
  }
  public void setRangeEnd(long value)
  {
    ScreenData.put("rangeEnd",String.valueOf(value));
    rangeEnd = value;
  }
  public void setDayRate(String value)
  {
    try
    {
      //MathContext not available in version of Java used - TA 23/02/2011
      double dbl = Double.parseDouble(SU.hasNoValue(value, "0"));
      String val = dp10.format(dbl);
      dayRate = new BigDecimal(val);
      ScreenData.put("dayRate", val);
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("dayRate", "0");
    }
  }
  public void setEveningRate(String value)
  {
    try
    {
      //MathContext not available in version of Java used - TA 23/02/2011
      double dbl = Double.parseDouble(SU.hasNoValue(value, "0"));
      String val = dp10.format(dbl);
      eveningRate = new BigDecimal(val);
      ScreenData.put("eveningRate", val);
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("eveningRate", "0");
    }
  }
  public void setWeekendRate(String value)
  {
    try
    {
      //MathContext not available in version of Java used - TA 23/02/2011
      double dbl = Double.parseDouble(SU.hasNoValue(value, "0"));
      String val = dp10.format(dbl);
      weekendRate = new BigDecimal(val);
      ScreenData.put("weekendRate", val);
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("weekendRate", "0");
    }
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("productCode"))
    {
      setProductCode(value);
    }
    else if (name.equals("productGroup"))
    {
      setProductGroup(value);
    }
    else if (name.equals("signName"))
    {
      setSignName(value);
    }
    else if (name.equals("masterAccountNumber"))
    {
      setMasterAccountNumber(value);
    }
    else if (name.equals("ratingType"))
    {
      setRatingType(value);
    }
    else if (name.equals("ratingDuration"))
    {
      setRatingDuration(value);
    }
    else if (name.equals("rangeStart"))
    {
      setRangeStart(value);
    }
    else if (name.equals("rangeEnd"))
    {
      setRangeEnd(value);
    }
    else if (name.equals("dayRate"))
    {
      setDayRate(value);
    }
    else if (name.equals("eveningRate"))
    {
      setEveningRate(value);
    }
    else if (name.equals("weekendRate"))
    {
      setWeekendRate(value);
    }
    else if (name.equals("fromCallMonth"))
    {
      setFromCallMonth(value);
    }
    else if (name.equals("toCallMonth"))
    {
      setToCallMonth(value);
    }
    else if (name.equals("providerId"))
    {
      setProviderId(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getProductCode()
  {
    return productCode;
  }
  public String getProductGroup()
  {
    return productGroup;
  }
  public String getSignName()
  {
    return signName;
  }
  public String getFromCallMonth()
  {
    return fromCallMonth;
  }
  public String getNewFromCallMonth()
  {
    return newFromCallMonth;
  }
  public String getToCallMonth()
  {
    return toCallMonth;
  }
  public String getMasterAccountNumber()
  {
    return masterAccountNumber;
  }
  public String getAccountNumber()
  {
    return accountNumber;
  }
  public String getRatingType()
  {
    return ratingType;
  }
  public String getRatingDuration()
  {
    return ratingDuration;
  }
  public String getProviderId()
  {
    return providerId>0?Long.toString(providerId):"";
  }
  public String getRangeStart()
  {
    return Long.toString(rangeStart);
  }
  public String getRangeEnd()
  {
    return rangeEnd>0?Long.toString(rangeEnd):"";
  }
  public String getDayRate()
  {
    //return dayRate.setScale(10, BigDecimal.ROUND_HALF_UP).toString();
    return dp10.format(dayRate.setScale(10, BigDecimal.ROUND_HALF_UP).doubleValue());
  }
  public String getEveningRate()
  {
    //return eveningRate.setScale(10, BigDecimal.ROUND_HALF_UP).toString();
    return dp10.format(eveningRate.setScale(10, BigDecimal.ROUND_HALF_UP).doubleValue());
  }
  public String getWeekendRate()
  {
    //return weekendRate.setScale(10, BigDecimal.ROUND_HALF_UP).toString();
    return dp10.format(weekendRate.setScale(10, BigDecimal.ROUND_HALF_UP).doubleValue());
  }
  public String getRatingSchemeId()
  {
    return ratingSchemeId;
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
    if ((FieldName.equals("productGroup")) ||
      (FieldName.equals("providerId")) ||
      (FieldName.equals("masterAccountNumber")) ||
      (FieldName.equals("accountNumber")) ||
      (FieldName.equals("signName")))
    {
      if ((Mode.equals("Confirm")) || (Mode.equals("View")) || (action.equals("Amend")) ||
      ((action.equals("AddTo"))/* && (Mode.equals("Amend"))*/ ))
      {
        return "DISABLED";
      }
      else
      {
        return "submit";
      }
    }
    else if (FieldName.equals("toCallMonth"))
    {
      return "DISABLED";
    }
    else if ((FieldName.equals("productCode")) ||
      (FieldName.equals("fromCallMonth")) ||
      (FieldName.equals("ratingType")) ||
      (FieldName.equals("ratingDuration")))
    {
      if ((Mode.equals("Confirm")) || (Mode.equals("View")) || (action.equals("Amend")) ||
        (action.equals("AddTo")))
      {
        return "DISABLED";
      }
      else
      {
        return "input";
      }
    }
    else if ((Mode.equals("Confirm")) || (Mode.equals("View")))
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
/*select 	Product_Code,
	Sign_Name,
	Master_Account_Number,
	Rating_Type,
	Range_Start,
	Range_End,
	Prime_Rate,
	Standard_Rate,
	Economy_Rate,
******************************************************************************/
  public boolean getRatingSchemeFromDB()
  {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Rating_Scheme(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,ratingSchemeId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          //fromCallMonth = RS.getString(1);
          //setProductCode(RS.getString(2));
          /*setSignNameProviderIdAndAccountNumbers(RS.getString(3),
            Long.toString(RS.getLong(12)), RS.getString(4), RS.getString(16),
            true);*/
          //setSignName(RS.getString(3));
          //setMasterAccountNumber(RS.getString(4));
          //setRatingType(RS.getString(5));
          rangeStart = RS.getLong(1);
          originalRangeStart = rangeStart;
          rangeEnd = RS.getLong(2);
          dayRate = RS.getBigDecimal(3,10);
          eveningRate = RS.getBigDecimal(4,10);
          weekendRate = RS.getBigDecimal(5,10);
          //setProductGroup(RS.getString(11));
          //setProviderId(RS.getLong(12));
          toCallMonth = RS.getString(6);
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
  public boolean validateRatingScheme()
  {
    boolean ok = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Validate_Rating_Scheme(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,!accountNumber.equals("")?null:signName.equals("")?null:signName);
        cstmt.setString(4,!accountNumber.equals("")?null:masterAccountNumber.equals("")?null:masterAccountNumber);
        cstmt.setString(5,ratingType);
        cstmt.setLong(6,rangeStart);
        cstmt.setLong(7,rangeEnd);
        cstmt.setString(8,toCallMonth);
        cstmt.setLong(9,originalRangeStart);
        cstmt.setString(10,Mode);
        cstmt.setString(11,action);
        cstmt.setString(12,accountNumber);
        cstmt.setString(13,ratingDuration);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            String ret = RS.getString(1);
            if (ret.equals("ok"))
            {
              ok = true;
            }
            else
            {
              Message = ret;
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
  public boolean updateRatingScheme()
  {
    boolean ok = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Update_Rating_Scheme(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,!accountNumber.equals("")?null:signName.equals("")?null:signName);
        cstmt.setString(4,!accountNumber.equals("")?null:masterAccountNumber.equals("")?null:masterAccountNumber);
        cstmt.setString(5,ratingType);
        cstmt.setLong(6,rangeStart);
        cstmt.setLong(7,rangeEnd);
        cstmt.setBigDecimal(8,dayRate);
        cstmt.setBigDecimal(9,eveningRate);
        cstmt.setBigDecimal(10,weekendRate);
        cstmt.setLong(11,originalRangeStart);
        cstmt.setString(12,userId);
        cstmt.setString(13,accountNumber);
        cstmt.setString(14,ratingDuration);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            int ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to update Rating Scheme</b></font>";
            }
            else
            {
              Message = "<font color=blue><b>Rating Scheme updated</b></font>";
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
  public boolean createRatingScheme()
  {
    boolean ok = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Create_Rating_Scheme(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,!accountNumber.equals("")?null:signName.equals("")?null:signName);
        cstmt.setString(4,!accountNumber.equals("")?null:masterAccountNumber.equals("")?null:masterAccountNumber);
        cstmt.setString(5,ratingType);
        cstmt.setLong(6,rangeStart);
        cstmt.setLong(7,rangeEnd);
        cstmt.setBigDecimal(8,dayRate);
        cstmt.setBigDecimal(9,eveningRate);
        cstmt.setBigDecimal(10,weekendRate);
        cstmt.setString(11,userId);
        cstmt.setString(12,accountNumber);
        cstmt.setString(13,ratingDuration);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            int ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to create Rating Scheme</b></font>";
            }
            else
            {
              Message = "<font color=blue><b>Rating Scheme created" +
                (ret==0?"":(". Please ensure that the corresponding " +
                (ratingDuration.equals("S")?"Long":"Short") +
                " duration Rating Scheme is created.")) +
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
        if (FieldName.equals("nameOrNumber"))
        {
          String sN = SU.isNull((String)ScreenData.get("signName"),"");
          String mAN = SU.isNull((String)ScreenData.get("masterAccountNumber"),"");
          if (((sN.equals("")) || (SU.isSpaces(sN))) &&
            ((mAN.equals("")) || (SU.isSpaces(mAN))))
          {
            setErrored(FieldName);
            Message = "<font color=red><b>Please select either 'Default Rating Scheme' or 'Master Account Number'";
            return false;
          }
        }
        else if ((FormField.equals("")) || (SU.isSpaces(FormField)))
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
    else if (!validateRatingScheme())
    {
      return false;
    }
    else
    {
      /*if ((billingCeaseDateh != null) && (billingCeaseDateh.before(billingStartDateh)))
      {
        Message = "<font color=red><b>Billing Cease Date cannot be before Billing Start Date";
        return false;
      }*/
      return true;
    }
  }
}

