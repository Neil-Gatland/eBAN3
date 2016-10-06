package JavaUtil;


import java.io.*;
import java.net.*;
import java.util.*;


/**
 * A static class for read-only access to the eBAN properties.
 *
 * <P>The properties are looked for in the following locations:</P>
 * <OL>
 *   <LI>If the system property <TT>EBillPropertiesFile</TT> is set, its value is interpreted
 *       as a file path and the properties are read from the according file.
 *   </LI>
 *   <LI>The class loader that loaded the <TT>EBillProperties</TT> class is asked for the
 *       resource <TT>ebill.properties</TT>. This means that the properties file has to be
 *       in the root directory of a jar file or in a directory contained in the classpath.
 *   </LI>
 * </OL>
 *
 * <P>A call to the {@link #reload() reload()} method reloads the properties.</P>
 * <P>To get a notification each time the properties file is reloaded,
 * {@link #addReloadListener(EBillProperties.ReloadListener) add} a
 * {@link ReloadListener} to this class.</P>
 * <P>Example:</P>
 * <PRE>class WorkingClass {
 *
 *   String workingPlace = null;
 *
 *   WorkingClass() {
 *     init();
 *     EBillProperties.addReloadListener(new ReloadListener() {
 *       public void reload() {
 *         init();
 *       }
 *     });
 *   }
 *
 *   void init() {
 *     workingPlace = EBillProperties.getProperty("Work.Location", "nowhere");
 *   }
 *
 *   ...
 * }</PRE>
 *
 * <P>Copyright (c) 2002  Danet Ltd</P>
 *
 * @author Werner Vollmer
 * @version 1.0
 */

public class EBANProperties extends Properties {

  /**
   * Listener for properties reload.
   */
  public static interface ReloadListener {

    /**
     * This method is called each time the properties file is reloaded.
     */
    public void reload();
  }


  /** The listeners list */
  private static ArrayList listeners = new ArrayList();


  /** The eBAN properties singleton */
  private static EBANProperties properties = null;

  /** A reload synchronisation object */
  private static Object reloadSync = new Object();

  /** A flag indicating that a reload is in progress */
  private static boolean reloadFlag = false;


  /** The default properties file name (<TT>ebill.properties</TT>) */
  public static final String DEFAULT_PROPERTIES_FILE_NAME = "eban.properties";

  /** The system property name where to read the file path from (<TT>EBANPropertiesFile</TT>) */
  public static final String PROPERTIES_FILE_NAME_SYSTEM_PROPERTY = "EBANPropertiesFile";

