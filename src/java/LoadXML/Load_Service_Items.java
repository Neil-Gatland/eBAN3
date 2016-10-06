package LoadXML;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import JavaUtil.StringUtil;
//import java.util.Random;
import java.sql.Connection;

public class Load_Service_Items extends Object
{
  private String Message="";

  public boolean LoadXML(String XMLFile,Connection Conn,String Run_Id)
  {
    //Create the XML document
    Document LoadDataDoc = null;
    String TagName,TagValue;
    NodeList Attributes;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    LoadXML.Tag_Handler TH = new LoadXML.Tag_Handler();
    int i,j,k,l;

    try
      {//Instantiate the parser and parse the file
	DocumentBuilderFactory docbuilderfactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docbuilder = docbuilderfactory.newDocumentBuilder();
	LoadDataDoc = docbuilder.parse(XMLFile);
      }
      catch (Exception e)
      {
	System.out.println("Load failed with error: "+e.getMessage());
	Message=e.getMessage();
	return false;
      }
      TH.setValue("Feed","Service Item");
      TH.setRun_Id(Run_Id);
      TH.setFileName(XMLFile);
      //Get the root element and all order elements

      /*In order to retrieve information from the Document, you need to retrieve the root element,
      which contains all of the data. Once you have this element, the application can retrieve data
      by element name using getElementsByTagName(). This method returns a NodeList,
      which can then be used to access each element.
      Because each ServiceItem contains product elements, repeat the process to create a second
      loop for each ServiceItem.

      Retrieving the actual data involves a combination of getElementsByTagName() and
      access of the children of nodes.
      */
    Element LoadData = LoadDataDoc.getDocumentElement();
    //get a list of all SERVICEITEM nodes
    NodeList ServiceItems = LoadData.getElementsByTagName("SERVICEITEM");
    //loop thru the nodes
    for (i = 0; i < ServiceItems.getLength(); i++)
    {//Get the child attributes of the Service Item
       	Element thisServiceItem = (Element)ServiceItems.item(i);
	Attributes = thisServiceItem.getChildNodes();
        TH.setValue("Table","Service_Item");
	for (j=1; j < Attributes.getLength(); j=j+2)
	{
	  Element thisAttribute = (Element)Attributes.item(j);
	  TagName=thisAttribute.getTagName();
	  if (thisServiceItem.getElementsByTagName(TagName).item(0).getFirstChild() != null)
	  {
	    TagValue=SU.isNull(thisServiceItem.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue(),"");
	  }
	  else
	  {
	    TagValue="";
	  }
	  TH.setValue(TagName,TagValue);
	  //System.out.println(TagName + ": "+ TagValue);
	}
	//Now write out the Service Item
	if (Attributes.getLength()>1)
	{
	  TH.write_Table(Conn);
	}
      //Now get a list of nodes for the service item and Loop through each Attribute for the ServiceItem
      NodeList ServiceItemAttributes = thisServiceItem.getElementsByTagName("SERVICEITEMATTRIBUTE");
      for (k=0; k < ServiceItemAttributes.getLength(); k++)
      {//get a list of the child attributes
	//Retrieve each Attribute
	Element thisServiceItemAttribute = (Element)ServiceItemAttributes.item(k);
	//Get ServiceItemAttribute information from attributes and child
	//elements

	Attributes = thisServiceItemAttribute.getChildNodes();
	for (l=1; l < Attributes.getLength(); l=l+2)
	{
          TH.setValue("Table","SERVICEITEMATTRIBUTE");
	  Element thisAttribute = (Element)Attributes.item(l);
	  TagName=thisAttribute.getTagName();
  	  if (thisServiceItemAttribute.getElementsByTagName(TagName).item(0).getFirstChild() != null)
	  {
	    TagValue=thisServiceItemAttribute.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue();
	  }
	  else
	  {
	    TagValue="";
	  }
	  //System.out.println(TagName + ": "+ TagValue);
	  TH.setValue(TagName,TagValue);
	  //Every name/value pair is a record, so write it out
	  TH.write_Table(Conn);
	}
      }
    }
    return true;
  }
  public String getMessage()
  {
    return Message;
  }
}