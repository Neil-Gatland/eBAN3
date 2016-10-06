<%
  String userId = (String)session.getAttribute("User_Id");
  String guestId = "AMEXGUEST";
%>
<SCRIPT language="JavaScript">
function sendSelected(selectedId)
{
<%if (userId.equals(guestId))
  {%>
  alert("This function is not available to guest users");
<%}
  else
  {%>
  open("ViewCredit.jsp?Credit_Id="+selectedId,
       "View_Credit",
       "resizable=no,menubar=no,titlebar=no,height=250,width=700");
<%}%>
}
</SCRIPT>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%=BAN.getCreditList()%>