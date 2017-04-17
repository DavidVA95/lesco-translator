package com.example.alexiscr.lescotranslator.Logic;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that request data from the web service and parses it.
 */
public class JSONParser {
    private final String word;

    public JSONParser(String word) {
        this.word = word;
    }

    /**
     * Search a LescoObject in the web service.
     * @return : The LescoObject found, if it exists.
     */
    public LescoObject consultWebService(){
        LescoObject lescoObjectResult = null;
        try {
            lescoObjectResult = new JSONTask().execute(Constants.WEB_SERVICE_LINK + word).get();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return lescoObjectResult;
    }

    /**
     * Performs an Async connection with the web service.
     */
    private class JSONTask extends AsyncTask<String, String, LescoObject>{

        /**
         * Connect with the server and gets the response.
         * @param strings : the URL to be executed.
         * @return : the LescoObject found, if exists.
         */
        @Override
        protected LescoObject doInBackground(String... strings) {
            String response = "[]", line;
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuffer = new StringBuilder();
                while((line = bufferedReader.readLine()) != null)
                    stringBuffer.append(line);
                response = stringBuffer.toString();
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            } finally {
                if(connection != null) {
                    connection.disconnect();
                    try {
                        if(bufferedReader != null)
                            bufferedReader.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getLocalizedMessage());
                    }
                }
            }
            return stringToLescoObject(response.substring(1, response.length() - 1));
        }

        /**
         * Converts a String response into a new LescoObject.
         * @param string : the String response.
         * @return : a new converted LescoObject.
         */
        private LescoObject stringToLescoObject(String string){
            JSONObject jsonObject = stringToJSONObject(string);
            LescoObject obtainedLescoObject = null;
            try {
                if(jsonObject != null) {
                    String word = jsonObject.getString("word");
                    obtainedLescoObject = new LescoObject(StaticMethods.accentEliminator(word), word,
                            ImageConverter.base64ToByteArray(jsonObject.getString("image")
                                    .replace("\\", "")), Globals.getInstance().lescoObjectCount++);
                }
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
            return obtainedLescoObject;
        }

        /**
         * Converts a String into a JSONObject.
         * @param string : the String to be converted.
         * @return : the JSONObject result.
         */
        private JSONObject stringToJSONObject(String string){
            JSONObject jsonObject = null;
            try{
                jsonObject = new JSONObject(string);
            } catch (JSONException ex){
                System.out.println(ex.getLocalizedMessage());
            }
            return jsonObject;
        }
    }
}
