<html>
<head>
  <title>GSO Reject Detail</title>
  <link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
</head>
<%@ page import="JavaUtil.StringUtil"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
  //DBUtilities.DBAccess DBA = new DBUtilities.DBAccess();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  ArrayList details = new ArrayList(DBA.getGSORejectDetail(Integer.parseInt(request.getParameter("rowId"))));
%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<script language="JavaScript">
function window_onload()
{
  window.focus();
}
</script>
<body language=javascript onload="return window_onload()">
<a name="top"></a>
<table width="100%" border="0" id=3>
  <tr>
    <td colspan=2 align=center>
      <b>GSO Reject Detail</b>
    </td>
  </tr>
  <tr>
    <td colspan=2>
      <!--header-->
      <table border=0 width=100%>
        <tr>
          <td valign=top><b>Service Partner:</b></td>
          <td valign=top class=bluebold><%=BAN.getServicePartnerName()%></td>
        </tr>
        <tr>
          <td valign=top><b>Service Partner Id:</b></td>
          <td valign=top class=bluebold><%=BAN.getServicePartnerId()%></td>
        </tr>
        <tr>
          <td valign=top><b>Billing Start Date:</b></td>
          <td valign=top class=bluebold><%=details.get(0)%></td>
        </tr>
        <tr>
          <td valign=top><b>SO Ref:</b></td>
          <td valign=top class=bluebold><%=details.get(1)%></td>
        </tr>
        <tr>
          <td valign=top><b>GSO Product Code:</b></td>
          <td valign=top class=bluebold><%=details.get(2)%></td>
        </tr>
        <tr>
          <td valign=top><b>MacSpec1:</b></td>
          <td valign=top class=bluebold><%=details.get(3)%></td>
        </tr>
        <tr>
          <td valign=top><b>Charge Type:</b></td>
          <td valign=top class=bluebold><%=details.get(4)%></td>
        </tr>
        <tr>
          <td valign=top><b>Charge Amount:</b></td>
          <td valign=top class=bluebold><%=details.get(5)%></td>
        </tr>
        <tr>
          <td valign=top><b>Charge Currency:</b></td>
          <td valign=top class=bluebold><%=details.get(6)%></td>
        </tr>
        <tr>
          <td valign=top><b><nobr>End Customer Name:</nobr></b></td>
          <td valign=top class=bluebold><%=details.get(7)%></td>
        </tr>
        <tr>
          <td valign=top><b>Service Start Date:</b></td>
          <td valign=top class=bluebold><%=details.get(8)%></td>
        </tr>
        <tr>
          <td valign=top><b>Job Number:</b></td>
          <td valign=top class=bluebold><%=details.get(9)%></td>
        </tr>
        <tr>
          <td valign=top><b>Activity Code:</b></td>
          <td valign=top class=bluebold><%=details.get(10)%></td>
        </tr>
        <tr>
          <td valign=top><b>MacSpec2:</b></td>
          <td valign=top class=bluebold><%=details.get(11)%></td>
        </tr>
        <tr>
          <td valign=top><b>MacPONumber:</b></td>
          <td valign=top class=bluebold><%=details.get(12)%></td>
        </tr>
        <tr>
          <td valign=top><b>A End Address:</b></td>
          <td valign=top class=bluebold><%=details.get(13)%></td>
        </tr>
        <tr>
          <td valign=top><b>A End Desc:</b></td>
          <td valign=top class=bluebold><%=details.get(14)%></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan=2 align=center>
      <input class=button type=button value="Close" style="width:90px" onClick="window.close()">
    </td>
  </tr>
</table><!--table 3-->
</body>
</html>


