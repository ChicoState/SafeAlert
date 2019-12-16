package com.example.buddii.Map;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class directionsJSONParser {
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public static Vector pointsA = new Vector();
    public static Vector pointsTurn = new Vector();
    public static Vector pointsTurnFrom = new Vector();
    public static Vector pointsTurnAfter = new Vector();
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {
            
            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            if(l == 0){
                                pointsTurn.add(list.get(1).latitude);
                                pointsTurnFrom.add(list.get(list.size()-1).latitude);
                                pointsTurn.add(list.get(1).longitude);
                                pointsTurnFrom.add(list.get(list.size()-1).longitude);
                                pointsTurnAfter.add(list.get(list.size()-2).latitude);
                                pointsTurnAfter.add(list.get(list.size()-2).longitude);
                                //turningObject.getPoint(list.get(1).longitude, list.get(1).latitude, list.get(list.size()-1).longitude, list.get(list.size()-1).latitude, list.get(list.size()-2).longitude, list.get(list.size()-2).latitude);

                            }

                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);

                        }
                    }
                    routes.add(path);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }

    //decodes the direction polylines
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}