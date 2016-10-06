package DBUtilities;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import JavaUtil.SuspendedDataDescriptor;

public class RevenueShareBean extends BANBean
{

    private String providerId;
    private String accountId;
    private String masterAccountId;
    private String productGroupId;
    private String productId;
    private String ratingSchemeId;
    private String ratingSchemeList;
    private String productCode;
    private String nameOrNumber;
    private String ratingType;
    private String ratingDuration;
    private String fromCallMonth;
    private String newFromCallMonth;
    private String toCallMonth;
    private String latestCallMonth;
    private String nextCallMonth;
    private String currentStatus;
    private String rqr09LoadTime;
    private String ratingTime;
    private String invoicingTime;
    private String ebillingUploadTime;
    private String processList;
    private String processMessageList;
    private long processSeqNo;
    private long nameProcessSeqNo;
    private String nameProcessStatus;
    private String nameProcessTime;
    private String nameProcessCompletedOK;
    private boolean canSubmit;
    private boolean canClose;
    private String signName;
    private String masterAccountNumber;
    private String accountNumber;
    private String productGroup;
    private boolean ratingSchemeFound;
    private String missingDataList;
    private String missingProvider;
    private String missingAccount;
    private String missingProduct;
    private String incorrectTrafficList;
    private String incorrectlyPosted;
    private String rerateRQR09;
    private String adjustmentInvoiceList;
    private String invoiceNumber;
    private String adjustmentStatus;
    private String suspendedDataList;
    private String suspendedType;
    private String suspendedAccountNumber;
    private String suspendedProduct;
    private String suspendedInd;
    private String suspendedWriteOff;
    private String suspendedDuration;
    private boolean suspendPopUp;
    private String minCallMonth;
    private boolean ratingSchemeClosed;
    private ArrayList downloadArray;
    private boolean refreshSuspendedGrid;
    private ArrayList suspendedList;
    private String suspendedSortOrder;
    private String accMoveHistList;
    private String unexpectedProductList;
    private String filterMonth;
    private String filterProvider;


  public RevenueShareBean ()
  {
    providerId = "";
    accountId = "";
    masterAccountId = "";
    productGroupId = "";
    productId = "";
    ratingSchemeId = "";
    ratingSchemeList = "";
    productCode = "";
    nameOrNumber = "";
    ratingType = "";
    ratingDuration = "";
    fromCallMonth = "";
    newFromCallMonth = "";
    toCallMonth = "";
    latestCallMonth = "";
    nextCallMonth = "";
    currentStatus = "";
    rqr09LoadTime = "";
    ratingTime = "";
    invoicingTime = "";
    ebillingUploadTime = "";
    processList = "";
    processMessageList = "";
    processSeqNo = 0;
    nameProcessSeqNo = 0;
    nameProcessStatus = "";
    nameProcessTime = "";
    nameProcessCompletedOK = "";
    canSubmit = false;
    canClose = false;
    signName = "";
    masterAccountNumber = "";
    accountNumber = "";
    productGroup = "";
    ratingSchemeFound = false;
    missingDataList = "";
    missingProvider = "";
    missingAccount = "";
    missingProduct = "";
    incorrectTrafficList = "";
    incorrectlyPosted = "";
    rerateRQR09 = "N";
    adjustmentInvoiceList = "";
    invoiceNumber = "";
    adjustmentStatus = "";
    suspendedDataList = "";
    suspendedType = "all";
    suspendedAccountNumber = "";
    suspendedProduct = "";
    suspendedInd = "";
    suspendedWriteOff = "";
    suspendedDuration = "";
    suspendPopUp = false;
    minCallMonth = "000000";
    ratingSchemeClosed = false;
    downloadArray = new ArrayList();
    refreshSuspendedGrid = true;
    suspendedList = new ArrayList();
    suspendedSortOrder = "1";
    accMoveHistList = "";
    unexpectedProductList = "";
    filterMonth = "All";
    filterProvider = "All";
  }

