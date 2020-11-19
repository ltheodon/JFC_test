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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class PredatorAndPrey {

    FcmRunner runner;
    CognitiveMap map;

    public PredatorAndPrey() throws Exception {
        //map = FcmIO.loadXml(getClass().getResourceAsStream("PredatorAndPrey.xml")).get(0);
    	String s = "PredatorAndPrey.xml";
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
        runner = new SimpleFcmRunner(map, 0.1, 1000);
    }

    public void test_scenario_1() throws Exception {
        resetMap();
        map.getConcepts().get("Food").setOutput(0.0);
        map.getConcepts().get("Prey").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 1", runner.converge());
    }

    public void test_scenario_2() throws Exception {
        resetMap();
        map.getConcepts().get("Food").setOutput(1.0);
        map.getConcepts().get("Prey").setOutput(1.0);
        map.getConcepts().get("Predators").setOutput(1.0);
        showResults("Scenario 2", runner.converge());
    }

    public void run() {
        try {
            System.out.print("Scenario\tConverged");
            for (Concept c : map.getConcepts().values()) {
                System.out.print("\t" + c.getName());
            }
            System.out.println();

            test_scenario_1();
            test_scenario_2();

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
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
        System.out.print(scenario + "\t" + converged);

        for (Concept c : map.getConcepts().values()) {
            System.out.print("\t");
            System.out.print(c.getOutput() != null ? nf.format(c.getOutput()) : "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        PredatorAndPrey example;
        try {
            example = new PredatorAndPrey();
            example.run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}