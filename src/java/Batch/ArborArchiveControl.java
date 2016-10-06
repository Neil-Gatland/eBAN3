package Batch;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.*;
import DBUtilities.*;
import JavaUtil.*;


public class ArborArchiveControl
{
  public static final String SPLIT = "SPLIT";
  public static final String CONTROLSPLIT = "CONTROLSPLIT";
  public static final String HEADERSPLIT = "HEADERSPLIT";
  public static final String HSPLIT = "HSPLIT";
  public static final String LOAD = "LOAD";
  public static final String NEWLOAD = "NEWLOAD";
  public static final String TEST = "TEST";
  public static final String REJECT = "REJECT";
  public static final String RS6000 = "RS6000";
  public static final String LOADCONTROL = "LOADCONTROL";
  public static final String FINALCONTROL = "FINALCONTROL";
  public static final String NOCONTROL = "NOCONTROL";
  private DBAccess dba;
  private StringUtil su;
  private String logDir;
  private String inDir;
  private String outDir;
  private String controlDir;
  private String rejectDir;
  private String dupDir;
  private String runTS;
  private final String DATETIMEFORMAT = "yyyyMMddHHmmss";
  private final String DATEFORMAT = "yyyyMMdd";
  private File logFile;
  private BufferedWriter logWriter;
  private String filename;
  private String outFilename;
  private final String[] COUNTRIES = {"UK", "AT", "BE", "NL", "FI", "FR", "DE",
    "IE", "IT", "LU", "PT", "ES", "CH"};
  private final String[] INVTEXTS = {"Credit Note No:", "Gutschrift-Nr.:",
    "N. nota di credito:", "Creditnota nr.:", "No facture d", "Invoice No:",
    "No facture :", "N. fattura:", "Rechnungs-Nr.:", "Factuurnr:",
    "Nº factura:"};
  private final String[] AMTTEXTS = {"Credits", "Gutschrift gesamt",
    "Crediti", "Creditsaldo", "Crédits", "This period amount",
    "Montant de cette période", "Importo questo periodo",
    "Betrag für den aktuellen Zeitraum", "Bedrag voor deze periode",
    "Total Factura"};
  private int invCount;
  private int invTextNo;
  private String invNoStart;
  private boolean invNoFound;
  private boolean invNoFound2;
  private boolean invNoFound3;
  private boolean accNoFound;
  private boolean accException;
  private boolean invDateFound;
  private String invoiceNo;
  private String accountNo;
  private String invoiceDate;
  private boolean creditNotes;
  private Vector lineArray;
  private Locale loc;

  private ArborArchiveControl(String type)
  {
    su = new StringUtil();
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    logDir = EBANProperties.getEBANProperty(EBANProperties.AALOGDIR);
    if ((type.equalsIgnoreCase(SPLIT)) || (type.equalsIgnoreCase(HSPLIT)) ||
      (type.equalsIgnoreCase(HEADERSPLIT)) || (type.equalsIgnoreCase(CONTROLSPLIT)))
    {
      dba = new DBAccess();
      //filename = fn;
      //String fName = filename.substring(0, filename.indexOf("."));
      inDir = EBANProperties.getEBANProperty(EBANProperties.AAINDIR);
      outDir = EBANProperties.getEBANProperty(EBANProperties.AAOUTDIR);
      controlDir = EBANProperties.getEBANProperty("arborcontroldir");
      logFile = new File(logDir + File.separator + "Arbor_Split_" +
        runTS + "_log.txt");
      //outFilename = fName + "_" + runTS + "_data.txt";
      outFilename = "control_data.txt";
    }
    else
    {
      dba = new DBAccess();
      if (type.equalsIgnoreCase(LOAD))
      {
        inDir = EBANProperties.getEBANProperty(EBANProperties.ALINDIR);
        outDir = EBANProperties.getEBANProperty(EBANProperties.ALOUTDIR);
        rejectDir = EBANProperties.getEBANProperty(EBANProperties.ALREJECTDIR);
        dupDir = EBANProperties.getEBANProperty("arborduplicatedir");
      }
      else if (type.equalsIgnoreCase(LOADCONTROL))
      {
        inDir = EBANProperties.getEBANProperty("arborcontroldir");
        rejectDir = EBANProperties.getEBANProperty(EBANProperties.ALREJECTDIR);
      }
      else if (type.equalsIgnoreCase(NOCONTROL))
      {
        inDir = EBANProperties.getEBANProperty("noarborcontroldir");
        outDir = EBANProperties.getEBANProperty("arborcontroloutdir");
      }
      else if (type.equalsIgnoreCase(FINALCONTROL))
      {
        inDir = EBANProperties.getEBANProperty("arborcontroldir");
        outDir = EBANProperties.getEBANProperty("arborcontroloutdir");
        rejectDir = EBANProperties.getEBANProperty(EBANProperties.ALREJECTDIR);
      }
      else if (type.equalsIgnoreCase(REJECT))
      {
        inDir = EBANProperties.getEBANProperty("rejindir");
        outDir = EBANProperties.getEBANProperty("rejoutdir");
        rejectDir = EBANProperties.getEBANProperty("rejrejdir");
      }
      else if (type.equalsIgnoreCase(RS6000))
      {
        inDir = EBANProperties.getEBANProperty("rsindir");
        outDir = EBANProperties.getEBANProperty("rsoutdir");
        rejectDir = EBANProperties.getEBANProperty("rsrejdir");
      }
      else //(type.equalsIgnoreCase(NEWLOAD))
      {
        inDir = EBANProperties.getEBANProperty(EBANProperties.ANINDIR);
        outDir = EBANProperties.getEBANProperty(EBANProperties.ANOUTDIR);
        rejectDir = EBANProperties.getEBANProperty(EBANProperties.ANREJECTDIR);
        dupDir = EBANProperties.getEBANProperty("arborduplicatedir");
        controlDir = EBANProperties.getEBANProperty("arborcontroldir");
      }
      logFile = new File(logDir + File.separator + "Arbor_Load_" +
        runTS + "_log.txt");
    }
    createLogFile();
  }

