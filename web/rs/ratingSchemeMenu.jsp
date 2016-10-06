<html>
<%@ include file="../includes/Page_Header6.jsp"%>
<%@ page import="JavaUtil.*"%>
<jsp:useBean id="BAN" class="DBUtilities.BANBean" scope="session"/>
<jsp:useBean id="RSB" class="DBUtilities.RevenueShareBean" scope="session"/>
<jsp:useBean id="HB" class="HTMLUtil.HTMLBean" scope="session"/>
<jsp:useBean id="DBA" class="DBUtilities.DBAccess" scope="session"/>

<%  String PageSent;
    String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
    String colspan;
    String system;
    StringUtil SU = new JavaUtil.StringUtil();
    String Message;
    String ratingSchemeId = RSB.getRatingSchemeId();
    String fromCallMonth = RSB.getFromCallMonth();
    String toCallMonth = RSB.getToCallMonth();
    boolean ratingSchemeClosed = RSB.getRatingSchemeClosed();
    String actionRatingScheme = (!RSB.getRatingSchemeFound()||
      !toCallMonth.equals("")||ratingSchemeClosed)?
      "alert('You must select an open Rating Scheme for this option')":
      "submitForm(this)";
    String actionRatingSchemeCurrent = (!RSB.getRatingSchemeFound()||
      !toCallMonth.equals("")||ratingSchemeClosed||(fromCallMonth.compareTo(RSB.getLatestCallMonth())<0))?
      "alert('You must select an open Rating Scheme for this option')":
      "submitForm(this)";
    String actionAccRatingScheme = (!RSB.getRatingSchemeFound()||
      !toCallMonth.equals("")||!RSB.getSignName().equals("")||ratingSchemeClosed)?
      "alert('You must select an open, non-Default Rating Scheme for this option')":
      "submitForm(this)";
    String actionRSExists = RSB.getRatingSchemeFound()?
      "alert('This Rating Scheme exists already')":
      RSB.getFromCallMonth().compareTo(RSB.getLatestCallMonth())<0&&!RSB.getFromCallMonth().equals("")?
      "alert('From Call Month cannot be before current Call month for a new Rating Scheme')":
      "submitForm(this)";
//Set Initial values
  //Are we coming from the handler page, or a fresh start?

  session.setAttribute("formname","ratingSchemeMenu");
  system = (String)session.getAttribute("System");

  Message=SU.isNull(RSB.getMessage(),SU.isNull((String)session.getAttribute("Message"),""));
  RSB.setMessage("");
  PageSent=SU.isNull((String)session.getAttribute("PageSent"),"");
  String selectStyle = " style=\"width:400px\"";
  boolean confirm = RSB.getMode().equals("Confirm");
  RSB.setMode("");

//"New Rating Scheme",ratingSchemeId.equals("")?"":"Advance From Call Month",
  colspan = "5";
  String Menu_Headings[]={"Go To", "Options"};
  String Options[][] = {{"Log Off","Revenue Share Desktop"},
    {confirm?"Confirm":"",confirm?"Cancel":"","Reset","Refresh","View All Processes"}};
  HB.setMenu_Headings(Menu_Headings);
  HB.setOption_Array(Options);

