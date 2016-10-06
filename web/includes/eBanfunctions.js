function openeBAN(URL)
{
	var newURL;
	newURL=URL.substring(0,URL.indexOf("htm"))+"jsp";
	window.open(newURL, "eBAN","width=950,height=700,top=0,left=0,status=yes,resizable=yes,scrollbars=yes,menubar=yes");
}
function checkEnter(event)
{
	var code = 0;

	if (navigator.appName == "Netscape")
		code = event.which;
	else
		code = event.keyCode;
	if (code==13)
	 {
	   document.all.ButtonPressed.value="Circuit";
	   //document.forms[CustomerBANMenu].submit();
         }
}
var weekend = [0,6];
var weekendColor = "#e0e0e0";
var fontface = "Verdana";
var fontsize = 2;

var gNow = new Date();
var ggWinCal;
isNav = (navigator.appName.indexOf("Netscape") != -1) ? true : false;
isIE = (navigator.appName.indexOf("Microsoft") != -1) ? true : false;

Calendar.Months = ["January", "February", "March", "April", "May", "June",
"July", "August", "September", "October", "November", "December"];

// Non-Leap year Month days..
Calendar.DOMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
// Leap year Month days..
Calendar.lDOMonth = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

function Calendar(p_item, p_WinCal, p_month, p_year, p_format) {
	if ((p_month == null) && (p_year == null))	return;

	if (p_WinCal == null)
		this.gWinCal = ggWinCal;
	else
		this.gWinCal = p_WinCal;

	if (p_month == null) {
		this.gMonthName = null;
		this.gMonth = null;
		this.gYearly = true;
	} else {
		this.gMonthName = Calendar.get_month(p_month);
		this.gMonth = new Number(p_month);
		this.gYearly = false;
	}

	this.gYear = p_year;
	this.gFormat = p_format;
	this.gBGColor = "white";
	this.gFGColor = "black";
	this.gTextColor = "black";
	this.gHeaderColor = "black";
	this.gReturnItem = p_item;
}

Calendar.get_month = Calendar_get_month;
Calendar.get_daysofmonth = Calendar_get_daysofmonth;
Calendar.calc_month_year = Calendar_calc_month_year;
Calendar.print = Calendar_print;

function Calendar_get_month(monthNo) {
	return Calendar.Months[monthNo];
}

function Calendar_get_daysofmonth(monthNo, p_year) {
	/*
	Check for leap year ..
	1.Years evenly divisible by four are normally leap years, except for...
	2.Years also evenly divisible by 100 are not leap years, except for...
	3.Years also evenly divisible by 400 are leap years.
	*/
	if ((p_year % 4) == 0) {
		if ((p_year % 100) == 0 && (p_year % 400) != 0)
			return Calendar.DOMonth[monthNo];

		return Calendar.lDOMonth[monthNo];
	} else
		return Calendar.DOMonth[monthNo];
}

function Calendar_calc_month_year(p_Month, p_Year, incr) {
	/*
	Will return an 1-D array with 1st element being the calculated month
	and second being the calculated year
	after applying the month increment/decrement as specified by 'incr' parameter.
	'incr' will normally have 1/-1 to navigate thru the months.
	*/
	var ret_arr = new Array();

	if (incr == -1) {
		// B A C K W A R D
		if (p_Month == 0) {
			ret_arr[0] = 11;
			ret_arr[1] = parseInt(p_Year) - 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) - 1;
			ret_arr[1] = parseInt(p_Year);
		}
	} else if (incr == 1) {
		// F O R W A R D
		if (p_Month == 11) {
			ret_arr[0] = 0;
			ret_arr[1] = parseInt(p_Year) + 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) + 1;
			ret_arr[1] = parseInt(p_Year);
		}
	}

	return ret_arr;
}

function Calendar_print() {
	ggWinCal.print();
}

function Calendar_calc_month_year(p_Month, p_Year, incr) {
	/*
	Will return an 1-D array with 1st element being the calculated month
	and second being the calculated year
	after applying the month increment/decrement as specified by 'incr' parameter.
	'incr' will normally have 1/-1 to navigate thru the months.
	*/
	var ret_arr = new Array();

	if (incr == -1) {
		// B A C K W A R D
		if (p_Month == 0) {
			ret_arr[0] = 11;
			ret_arr[1] = parseInt(p_Year) - 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) - 1;
			ret_arr[1] = parseInt(p_Year);
		}
	} else if (incr == 1) {
		// F O R W A R D
		if (p_Month == 11) {
			ret_arr[0] = 0;
			ret_arr[1] = parseInt(p_Year) + 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) + 1;
			ret_arr[1] = parseInt(p_Year);
		}
	}

	return ret_arr;
}

// This is for compatibility with Navigator 3, we have to create and discard one object before the prototype object exists.
new Calendar();

