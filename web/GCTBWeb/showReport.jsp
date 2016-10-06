<HTML>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.ResultSetMetaData"%>
<%@ page import="java.sql.Types"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%
 // Check page not cached on client
  response.setHeader("CacheControl", "no-cache");
  response.setHeader("Pragma", "no-cache"); // for http 1.0 browsers
  response.setIntHeader("Expires", -1);
  // change page content type to Excel
  response.setContentType("application/vnd.ms-excel");
  // make sure user is prompted to save or open file
  response.setHeader("content-disposition","attachment; filename=My_Export.xls");
  String reportType = request.getParameter("GCB_Download_Report");
  String month = "month";
  String year = "year";
  String param1 = "";
  String param2 = "";
  String invoiceMonth = null;
  if (reportType.endsWith("01"))
  {
    month = request.getParameter("reportMonthName");
    year = request.getParameter("reportYear");
    invoiceMonth = "01 " + month + " " + year;
  }
  else if (reportType.endsWith("02"))
  {
          param1 = request.getParameter("LLU_Global_Customer");
          param2 = request.getParameter("LLU_GSR");
  }
  else if (reportType.endsWith("03"))
  {
          param1 = request.getParameter("accountId");
  }
  else if (reportType.endsWith("04"))
  {
          param1 = request.getParameter("invoiceNo");
  }
  else if (reportType.endsWith("05"))
  {
          param1 = request.getParameter("All_Customer");
  }
  ResultSet RS = BAN.getGCBReportRS(reportType, invoiceMonth, param1, param2);
  if (RS == null)
  {
    response.reset();
    response.sendRedirect("chooseReport.jsp?instructions="+response.encodeURL("This report is not currently available."));
  }
%>
<HEAD>
  <title>GCB Billing Downloads</title>
</HEAD>
<BODY>
<%
  ResultSetMetaData rsmd = RS.getMetaData();
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
          String temp = RS.getString(i).replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim();
          while (temp.startsWith("-"))
          {
            String temp2 = temp.substring(1);
            //String temp3 = temp2.trim();
            try
            {
              Double.parseDouble(temp2);
              break;
            }
            catch (NumberFormatException nfe)
            { 
              temp = temp2.trim();
            }
          }%>
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
BAN.close();
%>
</TABLE>
</BODY>
</HTML>


