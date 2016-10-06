<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*,java.util.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  DBAccess DBA = new DBAccess();
  String Mode = "";
  String Action = "";
  StringUtil su = new StringUtil();
  String[] dates = new String[5];
  Calendar cal = Calendar.getInstance();
  for (int i = 0; i < 5; i++)
  {
    Date now = cal.getTime();
    String dateStr = su.DateToString(now);
    dates[i] = dateStr.substring(3,6) + " " + dateStr.substring(7,11);
    cal.add(cal.MONTH,-1);
  }

  String Business_Unit = BAN.getBusiness_Unit_for_Report();
  String Segment = BAN.getSegment_for_Report();
  String Customer = BAN.getCustomer_for_Report();
  if (request.getParameter("fromSelf") == null)
  {
    BAN.resetReportFilters();
    //Business_Unit = "All";
    //Segment = "All";
    //Customer = "All";
  }
  else
  {
    String ButtonPressed=(String)request.getParameter("ButtonPressed");
    if (ButtonPressed.equals("Reset Filters"))
    {
      BAN.resetReportFilters();
    }
    else
    {
      String BU = (String)request.getParameter("Business_Unit");
      String S = (String)request.getParameter("Segment");
      String C = (String)request.getParameter("Ad_Hoc_Customer2");
      if (!BU.equals(Business_Unit))
      {
        BAN.setBusiness_Unit_for_Report(BU);
        BAN.setSegment_for_Report("All");
        BAN.setCustomer_for_Report("All");
      }
      else if (!S.equals(Segment))
      {
        BAN.setSegment_for_Report(S);
        BAN.setCustomer_for_Report("All");
      }
      else if (!C.equals(Customer))
      {
        BAN.setCustomer_for_Report(C);
      }
    }
  }
  Business_Unit = BAN.getBusiness_Unit_for_Report();
  Segment = BAN.getSegment_for_Report();
  Customer = BAN.getCustomer_for_Report();


%>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<form name="AdHocReport" method="post" action="AdHocReport.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="fromSelf" type=hidden value="true">
<table border=0 id=1 width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td colspan=2>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the menu bar-->
    <td colspan=2>
        <%=HB.getBANMenu_Bar("Report","","",(String)session.getAttribute("System"))%>

    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td align="center">
	<table width="70%" border="0" id=2>
	  <tr>
            <td colspan=7><h3 align="center">Work in Progress</h3></td>
	  </tr>
	  <tr>
            <td width="30%" class=grid_menu>State</td>
            <td width="11%" class=grid_menu>Status</td>
            <td width="11%" class=grid_menu>Total</td>
            <td width="48%" align="center" colspan=4 class=grid_menu>Months</td>
	  </tr>
	  <tr>
            <td class=grid1>&nbsp;</td>
            <td class=grid1>&nbsp;</td>
            <td class=grid1>&nbsp;</td>
            <td width="12%" align="center" class=grid_menu>&lt;1</td>
            <td width="12%" align="center" class=grid_menu>1-2</td>
            <td width="12%" align="center" class=grid_menu>2-3</td>
            <td width="12%" align="center" class=grid_menu>&gt;3</td>
	  </tr>
          <%=BAN.getAdHocActive()%>
         </table><!--2-->
    </td>
  </tr>
  <tr>
    <td align="center">
	<table width="70%" border="0" id=3>
	  <tr>
            <td colspan=7><h3 align="center">Completed Invoices</h3></td>
	  </tr>
	  <tr>
            <td width="30%" class=grid_menu>&nbsp;</td>
            <td width="11%" class=grid_menu>&nbsp;</td>
            <td width="11%" align="center" class=grid_menu><%=dates[0]%></td>
            <td width="12%" align="center" class=grid_menu><%=dates[1]%></td>
            <td width="12%" align="center" class=grid_menu><%=dates[2]%></td>
            <td width="12%" align="center" class=grid_menu><%=dates[3]%></td>
            <td width="12%" align="center" class=grid_menu><%=dates[4]%></td>
	  </tr>
          <%=BAN.getAdHocProcessed()%>
         </table><!--3-->
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td align="center">
      <table width="70%" border="0" id=3>
        <tr>
          <td>
            <b><small>Business Unit<br>
            <%=DBA.getListBox("Business_Unit","submit",Business_Unit,"")%>
          </td>
          <td>
            <b><small>Segment<br>
            <%=DBA.getListBox("Segment","submit",Segment,Business_Unit)%>
          </td>
          <td>
            <b><small>Customer<br>
            <%=DBA.getListBox("Ad_Hoc_Customer2","submit",Customer,Segment)%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


