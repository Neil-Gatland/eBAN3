<%@ page import="DBUtilities.DBAccess"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<html>
<head>
<title>Raise a query</title>
</head>
<%
DBAccess DBA = new DBAccess();
Enumeration en = request.getParameterNames();
StringBuffer sb = new StringBuffer();
boolean isGSO = request.getParameter("gso")!=null;
StringBuffer gcId = new StringBuffer(isGSO?BAN.getSPName():BAN.getGlobalCustomerId());
if (gcId.length() == 0)
{
  gcId.append(request.getParameter("frmq_GCId"));
  sb.append("accountId=" + request.getParameter("frmq_AccId") +
    ",invoiceId=" + request.getParameter("frmq_InvId") + ",");
}
else
{
  long conglomCustId = BAN.getConglomCustomerId();
  if (conglomCustId != -1)
  {
    gcId.append(" (" + conglomCustId + ")");
  }
}
while (en.hasMoreElements())
{
  String pName = (String)en.nextElement();
  if ((!pName.equals("ButtonPressed")) && (!pName.equals("fromPage")) &&
    (!pName.equals("Global_Customer")) && (!pName.equals("queryText")) &&
    (!pName.equals("mode")) && (!pName.startsWith("EWF_HIDDEN")) &&
    (!pName.startsWith("frmq_")))
  {
    sb.append(pName + "=" + request.getParameter(pName) + ",");
  }
}

if (sb.length() > 0)
  sb.deleteCharAt(sb.length()-1);
//String gcId = request.getParameter("Global_Customer");
String queryText = request.getParameter("queryText");
String mode = request.getParameter("mode")==null?"":request.getParameter("mode");
String screenType = mode.equals("")?"":(" ("+mode+")");
String screenId = request.getParameter("fromPage") + screenType;
String userId = (String)session.getAttribute("User_Id");
String billingTeam = (String)session.getAttribute("billingTeam");
int queryId = DBA.insertQuery(gcId.toString(), screenId, userId, queryText,
  sb.toString(), userId, billingTeam);
String message = queryId>0?("(query id: "+String.valueOf(queryId)+")"):DBA.getMessage();
%>
<jsp:forward page="../query/finishQuery.jsp">
  <jsp:param name="message" value="<%=message%>"/>
</jsp:forward>
</html>


