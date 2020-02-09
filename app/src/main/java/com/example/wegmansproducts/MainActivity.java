package com.example.wegmansproducts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private EditText editText;
    private String name = "";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // This is where we write the mesage
        editText = (EditText) findViewById(R.id.editText);
        name = editText.getText().toString();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                try {
                    GET();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String args[] = {"129.21.157.116", "666", "YEET"};
            //Client client = new Client(args);
            //c.listen();
            //client.run();
        });
    }

    public static void GET() throws IOException, JSONException {
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

    }
    public static void readJSON(JSONObject obj) throws JSONException {
        JSONArray x = obj.getJSONArray("stores");
        for(int i = 0; i < x.length(); i++){
            System.out.println(x.getJSONObject(i));
        }
    }
}