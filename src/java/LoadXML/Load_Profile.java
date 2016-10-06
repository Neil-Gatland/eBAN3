package LoadXML;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import JavaUtil.StringUtil;
import java.sql.Connection;

public class Load_Profile extends Object
{
  protected void LoadXML(String XMLFile,Connection Conn,String Run_Id)
  {
    //Create the XML document

    Document LoadDataDoc = null;
    String TagName,TagValue;
    NodeList Attributes;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    LoadXML.Tag_Handler TH = new LoadXML.Tag_Handler();
    int ProfileIndex,AttributeIndex;

    try
      {//Instantiate the parser and parse the file
	DocumentBuilderFactory docbuilderfactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docbuilder = docbuilderfactory.newDocumentBuilder();
	LoadDataDoc = docbuilder.parse(XMLFile);
      }
      catch (Exception e)
      {
	System.out.println("Cannot read the XML file: "+e.getMessage());
	return;
      }
      //Get the root element and all order elements

    /*Retrieve the root element, which contains all of the data.
    Once you have this element, the application can retrieve data
    by element name using getElementsByTagName(). This method returns a NodeList,
    which can then be used to access each element.
    Because each ServiceItem contains product elements, repeat the process to create a second
    loop for each ServiceItem.
    Next, retrieve the actual data.

    Retrieving the data
    Retrieving the actual data involves a combination of getElementsByTagName() and
    access of the children of nodes.
    ...
    */
    Element LoadData = LoadDataDoc.getDocumentElement();
    TH.setValue("Table","Site");//Only one table in this feed
    TH.setRun_Id(Run_Id);
    TH.setFileName(XMLFile);

    //get a list of all Profile nodes
    NodeList Profiles = LoadData.getElementsByTagName("Profile");
    //loop thru the Profile nodes, each of which represents a site
    for (ProfileIndex = 0; ProfileIndex < Profiles.getLength(); ProfileIndex++)
    {
      //Retrieve each Attribute
      Element thisProfile = (Element)Profiles.item(ProfileIndex);
      //Get  Profile Attribute information from attributes and child
      //elements

      Attributes = thisProfile.getChildNodes();
      for (AttributeIndex=1; AttributeIndex < Attributes.getLength(); AttributeIndex=AttributeIndex+2)
      {
	Element thisAttribute = (Element)Attributes.item(AttributeIndex);
	TagName=thisAttribute.getTagName();
	if (thisProfile.getElementsByTagName(TagName).item(0).getFirstChild() != null)
	{
	  TagValue=thisProfile.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue();
	}
	else
	{
	  TagValue="";
	}
	//System.out.println(TagName + ": "+ TagValue);
	TH.setValue(TagName,TagValue);
      }
    }
  }
  /*
  public static void main (String args[])
  {
    int i;

    Load_Profile LP=new Load_Profile();

    for(i=0;i<1;i++)
    {
      LP.LoadXML(args[i]);
    }
  }*/
  /*********for debugging*********/
  public static void main (String[] args)
  {
    int i;

    Load_Profile LP=new Load_Profile();

    for(i=0;i<1;i++)
    {
      //LP.LoadXML("c:\\Barclays\\XML\\Profile.xml");
    }
  }
}