package com.example.alexiscr.lescotranslator.Logic;

import org.jetbrains.annotations.Contract;

/**
 * Created by AlexisCR on 20/11/2016.
 */

/**
 * Class that edit the text entered by the user into a correct format for the application.
 */
public class TextFormatter {

    @Contract(pure = true)
    private static String deleteUnnecessaryBlankSpaces(String target){
        String result = "";
        for(int i = 0; i < target.length(); i++){
            char character = target.charAt(i);
            if((int)character == 32){
                if(result.length() != 0){
                    if((int)result.charAt(result.length() - 1) != 32 && i != target.length() - 1)
                        result += character;
                }
            }
            else
                result += character;
        }
        return result;
    }

    @Contract(pure = true)
    private static boolean compareASCIIWithValues(int target, int... values){
        for(int value: values){
            if(value == target)
                return true;
        }
        return false;
    }

    public static String getTextWithValidFormat(String previousText){
        String temporalText = "";
        for(char character: previousText.toCharArray()){
            int asciiValue = (int)character;
            if((asciiValue >= 65 && asciiValue <= 90) || (asciiValue >= 97 && asciiValue <= 122) ||
                    (asciiValue >= 48 && asciiValue <= 57) || TextFormatter.compareASCIIWithValues(
                    asciiValue, 193, 201, 205, 211, 218, 225, 233, 237, 243, 250, 220, 209, 252,
                    241, 32))
                temporalText += character;
        }
        return TextFormatter.deleteUnnecessaryBlankSpaces(temporalText);
    }
}
