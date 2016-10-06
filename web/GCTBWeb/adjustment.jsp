<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="HTMLUtil.HTMLBean,JavaUtil.StringUtil"%>
<jsp:useBean id="adjBAN" class="DBUtilities.AdjustmentBANBean" scope="session"/>
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
    BAN_Summary = adjBAN.getBAN_Summary();
    BAN_Reason = adjBAN.getBAN_Reason();
    Required_BAN_Effective_Date=SU.isNull(adjBAN.getRequired_BAN_Effective_Date(),"Today");
    Mode=adjBAN.getMode();
    Action=adjBAN.getAction();
    SelectMode=adjBAN.getSelectMode();
    InputMode=adjBAN.getInputMode();
    Calendar=adjBAN.getCalendar();
    BAN_Identifier=adjBAN.getBanIdentifier();
    adjBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = adjBAN.getBanStatus();

    String customer = adjBAN.getCustomer();
    String invoiceRegion = adjBAN.getInvoiceRegion();
    String invoiceNumber = adjBAN.getInvoiceNumber();
    String adjustmentDescription = adjBAN.getAdjustmentDescription();
    String adjustmentCurrency = adjBAN.getAdjustmentCurrency();
    //StringBuffer adjustmentAmount = new StringBuffer(String.valueOf(adjBAN.getAdjustmentAmount()));
    //StringBuffer taxAmount = new StringBuffer(String.valueOf(adjBAN.getTotalTaxAmount()));
    String adjustmentCRDE = adjBAN.getAdjustmentCRDE();
    if (adjustmentCRDE.equals("Debit"))
    {
      adjBAN.setNetOrFull("Z");
    }
    String adjustmentType = adjBAN.getAdjustmentType();
    String taxTypeDescription = adjBAN.getTaxTypeDescription();
    String revenueType = adjBAN.getRevenueType();
    String revenueDescription = adjBAN.getRevenueDescription();
    String netOrFull = adjBAN.getNetOrFull();
    String reportRequired = adjBAN.getReportRequired();
    String rootCause = adjBAN.getRootCause();
    String chargeEntity = adjBAN.getChargeEntity();
    String purchaseOrderRef = adjBAN.getPurchaseOrderRef();
    String invNoCredited = adjBAN.getInvNoCredited();
    int adjDay = adjBAN.getAdjDay();
    int adjMonth = adjBAN.getAdjMonth();
    int adjYear = adjBAN.getAdjYear();
    String adjustmentDate = adjBAN.getAdjustmentDate();
    boolean disableDate = false;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Desktop","Ad Hoc Invoice"},
                            {"Submit","","Refresh","Raise Query"}};
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
      disableDate = true;
    }

    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);
    /*if (adjustmentAmount.charAt(adjustmentAmount.length()-2) == '.')
    {
      adjustmentAmount.append("0");
    }
    if (taxAmount.charAt(taxAmount.length()-2) == '.')
    {
      taxAmount.append("0");
    }*/
   %>

