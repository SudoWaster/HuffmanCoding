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

import huffmancoding.TextTools.CharacterOccurrence;
import huffmancoding.TextTools.HuffmanCharacter;
import huffmancoding.Tree.HuffmanTree;

/**
 * A decoder class implementation for Huffman coding
 *
 * @author cezary
 */
public class HuffmanDecoder {

    /**
     * Constructor for handled a joint stream - dictionary with text to decode
     * 
     * @param stream a Byte[] to fully decode
     */
    public HuffmanDecoder(Byte[] stream) {
        initStream(stream);
        createTree();
    }

    /**
     * Constructor for String dictionary byte stream.
     *
     * @param text a String to decode
     * @param dictionary a String byte stream of dictionary
     */
    public HuffmanDecoder(String text, String dictionary) {
        this.text = text;

        buffer = dictionary;
        createTree();
    }

    /**
     * Constructor for Byte array byte stream.
     *
     * @param text a String to decode
     * @param dictionary a Byte[] stream of dictionary
     */
    public HuffmanDecoder(String text, Byte[] dictionary) {
        //
        // Save the text to decode
        //
        this.text = text;

        initStream(dictionary);

        createTree();
    }

    /**
     * Converts Byte[] input stream to String and prepares for tree creation
     *
     * @param stream a Byte[] input
     */
    private void initStream(Byte[] stream) {
        //
        // Convert Byte[] to String
        //
        String convertedDictionary = new String();
        for(Byte b : stream) {
            String temp = Integer.toBinaryString(b.byteValue());

            while(temp.length() < 8) {
                temp = "0" + temp;
            }

            convertedDictionary += temp;
        }

        //
        // Create a tree
        //
        buffer = convertedDictionary;
    }

    /**
     * Method used to create a tree with the dictionary. The tree is used
     * to get the ids and eventually decode the String.
     *
     * Uses standard HuffmanEncoder header:
     *  8 bits for the amount of characters
     *
     * for every character:
     *  8 bits for the character
     *  8 bits for the character id length
     *
     * @param dictionary a dictionary (header) String on which the tree is based
     */
    private void createTree() {
        //
        // Get the character amount and init CharacterOccurrecne array
        //
        int characters = Integer.parseInt(cutByteFromBuffer(), 2);

        CharacterOccurrence[] occurrences = new CharacterOccurrence[characters];

        //
        // Get all characters from the dictionary
        //
        for(int i = 0; i < characters; i++) {
            //
            // Get the char from the first byte
            //
            char currentChar = (char) Integer.parseInt(cutByteFromBuffer(), 2);

            //
            // Calculate a virtual frequency of occurrence
            //
            double frequency =
                    (double) Integer.parseInt(cutByteFromBuffer(), 2)
                        / characters;

            //
            // Add to occurrences array
            //
            occurrences[i] = new CharacterOccurrence(currentChar, frequency);
        }

        //
        // The leftover buffer is probably the input text
        //
        if(buffer.length() > 0) {
            this.text = buffer;
        }

        //
        // Finally create the tree
        //
        tree = new HuffmanTree(occurrences);
        HuffmanTree.updateIDs(tree.root, "");
    }

    /**
     * Returns a decoded String
     *
     * @return a String decoded using Huffman method
     */
    public String getDecoded() {
        //
        // Prepare result and a temporary stream
        //
        String result = new String();
        String byteStream = new String();

        //
        // Create the stream from input text
        //
        for(char c : text.toCharArray()) {
            String currentByte = Integer.toBinaryString(c);

            while(currentByte.length() < 8) {
                currentByte = "0" + currentByte;  // fill the byte
            }

            byteStream += currentByte;
        }

        //
        // Create a HuffmanCharacter result variable
        //
        HuffmanCharacter character = null;

        while(character == null && byteStream.length() > 0) {
            //
            // Search for character by 1 bit
            //
            String characterBuffer = byteStream.substring(0, 1);
            byteStream = byteStream.substring(1);

            character = tree.get(characterBuffer);

            //
            // If found, add it to the result and reset searching
            //
            if(character != null) {
                result += character.getCharacter();
                character = null;
            }
        }

        //
        // Finish decoding
        //
        return result;
    }

    /**
     * Cut and return a byte from buffer or stream
     *
     * @param buffer a String to cut a byte from
     * @return a cut byte (in String)
     */
    private String cutByteFromBuffer() {
        //
        // Check if we're not extending the byte buffer
        //
        int length = 8;

        if(buffer.length() < 8) {
            length = buffer.length();
        }

        //
        // Cut the byte
        //
        String cutByte = buffer.substring(0, length);
        buffer = buffer.substring(length);

        return cutByte;
    }

    //
    // Input text objects
    //
    public HuffmanTree tree;

    //
    // Input String to decode
    //
    protected String text;

    //
    // Buffer used for tree creation from the dictionary
    //
    private String buffer;
}
