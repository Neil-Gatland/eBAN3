package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collection;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import JavaUtil.RSAccountDescriptor;
import JavaUtil.RSProductDescriptor;
import JavaUtil.RSRatingSchemeDescriptor;
import JavaUtil.RSThresholdDescriptor;

public class MasterAccountBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long masterAccountId;
    private long providerId;
    private String providerName;
    private String masterAccountNumber;
    private String masterAccountName;
    private String frequencyCode;
    private String origFrequencyCode;
    private String autoPrintInd;
    private String presentationInd;
    private String signName;
    private String deletedInd;
    private String productGroupList;
    private String managedMasterAccount;
    private String masterAccountChannel;

  public MasterAccountBean ()
  {
    mandatory.clear();
    mandatory.addElement("masterAccountNumber");
    mandatory.addElement("masterAccountName");
    mandatory.addElement("frequencyCode");
    //mandatory.addElement("autoPrintInd");
    mandatory.addElement("presentationInd");
    //mandatory.addElement("signName");
    //mandatory.addElement("deletedInd");
    mandatory.addElement("managedMasterAccount");
    mandatory.addElement("masterAccountChannel");
    errored.addElement("");
    masterAccountId = -1;
    providerId = -1;
    providerName = "";
    masterAccountNumber = "";
    masterAccountName = "";
    frequencyCode = "";
    autoPrintInd = "";
    presentationInd = "";
    signName = "";
    deletedInd = "";
    productGroupList = "";
    origFrequencyCode = "";
    masterAccountChannel = "";
    managedMasterAccount = "";
  }

  public void Reset()
  {
    super.Reset();
    errored.clear();
    masterAccountId = -1;
    providerId = -1;
    providerName = "";
    masterAccountNumber = "";
    masterAccountName = "";
    frequencyCode = "";
    autoPrintInd = "";
    presentationInd = "";
    signName = "";
    deletedInd = "";
    productGroupList = "";
    origFrequencyCode = "";
    masterAccountChannel = "";
    managedMasterAccount = "";
  }
