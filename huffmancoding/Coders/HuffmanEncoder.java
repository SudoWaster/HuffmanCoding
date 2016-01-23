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

package huffmancoding.Coders;

import huffmancoding.TextTools.HuffmanCharacter;
import huffmancoding.TextTools.OccurrenceCounter;
import huffmancoding.Tree.HuffmanTree;
import java.util.ArrayList;

/**
 * An encoder class that uses Huffman coding
 *
 * @author cezary
 */
public class HuffmanEncoder {

    /**
     * Init class with text - count characters and make a tree with default
     * unicode on
     *
     * @param text a String with input
     */
    public HuffmanEncoder(String text) {
        this(text, true);
    }

    /**
     * Init class with text - count characters and make a tree and determine
     * unicode setting
     *
     * @param text a String with input
     */
    public HuffmanEncoder(String text, boolean isUnicode) {
        this.text = text;
        this.isUnicode = isUnicode;
        this.counter = new OccurrenceCounter(text, isUnicode);
        this.tree = new HuffmanTree(counter.getFullOccurrence());
    }

    /**
     * Encode the given String to a byte array
     *
     * @return an encoded Byte array
     */
    public Byte[] getEncoded() {
        //
        // Make sure we have ids and init our bytelist
        //
        HuffmanTree.updateIDs(tree.root, "");

        ArrayList<Byte> bytelist = new ArrayList<Byte>();

        //
        // Init our leftover bits buffer
        //
        String leftover = new String();

        //
        // Go through the string
        //
        for(int i = 0; i < text.length(); i++) {
            //
            // Make a collective buffer and init it with leftover bits
            //
            String collectiveBuffer = leftover;

            //
            // Init character bytes
            //
            HuffmanCharacter leftCharacter = null;
            HuffmanCharacter rightCharacter = null;

            char currentChar = text.charAt(i);

            //
            // Get HuffmanCharacters
            //
            if(isUnicode) {
                //
                // If unicode, cut left byte
                //
                char leftChar = (char) (currentChar / 256);
                currentChar -= leftChar * 256;

                //
                // Add left side to buffer
                //
                leftCharacter = tree.get(leftChar);
                collectiveBuffer += leftCharacter.getID();
            }
            //
            // Add right side to buffer
            //
            rightCharacter = tree.get(currentChar);
            collectiveBuffer += rightCharacter.getID();

            //
            // Make sure we do not extend the byte
            //
            while(collectiveBuffer.length() >= 8) {
                //
                // Cut first byte and convert it
                //
                String tempByteBuffer = collectiveBuffer.substring(0, 8);
                collectiveBuffer = collectiveBuffer.substring(8);

                byte tempByte = (byte) Integer.parseInt(tempByteBuffer, 2);

                //
                // Add to the bytelist
                //
                bytelist.add(tempByte);
            }

            //
            // Save the leftovers for the next character
            //
            leftover = collectiveBuffer;
        }

        //
        // Don't forget the leftovers
        //
        if(leftover.length() > 0) {

            while(leftover.length() < 8) {
                leftover += "0";    // Shift left
            }

            bytelist.add((byte) Integer.parseInt(leftover, 2));
        }

        //
        // Return the resulting array
        //
        return bytelist.toArray(new Byte[bytelist.size()]);
    }


    /**
     * Returns a dictionary of used characters and their id for future use.
     * Thought to be parsed and small for many characters.
     *
     * It is a byte stream, which has a following form:
     *  8 bits for a total number of characters
     *
     * and for every character:
     *  8 bits for the character
     *  8 bits for the character id length
     *
     * Characters with the same id length are on the same level in the tree.
     *
     * @return a Byte array containing the dictionary
     */
    public Byte[] getDictionary() {
        //
        // Get the chars
        //
        HuffmanCharacter[] characters = tree.getAll();
        
        //
        // Create a String byte stream with symbolic length info
        // at the beginning.
        //
        String foundCharsByte = Integer.toBinaryString(characters.length);
        foundCharsByte = fillByte(foundCharsByte);

        String dictionaryStream = foundCharsByte;

        //
        // Iterate throgh used characters
        //
        for(HuffmanCharacter character : characters) {

            //
            // Prepare our character information for the dictionary
            //
            String charByte = "";
            String idLength = "";
            
            if(character != null) {
                //
                // If character is found, set up the info
                //
                charByte = Integer.toBinaryString(character.getCharacter());
                idLength = Integer.toBinaryString(character.getID().length());
            }

            //
            // Fill the byte for future parsing
            //
            charByte = fillByte(charByte);
            idLength = fillByte(idLength);

            //
            // Add to dictionary stream
            //
            dictionaryStream += charByte + idLength;
        }

        // ---------------------------------------------------------------- //

        //
        // Start parsing to byte array
        //
        ArrayList<Byte> dictionary = new ArrayList<Byte>();

        while(dictionaryStream.length() > 0) {
            //
            // Cut out a byte
            //
            int bytelength = dictionaryStream.length() < 8 ?
                dictionaryStream.length() : 8;

            String buffer = dictionaryStream.substring(0, bytelength);
            dictionaryStream = dictionaryStream.substring(bytelength);

            //
            // Fill it if needed
            //
            while(buffer.length() < 8) {
                buffer += "0";
            }

            //
            // Parse to array
            //
            dictionary.add((byte) Integer.parseInt(buffer, 2));
        }

        //
        // Return parsed byte array
        //
        return dictionary.toArray(new Byte[dictionary.size()]);
    }

    /**
     * Fills a byte in String to be a full 8 characters
     *
     * @param binary a String with binary
     * @return a full byte String representation
     */
    private String fillByte(String binary) {
        String result = binary;

        while(result.length() < 8 && result.length() != 0) {
            result = "0" + result;
        }

        return result;
    }

    //
    // Our text related objects
    //
    public OccurrenceCounter counter;
    public HuffmanTree tree;

    //
    // The input string
    //
    protected String text;
    protected boolean isUnicode;
}
