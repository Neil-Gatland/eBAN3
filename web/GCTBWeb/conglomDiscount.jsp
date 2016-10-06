<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="disBAN" class="DBUtilities.ConglomDiscountBANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    String BAN_Identifier;
    String Required_BAN_Effective_Date,BAN_Summary,BAN_Reason;
    String SelectMode="",Calendar="",InputMode="",Mode="",Action="";

    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();

    String conglomCustomerId=Long.toString(BAN.getConglomCustomerId());
    String conglomCustomerName=BAN.getConglomCustomerName();
//Initialization
    //Get values from last submission
    BAN_Summary = disBAN.getBAN_Summary();
    BAN_Reason = disBAN.getBAN_Reason();
    Required_BAN_Effective_Date=SU.isNull(disBAN.getRequired_BAN_Effective_Date(),"Today");
    Mode=disBAN.getMode();
    Action=disBAN.getAction();
    SelectMode=disBAN.getSelectMode();
    InputMode=disBAN.getInputMode();
    Calendar=disBAN.getCalendar();
    BAN_Identifier=disBAN.getBanIdentifier();
    disBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = disBAN.getBanStatus();

    String billedProduct = disBAN.getBilledProduct();
    String discountType = disBAN.getDiscountType();
    String discountPct = String.valueOf(disBAN.getDiscountPct());
    String appliedPct = String.valueOf(disBAN.getAppliedPct());
    String leadAccount = disBAN.getLeadAccount();
    String checkDigit = disBAN.getCheckDigit();
    String billedProductDesc = disBAN.getBilledProductDesc();
    String discountTypeDesc = disBAN.getDiscountTypeDesc();
    int efDay = disBAN.getEFDay();
    int efMonth = disBAN.getEFMonth();
    int efYear = disBAN.getEFYear();
    String effectiveFrom = disBAN.getEffectiveFrom();
    int etDay = disBAN.getETDay();
    int etMonth = disBAN.getETMonth();
    int etYear = disBAN.getETYear();
    String effectiveTo = disBAN.getEffectiveTo();
    boolean disableFromDate = false;
    boolean disableToDate = false;

    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu","Conglom Discounts"},
                            {"Submit","","Refresh","Exclude Accounts","Raise Query"}};
//System.out.println(Mode);
    if (Mode.equals("Confirm"))
    {
      Options[1][0] = "Confirm";
      Options[1][1] = "Cancel";
      Options[1][3] = "";
      disableFromDate = true;
      disableToDate = true;
    }
    else if (Mode.equals("View"))
    {
      Options[1][0] = "";
      Options[1][2] = "";
      Options[1][3] = "";
      disableFromDate = true;
      disableToDate = true;
    }
    else if (Mode.equals("Delete"))
    {
      Options[1][0] = "Delete";
      Options[1][2] = "";
      Options[1][3] = "";
      disableFromDate = true;
      disableToDate = true;
    }
    else if (Mode.equals("Amend"))
    {
      Options[1][0] = "Update";
      Options[1][2] = "";
      disableFromDate = true;
      disableToDate = false;
    }
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = Action + " a Conglomerate Billing Discount";
    %>
