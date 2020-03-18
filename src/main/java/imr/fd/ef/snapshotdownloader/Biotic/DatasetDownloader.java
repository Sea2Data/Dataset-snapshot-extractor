package imr.fd.ef.snapshotdownloader.Biotic;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import no.imr.formats.nmdbiotic.v3.MissionType;
import no.imr.formats.nmdbiotic.v3.MissionsType;
import no.imr.formats.nmdbiotic.v3.ObjectFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Constructs a dataset search, and optionally specifies data versions
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DatasetDownloader {
    
    protected String url;
    protected BioticConnectionV3 connection;
    protected Date date;
    protected File outputfile;
    protected Set<String> missiontypes = new HashSet<>();
    protected Set<Integer> years = new HashSet<>();
    protected Set<String> options = new HashSet<>();
    
    public DatasetDownloader(){
        this.options.add("-O");
        this.options.add("-H");
        this.options.add("-Y");
        this.options.add("-M");
        this.options.add("-D");
    }
    
    /**
     * Set url to connect to, must be NMDbiotic v. 3 compliant.
     * @param url 
     */
    public void setUrl(String url){
        this.url = url;
    }
    
    public void setOutputFile(String filepath) {
        this.outputfile = new File(filepath);
    }

    
    /**
     * Set date for restriction snapshots.
     * The last snapshot preceeding this date will be used.
     * @param date 
     */
    public void setDate(Date date){
        this.date = date;
    }
    
    /**
     * Add a missiontype to the set of missiontypes that search is restricted to.
     * @param missiontype 
     */
    public void addMissionType(Integer missiontype){
        this.missiontypes.add(missiontype.toString());
    }
    
    /**
     * Add a year to the set of years that search is restricted to.
     * @param year 
     */
    public void addYear(Integer year){
        this.years.add(year);
    }
    
    protected void parseUrl(String url){
        if (this.options.contains(url)){
            throw new RuntimeException("Malformed argument: " + url + " (expected url).");
        }

        this.url = url;
    }
    
    protected void parseYears(String[] args, int index) throws ParseException{
        for (int i=index; i<args.length; i++){
            if (this.options.contains(args[i])){
                this.parseArgs(args, i);
                break;
            } 
            else{
                try{
                    this.addYear(Integer.parseInt(args[i]));
                }
                catch (NumberFormatException e){
                    throw new RuntimeException("Error parsing years. Got:" + args[i]);
                }
            }
        }
    }
    protected void parseMissionTypes(String[] args, int index) throws ParseException{
        for (int i=index; i<args.length; i++){
            if (this.options.contains(args[i])){
                this.parseArgs(args, i);
                break;
            }
            else{
                this.addMissionType(Integer.parseInt(args[i]));
            }
        }
    }
    protected void parseDate(String[] args, int index) throws ParseException{
        String dateString = args[index];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.setDate(format.parse(dateString));
        this.parseArgs(args, index+1);
    }
    protected void parseFilename(String[] args, int index) throws ParseException{
        this.setOutputFile(args[index]);
        this.parseArgs(args, index+1);
    }
    
    protected void parseArgs(String[] args, int index) throws ParseException{
        if (args.length < 1){
            this.printUsage(System.out);
        }
        else if (index >= args.length){
        }
        else if (index == 0){
            this.parseUrl(args[0]);
            this.parseArgs(args, index+1);
        }
        else if (this.options.contains(args[index])){
            switch(args[index]){
                case "-H" : this.printUsage(System.out);
                break;
                case "-Y" : this.parseYears(args, index+1);
                break;
                case "-M" : this.parseMissionTypes(args, index+1);
                break;
                case "-D" : this.parseDate(args, index+1);
                break;
                case "-O" : this.parseFilename(args, index+1);
                break;
                default : assert false;
                break;
            }
                
        }
        else{
            throw new RuntimeException("Malformed argument: " + args[index]);
        }
    }
    
    protected void printUsage(PrintStream s){
        s.println("Extract collections of data sets from snapshots, with optional restriction on snapshot date.");
        s.println("Extract is based on generated snapshopts, so restriction dates prior to rollout of snapshot system will give files.");
        s.println("Snapshots are not necesarilly created for empty missions (missions with no fishstations) these are also silently skipped.");
        s.println("Usage: <url> <-Y> <year 1> ... <year n> <-M> <missiontype> ... <missiontype> [-D yyyy-MM-DD] <-O> <outputfile>");
        s.println("-H: Print this help message.");
        s.println("-Y: The years to include in collection.");
        s.println("-M: The mission types to include in collection.");
        s.println("-D: optional. If included the latest snapshot preceeding this date will be used for each dataset. And only datasets present before this date will be included.");
        s.println("-O: output file");
    }
    
    // list snapshots for data set
    // throws a SnapshotCriteriaException if snapshot can not be found or no snapshot meet criteria.
    protected String selectSnapshot(String dataset) throws SnapshotCriteriaException, IOException, JAXBException, BioticAPIException{
        Map<String, Date> setSnapshots = null;
        try{
            setSnapshots = this.connection.listSnapshots(dataset);
        } catch(BioticAPIException e){
            if (e.getResponseMessage().contains("No snapshots found.")){
                throw new SnapshotCriteriaException("No snapshot found for data set " + dataset);
            } else{
                throw e;
            }
        }
        Map.Entry<String, Date> newestEntry = null;
        for (Map.Entry<String, Date> entry: setSnapshots.entrySet()){
                
                // date is not restricted, or the date is before the restriction 
                if (this.date == null || entry.getValue().before(this.date)){
                    
                    // date is newer then newestentry
                    if (newestEntry == null || entry.getValue().after(newestEntry.getValue())) {
                        newestEntry = entry;
                    }
                    
                }
            }
            
            if (newestEntry == null){
                throw new SnapshotCriteriaException("No snapshot meet the criteria for data set " + dataset);
            }
        return newestEntry.getKey();
    }
    
    // only returns snapshots for the data avaialbe on the restriction date.
    protected Map<String, String> selectSnapshots(Set<String> datasets) throws IOException, JAXBException, BioticAPIException{
        Map<String, String> snapshots = new HashMap<>();
        
        for (String s : datasets){
            try{
                String snapshot = this.selectSnapshot(s);
                snapshots.put(s, snapshot);
            }
            catch (SnapshotCriteriaException e){
                
            }
        }
        return snapshots;
    }
    
    protected MissionsType fetchSnapshots(Map<String, String> snapshots) throws BioticAPIException, JAXBException, IOException{
        ObjectFactory of = new ObjectFactory();
        MissionsType missions = of.createMissionsType();
        
        for (Map.Entry<String, String> entry : snapshots.entrySet()){
            MissionType mission = this.connection.getSnapshot(entry.getKey(), entry.getValue());
            missions.getMission().add(mission);
        }
        
        return(missions);
    }
    
    protected void save(MissionsType collection, File outputfile) throws PropertyException, JAXBException, FileNotFoundException {
            
        JAXBContext jc = JAXBContext.newInstance(collection.getClass());
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        OutputStream stream = new FileOutputStream(outputfile);
        m.marshal(collection, stream);
        
    }
    
    public void run(String[] args) throws URISyntaxException, IOException, JAXBException, BioticAPIException, BioticParsingException, ParseException, RunException{
        
        this.parseArgs(args, 0);
        if (this.url == null || this.missiontypes.isEmpty() || this.years.isEmpty() ){
            System.err.println("...");
            System.err.println("Data sets not sufficiently specified. Aborting.");
            throw new RunException("Configuration issues");
        }
        
        this.connection = new BioticConnectionV3(this.url);
        Set<String> datasets = connection.findDataSets(this.years, this.missiontypes);
        Map<String, String> snapshots = this.selectSnapshots(datasets);
        MissionsType collection = this.fetchSnapshots(snapshots);
        this.save(collection, this.outputfile);
    }
    
    public static MissionsType readDataSet(File xml) throws FileNotFoundException, XMLStreamException, JAXBException{
        FileInputStream f = new FileInputStream(xml);
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLEventReader xmler = xmlif.createXMLEventReader(f);

        JAXBContext jc = JAXBContext.newInstance(MissionsType.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        MissionsType toplevel = MissionsType.class.cast(unmarshaller.unmarshal(xmler));
        return toplevel;
    }
    
    public static void main(String[] args) throws Exception{
        try{
            DatasetDownloader m = new DatasetDownloader();
            m.run(args);    
        }
        catch (RunException e){
            System.exit(1);
        }
        
    }
    
}
