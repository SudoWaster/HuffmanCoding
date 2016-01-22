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
     * Init class with text - count characters and make a tree
     *
     * @param text a String with input
     */
    public HuffmanEncoder(String text) {
        this.text = text;
        this.counter = new OccurrenceCounter(text);
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
            // Get current character corresponding object and id
            //
            HuffmanCharacter currentCharacter = tree.get(text.charAt(i));

            collectiveBuffer += currentCharacter.getID();

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

            bytelist.add(Byte.parseByte(leftover, 2));
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
        // Create our String buffer and our character amount info
        //
        String dictionaryStream = new String();
        int foundChars = 0;

        //
        // Search for used symbols
        //
        for(int i = 0; i < 256; i++) {
            //
            // Get the character info
            //
            HuffmanCharacter character = tree.get((char) i);

            //
            // Prepare our character information for the dictionary
            //
            String charByte = "";
            String idLength = "";
            //String id = "";

            if(character != null) {
                //
                // If character is found, set up the info
                //
                charByte = Integer.toBinaryString(character.getCharacter());
                idLength = Integer.toBinaryString(character.getID().length());
                //id = character.getID();

                foundChars++;
            }

            //
            // Fill the byte for future parsing
            //
            while(idLength.length() < 8 && idLength.length() != 0) {
                idLength = "0" + idLength;
            }

            //
            // Add to dictionary stream
            //
            dictionaryStream += charByte + idLength; // + id;
        }

        //
        // Add a symbolic length info at the beginning
        //
        dictionaryStream =
                Integer.toBinaryString(foundChars) + dictionaryStream;

        // ---------------------------------------------------------------- //

        //
        // Start parsing to byte array
        //
        ArrayList<Byte> dictionary = new ArrayList<Byte>();

        while(dictionaryStream.length() > 8) {
            //
            // Cut out a byte
            //
            String buffer = dictionaryStream.substring(0, 8);
            dictionaryStream = dictionaryStream.substring(8);

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

    //
    // Our text related objects
    //
    public OccurrenceCounter counter;
    public HuffmanTree tree;

    //
    // The input string
    //
    protected String text;
    
}
