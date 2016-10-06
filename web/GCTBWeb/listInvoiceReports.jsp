<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<% 
    //boolean isConglom = request.getParameter("type").equals("3");
    String type = request.getParameter("type");

    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off", (type.equals("3")?"Conglom ":"") + "Desktop", 
        type.equals("3")?"Conglom Billing Menu":type.equals("a")?"Ad Hoc":type.equals("f")?"":"Accounts"},
                            {"Refresh", "Raise Query"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = "List Invoice Reports";
    %>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("listInvoicesKey.htm", "lik", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<!--meta http-equiv="Refresh" content="15"-->
<form name="listInvoiceReports" method="post" action="listInvoiceReportsHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fileName" type=hidden value="">
<input name="type" type=hidden value="<%=request.getParameter("type")%>">
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
	        session.setAttribute("formname","listInvoiceReports");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
              <td>
            </tr>
<%if (type.equals("3"))
   {%>
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=BAN.getConglomCustomerName()%></td>
              <td><b>Products:</b></td>
              <td class=bluebold><%=BAN.getConglomProductCount()%></td>
              <td><b>Total Excl. Tax:</b></td>
              <td class=bluebold><%=BAN.getConglomInvTotalExc()%></td>
            </tr>
            <tr>
              <td><b>Frequency:</b></td>
              <td class=bluebold><%=BAN.getConglomFrequency()%></td>
              <td><b>Conglom. Invoice Ref.:</b></td>
              <td class=bluebold><%=BAN.getConglomInvoiceRef()%></td>
              <td><b>Tax:</b></td>
              <td class=bluebold><%=BAN.getConglomInvTax()%></td>
            </tr>
            <tr>
              <td><b>Period:</b></td>
              <td class=bluebold><%=BAN.getConglomPeriod()%></td>
              <td colspan="2">&nbsp;</td>
              <td><b>Total Incl. Tax:</b></td>
              <td class=bluebold><%=BAN.getConglomInvTotalInc()%></td>
            </tr>
<%}
  else
  {%>  
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=BAN.getAccDetsCustomer()%></td>
              <td><b>Period:</b></td>
              <td class=bluebold><%=BAN.getAccDetsPeriod()%></td>
              <td><b>DOB:</b></td>
              <td class=bluebold><%=BAN.getAccDetsDOB()%></td>
            </tr>
            <tr>
              <td><b>Account:</b></td>
              <td class=bluebold><%=BAN.getAccountId()%> <%=BAN.getAccountName()%></td>
              <td><b>Invoice No.:</b></td>
              <td class=bluebold><%=BAN.getInvoiceNo()%></td>
              <td><b>Total:</b></td>
              <td class=bluebold><%=BAN.getTotalCharges()%></td>
            </tr>
<%}%>            
            <tr>
              <td colspan="6">
                <table border=0 width=60% align="center">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=300>
                      <button class=grid_menu>Report Name</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=150>
                      <button class=grid_menu>Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu>Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="6">
                <table border=0 width=60% align="center">
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="invoiceReportGrid.jsp"></iframe>
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


