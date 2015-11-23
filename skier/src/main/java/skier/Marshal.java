package skier;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Marshal {
    private static final String fileName = "bd.txt";

    public static void main(String[] args) {
        new Marshal().runExample();
    }

    private void runExample() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Skier.class);
            Skier skier = createSkier();

            // Marshal a Skier object to file
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            FileOutputStream out = new FileOutputStream(fileName);
            m.marshal(skier, out);
            out.close();

            // Unmarshal from file
            Unmarshaller u = ctx.createUnmarshaller();
            Skier bdClone = (Skier) u.unmarshal(new File(fileName));
            System.out.println();
            m.marshal(bdClone, System.out);

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    private Skier createSkier() {
        Person p = new Person("Daniel", 4, "Male");
        List<String> list = new ArrayList<String>();
        list.add("12 Olympic Medals");
        list.add("9 World Championships");
        list.add("Winningest Winter Olympian");
        list.add("Greatest Nordic Skier");

        return new Skier(p, "Spain", list);
    }

}