  private void createLogFile()
  {
    try
    {
      logWriter = new BufferedWriter(new FileWriter(logFile));
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("Arbor Archive processing started at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4), false);
    }
    catch (Exception ex)
    {
      System.out.println("Error creating log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToLogFile(String message, boolean isError)
  {
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
    try
    {
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("Arbor Archive  processing ended at " +
        now.substring(8, 10) + ":" + now.substring(10, 12) + "." +
        now.substring(12, 14) + " on " + now.substring(6, 8) + "/" +
        now.substring(4, 6) + "/" + now.substring(0, 4), false);
      logWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void renameLogFile()
  {
    try
    {
      logFile.renameTo(new File(logFile.getAbsolutePath() + ".error"));
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private boolean test()
  {
    boolean success = false;
    try
    {
      long accountId = dba.testOraclePackage("0000000521");
      System.out.println("account Id:" + accountId);
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in loadPDFs(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean removeRejects()
  {
    boolean success = false;
    try
    {
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
    //  extract data from filename
          File fin = fileArray[i];
          String fileName = fin.getName();
System.out.println(fileName);
          // String split() method not available in this version of java
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          BufferedWriter bw = new BufferedWriter(new FileWriter(outDir +
            File.separator + fileName));
          BufferedWriter bw2 = new BufferedWriter(new FileWriter(rejectDir +
            File.separator + fileName));
          String line = buff.readLine();
          //if ((line != null) && (line.length() > 0))
          if (line != null)
          {
            boolean eof = false;
            while (!eof)
            {
              boolean reject = false;
              int startPos = 0;
              int storePos = 0;
              int tokens = 0;
              while (tokens < 12)
              {
                startPos = line.indexOf(',', startPos);
                tokens++;
                if (tokens == 5)
                {
                  storePos = startPos;
                  //System.out.println("storePos:"+storePos);
                }
                else if (tokens == 6)
                {
                  if (storePos == (startPos-1))
                  {
                    //System.out.println("is null");
                  }
                  else
                  {
                    break;
                  }
                }
                else if (tokens == 10)
                {
                  storePos = startPos;
                  //System.out.println("storePos:"+storePos);
                }
                else if (tokens == 11)
                {
                  //System.out.println("11:"+line.substring(storePos+1, startPos));
                  if (line.substring(storePos+1, startPos).equals("56"))
                  {
                    reject = true;
                  }
                }
                startPos++;
              }
//System.out.println("reject:"+reject);
              if (reject)
              {
                bw2.write(line);
                bw2.newLine();
              }
              else
              {
                bw.write(line);
                bw.newLine();
              }
              line = buff.readLine();
              if ((line == null) || (line.length() <= 0))
                eof = true;
            }
          }
          buff.close();
          bw.close();
          bw2.close();
        }
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in removeRejects(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean loadPDFs(String type)
  {
    boolean success = false;
    try
    {
    //loop round files
//System.out.println("inDir: " + inDir);
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
    //  extract data from filename
          File pdf = fileArray[i];
          String fileName = pdf.getName();
//System.out.println("fileName: " + fileName);
          // String split() method not available in this version of java
          StringTokenizer st = new StringTokenizer(fileName.substring(0,
            fileName.lastIndexOf(".")), "_");
          String[] pdfInfo = new String[st.countTokens()];
          int tCount = 0;
          while (st.hasMoreTokens())
          {
            pdfInfo[tCount] = st.nextToken();
//System.out.println("pdfInfo[" + tCount + "]: " + pdfInfo[tCount]);
            tCount++;
          }
          String status = dba.checkArborControlStatus(pdfInfo[0], pdfInfo[1]);
//System.out.println("status: " + status);
          long[] accountIds = dba.getIdsForAccount(pdfInfo[0]);
          //if (dba.accountExists(pdfInfo[0]))
/*System.out.println("accountIds[0]: " + accountIds[0]);
System.out.println("accountIds[1]: " + accountIds[1]);
System.out.println("accountIds[2]: " + accountIds[2]);*/
          if (accountIds[0] > 0)
          {
      //  load data onto db (speculative)
            String newName = null;
            String outPath = null;
            long loadRet = -1;
            if (type.equalsIgnoreCase(LOAD))
            {
              if (status.equals("Y")) //do not load, already processed
              {
                loadRet = -997;
              }
              else
              {
                newName = pdfInfo[1].startsWith("CN-")?"Arbor_Credit_Note.pdf":
                  "Arbor_Invoice.pdf";
                loadRet = dba.loadArborArchive(pdfInfo[0],
                  pdfInfo[1].replace('-','/'), new java.sql.Date(su.toDate(pdfInfo[2],
                  DATEFORMAT).getTime()), newName);
                outPath = outPath + File.separator + pdfInfo[1];
              }
            }
            else //NEWLOAD
            {
//System.out.println("NEWLOAD");
              /*if (status.equals("2")) //do not load, no control
              {
                loadRet = -999;
              }
              else */if (status.equals("3")) //do not load, control mismatch
              {
                loadRet = -998;
              }
              /*else if (status.equals("Y")) //do not load, already processed
              {
                loadRet = -997;
              }15/11/11 allow duplicates to be loaded*/
              else
              {
                boolean isSummary = pdfInfo[4].endsWith("1");
                String currency = pdfInfo[4].substring(0, pdfInfo[4].length()-1);
                while (currency.endsWith("0"))
                {
                  currency = currency.substring(0, currency.length()-1);
                }
//System.out.println("currency: " + currency);
                String newType = (pdfInfo[1].startsWith("CN-")?"Arbor_Credit_":
                  "Arbor_Invoice_") + (isSummary?"S":"D") + ".pdf";
//System.out.println("newType: " + newType);
                newName = (pdfInfo[1].startsWith("CN-")?"Arbor_Credit_":
                  "Arbor_Invoice_") + (isSummary?"S":"D") + "_" +
                  pdfInfo[1] + ".pdf";
//System.out.println("newName: " + newName);
                double invoiceAmt = (pdfInfo[3].indexOf(".") == -1)?
                  (Double.parseDouble(pdfInfo[3].substring(0, pdfInfo[3].length()-2) +
                  "." + pdfInfo[3].substring(pdfInfo[3].length()-2))):
                  Double.parseDouble(pdfInfo[3]);
//System.out.println("invoiceAmt: " + Double.toString(invoiceAmt));
                loadRet = dba.loadNewArbor(accountIds[2], accountIds[1],
                  accountIds[0], pdfInfo[1].replace('-','/'),
                  new java.sql.Date(su.toDate(pdfInfo[2], DATEFORMAT).getTime()),
                  invoiceAmt, currency, newType, newName, isSummary);
//System.out.println("loadRet: " + loadRet);
                outPath = outDir + File.separator + "Arbor" + File.separator +
                  accountIds[2] + File.separator + accountIds[1] +
                  File.separator + accountIds[0] + File.separator + loadRet;
//System.out.println("outPath: " + outPath);
              }
            }
            if (type.equalsIgnoreCase(LOAD))
            {
              if (loadRet != 0)
              {
                if (loadRet == 1)
                {
                  writeToLogFile("File " + fileName +
                    " rejected - duplicate found", true);
                }
                else if (loadRet == -997)
                {
                  writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                    "already processed", true);
                  pdf.renameTo(new File(dupDir + File.separator + fileName));
                }
               else
                {
                  writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                    dba.getMessage(), true);
                 }
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else
              {
        //  move file
                File newDir = new File(outPath);
                if (!newDir.exists())
                {
                  newDir.mkdirs();
                }
                if (!pdf.renameTo(new File(outPath + File.separator + newName)))
                {
                  writeToLogFile("Unable to move file " + fileName, false);
                }
              }
            }
            else //NEWLOAD
            {
//System.out.println("NEWLOAD2");
              /*if (loadRet == -999)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                  "no control record", true);
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else */if (loadRet == -998)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                  "control mismatch", true);
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else if (loadRet == -997)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                  "already processed", true);
                pdf.renameTo(new File(dupDir + File.separator + fileName));
              }
              else if (loadRet == -99)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                  "duplicate invoice found", true);
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else if (loadRet == -98)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): " +
                  "duplicate attachment record found", true);
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else if (loadRet < 0)
              {
                writeToLogFile("File " + fileName + " rejected - error in loadPDFs(): return code " +
                  loadRet + ", message: " + dba.getMessage(), true);
                pdf.renameTo(new File(rejectDir + File.separator + fileName));
              }
              else
              {
        //  move file
//System.out.println("NEWLOAD3");
                File newDir = new File(outPath);
                if (!newDir.exists())
                {
//System.out.println("NEWLOAD4");
                  newDir.mkdirs();
//System.out.println("NEWLOAD5");
                }
                File destination = new File(outPath + File.separator + newName);
                if (destination.exists())
                {
                  destination.delete();
                }
                if (pdf.renameTo(destination))
                {
//System.out.println("NEWLOAD6");
                  if (!type.equalsIgnoreCase(LOAD))
                  {
//System.out.println("NEWLOAD7");
                    if ((status.equals("Y")) || (status.equals("0")) ||
                      (status.equals("1")))
                      dba.updateArborControl(pdfInfo[1], "Y", true);
//System.out.println("NEWLOAD8");
                  }
                }
                else
                {
//System.out.println("NEWLOAD9");
                  writeToLogFile("Unable to move file " + fileName, false);
//System.out.println("NEWLOAD10");
                }
              }
            }
          }
          else
          {
//System.out.println("NEWLOAD11");
            writeToLogFile("File " + fileName + " rejected - account not found(): " +
              pdfInfo[0], false);
            dba.updateArborControl(pdfInfo[1], "1", false);
            if (!fileName.endsWith("2.pdf"))
            {
              //only need to insert for the first one
              dba.insertArborLoad(pdfInfo[0]);
            }
            pdf.renameTo(new File(rejectDir + File.separator + fileName));
          }
    //next
        }
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in loadPDFs() (exception): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean rs6000()
  {
    boolean success = false;
    boolean foundCWC = false;
    boolean foundFeature = false;
    String line = null;
    String sequenceNo = null;
    String renewDate = null;
    String siteId = null;
    int laCount = 0;
    int lineCount = 0;
    BufferedReader buff = null;
    BufferedWriter bw = null;
    try
    {
    	buff = new BufferedReader(new FileReader(inDir));
    	bw = new BufferedWriter(new FileWriter(outDir));
      //File inFile = new File(inDir);
      line = buff.readLine();
      //lineArray = new Vector(11);
      //lineArray.add(0, line);
      laCount++;
      while (line != null)
			{
//System.out.println("line:"+line);
				if (line.startsWith("SOMERSET/CWC"))
				{
					foundCWC = true;
					int startPos = line.indexOf(",") + 1;
					int endPos = line.indexOf(",", startPos);
					sequenceNo = line.substring(startPos, endPos).replace('O','0');
					lineCount = 0;
		      //lineArray = new Vector(11);
		      //lineArray.add(0, line);
					renewDate = "unknown";
				}
				if (foundCWC && !foundFeature && (lineCount == 1))
				{
					siteId = line.substring(0, line.indexOf(","));
				}
				if (line.startsWith("Renew Date"))
				{
					int startPos = line.indexOf("-") + 2;
					int endPos = line.indexOf(",");
					renewDate = line.substring(startPos, endPos);
				}
				if (line.startsWith("Feature Number"))
				{
					foundFeature = true;
/*System.out.println("array0:"+(String)lineArray.get(0));
System.out.println("array1:"+(String)lineArray.get(1));
System.out.println("array2:"+(String)lineArray.get(2));
System.out.println("array3:"+(String)lineArray.get(3));
System.out.println("array4:"+(String)lineArray.get(4));
					renewDate = "unknown";

					for (Enumeration en = lineArray.elements(); en.hasMoreElements(); )
					{
						String oldLine = (String)en.nextElement();
System.out.println("oldLine:"+oldLine);
						if (oldLine == null)
						{
							break;
						}
//System.out.println("oldLine:"+oldLine);
						if (oldLine.startsWith("Renew Date"))
						{
							int startPos = oldLine.indexOf("-") + 2;
							int endPos = oldLine.indexOf(",");
							renewDate = oldLine.substring(startPos, endPos);
							break;
						}
					}*/
					lineCount = 0;
				}
				if (foundCWC && foundFeature && (lineCount > 0))
				{
					if ((line.length() > 0) && (line.indexOf(",") > 0))
					{
                                                String processedLine = line;
                                                if (line.indexOf("\"") > 0)
                                                {
                                                  int sp1 = line.indexOf("\"") + 1;
                                                  int sp2 = line.indexOf("\"", sp1);
                                                  processedLine = line.substring(0, sp1 - 1) +
                                                    line.substring(sp1, sp2).replace(',', ' ') +
                                                    line.substring(sp2 + 1);
                                                }
						int pos1 = processedLine.indexOf(",") + 1;
						int pos2 = processedLine.indexOf(",", pos1) + 1;
						int pos3 = processedLine.indexOf(",", pos2) + 1;
						int pos4 = processedLine.indexOf(",", pos3) + 1;
						int pos5 = processedLine.indexOf(",", pos4);
						if (pos5 == -1)
						{
							pos5 = line.length();
						}
						String lineout = siteId + "," + sequenceNo + "," + renewDate + "," + processedLine.substring(0, pos5);
            bw.write(lineout);
            bw.newLine();
//System.out.println("lineout:"+lineout);
					}
					else
					{
						foundCWC = false;
						foundFeature = false;
					}
				}
				lineCount++;
        line = buff.readLine();
        /*lineArray.add(0, line);
        if (lineArray.size() > 12)
        {
          lineArray.remove(12);
        }*/

			}
			buff.close();
			bw.close();

			success = true;
		}
		catch (Exception ex)
		{
System.out.println("exception:"+ex.getMessage());
		}
		finally
		{
			return success;
		}
	}




  private boolean extractDataFromHtml()
  {
    boolean success = false;
    String line = null;
    int laCount = 0;
    try
    {
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
          lineArray = new Vector(11);
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          String id = fileArray[i].getName().substring(0, fileArray[i].getName().lastIndexOf("."));
//System.out.println(id);
          BufferedWriter bw = new BufferedWriter(new FileWriter(outDir +
            File.separator + id + "_" + outFilename));
          line = buff.readLine();
          lineArray.add(0, line);
          laCount++;
          //if ((line != null) && (line.length() > 0))
          if (line != null)
          {
            boolean eof = false;
            int invTextCount = -1;
            int pageNo = 0;
            int startPageNo = 0;
            int prevEndPageNo = 0;
            int prevStartPageNo = 0;
            boolean invTextFound = false;
            invNoFound = false;
            invNoFound2 = false;
            invNoFound3 = false;
            accNoFound = false;
            invDateFound = false;
            boolean fileTypeFound = false;
            creditNotes = false;
            invCount = 0;
            invTextNo = 0;
            invoiceNo = null;
            accountNo = null;
            invoiceDate = null;
            String invoiceAmt = null;
            String currency = null;
            String invText = "Invoice No:";
            String amtText = "This period amount";
            invNoStart = "(";
            ArrayList invTexts = new ArrayList();
            ArrayList amtTexts = new ArrayList();
            while (!eof)
            {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println(line);
              //determine file type
              int pp = line.indexOf("<title>pg_");
              if (pp > -1)
              {
//System.out.println(line);
                pp += 10;
                pageNo = Integer.parseInt(line.substring(pp, line.indexOf("<", pp)));
              }
              boolean page1 = (line.indexOf("> 1/") > -1) ||
                (line.indexOf(">1/") > -1) || (line.equals("</html>"));
              if (page1)
              {
//System.out.println("page1 found");
                prevStartPageNo = startPageNo;
                startPageNo = pageNo;
                prevEndPageNo = startPageNo - 1;
                if (prevEndPageNo > 2) //may need to change
                {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("1");
                  if (invoiceNo == null)
                  {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("2");
System.out.println("Could not find invoice number for item in file "+prevEndPageNo);
                    writeToLogFile("Could not find invoice number for item in file " +
                      fileArray[i].getName(), true);
                  }
                  else
                  {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("3");
                  //int actualStart = prevStartPageNo - 1;
                  //int actualEnd = prevEndPageNo - 1;
                  int actualStart = prevStartPageNo;
                  int actualEnd = prevEndPageNo;
//actualStart+=2;
//actualEnd+=2;
/*System.out.println("creditNotes:"+creditNotes);
System.out.println("accountNo:"+accountNo);
System.out.println("invoiceNo:"+invoiceNo);
System.out.println("actualStart :"+actualStart );
System.out.println("actualEnd:"+actualEnd);
System.out.println("invoiceDate:"+invoiceDate);
System.out.println("invoiceAmt:"+invoiceAmt);
System.out.println("currency:"+currency);
System.out.println(accountNo + "|" + invoiceNo + "|" + actualStart +
                    "-" + actualEnd + "|" + invoiceDate + "|" +
                    invoiceAmt + "|" + currency);*/
                  //write record
                  bw.write(accountNo + "|" + invoiceNo + "|" + actualStart +
                    "-" + actualEnd + "|" + invoiceDate + "|" +
                    invoiceAmt + "|" + currency);
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("4");
                  bw.newLine();
                  }
                  invTextFound = false;
                  invNoFound = false;
                  invNoFound2 = false;
                  invNoFound3 = false;
                  accNoFound = false;
                  invDateFound = false;
                  fileTypeFound = false;
                }
              }
              //find Invoice No
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("5");
              if (!fileTypeFound)
              {
                invTextCount = -1;
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("6");
                //if (page1)
                //{
                  for (Iterator it = lineArray.iterator(); it.hasNext(); )
                  {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("7");
                    invTextCount++;
                    String thisLine = (String)it.next();
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println(thisLine);
                    for (int j=0; j<INVTEXTS.length; j++)
                    {
                      if (thisLine.indexOf(INVTEXTS[j]) > -1)
                      {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("7a:" + invTextCount);
                        creditNotes = j < 5;
                        invText = INVTEXTS[j];
                        amtText = AMTTEXTS[j];
                        invNoStart = creditNotes?"CN/":"";
                        switch (j)
                        {
                          case 1:
                          case 8:
                            loc = Locale.GERMAN;
                            break;
                          case 2:
                          case 7:
                            loc = Locale.ITALIAN;
                            break;
                          case 3:
                          case 9:
                            loc = new Locale("nl", "NL");
                            break;
                          case 4:
                          case 6:
                            loc = Locale.FRENCH;
                            break;
                          case 10:
                            loc = new Locale("es", "ES");
                            break;
                          default:
                            loc = Locale.UK;
                        }
                        fileTypeFound = true;
                        break;
                      }
                    }
                    if (fileTypeFound)
                    {
                      break;
                    }
                  }
                //}
              }
              else
              {
                if ((!invNoFound) || (!accNoFound) || (!invDateFound))
                {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("8");
                  if (invTextCount > 0)
                  {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("9");
                    findType4(line, creditNotes);
                  }
                  else
                  {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("10");
                    findType5(lineArray, creditNotes);
                  }
                }
              }
              //find inv amt
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("11");
              /*boolean amtTextFound = false;
              for (Iterator it = amtTexts.iterator(); it.hasNext(); )
              {
                String aText = (String)it.next();
                amtTextFound = line.indexOf(aText) > -1;
                if (amtTextFound) break;
              }*/
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("12");
              if (line.indexOf(amtText) > -1)
              {
//if (id.equals("CWEINV000000000001.01_UK_WC1"))
//System.out.println("13");
                /*String oldLine = creditNotes?(String)lineArray.get(5):(String)lineArray.get(1);
                if (oldLine.indexOf(',') > -1)
                {
                  oldLine = su.removeChar(oldLine, ',');
                }
                if (oldLine.indexOf('.') > -1)
                {
                  oldLine = su.removeChar(oldLine, '.');
                }

                int startSpan = oldLine.indexOf("<span");
                int endField = oldLine.indexOf("</span>");
                int startField = oldLine.indexOf(">", startSpan) + 1;

                invoiceAmt = oldLine.substring(startField, endField);
                while (invoiceAmt.startsWith(" "))
                {
                  invoiceAmt = invoiceAmt.substring(1);
                }*/
                int cnCurrLine = 6;
                invoiceAmt = extractAmount(creditNotes?(String)lineArray.get(5):(String)lineArray.get(1));
                if (creditNotes)
                {
                  try
                  {
                    long test = Long.parseLong(invoiceAmt);
                  }
                  catch (NumberFormatException nfe)
                  {
                    invoiceAmt = extractAmount((String)lineArray.get(3));
                    cnCurrLine = 4;
                  }
                }

                String oldLine = creditNotes?(String)lineArray.get(cnCurrLine):(String)lineArray.get(2);
                int startSpan = oldLine.indexOf("<span");
                int endField = oldLine.indexOf("</span>");
                int startField = oldLine.indexOf(">", startSpan) + 1;
                currency = oldLine.substring(startField, endField).trim();
                /*while (currency.startsWith(" "))
                {
                  currency = currency.substring(1);
                }*/
//System.out.println("invoiceAmt:"+invoiceAmt);
//System.out.println("currency:"+currency);
              }

              line = buff.readLine();
              lineArray.add(0, line);
              if (lineArray.size() > 12)
              {
                lineArray.remove(12);
              }
              eof = line == null;
            }
          }
          buff.close();
          bw.close();
          File outF = new File(outDir + File.separator + id + "_" + outFilename);
          if (outF.length() == 0)
          {
            outF.delete();
          }
        }
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in extractDataFromHtml(): " + ex.getMessage() +
      " : line: " + line, true);
    }
    finally
    {
      return success;
    }
  }

  private String extractAmount(String oldLine)
  {
    String invoiceAmt = null;
    if (oldLine.indexOf(',') > -1)
    {
      oldLine = su.removeChar(oldLine, ',');
    }
    if (oldLine.indexOf('.') > -1)
    {
      oldLine = su.removeChar(oldLine, '.');
    }

    int startSpan = oldLine.indexOf("<span");
    int endField = oldLine.indexOf("</span>");
    int startField = oldLine.indexOf(">", startSpan) + 1;

    invoiceAmt = oldLine.substring(startField, endField).trim();
    /*while (invoiceAmt.startsWith(" "))
    {
      invoiceAmt = invoiceAmt.substring(1);
    }*/
    return invoiceAmt;
  }

  private boolean processControlFile()
  {
    boolean success = false;
    String line = null;
    try
    {
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
          if (fileArray[i].isFile())
          {
            BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
            line = buff.readLine();
            while ((line != null) && (!line.equals("")))
            {
              // String split() method not available in this version of java
              StringTokenizer st = new StringTokenizer(line, "|");
              String[] controlInfo = new String[st.countTokens()];
              int tCount = 0;
              while (st.hasMoreTokens())
              {
                controlInfo[tCount] = st.nextToken();
                tCount++;
              }
              dba.insertArborControl(controlInfo[0], controlInfo[1], controlInfo[2],
                controlInfo[3], controlInfo[4], controlInfo[5], controlInfo[6],
                controlInfo[7], fileArray[i].getName());
              //controlInfo[6]
              line = buff.readLine();
            }
            buff.close();
          }
        }
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in processControlFile(): " + ex.getMessage() +
      " : line: " + line, true);
    }
    finally
    {
      return success;
    }
  }

  private boolean finalizeControlFile()
  {
    boolean success = false;
    String line = null;
    BufferedWriter bw = null;
    BufferedReader buff = null;
    try
    {
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
          if (fileArray[i].isFile())
          {
            /*BufferedWriter*/ bw = new BufferedWriter(new FileWriter(outDir +
              File.separator + fileArray[i].getName()));
            /*BufferedReader*/ buff = new BufferedReader(new FileReader(fileArray[i]));
            line = buff.readLine();
            while ((line != null) && (!line.equals("")))
            {
              // String split() method not available in this version of java
              StringTokenizer st = new StringTokenizer(line, "|");
              String[] controlInfo = new String[st.countTokens()];
              int tCount = 0;
              while (st.hasMoreTokens())
              {
                controlInfo[tCount] = st.nextToken();
                tCount++;
              }
              String status = dba.checkArborControlStatus(controlInfo[0],
                controlInfo[2], fileArray[i].getName());
              if (status.equals("not found"))
              {
                status = dba.checkArborControlStatus(controlInfo[0],
                  controlInfo[2]);
              }
              if (status.equals("X"))
              {
                dba.updateArborControlProcessed(controlInfo[0],
                  controlInfo[2], fileArray[i].getName(), status, true);
              }
              bw.write(line + status);
              bw.newLine();
              line = buff.readLine();
            }
            //
            /*buff.close();
            bw.close();*/
          }
        }
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in finalizeControlFile(): " + ex.getMessage() +
      " : line: " + line, true);
    }
    finally
    {
      try
      {
        if (buff != null)
        {
          buff.close();
        }
        if (bw != null)
        {
          bw.close();
        }
      }
      catch (IOException ex)
      {
        writeToLogFile("Error in finalizeControlFile(): " + ex.getMessage(), true);
      }
      return success;
    }
  }

  private boolean extractData(boolean insertControlRecord)
  {
    boolean success = false;
    String line = null;
    int laCount = 0;
    try
    {
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)

        {
          lineArray = new Vector(11);
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          String id = fileArray[i].getName().substring(0, fileArray[i].getName().lastIndexOf("."));
//System.out.println(id);
          BufferedWriter bw = new BufferedWriter(new FileWriter(outDir +
            File.separator + id + "_" + outFilename));
          line = buff.readLine();
          lineArray.add(0, line);
          laCount++;
          //if ((line != null) && (line.length() > 0))
          if (!line.equals("%%EOF"))
          {
            boolean eof = false;
            int startPageNo = 0;
            int prevEndPageNo = 0;
            int prevStartPageNo = 0;
            boolean invTextFound = false;
            invNoFound = false;
            invNoFound2 = false;
            invNoFound3 = false;
            invDateFound = false;
            accException = false;
            boolean fileTypeFound = false;
            creditNotes = false;
            invCount = 0;
            invTextNo = 0;
            invoiceNo = null;
            accountNo = null;
            invoiceDate = null;
            String invoiceAmt = null;
            String currency = null;
            String invText = "Invoice No:";
            String amtText = "This period amount";
            invNoStart = "(";
            ArrayList invTexts = new ArrayList();
            ArrayList amtTexts = new ArrayList();
            ArrayList periodTexts = new ArrayList();
            boolean hasPeriod = false;
            while (!eof)
            {
              //determine file type
              if (!fileTypeFound)
              {
                if (line.indexOf("Postal Weightclass:") > -1)
                {
                  amtTexts.clear();
                  invTexts.clear();
                  creditNotes = (((String)lineArray.get(2)).indexOf("Credit Notes") > -1);
                  if (creditNotes)
                  {
//System.out.println("Credit Note");
                    invNoStart = "(CN/";
                    amtTexts.add("Credits");
                    amtTexts.add("Gutschrift gesamt");
                    amtTexts.add("Crediti");
                    amtTexts.add("Creditsaldo");
                    amtTexts.add("Crédits");
                    amtTexts.add("Créditos");
                    invTexts.add("Credit Note No:");
                    invTexts.add("Gutschrift-Nr.:");
                    invTexts.add("N. nota di credito:");
                    invTexts.add("Creditnota nr.:");
                    invTexts.add("No facture d");
                    invTexts.add("Nº extracto crédito:");
                  }
                  else
                  {
//System.out.println("Invoice");
                    invNoStart = "(";
                    amtTexts.add("This period amount");
                    amtTexts.add("Montant de cette période");
                    amtTexts.add("Importo questo periodo");
                    amtTexts.add("Betrag für den aktuellen Zeitraum");
                    amtTexts.add("Bedrag voor deze periode");
                    amtTexts.add("Total Factura");
                    invTexts.add("Invoice No:");
                    invTexts.add("No facture :");
                    invTexts.add("N. fattura:");
                    invTexts.add("Rechnungs-Nr.:");
                    invTexts.add("Factuurnr:");
                    invTexts.add("Nº factura:");
                  }
                  fileTypeFound = true;
                  periodTexts.add("Période facture :");
                  periodTexts.add("Rechnungszeitr.:");
                  periodTexts.add("Invoice Period:");
                  periodTexts.add("Periodo factura:");
                }
              }
              //find Page1
              int page1 = line.indexOf("%%Page: 1 ");
              if (page1 > -1)
              {
                prevStartPageNo = startPageNo;
                startPageNo = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
                prevEndPageNo = startPageNo - 1;
                if (prevEndPageNo > 2)
                {
                  if (invoiceNo == null)
                  {
                    writeToLogFile("Could not find invoice number for item in file " +
                      fileArray[i].getName(), true);
                  }
                  else
                  {
                    int actualStart = prevStartPageNo - 1;
                    int actualEnd = prevEndPageNo - 1;
                    //write record
                    for (int j=0; j<COUNTRIES.length; j++)
                    {
                      if (accountNo.indexOf(COUNTRIES[j]) == 0)
                      {
                        accountNo = accountNo.substring(2);
                        break;
                      }
                    }
/*System.out.println(accountNo + "|" + invoiceNo + "|" + actualStart +
"-" + actualEnd + "|" + reformatDate(invoiceDate, creditNotes) + "|" +
invoiceAmt + "|" + currency);
System.out.println("x");
System.out.println("name:"+fileArray[i].getName());
System.out.println("y");*/
                    if (insertControlRecord)
                    {
                      dba.insertArborControl(accountNo, invoiceNo,
                        reformatDate(invoiceDate, creditNotes), invoiceAmt,
                        currency, fileArray[i].getName());
                      bw.write(accountNo + "|" + invoiceNo + "|" + actualStart +
                        "-" + actualEnd + "|" + reformatDate(invoiceDate, creditNotes) + "|" +
                        invoiceAmt + "|" + currency);
                      bw.newLine();
                    }
                    else
                    {
                      String message = dba.checkArborControl(accountNo, invoiceNo,
                        reformatDate(invoiceDate, creditNotes), invoiceAmt,
                        currency, fileArray[i].getName(), "P", false);
                      String controlFileName = dba.getArborControlFileName();
                      if (controlFileName != null)
                      {
                        //if not found in Arbor_Control_Return
                        //  insert into Arbor_Control_Return
                      }
                      if (message.equals("ok"))
                      {
                        bw.write(accountNo + "|" + invoiceNo + "|" + actualStart +
                          "-" + actualEnd + "|" + reformatDate(invoiceDate, creditNotes) + "|" +
                          invoiceAmt + "|" + currency);
                        bw.newLine();
                      }
                      else
                      {
                        writeToLogFile("Error in extractData(): " + message, true);
                      }
                    }
                  }
                  invTextFound = false;
                  invNoFound = false;
                  invNoFound2 = false;
                  invNoFound3 = false;
                  invDateFound = false;
                  hasPeriod = false;
                  //lineArray = new Vector(11);
                }
/*else
System.out.println("prevEndPageNo:"+prevEndPageNo);
*/
              }
              if (!hasPeriod)
              {
/*if (line.indexOf("Rechnungszeitr.:") > -1)
{
System.out.println(line);
for (Iterator it = periodTexts.iterator(); it.hasNext(); )
{
String pText = (String)it.next();
System.out.println(pText);
System.out.println(line.indexOf(pText));
}
}*/
                for (Iterator it = periodTexts.iterator(); it.hasNext(); )
                {
                  String pText = (String)it.next();
                  hasPeriod = line.indexOf(pText) > -1;
/*if (line.indexOf("Rechnungszeitr.:") > -1)
{
System.out.println(pText);
System.out.println(line.indexOf(pText));
}*/
/*if (hasPeriod)
System.out.println("hasPeriod");*/
                  if (hasPeriod)
                    break;
                }
              }
              //find Invoice No
              if (!invTextFound)
              {
                invTextNo = 0;
                for (Iterator it = invTexts.iterator(); it.hasNext(); )
                {
                  String iText = (String)it.next();
                  invTextFound = line.indexOf(iText) > -1;
                  if (invTextFound)
                  {
//System.out.println("line:"+line);
//System.out.println(invTextNo);
                    break;
                  }
                  invTextNo++;
                }
                invCount = 0;
              }
              else
              {
                invCount++;
if ((!invDateFound) || (accException))
{
                if (creditNotes)
                {
                  switch (invTextNo)
                  {
                    case 1:
                      findType6(line);
                      break;
                    case 2:
                      findType2(line);
                      break;
                    default:
                      findType1(line);
                  }
                }
                else
                {
//System.out.println(line);
                  if (hasPeriod)
                  {
//System.out.println("findType3 1");
                    findType3(line, 6);
//System.out.println("after findType3 1");
                    //findType1(line);
                  }
                  else
                  {
                    switch (invTextNo)
                    {
                      case 1:
//System.out.println("findType3 2");
                        //findType3(line, 5);
                        //break;
                      default:
                        findType1(line);
                    }
                  }
                }
}
              }
              //find inv amt
//if (invNoFound3)
//System.out.println("find inv amt");
              boolean amtTextFound = false;
              for (Iterator it = amtTexts.iterator(); it.hasNext(); )
              {
                String aText = (String)it.next();
                amtTextFound = line.indexOf(aText) > -1;
                if (amtTextFound)
                {
//System.out.println("amtTextFound:"+line);
                  break;
                }
              }
              if (amtTextFound)
              {
                String oldLine = creditNotes?(String)lineArray.get(5):(String)lineArray.get(1);
//System.out.println("oldLine:"+oldLine);
                if (oldLine.indexOf(',') > -1)
                {
                  oldLine = su.removeChar(oldLine, ',');
                }
                if (oldLine.indexOf('.') > -1)
                {
                  oldLine = su.removeChar(oldLine, '.');
                }
                invoiceAmt = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
//System.out.println("invoiceAmt:"+invoiceAmt);
                try
                {
                  long test = Long.parseLong(invoiceAmt);
                  oldLine = creditNotes?(String)lineArray.get(6):(String)lineArray.get(2);
                }
                catch (NumberFormatException nfe)
                {
                  // this problem for CN only so far
                  oldLine = /*creditNotes?(String)lineArray.get(5):*/(String)lineArray.get(3);
                  if (oldLine.indexOf(',') > -1)
                  {
                    oldLine = su.removeChar(oldLine, ',');
                  }
                  if (oldLine.indexOf('.') > -1)
                  {
                    oldLine = su.removeChar(oldLine, '.');
                  }
                  invoiceAmt = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
                  oldLine = /*creditNotes?(String)lineArray.get(6):*/(String)lineArray.get(4);
                }
                currency = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
/*System.out.println("currency:"+currency);
System.out.println("invoiceNo:"+invoiceNo);
System.out.println("accountNo:"+accountNo);
System.out.println("invoiceDate:"+invoiceDate);*/
              }

              line = buff.readLine();
              lineArray.add(0, line);
              if (lineArray.size() > 12)
              {
                lineArray.remove(12);
              }
              eof = line.equals("%%EOF");
            }
          }
          buff.close();
          bw.close();
          File outF = new File(outDir + File.separator + id + "_" + outFilename);
          if (outF.length() == 0)
          {
            outF.delete();
          }
        }
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in extractData(): " + ex.getMessage() +
      " : line: " + line, true);
    }
    finally
    {
      return success;
    }
  }

  private void findType1(String line)
    throws Exception
  {
//if (invCount == 3)
//System.out.println("3line:"+line);
    if (invCount == 3/*4*/)
    {
//System.out.println("3line:"+line);
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
          (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
        {
          invNoFound = true;
//System.out.println("invNoFound:"+invNoFound);
          break;
        }
      }
      if (invNoFound)
      {
        invoiceNo = line.substring(line.indexOf("(") + 1,
          line.indexOf(")")).replace('/', '-');
      }
      else
      {
        boolean found = false;
        String oldLine = (String)lineArray.get(6/*8*/);
//System.out.println("line:"+line);
//System.out.println("oldLine:"+oldLine);
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if ((oldLine.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
            (oldLine.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
          {
            found = true;
            invNoFound3 = true;
            break;
          }
        }
        if (found)
        {
          invoiceNo = oldLine.substring(oldLine.indexOf("(") + 1,
            oldLine.indexOf(")")).replace('/', '-');
          oldLine = (String)lineArray.get(7/*9*/);
          accountNo = oldLine.substring(oldLine.indexOf("(") + 1,
            oldLine.indexOf(")"));
          for (int j=0; j<COUNTRIES.length; j++)
          {
            if (accountNo.indexOf(COUNTRIES[j]) == 0)
            {
              accountNo = accountNo.substring(2);
              break;
            }
          }
          int dateNo = creditNotes?5/*7*/:9/*10*/;
          oldLine = (String)lineArray.get(dateNo);
          invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1,
            oldLine.indexOf(")"));
          if (invoiceDate.equals("Invoice"))
          {
            for (Iterator itl = lineArray.iterator(); itl.hasNext(); )
            {
              String searchLine = (String)itl.next();
              boolean hasCustRef = searchLine.indexOf("Customer Ref") > -1;
              if (hasCustRef)
              {
                oldLine = (String)lineArray.get(dateNo-1);
                invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1,
                  oldLine.indexOf(")"));
                break;
              }
            }
          }
          invDateFound = true;
/*System.out.println("invoiceDate1:"+invoiceDate);
System.out.println("line:"+line);
System.out.println("oldLine:"+oldLine);*/
        }
        /*else
        {
          findType3(line);
        }*/
      }
    }
    else if (invCount == 4/*5*/)
    {
      if (invNoFound)
      {
//System.out.println("4line:"+line);
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
//System.out.println("accountNo:"+accountNo);
        accException = (accountNo.length() != 8);

      }
      else
      {
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
            (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
          {
            invNoFound2 = true;
            break;
          }
        }
        if (invNoFound2)
        {
          invoiceNo = line.substring(line.indexOf("(") + 1,
            line.indexOf(")")).replace('/', '-');
        }
      }
      /*else
      {
        findType3(line);
      }*/
    }
    else if (invCount == 5/*6*/)
    {
      if (invNoFound)
      {
        invoiceDate = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate2:"+invoiceDate);
        if (creditNotes)
        {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          try
          {
            sdf.parse(invoiceDate);
          }
          catch (java.text.ParseException pe)
          {
            String oldLine = (String)lineArray.get(3);
            invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
            invDateFound = true;
//System.out.println("invoiceDate3:"+invoiceDate);
          }
        }
        try
        {
          long accNo = Long.parseLong(accountNo);
        }
        catch (NumberFormatException nfe)
        {
          accException = true;
//System.out.println("accException:"+accountNo);
        }
      }
      else if (invNoFound2)
      {
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
      }
      /*else
      {
        findType3(line);
      }*/
    }
    else if (invCount == 6/*7*/)
    {
      if (accException)
      {
//System.out.println("6 line:"+line);
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            accException = false;
            break;
          }
        }
      }
