<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,JavaUtil.StringUtil,HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<%! String SQL, Group_Name, System;
%>
<%
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  DBAccess DBA = new DBAccess();
  Group_Name=(String)session.getAttribute("Group_Name");
  System=(String)session.getAttribute("System");
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  session.setAttribute("PageSent",request.getRequestURI());
  String PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  String Account_Id="", Invoice_Total="";
  if (PageSent.endsWith("WelecomHandler.jsp"))
  {
    Account_Id=BAN.getAccountId();
    Invoice_Total=BAN.getInvoiceTotal();
  }
  else
  {
    //Store old values
    session.setAttribute("Account_Id",BAN.getAccountId());
    session.setAttribute("Invoice_Total",BAN.getInvoiceTotal());
    BAN.setAccountId("");
    BAN.setInvoiceTotal("");
    Account_Id="";
    Invoice_Total="";
  }
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
    <td height="10px"></td>
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
		<td align="left" colspan="2">
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
		<td valign="top" height="45" width="598" colspan=4>
		  <h3 align="center">Create Managed Services Upload</h3>
	      </tr>
	      <tr>
                <td align=left width="25%" colspan=1><font color="#0000FF">Account
                  <br><%=DBA.getListBox("MS_Account_List","",Account_Id,"",true)%>
                </td>
                <td align=left width="5%" colspan=1>&nbsp;
                </td>
                <td align=left width="25%" colspan=1><font color="#0000FF">Total (net) 
                  <br><input class=inp type=text name="Invoice_Total" size="21"
                            value="<%=Invoice_Total%>">
                </td>
                <td align=left width="15%" colspan=1><font color="#0000FF">
                    <br><%=HB.getImageAsAnchor("clear")%>
                </td>
                <td align=left width="15%" colspan=1><font color="#0000FF">
                    <br><%=HB.getImageAsAnchor("check")%>
                </td>
                <td align=left width="15%" colspan=1><font color="#0000FF">
                    <br><%=HB.getImageAsAnchor("submit")%>
                </td>
	      </tr>
	      <tr>
		<td>
		  &nbsp;
		</td>
	        <td align=left>
		  <%=SU.isNull((String)session.getAttribute("Message"),"")%>
		  <%session.setAttribute("Message","");%>
		</td>
	      </tr>
              <tr>
                  <td colspan="7"><font color="#0000FF">
                      Current upload details for <%=BAN.getLoginId()%>
                  </td>
              </tr>
              <tr>
                  <td colspan="7">
                      &nbsp;
                  </td>
              </tr>
	    </table><!--4-->
	  </td>
        </tr>
      </table><!--2-->
     <tr>
      <td width="100%" colspan=2 class=bluebold>
                  <iframe frameborder=0 width="100%" height=40 id=GridHeader name=GridHeader
                    src="chargeGridHeader.jsp" scrolling="no">
                  </iframe>
                  <iframe frameborder=0 width="100%" height=140 id=GridData name=GridData
                    src="chargeGrid.jsp" scrolling="yes">
                  </iframe>
                </td>
      </form>
      </td>
    </tr>
    </table><!--1-->
</body>
</html>