Calendar.prototype.getMonthlyCalendarCode = function() {
	var vCode = "";
	var vHeader_Code = "";
	var vData_Code = "";

	// Begin Table Drawing code here..
	vCode = vCode + "<TABLE BORDER=1 BGCOLOR=\"" + this.gBGColor + "\">";

	vHeader_Code = this.cal_header();
	vData_Code = this.cal_data();
	vCode = vCode + vHeader_Code + vData_Code;

	vCode = vCode + "</TABLE>";

	return vCode;
}

Calendar.prototype.show = function() {
	var vCode = "";

	this.gWinCal.document.open();

	// Setup the page...
	this.wwrite("<html>");
	this.wwrite("<head><link rel='stylesheet' type='text/css' href='../nr/cw/newcss/world_ie.css'><title>Calendar</title>");

	this.wwrite("<SCRIPT language=\"JavaScript\">");
	this.wwrite("function setFormDate(inElementId,vDate)");
	this.wwrite("{");
	this.wwrite(" var ElementId=inElementId.substring(inElementId.indexOf(\".\")+1,40);");
	this.wwrite(" var HideElementId = ElementId+\"h\";");
	this.wwrite(" var FormName = inElementId.substring(0,6);");
	this.wwrite(" self.opener.document.all[HideElementId].value=vDate;");
	this.wwrite(" self.opener.document.all[ElementId].value=vDate;");
	this.wwrite(" self.opener.document.all[ElementId].value=vDate;");
	//this.wwrite("if (FormName == 'Charge')");
	//this.wwrite("{self.opener.document.all['From_Charge_Valid_Date'].value=vDate;");
	//this.wwrite("self.opener.document.all['From_Charge_Valid_Dateh'].value=vDate;}");
	this.wwrite("}");
	this.wwrite("</SCRIPT>");
	this.wwrite("</head>");

	this.wwrite("<body " +
		"link=\"" + this.gLinkColor + "\" " +
		"vlink=\"" + this.gLinkColor + "\" " +
		"alink=\"" + this.gLinkColor + "\" " +
		"text=\"" + this.gTextColor + "\">");
	this.wwriteA("<FONT FACE='" + fontface + "' SIZE=2><B>");
	this.wwriteA(this.gMonthName + " " + this.gYear);
	this.wwriteA("</B><BR>");

	// Show navigation buttons
	var prevMMYYYY = Calendar.calc_month_year(this.gMonth, this.gYear, -1);
	var prevMM = prevMMYYYY[0];
	var prevYYYY = prevMMYYYY[1];

	var nextMMYYYY = Calendar.calc_month_year(this.gMonth, this.gYear, 1);
	var nextMM = nextMMYYYY[0];
	var nextYYYY = nextMMYYYY[1];

	this.wwrite("<TABLE WIDTH='100%' BORDER=1 CELLSPACING=0 CELLPADDING=0 BGCOLOR='#e0e0e0'><TR><TD class=cal ALIGN=center class=blackbold>");
	//this.wwrite("[<A class=navchild HREF=\"" +
	//	"javascript:window.opener.Build(" +
	//	"'" + this.gReturnItem + "', '" + this.gMonth + "', '" + (parseInt(this.gYear)-1) + "', '" + this.gFormat + "'" +
	//	");" +
	//	"\"><<<\/A>]</TD><TD ALIGN=center>");
	this.wwrite("<A class=navchild HREF=\"" +
		"javascript:window.opener.Build(" +
		"'" + this.gReturnItem + "', '" + prevMM + "', '" + prevYYYY + "', '" + this.gFormat + "'" +
		");" +
		"\"><img border=0 src='../nr/cw/newimages/back.gif'><\/A></TD><TD class=cal ALIGN=center class=blackbold>");
	//this.wwrite("[<A class=navchild HREF=\"javascript:window.print();\">Print</A>]</TD><TD ALIGN=center>");
	this.wwrite("<A class=navchild HREF=\"" +
		"javascript:window.opener.Build(" +
		"'" + this.gReturnItem + "', '" + nextMM + "', '" + nextYYYY + "', '" + this.gFormat + "'" +
		");" +
		"\"><img border=0 src='../nr/cw/newimages/forward.gif'><\/A></TD></TR></TABLE><BR>");
	//this.wwrite("[<A class=navchild HREF=\"" +
		//"javascript:window.opener.Build(" +
		//"'" + this.gReturnItem + "', '" + this.gMonth + "', '" + (parseInt(this.gYear)+1) + "', '" + this.gFormat + "'" +
		//");" +
		//"\">>><\/A>]</TD></TR></TABLE><BR>");

	// Get the complete calendar code for the month..
	vCode = this.getMonthlyCalendarCode();
	this.wwrite(vCode);

	this.wwrite("</font></body></html>");
	this.gWinCal.document.close();
}

