import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.megadix.jfcm.CognitiveMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PatientReader {
	
    public static Map<String, Double> getOutputs(String fileName) {

    	Map<String, Double> map = new HashMap<String, Double>();
    	
        try {
            File fXmlFile = new File(fileName);
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
                    Double output = Double.parseDouble(eElement.getElementsByTagName("output").item(0).getTextContent());
                    map.put(name, output);
                }
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
        
		return map;
    }
	

    
    
    
    
    
    
  public static void main(String argv[]) {

    try {

    File fXmlFile = new File("patient_0.xml");
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);
            
    doc.getDocumentElement().normalize();

    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

    NodeList nList = doc.getElementsByTagName("concept");
            
    System.out.println("----------------------------");

    for (int temp = 0; temp < nList.getLength(); temp++) {

        Node nNode = nList.item(temp);
                
        System.out.println("\nCurrent Element :" + nNode.getNodeName());
                
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

            Element eElement = (Element) nNode;

            System.out.println("Nom : " + eElement.getAttribute("name"));
            System.out.println("output : " + eElement.getElementsByTagName("output").item(0).getTextContent());
            
        }
    }
    } catch (Exception e) {
    e.printStackTrace();
    }
  }

}