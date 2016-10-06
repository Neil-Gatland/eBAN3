<HTML>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<%
 // Check page not cached on client
  response.setHeader("CacheControl", "no-cache");
  response.setHeader("Pragma", "no-cache"); // for http 1.0 browsers
  response.setIntHeader("Expires", -1);
  // change page content type to Excel
  response.setContentType("application/vnd.ms-excel");
  // make sure user is prompted to save or open file
  Calendar now = Calendar.getInstance();
  int dd = now.get(now.DATE);
  int mm = now.get(now.MONTH) + 1;
  int yyyy = now.get(now.YEAR);
  int hh = now.get(now.HOUR_OF_DAY);
  int mi = now.get(now.MINUTE);
  int ss = now.get(now.SECOND);
  response.setHeader("content-disposition","attachment; filename=" +
    request.getParameter("fileName") +
      "_" + yyyy + (mm<10?"0":"") + mm + (dd<10?"0":"") +
      dd + "_" + (hh<10?"0":"") + hh + (mi<10?"0":"") + mi + (ss<10?"0":"") + ss +
      ".xls");
  Collection messageRows = RSB.getDownloadArray();
%>
<HEAD>
  <title>List Download</title>
</HEAD>
<BODY>
<TABLE BORDER=1>
<%
  for (Iterator it = messageRows.iterator(); it.hasNext(); )
  {%>
<TR>
  <%=(String)it.next()%>
</TR>
<%}%>
</TABLE>
</BODY>
</HTML>


