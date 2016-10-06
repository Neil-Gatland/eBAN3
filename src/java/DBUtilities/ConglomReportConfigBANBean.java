package DBUtilities;

import java.sql.*;
import java.util.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import JavaUtil.ConglomLocalDataDescriptor;
import JavaUtil.ConglomLocalReportDataDescriptor;

public class ConglomReportConfigBANBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long itemId;
    private String screenItem;
    private java.sql.Date screenDateh;
    private int sDay;
    private int sMonth;
    private int sYear;
    private long conglomCustProductId;
    private String billedProductId;
    private String billedProductExtractFreq;
    private String billedProductFeedSource;
    private String reportType;
    private String conglomDataItem[] = {"", "", "", "", ""};
    private int startPos[] = {-1, -1 ,-1, -1, -1};
    private int length[] = {-1, -1 ,-1, -1, -1};
    private int conglomSort[] = {-1, -1 ,-1, -1, -1};
    private String conglomSubLevel1;
    private String conglomSubLevel2;
    private static final String GOLDFISH = "GOLDFISH";
    private String sourceConglomId;
    private String sourceSystemId;
    private Collection goldfishLocalData;
    private boolean reload;

  public ConglomReportConfigBANBean ()
  {
    mandatory.clear();
    mandatory.addElement("conglomBilledProduct");
    mandatory.addElement("conglomReportType");
    errored.addElement("");
    itemId = 0;
    screenDateh = new java.sql.Date(new java.util.Date().getTime());
    screenItem = "";
    sDay = 0;
    sMonth = 0;
    sYear = 0;
    conglomCustProductId = 0;
    billedProductId = "none";
    billedProductExtractFreq = "";
    billedProductFeedSource = "none";
    reportType = "";
    conglomSubLevel1 = "";
    conglomSubLevel2 = "";
    sourceConglomId = "";
    sourceSystemId = "";
    reload = false;
  }

  public void Reset()
  {
    super.Reset();
    setErrored("clear");
    itemId = 0;
    screenDateh = new java.sql.Date(new java.util.Date().getTime());
    screenItem = "";
    sDay = 0;
    sMonth = 0;
    sYear = 0;
    conglomCustProductId = 0;
    billedProductId = "none";
    billedProductExtractFreq = "";
    billedProductFeedSource = "none";
    reportType = "";
    conglomSubLevel1 = "";
    conglomSubLevel2 = "";
    for (int i=0; i<5; i++)
    {
      startPos[i] = -1;
      length[i] = -1;
      conglomDataItem[i] = "";
      conglomSort[i] = -1;
    }
    sourceConglomId = "";
    sourceSystemId = "";
    reload = false;
  }
