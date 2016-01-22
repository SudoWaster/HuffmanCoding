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
     * Constructor for String dictionary byte stream.
     *
     * @param text a String to decode
     * @param dictionary a String byte stream of dictionary
     */
    public HuffmanDecoder(String text, String dictionary) {
        this.text = text;
        createTree(dictionary);
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

        //
        // Convert Byte[] to String
        //
        String temp = new String();
        for(Byte b : dictionary) {
            temp += Integer.toBinaryString(b.byteValue());
        }

        createTree(temp);
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
    private void createTree(String dictionary) {
        //
        // Init an original dictionary instance buffer
        //
        buffer = dictionary;

        //
        // Get the character amount and init CharacterOccurrecne array
        //
        int characters = Integer.parseInt(cutByteFromBuffer(), 2);
        System.out.println(characters);
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
        // Finally create the tree
        //
        tree = new HuffmanTree(occurrences);
        HuffmanTree.updateIDs(tree.root, "");

        for(CharacterOccurrence e : occurrences) {
            System.out.println(e.getCharacter() + " - " + e.getOccurrence() + " - " + tree.get(e.getCharacter()).getID());
        }

    }

    /**
     * Returns a decoded String
     *
     * @return a String decoded using Huffman method
     */
    public String getDecoded() {
        //
        // TODO: Use the tree to decode
        //
        String result = new String();

        String byteStream = new String();

        for(char c : text.toCharArray()) {
            byteStream += Integer.toBinaryString(c);
        }

        while(byteStream.length() > 0) {
            String buffer = "";

            HuffmanCharacter character = tree.get(buffer);

            while(character == null && byteStream.length() > 0) {
                buffer += byteStream.substring(0, 1);
                byteStream = byteStream.substring(1);
                //System.out.println(buffer);
            }

            if(character != null) {
                result += character.getCharacter();
            }
        }

        return result;
    }

    /**
     * Cut and return a byte from buffer or stream
     *
     * @param buffer a String to cut a byte from
     * @return a cut byte (in String)
     */
    private String cutByteFromBuffer() {
        int length = 8;

        if(buffer.length() < 8) {
            length = buffer.length();
        }
        
        String cutByte = buffer.substring(0, length);
        buffer = buffer.substring(length);

        return cutByte;
    }

    public HuffmanTree tree;

    protected String text;

    private String buffer;
}
