<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%! String SQL;
    String Global_Customer_Id="",Carrier_Name="";
    String Global_Customer_Name="";
    String Site_Reference="";
    String Message,ActionCustomer;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //DBAccess DBA = new DBAccess();
    String PageSent;
%>
<%//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  Message=SU.isBlank(BAN.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  if (PageSent.endsWith("BANMenuHandler.jsp")
  || (PageSent.endsWith("FromEdify2.jsp"))
  || (Message.endsWith("charges for this circuit<br>"))
  || (PageSent.endsWith("BANMenuHandler.jsp") && BAN.getAction().compareTo("Authorise")==0))
  {
    Global_Customer_Id=BAN.getGlobalCustomerId();
  }
  else
  {
    //Store old values
    session.setAttribute("Global_Customer",BAN.getGlobalCustomerId());

    BAN.setAction("");
    BAN.setCircuit_Reference("");
    BAN.setSite("");
    BAN.setGlobalCustomerId("");
    BAN.setGlobalCustomerName("");
    BAN.setAccount_Id("");
    BAN.setDivision_Id("");
    BAN.setCarrier_Name("");
    BAN.setBanIdentifier("");
    Global_Customer_Id="";
  }
  if (Global_Customer_Id.compareTo("")==0)
    {ActionCustomer="alert('You must select a Customer for this option')";}
    {ActionCustomer="submitForm(this)";}

  //new bit for managed services - TA 04/05/2005
  boolean isMANS = BAN.getGroupName().equals("MANS");
  String imgSrc = "<img src='../nr/cw/newimages/menu_brown.gif'>";
  String customerQualifier = "";
  String selectMode = BAN.getEdifySetCustomer()?"DISABLED":"submit";
  String createFixedCharge = HB.getButton("Create a Fixed Charge",ActionCustomer);
  String amendFixedCharge = HB.getButton("Amend a Fixed Charge",ActionCustomer);
  String deleteFixedCharge = HB.getButton("Delete a Fixed Charge",ActionCustomer);
  String selectStyle = " style=\"width:300px\"";
%>
  <a name="top"></a>
  <script language="JavaScript">
  <!--
  function Site_Reference_List_Process()
  {
    OSSBANMenu.ButtonPressed.value = 'Amend Site 2';
    OSSBANMenu.submit();
  }
  //-->
  </script>
  <table id=1 width ="100%">
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <form name="GDBANMenu" method="post" action="GDBANMenuHandler.jsp">
      <%=HB.getBANMenu_Bar("Menu","","",(String)session.getAttribute("System"))%>
      <input name="ButtonPressed" type=hidden value="">
      </td>
    </tr>
    <tr>
      <td colspan=3>
      &nbsp;
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
		<tr>
                  <td colspan="2">&nbsp;</td>
		</tr>
	        <tr><!-- Customer-->
		  <td align=left colspan=2><font color="#0000FF">Customer Name:</font>
		  </td>
		  <td align=left>
		    <%=DBA.getListBox("Globaldial_Customer",selectMode,Global_Customer_Id,customerQualifier,1,selectStyle,true)%>
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
		    </td>
		    <td height=20 valign=top width=200>
		    </td>
		    <td width=200>
		      <%=imgSrc%>&nbsp
		      <%=createFixedCharge%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    </td>
		    <td height=20 valign=top>
		     </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=amendFixedCharge%>
		     </td>
		  </tr>
		  <tr>
		    <td height=20 valign=top width=200>
		    </td>
		    <td height=20 valign=top>
		     </td>
		    <td>
		      <%=imgSrc%>&nbsp
		      <%=deleteFixedCharge%>
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
	</td
      </tr>
  </table><!--1-->
</body>
</html>


