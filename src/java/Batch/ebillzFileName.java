package Batch;

import java.util.*;
import JavaUtil.*;

public class ebillzFileName
{

  // class variables
  private String accountNumber;
  private String invoiceNumber;
  private String fileType;
  private boolean monthly;
  private boolean invoicePDF;
  private String pdfName;
  private String startDate;
  private String endDate;

  //working variables
  private String fileExtension;
  private int workLength;
  private String work;
  private StringUtil su;
  private String defaultDate;

  public ebillzFileName(String fileName)
  {
    // assume file is not invoice pdf and set pdf name to empty string
    invoicePDF = false;
    pdfName = "";
    // derive default date as first date of current month
    su = new StringUtil();
    GregorianCalendar gc = new GregorianCalendar();
    String now = su.DateToString(gc.getTime());
    defaultDate = "01-" + now.substring(3,6) + "-" + now.substring(9,11);
    workLength = fileName.length();
    // check file extension and determine file type
    fileExtension = fileName.substring(workLength -4,workLength).trim();
    // cater for upper case or lower case for CSV file names
    if ((fileExtension.startsWith(".csv"))||(fileExtension.startsWith("CSV")))
    {
      fileType = "cdr";
    }
    else
    {
      // cater for upper case or lower case for PDF file names
      if ((fileExtension.startsWith(".pdf"))||(fileExtension.startsWith(".PDF")))
      {
        fileType = "pdf";
      }
      else
      {
        fileType = "invalid";
      }
    }
    work = fileName.substring(0,workLength-4).trim();
    // extract the account number
    // There are now two different account number formats in use:
    // Movex - C99999 
    // Original - 999999_IR or 999999_UK
    String firstChar = work.substring(0,1);
    if (firstChar.equals("C"))
    {
        // Movex account number format      
        accountNumber = work.substring(0,6);
        workLength = work.length();
        work = work.substring(7,workLength);
    }
    else
    {
        // Original account number format        
        accountNumber = work.substring(0,9);
        workLength = work.length();
        work = work.substring(10,workLength);
    }
    workLength = work.length();
    // work out required values for cdr files
    if (fileType=="cdr")
    {
      if (work.endsWith("_Inclusive"))
      {
        monthly = false;
        work = work.substring(0,workLength-10);
        workLength = work.length();
        startDate = work.substring(workLength-22,workLength-13);
        endDate = work.substring(workLength-9,workLength);
      }
      else if (work.endsWith("_only"))
      {
        monthly = false;
        work = work.substring(0,workLength-5);
        workLength = work.length();
        startDate = work.substring(workLength-9,workLength);
        endDate = startDate;
      }
      else if (work.endsWith("_InvLines"))
      {
        monthly = true;
        work = work.substring(0,workLength-9);
        workLength = work.length();
        startDate = defaultDate;
        endDate = defaultDate;
      }
      else
      {
        monthly = true;
        invoiceNumber = work.substring(workLength-7,workLength);
        startDate = defaultDate;
        endDate = defaultDate;
      }
    }
    // work out required values for pdf files
    // (after August change only PDF supplied is invoice PDF containing all reports)
    if (fileType=="pdf")
    {
      monthly = true;
      startDate = defaultDate;
      endDate = defaultDate;
      invoiceNumber = work.substring(workLength-7,workLength);
      pdfName = "Invoice";
      invoicePDF = true;
    }
  }

  // get methods for public attributes
  public String getFileType()
  {
    return fileType;
  }
  public String getAccountNumber()
  {
    return su.isNull(accountNumber,"");
  }
  public String getInvoiceNumber()
  {
    return su.isNull(invoiceNumber,"");
  }
  public String getPDFName()
  {
    return su.isNull(pdfName,"");
  }
  public String getStartDate()
  {
    return su.isNull(startDate,"");
  }
  public String getEndDate()
  {
    return su.isNull(endDate,"");
  }
  public boolean getMonthly()
  {
    return monthly;
  }
  public boolean getInvoicePDF()
  {
    return invoicePDF;
  }

  // dummy main() required
  public static void main()
  {
  }

}