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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ApnolabHardMode {
    CognitiveMap map;

    public ApnolabHardMode() {
    	loadMap();
    }
    
    private void loadMap() {
    	
    	String s = "ApnolabFuzzyMap.xml";
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
    	
        map = new CognitiveMap("Investments");
        map = test.get(0);
        
        Concept ph = map.getConcepts().get("pression_haute");
        ph.setFixedOutput(true);
        Concept pb = map.getConcepts().get("pression_basse");
        pb.setFixedOutput(true);
        Concept pd = map.getConcepts().get("differentiel_pression");
        pd.setFixedOutput(true);
        Concept dr = map.getConcepts().get("duree_rampe");
        dr.setFixedOutput(true);
        Concept tm = map.getConcepts().get("temps_masque");
        tm.setFixedOutput(true);
        Concept em = map.getConcepts().get("empreinte_masque");
        em.setFixedOutput(true);
        Concept hum = map.getConcepts().get("humidificateur");
        hum.setFixedOutput(true);
        Concept tc = map.getConcepts().get("tuyau_chauffant");
        tc.setFixedOutput(true);
        
        Concept facteur = map.getConcepts().get("IMC");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("pathologies_cardio_neuro");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("respiration_buccale");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("medicaments");
        facteur.setFixedOutput(true);
        
    }


    @SuppressWarnings("null")
	private void execute(String scenario) {
    	
    	
        
        
        
    	Map<String, Double> rminList = new HashMap<String, Double>();
    	Map<String, Double> rmaxList = new HashMap<String, Double>();
    	Map<String, Double> stepList = new HashMap<String, Double>();
    	List<String> nameList = new ArrayList<String>();
        
        try {
            File fXmlFile = new File("patient_try.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("concept");
        	
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
            } catch (Exception e) {
            e.printStackTrace();
          }
        
        double res = 99999.0;
        double best_dp = 0.0;
        double best_ph = 0.0;
        double best_pb = 0.0;
        double best_dr = 0.0;
        double best_tm = 0.0;

        double dp = 0.0;
        double pb = 0.0;
        double dr = 0.0;
        double tm = 0.0;
        double ph = rminList.get("pression_haute");
        while (ph <= rmaxList.get("pression_haute")) {
          pb = rminList.get("pression_basse");
          while (pb <= rmaxList.get("pression_basse")) {
              dr = rminList.get("duree_rampe");
              while (dr <= rmaxList.get("duree_rampe")) {
                  tm = rminList.get("temps_masque");
                  while (tm <= rmaxList.get("temps_masque")) {
		              dp = ph-pb;
		              map.getConcepts().get("pression_haute").setOutput(ph);
		              map.getConcepts().get("pression_basse").setOutput(pb);
		              map.getConcepts().get("differentiel_pression").setOutput(dp);
		              map.getConcepts().get("duree_rampe").setOutput(dr);
		              map.getConcepts().get("temps_masque").setOutput(tm);
			          FcmRunner runner;
			          runner = new SimpleFcmRunner(map, 0.0000000001, 100);
			          runner.converge();
			          Concept et = map.getConcepts().get("efficacite_traitement");
			          double nb = et.getNextOutput();
			          et = map.getConcepts().get("tolerance_traitement");
			          double nb2 = et.getNextOutput();
		        	  loadMap();
		        	  if (res > nb + nb2) {
		        		  res = nb + nb2;
		        		  best_dp = dp;
		        		  best_ph = ph;
		        		  best_pb = pb;
		        		  best_dr = dr;
		        		  best_tm = tm;
		        	  }
		        	  tm += stepList.get("temps_masque");
                  }
            	  dr += stepList.get("duree_rampe");
              }
        	  pb += stepList.get("pression_basse");
            }
          ph += stepList.get("pression_haute");
        }
        
        System.out.println("Best values:");
        System.out.println(best_ph + " --- " + best_pb + " --- " + best_dp + " --- " + best_dr +  " --- " + best_tm + ".");
    	
    	


        

        map.getConcepts().get("pression_haute").setOutput(best_ph);
        map.getConcepts().get("pression_basse").setOutput(best_pb);
        map.getConcepts().get("differentiel_pression").setOutput(best_dp);
        map.getConcepts().get("duree_rampe").setOutput(best_dr);
        map.getConcepts().get("temps_masque").setOutput(best_tm);
        

        ExampleUtils.printMapHeader(map, "\t");
        ExampleUtils.printMapState(map);

        

    	FcmRunner runner;
        runner = new SimpleFcmRunner(map, 0.0000000001, 100);
        boolean conv = runner.converge();
        ExampleUtils.printMapState(map);
        System.out.println("Convergence:" + conv);

        System.out.println("");
        

        System.out.println("");
        System.out.println("Résultat:");
        double nb = 0.0;
        Concept et = map.getConcepts().get("efficacite_traitement");
        nb = et.getOutput();
        nb = et.getNextOutput();
        System.out.println(nb);
        et = map.getConcepts().get("tolerance_traitement");
        nb = et.getOutput();
        nb = et.getNextOutput();
        System.out.println(nb);
    }


    public static void main(String[] args) {
    	ApnolabHardMode sample = new ApnolabHardMode();
        sample.execute("test");
    }
}