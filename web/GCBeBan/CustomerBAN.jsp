<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="ncBAN" class="DBUtilities.NewCustomerBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

  <%!//Variables
    String BAN_Identifier,Global_Customer_Id,Global_Customer_Name,Global_Account_Manager;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason;
    String Required_BP_Start_Date;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";
    String Sales_Business_Unit="",Automated_Back_Billing="",DOB_Date="",Global_Billing_Analyst="";
    int BEDay, BEMonth, BEYear, BPSDay, BPSMonth, BPSYear;

    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    //Get values from last submission
    Global_Customer_Name=ncBAN.getGlobalCustomerName();
    BAN_Summary = ncBAN.getBAN_Summary();
    BAN_Reason = ncBAN.getBAN_Reason();
    BEDay = ncBAN.getBANEffectiveDateDays();
    BEMonth = ncBAN.getBANEffectiveDateMonths();
    BEYear = ncBAN.getBANEffectiveDateYears();
    BPSDay = ncBAN.getBPStartDateDays();
    BPSMonth = ncBAN.getBPStartDateMonths();
    BPSYear = ncBAN.getBPStartDateYears();
    Global_Account_Manager=ncBAN.getGlobal_Account_Manager();
    Required_BAN_Effective_Date=SU.isNull(ncBAN.getRequired_BAN_Effective_Date(),"Today");
    Required_BP_Start_Date=SU.isNull(ncBAN.getRequired_BP_Start_Date(),"Today");
    Global_Customer_Id=ncBAN.getGlobalCustomerId();
    Sales_Business_Unit=ncBAN.getSales_Business_Unit();
    Automated_Back_Billing=ncBAN.getAutomated_Back_Billing();
    DOB_Date=ncBAN.getDOB_Date();
    Global_Billing_Analyst=ncBAN.getGlobal_Billing_Analyst();

    Mode=ncBAN.getMode();

    SelectMode=ncBAN.getSelectMode();
    InputMode=ncBAN.getInputMode();
    Calendar=ncBAN.getCalendar();

    BAN_Identifier=ncBAN.getBanIdentifier();
    Action=ncBAN.getAction();
    ncBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
   %>

<script language="JavaScript">
<!--
function showUnusedCustomers()
{
  unusedCust.style.visibility = "visible";
}

function customerNotFound()
{
  alert('Please contact x@cw.com for further assistance');
}

//-->
</script>

   <table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
      <td>
	<form name="CustomerBAN" method="post" action="CustomerBANHandler.jsp">
        <%=HB.getBANMenu_Bar("GCBCustomerBAN",Mode,Action,50)%>
        <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
     <tr>
      <td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td valign="top" align="left" NOWRAP width=10><!--Formerly Menu Column--></td>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> a Customer</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(ncBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" colspan=2>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td height="22" class=optional>Billing Advice Status</td>
			<td height="22" class=bluebold>Draft</td>
			<td class=optional>Requested By</td>
			<td class=bluebold><%=session.getAttribute("User_Name")%></td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=ncBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=ncBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td class=<%=ncBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("CustomerBAN", "BANEffectiveDate", BEDay, false, false)%>
                          <%=HB.getMonths("CustomerBAN", "BANEffectiveDate", BEMonth, false, false)%>
                          <%=HB.getYears("CustomerBAN", "BANEffectiveDate", BEYear, false, false)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
		        <td>
		        </td>
		        <td>
		        </td>
		      </tr>
		      <tr>
			</td>
			<td id="unusedCust" class=<%=ncBAN.getClass("Unused_Global_Customer")%>><b><small>Unused Global Customers
			  <br><%=DBA.getListBox("Unused_Global_Customer",InputMode,Global_Customer_Id,"All")%>&nbsp;
        		  <a href="javascript:customerNotFound()" title="Customer not found"><img src="../nr/cw/newimages/icon_help.gif" alt="Click here if the customer you require is not in this list either" border="0" width="16" height="16" align="middle"></a>
			</td>
		        <td class=<%=ncBAN.getClass("BPStartDateh")%>>Billing Period Start Date<br>
                          <%=HB.getDays("CustomerBAN", "BPStartDate", BPSDay, false, false)%>
                          <%=HB.getMonths("CustomerBAN", "BPStartDate", BPSMonth, false, false)%>
                          <%=HB.getYears("CustomerBAN", "BPStartDate", BPSYear, false, false)%>
                          <input type="hidden" name="BPStartDateh" value="<%=Required_BP_Start_Date%>">
		        </td>
			<td class=<%=ncBAN.getClass("Global_Billing_Analyst")%>><b><small>Global Billing Analyst<br>
			  <%=DBA.getListBox("Global_Billing_Analyst",InputMode,Global_Billing_Analyst,"All")%>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=ncBAN.getClass("Sales_Business_Unit")%>><b><small>Sales Business Unit<br>
			  <%=DBA.getListBox("Sales_Business_Unit",InputMode,Sales_Business_Unit,"Sales Business Unit")%>
			</td>
		        <td class=<%=ncBAN.getClass("Automated_Back_Billing")%>><b><small>Automated Back Billing Required to<br>
			  <%=DBA.getListBox("Automated_Back_Billing",InputMode,Automated_Back_Billing,"Auto Back Billing")%>
			</td>
		        <td valign="top" class=<%=ncBAN.getClass("DOB_Date")%>><b><small>DOB Date<br>
			  <%=DBA.getListBox("DOB_Date",InputMode,DOB_Date,"DOB Date")%>
			</td>
		      </tr>
		      <tr>
			<td valign="top" class=<%=ncBAN.getClass("Global_Account_Manager")%>><b><small>Global Account Manager
			  <br><input class=inp type=text name="Global_Account_Manager" size=40 maxlength=255 value="<%=Global_Account_Manager%>" <%=InputMode%>>
			</td>
			<td  class=<%=ncBAN.getClass("Global_Account_Manager")%>><b><small>Global Service Delivery Manager(s)
			  <br><input class=inp type=text name="Global_SD_Manager1" size=40 maxlength=255 value="<%=Global_Account_Manager%>" <%=InputMode%>>
			  <br><input class=inp type=text name="Global_SD_Manager2" size=40 maxlength=255 value="<%=Global_Account_Manager%>" <%=InputMode%>>
			</td>
		    </table><!--4-->
		  </td>
		</tr>
	      </table><!--3-->
	    </td>
	  </tr>
        </table><!--2-->
        </form>
      </td>
    </tr>
     <tr>
      <td height="100">&nbsp
      </td>
    </tr>
    <!--Footer-->
    <tr>
      <td>
        <c:import url="http://localhost:8080/shared/includes/Footer.htm"/>
      </td>
    </tr>
  </table><!--1-->
</body>
</html>