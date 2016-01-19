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

package huffmancoding.Tree;

import huffmancoding.TextTools.CharacterOccurrence;

/**
 * A simple Huffman tree representation.
 *
 * @author cezary
 */
public class HuffmanTree {

    public static enum NODE {
        LEFT(0), RIGHT(1);

        private final int value;
        private NODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public HuffmanTree() {
        this.root = new HuffmanNode();
    }

    public void insert(CharacterOccurrence element) {
        HuffmanNode node = root;
        HuffmanNode parent = null;
        while(node != null && element.getOccurence() < node.value.getOccurence()) {

        }
    }
    
    private HuffmanNode root;
}