/*set Methods*/
  public void setReload(boolean value)
  {
    reload = value;
  }
  public void setConglomSubLevel1(String value)
  {
    ScreenData.put("conglomSubLevel1",value);
    conglomSubLevel1 = SU.isNull(value,"");
  }
  public void setConglomSubLevel2(String value)
  {
    ScreenData.put("conglomSubLevel2",value);
    conglomSubLevel2 = SU.isNull(value,"");
  }
  public void setBilledProductExtractFreq(String value)
  {
    billedProductExtractFreq = SU.isNull(value,"");
  }
  public void setReportType(String value)
  {
    ScreenData.put("conglomReportType",value);
    reportType = SU.isNull(value,"");
    if (!reportType.equals(""))
    {
      if (reportType.endsWith("L"))
      {
        if (!mandatory.contains("conglomDataItem1"))
        {
          mandatory.addElement("conglomDataItem1");
        }
        if (!mandatory.contains("pos1"))
        {
          mandatory.addElement("pos1");
        }
      }
      else
      {
        if (mandatory.contains("conglomDataItem1"))
        {
          mandatory.removeElement("conglomDataItem1");
        }
        if (mandatory.contains("pos1"))
        {
          mandatory.removeElement("pos1");
        }
      }
      getReportDetails();
      getReportSubTotalLevels();
    }
  }
  public void setConglomDataItem(int index, String value)
  {
    ScreenData.put("conglomDataItem"+index,value);
    conglomDataItem[index-1] = SU.isNull(value,"");
  }
  public void setStartPos(int index, String value)
  {
    String trimmed = value.replace('-', ' ').trim();
    try
    {
      setStartPos(index, Integer.parseInt(SU.hasNoValue(trimmed, "-1")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("startPos"+index, "-1");
    }
  }
  public void setStartPos(int index, int value)
  {
    ScreenData.put("startPos"+index,String.valueOf(value));
    startPos[index-1] = value;
  }
  public void setLength(int index, String value)
  {
    String trimmed = value.replace('-', ' ').trim();
    try
    {
      setLength(index, Integer.parseInt(SU.hasNoValue(trimmed, "-1")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("length"+index, "-1");
    }
  }
  public void setLength(int index, int value)
  {
    ScreenData.put("length"+index,String.valueOf(value));
    length[index-1] = value;
  }
  public void setConglomSort(int index, String value)
  {
    try
    {
      setConglomSort(index, Integer.parseInt(SU.hasNoValue(value, "-1")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("conglomSort"+index, "-1");
    }
  }
  public void setConglomSort(int index, int value)
  {
    ScreenData.put("conglomSort"+index,String.valueOf(value));
    conglomSort[index-1] = value;
  }
  public void setBilledProductFeedSource(String value)
  {
    //ScreenData.put("billedProductFeedSource",value);
    billedProductFeedSource = SU.isNull(value,"");
  }
  public void setBilledProductId(String value)
  {
    ScreenData.put("conglomBilledProduct",value);
    billedProductId = value;
    if (action.equals("Add"))
    {
      getBilledProductDetails();
    }
  }
  public void setConglomCustProductId(long value)
  {
    conglomCustProductId = value;
  }
  public void setScreenItem(String value)
  {
    ScreenData.put("screenItem",value);
    screenItem = SU.isNull(value,"");
  }
  public void setSDay(String value)
  {
    try
    {
      setSDay(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sDay", "0");
    }
  }
  public void setSDay(int value)
  {
    ScreenData.put("sDay",String.valueOf(value));
    sDay = value;
  }
  public void setSMonth(String value)
  {
    try
    {
      setSMonth(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sMonth","0");
    }
  }
  public void setSMonth(int value)
  {
    ScreenData.put("sMonth",String.valueOf(value));
    sMonth = value;
  }
  public void setSYear(String value)
  {
    try
    {
      setSYear(Integer.parseInt(SU.hasNoValue(value, "0")));
    }
    catch (java.lang.NumberFormatException NE)
    {
      ScreenData.put("sYear","0");
    }
  }
  public void setSYear(int value)
  {
    ScreenData.put("sYear",String.valueOf(value));
    sYear = value;
  }
  public void setScreenDate(String value)
  {
    ScreenData.put("screenDateh",value);
    screenDateh = SU.toJDBCDate(value);
  }
/****************************************************************************************/
  private void setDates()
  {
    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.setTime(screenDateh==null?(new java.util.Date())
      :screenDateh);
    sDay=cal.get(cal.DATE);
    sMonth=cal.get(cal.MONTH)+1;
    sYear=cal.get(cal.YEAR);
    /*if (billingCeaseDateh == null)
    {
      bcDay=0;
      bcMonth=0;
      bcYear=0;
    }
    else
    {
      cal.setTime(billingCeaseDateh);
      bcDay=cal.get(cal.DATE);
      bcMonth=cal.get(cal.MONTH)+1;
      bcYear=cal.get(cal.YEAR);
    }*/
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("screenItem"))
    {
      setScreenItem(value);
    }
    else if (name.equals("screenDateDay"))
    {
      setSDay(value);
    }
    else if (name.equals("screenDateMonth"))
    {
      setSMonth(value);
    }
    else if (name.equals("screenDateYear"))
    {
      setSYear(value);
    }
    else if (name.equals("screenStartDateh"))
    {
      setScreenDate(value);
    }
    else if (name.equals("conglomBilledProduct"))
    {
      setBilledProductId(value);
    }
    else if (name.equals("conglomReportType"))
    {
      setReportType(value);
    }
    else if (name.equals("startPos1"))
    {
      setStartPos(1, value);
    }
    else if (name.equals("length1"))
    {
      setLength(1, value);
    }
    else if (name.equals("conglomDataItem1"))
    {
      setConglomDataItem(1, value);
    }
    else if (name.equals("conglomSort1"))
    {
      setConglomSort(1, value);
    }
    else if (name.equals("startPos2"))
    {
      setStartPos(2, value);
    }
    else if (name.equals("length2"))
    {
      setLength(2, value);
    }
    else if (name.equals("conglomDataItem2"))
    {
      setConglomDataItem(2, value);
    }
    else if (name.equals("conglomSort2"))
    {
      setConglomSort(2, value);
    }
    else if (name.equals("startPos3"))
    {
      setStartPos(3, value);
    }
    else if (name.equals("length3"))
    {
      setLength(3, value);
    }
    else if (name.equals("conglomDataItem3"))
    {
      setConglomDataItem(3, value);
    }
    else if (name.equals("conglomSort3"))
    {
      setConglomSort(3, value);
    }
    else if (name.equals("startPos4"))
    {
      setStartPos(4, value);
    }
    else if (name.equals("length4"))
    {
      setLength(4, value);
    }
    else if (name.equals("conglomDataItem4"))
    {
      setConglomDataItem(4, value);
    }
    else if (name.equals("conglomSort4"))
    {
      setConglomSort(4, value);
    }
    else if (name.equals("startPos5"))
    {
      setStartPos(5, value);
    }
    else if (name.equals("length5"))
    {
      setLength(5, value);
    }
    else if (name.equals("conglomDataItem5"))
    {
      setConglomDataItem(5, value);
    }
    else if (name.equals("conglomSort5"))
    {
      setConglomSort(5, value);
    }
    else if (name.equals("conglomSubLevel1"))
    {
      setConglomSubLevel1(value);
    }
    else if (name.equals("conglomSubLevel2"))
    {
      setConglomSubLevel2(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public boolean getReload()
  {
    return reload;
  }
  public String getBilledProductExtractFreq()
  {
    return billedProductExtractFreq;
  }
  public String getConglomSubLevel1()
  {
    return conglomSubLevel1;
  }
  public String getConglomSubLevel2()
  {
    return conglomSubLevel2;
  }
  public String getBilledProductFeedSource()
  {
    return billedProductFeedSource;
  }
  public String getBilledProductId()
  {
    return billedProductId;
  }
  public long getConglomCustProductId()
  {
    return conglomCustProductId;
  }
  public String getScreenItem()
  {
    return screenItem;
  }
  public String getReportType()
  {
    return reportType;
  }
  public String getConglomDataItem(int index)
  {
    return conglomDataItem[index-1];
  }
  public String getStartPos(int index)
  {
    return startPos[index-1]==-1?"":Integer.toString(startPos[index-1]);
  }
  public String getLength(int index)
  {
    return length[index-1]==-1?"":Integer.toString(length[index-1]);
  }
  public String getConglomSort(int index)
  {
    return conglomSort[index-1]==-1?"":Integer.toString(conglomSort[index-1]);
  }
  public int getSDay()
  {
    return sDay;
  }
  public int getSMonth()
  {
    return sMonth;
  }
  public int getSYear()
  {
    return sYear;
  }
  public String getScreenDate()
  {
    if (screenDateh != null)
    {
      return SU.reformatDate(screenDateh.toString());
    }
    else
    {
      return null;
    }
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
  public String getSelectMode(String FieldName)
  {
    if ((Mode.equals("Confirm")) || (Mode.equals("View")))
    {
      return "DISABLED";
    }
    /*else if (FieldName.startsWith("conglomDataItem"))
    {
      return "onChange=\"saveDescription('" + FieldName.charAt(FieldName.length()-1) + "')\"";
    }*/
    else if (FieldName.equals("conglomBilledProduct"))
    {
      if (action.equals("Add"))
      {
        return "submit";
      }
      else
      {
        return "DISABLED";
      }
    }
    else
    {
      return super.getSelectMode();
    }
  }
  public String getInputMode(String FieldName)
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
/*******************************************************************************/
 public boolean getFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call db..Get_Stuff_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,itemId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          screenItem = RS.getString(1);
          screenDateh = RS.getDate(2);
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
public void updateReloadReportDetails()
{
  if ((updateReportDetails() == 1) && (reload))
  {
    if (reloadGoldfishLocalConfigData() == false)
    {
      String exceptionMessage = "";
      if (Message.indexOf(":") != -1)
      {
        exceptionMessage = " Message: " +Message.substring(Message.indexOf(":")+1);
      }
      Message = "<font color=red>Report details updated but unable to reload " +
        "Goldfish local config data." + exceptionMessage;
    }
    else
    {
      Message = "<font color=blue><b>Report configuration updated and " +
        "Goldfish local config data reloaded.</b></font>";
    }
  }
}
/*******************************************************************************/
public int updateReportDetails()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Update_Local_Data_eBAN(?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,this.getConglomCustomerId());
      cstmt.setString(2,billedProductId);
      cstmt.setString(3,reportType);
      cstmt.setString(4,conglomSubLevel1);
      cstmt.setString(5,conglomSubLevel2);
      cstmt.setString(6,conglomDataItem[0].equals("")?"":
        conglomDataItem[0].substring(conglomDataItem[0].indexOf("|")+1));
      cstmt.setString(7,conglomDataItem[0].equals("")?"":
        conglomDataItem[0].substring(0, conglomDataItem[0].indexOf("|")));
      cstmt.setInt(8,startPos[0]);
      cstmt.setInt(9,length[0]);
      cstmt.setInt(10,conglomSort[0]);
      cstmt.setString(11,conglomDataItem[1].equals("")?"":
        conglomDataItem[1].substring(conglomDataItem[1].indexOf("|")+1));
      cstmt.setString(12,conglomDataItem[1].equals("")?"":
        conglomDataItem[1].substring(0, conglomDataItem[1].indexOf("|")));
      cstmt.setInt(13,startPos[1]);
      cstmt.setInt(14,length[1]);
      cstmt.setInt(15,conglomSort[1]);
      cstmt.setString(16,conglomDataItem[2].equals("")?"":
        conglomDataItem[2].substring(conglomDataItem[2].indexOf("|")+1));
      cstmt.setString(17,conglomDataItem[2].equals("")?"":
        conglomDataItem[2].substring(0, conglomDataItem[2].indexOf("|")));
      cstmt.setInt(18,startPos[2]);
      cstmt.setInt(19,length[2]);
      cstmt.setInt(20,conglomSort[2]);
      cstmt.setString(21,conglomDataItem[3].equals("")?"":
        conglomDataItem[3].substring(conglomDataItem[3].indexOf("|")+1));
      cstmt.setString(22,conglomDataItem[3].equals("")?"":
        conglomDataItem[3].substring(0, conglomDataItem[3].indexOf("|")));
      cstmt.setInt(23,startPos[3]);
      cstmt.setInt(24,length[3]);
      cstmt.setInt(25,conglomSort[3]);
      cstmt.setString(26,conglomDataItem[4].equals("")?"":
        conglomDataItem[4].substring(conglomDataItem[4].indexOf("|")+1));
      cstmt.setString(27,conglomDataItem[4].equals("")?"":
        conglomDataItem[4].substring(0, conglomDataItem[4].indexOf("|")));
      cstmt.setInt(28,startPos[4]);
      cstmt.setInt(29,length[4]);
      cstmt.setInt(30,conglomSort[4]);
      cstmt.setString(31,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret != 1)
          {
            Message = "<font color=red><b>Unable to update report configuration</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Report configuration updated</b></font>";
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
public int createDBEntry()
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call db..Create_Stuff_eBAN(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,screenItem);
      cstmt.setDate(2,screenDateh);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new stuff</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New stuff created for " +
              screenItem +"</b></font>";
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

    //if (ButtonPressed.startsWith("Submit"))
    //{
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if (FieldName.equals("pos1"))
        {
          FormField=SU.isNull((String)ScreenData.get("startPos1"),"");
          String FormField2=SU.isNull((String)ScreenData.get("length1"),"");
          if ((FormField.equals("")) || (SU.isSpaces(FormField)) ||
            (FormField2.equals("")) || (SU.isSpaces(FormField2)))
          {
            setErrored(FieldName);
          }
        }
        else if ((FormField.equals("")) || (SU.isSpaces(FormField)))
        {
          setErrored(FieldName);
        }
      }
    //}
    if ((!errored.isEmpty()) && (errored.size() > 0))
    {
      Message = "<font color=red><b>One or more mandatory fields (highlighted in red) are missing";
      return false;
    }
    else
    {
      if ((!conglomSubLevel2.equals("")) && (conglomSubLevel1.equals("")))
      {
        Message = "<font color=red><b>First Level Sub-totalling must be selected if Second Level is selected ";
        setErrored("conglomSubLevel1");
        setErrored("conglomSubLevel2");
        return false;
      }
      else if (reportType.endsWith("L"))
      {
        int totalLength = 0;
        for (int i=0; i<conglomDataItem.length; i++)
        {
          String iStr = Integer.toString(i+1);
          if ((!conglomDataItem[i].equals("")) &&
            ((length[i] == -1) || (startPos[i] == -1)))
          {
            Message = "<font color=red><b>Please enter both start position and length";
            setErrored("pos" + iStr);
            return false;
          }
          if ((conglomDataItem[i].equals("")) &&
            ((length[i] != -1) || (startPos[i] != -1)))
          {
            Message = "<font color=red><b>Only enter start position and length if data item has been selected";
            setErrored("pos" + iStr);
            return false;
          }
          if ((length[i] == 0) || (startPos[i] == 0))
          {
            Message = "<font color=red><b>Start position and length must be greater than zero if entered";
            setErrored("pos" + iStr);
            return false;
          }
          if ((startPos[i] != -1) && (startPos[i] != (totalLength + 1)))
          {
            Message = "<font color=red><b>Start position " + iStr +
              " should be " + Integer.toString(totalLength + 1);
            setErrored("pos" + iStr);
            return false;
          }
          if (length[i] != -1)
          {
            totalLength += length[i];
            if (totalLength > 50)
            {
              Message = "<font color=red><b>The overall length of the Data Strings must be 50 or less";
              setErrored("pos" + iStr);
              return false;
            }
          }
          for (int j=0; j<conglomDataItem.length; j++)
          {
            String jStr = Integer.toString(j+1);
            if (i != j)
            {
              if ((!conglomDataItem[i].equals("")) &&
                (conglomDataItem[i].equals(conglomDataItem[j])))
              {
                Message = "<font color=red><b>Data Item " + iStr + " is the same as Data Item " +
                  jStr + ". Please reselect.";
                setErrored("conglomDataItem" + iStr);
                setErrored("conglomDataItem" + jStr);
                return false;
              }
              else if ((conglomSort[i] != -1) &&
                (conglomSort[i] == conglomSort[j]))
              {
                Message = "<font color=red><b>Sort Items must be unique. " +
                  "Please reselect.";
                setErrored("conglomDataItem" + iStr);
                setErrored("conglomDataItem" + jStr);
                setErrored("conglomSort1");
                return false;
              }
            }
          }
        }
      }
      return true;
    }
  }
/************************************************************************************************/
  private void getBilledProductDetails()
  {
    try
    {
      SQL = "select Feed_Source, Extract_Frequency " +
        "from conglomerate..Billed_Product (nolock) " +
        "where Billed_Product_Id = '" + billedProductId + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          setBilledProductFeedSource(RS.getString(1));
          setBilledProductExtractFreq(RS.getString(2));
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
    }
  }
/************************************************************************************************/
  public void getReportDetails()
  {
    for (int i=0; i<5; i++)
    {
      startPos[i] = -1;
      length[i] = -1;
      conglomDataItem[i] = "";
      conglomSort[i] = -1;
    }
    if ((getReportSubTotalLevels()) && (reportType.endsWith("L")))
    {
      getLocalDataItems();
    }
  }
/************************************************************************************************/
  private boolean getReportSubTotalLevels()
  {
    boolean ok = false;
    try
    {
      SQL = "select Sub_Total_Level1, Sub_Total_Level2 " +
        "from conglomerate..cust_report_params (nolock) " +
        "where Conglom_Cust_Id = " + this.getConglomCustomerId() + " " +
        "and Billed_Product_Id = '" + billedProductId + "' " +
        "and Report_Type = '" + reportType.substring(0, reportType.length()-1) + "' ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          conglomSubLevel1 = RS.getString(1);
          conglomSubLevel2 = RS.getString(2);
        }
        ok = true;
      }
    }
    catch(Exception ex)
    {
      Message=ex.getMessage();
    }
    finally
    {
      close();
      return ok;
    }
  }
/************************************************************************************************/
  private void getLocalDataItems()
  {
    try
    {
      SQL = "select LD_Description + '|' + LD_Ref_Type, LD_Data_Start, " +
        "LD_Data_length, LD_Sort_Seq_No " +
        "from conglomerate..cust_local_data_ref_item (nolock) " +
        "where Conglom_Cust_Id = " + this.getConglomCustomerId() + " " +
        "and Billed_Product_Id = '" + billedProductId + "' " +
        "and Report_Type = '" + reportType.substring(0, reportType.length()-1) + "' " +
        "order by LD_Column_Seq_No ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        int i = 0;
        while ((RS.next()) && (i < conglomDataItem.length))
        {
          conglomDataItem[i] = RS.getString(1);
          startPos[i] = RS.getInt(2);
          length[i] = RS.getInt(3);
          conglomSort[i] = RS.getInt(4);
          i++;
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
    }
  }
/*******************************************************************************/
  public boolean reloadGoldfishLocalConfigData()
  {
    boolean reload = true;
    try
    {
      goldfishLocalData = getCustReportParams();
      for (Iterator it = goldfishLocalData.iterator(); it.hasNext(); )
      {
        ConglomLocalDataDescriptor cldd = (ConglomLocalDataDescriptor)it.next();
        if (reportType.equals("CONSOLL"))
        {
          cldd.setLocalReportData(getConsolLocalReportData(cldd.getRefType()));
        }
        else //(reportType.equals("BILSUML"))
        {
          cldd.setLocalReportData(getBilsumLocalReportData(cldd.getRefType()));
        }
        Collection lrd = cldd.getLocalReportData();
        for (Iterator it2 = lrd.iterator(); it2.hasNext(); )
        {
          ConglomLocalReportDataDescriptor clrdd =
            (ConglomLocalReportDataDescriptor)it2.next();
          conglomDataItem[cldd.getColSeqNo()-1] = clrdd.getDescription() + ".|" +
            clrdd.getReferenceType();
          String referenceItemSort =
            cldd.getSortSeqNo()>0?clrdd.getReferenceItem():null;
          int ret = updateGoldfishLocalData(clrdd.getAccountNo(),
            clrdd.getBillingNo(), clrdd.getReferenceItem(), referenceItemSort,
            cldd.getColSeqNo());
          if (ret < 0)
          {
            reload = false;
          }
        }
      }
    }
    catch (Exception ex)
    {
      Message = "<font color=red>Unable to reload Goldfish local config data. " +
        "Message: " + ex.getMessage();
      reload = false;
    }
    finally
    {
      return reload;
    }
  }
/*******************************************************************************/
public int updateGoldfishLocalData(String sourceAccountNo, String billingNumber,
  String referenceItem, String referenceItemSort, int columnSeqNo)
{
  int ret = 0;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call conglomerate..Reload_Goldfish_Local_Data_eBAN(?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,this.getConglomCustomerId());
      cstmt.setString(2,billedProductId);
      cstmt.setString(3,reportType);
      cstmt.setString(4,sourceAccountNo);
      cstmt.setString(5,billingNumber);
      cstmt.setString(6,referenceItem);
      cstmt.setString(7,referenceItemSort);
      cstmt.setInt(8,columnSeqNo);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ret = RS.getInt(1);
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
  private Collection getCustReportParams()
  {
    ArrayList custReportParams = new ArrayList();
    try
    {
      SQL = "select LD_Ref_Type, LD_Column_Seq_No, LD_Sort_Seq_No " +
        "from conglomerate..Cust_Local_Data_Ref_Item (nolock) " +
        "where Conglom_Cust_Id = " + this.getConglomCustomerId() + " " +
        "and Billed_Product_Id = '" + billedProductId + "' " +
        "and Report_Type = '" + reportType.substring(0, reportType.length()-1) + "' " +
        "order by LD_Column_Seq_No ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          custReportParams.add(new ConglomLocalDataDescriptor(RS.getString(1),
            RS.getInt(2), RS.getInt(3)));
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
      return custReportParams;
    }
  }
/*******************************************************************************/
  private Collection getConsolLocalReportData(String refType)
  {
    ArrayList localReportData = new ArrayList();
    try
    {
      SQL = "select Aref.ACCO_ACCOUNT_NUMBER, " +
        "ReTty.DESCRIPTION, Aref.REFERENCE_ITEM, ReTty.REFERENCE_TYPE " +
        "from T_ACCOUNT_REFERENCE_ITEMS Aref, T_REFERENCE_TYPES ReTty " +
        "where Aref.ACCO_ACBS_SHORT_NAME = '" + sourceSystemId + "' " +
        "and Aref.ACCO_COCA_ID = '" + sourceConglomId + "' " +
        "and Aref.CURT_RETY_REFERENCE_TYPE = '" + refType + "' " +
        "and Aref.CURT_RETY_REFERENCE_TYPE = ReTty.REFERENCE_TYPE " +
        "order by ARef.ACCO_ACCOUNT_NUMBER ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,GOLDFISH))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          localReportData.add(new ConglomLocalReportDataDescriptor(RS.getString(1),
            RS.getString(2), RS.getString(3), null, RS.getString(4)));
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
      return localReportData;
    }
  }
/*******************************************************************************/
  private Collection getBilsumLocalReportData(String refType)
  {
    ArrayList localReportData = new ArrayList();
    try
    {
      SQL = "select Aref.BINU_ACCO_ACCOUNT_NUMBER, ReTty.DESCRIPTION, " +
        "Aref.REFERENCE_ITEM, Aref.BINU_BILLING_NUMBER, ReTty.REFERENCE_TYPE " +
        "from T_BILLING_NUMBER_REFERENCE_ITE Aref, T_REFERENCE_TYPES ReTty " +
        "where Aref.BINU_ACCO_ACBS_SHORT_NAME = '" + sourceSystemId + "' " +
        "and Aref.BINU_ACCO_COCA_ID = '" + sourceConglomId + "' " +
        "and Aref.CURT_RETY_REFERENCE_TYPE = '" + refType + "' " +
        "and Aref.CURT_RETY_REFERENCE_TYPE = ReTty.REFERENCE_TYPE " +
        "order by Aref.BINU_ACCO_ACCOUNT_NUMBER, Aref.BINU_BILLING_NUMBER ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,GOLDFISH))
      {
        RS = DBA.getResultsSet();
        while (RS.next())
        {
          localReportData.add(new ConglomLocalReportDataDescriptor(RS.getString(1),
            RS.getString(2), RS.getString(3), RS.getString(4), RS.getString(5)));
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
      return localReportData;
    }
  }
/************************************************************************************************/
  public boolean getBilledProductIdFromDB()
  {
    boolean getBilledProductId = false;
    try
    {
      SQL = "select Billed_Product_Id, Source_Conglom_Id, Source_System_Id " +
        "from conglomerate..view_cust_products (nolock) " +
        "where Conglom_Cust_Product_Id = " + conglomCustProductId + " ";
      DBA.setSQL(SQL);
      if (DBA.Connect(READ,P5))
      {
        RS = DBA.getResultsSet();
        if (RS.next())
        {
          setBilledProductId(RS.getString(1));
          sourceConglomId = RS.getString(2);
          sourceSystemId = RS.getString(3);
          getBilledProductId = true;
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
      return getBilledProductId;
    }
  }
/*******************************************************************************/
/* public boolean getBilledProductIdFromDB()
 {
    boolean getBilledProductId = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call conglomerate..Get_Customer_Product_eBAN(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,conglomCustProductId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          setBilledProductId(RS.getString(6));
          getBilledProductId = true;
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
      return getBilledProductId;
    }
  }*/
}

