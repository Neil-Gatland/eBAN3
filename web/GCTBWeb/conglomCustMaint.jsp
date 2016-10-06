<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="HTMLUtil.HTMLBean,JavaUtil.StringUtil"%>
<jsp:useBean id="ccBAN" class="DBUtilities.ConglomCustomerBANBean" scope="session"/>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%
    String BAN_Identifier;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
//Initialization
    //Get values from last submission
    BAN_Summary = ccBAN.getBAN_Summary();
    BAN_Reason = ccBAN.getBAN_Reason();
    Required_BAN_Effective_Date=SU.isNull(ccBAN.getRequired_BAN_Effective_Date(),"Today");
    Mode=ccBAN.getMode();
    Action=ccBAN.getAction();
    SelectMode=ccBAN.getSelectMode();
    InputMode=ccBAN.getInputMode();
    Calendar=ccBAN.getCalendar();
    BAN_Identifier=ccBAN.getBanIdentifier();
    ccBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = ccBAN.getBanStatus();
    String gcId = ccBAN.getGlobalCustomerId();

    String displayCustomer = BAN.getGlobalCustomerId() + " " +
      BAN.getConglomCustomerName() + " (" + BAN.getConglomCustomerId() + ")";
    String customerName = ccBAN.getCustomerName();
    String dob = ccBAN.getDob();
    String billingAddress[] = ccBAN.getBillingAddress();
    String billingRefPrefix = ccBAN.getBillingRefPrefix();
    String billingFrequency = ccBAN.getBillingFrequency();
    String zeroBal = ccBAN.getZeroBal();
    String vatExempt = ccBAN.getVATExempt();
    String invoiceTypeCode = ccBAN.getInvoiceTypeCode();
    String conMPR = ccBAN.getConMPR();
    String cwContactTitle = ccBAN.getCWContactTitle();
    String cwContactName = ccBAN.getCWContactName();
    String softBillReq = ccBAN.getSoftBillReq();
    String fao = ccBAN.getFAO();
    int bsDay = ccBAN.getBSDay();
    int bsMonth = ccBAN.getBSMonth();
    int bsYear = ccBAN.getBSYear();
    int bcDay = ccBAN.getBCDay();
    int bcMonth = ccBAN.getBCMonth();
    int bcYear = ccBAN.getBCYear();
    String billingStartDate = ccBAN.getBillingStartDate();
    String billingCeaseDate = ccBAN.getBillingCeaseDate();
    boolean disableDate = false;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop",Action.equals("Add")?"":"Conglom Billing Menu"},
                            {"Submit","","Refresh","Raise Query",Action.equals("Add")?"":"Add Product"}};
//System.out.println(Mode);
    if (Mode.equals("Confirm"))
    {
      Options[1][0] = "Confirm";
      Options[1][1] = "Cancel";
      disableDate = true;
    }
    else if (Mode.equals("View"))
    {
      Options[1][0] = "";
      Options[1][2] = "";
      disableDate = true;
    }
    else if (Mode.equals("Delete"))
    {
      Options[1][0] = "Delete";
      Options[1][2] = "";
      disableDate = true;
    }
    else if (Mode.equals("Amend"))
    {
      Options[1][0] = "Update";
      Options[1][2] = "";
      //disableDate = true;
    }

    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);
/*    if (adjustmentAmount.charAt(adjustmentAmount.length()-2) == '.')
    {
      adjustmentAmount.append("0");
    }
    if (taxAmount.charAt(taxAmount.length()-2) == '.')
    {
      taxAmount.append("0");
    }*/
   %>

