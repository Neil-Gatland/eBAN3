//Title:        HTMLBean
//Version:      1
//Copyright:    Copyright The Judge (c) 1999
//Author:       Judge Jules
//Company:      EDJ Computing Ltd
//Description:  HTML Stuff

package HTMLUtil;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Calendar;
import JavaUtil.StringUtil;
import JavaUtil.EBANProperties;

public class ModHTMLBean
{
	//Private Data

	private Enumeration Menu_Buttons;
	private Enumeration Menu_Headings;
	private Enumeration Menu_Options;
	private String Option_Array[][];
	private String HTML;
	private String Quote="\"";
	private JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
	private String sharedPath=
          EBANProperties.getEBANProperty(EBANProperties.SHAREDPATH);
	private String topPos;
        private String system;
        private String groupName;
        private int tabIndex;
        private String Status;
        private boolean custSelected;
        private String alt;
        private String status;
        private boolean isDirect;
        private boolean fromEdify;

	//Private Methods

	//Public Methods
	public ModHTMLBean()
	{
          topPos = "40";
          system = "";
          groupName = "";
          tabIndex = 0;
          Status = "";
          alt = "";
          isDirect = false;
          fromEdify = false;
          custSelected = false;
	}
	public void setOption_Array(String newMenu_Options[][])
	{
		Option_Array = newMenu_Options;
	}
	public void setMenu_Headings(String newMenu_Headings[])
	{
		Menu_Headings = SU.toEnumeration(newMenu_Headings);
	}
        public void setGroupName(String value)
        {
          groupName = value;
        }
/***************************************************************************************/
	public String getDays(String form, String label, int selected,
          boolean withBlank, boolean disabled, int ti)
	{
          tabIndex = ti;
          String select = getDays(form, label, selected, withBlank, disabled);
          tabIndex = 0;
          return select;
        }
/***************************************************************************************/
	public String getDays(String form, String label, int selected,
          boolean withBlank, boolean disabled)
	{
          StringBuffer html = new StringBuffer();
          html.append("<select " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
            (disabled?"disabled ":" ")+"style=\"width:40px\" id=\""+label+"Day\" name=\""+label+
            "Day\" onchange=\"ChangeHiddenDate('"+form+"','"+label+"',"+
            (withBlank?"true":"false")+")\">");
          Calendar rightNow = Calendar.getInstance();
          int thisDay = rightNow.get(rightNow.DAY_OF_MONTH);
          int daysInMonth = withBlank?31:rightNow.getActualMaximum(rightNow.DAY_OF_MONTH);
          int selectedDay = (selected==0)?(withBlank?0:thisDay):selected;
          if (withBlank)
          {
            html.append("<option");
            if (selected == 0)
              html.append(" selected ");
            html.append("></option>");
          }
          for (int i=1; i<=daysInMonth; i++)
          {
            html.append("<option");
            if (i == selectedDay)
              html.append(" selected ");
            html.append(">"+i+"</option>");
          }
          html.append("</select>");
          return html.toString();
        }

/***************************************************************************************/
	public String getDays(String form, String label, int selected,
          boolean withBlank, boolean disabled, int month, int year)
	{
          StringBuffer html = new StringBuffer();
          html.append("<select " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
            (disabled?"disabled ":" ")+"style=\"width:40px\" id=\""+label+"Day\" name=\""+label+
            "Day\" onchange=\"ChangeHiddenDate('"+form+"','"+label+"',"+
            (withBlank?"true":"false")+")\">");
          Calendar rightNow = Calendar.getInstance();
          rightNow.set(rightNow.DATE, selected);
          rightNow.set(rightNow.MONTH, month-1);
          rightNow.set(rightNow.YEAR, year);
          //int daysInMonth = withBlank?31:rightNow.getActualMaximum(rightNow.DAY_OF_MONTH);
          int daysInMonth = month==0?31:rightNow.getActualMaximum(rightNow.DAY_OF_MONTH);
          int selectedDay = selected;
          if (withBlank)
          {
            html.append("<option");
            if (selected == 0)
              html.append(" selected ");
            html.append("></option>");
          }
          for (int i=1; i<=daysInMonth; i++)
          {
            html.append("<option");
            if (i == selectedDay)
              html.append(" selected ");
            html.append(">"+i+"</option>");
          }
          html.append("</select>");
          return html.toString();
        }

/***************************************************************************************/
	public String getMonths(String form, String label, int selected,
          boolean withBlank, boolean disabled, int ti)
	{
          tabIndex = ti;
          String select = getMonths(form, label, selected, withBlank, disabled);
          tabIndex = 0;
          return select;
        }
/***************************************************************************************/
	public String getMonths(String form, String label, int selected,
          boolean withBlank, boolean disabled)
	{
          StringBuffer html = new StringBuffer();
          html.append("<select " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
            (disabled?"disabled ":" ")+"style=\"width:50px\" id=\""+label+"Month\" name=\""+label+
            "Month\" onchange=\"ChangeOptionDays('"+form+"','"+label+"',"+
            (withBlank?"true":"false")+",true)\">");
          Calendar rightNow = Calendar.getInstance();
          int thisMonth = rightNow.get(rightNow.MONTH)+1;
          int selectedMonth = (selected==0)?(withBlank?0:thisMonth):selected;
          String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"};
          if (withBlank)
          {
            html.append("<option");
            if (selected == 0)
              html.append(" selected ");
            html.append("></option>");
          }
          for (int i=1; i<13; i++)
          {
            html.append("<option");
            if (i == selectedMonth)
              html.append(" selected ");
            html.append(" value=\""+i+"\">"+months[i-1]+"</option>");
          }
          html.append("</select>");
          return html.toString();
        }

/***************************************************************************************/
	public String getYears(String form, String label, int selected,
          boolean withBlank, boolean disabled, int ti)
	{
          tabIndex = ti;
          String select = getYears(form, label, selected, withBlank, disabled);
          tabIndex = 0;
          return select;
        }
/***************************************************************************************/
	public String getYears(String form, String label, int selected,
          boolean withBlank, boolean disabled)
	{
          StringBuffer html = new StringBuffer();
          html.append("<select " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
            (disabled?"disabled ":" ")+"style=\"width:60px\" id=\""+label+"Year\" name=\""+label+
            "Year\" onchange=\"ChangeOptionDays('"+form+"','"+label+"',"+
            (withBlank?"true":"false")+",false)\">");

          Calendar rightNow = Calendar.getInstance();
          int thisYear = rightNow.get(rightNow.YEAR);
          int selectedYear = (selected==0)?(withBlank?0:thisYear):selected;
          if (withBlank)
          {
            html.append("<option");
            if (selected == 0)
              html.append(" selected ");
            html.append("></option>");
          }
          for (int i=(thisYear-10); i<(thisYear+10); i++)
          {
            html.append("<option");
            if (i == selectedYear)
              html.append(" selected ");
            html.append(">"+i+"</option>");
          }
          html.append("</select>");
          return html.toString();
        }

/***************************************************************************************/
	public String getButton(String label)
	{
		HTML="<button name=\""+label+"\" id="+SU.before(label)+" class=\"menu\" ";
		//HTML = HTML+" onMouseOver=\"this.className='menu_lit'\" ";
		//HTML = HTML+" onMouseout=\"this.className='menu'\" onClick=\"submitForm(this)\">"+label+"</button><br>";
		HTML = HTML+" onClick=\"submitForm(this)\">"+label+"</button><br>";
		return HTML;
	}
/***************************************************************************************/
	public String getButton(String label,String action)
	{
		if ((action.startsWith("submit")) ||(action.startsWith("window.open")))
		{
		 HTML="<button class=\"menu_bold\" onClick=\""+action+"\">"+label+"</button><br>";
		}
		else
		{
		  HTML="<button class=\"menu\" onClick=\""+action+"\">"+label+"</button><br>";
		}
		//HTML = HTML+" onMouseOver=\"this.className='menu_lit'\" onMouseout=\"this.className='menu'\" >"+label+"</button><br>";
		return HTML;
	}
/***************************************************************************************/
	public String getButtonLink(String Option)
	{
		HTML="<input type=image name=\""+Option+"\" value=\""+Option+"\" src=\""+sharedPath+"/nr/cw/newimages/link_button.gif\">";
		HTML = HTML+" <a name=\"Option  value=\""+Option+"\" class=\"navchild\" onClick=\"submitLink(this)\"><small>"+Option+"</a><br>";
		return HTML;
	}
/***************************************************************************************/
	public String getImageAsAnchor(String ImgName, int ti)
	{
          tabIndex = ti;
          String image = getImageAsAnchor(ImgName);
          tabIndex = 0;
          return image;
        }
/***************************************************************************************/
	public String getImageAsAnchor(String ImgName, String text)
	{
          alt = text;
          String image = getImageAsAnchor(ImgName);
          alt = "";
          return image;
        }
/***************************************************************************************/
	public String getImageAsAnchor(String ImgName)
	{
          StringBuffer HB = new StringBuffer();
          HB.append("<a " + (tabIndex>0?"tabindex="+Quote+tabIndex+Quote+ " ":"") +
            "href="+Quote +"javascript:"+ImgName+"Click()"+Quote +" ");
          HB.append(" onmouseout="+Quote +"MM_swapImgRestore()"+Quote);
          HB.append(" onmouseover="+Quote+"MM_swapImage('"+ImgName+"','','"+
            sharedPath+"/nr/cw/newimages/"+ImgName+"_lit.gif',1)"+Quote+"> ");
          HB.append("<img id="+Quote+ImgName+Quote+" name="+Quote+ImgName+Quote+
            " border=0 src="+Quote+sharedPath+"/nr/cw/newimages/"+ImgName+".gif"
            +Quote+(alt.equals("")?"":" alt="+Quote+alt+Quote) +
            "></a>");
          return HB.toString();
	}
/***************************************************************************************/
	public String getImage(String ImgName)
	{

		HTML="<input type=image name='"+ImgName;
		HTML = HTML+"' border=0 src='"+sharedPath+"/nr/cw/newimages/"+ImgName+".gif' ";
		HTML = HTML+" onmouseout="+Quote +"src='"+sharedPath+"/nr/cw/newimages/"+ImgName+".gif'"+Quote;
		HTML = HTML+" onmouseover="+Quote+"src='"+sharedPath+"/nr/cw/newimages/"+ImgName+"_lit.gif'"+Quote+">";
		return HTML;
	}
/***************************************************************************************/
  public String getMenu_Buttons(String Orientation)
  {
	String HTML="<table>";
	String Quote="\"";
	String Start="";
	String End="";
	String Button="";

	if (Orientation=="Horizontal")
	{
		Start="<td>";
		End="</td>";
	}
	else
	{
		Start="<tr><td>";
		End="</td></tr>";
	}
	try
	{
		while (Menu_Buttons.hasMoreElements())
		{
			Button=(String)Menu_Buttons.nextElement();
			HTML = HTML+Start+"<input type=image name='"+Button;
			HTML = HTML+"' src='"+sharedPath+"/nr/cw/newimages/"+Button+".gif' ";
			HTML = HTML+" onmouseout="+Quote +"src='"+sharedPath+"/nr/cw/newimages/"+Button+".gif'"+Quote;
			HTML = HTML+" onmouseover="+Quote+"src='"+sharedPath+"/nr/cw/newimages/"+Button+"_lit.gif'"+Quote+">"+End;
		}
	}
	catch(java.lang.NullPointerException ne){HTML=HTML+(ne.getMessage());}
		HTML = HTML+"</td></tr></table>";
	return HTML;
  }
/***************************************************************************************/
  public String getMenu_Bar()
  {
 	StringBuffer HTML = new StringBuffer("<table cellSpacing=0 cellPadding=0 width=\"100%\" border=0 class=\"headerfooter\"><tr>");
	String Quote="\"";
	String Option="";
	String Heading="";
	Enumeration Buttons;
	int i=0;
	try
	{
	  while (Menu_Headings.hasMoreElements())
	  { //First set the menu bar heading
            Heading=(String)Menu_Headings.nextElement();
	    HTML.append("<td class=gridheader><SPAN onmouseover=\"Open_Menu('").append(Heading);
	    HTML.append("')\" style=\"WIDTH: 100%; POSITION: absolute; TOP: "+topPos+"px\" onmouseout=\"Close_Menu('");
	    HTML.append(Heading).append("')\">\n");
	    HTML.append("<Button class=\"menu_bar\">").append(Heading).append("<SPAN id=\"").append(Heading);
	    HTML.append("\"style=\"LEFT: 0px; VISIBILITY: hidden; WIDTH: 100px; POSITION: absolute; TOP: 27px\">");
	    if (Heading != "Go To")
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    else
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    Menu_Options = SU.toEnumeration(Option_Array[i]);
	    while (Menu_Options.hasMoreElements())
	    {//now set the dropdown options for this heading
	        Option=(String)Menu_Options.nextElement();
		HTML.append("<TR><TD class=grid1><button class=dynamic_menu ");
                if (/*(system.equals("AdHoc")) &&*/ (!Option.equals("")) &&
                  (!Option.startsWith(" ")))
                {
                  HTML.append(" id=\""+Option+"\" onMouseOver=\"menuOver('"+Option+
                    "');\" onMouseOut=\"menuOut('"+Option+"');\" ");
                }
                HTML.append("onClick=");

		if ((Option.compareTo("Back")==0) || (Option.compareTo("Preceding Page")==0))
		{
		  HTML.append("\"history.back()\">");
		}
		else if (Option.compareTo("Main Menu")==0)
		{
		  HTML.append("\"location.href='Welcome.jsp'\">");
		}
		else if (Option.compareTo("Logout")==0)
		{
		  HTML.append("\"location.href='Logout.jsp'\">");
		}
		//else if (Option.compareTo("Customer BAN Menu")==0)
		//{
		//  HTML.append("\"location.href='CustomerBANMenu.jsp'\">");
		//}
		else if (Option.startsWith("Reject"))
		{
  		  HTML.append("\"getRejectReason()\">");
		}
		else if ((Option.equals("Authorise")) &&
                         (!groupName.equals("STP")))
		{
  		  HTML.append("\"getAuthorisationDetails()\">");
		}
		else if (Option.startsWith("Return"))
		{
  		  HTML.append("\"showReturn()\">");
		}
		else if (Option.startsWith("Show Filters"))
		{
                  HTML.append("\"Show_"+system+"_Filters()\">");
		}

		else if (Option.startsWith(" "))
		{
		  HTML.append( "\"\">");
		}
		else
		{
		  HTML.append("\"submitForm(this)\">");
		}
		HTML.append(Option).append("</button></TD></TR>");
	    }//end while
	    HTML.append("</TABLE></SPAN></SPAN></TD>");
	    i++;
	  }//end while
	  //Now right fill menu bar with blank buttons
	  /*if (i<8)
	  {
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: 112px\">\n");
	    HTML.append("<Button class=\"menu_bar\">&nbsp</SPAN></TD>");
	  }*/
	  for (i=i;i<9;i++)
	  {
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: "+topPos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">&nbsp</SPAN></TD>");
	  }
	}//end try
	catch(java.lang.NullPointerException ne){HTML.append(ne.getMessage());}
	HTML.append("</tr></table>");
	return HTML.toString();
  }
/*****************************************************************************************/
  public String getBANMenu_Bar(String Menu,String Mode, String Action, int top)
  {
    topPos = Integer.toString(top);
    String ret = getBANMenu_Bar(Menu, Mode, Action);
    topPos = "40";
    return ret;
  }
/*****************************************************************************************/
  public String getMenu_Bar(int top)
  {
    topPos = Integer.toString(top);
    String ret = getMenu_Bar();
    topPos = "40";
    return ret;
  }
/*****************************************************************************************/
  public String getMenu_Bar(int top, String system)
  {
    topPos = Integer.toString(top);
    this.system = system;
    String ret = getMenu_Bar();
    topPos = "40";
    system = "";
    return ret;
  }
/*****************************************************************************************/
  public String getBANMenu_Bar(String Menu,String Mode, String Action)
  {
 	StringBuffer HTML = new StringBuffer("<table cellSpacing=0 cellPadding=0 width=\"100%\" border=0 class=\"headerfooter\"><tr>");
	String Quote="\"";
	String Option="";
	String Heading="";
	Enumeration Buttons;
	int i=0;
	Enumeration BAN_Menu_Headings;
	String Standard_Menu_Headings[]={"","",""};
        String BAN_Option_Array[][] = {{"","","",""},{"","","",""},{"","",""}};

	if ((Menu.endsWith("BAN")) || (Menu.startsWith("List")))
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Options";
	  BAN_Option_Array[0][0]="Main Menu";
	  BAN_Option_Array[0][1]="Create BAN Menu";
	  BAN_Option_Array[0][2]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	}
	else if (Menu.compareTo("Welcome") == 0)
	{
	  Standard_Menu_Headings[0]="Go To";
	  BAN_Option_Array[0][0]="Connect to Desktop";
	  BAN_Option_Array[0][1]="";
	  BAN_Option_Array[0][2]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	}
	else if (Menu.compareTo("Menu") == 0)
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Options";
	  Standard_Menu_Headings[2]="Lists";

