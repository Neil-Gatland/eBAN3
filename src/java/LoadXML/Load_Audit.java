/*Creation of an XML Document object using the Java API for XML
Processing (JAXP). First, create the DocumentBuilderFactory, then use it to create the
DocumentBuilder. This DocumentBuilder is the actual parser, which takes the
xml file and reads each element to create a Document object.
Once you create the Document, loop through the data.

Before you can build an application that reads the XML file based on a mapping file, you first
need to read the XML without a mapping file.
In this example, the order element contains each order, and a product element contains
each item in the order. To access this data, a loop is constructed:
*/
package LoadXML;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import JavaUtil.StringUtil;
import java.sql.Connection;

public class Load_Audit extends Object
{
  private String Message;

  public boolean LoadXML(String XMLFile,Connection Conn,String Run_Id)
  {
    //Create the XML document
    Document LoadDataDoc = null;
    String TagName,TagValue;
    NodeList AuditAttributes;
    JavaUtil.StringUtil SU = new JavaUtil.StringUtil();
    LoadXML.Tag_Handler TH = new LoadXML.Tag_Handler();

    int AuditTagIndex,AuditAttributeIndex,AuditRecordIndex,TableIndex,DataItemIndex;

    try
      {//Instantiate the parser and parse the file
	DocumentBuilderFactory docbuilderfactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docbuilder = docbuilderfactory.newDocumentBuilder();
	LoadDataDoc = docbuilder.parse(XMLFile);
      }
      catch (Exception e)
      {
	System.out.println("Cannot read the XML file: "+e.getMessage());
	Message=e.getMessage();
	return false;
      }
      TH.setValue("Feed","Audit");
      TH.setRun_Id(Run_Id);
      TH.setFileName(XMLFile);

      //Get the root element and all order elements

      /* Retrieve a list of the root elements <Audit>.
      Then retrieve data by element name using getElementsByTagName().
      This method returns a NodeList, which can then be used to access each element.
      Because each AuditItem contains product elements, repeat the process to create a second
      loop for each ServiceItem.
      */

    Element LoadData = LoadDataDoc.getDocumentElement();
    //get a list of all Audit nodes
    NodeList Audit = LoadData.getElementsByTagName("Audit");
    //loop thru the all the Audit elements
    for (AuditTagIndex = 0; AuditTagIndex < Audit.getLength(); AuditTagIndex++)
    {//Get the child attributes of the Audit Item

      Element thisAuditItem = (Element)Audit.item(AuditTagIndex);
      AuditAttributes = thisAuditItem.getChildNodes();
      for ( AuditAttributeIndex=1;
	    AuditAttributeIndex < AuditAttributes.getLength();
	    AuditAttributeIndex=AuditAttributeIndex+2)
      {
	Element thisAttribute = (Element)AuditAttributes.item(AuditAttributeIndex);
	TagName=thisAttribute.getTagName();
	if (thisAuditItem.getElementsByTagName(TagName).item(0).getFirstChild() != null)
	{
	  TagValue=SU.isNull(thisAuditItem.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue(),"");
	}
	else
	{
	  TagValue="";
	}
	//Debug
	//System.out.println(TagName + ": "+ TagValue);
	//Send to Tag Handler
	TH.setValue(TagName,TagValue);
      }
      //At this point we should now be pointing at a <Data> tag
      //Now get a list of Data Type tags for this Audit item
      NodeList AuditRecords = thisAuditItem.getElementsByTagName("Data");
      for (AuditRecordIndex=0; AuditRecordIndex < AuditRecords.getLength(); AuditRecordIndex++)
      {
        //For Each <Data> tag, get a list of child nodes, each of which represents
        // a single database table update
        Element thisAuditRecord = (Element)AuditRecords.item(AuditRecordIndex);
	NodeList TableUpdates = thisAuditRecord.getChildNodes();

	for ( TableIndex=1;
	      TableIndex < TableUpdates.getLength();
	      TableIndex=TableIndex+2)
	{

	  Element thisTable = (Element)TableUpdates.item(TableIndex);

	  //This tag should be the name of the table
	  TagValue=thisTable.getTagName();
	  //System.out.println(TagValue);
	  TH.setValue("Table",TagValue);

	  //send table name to handler

	  //For Each Table name  tag, get a list of child nodes, which should comprise a
	  //pair of <NEW> <OLD> tags
	  //Element thisOldNew = (Element)AuditRecords.item(AuditRecordIndex);
	  NodeList OldNew = thisTable.getChildNodes();

	  // Position at the <New> tag
	  Element NewTag=(Element)OldNew.item(1);

	  //For the current tag, get a list of the Data Items within the <New> tag
	  NodeList DataItems  = NewTag.getChildNodes();

	  for ( DataItemIndex=1;
		DataItemIndex < DataItems.getLength(); DataItemIndex=DataItemIndex+2)
	  {
	    Element thisDataItem = (Element)DataItems.item(DataItemIndex);
	    TagName=thisDataItem.getTagName();
	    if (NewTag.getElementsByTagName(TagName).item(0).getFirstChild() != null)
	    {
	      TagValue=NewTag.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue();
	    }
	    else
	    {
	      TagValue="";
	    }
	    //System.out.println(TagName + ": "+ TagValue);
	    TH.setValue(TagName,TagValue);
	  }
	  if (DataItems.getLength()>1)
	  {
	    TH.write_Table(Conn);
	  }
	  else
	  {
	    TH.setValue("Table","");
	  }
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