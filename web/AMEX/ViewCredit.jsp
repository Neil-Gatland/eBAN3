<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<title>View Credit</title>
<link rel="stylesheet" type="text/css" href="../nr/cw/newcss/world_ie.css">
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String Credit_Id=(String)request.getParameter("Credit_Id");
  BAN.setCredit_Id(Credit_Id);
  BAN.getCredit_Details();
  String CR_DR=BAN.getCredit_Details_CR_DR();

%>
  <script language="JavaScript" src="../includes/eBanfunctions.js">
  </script>
  <script language="JavaScript">
  <!--
  //-->
  </script>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td>
        <form name="View_Credit" method="post" action="SubmitCredit.jsp">
        <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td>
	<table border="0" id=2>
	  <tr>
	    <td colspan=8 valign="top"><br>
	      <h3 align="center"><b>View Home Workers Voice
                <%=CR_DR%></b></h3>
            </td>
          <tr>
            <td class="grid_menu" colspan=9>
              Customer
            </td>
          </tr>
          <tr>
            <td class="grid1" colspan=9>
              <%=BAN.getCredit_Details_Customer_Name()%>
            </td>
          </tr>
          <tr>
            <td class="grid_menu">
              Staff No
            </td>
            <td class="grid_menu">
              User Name
            </td>
            <td class="grid_menu">
              CLI
            </td>
            <td class="grid_menu">
              Charge From
            </td>
            <td class="grid_menu">
              Charge To
            </td>
            <td class="grid_menu">
              Total Charge
            </td>
            <td class="grid_menu">
              Calls Charge
            </td>
            <td class="grid_menu">
              Management Charge
            </td>
            <td class="grid_menu">
              Markup Charge
            </td>
          </tr>
          <tr>
            <td class="grid1">
              <%=BAN.getCredit_Details_Staff_Number()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_User_Name()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_CLI()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Charge_From()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Charge_To()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Call_Charge()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Calls_Charge()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Management_Charge()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Markup_Charge()%>
            </td>
          </tr>
          <tr>
            <td class="grid_menu" colspan=4>
              <%=CR_DR%> Description
            </td>
            <td class="grid_menu">
              <%=CR_DR%> Date
            </td>
            <td class="grid_menu">
              Total <%=CR_DR%>
            </td>
            <td class="grid_menu">
              Calls <%=CR_DR%>
            </td>
            <td class="grid_menu">
              Management <%=CR_DR%>
            </td>
            <td class="grid_menu">
              Markup <%=CR_DR%>
            </td>
          </tr>
          <tr>
            <td class="grid1" colspan=4>
              <%=BAN.getCredit_Details_Credit_Description()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Credit_Date()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Total_Credit()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Calls_Credit()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Management_Credit()%>
            </td>
            <td class="grid1">
              <%=BAN.getCredit_Details_Markup_Credit()%>
            </td>
          </tr>
        </table><!--2-->
      </td>
      <td>
        </form>
      <td>
    </tr>
  </table><!--1-->
</body>
</html>