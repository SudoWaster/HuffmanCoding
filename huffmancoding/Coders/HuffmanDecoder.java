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
import huffmancoding.Tree.HuffmanTree;

/**
 *
 * @author cezary
 */
public class HuffmanDecoder {


    public HuffmanDecoder(String text, String dictionary) {
        this.text = text;
    }

    public HuffmanDecoder(String text, Byte[] dictionary) {
        String temp = new String();
        for(Byte b : dictionary) {
            temp += Integer.toBinaryString(b.byteValue());
        }

        this.text = text;
    }

    private void createTree(String dictionary) {
        String header = dictionary;

        int characters = Integer.parseInt(cutByteFrom(header), 2);

        CharacterOccurrence[] occurrences = new CharacterOccurrence[characters];

        for(int i = 0; i < characters; i++) {
            char currentChar = (char) Integer.parseInt(cutByteFrom(header), 2);

            double frequency =
                    (double) Integer.parseInt(cutByteFrom(header), 2) / characters;

            occurrences[i] = new CharacterOccurrence(currentChar, frequency);
        }

        tree = new HuffmanTree(occurrences);
    }

    private String cutByteFrom(String buffor) {
        String bufforByte = buffor.substring(0, 8);
        buffor = buffor.substring(8);

        return bufforByte;
    }

    public HuffmanTree tree;

    protected String text;
}