  public void Reset()
  {
    super.Reset();
    providerId = "";
    accountId = "";
    masterAccountId = "";
    productGroupId = "";
    productId = "";
    ratingSchemeId = "";
    ratingSchemeList = "";
    productCode = "";
    nameOrNumber = "";
    ratingType = "";
    ratingDuration = "";
    fromCallMonth = "";
    newFromCallMonth = "";
    toCallMonth = "";
    processList = "";
    processMessageList = "";
    processSeqNo = 0;
 /*   latestCallMonth = "";
    currentStatus = "";
    rqr09LoadTime = "";
    ratingTime = "";
    invoicingTime = "";
    ebillingUploadTime = "";*/
    nameProcessSeqNo = 0;
    nameProcessStatus = "";
    nameProcessTime = "";
    nameProcessCompletedOK = "";
    canSubmit = false;
    canClose = false;
    signName = "";
    masterAccountNumber = "";
    accountNumber = "";
    productGroup = "";
    ratingSchemeFound = false;
    missingDataList = "";
    missingProvider = "";
    missingAccount = "";
    missingProduct = "";
    incorrectTrafficList = "";
    incorrectlyPosted = "";
    rerateRQR09 = "N";
    adjustmentInvoiceList = "";
    invoiceNumber = "";
    adjustmentStatus = "";
    suspendedDataList = "";
    suspendedType = "all";
    suspendedAccountNumber = "";
    suspendedProduct = "";
    suspendedInd = "";
    suspendedWriteOff = "";
    suspendedDuration = "";
    suspendPopUp = false;
    minCallMonth = "000000";
    ratingSchemeClosed = false;
    downloadArray = new ArrayList();
    refreshSuspendedGrid = true;
    suspendedList = new ArrayList();
    suspendedSortOrder = "1";
    accMoveHistList = "";
    unexpectedProductList = "";
    filterMonth = "All";
    filterProvider = "All";
  }
/*set Methods*/
  public void setSuspendedSortOrder(String value)
  {
    suspendedSortOrder = SU.isNull(value,"");
  }
  public void setRefreshSuspendedGrid(boolean value)
  {
    refreshSuspendedGrid = value;
  }
  public void setProviderId(String value)
  {
    providerId = SU.isNull(value,"");
    if (providerId.equals(""))
    {
      setMasterAccountNumber("");
    }
  }
  public void setProductId(String value)
  {
    productId = SU.isNull(value,"");
  }
  public void setProductGroupId(String value)
  {
    productGroupId = SU.isNull(value,"");
  }
  public void setProductGroup(String value)
  {
    String pG = SU.isNull(value,"");
    if (!productGroup.equals(pG))
    {
      productCode = "";
    }
    productGroup = SU.isNull(value,"");
  }
  public void setMasterAccountId(String value)
  {
    masterAccountId = SU.isNull(value,"");
  }
  public void setAccountId(String value)
  {
    accountId = SU.isNull(value,"");
  }
  public void setFromCallMonth(String value)
  {
    fromCallMonth = SU.isNull(value,"");
  }
  public void setFilterMonth(String value)
  {
    filterMonth = SU.isNull(value,"");
  }
  public void setFilterProvider(String value)
  {
    filterProvider = SU.isNull(value,"");
  }
  public void setToCallMonth(String value)
  {
    toCallMonth = SU.isNull(value,"");
  }
  public void setProductCode(String value)
  {
    productCode = SU.isNull(value,"");
  }
  public void setRatingType(String value)
  {
    ratingType = SU.isNull(value,"");
  }
  public void setRatingDuration(String value)
  {
    ratingDuration = SU.isNull(value,"");
  }
  public void setSignName(String value)
  {
    signName = SU.isNull(value,"");
    if (!signName.equals(""))
    {
      nameOrNumber = signName;
      setProviderId("");
    }
  }
  public void setSignNameProviderIdAndMasterAccountNumber(String signNameIn,
    String providerIn, String masterAccountNumberIn, boolean initial)
  {
    String sNI = SU.isNull(signNameIn,"");
    String pI = SU.isNull(providerIn,"");
    String mANI = SU.isNull(masterAccountNumberIn,"");
    if (!signName.equals(sNI))
    {
      signName = sNI;
      nameOrNumber = signName;
      if (!signName.equals(""))
      {
        providerId = "";
        masterAccountNumber = "";
      }
    }
    else if (!providerId.equals(pI))
    {
      providerId = pI;
      nameOrNumber = "";
      masterAccountNumber = "";
      if (!providerId.equals(""))
      {
        signName = "";
        if (initial)
        {
          masterAccountNumber = mANI;
        }
      }
    }
    else if (!masterAccountNumber.equals(mANI))
    {
      masterAccountNumber = mANI;
      nameOrNumber = masterAccountNumber;
      if (!masterAccountNumber.equals(""))
      {
        signName = "";
      }
    }
  }
  public void setSignNameProviderIdAndAccountNumbers(String signNameIn,
    String providerIn, String masterAccountNumberIn, String accountNumberIn,
    boolean initial)
  {
    String sNI = SU.isNull(signNameIn,"");
    String pI = SU.isNull(providerIn,"");
    String mANI = SU.isNull(masterAccountNumberIn,"");
    String aNI = SU.isNull(accountNumberIn,"");
    if (!signName.equals(sNI))
    {
      signName = sNI;
      nameOrNumber = signName;
      if (!signName.equals(""))
      {
        providerId = "";
        masterAccountNumber = "";
        accountNumber = "";
      }
    }
    else if (!providerId.equals(pI))
    {
      providerId = pI;
      nameOrNumber = "";
      masterAccountNumber = "";
      accountNumber = "";
      if (!providerId.equals(""))
      {
        signName = "";
        if (initial)
        {
          masterAccountNumber = mANI;
          nameOrNumber = masterAccountNumber;
          accountNumber = aNI;
        }
      }
    }
    else if (!masterAccountNumber.equals(mANI))
    {
      masterAccountNumber = mANI;
      nameOrNumber = masterAccountNumber;
      if (!masterAccountNumber.equals(""))
      {
        signName = "";
        accountNumber = "";
      }
    }
    else if (!accountNumber.equals(aNI))
    {
      accountNumber = aNI;
      if (!accountNumber.equals(""))
      {
        nameOrNumber = "";
      }
      /*nameOrNumber = masterAccountNumber;
      if (!masterAccountNumber.equals(""))
      {
        signName = "";
      }*/
    }
  }
  public void setMasterAccountNumber(String value)
  {
    masterAccountNumber = SU.isNull(value,"");
    if (!masterAccountNumber.equals(""))
    {
      nameOrNumber = masterAccountNumber;
      signName = "";
    }
  }
  /*public void setRatingSchemeId(String value)
  {
    ratingSchemeId = SU.isNull(value,"");
    int pos1 = ratingSchemeId.indexOf("|");
    int pos2 = ratingSchemeId.indexOf("|", pos1+1);
    int pos3 = ratingSchemeId.indexOf("|", pos2+1);
    int pos4 = ratingSchemeId.lastIndexOf("|");
    if ((ratingSchemeId.equals("")) || (pos1 <= 0) || (pos2 <= 0) ||
      (pos3 <= 0) || (pos4 <= 0))
    {
      fromCallMonth = "";
      toCallMonth = "";
      productCode = "";
      nameOrNumber = "";
      ratingType = "";
      ratingDuration = "";
    }
    else
    {
      fromCallMonth = ratingSchemeId.substring(0, pos1);
      toCallMonth = ratingSchemeId.substring(++pos1, pos2);
      nameOrNumber = ratingSchemeId.substring(++pos2, pos3);
      productCode = ratingSchemeId.substring(++pos3, pos4);
      ratingType = ratingSchemeId.substring(++pos4);
      ratingDuration = "";
    }
  }*/
  public void setMissingDataList(String value)
  {
    missingDataList = SU.isNull(value,"");
  }
  public void setMissingProvider(String value)
  {
    missingProvider = SU.isNull(value,"");
  }
  public void setMissingAccount(String value)
  {
    missingAccount = SU.isNull(value,"");
  }
  public void setMissingProduct(String value)
  {
    missingProduct = SU.isNull(value,"");
  }
  public void setRatingSchemeList(String value)
  {
    ratingSchemeList = SU.isNull(value,"");
  }
  public void setNewFromCallMonth(String value)
  {
    newFromCallMonth = SU.isNull(value,"");
  }
  public void setProcessList(String value)
  {
    processList = SU.isNull(value,"");
  }
  public void setAccountMoveHistoryList(String value)
  {
    accMoveHistList = SU.isNull(value,"");
  }
  public void setProcessMessageList(String value)
  {
    processMessageList = SU.isNull(value,"");
  }
  public void setProcessSeqNo(long value)
  {
    processSeqNo = value;
  }
  public void setCanSubmit(boolean value)
  {
    canSubmit = value;
  }
  public void setCanClose(boolean value)
  {
    canClose = value;
  }
  public void setUnexpectedProductList(String value)
  {
    unexpectedProductList = SU.isNull(value,"");
  }
  public void setIncorrectTrafficList(String value)
  {
    incorrectTrafficList = SU.isNull(value,"");
  }
  public void setIncorrectlyPosted(String value)
  {
    incorrectlyPosted = SU.isNull(value,"");
  }
  public void setRerateRQR09(String value)
  {
    rerateRQR09 = SU.isNull(value,"");
  }
  public void setAdjustmentInvoiceList(String value)
  {
    adjustmentInvoiceList = SU.isNull(value,"");
  }
  public void setInvoiceNumber(String value)
  {
    invoiceNumber = SU.isNull(value,"");
  }
  public void setAdjustmentStatus(String value)
  {
    adjustmentStatus = SU.isNull(value,"");
  }
  public void setSuspendedDataList(String value)
  {
    suspendedDataList = SU.isNull(value,"");
  }
  public void setSuspendedType(String value)
  {
    suspendedType = SU.isNull(value,"");
  }
  public void setSuspendedAccountNumber(String value)
  {
    suspendedAccountNumber = SU.isNull(value,"");
  }
  public void setSuspendedProduct(String value)
  {
    suspendedProduct = SU.isNull(value,"");
  }
  public void setSuspendedInd(String value)
  {
    suspendedInd = SU.isNull(value,"");
  }
  public void setSuspendedWriteOff(String value)
  {
    suspendedWriteOff = SU.isNull(value,"");
  }
  public void setSuspendedDuration(String value)
  {
    suspendedDuration = SU.isNull(value,"");
  }
  public void setSuspendPopUp(boolean value)
  {
    suspendPopUp = value;
  }
  public void setMinCallMonth(String value)
  {
    minCallMonth = value;
  }
/*******************************************************************************/
/*get Methods*/
  public boolean getRefreshSuspendedGrid()
  {
    return refreshSuspendedGrid;
  }
  public String getMissingDataList()
  {
    return missingDataList;
  }
  public String getMissingProvider()
  {
    return missingProvider;
  }
  public String getMissingAccount()
  {
    return missingAccount;
  }
  public String getMissingProduct()
  {
    return missingProduct;
  }
  public String getProviderId()
  {
    return providerId;
  }
  public String getProductId()
  {
    return productId;
  }
  public String getProductGroupId()
  {
    return productGroupId;
  }
  public String getMasterAccountId()
  {
    return masterAccountId;
  }
  public String getAccountId()
  {
    return accountId;
  }
  public String getRatingSchemeId()
  {
    return ratingSchemeId;
  }
  public String getRatingSchemeList()
  {
    return ratingSchemeList;
  }
  public String getNewFromCallMonth()
  {
    return newFromCallMonth;
  }
  public String getFromCallMonth()
  {
    return fromCallMonth;
  }
  public String getFilterMonth()
  {
    return filterMonth;
  }
  public String getFilterProvider()
  {
    return filterProvider;
  }
  public String getToCallMonth()
  {
    return toCallMonth;
  }
  public String getNameOrNumber()
  {
    return nameOrNumber;
  }
  public String getProductCode()
  {
    return productCode;
  }
  public String getRatingType()
  {
    return ratingType;
  }
  public String getRatingDuration()
  {
    return ratingDuration;
  }
  public String getLatestCallMonth()
  {
    return latestCallMonth;
  }
  public String getNextCallMonth()
  {
    return nextCallMonth;
  }
  public String getCurrentStatus()
  {
    return currentStatus;
  }
  public Collection getDownloadArray()
  {
    return downloadArray;
  }
  public String getRQR09LoadTime()
  {
    return rqr09LoadTime;
  }
  public String getRatingTime()
  {
    return ratingTime;
  }
  public String getInvoicingTime()
  {
    return invoicingTime;
  }
  public String getEbillingUploadTime()
  {
    return ebillingUploadTime;
  }
  public String getProcessList()
  {
    return processList;
  }
  public String getAccountMoveHistoryList()
  {
    return accMoveHistList;
  }
  public String getProcessMessageList()
  {
    return processMessageList;
  }
  public long getProcessSeqNo()
  {
    return processSeqNo;
  }
  public long getNameProcessSeqNo()
  {
    return nameProcessSeqNo;
  }
  public String getNameProcessStatus()
  {
    return nameProcessStatus;
  }
  public String getNameProcessTime()
  {
    return nameProcessTime;
  }
  public String getNameProcessCompletedOK()
  {
    return nameProcessCompletedOK;
  }
  public boolean getCanSubmit()
  {
    return canSubmit;
  }
  public boolean getCanClose()
  {
    return canClose;
  }
  public boolean getRatingSchemeFound()
  {
    return ratingSchemeFound;
  }
  public String getProductGroup()
  {
    return productGroup;
  }
  public String getSignName()
  {
    return signName;
  }
  public String getMasterAccountNumber()
  {
    return masterAccountNumber;
  }
  public String getAccountNumber()
  {
    return accountNumber;
  }
  public String getIncorrectTrafficList()
  {
    return incorrectTrafficList;
  }
  public String getUnexpectedProductList()
  {
    return unexpectedProductList;
  }
  public String getIncorrectlyPosted()
  {
    return incorrectlyPosted;
  }
  public String getRerateRQR09()
  {
    return rerateRQR09;
  }
  public String getAdjustmentInvoiceList()
  {
    return adjustmentInvoiceList;
  }
  public String getInvoiceNumber()
  {
    return invoiceNumber;
  }
  public String getAdjustmentStatus()
  {
    return adjustmentStatus;
  }
  public String getSuspendedSortOrder()
  {
    return suspendedSortOrder;
  }
  public String getSuspendedDataList()
  {
    return suspendedDataList;
  }
  public String getSuspendedType()
  {
    return suspendedType;
  }
  public String getSuspendedAccountNumber()
  {
    return suspendedAccountNumber;
  }
  public String getSuspendedProduct()
  {
    return suspendedProduct;
  }
  public String getSuspendedInd()
  {
    return suspendedInd;
  }
  public String getSuspendedWriteOff()
  {
    return suspendedWriteOff;
  }
  public String getSuspendedDuration()
  {
    return suspendedDuration;
  }
  public boolean getRatingSchemeClosed()
  {
    return ratingSchemeClosed;
  }
  public boolean getSuspendPopUp()
  {
    return suspendPopUp;
  }
  public String getMinCallMonth()
  {
    return minCallMonth;
  }
/*******************************************************************************/
  public String populateRatingSchemeList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    ratingSchemeFound = false;
    ratingSchemeClosed = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Rating_Scheme_List(?,?,?,?,?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,nameOrNumber);
        cstmt.setString(4,ratingType);
        cstmt.setString(5,accountNumber);
        cstmt.setString(6,ratingDuration);

