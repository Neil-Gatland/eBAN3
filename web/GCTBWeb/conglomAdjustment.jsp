<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="adjBAN" class="DBUtilities.ConglomAdjustmentBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    String BAN_Identifier;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();

    String conglomCustomerId=Long.toString(BAN.getConglomCustomerId());
    String conglomCustomerName=BAN.getConglomCustomerName();
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

    String billedProduct = adjBAN.getBilledProduct();
    String adjustmentCRDE = adjBAN.getAdjustmentCRDE();
    String docketText = adjBAN.getDocketText();
    String docketNumber = adjBAN.getDocketNumber();
    String adjustmentDescription = adjBAN.getAdjustmentDescription();
    StringBuffer netAmount = new StringBuffer(String.valueOf(adjBAN.getNetAmount()));
    StringBuffer vatAmount = new StringBuffer(String.valueOf(adjBAN.getVATAmount()));
    StringBuffer totalAmount = new StringBuffer(String.valueOf(adjBAN.getTotalAmount()));
    String sourceInvoice = adjBAN.getSourceInvoice();
    String dummyInvoice = adjBAN.getDummyInvoice();
    int adjDay = adjBAN.getAdjDay();
    int adjMonth = adjBAN.getAdjMonth();
    int adjYear = adjBAN.getAdjYear();
    String adjustmentDate = adjBAN.getAdjustmentDate();
    boolean disableDate = false;

    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu","Conglom Adjustments"},
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
    if (netAmount.charAt(netAmount.length()-2) == '.')
    {
      netAmount.append("0");
    }
    if (vatAmount.charAt(vatAmount.length()-2) == '.')
    {
      vatAmount.append("0");
    }
    if (totalAmount.charAt(totalAmount.length()-2) == '.')
    {
      totalAmount.append("0");
    }

    String title = Action + " a Conglomerate Billing Adjustment";
    %>
<form name="conglomAdjustment" method="post" action="conglomAdjustmentHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="adjustmentId" type=hidden value="">
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
	  <h2 align="center"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="3" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","conglomAdjustment");
	      %>
    	        <%=SU.hasNoValue(adjBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td align=left class=bluebold><nobr><%=BAN.getGlobalCustomerId() + " " +
                 conglomCustomerName+" ("+conglomCustomerId+")"%></nobr></td>
              <td><b>&nbsp;</b></td>
            </tr>
            <tr>
              <td colspan="3">
                <hr>
              </td>
            </tr>
	    <tr>
              <td class=<%=adjBAN.getClass("adjustmentCRDE")%>><b>Adjustment Type
                <br><%=DBA.getListBox("adjustmentCRDE",SelectMode,adjustmentCRDE,"Credit Debit Sundry")%>
              </td>
              <td class=<%=adjBAN.getClass("conglomBilledProduct3")%>><b>Billed Product
                <br><%=DBA.getListBox("conglomBilledProduct3",InputMode,billedProduct,conglomCustomerId,"cust")%>
              </td>
	    </tr>
	    <tr>
              <td valign="top" class=<%=adjBAN.getClass("adjustmentDateh")%>>Adjustment Date<br>
                <%=HB.getDays("conglomAdjustment", "adjustmentDate", adjDay, false, disableDate)%>
                <%=HB.getMonths("conglomAdjustment", "adjustmentDate", adjMonth, false, disableDate)%>
                <%=HB.getYears("conglomAdjustment", "adjustmentDate", adjYear, false, disableDate)%>
                <input type="hidden" name="adjustmentDateh" value="<%=adjustmentDate%>">
              </td>
              <td class=<%=adjBAN.getClass("docketNumber")%>><b><%=docketText%>
                <br><input style="height:18px;font-size:xx-small;" type=text name="docketNumber" value="<%=docketNumber%>" <%=adjBAN.getMode("docketNumber")%>>
              </td>
	    </tr>
	    <tr>
              <td class=<%=adjBAN.getClass("adjustmentDescription")%> colspan=2>Adjustment Description
                <br><input style="width:90%;height:18px;font-size:xx-small;" type=text name="adjustmentDescription" value="<%=adjustmentDescription%>" <%=adjBAN.getMode("adjustmentDescription")%>>
              </td>
	    </tr>
	    <tr>
              <td class=<%=adjBAN.getClass("adjustmentAmount")%>>Adjustment Amount:
              </td>
              <td class=<%=adjBAN.getClass("associateInvoice")%>><b>Associate With Invoice:</b>
              </td>
	    </tr>
	    <tr>
<td><table><tr>
              <td class=<%=adjBAN.getClass("adjustmentAmount")%>><b>Net
                <br><input style="height:18px;font-size:xx-small;" type=text name="netAmount" value="<%=netAmount.toString()%>" <%=adjBAN.getMode("netAmount")%>>
              </td>
              <td class=<%=adjBAN.getClass("adjustmentAmount")%>><b>VAT
                <br><input style="height:18px;font-size:xx-small;" type=text name="vatAmount" value="<%=vatAmount.toString()%>" <%=adjBAN.getMode("vatAmount")%>>
              </td>
              <td class=<%=adjBAN.getClass("adjustmentAmount")%>><b>Total
                <br><input style="height:18px;font-size:xx-small;" type=text name="totalAmount" value="<%=totalAmount.toString()%>" <%=adjBAN.getMode("totalAmount")%>>
              </td>
</tr></table></td>
<td><table border=0><tr>
              <td class=<%=adjBAN.getClass("associateInvoice")%>><b>Source Invoice
                <br><input style="height:18px;font-size:xx-small;" type=text name="sourceInvoice" value="<%=sourceInvoice%>" <%=adjBAN.getMode("sourceInvoice")%>>
              </td>
              <td valign=top class=<%=adjBAN.getClass("associateInvoice")%>><b>OR</b></td>
              <td width=60% class=<%=adjBAN.getClass("associateInvoice")%>><b><nobr>Dummy Invoice For Account</nobr>
                <br><input style="height:18px;font-size:xx-small;" type=text name="dummyInvoice" value="<%=dummyInvoice%>" <%=adjBAN.getMode("dummyInvoice")%>>
              </td>
</tr></table></td>
	    </tr>
<!--
-->
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