<script language="JavaScript">
function saveText(listName)
{
        //var x=document.getElementsByName(listName)
        var descName = listName + "Desc";
        var listObj = document.getElementById(listName);
        var descObj = document.getElementById(descName);
        //alert(listObj[listObj.selectedIndex].text);
        descObj.value = listObj[listObj.selectedIndex].text;
        //alert(descObj.value);
        conglomDiscount.submit();
}
function checkLeadAccount()
{
  var la = new String(trimAll(conglomDiscount.leadAccount.value));
  if (la.length == 0)
  {
    conglomDiscount.leadAccount.value = la;
    conglomDiscount.checkDigitShow.value = "";
    conglomDiscount.checkDigit.value = "";
  }
  else
  {
    if ((validateNumeric(la) == false) || (la.length != 8))
    {
      alert("Lead Account must be numeric and 8 digits in length");
    }
    else
    {
      conglomDiscount.leadAccount.value = la;
      //calculate check digit
      conglomDiscount.checkDigit.value = ((la.charAt(1)*1)+
        (la.charAt(2)*3)+(la.charAt(3)*5)+(la.charAt(4)*7)+(la.charAt(5)*11)+
        (la.charAt(6)*13)+(la.charAt(7)*17)) % 10;
      conglomDiscount.checkDigitShow.value =
        conglomDiscount.checkDigit.value;
    }
  }
}
</script>
<form name="conglomDiscount" method="post" action="conglomDiscountHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="discountId" type=hidden value="">
<input name="checkDigit" type=hidden value="<%=checkDigit%>">
<input name="conglomDiscountTypeDesc" type=hidden value="<%=discountTypeDesc%>">
<input name="conglomBilledProduct3Desc" type=hidden value="<%=billedProductDesc%>">
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
	  <h2 align="center"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="3" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","conglomDiscount");
	      %>
    	        <%=SU.hasNoValue(disBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
              <td>
            </tr>
            <tr>
              <td><b>Customer:</b></td>
              <td align=left class=bluebold><nobr><%=BAN.getGlobalCustomerId() + " " +
                 conglomCustomerName+" ("+conglomCustomerId+")"%></nobr></td>
              <td><b>&nbsp;</b></td>
            </tr>
            <tr>
              <td colspan="3">
                <hr>
              </td>
            </tr>
	    <tr>
              <td class=<%=disBAN.getClass("conglomDiscountType")%>><b>Discount Type
                <br><%=DBA.getListBox("conglomDiscountType",disBAN.getSelectMode("conglomDiscountType"),discountType,billedProduct)%>
              </td>
              <td class=<%=disBAN.getClass("conglomBilledProduct3")%>><b>Billed Product
                <br><%=DBA.getListBox("conglomBilledProduct3",disBAN.getSelectMode("conglomBilledProduct3"),billedProduct,conglomCustomerId,"cust")%>
              </td>
	    </tr>
	    <tr>
              <td valign="top" class=<%=disBAN.getClass("effectiveFromh")%>>Effective From<br>
                <%=HB.getDays("conglomDiscount", "effectiveFrom", efDay, false, disableFromDate)%>
                <%=HB.getMonths("conglomDiscount", "effectiveFrom", efMonth, false, disableFromDate)%>
                <%=HB.getYears("conglomDiscount", "effectiveFrom", efYear, false, disableFromDate)%>
                <input type="hidden" name="effectiveFromh" value="<%=effectiveFrom%>">
              </td>
              <td valign="top" class=<%=disBAN.getClass("effectiveToh")%>>Effective To<br>
                <%=HB.getDays("conglomDiscount", "effectiveTo", etDay, true, disableToDate)%>
                <%=HB.getMonths("conglomDiscount", "effectiveTo", etMonth, true, disableToDate)%>
                <%=HB.getYears("conglomDiscount", "effectiveTo", etYear, true, disableToDate)%>
                <input type="hidden" name="effectiveToh" value="<%=effectiveTo%>">
              </td>
	    </tr>
	    <tr>
              <td class=<%=disBAN.getClass("discountPct")%>><b>Discount Percentage
                <br><input style="height:18px;font-size:xx-small;" type=text name="discountPct" value="<%=discountPct%>" <%=disBAN.getMode("discountPct")%>>
              </td>
              <td class=<%=disBAN.getClass("appliedPct")%>><b>Percentage Applied To
                <br><input style="height:18px;font-size:xx-small;" type=text name="appliedPct" value="<%=appliedPct%>" <%=disBAN.getMode("appliedPct")%>>
              </td>
	    </tr>
	    <tr>
              <td class=<%=disBAN.getClass("leadAccount")%>><b>Lead Account
                <br><input style="height:18px;font-size:xx-small;" type=text name="leadAccount" value="<%=leadAccount%>" <%=disBAN.getMode("leadAccount")%> onChange="checkLeadAccount()">
              </td>
              <td class=<%=disBAN.getClass("checkDigit")%>><b>Check Digit
                <br><input style="height:18px;font-size:xx-small;" type=text name="checkDigitShow" value="<%=checkDigit%>" readonly>
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


