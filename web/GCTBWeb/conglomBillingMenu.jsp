<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%
  String sharedPath = EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  session.setAttribute("formname","conglomBillingMenu");
  String SQL;

  String action;
  String Menu_Headings[]={"Go To", "Options"/*, "Admin"*/};
  String Options[][] = {{"Log Off","Conglom Desktop"},
                          {"Refresh","Raise Query"}};//,
                          //{/*"DOB",*/"Change Password","Ad Hoc Invoice",/*"Account Discount",*/"Account Administration"}};

  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
  String actAsLogon = BAN.getActAsLogon();
  String logonGroup = BAN.getLogonGroup();
  String sortOrder = BAN.getDesktopSortOrder();
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if ((buttonPressed == 'Customer_Order') ||
    (buttonPressed == 'DOB') ||
    (buttonPressed == 'Status'))
  {
    conglomBillingMenu.sortOrder.value = buttonPressed;
    conglomBillingMenu.submit();
  }
  else if (buttonPressed == 'Action')
  {
    window.open("conglomBillingMenuKey.htm", "cdk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="conglomBillingMenu" method="post" action="conglomBillingMenuHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="gcId" type=hidden value="">
<input name="sortOrder" type=hidden value="<%=sortOrder%>">
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
	  <h2 align="left">Conglomerate Billing Menu</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold height="20px">
	       <%=(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))%>
<%
//(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))
session.setAttribute("Message","");
BAN.setMessage("");
%>
              </td>
            </tr>
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
            <tr>
              <td colspan="6" height="20">&nbsp;</td>
	    </tr>
            <tr>
              <td colspan="6" height="20" align="center"><b>Please select the required option from the list below</b></td>
	    </tr>
            <tr>
              <td colspan="6" height="20">&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Conglomerate Customer Profile Maintenance","submitForm(this)")%></td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Conglomerate Product Discounts","submitForm(this)")%></td>
              <td>&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Re-pull Billing Data from Goldfish","submitForm(this)")%></td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Manual input of Source Invoices","submitForm(this)")%></td>
              <td>&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Adjustments","submitForm(this)")%></td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Prior Period Set-Up","submitForm(this)")%></td>
              <td>&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Generate Trial Bill","submitForm(this)")%></td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Maintain Bill Prod","submitForm(this)")%></td>
              <td>&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("View Trial Bill","submitForm(this)")%></td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Exception Processing","submitForm(this)")%></td>
              <td>&nbsp;</td>
	    </tr>
            <tr>
              <td>&nbsp;</td>
              <td colspan="2"><img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Generate Close Bill","submitForm(this)")%></td>
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


