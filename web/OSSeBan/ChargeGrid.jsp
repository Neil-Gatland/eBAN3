<!--jsp:useBean id="chBAN" class="DBUtilities.OSSChargeBANBean" scope="session"/-->

<%@ page isThreadSafe="false" %>
<%@ page import="DBUtilities.OSSChargeBANBean"%>
<%
  String userId = (String)session.getAttribute("User_Id");
  OSSChargeBANBean chBAN = (OSSChargeBANBean)session.getAttribute("chBAN" + userId);
  if (chBAN == null)
  {
    chBAN = new OSSChargeBANBean();
    session.setAttribute("chBAN" + userId, chBAN);
  }
  String chargeList = chBAN.getChargeList();
  if (!chargeList.endsWith("</table>"))
  {
    chargeList = chBAN.getChargeList(); //have another go - don't ask!
  }
%>
<SCRIPT language="JavaScript">
function sendSelected(selectedId)
{
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.Charge_Id.value=selectedId;
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.ButtonPressed.value="Select";
  parent.document.forms.<%=(String)session.getAttribute("formname")%>.submit();
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%=chargeList%>

