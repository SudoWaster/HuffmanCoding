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
        HuffmanNode newNode = new HuffmanNode(element);
        System.out.println(newNode.value.getCharacter());

        if(root.isLeaf()) {
            root.setNode(NODE.LEFT.getValue(), newNode);
        } else if(hasNullNodes(root)) {
            seekAndInject(root, newNode);
        } else {
            HuffmanNode theNewRoot = new HuffmanNode();
            theNewRoot.setNode(NODE.LEFT.getValue(), root);
            if(element.getOccurrence() > root.value.getOccurrence()) {
                theNewRoot.setNode(NODE.RIGHT.getValue(), newNode);
            } else {
                HuffmanNode theParallel = new HuffmanNode();
                theParallel.setNode(NODE.LEFT.getValue(), newNode);
                theNewRoot.setNode(NODE.RIGHT.getValue(), theParallel);
            }
            root = theNewRoot;
        }
    }

    private void seekAndInject(HuffmanNode node, HuffmanNode element) {
        HuffmanNode temp = node;
        HuffmanNode parent = null;
        int nodeSide = 0;
        
        while(hasNullNodes(temp) && (temp != null)) {
            parent = temp;
            if(hasNullNodes(temp.getNode(NODE.LEFT.getValue()))) {
                temp = temp.getNode(NODE.LEFT.getValue());
                nodeSide = 0;
            } else {
                temp = temp.getNode(NODE.RIGHT.getValue());
                nodeSide = 1;
            }
        }

        parent.setNode(nodeSide, element);
    }

    public static boolean hasNullNodes(HuffmanNode node) {
        if(node == null) {
            return true;
        } else if(node.isLeaf()) {
            System.out.println(node.value.getCharacter() + " " + node.id);
            return false;
        } else {
            return (hasNullNodes(node.getNode(NODE.LEFT.getValue()))
                    || hasNullNodes(node.getNode(NODE.RIGHT.getValue())));
        }
    }

    public static void updateIDs(HuffmanNode node, String id) {
        
        node.id = id;

        if(node.getNode(NODE.LEFT.getValue()) != null) {
            updateIDs(node.getNode(NODE.LEFT.getValue()), id + NODE.LEFT.getValue());
        }

        if(node.getNode(NODE.RIGHT.getValue()) != null) {
            updateIDs(node.getNode(NODE.RIGHT.getValue()), id + NODE.RIGHT.getValue());
        }
    }
    
    public HuffmanNode root;
}