//System.out.println("6:");
      if (invoiceNo != null)
      {
//System.out.println("6a:");
        if (invNoFound2)
        {
          invoiceDate = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
          invDateFound = true;
//System.out.println("invoiceDate4:"+invoiceDate);
        }
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
        try
        {
//System.out.println("try account no");
          long accNo = Long.parseLong(accountNo);
        }
        catch (NumberFormatException nfe)
        {
          accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
          for (int j=0; j<COUNTRIES.length; j++)
          {
            if (accountNo.indexOf(COUNTRIES[j]) == 0)
            {
              accountNo = accountNo.substring(2);
              break;
            }
          }
          if (!accException)
          {
            String oldLine = (String)lineArray.get(4);
            invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
            invDateFound = true;
          }
//System.out.println("invoiceDate5:"+invoiceDate);
        }
      }
    }
  }

  private void findType2(String line)
  {
//if (!invNoFound)
//{
/*if (invCount == 3)
{
System.out.println(line);
}*/
    if (invCount == 4)
    {
//System.out.println(line);
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
          (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
        {
//System.out.println(line);
          invNoFound = true;
          break;
        }
      }
      if (invNoFound)
      {
        invoiceNo = line.substring(line.indexOf("(") + 1,
          line.indexOf(")")).replace('/', '-');
      }
      else
      {
        String oldLine = (String)lineArray.get(8);
        invoiceNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")")).replace('/', '-');
        oldLine = (String)lineArray.get(9);
        accountNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
        int dateNo = creditNotes?7:10;
        oldLine = (String)lineArray.get(dateNo);
        invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate6:"+invoiceDate);
      }
    }
    else if (invCount == 6)
    {
      if (invNoFound)
      {
        String oldLine = (String)lineArray.get(3);
        invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate7:"+invoiceDate);
      }
    }
    else if (invCount == 7)
    {
      if (invNoFound)
      {
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
      }
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if (accountNo.indexOf(COUNTRIES[j]) == 0)
        {
          accountNo = accountNo.substring(2);
          break;
        }
      }
    }
