package City;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Street {
    private String name;
    private List<String> houses;

    Street(String name) {
        this.name = name;
        houses = new ArrayList<>();
    }

    void addHouse(String house) {
        houses.add(house);
    }

    @Override
    public String toString() {
        return "Street{" +
                "name='" + name + '\'' +
                ", houses=" + houses +
                '}';
    }
}

public class City extends DefaultHandler {
    private String cityName;
    private String citySize;
    private List<Street> streets;
    private Street currentStreet;
    private String currentHouse;

    public City() {
        streets = new ArrayList<>();
    }

    public void parseXML(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(filePath), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("city")) {
            cityName = attributes.getValue("name");
            citySize = attributes.getValue("size");
        } else if (qName.equalsIgnoreCase("street")) {
            String streetName = attributes.getValue("name");
            currentStreet = new Street(streetName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentStreet != null && currentHouse != null) {
            String houseName = new String(ch, start, length);
            currentHouse = houseName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("street")) {
            streets.add(currentStreet);
            currentStreet = null;
        } else if (qName.equalsIgnoreCase("house")) {
            currentStreet.addHouse(currentHouse);
            currentHouse = null;
        }
    }

    public void printCity() {
        System.out.println("City: " + cityName + " (Size: " + citySize + ")");
        for (Street street : streets) {
            System.out.println(street);
        }
    }

    public static void main(String[] args) {
        City city = new City();
        city.parseXML("D:\\project\\SAXParser\\src\\City\\city.xml");
        city.printCity();
    }
}