<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function toggleSort(column)
{
  if (column == 'action')
  {
    window.open("conglomCustMaintKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="conglomCustMaint" method="post" action="conglomCustMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="billedProductId" type=hidden value="">
<input name="billedProductFreq" type=hidden value="">
<input name="billedProductSource" type=hidden value="">
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <%=HB.getMenu_Bar()%>
    </td>
  </tr>
  <tr>
   <td colspan=3>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
      <tr id=2>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h2 align="center"><%=Action%> Conglomerate Customer <%=Action.equals("Add")?"":"Profile"%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","conglomCustMaint");
	      %>
    	        <%=SU.hasNoValue(ccBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
              <td>
            </tr>
<%if (!Action.equals("Add"))
  {%>
            <tr>
              <td width="15%"><b>Customer:</b></td>
              <td align=left class=bluebold><%=displayCustomer%></td>
              <td colspan=4>
                &nbsp;
              </td>
            </tr>
<%}%>
            <tr>
              <td colspan=6>
                <hr>
              </td>
            </tr>
            <tr>
              <td colspan=6>
                <table border=0 width=100%>
<%if (Action.equals("Add"))
  {%>
                  <tr>
                    <td colspan=4 class=<%=ccBAN.getClass("conglomCustomer")%>><b>Global Customer
                      <br><%=DBA.getListBox("conglomCustomer",InputMode,gcId,BAN.getActAsLogon(),1," style=\"width:450px\"",true)%>
                    </td>
                  </tr>
<%}%>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("customerName")%>><b>Customer Name
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="customerName" value="<%=customerName%>" <%=ccBAN.getMode("customerName")%>>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("dob")%>><b>DOB
                      <br><%=DBA.getListBox("conglomDOB",InputMode,dob,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress1")%>><b>Billing Address:<br>Line 1
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress1" value="<%=billingAddress[0]%>" <%=ccBAN.getMode("billingAddress1")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 valign="top" class=<%=ccBAN.getClass("billingStartDateh")%>>Billing Start Date<br>
                      <%=HB.getDays("conglomCustMaint", "billingStartDate", bsDay, false, disableDate)%>
                      <%=HB.getMonths("conglomCustMaint", "billingStartDate", bsMonth, false, disableDate)%>
                      <%=HB.getYears("conglomCustMaint", "billingStartDate", bsYear, false, disableDate)%>
                      <input type="hidden" name="billingStartDateh" value="<%=billingStartDate%>">
                    </td>
                    <td colspan=2 valign="top" class=<%=ccBAN.getClass("billingCeaseDateh")%>>Billing Cease Date<br>
                      <%=HB.getDays("conglomCustMaint", "billingCeaseDate", bcDay, true, disableDate)%>
                      <%=HB.getMonths("conglomCustMaint", "billingCeaseDate", bcMonth, true, disableDate)%>
                      <%=HB.getYears("conglomCustMaint", "billingCeaseDate", bcYear, true, disableDate)%>
                      <input type="hidden" name="billingCeaseDateh" value="<%=billingCeaseDate%>">
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress2")%>><b>Line 2
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress2" value="<%=billingAddress[1]%>" <%=ccBAN.getMode("billingAddress2")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("billingRefPrefix")%>><b>Billing Reference Prefix
                      <br><input style="height:18px;font-size:xx-small;" type=text name="billingRefPrefix" value="<%=billingRefPrefix%>" <%=ccBAN.getMode("billingRefPrefix")%>>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingFrequency")%>><b>Billing Frequency
                      <br><%=DBA.getListBox("conglomBillingFrequency",InputMode,billingFrequency,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress3")%>><b>Line 3
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress3" value="<%=billingAddress[2]%>" <%=ccBAN.getMode("billingAddress3")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("zeroBal")%>><b>Show Zero Balance Bills
                      <br><%=DBA.getListBox("zeroBal",InputMode,zeroBal,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("vatExempt")%>><b>VAT Exempt Indicator
                      <br><%=DBA.getListBox("vatExempt",InputMode,vatExempt,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress4")%>><b>Line 4
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress4" value="<%=billingAddress[3]%>" <%=ccBAN.getMode("billingAddress4")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("invoiceTypeCode")%>><b>Invoice Type Code
                      <br><%=DBA.getListBox("invoiceTypeCode",InputMode,invoiceTypeCode,"x")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("conMPR")%>><b>Consolidated Multi Period Reporting
                      <br><%=DBA.getListBox("conMPR",InputMode,conMPR,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress5")%>><b>Line 5
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress5" value="<%=billingAddress[4]%>" <%=ccBAN.getMode("billingAddress5")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("cwContactTitle")%>><b>C&W Contact Title
                      <br><input style="height:18px;font-size:xx-small;" type=text name="cwContactTitle" value="<%=cwContactTitle%>" <%=ccBAN.getClass("cwContactTitle")%>>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("cwContactName")%>><b>C&W Contact Name
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="cwContactName" value="<%=cwContactName%>" <%=ccBAN.getClass("cwContactName")%>>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress6")%>><b>Line 6
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="billingAddress6" value="<%=billingAddress[5]%>" <%=ccBAN.getMode("billingAddress7")%>>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=2 class=<%=ccBAN.getClass("softBillReq")%>><b>Softcopy Bill Required
                      <br><%=DBA.getListBox("softBillReq",InputMode,softBillReq,"")%>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("fao")%>><b>For Attention Of
                      <br><input style="width:250px;height:18px;font-size:xx-small;" type=text name="fao" value="<%=fao%>" <%=ccBAN.getMode("fao")%>>
                    </td>
                    <td colspan=2 class=<%=ccBAN.getClass("billingAddress7")%>><b>Line 7
                      <br><input style="width:250px;eight:18px;font-size:xx-small;" type=text name="billingAddress7" value="<%=billingAddress[6]%>" <%=ccBAN.getMode("billingAddress7")%>>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
<%if (!Action.equals("Add"))
  {%>
            <tr>
              <td colspan=6 align=center>
                <table border=0 width=70%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=230>
                      <button class=grid_menu onclick="toggleSort('')">Billed Product Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=110>
                      <button class=grid_menu onclick="toggleSort('')">Extract Frequency</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=110>
                      <button class=grid_menu onclick="toggleSort('')">Billing Start Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=110>
                      <button class=grid_menu onclick="toggleSort('')">Billing Cease Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="toggleSort('action')">Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=5>
                    <iframe frameborder=0 width="100%" height=100 id=GridData name=GridData src="billedProductGrid.jsp?sortOrder=sortOrder"></iframe>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
<%}%>
	  </table><!--table 3-->
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


