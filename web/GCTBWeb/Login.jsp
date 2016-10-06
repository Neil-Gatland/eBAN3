<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="HTMLUtil.HTMLBean"%>
<%@ page import="JavaUtil.*"%>
<%
    String error = "";
    if (session.getAttribute("Error") != null )
    {
      error = (String)session.getAttribute("Error");
    }
//
    session = request.getSession(false);
    if (session != null)
      session.invalidate();
    session = request.getSession(true);
    HTMLBean HTMLB = new HTMLBean();
    String type = request.getParameter("type")==null?"":(String)request.getParameter("type");
    String system = request.getParameter("system")==null?"OSS":(String)request.getParameter("system");
    String message = "To login please enter your user name and password";
    String email = EBANProperties.getEBANProperty(EBANProperties.SUPPORTEMAIL);
    if (type.equals("l"))
    {
      message = "<font color=red><b>You have ended your session. Please " +
        "re-enter your login details to continue</b></font>";
    }
    else if ((type.equals("r")) ||(type.equals("t")))
    {
      message = "<font color=red><b>Your session has expired. Please " +
        "re-enter your login details to continue</b></font>";
    }
    else if (type.equals("c"))
    {
      message = "<font color=red><b>Your session has been corrupted. Please " +
        "re-enter your login details to continue</b></font>";
    }
    session.setAttribute("System",system);
    String text = "For temporary, read-only access to Ad Hoc Billing. " +
    " If you require an Ad Hoc user profile please contact '" + email + "'";
%>
<script language="JavaScript">
window.name="gctbmain";
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
		  <h1>Welcome to GM Billing</h1>
	  	  <h2 class="Instructions"><%=message%></h2>
        	</td>
	      </tr>
		<form method="post" action="Authenticate.jsp">
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
  		  <td colspan=3>
                    &nbsp;
	 	  </td>
		</tr>
                <tr>
  		  <td>
                    <input class=button type=submit value="Log In">
	 	  </td>
		</tr>
		      <tr>
			<td colspan=2><font color=red><b><%=error%>
                        </b></font>
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
                          please contact the
                          <A HREF='mailto:<%=email%>'>System Administrator</A>
                        </td>
		      </tr>
		</form>
		<tr>
		  <td colspan=3 height="150">
                    <iframe frameborder=0 width="100%" height=150 id=notices name=notices src="notices.jsp"></iframe>
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
	  <td height="400" align=right valign=top>&nbsp
      <img src="../nr/cw/newimages/danetdevoteam2.gif" border="0" width="81" height="52" align="middle">
	  </td>
	</tr>
      </table><!--1-->
</body>
</html>

