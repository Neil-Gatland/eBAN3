<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<%@ page import="JavaUtil.EBANProperties"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%  BAN.getGIVNJobQueue(false);
    boolean firstTime = request.getParameter("firstTime")!=null;


    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Desktop"},
                            {"Submit","Refresh","Raise Query"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"Charge_Id"};
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = "Bill Submission";
    %>
<form name="billSubmission" method="post" action="billSubmissionHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="iRId" type=hidden value="">
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
	  <h2 align="left"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","billSubmission");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
    	        <%BAN.setMessage("");%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=BAN.getAccDetsCustomer()%></td>
              <td><b>Period:</b></td>
              <td class=bluebold><%=BAN.getAccDetsPeriod()%></td>
              <td><b>DOB:</b></td>
              <td class=bluebold><%=BAN.getAccDetsDOB()%></td>
            </tr>
            <tr>
              <td><b>Accounts:</b></td>
              <td class=bluebold><%=BAN.getAccDetsAccounts()%></td>
              <td><b>Status:</b></td>
              <td class=bluebold><%=BAN.getAccDetsStatus()%></td>
              <td><b>Exceptions:</b></td>
              <td class=bluebold><%=BAN.getAccDetsExceptions()%></td>
            </tr>
            <tr class=gridHeader>
              <td class=gridHeader colspan=2>Run Type</td>
              <td class=gridHeader colspan=2>Submission Time</td>
              <td class=gridHeader colspan=2>Backdates Required</td>
              <!--td class=gridHeader colspan=2>Scheduling Required</td-->
            </tr>
            <tr class=grid1>
              <td class=grid1 colspan=2><input type="radio" name="runType" value="trial" <%=BAN.getRunTypeChecked("trial")%>> Trial</td>
              <td class=grid1 colspan=2><input type="radio" name="runTime" value="now" <%=BAN.getRunTimeChecked("now")%>> Submit for processing now</td>
              <td class=grid1 colspan=2><input type="radio" name="backdates" value="C" <%=BAN.getBackdatesChecked("C")%>> At charge level</td>
              <!--td class=grid1 colspan=2><input type="checkbox" name="scheduleCrystal" value="yes" <%=BAN.getSheduleCrystalChecked()%>> Schedule Crystal Report</td-->
            </tr>
            <tr class=grid1>
              <td class=grid1 colspan=2><input type="radio" name="runType" value="close"  <%=BAN.getRunTypeChecked("close")%>> Close</td>
              <td class=grid1 colspan=2><input type="radio" name="runTime" value="overnight"  <%=BAN.getRunTimeChecked("overnight")%>> Submit for processing overnight</td>
              <td class=grid1 colspan=2><input type="radio" name="backdates" value="N"  <%=BAN.getBackdatesChecked("N")%>> No backdates</td>
              <!--td class=grid1 colspan=2>&nbsp;</td-->
            </tr>
<!--
            <tr class=grid1>
              <td class=grid1>&nbsp;</td>
              <td class=grid1 colspan=2>&nbsp;</td>
              <td class=grid1><input type="radio" name="backdates" value="Y"  BAN.getBackdatesChecked("Y")> At service level</td>
              <td class=grid1 colspan=2>&nbsp;</td>
            </tr>
-->
            <tr>
              <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="6" align=center>
                <table border=0 width=90%>
                  <tr>
                    <td colspan="5">
                      <h3>Job Queue</h3>
                    </td>
                  </tr>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=398>
                      <button class=grid_menu>Customer</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=98>
                      <button class=grid_menu>Period</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=98>
                      <button class=grid_menu>Jobname</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=98>
                      <button class=grid_menu>Submitted</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu>Running</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=5>
	              <iframe frameborder=0 width="90%" height=250 id=GridData name=GridData src="jobQueueGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
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


