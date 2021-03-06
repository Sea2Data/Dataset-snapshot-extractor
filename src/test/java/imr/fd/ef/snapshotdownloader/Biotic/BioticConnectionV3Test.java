/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdbiotic.v3.MissionType;
import no.imr.formats.nmdcommon.v2.ListType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3Test {

    protected String url;
    protected String urlencoding = "UTF-8";

    public BioticConnectionV3Test() throws Exception {
        url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
    }

    /**
     * Test of get method, of class BioticConnectionV3.
     */
    @org.junit.Test
    public void testGet() throws Exception {
        System.out.println("get");
        String path = "/Forskningsfart�y/2018/G.O.Sars_LMEL";
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        InputStream result = instance.get(path, null);

        BufferedReader br = new BufferedReader(new InputStreamReader(result));

        boolean hasbody = false;
        while ((br.readLine()) != null) {
            hasbody = true;
        }
        assert (hasbody);
        instance.disconnect();

    }


    /**
     * Test of listAll method, of class BioticConnectionV3.
     */
    // @Test commented out to avoid traffic on API
    public void testListAll() throws Exception {
        System.out.println("ListAll");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        ListType result = instance.listAll();
        assertFalse(result.getRow().isEmpty());
    }

    /**
     * Test of findDataSets method, of class BioticConnectionV3.
     */
    @Test
    public void testFindDataSets_5args() throws Exception {
        System.out.println("findDataSets");
        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<String> platforms = null;
        Set<Integer> missionnumbers = null;
        Set<String> cruises = new HashSet<>();
        cruises.add("1972002");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        Set<String> result = instance.findDataSets(years, missiontypes, platforms, missionnumbers, cruises);
        for (String s : result) {
            System.out.println(s);
        }
        assertEquals(2, result.size());
    }

    /**
     * Test of findDataSets method, of class BioticConnectionV3.
     */
    @Test
    public void testFindDataSets_Set_Set() throws Exception {
        System.out.println("findDataSets");
        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        Set<String> result = instance.findDataSets(years, missiontypes);
        for (String s : result) {
            System.out.println(s);
        }
        assertEquals(3, result.size());
    }

    /**
     * testGetSnapshot
     *
     * @throws Exception
     */
    @Test
    public void testSnapshot() throws Exception {
        System.out.println("testSnapshot");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        MissionType mission = instance.getSnapshot("Forskningsfart�y/2017/G.O.Sars_LMEL/2017150", "2018-06-05T22.00.00.096Z");
        System.out.println(mission.getCruise());
        assertTrue(mission != null);
    }
    
       /**
     * testGetSnapshot
     *
     * @throws Exception
     */
    @Test
    public void testListSnapshots() throws Exception {
        System.out.println("testListSnapshots");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        Map<String, Date> snapshots = instance.listSnapshots("Forskningsfart�y/2017/G.O.Sars_LMEL/2017150");
        assertTrue(snapshots.size() > 0);
        Set<Map.Entry<String, Date>> entries = snapshots.entrySet();
        Map.Entry<String, Date> entry = entries.iterator().next();
        String id = entry.getKey();
        Date date = entry.getValue();
        System.out.print(id + ":" + date.toString());
        
    }
    
    /**
     * testGetCatchsample
     *
     * @throws Exception
     */
    @Test
    public void testGetCatchSample() throws Exception {
        System.out.println("testGetCatchsample");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        CatchsampleType cs = instance.getCatchSample("Forskningsfart�y/2013/G.O.Sars_LMEL/2013111", 2774, 15);
        System.out.println(cs.getCommonname());
        assertTrue(cs != null);
    }

}
