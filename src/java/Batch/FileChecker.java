
package Batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileChecker 
{      
    // class variables
    
    // class constants
    
    private FileChecker()
    {
       // ????        
    }
    
    private void checkFile(String filename)
    {
       // Check that the file exists
      File cFile = new File(filename);
      long recordCount = 0, statusCount = 0;
      if (cFile.exists())
      {
         try
         {
            BufferedReader cfr = new BufferedReader(new FileReader(cFile));
            String cfrLine = cfr.readLine(), prevLine = "";
            int fileSuffix = 1;
            String outFilename = "CSVfile"+fileSuffix+".csv";
            File wFile = new File(outFilename);
            BufferedWriter wFileWriter = new BufferedWriter(new FileWriter(wFile));
            while (!(cfrLine==null))
            {
              if (statusCount==3000000)
              {
                  wFileWriter.close();
                  wFile = null;
                  fileSuffix++;
                  outFilename = "CSVfile"+fileSuffix+".csv";
                  wFile = new File(outFilename);
                  wFileWriter = new BufferedWriter(new FileWriter(wFile));
                  statusCount = 0;
              }
              wFileWriter.write(cfrLine);
              wFileWriter.newLine();
              prevLine = cfrLine;
              cfrLine = cfr.readLine();  
              recordCount++;
              statusCount++;
            }
            cfr.close();
            wFileWriter.close();
            System.out.println(prevLine.trim());
         }
        catch(Exception Ex)
        {
          System.out.println(Ex.getMessage()+" for file "+filename);
        }
        //System.out.println("Successfully read file "+filename+" : "+recordCount+" records processed");
      }
      else
          System.out.println("File "+filename+" does not exist");
      
    }
           
    public static void main(String[] args)
    {
       FileChecker fc = new FileChecker(); 
       if (args.length>0)
            fc.checkFile(args[0]);
       else
           System.out.println("Please supply a filename");
       fc = null;
    }
    
}