Calendar.prototype.showY = function() {
	var vCode = "";
	var i;
	var vr, vc, vx, vy;		// Row, Column, X-coord, Y-coord
	var vxf = 285;			// X-Factor
	var vyf = 200;			// Y-Factor
	var vxm = 10;			// X-margin
	var vym;				// Y-margin
	if (isIE)	vym = 75;
	else if (isNav)	vym = 25;

	this.gWinCal.document.open();

	this.wwrite("<html>");
	this.wwrite("<head><title>Calendar</title>");
	this.wwrite("<style type='text/css'>\n<!--");
	for (i=0; i<12; i++) {
		vc = i % 3;
		if (i>=0 && i<= 2)	vr = 0;
		if (i>=3 && i<= 5)	vr = 1;
		if (i>=6 && i<= 8)	vr = 2;
		if (i>=9 && i<= 11)	vr = 3;

		vx = parseInt(vxf * vc) + vxm;
		vy = parseInt(vyf * vr) + vym;

		this.wwrite(".lclass" + i + " {position:absolute;top:" + vy + ";left:" + vx + ";}");
	}
	this.wwrite("-->\n</style>");
	this.wwrite("</head>");

	this.wwrite("<body " +
		"link=\"" + this.gLinkColor + "\" " +
		"vlink=\"" + this.gLinkColor + "\" " +
		"alink=\"" + this.gLinkColor + "\" " +
		"text=\"" + this.gTextColor + "\">");
	this.wwrite("<FONT FACE='" + fontface + "' SIZE=2><B>");
	this.wwrite("Year : " + this.gYear);
	this.wwrite("</B><BR>");

	// Show navigation buttons
	var prevYYYY = parseInt(this.gYear) - 1;
	var nextYYYY = parseInt(this.gYear) + 1;

	this.wwrite("<TABLE WIDTH='100%' BORDER=1 CELLSPACING=0 CELLPADDING=0 BGCOLOR='#e0e0e0'><TR><TD class=cal ALIGN=center>");
	this.wwrite("[<A class=navchild HREF=\"" +
		"javascript:window.opener.Build(" +
		"'" + this.gReturnItem + "', null, '" + prevYYYY + "', '" + this.gFormat + "'" +
		");" +
		"\" alt='Prev Year'><<<\/A>]</TD><TD class=cal ALIGN=center>");
	this.wwrite("[<A class=navchild HREF=\"javascript:window.print();\">Print</A>]</TD><TD class=cal ALIGN=center>");
	this.wwrite("[<A class=navchild HREF=\"" +
		"javascript:window.opener.Build(" +
		"'" + this.gReturnItem + "', null, '" + nextYYYY + "', '" + this.gFormat + "'" +
		");" +
		"\">>><\/A>]</TD></TR></TABLE><BR>");

	// Get the complete calendar code for each month..
	var j;
	for (i=11; i>=0; i--) {
		if (isIE)
			this.wwrite("<DIV ID=\"layer" + i + "\" CLASS=\"lclass" + i + "\">");
		else if (isNav)
			this.wwrite("<LAYER ID=\"layer" + i + "\" CLASS=\"lclass" + i + "\">");

		this.gMonth = i;
		this.gMonthName = Calendar.get_month(this.gMonth);
		vCode = this.getMonthlyCalendarCode();
		this.wwrite(this.gMonthName + "/" + this.gYear + "<BR>");
		this.wwrite(vCode);

		if (isIE)
			this.wwrite("</DIV>");
		else if (isNav)
			this.wwrite("</LAYER>");
	}

	this.wwrite("</font><BR></body></html>");
	this.gWinCal.document.close();
}

Calendar.prototype.wwrite = function(wtext) {
	this.gWinCal.document.writeln(wtext);
}

Calendar.prototype.wwriteA = function(wtext) {
	this.gWinCal.document.write(wtext);
}

Calendar.prototype.cal_header = function() {
	var vCode = "";

	vCode = vCode + "<TR>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Sun</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Mon</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Tue</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Wed</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Thu</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='14%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Fri</B></FONT></TD>";
	vCode = vCode + "<TD class=cal WIDTH='16%'><FONT SIZE='2' FACE='" + fontface + "' COLOR='" + this.gHeaderColor + "'><B>Sat</B></FONT></TD>";
	vCode = vCode + "</TR>";

	return vCode;
}