  /** Properties which can be read from the file */
  public static final String SHAREDPATH = "sharedPath";
  public static final String P3SERVERNAME = "p3ServerName";
  public static final String P4SERVERNAME = "p4ServerName";
  public static final String P5SERVERNAME = "p5ServerName";
  public static final String EDIFYSERVERNAME = "edifyServerName";
  public static final String P3CONNECTIONS = "p3Connections";
  public static final String P4CONNECTIONS = "p4Connections";
  public static final String P5CONNECTIONS = "p5Connections";
  public static final String MAXACCOUNTS = "maxAccounts";
  public static final String SUPPORTEMAIL = "supportEmail";
  public static final String PROCESSEMAIL = "processEmail";
  public static final String USERPATH = "userPath";
  public static final String VALIDPATH = "validPath";
  public static final String DROPDIR = "dropDir";
  public static final String REJECTDIR = "rejectDir";
  public static final String PROCESSDIR = "processDir";
  public static final String ENVIRONMENT = "environment";
  public static final String SERVERNAME = "serverName";
  public static final String CONNECTIONS = "connections";
  public static final String BASADCONTROLDIR = "basaDControlDir";
  public static final String BASADDROPDIR = "basaDDropDir";
  public static final String BASADVALIDDIR = "basaDValidDir";
  public static final String VBSDCONTROLDIR = "vbsDControlDir";
  public static final String VBSDDROPDIR = "vbsDDropDir";
  public static final String VBSDVALIDDIR = "vbsDValidDir";
  public static final String BASAMCONTROLDIR = "basaMControlDir";
  public static final String BASAMDROPDIR = "basaMDropDir";
  public static final String BASAMVALIDDIR = "basaMValidDir";
  public static final String VBSMCONTROLDIR = "vbsMControlDir";
  public static final String VBSMDROPDIR = "vbsMDropDir";
  public static final String VBSMVALIDDIR = "vbsMValidDir";
  public static final String VBSMZIPDIR = "vbsMZipDir";
  public static final String VBSDZIPDIR = "vbsDZipDir";
  public static final String ORACLEDBNAME = "oracleDBName";
  public static final String ZIPPATH = "zipPath";
  public static final String DFLOGDIR = "dfLogDir";
  public static final String VBSMARCDIR = "vbsMArcDir";
  public static final String VBSDARCDIR = "vbsDArcDir";
  public static final String BASAMARCDIR = "basaMArcDir";
  public static final String BASADARCDIR = "basaDArcDir";
  public static final String FIELD7 = "field7";
  public static final String DFATTDIR = "dfAttDir";
  public static final String DFARCDIR = "dfArcDir";
  public static final String DFMARCDIR = "dfMArcDir";
  public static final String SPIRITACC = "spiritAcc";
  public static final String EBATTDIR = "ebAttDir";
  public static final String VATRATE = "vatRate";
  public static final String AALOGDIR = "aaLogDir";
  public static final String AAINDIR = "aaInDir";
  public static final String AAOUTDIR = "aaOutDir";
  public static final String ALINDIR = "alInDir";
  public static final String ALOUTDIR = "alOutDir";
  public static final String ALREJECTDIR = "alRejectDir";
  public static final String ANINDIR = "anInDir";
  public static final String ANOUTDIR = "anOutDir";
  public static final String ANREJECTDIR = "anRejectDir";
  public static final String EZDROPDIR = "ezDropDir";
  public static final String EZRECYCDIR = "ezRecycleDir";
  public static final String EZINVALIDDIR = "ezInvalidDir";
  public static final String EZLOGDIR = "ezLogDir";
  public static final String EZWORKDIR = "ezWorkDir";
  public static final String EZATTACHDIR = "ezAttachDir";
  public static final String SIDROPDIR = "splitImpactDropDir";
  public static final String SISPLITDIR = "splitImpactSplitDir";
  public static final String SILOGDIR = "splitImpactLogDir";
  public static final String SIPROCDIR = "splitImpactProcessedDir";
  public static final String SIWR = "splitImpactWadReplace";
  public static final String LSIDROPDIR = "loadSSBSInvoicesDropDir";
  public static final String LSILOGDIR = "loadSSBSInvoicesLogDir";
  public static final String LSIATTACHDIR = "loadSSBSInvoicesAttachDir";
  public static final String PFDLDROPDIR = "produceFTPDownloadListDropDir";
  public static final String PFDLLOGDIR = "produceFTPDownloadListLogDir";
  public static final String PFDLPROCDIR = "produceFTPDownloadListProcDir";
  public static final String PFDLCOMMDIR = "produceFTPDownloadListCommDir";
  public static final String PFDLCDRDIR = "produceFTPDownloadListCDRDir";
  public static final String PFDLTCPIP = "produceFTPDownloadListTCPIP";
  public static final String PFDLUSERNAME = "produceFTPDownloadListUsername";
  public static final String PFDLPASSWORD = "produceFTPDownloadListPassword";
  public static final String PSCFDOWNDIR = "processSSBSCDRFilesDownDir";
  public static final String PSCFCDRDIR = "processSSBSCDRFilesCDRDir";
  public static final String PSCFINVALIDDIR = "processSSBSCDRFilesInvDir";
  public static final String PSCFLOGDIR = "processSSBSCDRFilesLogDir";
  public static final String PSCFWORKDIR = "processSSBSCDRFilesWorkDir";
  public static final String PSCFWORK2DIR = "processSSBSCDRFilesWork2Dir";
  public static final String PSCFATTACHDIR = "processSSBSCDRFilesAttachDir";
  public static final String PSCFMAXSIZE = "processSSBSCDRFilesMaxSize";
  public static final String ICPLOGDIR = "iptCDRProcessingLogDir";
  public static final String ICPDROPDIR = "iptCDRProcessingDropDir";
  public static final String ICPREJDIR = "iptCDRProcessingRejDir";
  public static final String ICPPROCDIR = "iptCDRProcessingProcDir";
  public static final String ICPATTACHDIR = "iptCDRProcessingAttachDir";
  public static final String ICPSUMSWITCH = "iptCDRProcessingSummarySwitch";
  public static final String ICPWORKDIR = "iptCDRProcessingWorkDir";
  public static final String UASLCDDROPDIR ="updateAccountSLCDDropDir";
  public static final String UASLCDPROCDIR ="updateAccountSLCDProcDir";
  public static final String UASLCDLOGDIR ="updateAccountSLCDLogDir";
  public static final String CDRDELLOGDIR ="cdrDeletionLogDir";
  public static final String CDRDELBILLSOURCE ="cdrDeletionBillingSource";
  public static final String CDRDELMODE ="cdrDeletionMode";
  public static final String CDRDELATTACHPATH ="cdrDeletionAttachmentPath";
  public static final String CDRDELARCHIVEATTACHPATH ="cdrDeletionArchiveAttachmentPath";
  public static final String CDRDELDESTPATH ="cdrDeletionDestinationPath";
  public static final String CDRDELDAILYPERIOD ="cdrDeletionDailyPeriod";
  public static final String CDRDELMONTHLYPERIOD ="cdrDeletionMonthlyPeriod";
  public static final String ATTACHDELLOGDIR ="attachmentDeletionLogDir";
  public static final String ATTACHDELATTACHPATH ="attachmentDeletionAttachmentPath";
  public static final String ATTACHDELARCHIVEATTACHPATH ="attachmentDeletionArchiveAttachmentPath";
  public static final String ATTACHDELDESTPATH ="attachmentDeletionDestinationPath";
  public static final String ATTACHDELREPORTARCHIVE="attachmentDeletionReportArchive";
  public static final String NGIDLOGDIR="nonGCBInvoiceDeletionLogDir";
  public static final String NGIDATTACHDIR="nonGCBInvoiceDeletionAttachmentDir";
  public static final String NGIDATTACH2DIR="nonGCBInvoiceDeletionAttachment2Dir";
  public static final String NGIDATTACHARCHIVEDIR="nonGCBInvoiceDeletionAttachmentArchiveDir";
  public static final String NGIDDESTPATH="nonGCBInvoiceDeletionDestinationPath";
  public static final String NGIDNOMONTHS="nonGCBInvoiceDeletionNumberMonths";
  public static final String NGIDREPORTARCHIVE="nonGCBInvoiceDeletionReportArchive";
  public static final String ARBORCDRLOGDIR="arborCDRLogDir";
  public static final String ARBORCDRDROPDIR="arborCDRDropDir";
  public static final String ARBORCDRPROCDIR="arborCDRProcDir";
  public static final String ARBORCDRREJDIR="arborCDRRejDir";
  public static final String ARBORCDRATTACHDIR="arborCDRAttachDir";
  public static final String ARBORCDRWORKDIR="arborCDRWorkDir";
  public static final String ARBORCDRWORK2DIR="arborCDRWork2Dir";
  public static final String ARBORCDRORPHANDIR="arborCDROrphanDir";
  public static final String ARBORCDRFAILDIR="arborCDRFailDir";
  public static final String ARBORCDRMAXRECORDSCOUNT="arborCDRMaxRecordsCount";
  public static final String REVSHARELOGDIR="revShareLogDir";
  public static final String REVSHAREJNLDIR="revShareJournalDir";
  public static final String REVSHAREDROPDIR="revShareDropDir";
  public static final String REVSHAREARCHIVEDROPDIR="revShareArchiveDropDir";
  public static final String REVSHAREINVOICEDIR="revShareInvoiceDir";
  public static final String REVSHAREUPLOADDIR="revShareUploadDir";
  public static final String REVSHAREIGNOREDIR="revShareIgnoreDir";
  public static final String PROCESSRSFILESLOGDIR="processRSFilesLogDir";
  public static final String PROCESSRSFILESDROPDIR="processRSFilesDropDir";
  public static final String PROCESSRSFILESPROCDIR="processRSFilesProcDir";
  public static final String PROCESSRSFILESATTACHDIR="processRSFilesAttachDir";
  public static final String SPIDROPDIR = "splitPCBImpactDropDir";
  public static final String SPISPLITDIR = "splitPCBImpactSplitDir";
  public static final String SPSISPLITDIR = "splitPCBSSBSImpactSplitDir";
  public static final String SPILOGDIR = "splitPCBImpactLogDir";
  public static final String SPIPROCDIR = "splitPCBImpactProcessedDir";
  public static final String LPIDROPDIR = "loadPCBInvoicesDropDir";
  public static final String LPILOGDIR = "loadPCBInvoicesLogDir";
  public static final String LPIATTACHDIR = "loadPCBInvoicesAttachDir";
  public static final String PBFDROPDIR = "processBIERFilesDropDir";
  public static final String PBFLOGDIR = "processBIERFilesLogDir";
  public static final String PBFPROCDIR = "processBIERFilesProcessedDir";
  public static final String LDAPINITIALCONTEXTFACTORY = "ldapInitialContextFactory";
  public static final String LDAPPROVIDERURL = "ldapProviderURL";
  public static final String LDAPSECURITYAUTHENTICATION = "ldapSecurityAuthentication";
  public static final String LDAPDOMAIN = "ldapDomain";
  public static final String LDAPDOMAINLIST = "ldapDomainList";
  public static final String LDAPSEARCHBASE = "ldapSearchBase";
  public static final String LDAPSEARCHOBJECTCLASS = "ldapSearchObjectClass";
  public static final String LDAPSEARCHIDENTIFIER = "ldapSearchIdentifier";
  public static final String LDAPMAILIDENTIFIER = "ldapMailIdentifier";
  public static final String LDAPENVIRONMENT = "ldapEnvironment";
  public static final String LDAPLOCALSECURITYPRINCIPAL = "ldapLocalSecurityPrincipal";
  public static final String LDAPLOCALUID = "ldapLocalUID";

