<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="PRO" class="DBUtilities.ProviderBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","providerMaint");
  String SQL;
  String Mode=PRO.getMode();
  String providerId = PRO.getProviderId();

  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Reference Data Menu",
    "Missing Data Identification",providerId.equals("-1")?"":"Master Account Maintenance"},
    {"Submit","","Refresh","View All Processes"}};

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][1] = "";
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][1] = "Cancel";
  }
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("productKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="providerMaint" method="post" action="providerMaintHandler.jsp">
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
	  <h3 align="center">Provider Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="4" width=100% class=bluebold>
    	        <%=SU.hasNoValue(PRO.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
PRO.setMessage("");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan=4>
                <table width="100%" border="0">
                  <tr>
                    <td><b>Call Month:</b></td>
                    <td class=bluebold><%=RSB.getLatestCallMonth()%></td>
                    <td><b>Current Status:</b></td>
                    <td class=bluebold><%=RSB.getCurrentStatus()%></td>
                    <td><b>RQR09 Load Time:</b></td>
                    <td class=bluebold><%=RSB.getRQR09LoadTime()%></td>
                  </tr>
                  <tr>
                    <td><b>Rating Time:</b></td>
                    <td class=bluebold><%=RSB.getRatingTime()%></td>
                    <td><b>Invoicing Time:</b></td>
                    <td class=bluebold><%=RSB.getInvoicingTime()%></td>
                    <td><b>Ebilling Upload Time:</b></td>
                    <td class=bluebold><%=RSB.getEbillingUploadTime()%></td>
                  </tr>
                  <tr>
                    <td colspan="6">
                      <hr>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
<!--
              <td class=<%=PRO.getClass("providerId")%>><b>Provider Id
                <br><input style="height:18px;font-size:xx-small;" type=text name="providerId" value="<%=PRO.getProviderId()%>" <%=PRO.getMode("providerId")%>>
              </td>
-->
              <td colspan=2 class=<%=PRO.getClass("providerName")%>><b>Provider Name
                <br><input style="height:18px;font-size:xx-small;width:40%;" type=text name="providerName" value="<%=PRO.getProviderName()%>" <%=PRO.getMode("providerName")%>>
<%if (!providerId.equals("-1"))
  {%>
                (Id: <%=providerId%>)
<%}%>

              </td>
              <td class=<%=PRO.getClass("npid")%>><b>NPID
                <br><input style="height:18px;font-size:xx-small;" type=text name="npid" value="<%=PRO.getNPID()%>" <%=PRO.getMode("npid")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine1")%>><b>Address Line 1
                <br><input style="height:18px;font-size:xx-small;width:60%;" type=text name="addressLine1" value="<%=PRO.getAddressLine1()%>" <%=PRO.getMode("addressLine1")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine2")%>><b>Address Line 2
                <br><input style="height:18px;font-size:xx-small;width:60%;" type=text name="addressLine2" value="<%=PRO.getAddressLine2()%>" <%=PRO.getMode("addressLine2")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine3")%>><b>Address Line 3
                <br><input style="height:18px;font-size:xx-small;width:60%;" type=text name="addressLine3" value="<%=PRO.getAddressLine3()%>" <%=PRO.getMode("addressLine3")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine4")%>><b>Address Line 4
                <br><input style="height:18px;font-size:xx-small;width:60%;" type=text name="addressLine4" value="<%=PRO.getAddressLine4()%>" <%=PRO.getMode("addressLine4")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine5")%>><b>Address Line 5
                <br><input style="height:18px;font-size:xx-small;width:60%;" type=text name="addressLine5" value="<%=PRO.getAddressLine5()%>" <%=PRO.getMode("addressLine5")%>>
              </td>
	    </tr>
            <tr>
              <td colspan="4" class=<%=PRO.getClass("addressLine6")%>><b>Postcode
                <br><input style="height:18px;font-size:xx-small;" type=text name="addressLine6" value="<%=PRO.getAddressLine6()%>" <%=PRO.getMode("addressLine6")%>>
              </td>
	    </tr>
            <tr>
              <td class=<%=PRO.getClass("bankDetailsReturned")%>><b>Bank Details Returned
                <br><%=DBA.getListBox("bankDetailsReturned",PRO.getSelectMode(),PRO.getBankDetailsReturned(),"",1,"style=\"width:15%\"",true)%>
              </td>
              <td class=<%=PRO.getClass("bankSortCode")%>><b>Bank Sort Code
                <br><input style="height:18px;font-size:xx-small;" maxlength="2" size="2" type=text name="bankSortCode1" value="<%=PRO.getBankSortCode1()%>" <%=PRO.getMode("bankSortCode")%>> -
                <input style="height:18px;font-size:xx-small;" maxlength="2" size="2" type=text name="bankSortCode2" value="<%=PRO.getBankSortCode2()%>" <%=PRO.getMode("bankSortCode")%>> -
                <input style="height:18px;font-size:xx-small;" maxlength="2" size="2" type=text name="bankSortCode3" value="<%=PRO.getBankSortCode3()%>" <%=PRO.getMode("bankSortCode")%>>
              </td>
              <td class=<%=PRO.getClass("bankAccountNumber")%>><b>Bank Account Number
                <br><input style="height:18px;font-size:xx-small;" maxlength="10" type=text name="bankAccountNumber" value="<%=PRO.getBankAccountNumber()%>" <%=PRO.getMode("bankAccountNumber")%>>
              </td>
	    </tr>
            <tr>
              <td class=<%=PRO.getClass("vatExemptInd")%>><b>VAT Exempt
                <br><%=DBA.getListBox("vatExemptInd",PRO.getMode("vatExemptInd"),PRO.getVATExemptInd(),"",1,"style=\"width:15%\"",true)%>
              </td>
              <td class=<%=PRO.getClass("vatNumber")%>><b>VAT Number
                <br><input style="height:18px;font-size:xx-small;" type=text name="vatNumber" value="<%=PRO.getVATNumber()%>" <%=PRO.getMode("vatNumber")%>>
              </td>
              <td class=<%=PRO.getClass("sapVendorNumber")%>><b>SAP Vendor Number
                <br><input style="height:18px;font-size:xx-small;" type=text name="sapVendorNumber" value="<%=PRO.getSAPVendorNumber()%>" <%=PRO.getMode("sapVendorNumber")%>>
              </td>
	    </tr>
            <tr>
              <td class=<%=PRO.getClass("selfBillAgreement")%>><b>Self-Bill Agreement
                <br><%=DBA.getListBox("selfBillAgreement",PRO.getMode("selfBillAgreement"),PRO.getSelfBillAgreement(),"",1,"style=\"width:15%\"",true)%>
              </td>
              <td class=<%=PRO.getClass("customerContract")%>><b>Customer Contract
                <br><%=DBA.getListBox("customerContract",PRO.getMode("customerContract"),PRO.getCustomerContract(),"",1,"style=\"width:15%\"",true)%>
              </td>
              <td class=<%=PRO.getClass("sapUpload")%>><b>Upload to SAP
                <br><%=DBA.getListBox("sapUpload",PRO.getMode("sapUpload"),PRO.getSAPUpload(),"",1,"style=\"width:15%\"",true)%>
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


