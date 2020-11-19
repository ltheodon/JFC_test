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



public class WildlifePark {

    FcmRunner runner;
    CognitiveMap map;
    NumberFormat nf;

    public WildlifePark() throws Exception {
        //map = FcmIO.loadXml(getClass().getResourceAsStream("WildlifePark.fcm.xml")).get(0);
    	String s = "WildlifePark.fcm.xml";
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
        nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1
     */
    public void test_scenario_1() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);

        map.getConcepts().get("Rangers").setOutput(0.1);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);

        map.getConcepts().get("Poachers").setOutput(0.0);

        map.getConcepts().get("Erbivores").setOutput(0.0);

        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 1", runner.converge());

    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1 and 0.5
     */
    public void test_scenario_2() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.5);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 2", runner.converge());

    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1 and 0.9
     */
    public void test_scenario_3() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.9);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 3", runner.converge());

    }


    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.1
     */
    public void test_scenario_4() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.1);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 4", runner.converge());

    }


    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.5
     */
    public void test_scenario_5() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.5);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 5", runner.converge());

    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.9
     */
    public void test_scenario_6() throws Exception {
        resetMap();

        // FIXED VALUES
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.9);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        // start from zero
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);

        showResults("Scenario 6", runner.converge());

    }

    public void run() {
        try {
            System.out.print("Scenario\tConverged\t");
            ExampleUtils.printMapHeader(map, "\t");

            test_scenario_1();
            test_scenario_2();
            test_scenario_3();
            test_scenario_4();
            test_scenario_5();
            test_scenario_6();

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
        System.out.print(scenario + "\t" + converged + "\t");
        ExampleUtils.printMapState(map, "\t", nf);
    }

    public static void main(String[] args) {
        WildlifePark example;
        try {
            example = new WildlifePark();
            example.run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}