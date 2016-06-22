package PROVIDER;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by seladanghijau on 22/6/2016.
 */
public class InventoryProvider {
    private static String serialNoURL = "http://seladanghijau.pe.hu/GetNextSerialNo.php";

    public static String getSerialNo() {
        // declare variables -------------------------------------------------------------------------------------------
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader reader;
        StringBuffer response;

        String jsonDecodeResult;
        int serialNo = 0;
        // -------------------------------------------------------------------------------------------------------------

        try {
            url = new URL(serialNoURL); // initialize url

            // initialize http url connection --------------------------------------------------------------------------
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // ---------------------------------------------------------------------------------------------------------

            // get response --------------------------------------------------------------------------------------------
            inputStream = httpURLConnection.getInputStream(); // get inputstream from http connection (to get input from http)
            reader = new BufferedReader(new InputStreamReader(inputStream)); // initialize buffered reader with the connection input stream (to read inputs)
            response = new StringBuffer(); // initialize string buffer (to customize received http response string)

            String readResponse;
            while((readResponse = reader.readLine()) != null) {
                response.append(readResponse);
            }
            reader.close();
            // ---------------------------------------------------------------------------------------------------------

            // decode json -------------------------------------------------------------------------------------------------
            JSONArray jsonArray;
            JSONObject jsonObject;

            jsonDecodeResult = response.toString();
            jsonObject = new JSONObject(jsonDecodeResult);
            jsonArray = jsonObject.getJSONArray("SERIALNO");

            serialNo = jsonArray.getJSONObject(0).getInt("SERIALNO"); // get serial no from json
            // -------------------------------------------------------------------------------------------------------------
        } catch (Exception e) { e.printStackTrace(); }

        return serialNoFormat(serialNo); // format the serial no and return them
    }

    // format serial no into string
    public static String serialNoFormat(int serialNo) {
        // format the serial no
        DecimalFormat dcFormat = new DecimalFormat("#000000000.##");
        dcFormat.setDecimalSeparatorAlwaysShown(false);

        return "" + dcFormat.format(serialNo);
    }

    // produce the luhn check digit
    public static String getCheckDigit(String number) {
        int sum = 0;
        int checkDigit = 0;

        try {
            for (int x = 0; x < number.length(); x++) {
                int tempNum = Integer.parseInt(number.charAt(x) + "");

                if (x % 2 == 0) {
                    tempNum *= 2;

                    if (tempNum > 9)
                        tempNum -= 9;
                } else
                    tempNum *= 1;

                sum += tempNum;
            }

            checkDigit = (sum * 9) % 10;
        } catch(Exception e) { e.printStackTrace(); }

        return "" + checkDigit;
    }
}
