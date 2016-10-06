<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  session.setAttribute("formname","gsoDetail");
  String SQL;
  //DBAccess DBA = new DBAccess();
  String type = request.getParameter("type");
  String show1 = null;
  String show2 = null;
  String show3 = null;
  String typeTitle = null;
  String gsoJobId = BAN.getGSOJobId();
  String gsoMessageDate = BAN.getGSOMessageDate();
  if (type.equals("request"))
  {
    show1 = "Show Rejects";
    show2 = "Show Exceptions";
    show3 = "Show Billing Log";
    typeTitle = "Requests";
  }
  else if (type.equals("reject"))
  {
    show1 = "Show Requests";
    show2 = "Show Exceptions";
    show3 = "Show Billing Log";
    typeTitle = "Rejects";
  }
  else if (type.equals("exception"))
  {
    show1 = "Show Requests";
    show2 = "Show Rejects";
    show3 = "Show Billing Log";
    typeTitle = "Exceptions";
  }
  else //if (type.equals("log"))
  {
    show1 = "Show Requests";
    show2 = "Show Rejects";
    show3 = "Show Exceptions";
    typeTitle = "Billing Log";
  }
  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","GSO Desktop"},
    {"Refresh","Raise Query",show1,show2,show3}};

  //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  //Define the key values to be stored in the Radio button
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  //String Message=SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  //session.setAttribute("Message","");
  //BAN.setMessage("");
  String userId = (String)session.getAttribute("User_Id");
  String actAsLogon = BAN.getActAsLogon();
  String logonGroup = BAN.getLogonGroup();
  String sortOrder = BAN.getDesktopSortOrder();
%>
<script language="JavaScript">
function checkRejectsToShow()
{
  if (<%=BAN.getGSORejects()%> == 0)
  {
    alert("There are no rejects to display.");
  }
  else
  {
    gsoDetail.ButtonPressed.value = "Show Rejects";
    gsoDetail.submit();
  }
}
function checkExceptionsToShow()
{
  if (<%=BAN.getGSOExceptions()%> == 0)
  {
    alert("There are no exceptions to display.");
  }
  else
  {
    gsoDetail.ButtonPressed.value = "Show Exceptions";
    gsoDetail.submit();
  }
}
function Toggle_Sort(buttonPressed)
{
  if ((buttonPressed == 'Customer_Order') ||
    (buttonPressed == 'DOB') ||
    (buttonPressed == 'Status'))
  {
    gsoDetail.sortOrder.value = buttonPressed;
    gsoDetail.submit();
  }
  else if (buttonPressed == 'Action')
  {
    window.open("gsoDetailKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="gsoDetail" method="post" action="gsoDetailHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="rowId" type=hidden value="">
<input name="sortOrder" type=hidden value="<%=sortOrder%>">
<input name="type" type=hidden value="<%=type%>">
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
	  <h2 align="left">GSO Billing Detail - <%=typeTitle%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	       <%=(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))%>
<%
//(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))
session.setAttribute("Message","");
BAN.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td><b>Service Partner:</b></td>
              <td  colspan="3" class=bluebold><%=BAN.getServicePartnerName()%></td>
              <td><b>Status:</b></td>
              <td class=bluebold><%=BAN.getGSOStatus()%></td>
            </tr>
            <tr>
              <td><b>Billing Period:</b></td>
              <td class=bluebold><%=BAN.getGSOBillingPeriod()%></td>
              <td><b>Extract Rejects:</b></td>
              <td class=bluebold><%=BAN.getGSORejects()%></td>
              <td><b>Billing Exceptions:</b></td>
              <td class=bluebold><%=BAN.getGSOExceptions()%></td>
            </tr>
            <tr>
              <td colspan="6" align=center>
                <!--header-->
<%if (type.equals("request"))
{%>
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Request Type</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=65>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Trial/Close</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Job Id</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=85>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Requested On</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Started On</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=85>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Completed On</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=110>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Completion Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=205>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Rejection Reason</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu>&nbsp;</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=10>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="gsoDetailGrid.jsp?sortOrder=<%=sortOrder%>&type=<%=type%>"></iframe>
                    </td>
                  </tr>
                </table>
<%}
  else if (type.equals("reject"))
  {%>
                <table border=0 width=70%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Billing Start Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=150>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">SO Ref</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=150>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">GSO Product Code</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">MacSpec1</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Charge Type</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu>Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=6>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="gsoDetailGrid.jsp?sortOrder=<%=sortOrder%>&type=<%=type%>"></iframe>
                    </td>
                  </tr>
                </table>
<%}
  else if (type.equals("exception"))
  {%>
                <table border=0 width=70%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=295>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">End Customer</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=345>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Exception</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu>&nbsp;</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=6>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="gsoDetailGrid.jsp?sortOrder=<%=sortOrder%>&type=<%=type%>"></iframe>
                    </td>
                  </tr>
                </table>
<%}
  else //if (type.equals("log"))
  {%>
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader valign=top width=150>Job Id<br>
                      <%=DBA.getListBox("gsoJobId","input",gsoJobId,BAN.getServicePartnerName())%>
                    </td>
                    <td class=gridHeader valign=top width=150>Message Date<br>
                      <%=DBA.getListBox("gsoMessageDate","input",gsoMessageDate,BAN.getServicePartnerName())%>
                    </td>
                    <td class=gridHeader valign=top>&nbsp;
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="6" align=center>
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Job Id</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=90>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Message Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=760>
                      <button class=grid_menu onclick="Toggle_Sort(' ')">Message</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu>&nbsp;</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=6>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="gsoDetailGrid.jsp?sortOrder=<%=sortOrder%>&type=<%=type%>"></iframe>
                    </td>
                  </tr>
                </table>
<%}%>
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


