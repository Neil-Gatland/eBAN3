<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="TRB" class="DBUtilities.ThresholdBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","thresholdMaint");
  String SQL;
  String Mode=TRB.getMode();

  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Reference Data Menu"},
    {"Submit","","Refresh","View All Processes"}};

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
  String thresholdType = TRB.getThresholdType();
  String showPRS = thresholdType.equalsIgnoreCase("invoice")?"(PRS)":"";
%>
<script language="JavaScript">
</script>
<form name="thresholdMaint" method="post" action="thresholdMaintHandler.jsp">
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
	  <h3 align="center"><%=TRB.getThresholdType()%>/<%=TRB.getMasterAccountDisplay().equals("")?"System":"Master"%> Threshold Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="4" width=100% class=bluebold>
    	        <%=SU.hasNoValue(TRB.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
TRB.setMessage("");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan=4>
                <table width="100%" border="0">
                  <tr>
                    <td><b>Call Month:</b></td>
                    <td class=bluebold><%=RSB.getLatestCallMonth()%></td>
                    <td><b>Current Status:</b></td>
                    <td class=bluebold><%=RSB.getCurrentStatus()%></td>
                    <td><b>RQR09 Load Time:</b></td>
                    <td class=bluebold><%=RSB.getRQR09LoadTime()%></td>
                  </tr>
                  <tr>
                    <td><b>Rating Time:</b></td>
                    <td class=bluebold><%=RSB.getRatingTime()%></td>
                    <td><b>Invoicing Time:</b></td>
                    <td class=bluebold><%=RSB.getInvoicingTime()%></td>
                    <td><b>Ebilling Upload Time:</b></td>
                    <td class=bluebold><%=RSB.getEbillingUploadTime()%></td>
                  </tr>
                  <tr>
      <%if (!TRB.getMasterAccountDisplay().equals(""))
        {%>
                    <td><b>Master Account:</b></td>
                    <td class=bluebold><%=TRB.getMasterAccountDisplay()%></td>
      <%}%>
      <%if (!TRB.getProductGroupDisplay().equals(""))
        {%>
                    <td><b>Product Group:</b></td>
                    <td class=bluebold><%=TRB.getProductGroupDisplay()%></td>
      <%}%>
                  </tr>
                  <tr>
                    <td colspan="6">
                      <hr>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan=2 width="50%" class=<%=TRB.getClass("amountPRS")%>><b>Minimum Amount <%=showPRS%></b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="amountPRS" value="<%=TRB.getAmountPRS().toString()%>" <%=TRB.getMode("amountPRS")%>>
              </td>
              <td colspan=2 width="50%" class=<%=TRB.getClass("minutesPRS")%>><b>Minimum Duration (Minutes) <%=showPRS%></b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="minutesPRS" value="<%=TRB.getMinutesPRS().toString()%>" <%=TRB.getMode("minutesPRS")%>>
              </td>
	    </tr>
<%if (thresholdType.equalsIgnoreCase("invoice"))
  {%>
            <tr>
              <td colspan=2 width="50%" class=<%=TRB.getClass("amountNonPRS")%>><b>Minimum Amount (Non-PRS)</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="amountNonPRS" value="<%=TRB.getAmountNonPRS().toString()%>" <%=TRB.getMode("amountNonPRS")%>>
              </td>
              <td colspan=2 width="50%" class=<%=TRB.getClass("minutesNonPRS")%>><b>Minimum Duration (Minutes) (Non-PRS)</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="minutesNonPRS" value="<%=TRB.getMinutesNonPRS().toString()%>" <%=TRB.getMode("minutesNonPRS")%>>
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


