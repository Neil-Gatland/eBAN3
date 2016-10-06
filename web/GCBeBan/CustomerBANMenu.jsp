<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%! String SQL;
    String Global_Customer_Id="";
    String Product="";
    String Global_Customer_Name="";
    String Account_Id="",Division_Id="";
    String Service_Reference="";
    String Message,ActionAccount,ActionService,ActionCustomer,ActionDivision,ActionProduct;
    String Account_Qualifier="",Service_Qualifier="All",Customer_Qualifier="All";
    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBAccess DBA = new DBAccess();
    String PageSent;
    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
    String colspan;
    String system;
    String startComment;
    String endComment;
%>
<%//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  system = (String)session.getAttribute("System");

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  BAN.setMessage("");
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (Message.endsWith("charges for this Service<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Product=BAN.getProduct();
    session.setAttribute("Product",Product);
    Customer_Qualifier=SU.isBlank(Product,"All");
    //Customer_Qualifier=(String)session.getAttribute("User_Id");
    Global_Customer_Id=BAN.getGlobalCustomerId();
    Account_Qualifier=SU.isBlank(Global_Customer_Id,"");
    Account_Id=BAN.getAccount_Id();
    Division_Id=BAN.getDivision_Id();
    Service_Qualifier=SU.isBlank(Division_Id,"All");
    Service_Reference=BAN.getService_Reference();
  }
  else
  {
    //Store old values
    session.setAttribute("Product",BAN.getProduct());
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Account",BAN.getAccount_Id());
    session.setAttribute("Division",BAN.getDivision_Id());
    session.setAttribute("Service",BAN.getService_Reference());

    BAN.setService_Reference("");
    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");
    Global_Customer_Id="";
    Product="";
    Account_Id="";
    Service_Reference="";
    Division_Id="";
  }
  String selectStyle = " style=\"width:400px\"";

  /*if (Product.compareTo("")==0)
    {ActionProduct="alert('You must select a Product for this option')";}
  else
    {*/ActionProduct="submitForm(this)";//}
  if (Global_Customer_Id.compareTo("")==0)
    {ActionCustomer="alert('You must select a Customer for this option')";}
  else
    {ActionCustomer="submitForm(this)";}
  if (Account_Id.compareTo("")==0)
    {ActionAccount="alert('You must select an Account for this option')";}
  else
    {ActionAccount="submitForm(this)";}
  if (Division_Id.compareTo("")==0)
    {ActionDivision="alert('You must select a Division for this option')";}
  else
    {ActionDivision="submitForm(this)";}
  if ((SU.isNull(Service_Reference,"").compareTo("")==0) ||
      (SU.isNull(Service_Reference,"").startsWith("N/A")))
    {ActionService="alert('You must select a Service Reference for this option')";}
  else
    {ActionService="submitForm(this)";}

  //if (Product.equals("05"))
  //{
    colspan = "4";
    startComment = "";
    endComment = "";
  /*}
  else
  {
    colspan = "1";
    startComment = "<!--";
    endComment = "-->";
  }*/
  boolean fE = HB.getFromEdify();
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{fE?"":"Log Off",fE?"Connect to Desktop":"Desktop","Account Administration Main Menu"},
                            {"Clear",fE?"":"Raise Query"}};
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);

%>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="CustomerBANMenu" method="post" action="CustomerBANMenuHandler.jsp">
      <%=HB.getMenu_Bar()%>
      <input name="ButtonPressed" type=hidden value="">
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
	      <h3 align="center"><b>Account Administration - Create a BAN</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <b>Please select the entries from the lists relevant to the type of BAN you wish to create</b>
	      <table width="100%" border="0">
<!--
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Product:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("Product","submit",Product,"Product")%>
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
-->
<input type="hidden" name="Product" value="">
<input type="hidden" name="Division" value="">
<input type="hidden" name="Service" value="">
	        <tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("GCB_Customer","submit",Global_Customer_Id,(String)session.getAttribute("Act_As_User"),1,selectStyle,true)%>
<!--
		    DBA.getListBox("Product_Customer","submit",Global_Customer_Id,Customer_Qualifier)
-->
		  </td>
                  <td>&nbsp;</td>
		</tr>
<!--
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Division:</font>
		  </td>
		  <td  align=left>
		  DBA.getListBox("Division","submit",Division_Id,Account_Qualifier)%>
		  </td>
                  <td width="50%">&nbsp;</td>
		</tr>
		<tr>
		  <td  align=left>
		    <font color="#0000FF">Service Ref:</font>
		  </td>
		  <td align=right>
                  startComment
		  <font color="#0000FF">select
                  endComment
		  </td>
		  <td  align=left>
		    DBA.getListBox("Service","submit",Service_Reference,Service_Qualifier)
		  </td>
                  <td width="50%">
                    startComment
		    <font color="#0000FF">or enter a value
		    <input class=inp type=text name="Service_Ref" value="<%=Service_Reference%>"
		     onKeyPress="this.value=this.value.toUpperCase();checkEnter(event);">
                    endComment
		  </td>
		</tr>
-->
		<tr>
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("GCB_Account","submit",Account_Id,Account_Qualifier,1,selectStyle,true)%>
		  </td>
                  <td>&nbsp;</td>
		</tr>
	      </table>
	      <table border="0">
		  <tr>
		    <td colspan=<%=colspan%>>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=800 colspan=<%=colspan%> valign="top"> <b>Please select the type of BAN you wish to create</b>
		  </tr>
		  <tr>
		    <td colspan=<%=colspan%>>&nbsp
		    </td>
		  <tr>
                    <%=startComment%>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Customer",ActionProduct)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an Account",ActionCustomer)%>
		    </td>
<!--
		    <td height=20 valign=top width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Service",ActionDivision)%>
		    </td>
                    <%=endComment%>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Recurring Charge",ActionService)%>
		     </td>
-->
		  </tr>
		  <tr>
                    <%=startComment%>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Customer",ActionCustomer)%>
		    </td>
		    <td height=20 valign=top width=200>
		    <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		    <%=HB.getButton("Amend an Account",ActionAccount)%>
		    </td>
<!--
		    <td height=20 valign=top>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend Service Details",ActionService)%>
		     </td>
                    <%=endComment%>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend/Cease a Recurring Charge",ActionService)%>
		     </td>
-->
		  </tr>
                  <%=startComment%>
		  <tr>
		    <td height=20 width=200>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Deactivate a Customer",ActionCustomer)%>
		    </td>
		    <td>
		    </td>
<!--
		    <td height=20 valign=top>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Cease a Service",ActionService)%>
		    </td>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Credit",ActionService)%>
		     </td>
-->
		  </tr>
                  <%=endComment%>
<!--
		  <tr>
                  <%=startComment%>
		    <td colspan=3>
		    </td>
                  <%=endComment%>
		    <td height=20>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a One Off Charge",ActionService)%>
		     </td>
		  </tr>
                    <%=startComment%>
		  <tr>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an Adjustment",ActionAccount)%>
		     </td>
		  </tr>
		  <tr>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Discount Plan","alert('Not yet available')")%>
		     </td>
		  </tr>
		  <tr>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		    </td>
		    <td>
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Discount Plan","alert('Not yet available')")%>
		     </td>
-->
                   </tr>
                    <%=endComment%>
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
</body>
</html>
