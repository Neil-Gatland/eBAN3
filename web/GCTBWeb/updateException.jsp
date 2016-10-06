<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="HTMLUtil.HTMLBean"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>
<% String SQL;
    boolean isConglom = request.getParameter("conglom")!=null;
    String fromBilling = isConglom?(request.getParameter("fromBilling")==null?"false":
      request.getParameter("fromBilling")):"false";
    String option3 = fromBilling.equals("true")?"Conglom Billing Menu":"Exception List";
    String option4 = fromBilling.equals("true")?"Exception List":"";
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Log Off",(isConglom?"Conglom ":"")+"Desktop",option3,option4},
                            {"Update","Cancel","Raise Query"},
			    {"All Charges","Selected Charges"},
			    {"Show Filters","Most Recent Values","Reset Filters"}};

    //DBAccess DBA = new DBAccess();
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    String title = (isConglom?"Conglomerate":"Data") + " Billing Exception Details";
    %>
<form name="updateException" method="post" action="updateExceptionHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<%if (isConglom)
  {%>
<input name="conglom" type=hidden value="true">
<input type=hidden name="fromBilling" value="<%=fromBilling%>">
<%}%>
<a name="top"></a>
<table id=1 border=0 width="100%">
  <tr>
    <td colspan=3>
      <%@ include file="../includes/Page_Header4.htm"%>
    </td>
  </tr>
  <tr><!-- This row holds the local menu bar-->
    <td colspan=3>
      <%=HB.getMenu_Bar()%>
    </td>
  </tr>
  <tr>
   <td colspan=3>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
      <tr id=2>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h2 align="left"><%=title%></h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	      <td colspan="4" width=100% class=bluebold>
	      <%
	        session.setAttribute("formname","updateException");
	      %>
    	        <%=SU.isNull(BAN.getMessage(),"")%>
              <td>
            </tr>
            <tr>
              <td><b>Job Name:</b></td>
              <td class=bluebold><%=isConglom?"Conglomerate":"Data"%> Billing</td>
              <td><b><%=isConglom?"Invoice/Docket Number:":"Invoice Region/Number:"%></b></td>
              <td class=bluebold><%=isConglom?BAN.getInvoiceDocketNo():BAN.getExceptionIRIN()%></td>
            </tr>
            <tr>
              <td colspan="4">
                <table border=0 width=100%>
                </table>
              </td>
            </tr>
            <tr>
              <td colspan="4">
                <table border=0 width=100%>
                  <tr class=gridHeader>
                    <td class=gridHeader>Field</td>
                    <td class=gridHeader colspan=3>Value</td>
                  </tr>
                <%=isConglom?BAN.getConglomExceptionDetails():BAN.getExceptionDetails()%>
                  <tr class=gridHeader>
                    <td class=gridHeader>New Status</td>
                    <td class=gridHeader colspan=3>Note</td>
                  </tr>
                  <tr>
                    <td class=gridHeader>New Status</td>
                    <td class=gridHeader colspan=3>Note</td>
                  </tr>
                  <tr>
                    <td valign=top>
                      <%=DBA.getListBox((isConglom?"Conglom_":"")+"Exception_Status2","select",BAN.getExceptionStatus(),"",1," style=\"width:150px\"",false)%>
                    </td>
                    <td colspan=3>
                      <textarea name=exceptionNote cols=40 rows=3></textarea>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
	  </table><!--table 3-->
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
    </td>
  </tr>
</table><!--1-->
</form>
</body>
</html>


