<%//<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>*/%>
//<c:import url="http://localhost:8080/shared/includes/Page_Header1.htm"/>
%>
<%@ page import="DBUtilities.ServiceBANBean,HTMLUtil.HTMLBean,JavaUtil.StringUtil"%>
<%@ include file="../includes/Page_Header1.htm"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="svBAN" class="DBUtilities.ServiceBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
  <%!//Variables
    String SQL;
    String Global_Customer_Id,Global_Customer_Name,Invoice_Region,GSR,Service_Type,Site,Speed;
    String From_End_Code="",To_End_Code="";
    String Return_BAN_To_Name,BAN_Summary,BAN_Reason,Product_Type,Division_Id,Account_Id;
    String Required_BAN_Effective_Date,Live_Service_Date,Ready_For_Service_Date,Billing_Start_Date,Billing_End_Date;
    String BAN_Identifier="",Customer_Reference="",Service_Description="";
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    //Utilities.DBAccess DBA = new DBUtilities.DBAccess();
   %>
  <%//Initialization
    session.setAttribute("PageSent",request.getRequestURI());
    Global_Customer_Id=BAN.getGlobalCustomerId();
    Global_Customer_Name=svBAN.getGlobalCustomerName();
    Action=svBAN.getAction();

    Mode=svBAN.getMode();
    SelectMode=svBAN.getSelectMode();
    InputMode=svBAN.getInputMode();
    Calendar=svBAN.getCalendar();

    //Get values from last submission
    Return_BAN_To_Name=svBAN.getReturn_BAN_To_Name();
    BAN_Summary = svBAN.getBAN_Summary();
    BAN_Reason = svBAN.getBAN_Reason();
    Service_Type=svBAN.getService_Type();
    Speed=svBAN.getSpeed();
    Required_BAN_Effective_Date=SU.isNull(svBAN.getRequired_BAN_Effective_Date(),"Today");
    Live_Service_Date=SU.isNull(svBAN.getLive_Service_Date(),"Today");
    Ready_For_Service_Date=SU.isNull(svBAN.getReady_For_Service_Date(),"Today");
    Billing_Start_Date=SU.isNull(svBAN.getBilling_Start_Date(),"Today");
    Billing_End_Date=SU.isNull(svBAN.getBilling_End_Date(),"");
    Site=svBAN.getSite();
    Division_Id=svBAN.getDivision_Id();
    Product_Type=svBAN.getProduct_Type();
    From_End_Code=svBAN.getFrom_End_Code();
    To_End_Code=svBAN.getTo_End_Code();
    Customer_Reference=svBAN.getCustomer_Reference();
    Service_Description=svBAN.getService_Description();
   %>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%/*<c:import url="http://localhost:8080/shared/includes/Page_Header2.htm"/>*/%>
      <%@ include file="../includes/Page_Header2.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="ServiceBAN" method="post" action="ServiceBANHandler.jsp">
        <%=HB.getBANMenu_Bar("ServiceBAN",Mode,Action,"Data Billing")%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="RejectReason" type=hidden value="">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td valign="top" align="left" NOWRAP width=10><!--Formerly Menu Column--></td>
	    <!-- this is a spacer column-->
	    <td width="13">&nbsp;</td>
	    <td  valign ="Top"><!-- Main table starts here-->
	      <span id=formspan>
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> Service</h2>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(svBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <%=session.getAttribute("Error")%>
		    <%session.setAttribute("Error","");%>
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			<table width=100%>
			  <tr>
			    <td><b>Customer:</b></td>
			    <td class=bluebold><%=Global_Customer_Name%></td>
			    <td><b>Division:</b></td>
			    <td class=bluebold><%=Division_Id%></td>
			  </tr>
			  <tr>
			    <td><b>Billing Advice Status:</td>
			    <td class=bluebold><%=svBAN.getBanStatus()%></td>
			    <td><b>Requested By:</td>
			    <td class=bluebold>
			      <%=SU.isNull(svBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			    </td>
			  </tr>
			</table>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=svBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
			  <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=svBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
			</td>
			<td class=<%=svBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
			    <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=svBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
			</td>
			<td>
			</td>
			<td class=<%=svBAN.getClass("Required_BAN_Effective_Dateh")%>>Effective Date<br>
			  <input class=inp type="text" name="Required_BAN_Effective_Date" READONLY value=<%=Required_BAN_Effective_Date%>>
			  <a <%=svBAN.getMode("Effective_Date")%>href="javascript:show_calendar('CircuitBAN.Required_BAN_Effective_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			  <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a><br>
			  <input type=hidden name="Required_BAN_Effective_Dateh" value=<%=Required_BAN_Effective_Date%>>
			</td>
		      </tr>
		      <tr>
			<td class=<%=svBAN.getClass("Customer_Reference")%>>Customer Reference
			  <br><input class=inp type=text name="Customer_Reference" value="<%=Customer_Reference%>" <%=InputMode%>>
			</td>
			<td class=<%=svBAN.getClass("Product_Type")%> colspan=2><b><small>Product Type
			<br>
			  <%=DBA.getListBox("GCB_Product_Type",InputMode,Product_Type,"")%>
			</td>
			<td height="18" class=<%=svBAN.getClass("Service_Type")%> colspan=2><b><small>Service Type
			  <br>
			  <%=DBA.getListBox("Service_Type",InputMode,Service_Type,"")%>
			</td>
  		      </tr>
		      <tr>
			 <td class=<%=svBAN.getClass("Live_Service_Dateh")%>>Live Service Date<br>
			   <input class=inp type="text" name="Live_Service_Date" READONLY value=<%=Live_Service_Date%>>
			   <a <%=svBAN.getMode("Live_Service_Date")%>href="javascript:show_calendar('ServiceBAN.Live_Service_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			   <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a><br>
			   <input type=hidden name="Live_Service_Dateh" value=<%=Live_Service_Date%>>
			</td>
			   <td class=<%=svBAN.getClass("Ready_For_Service_Dateh")%>>Ready For Service Date<br>
			   <input class=inp type="text" name="Ready_For_Service_Date" READONLY value=<%=Ready_For_Service_Date%>>
			   <a <%=svBAN.getMode("Ready_For_Service_Date")%>href="javascript:show_calendar('ServiceBAN.Ready_For_Service_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			   <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a><br>
			   <input type=hidden name="Ready_For_Service_Dateh" value=<%=Ready_For_Service_Date%>>
			</td>
			<td class=<%=svBAN.getClass("Site")%> colspan=2><b><small>Site<br>
			  <%=DBA.getListBox("Site",InputMode,Site,Global_Customer_Id)%>
			</td>
		      </tr>
		      <tr>
			<td class=<%=svBAN.getClass("Service_Description")%> colspan="2">
			  <b><small>Service Description
			  <br><input class=longinp type=text name="Service_Description" width=200 <%=InputMode%> value="<%=Service_Description%>">
			</td>
			<td class=<%=svBAN.getClass("Speed")%> colspan=2><b><small>Speed
			  <br><textarea name="Speed" cols="50" rows=1><%=Speed%></textarea>
			</td>
		      </tr>
		      <tr>
			 <td class=<%=svBAN.getClass("Billing_Start_Dateh")%>>Billing Start Date<br>
			   <input class=inp type="text" name="Billing_Start_Date" READONLY value=<%=Billing_Start_Date%>>
			   <a <%=svBAN.getMode("Billing_Start_Date")%>href="javascript:show_calendar('ServiceBAN.Billing_Start_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			   <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a><br>
			   <input type=hidden name="Billing_Start_Dateh" value=<%=Billing_Start_Date%>>
			</td>
			   <td class=<%=svBAN.getClass("Billing_End_Dateh")%>>Billing End Date<br>
			   <input class=inp type="text" name="Billing_End_Date" READONLY value=<%=Billing_End_Date%>>
			   <a <%=svBAN.getMode("Billing_End_Date")%>href="javascript:show_calendar('ServiceBAN.Billing_End_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			   <img src="/shared/nr/cw/newimages/show_calendar.gif" border=0></a><br>
			   <input type=hidden name="Billing_End_Dateh" value=<%=Billing_End_Date%>>
			</td>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
		        <td colspan=4  class=bluebold>Site Details</td>
		      </tr>
		      <tr>
			<td class=<%=svBAN.getClass("From_End")%> colspan=2><b><small>From End<br>
			  <%=DBA.getListBox("From_End",InputMode,From_End_Code,Global_Customer_Id)%>
			</td>
			<td class=<%=svBAN.getClass("To_End")%> colspan=2><b><small>To End<br>
			  <%=DBA.getListBox("To_End",InputMode,To_End_Code,Global_Customer_Id)%>
			</td>
		      </tr>
		    </table><!--4-->
		    </span>
		  </td>
		</tr>
	      </table><!--3-->
	    </td>
	  </tr>
        </table><!--2-->
        </form>
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


