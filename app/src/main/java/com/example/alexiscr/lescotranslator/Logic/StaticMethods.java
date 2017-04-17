package com.example.alexiscr.lescotranslator.Logic;

/**
 * Created by Kim Morales on 12-Nov-16.
 */

/**
 * Class that storage the Statics methods of the application.
 */
public class StaticMethods {

    /**
     * Takes a word and returns it without accent marks.
     * @param word : target word to search for accent marks.
     * @return : the word without accent marks.
     */
    public static String accentEliminator(String word){
        char[] charArray = word.toCharArray();
        for(int i = 0; i < charArray.length; i++){
            int position = Constants.WITH_ACCENT_MARKS.indexOf(charArray[i]);
            if(position > -1)
                charArray[i] = Constants.WITHOUT_ACCENT_MARKS.charAt(position);
        }
        return new String(charArray);
    }

    /**
     * Call the function that search for a word in the web service.
     * @param word : the word to search.
     * @return : a LescoObject found, if it exists.
     */
    public static LescoObject getLescoObjectInWebService(String word){
        JSONParser jsonParser = new JSONParser(word);
        return jsonParser.consultWebService();
    }
}
