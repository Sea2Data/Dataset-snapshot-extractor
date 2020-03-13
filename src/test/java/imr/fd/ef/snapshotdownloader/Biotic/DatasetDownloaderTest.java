/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import no.imr.formats.nmdbiotic.v3.MissionsType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DatasetDownloaderTest {
    
    public DatasetDownloaderTest() {
    }

    /**
     * Test of setUrl method, of class DatasetDownloader.
     */
    @Test
    public void testSetUrl() {
        System.out.println("setUrl");
        String url = "";
        DatasetDownloader instance = new DatasetDownloader();
        assertTrue(instance.url == null);
        instance.setUrl(url);
        assertTrue(instance.url != null);
    }

    /**
     * Test of setOutputFile method, of class DatasetDownloader.
     */
    @Test
    public void testSetOutputFile() {
        System.out.println("setOutputFile");
        String filepath = "";
        DatasetDownloader instance = new DatasetDownloader();
        assertTrue(instance.outputfile == null);
        instance.setOutputFile(filepath);
        assertTrue(instance.outputfile != null);
    }

    /**
     * Test of setDate method, of class DatasetDownloader.
     */
    @Test
    public void testSetDate() {
        System.out.println("setDate");
        Date date = new Date();
        DatasetDownloader instance = new DatasetDownloader();
        assertTrue(instance.date == null);
        instance.setDate(date);
        assertTrue(instance.date != null);
    }

    /**
     * Test of addMissionType method, of class DatasetDownloader.
     */
    @Test
    public void testAddMissionType() {
        System.out.println("addMissionType");
        Integer missiontype = 13;
        DatasetDownloader instance = new DatasetDownloader();
        assertTrue(instance.missiontypes.size() == 0);
        instance.addMissionType(missiontype);
        assertTrue(instance.missiontypes.size() == 1);
        instance.addMissionType(13);
        assertTrue(instance.missiontypes.size() == 1);
        instance.addMissionType(14);
        assertTrue(instance.missiontypes.size() == 2);
    }

    /**
     * Test of addYear method, of class DatasetDownloader.
     */
    @Test
    public void testAddYear() {
        System.out.println("addYear");
        Integer year = 2019;
        DatasetDownloader instance = new DatasetDownloader();
        instance.addYear(year);
        assertTrue(instance.years.size() == 1);
        instance.addYear(year);
        assertTrue(instance.years.size() == 1);
        instance.addYear(2020);
        assertTrue(instance.years.size() == 2);
    }

    /**
     * Test of parseUrl method, of class DatasetDownloader.
     */
    @Test
    public void testParseUrl() {
        System.out.println("parseUrl");
        String url = "";
        DatasetDownloader instance = new DatasetDownloader();
        assertTrue(instance.url == null);
        instance.parseUrl(url);
        assertTrue(instance.url != null);
    }

    /**
     * Test of parseYears method, of class DatasetDownloader.
     */
    @Test
    public void testParseYears() throws Exception {
        System.out.println("parseYears");
        String[] args = new String[3];
        args[1] = "2019";
        args[2] = "1919";
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseYears(args, 1);
        assertTrue(instance.years.size() == 2);
    }

    /**
     * Test of parseMissionTypes method, of class DatasetDownloader.
     */
    @Test
    public void testParseMissionTypes() throws Exception {
        System.out.println("parseMissionTypes");
        String[] args = new String[3];
        args[1] = "3";
        args[2] = "3";
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseMissionTypes(args, 1);
        assertTrue(instance.missiontypes.size() == 1);
    }

    /**
     * Test of parseDate method, of class DatasetDownloader.
     */
    @Test
    public void testParseDate() throws Exception {
        System.out.println("parseDate");
        String[] args = new String[3];
        args[1] = "-D";
        args[2] = "2019-02-03";
        int index = 2;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseDate(args, index);
        assertTrue(instance.date != null);
        assertTrue(instance.date.getMonth() == 1);
        assertTrue(instance.date.getYear() == 119);
    }

    /**
     * Test of parseFilename method, of class DatasetDownloader.
     */
    @Test
    public void testParseFilename() throws Exception {
        System.out.println("parseFilename");
        String[] args = new String[3];
        args[1] = "-O";
        args[2] = "fn/ft";
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseFilename(args, 2);
        assertTrue(instance.outputfile.getName().equals("ft"));
        assertTrue(instance.outputfile.getParent().equals("fn"));
    }

    /**
     * Test of parseArgs method, of class DatasetDownloader.
     */
    @Test
    public void testParseArgs() throws Exception {
        System.out.println("parseArgs");
        String[] args = new String[12];
        args[0] = "url";
        args[1] = "-D";
        args[2] = "2009-03-02";
        args[3] = "-Y";
        args[4] = "1919";
        args[5] = "1920";
        args[6] = "1919";
        args[7] = "-O";
        args[8] = "filen";
        args[9] = "-M";
        args[10] = "3";
        args[11] = "4";
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseArgs(args, 0);
        assertTrue(instance.url.equals("url"));
        assertTrue(instance.date.getMonth() == 2);
        assertTrue(instance.date.getYear() == 109);
        assertTrue(instance.years.size() == 2);
        assertTrue(instance.missiontypes.size() == 2);
        assertTrue(instance.outputfile.getName().equals("filen"));
    }
    
    @Test
    public void testParseArgs2() throws Exception {
        System.out.println("parseArgs2");
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File targetfolder = folder.newFolder();
        
        String[] args = new String[7];
        args[0] = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        args[1] = "-Y";
        args[2] = "2018";
        args[3] = "-M";
        args[4] = "19";
        args[5] = "-O";
        args[6] = targetfolder.getAbsolutePath() + File.separator + "test.xml";

        DatasetDownloader instance = new DatasetDownloader();
        instance.parseArgs(args, 0);
        assertTrue(instance.url.equals(args[0]));
        assertTrue(instance.years.size() == 1);
        assertTrue(instance.missiontypes.size() == 1);
        assertTrue(instance.outputfile.getName().equals("test.xml"));
    }

    /**
     * Test of printUsage method, of class DatasetDownloader.
     */
    @Test
    public void testPrintUsage() {
        System.out.println("printUsage");
        PrintStream s = System.out;
        DatasetDownloader instance = new DatasetDownloader();
        instance.printUsage(s);
    }

    /**
     * Test of selectSnapshots method, of class DatasetDownloader.
     */
    public void testSelectSnapshots() throws Exception {
        System.out.println("selectSnapshots");
        Set<String> datasets = null;
        DatasetDownloader instance = new DatasetDownloader();
        Map<String, String> expResult = null;
        Map<String, String> result = instance.selectSnapshots(datasets);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchSnapshots method, of class DatasetDownloader.
     */
    public void testFetchSnapshots() throws Exception {
        System.out.println("fetchSnapshots");
        Map<String, String> snapshots = null;
        DatasetDownloader instance = new DatasetDownloader();
        MissionsType expResult = null;
        MissionsType result = instance.fetchSnapshots(snapshots);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class DatasetDownloader.
     */
    public void testSave() throws Exception {
        System.out.println("save");
        MissionsType collection = null;
        File outputfile = null;
        DatasetDownloader instance = new DatasetDownloader();
        instance.save(collection, outputfile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    @Test
    public void testRun() throws Exception {
        
        System.out.println("run");
        DatasetDownloader instance = new DatasetDownloader();
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File targetfolder = folder.newFolder();
        
        String[] args = new String[7];
        args[0] = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        args[1] = "-Y";
        args[2] = "2018";
        args[3] = "-M";
        args[4] = "19";
        args[5] = "-O";
        args[6] = targetfolder.getAbsolutePath() + File.separator + "test.xml";
        instance.run(args);

        MissionsType t = DatasetDownloader.readDataSet(new File(args[6]));
        assertTrue(t.getMission().get(0).getMissiontype().equals("19"));
        assertTrue(t.getMission().get(0).getFishstation().size()>1);
        
    }
    
    //@Test
    public void testRunDate() throws Exception {
        
        System.out.println("run");
        DatasetDownloader instance = new DatasetDownloader();
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File targetfolder = folder.newFolder();
        
        String[] args = new String[7];
        args[0] = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        args[1] = "-Y";
        args[2] = "2018";
        args[3] = "-M";
        args[4] = "19";
        args[5] = "-O";
        args[6] = targetfolder.getAbsolutePath() + File.separator + "test.xml";
        instance.run(args);

        MissionsType t = DatasetDownloader.readDataSet(new File(args[6]));
        assertTrue(t.getMission().get(0).getMissiontype().equals("19"));
        assertTrue(t.getMission().get(0).getFishstation().size()>1);
        fail("Prototype. Add check with date, against predownloaded snapshots.");
    }
    
}