%>
<script language="JavaScript">
function Toggle_Sort(buttonPressed)
{
  if (buttonPressed == 'Action')
  {
    window.open("ratingSchemeMenuKey.htm", "ndk", "toolbar=no,menubar=no,location=no," +
      "directories=no,status=no,scrollbars=no,resizable=no,height=200,width=200,top=50,left=50");
  }
}
</script>
<form name="ratingSchemeMenu" method="post" action="ratingSchemeMenuHandler.jsp">
  <input name="ButtonPressed" type=hidden value="">
  <input name="ratingSchemeId" type=hidden value="">
  <a name="top"></a>
  <table id=1 width="100%" border=0>
    <tr>
      <td colspan=3>
        <%@ include file="../includes/Page_Header4.htm"%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
      <%=HB.getMenu_Bar()%>
      </td>
    </tr>
    <tr>
      <td colspan=3>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id=2>
          <tr>
            <td colspan=4>
	      <h3 align="center"><b>Rating Scheme</b></h3>
	      <%=Message%><br>
	      <%session.setAttribute("Message","");%></font>
            </td>
	  </tr>
          <tr>
            <td colspan=4>
	      <table width="100%" border="0">
                <tr>
                  <td><b>Call Month:</b></td>
                  <td class=bluebold><%=RSB.getLatestCallMonth()%></td>
                  <td><b>Current Status:</b></td>
                  <td class=bluebold><%=RSB.getCurrentStatus()%></td>
                  <td><b>RQR09 Load Time:</b></td>
                  <td class=bluebold><%=RSB.getRQR09LoadTime()%></td>
                </tr>
                <tr>
                  <td><b>Rating Time:</b></td>
                  <td class=bluebold><%=RSB.getRatingTime()%></td>
                  <td><b>Invoicing Time:</b></td>
                  <td class=bluebold><%=RSB.getInvoicingTime()%></td>
                  <td><b>Ebilling Upload Time:</b></td>
                  <td class=bluebold><%=RSB.getEbillingUploadTime()%></td>
                </tr>
                <tr>
                  <td colspan="6">
                    <hr>
                  </td>
                </tr>
              </table>
            </td>
	  </tr>
	  <tr>
	    <td valign="top" colspan="6">
	      <table width="100%" border="0">
<!--
	        <tr>
		  <td align=left colspan=2><font color="#0000FF">Rating Scheme:</font>
		  </td>
		  <td align=left>
		    DBA.getListBox("rsRatingScheme","submit",ratingSchemeId,(String)session.getAttribute("User_Id"),1,selectStyle,true)
		  </td>
                  <td>&nbsp;</td>
		</tr>
