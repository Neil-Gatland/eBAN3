<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    String conglomCustomerId=Long.toString(BAN.getConglomCustomerId());
    String conglomCustomerName=BAN.getConglomCustomerName();
    String billedProduct = BAN.getBilledProductForList();
    //DBAccess DBA = new DBAccess();


    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu"},
                            {"Refresh","Add","Raise Query"}};

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = "List Conglomerate Billing Adjustments";
    %>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("conglomAdjustmentKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="listConglomAdjustments" method="post" action="listConglomAdjustmentsHandler.jsp">
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
	  <h2 align="left"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="8" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","listConglomAdjustments");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td align=left class=bluebold><nobr><%=BAN.getGlobalCustomerId() + " " +
                 conglomCustomerName+" ("+conglomCustomerId+")"%></nobr></td>
              <td width="5%"><b>&nbsp;</b></td>
              <td><b>Billed Product:</b></td>
              <td align=left class=bluebold><nobr><%=DBA.getListBox("conglomBilledProduct2","submit",billedProduct,conglomCustomerId,"cust",1," style=\"width:200px\"",false)%></nobr></td>
              <td colspan=3><b>&nbsp;</b></td>
            </tr>
            <tr>
              <td colspan="8">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=165>
                      <button class=grid_menu>Billed Product</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu>Adjustment Date</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=120>
                      <button class=grid_menu>Docket Number</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=70>
                      <button class=grid_menu>Type</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=300>
                      <button class=grid_menu>Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width=90>
                      <button class=grid_menu>Amount</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action</button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=8>
	        <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="conglomAdjustmentGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
	      </td>
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


