<HTML>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.ResultSetMetaData"%>
<%@ page import="java.sql.Types"%>
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
  String reportType = request.getParameter("reportType");
  String providerNameParameter = reportType.equals("rateflag")?"providerName2":
    "providerName";
  String providerName = request.getParameter(providerNameParameter)==null?"":
    request.getParameter(providerNameParameter);
  String reportCode = request.getParameter("rsReport");
  String callMonth = request.getParameter("fromCallMonth")==null?"All":
    request.getParameter("fromCallMonth");
  String reportName = providerName.equals("")?callMonth:
    (providerName.replace(' ', '_') + (callMonth.equals("All")?"":"_" + callMonth));

  response.setHeader("content-disposition","attachment; filename=" + reportCode.substring(0, reportCode.length()-2) +
    "_report_" + reportName + "_" + yyyy + (mm<10?"0":"") + mm + (dd<10?"0":"") +
    dd + "_" + (hh<10?"0":"") + hh + (mi<10?"0":"") + mi + (ss<10?"0":"") + ss +
    ".xls");
  ResultSet RS = null;
  ResultSetMetaData rsmd = null;
  try
  {
    if (RSB.processRunning("All"))
    {
      response.reset();
      response.sendRedirect("chooseReport.jsp?instructions=" +
        response.encodeURL("The report viewing facility is currently unavailable.") +
        "&rsReport=" + reportCode + "&fromCallMonth=" + callMonth +
          "&reportType=" + reportType + "&providerName=" + providerName + "&fromSelf=true");
      response.flushBuffer();
    }
    else
    {
      RS = RSB.getReportRS(reportCode, callMonth, providerName);
      rsmd = RS.getMetaData();
      if ((rsmd.getColumnCount() == 1) &&
        (rsmd.getColumnLabel(1).equals("reportNotAvailableError")))
      {
        //RSB.close();
        response.reset();
        response.sendRedirect("chooseReport.jsp?instructions=" +
          response.encodeURL("This report is not currently available.") +
          "&rsReport=" + reportCode + "&fromCallMonth=" + callMonth +
          "&reportType=" + reportType + "&providerName=" + providerName + "&fromSelf=true");
        response.flushBuffer();
      }
    }
  }
  catch (NullPointerException ex)
  {
    RSB.close();
    response.reset();
    response.sendRedirect("chooseReport.jsp?instructions=" +
      response.encodeURL("This report is not currently available.") +
      "&rsReport=" + reportCode + "&fromCallMonth=" + callMonth +
      "&reportType=" + reportType + "&providerName=" + providerName + "&fromSelf=true");
    response.flushBuffer();
  }
%>
<HEAD>
  <title>Revenue Share Billing Downloads</title>
</HEAD>
<BODY>
<%
  int numberOfColumns = rsmd.getColumnCount() + 1;
%>
<TABLE BORDER=1>
<THEAD><TR>
<%
  for (int i = 1; i<numberOfColumns; i++)
  {%>
<TH style="background-color : #CCCCFF;FONT-SIZE: xx-small;">
     <%=rsmd.getColumnName(i)%>
</TH>
<%}
%>
</TR></THEAD>
<%
  while (RS.next())
  {%>
<TR>
  <%for (int i = 1; i<numberOfColumns; i++)
    {%>
  <TD style="FONT-SIZE: xx-small;">
    <%// if the column is a TIMESTAMP type, convert to date to get rid of the time
      if ((rsmd.getColumnType(i)+2) == Types.TIMESTAMP)
      {%>
        <%=RS.getDate(i)%>
    <%}
      else
      {
        try
        {
          String temp = RS.getString(i).replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim();%>
          <%=temp%>
      <%}
        catch (Exception ex)
        {%>
          <%=RS.getString(i)%>
      <%}%>
    <%}%>
  </TD>
  <%}%>
</TR>
<%}
RSB.close();
%>
</TABLE>
</BODY>
</HTML>


