<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="JavaUtil.StringUtil"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<%! String SQL, Group_Name, System;
%>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  //DBAccess DBA = new DBAccess();
  Group_Name=(String)session.getAttribute("Group_Name");
  System=(String)session.getAttribute("System");
  //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  HB.setGroupName(Group_Name);
  session.setAttribute("PageSent",request.getRequestURI());
  String banStatus = BAN.getBanStatus();
//  String banId =
  //  ((banStatus.equals("Canceled")) || (banStatus.equals("Implemented")))?""
    //:BAN.getIsDirect()?BAN.getBanIdentifier():"";
  String banId =
    ((banStatus.equals("Canceled")) || (banStatus.equals("Implemented")))?"":BAN.getBanIdentifier();
  BAN.setIsDirect(false);
  BAN.setBanStatus("Draft");
  BAN.setBanIdentifier("");
  BAN.setGroupName(Group_Name);
%>
<a name="top"></a>
<table id=1 width ="100%">
  <tr>
    <td>
      <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <!-- This row holds the local menu bar-->
	<form name="Welcome" method="post" action="WelcomeHandler.jsp">
        <%=HB.getBANMenu_Bar("Welcome","","",(String)session.getAttribute("System"))%>
        <input name="ButtonPressed" type=hidden value="">
        <input name="listType" type=hidden value="">
        <a style="visibility: hidden" id="linkid"></a>
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
                  <table border="0" width="598">
                    <tr>
                      <td colspan=2><font color=#003399>
                        <nobr>BAN Id <input type="text" id="banId" name="banId" value="<%=banId%>"></nobr>
                        </font>
                      </td>
                      <td><font color=#003399 size="1">
                        Enter BAN Id, if known, <br>or
                        leave blank for a list of available BANs.<br>Then select required option.
                        </font>
                      </td>
                    </tr>
                  </table>
		  <%=DBA.getMenu(System,Group_Name,(String)session.getAttribute("User_Name"))%>

	        </td>
	      </tr>
	      <tr>
		<td>
		  &nbsp
		</td>
	        <td class=bluebold align=left>
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
      <!--Footer-->

    <tr>
        <td>
	</td>
      </tr>
    </table><!--1-->
</body>
</html>