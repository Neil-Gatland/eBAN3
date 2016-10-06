<html>
<head>
  <title>Move Account</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="ACC" class="DBUtilities.AccountBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  String selectStyle = " style=\"width:400px\"";
  String message ="<font color=blue><b>Please select Provider, followed by Master Account and then press 'Move'</b></font>";
  String providerId = "";
  String masterAccountId = "";
  String origMasterAccountId = "";
  String button1Text = "Move";
  String button2Text = "Cancel";
  String button1OnClick = "valAndSub();";
  String button2OnClick = "window.close();";
  String fromSelf = request.getParameter("fromSelf") == null?"":
    request.getParameter("fromSelf");
  if (!fromSelf.equals(""))
  {
    origMasterAccountId = request.getParameter("origMasterAccountId");
    providerId = request.getParameter("rsProvider");
    masterAccountId = request.getParameter("rsMasterAccount");
    if (!providerId.equals(RSB.getProviderId()))
    {
      RSB.setProviderId(SU.isNull(providerId,""));
      RSB.setMasterAccountId("");
    }
    else
    {
      RSB.setMasterAccountId(masterAccountId);
    }
    if (fromSelf.equals("move"))
    {
      ACC.setMasterAccountId(masterAccountId);
      if (ACC.updateAccount())
      {
        RSB.setMessage("<font color=blue><b>Account " + ACC.getAccountName() +
          "/" + ACC.getAccountNumber() + " (" + ACC.getAccountId() +
          ") moved</b>");
      }
    }
  }
  else
  {
    providerId = RSB.getProviderId();
    masterAccountId = RSB.getMasterAccountId();
    origMasterAccountId = masterAccountId;
  }
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function valAndSub()
{
  moveAccount.button1.disabled = true;
<%if ((masterAccountId.equals("")) || (masterAccountId.equals(origMasterAccountId)))
  {%>
    alert("Please select a new Master Account for this Account");
<%}
  else
  {%>
    moveAccount.button1.disabled = false;
    if (confirm("Press 'OK' to confirm this move or 'Cancel' to abort"))
    {
      moveAccount.fromSelf.value = "move";
      moveAccount.submit();
    }
<%}%>
}


function window_onload()
{
  window.focus();
<%if (fromSelf.equals("move"))
  {%>
  window.opener.location.href = "referenceDataMenu.jsp";
  self.close();
<%}%>
}
</script>
<body language=javascript onload="return window_onload()">
<form name="moveAccount" method="post" action="moveAccount.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<input name="origMasterAccountId" type=hidden value="<%=origMasterAccountId%>">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>Move Account <%=ACC.getAccountName()%>/<%=ACC.getAccountNumber()%> (<%=ACC.getAccountId()%>)</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Provider:</font>
          </td>
          <td  align=left>
          <%=DBA.getListBox("rsProvider","submit",providerId,(String)session.getAttribute("User_Id"),1,selectStyle,true)%>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td align=left colspan=2><font color="#0000FF">Master Account:</font>
          </td>
          <td  align=left>
          <%=DBA.getListBox("rsMasterAccount","submit",masterAccountId,providerId,1,selectStyle,true)%>
          </td>
          <td>&nbsp;</td>
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
      <input name="button1" class=button type=button value="<%=button1Text%>" style="width:90px" onClick="<%=button1OnClick%>">
    </td>
    <td align=left>
      <input class=button type=button value="<%=button2Text%>" style="width:90px" onClick="<%=button2OnClick%>">
    </td>
  </tr>
</table><!--table 3-->
</form>
</body>
</html>


