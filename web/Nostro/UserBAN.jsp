<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="nosuBAN" class="DBUtilities.NostroUserBANBean" scope="session"/>

<%! String SQL;
    String ActionCustomer="";
    String Message;
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent,SelectMode,InputMode,BAN_Identifier;
    String Product,Global_Customer_Id,Global_Customer_Name;
    String BAN_Summary,BAN_Reason,Product_Type="";
    String Required_BAN_Effective_Date, From_User_Valid_Date,
      To_User_Valid_Date;
    int BEDay, BEMonth, BEYear, FUVDay, FUVMonth, FUVYear, TUVDay, TUVMonth,
      TUVYear;
    String Nostro_User_Id="",User_Name="",User_Surname="";


%>
<%//Set Initial values
    String Mode=nosuBAN.getMode();//.equals("Add")?"Initiate":nosuBAN.getMode();
    String Action=nosuBAN.getAction();
    SelectMode=nosuBAN.getSelectMode();
    boolean disableDate=SelectMode.equals("DISABLED")?true:false;
    InputMode=nosuBAN.getInputMode();

  //Are we coming from the handler page, or a fresh start?

  //Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
    Message=SU.isNull((String)session.getAttribute("Message"),"");
    PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
    Global_Customer_Id=nosuBAN.getGlobalCustomerId();
    Global_Customer_Name=nosuBAN.getGlobalCustomerName();
    BAN_Summary = nosuBAN.getBAN_Summary();
    BAN_Reason = nosuBAN.getBAN_Reason();
    BEDay = nosuBAN.getBANEffectiveDateDays();
    BEMonth = nosuBAN.getBANEffectiveDateMonths();
    BEYear = nosuBAN.getBANEffectiveDateYears();
    FUVDay = nosuBAN.getUserStartDateDays();
    FUVMonth = nosuBAN.getUserStartDateMonths();
    FUVYear = nosuBAN.getUserStartDateYears();
    TUVDay = nosuBAN.getUserEndDateDays();
    TUVMonth = nosuBAN.getUserEndDateMonths();
    TUVYear = nosuBAN.getUserEndDateYears();
    Required_BAN_Effective_Date=SU.isNull(nosuBAN.getRequired_BAN_Effective_Date(),"Today");
    From_User_Valid_Date=SU.isNull(nosuBAN.getUserStartDate(),"Today");
    To_User_Valid_Date=SU.isNull(nosuBAN.getUserEndDate(),"");
    Nostro_User_Id=nosuBAN.getNostroUserId();
    User_Name=nosuBAN.getUserName();
    User_Surname=nosuBAN.getUserSurname();
