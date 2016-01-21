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

package huffmancoding;

import huffmancoding.Coders.HuffmanEncoder;
import huffmancoding.TextTools.CharacterOccurrence;
import huffmancoding.TextTools.Occurrence;
import huffmancoding.TextTools.OccurrenceCounter;
import huffmancoding.Tree.HuffmanTree;

/**
 *
 * @author cezary
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String input = "Copyright C Cezary Regec SudoWaster " +
            "This program is free software you can redistribute it andor modify it under the terms of the GNU General Public License as published by the Free Software Foundation either version of the License or at your option any later version " +
            "This program is distributed in the hope that it will be useful but WITHOUT ANY WARRANTY without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE  See the GNU General Public License for more details " +
            "You should have received a copy of the GNU General Public License along with this program if not write to the Free Software Foundation Inc Temple Place Suite Boston MA USA";

        //
        // Test occurrence count, sorting and tree creation
        //
        OccurrenceCounter c = new OccurrenceCounter(input);
        CharacterOccurrence[] o = c.getFullOccurrence();
        Occurrence.sort(o);
        HuffmanTree tree = new HuffmanTree();
        
        for(CharacterOccurrence e : o) {
            tree.insert(e);
        }

        HuffmanTree.updateIDs(tree.root, "");

        //
        // Tree verification
        //
        for(CharacterOccurrence e : o) {
            System.out.println(e.getCharacter() + " - " + e.getOccurrence() + " - " + tree.get(e.getCharacter()).getID());
        }

        //
        // Test encoding
        //
        HuffmanEncoder he = new HuffmanEncoder(input);
        Byte[] encoded = he.getEncoded();

        String result = new String();
        for(Byte e : encoded) {
            result += (char)e.byteValue();
        }

        System.out.println(input + "\n" + result);
        System.out.println((double)result.length()/input.length());
    }

}
