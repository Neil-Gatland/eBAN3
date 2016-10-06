<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="itmBAN" class="DBUtilities.InvoiceThresholdBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","invoiceThresholdMaint");
  String SQL;
  String Mode=itmBAN.getMode();

  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop"},
    {"Submit","","Refresh",""}};

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][1] = "";
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][1] = "";
  }
  //Define the key values to be stored in the Radio button
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
%>
<script language="JavaScript">
</script>
<form name="invoiceThresholdMaint" method="post" action="invoiceThresholdMaintHandler.jsp">
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
	  <h3 align="center">Invoice Threshold Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="2" width=100% class=bluebold>
    	        <%=SU.hasNoValue(itmBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
itmBAN.setMessage("");
BAN.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan=2>
                <hr>
              </td>
            </tr>
            <tr>
              <td width="50%" class=<%=itmBAN.getClass("invoiceThresholdPRS")%>><b>Invoice Threshold (PRS)
                <br><input style="height:18px;font-size:xx-small;" type=text name="invoiceThresholdPRS" value="<%=itmBAN.getInvoiceThresholdPRS().toString()%>" <%=itmBAN.getMode("invoiceThresholdPRS")%>>
              </td>
              <td class=<%=itmBAN.getClass("minDurationPRS")%>><b>Minimum Duration (PRS)
                <br><input style="height:18px;font-size:xx-small;" type=text name="minDurationPRS" value="<%=itmBAN.getMinDurationPRS().toString()%>" <%=itmBAN.getMode("minDurationPRS")%>>
              </td>
	    </tr>
            <tr>
              <td class=<%=itmBAN.getClass("invoiceThresholdNonPRS")%>><b>Invoice Threshold (Non-PRS)
                <br><input style="height:18px;font-size:xx-small;" type=text name="invoiceThresholdNonPRS" value="<%=itmBAN.getInvoiceThresholdNonPRS().toString()%>" <%=itmBAN.getMode("invoiceThresholdNonPRS")%>>
              </td>
              <td class=<%=itmBAN.getClass("minDurationNonPRS")%>><b>Minimum Duration (Non-PRS)
                <br><input style="height:18px;font-size:xx-small;" type=text name="minDurationNonPRS" value="<%=itmBAN.getMinDurationNonPRS().toString()%>" <%=itmBAN.getMode("minDurationNonPRS")%>>
              </td>
	    </tr>
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


