package Batch;

import DBUtilities.DBAccess;
import JavaUtil.EBANProperties;
import JavaUtil.StringUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.GregorianCalendar;

public class processBIERFiles 
{   
    // global variables
    private DBAccess dba;
    private StringUtil su;
    private String runTS;
    private File logFile;
    private BufferedWriter logWriter, textWriter;
    private String dropDir, logDir, procDir, splitPCBDir, splitSSBSDir;
    private String message;
    private File docSectn, docPage, document, invceItem;
    private boolean docStart, pageStart, invoice;
    // global constants
    private final String PCB = "PCB";
    private final String PBF = "Process BIER Files";
    private final String SPIF = "Split PCB Impact Files";
    private final String LPI = "Load PCB Invoices";
    private final String SIF = "Split Impact Files";
    private final String LSI = "Load SSBS Invoices";
    private final String SSBS = "SSBS";
    private final String ACTIVE = "Active";
    private final String INACTIVE = "Inactive"; 
    private final String NOTAVAILABLE = "Not available";
    private final String DTFMT = "yyyyMMddHHmmss"; 
    private final String TBEA031 = "TBEA031";
    private final String TBEA05 = "TBEA05";
    private final String TBEA011 = "TBEA011";
    private final String TBEA021 = "TBEA021";
    private final String MISSING = "Missing";
    private final String LASTMAINTAINEDBY = "process BIER Files";
    private final String PAGECONTROL = "0003";
    private final String PAGECONTROLALT = "0004";
    private final String USCORE = "_";
    private final String MINUS = "-";
    private final String CREDITSUFFIX = "C";
    private final String INVOICE = "INVOICE";
    private final String OTHERS = "OTHERS";
    private final String BLUE = "BLUE";
    private final String GREEN = "GREEN";
    private final String TXT = ".txt";
    private final String THROWLINE1 = "0";
    private final String THROWLINE2 = "-";
    private final String FORMFEED = "\f";
    private final String NEWLINE = "\n";
    private final String DOLLAR = "$";
    private final String POUND = "£";
   
    private processBIERFiles()
    {
        // create required classes
        dba = new DBAccess(); 
        su = new StringUtil();
        // get directory names
        dropDir = EBANProperties.getEBANProperty(EBANProperties.PBFDROPDIR);
        logDir = EBANProperties.getEBANProperty(EBANProperties.PBFLOGDIR);
        procDir = EBANProperties.getEBANProperty(EBANProperties.PBFPROCDIR);
        splitPCBDir = EBANProperties.getEBANProperty(EBANProperties.SPISPLITDIR);
        splitSSBSDir = EBANProperties.getEBANProperty(EBANProperties.SPSISPLITDIR);
    }
    
    private void processDroppedFiles()
    {
        writeToLogFile("   ");
        String docSectnName = MISSING, docPageName = MISSING, documentName = MISSING, invceItemName = MISSING;
        int fileCount = 0;
        boolean OK = true;
        // Check drop directory for exactly the four required files
        File directory = new File(dropDir);
        File[] fileArray = directory.listFiles();
        for (int i = 0; i < fileArray.length; i++)
        {
            File textFile = fileArray[i];
            String textFileName = textFile.getName();
            //System.out.println(textFileName);
            if (textFileName.startsWith(TBEA031))
                docSectnName = textFileName;
            if (textFileName.startsWith(TBEA05))
                docPageName = textFileName;
            if (textFileName.startsWith(TBEA011))
                documentName = textFileName;
            if (textFileName.startsWith(TBEA021))
                invceItemName = textFileName;
            fileCount++;
        }
        if ((docSectnName.equals(MISSING))||(docPageName.equals(MISSING))||
                (documentName.equals(MISSING))||(invceItemName.equals(MISSING)))
        {
            OK = false;
            message = "   FAILURE: One of the required files is missing";
            writeToLogFile(message);
            System.out.println(message);
        }
        if ((OK)&&(fileCount!=4))
        {
            OK = false;
            message = "   FAILURE: There are more than four files in the drop directory";
            writeToLogFile(message);
            System.out.println(message);
        }
        if (OK)
        {
            // create file objects
            docSectn = new File(dropDir+File.separator+docSectnName);
            docPage = new File(dropDir+File.separator+docPageName);
            document = new File(dropDir+File.separator+documentName);
            invceItem = new File(dropDir+File.separator+invceItemName);
            
        }
        if (OK)
        {
            // clear down BIER tables
            OK = dba.cleardownBIERTables();
        }
        if (OK)
        {
            // populate BIER document table
            OK = loadBIERDocument(document);
        }
        if (OK)
        {
            // populate BIER doc sectn table
            OK = loadBIERDocSectn(docSectn);
        }
        if (OK)
        {
            // process BIER Doc Page file
            OK = processBIERDocPage(docPage);
        }
        if (OK)
        {
            // Move four BIER files into the processed files directory            
            File docSectnNewLoc = new File(procDir+File.separator+docSectnName);
            File docPageNewLoc = new File(procDir+File.separator+docPageName);
            File documentNewLoc = new File(procDir+File.separator+documentName);
            File invceItemNewLoc = new File(procDir+File.separator+invceItemName);
            if (!docSectn.renameTo(docSectnNewLoc))
            {
                message = "   WARNING: Failed to move Doc Sectn file to processed directory";
                writeToLogFile(message);
                System.out.println(message);                
            }
            if (!docPage.renameTo(docPageNewLoc))
            {
                message = "   WARNING: Failed to move Doc Page file to processed directory";
                writeToLogFile(message);
                System.out.println(message);                
            }
            if (!document.renameTo(documentNewLoc))
            {
                message = "   WARNING: Failed to move Document file to processed directory";
                writeToLogFile(message);
                System.out.println(message);                
            }
            if (!invceItem.renameTo(invceItemNewLoc))
            {
                message = "   WARNING: Failed to move Invce Item file to processed directory";
                writeToLogFile(message);
                System.out.println(message);                
            }
        }        
    } 
    
