package JavaUtil;

import java.io.FilenameFilter;

public class ArchiveZipFileFilter implements FilenameFilter
{
  public boolean accept(java.io.File f, String name)
  {
    boolean acceptable = false;
    return (!name.toLowerCase().endsWith(".zip"));
  }
}
