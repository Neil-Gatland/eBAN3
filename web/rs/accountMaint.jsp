<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String fromForm = SU.isNull((String)session.getAttribute("formname"), "");
  session.setAttribute("formname","accountMaint");
  String SQL;
  String Mode=ACC.getMode();
  String accountId = ACC.getAccountId();
  String masterAccountId = ACC.getMasterAccountId();
  String action;
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop","Reference Data Menu",
    "Missing Data Identification","Provider Maintenance","Master Account Maintenance"},
    {"Submit","","","","Refresh","View All Processes"}};

  if (Mode.equals("Confirm"))
  {
    Options[1][0] = "Confirm";
    Options[1][1] = "Cancel";
    Options[1][2] = "";
    Options[1][3] = "";
  }
  else if (Mode.equals("View"))
  {
    Options[1][0] = "";
    Options[1][1] = "";
    Options[1][2] = "";
    Options[1][3] = "";
  }
  else if (Mode.equals("Delete"))
  {
    Options[1][0] = "Delete";
    Options[1][1] = "";
    Options[1][2] = "";
    Options[1][3] = "";
    Options[1][4] = "";
  }
  else if (Mode.equals("Amend"))
  {
    Options[1][0] = "Update";
    Options[1][1] = "Add Product";
    Options[1][2] = "Move Account";
    Options[1][3] = "View Account Move History";
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
    window.open("accountProductKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=100,width=400,top=50,left=50");
  }
}
</script>
<form name="accountMaint" method="post" action="accountMaintHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="productId" type=hidden value="">
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
	  <h3 align="center">Account Maintenance</h3>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="3" width=100% class=bluebold>
    	        <%=SU.hasNoValue(ACC.getMessage(),"<font color=blue><b>Mandatory fields are shown in blue</b></font>")%>
<%
session.setAttribute("Message","");
ACC.setMessage("");
RSB.setMessage("");
%>
              </td>
            </tr>
            <tr>
              <td colspan=3>
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
                    <td><b>Master Account:</b></td>
                    <td class=bluebold><%=ACC.getMasterAccountName()%>/<%=ACC.getMasterAccountNumber()%> (<%=masterAccountId%>)</td>
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
              <td width="50%" colspan=2 class=<%=ACC.getClass("accountName")%>><b>Account Name</b>
                <br><input style="height:18px;font-size:xx-small;width:75%" type=text name="accountName" value="<%=ACC.getAccountName()%>" <%=ACC.getMode("accountName")%>>
<%if (!accountId.equals("-1"))
  {%>
                (Id: <%=accountId%>)
<%}%>
              </td>
              <td width="50%" class=<%=ACC.getClass("accountNumber")%>><b>Account Number</b>
                <br><input style="height:18px;font-size:xx-small;" maxlength="7" type=text name="accountNumber" value="<%=ACC.getAccountNumber()%>" <%=ACC.getMode("accountNumber")%>>
              </td>
            </tr>
            <tr>
              <td class=<%=ACC.getClass("invoiceInd")%>><b>Invoice</b>
                <br><%=DBA.getListBox("invoiceInd",ACC.getMode("invoiceInd"),ACC.getInvoiceInd(),"",1,"style=\"width:15%\"",true)%>
              </td>
            </tr>
            <tr>
              <td colspan=3>&nbsp;
              </td>
            </tr>
<%if (!accountId.equals("-1"))
  {%>
            <tr>
              <td colspan=3>
                <table border=0 width="70%">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width="100">
                      <button class=grid_menu>Product Code</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="400">
                      <button class=grid_menu>Product Description</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="100">
                      <button class=grid_menu>Product Group</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=4>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="accountProductGrid.jsp"></iframe>
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


