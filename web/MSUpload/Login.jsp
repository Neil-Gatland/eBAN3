<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="HTMLUtil.HTMLBean"%>
<%@ page import="JavaUtil.*"%>
<%!  HTMLUtil.HTMLBean HTMLB = new HTMLUtil.HTMLBean();%>
<%
    String type = request.getParameter("type")==null?"":(String)request.getParameter("type");
    String system = request.getParameter("system")==null?"MSUpload":(String)request.getParameter("system");
    String message = "To login please enter your user name and password";
    String email = EBANProperties.getEBANProperty(EBANProperties.SUPPORTEMAIL);
    if (type.equals("l"))
    {
      message = "<font color=red><b>You have ended your session. Please " +
        "re-enter your login to continue</b></font>";
    }
    else if (type.equals("r"))
    {
      message = "<font color=red><b>Your session has expired. Please " +
        "re-enter your login to continue</b></font>";
    }
    session.setAttribute("System",system);
%>
<script language="JavaScript">
window.name="ahmain";

</script>
<script language="JavaScript" src="../includes/eBanfunctions.js">
</script>
<a name="top"></a>
<table id=1>
  <tr><td>
    <%@ include file="../includes/Page_Header4.htm"%>
   <tr><td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	<tr>
	<!-- this is a spacer column-->
	  <td width="13">&nbsp;</td>
	  <td width="200" valign="top" align="left">
	  </td>
          <td width="12"><!-- a spacer column-->
		&nbsp;
	  </td>
	  <td  valign ="Top"><a name="content"></a>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0" id=3>
	      <tr height="9">
		<td valign="top" height="45" width="598"  colspan=3>
		  <h1>Managed Services Upload Creation</h1>
	  	  <h2 class="Instructions"><%=message%></h2>
        	</td>
	      </tr>
              <tr>
		<form method="post" action="Authenticate.jsp">
  		<td colspan=3>
		  <%=HTMLB.getImage("login")%>
		</td>
		</tr>
		<tr>
		  <td>Username: </td>
		  <td>Password: </td>
		  <td width=100>&nbsp; </td>
		</tr>
		<tr>
		    <td>
		      <input name=username size=7 style="WIDTH: 90px">
		    </td>
		    <td>
		      <input name=userpass size=7 style="WIDTH: 90px" type=password>
		    </td>
		    </tr>
		      <tr>
			<td colspan=2><font color=red><b>
			<%
			if (session.getAttribute("Error") != null )
			{
			  if (session.getAttribute("Error").toString().startsWith("No Data Found") == true)
			  {
			    %><%="Invalid Username or Password"%><%
			  }
			  else
			  {
			    %><%=session.getAttribute("Error")%><%
			  }
			 }%></b></font>
			</td>
		      </tr>
		      <tr>
                        <td colspan=3>
                          &nbsp;
                        </td>
		      </tr>
		      <tr>
                        <td colspan=3>
                          &nbsp;
                        </td>
		      </tr>
		      <tr>
                        <td colspan=3>
                          &nbsp;
                        </td>
		      </tr>
		      <tr>
                        <td colspan=3 class="Instructions">
                          If you experience problems logging onto the system
                          then please contact the
                          <A HREF='mailto:<%=email%>'>System Administrator</A>
                        </td>
		      </tr>
		</form>
		<tr>
		  <td height="150">
		  </td>
		 </tr>
		</table><!--3-->
	      </td>
	      <td width="22">&nbsp;</td><!--spacer-->
	      <td width="153" valign="top"><!--Right Hand column-->
	      </td>
	    </tr>
	  </table><!--2-->
	  <!--Footer-->
	 <tr>
	  <td height="400">&nbsp
	  </td>
	</tr>
      </table><!--1-->
</body>
</html>


