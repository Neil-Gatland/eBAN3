package Batch;

import java.io.*;

public class deleteBVDailyFiles
{
  // class variables
  public static String mode;
  public static String type;
  public static String startDir;
  public String startPath;

  private deleteBVDailyFiles()
  {
  }

  private void control()
  {
    int scanCount = 0, deleteCount = 0;
    startPath = startDir+File.separator+type;
    File topFolder = new File(startPath);
    if (topFolder.exists())
    {
      // build customer folder file array and scan for payment group folders
      File[] customerArray = topFolder.listFiles();
      if (customerArray!=null)
      {
        for (int c = 0; c < customerArray.length; c++)
        {
          File cFile = customerArray[c];
          String cFileName = cFile.getName();
          // check that customer file is a folder, build payment group
          // folder array and scan for account folders
          if (cFile.isDirectory())
          {
            File[] paymentGroupArray = cFile.listFiles();
            if (paymentGroupArray!=null)
            {
              for (int pg = 0; pg < paymentGroupArray.length; pg++)
              {
                File pgFile = paymentGroupArray[pg];
                String pgFileName = pgFile.getName();
                // check that payment group file is a folder, build account
                // folder array and scan for files
                if (pgFile.isDirectory())
                {
                  File[] accountArray = pgFile.listFiles();
                  if (accountArray!=null)
                  {
                    for (int a = 0; a < accountArray.length; a++)
                    {
                      File aFile = accountArray[a];
                      String aFileName = aFile.getName();
                      // check that account file is a folder, build file array
                      // and process files
                      if (aFile.isDirectory())
                      {
                        // build file array and look for daily files
                        File[] fileArray = aFile.listFiles();
                        if (fileArray!=null)
                        {
                          for (int f = 0; f < fileArray.length; f++)
                          {
                            File testFile = fileArray[f];
                            String testFileName = testFile.getName();
                            // check that file is a file and not a directory
                            if (testFile.isFile())
                            {
                              boolean dailyFile = false;
                              if ((type.startsWith("BASA"))&&(testFileName.startsWith("B_D")))
                                dailyFile = true;
                              if ((type.startsWith("VBS"))&&(testFileName.startsWith("V_D")))
                                dailyFile = true;
                              if (dailyFile)
                              {
                                //System.out.println
                                //  (startDir+File.separator+
                                //   type+File.separator+
                                //   cFileName+File.separator+
                                //   pgFileName+File.separator+
                                //   aFileName+File.separator+
                                //   testFileName);
                                scanCount++;
                                if (mode.startsWith("delete"))
                                //
                                {
                                  if (testFile.delete())
                                    deleteCount++;
                                  else
                                    System.out.println("Failed to delete file "+testFile.getAbsolutePath());
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      System.out.println(scanCount+" daily files found");
      if (deleteCount>0)
        System.out.println(deleteCount+" daily files deleted");
    }
    else
      System.out.println("Top level folder "+startPath+" does not exist");

  }

  private static boolean getParameters(String[] args)
  {
    boolean ok = true;
    if (args.length!=3)
      ok=false;
    else
    {
      if ((args[0].startsWith("scan"))||(args[0].startsWith("delete")))
        mode = args[0];
      else
        ok = false;
    }
    if (ok)
    {
      if (args[1].length()>0)
        startDir=args[1];
      else
        ok = false;
    }
    if (ok)
    {
      if (args[2].startsWith("BASA"))
        type = "BASA";
      else if (args[2].startsWith("VBS"))
        type = "VBS";
      else
        ok = false;
    }
    return ok;
  }

  public static void main(String[] args)
  {
    if (getParameters(args))
    {
      deleteBVDailyFiles dVBdf = new deleteBVDailyFiles();
      dVBdf.control();
    }
    else
      System.out.println("Supplied parameters missing or invalid (mode, directory, type)");
  }

}



