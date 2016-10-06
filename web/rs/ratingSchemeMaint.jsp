<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RAB" class="DBUtilities.RatingSchemeBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","ratingSchemeMaint");
  String SQL;
  String Mode=RAB.getMode();

  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Rating Scheme"},
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
    Options[1][0] = RAB.getAction().equals("AddTo")?"Submit":"Update";
    Options[1][1] = "";
  }
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("productKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="ratingSchemeMaint" method="post" action="ratingSchemeMaintHandler.jsp">
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
	  <h3 align="center">Rating Scheme Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="3" width=100% class=bluebold>
    	        <%=SU.hasNoValue(RAB.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
RAB.setMessage("");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="3">
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
                    <td colspan="6">
                      <hr>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("fromCallMonth")%>><b>From Call Month</b>
                <br><%=DBA.getListBox("fromCallMonth",RAB.getMode("fromCallMonth"),RAB.getFromCallMonth(),RSB.getMinCallMonth(),1,"style=\"width:30%\"",true)%>
              </td>
              <td class=<%=RAB.getClass("toCallMonth")%>><b>To Call Month</b>
                <br><%=DBA.getListBox("toCallMonth",RAB.getMode("toCallMonth"),RAB.getToCallMonth(),"000000",1,"style=\"width:30%\"",true)%>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("productGroup")%>><b>Product Group</b>
                <br><%=DBA.getListBox("productGroup",RAB.getMode("productGroup"),RAB.getProductGroup(),"",1,"style=\"width:80%\"",true)%>
              </td>
              <td rowspan="3" colspan="2" style="border:solid 2px blue">
                <table width="100%" border="0">
                  <tr>
                    <td rowspan="3" class=<%=RAB.getClass("nameOrNumber")%> width="30%"><b>Default Rating Scheme</b>
                      <br><%=DBA.getListBox("signName",RAB.getMode("signName"),RAB.getSignName(),"",1,"style=\"width:50%\"",true)%>
                    </td>
                    <td rowspan="3" class=<%=RAB.getClass("nameOrNumber")%> width="15%" align="left">OR
                    </td>
                    <td class=<%=RAB.getClass("nameOrNumber")%> width="45%"><b>Provider</b>
                      <br><%=DBA.getListBox("providerId",RAB.getMode("providerId"),RAB.getProviderId(),"",1,"style=\"width:80%\"",true)%>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=RAB.getClass("nameOrNumber")%> width="45%"><b>Master Account Number</b>
                      <br><%=DBA.getListBox("masterAccountNumber",RAB.getMode("masterAccountNumber"),RAB.getMasterAccountNumber(),RAB.getProviderId(),1,"style=\"width:80%\"",true)%>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=RAB.getClass("accountNumber")%> width="45%"><b>Account Number</b>
                      <br><%=DBA.getListBox("accountNumber",RAB.getMode("accountNumber"),RAB.getAccountNumber(),RAB.getMasterAccountNumber(),1,"style=\"width:80%\"",true)%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("productCode")%>><b>Product</b>
                <br><%=DBA.getListBox("productCode",RAB.getMode("productCode"),RAB.getProductCode(),RAB.getProductGroup(),1,"style=\"width:80%\"",true)%>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("ratingType")%>><b>Rating Type</b>
                <br><%=DBA.getListBox("ratingType",RAB.getMode("ratingType"),RAB.getRatingType(),"RS Rating Type",1,"style=\"width:30%\"",true)%>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("ratingDuration")%>><b>Duration</b>
                <br><%=DBA.getListBox("ratingDuration",RAB.getMode("ratingDuration"),RAB.getRatingDuration(),"RS Duration",1,"style=\"width:40%\"",true)%>
              </td>
              <td class=<%=RAB.getClass("rangeStart")%>><b>Range Start</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="rangeStart" value="<%=RAB.getRangeStart()%>" <%=RAB.getMode("rangeStart")%>>
              </td>
              <td class=<%=RAB.getClass("rangeEnd")%>><b>Range End</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="rangeEnd" value="<%=RAB.getRangeEnd()%>" <%=RAB.getMode("rangeEnd")%>>
              </td>
            </tr>
            <tr>
              <td class=<%=RAB.getClass("dayRate")%>><b>Day Rate £</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="dayRate" value="<%=RAB.getDayRate()%>" <%=RAB.getMode("dayRate")%>>
              </td>
              <td class=<%=RAB.getClass("eveningRate")%>><b>Evening Rate £</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="eveningRate" value="<%=RAB.getEveningRate()%>" <%=RAB.getMode("eveningRate")%>>
              </td>
              <td class=<%=RAB.getClass("weekendRate")%>><b>Weekend Rate £</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="weekendRate" value="<%=RAB.getWeekendRate()%>" <%=RAB.getMode("weekendRate")%>>
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