Calendar.prototype.cal_data = function() {
	var vDate = new Date();
	vDate.setDate(1);
	vDate.setMonth(this.gMonth);
	vDate.setFullYear(this.gYear);

	var vFirstDay=vDate.getDay();
	var vDay=1;
	var vLastDay=Calendar.get_daysofmonth(this.gMonth, this.gYear);
	var vOnLastDay=0;
	var vCode = "";

	/*
	Get day for the 1st of the requested month/year..
	Place as many blank cells before the 1st day of the month as necessary.
	*/

	vCode = vCode + "<TR>";
	for (i=0; i<vFirstDay; i++) {
		vCode = vCode + "<TD class=cal WIDTH='14%'" + this.write_weekend_string(i) + "><FONT SIZE='2' FACE='" + fontface + "'> </FONT></TD>";
	}

	// Write rest of the 1st week
	for (j=vFirstDay; j<7; j++) {
		vCode = vCode + "<TD class=cal WIDTH='14%'" + this.write_weekend_string(j) + "><FONT SIZE='2' FACE='" + fontface + "'>" +
			"<A class=navchild HREF='#' " +
				//"onClick=\"self.opener.document." + this.gReturnItem + "h.value='" +
				//this.format_data(vDay) +
				//"';self.opener.document." + this.gReturnItem + ".value='" +
				//this.format_data(vDay) +
				//";window.close();\">" +
				"onClick=\"setFormDate('"+this.gReturnItem+"','"+this.format_data(vDay)+"');window.close();\">" +
				this.format_day(vDay) +
			"</A>" +
			"</FONT></TD>";
		vDay=vDay + 1;
	}
	vCode = vCode + "</TR>";

	// Write the rest of the weeks
	for (k=2; k<7; k++) {
		vCode = vCode + "<TR>";

		for (j=0; j<7; j++) {
			vCode = vCode + "<TD class=cal WIDTH='14%'" + this.write_weekend_string(j) + "><FONT SIZE='2' FACE='" + fontface + "'>" +
			"<A class=navchild HREF='#' " +
				//"onClick=\"self.opener.document." + this.gReturnItem + "h.value='" +
				//this.format_data(vDay) +
				//"';self.opener.document." + this.gReturnItem + ".value='" +
				//this.format_data(vDay) +
				//"';window.close();\">" +
				"onClick=\"setFormDate('"+this.gReturnItem+"','"+this.format_data(vDay)+"');window.close();\">" +
				this.format_day(vDay) +
				"</A>" +
				"</FONT></TD>";
			vDay=vDay + 1;

			if (vDay > vLastDay) {
				vOnLastDay = 1;
				break;
			}
		}

		if (j == 6)
			vCode = vCode + "</TR>";
		if (vOnLastDay == 1)
			break;
	}

	// Fill up the rest of last week with proper blanks, so that we get proper square blocks
	for (m=1; m<(7-j); m++) {
		if (this.gYearly)
			vCode = vCode + "<TD class=cal WIDTH='14%'" + this.write_weekend_string(j+m) +
			"><FONT SIZE='2' FACE='" + fontface + "' COLOR='gray'> </FONT></TD>";
		else
			vCode = vCode + "<TD class=cal WIDTH='14%'" + this.write_weekend_string(j+m) +
			"><FONT SIZE='2' FACE='" + fontface + "' COLOR='gray'>" + m + "</FONT></TD>";
	}

	return vCode;
}

Calendar.prototype.format_day = function(vday) {
	var vNowDay = gNow.getDate();
	var vNowMonth = gNow.getMonth();
	var vNowYear = gNow.getFullYear();

	if (vday == vNowDay && this.gMonth == vNowMonth && this.gYear == vNowYear)
		return ("<FONT COLOR=\"RED\"><B>" + vday + "</B></FONT>");
	else
		return (vday);
}
Calendar.prototype.write_weekend_string = function(vday) {
	var i;

	// Return special formatting for the weekend day.
	for (i=0; i<weekend.length; i++) {
		if (vday == weekend[i])
			return (" BGCOLOR=\"" + weekendColor + "\"");
	}

	return "";
}

