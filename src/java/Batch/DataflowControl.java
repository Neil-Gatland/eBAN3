package Batch;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import DBUtilities.*;
import JavaUtil.*;


public class DataflowControl
{
  public static final String MONTHLY = "MONTHLY";
  public static final String DAILY = "DAILY";
  public static final String VBS = "VBS";
  public static final String BASA = "BASA";
  public static final String ARCHIVE = "ARCHIVE";
  public static final String SPIRIT = "SPIRIT";
  private DBAccess dba;
  private StringUtil su;
  private String source;
  private String period;
  private final String DQ = "\"";
  private final String SQ = "'";
  private final String DQCOMMADQ = "\",\"";
  private final String COMMA = ",";
  private final String DASH = "-";
  private final String TENSPACES = "          ";
  private final String THIRTYSPACES = "                              ";
  private final String TENZEROES = "0000000000";
  private final String JOBNAME = "Dataflow Raw File Processing";
  private final String DATEFORMAT = "yyyyMMdd";
  private final String DATEFORMAT2 = "dd/MM/yyyy";
  private final String DATETIMEFORMAT = "yyyyMMddHHmmss";
  public final String ENERGIS = "energis";
  public final String OO1 = "001";
  public final String OO2 = "002";
  public final String SETID = "BU001";
  public final String VSETID = "SNR";
  public final String VID = "810792";
  public final String TEL = "TEL";
  public final String GLACC = "3210";
  public final String C9701 = "C9701";
  public final String CALLCHARGES = "Call charges";
  public final String ROUNDING = "ROUNDING";
  public final String CORRECTION = "Rounding correction";
  private final String[] NEGATIVES = {"}", "J", "K", "L", "M", "N", "O", "P",
    "Q", "R"};
  private final String[] RATINGPERIODS = {"X", "W", "E", "S", "X", "D"};
  private final String[] TIMES = {"00", "01", "02", "03", "04", "05", "06",
    "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
    "19", "20", "21", "22", "23"};
  //private String[] FIELD7CODES;
  private final String[] FIELD7CODES = {"020","022","050","069","089","090",
    "091","092","093","094","095","096","097","200","801","802","803","816","840",
    "849","850","869","911","912","913","914","915","916","917","918","919",
    "993","994","995","996","997"};
  private long cdrCount;
  private long durationMins;
  private long durationSecs;
  //private double totalCost;
  private long totalCost;
  private long totalDuration;
  private String validDir;
  private String dropDir;
  private String controlDir;
  private String rejectDir;
  private String zipDir;
  private String archiveDir;
  private String vbsDailyBillingDate;
  private Hashtable bandSummary;
  private Hashtable cliSummary;
  private Hashtable timeSummary;
  private Hashtable dateSummary;
  private Hashtable spiritSummary;
  private File logFile;
  private String logDir;
  private String attachmentDir;
  private BufferedWriter logWriter;
  private long inputFileId;
  private long outputFileId;
  private long dfControlId;
  private boolean processingError;
  private DataflowInputFileDescriptor difd;
  private String runTS;
  private boolean bigCli;
  private int maxCli;
  private int redCli;
  private int topCli;
  private String redCliAccount;

  private DataflowControl()
  {
    dba = new DBAccess();
    su = new StringUtil();
  }

  private DataflowControl(String source)
  {
    dba = new DBAccess();
    su = new StringUtil();
    this.source = source;
    logDir = EBANProperties.getEBANProperty(EBANProperties.DFLOGDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.DFARCDIR);
    attachmentDir = EBANProperties.getEBANProperty(EBANProperties.DFATTDIR);
    redCliAccount = EBANProperties.getEBANProperty("redCliAccount");
    maxCli = Integer.parseInt(EBANProperties.getEBANProperty("maxCli"));
    redCli = Integer.parseInt(EBANProperties.getEBANProperty("redCli"));
    topCli = maxCli;
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    logFile = new File(logDir + File.separator + source + "_" +
      runTS + "_log.txt");
    createLogFile();
  }

  private DataflowControl(String source, String period, boolean archive)
  {
    dba = new DBAccess();
    su = new StringUtil();
    this.source = source;
    this.period = period;
    logDir = EBANProperties.getEBANProperty(EBANProperties.DFLOGDIR);
    if (period.equals(MONTHLY))
    {
      archiveDir = EBANProperties.getEBANProperty(EBANProperties.DFMARCDIR);
    }
    else
    {
      archiveDir = EBANProperties.getEBANProperty(EBANProperties.DFARCDIR);
    }
    attachmentDir = EBANProperties.getEBANProperty(EBANProperties.DFATTDIR);
    redCliAccount = EBANProperties.getEBANProperty("redCliAccount");
    maxCli = Integer.parseInt(EBANProperties.getEBANProperty("maxCli"));
    redCli = Integer.parseInt(EBANProperties.getEBANProperty("redCli"));
    topCli = maxCli;
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    logFile = new File(logDir + File.separator + source + "_" +
      runTS + "_log.txt");
    createLogFile();
  }

  private DataflowControl(String source, String period)
  {
    dba = new DBAccess();
    su = new StringUtil();
    this.source = source;
    this.period = period;
    bandSummary = new Hashtable();
    cliSummary = new Hashtable();
    timeSummary = new Hashtable();
    dateSummary = new Hashtable();
    spiritSummary = new Hashtable();
    bigCli = false;
    inputFileId = -1;
    outputFileId = -1;
    dfControlId = -1;
    processingError = false;
    difd = null;
    redCliAccount = EBANProperties.getEBANProperty("redCliAccount");
    maxCli = Integer.parseInt(EBANProperties.getEBANProperty("maxCli"));
    redCli = Integer.parseInt(EBANProperties.getEBANProperty("redCli"));
    //Properties field7 = EBANProperties.getEBANProperties(EBANProperties.FIELD7);
    //FIELD7CODES = (String[])field7.values().toArray(new String[field7.size()]);
    logDir = EBANProperties.getEBANProperty(EBANProperties.DFLOGDIR);
    runTS = su.reformatDate("now", null, DATETIMEFORMAT);
    logFile = new File(logDir + File.separator + source + "_" + period + "_" +
      runTS + "_log.txt");
    createLogFile();
  }

  private void testDBConnection()
  {
    if (dba.oracleTest())
    {
      System.out.println("Database connection successful");
    }
    else
    {
      System.out.println("Database connection failed");
    }
    System.exit(1);
  }

