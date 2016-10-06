<html>
<head>
  <title>Update Conglomerate Bill Item Status</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
  String itemId = request.getParameter("itemId");
  String message = "<font color=blue>Select new status and press 'OK'</font>";
  String conglomCustName = BAN.getGlobalCustomerId() + " " +
    BAN.getConglomCustomerName() + " (" + BAN.getConglomCustomerId() + ")";
  String button1Title = "";
  String button1Text = "OK";
  String button2Text = "Exit";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  String product = request.getParameter("product");
  String sourceAccount = request.getParameter("sourceAccount");
  String status = "";
  String originalStatus = "";
  String reason = "";
  String mode = "input";
  String readonly = "";
  boolean success = false;
  if (!fromSelf.equals(""))
  {
    if (fromSelf.equals("confirm"))
    {
      button1Text = "Confirm";
      button2Text = "Cancel";
      button1OnClick = "confirmAndSub();";
      message = "<font color=blue>Press 'Confirm' to update the item</font>";
      status = request.getParameter("conglomItemStatus");
      originalStatus = request.getParameter("originalStatus");
      reason = request.getParameter("reason");
      mode = "disabled";
      readonly = "readonly";
    }
    else
    {
      status = request.getParameter("status");
      originalStatus = request.getParameter("originalStatus");
      reason = request.getParameter("storedReason");
      success = BAN.updateConglomBillItemStatus(itemId, status, reason,
        (String)session.getAttribute("User_Id"));
      message = BAN.getMessage();
      button1Text = "";
      button1OnClick = "";
    }
  }
  else
  {
    status = request.getParameter("status");
    originalStatus = status;
    BAN.getConglomItemStatusList(itemId);
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  if ((updateConglomItemStatus.conglomItemStatus.selectedIndex == -1) ||
     (updateConglomItemStatus.conglomItemStatus[updateConglomItemStatus.conglomItemStatus.selectedIndex].value == updateConglomItemStatus.originalStatus.value))
  {
    alert('Status has not been changed');
  }
  else
  {
    updateConglomItemStatus.fromSelf.value = "confirm";
    updateConglomItemStatus.submit();
  }
}

function confirmAndSub()
{
  updateConglomItemStatus.fromSelf.value = "update";
  updateConglomItemStatus.submit();
}


function window_onload()
{
  window.focus();
<%if ((fromSelf.equals("update")) && (success))
  {%>
  window.opener.location.href = "listConglomBillProd.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="updateConglomItemStatus" method="post" action="updateConglomItemStatus.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="refresh">
<input name="itemId" type=hidden value="<%=itemId%>">
<input name="product" type=hidden value="<%=product%>">
<input name="sourceAccount" type=hidden value="<%=sourceAccount%>">
<input name="status" type=hidden value="<%=status%>">
<input name="originalStatus" type=hidden value="<%=originalStatus%>">
<input name="storedReason" type=hidden value="<%=reason%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Update Conglomerate Bill Item Status</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td><b>Customer:</b></td>
          <td class=bluebold><%=conglomCustName%></td>
        </tr>
        <tr>
          <td><b>Product:</b></td>
          <td class=bluebold><%=product%></td>
          </td>
        </tr>
        <tr>
          <td><b>Source Account:</b></td>
          <td class=bluebold><%=sourceAccount%></td>
          </td>
        </tr>
        <tr>
          <td><b>Status:</b></td>
          <td class=bluebold><nobr><%=DBA.getListBox("conglomItemStatus",mode,status,"dummy", false)%></nobr></td>
        </tr>
        <tr>
          <td><b>Reason for change:</b></td>
          <td class=bluebold><input name="reason" value="<%=reason%>" <%=readonly%>></td>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <b><%=message%></b>
    </td>
  </tr>
  <tr>
    <td align=right>
      <input class=button title="<%=button1Title%>" type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center height="18px">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <table border=0 width=100% align="center">
        <tr class=gridHeader>
          <td class=gridHeader NOWRAP valign=top width=205>
            <button class=grid_menu>Change Reason</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=90>
            <button class=grid_menu>Previous Status</button>
          </td>
          <td class=gridHeader NOWRAP valign=top width=70>
            <button class=grid_menu>Updated By</button>
          </td>
          <td class=gridHeader NOWRAP valign=top>
            <button class=grid_menu>Updated Date</button>
          </td>
        </tr>
        <tr>
          <td colspan=4>
            <iframe frameborder=0 width="100%" height=250 id=GridData name=GridData src="conglomItemStatusGrid.jsp?itemId=<%=itemId%>"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


