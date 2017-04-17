package com.example.alexiscr.lescotranslator.Logic;

import java.util.ArrayList;

/**
 * Created by AlexisCR on 20/07/2016.
 */

/**
 * Class that translates a text into a LescoObject array.
 */
public class Translator {
    private static final Globals instance = Globals.getInstance();

    /**
     * Spells a word through letters and numbers LescoObjects.
     * @param word : a word that wasn't found in any resource.
     * @return : a LescoObject array with the letters that represent the word.
     */
    private static ArrayList<LescoObject> wordSpell(String word){
        ArrayList<LescoObject> characters = new ArrayList<>();
        LescoObject actualLescoObject;
        for(char character: word.toCharArray()) {
            String characterString = String.valueOf(character);
            actualLescoObject =
                    LocalDatabaseManager.getLescoObjectInLocalDatabase(
                            StaticMethods.accentEliminator(characterString.toLowerCase()));
            actualLescoObject = LescoObject.makeCopyForWordSpell(actualLescoObject, characterString,
                    word);
            actualLescoObject.setChunk(true);
            characters.add(actualLescoObject);
        }
        instance.lescoObjectCount++;
        return characters;
    }

    /**
     * Function that searches for the LescoObjects that corresponds to the text entered by the user.
     * @param string : text that the user entered.
     * @return : a LescoObject array.
     */
    public static ArrayList<LescoObject> stringToLescoObjectArrayList(String string){
        instance.lescoObjectCount = 0;
        String[] splittedString = TextFormatter.getTextWithValidFormat(string).split(" ");
        ArrayList<LescoObject> lescoObjects = new ArrayList<>();
        for(String word : splittedString){
            LescoObject temporalLescoObject = LocalDatabaseManager.getTemporalLescoObject(word); // Searching in the temporal list.
            if(temporalLescoObject == null) {
                temporalLescoObject = LocalDatabaseManager.getLescoObjectInLocalDatabase(word); // Searching in the local database.
                if(temporalLescoObject == null){
                    temporalLescoObject = StaticMethods.getLescoObjectInWebService(word); // Searching in the web service.
                    if (temporalLescoObject == null)
                        lescoObjects.addAll(wordSpell(word)); // Spelling the word as the last resort.
                    else {
                        lescoObjects.add(temporalLescoObject);
                        instance.addTemporalLescoObject(temporalLescoObject); // Found in the web service.
                    }
                }
                else
                    lescoObjects.add(temporalLescoObject); // Found in the local database.
            }
            else
                lescoObjects.add(temporalLescoObject); // Found in the temporal list.
        }
        return lescoObjects;
    }
}