Calendar.prototype.format_data = function(p_day) {
	var vData;
	var vMonth = 1 + this.gMonth;
	vMonth = (vMonth.toString().length < 2) ? "0" + vMonth : vMonth;
	var vMon = Calendar.get_month(this.gMonth).substr(0,3).toUpperCase();
	var vFMon = Calendar.get_month(this.gMonth).toUpperCase();
	var vY4 = new String(this.gYear);
	var vY2 = new String(this.gYear.substr(2,2));
	var vDD = (p_day.toString().length < 2) ? "0" + p_day : p_day;

	switch (this.gFormat) {
		case "MM\/DD\/YYYY" :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
			break;
		case "MM\/DD\/YY" :
			vData = vMonth + "\/" + vDD + "\/" + vY2;
			break;
		case "MM-DD-YYYY" :
			vData = vMonth + "-" + vDD + "-" + vY4;
			break;
		case "MM-DD-YY" :
			vData = vMonth + "-" + vDD + "-" + vY2;
			break;

		case "DD\/MON\/YYYY" :
			vData = vDD + "\/" + vMon + "\/" + vY4;
			break;
		case "DD\/MON\/YY" :
			vData = vDD + "\/" + vMon + "\/" + vY2;
			break;
		case "DD-MON-YYYY" :
			vData = vDD + "-" + vMon + "-" + vY4;
			break;
		case "DD-MON-YY" :
			vData = vDD + "-" + vMon + "-" + vY2;
			break;

		case "DD\/MONTH\/YYYY" :
			vData = vDD + "\/" + vFMon + "\/" + vY4;
			break;
		case "DD\/MONTH\/YY" :
			vData = vDD + "\/" + vFMon + "\/" + vY2;
			break;
		case "DD-MONTH-YYYY" :
			vData = vDD + "-" + vFMon + "-" + vY4;
			break;
		case "DD-MONTH-YY" :
			vData = vDD + "-" + vFMon + "-" + vY2;
			break;

		case "DD\/MM\/YYYY" :
			vData = vDD + "\/" + vMonth + "\/" + vY4;
			break;
		case "DD\/MM\/YY" :
			vData = vDD + "\/" + vMonth + "\/" + vY2;
			break;
		case "DD-MM-YYYY" :
			vData = vDD + "-" + vMonth + "-" + vY4;
			break;
		case "DD-MM-YY" :
			vData = vDD + "-" + vMonth + "-" + vY2;
			break;

		default :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
	}

	return vData;
}

function Build(p_item, p_month, p_year, p_format) {
	var p_WinCal = ggWinCal;
	gCal = new Calendar(p_item, p_WinCal, p_month, p_year, p_format);

	// Customize your Calendar here..
	gCal.gBGColor="white";
	gCal.gLinkColor="black";
	gCal.gTextColor="black";
	gCal.gHeaderColor="darkgreen";

	// Choose appropriate show function
	if (gCal.gYearly)	gCal.showY();
	else	gCal.show();
}

function show_calendar() {
	/*
		p_month : 0-11 for Jan-Dec; 12 for All Months.
		p_year	: 4-digit year
		p_format: Date format (mm/dd/yyyy, dd/mm/yy, ...)
		p_item	: Return Item.
	*/

	p_item = arguments[0];
	if (arguments[1] == null)
		p_month = new String(gNow.getMonth());
	else
		p_month = arguments[1];
	if (arguments[2] == "" || arguments[2] == null)
		p_year = new String(gNow.getFullYear().toString());
	else
		p_year = arguments[2];
	if (arguments[3] == null)
		p_format = "DD-MON-YYYY";
	else
		p_format = arguments[3];

	vWinCal = window.open("", "Calendar",
		"width=250,height=250,status=no,resizable=no,top=300,left=500");
	vWinCal.opener = self;
	ggWinCal = vWinCal;

	Build(p_item, p_month, p_year, p_format);
}
/*
Yearly Calendar Code Starts here
*/
function show_yearly_calendar(p_item, p_year, p_format) {
	// Load the defaults..
	if (p_year == null || p_year == "")
		p_year = new String(gNow.getFullYear().toString());
	if (p_format == null || p_format == "")
		p_format = "DD-MON-YYYY";

	var vWinCal = window.open("", "Calendar", "scrollbars=yes");
	vWinCal.opener = self;
	ggWinCal = vWinCal;

	Build(p_item, null, p_year, p_format);
}
/******************************************************/
function getRejectReason()
{
	window.open("RejectReason.htm", "RejectReason","width=500,height=200,top=300,left=300,dependent,alwaysraised,directories=no,location=no,menubar=no,resizable=no,toolbar=no");
}
/******************************************************/
function getAuthorisationDetails()
{
	window.open("Authorisation.htm", "RejectReason","width=500,height=200,top=300,left=300,dependent,alwaysraised,directories=no,location=no,menubar=no,resizable=no,toolbar=no");
}
/*******************************************************/
function sendReject()
{
   self.opener.document.all.RejectReason.value=document.all.RejectReason.value;
   self.opener.document.all.ButtonPressed.value="Reject";
   self.opener.document.all.ButtonPressed.form.submit();
   window.close();
}
/*******************************************************/
function sendAuth()
{

  if (document.all.AuthManager.value=="")
  {
    alert('Please enter an Authorising Manager');
  }
  else if (document.all.AuthDetails.value=="")
  {
    alert('Please enter Authorisation Details');
  }
  else
  {
    self.opener.document.all.AuthManager.value=document.all.AuthManager.value;
    self.opener.document.all.AuthDetails.value=document.all.AuthDetails.value;
    self.opener.document.all.ButtonPressed.value="Authorise";
    self.opener.document.all.ButtonPressed.form.submit();
    window.close();
  }
}
/*******************************************************/
function send(formname)
{
  document.formname.submit();
}
/*******************************************************/
function closeWindow()
{
  window.close();
}
/*******************************************************/
function showReturn()
{
	window.open("ReturnBan.htm", "","width=500,height=250,top=300,left=300,dependent,alwaysraised,directories=no,location=no,menubar=no,resizable=no,toolbar=no");
}
/*******************************************************/
function sendReturn()
{
  self.opener.document.all.RejectReason.value=document.all.RejectReason.value;
  self.opener.document.all.ButtonPressed.value="Return";
  self.opener.document.all.ButtonPressed.form.submit();
  window.close();
}
/*******************************************************/
var mymessage = "function disabled";

