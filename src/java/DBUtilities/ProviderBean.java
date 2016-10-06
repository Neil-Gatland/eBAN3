package DBUtilities;

import java.sql.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class ProviderBean extends BANBean
{

    private String SQL;
    private Hashtable ScreenData=new Hashtable();
    protected Vector mandatory=new Vector();
    private long providerId;
    private String providerName;
    private String npid;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String addressLine6;
    private String bankDetailsReturned;
    private String bankSortCode1;
    private String bankSortCode2;
    private String bankSortCode3;
    private String bankAccountNumber;
    private String vatExemptInd;
    private String vatNumber;
    private String sapVendorNumber;
    private String selfBillAgreement;
    private String customerContract;
    private String sapUpload;

  public ProviderBean ()
  {
    mandatory.clear();
    mandatory.addElement("providerName");
    mandatory.addElement("addressLine1");
    mandatory.addElement("bankDetailsReturned");
    mandatory.addElement("vatExemptInd");
    mandatory.addElement("selfBillAgreement");
    mandatory.addElement("customerContract");
    mandatory.addElement("sapUpload");
    errored.addElement("");
    providerId = -1;
    providerName = "";
    npid = "";
    addressLine1 = "";
    addressLine2 = "";
    addressLine3 = "";
    addressLine4 = "";
    addressLine5 = "";
    addressLine6 = "";
    bankDetailsReturned = "";
    bankSortCode1 = "";
    bankSortCode2 = "";
    bankSortCode3 = "";
    bankAccountNumber = "";
    vatExemptInd = "";
    vatNumber = "";
    sapVendorNumber = "";
    selfBillAgreement = "";
    customerContract = "";
    sapUpload = "Y";
  }

  public void Reset()
  {
    super.Reset();
    errored.clear();
    providerId = -1;
    providerName = "";
    npid = "";
    addressLine1 = "";
    addressLine2 = "";
    addressLine3 = "";
    addressLine4 = "";
    addressLine5 = "";
    addressLine6 = "";
    bankDetailsReturned = "";
    bankSortCode1 = "";
    bankSortCode2 = "";
    bankSortCode3 = "";
    bankAccountNumber = "";
    vatExemptInd = "";
    vatNumber = "";
    sapVendorNumber = "";
    selfBillAgreement = "";
    customerContract = "";
    sapUpload = "Y";
  }
/*set Methods*/
  public void setProviderId(String value)
  {
    providerId = Long.parseLong(value);
  }
  public void setProviderName(String value)
  {
    ScreenData.put("providerName",value);
    providerName = value;
  }
  public void setNPID(String value)
  {
    ScreenData.put("npid",value);
    npid = value;
  }
  public void setAddressLine1(String value)
  {
    ScreenData.put("addressLine1",value);
    addressLine1 = value;
  }
  public void setAddressLine2(String value)
  {
    ScreenData.put("addressLine2",value);
    addressLine2 = value;
  }
  public void setAddressLine3(String value)
  {
    ScreenData.put("addressLine3",value);
    addressLine3 = value;
  }
  public void setAddressLine4(String value)
  {
    ScreenData.put("addressLine4",value);
    addressLine4 = value;
  }
  public void setAddressLine5(String value)
  {
    ScreenData.put("addressLine5",value);
    addressLine5 = value;
  }
  public void setAddressLine6(String value)
  {
    ScreenData.put("addressLine6",value);
    addressLine6 = value;
  }
  public void setBankDetailsReturned(String value)
  {
    ScreenData.put("bankDetailsReturned",value);
    if (!bankDetailsReturned.equals(value))
    {
      if (SU.isNull(value,"").equals("Y"))
      {
        mandatory.addElement("bankSortCode");
        mandatory.addElement("bankAccountNumber");
      }
      else
      {
        mandatory.removeElement("bankSortCode");
        mandatory.removeElement("bankAccountNumber");
      }
    }
    bankDetailsReturned = value;
}
  public void setBankSortCode1(String value)
  {
    ScreenData.put("bankSortCode1",value);
    bankSortCode1 = value;
  }
  public void setBankSortCode2(String value)
  {
    ScreenData.put("bankSortCode2",value);
    bankSortCode2 = value;
  }
  public void setBankSortCode3(String value)
  {
    ScreenData.put("bankSortCode3",value);
    bankSortCode3 = value;
  }
  public void setBankAccountNumber(String value)
  {
    ScreenData.put("bankAccountNumber",value);
    bankAccountNumber = value;
  }
  public void setVATExemptInd(String value)
  {
    ScreenData.put("vatExemptInd",value);
    vatExemptInd = value;
  }
  public void setVATNumber(String value)
  {
    ScreenData.put("vatNumber",value);
    vatNumber = value;
  }
  public void setSAPVendorNumber(String value)
  {
    ScreenData.put("sapVendorNumber",value);
    sapVendorNumber = value;
  }
  public void setSAPUpload(String value)
  {
    ScreenData.put("sapUpload",value);
    sapUpload = value;
  }
  public void setSelfBillAgreement(String value)
  {
    ScreenData.put("selfBillAgreement",value);
    selfBillAgreement = value;
  }
  public void setCustomerContract(String value)
  {
    ScreenData.put("customerContract",value);
    customerContract = value;
  }

  /*****************************************************************/
  public void setParameter(String name,String value)
  {
    if (name.equals("npid"))
    {
      setNPID(value);
    }
    else if (name.equals("providerName"))
    {
      setProviderName(value);
    }
    else if (name.equals("addressLine1"))
    {
      setAddressLine1(value);
    }
    else if (name.equals("addressLine2"))
    {
      setAddressLine2(value);
    }
    else if (name.equals("addressLine3"))
    {
      setAddressLine3(value);
    }
    else if (name.equals("addressLine4"))
    {
      setAddressLine4(value);
    }
    else if (name.equals("addressLine5"))
    {
      setAddressLine5(value);
    }
    else if (name.equals("addressLine6"))
    {
      setAddressLine6(value);
    }
    else if (name.equals("bankDetailsReturned"))
    {
      setBankDetailsReturned(value);
    }
    else if (name.equals("bankSortCode1"))
    {
      setBankSortCode1(value);
    }
    else if (name.equals("bankSortCode2"))
    {
      setBankSortCode2(value);
    }
    else if (name.equals("bankSortCode3"))
    {
      setBankSortCode3(value);
    }
    else if (name.equals("bankAccountNumber"))
    {
      setBankAccountNumber(value);
    }
    else if (name.equals("vatExemptInd"))
    {
      setVATExemptInd(value);
    }
    else if (name.equals("vatNumber"))
    {
      setVATNumber(value);
    }
    else if (name.equals("sapVendorNumber"))
    {
      setSAPVendorNumber(value);
    }
    else if (name.equals("sapUpload"))
    {
      setSAPUpload(value);
    }
    else if (name.equals("selfBillAgreement"))
    {
      setSelfBillAgreement(value);
    }
    else if (name.equals("customerContract"))
    {
      setCustomerContract(value);
    }
  }
/*******************************************************************************/
/*get Methods*/
  public String getProviderId()
  {
    return Long.toString(providerId);
  }
  public String getProviderName()
  {
    return providerName;
  }
  public String getNPID()
  {
    return npid;
  }
  public String getAddressLine1()
  {
    return addressLine1;
  }
  public String getAddressLine2()
  {
    return addressLine2;
  }
  public String getAddressLine3()
  {
    return addressLine3;
  }
  public String getAddressLine4()
  {
    return addressLine4;
  }
  public String getAddressLine5()
  {
    return addressLine5;
  }
  public String getAddressLine6()
  {
    return addressLine6;
  }
  public String getBankDetailsReturned()
  {
    return bankDetailsReturned;
  }
  public String getBankSortCode1()
  {
    return bankSortCode1;
  }
  public String getBankSortCode2()
  {
    return bankSortCode2;
  }
  public String getBankSortCode3()
  {
    return bankSortCode3;
  }
  public String getBankAccountNumber()
  {
    return bankAccountNumber;
  }
  public String getVATExemptInd()
  {
    return vatExemptInd;
  }
  public String getVATNumber()
  {
    return vatNumber;
  }
  public String getSAPVendorNumber()
  {
    return sapVendorNumber;
  }
  public String getSAPUpload()
  {
    return sapUpload;
  }
  public String getSelfBillAgreement()
  {
    return selfBillAgreement;
  }
  public String getCustomerContract()
  {
    return customerContract;
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
      ((Mode.equals("Amend")) && (FieldName.equals("providerCode"))))
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
          npid = RS.getString(2);
          addressLine1 = RS.getString(3);
          addressLine2 = RS.getString(4);
          addressLine3 = RS.getString(5);
          addressLine4 = RS.getString(6);
          addressLine5 = RS.getString(7);
          addressLine6 = RS.getString(8);
          setBankDetailsReturned(RS.getString(9));
          bankSortCode1 = RS.getString(10);
          bankSortCode2 = RS.getString(11);
          bankSortCode3 = RS.getString(12);
          bankAccountNumber = RS.getString(13);
          vatExemptInd = RS.getString(14);
          vatNumber = RS.getString(15);
          sapVendorNumber = RS.getString(16);
          selfBillAgreement = RS.getString(17);
          customerContract = RS.getString(18);
          sapVendorNumber = RS.getString(16);
          sapUpload = RS.getString(21);
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
  public boolean deleteProvider()
  {
    boolean success = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Delete_Provider(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,providerId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          if (RS.getInt(1) == 0)
          {
            success = true;
            Message = "<font color=blue><b>Provider deleted.</b></font>";
          }
          else
          {
            success = false;
            Message = "<font color=red><b>Unable to delete Provider.</b></font>";
          }
        }
      }
      else
      {
        success = false;
        Message = "<font color=red><b>Unable to delete Provider.</b></font>";
      }
    }
    catch(Exception ex)
    {
      Message="<font color=red><b>"+ex.getMessage();
    }//message set in underlying code
    finally
    {
      close();
      return success;
    }
  }
