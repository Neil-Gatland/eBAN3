<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="DBUtilities.DBAccess,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="siteBAN" class="DBUtilities.SiteBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>

<%  String SQL;
    String ActionCustomer="";
    String Message;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Product,Global_Customer_Id,Global_Customer_Name;
    String BAN_Summary,BAN_Reason,Product_Type="";
    String Required_BAN_Effective_Date;
    int BEDay, BEMonth, BEYear;
    String NostroRefs;

    String Mode = siteBAN.getMode();
    String Action = siteBAN.getAction();
    String Status = siteBAN.getBanStatus();
    SelectMode=siteBAN.getSelectMode();
    BAN_Identifier=siteBAN.getBanIdentifier();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=siteBAN.getInputMode();
    String Site_Reference = siteBAN.getSiteReference();
    String Site_Name = siteBAN.getSiteName();
    String Site_Address = siteBAN.getSiteAddress();
    String Country = siteBAN.getCountry();
    String Billing_Region = siteBAN.getBillingRegion();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=siteBAN.getGlobalCustomerId();
    Global_Customer_Name=siteBAN.getGlobalCustomerName();
    BAN_Summary = siteBAN.getBAN_Summary();
    BAN_Reason = siteBAN.getBAN_Reason();
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="SiteBAN" method="post" action="SiteBANHandler.jsp">
        <%=HB.getBANMenu_Bar("BAN",Mode,Action,(String)session.getAttribute("System"))%>

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
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
		<tr>
		  <td valign="top" colspan=2>
		    <h3 align="center"><%=Mode%> Site</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(siteBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
			    <tr>
			      <td><b>Customer:</b></td>
			      <td class=bluebold><%=Global_Customer_Name%></td>
			      <td colspan="2">&nbsp;</td>
			    </tr>
			    <tr>
			      <td><b>Billing Advice Status:</td>
			      <td class=bluebold><%=Status%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isBlank(siteBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			      </td>
			    </tr>
			  </table>
			</td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
		      <tr>
			<td class=<%=siteBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=siteBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
		        <td>
		        </td>
			<td>
			  <span style="VISIBILITY: <%=siteBAN.getRejectVisibility()%>">
			    <b><small><%=Status%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=siteBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
		      </tr>
		      <tr>
		        <td class=<%=siteBAN.getClass("Site_Reference")%>><b><small>Site Reference<br>
			  <input class=inp type="text" name="Site_Reference" maxlength="50" size="25" value="<%=Site_Reference%>" <%=siteBAN.getMode("Site_Reference")%>>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=siteBAN.getClass("Site_Name")%>><b><small>Site Name<br>
			  <input class=inp type="text" name="Site_Name" maxlength="100" size="50" value="<%=Site_Name%>" <%=siteBAN.getMode("Site_Name")%>>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=siteBAN.getClass("Site_Address")%>><b><small>Site Address<br>
			  <input class=inp type="text" name="Site_Address" maxlength="255" size="100" value="<%=Site_Address%>" <%=siteBAN.getMode("Site_Address")%>>
			</td>
		      </tr>
		      <tr>
 			<td valign="top" class=<%=siteBAN.getClass("Country")%>><b><small>Country<br>
			  <%=DBA.getListBox("Country",SelectMode,Country,"",
                          1," style=\"width:45%;\"",true)%>
			</td>
		      </tr>
		      <tr>
 			<td valign="top" class=<%=siteBAN.getClass("Billing_Region")%>><b><small>Billing Region<br>
			  <%=DBA.getListBox("Billing_Region",InputMode,Billing_Region,Global_Customer_Id,
                          1," style=\"width:45%;\"",true)%>
			</td>
		      </tr>
		      <tr>
		        <td class=<%=siteBAN.getClass("Geo_Code")%>><b><small>Geocode<br>
			  <input class=inp type="text" name="Geo_Code" maxlength="9" size="25" <%=siteBAN.getMode("Geo_Code")%> value="<%=siteBAN.getGeoCode()%>">
			</td>
		      </tr>
                   </table>
                  </table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
  </table><!--1-->
</body>
</html>


