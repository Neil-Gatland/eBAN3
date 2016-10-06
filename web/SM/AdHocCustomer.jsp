<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="noscBAN" class="DBUtilities.NostroCustomerBANBean" scope="session"/>

<%! String SQL;
    String ActionCustomer="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Product,Global_Customer_Id,Global_Customer_Name;
    String BAN_Summary,BAN_Reason,Product_Type="";
    String Required_BAN_Effective_Date;
    int BEDay, BEMonth, BEYear;
    String NostroRefs;

%>
<%//Set Initial values
    String Mode=noscBAN.getMode();
    String Action=noscBAN.getAction();
    SelectMode=noscBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=noscBAN.getInputMode();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=noscBAN.getGlobalCustomerId();
    Global_Customer_Name=noscBAN.getGlobalCustomerName();
    BAN_Summary = noscBAN.getBAN_Summary();
    BAN_Reason = noscBAN.getBAN_Reason();
    BEDay = noscBAN.getBANEffectiveDateDays();
    BEMonth = noscBAN.getBANEffectiveDateMonths();
    BEYear = noscBAN.getBANEffectiveDateYears();
    Required_BAN_Effective_Date=SU.isNull(noscBAN.getRequired_BAN_Effective_Date(),"Today");
    NostroRefs=noscBAN.getNostroRefs();
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
	<form name="AdHocCustomer" method="post" action="AdHocCustomerHandler.jsp">
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
		    <h3 align="center"><%=Mode%> a Customer</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(noscBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			</td>
		      </tr>
		      <tr>
			<td colspan=4>
			  <hr>
			</td>
		      </tr>
                      <tr>
                        <td>
                          <span style="VISIBILITY: <%=noscBAN.getRejectVisibility()%>">
                            <b><small><%=noscBAN.getBanStatus()%> Reason
                            <br><textarea class=red cols="40" rows="2" READONLY><%=noscBAN.getRejectReason()%></textarea>
                          </span>
                        </td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan=4 align=center class=<%=noscBAN.getClass("CustomerName")%> valign="top">
                          <b><small>Customer Name<br>
                          <input class=inp type=text name="New_Nostro_Reference" size="35" <%=InputMode%>>
                        </td>
                      </tr>
                      <tr>
                        <td colspan=4>&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan=4 align=center>
                          <%=HB.getImageAsAnchor("submit")%>
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