//}
  }

  private void findType3(String line, int start)
  {
//if (!invNoFound)
//{
System.out.println("invCount:"+invCount);
    if (invCount == start)
    {
//System.out.println(line+start);
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
          (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
        {
//System.out.println(line);
          invNoFound = true;
          break;
        }
      }
      if (invNoFound)
      {
        invoiceNo = line.substring(line.indexOf("(") + 1,
          line.indexOf(")")).replace('/', '-');
      }
      else
      {
        String oldLine = (String)lineArray.get(9/*8*/);
//System.out.println("oldLine:"+oldLine);
        invoiceNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")")).replace('/', '-');
//System.out.println("invoiceNo:"+invoiceNo);
        oldLine = (String)lineArray.get(10/*9*/);
        accountNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
//System.out.println("accountNo:"+accountNo);
        int dateNo = creditNotes?7:11/*10*/;
        oldLine = (String)lineArray.get(dateNo);
//System.out.println("oldLine:"+oldLine);
        invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate8:"+invoiceDate);
      }
    }
    else if (invCount == (start+1))
    {
System.out.println(line);
System.out.println("find3 1:"+accountNo);
      if (invNoFound)
      {
//System.out.println("invNoFound");
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
      }
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if (accountNo.indexOf(COUNTRIES[j]) == 0)
        {
          accountNo = accountNo.substring(2);
          break;
        }
      }
      /*}
      else
      {
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
            (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
          {
            invNoFound2 = true;
            break;
          }
        }
        if (invNoFound2)
        {
          invoiceNo = line.substring(line.indexOf("(") + 1,
            line.indexOf(")")).replace('/', '-');
        }
      }*/
    }
    else if (invCount == (start+2))
    {
System.out.println("find3 2");
System.out.println(line);
      if (invNoFound)
      {
        invoiceDate = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate9:"+invoiceDate);
      }
    }
      /*else if (invNoFound2)
      {
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
      }
    }
    else if (invCount == 8)
    {
      if (invoiceNo != null)
      {
        if (invNoFound2)
        {
          invoiceDate = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        }
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (accountNo.indexOf(COUNTRIES[j]) == 0)
          {
            accountNo = accountNo.substring(2);
            break;
          }
        }
        try
        {
          long accNo = Long.parseLong(accountNo);
        }
        catch (NumberFormatException nfe)
        {
          accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
          for (int j=0; j<COUNTRIES.length; j++)
          {
            if (accountNo.indexOf(COUNTRIES[j]) == 0)
            {
              accountNo = accountNo.substring(2);
              break;
            }
          }
          String oldLine = (String)lineArray.get(4);
          invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
        }
      }
    }*/
