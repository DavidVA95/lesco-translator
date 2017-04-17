package com.example.alexiscr.lescotranslator.Logic;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by AlexisCR on 19/07/2016.
 */

/**
 * Class that manage the Local Database.
 */
public class LocalDatabaseManager extends AppCompatActivity {

    /**
     * Sets a RealConfiguration and returns it.
     * @param context the application context.
     * @return the instance of Realm.
     */
    public static Realm getRealmInstance(Context context){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        return Realm.getDefaultInstance();
    }

    /**
     * Search for an LescoObject in the local database.
     * @param word the word that represents the LescoObject to search.
     * @return a LescoObject, if found.
     */
    @Nullable
    public static LescoObject getLescoObjectInLocalDatabase(String word){
        LescoObject foundLescoObject = Realm.getDefaultInstance().where(LescoObject.class)
                .equalTo("word", word.toLowerCase()).findFirst();
        return (foundLescoObject != null ?
                LescoObject.makeCopyForLocalDatabaseResult(foundLescoObject, word) : null);
    }

    /**
     * Stores a LescoObject in the local database.
     * @param context : the application context.
     * @param newLescoObject : the LescoObject to be stored.
     * @return : the final state of the transaction, true if success or false if fail.
     */
    @NonNull
    public static Boolean addLescoObjectToDatabase(Context context, LescoObject newLescoObject){
        Realm realm = getRealmInstance(context);
        try {
            realm.beginTransaction();
            LescoObject newRealmLescoObject = realm.copyToRealm(newLescoObject);
            realm.commitTransaction();
        } catch (Exception ex) {
            realm.commitTransaction();
            return false;
        }
        return true;
    }

    /**
     * In the first run of the app, this method request all the LescoObject that corresponds to the
     * alphabet letters and the numbers.
     * @param context the application context.
     */
    public static void initializeDatabase(Context context){
        getRealmInstance(context);
        if(getLescoObjectInLocalDatabase("z") == null) {
            for(char character: Constants.ALPHABET_AND_NUMBERS.toCharArray())
                addLescoObjectToDatabase(context,
                        StaticMethods.getLescoObjectInWebService(Character.toString(character)));
        }
    }

    /**
     * Search for an LescoObject that was probably consulted before.
     * @param word : the word to search.
     * @return : a previous consulted LescoObject, if it exists.
     */
    @Nullable
    public static LescoObject getTemporalLescoObject(String word){
        for(LescoObject temporalLescoObject : Globals.getInstance().getTemporalList()){
            if(temporalLescoObject.getWord().equals(word.toLowerCase()))
                return LescoObject.makeCopyWithOriginalWord(temporalLescoObject, word);
        }
        return null;
    }
}