	  BAN_Option_Array[0][0]="Connect to Desktop";
	  BAN_Option_Array[0][1]="Main Menu";
	  BAN_Option_Array[0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";

	  BAN_Option_Array[1][0]="Clear";
	  BAN_Option_Array[1][1]="Use Previous Values";

	  BAN_Option_Array[2][0]="BANs for this Customer";
	  BAN_Option_Array[2][1]="Charge BANs for this Account";
	  BAN_Option_Array[2][2]="Charge BANs for this Circuit";
	}

	BAN_Menu_Headings=SU.toEnumeration(Standard_Menu_Headings);

	if (Menu.endsWith("BAN"))
	{
	  if (Menu.startsWith("Circuit1"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="Page 2 of 2";
	  }
	  else if (Menu.startsWith("Circuit2"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="Page 1 of 2";
	  }
	  else if (Menu.equals("CircuitBAN"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="";
	  }
	  if (Action.compareTo("Authorise") == 0)
	  {
	    BAN_Option_Array [0][2]="Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][0]="Authorise BAN";
	    BAN_Option_Array [1][1]="Return BAN";
	    BAN_Option_Array [1][2]="Reject BAN";
	  }
	  else if (Action.compareTo("Amend") == 0)
	  {
	    BAN_Option_Array [0][2]="Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][0]=system.equals("OSS")?"":"Save Draft Version of BAN";
	    BAN_Option_Array [1][1]="Submit BAN for Authorisation";
	    //BAN_Option_Array [1][1]="Submit BAN " + (System.equals("OSS")?"":"for Authorisation");
	  }
	  else if (Action.compareTo("Cancel") == 0)
	  {
	    BAN_Option_Array [0][2]="Back To List";
	    BAN_Option_Array [1][0]="Cancel BAN";
	    BAN_Option_Array [1][1]="";
	    BAN_Option_Array [1][2]="";
	  }
	  else if ((Action.compareTo("View") == 0) ||
		  (Action.compareTo("History") == 0))
	  {
	    BAN_Option_Array [0][2]="Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][0]="";
	    BAN_Option_Array [1][1]="";
	  }
	  else
	  {
	    BAN_Option_Array [1][0]=system.equals("OSS")?"":"Save Draft Version of BAN";
	    //BAN_Option_Array [1][1]="Submit BAN " + (System.equals("OSS")?"":"for Authorisation");
	    BAN_Option_Array [1][1]="Submit BAN for Authorisation";
	    BAN_Option_Array [1][2]="Clear ";
	  }
	}
	try
	{
	  while (BAN_Menu_Headings.hasMoreElements())
	  {
	    //First set the menu bar heading
            Heading=(String)BAN_Menu_Headings.nextElement();
	    if (Heading.compareTo("")==0)
	      break;
	    HTML.append("<td class=gridheader><SPAN onmouseover=\"Open_Menu('").append(Heading);
	    HTML.append("')\" style=\"WIDTH: 100%; POSITION: absolute; TOP: "+topPos+"px\" onmouseout=\"Close_Menu('");
	    HTML.append(Heading).append("')\">\n");
	    HTML.append("<Button class=\"menu_bar\">").append(Heading).append("<SPAN id=\"").append(Heading);
	    HTML.append("\"style=\"LEFT: 0px; VISIBILITY: hidden; WIDTH: 100px; POSITION: absolute; TOP: 27px\">");
	    if (Heading != "Go To")
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    else
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    Menu_Options = SU.toEnumeration(BAN_Option_Array[i]);
	    while (Menu_Options.hasMoreElements())
	    {//now set the dropdown options for this heading
	        Option=(String)Menu_Options.nextElement();
		HTML.append("<TR><TD class=grid1><button class=dynamic_menu onClick=");

		if ((Option.compareTo("Back")==0) || (Option.compareTo("Preceding Page")==0))
		{
		  HTML.append("\"history.back()\">");
		}
		else if (Option.compareTo("Main Menu")==0)
		{
		  HTML.append("\"location.href='Welcome.jsp'\">");
		}
		else if (Option.compareTo("Logout")==0)
		{
		  HTML.append("\"location.href='Logout.jsp'\">");
		}
		//else if (Option.compareTo("Customer BAN Menu")==0)
		//{
		//  HTML.append("\"location.href='CustomerBANMenu.jsp'\">");
		//}
		else if (Option.startsWith("Reject"))
		{
  		  HTML.append("\"getRejectReason()\">");
		}
		else if ((Option.equals("Authorise"))&&
                         (!groupName.equals("STP")))
		{
  		  HTML.append("\"getAuthorisationDetails()\">");
		}
		else if (Option.startsWith("Return"))
		{
  		  HTML.append("\"showReturn()\">");
		}
		else if (Option.startsWith("Show Filters"))
		{
  		  HTML.append("\"Show_OSS_Filters()\">");
		}

		else if (Option.startsWith(" "))
		{
		  HTML.append( "\"\">");
		}
		else
		{
		  HTML.append("\"submitForm(this)\">");
		}
		HTML.append(Option).append("</button></TD></TR>");
	    }//end while
	    HTML.append("</TABLE></SPAN></SPAN></TD>");
	    i++;
	  }//end while
	  //Now right fill menu bar with blank buttons
	  for (i=i;i<9;i++)
	  {
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%px; POSITION: absolute; TOP: "+topPos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">&nbsp</SPAN></TD>");
	  }
	  //Now put the system name in
	}//end try
	catch(java.lang.NullPointerException ne){HTML.append(ne.getMessage());}
	HTML.append("</tr></table>");
	return HTML.toString();
  }
/*******************************************************************************/
  public String getBANMenu_Bar(String Menu, String Mode, String Action,
    String System, boolean isD)
  {
    isDirect = isD;
    String ret = getBANMenu_Bar(Menu, Mode, Action, System);
    isDirect = false;
    return ret;
  }
/*******************************************************************************/
  public String getBANMenu_Bar(String Menu, String Mode, String Action,
    String System, String st)
  {
    Status = st;
    String ret = getBANMenu_Bar(Menu, Mode, Action, System);
    Status = "";
    return ret;
  }
/*******************************************************************************/
  public String getMainMenu_Bar(String System, String gcId)
  {
    custSelected = !gcId.equals("");
    String ret = getBANMenu_Bar("Menu", "", "", System);
    custSelected = false;
    return ret;
  }
/*******************************************************************************/
  public String getBANMenu_Bar(String Menu,String Mode, String Action,String System)
  {
 	StringBuffer HTML = new StringBuffer("<table cellSpacing=0 cellPadding=0 width=\"100%\" border=0 class=\"headerfooter\"><tr>");
	String Quote="\"";
	String Option="";
	String Heading="",System_Name="";
	Enumeration Buttons;
	int i=0;
	Enumeration BAN_Menu_Headings;
	String Standard_Menu_Headings[]={"","",""};
        String BAN_Option_Array[][] = {{"","","",""},{"","","","","",""},{"","",""}};

        //String pos = System.equals("OSS")?"128":"40";
        String pos = "40";

        if (System.compareTo("OSS") == 0)
	{
	  //System_Name="OSS Billing";
	  System_Name="&nbsp;";
	}
	else if (System.compareTo("GCB") == 0)
	{
  	  //System_Name="Data Billing";
	  System_Name="&nbsp;";
	}
	else if (System.compareTo("AdHoc") == 0)
	{
  	  System_Name="Ad Hoc Billing";
	}
	else if (System.compareTo("AMEX") == 0)
	{
  	  System_Name="Home Workers";
	}
	else if (System.compareTo("BAR") == 0)
	{
  	  System_Name="Managed Services";
	}
	else
	{
  	  System_Name=System;
	}
	if ((Menu.endsWith("BAN")) || (Menu.startsWith("List")))
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Options";
	  BAN_Option_Array[0][0]=(System.equals("OSS")?"":"Main Menu");
          if ((System.equals("AdHoc")) && (groupName.equals("Guest")))
          {
            BAN_Option_Array[0][1]="";
            BAN_Option_Array[0][2]="";
          }
          else
          {
            BAN_Option_Array[0][1]="Create " + (System.equals("AdHoc")?"Invoice":"BAN") + " Menu";
            BAN_Option_Array[0][2]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
          }
          if (System.equals("AMEX"))
          {
            BAN_Option_Array[0][1]="";
            BAN_Option_Array[0][2]="";
          }
	}
	else if ((Menu.startsWith("Test")))
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Options";
	  BAN_Option_Array[0][0]="Main Menu";
	  BAN_Option_Array[0][1]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	}
	else if (Menu.compareTo("Welcome") == 0)
	{
	  Standard_Menu_Headings[0]="Go To";
	  BAN_Option_Array[0][0]=(System.equals("OSS")||System.equals("GCB"))?groupName.equals("MANS")?"":"Connect to Desktop":"";
	  BAN_Option_Array[0][1]="";
	  BAN_Option_Array[0][2]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	}
	else if (Menu.compareTo("Report") == 0)
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Filter";
	  BAN_Option_Array[0][0]="Main Menu";
	  BAN_Option_Array[0][1]="";
	  BAN_Option_Array[0][2]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	  BAN_Option_Array[1][0]="Reset Filters";
	}
	else if (Menu.compareTo("Menu") == 0)
	{
	  Standard_Menu_Headings[0]="Go To";
	  Standard_Menu_Headings[1]="Options";
	  //Standard_Menu_Headings[2]=System.equals("AdHoc")?"":"Lists";
	  //Standard_Menu_Headings[2]=System.equals("AMEX")?"":"Lists";
	  //Standard_Menu_Headings[2]=fromEdify?"":"Info";

	  BAN_Option_Array[0][0]=(System.equals("OSS")||System.equals("GCB"))?groupName.equals("MANS")?"":"Connect to Desktop":"";
	  BAN_Option_Array[0][1]=(System.equals("OSS")?"":"Main Menu");
	  //BAN_Option_Array[0][1]="";
	  BAN_Option_Array[0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";

	  BAN_Option_Array[1][0]=!fromEdify&&custSelected?"Account Details":"Clear";
	  //BAN_Option_Array[1][1]=System.equals("AdHoc")?"":"Use Previous Values";
	  //BAN_Option_Array[1][1]=System.equals("AMEX")?"":"Use Previous Values";
	  BAN_Option_Array[1][1]="";
          if (!fromEdify&&custSelected)
          {
            BAN_Option_Array[1][1]="Exceptions";
            BAN_Option_Array[1][2]="Bill Submission";
            BAN_Option_Array[1][3]="Monthly Billing Log";
            BAN_Option_Array[1][4]="GCD Extract";
            BAN_Option_Array[1][5]="Clear";
          }
	}

	//BAN_Menu_Headings=SU.toEnumeration(Standard_Menu_Headings);

	if (Menu.startsWith("Test"))
	{
          if (Menu.endsWith("1"))
          {
	    BAN_Option_Array [1][1]="Clear";
	    BAN_Option_Array [1][2]="Page 2 of 3";
	    BAN_Option_Array [1][3]="Page 3 of 3";
          }
          if (Menu.endsWith("2"))
          {
	    BAN_Option_Array [1][1]="Clear";
	    BAN_Option_Array [1][2]="Page 1 of 3";
	    BAN_Option_Array [1][3]="Page 3 of 3";
          }
          if (Menu.endsWith("3"))
          {
	    BAN_Option_Array [1][1]="Clear";
	    BAN_Option_Array [1][2]="Page 1 of 3";
	    BAN_Option_Array [1][3]="Page 2 of 3";
          }
        }
	if (Menu.endsWith("BAN"))
	{
	  if (Menu.startsWith("Circuit1"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="";
	  }
	  else if (Menu.startsWith("Circuit2"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="Page 1 of 2";
	  }
	  else if (Menu.equals("CircuitBAN"))
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][2]="Clear";
	    BAN_Option_Array [1][3]="";
	  }
	  /*if (Action.compareTo("direct") == 0)
	  {
	    BAN_Option_Array [0][2]="";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout;
            if (Status.equals("Proposed"))
            {
              Standard_Menu_Headings[1]="Authorise Options";
              BAN_Option_Array [1][0]="Authorise BAN";
              BAN_Option_Array [1][1]="Return BAN";
              BAN_Option_Array [1][2]="Reject BAN";
              //Standard_Menu_Headings[2]="Amend Options";
              //BAN_Option_Array [2][0]="Save Draft Version of BAN";
              //BAN_Option_Array [2][1]="Submit BAN for Authorisation";
              Standard_Menu_Headings[2]="Cancel Options";
              BAN_Option_Array [2][0]="Cancel BAN";
            }
            else if (Status.equals("Draft") || Status.equals("Returned"))
            {
              Standard_Menu_Headings[1]="Amend Options";
              BAN_Option_Array [1][0]="Save Draft Version of BAN";
              BAN_Option_Array [1][1]="Submit BAN for Authorisation";
              Standard_Menu_Headings[2]="Cancel Options";
              BAN_Option_Array [2][0]="Cancel BAN";
            }
          }*/
	  if (Action.compareTo("Authorise") == 0)
	  {
	    BAN_Option_Array [0][2]=isDirect?"":"Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
	    BAN_Option_Array [1][0]="Authorise" + (System.equals("AdHoc")?"":" BAN");
	    BAN_Option_Array [1][1]="Return" + (System.equals("AdHoc")?"":" BAN");
	    BAN_Option_Array [1][2]="Reject" + (System.equals("AdHoc")?"":" BAN");
	  }
	  else if (Action.compareTo("Amend") == 0)
	  {
	    BAN_Option_Array [0][2]=isDirect?"":"Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
            if (System.equals("OSS"))
            {
              BAN_Option_Array [1][0]="";
            }
            else
            {
  	      BAN_Option_Array [1][0]="Save Draft" +
                (System.equals("AdHoc")?"/Provisional Invoice":" Version of BAN");
            }
	    BAN_Option_Array [1][1]=System.equals("AdHoc")?
              (groupName.equals("STP")?"Authorise":"Save Exception"):
              "Submit BAN " + (System.equals("OSS")?"":"for Authorisation");
	    BAN_Option_Array [1][2]=(System.equals("AdHoc")&&!groupName.equals("STP"))?
              "Submit for Authorisation":"";
	  }
	  else if (Action.compareTo("Cancel") == 0)
	  {
	    BAN_Option_Array [0][2]=isDirect?"":"Back To List";
	    BAN_Option_Array [1][0]="Cancel" + (System.equals("AdHoc")?"":" BAN");
	    BAN_Option_Array [1][1]="";
	    BAN_Option_Array [1][2]="";
	  }
	  else if ((Action.compareTo("View") == 0) ||
		  (Action.compareTo("Active") == 0) ||
		  (Action.compareTo("Processed") == 0)||
		  (Action.compareTo("Print") == 0)||
		  (Action.compareTo("History") == 0))
	  {
	    BAN_Option_Array [0][2]=isDirect?"":System.equals("OSS")?"":"Back To List";
	    BAN_Option_Array [0][3]=groupName.equals("MANS")?"Close":fromEdify?"":"Logout";
            if ((Action.equals("Processed") || Action.equals("Print")) &&
              (System.equals("AdHoc")) && (Status.equals("Implemented")) &&
              ((groupName.equals("Back")) || (groupName.equals("Admin"))))
            {
              BAN_Option_Array [1][0]="Create a Reversal";
              BAN_Option_Array [1][1]=groupName.equals("Admin")?"Print":"";
            }
            else
            {
              Standard_Menu_Headings[1]="";
              BAN_Option_Array [1][0]="";
              BAN_Option_Array [1][1]="";
            }
	  }
	  else
	  {
            if (System.equals("OSS"))
            {
              BAN_Option_Array [1][0]="";
            }
            else
            {
  	      BAN_Option_Array [1][0]="Save Draft" +
                (System.equals("AdHoc")?"/Provisional Invoice":" Version of BAN");
            }
	    BAN_Option_Array [1][1]=System.equals("AdHoc")?
              (groupName.equals("STP")?"Authorise":
              "Save Exception"):"Submit BAN " + (System.equals("OSS")?"":"for Authorisation");
	    BAN_Option_Array [1][2]=System.equals("AdHoc")?
              (groupName.equals("STP")?"Clear ":
              "Submit for Authorisation"):"Clear ";
            if (System.equals("AdHoc") && !groupName.equals("STP"))
              BAN_Option_Array [1][3]="Clear ";
	  }
	}

	BAN_Menu_Headings=SU.toEnumeration(Standard_Menu_Headings);

	try
	{
	  while (BAN_Menu_Headings.hasMoreElements())
	  {
	    //First set the menu bar heading
            Heading=(String)BAN_Menu_Headings.nextElement();
	    if (Heading.compareTo("")==0)
	      break;
	    HTML.append("<td class=gridheader><SPAN onmouseover=\"Open_Menu('").append(Heading);
	    HTML.append("')\" style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\" onmouseout=\"Close_Menu('");
	    HTML.append(Heading).append("')\">\n");
	    HTML.append("<Button class=\"menu_bar\">").append(Heading).append("<SPAN id=\"").append(Heading);
	    HTML.append("\"style=\"LEFT: 0px; VISIBILITY: hidden; WIDTH: 100px; POSITION: absolute; TOP: 27px\">");
            //" +(System.equals("OSS")?"29":"27") + "px\">");
	    if (Heading != "Go To")
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    else
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    Menu_Options = SU.toEnumeration(BAN_Option_Array[i]);
	    while (Menu_Options.hasMoreElements())
	    {//now set the dropdown options for this heading
	        Option=(String)Menu_Options.nextElement();
		HTML.append("<TR><TD class=grid1><button class=dynamic_menu ");
                if (/*(System.equals("AdHoc")) &&*/ (!Option.equals("")) &&
                  (!Option.startsWith(" ")))
                {
                  HTML.append(" id=\""+Option+"\" onMouseOver=\"menuOver('"+Option+
                    "');\" onMouseOut=\"menuOut('"+Option+"');\" ");
                }
                HTML.append("onClick=");

		if ((Option.compareTo("Back")==0) || (Option.compareTo("Preceding Page")==0))
		{
		  HTML.append("\"history.back()\">");
		}
		else if (Option.compareTo("Main Menu")==0)
		{
		  HTML.append("\"location.href='Welcome.jsp'\">");
		}
		else if (Option.compareTo("Logout")==0)
		{
		  HTML.append("\"location.href='Logout.jsp'\">");
		}
		else if (Option.compareTo("Close")==0)
		{
		  HTML.append("\"window.close()\">");
		}
		//else if (Option.compareTo("Customer BAN Menu")==0)
		//{
		//  HTML.append("\"location.href='CustomerBANMenu.jsp'\">");
		//}
		else if (Option.startsWith("Reject"))
		{
  		  HTML.append("\"getRejectReason()\">");
		}
		else if ((Option.equals("Authorise"))&&
                         (!groupName.equals("STP")))
		{
  		  HTML.append("\"getAuthorisationDetails()\">");
		}
		else if (Option.startsWith("Return"))
		{
  		  HTML.append("\"showReturn()\">");
		}
		else if (Option.startsWith("Show Filters"))
		{
  		  HTML.append("\"Show_OSS_Filters()\">");
		}
		else if (Option.startsWith("Save Exception"))
		{
  		  HTML.append("\"confirmException()\">");
		}
		else if (Option.equals("Clear "))
		{
  		  HTML.append("\"confirmClear"+(System.equals("OSS")?"OSS":"")+
                    "()\">");
		}
		else if (Option.equals("Print"))
		{
  		  HTML.append("\"printInvoice()\">");
		}
		else if (Option.startsWith(" "))
		{
		  HTML.append( "\"\">");
		}
		else
		{
		  HTML.append("\"submitForm(this)\">");
		}
		HTML.append(Option).append("</button></TD></TR>");
	    }//end while
	    HTML.append("</TABLE></SPAN></SPAN></TD>");
	    i++;
	  }//end while
	  //Now right fill menu bar with blank buttons
	  for (i=i;i<8;i++)
	  {
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">&nbsp</SPAN></TD>");
	  }
	  //Now put the system name in colums 8/9 of the menu bar
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">"+System_Name+"</SPAN></TD>");

	}//end try
	catch(java.lang.NullPointerException ne){HTML.append(ne.getMessage());}
	HTML.append("</tr></table>");
	return HTML.toString();
  }




