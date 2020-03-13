/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.snapshotdownloader.Biotic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdcommon.v2.ListElementType;
import no.imr.formats.nmdcommon.v2.ListType;
import no.imr.formats.nmdcommon.v2.RowElementType;

/**
 * adapter to API handles fetching and updating
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3{

    protected String scheme = "http";
    protected String host;
    protected String path;
    protected Integer port = 8080;
    protected HttpURLConnection conn; // prevents implementing serializable
    protected String urlencoding = "UTF-8";

    public BioticConnectionV3(String url) throws URISyntaxException {
        URI uri = new URI(url);
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
    }
    
    /**
     * Convenience method for getting biotic connections to serializable classes
     * @param url
     * @return
     * @throws URISyntaxException 
     */
    public static BioticConnectionV3 createBiotic3Conncetion(String url) throws URISyntaxException{
        return new BioticConnectionV3(url);
    }

    /**
     * Creates http connection at port 8080
     *
     * @param host
     * @param path
     */
    public BioticConnectionV3(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public BioticConnectionV3(String scheme, String host, String path, Integer port) {
        this.scheme = scheme;
        this.host = host;
        this.path = path;
        this.port = port;
    }

    protected String handleResponse(HttpURLConnection conn) throws IOException{
        BufferedReader in = null;
            in = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
    }

    protected InputStream get(String path, String query) throws IOException, BioticAPIException {

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        URI uri;
        try {
            uri = new URI(this.scheme, null, this.host, this.port, this.path + path, query, null);
        } catch (URISyntaxException ex) {
            throw new RuntimeException("malformed uri");
        }

        URL url = new URL(uri.toASCIIString());
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml; charset=UTF-8");

        Integer response = conn.getResponseCode();

        if (response != 200) {
            String responsemsg = conn.getResponseMessage();
            responsemsg += handleResponse(conn);
            
            throw new BioticAPIException(response, uri, responsemsg);
        }

        return conn.getInputStream();
    }

    protected void disconnect() {
        if (this.conn != null) {
            this.conn.disconnect();
        }
    }

    /**
     * List all data sets Format described in
     * https://confluence.imr.no/display/API/Biotic+V3+API+documentation
     *
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws BioticAPIException
     */
    public ListType listAll() throws IOException, JAXBException, BioticAPIException {

        ListType all = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdcommon.v2.ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream inStream = this.get("", "type=ListAll");
            all = ((JAXBElement<ListType>) jaxbUnmarshaller.unmarshal(inStream)).getValue();
            inStream.close();
        } finally {
            this.disconnect();

        }

        return all;
    }

    /**
     * Find data sets in biotic API with data field values found in the
     * corresponding arguments (all sets). If a null-reference is passed in
     * stead of a set it is interpreted as "all values permitted". Note that not
     * all variables are keys. Unconstrained variables that are not keys might
     * be missing, and will then be permitted.
     *
     * @param years
     * @param missiontypes
     * @param platforms
     * @param missionnumbers
     * @param cruises
     * @return path to data sets in API
     */
    protected Set<String> findDataSets(Set<Integer> years, Set<String> missiontypes, Set<String> platforms, Set<Integer> missionnumbers, Set<String> cruises) throws IOException, JAXBException, BioticAPIException, BioticParsingException {
        ListType all = this.listAll();
        Set<String> paths = new HashSet<>();
        for (RowElementType row : all.getRow()) {
            boolean keep = true;
            String path = null;
            for (ListElementType e : row.getElement()) {
                String name = e.getName();
                if (name.equals("startyear") && years != null && !years.contains(Integer.parseInt(e.getValue()))) {
                    keep = false;
                }
                if (name.equals("missiontype") && missiontypes != null && !missiontypes.contains(e.getValue())) {
                    keep = false;
                }
                if (name.equals("platform") && platforms != null && !platforms.contains(e.getValue())) {
                    keep = false;
                }
                if (name.equals("missionnumber") && missionnumbers != null && !missionnumbers.contains(Integer.parseInt(e.getValue()))) {
                    keep = false;
                }
                if (name.equals("cruise") && cruises != null && !cruises.contains(e.getValue())) {
                    keep = false;
                }
                if (name.equals("path")) {
                    path = e.getValue();
                }
            }
            if (keep) {
                if (path == null) {
                    throw new BioticParsingException("Path not given for data set:" + row.toString());
                }
                paths.add(path);
            }
        }
        return paths;
    }

    public Set<String> findDataSets(Set<Integer> years, Set<String> missiontypes) throws IOException, JAXBException, JAXBException, BioticAPIException, BioticParsingException {
        return findDataSets(years, missiontypes, null, null, null);
    }

    public List<no.imr.formats.nmdbiotic.v3.FishstationType> listFishstation(String missionpath) throws IOException, BioticAPIException, JAXBException {

        no.imr.formats.nmdbiotic.v3.ListType all = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdbiotic.v3.ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream inStream = this.get(missionpath + "/model/mission/fishstation", "version=3.0");
            Source source = new StreamSource(inStream);
            all = ((JAXBElement<no.imr.formats.nmdbiotic.v3.ListType>) jaxbUnmarshaller.unmarshal(source, no.imr.formats.nmdbiotic.v3.ListType.class)).getValue();
            inStream.close();
        } catch (BioticAPIException e) {
            if (e.getResponse() == 404) {
                return new LinkedList<no.imr.formats.nmdbiotic.v3.FishstationType>();
            } else {
                throw e;
            }
        } finally {
            this.disconnect();

        }

        LinkedListConverter<no.imr.formats.nmdbiotic.v3.FishstationType> ll = new LinkedListConverter<>();

        return ll.toLinkedList(all);
    }

    public List<CatchsampleType> listCatchsamples(String missionpath, BigInteger serialn) throws IOException, BioticAPIException, JAXBException {
        no.imr.formats.nmdbiotic.v3.ListType all = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdbiotic.v3.ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream inStream = this.get(missionpath + "/model/mission/fishstation/" + serialn.toString() + "/catchsample", "version=3.0");
            Source source = new StreamSource(inStream);
            all = ((JAXBElement<no.imr.formats.nmdbiotic.v3.ListType>) jaxbUnmarshaller.unmarshal(source, no.imr.formats.nmdbiotic.v3.ListType.class)).getValue();
            inStream.close();
        } catch (BioticAPIException e) {
            if (e.getResponse() == 404) {
                return new LinkedList<CatchsampleType>();
            } else {
                throw e;
            }
        } finally {
            this.disconnect();

        }

        LinkedListConverter<no.imr.formats.nmdbiotic.v3.CatchsampleType> ll = new LinkedListConverter<>();

        return ll.toLinkedList(all);
    }

    /**
     * Get catchsample with given address
     *
     * @param missionpath
     * @param serialnumber
     * @param catchsampleid
     * @return
     * @throws JAXBException
     * @throws IOException
     * @throws BioticAPIException
     */
    public CatchsampleType getCatchSample(String missionpath, Integer serialnumber, Integer catchsampleid) throws JAXBException, IOException, BioticAPIException {
        no.imr.formats.nmdbiotic.v3.CatchsampleType cs = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdbiotic.v3.ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream inStream = this.get(missionpath + "/model/mission/fishstation/" + serialnumber.toString() + "/catchsample/" + catchsampleid.toString(), "version=3.0");
            Source source = new StreamSource(inStream);
            cs = ((JAXBElement<no.imr.formats.nmdbiotic.v3.CatchsampleType>) jaxbUnmarshaller.unmarshal(source, no.imr.formats.nmdbiotic.v3.CatchsampleType.class)).getValue();
            inStream.close();
        } catch (BioticAPIException e) {
            if (e.getResponse() == 404) {
                return null;
            } else {
                throw e;
            }
        } finally {
            this.disconnect();

        }

        return cs;
    }

    /**
     * Converts listtypes to linked list with specified types
     *
     * @param <T>
     */
    private static class LinkedListConverter<T> {

        public LinkedListConverter() {
        }

        public List<T> toLinkedList(no.imr.formats.nmdbiotic.v3.ListType all) {
            List<T> newlist = new LinkedList<>();
            for (Object r : all.getRow()) {
                newlist.add((T) r);
            }
            return newlist;
        }
    }

}
