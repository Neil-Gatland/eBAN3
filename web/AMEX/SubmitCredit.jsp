<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="acb" class="DBUtilities.AssureChargeBean" scope="session"/>

<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String Credit_Description=(String)request.getParameter("Credit_Description");
  String Credit_Value=(String)request.getParameter("Credit_Value");
  String CR_DR=(String)request.getParameter("CR_DR");
  String Visit_Date=(String)request.getParameter("VisitDateh");
  String VisitDateDay=(String)request.getParameter("VisitDateDay");
  String VisitDateMonth=(String)request.getParameter("VisitDateMonth");
  String VisitDateYear=(String)request.getParameter("VisitDateYear");
  int VDay=SU.toInt(VisitDateDay);
  int VMonth=SU.toInt(VisitDateMonth);
  int VYear=SU.toInt(VisitDateYear);
  String CLI_Id=BAN.getCLI_Id();
  BAN.setCredit_Amount(Credit_Value);
  BAN.setCredit_Description(Credit_Description);
  BAN.setCR_DR(CR_DR);
  BAN.setVDay(VDay);
  BAN.setVMonth(VMonth);
  BAN.setVYear(VYear);
  acb.setVisit_Date(Visit_Date);
  String AddCreditMessage=BAN.AddCredit(Visit_Date);
  acb.setVisit_Date("");
  session.setAttribute("AddCreditMessage",AddCreditMessage);
  if (AddCreditMessage!="")
  {
    %>
      <jsp:forward page="AddCredit.jsp"/>
    <%
  }

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  </script>
</body>
</html>