  private void createLogFile()
  {
    try
    {
      logWriter = new BufferedWriter(new FileWriter(logFile));
      GregorianCalendar gc = new GregorianCalendar();
      String now = su.DateToString(gc.getTime(), DATETIMEFORMAT);
      writeToLogFile("Dataflow " + (source.equals(ARCHIVE)?"archive":"file") +
        " processing started at " +
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
      if (isError)
      {
        processingError = true;
        String dbMessage = "Processing error: see log file " +
          logFile.getAbsolutePath() + " for details";
        if (inputFileId > 0)
        {
          dba.updateDataflowInputRecord(inputFileId, null, "N", dbMessage, -1, -1,
            -1, -1, JOBNAME);
        }
        if (outputFileId > 0)
        {
          dba.updateDataflowOutputRecord(outputFileId, "N", dbMessage, JOBNAME);
        }
      }
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
      writeToLogFile("Dataflow " + (source.equals(ARCHIVE)?"archive":"file") +
        " processing ended at " +
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

  private boolean processBASADaily()
  {
    boolean success = false;
    validDir = EBANProperties.getEBANProperty(EBANProperties.BASADVALIDDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.BASADDROPDIR);
    controlDir = EBANProperties.getEBANProperty(EBANProperties.BASADCONTROLDIR);
    rejectDir = EBANProperties.getEBANProperty(EBANProperties.REJECTDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.BASADARCDIR);
    String controlBillingDate = null;

    //get control file
    try
    {
      File control = new File(controlDir);
      File[] fileArray = control.listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          initialiseFileIds();
          File controlFile = fileArray[i];
          String controlFileName = controlFile.getName();
          BufferedReader buff = new BufferedReader(new FileReader(controlFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;

            java.sql.Date billingDate = su.toJDBCDate(line.substring(4, 10), "yyMMdd");
            controlBillingDate = su.DateToString(new Date(billingDate.getTime()),
              DATEFORMAT);
            while (!eof)
            {
              initialiseFileIds();
              String fileName = line.substring(0, 4) + line.substring(4, 10) +
                ".dat";

              if (dba.existsDataflowInputFileRecord(fileName, controlFileName))
              {
                throw new Exception("Input file record exists already");
              }
              inputFileId = dba.createDataflowInputRecord(fileName,
                billingDate, controlFileName, JOBNAME, 0, 0, 0);
              if (inputFileId <= 0)
              {
                writeToLogFile("Error in processBASADaily(): " +
                  dba.getMessage(), true);
              }
              dfControlId = dba.createDataflowAccountControl(inputFileId,
                0, 0, 0, 0, line.substring(22, 31), JOBNAME);
              if (dfControlId <= 0)
              {
                writeToLogFile("Error in processBASADaily(): " +
                  dba.getMessage(), true);
              }

              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
          }
          else
          {
            writeToLogFile("Error in processBASADaily(): empty control file " +
              controlFileName, true);
          }
          buff.close();
          archiveFile(controlFile, archiveDir, rejectDir);
        }
      }
      else
      {
        writeToLogFile("processBASADaily(): no control files found", false);
      }
      //read and reformat data files
      File outputDir = new File(validDir);
      File dataFiles = new File(dropDir);
      fileArray = dataFiles.listFiles();
      if (fileArray != null)
      {
        for (int i=0; i<fileArray.length; i++)
        {
          initialiseFileIds();
          File dataFile = fileArray[i];
          String dataFileName = dataFile.getName();
          BufferedReader buff =
            new BufferedReader(new FileReader(dataFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            String accountNo = line.substring(9, 18);
            difd = dba.getDataflowControlRecord(accountNo, controlBillingDate,
              dataFileName);
            if (difd != null)
            {
              cdrCount = 0;
              durationMins = 0;
              durationSecs = 0;
              totalCost = 0;
              inputFileId = difd.getInputId();
              dfControlId = difd.getControlId();
              java.util.Date billDate = difd.getBillingDate();
              String billingDate = su.DateToString(billDate, DATEFORMAT);
              String outputFileName = "B_D_" + accountNo + "_" + billingDate;
              File outputFile = new File(outputDir.getAbsolutePath(),
                outputFileName + ".csv");
              outputFile.createNewFile();
              BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
              boolean eof = false;
              while (!eof)
              {
                String cost = reformatCost(line.substring(139, 148));
                if ((su.isNumeric(line.substring(131, 136))) &&
                  (su.isNumeric(line.substring(136, 138))) && (su.isNumeric(cost)))
                {
                  cdrCount++;
                  String outputLine = reformatBASADaily(line, billingDate, cost);
                  incrementTotals(line.substring(131, 136),
                    line.substring(136, 138), cost);
                  bw.write(outputLine);
                  bw.newLine();
                }
                else
                {
                  writeToLogFile("Error in processBASADaily(): control " +
                    "totals not numeric for file: " + dataFileName + " line: " +
                    line , true);
                }
                line = buff.readLine();
                if (line == null)
                {
                  eof = true;
                }
                else if (line.length() <= 0)
                {
                  eof = true;
                }
              }
              bw.close();
              finalise("processBASADaily", accountNo, outputFile,
                outputDir, outputFileName, billDate, "BASA", false);
            }
            else
            {
              writeToLogFile("Error in processBASADaily(): no control record for " +
                dataFileName, true);
            }
            buff.close();
            archiveFile(dataFile, archiveDir, rejectDir);
          }
          else
          {
            writeToLogFile("Error in processBASADaily(): empty data file " +
              dataFileName, true);
          }
        }
      }
      else
      {
        writeToLogFile("processBASADaily(): no data files found", true);
      }
      zipArchivedFiles("B_D");
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in processBASADaily(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean processBASAMonthly()
  {
    boolean success = false;
    validDir = EBANProperties.getEBANProperty(EBANProperties.BASAMVALIDDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.BASAMDROPDIR);
    controlDir = EBANProperties.getEBANProperty(EBANProperties.BASAMCONTROLDIR);
    rejectDir = EBANProperties.getEBANProperty(EBANProperties.REJECTDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.BASAMARCDIR);
    Properties spiritAccounts =  EBANProperties.getEBANProperties(EBANProperties.SPIRITACC);
    boolean spiritAccount = false;
    String savedFileName = null;
    String savedDate = null;
    long originalInputFileId = 0;
    //get control file
    try
    {
      File control = new File(controlDir);
      File[] fileArray = control.listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          initialiseFileIds();
          File controlFile = fileArray[i];
          boolean dateStored = false;
          String controlFileName = controlFile.getName();
          String fileName = controlFileName;//"ddb" + controlFileName.substring(4, 8) + ".dat";
          BufferedReader buff =
            new BufferedReader(new FileReader(controlFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;
            java.sql.Date billingDate = null;
            while (!eof)
            {
              //initialiseFileIds();
              if (!dateStored)
              {
                if (su.validDate(line.substring(14, 18)+line.substring(19, 21)+
                  line.substring(22, 24), DATEFORMAT))
                {
                  String cycle = line.substring(35, 37);
                  String day = line.substring(22, 24);
                  if ((cycle.equals("45")) && (day.equals("01")))
                  {
                    day = "02"/*previously "05"*/;
                  }
                  savedDate = line.substring(14, 18) + line.substring(19, 21) +
                    day;
                  billingDate = su.toJDBCDate(line.substring(14, 18)+
                  line.substring(19, 21)+day, DATEFORMAT);
                  //create one row to store the billing date
                  inputFileId = dba.createDataflowInputRecord(fileName,
                    billingDate, controlFileName, JOBNAME, 0, 0, 0);
                  savedFileName = fileName;
                  originalInputFileId = inputFileId;
                  if (inputFileId <= 0)
                  {
                    writeToLogFile("Error in processBASAMonthly(): " +
                      dba.getMessage(), true);
                  }
                  dateStored = true;
                }
              }
              if ((line.length() > 16) &&
                (su.isNumeric(line.substring(6, 15)))) //account row
              {
                String cdr = line.substring(80, 89).trim().equals("")?"0":
                  line.substring(80, 89).trim();
                String mins = line.substring(91, 100).trim().equals("")?"0":
                  line.substring(91, 100).trim();
                String secs = line.substring(101, 103).trim().equals("")?"0":
                  line.substring(101, 103).trim();
                if ((su.isNumeric(cdr)) && (su.isNumeric(mins)) &&
                  (su.isNumeric(secs)))
                {
                  dfControlId = dba.createDataflowAccountControl(inputFileId,
                    Long.parseLong(cdr), Long.parseLong(mins),
                    Long.parseLong(secs), 0, line.substring(6, 15), JOBNAME);
                  if (dfControlId <= 0)
                  {
                    writeToLogFile("Error in processBASAMonthly(): " +
                      dba.getMessage(), true);
                  }
                }
                else
                {
                  writeToLogFile("Error in processBASAMonthly(): " +
                    "totals not numeric for account:" + line.substring(6, 15),
                    true);
                }
              }
              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
          }
          else
          {
            writeToLogFile("Error in processBASAMonthly(): empty control file" +
              controlFileName, true);
          }
          buff.close();
          archiveFile(controlFile, archiveDir, rejectDir);
        }
      }
      else
      {
        writeToLogFile("processBASAMonthly(): no control files found", false);
      }
      //read and reformat data files
      File outputDir = new File(validDir);
      File dataFiles = new File(dropDir);
      fileArray = dataFiles.listFiles();
      if (fileArray != null)
      {
        for (int i=0; i<fileArray.length; i++)
        {
          boolean inputFileStored = false;
          File dataFile = fileArray[i];
          cdrCount = 0;
          durationMins = 0;
          durationSecs = 0;
          totalCost = 0;
          difd = null;
          String accountNo = "";
          BufferedWriter bw = null;
          String outputFileName = null;
          File outputFile = null;
          java.util.Date billDate = null;
          String billingDate = null;
          initialiseFileIds();
          String dataFileName = dataFile.getName();
          BufferedReader buff =
            new BufferedReader(new FileReader(dataFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;
            while (!eof)
            {
              if (!accountNo.equals(line.substring(0, 9)))
              {
                if (bw != null)
                {
                  bw.close();
                  finalise("processBASAMonthly", accountNo, outputFile,
                    outputDir, outputFileName, billDate, "BASA", spiritAccount);
                }
                initialiseFileIds();
                cdrCount = 0;
                durationMins = 0;
                durationSecs = 0;
                totalCost = 0;
                accountNo = line.substring(0, 9);
                spiritAccount = spiritAccounts.containsKey(accountNo);
                difd = dba.getDataflowControlRecord(accountNo, savedDate,
                  savedFileName);
                if (difd != null)
                {
                  //originalInputFileId = difd.getInputId();
                  dfControlId = difd.getControlId();
                  billDate = difd.getBillingDate();
                  billingDate = su.DateToString(billDate, DATEFORMAT);
                  outputFileName = "B_M_" + accountNo + "_" + billingDate;
                  outputFile = new File(outputDir.getAbsolutePath(),
                    outputFileName + ".csv");
                  outputFile.createNewFile();
                  bw = new BufferedWriter(new FileWriter(outputFile));
                  if (!inputFileStored)
                  {
                    inputFileId = dba.createDataflowInputRecord(dataFileName,
                      new java.sql.Date(billDate.getTime()),
                      difd.getControlFilename(), JOBNAME, 0, 0, 0);
                    if (inputFileId <= 0)
                    {
                      writeToLogFile("Error in processBASAMonthly(): " +
                        dba.getMessage(), true);
                    }
                    inputFileStored = true;
                  }
                  else
                  {
                    inputFileId = dba.getDataflowInputFileId(dataFileName,
                      difd.getControlFilename());
                    if (inputFileId <= 0)
                    {
                      writeToLogFile("Error in processBASAMonthly(): " +
                        dba.getMessage(), true);
                    }
                  }
                  String message = dba.updateDataflowAccountControl(dfControlId,
                    inputFileId, -1, -1, -1, -1, -1, JOBNAME);
                  if (message != null)
                  {
                    writeToLogFile("Error in processBASAMonthly(): " +
                      message, true);
                  }
                }
                else
                {
                  writeToLogFile("Error in processBASAMonthly(): " +
                    "no control record found for account " + accountNo, true);
                }
              }
              String cost = reformatCost(line.substring(125, 134));
              if ((su.isNumeric(line.substring(118, 123))) &&
                (su.isNumeric(line.substring(123, 125))) && (su.isNumeric(cost)))
              {
                cdrCount++;
                String outputLine = reformatMonthly(line, "BASA", cost);
                incrementTotals(line.substring(118, 123),
                  line.substring(123, 125), cost);
                bw.write(outputLine);
                summaryProcessing(line, "BASA", cost, spiritAccount);
                bw.newLine();
              }
              else
              {
                writeToLogFile("Error in processBASAMonthly(): control " +
                  "totals not numeric for file: " + dataFileName + " line: " +
                  line , true);
              }
              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
            bw.close();
            finalise("processBASAMonthly", accountNo, outputFile,
              outputDir, outputFileName, billDate, "BASA", spiritAccount);
          }
          else
          {
            writeToLogFile("Error in processBASAMonthly(): empty data file " +
              dataFileName, true);
          }
          buff.close();
          archiveFile(dataFile, archiveDir, rejectDir);
        }
        //remove original input record
        String message = dba.deleteDataflowInputRecord(originalInputFileId);
        if (message != null)
        {
          writeToLogFile("Error in processBASAMonthly(): " +
            message, true);
        }
      }
      else
      {
        writeToLogFile("processBASAMonthly(): no data files found", false);
      }
      zipArchivedFiles("B_M");
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in processBASAMonthly(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean processVBSDaily()
  {
    boolean success = false;
    validDir = EBANProperties.getEBANProperty(EBANProperties.VBSDVALIDDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.VBSDDROPDIR);
    controlDir = EBANProperties.getEBANProperty(EBANProperties.VBSDCONTROLDIR);
    rejectDir = EBANProperties.getEBANProperty(EBANProperties.REJECTDIR);
    zipDir = EBANProperties.getEBANProperty(EBANProperties.VBSDZIPDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.VBSDARCDIR);
    //find relevant zip and unzip it
    unzipVBSDaily();
//return true;/*

    //get control file
    try
    {
      File control = new File(controlDir);
      File[] fileArray = control.listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          initialiseFileIds();
          File controlFile = fileArray[i];
          String controlFileName = controlFile.getName();
          BufferedReader buff = new BufferedReader(new FileReader(controlFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;

            while (!eof)
            {
              initialiseFileIds();
              if ((line.substring(0, 1).equalsIgnoreCase("M")) &&
                (line.substring(5, 6).equals(".")))//only interested in M types
              {
                String fileName = line.substring(0, line.indexOf(","));
                if (dba.existsDataflowInputFileRecord(fileName, controlFileName))
                {
                  throw new Exception("Input file record exists already");
                }
                inputFileId = dba.createDataflowInputRecord(fileName,
                  null, controlFileName, JOBNAME, 0, 0, 0);
                if (inputFileId <= 0)
                {
                  writeToLogFile("Error in processVBSDaily(): " +
                    dba.getMessage(), true);
                }
                dfControlId = dba.createDataflowAccountControl(inputFileId,
                  0, 0, 0, 0, line.substring(line.indexOf(",")+1), JOBNAME);
                if (dfControlId <= 0)
                {
                  writeToLogFile("Error in processVBSDaily(): " +
                    dba.getMessage(), true);
                }
              }
              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
          }
          else
          {
            writeToLogFile("Error in processVBSDaily(): empty control file", true);
          }
          buff.close();
          archiveFile(controlFile, null, rejectDir);
        }
      }
      else
      {
        writeToLogFile("processVBSDaily(): no control files", false);
      }
      //read and reformat data files
      File outputDir = new File(validDir);
      File dataFiles = new File(dropDir);
      fileArray = dataFiles.listFiles();
      if (fileArray != null)
      {
        for (int i=0; i<fileArray.length; i++)
        {
          initialiseFileIds();
          File dataFile = fileArray[i];
          String dataFileName = dataFile.getName();
          if ((dataFileName.substring(0, 1).equalsIgnoreCase("M")) &&
            (dataFileName.substring(5, 6).equals(".")))//only interested in M types
          {
            BufferedReader buff =
              new BufferedReader(new FileReader(dataFile.getAbsolutePath()));
            String line = buff.readLine();
            if ((line != null) && (line.length() > 0))
            {
              initialiseFileIds();
              String accountNo = line.substring(1, 10);
              difd = dba.getDataflowControlRecord(accountNo, null, dataFileName);
              if (difd != null)
              {
                cdrCount = 0;
                durationMins = 0;
                durationSecs = 0;
                totalCost = 0;
                inputFileId = difd.getInputId();
                dfControlId = difd.getControlId();
                String outputFileName = "V_D_" + accountNo + "_" +
                  vbsDailyBillingDate;
                File outputFile = new File(outputDir.getAbsolutePath(),
                  outputFileName + ".csv");
                outputFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
                boolean eof = false;
                while (!eof)
                {
                  cdrCount++;
                  // duration processing questionable
                  String dur = line.substring(171, 180).trim();
                  long durSecs = Long.parseLong(dur);
                  String secs = String.valueOf(durSecs % 60);
                  String mins = String.valueOf((durSecs - (durSecs % 60)) / 60);
                  incrementTotals(mins, secs, line.substring(183, 192));
                  bw.write(line.substring(0, line.lastIndexOf(",")));
                  bw.newLine();
                  line = buff.readLine();
                  if (line == null)
                  {
                    eof = true;
                  }
                  else if (line.length() <= 0)
                  {
                    eof = true;
                  }
                }
                bw.close();
                finalise("processVBSDaily", accountNo, outputFile,
                  outputDir, outputFileName, su.toJDBCDate(vbsDailyBillingDate,
                  DATEFORMAT), "VBS", false);
              }
              else
              {
                writeToLogFile("Error in processVBSDaily(): " +
                  "no control record found for data file " + dataFileName, true);
              }
              buff.close();
            }
            else
            {
              //no control record
              if ((dataFileName.substring(0, 1).equalsIgnoreCase("M")) &&
                (dataFileName.substring(5, 6).equals(".")))
              {
                writeToLogFile("Error in processVBSDaily(): empty data file " +
                  dataFileName, true);
              }
            }
          }
          archiveFile(dataFile, null, rejectDir);
        }
      }
      else
      {
        writeToLogFile("processVBSDaily(): no data files", false);
      }
      zipArchivedFiles("V_D");
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in processVBSDaily(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean processVBSMonthly()
  {
    boolean success = false;
    validDir = EBANProperties.getEBANProperty(EBANProperties.VBSMVALIDDIR);
    dropDir = EBANProperties.getEBANProperty(EBANProperties.VBSMDROPDIR);
    controlDir = EBANProperties.getEBANProperty(EBANProperties.VBSMCONTROLDIR);
    rejectDir = EBANProperties.getEBANProperty(EBANProperties.REJECTDIR);
    zipDir = EBANProperties.getEBANProperty(EBANProperties.VBSMZIPDIR);
    archiveDir = EBANProperties.getEBANProperty(EBANProperties.VBSMARCDIR);
    String savedFileName = null;
    long originalInputFileId = 0;
    //get control file
    try
    {
      File control = new File(controlDir);
      File[] fileArray = control.listFiles();
      if (fileArray != null)
      {
        for (int i = 0; i < fileArray.length; i++)
        {
          initialiseFileIds();
          File controlFile = fileArray[i];
          boolean dateStored = false;
          String controlFileName = controlFile.getName();
          String fileName = "ddb" + controlFileName.substring(3, 7);
          savedFileName = fileName;
          BufferedReader buff = new BufferedReader(new FileReader(controlFile.getAbsolutePath()));
          String line = buff.readLine();

          //create one row to store the control file name
          inputFileId = dba.createDataflowInputRecord(fileName,
            null, controlFileName, JOBNAME, 0, 0, 0);
          if (inputFileId <= 0)
          {
            writeToLogFile("Error in processVBSMonthly(): " +
              dba.getMessage(), true);
          }
          originalInputFileId = inputFileId;
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;
            while (!eof)
            {
              if ((su.isNumeric(line.substring(237, 247).trim())) &&
                (su.isNumeric(line.substring(248, 263).trim())) &&
                (su.isNumeric(line.substring(264, 266))))
              {
                dfControlId = dba.createDataflowAccountControl(inputFileId,
                  Long.parseLong(line.substring(237, 247).trim()),
                  Long.parseLong(line.substring(248, 263).trim()),
                  Long.parseLong(line.substring(264, 266)), 0,
                  line.substring(2, 11), JOBNAME);
                if (dfControlId <= 0)
                {
                  writeToLogFile("Error in processVBSMonthly(): " +
                    dba.getMessage(), true);
                }
              }
              else
              {
                writeToLogFile("Error in processVBSMonthly(): " +
                  "totals not numeric for account:" + line.substring(2, 11), true);
              }
              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
          }
          else
          {
            writeToLogFile("Error in processVBSMonthly(): empty control file", true);
          }
          buff.close();
          archiveFile(controlFile, archiveDir, rejectDir);
        }
      }
      else
      {
        writeToLogFile("processVBSMonthly(): no control files found", false);
      }
      //unzip data files
      unzipVBSMonthly();
      //read and reformat data files
      File outputDir = new File(validDir);
      File dataFiles = new File(dropDir);
      fileArray = dataFiles.listFiles();
      if (fileArray != null)
      {
        for (int i=0; i<fileArray.length; i++)
        {
          initialiseFileIds();
          boolean inputFileStored = false;
          File dataFile = fileArray[i];
          cdrCount = 0;
          durationMins = 0;
          durationSecs = 0;
          totalCost = 0;
          difd = null;
          String accountNo = "";
          BufferedWriter bw = null;
          String outputFileName = null;
          File outputFile = null;
          java.util.Date billDate = null;
          String billingDate = null;
          inputFileId = -1;
          String dataFileName = dataFile.getName();
          BufferedReader buff =
            new BufferedReader(new FileReader(dataFile.getAbsolutePath()));
          String line = buff.readLine();
          if ((line != null) && (line.length() > 0))
          {
            boolean eof = false;
            while (!eof)
            {
              if (!accountNo.equals(line.substring(0, 9)))
              {
                if (bw != null)
                {
                  bw.close();
                  finalise("processVBSMonthly", accountNo, outputFile,
                    outputDir, outputFileName, billDate,"VBS", false);
                }
                initialiseFileIds();
                cdrCount = 0;
                durationMins = 0;
                durationSecs = 0;
                totalCost = 0;
                accountNo = line.substring(0, 9);
                if (accountNo.equals(redCliAccount))
                {
                  topCli = redCli;
                }
                else
                {
                  topCli = maxCli;
                }
                difd = dba.getDataflowControlRecord(accountNo, null,
                  savedFileName);
                if (difd != null)
                {
                  inputFileId = difd.getInputId();
                  //originalInputFileId = inputFileId;
                  dfControlId = difd.getControlId();
                  billingDate = line.substring(47, 55);
                  billDate = su.toDate(billingDate, DATEFORMAT);
                  outputFileName = "V_M_" + accountNo + "_" + billingDate;
                  outputFile = new File(outputDir.getAbsolutePath(),
                    outputFileName + ".csv");
                  outputFile.createNewFile();
                  bw = new BufferedWriter(new FileWriter(outputFile));

                  if (!inputFileStored)
                  {
                    inputFileId = dba.createDataflowInputRecord(dataFileName,
                      new java.sql.Date(billDate.getTime()),
                      difd.getControlFilename(), JOBNAME, 0, 0, 0);
                    if (inputFileId <= 0)
                    {
                      writeToLogFile("Error in processVBSMonthly(): " +
                        dba.getMessage(), true);
                    }
                    inputFileStored = true;
                  }
                  else
                  {
                    inputFileId = dba.getDataflowInputFileId(dataFileName,
                      difd.getControlFilename());
                    if (inputFileId <= 0)
                    {
                      writeToLogFile("Error in processVBSMonthly(): " +
                        dba.getMessage(), true);
                    }
                  }
                  String message = dba.updateDataflowAccountControl(dfControlId,
                    inputFileId, -1, -1, -1, -1, -1, JOBNAME);
                  if (message != null)
                  {
                    writeToLogFile("Error in processVBSMonthly(): " +
                      message, true);
                  }



                }
                else
                {
                  writeToLogFile("Error in processVBSMonthly(): " +
                    "no control record found for account " + accountNo, true);
                }
              }
              cdrCount++;
/*if (cdrCount == 9000000)
{
  bw.close();
  bw = new BufferedWriter(new FileWriter(outputFile.getAbsolutePath(), true));
}*/
              String outputLine = reformatMonthly(line, "VBS",
                line.substring(125, 134));
              incrementTotals(line.substring(118, 123),
                line.substring(123, 125), line.substring(125, 134));
              bw.write(outputLine);
              summaryProcessing(line, "VBS", line.substring(125, 134), false);
              bw.newLine();
              line = buff.readLine();
              if (line == null)
              {
                eof = true;
              }
              else if (line.length() <= 0)
              {
                eof = true;
              }
            }
            bw.close();
            finalise("processVBSMonthly", accountNo, outputFile,
              outputDir, outputFileName, billDate, "VBS", false);
          }
          else
          {
            writeToLogFile("Error in processVBSMonthly(): empty data file", true);
          }
          buff.close();
          archiveFile(dataFile, null, rejectDir);
        }
        //remove original input record
        String message = dba.deleteDataflowInputRecord(originalInputFileId);
        if (message != null)
        {
          writeToLogFile("Error in processVBSMonthly(): " +
            message, true);
        }
      }
      else
      {
        writeToLogFile("processVBSMonthly(): no data files found", false);
      }
      zipArchivedFiles("V_M");
      success = true;
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in processVBSMonthly(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private void zipFile(Vector filesToZip,
    String zippedFile)
  {
    if (!filesToZip.isEmpty())
    {
      // Create a buffer for reading the files
      byte[] buf = new byte[1024];

      try
      {
        // Create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));

        for (Iterator it = filesToZip.iterator(); it.hasNext(); )
        {
          File fileToZip = (File)it.next();
          // Compress the file
          FileInputStream in = new FileInputStream(fileToZip.getAbsolutePath());

          // Add ZIP entry to output stream.
          out.putNextEntry(new ZipEntry(fileToZip.getName()));

          // Transfer bytes from the file to the ZIP file
          int len;
          while ((len = in.read(buf)) > 0)
          {
            out.write(buf, 0, len);
          }

          // Complete the entry
          out.closeEntry();
          in.close();
          fileToZip.delete();
        }
        // Complete the ZIP file
        out.close();
      }
      catch (IOException ex)
      {
        writeToLogFile("Error in zipFile(): " + ex.getMessage(), true);
      }
    }
  }

  private void unzipVBSDaily()
  {
    try
    {
      // find vbs daily zip file(s)
      File directory = new File(zipDir);
      File[] list = directory.listFiles(new VBSDailyZipFileFilter());
      for (int i = 0; i < list.length; i++)
      {
        // Open the ZIP file
        String inFilename = list[i].getAbsolutePath();
        vbsDailyBillingDate = list[i].getName().substring(3, 11);
        ZipInputStream in = new ZipInputStream(new FileInputStream(inFilename));
        ZipEntry zipEntry;
        Vector fileNames = new Vector();
        String controlFilePath = null;

        while((zipEntry = in.getNextEntry()) != null)
        {
          String outFilename = zipEntry.getName();
          String outFilePath;
          if (outFilename.toLowerCase().endsWith(".ctl"))
          {
            outFilePath = controlDir + File.separator + outFilename;
            controlFilePath = outFilePath;
          }
          else
          {
            outFilePath = dropDir + File.separator + outFilename;
            fileNames.add(outFilename);
          }

          // Open the output file
          OutputStream out = new FileOutputStream(outFilePath);
          // Transfer bytes from the ZIP file to the output file
          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0)
          {
              out.write(buf, 0, len);
          }

          // Close the streams
          out.close();
        }
        in.close();
        list[i].renameTo(new File(archiveDir + File.separator +
          list[i].getName()));

        // check for splits
        splitFileProcessing(fileNames, controlFilePath);
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in unzipVBSDaily(): " + ex.getMessage(), true);
    }
  }

  private void splitFileProcessing(Vector fileNames, String controlFilePath)
    throws Exception
  {
    Collections.sort(fileNames);
    String previousName = "9999.99";
    boolean hasSplits = false;

    for (Iterator it = fileNames.iterator(); it.hasNext(); )
    {
      String fileName = (String)it.next();
      if (fileName.substring(0, fileName.lastIndexOf(".")).
        equals(previousName.substring(0, previousName.lastIndexOf("."))))
      {
        hasSplits = true;
        File splitFile = new File(dropDir + File.separator + fileName);
        BufferedReader buff =
          new BufferedReader(new FileReader(splitFile));
        BufferedWriter bw =
          new BufferedWriter(new FileWriter(dropDir + File.separator + previousName, true));
        String line = buff.readLine();
        if ((line != null) && (line.length() > 0))
        {
          boolean eof = false;
          while (!eof)
          {
            bw.write(line);
            bw.newLine();
            line = buff.readLine();
            if (line == null)
            {
              eof = true;
            }
            else if (line.length() <= 0)
            {
              eof = true;
            }
          }
        }
        bw.close();
        buff.close();
        splitFile.delete();
      }
      else
      {
        previousName = fileName;
      }
    }
    if (hasSplits)
    {
      Vector controlRecs = new Vector();
      BufferedReader buff =
        new BufferedReader(new FileReader(controlFilePath));
      String line = buff.readLine();
      if ((line != null) && (line.length() > 0))
      {
        boolean eof = false;
        while (!eof)
        {
          controlRecs.add(line);
          line = buff.readLine();
          if (line == null)
          {
            eof = true;
          }
          else if (line.length() <= 0)
          {
            eof = true;
          }
        }
      }
      buff.close();
      Collections.sort(controlRecs);
      String previousLine = "";
      BufferedWriter bw =
        new BufferedWriter(new FileWriter(controlFilePath, false));
      for (Iterator it = controlRecs.iterator(); it.hasNext(); )
      {
        line = (String)it.next();
        if (!line.substring(0, line.lastIndexOf(".")).equals(previousLine))
        {
          //write out
          bw.write(line);
          bw.newLine();
          previousLine = line.substring(0, line.lastIndexOf("."));
        }
      }
      bw.close();
    }
  }

  private void unzipVBSMonthly()
  {
    try
    {
      File directory = new File(zipDir);
      File[] list = directory.listFiles();
      for (int i = 0; i < list.length; i++)
      {
        // Open the compressed file
        String inFilename = list[i].getAbsolutePath();
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));

        // Open the output file
        String outFilePath = dropDir + File.separator + list[i].getName();
        OutputStream out = new FileOutputStream(outFilePath);
        // Transfer bytes from the compressed file to the output file
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
        {
            out.write(buf, 0, len);
        }
        // Close the file and stream
        in.close();
        out.close();
        list[i].renameTo(new File(archiveDir + File.separator +
          list[i].getName()));
      }
    }
    catch (IOException ex)
    {
      writeToLogFile("Error in unzipVBSMonthly(): " + ex.getMessage(), true);
    }

  }

  private void writeSummaryFiles(File outputDir, String outputFileName,
    Vector filesToZip, long dfControlId)
  {
    writeBandSummaryFile(outputDir, outputFileName, filesToZip);
    writeCliSummaryFile(outputDir, outputFileName, filesToZip);
    writeSumSummaryFile(outputDir, outputFileName, filesToZip, dfControlId);
  }

  private void writeBandSummaryFile(File outputDir, String outputFileName,
    Vector filesToZip)
  {
    try
    {
      File outputFile = new File(outputDir.getAbsolutePath(),
        outputFileName.substring(0, 13) + "_BAND_" +
        outputFileName.substring(14) + ".csv");
      outputFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
      bw.write("Cable & Wireless - Summary of Call Destinations for," +
        outputFileName.substring(4, 13) + ",Date " +
        outputFileName.substring(20, 22)+ DASH + outputFileName.substring(18,20) +
        DASH + outputFileName.substring(14, 18));
      bw.newLine();
      bw.write("Charge Band,Destination,Calls,Cost,Seconds");
      bw.newLine();
      Vector v = new Vector(bandSummary.keySet());
      Collections.sort(v);
      for (Iterator it = v.iterator(); it.hasNext(); )
      {
        String key =  (String)it.next();
        DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)bandSummary.get(key);
        bw.write(DQ + SQ + dtd.getId() + DQCOMMADQ + dtd.getDescription() +
          DQCOMMADQ + dtd.getTotalCalls() + DQCOMMADQ +
          //su.roundToString(dtd.getCost(), 3) +
          dtd.getCostAsString() +
          DQCOMMADQ + dtd.getDuration() + DQ);
        bw.newLine();
      }
      bw.write("End of Report, , , ,");
      bw.newLine();
      bw.close();
      filesToZip.add(outputFile);
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in writeBandSummaryFile(): " + ex.getMessage(), true);
    }
  }

  private void writeCliSummaryFile(File outputDir, String outputFileName,
    Vector filesToZip)
  {
    try
    {
      File outputFile = new File(outputDir.getAbsolutePath(),
        outputFileName.substring(0, 13) + "_CLI_" +
        outputFileName.substring(14) + ".csv");
      outputFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
      bw.write("Cable & Wireless - Summary by Calling Line for," +
        outputFileName.substring(4, 13) + ",Date " +
        outputFileName.substring(20, 22)+ DASH + outputFileName.substring(18, 20) +
        DASH + outputFileName.substring(14, 18));
      bw.newLine();
      bw.write("Product Code,Calling Line,Calls,Cost,Seconds,Cost Centre");
      bw.newLine();
      if (bigCli)
      {
        dba.getCli(bw);
      }
      else
      {
        Vector v = new Vector(cliSummary.keySet());
        Collections.sort(v);
        for (Iterator it = v.iterator(); it.hasNext(); )
        {
          String key =  (String)it.next();
          DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)cliSummary.get(key);
          bw.write(DQ + SQ + dtd.getProductCode() + DQCOMMADQ + SQ +
            dtd.getId().substring(0,12) + DQCOMMADQ +
            //dtd.getTotalCalls() + DQCOMMADQ + su.roundToString(dtd.getCost()/1000, 3) +
            dtd.getTotalCalls() + DQCOMMADQ + dtd.getCostAsString() +
            DQCOMMADQ + dtd.getDuration() + DQCOMMADQ + dtd.getDescription() + DQ);
          bw.newLine();
        }
      }
      dba.deleteCli();
      bw.write("End of Report, , , , ,");
      bw.newLine();
      bw.close();

      filesToZip.add(outputFile);
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in writeCliSummaryFile(): " + ex.getMessage(), true);
    }
  }

/*  private void writeCliSummaryFile(File outputDir, String outputFileName,
    Vector filesToZip)
  {
    try
    {
      File outputFile = new File(outputDir.getAbsolutePath(),
        outputFileName.substring(0, 13) + "_CLI_" +
        outputFileName.substring(14) + ".csv");
      outputFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
      bw.write("Cable & Wireless - Summary by Calling Line for," +
        outputFileName.substring(4, 13) + ",Date " +
        outputFileName.substring(20, 22)+ DASH + outputFileName.substring(18, 20) +
        DASH + outputFileName.substring(14, 18));
      bw.newLine();
      bw.write("Product Code,Calling Line,Calls,Cost,Seconds,Cost Centre");
      bw.newLine();
      Vector v = new Vector(cliSummary.keySet());
      Collections.sort(v);
      for (Iterator it = v.iterator(); it.hasNext(); )
      {
        String key =  (String)it.next();
        DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)cliSummary.get(key);
        bw.write(DQ + SQ + dtd.getProductCode() + DQCOMMADQ + SQ +
          dtd.getId().substring(0,12) + DQCOMMADQ +
          //dtd.getTotalCalls() + DQCOMMADQ + su.roundToString(dtd.getCost()/1000, 3) +
          dtd.getTotalCalls() + DQCOMMADQ + dtd.getCostAsString() +
          DQCOMMADQ + dtd.getDuration() + DQCOMMADQ + dtd.getDescription() + DQ);
        bw.newLine();
      }
      bw.write("End of Report, , , , ,");
      bw.newLine();
      bw.close();
      filesToZip.add(outputFile);
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in writeCliSummaryFile(): " + ex.getMessage(), true);
    }
  }
*/
  private void writeSumSummaryFile(File outputDir, String outputFileName,
    Vector filesToZip, long dfControlId)
  {
    try
    {
      String message = null;
      File outputFile = new File(outputDir.getAbsolutePath(),
        outputFileName.substring(0, 13) + "_SUM_" +
        outputFileName.substring(14) + ".csv");
      outputFile.createNewFile();
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
      String bDate = outputFileName.substring(20, 22) + DASH +
        outputFileName.substring(18, 20) + DASH +
        outputFileName.substring(14, 18);
      bw.write("Cable & Wireless - Summary by Date and General for," +
        outputFileName.substring(4, 13) + ",Date " + bDate);
      bw.newLine();
      //StringBuffer cost = new StringBuffer(su.roundToString(totalCost/1000, 3));
      StringBuffer cost = new StringBuffer(String.valueOf(totalCost));
      int pos = cost.length() - 3;
      while (pos < 1)
      {
        cost.insert(0, '0');
        pos = cost.length() - 3;
      }
      cost.insert(pos, '.');
      String decCost = cost.toString();
      int n = decCost.length() - decCost.indexOf(".");
      if (n < 4)
      {
        for (int i = n; i < 4; i++)
        {
          cost.append("0");
        }
        decCost = cost.toString();
      }
      bw.write("Bill Date," + bDate + ",Account Number," +
        outputFileName.substring(4, 13) + ",Total Calls," + cdrCount +
        ",Value," + decCost + ",Total Seconds," + totalDuration);
      bw.newLine();
      //add in the 'empty' times
      for (int i = 0; i < TIMES.length; i++)
      {
        if (!timeSummary.containsKey(TIMES[i]))
        {
          timeSummary.put(TIMES[i], new DataflowTotalDescriptor(TIMES[i], null,
            null, 0, 0));
        }
      }
      Vector v = new Vector(timeSummary.keySet());
      Collections.sort(v);
      for (Iterator it = v.iterator(); it.hasNext(); )
      {
        String key = (String)it.next();
        DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)timeSummary.get(key);
        bw.write("Time Slot," + dtd.getId() + ":00,Calls," +
          //dtd.getTotalCalls() + ",Cost," + su.roundToString(dtd.getCost()/1000, 3) +
          dtd.getTotalCalls() + ",Cost," + dtd.getCostAsString() +
          ",Seconds," + dtd.getDuration());
        bw.newLine();
        message = dba.createSummaryMgtRecord(dfControlId, "Time", dtd.getId() + ":00",
          JOBNAME, dtd.getTotalCalls(), (double)dtd.getCost()/1000, dtd.getDuration());
        if (message != null)
        {
          writeToLogFile("Error in writeSumSummaryFile(): " + message, true);
        }
      }
      for (int i = 0; i < 4; i++)
      {
        bw.write("Spare , , , , , ,");
        bw.newLine();
      }
      bw.write("Date (dd-mm-yyyy),Calls,Cost,Seconds");
      bw.newLine();
      v = new Vector(dateSummary.keySet());
      Collections.sort(v, Collections.reverseOrder());
      for (Iterator it = v.iterator(); it.hasNext(); )
      {
        String key = (String)it.next();
        DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)dateSummary.get(key);
        bw.write(SQ + dtd.getDescription() + COMMA +
          //dtd.getTotalCalls() + COMMA + su.roundToString(dtd.getCost()/1000, 3) +
          dtd.getTotalCalls() + COMMA + dtd.getCostAsString() +
          COMMA + dtd.getDuration());
        bw.newLine();
        message = dba.createSummaryMgtRecord(dfControlId, "Date", dtd.getDescription(),
          JOBNAME, dtd.getTotalCalls(), (double)dtd.getCost()/1000, dtd.getDuration());
        if (message != null)
        {
          writeToLogFile("Error in writeSumSummaryFile(): " + message, true);
        }
      }
      bw.write("End of Report, , , , , , , , ,");
      bw.newLine();
      bw.close();
      filesToZip.add(outputFile);
    }
    catch (Exception ex)
    {
      writeToLogFile("Error in writeSumSummaryFile(): " + ex.getMessage(), true);
    }
  }

  private void summaryProcessing(String lineIn, String source, String cost,
    boolean spiritAccount)
  {
    long duration =(Long.parseLong(lineIn.substring(118, 123)) * 60) +
      Long.parseLong(lineIn.substring(123, 125));
    //double decCost = Double.parseDouble(cost.substring(0, 6) + "." +
      //cost.substring(6));
    long decCost = Long.parseLong(cost);
    summaryProcessing("band", bandSummary, lineIn.substring(141, 144), null,
      null, duration, decCost, source);
    String productCode = lineIn.substring(55, 58);
    boolean useField7 = false;
    for (int j = 0; j < FIELD7CODES.length; j++)
    {
      if (productCode.equals(FIELD7CODES[j]))
      {
        useField7 = true;
        break;
      }
    }
    String numberToUse = useField7?lineIn.substring(58, 70):
      lineIn.substring(21, 33);
    /*cliSummaryProcessing(numberToUse,
      (source.equals("VBS")?lineIn.substring(144, 174):
      lineIn.substring(144, lineIn.length())), productCode,
      duration, decCost, source);*/
    cliSummaryProcessing(cliSummary, numberToUse,
      (source.equals("VBS")?lineIn.substring(144, 174):
      lineIn.substring(144, lineIn.length())), productCode,
      duration, decCost, source);
    summaryProcessing("time", timeSummary, lineIn.substring(78, 80),
      null, null, duration, decCost, source);
    summaryProcessing("date", dateSummary, lineIn.substring(70, 78),
      lineIn.substring(76, 78) + "-" + lineIn.substring(74, 76) + "-" +
      lineIn.substring(70, 74), null, duration, decCost, source);
    if (spiritAccount)
    {
      int end = lineIn.length()>149?149:lineIn.length();
      summaryProcessing("spirit", spiritSummary,
        //lineIn.substring(144, lineIn.length()), numberToUse, null, 0, decCost,
        numberToUse, lineIn.substring(144, end), null, 0, decCost,
        source);
    }
  }

  private void summaryProcessing(String type, Hashtable summary, String id,
    String description, String productCode, long duration, /*double*/long cost,
    String source)
  {
    DataflowTotalDescriptor dtd = null;
    if (summary.containsKey(id))
    {
      dtd = (DataflowTotalDescriptor)summary.get(id);
      dtd.incrementTotalCalls();
      dtd.incrementDuration(duration);
      dtd.incrementCost(cost);
    }
    else
    {
      if (type.equals("band"))
      {
        description = dba.getDataflowBandDescription(id, source);
        if (description.startsWith("Error: "))
        {
          writeToLogFile("Error in summaryProcessing(): " + description, true);
          description = "unknown";
        }
        dtd = new DataflowTotalDescriptor(id, description, duration, cost);
      }
      else
      {
        dtd = new DataflowTotalDescriptor(id, description, productCode,
          duration, cost);
      }
      summary.put(id, dtd);
    }
  }

  private void cliSummaryProcessing(String id,
    String description, String productCode, long duration, /*double*/long cost,
    String source, long count)
  {
    if (dba.checkCli(id, productCode))
    {
      dba.updateCli(id, duration, cost, count);
    }
    else
    {
      dba.insertCli(id, description, productCode,
        duration, cost, count);
    }
  }

  private void cliSummaryProcessing(Hashtable summary, String id,
    String description, String productCode, long duration, /*double*/long cost,
    String source)
  {
    DataflowTotalDescriptor dtd = null;
    if (summary.containsKey(id+"/"+productCode))
    {
      dtd = (DataflowTotalDescriptor)summary.get(id+"/"+productCode);
      dtd.incrementTotalCalls();
      dtd.incrementDuration(duration);
      dtd.incrementCost(cost);
    }
    else
    {
      dtd = new DataflowTotalDescriptor(id, description, productCode,
        duration, cost);
      summary.put(id+"/"+productCode, dtd);
    }
    if (summary.size() > maxCli)
    {
      bigCli = true;
      writeCli(summary, source);
      summary.clear();
    }
  }

  private void writeCli(Hashtable summary, String source)
  {
    Enumeration en = summary.elements();
    while (en.hasMoreElements())
    {
      DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)en.nextElement();
      cliSummaryProcessing(dtd.getId(), dtd.getDescription(),
        dtd.getProductCode(), dtd.getDuration(), dtd.getCost(), source,
        dtd.getTotalCalls());
    }
  }

  private void writeSpiritDetail(String accountNo, java.util.Date billDate)
  {
    Enumeration en = spiritSummary.elements();
    while (en.hasMoreElements())
    {
      DataflowTotalDescriptor dtd = (DataflowTotalDescriptor)en.nextElement();
      //dba.insertSpiritDetail(dtd.getId(), dtd.getDescription(), dtd.getCost(),
      dba.insertSpiritDetail(dtd.getDescription(), dtd.getId(), dtd.getCost(),
        source, accountNo, su.DateToString(billDate, DATEFORMAT));
    }
  }

  private void incrementTotals(String mins, String secs, String cost)
  {

    durationMins += Long.parseLong(mins);
    durationSecs += Long.parseLong(secs);
    //totalCost += Double.parseDouble(cost.substring(0, 6) + "." +
      //cost.substring(6));
    totalCost += Long.parseLong(cost);
  }

  private void finaliseTotals()
  {
    totalDuration = (durationMins * 60) + durationSecs;
    durationSecs = totalDuration % 60;
    durationMins = (totalDuration - durationSecs) / 60;
  }

  private boolean checkTotals()
  {
    return (durationMins == difd.getDurationMins()) &&
      (durationSecs == difd.getDurationSecs()) &&
      //(totalCost == difd.getCost()) &&
      (cdrCount == difd.getTotalCalls());
  }

  private String reformatBASADaily(String lineIn, String billingDate,
    String cost)
  {
    String duration =
      String.valueOf((Long.parseLong(lineIn.substring(131, 136)) * 60) +
      Long.parseLong(lineIn.substring(136, 138)));
    String finalDuration = TENSPACES.substring(0, 9-duration.length()) + duration;
    int ratingPeriod = Integer.parseInt(lineIn.substring(138, 139));
    return DQ + lineIn.substring(9, 18) + DQCOMMADQ + lineIn.substring(18, 30) + //TENSPACES.substring(0, 3) +
      DQCOMMADQ + lineIn.substring(33, 52) + TENSPACES.substring(0, 5) +
      DQCOMMADQ + TENSPACES.substring(0, 2) + DQCOMMADQ + billingDate +
      DQCOMMADQ + lineIn.substring(30, 33) + DQCOMMADQ +
      lineIn.substring(52, 64) + DQCOMMADQ + lineIn.substring(116, 124) +
      DQCOMMADQ + lineIn.substring(124, 131) + DQCOMMADQ +
      lineIn.substring(96, 116) + DQCOMMADQ + lineIn.substring(84, 96) +
      DQCOMMADQ + RATINGPERIODS[ratingPeriod] + DQCOMMADQ +
      TENSPACES.substring(0, 5) + DQCOMMADQ + TENSPACES.substring(0, 5) +
      DQCOMMADQ + finalDuration + DQCOMMADQ +
      cost + DQCOMMADQ + TENZEROES.substring(0, 7) +
      DQCOMMADQ + lineIn.substring(148, 151) + DQCOMMADQ + THIRTYSPACES + DQ;
  }

  private String reformatMonthly(String lineIn, String type, String cost)
  {
    String duration =
      String.valueOf((Long.parseLong(lineIn.substring(118, 123)) * 60) +
      Long.parseLong(lineIn.substring(123, 125)));
    String finalDuration = TENSPACES.substring(0, 9-duration.length()) + duration;
    String serviceLocDesc = type.equals("VBS")?lineIn.substring(144, 174):
      (lineIn.substring(144, lineIn.length()) +
      THIRTYSPACES.substring(lineIn.length()-144));
    return DQ + lineIn.substring(0, 9) + DQCOMMADQ + lineIn.substring(9, 21) + //TENSPACES.substring(0, 3) +
      DQCOMMADQ + lineIn.substring(21, 45) +
      DQCOMMADQ + TENSPACES.substring(0, 2) + DQCOMMADQ + lineIn.substring(47, 55) +
      DQCOMMADQ + lineIn.substring(55, 58) + DQCOMMADQ +
      lineIn.substring(58, 70) + DQCOMMADQ + lineIn.substring(70, 78) +
      DQCOMMADQ + lineIn.substring(78, 85) + DQCOMMADQ +
      lineIn.substring(85, 105) + DQCOMMADQ + lineIn.substring(105, 117) +
      DQCOMMADQ + lineIn.substring(117, 118) + DQCOMMADQ +
      TENSPACES.substring(0, 5) + DQCOMMADQ + TENSPACES.substring(0, 5) +
      DQCOMMADQ + finalDuration + DQCOMMADQ +
      cost + DQCOMMADQ + lineIn.substring(134, 141) +
      DQCOMMADQ + lineIn.substring(141, 144) + DQCOMMADQ +
      serviceLocDesc + DQ;
  }

  private void finalise(String type, String accountNo,
    File outputFile, File outputDir, String outputFileName,
    java.util.Date billDate, String source, boolean spiritAccount)
  {
    Vector filesToZip = new Vector();
    String zipFileName = outputDir.getAbsolutePath() + File.separator +
      outputFileName + ".zip";
    finaliseTotals();
    String message = null;
    String successInd = "Y";
    if ((type.indexOf("Monthly") > 0) && (!checkTotals()))
    {
      successInd = "N";
      message = "control record totals mismatch for account " +
        difd.getAccountNo();
      writeToLogFile("Error in finalise() for " + type + ": " +
        message, true);
    }
    outputFileId = dba.createDataflowOutputRecord(zipFileName,
      new java.sql.Date(billDate.getTime()), cdrCount, durationMins,
      durationSecs, (double)totalCost/1000, JOBNAME, successInd, message);
    if (outputFileId <= 0)
    {
      writeToLogFile("Error in finalise() for " + type + ": " +
        dba.getMessage(), true);
    }
    message = dba.updateDataflowAccountControl(dfControlId, -1, outputFileId,
      cdrCount, durationMins, durationSecs, (double)totalCost/1000, JOBNAME);
    if (message != null)
    {
      writeToLogFile("Error in finalise() for " + type + ": " + message, true);
    }
    message = dba.updateDataflowInputRecord(inputFileId,
      new java.sql.Date(billDate.getTime()),
      (processingError?"N":"Y"), null, -1, -1, -1, -1, JOBNAME);
    if (message != null)
    {
      writeToLogFile("Error in finalise() for " + type + ": " + message, true);
    }
    if (type.indexOf("Monthly") > 0)
    {
      if (bigCli)
      {
        writeCli(cliSummary, source);
      }
      writeSummaryFiles(outputDir, outputFileName, filesToZip, dfControlId);
      bandSummary = new Hashtable();
      cliSummary = new Hashtable();
      timeSummary = new Hashtable();
      dateSummary = new Hashtable();
      if (spiritAccount)
      {
        writeSpiritDetail(accountNo, billDate);
      }
      spiritSummary = new Hashtable();
      bigCli = false;
    }
    filesToZip.add(outputFile);
    zipFile(filesToZip, zipFileName);
  }

  private void zipArchivedFiles(String type)
  {
    Vector filesToZip = new Vector();
    File arcDir = new File(archiveDir);
    String zipFileName = arcDir.getAbsolutePath() + File.separator +
      type + "_" + runTS + ".zip";
    if (type.equals("V_D"))
    {
      File[] list = arcDir.listFiles(new VDArchiveZipFileFilter());
      if (list.length > 1)
      {
        writeToLogFile("Error in zipArchivedFiles(): more than one unarchived " +
          "VBS Daily file", true);
      }
      else
      {
        list[0].renameTo(new File(zipFileName));
      }
    }
    else
    {
      File[] list = arcDir.listFiles(new ArchiveZipFileFilter());
      filesToZip.addAll(Arrays.asList(list));
      zipFile(filesToZip, zipFileName);
    }
  }

  private void initialiseFileIds()
  {
    processingError = false;
    inputFileId = 0;
    outputFileId = 0;
    dfControlId = 0;
  }

  private void archiveFile(File f, String archiveDir, String rejectDir)
  {
    if (processingError)
    {
      f.renameTo(new File(rejectDir + File.separator +
        f.getName()));
    }
    else if (archiveDir == null)
    {
      f.delete();
    }
    else
    {
      f.renameTo(new File(archiveDir + File.separator +
        f.getName()));
    }

  }

  private String reformatCost(String costIn)
  {
    String costOut = costIn;
    if (!su.isNumeric(costIn))
    {
      for (int j = 0; j < NEGATIVES.length; j++)
      {
        if (costIn.substring(costIn.length()-1).equals(NEGATIVES[j]))
        {
          costOut = "-" + costIn.substring(1, costIn.length()-1) +
            Integer.toString(j);
          break;
        }
      }
    }
    return costOut;
  }

  private boolean processFiles()
  {
    boolean success = false;
    if (dba.processAlreadyRunning(source, period))
    {
      writeToLogFile("DataflowControl already running for " + source + " " +
        period, true);
    }
    else
    {
      String message = dba.updateRunControl("Process Dataflow " + period +
        " Files", "Active", source);
      if (message == null)
      {
        if (source.equalsIgnoreCase(BASA))
        {
          if (period.equalsIgnoreCase(DAILY))
          {
            success = processBASADaily();
          }
          else
          {
            success = processBASAMonthly();
          }
        }
        else
        {
          if (period.equalsIgnoreCase(DAILY))
          {
            success = processVBSDaily();
          }
          else
          {
            success = processVBSMonthly();
          }
        }
        if (success)
        {
          message = dba.updateRunControl("Process Dataflow " + period +
            " Files", "Inactive", source);
        }
      }
      if (message != null)
      {
        writeToLogFile("Error updating Run_Control: " + message, true);
      }
    }
    return success;
  }

  private boolean archiveDaily()
  {
    boolean success = false;
    try
    {
      Collection fTA = dba.getDataflowArchiveFiles(Integer.parseInt(EBANProperties.getEBANProperty("archiveAfterDays")));
      if (!dba.getMessage().equals(""))
      {
        writeToLogFile("Error getting files to archive: " + dba.getMessage(),
          false);
      }
      if (!fTA.isEmpty())
      {
        SmartLongArray inputFileIds = new SmartLongArray();
        SmartLongArray outputFileIds = new SmartLongArray();
        SmartLongArray controlFileIds = new SmartLongArray();
        for (Iterator it = fTA.iterator(); it.hasNext(); )
        {
          DataflowArchiveFileDescriptor dafd =
            (DataflowArchiveFileDescriptor)it.next();
          inputFileIds.add(dafd.getInputFileId());
          outputFileIds.add(dafd.getOutputFileId());
          controlFileIds.add(dafd.getControlFileId());
        }

        //bulk delete from db
        String message = dba.deleteDataflowRecords(inputFileIds.toArray(),
          outputFileIds.toArray(), controlFileIds.toArray());
        if (message == null)
        {
          //loop round the files and move them
          for (Iterator it = fTA.iterator(); it.hasNext(); )
          {
            DataflowArchiveFileDescriptor dafd =
              (DataflowArchiveFileDescriptor)it.next();
            File f = new File(attachmentDir + File.separator +
              dafd.getFilePath().replace('/', File.separatorChar));
            String outPath = archiveDir + File.separator + dafd.getAccountNo();
            File newDir = new File(outPath);
            if (!newDir.exists())
            {
              newDir.mkdirs();
            }
            if (!f.renameTo(new File(outPath + File.separator + f.getName())))
            {
              writeToLogFile("Unable to move file " + f.getPath(), false);
            }
          }
          success = true;
        }
        else
        {
          writeToLogFile(message, false);
        }
      }
      else
      {
        writeToLogFile("No files to archive", false);
        success = true;
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error archiving files: " +
        ex.getMessage(), false);
    }
    finally
    {
      return success;
    }
  }

  private boolean archiveDaily2()
  {
    boolean success = false;
    try
    {
      Collection fTA = dba.getDataflowArchiveFiles(Integer.parseInt(EBANProperties.getEBANProperty("archiveAfterDays")));
      if (!dba.getMessage().equals(""))
      {
        writeToLogFile("Error getting files to archive: " + dba.getMessage(),
          false);
      }
      if (!fTA.isEmpty())
      {
        //loop round the files, delete db records and move them
        for (Iterator it = fTA.iterator(); it.hasNext(); )
        {
          DataflowArchiveFileDescriptor dafd =
            (DataflowArchiveFileDescriptor)it.next();
          String message = dba.deleteDataflowRecordsSingle(dafd.getInputFileId(),
            dafd.getOutputFileId(), dafd.getControlFileId());
          if (message == null)
          {
            File f = new File(attachmentDir + File.separator +
              dafd.getFilePath().replace('/', File.separatorChar));
            String outPath = archiveDir + File.separator + dafd.getAccountNo();
            File newDir = new File(outPath);
            if (!newDir.exists())
            {
              newDir.mkdirs();
            }
            if (!f.renameTo(new File(outPath + File.separator + f.getName())))
            {
              writeToLogFile("Unable to move file " + f.getPath(), false);
            }
          }
          else
          {
            writeToLogFile(message, false);
          }
        }
        success = true;
      }
      else
      {
        writeToLogFile("No files to archive", false);
        success = true;
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error archiving files: " +
        ex.getMessage(), false);
    }
    finally
    {
      return success;
    }
  }

  private boolean archiveMonthly()
  {
    boolean success = false;
    try
    {
      int monthsOld =Integer.parseInt(EBANProperties.getEBANProperty("archiveAfterMonths"));
      String archivePrefix = EBANProperties.getEBANProperty("archivePrefix") +
        "/";
      Collection fTA = dba.getDataflowMonthlyArchiveFiles(monthsOld, archivePrefix);
      if (!dba.getMessage().equals(""))
      {
        writeToLogFile("Error getting files to archive: " + dba.getMessage(),
          false);
      }
      if (!fTA.isEmpty())
      {
        //update db
        if (dba.updateDataflowMonthlyForArchive(monthsOld, archivePrefix))
        {
          //loop round the files and move them
          for (Iterator it = fTA.iterator(); it.hasNext(); )
          {
            String fullPath = (String)it.next();
            String allButName = fullPath.substring(0, fullPath.lastIndexOf(File.separator));
            String nameOnly = fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
            File f = new File(attachmentDir + File.separator + fullPath);
            String outPath = archiveDir + File.separator + allButName;
            File newDir = new File(outPath);
            if (!newDir.exists())
            {
              newDir.mkdirs();
            }
            if (!f.renameTo(new File(outPath + File.separator + nameOnly)))
            {
              writeToLogFile("Unable to move file " + f.getPath(), false);
            }
          }
          success = true;
        }
        else
        {
          writeToLogFile(dba.getMessage(), false);
        }
      }
      else
      {
        writeToLogFile("No files to archive", false);
        success = true;
      }
    }
    catch (Exception ex)
    {
      writeToLogFile("Error archiving files: " +
        ex.getMessage(), false);
    }
    finally
    {
      return success;
    }
  }

  private boolean archive()
  {
    boolean success = false;
    if (dba.processAlreadyRunning(BASA, source))
    {
      writeToLogFile("DataflowControl already running for " + source, false);
    }
    else
    {
      String message = dba.updateRunControl(source, "Active", BASA);
      if (message == null)
      {
        if (period.equals(MONTHLY))
        {
          success = archiveMonthly();
        }
        else
        {
          String archiveType = EBANProperties.getEBANProperty("archiveType", "none");
          if (archiveType.equals("single"))
          {
            success = archiveDaily2();
          }
          else
          {
            success = archiveDaily();
          }
        }
        if (success)
        {
          message = dba.updateRunControl(source, "Inactive", BASA);
        }
      }
      if (message != null)
      {
        writeToLogFile("Error updating Run_Control: " + message, false);
      }
    }
    return success;
  }

  private boolean createSpiritFile()
  {
    boolean success = true;
    try
    {
      Collection spiritDetail = dba.getSpiritDetail();
      if (!spiritDetail.isEmpty())
      {
        validDir = EBANProperties.getEBANProperty(EBANProperties.EBATTDIR);
        Properties spiritAccounts =
          EBANProperties.getEBANProperties(EBANProperties.SPIRITACC);
        boolean first = true;
        SpiritDescriptor sd = null;
        for (Iterator it = spiritDetail.iterator(); it.hasNext(); )
        {
          if (first)
          {
            sd = (SpiritDescriptor)it.next();
            first = false;
          }
          String storedAccNo = sd.getAccountNo();
          String storedSetId = spiritAccounts.getProperty(storedAccNo);
          String storedBS = sd.getBillingSource();
          String billingDate = su.DateToString(sd.getBillingDate(), DATEFORMAT);
          String billingDate2 = su.DateToString(sd.getBillingDate(), DATEFORMAT2);
          GregorianCalendar gc = new GregorianCalendar();
          gc.setTime(sd.getBillingDate());
          gc.add(gc.MONTH, 1);
          gc.add(gc.DATE, -1);
          String endDate = su.DateToString(gc.getTime(), DATEFORMAT2);
      //  get invoice
          String[] invoice = dba.getSpiritInvoice(storedAccNo, billingDate);
      //  create file
          String outputFileName = "B_M_" + storedAccNo + "_" + invoice[1] + "_" +
            billingDate;
          long[] ids = dba.getSpiritIds(storedAccNo, storedBS);
          long[] totals = dba.getSpiritTotalForAccount(storedAccNo, storedBS);
          File outputDir = new File(validDir);
          String outputPath = ENERGIS + File.separator +
            BASA + File.separator + ids[0] + File.separator + ids[1] +
            File.separator + ids[2] + File.separator + invoice[0];
          new File(outputDir.getAbsolutePath() + File.separator + outputPath).mkdirs();
          File outputFile = new File(outputDir.getAbsolutePath() + File.separator + outputPath,
            outputFileName + ".csv");
          outputFile.createNewFile();
          BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
      //  write header record
          String outputLine = DQ + OO1 + DQCOMMADQ + storedSetId + DQCOMMADQ + VSETID +
            DQCOMMADQ + VID + DQCOMMADQ + storedAccNo + DQ + COMMA +
            su.getLongAs2dpString(totals[1]) + COMMA + Long.toString(totals[0]);
          bw.write(outputLine);
          bw.newLine();
          long lineCount = 0;
          long grossTotal = 0;
          long vatTotal = 0;
          String lc = null;
      //  while same account
          while ((storedAccNo.equals(sd.getAccountNo())) &&
            (storedBS.equals(sd.getBillingSource())))
          {
            lc = Long.toString(++lineCount);
            grossTotal += sd.getGross();
            vatTotal += sd.getVat();
      //    write record
            outputLine = DQ + OO2 + DQ + COMMA + lc +
              COMMA + DQ + storedSetId + DQCOMMADQ + VSETID +
              DQCOMMADQ + VID + DQCOMMADQ + storedAccNo + DQCOMMADQ + sd.getCLI() +
              DQCOMMADQ + DQCOMMADQ + sd.getHouseCode() + DQCOMMADQ + invoice[1] +
              DASH + lc + DQ + COMMA + billingDate2 + COMMA + DQ + TEL + DQCOMMADQ +
              sd.getCLI() + DQCOMMADQ + GLACC + DQ + COMMA + billingDate2 + COMMA +
              endDate + COMMA + sd.getGrossAsString() + COMMA + sd.getVatAsString() +
              COMMA + DQ + DQ + COMMA + COMMA + DQ + DQ + COMMA + COMMA + DQ +
              CALLCHARGES + DQ;
            bw.write(outputLine);
            bw.newLine();
            if (it.hasNext())
            {
              sd = (SpiritDescriptor)it.next();
            }
            else
            {
              break;
            }
          }
      //  write rounding record
          String grossRounding = "0.00";
          String vatRounding = "0.00";
          if ((totals[1] != grossTotal) || (totals[2] != vatTotal))
          {
            long grossDiff = totals[1] - grossTotal;
            grossRounding = su.getLongAs2dpString(grossDiff);
            long vatDiff = totals[2] - vatTotal;
            vatRounding = su.getLongAs2dpString(vatDiff);
          }
          lc = Long.toString(++lineCount);
          outputLine = DQ + OO2 + DQ + COMMA + lc +
            COMMA + DQ + storedSetId + DQCOMMADQ + VSETID +
            DQCOMMADQ + VID + DQCOMMADQ + storedAccNo + DQCOMMADQ + ROUNDING +
            DQCOMMADQ + DQCOMMADQ + C9701 + DQCOMMADQ + invoice[1] +
            DASH + lc + DQ + COMMA + billingDate2 + COMMA + DQ + TEL + DQCOMMADQ +
            ROUNDING + DQCOMMADQ + GLACC + DQ + COMMA + billingDate2 + COMMA +
            endDate + COMMA + grossRounding + COMMA + vatRounding +
            COMMA + DQ + DQ + COMMA + COMMA + DQ + DQ + COMMA + COMMA + DQ +
            CORRECTION + DQ;
          bw.write(outputLine);
          bw.newLine();
      //  close file
          bw.close();
      //  write to db
          success = dba.insertAttachment("invoice", Long.parseLong(invoice[0]),
            "CDR Summary", (outputPath+File.separator+outputFileName+".csv")
            .replace(File.separator.charAt(0), '/'), "cdr",
            "batch");
        }
      //delete records
        if (success)
        {
        //success = dba.deleteSpiritDetail();
        }
      }
    }
    catch (Exception ex)
    {
      success = false;
      writeToLogFile("Error in createSpiritFile(): " + ex.getMessage(), true);
    }
    finally
    {
      return success;
    }
  }

  private boolean spiritProcessing()
  {
    boolean success = false;
    if (dba.processAlreadyRunning(BASA, source))
    {
      writeToLogFile("DataflowControl already running for " + source, false);
    }
    else
    {
      String message = dba.updateRunControl(source, "Active", BASA);
      if (message == null)
      {
        success = createSpiritFile();
        if (success)
        {
          message = dba.updateRunControl(source, "Inactive", BASA);
        }
      }
      if (message != null)
      {
        writeToLogFile("Error updating Run_Control: " + message, false);
      }
    }
    return success;
  }

  private static String extractSource(String[] args)
  {
    String source = null;
    if (args.length == 0)
    {
      System.out.println("No billing source or process given. Valid values are: BASA VBS ARCHIVE SPIRIT");
      System.exit(1);
    }
    source = args[0];
    if (!(source.equalsIgnoreCase(BASA) || source.equalsIgnoreCase(VBS) ||
      source.equalsIgnoreCase(ARCHIVE) || source.equalsIgnoreCase(SPIRIT) ||
      source.equalsIgnoreCase("test")))
    {
      System.out.println("Invalid billing source or process given. Valid values are: BASA VBS ARCHIVE SPIRIT");
      System.exit(1);
    }
    return source;
  }

  private static String extractPeriod(String[] args)
  {
    String period = null;
    if (args.length == 1)
    {
      System.out.println("No period given. Valid values are: DAILY MONTHLY");
      System.exit(1);
    }
    period = args[1];
    if (!(period.equalsIgnoreCase(DAILY) || period.equalsIgnoreCase(MONTHLY)))
    {
      System.out.println("Invalid period given. Valid values are: DAILY MONTHLY");
      System.exit(1);
    }
    return period;
  }

  public static void main(String[] args)
  {
    DataflowControl dc = null;
    boolean success = false;
    String source = extractSource(args);
    if (source.equalsIgnoreCase("test"))
    {
      dc = new DataflowControl();
      dc.testDBConnection();
      /*Properties test = EBANProperties.getEBANProperties(EBANProperties.FIELD7);
      String[] array = (String[])test.values().toArray(new String[test.size()]);

      for (int i = 0; i < array.length; i++)
      {
        System.out.println(array[i]);
      }*/
    }
    else if (source.equalsIgnoreCase(ARCHIVE))
    {
      String period = extractPeriod(args);
      dc = new DataflowControl(source, period, true);
      success = dc.archive();
    }
    else if (source.equalsIgnoreCase(SPIRIT))
    {
      dc = new DataflowControl(source);
      success = dc.spiritProcessing();
    }
    else
    {
      String period = extractPeriod(args);
      dc = new DataflowControl(source, period);
      success = dc.processFiles();
    }
    dc.closeLogFile();
    if (!success)
    {
      dc.renameLogFile();
    }
  }

}



