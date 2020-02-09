package com.example.wegmansproducts;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONDATASTUFF extends AsyncTask<Void, Void, Void> {
        @Override
    protected Void doInBackground(Void... voids) {
            try {
                URL store = new URL("https://api.wegmans.io/stores?Subscription-Key=6e17bbcf8f084f8c89b9c17c7e2462b4&api-version=2018-10-18");
                URL productCategory = new URL("https://api.wegmans.io/products/categories?api-version=2018-10-18&Subscription-Key=6e17bbcf8f084f8c89b9c17c7e2462b4");
                HttpURLConnection con = (HttpURLConnection) store.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    readJSON(new JSONObject(response.toString()));
                    // print result
                } else {
                    System.out.println("GET request not worked");
                }
            }catch(Exception e){}
     return null;
    }
    public static void readJSON(JSONObject obj) throws JSONException {
        JSONArray x = obj.getJSONArray("stores");
        for(int i = 0; i < x.length(); i++){
            System.out.println(x.getJSONObject(i));
        }
    }
}