function rtclickcheck(keyp){
  if (navigator.appName == "Netscape" && keyp.which == 3) {
    alert(mymessage);
    return false;
  }

  if (navigator.appVersion.indexOf("MSIE") != -1 && event.button == 2) {
    alert(mymessage);
    return false;
  }
}
/*******************************************************/
//document.onmousedown = rtclickcheck

/**********************************************************************/
/* This function is used for onMouseOver operations on Option buttions*/
/**********************************************************************/

function highlightButton(Button)
{
	Button.className='menu_lit';
}
/********************************************************/
/* This function is used for Option buttions		*/
/********************************************************/
function submitForm(Button)
{
	document.all.ButtonPressed.value=Button.value;
	Button.form.submit();
}
function Back()
{
	document.back();
}
function Open_Menu(ElementId)
{
	if (document.layers && document.layers[ElementId])
		document.layers[ElementId].visibility = 'visible';
	else if (document.all && document.all[ElementId])
		document.all[ElementId].style.visibility = 'visible';
}

function Close_Menu(ElementId)
{
	if (document.layers && document.layers[ElementId])
		document.layers[ElementId].visibility = 'hidden';
	else if (document.all && document.all[ElementId])
		document.all[ElementId].style.visibility = 'hidden';
}
function Hide_Filters()
{
  document.all['Customer'].style.visibility = 'hidden';
  document.all['Account'].style.visibility = 'hidden';
  document.all['GSR'].style.visibility = 'hidden';
  document.all['Status'].style.visibility = 'hidden';
}
function Show_Filters()
{
  document.all['Customer'].style.visibility = 'visible';
  document.all['Account'].style.visibility = 'visible';
  document.all['GSR'].style.visibility = 'visible';
  document.all['Status'].style.visibility = 'visible';
  document.all['Type'].style.visibility = 'visible';
  document.all['Month'].style.visibility = 'visible';
}
function Show_Nostro_Filters()
{
  document.all['Customer'].style.visibility = 'visible';
  document.all['Account_List'].style.visibility = 'visible';
  document.all['Payment_Group'].style.visibility = 'visible';
  document.all['User'].style.visibility = 'visible';
  document.all['Status'].style.visibility = 'visible';
  document.all['Type'].style.visibility = 'visible';
  document.all['Month'].style.visibility = 'visible';
}
function Show_OSS_Filters()
{
  document.all['Customer'].style.visibility = 'visible';
  document.all['Account_List'].style.visibility = 'visible';
  document.all['GSR'].style.visibility = 'visible';
  document.all['Type'].style.visibility = 'visible';
  document.all['Month'].style.visibility = 'visible';
}
function Show_AdHoc_Filters()
{
  document.all['Customer'].style.visibility = 'visible';
  document.all['Account_List'].style.visibility = 'visible';
  document.all['Invoice'].style.visibility = 'visible';
  document.all['Status'].style.visibility = 'visible';
  document.all['Type'].style.visibility = 'visible';
  document.all['Invoice_Month'].style.visibility = 'visible';
  document.all['Visit_Month'].style.visibility = 'visible';
}
function Open_Form(ElementId,Button,NewLabel)
{
	var HideElementId = ElementId+"Hide";
	if (document.layers && document.layers[ElementId])
		document.layers[ElementId].visibility = 'visible';
	else if (document.all && document.all[ElementId])
		{
		   document.all[ElementId].style.visibility = 'visible';
		   document.all[HideElementId].style.visibility = 'hidden';
		}
	Button.value=NewLabel;
}
function Close_Form(ElementId)
{
	var HideElementId = ElementId+"Hide";
	if (document.layers && document.layers[ElementId])
		document.layers[ElementId].visibility = 'hidden';
	else if (document.all && document.all[ElementId])
	        {
		  document.all[ElementId].style.visibility = 'hidden';
		  document.all[HideElementId].style.visibility = 'visible';
	        }
}
function Close_Parent_Form(ElementId)
{
	var HideElementId = ElementId+"Hide";
	if (parent.document.all && parent.document.all[ElementId])
	{
	  parent.document.all[ElementId].style.visibility = 'hidden';
	  parent.document.all[HideElementId].style.visibility = 'visible';
	}
}
function Toggle_Menu(ElementId)
{
	if (document.all[ElementId].style.visibility='hidden')
	{
	  document.all[ElementId].style.visibility = 'visible';
	}
	else
	{
	  document.all[ElementId].style.visibility = 'hidden';
	}
}