/*******************************************************************************/
public boolean updateProvider()
{
  boolean ok = false;
  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Update_Provider(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setLong(1,providerId);
      cstmt.setString(2,providerName);
      cstmt.setString(3,npid);
      cstmt.setString(4,addressLine1);
      cstmt.setString(5,addressLine2);
      cstmt.setString(6,addressLine3);
      cstmt.setString(7,addressLine4);
      cstmt.setString(8,addressLine5);
      cstmt.setString(9,addressLine6);
      cstmt.setString(10,bankDetailsReturned);
      if (bankSortCode1.equals(""))
      {
        cstmt.setString(11,null);
      }
      else
      {
        cstmt.setString(11,bankSortCode1 + "-" + bankSortCode2 + "-" + bankSortCode3);
      }
      cstmt.setString(12,bankAccountNumber);
      cstmt.setString(13,vatExemptInd);
      cstmt.setString(14,vatNumber);
      cstmt.setString(15,sapVendorNumber);
      cstmt.setString(16,selfBillAgreement);
      cstmt.setString(17,customerContract);
      cstmt.setString(18,userId);
      cstmt.setString(19,sapUpload);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          ok = RS.getInt(1)==0;
          if (ok)
          {
            Message = "<font color=blue><b>Provider " + providerName + " (" +
            providerId + ") updated</b></font>";
          }
          else
          {
            Message = "<font color=red><b>Unable to update Provider</b></font>";
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
public boolean createProvider()
{
  boolean ok = false;

  try{
    if (DBA.Connect(PREPARE,P5))
    {
      SQL = "{call RevShare..Create_Provider(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,providerName);
      cstmt.setString(2,npid);
      cstmt.setString(3,addressLine1);
      cstmt.setString(4,addressLine2);
      cstmt.setString(5,addressLine3);
      cstmt.setString(6,addressLine4);
      cstmt.setString(7,addressLine5);
      cstmt.setString(8,addressLine6);
      cstmt.setString(9,bankDetailsReturned);
      if (bankSortCode1.equals(""))
      {
        cstmt.setString(10,null);
      }
      else
      {
        cstmt.setString(10,bankSortCode1 + "-" + bankSortCode2 + "-" + bankSortCode3);
      }
      cstmt.setString(11,bankAccountNumber);
      cstmt.setString(12,vatExemptInd);
      cstmt.setString(13,vatNumber);
      cstmt.setString(14,sapVendorNumber);
      cstmt.setString(15,selfBillAgreement);
      cstmt.setString(16,customerContract);
      cstmt.setString(17,userId);
      cstmt.setString(18,sapUpload);
      cstmt.execute();
      RS = cstmt.getResultSet();
      try
      {
        if (RS.next())
        {
          long ret = RS.getLong(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to create new Provider</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>New Provider " + providerName +
              " (id: " + ret + ") created.</b></font>";
            providerId = ret;
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
  private boolean providerExists()
  {
    boolean exists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Provider(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,providerId);
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
/******************************************************************************/
  public boolean isValid(String ButtonPressed)
  {
    Enumeration FormFields=mandatory.elements();
    setErrored("clear");
    Message = "<font color=red><b>";
    String FormField="";
    String FieldName;
    String value="";

    if ((ButtonPressed.equals("Submit")) || (ButtonPressed.equals("Update")))
    {
      while (FormFields.hasMoreElements())
      {
        FieldName=(String)FormFields.nextElement();
        FormField=SU.isNull((String)ScreenData.get(FieldName),"");
        if (FieldName.equals("bankSortCode"))
        {
          for (int i =1; i < 4; i++)
          {
            FormField=SU.isNull((String)ScreenData.get(FieldName+i),"");
            if ((FormField.equals("")) || (SU.isSpaces(FormField)))
            {
              setErrored(FieldName);
            }
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
    else
    {
      if ((Mode.equals("Create")) && (providerExists()))
      {
        setErrored("providerCode");
        Message = "<font color=red><b>A Provider with this Provider Code exists already.";
        return false;
      }
      else if (!bankSortCode1.equals(""))
      {
        try
        {
          int temp = Integer.parseInt(bankSortCode1);
          temp = Integer.parseInt(bankSortCode2);
          temp = Integer.parseInt(bankSortCode3);
        }
        catch (NumberFormatException nfe)
        {
          setErrored("bankSortCode");
          Message = "<font color=red><b>Bank Sort Code must be numeric.";
          return false;
        }
        if ((bankSortCode1.length() != 2) || (bankSortCode2.length() != 2) ||
          (bankSortCode3.length() != 2))
        {
          setErrored("bankSortCode");
          Message = "<font color=red><b>Each element of the Bank Sort Code must be 2 digits.";
          return false;
        }
      }
      else if (!bankAccountNumber.equals(""))
      {
        try
        {
          int temp = Integer.parseInt(bankAccountNumber);
        }
        catch (NumberFormatException nfe)
        {
          setErrored("bankAccountNumber");
          Message = "<font color=red><b>Bank Account Number must be numeric.";
          return false;
        }
        if ((bankAccountNumber.length() != 8) && (bankAccountNumber.length() != 10))
        {
          setErrored("bankSortCode");
          Message = "<font color=red><b>Bank Account Number must be 8 or 10 digits.";
          return false;
        }
      }
      return true;
    }
  }
}