    private boolean loadBIERDocument(File bierDocumentFile)
    {
        // loads data from document file onto the BIER Document table
        boolean result = false;
        int loopCount = 0, insertCount = 0;
        try
        {
            // open document file
            BufferedReader bdr = new BufferedReader(new FileReader(bierDocumentFile));
            // get first line
            String documentLine = bdr.readLine();
            boolean OK = true;
            while (documentLine!=null)
            {
                loopCount++;
                // format required data items
                String accountNumber = documentLine.substring(0,8);
                String documentDate = // reformat date as YYYYMMDD
                    documentLine.substring(14,18) + 
                    documentLine.substring(11,13) + 
                    documentLine.substring(8,10); 
                String invoiceNo = documentLine.substring(18,26);
                String totalAmount = formatAmount(documentLine.substring(75,86));
                String noPages = formatAmount(documentLine.substring(67,75));
                //System.out.println(accountNumber+":"+documentDate+":"+invoiceNo+":"+totalAmount+":"+noPages);
                // write BIER Document row (ignore if previous insert has failed)
                if (OK)
                {
                    OK = dba.insertBIERDocument(
                            accountNumber, 
                            documentDate, 
                            invoiceNo, 
                            totalAmount, 
                            noPages, 
                            LASTMAINTAINEDBY);
                    insertCount++;
                }
                    
                // read next line
                documentLine = bdr.readLine();
            }
            bdr.close();
            message = "   BIER Document  : "+loopCount+" records read : "+insertCount+" records inserted";
            writeToLogFile(message);
            System.out.println(message);
            result = true;
        }   
        catch(Exception ex)
        {
            message = "   FAILURE: Error processing Document file : "+ex.getMessage();            
            writeToLogFile(message);
            System.out.println(message);
        }
        return result;
    }
    
     private boolean loadBIERDocSectn(File bierDocSectnFile)
    {
        boolean result = false;
        try
        {
            // open doc sectn file
            BufferedReader bdsr = new BufferedReader(new FileReader(bierDocSectnFile));
            // get first line
            String docSectnLine = bdsr.readLine();
            boolean OK = true;
            int loopCount = 0, insertCount = 0;
            while(docSectnLine!=null)
            {
                loopCount++;
                // format required data items
                String accountNumber = docSectnLine.substring(0,8);
                String documentDate = // reformat date as YYYYMMDD
                    docSectnLine.substring(14,18) + 
                    docSectnLine.substring(11,13) + 
                    docSectnLine.substring(8,10); 
                String invoiceNo = docSectnLine.substring(18,26);
                String sectionType = docSectnLine.substring(64,104).trim();
                String startPage = formatAmount(docSectnLine.substring(104,112));
                String endPage = formatAmount(docSectnLine.substring(112,120));
                //System.out.println(accountNumber+":"+documentDate+":"+invoiceNo+":"+
                //                    sectionType+":"+startPage+":"+endPage);
                // write BIER Doc Sectn row (ignore if previous insert has failed)
                if (OK)
                {
                    OK = dba.insertBIERDocSectn(
                                accountNumber, 
                                documentDate, 
                                invoiceNo, 
                                sectionType, 
                                startPage, 
                                endPage, 
                                LASTMAINTAINEDBY);
                    insertCount++;
                }
                // read next line
                docSectnLine = bdsr.readLine();
            }            
            bdsr.close();
            message = "   BIER Doc Sectn : "+loopCount+" records read : "+insertCount+" records inserted";
            writeToLogFile(message);
            System.out.println(message);
            result = true;
        }
        catch(Exception ex)
        {
            message = "   FAILURE: Error processing Doc Sectn file : "+ex.getMessage();
            writeToLogFile(message);
            System.out.println(message);
        }        
        return result;
    }
       