%>
  <script language="JavaScript">
  <!--
  //-->
  </script>
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
      <td colspan=2>
	<form name="UserBAN" method="post" action="UserBANHandler.jsp">
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
		    <h3 align="center"><%=Mode%> a User</h3>
		    <%=SU.hasValue(BAN_Identifier,"</td></tr><tr><td><font color=darkblue><b>BAN Id:- "+BAN_Identifier+"<td>")+SU.isBlank(nosuBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b>")%>
		  </td>
		</tr>
		<tr>
		  <td valign="top" height="341" colspan=2>
		    <table width="100%" border="0"  cellpadding="0" cellspacing="0" id=4>
		      <tr>
			<td colspan=4>
			  <table width=100%>
                            <!--
			    <tr>
			      <td><b>Product:</b></td>
			      <td class=bluebold><%=nosuBAN.getProductName()%></td>
			      <td cosplan=2>&nbsp;</td>
			    </tr>
                            -->
			    <tr>
			      <td><b>Customer:</b></td>
			      <td class=bluebold><%=Global_Customer_Name%></td>
			      <td><b>Billing Customer Reference:</b></td>
			      <td class=bluebold><%=Global_Customer_Id%></td>
			    </tr>
			    <tr>
			      <td><b>Billing Advice Status:</td>
			      <td class=bluebold><%=nosuBAN.getBanStatus()%></td>
			      <td><b>Requested By:</td>
			      <td class=bluebold>
				<%=SU.isNull(nosuBAN.getBanCreatedBy(),(String)session.getAttribute("User_Name"))%><br>
			      </td>
			    </tr>
			    <tr>
			      <td><b>Account Name:</td>
			      <td class=bluebold><%=nosuBAN.getAccountName()%></td>
			      <td><b>Account Id:</td>
			      <td class=bluebold><%=nosuBAN.getAccount_Id()%></td>
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
			<td class=<%=nosuBAN.getClass("BAN_Summary")%>>
			  <b><small>BAN summary
		          <br><textarea class=tworows name="BAN_Summary" cols="40" rows="2" <%=nosuBAN.getMode("BAN_Summary")%>><%=BAN_Summary%></textarea>
		        </td>
                        <!--
		        <td class=<%=nosuBAN.getClass("BAN_Reason")%>>
			  <b><small>BAN Reason
		          <br><textarea class=tworows name="BAN_Reason" cols="40" rows="2" <%=nosuBAN.getMode("BAN_Reason")%>><%=BAN_Reason%></textarea>
		        </td>
                        -->
		        <td class=<%=nosuBAN.getClass("BANEffectiveDateh")%>>BAN Effective Date<br>
                          <%=HB.getDays("UserBAN", "BANEffectiveDate", BEDay, false, disableDate)%>
                          <%=HB.getMonths("UserBAN", "BANEffectiveDate", BEMonth, false, disableDate)%>
                          <%=HB.getYears("UserBAN", "BANEffectiveDate", BEYear, false, disableDate)%>
                          <input type="hidden" name="BANEffectiveDateh" value="<%=Required_BAN_Effective_Date%>">
		        </td>
			<td>
			  <span style="VISIBILITY: <%=nosuBAN.getRejectVisibility()%>">
			    <b><small><%=nosuBAN.getBanStatus()%> Reason
		            <br><textarea class=red cols="40" rows="2" READONLY><%=nosuBAN.getRejectReason()%></textarea>
			  </span>
		        </td>
                        <td>&nbsp;</td>
		      </tr>
		<tr>
                  <td colspan=4>&nbsp;
                  </td>
                </tr>
                  <td class=<%=nosuBAN.getClass("Nostro_User_Id")%>>
		    <b><small>User Id<br>
		    <input class=inp type=text name="Nostro_User_Id" size="35"
                      value="<%=Nostro_User_Id%>" readonly>
                  </td>
		</tr>
                <!--
                </tr>
                  <td class=<%=nosuBAN.getClass("User_Name")%>>
		    <b><small>User Given Name<br>
		    <input class=inp type=text name="User_Name" size="35"
                      value="<%=User_Name%>" <%=InputMode%>>
                  </td>
                  <td class=<%=nosuBAN.getClass("User_Surname")%>>
		    <b><small>User Surname<br>
		    <input class=inp type=text name="User_Surname" size="35"
                      value="<%=User_Surname%>" <%=InputMode%>>
                  </td>
		</tr>
                -->
		<tr>
                  <td class=<%=nosuBAN.getClass("UserStartDateh")%>>User Valid From Date<br>
                    <%=HB.getDays("UserBAN", "UserStartDate", FUVDay, false, disableDate)%>
                    <%=HB.getMonths("UserBAN", "UserStartDate", FUVMonth, false, disableDate)%>
                    <%=HB.getYears("UserBAN", "UserStartDate", FUVYear, false, disableDate)%>
                    <input type="hidden" name="UserStartDateh" value="<%=From_User_Valid_Date%>">
                  </td>
                  <td class=<%=nosuBAN.getClass("UserEndDateh")%>>User Valid To Date<br>
                    <%=HB.getDays("UserBAN", "UserEndDate", TUVDay, true, disableDate)%>
                    <%=HB.getMonths("UserBAN", "UserEndDate", TUVMonth, true, disableDate)%>
                    <%=HB.getYears("UserBAN", "UserEndDate", TUVYear, true, disableDate)%>
                    <input type="hidden" name="UserEndDateh" value="<%=To_User_Valid_Date%>">
                  </td>
		</tr>
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