//}
  }

  private void findType4(String line, boolean creditNotes)
    throws Exception
  {
//System.out.println("find1");
    if (!invNoFound)
    {
      int iStart = -1;
      int iEnd = -1;
      for (int j=0; j<COUNTRIES.length; j++)
      {
//System.out.println("find2");
        if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
          (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
        {
          invNoFound = true;
          iStart = line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1?
            line.indexOf(invNoStart + COUNTRIES[j] + "10/"):
            line.indexOf(invNoStart + COUNTRIES[j] + "58/");
          iEnd = line.indexOf("</span>", iStart);
//System.out.println("find4");
          break;
        }
      }
//System.out.println("find5");
     if (invNoFound)
      {
        invoiceNo = line.substring(iStart, iEnd).replace('/', '-');
//System.out.println("invNoFound:"+invoiceNo);
        if ((creditNotes) && (!invDateFound))
        {
          String oldLine = (String)lineArray.get(1);
          int startSpan = oldLine.indexOf("<span");
          if (startSpan > -1)
          {
            int endField = oldLine.indexOf("</span>");
            int startField = oldLine.indexOf(">", startSpan) + 1;
            String field = oldLine.substring(startField, endField);
//System.out.println("field:"+field);
            invoiceDate = isDate(field, creditNotes);
            if (!invoiceDate.equals("fail"))
            {
              invDateFound = true;
//System.out.println("invDateFound3:"+invoiceDate);
            }
          }
        }
      }
    }
    else if (!accNoFound)
    {
//System.out.println("find6");
      int startSpan = line.indexOf("<span");
      if (startSpan > -1)
      {
        int endField = line.indexOf("</span>");
        int startField = line.indexOf(">", startSpan) + 1;
        String field = line.substring(startField, endField).trim();
        /*while (field.startsWith(" "))
        {
          field = line.substring(++startField, endField);
        }*/
        for (int j=0; j<COUNTRIES.length; j++)
        {
          if (field.indexOf(COUNTRIES[j]) == 0)
          {
            field = field.substring(2);
            break;
          }
        }
//System.out.println("field:"+field);
        if (field.length() < 8)
        {
          return;
        }
        try
        {
          long accNo = Long.parseLong(field);
          accountNo = field;
          for (int j=0; j<COUNTRIES.length; j++)
          {
            if (accountNo.indexOf(COUNTRIES[j]) == 0)
            {
              accountNo = accountNo.substring(2);
              break;
            }
          }
          accNoFound = true;
//System.out.println("accNoFound:"+accountNo);
        }
        catch (NumberFormatException nfe)
        {
          if (!invDateFound)
          {
            invoiceDate = isDate(field, creditNotes);
            if (!invoiceDate.equals("fail"))
            {
              invDateFound = true;
//System.out.println("invDateFound1:"+invoiceDate);
            }
          }
        }
      }
    }
    else if (!invDateFound)
    {
//System.out.println("find7:"+line);
      int startSpan = line.indexOf("<span");
      if (startSpan > -1)
      {
        int endField = line.indexOf("</span>");
        int startField = line.indexOf(">", startSpan) + 1;
        String field = line.substring(startField, endField);
//System.out.println("field:"+field);
        invoiceDate = isDate(field, creditNotes);
        if (!invoiceDate.equals("fail"))
        {
          invDateFound = true;
//System.out.println("invDateFound2:"+invoiceDate);
        }
      }
//System.out.println("find8");
    }
  }

  private void findType5(Vector lineArray, boolean creditNotes)
    throws Exception
  {
    for (Iterator it = lineArray.iterator(); it.hasNext(); )
    {
      findType4((String)it.next(), creditNotes);
    }
  }

  private void findType6(String line)
  {
//if (!invNoFound)
//{
/*if (invCount == 3)
{
System.out.println(line);
}*/
    if (invCount == 3)
    {
//System.out.println(line);
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if ((line.indexOf(invNoStart + COUNTRIES[j] + "10/") > -1) ||
          (line.indexOf(invNoStart + COUNTRIES[j] + "58/") > -1))
        {
//System.out.println(line);
          invNoFound = true;
          break;
        }
      }
      if (invNoFound)
      {
        invoiceNo = line.substring(line.indexOf("(") + 1,
          line.indexOf(")")).replace('/', '-');
//System.out.println("invoiceNo:"+invoiceNo);
      }
      /*else
      {
        String oldLine = (String)lineArray.get(8);
        invoiceNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")")).replace('/', '-');
        oldLine = (String)lineArray.get(9);
        accountNo = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        int dateNo = creditNotes?7:10;
        oldLine = (String)lineArray.get(dateNo);
        invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1,
          oldLine.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate6:"+invoiceDate);
      }*/
    }
    /*else if (invCount == 6)
    {
      if (invNoFound)
      {
        String oldLine = (String)lineArray.get(3);
        invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
        invDateFound = true;
//System.out.println("invoiceDate7:"+invoiceDate);
      }
    }*/
    else if (invCount == 6)
    {
      if (invNoFound)
      {
        accountNo = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
      }
      for (int j=0; j<COUNTRIES.length; j++)
      {
        if (accountNo.indexOf(COUNTRIES[j]) == 0)
        {
          accountNo = accountNo.substring(2);
          break;
        }
      }
      String oldLine = (String)lineArray.get(4);
      invoiceDate = oldLine.substring(oldLine.indexOf("(") + 1, oldLine.indexOf(")"));
      invDateFound = true;
    }
//}
  }

  private String reformatDate(String dateIn, boolean creditNotes)
    throws Exception
  {
//System.out.println(dateIn);
    String dd = null;
    try
    {
      dd = dateIn.substring(0, 2);
      int testNo = Integer.parseInt(dd);
    }
    catch (NumberFormatException nex)
    {
      dd = "0" + dateIn.substring(0, 1);
    }
    if (creditNotes)
    {
      return dateIn.substring(6, 10) + dateIn.substring(3, 5) + dd;
    }
    else
    {
      String yyyy = dateIn.substring(dateIn.lastIndexOf(" ")+1);
      //Locale loc;
      String mmm = dateIn.substring(dateIn.indexOf(" ")+1, dateIn.lastIndexOf(" ")).trim();
      if ((mmm.startsWith("DÊc")) || (mmm.startsWith("Dëc")))
      {
        mmm = "december";
        loc = Locale.UK;
      }
      else if ((mmm.length() == 7) && (mmm.startsWith("F")) &&
        (mmm.endsWith("vrier")))
      //else if ((mmm.startsWith("FÊv")) || (mmm.startsWith("Fëv")) ||
      //(mmm.startsWith("F v")) || (mmm.startsWith("F×v")))
      {
        mmm = "february";
        loc = Locale.UK;
      }
      else if ((mmm.equals("Aoôt")) || (mmm.equals("Aoˆt")))
      {
        mmm = "august";
        loc = Locale.UK;
      }
      else if ((mmm.length() == 4) && (mmm.startsWith("M")) &&
        (mmm.endsWith("rz")))
      {
        mmm = "März";
        loc = Locale.GERMAN;
      }
      else if ((mmm.indexOf("de ") > -1) && (mmm.indexOf(" de") > -1))
      {
        mmm = mmm.substring(mmm.indexOf("de ")+3, mmm.indexOf(" de")).trim();
        loc = new Locale("es", "ES");
      }
      else
      {
        switch (invTextNo)
        {
          case 1:
            loc = Locale.FRENCH;
            break;
          case 2:
            loc = Locale.ITALIAN;
            break;
          case 3:
            loc = Locale.GERMAN;
            break;
          case 4:
            loc = new Locale("nl", "NL");
            break;
          case 5:
            loc = new Locale("es", "ES");
            break;
          default:
            loc = Locale.UK;
        }
      }

      return reformatDate(yyyy+mmm+dd, "yyyyMMMdd", "yyyyMMdd", loc);
    }
  }

  private String reformatDate(String DateString,String FromFormat,
    String ToFormat, Locale loc)
    throws Exception
  {
//System.out.println("reformatDate 1:"+loc.getCountry()+loc.getLanguage());
    java.util.Date uDate;
    String newDate="";
    Date date = new Date();
//System.out.println("reformatDate 2");

      SimpleDateFormat sdfinput = new SimpleDateFormat(FromFormat, loc);
      SimpleDateFormat sdfoutput = new SimpleDateFormat(ToFormat, loc);
      //try {
//System.out.println("reformatDate 3:"+DateString);

        uDate=sdfinput.parse(DateString);
//System.out.println("reformatDate 4:"+uDate);
        newDate=sdfoutput.format(uDate);
//System.out.println("reformatDate 5:"+newDate);
      //}
      //catch(java.text.ParseException pe){Message=pe.getMessage();return null;}
    return newDate;
  }

  private String isDate(String inDate, boolean creditNotes)
    throws Exception
  {

    String dateIn = inDate.trim();
    /*while (dateIn.startsWith(" "))
    {
      dateIn = dateIn.substring(1);
    }*/
//System.out.println("isDate:"+inDate);
    String newDate = "fail";
    String dateFormat = null;
    String dateToTest = null;
    String dd = null;
    String mmm = null;
    String yyyy = null;
    if (dateIn.length() < 10)
    {
      return newDate;
    }
    if (creditNotes)
    {
      if (dateIn.length() > 10)
      {
        return newDate;
      }
      dateFormat = "dd/MM/yyyy";
      dateToTest = dateIn;
    }
    else
    {
      dateFormat = "dd MMM yyyy";
      if ((dateIn.indexOf(" ") == -1) ||
        (dateIn.indexOf(" ") == dateIn.lastIndexOf(" ")))
      {
        return newDate;
      }
      try
      {
        dd = dateIn.substring(0, 2);
        int testNo = Integer.parseInt(dd);
      }
      catch (NumberFormatException nex)
      {
        dd = "0" + dateIn.substring(0, 1);
      }
      yyyy = dateIn.substring(dateIn.lastIndexOf(" ")+1);
      //Locale loc;
      mmm = dateIn.substring(dateIn.indexOf(" ")+1, dateIn.lastIndexOf(" ")).trim();
      if ((mmm.startsWith("DÊc")) || (mmm.startsWith("Dëc")))
      {
        mmm = "december";
        loc = Locale.UK;
      }
      else if ((mmm.length() == 7) && (mmm.startsWith("F")) &&
        (mmm.endsWith("vrier")))
      //else if ((mmm.startsWith("FÊv")) || (mmm.startsWith("Fëv")) ||
        //(mmm.startsWith("F v")) || (mmm.startsWith("F×v")))
      {
        mmm = "february";
        loc = Locale.UK;
      }
      else if ((mmm.equals("Aoôt")) || (mmm.equals("Aoˆt")))
      {
        mmm = "august";
        loc = Locale.UK;
      }
      else if ((mmm.length() == 4) && (mmm.startsWith("M")) &&
        (mmm.endsWith("rz")))
      {
//System.out.println("März");
        mmm = "März";
        loc = Locale.GERMAN;
      }
      else if ((mmm.indexOf("de ") > -1) && (mmm.indexOf(" de") > -1) &&
        (mmm.indexOf("de ")!=mmm.indexOf(" de")+1))
      {
        mmm = mmm.substring(mmm.indexOf("de ")+3, mmm.indexOf(" de")).trim();
        loc = new Locale("es", "ES");
//System.out.println("mmm:"+mmm+":end");
      }
      dateToTest = dd + " " + mmm + " " + yyyy;
    }
//System.out.println("date:"+dateToTest+":end");
    try
    {
//System.out.println("sdf:"+loc.getLanguage());
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, loc);
//System.out.println("sdf1");
      java.util.Date uDate = sdf.parse(dateToTest);
//java.util.Date uDate = sdf.parse(inDate);
//System.out.println("sdf2");
      if (creditNotes)
      {
        newDate = dateIn.substring(6, 10) + dateIn.substring(3, 5) +
          dateIn.substring(0, 2);
      }
      else
      {
//System.out.println("reformat");
        newDate = reformatDate(yyyy+mmm+dd, "yyyyMMMdd", "yyyyMMdd", loc);
//System.out.println("newDate:"+newDate);
      }
    }
    catch (java.text.ParseException pe)
    {
//System.out.println("ParseException:"+pe.getMessage());
    }
    return newDate;
  }

  private boolean noControlFile()
  {
    boolean success = false;
    String line = null;
    String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG",
      "SEP","OCT","NOV","DEC"};
    try
    {
//System.out.println("1");
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
//System.out.println("2");
        GregorianCalendar now = new GregorianCalendar();
        String yyyy = Integer.toString(now.get(now.YEAR));
        int nowMonth = now.get(now.MONTH) + 1;
        String mm = (nowMonth<10?"0":"") + Integer.toString(nowMonth);
        String dd = (now.get(now.DATE)<10?"0":"") + Integer.toString(now.get(now.DATE));
        String controlFileName =  "Arbor_Control_" + yyyy + mm + dd + "000000.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(outDir +
          File.separator + controlFileName));
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
//System.out.println("3");
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          line = buff.readLine();
          //while ((line != null) && (!line.equals("")))
          while (!line.equals("%%EOF"))
          {
//System.out.println(line);
            if (line.startsWith("%%XGS INDEX KEY "))
            {
//System.out.println("4");
              //extract the data
              int start = line.indexOf("ACCOUNT NO=") + 11;
              int end = line.indexOf("BILL_") - 1;
              String accountNo = line.substring(start, end);
              start = end + 14;
              end = line.indexOf("INVOICE_NO=") - 1;
              String billRefNo = line.substring(start, end);
              start = end + 12;
              end = line.indexOf("AMT", start) - 1;
              String invoiceNo = line.substring(start, end);
              start = end + 5;
              end = line.indexOf("CURR", start) - 1;
              String amt = line.substring(start, end);
              int dot = amt.indexOf('.');
              String amount = amt.substring(0, dot) + amt.substring(dot+1);
              start = end + 6;
              end = line.indexOf("DATE", start) - 1;
              String currency = line.substring(start, end);
              start = end + 6;
              end = line.indexOf("PAGE", start) - 1;
              String headerDate = line.substring(start, end);
              int mmH = Integer.parseInt(headerDate.substring(3, 5));
              String billDate = headerDate.substring(0, 2) + "-" + months[mmH-1] +
								"-" + headerDate.substring(8, 10);
              String accountName = "Unknown";
              String format = "P";
              String status = "C";

              bw.write(accountNo + "|" + accountName + "|" + invoiceNo + "|" +
                billDate + "|" + amount + "|" + currency + "|" +
                fileArray[i].getName() + "|" + format + "|" + billRefNo + "|" +
                status);
              bw.newLine();
              dba.insertArborControl(accountNo, accountName, invoiceNo, billDate,
                amount, currency, fileArray[i].getName(), format,
                controlFileName, status);
            }
            line = buff.readLine();
          }
          buff.close();
        }
        bw.close();
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in noControlFile(): " + ex.getMessage() +
      " : line: " + line, true);
    }
    finally
    {
      return success;
    }
  }

  private boolean extractDataFromHeader()
  {
    boolean success = false;
    String line = null;
    String fileName = "unknown";
    String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG",
      "SEP","OCT","NOV","DEC"};
    try
    {
    //loop round files
      File inFiles = new File(inDir);
      File[] fileArray = inFiles.listFiles();
      if (fileArray != null)
      {
    //for each file
        for (int i = 0; i < fileArray.length; i++)
        {
          fileName = fileArray[i].getName();
          String id = fileArray[i].getName().substring(0, fileArray[i].getName().lastIndexOf("."));
          BufferedWriter bw = new BufferedWriter(new FileWriter(outDir +
            File.separator + id + "_" + outFilename));
          BufferedReader buff = new BufferedReader(new FileReader(fileArray[i]));
          line = buff.readLine();
          //while ((line != null) && (!line.equals("")))
          while (!line.equals("%%EOF"))
          {
            if (line.startsWith("%%XGS INDEX KEY "))
            {
              //extract the data
              int start = line.indexOf("ACCOUNT NO=") + 11;
              int end = line.indexOf("BILL_") - 1;
              String accountNo = line.substring(start, end);
              start = end + 14;
              end = line.indexOf("INVOICE_NO=") - 1;
              String billRefNo = line.substring(start, end);
              start = end + 12;
              end = line.indexOf("AMT", start) - 1;
              String invoiceNo = line.substring(start, end).replace('/', '-');
              start = end + 5;
              end = line.indexOf("CURR", start) - 1;
              String tempAmt = line.substring(start, end);
              String invoiceAmt = null;
              if (tempAmt.length() == 2)
              {
                invoiceAmt = "0." + tempAmt;
              }
              else if (tempAmt.length() == 1)
              {
                invoiceAmt = "0.0" + tempAmt;
              }
              else
              {
                invoiceAmt = tempAmt;
              }
              start = end + 6;
              end = line.indexOf("DATE", start) - 1;
              String currency = line.substring(start, end);
              start = end + 6;
              end = line.indexOf("PAGE", start) - 1;
              String headerDate = line.substring(start, end);
              String billDate = headerDate.substring(6, 10) +
                headerDate.substring(3, 5) + headerDate.substring(0, 2);
              start = end + 6;
              end = line.length();
              String pageRange = line.substring(start, end);
              int dash = pageRange.indexOf("-");
              int firstPage = Integer.parseInt(pageRange.substring(0, dash).trim());
              int lastPage = Integer.parseInt(pageRange.substring(++dash).trim());
              firstPage--;
              lastPage--;
              String format = "P";
//System.out.println("controlDir: " + controlDir);
//System.out.println("accountNo: " + accountNo);
/*System.out.println("invoiceNo: " + invoiceNo);
System.out.println("billDate: " + billDate);
System.out.println("invoiceAmt: " + invoiceAmt);
System.out.println("currency: " + currency);
System.out.println("fileArray[i].getName(): " + fileArray[i].getName());
*/
              String message = dba.checkArborControl(accountNo, invoiceNo,
                billDate, invoiceAmt, currency, fileArray[i].getName(), "P",
                true);
              String controlFileName = dba.getArborControlFileName();
//System.out.println("message: " + message);
              if ((message.equals("ok")) || (message.startsWith("Header/Control:")))
              {
                bw.write(accountNo + "|" + invoiceNo + "|" + firstPage + "-" +
                  lastPage + "|" + billDate + "|" + invoiceAmt + "|" + currency);
                bw.newLine();
                if (message.startsWith("Header/Control:"))
                {
                  if (message.startsWith("Header/Control: No control record found for account"))
                  {
                    if (controlFileName == null)
                    {
                      File cd = new File(controlDir);
                      File[] fA = cd.listFiles();
                      for (int j = 0; j < fA.length; j++)
                      {
                        if ((fA[j].isFile()) && (!fA[j].getName().equals("today.txt")))
                        {
                          controlFileName = fA[j].getName();
                        }
                      }
                      if (controlFileName == null)
                      {
                        controlFileName = "today.txt";
                      }
                    }
                    String reformatDate = billDate.substring(6,8) + "-" +
                      months[Integer.parseInt(billDate.substring(4,6))-1] +
                      "-" + billDate.substring(2,4);
                    String reformatAmt = invoiceAmt.indexOf(".")<0?invoiceAmt:
                      (invoiceAmt.substring(0,invoiceAmt.indexOf("."))+
                      invoiceAmt.substring(invoiceAmt.indexOf(".")+1));
                    boolean res = dba.insertArborControl(accountNo, "Unknown", invoiceNo,
                      reformatDate, invoiceAmt, currency, fileArray[i].getName(),
                      "P", controlFileName, "2");
//System.out.println("dba.insertArborControl(): " + res);
//System.out.println("dba.getMessage(): " + dba.getMessage());
//System.out.println("controlFile: " + controlDir + File.separator + controlFileName);
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(controlDir +
                      File.separator + controlFileName, true));
                    bw2.write(accountNo + "|" + "Unknown" + "|" + invoiceNo.replace('-','/') + "|" +
                      reformatDate + "|" + reformatAmt + "|" + currency + "|" +
                      fileArray[i].getName() + "|" + "P" + "|" + billRefNo + "|");
                    bw2.newLine();
                    bw2.close();
                 }
                  else
                  {
                    dba.updateArborControlProcessed(accountNo, invoiceNo,
                      fileArray[i].getName(), "3", false);
                  }
                }
              }
              else
              {
                writeToLogFile("Error in extractDataFromHeader(): " + message, true);
              }

            }
            line = buff.readLine();
            if (line == null)
            {
              line = "%%EOF";
            }
          }
          buff.close();
          bw.close();
        }
      }
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in extractDataFromHeader() for file: " + fileName +
        ".  Message: " + ex.getMessage() + ". Line: " + line, true);
    }
    finally
    {
      return success;
    }
  }

  private static String extractFilename(String[] args)
  {
    String filename = null;
    if ((args.length == 1) && (args[0].equalsIgnoreCase(SPLIT)))
    {
      System.out.println("No filename given.");
      System.exit(1);
    }
    else if (args.length > 1)
    {
      filename = args[1];
    }
    return filename;
  }

  private static String extractType(String[] args)
  {
    String type = null;
    if (args.length == 0)
    {
      System.out.println("No run type given.  Valid values are SPLIT, " +
        "HSPLIT, LOAD, LOADCONTROL, NOCONTROL, FINALCONTROL, CONTROLSPLIT, HEADERSPLIT, REJECT and NEWLOAD");
      System.exit(1);
    }
    type = args[0];
    if ((!type.equalsIgnoreCase(SPLIT)) && (!type.equalsIgnoreCase(LOAD)) &&
      (!type.equalsIgnoreCase(TEST)) && (!type.equalsIgnoreCase(HSPLIT)) &&
      (!type.equalsIgnoreCase(NEWLOAD))&&
      (!type.equalsIgnoreCase(RS6000))&&
      (!type.equalsIgnoreCase(LOADCONTROL))&&
      (!type.equalsIgnoreCase(NOCONTROL))&&
      (!type.equalsIgnoreCase(FINALCONTROL))&&
      (!type.equalsIgnoreCase(CONTROLSPLIT))&&
      (!type.equalsIgnoreCase(HEADERSPLIT))&&
      (!type.equalsIgnoreCase(REJECT)))
    {
      System.out.println("Invalid run type given.  Valid values are SPLIT, " +
        "HSPLIT, LOAD, LOADCONTROL, NOCONTROL, FINALCONTROL, CONTROLSPLIT, HEADERSPLIT, REJECT and NEWLOAD");
      System.exit(1);
    }
    return type;
  }

  public static void main(String[] args)
  {
    ArborArchiveControl aac = null;
    boolean success = false;
    String type = extractType(args);
    //String filename = extractFilename(args);
    aac = new ArborArchiveControl(type);
//System.out.println(type);
    if (type.equalsIgnoreCase(CONTROLSPLIT))
    {
      success = aac.extractData(false);
    }
    else if (type.equalsIgnoreCase(SPLIT))
    {
      success = aac.extractData(true);
    }
    else if (type.equalsIgnoreCase(HEADERSPLIT))
    {
      success = aac.extractDataFromHeader();
    }
    else if (type.equalsIgnoreCase(LOADCONTROL))
    {
      success = aac.processControlFile();
    }
    else if (type.equalsIgnoreCase(NOCONTROL))
    {
      success = aac.noControlFile();
    }
    else if (type.equalsIgnoreCase(FINALCONTROL))
    {
      success = aac.finalizeControlFile();
    }
    else if (type.equalsIgnoreCase(HSPLIT))
    {
      success = aac.extractDataFromHtml();
    }
    else if ((type.equalsIgnoreCase(LOAD)) || (type.equalsIgnoreCase(NEWLOAD)))
    {
//System.out.println(type);
      success = aac.loadPDFs(type);
    }
    else if (type.equalsIgnoreCase(REJECT))
    {
//System.out.println(type);
      success = aac.removeRejects();
    }
    else if (type.equalsIgnoreCase(RS6000))
    {
//System.out.println(type);
      success = aac.rs6000();
    }
    else //test
    {
      success = aac.test();
    }
    aac.closeLogFile();
    if (!success)
    {
      aac.renameLogFile();
    }
  }

}



