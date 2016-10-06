<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="ADJ" class="DBUtilities.AdjustmentBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","adjustmentMaint");
  String SQL;
  String Mode=ADJ.getMode();

  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Manual Adjustment Invoicing"},
    {"Submit","","","Refresh","View All Processes"}};

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
    Options[1][2] = "";
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
    Options[1][2] = "";
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][1] = "";
    Options[1][2] = "";
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
    Options[1][2] = "Add Adjustment Line";
  }
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);
  String userId = (String)session.getAttribute("User_Id");
  boolean disableDate = true;

%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("adjustmentLineKey.htm", "alk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=500,top=50,left=50");
  }
}
</script>
<form name="adjustmentMaint" method="post" action="adjustmentMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="invoiceNo" type=hidden value="">
<input name="lineNo" type=hidden value="">
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
	  <h3 align="center">Adjustment Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="4" width=100% class=bluebold>
    	        <%=SU.hasNoValue(ADJ.getMessage(),"")%>
<%
session.setAttribute("Message","");
ADJ.setMessage("");
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
              <td colspan=2 class=<%=ADJ.getClass("providerId")%>><b>Provider</b>
                <br><%=DBA.getListBox("providerId",ADJ.getMode("providerId"),ADJ.getProviderId(),(String)session.getAttribute("User_Id"),1,"style=\"width:80%\"",true)%>
              </td>
              <td colspan=2 class=<%=ADJ.getClass("masterAccountId")%>><b>Master Account</b>
                <br><%=DBA.getListBox("masterAccountId",ADJ.getMode("masterAccountId"),ADJ.getMasterAccountId(),ADJ.getProviderId(),1,"style=\"width:80%\"",true)%>
              </td>
            </tr>
            <tr>
              <td class=<%=ADJ.getClass("invoiceNumber")%>><b>Invoice Number</b>
                <br><input style="height:18px;font-size:xx-small;" maxlength=20 size=25 type=text name="invoiceNumber" value="<%=ADJ.getInvoiceNumber()%>" <%=ADJ.getMode("invoiceNumber")%>>
              </td>
              <td valign="top" class=<%=ADJ.getClass("taxPointDateh")%>>Tax Point Date<br>
                <%=HB.getDays("adjustmentMaint", "taxPointDate", ADJ.getTPDay(), false, disableDate)%>
                <%=HB.getMonths("adjustmentMaint", "taxPointDate",  ADJ.getTPMonth(), false, disableDate)%>
                <%=HB.getYears("adjustmentMaint", "taxPointDate",  ADJ.getTPYear(), false, disableDate)%>
                <input type="hidden" name="taxPointDateh" value="<%=ADJ.getTaxPointDate()%>">
              </td>
            </tr>
            <tr>
              <td class=<%=ADJ.getClass("adjustmentCRDE")%>><b>Adjustment Type</b>
                <br><%=DBA.getListBox("adjustmentCRDE",ADJ.getMode("adjustmentCRDE"),ADJ.getAdjustmentType(),"Credit Debit")%>
              </td>
<%if (ADJ.getAction().equals("Add"))
  {%>
              <td colspan=2 class=<%=ADJ.getClass("adjustmentDescription")%>><b>Adjustment Line Description</b>
                <br><input style="height:18px;font-size:xx-small;width:80%" maxlength=200 type=text name="adjustmentDescription" value="<%=ADJ.getAdjustmentDescription()%>" <%=ADJ.getMode("adjustmentDescription")%>>
              </td>
              <td class=<%=ADJ.getClass("adjustmentAmount")%>><b>Adjustment Line Amount</b>
                <br><input style="height:18px;font-size:xx-small;" onBlur="submit()" type=text name="adjustmentAmount" value="<%=ADJ.getAdjustmentAmount()%>" <%=ADJ.getMode("adjustmentAmount")%>>
              </td>
<%}%>
            </tr>
            <tr>
              <td class=<%=ADJ.getClass("netAmount")%>><b>Net Amount</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="netAmount" value="<%=ADJ.getNetAmount()%>" <%=ADJ.getMode("netAmount")%>>
              </td>
              <td class=<%=ADJ.getClass("vatAmount")%>><b>VAT Amount</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="vatAmount" value="<%=ADJ.getVATAmount()%>" <%=ADJ.getMode("vatAmount")%>>
              </td>
              <td class=<%=ADJ.getClass("vatRate")%>><b>VAT Rate %</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="vatRate" value="<%=ADJ.getVATRate()%>" <%=ADJ.getMode("vatRate")%>>
              </td>
              <td class=<%=ADJ.getClass("grossAmount")%>><b>Gross Amount</b>
                <br><input style="height:18px;font-size:xx-small;" type=text name="grossAmount" value="<%=ADJ.getGrossAmount()%>" <%=ADJ.getMode("grossAmount")%>>
              </td>
            </tr>
            <tr>
              <td colspan=4>&nbsp;</td>
	    </tr>
<%if (!ADJ.getAction().equals("Add"))
  {%>
            <tr>
              <td colspan=4>
                <table border=0 width="70%">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width="450">
                      <button class=grid_menu>Adjustment Line Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="180">
                      <button class=grid_menu>Adjustment Line Amount</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=3>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="adjustmentLineGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
<%}%>
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


