<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,JavaUtil.StringUtil,HTMLUtil.HTMLBean"%>
<%! String SQL, Group_Name, System;
%>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  DBAccess DBA = new DBAccess();
  Group_Name=(String)session.getAttribute("Group_Name");
  System=(String)session.getAttribute("System");
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  session.setAttribute("PageSent",request.getRequestURI());
%>
<a name="top"></a>
<table id=1 width="100%">
  <tr>
    <td>
      <%@ include file="../includes/Page_Header4.htm"%>
      <!-- This row holds the local menu bar-->
	<form name="Welcome" method="post" action="WelcomeHandler.jsp">
        <%=HB.getBANMenu_Bar("Welcome","","",(String)session.getAttribute("System"))%>
        <input name="ButtonPressed" type=hidden value="">
    </td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	<tr>
	  <!-- this is a spacer column-->
	  <td width="1">&nbsp;</td>
	  <td valign="top" align="left" NOWRAP>
	  <br>
	    <table border="0" cellspacing="0" cellpadding="0" width="100%" id =3>
	      <tr height="30">
		<td colspan="2">
			&nbsp;
		</td>
	      </tr>
	    </table><!--id=3-->
	  </td>
	  <td width="12"><!-- a spacer column-->
		&nbsp;
	  </td>
	  <td  valign ="Top"><a name="content"></a>
	    <table width="740" border="0" cellpadding="0" cellspacing="0" id=4>
	      <tr>
	        <td width="142" valign="top">&nbsp;</td>
		<td valign="top" height="45" width="598">
		  <h3 align="left">Select one of the following options:</h3>
	      </tr>
	      <tr>
		<td width="142" valign="top">&nbsp;
		</td>
		<td>
		  <%=DBA.getMenu(System,Group_Name,(String)session.getAttribute("User_Name"))%>
	        </td>
	      </tr>
	      <tr>
		<td>
		  &nbsp
		</td>
	        <td align=left>
		  <%=SU.isNull((String)session.getAttribute("Message"),"")%>
		  <%session.setAttribute("Message","");%>
		</td>
	      </tr>
	    </table><!--4-->
	  </td>
	  <td width="22">&nbsp;</td>
	  <td width="153" valign="top" >
	    <table border="0" width="152" cellpadding="0" cellspacing="0" ID=5>
	      <tr height="47">
		<td colspan="3">
		  &nbsp;
		</td>
	      </tr>
	      <tr>
		<td height="22" width="9" >
			&nbsp;
		</td>
		<td colspan="2" height="22">
		</td>
	      </tr>
	      <tr>
		<td colspan="2">&nbsp;
		</td>
	      </tr>
	    </table><!--5-->
	  </td>
	</tr>
      </table><!--2-->
     <tr>
      <td height="300">&nbsp
      </form>
      </td>
    </tr>
    </table><!--1-->
</body>
</html>