package basic;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Main {

    public static void main(String[] args) {
        PersonNoProps bd = new PersonNoProps("Bjoern Daehlie", 49, "Male");

        XStream xmlStream = new XStream(new DomDriver());
        xmlStream.alias("skier", PersonNoProps.class); // for readability

        // serialize to XML
        String xml = xmlStream.toXML(bd);
        System.out.println(xml);

        PersonNoProps bdClone = (PersonNoProps) xmlStream.fromXML(xml);
        System.out.println(xmlStream.toXML(bdClone));

        XStream jsonStream = new XStream(new JsonHierarchicalStreamDriver());
        jsonStream.alias("skier", PersonNoProps.class); // for readability

        String json = jsonStream.toXML(bd); // it's really toJson now
        System.out.println(json);
    }
}
