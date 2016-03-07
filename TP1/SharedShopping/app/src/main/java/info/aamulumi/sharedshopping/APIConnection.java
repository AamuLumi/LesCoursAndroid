package info.aamulumi.sharedshopping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * > @APIConnection
 * <p/>
 * Connector to ShoppingListAPI
 * based on old connection to Label[i] API
 * (https://github.com/asso-labeli/labeli-android-app/blob/master/src/net/tools/RequestSender.java)
 *
 * @author Florian "Aamu Lumi" Kauder
 *         for the project Les Cours Android - TP1
 *         <p/>
 *         MIT License
 */
public abstract class APIConnection {

    // URLs
    public static String apiUrl = "http://shoppinglist.aamulumi.info/";
    private static String urlShoppingLists = apiUrl + "lists";

    // HTTP
    private static String GET = "GET";
    private static String POST = "POST";
    private static String DELETE = "DELETE";
    private static String PUT = "PUT";

    // Lists JSON Tags
    private static String tagShoppingListName = "name";
    private static String tagShoppingListItems = "items";

    // Items JSON Tags
    private static String tagShoppingItemName = "name";
    private static String tagShoppingItemQuantity = "quantity";
    private static String tagShoppingItemPrice = "price";

    private static RequestSender jParser = new RequestSender();

    /**
     * Send HTTP Request with params (x-url-encoded)
     *
     * @param url            - URL of the request
     * @param method         - HTTP method (GET, POST, PUT, DELETE, ...)
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return JSONObject returned by the server
     */
    private static JSONObject makeHttpRequest(String url,
                                              String method, HashMap<String, String> urlParameters,
                                              HashMap<String, String> bodyParameters) {
        return jParser.makeHttpRequest(url, method, urlParameters, bodyParameters);
    }

	/* ******************************************************
     *
	 * HTTP Request Methods
	 *
	 ********************************************************/

    /**
     * Send a GET request and create a list with returned JSON datas.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     * - data : JSONArray - contains datas which will be parsed
     *
     * @param url            - url to access
     * @param parseMethod    - name of the method used to parse an object in data
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    private static <T> ArrayList<T> getItems(String url, String parseMethod,
                                             HashMap<String, String> urlParameters,
                                             HashMap<String, String> bodyParameters) {
        ArrayList<T> list = new ArrayList<>();

        // Get parseMethod
        Class<?>[] cArg = new Class[1];
        cArg[0] = JSONObject.class;

        Method parse;
        try {
            parse = APIConnection.class.getMethod(parseMethod, cArg);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            return null;
        }

        // Do the request
        JSONObject json = makeHttpRequest(url, GET, urlParameters, bodyParameters);

        if (json == null)
            return null;
        try {
            int success = json.getInt("success");
            // Parse if successful
            if (success == 1) {
                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    @SuppressWarnings("unchecked")
                    T tmp = (T) parse.invoke(APIConnection.class, data.getJSONObject(i));
                    list.add(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    /**
     * Send a HTTP request and parse the returned element.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     * - data : JSONObject - element which will be parsed
     *
     * @param url            - url to access
     * @param method         - HTTP method (GET, PUT, ...)
     * @param parseMethod    - name of the method used to parse an object in data
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    @SuppressWarnings("unchecked")
    private static <T> T doHTTPRequestAndParse(String url, String method, String parseMethod,
                                               HashMap<String, String> urlParameters,
                                               HashMap<String, String> bodyParameters) {
        // Get parseMethod
        Class<?>[] cArg = new Class[1];
        cArg[0] = JSONObject.class;

        Method parse;
        try {
            parse = APIConnection.class.getMethod(parseMethod, cArg);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            return null;
        }

        // Do the request
        JSONObject json = makeHttpRequest(url, method, urlParameters, bodyParameters);

        if (json == null)
            return null;
        try {
            int success = json.getInt("success");
            // Parse if successful
            if (success == 1) {
                return (T) parse.invoke(APIConnection.class, json.getJSONObject("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * Send a HTTP request and check the returned element.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     *
     * @param url            - url to access
     * @param method         - HTTP method (GET, PUT, ...)
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    public static boolean doHTTPRequest(String url, String method,
                                        HashMap<String, String> urlParameters,
                                        HashMap<String, String> bodyParameters) {
        JSONObject json = makeHttpRequest(url, method, urlParameters, bodyParameters);

        if (json == null)
            return false;
        try {
            int success = json.getInt("success");
            // Parse if successful
            if (success == 1) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Send a GET request and parse the returned element.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     * - data : JSONObject - element which will be parsed
     *
     * @param url            - url to access
     * @param parseMethod    - name of the method used to parse an object in data
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    private static <T> T getItem(String url, String parseMethod,
                                 HashMap<String, String> urlParameters,
                                 HashMap<String, String> bodyParameters) {
        return APIConnection.doHTTPRequestAndParse(url, GET, parseMethod, urlParameters,
                bodyParameters);
    }

    /**
     * Send a POST request and parse the returned element.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     * - data : JSONObject - element which will be parsed
     *
     * @param url            - url to access
     * @param parseMethod    - name of the method used to parse an object in data
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    private static <T> T createItem(String url, String parseMethod,
                                    HashMap<String, String> urlParameters,
                                    HashMap<String, String> bodyParameters) {
        return APIConnection.doHTTPRequestAndParse(url, POST, parseMethod, urlParameters,
                bodyParameters);
    }

    /**
     * Send a PUT request and parse the returned element.
     * The JSONObject returned by server must have :
     * - success : int - 1 if request is successful
     * - data : JSONObject - element which will be parsed
     *
     * @param url            - url to access
     * @param parseMethod    - name of the method used to parse an object in data
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    private static <T> T editItem(String url, String parseMethod,
                                  HashMap<String, String> urlParameters,
                                  HashMap<String, String> bodyParameters) {
        return APIConnection.doHTTPRequestAndParse(url, PUT, parseMethod, urlParameters,
                bodyParameters);
    }

    /**
     * Send a DELETE request.
     *
     * @param url            - url to access
     * @param urlParameters  - parameters send in URL
     * @param bodyParameters - parameters send in body (encoded)
     * @return the list of parsed elements
     */
    public static boolean deleteItem(String url,
                                     HashMap<String, String> urlParameters,
                                     HashMap<String, String> bodyParameters) {
        return APIConnection.doHTTPRequest(url, DELETE, urlParameters, bodyParameters);
    }

	/* ******************************************************
     *
	 * API Module : Users
	 *
	 ********************************************************/

    /**
     * Send a request to get a shopping list
     *
     * @return the required shopping list
     */
    public static ShoppingList getShoppingList(String id) {
        return APIConnection.getItem(urlShoppingLists + "/" + id, "parseShoppingList", null, null);
    }

    /**
     * Send a request to create a shopping list
     *
     * @param name - name of the shopping list
     * @return the created user
     */
    public static ShoppingList createShoppingList(String name) {
        HashMap<String, String> params = new HashMap<>(1);
        params.put("name", name);

        return APIConnection.createItem(urlShoppingLists, "parseShoppingList", null, params);
    }

	/* ******************************************************
	 *
	 * Parsing Methods
	 *
	 ********************************************************/

    /**
     * Parse a JSONObject to a ShoppingList object
     *
     * @param o - JSONObject to parse
     * @return the parsed ShoppingList
     * @throws JSONException
     */
    public static ShoppingList parseShoppingList(JSONObject o) throws JSONException {
        String name = o.getString(tagShoppingListName);
        ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();

        JSONArray tmp = o.getJSONArray(tagShoppingListItems);

        for (int i = 0; i < tmp.length(); i++)
            items.add(parseShoppingListItem(tmp.getJSONObject(i)));

        return new ShoppingList(name, items);
    }

    /**
     * Parse a JSONObject to a ShoppingListITem object
     *
     * @param o - JSONObject to parse
     * @return the parse ShoppingListItem
     * @throws JSONException
     */
    public static ShoppingListItem parseShoppingListItem(JSONObject o) throws JSONException {
        String name = o.getString(tagShoppingItemName);
        int quantity = o.getInt(tagShoppingItemQuantity);
        double price = o.getDouble(tagShoppingItemPrice);

        return new ShoppingListItem(name, quantity, price);
    }
}

