<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page isThreadSafe="false" %>
<%@ page import="HTMLUtil.HTMLBean"%>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->
<%
    String userId = (String)session.getAttribute("User_Id");
    OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
    if (chBAN == null)
    {
      chBAN = new OSSChargeBANBean();
      session.setAttribute("chBAN" + userId, chBAN);
    }
    String SQL;
    String Global_Customer_Id=chBAN.getGlobal_Customer_Id_for_List();
    String Global_Customer_Name=chBAN.getGlobalCustomerName();
    String Global_Service_Reference=chBAN.getGSR_for_List();
    String Division_Id=chBAN.getDivision_for_List();
    String Division=chBAN.getDivisionName();
    String Invoice_Region="";
    String Status;
    String BAN_Date;
    String Message;
    String Qualifier="";
    boolean firstTime = request.getParameter("firstTime")!=null;


    String action;
    String Menu_Headings[]={"Go To"};
    String Options[][] = {{"Log Off","Desktop","Create BAN Menu"," "," "},
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
      action=request.getQueryString();
    }
    else
    {
      action=chBAN.getAction();
    }
    String Charge_Category=chBAN.getCharge_Category();
    String title = "List " + (Charge_Category.equals("03")?"Credits":
      ((Charge_Category.equals("01")?"Recurring":"One Off") + " Charges"));
    %>
<form name="ListCharges" method="post" action="ListChargesHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input type=hidden name="Charge_Id" value=" ">
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
	        session.setAttribute("formname","ListCharges");
	      %>
    	        <%=SU.isNull(chBAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td class=bluebold><%=Global_Customer_Name%></td>
              <td><b>Division:</b></td>
              <td class=bluebold><%=Division%></td>
              <td><b>C2 Ref No:</b></td>
              <td class=bluebold><%=chBAN.getC2RefNo_for_List()%></td>
              <td><b>Circuit:</b></td>
              <td class=bluebold><%=Global_Service_Reference%></td>
            </tr>
            <tr>
              <td colspan="8">
	        <!--chBAN.getChargeHeader()-->
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width=450><button class=grid_menu>Description</button></td>
                    <td class=gridHeader NOWRAP valign=top width=150><button class=grid_menu>Amount</button></td>
                    <td class=gridHeader NOWRAP valign=top width=140><button class=grid_menu><%=(Charge_Category.equals("01")?"From":"Charge Payable")%> Date</button></td>
                    <td class=gridHeader NOWRAP valign=top width=140><button class=grid_menu><%=(Charge_Category.equals("01")?"To Date":"")%></button></td>
                    <td class=gridHeader NOWRAP valign=top><button class=grid_menu>Select</button></td><td width=10 bgcolor=#FFFFFF>&nbsp</td></tr>
                  </tr>
                  <tr>
                    <td colspan=5>
<%
  /*if (((firstTime) &&
    ((!chBAN.getGSR_for_List().equals("All")) ||
    (!chBAN.getBAN_Month_for_List().equals("All")) ||
    (!chBAN.getC2RefNo_for_List().equals("All")) ||
    (!chBAN.getCharge_Description_for_List().equals("All")))) ||
    (!firstTime))
  {*/%>
                      <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="ChargeGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
<%/*}*/%>
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


