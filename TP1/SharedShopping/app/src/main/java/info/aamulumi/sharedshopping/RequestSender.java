package info.aamulumi.sharedshopping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

/**
 * > @RequestSender
 * <p/>
 * Object to send/get HTTP datas to/from a server.
 *
 * @author Florian "Aamu Lumi" Kauder
 *         for the project @Label[i] App
 *
 * MIT License
 */
public class RequestSender {

    public static String GET = "GET";
    public static String POST = "POST";
    public static String PUT = "PUT";
    public static String DELETE = "DELETE";

    private static final String COOKIES_HEADER = "Set-Cookie";
    private CookieManager mCookieManager;

    /**
     * Constructor - Init the cookie manager
     */
    public RequestSender() {
        mCookieManager = new CookieManager();
    }

    /**
     * Send HTTP Request with params (x-url-encoded)
     *
     * @param requestURL     - URL of the request
     * @param method         - HTTP method (GET, POST, PUT, DELETE, ...)
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return JSONObject returned by the server
     */
    public JSONObject makeHttpRequest(String requestURL, String method,
                                      HashMap<String, String> urlParameters,
                                      HashMap<String, String> bodyParameters) {
        HttpURLConnection connection = null;
        URL url;
        JSONObject jObj = null;

        try {
            // Check if we must add parameters in URL
            if (urlParameters != null) {
                String stringUrlParams = getFormattedParameters(urlParameters);
                url = new URL(requestURL + "?" + stringUrlParams);
            } else
                url = new URL(requestURL);

            // Create connection
            connection = (HttpURLConnection) url.openConnection();

            // Add cookies to request
            if (mCookieManager.getCookieStore().getCookies().size() > 0)
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));

            // Set request parameters
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(method);
            connection.setDoInput(true);

            // Check if we must add parameters in body
            if (bodyParameters != null) {
                // Create a string with parameters
                String stringBodyParameters = getFormattedParameters(bodyParameters);

                // Set output request parameters
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(stringBodyParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "fr-FR");

                // Send body's request
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(stringBodyParameters);

                writer.flush();
                writer.close();
                os.close();
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // If response is 200 (OK)
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Keep new cookies in the manager
                List<String> cookiesHeader = connection.getHeaderFields().get(COOKIES_HEADER);
                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader)
                        mCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }

                // Read the response
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                // Parse the response to a JSON Object
                try {
                    jObj = new JSONObject(sb.toString());
                } catch (JSONException e) {
                    Log.d("JSON Parser", "Error parsing data " + e.toString());
                    Log.d("JSON Parser", "Setting value of jObj to null");
                    jObj = null;
                }
            } else {
                Log.w("HttpUrlConnection", "Error : server sent code : " + responseCode);
            }
        } catch (MalformedURLException e) {
            Log.e("Network", "Error in URL");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close connection
            if (connection != null)
                connection.disconnect();
        }

        return jObj;
    }

    /**
     * Create a string formatted with parameters
     *
     * @param params - HashMap containing all parameters
     * @return formatted string
     * @throws UnsupportedEncodingException
     */
    private String getFormattedParameters(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        // Add each entry to String
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