    private boolean processBIERDocPage(File bierDocPageFile)
    {
        // loads data from document file onto the BIER Document table
        boolean result = false;
        
        try
        {            
            // open doc page file
            BufferedReader bdpr = new BufferedReader(new FileReader(bierDocPageFile));
            // get first line
            String invoiceNo =  invoiceNo = "";
            String docPageLine = bdpr.readLine();
            String storedInvoiceNo = "XXXXXXXX";
            boolean OK = true;
            int loopCount = 0, pageCount = 0, invoiceCount = 0, invoiceFileCount = 0, othersFileCount = 0;
            String accountNumber = "", documentDate = "", pageNoText = "";
            String taxPointDate = "", noPagesText = "", totalAmount = "";
            String invoiceStartPageText = "", invoiceEndPageText = "";
            int pageNo, noPages = 0, invoiceStartPage = 0, invoiceEndPage = 0;
            String pageLine = "";
            while(docPageLine!=null&&OK)
            {
                loopCount++;
                String partNum = docPageLine.substring(0,4);
                // line detailing the page
                if ((partNum.equals(PAGECONTROL))||(partNum.equals(PAGECONTROLALT)))
                {   
                    pageStart = true;
                    accountNumber = docPageLine.substring(4,12);
                    documentDate = // reformat date as YYYYMMDD
                        docPageLine.substring(18,22) + 
                        docPageLine.substring(15,17) + 
                        docPageLine.substring(12,14); 
                    invoiceNo = docPageLine.substring(22,30);
                    pageNoText = formatAmount(docPageLine.substring(30,38));
                    pageNo = Integer.parseInt(pageNoText);
                    if (!storedInvoiceNo.startsWith(invoiceNo))
                    {
                        // close invoice or others text file (except at first invoice)                        
                        if (!storedInvoiceNo.startsWith("XXXXXXXX"))                                                   
                            if (!closeTextFile())
                                OK = false;
                        // new invoice started so get details
                        String[] invoiceDetails = dba.getBIERInvoiceDetails(invoiceNo);
                        taxPointDate = invoiceDetails[0];
                        noPagesText = invoiceDetails[1];
                        totalAmount = invoiceDetails[2];
                        invoiceStartPageText = invoiceDetails[3];
                        invoiceEndPageText = invoiceDetails[4];
                        //System.out.println(taxPointDate+":"+noPagesText+":"+totalAmount+":"+invoiceStartPageText+":"+invoiceEndPageText);                        
                        if (taxPointDate.startsWith("Not found"))
                        {
                            // stop processing if invoice details not found
                            OK = false;
                            message = "   FAILURE: Error getting details from BIER Invoice View for invoice "+invoiceNo;
                            writeToLogFile(message);
                            System.out.println(message);
                        }
                        else
                        {
                            // convert page counts to integer
                            noPages = Integer.parseInt(noPagesText);
                            invoiceStartPage = Integer.parseInt(invoiceStartPageText);
                            invoiceEndPage = Integer.parseInt(invoiceEndPageText);
                            // open invoice text file
                            if (!openTextFile(accountNumber,invoiceNo,totalAmount,taxPointDate,INVOICE))
                                OK = false;
                            else
                            {
                                invoiceFileCount++;
                                invoice = true;
                                docStart = true;
                            }
                            //System.out.println("Open invoice text file for invoice "+invoiceNo);
                        }
                        invoiceCount++;
                    }
                    if (pageNo==invoiceEndPage+1)
                    {
                        // close invoice text file 
                        if (!closeTextFile())
                            OK = false;                       
                        //System.out.println("Close invoice text file for invoice "+invoiceNo);
                        // open others text file
                        if (!openTextFile(accountNumber,invoiceNo,totalAmount,taxPointDate,OTHERS))
                            OK = false;
                        else
                        {
                            othersFileCount++;
                            invoice = false;
                            docStart = true;
                        }
                        //System.out.println("Open others text file for "+invoiceNo);
                    }                    
                    storedInvoiceNo = invoiceNo;                    
                    //System.out.println(accountNumber+":"+documentDate+":"+invoiceNo+":"+pageNo);
                    pageCount++;
                }
                // page line
                else
                {
                   String lineControl = docPageLine.substring(0,1);
                   String lineData = docPageLine.substring(1, docPageLine.length());
                   if (docStart)
                   // ignore first newpage at start of document
                   {
                       if (invoice)
                            pageLine = NEWLINE + NEWLINE + NEWLINE + lineData;
                       else
                            pageLine = NEWLINE + lineData;
                       docStart = false;
                       pageStart = false;
                   }
                   else
                   {
                       // Process line control characters
                       if (pageStart)
                       {
                           // throw new page except at start of document
                           if (invoice)
                                pageLine = FORMFEED + NEWLINE + NEWLINE + NEWLINE + lineData;
                           else
                                pageLine = FORMFEED + lineData;
                           pageStart = false;
                       }
                       else if (lineControl.startsWith(THROWLINE1))
                       {
                           // throw new line always on 0
                           pageLine = NEWLINE + lineData;
                       }
                       else if (lineControl.startsWith(THROWLINE2))
                       {
                           // throw new line on - if line is not blank
                           if (lineData.trim().length()>0)
                                pageLine = NEWLINE + lineData;
                           else
                                pageLine = lineData;
                       }
                       else
                           pageLine = lineData;
                   }    
                   // Write line to current open file
                   if (!writeToTextFile(pageLine))
                       OK = false;
                }
                // read next line
                docPageLine = bdpr.readLine();                
            }
            // Close last invoice or others text file
            if (!closeTextFile())
                OK = false;          
            //System.out.println("Close invoice or others text file for invoice "+invoiceNo);
            bdpr.close();
            message = 
                    "   BIER Doc Page  : "+loopCount+" records read : "+pageCount+
                    " pages processed : "+invoiceCount+" invoices processed : "+
                    invoiceFileCount + " invoice text files : "+
                    othersFileCount + " other text files";
            writeToLogFile(message);
            System.out.println(message);
            if (OK)
                result = true;
        }
        catch(Exception ex)
        {
            message = "   FAILURE: Error processing Doc Sectn file : "+ex.getMessage();
            writeToLogFile(message);
            System.out.println(message);
        }        
        return result;
    }
        
