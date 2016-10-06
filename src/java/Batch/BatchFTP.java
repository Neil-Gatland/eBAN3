package Batch;

import java.io.*;
import com.enterprisedt.net.ftp.*;
import JavaUtil.*;
/**
 *
 *
 * <P>Copyright (c) 2003  Danet Ltd</P>
 *
 * @author Tim Aitchison
 * @version 1.0
 */

public class BatchFTP
{

  private File inputFile;
  private boolean weekly;
  private String fileName;
  private String newName;
  private FTPClient ftp = null;
  private String ftpServerName = null;
  private int ftpServerPort = 0;
  private String ftpUser = null;
  private String ftpPassword = null;
  private String ftpPassive = null;
  private String ftpSite = null;
  private String ftpType = null;
  private String ftpServerDir = null;
  private String ftpTempDir = null;
  private BufferedWriter logWriter;
  private File logFile;
  private final String DATETIMEFORMAT = "dd/MM/yyyy HH:mm:ss";
  private final String TIMESTAMPFORMAT = "yyyyMMddHHmmssSSS";
  private String logDir;

  private BatchFTP()
  {
  }

  public BatchFTP(String type, String name)
  {
    String inputDir = null;
    if ((type.equals("SAP")) || (type.equals("ACCTSAP")))
    {
      inputDir =
        EBANProperties.getEBANProperty("SAPFTP.ftpInputDir", "e:\\sapfeed");
      logDir =
        EBANProperties.getEBANProperty("SAPFTP.ftpLogDir", "e:\\sapfeed");
      ftpServerName =
        EBANProperties.getEBANProperty("SAPFTP.ftpServerName", "10.196.137.88"); //"testserver";
      ftpServerPort = Integer.parseInt(
        EBANProperties.getEBANProperty("SAPFTP.ftpServerPort", "21")); //"21";
      ftpUser =
        EBANProperties.getEBANProperty("SAPFTP.ftpUser", "wmp_ftp"); //"B2BFTP";
      ftpPassword =
        EBANProperties.getEBANProperty("SAPFTP.ftpPassword", "wmp_ftp"); //"B2BFTP";
      ftpPassive =
        EBANProperties.getEBANProperty("SAPFTP.ftpPassive"); //not there
      ftpSite =
        EBANProperties.getEBANProperty("SAPFTP.ftpSite"); //"VARrecfm Lrecl=1004 Recfm=VB BLKSIZE=0"
      ftpType =
        EBANProperties.getEBANProperty("SAPFTP.ftpType", "ASCII"); //"ASCII"
      ftpServerDir =
        EBANProperties.getEBANProperty("SAPFTP.ftpServerDir", "/apps/wm/comdata/ftp/gcb"); //"deFTP"
      ftpTempDir =
        EBANProperties.getEBANProperty("SAPFTP.ftpTempDir"); //"deCalls"
    }
    else
    {
    }
    if (!name.equals("connect_only"))
    {
      fileName = name;
      String localFile = inputDir + File.separator + name;
      inputFile = new File(localFile);
    }
    try
    {
      logFile = new File(logDir + File.separator + type + "_FTP_log.txt");
      logWriter = new BufferedWriter(new FileWriter(logFile));
    }
    catch (Exception ex)
    {
      System.out.println("Error creating log file: " + ex.getMessage());
      System.exit(1);
    }
  }

 private void closeLogFile()
  {
    try
    {
      logWriter.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error closing log file: " + ex.getMessage());
      System.exit(1);
    }
  }

  private void writeToLogFile(String message)
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

  public boolean transfer()
  {
    boolean fileTransferred = false;
    if (inputFile.exists())
    {
      try
      {
        try
        {
          initFTP();
        }
        catch (FTPException fe)
        {
          writeToLogFile("BatchFTP Error: FTP Error while establishing connection: "
                                    + fe.getReplyCode() + "  "+ fe.getMessage());
        }
        catch (IOException ioe)
        {
          writeToLogFile("BatchFTP Error: IO Error while establishing connection: "
                                    + ioe.getMessage());
        }
        try
        {
          fileTransferred = transferFile();
          }
          catch (FTPException fe)
          {
            writeToLogFile("BatchFTP Error: FTP Error during file transfer: " +
              fe.getReplyCode() + "  "+ fe.getMessage());
          }
          catch (Exception ex)
          {
            writeToLogFile("BatchFTP Error: File transfer error: " + ex.getMessage());
          }
          finally
          {
            closeFTP();
          }
      }
      finally
      {
        closeFTP();
      }
    }
    else
    {
      writeToLogFile("BatchFTP Error: Input file does not exist");
    }
    return fileTransferred;
  }

