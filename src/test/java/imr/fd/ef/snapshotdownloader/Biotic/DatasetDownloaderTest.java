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
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        String[] args = null;
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseYears(args, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseMissionTypes method, of class DatasetDownloader.
     */
    @Test
    public void testParseMissionTypes() throws Exception {
        System.out.println("parseMissionTypes");
        String[] args = null;
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseMissionTypes(args, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseDate method, of class DatasetDownloader.
     */
    @Test
    public void testParseDate() throws Exception {
        System.out.println("parseDate");
        String[] args = new String[3];
        args[1] = "-D";
        args[2] = "2019-02-02";
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseDate(args, index);
        assertTrue(instance.date.getMonth() == 1);
    }

    /**
     * Test of parseFilename method, of class DatasetDownloader.
     */
    @Test
    public void testParseFilename() throws Exception {
        System.out.println("parseFilename");
        String[] args = null;
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseFilename(args, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseArgs method, of class DatasetDownloader.
     */
    @Test
    public void testParseArgs() throws Exception {
        System.out.println("parseArgs");
        String[] args = null;
        int index = 0;
        DatasetDownloader instance = new DatasetDownloader();
        instance.parseArgs(args, index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printUsage method, of class DatasetDownloader.
     */
    @Test
    public void testPrintUsage() {
        System.out.println("printUsage");
        PrintStream s = null;
        DatasetDownloader instance = new DatasetDownloader();
        instance.printUsage(s);
    }

    /**
     * Test of selectSnapshots method, of class DatasetDownloader.
     */
    @Test
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
    @Test
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
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        MissionsType collection = null;
        File outputfile = null;
        DatasetDownloader instance = new DatasetDownloader();
        instance.save(collection, outputfile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class DatasetDownloader.
     */
    @Test
    public void testRun() throws Exception {
        System.out.println("run");
        DatasetDownloader instance = new DatasetDownloader();
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class DatasetDownloader.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        DatasetDownloader.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
