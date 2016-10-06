var menuskin = "skin1"; // skin0, or skin1
var display_url = 0; // Show URLs in status bar?
var ElementId;

function showmenuie5() 
{
	if (event.srcElement.getAttribute("url") == null)
	{
		return false;
	}

	ElementId=event.srcElement.url;

	var rightedge = document.body.clientWidth-event.clientX;
	var bottomedge = document.body.clientHeight-event.clientY;
	if (rightedge < ie5menu.offsetWidth)
		ie5menu.style.left = document.body.scrollLeft + event.clientX - ie5menu.offsetWidth;
	else
		ie5menu.style.left = document.body.scrollLeft + event.clientX;
	if (bottomedge < ie5menu.offsetHeight)
		ie5menu.style.top = document.body.scrollTop + event.clientY - ie5menu.offsetHeight;
	else
		ie5menu.style.top = document.body.scrollTop + event.clientY;
	ie5menu.style.visibility = "visible";
	return false;
}
function hidemenuie5() 
{
	ie5menu.style.visibility = "hidden";
}
function highlightie5() 
{
   if (event.srcElement.className == "menuitems") 
   {
     event.srcElement.style.backgroundColor = "highlight";
     event.srcElement.style.color = "white";
     if (display_url)
       window.status = event.srcElement.url;
   }
}
function lowlightie5() 
{
   if (event.srcElement.className == "menuitems") 
   {
      event.srcElement.style.backgroundColor = "";
      event.srcElement.style.color = "black";
      window.status = "";
   }
}
function jumptoie5() 
{
   if (event.srcElement.className == "menuitems")
   {
     if (event.srcElement.getAttribute("url") != null)
     //window.open(event.srcElement.url, event.srcElement.getAttribute("url"));
     {
	document.all.OrderBy.value=ElementId+event.srcElement.getAttribute("url");
	document.all.OrderBy.form.submit();
     }
else
     {
	window.location = event.srcElement.url;

     }
 }
}
//  End -->