    private String formatAmount(String unformattedAmount)
    {
        // remove leading spaces and decimal point from numeric data
        String result = "";
        boolean ignore = true;
        for (int i = 0; i<unformattedAmount.length(); i++)
        {
          String testChar = unformattedAmount.substring(i, i+1).trim(); 
          if (testChar.equals("0"))
          {
              if (!ignore)
                result = result + testChar;
          }
          else
          {
              if (!testChar.equals("."))
              {
                  ignore = false;
                  result = result + testChar;       
              }                         
          }
        }
        if (result.length()==0)
            result = "000";
        return result;
    }  
          
    private boolean openTextFile(String accountNo, String invoiceNo, String totalAmount, String taxPointDate, String type)
    {
        // open a text file
        boolean result = true;
        // determine file name
        String filename = accountNo + USCORE + taxPointDate + USCORE + invoiceNo + USCORE + type + USCORE;
        if (invoiceNo.endsWith(CREDITSUFFIX)) // add negative sign to amount for credit note
            filename = filename + MINUS;
        filename = filename + totalAmount + USCORE + taxPointDate + USCORE + taxPointDate;
        if (type.startsWith(INVOICE))
        {
            if ((accountNo.startsWith("080"))||(accountNo.startsWith("081")))
                filename = filename + USCORE + GREEN;
            else
                filename = filename + USCORE + BLUE;
        }
        filename = filename + TXT;
        // determine if PCB or SSBS for path
        String pathname = splitPCBDir;
        if (invoiceNo.startsWith("1"))
            pathname = splitSSBSDir;
        pathname = pathname + File.separator + filename;
        try
        {          
            textWriter = new BufferedWriter(new FileWriter(new File(pathname)));
        }
        catch (Exception ex)
        {
          System.out.println("Error opening text file file : " +pathname+" : "+ ex.getMessage());
          result = false;
        }
        return result;
    }  
        
