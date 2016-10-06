<html>
<jsp:include page="../includes/Page_Header5.htm" flush="true" />
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<%  String SQL;
    String Global_Customer_Id="";
    String Global_Service_Reference="";
    String Invoice_Region="";
    String Status;
    String BAN_Date;
    String Qualifier="";

    String action;
    String Menu_Headings[]={"Go To","List","Filter"};
    String Group_Name=(String)session.getAttribute("Group_Name");
    boolean fE = HB.getFromEdify(); 
    String Options[][] = {{Group_Name.equals("MANS")?"Close":fE?"":"Log Off",Group_Name.equals("MANS")?" ":fE?"":"Desktop","Account Administration Main Menu"," "," "},
			    {"All BANs","Selected BANs"},
			    {"Show Filters","Most Recent Values","Reset Filters"}};
    //HTMLUtil.HTMLBean HB = new HTMLUtil.HTMLBean();
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    //Define the key values to be stored in the Radio button
    String[] Key={"BAN_Identifier"};
    %>
    <%
    HB.setMenu_Headings(Menu_Headings);
    HB.setOption_Array(Options);

    if (SU.isNull((String)request.getQueryString(),"") != "")
    {//First time thru
      BAN.setOrderBy("Status");
      action=request.getQueryString();
      BAN.setBANs_to_List("All BANs");
      if(action.compareTo("Returned")==0)
      {
	BAN.setStatus_for_List("Returned");
	action="Amend";
      }
      BAN.setAction(action);
      BAN.setShowFilters("visible");
    }
    else
    {
      action=BAN.getAction();
    }
    %>
  <table width="100%">
  <tr><!-- This row holds the standard C&W intranet bar-->
    <td>
      <%@ include file="../includes/Page_Header4.htm"%>
      <form name="listBans" method="post" action="ListBansHandler.jsp">
        <%=HB.getMenu_Bar(40)%>
        <input name="ButtonPressed" type=hidden value="">
        <input type=hidden name="BAN_Id" value=" ">
	<input type=hidden name="OrderBy" value="">
    </td>
  </tr>
  <tr>
   <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
      <tr>
	<!-- this is a spacer column-->
	<td width="1" id=2>&nbsp;</td>
	<td width="1" valign="top" align="left" id=2>
	</td>
	<td width="12" id=2><!-- a spacer column-->
	  &nbsp;
	</td>
	<td valign="top" id=2>
	  <h2 align="left">List BANs</h2>
	  <table width="100%" border="0" id=3>
	    <tr>
	    <td width=100% class=bluebold>
	    <%=SU.isNull((String)session.getAttribute("Message"),"")%><br>
	    <%
	      session.setAttribute("formname","listBans");
	      session.setAttribute("Message","");
	    %>
	      Click on the headings to set the filter values
	      <%=BAN.getGCBHeader()%>
	      <%if(SU.isNull((String)request.getQueryString(),"").compareTo("View") != 0)
	      {//First time thru on a general lookup, don't want to waste time listing all
		%>
	        <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="Grid.jsp" ></iframe>
	      <%}%>
	    </td>
	  </tr>
	</table><!--table 3-->
	</form>
      </td>
      <td width="1">&nbsp;</td><!--spacer-->
      <td width="1" valign="top"><!--Right Hand column-->
      </td>
    </tr>
  </table><!--2-->
</table><!--1-->
</body>
</html>


