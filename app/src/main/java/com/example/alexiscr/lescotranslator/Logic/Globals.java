package com.example.alexiscr.lescotranslator.Logic;

import java.util.ArrayList;

/**
 * Singleton class for implementation of global variables and methods in the application.
 */
public class Globals {
    private static Globals instance;
    private ArrayList<LescoObject> temporalList;
    public int lescoObjectCount;

    /**
     * Default class builder.
     */
    private Globals() {}

    /**
     * Returns the Singleton instance.
     * @return : the Singleton instance, if it doesn't exists, it's instantiated.
     */
    public static Globals getInstance() {
        if(instance == null){
            instance = new Globals();
            instance.temporalList = new ArrayList<>();
            instance.lescoObjectCount = 0;
        }
        return instance;
    }

    /**
     * Validates if a String is an empty one.
     * @param string : the String to be validated.
     * @return : true if is an empty String or false if not.
     */
    public static boolean isEmptyString(String string){
        for(char c : string.toCharArray()){
            if(c != ' ')
                return false;
        }
        return true;
    }

    /**
     * Function that returns the temporal LescoObject list
     * @return : the temporal LescoObject list.
     */
    public ArrayList<LescoObject> getTemporalList(){
        return instance.temporalList;
    }

    /**
     * Function that receives a LescoObject and adds it into the temporal LescoObject list.
     * @param newTemporalLescoObject : the new LescoObject to be added.
     */
    public void addTemporalLescoObject(LescoObject newTemporalLescoObject){
        instance.temporalList.add(newTemporalLescoObject);
    }
}
