package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class loadPCBInvoices
{

  // class variables
  private String dropDir, logDir, attachDir, runTS;
  private StringUtil su;
  private DBAccess dba;
  private BufferedWriter logWriter, splitWriter;
  private File logFile;
  private int fileCount = 0 , failCount = 0, loadedCount = 0;
  // class constants
  private final String PCB = "PCB";
  private final String SPIF = "Split PCB Impact Files";
  private final String LPI = "Load PCB Invoices";
  private final String NOTAVAILABLE = "Not available";
  private final String ACTIVE = "Active";
  private final String INACTIVE = "Inactive";
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String USCORE = "_";
  private final String PERIOD = ".";
  private final String PDF = "pdf";
  private final String BCKSLASH = "\\";
  private final String FWDSLASH = "/";
  private final String MINUS = "-";

  private loadPCBInvoices()
  {
    su = new StringUtil();
    dba = new DBAccess();
    dropDir = EBANProperties.getEBANProperty(EBANProperties.LPIDROPDIR);
    logDir = EBANProperties.getEBANProperty(EBANProperties.LPILOGDIR);
    attachDir = EBANProperties.getEBANProperty(EBANProperties.LPIATTACHDIR);
  }

  private boolean checkRunControl()
  {
    boolean result = false;
    String status = dba.getRunControlStatus(PCB,LPI), message="";
    if (status.startsWith(NOTAVAILABLE))
    {
      message = "!!!Expected job control row for this process is missing!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
    else
    {
      if (status.startsWith(ACTIVE))
      {
        message = "!!!this process is already running!!!";
        writeToLogFile(message);
        System.out.println(message);
      }
      else
      {
        status = dba.getRunControlStatus(PCB,SPIF);
        if (!status.startsWith(INACTIVE))
        {
          message = "!!!cannot run while split PCB impact file process is running!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
          if (dba.updateRunControlStatus(PCB,LPI,ACTIVE))
          {
            result = true;
          }
          else
          {
            message = "!!!cannot set run control for this process!!!";
            writeToLogFile(message);
            System.out.println(message);
          }
        }
      }
    }
    return result;
  }

  private void resetRunControl()
  {
    if (!dba.updateRunControlStatus(PCB,LPI,INACTIVE))
    {
      String message = "!!!failed to rest run control for this process to inactive!!!";
      writeToLogFile(message);
      System.out.println(message);
    }
  }

  private void processPDFs()
  {
    writeToLogFile(" ");
    try
    {
      // process files in the pdf directory
      File directory = new File(dropDir);
      File[] fileArray = directory.listFiles();
      for (int i = 0; i < fileArray.length; i++)
      {
        File pdfFile = fileArray[i];
        String absoluteFilename = pdfFile.getAbsolutePath();
        String filename = pdfFile.getName();
        if (pdfLoaded(absoluteFilename, filename))
          loadedCount++;
        else
        {
          failCount++;
        }
        fileCount++;
      }
    }
    catch(Exception ex)
    {
      writeToLogFile("Error in processPDFs : " + ex.getMessage());
    }
  }

  private boolean pdfLoaded( String filename, String pdfname)
  {
    boolean result = false;
    String webName = "";
    // analyse file name
    int j = 0; // this signifies which element of the file name we are in
    String[] name = new String[10];
    String work ="";
    for(int i=0; i<pdfname.length(); i++)
    {
      if (String.valueOf(pdfname.charAt(i)).startsWith(USCORE)||String.valueOf(pdfname.charAt(i)).startsWith(PERIOD))
      {
        name[j] = work;
        work = "";
        j++;
      }
      else
      {
        work = work + pdfname.charAt(i);
      }
    }
    name[j] = work;
    if (j<7)
      writeToLogFile("   Not enough data supplied in file name for file "+pdfname);
    else if (((j>7)&&((!name[7].startsWith("BLUE"))))||((j>8)))
      writeToLogFile("   Too much data supplied in file name for file "+pdfname);
    else if ((!name[7].startsWith(PDF))&&(!name[8].startsWith(PDF)))
      writeToLogFile("   Invalid file type for file "+pdfname);
    else
    {
      String accountNumber = name[0];
      String taxPointDate = name[1];
      String invoiceNumber = name[2];
      String type = name[3];
      String amountText = name[4];
      String periodFrom = name[5];
      String periodTo = name[6];
      // Get account, payment group and customers ids for the account
      long[] ids = new long[3];
      ids = dba.PCBAccountIds(accountNumber);
      long accountId = ids[0];
      long paymentGroupId = ids[1];
      long customerId = ids[2];
      if (accountId==-1)
        // cannot process the file as the account has not been set up
        writeToLogFile("   Account "+accountNumber+" not set up for "+pdfname);
      else
      {
        // the PCB account has been set up so create the invoice
        long invoiceId = dba.PCBInvoice(invoiceNumber,accountId,taxPointDate,amountText,periodFrom,periodTo);
        if (invoiceId==-1)
          // the invoice has not been created
          writeToLogFile("   Failed to create invoice "+invoiceNumber+" for "+pdfname);
        else
        {
          // create (or replace) the attachment
          String attachName =
            accountNumber + USCORE +
            taxPointDate + USCORE +
            invoiceNumber + USCORE +
            type + PERIOD + PDF;
          String locationPath =
            PCB + FWDSLASH +
            customerId + FWDSLASH +
            paymentGroupId + FWDSLASH +
            accountId + FWDSLASH +
            invoiceId + FWDSLASH;
          String attachPath =
            attachDir + BCKSLASH +
            PCB + BCKSLASH +
            customerId + BCKSLASH +
            paymentGroupId + BCKSLASH +
            accountId + BCKSLASH +
            invoiceId + BCKSLASH;
          String location = locationPath + attachName;
          if (type.startsWith("INVOICE"))
            if (amountText.startsWith(MINUS))
              webName = "Credit Note";
            else
              webName = "Invoice";
          else
            webName = "Other Report(s)";
          if (!dba.PCBAttachment(location,invoiceId,taxPointDate,webName))
            writeToLogFile("   Failed to create attachment for "+pdfname);
          else
          {
            // create or use the attachment directory
            boolean ok = true;
            File attachDirectory = new File(attachPath);
            if (!attachDirectory.exists())
            {
              if (!attachDirectory.mkdirs())
              {
                writeToLogFile("   Failed to create attachment directory for "+pdfname);
                ok = false;
              }
            }
            if (ok)
            {
              // create abstract file entries for the current file and the new attachment
              File current = new File(filename);
              File attachment = new File(attachPath+attachName);
              // check if attachment already exists, if it does delete it
              if (attachment.exists())
              {
                if (!attachment.delete())
                {
                  writeToLogFile("   Failed to delete existing attachment for "+pdfname);
                  ok = false;
                }
              }
              if (ok)
              {
                // move current file to attachment directory
                if (current.renameTo(attachment))
                  result = true;
                else
                  writeToLogFile("   Failed to move attachment for "+pdfname+" to the attachment directory");
              }
            }
          }
        }
      }
    }
    return result;
  }

  private void openLogFile()
  {
    // open a new log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      runTS = su.reformatDate("now", null, DTFMT);
      logFile = new File(logDir + File.separator + runTS + "_loadPCBInvoices_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
      String now = su.DateToString(gc.getTime(), DTFMT);
      writeToLogFile("loadPCBInvoices processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
    }
    catch (Exception ex)
    {
      System.out.println("Error opening log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToLogFile(String message)
  {
    // write supplied text to program log file
    try
    {
      logWriter.write(message);
      logWriter.newLine();
    }
    catch (Exception ex)
    {
      System.out.println("Error writing message '" + message +
        "' to log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void closeLogFile()
  {
    // at end of program close log file
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DTFMT);
      // summarise pdf files processed in log file
      writeToLogFile(" ");
      writeToLogFile("pdfs processed:       "+fileCount);
      writeToLogFile("loaded successfully:  "+loadedCount);
      writeToLogFile("failed to load:       "+failCount);
      writeToLogFile(" ");
      writeToLogFile("loadPCBInvoices processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4));
      logWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }


  public static void main(String[] args)
  {
    // control processing
    loadPCBInvoices lpi = new loadPCBInvoices();
    lpi.openLogFile();
    if (lpi.checkRunControl())
    {
      lpi.processPDFs();
      lpi.resetRunControl();
    }
    lpi.closeLogFile();
  }

}