    /**
   * Close the FTP connection.
   *
   * <P>If an error occures, it is caught and a warning is logged; otherwise a success
   * message is logged. The {@link #ftp} member is always set to <TT>null</TT>.</P>
   */
  private void closeFTP()
  {
    if (ftp != null)
    {
      try
      {
        ftp.quit();
        writeToLogFile("Closed FTP connection");
      }
      catch (Exception ex)
      {
        writeToLogFile("BatchFTP Warning: Exception while closing FTP connection: '" + ex + "'.");
      }
      ftp = null;
    }
  }

  /**
   * Initialize the FTP connection.
   *
   * <P>Establishes the control connection and logs in with the user name given in the property
   * <TT>CallLink.ftpUser</TT> and the password given in <TT>CallLink.ftpPassword</TT>.
   * Thereafter global FTP commands commands are sent including <TT>PASSIVE</TT>
   * (if the property <TT>CallLink.ftpPassive</TT> is not empty), <TT>SITE</TT>
   * (if the property <TT>CallLink.ftpSite</TT> is not empty), and <TT>TYPE</TT>
   * ("<TT>ASCII</TT>" if the property <TT>CallLink.ftpType</TT> is set to this value,
   * "<TT>BINARY</TT>" otherwise).</P>
   * <P>If an error occurs, the connection is closed and the {@link #ftp} member is set to
   * <TT>null</TT>, otherwise the FTP connection object is stored in {@link #ftp}.</P>
   *
   * @return  <TT>true</TT> if the connection was initialized successfully,
   *          <TT>false</TT> if an error occured
   */
  private boolean initFTP()
        throws FTPException, IOException
  {
    boolean success = false;
    try
    {
      writeToLogFile("Opening FTP connection");
      ftp = new FTPClient(ftpServerName, ftpServerPort);
      ftp.login(ftpUser, ftpPassword);
      if (ftpPassive != null)
      {
        ftp.setConnectMode(FTPConnectMode.PASV);
      }
      if (ftpSite != null)
      {
        ftp.site(ftpSite);
      }
      if (ftpType != null)
      {
        ftp.setType("ASCII".equalsIgnoreCase(ftpType)? FTPTransferType.ASCII
                                                     : FTPTransferType.BINARY);
      }
      if (ftpServerDir != null)
      {
        ftp.chdir(ftpServerDir);
      }
      writeToLogFile("Connection established");
      success = true;
    }
    catch (FTPException fe)
    {
      fe.fillInStackTrace();
      if (ftp != null)
      {
        closeFTP();
      }
      throw fe;
    }
    return success;
  }

  /**
   * An exception for file transfer errors.
   */
  public static class TransferException extends Exception
  {
    TransferException(String s)
    {
      super(s);
    }
  }

  private boolean transferFile()
        throws IOException, FTPException
  {
    boolean ok = false;

    if (ftp == null) {
      initFTP();
    }

    writeToLogFile("Transferring " + fileName);
    ftp.put(inputFile.getAbsolutePath(), fileName);
    if (newName != null)
    {
      writeToLogFile("Renaming to " + newName);
      ftp.rename(fileName, newName);
    }
    writeToLogFile("Transfer successful");
    ok = true;

    return ok;
  }

  public static void main(String[] args)
  {
    //String step = extractStep(args);
    //String fileName = extractFileName(args);
    BatchFTP bf = null;
    if (args.length == 2)
    {
      try
      {
        bf = new BatchFTP(args[0], args[1]);
        if (args[1].equals("connect_only"))
        {
          bf.initFTP();
          bf.closeFTP();
        }
        else
        {
          bf.transfer();
        }
        bf.closeLogFile();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        System.err.println();
      }
      finally
      {
      }
    }
  }
}