function Toggle_Visibility(ElementId,Button,NewLabel)
{
	var HideElementId = ElementId+"Hide";
	if (document.all[ElementId].style.visibility='hidden')
	{
	  document.all[ElementId].style.visibility = 'visible';
	  document.all[HideElementId].style.visibility = 'hidden';
	}
	else
	{
	  document.all[ElementId].style.visibility = 'hidden';
	  document.all[HideElementId].style.visibility = 'visible';
	}
	Button.value=NewLabel;
}
function sendOrderBy(OrderBy)
{
  this.form.BAN_Id.value=" ";
  this.form.OrderBy.value=OrderBy;
  this.form.submit();
}
function openSite()
{
  document.all[SiteFrame].className="siteopen";
}
function Calculate_VAT(Amount,VAT_Indicator,Tax_Rate)
{
  if (document.all.VAT_Indicator.value=="Y")
  {
    document.all.VAT_Amount.value=document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value);
    document.all.Total_Amount.value=(document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value))+parseFloat(Amount);
    document.all.VAT_Amounth.value=document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value);
    document.all.Total_Amounth.value=(document.all.Gross_Amount.value/100*parseFloat(document.all.Local_Tax_Rateh.value))+parseFloat(Amount);
  }
  else
  {
    document.all.VAT_Amount.value=0.0;
    document.all.Total_Amount.value=document.all.Gross_Amount.value;
    document.all.VAT_Amounth.value=0.0;
    document.all.Total_Amounth.value=document.all.Gross_Amount.value;
  }
}
function setVAT()
{
    document.all.VAT_Amounth.value=document.all.VAT_Amount.value;
    document.all.Total_Amount.value=document.all.VAT_Amount.value;
    document.all.Total_Amounth.value=document.all.VAT_Amount.value;
}
function setSiteRefs(CircuitRef)
{
  document.all.FromEnd_Site_Reference.value=CircuitRef+'-A';
  document.all.ToEnd_Site_Reference.value=CircuitRef+'-B';
}

function DaysInMonth(WhichMonth, WhichYear)
{
  var DaysInMonth = 31;
  if (WhichMonth != "" && WhichYear != "")
  {
    if (WhichMonth == "Apr" || WhichMonth == "Jun" || WhichMonth == "Sep" || WhichMonth == "Nov") DaysInMonth = 30;
    if (WhichMonth == "Feb" && (WhichYear/4) != Math.floor(WhichYear/4))	DaysInMonth = 28;
    if (WhichMonth == "Feb" && (WhichYear/4) == Math.floor(WhichYear/4))	DaysInMonth = 29;
  }
  return DaysInMonth;
}

//function to change the available days in a months
function ChangeOptionDays(Form, Which, withBlank,monthChanged)
{
  DaysObject = eval(Form + "." + Which + "Day");
  MonthObject = eval(Form + "." + Which + "Month");
  YearObject = eval(Form + "." + Which + "Year");
  DateObject = eval(Form + "." + Which + "h");

  var Month;
  var Year;
  if (withBlank)
  {
    if (monthChanged)
    {
      if (MonthObject.selectedIndex == 0)
      {
        YearObject.selectedIndex = 0;
      }
      else
      {
        if (YearObject.selectedIndex == 0)
        {
          YearObject.selectedIndex = 11;
        }
      }
    }
    else
    {
      if (YearObject.selectedIndex == 0)
      {
        MonthObject.selectedIndex = 0;
      }
      else
      {
        if (MonthObject.selectedIndex == 0)
        {
          MonthObject.selectedIndex = 1;
        }
      }
    }
  }

  Month = MonthObject[MonthObject.selectedIndex].text;
  Year = YearObject[YearObject.selectedIndex].text;

  DaysForThisSelection = DaysInMonth(Month, Year);
  CurrentDaysInSelection = DaysObject.length;
  if (withBlank)
    CurrentDaysInSelection--;
  if (CurrentDaysInSelection > DaysForThisSelection)
  {
    for (i=0; i<(CurrentDaysInSelection-DaysForThisSelection); i++)
    {
      DaysObject.options[DaysObject.options.length - 1] = null
    }
  }
  if (DaysForThisSelection > CurrentDaysInSelection)
  {
    for (i=0; i<(DaysForThisSelection-CurrentDaysInSelection); i++)
    {
      optionValue = withBlank?DaysObject.options.length:(DaysObject.options.length + 1);
      NewOption = new Option(optionValue);
      DaysObject.add(NewOption);
    }
  }

  if (DaysObject.selectedIndex < 0) DaysObject.selectedIndex = 0;

  if (((Month == "") && (monthChanged)) || ((Year == "") && (!monthChanged)))
  {
    DateObject.value = "";
    DaysObject.selectedIndex = 0;
  }
  else
  {
    if ((DaysObject.selectedIndex == 0) && (withBlank))
      DaysObject.selectedIndex = 1;
    if ((Form =="FixedChargeBAN") && (Which == "To_Charge_Valid_Date"))
    {
      DaysObject.selectedIndex = DaysForThisSelection;
    }
    if ((MonthObject.selectedIndex == 0) && (withBlank))
      MonthObject.selectedIndex = 1;
    if ((YearObject.selectedIndex == 0) && (withBlank))
      YearObject.selectedIndex = 11;

    DateObject.value = DaysObject[DaysObject.selectedIndex].text + "-" +
    Month + "-" + Year;
  }
}