<form name="adjustment" method="post" action="adjustmentHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
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
	  <h2 align="center"><%=Action%> an Adjustment</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","adjustment");
	      %>
    	        <%=SU.hasNoValue(adjBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=customer%></td>
              <td><b>Invoice Region:</b></td>
              <td class=bluebold><%=invoiceRegion%></td>
              <td><b>Invoice Number:</b></td>
              <td class=bluebold><%=invoiceNumber%></td>
            </tr>
            <tr>
              <td colspan=6>
                <hr>
              </td>
            </tr>
            <tr>
              <td class=<%=adjBAN.getClass("adjustmentDescription")%> colspan=4>Adjustment Description
                <br><input style="width:90%;height:18px;font-size:xx-small;" type=text name="adjustmentDescription" value="<%=adjustmentDescription%>" <%=adjBAN.getMode("adjustmentDescription")%>>
              </td>
              <td valign="top" class=<%=adjBAN.getClass("adjustmentDateh")%>>Adjustment Date<br>
                <%=HB.getDays("adjustment", "adjustmentDate", adjDay, false, disableDate)%>
                <%=HB.getMonths("adjustment", "adjustmentDate", adjMonth, false, disableDate)%>
                <%=HB.getYears("adjustment", "adjustmentDate", adjYear, false, disableDate)%>
                <input type="hidden" name="adjustmentDateh" value="<%=adjustmentDate%>">
              </td>
            </tr>
            <tr>
              <td colspan=2 class=<%=adjBAN.getClass("adjustmentCurrency")%>><b>Adjustment Currency
                <br><%=DBA.getListBox("adjustmentCurrency",InputMode,adjustmentCurrency,customer,invoiceRegion)%>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("adjustmentAmount")%>><b>Adjustment Amount
                <br><input style="height:18px;font-size:xx-small;" type=text name="adjustmentAmount" value="<%=adjBAN.getAdjustmentAmount().toString()%>" <%=adjBAN.getMode("adjustmentAmount")%>>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("adjustmentCRDE")%>><b>Credit/Debit
                <br><%=DBA.getListBox("adjustmentCRDE",SelectMode,adjustmentCRDE,"Credit Debit")%>
              </td>
            </tr>
            <tr>
              <td colspan=2 class=<%=adjBAN.getClass("adjustmentType")%>><b>Adjustment Type
                <br><%=DBA.getListBox("adjustmentType",InputMode,adjustmentType,"")%>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("revenueType")%>><b>Revenue Type
                <br><%=DBA.getListBox("revenueType",InputMode,revenueType,"")%>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("revenueDescription")%>><b>Revenue Description
                <br><%=DBA.getListBox("revenueDescription",InputMode,revenueDescription,"")%>
              </td>
            </tr>
            <tr>
              <td colspan=2 class=<%=adjBAN.getClass("netOrFull")%>><b>Net Or Full
                <br><%=DBA.getListBox("netOrFull",adjBAN.getInputMode("netOrFull"),netOrFull,"")%>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("rootCause")%>><b>Root Cause
                <br><%=DBA.getListBox("rootCause",InputMode,rootCause,"")%>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("chargeEntity")%>><b>Charge Entity
                <br><%=DBA.getListBox("chargeEntity",InputMode,chargeEntity,"")%>
              </td>
            </tr>
            <tr>
              <td class=<%=adjBAN.getClass("purchaseOrderRef")%> colspan=4>Purchase Order Reference
                <br><input style="width:90%;height:18px;font-size:xx-small;" type=text name="purchaseOrderRef" value="<%=purchaseOrderRef%>" <%=adjBAN.getMode("purchaseOrderRef")%>>
              </td>
              <td colspan=2 class=<%=adjBAN.getClass("invNoCredited")%>><b>Invoice Number Credited
                <br><input style="height:18px;font-size:xx-small;" type=text name="invNoCredited" value="<%=invNoCredited%>" <%=adjBAN.getMode("invNoCredited")%>>
              </td>
            </tr>
<%
if ((!Mode.equals("View")) && (!Mode.equals("Delete")))
{
%>
            <tr>
              <td colspan=6 class=<%=adjBAN.getClass("reportRequired")%>><b>Report Required (only set this to 'Y' when you have entered all adjustments for this invoice and wish to see the trial report)
                <br><%=DBA.getListBox("reportRequired",adjBAN.getInputMode("reportRequired"),reportRequired,"")%>
              </td>
            </tr>
<%
}
if ((Mode.equals("Confirm")) || (Mode.equals("View")) || (Mode.equals("Delete")))
{
%>
            <tr>
              <td colspan=2 class="optional"><b>Tax Type
                <br><input style="width:100%;height:18px;font-size:xx-small;" type=text name="taxTypeDescription" value="<%=taxTypeDescription%>" READONLY>
              </td>
              <td colspan=2 class="optional"><b>Tax Amount
                <br><input style="height:18px;font-size:xx-small;" type=text name="taxAmount" value="<%=adjBAN.getTotalTaxAmount().toString()%>" READONLY>
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


