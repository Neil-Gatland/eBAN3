<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="manBAN" class="DBUtilities.ConglomManualInvoiceBANBean" scope="session"/>
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
    BAN_Summary = manBAN.getBAN_Summary();
    BAN_Reason = manBAN.getBAN_Reason();
    Required_BAN_Effective_Date=SU.isNull(manBAN.getRequired_BAN_Effective_Date(),"Today");
    Mode=manBAN.getMode();
    Action=manBAN.getAction();
    SelectMode=manBAN.getSelectMode();
    InputMode=manBAN.getInputMode();
    Calendar=manBAN.getCalendar();
    BAN_Identifier=manBAN.getBanIdentifier();
    manBAN.setBanCreatedBy((String)session.getAttribute("User_Name"));
    String Status = manBAN.getBanStatus();

    String billedProduct = manBAN.getBilledProduct();
    int bDay = manBAN.getBDay();
    int bMonth = manBAN.getBMonth();
    int bYear = manBAN.getBYear();
    String billDate = manBAN.getBillDate();
    //String billPeriodRef = manBAN.getBillPeriodRef();
    String sourceAccountNo = manBAN.getSourceAccountNo();
    String checkDigit = manBAN.getCheckDigit();
    String sourceInvoiceNo = manBAN.getSourceInvoiceNo();
    String billMonth = manBAN.getBillMonth();

    String oneOffCharges = manBAN.getOneOffCharges();
    String recurringCharges = manBAN.getRecurringCharges();
    String usageCharges = manBAN.getUsageCharges();
    String miscCharges = manBAN.getMiscCharges();

    String sourceDiscTotal = manBAN.getSourceDiscTotal();
    String invoiceNetAmount = manBAN.getInvoiceNetAmount();
    String invoiceVATAmount = manBAN.getInvoiceVATAmount();
    String invoiceTotalAmount = manBAN.getInvoiceTotalAmount();
    String installCharges = manBAN.getInstallCharges();
    String rentalCharges = manBAN.getRentalCharges();
    String callinkCharges = manBAN.getCallinkCharges();
    String vpnCharges = manBAN.getVPNCharges();
    String callCharges = manBAN.getCallCharges();
    String authCodeCharges = manBAN.getAuthCodeCharges();
    String easyUsageCharges = manBAN.getEasyUsageCharges();
    String easyQtrlyCharges = manBAN.getEasyQtrlyCharges();
    String sundryCharges = manBAN.getSundryCharges();
    String specialCharges = manBAN.getSpecialCharges();
    String conglomDiscountNetAmount = manBAN.getConglomDiscountNetAmountStr();
    String overrideVAT = manBAN.getOverrideVAT();
    boolean disableDate = false;

    String action;
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off","Conglom Desktop","Conglom Billing Menu","Conglom Manual Source Invoices"},
                            {"Submit","","Refresh","Raise Query"}};
//System.out.println(Mode);
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
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = Action + " a Conglomerate Manual Source Invoice";
    %>
