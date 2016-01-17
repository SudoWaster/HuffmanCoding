/*
 *  Copyright (C) 2016 Cezary Regec (SudoWaster)
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package huffmancoding.TextTools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple counter for counting statistic occurrence frequency for every 
 * character in a given string.
 *
 * @author cezary
 */
public class OccurrenceCounter {

    /**
     * Init fields with a String
     *
     * @param txt a String that we'll be counting characters of
     */
    public OccurrenceCounter(String txt) {
        this.text = txt;
    }

    /**
     * Init fields with a char array (convert it).
     *
     * @param txt a char array converted to a String we'll be counting
     * characters of
     */
    public OccurrenceCounter(char[] txt) {
        this.text = String.valueOf(txt);
    }

    /**
     * Calls for counting and returns occurrence of every Character in a String.
     *
     * @return a CharacterOccurrence array
     */
    public CharacterOccurrence[] getFullOccurrence() {

        count();
        
        return occurrence;
    }

    /**
     * Get single character occurrence frequency in a String.
     *
     * @param character a character we're looking for
     * @return a CharacterOccurrence object of this character
     */
    public CharacterOccurrence getCharacterOccurrence(char character) {
        //
        // You know what that means.
        //
        count();

        //
        // Search for the character in CharacterOccurrence array
        //
        for(CharacterOccurrence element : occurrence) {

           //
           // If found, just return the CharacterOccurrence element;
           //
            if(element.getCharacter() == character) {

                return element;
            }

        }

        //
        // The sad ending - the character does not occur.
        //
        return new CharacterOccurrence(character, 0);
    }

    /**
     *
     * Time-saving method for lazy CPUs, checks if it has been already counted
     * and if it's not, calls two other methods that count and convert to
     * a CharacterOccurrence array.
     *
     */
    private void count() {
        //
        // Check if the counting has not been done
        //
        if(!isDone) {
            //
            // Better count everything now.
            //
            checkOccurrence();
            convertToCharacterOccurrence();

            //
            // And we don't need to do the same every time.
            //
            isDone = true;
        }
    }

    /**
     *
     * This method iterates through whole String counting letter occurrences.
     * It stores the result in a HashMap.
     *
     */
    private void checkOccurrence() {
        //
        // Prepare our dictionary.
        //
        dictionary = new HashMap<Character, Integer>();

        //
        // The length will come in handy later.
        //
        this.length = this.text.length();

        //
        // Let's iterate through string to count letters.
        //
        for(int i = 0; i < this.length; i++) {
            //
            // We need to know current char and new occurrence.
            //
            char currentChar = this.text.charAt(i);
            int occurrenceNumber = 1;


            if(dictionary.containsKey(currentChar)) {
                //
                // If it's already in HashMap, update the occurrence
                //
                occurrenceNumber = dictionary.get(currentChar) + 1;
            }

            //
            // Put value under our char key
            //
            dictionary.put(currentChar, occurrenceNumber);
        }
    }

    /**
     *
     * Converts our dictionary HashMap with Characters and their number
     * of occurrences to a CharOccurrence array called occurrences with chars
     * and their frequency.
     *
     */
    private void convertToCharacterOccurrence() {
        //
        // Create temporary result array
        //
        CharacterOccurrence[] occurrence =
                new CharacterOccurrence[dictionary.size()];

        //
        // Let's iterate through HashMap, notice we need indexes for the array
        //
        Iterator iterator = dictionary.entrySet().iterator();
        for(int i = 0; iterator.hasNext(); i++) {
            //
            // Get current elements
            //
            Map.Entry dictionaryEntry = (Map.Entry) iterator.next();

            //
            // Prepare character and its frequency
            //
            Character character = (Character) dictionaryEntry.getKey();

            int occurrenceNumber = (Integer) dictionaryEntry.getValue();
            double frequency = (double) occurrenceNumber / length;

            //
            // Add those values to array
            //
            occurrence[i] = new CharacterOccurrence(character.charValue(),
                    frequency);
        }

        //
        // We're done, usable occurrence variable can have our array
        //
        this.occurrence = occurrence;
    }

    
    //
    // The original String and its length
    //
    private String text;
    private int length;

    //
    // Character occurrence holders
    //
    private HashMap<Character, Integer> dictionary;
    private CharacterOccurrence[] occurrence;

    //
    // Whether the String has been counted yet
    //
    private boolean isDone = false;
}
