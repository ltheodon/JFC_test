import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;
import org.megadix.jfcm.ConceptActivator;
import org.megadix.jfcm.FcmConnection;
import org.megadix.jfcm.act.SignumActivator;
import org.megadix.jfcm.conn.WeightedConnection;
import org.megadix.jfcm.utils.FcmIO;
import org.megadix.jfcm.utils.FcmRunner;
import org.megadix.jfcm.utils.SimpleFcmRunner;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ApnolabTest {
    CognitiveMap map;

    public ApnolabTest() {
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
    	int size = test.size();
    	System.out.println("Size: " + size);
    	
    	
    	
    	


    	
        map = new CognitiveMap("Investments");
        map = test.get(0);

    }

    void resetMap() {
        map.getConcepts().get("Interest rate").setOutput(0.0);
        map.getConcepts().get("Productive investments").setOutput(0.0);
        map.getConcepts().get("Occupation").setOutput(0.0);
        map.getConcepts().get("Inflation").setOutput(0.0);
    }

    private void execute(String scenario) {
        System.out.println("Scenario: " + scenario);
        
        
        Map<String, Double> patientMap = PatientReader.getOutputs("patient_0.xml");

        Concept ph = map.getConcepts().get("pression_haute");
        ph.setOutput(patientMap.get("pression_haute"));
        System.out.println("pression_haute:" + patientMap.get("pression_haute"));
        ph.setFixedOutput(true);
        Concept pb = map.getConcepts().get("pression_basse");
        pb.setOutput(patientMap.get("pression_basse"));
        System.out.println("pression_basse:" + patientMap.get("pression_basse"));
        pb.setFixedOutput(true);
        Concept pd = map.getConcepts().get("differentiel_pression");
        pd.setOutput(patientMap.get("pression_haute")-patientMap.get("pression_basse"));
        pd.setFixedOutput(true);
        Concept dr = map.getConcepts().get("duree_rampe");
        dr.setOutput(patientMap.get("duree_rampe"));
        System.out.println("duree_rampe:" + patientMap.get("duree_rampe"));
        dr.setFixedOutput(true);
        Concept tm = map.getConcepts().get("temps_masque");
        tm.setOutput(patientMap.get("temps_masque"));
        System.out.println("temps_masque:" + patientMap.get("temps_masque"));
        tm.setFixedOutput(true);
        Concept em = map.getConcepts().get("empreinte_masque");
        em.setOutput(patientMap.get("empreinte_masque"));
        System.out.println("empreinte_masque:" + patientMap.get("empreinte_masque"));
        em.setFixedOutput(true);
        Concept hum = map.getConcepts().get("humidificateur");
        hum.setOutput(patientMap.get("humidificateur"));
        System.out.println("humidificateur:" + patientMap.get("humidificateur"));
        hum.setFixedOutput(true);
        Concept tc = map.getConcepts().get("tuyau_chauffant");
        tc.setOutput(patientMap.get("tuyau_chauffant"));
        System.out.println("tuyau_chauffant:" + patientMap.get("tuyau_chauffant"));
        tc.setFixedOutput(true);
        Concept facteur = map.getConcepts().get("IMC");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("pathologies_cardio_neuro");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("respiration_buccale");
        facteur.setFixedOutput(true);
        facteur = map.getConcepts().get("medicaments");
        facteur.setFixedOutput(true);

        ExampleUtils.printMapHeader(map, "\t");
        ExampleUtils.printMapState(map);

        /*for (int i = 0; i < 5; i++) {
            map.execute();
            ExampleUtils.printMapState(map);
        }*/

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
    	ApnolabTest sample = new ApnolabTest();
        sample.execute("test");
    }
}