function ChangeHiddenDate(Form, Which, withBlank)
{
  DaysObject = eval(Form + "." + Which + "Day");
  MonthObject = eval(Form + "." + Which + "Month");
  YearObject = eval(Form + "." + Which + "Year");
  DateObject = eval(Form + "." + Which + "h");

  if (DaysObject[DaysObject.selectedIndex].text == "")
  {
    DateObject.value = "";
    MonthObject.selectedIndex = 0;
    YearObject.selectedIndex = 0;
  }
  else
  {
    if ((MonthObject.selectedIndex == 0) && (withBlank))
      MonthObject.selectedIndex = 1;
    if ((YearObject.selectedIndex == 0) && (withBlank))
      YearObject.selectedIndex = 11;
  }
  Month = MonthObject[MonthObject.selectedIndex].text;
  Year = YearObject[YearObject.selectedIndex].text;
  DateObject.value = DaysObject[DaysObject.selectedIndex].text + "-" +
  Month + "-" + Year;
}

function createUser(type)
{
  window.open('CreateUser.jsp?type='+type,'createUser','toolbar=no,menubar=no,location=no,directories=no,status=no,scrollbars=auto,resizable=yes,height=255,width=360,top=250,left=400');

}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
//alert("in there!");
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function confirmException()
{
  if (confirm("Click 'OK' to confirm you wish to save as an Exception.  Otherwise, click 'Cancel'."))
  {
    document.forms[0].ButtonPressed.value='Save Exception';
    document.forms[0].submit();
  }
}

function confirmClear()
{
  if (confirm("Click 'OK' to confirm you wish to clear the screen.  Otherwise, click 'Cancel'."))
  {
    document.forms[0].ButtonPressed.value='Clear';
    document.forms[0].submit();
  }
}

function confirmClearOSS()
{
  if (confirm("Click 'OK' to confirm you wish to clear the screen.  Otherwise, click 'Cancel'."))
  {
    document.forms[0].ButtonPressed.value='Clear';
    document.forms[0].submit();
  }
}

function menuOver(thistag)
{
   obj = document.getElementById(thistag);
   obj.className = 'dynamic_menu_lit'
}

function menuOut(thistag)
{
   obj = document.getElementById(thistag);
   obj.className = 'dynamic_menu'
}

function linkClick(listType, formNo)
{
  //document.forms[formNo].ButtonPressed.value='here';
  //document.forms[formNo].listType.value=listType;
  //document.forms[formNo].submit();
  document.forms[0].ButtonPressed.value='here';
  document.forms[0].listType.value=listType;
  document.forms[0].submit();
}

function trimAll( strValue )
{
/************************************************
DESCRIPTION: Removes leading and trailing spaces.

PARAMETERS: Source string from which spaces will
  be removed;

RETURNS: Source string with whitespaces removed.
*************************************************/
 var objRegExp = /^(\s*)$/;

    //check for all spaces
    if(objRegExp.test(strValue))
    {
       strValue = strValue.replace(objRegExp, '');
       if( strValue.length == 0)
          return strValue;
    }

   //check for leading & trailing spaces
   objRegExp = /^(\s*)([\W\w]*)(\b\s*$)/;
   if(objRegExp.test(strValue))
   {
       //remove leading and trailing whitespace characters
       strValue = strValue.replace(objRegExp, '$2');
    }
  return strValue;
}

function  validateNumeric( strValue )
{
/*****************************************************************
DESCRIPTION: Validates that a string contains only valid numbers.

PARAMETERS:
   strValue - String to be tested for validity

RETURNS:
   True if valid, otherwise false.
******************************************************************/
  var objRegExp  =  /(^-?\d\d*\.\d*$)|(^-?\d\d*$)|(^-?\.\d\d*$)/;

  //check for numeric characters
  return objRegExp.test(strValue);
}












