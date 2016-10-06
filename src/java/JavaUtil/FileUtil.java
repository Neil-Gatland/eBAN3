package JavaUtil;

import java.io.*;

  public class FileUtil {

    public FileUtil()
    {
    }
     //--------------------------------------------------< main >--------//

     public static void main (String[] args)
     {
     }

     public BufferedReader openFile(String FileName)
     {

        String record = null;
        int recCount = 0;
	FileReader fr =  null;
	BufferedReader br = null;

        try
	{
	   fr = new FileReader(FileName);
           br = new BufferedReader(fr);
        }
	catch (IOException e)
	{
           // catch possible io errors from readLine()
           System.out.println(e.getMessage());
           //e.printStackTrace();
	   //return
        }
	return br;
     }
     public String readNext(BufferedReader br)
     {
        String record = new String();

        try
	{
           if ((record = br.readLine()) != null)
	   {
              return record;
           }
	   else
	   {
	      return "EOF";
	   }
        } catch (IOException e)
	{
           return "Error: "+ (e.getMessage());
           //e.printStackTrace();
        }
     }
  } // end of class