/*set Methods*/
  public void setManagedMasterAccount (String value)
  {
    ScreenData.put("managedMasterAccount",value);
    managedMasterAccount = SU.isNull(value,"");
  }
  public void setMasterAccountChannel (String value)
  {
    ScreenData.put("masterAccountChannel",value);
    masterAccountChannel = SU.isNull(value,"");
  }
  public void setMasterAccountId (String value)
  {
    masterAccountId  = Long.parseLong(value);
  }
  public void setProviderId (String value)
  {
    providerId  = Long.parseLong(value);
  }
  public void setMasterAccountNumber(String value)
  {
    ScreenData.put("masterAccountNumber",value);
    masterAccountNumber = value;
  }
  public void setMasterAccountName(String value)
  {
    ScreenData.put("masterAccountName",value);
    masterAccountName = value;
  }
  public void setFrequencyCode(String value)
  {
    ScreenData.put("frequencyCode",value);
    frequencyCode = SU.isNull(value,"");
  }
  public void setAutoPrintInd(String value)
  {
    ScreenData.put("autoPrintInd",value);
    autoPrintInd = SU.isNull(value,"");
  }
  public void setPresentationInd(String value)
  {
    ScreenData.put("presentationInd",value);
    presentationInd = SU.isNull(value,"");
  }
  public void setSignName(String value)
  {
    ScreenData.put("signName",value);
    signName = SU.isNull(value,"");
  }
  public void setDeletedInd(String value)
  {
    ScreenData.put("deletedInd",value);
    deletedInd = SU.isNull(value,"");
  }
  public void setProductGroupList(String value)
  {
    productGroupList = SU.isNull(value,"");
  }
  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("masterAccountNumber"))
    {
      setMasterAccountNumber(value);
    }
    else if (name.equals("masterAccountName"))
    {
      setMasterAccountName(value);
    }
    else if (name.equals("masterAccountChannel"))
    {
      setMasterAccountChannel(value);
    }
    else if (name.equals("managedMasterAccount"))
    {
      setManagedMasterAccount(value);
    }
    else if (name.equals("frequencyCode"))
    {
      setFrequencyCode(value);
    }
    else if (name.equals("autoPrintInd"))
    {
      setAutoPrintInd(value);
    }
    else if (name.equals("presentationInd"))
    {
      setPresentationInd(value);
    }
    else if (name.equals("signName"))
    {
      setSignName(value);
    }
    else if (name.equals("deletedInd"))
    {
      setDeletedInd(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getManagedMasterAccount ()
  {
    return managedMasterAccount;
  }
  public String getMasterAccountChannel ()
  {
    return masterAccountChannel;
  }
  public String getMasterAccountId ()
  {
    return Long.toString(masterAccountId);
  }
  public String getProviderId ()
  {
    return Long.toString(providerId);
  }
  public String getProviderName()
  {
    return providerName;
  }
  public String getMasterAccountNumber()
  {
    return masterAccountNumber;
  }
  public String getMasterAccountName()
  {
    return masterAccountName;
  }
  public String getFrequencyCode()
  {
    return frequencyCode;
  }
  public String getAutoPrintInd()
  {
    return autoPrintInd;
  }
  public String getPresentationInd()
  {
    return presentationInd;
  }
  public String getSignName()
  {
    return signName;
  }
  public String getDeletedInd()
  {
    return deletedInd ;
  }
  public String getProductGroupList()
  {
    return productGroupList;
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
      ((Mode.equals("Amend")) && (FieldName.equals("masterAccountNumber"))))
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
 public boolean getMasterAccountFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Master_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          providerId = RS.getLong(1);
          setMasterAccountNumber(RS.getString(2));
          masterAccountName = RS.getString(3);
          frequencyCode = RS.getString(4);
          origFrequencyCode = frequencyCode;
          //autoPrintInd = RS.getString(5);
          presentationInd = RS.getString(6);
          signName = RS.getString(7);
          managedMasterAccount = RS.getString(11);
          masterAccountChannel = RS.getString(12);
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
 private boolean masterAccountExists()
 {
    boolean exists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Master_Account_Number(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,masterAccountNumber);
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
/*******************************************************************************/
public boolean updateMasterAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Master_Account(?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,masterAccountId);
      cstmt.setLong(2,providerId);
      cstmt.setString(3,masterAccountNumber);
      cstmt.setString(4,masterAccountName);
      cstmt.setString(5,frequencyCode);
      //cstmt.setString(6,autoPrintInd);
      cstmt.setString(6,presentationInd);
      cstmt.setString(7,signName);
      //cstmt.setString(9,deletedInd);
      cstmt.setString(8,userId);
      cstmt.setString(9,managedMasterAccount);
      cstmt.setString(10,masterAccountChannel);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Master Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Master Account " +
              masterAccountName + "/" + masterAccountNumber + " (" + ret +
              ") updated.</b></font>";
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
public boolean updateRatingSchemeApproved(String productGroup,
  String ratingSchemeApproved)
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Set_Rating_Scheme_Approved(?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,masterAccountId);
      cstmt.setString(2,productGroup);
      cstmt.setString(3,ratingSchemeApproved);
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
            Message = "<font color=red><b>Unable to update Rating Scheme Approved</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Rating Scheme Approved updated.</b></font>";
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
public boolean deleteMasterAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Delete_Master_Account(?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,masterAccountId);
      cstmt.setString(2,userId);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to delete Master Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Master Account " +
              masterAccountName + "/" + masterAccountNumber + " (" + ret +
              ") deleted.</b></font>";
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
public boolean createMasterAccount()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Master_Account(?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,providerId);
      cstmt.setString(2,masterAccountNumber);
      cstmt.setString(3,masterAccountName);
      cstmt.setString(4,frequencyCode);
      //cstmt.setString(5,autoPrintInd);
      cstmt.setString(5,presentationInd);
      cstmt.setString(6,signName);
      //cstmt.setString(8,deletedInd);
      cstmt.setString(7,userId);
      cstmt.setString(8,managedMasterAccount);
      cstmt.setString(9,masterAccountChannel);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          long ret = RS.getLong(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new Master Account</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New Master Account " +
              masterAccountName + "/" + masterAccountNumber + " (" + ret +
              ") created.</b></font>";
            masterAccountId = ret;
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
        if (masterAccountExists())
        {
          setErrored("masterAccountNumber");
          Message = "<font color=red><b>This Master Account Number exists already.";
          return false;
        }
        else if (masterAccountNumber.length() != 7)
        {
          setErrored("masterAccountNumber");
          Message = "<font color=red><b>Master Account Number must have seven digits.";
          return false;
        }
        else if (masterAccountNumber.indexOf(" ") != -1)
        {
          setErrored("masterAccountNumber");
          Message = "<font color=red><b>Master Account Number cannot contain spaces.";
          return false;
        }
        else
        {
          try
          {
            Long.parseLong(masterAccountNumber);
          }
          catch (NumberFormatException nfe)
          {
            setErrored("masterAccountNumber");
            Message = "<font color=red><b>Master Account Number must be numeric.";
            return false;
          }
        }
      }
      /*else if ((Mode.equals("Create")) && (deletedInd.equals("Y")))
      {
        setErrored("deletedInd");
        Message = "<font color=red><b>Master Account cannot be created with deleted ind set to 'Y'.";
        return false;
      }
      else if ((Mode.equals("Amend")) && (!canDelete()))
      {
        setErrored("deletedInd");
        Message = "<font color=red><b>Master Account cannot be deleted until all of its accounts have been deleted";
        return false;
      }*/
      else if ((Mode.equals("Amend")) && (!frequencyChangeAllowed()))
      {
        setErrored("frequencyCode");
        return false;
      }
      return true;
    }
  }
/*******************************************************************************/
 public boolean getProviderFromDB()
 {
    boolean getFromDB = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Provider(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,providerId);
        cstmt.execute();
        RS = cstmt.getResultSet();

        if (RS.next())
        {
          providerName = RS.getString(1);
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
 public boolean canDelete()
 {
    boolean canDelete = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Active_Account_For_Master(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
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
/*******************************************************************************/
 public boolean frequencyChangeAllowed()
 {
    boolean frequencyChangeAllowed = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        if (origFrequencyCode.equals(frequencyCode))
        {
          frequencyChangeAllowed = true;
        }
        else
        {
          SQL = "{call RevShare..Check_Master_Account_Frequency_Change(?,?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,origFrequencyCode);
          cstmt.setString(2,frequencyCode);
          cstmt.execute();
          RS = cstmt.getResultSet();
          if (RS.next())
          {
            String ret = RS.getString(1);
            if (ret.equals("ok"))
            {
              frequencyChangeAllowed = true;
            }
            else
            {
              Message = ret;
            }
          }
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
      return frequencyChangeAllowed;
    }
 }
/*******************************************************************************/
  public String populateProductGroupList()
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

        SQL = "{call RevShare..Get_Master_Account_Product_Group_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);

	columns=2;

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
            String thisProductGroup = RS.getString(counter);
            boolean approved = RS.getString(++counter).equals("Y");
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            if (approved)
            {
              grid.append("<input class=listbutton type=button value=\"N\" " +
                "onClick=\"sendSelected('" + thisProductGroup + "','N')\">");
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\"Y\" " +
                "onClick=\"sendSelected('" + thisProductGroup + "','Y')\">");
            }
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Products for this Product Group</b></font>";
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
        setProductGroupList(grid.toString());
        return grid.toString();
      }
      else
      {
        setProductGroupList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String getApprovedProductGroups()
  {
    StringBuffer sb = new StringBuffer("");

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Master_Account_Approved_Product_Groups(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            sb.append(RS.getString(1) + ", ");
          }
          sb.setLength(sb.length()-2);
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
      return sb.toString();
    }
  }
/*******************************************************************************/
  public Collection getAppliedRatingSchemes(String latestCallMonth, String accountNo)
  {
    ArrayList al = new ArrayList();

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Rating_Schemes_For_Account_Master(?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);
        cstmt.setString(2,masterAccountNumber);
        cstmt.setString(3,accountNo);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            al.add(new RSRatingSchemeDescriptor(RS.getString(1), RS.getString(2),
              RS.getString(3), RS.getString(4), RS.getString(5), RS.getString(6),
              RS.getString(7), RS.getString(8), RS.getString(9)));
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
      return al;
    }
  }
/*******************************************************************************/
  public Collection getDefaultRatingScheme(String latestCallMonth)
  {
    ArrayList al = new ArrayList();

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Default_Rating_Scheme(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);
        cstmt.setString(2,signName);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            al.add(new RSRatingSchemeDescriptor(RS.getString(1), RS.getString(2),
              RS.getString(3), RS.getString(4), RS.getString(5), RS.getString(6),
              RS.getString(7), RS.getString(8), RS.getString(9)));
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
      return al;
    }
  }
/*******************************************************************************/
  public Collection getAccounts()
  {
    ArrayList al = new ArrayList();

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Accounts_For_Master_Account(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            al.add(new RSAccountDescriptor(RS.getLong(1), RS.getString(2),
              RS.getString(3), RS.getString(4)));
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
      return al;
    }
  }
/*******************************************************************************/
  public Collection getAccountProducts(long accountId)
  {
    ArrayList al = new ArrayList();

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Account_Products(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,accountId);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            al.add(new RSProductDescriptor(RS.getString(1), RS.getString(2),
              RS.getString(3)));
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
      return al;
    }
  }
