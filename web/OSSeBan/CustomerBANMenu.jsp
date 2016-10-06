<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<%@ include file="../includes/Page_Header1.htm"%>
<%@ page import="DBUtilities.DBAccess,HTMLUtil.HTMLBean,JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>

<%! String SQL;
    String Global_Customer_Id="",Carrier_Name="";
    String Global_Customer_Name="";
    String Account_Id="";
    String Circuit_Reference="";
    String Message,ActionAccount,ActionCircuit,ActionCarrier,ActionCustomer;
    String Account_Qualifier="All",Circuit_Qualifier="All";
    HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    DBAccess DBA = new DBAccess();
    String PageSent;
%>
<%//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  Message=SU.isNull(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (Message.endsWith("charges for this circuit<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Global_Customer_Id=BAN.getGlobalCustomerId();
    Account_Qualifier=SU.isBlank(Global_Customer_Id,"All");
    Account_Id=BAN.getAccount_Id();
    Circuit_Qualifier=SU.isBlank(Account_Id,"All");
    Circuit_Reference=BAN.getCircuit_Reference();
    Carrier_Name=BAN.getCarrier_Name();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());
    session.setAttribute("Account",BAN.getAccount_Id());
    session.setAttribute("Circuit",BAN.getCircuit_Reference());
    session.setAttribute("Carrier",BAN.getCarrier_Name());

    BAN.setCircuit_Reference("");
    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setCarrier_Name("");
    Global_Customer_Id="";
    Account_Id="";
    Circuit_Reference="";
    Carrier_Name="";
  }
  if (Global_Customer_Id.compareTo("")==0)
    {ActionCustomer="alert('You must select a Customer for this option')";}
  else
    {ActionCustomer="submitForm(this)";}
  if (Account_Id.compareTo("")==0)
    {ActionAccount="alert('You must select an Account for this option')";}
  else
    {ActionAccount="submitForm(this)";}

  if ((SU.isNull(Circuit_Reference,"").compareTo("")==0) ||
      (SU.isNull(Circuit_Reference,"").startsWith("N/A")) ||
      (Message.endsWith("cancelled")) ||
      (Message.endsWith("Invalid Circuit Reference")))
    {ActionCircuit="alert('You must select a Circuit Reference for this option')";}
  else if ((Global_Customer_Id.compareTo("")==0) ||
	    (Account_Id.compareTo("")==0))
    {ActionCircuit="alert('This Circuit either has no owning Account or Customer ')";}
  else
    {ActionCircuit="submitForm(this)";}

  if (Carrier_Name.compareTo("")==0)
    {ActionCarrier="alert('You must select a Carrier for this option')";}
  else
    {ActionCarrier="submitForm(this)";}
%>
  <a name="top"></a>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header2.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="CustomerBANMenu" method="post" action="CustomerBANMenuHandler.jsp">
      <%=HB.getBANMenu_Bar("Menu","","",(String)session.getAttribute("System"))%>
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
	      <h3 align="center"><b>Create a BAN for an existing Customer</b></h3>
	       <%=Message%><br>
	       <%session.setAttribute("Message","");%></font>
	      <b>Please select the entries from the lists relevant to the type of BAN you wish to create</b>
	      <table width="100%" border="0">
	        <tr><!-- Customer-->
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("Global_Customer","submit",Global_Customer_Id,"")%>
		  </td>
		</tr>
		<tr>
		  <!-- Invoice Region-->
		  <td align=left colspan=2><font color="#0000FF">Account:</font>
		  </td>
		  <td  align=left>
		  <%=DBA.getListBox("Account","submit",Account_Id,Account_Qualifier)%>
		  </td>
		</tr>
		<tr>
		  <td  align=left>
		    <font color="#0000FF">Circuit Id:</font>
		  </td>
		  <td align=right>
		  <font color="#0000FF">select
		  </td>
		  <td  align=left>
		    <!--Circuit_Reference-->
		    <%=DBA.getListBox("Circuit","submit",Circuit_Reference,Circuit_Qualifier)%>
		    <font color="#0000FF">or enter a value
		    <input class=inp type=text name="Circuit_Ref" value="<%=Circuit_Reference%>"
		     onKeyPress="this.value=this.value.toUpperCase();checkEnter(event);">
		  </td>
		</tr>
		<tr>
		  <td  align=left colspan=2>
		    <font color="#0000FF">Carrier</font>
		  </td>
		  <td  align=left>
		    <!--Carrier-->
		      <%=DBA.getListBox("Carrier","submit",Carrier_Name,"")%>
		  </td>
		</tr>
	      </table>
	      <table border="0">
		  <tr>
		    <td colspan=3>&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td width=600 colspan=3 valign="top"> <b>Please select the type of BAN you wish to create</b>
		  </tr>
		  <tr>
		    <td colspan=3>&nbsp
		    </td>
		  <tr>
		    <td height=20 valign=top width=200>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Customer Account",ActionCustomer)%>
		    </td>
		    <td height=20 valign=top width=200>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Circuit",ActionAccount)%>
		    </td>
		    <td width=200>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Recurring Charge",ActionCircuit)%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		    <%=HB.getButton("Amend a Customer's Account",ActionAccount)%>
		    </td>
		    <td height=20 valign=top>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend Circuit Details",ActionCircuit)%>
		     </td>
		    <td>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Recurring Charge",ActionCircuit)%>
		     </td>
		  </tr>
		  <tr>
		    <td width=200>
		    </td>
		    <td height=20 valign=top>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Cease a Circuit",ActionCircuit)%>
		    </td>
		    <td>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Cease a Recurring Charge",ActionCircuit)%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    </td>
		    <td>
		    </td>
		    <td>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Credit",ActionCircuit)%>
		     </td>
		  </tr>
		  <tr>
		    <td>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a Carrier","submitForm(this)")%>
		    </td>
		    <td>
		    </td>
		    <td>
		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create a One Off Charge",ActionCircuit)%>
		     </td>
		  </tr>
		  <tr>
		    <td>
    		      <img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Amend a Carrier",ActionCarrier)%>
		    </td>
		    <td>
		    </td>
		    <td>
		      <!--<img src='../nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create an Adjustment",ActionAccount)%>
                      -->
		     </td>
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
	  <%@ include file="../includes/Footer.htm"%>
	</td
      </tr>
  </table><!--1-->
</body>
</html>


