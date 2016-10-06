package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

public class NostroUserBANBean extends BANBean
{
  private String sql;
  private String Nostro_Reference;
  private String Nostro_User_Id;
  private String User_Name;
  private String User_Surname;
  private java.sql.Date UserStartDate=null,UserEndDate=null;
  protected Vector mandatory=new Vector();
  private Hashtable ScreenData=new Hashtable();
  private int BEDays, BEMonths, BEYears, USDays, USMonths, USYears,
    UEDays, UEMonths, UEYears;

  public NostroUserBANBean ()
  {
    Nostro_Reference = "";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    USDays=0;
    USMonths=0;
    USYears=0;
    UEDays=0;
    UEMonths=0;
    UEYears=0;
    mandatory.clear();
    //mandatory.addElement("Nostro_Reference");
    mandatory.addElement("BANEffectiveDateh");
    mandatory.addElement("UserStartDateh");
    mandatory.addElement("Nostro_User_Id");
    //mandatory.addElement("User_Name");
    //mandatory.addElement("User_Surname");
    errored.addElement("");
  }

  public void Reset()
  {
    super.Reset();
    Nostro_Reference = "";
    BEDays=0;
    BEMonths=0;
    BEYears=0;
    USDays=0;
    USMonths=0;
    USYears=0;
    UEDays=0;
    UEMonths=0;
    UEYears=0;
    Nostro_User_Id = "";
    User_Name = "";
    User_Surname = "";
    UserStartDate=null;
    UserEndDate=null;
    Required_BAN_Effective_Dateh=null;
    setErrored("clear");
  }
/************************************************************************************************/
public String getNostroReference()
{
  return Nostro_Reference;
}
/************************************************************************************************/
public void setNostroReference(String inRef)
{
  Nostro_Reference = inRef;
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
/************************************************************************************************/
  public String getMode(String FieldName)
  {//This defines whether or not a field is enterable

    if (FieldName.compareTo("Unused_Global_Customer") ==0)
    {
      if (Mode.compareTo("Amend") == 0)
	  return "READONLY";
      else
	  return InputMode;
    }
    if (action.compareTo("Authorise") == 0)
    {
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
/************************************************************************************************/
  public boolean createUserBAN()
  {
    boolean createUserBAN = false;
    String newUserId = null;
    try{
	  if (DBA.Connect(WRITE,P5))
	  {
            SQL = "{call eban..Create_Nostro_User_BAN " +
              "(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            cstmt = DBA.Conn.prepareCall(SQL);
            cstmt.setString(1,banStatus);
            cstmt.setString(2,Global_Customer_Id);
            cstmt.setString(3,BAN_Summary);
            cstmt.setDate(4,Required_BAN_Effective_Dateh);
            cstmt.setString(5,Mode);
            cstmt.setString(6,banCreatedBy);
            cstmt.setString(7,Nostro_Reference);
            cstmt.setString(8,Nostro_User_Id);
            cstmt.setString(9,User_Name);
            cstmt.setString(10,User_Surname);
            cstmt.setDate(11,UserStartDate);
            cstmt.setDate(12,UserEndDate);
            cstmt.setString(13,Account_Id);

	    try
	    {
	      cstmt.execute();
	      RS = cstmt.getResultSet();
	      if (RS.next())
	      {
	        banIdentifier=RS.getString(1);
	        newUserId=RS.getString(2);
                createUserBAN = true;
        	Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created"+
                (Nostro_User_Id.equals(newUserId)?"":" - User Id changed to "+newUserId+
                " to avoid duplication");
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
	  return createUserBAN;
        }
  }
/*******************************************************************************/
 public boolean getUserBAN()
 {
    boolean getUserBAN = false;
    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call eban..get_Nostro_User_BAN ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,banIdentifier);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    banStatus=(RS.getString(1));
	    Global_Customer_Id=RS.getString(2);
	    Required_BAN_Effective_Dateh=RS.getDate(3);
   	    BAN_Summary=RS.getString(4);
	    rejectReason=RS.getString(5);
	    Mode=(RS.getString(6));
	    Nostro_Reference=(RS.getString(7));
	    Nostro_User_Id=(RS.getString(8));
	    User_Name=(RS.getString(9));
	    User_Surname=(RS.getString(10));
	    UserStartDate=(RS.getDate(11));
	    UserEndDate=(RS.getDate(12));
	    Account_Id=(RS.getString(13));
            setDates();
            getUserBAN = true;
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
      Message="<font color=red><b>Null Pointer "+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getUserBAN;
    }
 }
/*******************************************************************************/
 public boolean getUser()
 {
    boolean getUser = false;
    try{
      if (DBA.Connect(PREPARE,P3))
      {
        SQL = "{call nostro..get_Nostro_User ";
        SQL += "(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
	cstmt.setString(1,Nostro_User_Id);
        cstmt.execute();
	RS = cstmt.getResultSet();

	try
	{
	  if (RS.next())
	  {
	    User_Name=(RS.getString(1));
	    User_Surname=(RS.getString(2));
	    UserStartDate=(RS.getDate(3));
	    UserEndDate=(RS.getDate(4));
            setDates();
            getUser = true;
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
      Message="<font color=red><b>Null Pointer "+se.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return getUser;
    }
 }
/************************************************************************************************/
  public boolean AuthoriseUserBAN()
  {
    if (insertUpdateNostroUser())
    {
      if (updateBANStatus())
        return true;
      else
      {
        Message="<font color=red><b>Unable to update BAN status. Please contact system support.";
        return false;
      }
    }
    else
      return false;
  }
/*********************************************************************/
  private boolean insertUpdateNostroUser()
  {
    boolean insertUpdateNostroUser = false;
    try{
	  if (DBA.Connect(WRITE,P3))
	  {
            if (Mode.equals("Add"))
            {
              SQL = "{call nostro..Insert_Nostro_User " +
                "(?,?,?,?,?,?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,Nostro_Reference);
              cstmt.setString(3,Nostro_User_Id);
              cstmt.setString(4,User_Name);
              cstmt.setString(5,User_Surname);
              cstmt.setDate(6,UserStartDate);
              cstmt.setDate(7,UserEndDate);
              cstmt.setString(8,Account_Id);
              cstmt.setString(9,userId);
            }
            else
            {
              SQL = "{call nostro..Update_Nostro_User " +
                "(?,?,?,?,?,?,?,?,?)}";

              cstmt = DBA.Conn.prepareCall(SQL);
              cstmt.setString(1,Global_Customer_Id);
              cstmt.setString(2,Nostro_Reference);
              cstmt.setString(3,Nostro_User_Id);
              cstmt.setString(4,User_Name);
              cstmt.setString(5,User_Surname);
              cstmt.setDate(6,UserStartDate);
              cstmt.setDate(7,UserEndDate);
              cstmt.setString(8,Account_Id);
              cstmt.setString(9,userId);
            }

	    try
	    {
	      cstmt.execute();
              insertUpdateNostroUser = true;
              Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" created";
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
          return insertUpdateNostroUser;
        }
  }
/************************************************************************************************/
  private boolean updateBANStatus()
  {
    boolean updateBANStatus = false;
    try{
      SQL = "UPDATE eban..Nostro_User_BAN " +
        "Set BAN_Status_Code = 'Implemented' " +
        "WHERE BAN_Identifier = '" + banIdentifier + "' ";
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        updateBANStatus = true;
      }
    }
    catch(java.sql.SQLException se)
    {
      Message=se.getMessage();
      updateBANStatus = false;
    }
    catch(java.lang.NullPointerException se)
    {
      Message="<font color=red><b>"+se.getMessage();
      updateBANStatus = false;
    }//message set in underlying code
    finally
    {
      closeupdate();
      return updateBANStatus;
    }
  }
/************************************************************************************************/
  public boolean updateUserBAN()
  {
    boolean updateUserBAN = false;
    int rowcount=0;
    try{
      if (DBA.Connect(WRITE,P5))
      {
        SQL = "{call eban..Update_Nostro_User_BAN ";
        SQL += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,banIdentifier);
        cstmt.setString(2,banStatus);
	cstmt.setString(3,Global_Customer_Id);
	cstmt.setString(4,BAN_Summary);
	cstmt.setDate(5,Required_BAN_Effective_Dateh);
	cstmt.setString(6,Nostro_Reference);
	cstmt.setString(7,Nostro_User_Id);
	cstmt.setString(8,User_Name);
	cstmt.setString(9,User_Surname);
	cstmt.setDate(10,UserStartDate);
	cstmt.setDate(11,UserEndDate);
	cstmt.setString(12,Account_Id);
	cstmt.setString(13,userId);
	cstmt.setString(14,rejectReason);

        cstmt.execute();
        rowcount=cstmt.getUpdateCount();
	if (rowcount != 1)
	{
	  Message="Error! - "+Integer.toString(rowcount) + " Rows Updated";
	}
        else
        {
          updateUserBAN = true;
          Message = "<font color=blue><b>BAN Id:-"+banIdentifier+" "+action+
            (action.endsWith("e")?"d":"ed");
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
      closeupdate();
      return updateUserBAN;
    }
  }
/*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.compareTo("BANEffectiveDateh")==0)
    {
     setRequired_BAN_Effective_Date(value);
     ScreenData.put("BANEffectiveDateh",value);
    }
    else if (name.compareTo("BANEffectiveDateDay")==0)
    {
     setBANEffectiveDateDays(value);
     ScreenData.put("BANEffectiveDateDay",value);
    }
    else if (name.compareTo("BANEffectiveDateMonth")==0)
    {
     setBANEffectiveDateMonths(value);
     ScreenData.put("BANEffectiveDateMonth",value);
    }
    else if (name.compareTo("BANEffectiveDateYear")==0)
    {
     setBANEffectiveDateYears(value);
     ScreenData.put("BANEffectiveDateYear",value);
    }
    else if (name.compareTo("UserStartDateh")==0)
    {
     setUserStartDate(value);
     ScreenData.put("UserStartDateh",value);
    }
    else if (name.compareTo("UserStartDateDay")==0)
    {
     setUserStartDateDays(value);
     ScreenData.put("UserStartDateDay",value);
    }
    else if (name.compareTo("UserStartDateMonth")==0)
    {
     setUserStartDateMonths(value);
     ScreenData.put("UserStartDateMonth",value);
    }
    else if (name.compareTo("UserStartDateYear")==0)
    {
     setUserStartDateYears(value);
     ScreenData.put("UserStartDateYear",value);
    }
    else if (name.compareTo("UserEndDateh")==0)
    {
     setUserEndDate(value);
    }
    else if (name.compareTo("UserEndDateDay")==0)
    {
     setUserEndDateDays(value);
     ScreenData.put("UserEndDateDay",value);
    }
    else if (name.compareTo("UserEndDateMonth")==0)
    {
     setUserEndDateMonths(value);
     ScreenData.put("UserEndDateMonth",value);
    }
    else if (name.compareTo("UserEndDateYear")==0)
    {
     setUserEndDateYears(value);
     ScreenData.put("UserEndDateYear",value);
    }
    else if (name.compareTo("BAN_Summary")==0)
    {
      setBAN_Summary(value);
      ScreenData.put("BAN_Summary",value);
    }
    else if (name.compareTo("BAN_Reason")==0)
    {
      setBAN_Reason(value);
      ScreenData.put("BAN_Reason",value);
    }
    else if (name.compareTo("Nostro_Reference")==0)
    {
      setNostroReference(value);
      ScreenData.put("Nostro_Reference",value);
    }
    else if (name.compareTo("Nostro_User_Id")==0)
    {
      setNostroUserId(value);
      ScreenData.put("Nostro_User_Id",value);
    }
    else if (name.compareTo("User_Name")==0)
    {
      setUserName(value);
      ScreenData.put("User_Name",value);
    }
    else if (name.compareTo("User_Surname")==0)
    {
      setUserSurname(value);
      ScreenData.put("User_Surname",value);
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
  public void setUserStartDate(String newDate)
  {
    UserStartDate = SU.toJDBCDate(newDate);
  }
/************************************************************************************************/
  public String getUserStartDate()
  {
    if (UserStartDate != null)
    {
      return SU.reformatDate(UserStartDate.toString());
    }
    else
    {
      return null;
    }
  }
/************************************************************************************************/
  public int getUserStartDateDays()
  {
    return USDays;
  }
/************************************************************************************************/
  public void setUserStartDateDays(String value)
  {
    USDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getUserStartDateMonths()
  {
    return USMonths;
  }
/************************************************************************************************/
  public void setUserStartDateMonths(String value)
  {
    USMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getUserStartDateYears()
  {
    return USYears;
  }
/************************************************************************************************/
  public void setUserStartDateYears(String value)
  {
    USYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public void setUserEndDate(String newDate)
  {
    UserEndDate = SU.toJDBCDate(newDate);
  }
/************************************************************************************************/
  public String getUserEndDate()
  {
    if (UserEndDate != null)
    {
      return SU.reformatDate(UserEndDate.toString());
    }
    else
    {
      return null;
    }
  }
/************************************************************************************************/
  public int getUserEndDateDays()
  {
    return UEDays;
  }
/************************************************************************************************/
  public void setUserEndDateDays(String value)
  {
    UEDays = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getUserEndDateMonths()
  {
    return UEMonths;
  }
/************************************************************************************************/
  public void setUserEndDateMonths(String value)
  {
    UEMonths = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public int getUserEndDateYears()
  {
    return UEYears;
  }
/************************************************************************************************/
  public void setUserEndDateYears(String value)
  {
    UEYears = value.equals("")?0:Integer.parseInt(value);
  }
/************************************************************************************************/
  public String getNostroUserId()
  {
    return Nostro_User_Id;
  }
/************************************************************************************************/
  public void setNostroUserId(String value)
  {
    Nostro_User_Id = value;
  }
/************************************************************************************************/
  public String getUserName()
  {
    return User_Name;
  }
/************************************************************************************************/
  public void setUserName(String value)
  {
    User_Name = value;
  }
/************************************************************************************************/
  public String getUserSurname()
  {
    return User_Surname;
  }
/************************************************************************************************/
  public void setUserSurname(String value)
  {
    User_Surname = value;
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

	if ((ButtonPressed.startsWith("Submit") ||
          (ButtonPressed.startsWith("Save Draft"))) &&
          (Mode.compareTo("Cease") !=0))
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
	else
	  return true;
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
    cal.setTime(UserStartDate==null?(new java.util.Date())
      :UserStartDate);
    USDays=cal.get(cal.DATE);
    USMonths=cal.get(cal.MONTH)+1;
    USYears=cal.get(cal.YEAR);
    if (UserEndDate==null)
    {
      UEDays=0;
      UEMonths=0;
      UEYears=0;
    }
    else
    {
      cal.setTime(UserEndDate);
      UEDays=cal.get(cal.DATE);
      UEMonths=cal.get(cal.MONTH)+1;
      UEYears=cal.get(cal.YEAR);
    }
  }
/************************************************************************************************/
  public void assignNewUserId()
  {
    final String FIVE_ZEROS = "00000";
    try{
      SQL = "SELECT MAX(User_Id) from eban..Nostro_User_BAN " +
       "WHERE Global_Customer_Id = '" + Global_Customer_Id + "' ";
      if (DBA.Connect(PREPARE,P5))
      {

        Stmt = DBA.Conn.createStatement();

        Stmt.execute(SQL);
        RS = Stmt.getResultSet();
        if (RS.next())
        {
          String lastUserId = RS.getString(1);
          if (lastUserId == null)
          {
            Nostro_User_Id = Global_Customer_Id + "000001";
          }
          else
          {
            String numericComponent =
              lastUserId.substring(lastUserId.indexOf(Global_Customer_Id) +
              Global_Customer_Id.length());
            int idNumber = Integer.parseInt(numericComponent);
            idNumber++;
            numericComponent = Integer.toString(idNumber);
            Nostro_User_Id = Global_Customer_Id +
              FIVE_ZEROS.substring(numericComponent.length() -1) +
              numericComponent;
          }
        }
        else
        {
          Nostro_User_Id = Global_Customer_Id + "000001";
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
    }
  }
}