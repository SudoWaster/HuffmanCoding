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


    public Byte[] getDictionary() {
        //
        // TODO: Make a method returning a byte array for dictionary headers.
        //      Should use a byte for the id length and a following space
        //      for the id. It should utilize at least 256 bytes for every
        //      ASCII character (length, id, length, id, length, id etc.)
        //
        String dictionaryHeader = new String();

        for(int i = 0; i < 256; i++) {
            HuffmanCharacter character = tree.get((char) i);

            String idLength = "";
            String id = "";

            if(character != null) {
                idLength = Integer.toBinaryString(character.getID().length());
                id = character.getID();
            }

            while(idLength.length() < 8) {
                idLength = "0" + idLength;
            }

            dictionaryHeader += idLength + id;
        }

        ArrayList<Byte> dictionary = new ArrayList<Byte>();

        while(dictionaryHeader.length() > 8) {
            String buffer = dictionaryHeader.substring(0, 8);
            dictionaryHeader = dictionaryHeader.substring(8);

            while(buffer.length() < 8) {
                buffer += "0";
            }

            dictionary.add(Byte.parseByte(buffer, 2));
        }

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
