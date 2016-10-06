<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="cbpBAN" class="DBUtilities.ConglomBilledProductBANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  session.setAttribute("formname","conglomBilledProductMaint");
  String SQL;
  //DBAccess DBA = new DBAccess();

  String action;
  String Menu_Headings[]={"Go To", "Options"/*, "Admin"*/};
  String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu","Conglom Cust Profile Maintenanance"},
                            {"Submit","","Refresh","Raise Query"}};

  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String billedProduct = cbpBAN.getBilledProductId();
  String extractFrequency = cbpBAN.getBilledProductExtractFreq();
  String leadAccount = cbpBAN.getLeadAccount();
  String checkDigit = cbpBAN.getCheckDigit();
  String goldfishConglomId = cbpBAN.getGoldfishConglomId();
  String displayCustomer = BAN.getGlobalCustomerId() + " " +
    BAN.getConglomCustomerName() + " (" + BAN.getConglomCustomerId() + ")";
  String feedSource = cbpBAN.getBilledProductFeedSource();
  boolean disableDate = false;
  int bsDay = cbpBAN.getBSDay();
  int bsMonth = cbpBAN.getBSMonth();
  int bsYear = cbpBAN.getBSYear();
  int bcDay = cbpBAN.getBCDay();
  int bcMonth = cbpBAN.getBCMonth();
  int bcYear = cbpBAN.getBCYear();
  String billingStartDate = cbpBAN.getBillingStartDate();
  String billingCeaseDate = cbpBAN.getBillingCeaseDate();
  String Mode=cbpBAN.getMode();
  String Action=cbpBAN.getAction();
  String SelectMode=cbpBAN.getSelectMode();
  String InputMode=cbpBAN.getInputMode();
  String sortOrder = "";
  String allSources = Action.equals("Add")?"all":"";

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
    disableDate = true;
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][2] = "";
    disableDate = true;
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][2] = "";
    disableDate = true;
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][2] = "";
    //disableDate = true;
  }

%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function checkLeadAccount()
{
  var la = new String(trimAll(conglomBilledProductMaint.leadAccount.value));
  if (la.length == 0)
  {
    conglomBilledProductMaint.leadAccount.value = la;
    conglomBilledProductMaint.checkDigitShow.value = "";
    conglomBilledProductMaint.checkDigit.value = "";
  }
  else
  {
    if ((validateNumeric(la) == false) || (la.length != 8))
    {
      alert("Lead Account must be numeric and 8 digits in length");
    }
    else
    {
      conglomBilledProductMaint.leadAccount.value = la;
      //calculate check digit
      conglomBilledProductMaint.checkDigit.value = ((la.charAt(1)*1)+
        (la.charAt(2)*3)+(la.charAt(3)*5)+(la.charAt(4)*7)+(la.charAt(5)*11)+
        (la.charAt(6)*13)+(la.charAt(7)*17)) % 10;
      conglomBilledProductMaint.checkDigitShow.value =
        conglomBilledProductMaint.checkDigit.value;
    }
  }
}
</script>
<form name="conglomBilledProductMaint" method="post" action="conglomBilledProductMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="checkDigit" type=hidden value="<%=checkDigit%>">
<input name="billedProductExtractFreq" type=hidden value="<%=extractFrequency%>">
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
	  <h2 align="center">Conglomerate Customer Profile - <%=Action%> Product</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="6" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","conglomBilledProductMaint");
	      %>
    	        <%=SU.hasNoValue(cbpBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
              <td>
            </tr>
            <tr>
              <td width="15%"><b>Customer:</b></td>
              <td align=left class=bluebold><%=displayCustomer%></td>
              <td colspan=4>
                &nbsp;
              </td>
            </tr>
            <tr>
              <td colspan=6>
                <hr>
              </td>
            </tr>
            <tr>
              <td colspan=6>
                <table border=0 width=100%>
                  <tr>
                    <td class=<%=cbpBAN.getClass("conglomBilledProduct")%>><b>Product Name
                      <br><%=DBA.getListBox("conglomBilledProduct",cbpBAN.getSelectMode("conglomBilledProduct"),
                        billedProduct,feedSource,allSources,1,"",true)%>
                    </td>
                    <td class=<%=cbpBAN.getClass("billedProductExtractFreq")%>><b>Extract Frequency
                      <br><input style="height:18px;font-size:xx-small;" type=text name="extractFrequencyShow" value="<%=extractFrequency%>" readonly>
                    </td>
                  </tr>
                  <tr>
                    <td valign="top" class=<%=cbpBAN.getClass("billingStartDateh")%>>Billing Start Date<br>
                      <%=HB.getDays("conglomBilledProductMaint", "billingStartDate", bsDay, false, disableDate)%>
                      <%=HB.getMonths("conglomBilledProductMaint", "billingStartDate", bsMonth, false, disableDate)%>
                      <%=HB.getYears("conglomBilledProductMaint", "billingStartDate", bsYear, false, disableDate)%>
                      <input type="hidden" name="billingStartDateh" value="<%=billingStartDate%>">
                    </td>
                    <td valign="top" class=<%=cbpBAN.getClass("billingCeaseDateh")%>>Billing Cease Date<br>
                      <%=HB.getDays("conglomBilledProductMaint", "billingCeaseDate", bcDay, true, disableDate)%>
                      <%=HB.getMonths("conglomBilledProductMaint", "billingCeaseDate", bcMonth, true, disableDate)%>
                      <%=HB.getYears("conglomBilledProductMaint", "billingCeaseDate", bcYear, true, disableDate)%>
                      <input type="hidden" name="billingCeaseDateh" value="<%=billingCeaseDate%>">
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=cbpBAN.getClass("leadAccount")%>><b>Lead Account
                      <br><input style="height:18px;font-size:xx-small;" type=text name="leadAccount" value="<%=leadAccount%>" <%=cbpBAN.getMode("leadAccount")%> onChange="checkLeadAccount()">
                    </td>
                    <td class=<%=cbpBAN.getClass("checkDigit")%>><b>Check Digit
                      <br><input style="height:18px;font-size:xx-small;" type=text name="checkDigitShow" value="<%=checkDigit%>" readonly>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=cbpBAN.getClass("goldfishConglomId")%>><b>Goldfish Conglom Id
                      <br><input style="height:18px;font-size:xx-small;" type=text name="goldfishConglomId" value="<%=goldfishConglomId%>" <%=cbpBAN.getMode("goldfishConglomId")%>>
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