/*******************************************************************************/
  public Collection getThresholds()
  {
    ArrayList al = new ArrayList();

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Master_Account_Thresholds(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,masterAccountId);
        cstmt.execute();
	RS = cstmt.getResultSet();
	try
	{
          while (RS.next())
          {
            al.add(new RSThresholdDescriptor(RS.getString(1), RS.getString(2),
              RS.getString(3), RS.getString(4)));
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
      return al;
    }
  }
/***********************************************************************************/
  public String getListBox(String Table,String Mode,String FirstEntry,
    String QualifierValue, int listBoxSize, String listBoxStyle,
    boolean blankLine)
  {
    DBA.selectStyle = listBoxStyle;
    DBA.selectSize = listBoxSize;
    DBA.addBlankLine = blankLine;
    setListBoxMetaData(Table, QualifierValue);
    String ret = DBA.getListBox(Table, Mode, FirstEntry, QualifierValue);
    DBA.selectSize = 1;
    DBA.selectStyle = "";
    DBA.addBlankLine = true;
    return ret;
  }
/***********************************************************************************/
  private void setListBoxMetaData(String TableName,String QualifierValue)
  {
    if (TableName.equals("masterAccountChannel"))
    {
      DBA.DB = "RevShare";
      DBA.ListDataSource = "";
      DBA.Name = "Channel + ' - ' + Channel_Description";
      DBA.Value = "Channel";
      DBA.Qualifier = "";
      DBA.OrderBy = "1";
      DBA.DBTable = "Channel";
      DBA.Alert = "";
    }
  }
}