-->
	        <tr>
		  <td align=left colspan=4  style="border:solid 2px blue">

                <table width="100%" border="0">
                  <tr>
                    <td rowspan="3" width="15%"><font color="#0000FF">Default Rating Scheme:</font>
                    </td>
                    <td rowspan="3" width="15%"><%=DBA.getListBox("signName","submit",RSB.getSignName(),"",1,"style=\"width:90%\"",true)%>
                    </td>
                    <td rowspan="3" width="5%" align="left"><font color="#0000FF">OR</font>
                    </td>
                    <td rowspan="3" width="5%" align="left"><font color="#0000FF">&nbsp</font>
                    </td>
                    <td width="15%"><font color="#0000FF">Provider:</font>
                    </td>
                    <td width="35%"><%=DBA.getListBox("providerId","submit",RSB.getProviderId(),"",1,"style=\"width:90%\"",true)%>
                    </td>
                  </tr>
                  <tr>
                    <td><font color="#0000FF">Master Account Number:</font>
                    </td>
                    <td><%=DBA.getListBox("masterAccountNumber","submit",RSB.getMasterAccountNumber(),RSB.getProviderId(),1,"style=\"width:90%\"",true)%>
                    </td>
                  </tr>
                  <tr>
                    <td><font color="#0000FF">Account Number:</font>
                    </td>
                    <td><%=DBA.getListBox("accountNumber","submit",RSB.getAccountNumber(),RSB.getMasterAccountNumber(),1,"style=\"width:90%\"",true)%>
                    </td>
                  </tr>
                </table>
                  </td>
                </tr>
		<tr>
                </tr>
                <tr>
                  <td colspan="4">
                    <table width="100%" border="0">
                      <tr>
                        <td align=left width="10%"><font color="#0000FF">Product Group:</font>
                        </td>
                        <td align=left colspan="2" width="25%">
                        <%=DBA.getListBox("productGroup","submit",RSB.getProductGroup(),"",1,"style=\"width:95%\"",true)%>
                        </td>
                        <td align=center width="15%">&nbsp;
                        </td>
                       <td align=center width="15%"><font color="#0000FF">Product:</font>
                        </td>
                        <td align=left colspan="2" width="30%">
                        <%=DBA.getListBox("productCode","submit",RSB.getProductCode(),RSB.getProductGroup(),1,"style=\"width:80%\"",true)%>
                        </td>
                      </tr>
                      <tr>
                        <td><font color="#0000FF">Rating Type:</font>
                        </td>
                        <td align=left width="12%">
                        <%=DBA.getListBox("ratingType","submit",RSB.getRatingType(),"RS Rating Type",1,"style=\"width:80%\"",true)%>
                        </td>
                        <td width="13%"><font color="#0000FF">From Call Month:</font>
                        </td>
                        <td align=left colspan="2">
                          <%=DBA.getListBox("fromCallMonth","submit",fromCallMonth,"000000",1,"style=\"width:30%\"",true)%>
                          <%if (!toCallMonth.equals(""))
                            {%>
                            <font color=red><b>(To Call Month: <%=toCallMonth%>)</b></font>
                          <%}
                            else if (ratingSchemeClosed)
                            {%>
                            <font color=red><b>(Closed)</b></font>
                          <%}%>
                        </td>
                        <td width="10%"><font color="#0000FF">Duration:</font>
                        </td>
                        <td align=left width="20%">
                        <%=DBA.getListBox("ratingDuration","submit",RSB.getRatingDuration(),"RS Duration",1,"style=\"width:70%\"",true)%>
                        </td>
                      </tr>
                    </table>
                  </td>
		</tr>
	      </table>
            </td>
	  </tr>
	  <tr>
	    <td valign="top" colspan="6">
	      <table width="100%">
		  <tr>
		    <td colspan="4">&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td colspan="4" valign="top"> <b>Please select from the list of maintenance options below</b>
		  </tr>
		  <tr>
		    <td colspan="4">&nbsp
		    </td>
		  </tr>
		  <tr>
		    <td height=20 width="25%">
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Create New Rating Scheme",actionRSExists)%>
		    </td>
		    <td height=20 valign=top width="25%">
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Add Range to Rating Scheme",actionRatingSchemeCurrent)%>
		    </td>
		    <td height=20 valign=top width="25%">
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Advance From Call Month For Rating Scheme",actionRatingScheme)%>
		    </td>
		    <td height=20 valign=top width="25%">
		      <img src='<%=sharedPath%>/nr/cw/newimages/menu_brown.gif'>&nbsp
		      <%=HB.getButton("Close Rating Scheme",actionAccRatingScheme)%>
		    </td>
                  </tr>
		</form>
	      </table>
	      </td>
	    </tr>
	    <tr>
	      <td colspan=3>&nbsp;
	      </td>
	      <td>&nbsp;
	      </td>
	    </tr>
	    <tr>
	      <td colspan=3>&nbsp;
	      </td>
	      <td>
                <table border=0 width="100%">
                  <tr class=gridHeader>
                    <td class=gridHeader NOWRAP valign=top width="150">
                      <button class=grid_menu>Range Start</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="150">
                      <button class=grid_menu>Range End</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="200">
                      <button class=grid_menu>Day Rate £</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="200">
                      <button class=grid_menu>Evening Rate £</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top width="200">
                      <button class=grid_menu>Weekend Rate £</button>
                    </td>
                    <td class=gridHeader NOWRAP valign=top>
                      <button class=grid_menu onclick="Toggle_Sort('Action')">Action <font color=red>(click here for key)</font></button>
                    </td>
                  </tr>
                  <tr>
                    <td colspan=6>
                    <iframe frameborder=0 width="100%" height=350 id=GridData name=GridData src="ratingSchemeGrid.jsp"></iframe>
                    </td>
                  </tr>
                </table>
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