  static {
    try {
      reload();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex.getMessage());
    }
  }



  /**
   * Add a reload listener to get notifications when the properties file is reloaded.
   *
   * @param  listener  the reload listener to add
   */
  public static void addReloadListener(ReloadListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove a reload listener.
   *
   * @param  listener  the reload listener to remove
   */
  public static void removeReloadListener(ReloadListener listener) {
    listeners.remove(listener);
  }


  /**
   * Get an eBAN property.
   *
   * @param  key  the property key
   * @return  the property string or <TT>null</TT> is it was not accessible
   */
  public static String getEBANProperty(String key)
  {
    return properties.getProperty(key);
  }


  /**
   * Get an eBAN property.
   *
   * @param  key  the property key
   * @param  defaultValue  the default value if the key does not exist
   * @return  the property string or <TT>null</TT> is it was not accessible
   */
  public static String getEBANProperty(String key, String defaultValue)
  {

    return properties.getProperty(key, defaultValue);
  }


  /**
   * Get all properties where the key starts with the given prefix.
   *
   * <P>The matching properties are copied to a new {@link Properties} object where the
   * given prefix is removed from the key names.</P>
   *
   * @param  key  the property key
   * @return  the property string or <TT>null</TT> is it was not accessible
   */
  public static Properties getEBANProperties(String keyPrefix)
  {
    Properties pp = new Properties();
    int prefixLength = keyPrefix.length();

    for (Enumeration keyEn = properties.keys(); keyEn.hasMoreElements(); ) {
      String key = keyEn.nextElement().toString();
      if (key.startsWith(keyPrefix) && key.length() > prefixLength) {
        pp.put(key.substring(prefixLength), properties.get(key));
      }
    }
    return pp;
  }


  /**
   * Reload the properties file and notify all listeners of the reload.
   *
   * <P>If the properties have already been loaded and the reload fails, the old properties
   * are preserved.</P>
   */
  public static void reload()
        throws IOException
  {
    boolean doReload = false;
    synchronized (reloadSync) {
      if (!reloadFlag) {
        reloadFlag = true;
        doReload = true;
      }
    }
    if (doReload) {
      try {
        EBANProperties newProperties = new EBANProperties();
        EBANProperties oldProperties = properties;
        properties = newProperties;
        for (Iterator it = listeners.iterator(); it.hasNext(); ((ReloadListener)it.next()).reload());
        if (oldProperties != null) {
          oldProperties.clear();
        }
      } finally {
        reloadFlag = false;
      }
    }
  }



  /**
   * Create the properties singleton.
   */
  private EBANProperties()
        throws IOException
  {
    InputStream propStream = null;
    String filePath = "";
    filePath = System.getProperty(PROPERTIES_FILE_NAME_SYSTEM_PROPERTY);
    String msg = null;
    if (filePath == null) {
      msg = "Loading properties file '" + DEFAULT_PROPERTIES_FILE_NAME + "' from classpath.";
      propStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE_NAME);
    } else {
      msg = "Loading properties file '" + filePath + "'.";
      File propertiesFile = new File(filePath);
      if (!propertiesFile.exists()) {
        throw new FileNotFoundException("Cannot find the properties file '"
                                        + propertiesFile.getCanonicalPath() + "'");
      }
      propStream = new FileInputStream(propertiesFile);
    }
    load(new BufferedInputStream(propStream));
    System.out.println(msg);
  }

}