	columns=5;

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
            String thisRatingSchemeId = RS.getString(counter);
            toCallMonth = RS.getString(++counter);
            ratingSchemeClosed = RS.getString(++counter).equals("Y");
            boolean canUpdate = toCallMonth.equals("") && !ratingSchemeClosed &&
              (fromCallMonth.compareTo(latestCallMonth)>=0);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            if (canUpdate)
            {
              grid.append("<input class=listbutton type=button value=\"U\" " +
                "onClick=\"sendSelected('" + thisRatingSchemeId + "','RSUpdate')\">");
            }
            else
            {
              grid.append("<input class=listbutton type=button value=\"\" onClick=\"alert('This rating scheme cannot be updated.');\">");
            }
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Rating Schemes for this selection</b></font>";
            ratingSchemeFound = false;
            toCallMonth = "";
          }
          else
          {
            ratingSchemeFound = true;
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
        setRatingSchemeList(grid.toString());
        return grid.toString();
      }
      else
      {
        setRatingSchemeList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateMissingDataList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    ratingSchemeFound = false;
    downloadArray = new ArrayList();
    downloadArray.clear();
    downloadArray.add("<td width=200 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Missing Data Type</td><td width=400 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Missing Data Value</td>");

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Missing_Data_List()}";
	cstmt = DBA.Conn.prepareCall(SQL);

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
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<input class=listbutton type=button value=\"C\" " +
              "onClick=\"sendSelected('" + RS.getString(counter) + "','" + RS.getString(++counter) + "')\">");
            grid.append("</td></tr>");
            //add to array for downloading
            counter++;
            downloadArray.add(RS.getString(++counter));
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Missing Data Items</b></font>";
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
        setMissingDataList(grid.toString());
        return grid.toString();
      }
      else
      {
        setMissingDataList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateSuspendedDataList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    ratingSchemeFound = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Suspended_Data_List(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,suspendedType);
        cstmt.setString(2,suspendedSortOrder);
	columns=8;

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
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            String accountNo = RS.getString(counter);
            String prodCode = RS.getString(++counter);
            grid.append("<a name=\"" + accountNo + prodCode + "\"></a>");
            grid.append("<input class=listbutton type=button value=\"R\" " +
              "onClick=\"sendSelected('" + accountNo + "','"  +
              prodCode + "','"  + RS.getString(++counter) + "','"  +
              RS.getString(++counter) + "','" + RS.getString(++counter) + "')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Suspended Data Items</b></font>";
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
        setSuspendedDataList(grid.toString());
        return grid.toString();
      }
      else
      {
        setSuspendedDataList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public Collection populateSuspendedDataList2()
  {
    if (refreshSuspendedGrid)
    {
      suspendedList = new ArrayList();
      try{
        if (DBA.Connect(PREPARE,P5))
        {
          SQL = "{call RevShare..Get_Suspended_Data_List2(?)}";
          cstmt = DBA.Conn.prepareCall(SQL);
          cstmt.setString(1,suspendedType);


          cstmt.execute();
          RS = cstmt.getResultSet();
          try
          {
            while (RS.next())
            {
              suspendedList.add(new SuspendedDataDescriptor(RS.getString(1),
                RS.getString(2), RS.getString(3), RS.getString(4),
                RS.getString(5), RS.getString(6), RS.getString(7),
                RS.getString(8), RS.getString(9), RS.getString(10)));
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
      }
    }
    return suspendedList;
  }
/*******************************************************************************/
  public String populateProcessList()
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

        SQL = "{call RevShare..Get_Process_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);

	columns=5;

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
            String processSeqNo = RS.getString(counter);
            String processName = RS.getString(++counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<input class=listbutton type=button value=\"V\" " +
              "onClick=\"sendSelected('" + processSeqNo + "','" + processName +
              "','View')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Process items for this selection</b></font>";
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
        setProcessList(grid.toString());
        return grid.toString();
      }
      else
      {
        setProcessList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateTrafficList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    downloadArray = new ArrayList();
    downloadArray.clear();
    downloadArray.add("<td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Account Number</td><td width=250 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Account Name</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Network Source</td><td width=250 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Provider Name</td><td width=140 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "RQR09 Record Count</td><td width=110 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Incorrectly Posted</td>");

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;
	columns=6;

        SQL = "{call RevShare..Get_Incorrectly_Posted_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,incorrectlyPosted);
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
            String accountNo = RS.getString(counter);
            String networkSource = RS.getString(++counter);
            String newValue = RS.getString(++counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<a name=\"" + accountNo + networkSource + "\"></a>");
            grid.append("<input class=listbutton type=button value=\"R\" " +
              "onClick=\"sendSelected('" + accountNo + "','" + networkSource +
              "','" + newValue + "','Remove')\">");
            grid.append("</td></tr>");
            //add to array for downloading
            downloadArray.add(RS.getString(++counter));
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no items to view</b></font>";
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
        setIncorrectTrafficList(grid.toString());
        return grid.toString();
      }
      else
      {
        setIncorrectTrafficList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateUnexpectedProductList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    downloadArray = new ArrayList();
    downloadArray.clear();
    downloadArray.add("<td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Master Account Number</td><td width=250 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Master Account Name</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Account Number</td><td width=250 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Account Name</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Invoice Number</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Additional Invoice Number</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Suspended</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Invoiced</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Product Description</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Product Code</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "NTS</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Expected Product Code</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Expected Product Description</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Day Calls</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Evening Calls</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Weekend Calls</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Total Calls</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Day Minutes</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Evening Minutes</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Weekend Minutes</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Total Minutes</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Total Rated Amount</td>");

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;
	columns=9;

        SQL = "{call RevShare..Get_Unexpected_Product_NTS_List()}";
	cstmt = DBA.Conn.prepareCall(SQL);
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
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">&nbsp;</td></tr>");
            //add to array for downloading
            downloadArray.add(RS.getString(counter));
            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no items to view</b></font>";
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
        setUnexpectedProductList(grid.toString());
        return grid.toString();
      }
      else
      {
        setUnexpectedProductList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateAdjustmentInvoiceList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    if (!Mode.equals("Confirm"))
    {
      Message = "";
    }

    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;
	columns=9;

        SQL = "{call RevShare..Get_Adjustment_Invoice_List()}";
	cstmt = DBA.Conn.prepareCall(SQL);
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
            String invoiceNo = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">");
            grid.append("<input class=listbutton type=button value=\"U\" " +
              "onClick=\"sendSelected('" + invoiceNo + "','Update')\">");
            grid.append("<input class=listbutton type=button value=\"C\" " +
              "onClick=\"sendSelected('" + invoiceNo + "','Complete')\">");
            grid.append("<input class=listbutton type=button value=\"N\" " +
              "onClick=\"sendSelected('" + invoiceNo + "','NotReqd')\">");
            grid.append("</td></tr>");
            //End the table
          }
          grid.append("</table>");
          if ((rowcount == 0) && (!Mode.equals("Confirm")))
          {
            Message="<font color=blue><b>There are no items to view</b></font>";
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
        setAdjustmentInvoiceList(grid.toString());
        return grid.toString();
      }
      else
      {
        setAdjustmentInvoiceList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateProcessMessageList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    downloadArray = new ArrayList();
    downloadArray.clear();
    downloadArray.add("<td width=600 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Message</td><td width=150 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Message Time</td>");
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Process_Message_List(?,?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);
        cstmt.setLong(2,processSeqNo);

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
            //String processSeqNo = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>");
            //add to array for downloading
            downloadArray.add(RS.getString(++counter));

            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Process items for this selection</b></font>";
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
        setProcessMessageList(grid.toString());
        return grid.toString();
      }
      else
      {
        setProcessMessageList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public String populateAccountMoveHistoryList()
  {
    int counter=0;
    int rowcount;
    BigInteger rows = new BigInteger("0");
    String gridClass;
    StringBuffer grid=new StringBuffer("");
    Message="";
    downloadArray = new ArrayList();
    downloadArray.clear();
    downloadArray.add("<td width=200 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Old Master Account</td><td width=200 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Old Provider</td><td width=200 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "New Master Account</td><td width=200 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "New Provider</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Move Date</td><td width=100 style=\"background-color : #CCCCFF;FONT-SIZE: xx-small;\">" +
      "Moved By</td>");
    try{
      if (DBA.Connect(PREPARE,P5))
      {
	grid.append("<table border=0>");

	rowcount = 0;

        SQL = "{call RevShare..Get_Account_Move_History_List(?)}";
	cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,Long.parseLong(accountId));

	columns=6;

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
            //String processSeqNo = RS.getString(counter);
            grid.append("<td class=");
            grid.append(gridClass);
            grid.append(">&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>");
            //add to array for downloading
            downloadArray.add(RS.getString(+counter));

            //End the table
          }
          grid.append("</table>");
          if (rowcount == 0)
          {
            Message="<font color=blue><b>There are no Process items for this selection</b></font>";
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
        setAccountMoveHistoryList(grid.toString());
        return grid.toString();
      }
      else
      {
        setAccountMoveHistoryList("");
        return Message;
      }
    }
  }
/*******************************************************************************/
  public boolean callMonthExists()
  {
    boolean callMonthExists = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Rating_Scheme_For_Month(?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,newFromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,nameOrNumber);
        cstmt.setString(4,ratingType);
        cstmt.setString(5,accountNumber);
        cstmt.execute();
        RS = cstmt.getResultSet();
        callMonthExists = RS.next();
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
      return callMonthExists;
    }
  }
/*******************************************************************************/
 public boolean canMoveAccount()
 {
    boolean canMove = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Check_Account_Is_Master(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setLong(1,Long.parseLong(accountId));
        cstmt.execute();
        RS = cstmt.getResultSet();
        canMove = !RS.next();
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
      return canMove;
    }
 }
/*******************************************************************************/
  public boolean canAdvanceCallMonth()
  {
    boolean canAdvanceCallMonth = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Next_Call_Month(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        canAdvanceCallMonth = RS.next();
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
      return canAdvanceCallMonth;
    }
  }
/*******************************************************************************/
  public boolean getMinCallMonthFromDB()
  {
    boolean getMinCallMonth = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Min_Call_Month(?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,latestCallMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          minCallMonth = RS.getString(1);
          getMinCallMonth = true;
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
      return getMinCallMonth;
    }
  }
/*******************************************************************************/
  public boolean canCompleteInvoice(String imvNo)
  {
    boolean canCompleteInvoice = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Check_Invoice_Can_Complete(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,imvNo);
        cstmt.execute();
        RS = cstmt.getResultSet();
        canCompleteInvoice = RS.next();
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
      return canCompleteInvoice;
    }
  }
/*******************************************************************************/
  public boolean processRunning(String processName)
  {
    boolean processRunning = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Active_Process(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,processName);
        cstmt.execute();
        RS = cstmt.getResultSet();
        processRunning = RS.next();
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
      return processRunning;
    }
  }
/*******************************************************************************/
  public boolean getLatestProcessControl()
  {
    boolean getLatestProcessControl = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Process_Control_Latest()}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          latestCallMonth = RS.getString(1);
          currentStatus = RS.getString(2);
          rqr09LoadTime = RS.getString(3);
          ratingTime = RS.getString(4);
          invoicingTime = RS.getString(5);
          ebillingUploadTime = RS.getString(6);
          nextCallMonth = RS.getString(7);
          getLatestProcessControl = true;
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
      return getLatestProcessControl;
    }
  }
