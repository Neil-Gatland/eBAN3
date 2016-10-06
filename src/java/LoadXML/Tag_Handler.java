package LoadXML;

import JavaUtil.StringUtil;
import EntityBeans.*;
import java.sql.Connection;

public class Tag_Handler
{
  String Table="",ModifiedBy="",ModifiedOn="",Record_Id="",Feed="",FileName,Run_Id="",AuditTrailId="";
  String ModifiedType="",VersionNumber="",Message="";
  SiteBean Site = new EntityBeans.SiteBean();
  ServiceItemBean ServiceItem = new EntityBeans.ServiceItemBean();
  ServiceItemAttributeBean ServiceAttribute = new EntityBeans.ServiceItemAttributeBean();
  CustomerBean Customer = new EntityBeans.CustomerBean();
  private EntityBean Entity = new EntityBeans.EntityBean();

  public Tag_Handler()
  {
  }
  public void setValue (String name,String value)
  {//Not a tag, just an application control
    if (name.compareToIgnoreCase("Table")==0)
    {
      setTable(value);
    }
    else if (name.compareTo("Feed") == 0)
    {//Not a tag, just an application control
      Feed=value;
    }//All the rest are tags

    //Site Tags
    else if (Table.compareToIgnoreCase("SITE")==0)
    {
      Site.setValuefromTag(name,value);
    }
    else if (Table.compareToIgnoreCase("Profile")==0)
    {
      Site.setValuefromTag(name,value);
    }

    //Service Item Tags
    else if ((Table.compareToIgnoreCase("SERVICEITEM")==0) ||
	    (Table.compareToIgnoreCase("Service_Item")==0))
    {
      ServiceItem.setValuefromTag(name,value);
    }
    else if (Table.compareToIgnoreCase("SERVICEITEMATTRIBUTE")==0)
    {
      ServiceAttribute.setValuefromTag(name,value);
    }
    else if (Table.compareToIgnoreCase("Site_Address")==0)
    {
      ServiceAttribute.setValuefromTag(name,value);
    }
    else if (Table.compareToIgnoreCase("ORGANISATION")==0)
    {
      Customer.setValuefromTag(name,value);
    }
    //Generic Tags
    else if (name.compareToIgnoreCase("AuditTrailId")==0)
    {
      AuditTrailId=value;
    }
    else if (name.compareToIgnoreCase("RecordId")==0)
    {
      Record_Id=value;
    }
    else if (name.compareToIgnoreCase("VersionNumber")==0)
    {
      VersionNumber=value;
    }
    else if (name.compareToIgnoreCase("AuditTrailId")==0)
    {
      AuditTrailId=value;
    }
    else if (name.compareToIgnoreCase("ModifiedType")==0)
    {
      ModifiedType=value;
    }
    else if (name.compareToIgnoreCase("ModifiedBy")==0)
    {
      Entity.setModifiedBy(value);
      ModifiedBy=value;
    }
    else if (name.compareToIgnoreCase("ModifiedOn")==0)
    {
      Entity.setModifiedOn(value);
      ModifiedOn=value;
    }
  }
public void setFileName(String value)
{
  FileName=value;
}
public void setRun_Id(String value)
{
  Run_Id=value;
}
public void setTable(String value)
{
  Table=value;
  if (Table.compareToIgnoreCase("Site")==0)
  {
    Site.setFileName(FileName);
    Site.setRun_Id(Run_Id);
  }
    else if ((Table.compareToIgnoreCase("SERVICEITEM")==0) ||
	    (Table.compareToIgnoreCase("Service_Item")==0))
  {
    ServiceItem.setFileName(FileName);
    ServiceItem.setRun_Id(Run_Id);
    ServiceItem.setBarclays_Site_Id(Site.getReference());
    ServiceItem.setSite_Name(Site.getName());
  }
  else if (Table.compareToIgnoreCase("SERVICEITEMATTRIBUTE")==0)
  {
    ServiceAttribute.setFileName(FileName);
    ServiceAttribute.setRun_Id(Run_Id);
    ServiceAttribute.setProduct_Id(ServiceItem.getProduct_Id());
  }
}
/******************************************************************************/
  public void write_Table(Connection Conn)
  {
    if (Table.compareToIgnoreCase("SITE")==0)
    {
      Site.setRecord_Id(Record_Id);
      Site.setModifiedBy(ModifiedBy);
      Site.setModifiedOn(ModifiedOn);
      Site.create(Conn);
    }
    else if ((Table.compareToIgnoreCase("SERVICEITEM")==0) ||
	    (Table.compareToIgnoreCase("Service_Item")==0))
    {
      if (Feed.compareTo("Audit")==0)
      {
        ServiceItem.setCMDB_Service_Item_Id(Record_Id);
        ServiceItem.setModifiedBy(ModifiedBy);
        ServiceItem.setModifiedOn(ModifiedOn);
      }
      else if (Feed.compareTo("Service Item")==0)
      {//Set product id from parent service - needed for product attribute lookups
	ServiceAttribute.setProduct_Id(ServiceItem.getProduct_Id());
      }
      ServiceItem.create(Conn);
    }
    else if (Table.compareToIgnoreCase("SERVICEITEMATTRIBUTE")==0)
    {
      if (Feed.compareTo("Audit")==0)
      {
        ServiceAttribute.setCMDB_Service_Item_Attribute_Id(Record_Id);;
        ServiceAttribute.setModifiedBy(ModifiedBy);
        ServiceAttribute.setModifiedOn(ModifiedOn);
      }
      else if (Feed.compareTo("Service Item")==0)
      {
        ServiceAttribute.setCMDB_Service_Item_Id(ServiceItem.getCMDB_Service_Item_Id());
	//ServiceAttribute.setCMDB_Service_Item_Attribute_Id(Record_Id);
	ServiceAttribute.setModifiedBy(ServiceItem.getModifiedBy());
	ServiceAttribute.setModifiedOn(ServiceItem.getModifiedOn());
      }
      ServiceAttribute.create(Conn);
    }
    else if (Table.compareToIgnoreCase("ORGANISATION")==0)
    {
      Customer.setRecordId(Record_Id);
      Customer.setRun_Id(Run_Id);
      Customer.setAuditTrailId(AuditTrailId);
      Customer.setVersionNumber(VersionNumber);
      Customer.setModifiedType(ModifiedType);
      Customer.setModifiedBy(ModifiedBy);
      Customer.setModifiedOn(ModifiedOn);
      Customer.create();
    }

    else
    {
      Message="Unknown data type:- "+Table;
    }
    Table="";
  }
}