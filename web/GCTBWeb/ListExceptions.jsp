<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    String Global_Customer_Id=BAN.getGlobalCustomerId();
    String Global_Customer_Name=BAN.getGlobalCustomerName();
    String exceptionType=BAN.getExceptionTypeForList();
    String exceptionStatus=BAN.getExceptionStatusForList();
    String iRIN=BAN.getIRINForList();
    String gsr=BAN.getGSR_for_List();
    String billingPeriod=BAN.getBPSD()==null?"":BAN.getBPSD();
    //DBAccess DBA = new DBAccess();
    boolean firstTime = request.getParameter("firstTime")!=null;


    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Desktop"},
                            {"Refresh","Reset Filters","Raise Query"},
			    {"All Charges","Selected Charges"},
			    {"Show Filters","Most Recent Values","Reset Filters"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"Charge_Id"};
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    if (SU.isNull((String)request.getQueryString(),"") != "")
    {//First time thru
      //action=request.getQueryString();
    }
    else
    {
      //action=chBAN.getAction();
    }
    String title = "List Data Billing Exceptions";
    %>
<script language="JavaScript">
function showKey()
{
  window.open("exceptionKey.htm", "ek", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
}
</script>
<form name="ListExceptions" method="post" action="ListExceptionsHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input type=hidden name="exId" value=" ">
<input type=hidden name="exType" value=" ">
<input type=hidden name="exIRIN" value=" ">
<input type=hidden name="exStatus" value=" ">
<input type=hidden name="OrderBy" value="">
<input type=hidden name="Description" value="">
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
	      <td colspan="8" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","ListExceptions");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Job Name:</b></td>
              <td class=bluebold>Data Billing</td>
              <td><b>Customer:</b></td>
              <td class=bluebold><nobr><%=Global_Customer_Name+" ("+Global_Customer_Id+")"%></nobr></td>
              <td><b>Billing Period:</b></td>
              <td class=bluebold><%=billingPeriod%></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td colspan="8">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader valign=top width=360>Exception Type<br>
                      <%=DBA.getListBox("Exception_Type","submit",exceptionType,"",1," style=\"width:350px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top width=110>Exception Status<br>
                      <%=DBA.getListBox("Exception_Status","submit",exceptionStatus,"",1," style=\"width:100px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top width=290><nobr>Invoice Region / No.</nobr><br>
                      <%=DBA.getListBox("IRIN","submit",iRIN,Global_Customer_Id,"",1," style=\"width:280px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top>GSR<br>
                      <%=DBA.getListBox("GSR","submit",gsr,Global_Customer_Id,1," style=\"width:150px\"",false)%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="8">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=340>
                      <button class=grid_menu>Exception</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Invoice No.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu>Invoice Region</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu>Global Service Ref.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Site</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu>Amount</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=50>
                      <button class=grid_menu>Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="showKey()">Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
<%
  if (((firstTime) &&
    ((!BAN.getGSR_for_List().equals("All")) ||
    (!BAN.getExceptionTypeForList().equals("All")) ||
    (!BAN.getExceptionStatusForList().equals("All")) ||
    (!BAN.getIRINForList().equals("All")))) ||
    (!firstTime))
  {%>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="ExceptionGrid.jsp"></iframe>
<%}%>
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


