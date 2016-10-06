package JavaUtil;

import java.io.FilenameFilter;

public class VDArchiveZipFileFilter implements FilenameFilter
{
  public boolean accept(java.io.File f, String name)
  {
    boolean acceptable = false;
    return (!name.toUpperCase().startsWith("V_D"));
  }
}
