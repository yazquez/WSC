package predictions2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.json.JSONObject;
import org.json.XML;

public class PredictionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Predictions predictions;

    @Override
    public void init() {
        predictions = new Predictions();
        predictions.setServletContext(this.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("id");
        Integer key = (param == null) ? null : new Integer(param.trim());

        boolean json = false;
        String accept = request.getHeader("accept");

        if (accept != null && accept.contains("json"))
            json = true;

        if (key == null) {
            ConcurrentMap<Integer, Prediction> map = predictions.getMap();
            Object[] list = map.values().toArray();
            Arrays.sort(list);
            sendResponse(response, predictions.toXML(list), json);

        } else {
            Prediction prediction = predictions.getMap().get(key);
            if (prediction == null) { // no such Prediction
                String msg = key + " does not map to a prediction.\n";
                sendResponse(response, predictions.toXML(msg), false);
            } else { // requested Prediction found
                sendResponse(response, predictions.toXML(prediction), json);
            }
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String who = request.getParameter("who");
        String what = request.getParameter("what");
        if (who == null || what == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        Prediction p = new Prediction();
        p.setWhat(what);
        p.setWho(who);
        int id = predictions.addPrediction(p);
        String msg = "Prediction " + id + " created.\n";
        sendResponse(response, predictions.toXML(msg), false);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        String key = null;
        String modifiedData = null;
        boolean isWhoModified = false;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String data = br.readLine();
            /*
             * To simplify the hack, assume that the PUT request has exactly two parameters: the id and either who or
             * what. Assume, further, that the id comes first. From the client side, a hash character # separates the id
             * and the who/what, e.g., id=33#who=Homer Allision
             */
            String[] args = data.split("#");
            String[] parts1 = args[0].split("=");
            key = parts1[1];
            String[] parts2 = args[1].split("="); // parts2[0] is key
            if (parts2[0].contains("who"))
                isWhoModified = true;
            modifiedData = parts2[1];

        } catch (Exception e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        if (key == null || modifiedData == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }

        Prediction p = predictions.getMap().get(new Integer(key.trim()));

        if (p == null) {
            String msg = key + " does not map to a Prediction.\n";
            sendResponse(response, predictions.toXML(msg), false);
        } else {
            if (isWhoModified) {
                p.setWho(modifiedData);
            } else {
                p.setWhat(modifiedData);
            }
            String msg = "Prediction " + key + " has been edited.\n";
            sendResponse(response, predictions.toXML(msg), false);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("id");
        Integer key = (param == null) ? null : new Integer(param.trim());

        if (key == null) {
            throw new HTTPException(HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            predictions.getMap().remove(key);
            String msg = "Prediction " + key + " removed.\n";
            sendResponse(response, predictions.toXML(msg), false);
        } catch (Exception e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    // Method Not Allowed
    @Override
    public void doTrace(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void doHead(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) {
        throw new HTTPException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // Send the response payload to the client.
    private void sendResponse(HttpServletResponse response, String payload, boolean json) {
        // TODO Auto-generated method stub
        try {
            if (json) {
                JSONObject jobt = XML.toJSONObject(payload);
                payload = jobt.toString(3); // 3 is indentation level for nice look
            }
            OutputStream out = response.getOutputStream();
            out.write(payload.getBytes());
            out.flush();

        } catch (Exception e) {
            throw new HTTPException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
