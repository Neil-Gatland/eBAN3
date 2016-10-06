package Batch;

import java.io.*;
import java.util.*;
import JavaUtil.*;
import DBUtilities.*;

public class createSAPUploadFile
{

  // class variables
  private DBAccess dba;
  private StringUtil su;
  private String journalDir, journalFileName, journalFilePathname, invoiceList;
  private BufferedWriter journalBW;
  // class constants
  private final String DTFMT = "yyyyMMddHHmmss";
  private final String CSVEXT = ".csv";
  private final String USCORE = "_";
  private final String filePrefix = "RSMA_SAP_Journal_";
  private final String NEWLINE = "\n";
  private final String DBLQUOTE = "\"";
  private final String SINGLEQUOTE = "'";
  private final String COMMA = ",";
  private final String EMPTY = " ";

  public createSAPUploadFile()
  {
    dba = new DBAccess();
    su = new StringUtil();
    journalDir = EBANProperties.getEBANProperty(EBANProperties.REVSHAREJNLDIR);
  }

  public int makeFile(String callMonth, String updatedBy)
  {
    int count=0, result=0;
    invoiceList = "";
    // Ignore all invoices where they have been uploaded to ebilling and not sent to SAP
    // but the provider is not set to send invoices to SAP
    result = dba.ignoreInvoicesForSAP(updatedBy);
    if (result==-1)
      count = -1;
    if (count==0)
    {
      boolean firstTime = true, ok = true;
      String jLine = "";
      // Identify invoices left for upload to SAP
      java.sql.ResultSet invoicesRS = dba.getSAPInvoicesToUpload();
      try
      {
        while ((invoicesRS.next())&&(ok))
        {
          if (firstTime)
          // on first invoice determine file name and open for writing
          {
            GregorianCalendar gc = new GregorianCalendar();
            String now = su.DateToString(gc.getTime(), DTFMT);
            String dts = now.substring(0, 8) + USCORE + now.substring(8, 14);
            journalFileName = filePrefix + dts + CSVEXT;
            journalFilePathname = journalDir + File.separator + journalFileName;
            if (!openJournalFile())
            {
              ok = false;
              count = -1;
            }
            if (ok)
            {
              // write out journal file header
              jLine =
                DBLQUOTE+"NewDoc"+DBLQUOTE+COMMA+
                DBLQUOTE+"COMP_CODE"+DBLQUOTE+COMMA+
                DBLQUOTE+"DOC_TYPE"+DBLQUOTE+COMMA+
                DBLQUOTE+"DOC_DATE"+DBLQUOTE+COMMA+
                DBLQUOTE+"PSTNG_DATE"+DBLQUOTE+COMMA+
                DBLQUOTE+"TRANSLATION_DATE"+DBLQUOTE+COMMA+
                DBLQUOTE+"HEADER_TXT"+DBLQUOTE+COMMA+
                DBLQUOTE+"Ref_Doc_Number"+DBLQUOTE+COMMA+
                DBLQUOTE+"CURRENCY"+DBLQUOTE+COMMA+
                DBLQUOTE+"Exchng. Rate"+DBLQUOTE+COMMA+
                DBLQUOTE+"GL_Account"+DBLQUOTE+COMMA+
                DBLQUOTE+"AMT_DOCCUR"+DBLQUOTE+COMMA+
                DBLQUOTE+"Tax code"+DBLQUOTE+COMMA+
                DBLQUOTE+"TAXJURCODE"+DBLQUOTE+COMMA+
                DBLQUOTE+"Tax Amount"+DBLQUOTE+COMMA+
                DBLQUOTE+"VENDOR"+DBLQUOTE+COMMA+
                DBLQUOTE+"Vendor Amount"+DBLQUOTE+COMMA+
                DBLQUOTE+"PAYMENT TERMS"+DBLQUOTE+COMMA+
                DBLQUOTE+"PAY Method"+DBLQUOTE+COMMA+
                DBLQUOTE+"BASE_DATE"+DBLQUOTE+COMMA+
                DBLQUOTE+"PROFIT_CENTER"+DBLQUOTE+COMMA+
                DBLQUOTE+"COSTCENTER"+DBLQUOTE+COMMA+
                DBLQUOTE+"INTERNAL_ORDER"+DBLQUOTE+COMMA+
                DBLQUOTE+"PROJECT_CODE"+DBLQUOTE+COMMA+
                DBLQUOTE+"REF_1"+DBLQUOTE+COMMA+
                DBLQUOTE+"REF_2"+DBLQUOTE+COMMA+
                DBLQUOTE+"REF_3"+DBLQUOTE+COMMA+
                DBLQUOTE+"ASSIGNMENT"+DBLQUOTE+COMMA+
                DBLQUOTE+"QUANTITY"+DBLQUOTE+COMMA+
                DBLQUOTE+"UNIT_MEASURE"+DBLQUOTE+COMMA+
                DBLQUOTE+"LINE_ITEM"+DBLQUOTE+COMMA+
                DBLQUOTE+"VEND_TEXT"+DBLQUOTE+COMMA+
                DBLQUOTE+"SEGMENT"+DBLQUOTE+COMMA+
                DBLQUOTE+"CHANNEL"+DBLQUOTE+COMMA+
                DBLQUOTE+"CUST_TYPE"+DBLQUOTE+COMMA+
                DBLQUOTE+"CALL_ORG"+DBLQUOTE+COMMA+
                DBLQUOTE+"BEAR_TECH"+DBLQUOTE+COMMA+
                DBLQUOTE+"VAL_TIER"+DBLQUOTE+COMMA+
                DBLQUOTE+"DEV_TECH"+DBLQUOTE+COMMA+
                DBLQUOTE+"CUSTOMER"+DBLQUOTE;      
              if (!writeJournalLine(jLine))
              {
                ok = false;
                count = -2;
              }
            }
            firstTime = false;
          }
          if (ok)
          {
            String invoiceNumber = invoicesRS.getString("Invoice_Number");
            // Get SAP details
            String[] journalDetails = new String[17];
            journalDetails = dba.getJournalDetails(invoiceNumber);
            String jdInvoiceNumber = journalDetails[0];
            String documentDate = journalDetails[1];
            String transactionType = journalDetails[2];
            String documentType = journalDetails[3];
            String companyCode = journalDetails[4];
            String postingDate = journalDetails[5];
            String period = journalDetails[6];
            String reference = journalDetails[7];
            String headerText = journalDetails[8];
            String taxAmount = journalDetails[9];
            String taxCode = journalDetails[10];
            String postingKeyLine1 = journalDetails[11];
            String postingKeyLine2 = journalDetails[12];
            String accountLine1 = journalDetails[13];
            String accountLine2 = journalDetails[14];
            String amountLine1 = journalDetails[15];
            String amountLine2 = journalDetails[16];
            if (jdInvoiceNumber.equals("Failed"))
            {
              ok = false;
              count = -3;
            }
            // Write out invoice line 1
            if (ok)
            {
              jLine =
                DBLQUOTE+"X"+DBLQUOTE+COMMA+
                DBLQUOTE+companyCode+DBLQUOTE+COMMA+
                DBLQUOTE+documentType+DBLQUOTE+COMMA+
                DBLQUOTE+documentDate+DBLQUOTE+COMMA+
                DBLQUOTE+postingDate+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+headerText+DBLQUOTE+COMMA+
                DBLQUOTE+reference+DBLQUOTE+COMMA+
                DBLQUOTE+"GBP"+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+accountLine1+DBLQUOTE+COMMA+
                DBLQUOTE+amountLine1+DBLQUOTE+COMMA+
                DBLQUOTE+taxCode+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+taxAmount+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+"GB07002784"+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+headerText+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE;     
                if (!writeJournalLine(jLine))
                {
                  ok = false;
                  count = - 4;
                }
              }
            // Write out invoice line 2
            if (ok)
            {
              jLine =
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+companyCode+DBLQUOTE+COMMA+
                DBLQUOTE+documentType+DBLQUOTE+COMMA+
                DBLQUOTE+documentDate+DBLQUOTE+COMMA+
                DBLQUOTE+postingDate+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+headerText+DBLQUOTE+COMMA+
                DBLQUOTE+reference+DBLQUOTE+COMMA+
                DBLQUOTE+"GBP"+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+accountLine2+DBLQUOTE+COMMA+
                DBLQUOTE+amountLine2+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+headerText+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE+COMMA+
                DBLQUOTE+EMPTY+DBLQUOTE;     
                if (!writeJournalLine(jLine))
                {
                  ok = false;
                  count = - 5;
                }
              }
            // increment invoice count and invoice list
            if (ok)
            {
              count++;
              invoiceList = invoiceList+SINGLEQUOTE+invoiceNumber+SINGLEQUOTE+COMMA;
            }
          }
        }
      }
      catch(java.sql.SQLException ex)
      {
        count = -6;
      }
      finally
      {
        try
        {
          // close invoice record set and close journal file 
          invoicesRS.close();
          invoicesRS = null;
          // only close file if there is at least one invoice and file has been opened
          if ((ok)&&(count > 0))
          {
            if (!closeJournalFile())
              count = -7;
          }
        }
        catch(java.sql.SQLException ex)
        {
          count = -8;
        }
      }
    }
    // Update invoices as sent to sap with journal file name
    if (count>1)
    {
      // remove comma at end of invoice list
      invoiceList = invoiceList.substring(0,invoiceList.length() - 1);
      if (!dba.updateInvoicesSentToSAP(invoiceList,journalFileName,updatedBy))
        count = -9;
    }
    return count;
  }

  // open journal file
  private boolean openJournalFile()
  {
    boolean result = false;
    try
    {
      journalBW = new BufferedWriter(new FileWriter(new File(journalFilePathname)));
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error opening journal file "+journalFilePathname+" : "+ex.getMessage());
    }
    return result;
  }

  // write out a string to the journal file
  private boolean writeJournalLine(String journalLine)
  {
    boolean result = false;
    try
    {
      journalBW.write(journalLine+NEWLINE);
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error writing line to journal file : "+ex.getMessage());
    }
    return result;
  }

  // close journal file
  private boolean closeJournalFile()
  {
    boolean result = false;
    try
    {
      journalBW.close();
      result = true;
    }
    catch (Exception ex)
    {
      System.out.println("Error closing journal file "+journalFilePathname+" : "+ex.getMessage());
    }
    return result;
  }

  private void defaultRun()
  {
    String callMonth = dba.getCurrentCallMonth();
    int result = makeFile(callMonth,"Stand alone run");
    System.out.println("Stand alone run result is "+result);
  }

  public static void main(String[] args)
  {
    createSAPUploadFile cSUF = new createSAPUploadFile();
    cSUF.defaultRun();
  }

}



