package JavaUtil;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * A "lightweight" descriptor class for data from the PDF_Email_Request and Notification_Email_Request tables.
 *
 * <P>Copyright (c) 2007 Danet Ltd</P>
 * @author Tim Aitchison
 * @version 1.0
 */

public class EmailRequestDescriptor
      implements Serializable
{
  /** Constant for column Notification_Type value "PDF" */
  public static final String NOTIFICATION_TYPE_P = "PDF";

  /** Constant for column Notification_Type value "Monthly CDRs" */
  public static final String NOTIFICATION_TYPE_M = "Monthly CDRs";

  /** Constant for column Notification_Type value "Daily CDRs" */
  public static final String NOTIFICATION_TYPE_D = "Daily CDRs";

  /** All values for column Notification_Type */
  public static final String[] NOTIFICATION_TYPE__VALUES = new String[]
  {
    NOTIFICATION_TYPE_P,
    NOTIFICATION_TYPE_M,
    NOTIFICATION_TYPE_D
  };

  /** Column Login_Id */
  private long loginId = 0;

  /** Column Invoice_Id */
  private long invoiceId = 0;

  /** Column Attachment_Name */
  private String attachmentName = null;

  /** Column Notification_Type (Notification_Email_Request only) */
  private String notificationType = null;

  /** Column Last_Maintained_Date */
  private DateTime lastMaintainedDate = null;

  /** Column Last_Maintained_By */
  private String lastMaintainedBy = null;

  /** Column Email_Address (from Login) */
  private String emailAddress = null;

  /** Column Location (from Attachment) */
  private String attachmentLocation = null;

  private ArrayList attachmentLocationArray = new ArrayList();

  /** Column Invoice_No (from Invoice) */
  private String invoiceNo = null;

  /** Column Account_Number/Account_Name (from Account) */
  private String accountDetails = null;

  /** Column Account_Id (from Account)*/
  private long accountId = 0;

  /** Column Payment_Group_Id (from Account)*/
  private long paymentGroupId = 0;

  /** Column Information_Group_Id (from Information_Account_Link)*/
  private long informationGroupId = 0;

  public EmailRequestDescriptor()
  { }


  /**
   * Create a descriptor providing all data fields.
   *
   * <P>This constructor is intended for use by the DAO object.</P>
   */
/*  public EmailRequestDescriptor(long loginId, long invoiceId,
    String attachmentName, String attachmentType, Date lastMaintainedDate,
    String lastMaintainedBy)
  {
    this.loginId = loginId;
    this.invoiceId = invoiceId;
    this.attachmentName = attachmentName;
    this.attachmentType = attachmentType;
    this.lastMaintainedDate = lastMaintainedDate==null?null:new DateTime(lastMaintainedDate);
    this.lastMaintainedBy = lastMaintainedBy;
  }

  public EmailRequestDescriptor(long loginId, long invoiceId,
    String attachmentName, String attachmentType, Date lastMaintainedDate,
    String lastMaintainedBy, String emailAddress)
  {
    this.loginId = loginId;
    this.invoiceId = invoiceId;
    this.attachmentName = attachmentName;
    this.attachmentType = attachmentType;
    this.lastMaintainedDate = lastMaintainedDate==null?null:new DateTime(lastMaintainedDate);
    this.lastMaintainedBy = lastMaintainedBy;
    this.emailAddress = emailAddress;
  }*/

  public EmailRequestDescriptor(long loginId, long invoiceId,
    String attachmentName, Timestamp lastMaintainedDate,
    String lastMaintainedBy, String emailAddress, String attachmentLocation,
    String invoiceNo, String accountDetails, long accountId, long groupId,
    String groupType)
  {
    this.loginId = loginId;
    this.invoiceId = invoiceId;
    this.attachmentName = attachmentName;
    this.lastMaintainedDate = lastMaintainedDate==null?null:new DateTime(lastMaintainedDate);
    this.lastMaintainedBy = lastMaintainedBy;
    this.emailAddress = emailAddress;
    this.attachmentLocation = attachmentLocation;
    if (attachmentLocation != null)
    {
      attachmentLocationArray.add(attachmentLocation.replace('/', File.separatorChar));
    }
    this.invoiceNo = invoiceNo;
    this.accountDetails = accountDetails;
    this.accountId = accountId;
    if (groupType.equals("P"))
    {
      this.paymentGroupId = groupId;
    }
    else
    {
      this.informationGroupId = groupId;
    }
  }

  public EmailRequestDescriptor(long loginId, long invoiceId,
    long groupId, String groupType, Timestamp lastMaintainedDate,
    String lastMaintainedBy, String emailAddress,
    String invoiceNo, String accountDetails, long accountId,
    String notificationType)
  {
    this.loginId = loginId;
    this.invoiceId = invoiceId;
    this.attachmentName = attachmentName;
    this.lastMaintainedDate = lastMaintainedDate==null?null:new DateTime(lastMaintainedDate);
    this.lastMaintainedBy = lastMaintainedBy;
    this.emailAddress = emailAddress;
    this.attachmentLocation = attachmentLocation;
    if (attachmentLocation != null)
    {
      attachmentLocationArray.add(attachmentLocation.replace('/', File.separatorChar));
    }
    this.invoiceNo = invoiceNo;
    this.accountDetails = accountDetails;
    this.accountId = accountId;
    if (groupType.equals("P"))
    {
      this.paymentGroupId = groupId;
    }
    else
    {
      this.informationGroupId = groupId;
    }
    this.notificationType = notificationType;
  }



  public long getLoginId()
  {
    return loginId;
  }

  public long getInvoiceId()
  {
    return invoiceId;
  }

  public String getAttachmentName()
  {
    return attachmentName;
  }

  /*public String getAttachmentType()
  {
    return attachmentType;
  }*/

  public String getNotificationType()
  {
    return notificationType;
  }

  public DateTime getLastMaintainedDate()
  {
    return lastMaintainedDate;
  }

  public String getLastMaintainedBy()
  {
    return lastMaintainedBy;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getAttachmentLocation()
  {
    return attachmentLocation;
  }

  public String getAttachmentLocationAsFilePath()
  {
    return attachmentLocation.replace('/', File.separatorChar);
  }

  public String getInvoiceNo()
  {
    return invoiceNo;
  }

  public String getAccountDetails()
  {
    return accountDetails;
  }

  public long getAccountId()
  {
    return accountId;
  }

  public long getPaymentGroupId()
  {
    return paymentGroupId;
  }

  public long getInformationGroupId()
  {
    return informationGroupId;
  }

  public long getGroupId()
  {
    if (paymentGroupId == 0)
    {
      return informationGroupId;
    }
    else
    {
      return paymentGroupId;
    }
  }

  public String getGroupType()
  {
    if (paymentGroupId == 0)
    {
      return "I";
    }
    else
    {
      return "P";
    }
  }

  public ArrayList getAttachmentLocationArray()
  {
    return attachmentLocationArray;
  }

  public void addAttachmentLocation(String attachmentLocation)
  {
    attachmentLocationArray.add(attachmentLocation.replace('/', File.separatorChar));
  }

  public void addAttachmentLocationArray(ArrayList attachmentLocationArray)
  {
    this.attachmentLocationArray.addAll(0, attachmentLocationArray);
  }

}
