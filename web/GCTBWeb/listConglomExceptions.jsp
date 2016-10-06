<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    String conglomCustomerId=Long.toString(BAN.getConglomCustomerId());
    String conglomCustomerName=BAN.getConglomCustomerName();
    String exceptionType=BAN.getExceptionTypeForList();
    String exceptionStatus=BAN.getExceptionStatusForList();
    String period=BAN.getPeriodForList();
    String invoiceDocketNo=BAN.getInvoiceDocketNoForList();
    String billedProduct=BAN.getBilledProductForList();
    //DBAccess DBA = new DBAccess();
    boolean firstTime = request.getParameter("firstTime")!=null;
    String fromBilling = request.getParameter("fromBilling");


    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop",fromBilling.equals("true")?"Conglom Billing Menu":""},
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
    String title = "List Conglomerate Billing Exceptions";
    %>
<script language="JavaScript">
function showKey()
{
  window.open("exceptionKey.htm", "ek", "toolbar=no,menubar=no,location=no," +
    "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
}
</script>
<form name="listConglomExceptions" method="post" action="listConglomExceptionsHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input type=hidden name="exId" value=" ">
<input type=hidden name="exType" value=" ">
<input type=hidden name="period" value=" ">
<input type=hidden name="invoiceDocketNo" value=" ">
<input type=hidden name="billedProduct" value=" ">
<input type=hidden name="exStatus" value=" ">
<input type=hidden name="OrderBy" value="">
<input type=hidden name="Description" value="">
<input type=hidden name="fromBilling" value="<%=fromBilling%>">
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
	        session.setAttribute("formname","listConglomExceptions");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Job Name:</b></td>
              <td class=bluebold>Conglomerate Billing</td>
              <td><b>Customer:</b></td>
              <td class=bluebold><nobr><%=conglomCustomerName+" ("+conglomCustomerId+")"%></nobr></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td colspan="8">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader valign=top width=110>Billed Product<br>
                      <%=DBA.getListBox("Conglom_Billed_Product","submit",billedProduct,conglomCustomerId,1," style=\"width:100px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top width=400>Exception Type<br>
                      <%=DBA.getListBox("Conglom_Exception_Type","submit",exceptionType,"x",1," style=\"width:390px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top width=110>Exception Status<br>
                      <%=DBA.getListBox("Conglom_Exception_Status","submit",exceptionStatus,"",1," style=\"width:100px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top width=110><nobr>Period</nobr><br>
                      <%=DBA.getListBox("Conglom_Period","submit",period,conglomCustomerId,1," style=\"width:100px\"",false)%>
                    </td>
                    <td class=gridHeader valign=top>Inv/Docket No.<br>
                      <%=DBA.getListBox("Conglom_Inv_Doc","submit",invoiceDocketNo,conglomCustomerId,1," style=\"width:100px\"",false)%>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="8">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu>Billed Product</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=410>
                      <button class=grid_menu>Exception</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=95>
                      <button class=grid_menu>Source Account</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=95>
                      <button class=grid_menu>Inv/Docket No.</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=100>
                      <button class=grid_menu>Date Billed/Period</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=80>
                      <button class=grid_menu>Status</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="showKey()">Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
<%
  /*if (((firstTime) &&
    ((!exceptionType.equals("All")) ||
    (!exceptionStatus.equals("All")) ||
    (!period.equals("All")) ||
    (!billedProduct.equals("All")) ||
    (!invoiceDocketNo.equals("All")))) ||
    (!firstTime))
  {*/%>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="conglomExceptionGrid.jsp"></iframe>
<%/*}*/%>
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


