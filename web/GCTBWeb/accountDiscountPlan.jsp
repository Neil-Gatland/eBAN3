<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  session.setAttribute("formname","accountDiscountPlan");
  String SQL;
  //DBAccess DBA = new DBAccess();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
	boolean disableDate = false;
	String mode = "Create";
	String discountType = "";
	String discountPct = "0.0";
	String InputMode = "submit";
	String globalCustomerId = BAN.getGlobalCustomerId();
	String accountId = BAN.getAccountId();
	String requiredDStartDate = "";
	String requiredDEndDate = "";
	int dSDay = 0;
	int dSMonth = 0;
	int dSYear = 0;
	int dEDay = 0;
	int dEMonth = 0;
	int dEYear = 0;

%>
<script language="JavaScript">
  function addarrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Add not allowed");
  <%}
    else
    {%>
    if (accountDiscountPlan.CDC2E.selectedIndex == -1)
    {
      alert("Please select a charge description code to add");
    }
    else
    {
      accountDiscountPlan.ButtonPressed.value="CDC Add";
      accountDiscountPlan.submit();
    }
  <%}%>
  }

  function removearrowClick()
  {
  <%if (disableDate)
    {%>
    alert("Remove not allowed");
  <%}
    else
    {%>
    if (accountDiscountPlan.ECDC.selectedIndex == -1)
    {
      alert("No account selected for removal");
    }
    else
    {
      accountDiscountPlan.ButtonPressed.value="CDC Remove";
      accountDiscountPlan.submit();
    }
  <%}%>
  }
</script>
<form name="accountDiscountPlan" method="post" action="accountDiscountPlanHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
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
	  <h2 align="left"><%=mode%> an Account Discount Plan</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="8" width=100% class=bluebold>
	       <%=(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))%>
<%
//(SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),"")))
session.setAttribute("Message","");
BAN.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td width="10%"><b>Customer:</b></td>
              <td width="40%" align="left" colspan=3 class=bluebold><%=BAN.getGlobalCustomerName()%></td>
              <td width="10%"><b>Account:</b></td>
              <td width="40%" colspan=3 class=bluebold><%=BAN.getAccountName()%></td>
            </tr>
            <tr>
              <td colspan="8">
							  <table width="100%" border="1" id=4>
                <tr>
									<td class=mandatory>Discount Type<br>
              			<%=DBA.getListBox("Discount_Type","",discountType,"A%","")%>
									</td>
									<td>&nbsp;</td>
									<td class=mandatory>Discount Percentage (0.5 to 99.0)<br>
										<input class=inp type=text name="discountPct" size="5"
                    	value="<%=discountPct%>" <%=InputMode%>>
                  </td>
                </tr>
                <tr>
                  	<td class=optional valign="top">
		    							Charge Description Codes to exclude<br>
		    							<%=DBA.getListBox("CDC2E",InputMode,"",
                    	globalCustomerId,accountId,3,
                    	"style=\"height:80\"",false)%>
                  	</td>
		  							<td align="center">
                    	<%=HB.getImageAsAnchor("addarrow")%><br>&nbsp;<br>
                    	<%=HB.getImageAsAnchor("removearrow")%>
		  							</td>
        	          <td class=optional valign="top">
		    							Excluded Charge Description Codes<br>
		    							<%=DBA.getListBox("ECDC",InputMode,"",
                      globalCustomerId,accountId,3,
                      "style=\"height:80\"",false)%>
              	    </td>
									</tr>
									<tr>
		        				<td class=mandatory>Discount Start Date<br>
                          <%=HB.getDays("accountDiscountPlan", "DStartDate", dSDay, false, false)%>
                          <%=HB.getMonths("accountDiscountPlan", "DStartDate", dSMonth, false, false)%>
                          <%=HB.getYears("accountDiscountPlan", "DStartDate", dSYear, false, false)%>
                          <input type="hidden" name="dStartDateh" value="<%=requiredDStartDate%>">
		        				</td>
										<td>&nbsp;</td>
		        				<td class=optional>Discount End Date<br>
                          <%=HB.getDays("accountDiscountPlan", "DEndDate", dEDay, false, false)%>
                          <%=HB.getMonths("accountDiscountPlan", "DEndDate", dEMonth, false, false)%>
                          <%=HB.getYears("accountDiscountPlan", "DEndDate", dEYear, false, false)%>
                          <input type="hidden" name="dEndDateh" value="<%=requiredDEndDate%>">
		        				</td>
									</tr>
							  </table><!--table 4-->
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


