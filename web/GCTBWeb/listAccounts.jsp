<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<% String SQL;
    String accountType = request.getParameter("accountType");


    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Desktop"},
                            {(accountType.equals("i")?"Show All Accounts":"Show Accounts With Invoices"), "Raise Query"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"Charge_Id"};
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = "List Accounts";
    %>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("listAccountsKey.htm", "lak", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="listAccounts" method="post" action="listAccountsHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="iRId" type=hidden value="">
<input name="accountId" type=hidden value="">
<input name="accountName" type=hidden value="">
<input name="total" type=hidden value="">
<input name="accountType" type=hidden value="<%=accountType%>">
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
	        session.setAttribute("formname","listAccounts");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
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
            <tr>
              <td colspan="6">
                <table border=0 width=100%>
                  <tr class=gridHeader>
<%if (accountType.equals("i"))
  {%>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Account No.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=170>
                      <button class=grid_menu>Account Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Invoice No.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=60>
                      <button class=grid_menu>Inv. Curr.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=55>
                      <button class=grid_menu>Tax Type</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Exceptions</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=60>
                      <button class=grid_menu>Charges</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=85>
                      <button class=grid_menu>Adjustments</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=55>
                      <button class=grid_menu>Tax</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu>Total</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu>SAP Ref.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                    </td>
<%}
  else
  {%>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Account No.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=170>
                      <button class=grid_menu>Account Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu>Invoice Currency</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=50>
                      <button class=grid_menu>Tax Type</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=65>
                      <button class=grid_menu>Tax Rate %</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu>General Ledger Code</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=140>
                      <button class=grid_menu>C&amp;W Company Addr. Id</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=105>
                      <button class=grid_menu>Last Invoice Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=75>
                      <button class=grid_menu>SAP Ref.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                    </td>
<%}%>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="6">
                <table border=0 width=100%>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="accountGrid.jsp?accountType=<%=accountType%>"></iframe>
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


