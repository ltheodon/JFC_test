import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;
import org.megadix.jfcm.ConceptActivator;
import org.megadix.jfcm.FcmConnection;
import org.megadix.jfcm.act.SignumActivator;
import org.megadix.jfcm.conn.WeightedConnection;
import org.megadix.jfcm.utils.FcmIO;
import org.megadix.jfcm.utils.FcmRunner;
import org.megadix.jfcm.utils.SimpleFcmRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



public class WildApnolab {

    FcmRunner runner;
    CognitiveMap map;
    NumberFormat nf;

    public WildApnolab() throws Exception {
        //map = FcmIO.loadXml(getClass().getResourceAsStream("WildlifePark.fcm.xml")).get(0);
    	String s = "WildApnolab.xml";
    	List<CognitiveMap> test = null;
    	try {
			test = FcmIO.loadXml(s);
		} catch (FileNotFoundException e) {
			System.out.println("Success");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Faillure");
			e.printStackTrace();
		}
        map = new CognitiveMap("WildApnolab");
        map = test.get(0);
        runner = new SimpleFcmRunner(map, 0.01, 100000);
        nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
    }

    

    
    
    public void test_scenario_1(double rain, double rangers) throws Exception {
        
        
       	Map<String, Double> rminList = new HashMap<String, Double>();
    	Map<String, Double> rmaxList = new HashMap<String, Double>();
    	Map<String, Double> stepList = new HashMap<String, Double>();
    	Map<String, Double> costList = new HashMap<String, Double>();
    	Map<String, Integer> interList = new HashMap<String, Integer>();
    	Map<String, Double> paramList = new HashMap<String, Double>();
    	List<String> nameList = new ArrayList<String>();
    	List<String> costNameList = new ArrayList<String>();
        
        try {
            File fXmlFile = new File("Wild_try.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("concept");
            NodeList coList = doc.getElementsByTagName("cost");
        	
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getAttribute("name");
                    Double rmin = Double.parseDouble(eElement.getElementsByTagName("rmin").item(0).getTextContent());
                    Double rmax = Double.parseDouble(eElement.getElementsByTagName("rmax").item(0).getTextContent());
                    Double step = Double.parseDouble(eElement.getElementsByTagName("step").item(0).getTextContent());
                    rminList.put(name, rmin);
                    rmaxList.put(name, rmax);
                    stepList.put(name, step);
                    nameList.add(name);
	                }
	            }
            
            for (int temp = 0; temp < coList.getLength(); temp++) {
                Node nNode = coList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getAttribute("name");
                    Double cost = Double.parseDouble(eElement.getElementsByTagName("weight").item(0).getTextContent());
                    costList.put(name, cost);
                    costNameList.add(name);
	                }
	            }
            
            } catch (Exception e) {
            e.printStackTrace();
          }
        
        int nb_iter = 1;
        int nb_inter = 0;
        
        for (String name : nameList) {
        	Double rmin = rminList.get(name);
        	Double rmax = rmaxList.get(name);
        	Double step = stepList.get(name);
        	nb_inter = (int) (step/100+(rmax - rmin) / step)+1; 
        	interList.put(name, nb_inter);
        	nb_iter *= nb_inter;
            //System.out.print(nb_inter + "\n");
        }
        
        
        
        // Global Iteration
        double top_resultat = -99999.0;
    	Map<String, Double> bestParamList = new HashMap<String, Double>();
        for (String name : nameList) {
        	bestParamList.put(name, 0.0);
        }
        for (int i=0; i<nb_iter; i++) {
        	int tmp_tot = 1;
        	for (String name : nameList) {
        		int r = i/tmp_tot % interList.get(name);
        		paramList.put(name,rminList.get(name) + r*stepList.get(name));
        		tmp_tot *= interList.get(name);
        	}
        	
        	// DO THE MAP!!§§§
            resetMap();
            map.getConcepts().get("Rain").setOutput(0.1);
            map.getConcepts().get("Rangers").setOutput(0.1);
            map.getConcepts().get("Grassland").setOutput(0.5);
            map.getConcepts().get("Poachers").setOutput(0.1);
            map.getConcepts().get("Herbivores").setOutput(0.2);
            map.getConcepts().get("Predators").setOutput(0.1);
            

            // FIXED VALUES
            for (String name : nameList) {
            	map.getConcepts().get(name).setOutput(paramList.get(name));
                map.getConcepts().get(name).setFixedOutput(true);
            	//System.out.print(name + ":" + paramList.get(name) + "\n");
            }
            

            boolean b = runner.converge();
        	//System.out.print("Convergence:" + b + "\n");
        	
        	double resultat = 0.0;
            for (String name : costNameList) {
            	//System.out.print("name:" + name + "\n");
            	Double out = map.getConcepts().get(name).getOutput();
            	resultat += costList.get(name)*out;
            	//System.out.print("resultat: " + resultat + "\n");
            }
            if (b & (resultat > top_resultat)){
            	top_resultat = resultat;
            	for (String name : nameList) {
                	bestParamList.put(name, paramList.get(name));
                }
            }
        	
        	
            
            //Double nb = map.getConcepts().get("Herbivores").getOutput();
        	//System.out.print("Herbivores:" + nb + "\n");
        	//System.out.print("resultat:" + resultat + "\n");
        }
        
        System.out.print("Meilleurs résultats: " + top_resultat + "\n");
        for (String name : nameList) {
        	System.out.print(name + ":" + bestParamList.get(name) + "\n");
        }

    }


    public void run() {
        try {
            //System.out.print("Scenario\tConverged\t");
            //ExampleUtils.printMapHeader(map, "\t");

            test_scenario_1(0.1,0.1);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void resetMap() {
    	Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept concept = iter.next();
            concept.setOutput(0.0);
            concept.setPrevOutput(null);
            concept.setFixedOutput(false);
        }
    }

    void showResults(String scenario, boolean converged) {
        //System.out.print(scenario + "\t" + converged + "\t");
        //ExampleUtils.printMapState(map, "\t", nf);
    }

    public static void main(String[] args) {
        WildApnolab example;
        try {
            example = new WildApnolab();
            example.run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}