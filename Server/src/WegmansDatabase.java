import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class WegmansDatabase {

    public static void main(String[] args) throws IOException{
        GET();
    }

    public static void GET() throws IOException {
        URL store = new URL("https://api.wegmans.io/stores?Subscription-Key=6e17bbcf8f084f8c89b9c17c7e2462b4&api-version=2018-10-18");
        URL product = new URL()
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

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }

    }
}