/*  public String buildMenuHTML(Enumeration BAN_Menu_Headings,
    String[][] BAN_Option_Array, String pos)
  {
 	StringBuffer HTML = new StringBuffer("<table cellSpacing=0 cellPadding=0 width=\"100%\" border=0 class=\"headerfooter\"><tr>");
	String Quote="\"";
	String Option="";
	String Heading="",System_Name="";
	Enumeration Buttons;
	int i=0;
	try
	{
	  while (BAN_Menu_Headings.hasMoreElements())
	  {
	    //First set the menu bar heading
            Heading=(String)BAN_Menu_Headings.nextElement();
	    if (Heading.compareTo("")==0)
	      break;
	    HTML.append("<td class=gridheader><SPAN onmouseover=\"Open_Menu('").append(Heading);
	    HTML.append("')\" style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\" onmouseout=\"Close_Menu('");
	    HTML.append(Heading).append("')\">\n");
	    HTML.append("<Button class=\"menu_bar\">").append(Heading).append("<SPAN id=\"").append(Heading);
	    HTML.append("\"style=\"LEFT: 0px; VISIBILITY: hidden; WIDTH: 100px; POSITION: absolute; TOP: 27px\">");
            //" +(System.equals("OSS")?"29":"27") + "px\">");
	    if (Heading != "Go To")
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    else
	    {
	      HTML.append("<TABLE cellSpacing=0 cellPadding=0 width=220px border=2>\n");
	    }
	    Menu_Options = SU.toEnumeration(BAN_Option_Array[i]);
	    while (Menu_Options.hasMoreElements())
	    {//now set the dropdown options for this heading
	        Option=(String)Menu_Options.nextElement();
		HTML.append("<TR><TD class=grid1><button class=dynamic_menu ");
                if ((!Option.equals("")) &&
                  (!Option.startsWith(" ")))
                {
                  HTML.append(" id=\""+Option+"\" onMouseOver=\"menuOver('"+Option+
                    "');\" onMouseOut=\"menuOut('"+Option+"');\" ");
                }
                HTML.append("onClick=");

		if ((Option.compareTo("Back")==0) || (Option.compareTo("Preceding Page")==0))
		{
		  HTML.append("\"history.back()\">");
		}
		else if (Option.compareTo("Main Menu")==0)
		{
		  HTML.append("\"location.href='Welcome.jsp'\">");
		}
		else if (Option.compareTo("Logout")==0)
		{
		  HTML.append("\"location.href='Logout.jsp'\">");
		}
		else if (Option.compareTo("Close")==0)
		{
		  HTML.append("\"window.close()\">");
		}
		//else if (Option.compareTo("Customer BAN Menu")==0)
		//{
		//  HTML.append("\"location.href='CustomerBANMenu.jsp'\">");
		//}
		else if (Option.startsWith("Reject"))
		{
  		  HTML.append("\"getRejectReason()\">");
		}
		else if ((Option.equals("Authorise"))&&
                         (!groupName.equals("STP")))
		{
  		  HTML.append("\"getAuthorisationDetails()\">");
		}
		else if (Option.startsWith("Return"))
		{
  		  HTML.append("\"showReturn()\">");
		}
		else if (Option.startsWith("Show Filters"))
		{
  		  HTML.append("\"Show_OSS_Filters()\">");
		}
		else if (Option.startsWith("Save Exception"))
		{
  		  HTML.append("\"confirmException()\">");
		}
		else if (Option.equals("Clear "))
		{
  		  HTML.append("\"confirmClear"+(System.equals("OSS")?"OSS":"")+
                    "()\">");
		}
		else if (Option.equals("Print"))
		{
  		  HTML.append("\"printInvoice()\">");
		}
		else if (Option.startsWith(" "))
		{
		  HTML.append( "\"\">");
		}
		else
		{
		  HTML.append("\"submitForm(this)\">");
		}
		HTML.append(Option).append("</button></TD></TR>");
	    }//end while
	    HTML.append("</TABLE></SPAN></SPAN></TD>");
	    i++;
	  }//end while
	  //Now right fill menu bar with blank buttons
	  for (i=i;i<8;i++)
	  {
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">&nbsp</SPAN></TD>");
	  }
	  //Now put the system name in colums 8/9 of the menu bar
	    HTML.append("<TD class=gridheader><SPAN style=\"WIDTH: 100%; POSITION: absolute; TOP: "+pos+"px\">\n");
	    HTML.append("<Button class=\"menu_bar\">"+System_Name+"</SPAN></TD>");

	}//end try
	catch(java.lang.NullPointerException ne){HTML.append(ne.getMessage());}
	HTML.append("</tr></table>");
	return HTML.toString();
  }*/

  /*****************************************************************************/
  public void setFromEdify(boolean value)
  {
    fromEdify = value;
  }

  public boolean getFromEdify()
  {
    return fromEdify;
  }

  //for debugging
  public void main (String[] args)
  {
    HTMLBean HB = new HTMLBean();
    Enumeration e;
    String H;
    String Option_Array[][] = {{"Preceding Page","Main Menu","Create Customer BAN Menu"},
			      {"Save Draft Version of BAN","Submit Proposed BAN for Authorisation"}};
    //Vector Menu_Option = new Vector();
    String Menu_Headings[]={"Go To","Options"};
    //Enumeration Option=Menu_Option.elements();
    //Enumeration Menu_Options[]={Option,Option,Option};
    setMenu_Headings(Menu_Headings);
        //Menu_Option.addElement("Back");
	//Menu_Option.addElement("Main Menu");
	//Menu_Option.addElement("Create Customer BAN Menu");
        //Menu_Option.addElement("Logout");
	//Menu_Options[0]=Menu_Option.elements();
	//Menu_Option.clear();
        //Menu_Option.addElement("Save Draft Version of BAN");
        //Menu_Option.addElement("Submit Proposed BAN for Authorisation");
	//Menu_Options[1]=Menu_Option.elements();

	  /* Vector v = new Vector();
	  v.addElement("View Your Account");
	  v.addElement("View Your Statement");
	  v.addElement("Update Your Profile");
	  e=v.elements();
	  HB.setMenu_Buttons(e);
	  H=HB.getMenu_Buttons("Vertical");*/
	  H=HB.getButton("go");
	  System.out.println(H);
  }

}