<script language="JavaScript">
function overrideClicked()
{
<%if (!disableDate)
  {%>
  if (conglomInvoice.overrideVAT.checked == true)
  {
    conglomInvoice.invoiceVATAmount.readOnly = false;
  }
  else
  {
    conglomInvoice.invoiceVATAmount.readOnly = true;
  }
<%}%>
}
function checkSourceAccountNo()
{
  var la = new String(trimAll(conglomInvoice.sourceAccountNo.value));
  if (la.length == 0)
  {
    conglomInvoice.sourceAccountNo.value = la;
    conglomInvoice.checkDigitShow.value = "";
    conglomInvoice.checkDigit.value = "";
  }
  else
  {
    if ( (validateNumeric(la) == true) &&
         ( (la.length==8) || ((la.length==10)&&(la.substr(0,1)==5)) ) )
    {
      conglomInvoice.sourceAccountNo.value = la;
      //calculate check digit
      conglomInvoice.checkDigit.value = ((la.charAt(1)*1)+
        (la.charAt(2)*3)+(la.charAt(3)*5)+(la.charAt(4)*7)+(la.charAt(5)*11)+
        (la.charAt(6)*13)+(la.charAt(7)*17)) % 10;
      conglomInvoice.checkDigitShow.value =
        conglomInvoice.checkDigit.value;  
    }
    else
    {
        alert(  "Source Account must be numeric and either 8 digits in length "+
                "or 10 digits and start with a '5' (for managed services)" );
    }
  }
}
</script>
<form name="conglomInvoice" method="post" action="conglomInvoiceHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="checkDigit" type=hidden value="<%=checkDigit%>">
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
	        session.setAttribute("formname","conglomInvoice");
	      %>
    	        <%=SU.hasNoValue(manBAN.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
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
              <td valign=top>
                <table id="5" border="0" width="100%">
                  <tr>
                    <td class=<%=manBAN.getClass("sourceAccountNo")%>>Source Account (and Check Digit)
                      <br><input style="height:18px;font-size:xx-small;" type=text name="sourceAccountNo" value="<%=sourceAccountNo%>" <%=manBAN.getMode("sourceAccountNo")%> onChange="checkSourceAccountNo()">
                      <input style="width:20px;height:18px;font-size:xx-small;" type=text name="checkDigitShow" value="<%=checkDigit%>" readonly>
                    </td>
                    <td class=<%=manBAN.getClass("conglomBilledProduct3")%>><b>Billed Product
                      <br><%=DBA.getListBox("conglomBilledProduct3",manBAN.getSelectMode("conglomBilledProduct3"),billedProduct,conglomCustomerId,"cust")%>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("sourceInvoiceNo")%>>Source Invoice
                      <br><input style="height:18px;font-size:xx-small;" type=text name="sourceInvoiceNo" value="<%=sourceInvoiceNo%>" <%=manBAN.getMode("sourceInvoiceNo")%>>
                    </td>
                    <td valign="top" class=<%=manBAN.getClass("billDateh")%>>Bill Date<br>
                      <%=HB.getDays("conglomInvoice", "billDate", bDay, false, disableDate)%>
                      <%=HB.getMonths("conglomInvoice", "billDate", bMonth, false, disableDate)%>
                      <%=HB.getYears("conglomInvoice", "billDate", bYear, false, disableDate)%>
                      <input type="hidden" name="billDateh" value="<%=billDate%>">
                    </td>
                  </tr>
                </table>
              </td>
              <td>
                <table id="4" border="1" width="100%">
<%if ((billedProduct.equals("SSVO")) || (billedProduct.equals("PCBL")))
  {%>
                  <tr>
                    <td class=<%=manBAN.getClass("totals")%> rowspan="3">Charge Totals (net)</td>
                    <td class=<%=manBAN.getClass("installCharges")%>>Installs
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="installCharges" value="<%=installCharges%>" <%=manBAN.getMode("installCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("rentalCharges")%>>Rentals
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="rentalCharges" value="<%=rentalCharges%>" <%=manBAN.getMode("rentalCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("easyQtrlyCharges")%>>Easy Access Quarterly
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="easyQtrlyCharges" value="<%=easyQtrlyCharges%>" <%=manBAN.getMode("easyQtrlyCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("totals")%> rowspan="5">Usage Charge Types</td>
                    <td class=<%=manBAN.getClass("vpnCharges")%>>VPN
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="vpnCharges" value="<%=vpnCharges%>" <%=manBAN.getMode("vpnCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("callCharges")%>>Call/PSTN
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="callCharges" value="<%=callCharges%>" <%=manBAN.getMode("callCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("easyUsageCharges")%>>Easy Access
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="easyUsageCharges" value="<%=easyUsageCharges%>" <%=manBAN.getMode("easyUsageCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("callinkCharges")%>>Callink
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="callinkCharges" value="<%=callinkCharges%>" <%=manBAN.getMode("callinkCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("authCodeCharges")%>>Auth Code
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="authCodeCharges" value="<%=authCodeCharges%>" <%=manBAN.getMode("authCodeCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("totals")%> rowspan="2">Other Charge Types</td>
                    <td class=<%=manBAN.getClass("sundryCharges")%>>Sundry
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="sundryCharges" value="<%=sundryCharges%>" <%=manBAN.getMode("sundryCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("specialCharges")%>>Special
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="specialCharges" value="<%=specialCharges%>" <%=manBAN.getMode("specialCharges")%>>
                    </td>
                  </tr>
<%}
  else
  {%>
                  <tr>
                    <td class=<%=manBAN.getClass("totals")%> rowspan="4">Charge Totals (net)</td>
                    <td class=<%=manBAN.getClass("oneOffCharges")%>>One Off
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="oneOffCharges" value="<%=oneOffCharges%>" <%=manBAN.getMode("oneOffCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("recurringCharges")%>>Recurring
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="recurringCharges" value="<%=recurringCharges%>" <%=manBAN.getMode("recurringCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("usageCharges")%>>Usage
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="usageCharges" value="<%=usageCharges%>" <%=manBAN.getMode("usageCharges")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("miscCharges")%>>Misc.
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="miscCharges" value="<%=miscCharges%>" <%=manBAN.getMode("miscCharges")%>>
                    </td>
                  </tr>
<%}%>
                  <tr>
                    <td>&nbsp;</td>
                    <td class=<%=manBAN.getClass("sourceDiscTotal")%>>Source Discount
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="sourceDiscTotal" value="<%=sourceDiscTotal%>" <%=manBAN.getMode("sourceDiscTotal")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("totals")%> rowspan="3">Invoiced Amount</td>
                    <td class=<%=manBAN.getClass("invoiceNetAmount")%>>Net
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="invoiceNetAmount" value="<%=invoiceNetAmount%>" <%=manBAN.getMode("invoiceNetAmount")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("invoiceVATAmount")%>>VAT (override <input type=checkbox name=overrideVAT value=overrideVAT onClick="overrideClicked()" <%=manBAN.getInputMode("overrideVAT")%> <%=overrideVAT%>>)
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="invoiceVATAmount" value="<%=invoiceVATAmount%>" <%=manBAN.getMode("invoiceVATAmount")%>>
                    </td>
                  </tr>
                  <tr>
                    <td class=<%=manBAN.getClass("invoiceTotalAmount")%>>Total
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="invoiceTotalAmount" value="<%=invoiceTotalAmount%>" <%=manBAN.getMode("invoiceTotalAmount")%>>
                    </td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td class=<%=manBAN.getClass("conglomDiscountNetAmount")%>>Conglom Discount
                    </td>
                    <td>
                      <input style="height:18px;font-size:xx-small;" type=text name="conglomDiscountNetAmount" value="<%=conglomDiscountNetAmount%>" <%=manBAN.getMode("conglomDiscountNetAmount")%>>
                    </td>
                  </tr>
                </table>
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


