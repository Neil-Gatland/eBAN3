<html>
<%@ page import="JavaUtil.*"%>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<%!     HTMLUtil.HTMLBean HTMLB = new HTMLUtil.HTMLBean();
        StringUtil SU = new StringUtil();%>
<%
    String type = request.getParameter("type")==null?"":(String)request.getParameter("type");
    String system = request.getParameter("system")==null?"GCB":(String)request.getParameter("system");
    String message = "To login please enter your user name and password";
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
		  <h1>Welcome to Data Billing</h1>
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
			<td colspan=2>
			<%=SU.isNull((String)session.getAttribute("Error"),"")%>
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
	<tr>
	  <td>
	  </td>
	 </tr>
      </table><!--1-->
</body>
</html>