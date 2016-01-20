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

    /**
     *
     * This provides node indication standard
     *
     */
    public static enum NODE {
        LEFT(0), RIGHT(1);

        //
        // Value representation
        //
        private final int value;
        private NODE(int value) {
            this.value = value;
        }

        /**
         * @return int representation
         */
        public int getValue() {
            return value;
        }
    }

    
    /**
     *
     * Default and only sensible constructor.
     *
     */
    public HuffmanTree() {
        this.root = new HuffmanNode();
    }


    /**
     * Used for inserting elements from the bottom.
     * Elements with the same occurrence are places at the same level.
     *
     * @param element a CharacterOccurrence element to insert
     */
    public void insert(CharacterOccurrence element) {
        //
        // Prepare a node to insert
        //
        HuffmanNode newNode = new HuffmanNode(element);

        if(root.isLeaf()) {
            //
            // If it's the leaf, plant the tree.
            //
            root.setNode(NODE.LEFT.getValue(), newNode);

        } else if(hasNullNodes(root)) {
            //
            // If there's a null node - use it.
            //
            seekAndInject(root, newNode);

        } else {
            //
            // Create a new level for the element
            //
            // Stage a new root
            //
            HuffmanNode theNewRoot = new HuffmanNode();
            theNewRoot.setNode(NODE.LEFT.getValue(), root);

            if(element.getOccurrence() > root.value.getOccurrence()) {
                //
                // If the element has a higher occurrence put it
                // on the higher level
                //
                theNewRoot.setNode(NODE.RIGHT.getValue(), newNode);

            } else {
                //
                // If the element has a lower or equal occurrence put it
                // on the same level
                //
                // Prepare a new parallel node
                //
                HuffmanNode theParallel = new HuffmanNode();

                //
                // Put the parallel node next to the current
                //
                theParallel.setNode(NODE.LEFT.getValue(), newNode);
                theNewRoot.setNode(NODE.RIGHT.getValue(), theParallel);
            }

            //
            // Update root node
            //
            root = theNewRoot;
        }
    }


    /**
     * Searches for a free node and inserts a HuffmanNode element
     *
     * @param node a HuffmanNode to begin search
     * @param element a HuffmanNode element to insert
     */
    private void seekAndInject(HuffmanNode node, HuffmanNode element) {
        //
        // Prepare temporary search variables
        //
        HuffmanNode temp = node;
        HuffmanNode parent = null;
        int nodeSide = 0;
        
        while(hasNullNodes(temp) && (temp != null)) {
            //
            // Go deeper
            //
            parent = temp;

            //
            // Does left node have any null nodes?
            //
            if(hasNullNodes(temp.getNode(NODE.LEFT.getValue()))) {
                //
                // Travel left and remember where you are
                //
                temp = temp.getNode(NODE.LEFT.getValue());
                nodeSide = 0;
            } else {
                //
                // Travel right and remember where you are
                //
                temp = temp.getNode(NODE.RIGHT.getValue());
                nodeSide = 1;
            }
        }

        //
        // Insert node element to the remembered position
        //
        parent.setNode(nodeSide, element);
    }


    /**
     * Used for determining whether a node has null nodes. Excludes leaves
     * since they are not branches - they have two null nodes.
     *
     * @param node a HuffmanNode to search
     * @return a boolean determining whether there are null nodes
     */
    public static boolean hasNullNodes(HuffmanNode node) {
        if(node == null) {
            //
            // If this node is null, then there are null nodes!
            //
            return true;
        } else if(node.isLeaf()) {
            //
            // We don't look into leaves' nodes, because they are unused.
            //
            return false;
        } else {
            //
            // Go deeper.
            //
            return (hasNullNodes(node.getNode(NODE.LEFT.getValue()))
                    || hasNullNodes(node.getNode(NODE.RIGHT.getValue())));
        }
    }


    /**
     * Set the IDs of the HuffmanNode elements using Huffman coding style.
     * Branch representation depends on the NODE enum values.
     *
     * @param node a HuffmanNode to search and mark
     * @param prefix a prefix to the element id
     */
    public static void updateIDs(HuffmanNode node, String prefix) {

        //
        // Set current node id
        //
        node.id = prefix;

        if(node.getNode(NODE.LEFT.getValue()) != null) {
            //
            // If we can, go and mark left
            //
            updateIDs(node.getNode(NODE.LEFT.getValue()), 
                    node.id + NODE.LEFT.getValue());
        }

        if(node.getNode(NODE.RIGHT.getValue()) != null) {
            //
            // If we can, go and mark right
            //
            updateIDs(node.getNode(NODE.RIGHT.getValue()), 
                    node.id + NODE.RIGHT.getValue());
        }
    }


    /**
     *
     * A root node where everything begins from
     * 
     */
    public HuffmanNode root;
}
