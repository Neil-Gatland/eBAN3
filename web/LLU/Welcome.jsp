<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%
  String SQL;
  String Customer_Id="";
  String Customer_Name="";
  String C2_Ref="";
  String Service_Reference="";
  String Message,ActionService;
  HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
  JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
  JavaUtil.UserData UD = new UserData();
  DBAccess DBA = new DBAccess();
  String PageSent;
  String sharedPath=
        EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
  String colspan;
  String system = (String)session.getAttribute("System");
  String groupName = (String)session.getAttribute("Group_Name");
  String email = EBANProperties.getEBANProperty(EBANProperties.PROCESSEMAIL);
  Message=SU.isNull((String)session.getAttribute("Message"),"");
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("WelcomeHandler.jsp"))
  {
    Customer_Id=BAN.getGlobalCustomerId();
    C2_Ref=BAN.getC2Ref();
    Service_Reference=BAN.getService_Reference();

  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("C2_Ref",BAN.getC2Ref());
    session.setAttribute("Service_Reference",BAN.getService_Reference());

    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setC2Ref("");
    BAN.setService_Reference("");
    Customer_Id="";
    C2_Ref="";
    Service_Reference="";

  }

  if (Service_Reference.length()==0)
    ActionService = "alert('You must select a Circuit for this option')";
  else
    ActionService = "submitForm(this)";

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
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="Menu" method="post" action="WelcomeHandler.jsp">
      <%=
      HB.getBANMenu_Bar("Menu","","","")
      %>
      <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
	  <tr>
            <td>&nbsp;
            </td>
	    <td valign="top"><br>
	      <h3 align="center"><b>LLU Circuit Speed Update - Circuit Selection</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <table width="100%" border="0">
		<tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=2><font color="#000000"><b>Select the required Customer and Circuit</b></font>
		  </td>
		  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td width="154">
                    &nbsp;
                  </td>
                </tr>
	        <tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=1>
                    <font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("LLU_Customer","submit",Customer_Id,"")%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td width="154">
                    &nbsp;
                  </td>
                </tr>
	        <tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=1>
                    <font color="#0000FF">C2 Ref No:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("C2_Ref","submit",C2_Ref,Customer_Id)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td width="154">
                    &nbsp;
                  </td>
                </tr>
	        <tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=1>
                    <font color="#0000FF">Circuit Id:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("Service_Reference","submit",Service_Reference,Customer_Id,C2_Ref)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
                <tr>
                  <td width="154">
                    &nbsp;
                  </td>
                </tr>
		<tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=2><font color="#000000"><b>Please select the type of BAN you wish to create</b></font>
		  </td>
		  <td>&nbsp;</td>
		</tr>
		<tr>
                  <td width="154">
                    &nbsp;
                  </td>
                </tr>
		<tr>
                  <td width="154">
                    &nbsp;
                  </td>
		  <td align=left colspan=2>
                    <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
                    <%=HB.getButton("Amend a Service",ActionService)%>
		  </td>
		  <td>&nbsp;</td>
		</tr>
	      </table>
	      </td>
            <td>&nbsp;
            </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
    </form>
    </tr>
  </table><!--1-->
</body>
</html>