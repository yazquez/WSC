package amazon1;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RestfulAmazon {
    // private static final String endpoint = "ecs.amazonaws.com";
    private static final String endpoint = "ec2.us-west-2.amazonaws.com";
    private static final String itemId = "0545010225"; // Harry Potter
    private static final String accessKeyId = "AKIAIDYXXXXVJEDZYKEA";
    private static final String secretKey = "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";

    public static void main(String[] args) {
        // if (args.length < 2) {
        // System.err.println("RestfulAmazon <accessKeyId> <secretKey>");
        // return;
        // }
        // new RestfulAmazon().lookupStuff(args[0].trim(), args[1].trim());
        new RestfulAmazon().lookupStuff(accessKeyId, secretKey);
    }

    private void lookupStuff(String accessKeyId, String secretKey) {
        RequestHelper helper = new RequestHelper(endpoint, accessKeyId, secretKey);
        String requestUrl = null;
        String title = null;

        // Store query string params in a hash.
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", itemId);
        params.put("ResponseGroup", "Small");
        params.put("AssociateTag", "kalin"); // any string should do

        requestUrl = helper.sign(params);
        String response = requestAmazon(requestUrl);

        // The string "null" is returned before the XML document.
        String noNullResponse = response.replaceFirst("null", "");
        System.out.println("Raw xml:\n" + noNullResponse);
        System.out.println("Author: " + getAuthor(noNullResponse));
    }

    private String requestAmazon(String stringUrl) {
        String response = null;
        try {
            URL url = new URL(stringUrl);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String chunk = null;
            while ((chunk = in.readLine()) != null)
                response += chunk;
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Arrrg! " + e);
        }

        return response;
    }

    private String getAuthor(String xml) {
        String author = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setNamespaceAware(true);
            DocumentBuilder builder = fact.newDocumentBuilder();

            Document doc = builder.parse(bais);
            NodeList results = doc.getElementsByTagName("Author");
            for (int i = 0; i < results.getLength(); i++) {
                Element e = (Element) results.item(i);
                NodeList nodes = e.getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node child = nodes.item(j);
                    if (child.getNodeType() == Node.TEXT_NODE)
                        author = child.getNodeValue();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Xml bad!", e);
        }
        return author;
    }
}