    private boolean closeTextFile()
    {
        // close text file
        boolean result = true;
        try
        {
          textWriter.close();
        }
        catch (Exception ex)
        {
          System.out.println("Error closing text file: " + ex.getMessage());
          result = false;
        }
        return result;
    }
    
    private boolean checkRunControl()
    {
        boolean result = false;
        String status = dba.getRunControlStatus(PCB,PBF);
        // required run control is missing
        if (status.startsWith(NOTAVAILABLE))
        {
          message = "!!!Expected job control row for this process is missing!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
        else
        {
            // process is already running
            if (status.startsWith(ACTIVE))
            {
                message = "!!!!!!this process is already running!!!!!!";
                writeToLogFile(message);
                System.out.println(message); 
            }
            else
            {
                String statusSPIF = dba.getRunControlStatus(PCB, SPIF);
                String statusLPI = dba.getRunControlStatus(PCB, LPI);
                String statusSIF = dba.getRunControlStatus(SSBS, SIF);
                String statusLSI = dba.getRunControlStatus(SSBS, LSI);
                // another process is running that uses the same directories
                if ((statusSPIF.startsWith(ACTIVE))||(statusLPI.startsWith(ACTIVE))||
                        (statusSIF.startsWith(ACTIVE))||(statusLSI.startsWith(ACTIVE)))
                    
                {
                   message = "!!! related process(es) ";
                   if (statusSPIF.startsWith(ACTIVE))
                       message = message+"'"+SPIF+"' ";
                   if (statusLPI.startsWith(ACTIVE))
                       message = message+"'"+LPI+"' ";
                   if (statusSIF.startsWith(ACTIVE))
                       message = message+"'"+SIF+"' ";
                   if (statusLSI.startsWith(ACTIVE))
                       message = message+"'"+LSI+"' ";
                   message = message+"running!!!";
                   writeToLogFile(message);
                   System.out.println(message);                   
                }
                else
                {
                    // process can run
                    if (dba.updateRunControlStatus(PCB,PBF,ACTIVE))
                        result = true;
                    else
                    {
                        // error setting run control for this process
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
        // reset run control at completion to allow process to run again
        if (!dba.updateRunControlStatus(PCB,PBF,INACTIVE))
        {
          String message = "!!!failed to rest run control for this process to inactive!!!";
          writeToLogFile(message);
          System.out.println(message);
        }
    }
    
    private boolean openLogFile()
    {
        boolean result = true;
        // open a new log file
        try
        {
            GregorianCalendar gc = new GregorianCalendar();
            runTS = su.reformatDate("now", null, DTFMT);
            logFile = new File(logDir + File.separator + runTS + "_processBIERFiles_log.txt");
            logWriter = new BufferedWriter(new FileWriter(logFile));
            String now = su.DateToString(gc.getTime(), DTFMT);
            writeToLogFile("processBIERFiles processing started at " +
                now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
                now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
                now.substring(4, 6) + "/" + now.substring(0, 4));
        }
        catch (Exception ex)
        {
          System.out.println("Error opening log file: " + ex.getMessage());
          result = false;
        }
        return result;
    }
      
    private boolean writeToTextFile(String textline)
    {
        // write supplied text to text file
        boolean result = true;
        try
        {
          textWriter.write(scanText(textline));
          textWriter.newLine();
        }
        catch (Exception ex)
        {
          System.out.println("Error writing text line '" + textline +
            "' to text file: " + ex.getMessage());
          result = false;
        }
        return result;
    }
    
    private String scanText(String text)
    {
        // tidies up text data
        String result = "";
        for (int i = 0; i<text.length(); i++)
        {
            String testChar = text.substring(i,i+1);
            if (testChar.startsWith(DOLLAR))
                    result = result + POUND;
            else
                result = result + testChar;            
        }
        return result;
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
          writeToLogFile("processBIER Files processing ended at " +
            now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
            now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
            now.substring(4, 6) + "/" + now.substring(0, 4));
          logWriter.close();
        }
        catch (Exception ex)
        {
          System.out.println("Error closing log file: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args)
    {
        processBIERFiles pbf = new processBIERFiles();
        // open process log file
        if (pbf.openLogFile())
        {
            // check that the process is not already running
            if (pbf.checkRunControl())
            {
              // process BIER files
              pbf.processDroppedFiles();  
              // reset run control at completion
              pbf.resetRunControl();
            }  
           // close process log file
           pbf.closeLogFile();
        }          
    }
}
