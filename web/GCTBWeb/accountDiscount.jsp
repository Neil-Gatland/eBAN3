<html>
<jsp:include page="../includes/Page_Header6.jsp" flush="true" />
<%@ page import="JavaUtil.*,HTMLUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<% String SQL;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    String Global_Customer_Id=BAN.getGlobalCustomerId();
    String Account_Id=BAN.getAccount_Id();
    String Discount_Plan=BAN.getDiscountPlan();
    String Message;
    String ActionAccount;
    String ActionDiscount;
    String Account_Qualifier=SU.isBlank(Global_Customer_Id,"All");
    String Discount_Plan_Qualifier=SU.isBlank(Account_Id,"All");
    String Customer_Qualifier="All";
    //DBAccess DBA = new DBAccess();
    String PageSent;
    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
    String system = (String)session.getAttribute("System");
    String Menu_Headings[]={"Go To", "Options"};
    String Options[][] = {{"Desktop","Log Off"},
                            {"Refresh","Raise Query"}};
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  BAN.setMessage("");
  String selectStyle = " style=\"width:400px\"";

  if (Account_Id.compareTo("")==0)
    {ActionAccount="alert('You must select a Customer and Account for this option')";}
  else
    {ActionAccount="submitForm(this)";}
  if (Discount_Plan.compareTo("")==0)
    {ActionDiscount="alert('You must select a Discount Plan for this option')";}
  else
    {ActionDiscount="submitForm(this)";}

%>
<script language="JavaScript">
  function accChange()
  {
		accountDiscount.accountName.value = accountDiscount.GCB_Account[accountDiscount.GCB_Account.selectedIndex].text;
		accountDiscount.submit();
	}
</script>
<form name="accountDiscount" method="post" action="accountDiscountHandler.jsp">
<input name="ButtonPressed" type=hidden value="">
<input name="accountName" type=hidden value="">
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
	  <tr>
	    <td width="13">&nbsp;
	    <!-- this is a spacer column-->
	    </td>
	    <td width="100" valign="top" align="left">
	    <!--former menu column-->
	    </td>
	    <td width="12"><!-- a spacer column-->
		&nbsp;
	    </td>
	    <td valign="top">
	      <h3 align="center"><b>Account Discounts</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <table width="100%" border="0">
	        <tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Customer:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("GCB_Customer2","submit",Global_Customer_Id,BAN.getActAsLogon(),1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("GCB_Account","onChange=\"accChange()\"",Account_Id,Account_Qualifier,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Discount Plan:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Discount_Plan","submit",Discount_Plan,Discount_Plan_Qualifier,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
		  <tr>
                  <td height=20 colspan=2>&nbsp;</td>
		</tr>
		  <tr>
                  <td colspan=2>&nbsp;</td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Discount Plan",ActionAccount)%>
		    </td>
		  </tr>
		  <tr>
                  <td colspan=2>&nbsp;</td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Discount Plan",ActionDiscount)%>
		    </td>
		  </tr>
		</table>
		</form>
	      </td>
	    </tr>
	  </table><!--2-->
	</td>
      </tr>
     <tr>
      <td height="100">&nbsp
      </td>
    </tr>
      <!--Footer-->
      <tr>
        <td>
	  &nbsp;
	</td
      </tr>


</table><!--1-->
</form>
</body>
</html>