/*******************************************************************************/
  public boolean getLatestProcessRowByName(String processName)
  {
    boolean getLatestProcess = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Process_Latest_By_Name(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,processName);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          nameProcessSeqNo = RS.getLong(2);
          nameProcessTime = RS.getString(3);
          nameProcessStatus = RS.getString(4);
          nameProcessCompletedOK = RS.getString(5);
          getLatestProcess = true;
        }
        else
        {
          nameProcessSeqNo = 0;
          nameProcessTime = "";
          nameProcessStatus = "Not yet performed";
          nameProcessCompletedOK = "";
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
      return getLatestProcess;
    }
  }
/*******************************************************************************/
  public boolean getProcessCompletedByName(String processName)
  {
    boolean getProcessCompleted = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Get_Process_Completed_By_Name(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,processName);
        cstmt.execute();
        RS = cstmt.getResultSet();
        getProcessCompleted = RS.next();
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
      return getProcessCompleted;
    }
  }
/*******************************************************************************/
  public boolean createProcessRow(String processName, String processStatus,
    String completedOK, String rerateRQR09)
  {
    boolean ok = false;

    try{
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Create_Process(?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);
        cstmt.setString(2,processName);
        cstmt.setString(3,processStatus);
        cstmt.setString(4,completedOK);
        cstmt.setString(5,rerateRQR09);
        cstmt.setString(6,userId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        try
        {
          if (RS.next())
          {
            int ret = RS.getInt(1);
            if (ret < 0)
            {
              Message = "<font color=red><b>Unable to initiate process '" +
                processName + "'</b></font>";
            }
            else
            {
              Message = "<font color=blue><b>Process '" + processName +
              "' initiated</b></font>";
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
  public boolean advanceRatingSchemeCallMonth()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Advance_Rating_Scheme_Call_Month(?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,nameOrNumber);
        cstmt.setString(4,ratingType);
        cstmt.setString(5,newFromCallMonth);
        cstmt.setString(6,userId);
        cstmt.setString(7,accountNumber);
        cstmt.setString(8,ratingDuration);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to advance Rating Scheme From Call Month</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Rating Scheme From Call Month advanced to " +
              newFromCallMonth +
             (ret==0?"":(". Please ensure that the corresponding " +
              (ratingDuration.equals("S")?"Long":"Short") +
              " duration Rating Scheme is advanced or created.")) +
              "</b></font>";
            ok = true;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean closeRatingScheme()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Close_Rating_Scheme(?,?,?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,fromCallMonth);
        cstmt.setString(2,productCode);
        cstmt.setString(3,nameOrNumber);
        cstmt.setString(4,ratingType);
        cstmt.setString(5,newFromCallMonth);
        cstmt.setString(6,userId);
        cstmt.setString(7,accountNumber);
        cstmt.setString(8,ratingDuration);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to close Rating Scheme</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Rating Scheme closed</b></font>";
            ok = true;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean updateIncorrectlyPostedIndicator(String accountNumber,
    String networkSource, String incorrectlyPostedInd)
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Update_Incorrectly_Posted(?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,accountNumber);
        cstmt.setString(2,networkSource);
        cstmt.setString(3,incorrectlyPostedInd);
        cstmt.setString(4,userId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Incorrectly Posted</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Incorrectly Posted updated to " +
              incorrectlyPostedInd + " for " + accountNumber + " " +
              networkSource + "</b></font>";
            ok = true;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean updateRQR09SuspenseWrittenOff()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Update_RQR09_Suspense_Written_Off(?,?,?,?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,suspendedAccountNumber);
        cstmt.setString(2,suspendedProduct);
        cstmt.setString(3,suspendedInd);
        cstmt.setString(4,suspendedWriteOff);
        cstmt.setString(5,suspendedDuration);
        cstmt.setString(6,userId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret <= 0)
          {
            Message = "<font color=red><b>Unable to update Written Off Status</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Written Off Status updated to " +
              suspendedWriteOff + " for " + ret + " suspended call data item" +
              (ret>1?"s":"") + "</b></font>";
            ok = true;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean updateInvoiceAdjustmentStatus()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Update_Invoice_Adjustment_Status(?,?,?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,invoiceNumber);
        cstmt.setString(2,adjustmentStatus);
        cstmt.setString(3,userId);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to update Invoice Adjustment Status</b></font>";
          }
          else
          {
            Message = "<font color=blue><b>Invoice Adjustment Status updated to '" +
              adjustmentStatus + "' for " + invoiceNumber + "</b></font>";
            ok = true;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean canCloseMonth()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Check_Process_Completion_For_Month(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,latestCallMonth);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          int ret = RS.getInt(1);
          if (ret < 0)
          {
            Message = "<font color=red><b>Unable to determine process status for current Call Month</b></font>";
            canClose = false;
          }
          else if (ret == 1)
          {
            ok = true;
            canClose = true;
          }
          else if (ret == 2)
          {
            Message = "<font color=blue><b>Close processing has been initiated for the current Call Month</b></font>";
            canClose = false;
          }
          else
          {
            Message = "<font color=blue><b>Current Call Month cannot be closed at this time</b></font>";
            canClose = false;
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
      return ok;
    }
  }
/*******************************************************************************/
  public boolean findProviderIdForMissingAccount()
  {
    boolean ok = false;
    try
    {
      if (DBA.Connect(PREPARE,P5))
      {
        SQL = "{call RevShare..Find_Provider_For_RQR09_Account(?)}";
        cstmt = DBA.Conn.prepareCall(SQL);
        cstmt.setString(1,missingAccount);
        cstmt.execute();
        RS = cstmt.getResultSet();
        if (RS.next())
        {
          providerId = Long.toString(RS.getLong(1));
          ok = true;
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
/************************************************************************************************/
  private boolean canShowReport(String reportId)
  {
    return false;
  }
/************************************************************************************************/
  public ResultSet getReportRS(String reportId, String callMonthSelected,
    String reportName)
  {
    RS = null;
    try
    {
      SQL = "{call RevShare..Get_Report(?,?,?)}";
      cstmt = DBA.Conn.prepareCall(SQL);
      cstmt.setString(1,reportId);
      cstmt.setString(2,reportName);
      cstmt.setString(3,callMonthSelected);
      cstmt.execute();
      RS = cstmt.getResultSet();
    }
    catch(Exception ex)
    {
      close();
      Message=ex.getMessage();
    }
    finally
    {
      return RS;
    }
  }
/******************************************************************************/
  public void close ()
  {
    try
    {
      super.close();
    }
    catch (Exception ex)
    {
    }
  }
/******************************************************************************/
  public String getMode(String FieldName)
  {
    if (FieldName.equals("rerateRQR09"))
    {
      if ((canSubmit) && (!Mode.equals("Confirm")))
      {
        return InputMode;
      }
      else
      {
        return "READONLY";
      }
    }
    else
    {
      return super.getMode();
    }
  }
}

