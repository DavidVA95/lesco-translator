package com.example.alexiscr.lescotranslator.Logic;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Class that associates words with images of Lesco signs.
 */
public class LescoObject extends RealmObject {
    @PrimaryKey
    private String word;
    private byte[] image;
    @Ignore
    private String originalWord;
    @Ignore
    private boolean chunk;
    @Ignore
    private int identifier;

    /**
     * Empty class builder used by Realm.
     */
    public LescoObject(){}

    /**
     * Class builder.
     * @param word : word the represent the LescoObject in the logic level.
     * @param originalWord : the actual word, written by the user.
     * @param image : image that represents the word in Lesco language.
     * @param identifier : identifier used to differentiate LescoObjects, spelling LescoObjects that
     *                     represents the same word will have the same identifier.
     */
    public LescoObject(String word, String originalWord, byte[] image, int identifier) {
        this.word = word;
        this.originalWord = originalWord;
        this.image = image;
        this.chunk = false;
        this.identifier = identifier;
    }

    public String getWord() {
        return this.word;
    }

    public String getOriginalWord() { return this.originalWord; }

    public byte[] getImage(){
        return this.image;
    }

    public boolean isChunk() { return this.chunk; }

    public void setChunk(boolean chunkState) { this.chunk = chunkState; }

    public int getIdentifier() { return this.identifier; }

    public static LescoObject makeCopyWithOriginalWord(LescoObject lescoObject, String originalWord){
        return new LescoObject(lescoObject.getWord(), originalWord, lescoObject.getImage(),
                Globals.getInstance().lescoObjectCount++);
    }

    public static LescoObject makeCopyForLocalDatabaseResult(LescoObject lescoObject, String originalWord){
        return new LescoObject(lescoObject.getWord(), originalWord, lescoObject.getImage(), 0);
    }

    public static LescoObject makeCopyForWordSpell(LescoObject lescoObject, String stringCharacter,
                                                   String completeWord){
        return new LescoObject(completeWord, stringCharacter, lescoObject.getImage(),
                Globals.getInstance().lescoObjectCount);
    }
}
