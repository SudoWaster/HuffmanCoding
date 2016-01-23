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
import huffmancoding.TextTools.HuffmanCharacter;
import huffmancoding.TextTools.Interfaces.OccurrenceIndex;
import huffmancoding.TextTools.Occurrence;
import java.util.ArrayList;

/**
 * A simple Huffman tree implementation.
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
     * Default constructor.
     *
     */
    public HuffmanTree() {
        this.root = new HuffmanNode();
        this.originalOccurrences = new ArrayList<CharacterOccurrence>();
    }

    /**
     * Constructor that also created a tree based on an array of
     * CharacterOccurrences
     *
     * @param occurrences a CharacterOccurrences array
     */
    public HuffmanTree(CharacterOccurrence[] occurrences) {
        this();

        Occurrence.sort(occurrences);

        for(CharacterOccurrence element : occurrences) {
            insert(element);
        }
    }


    /**
     * Used for inserting elements from the bottom.
     * Elements with the same occurrence are places at the same level.
     *
     * @param element a CharacterOccurrence element to insert
     */
    public void insert(CharacterOccurrence element) {
        //
        // Save for later
        //
        this.originalOccurrences.add(element);

        //
        // Prepare a node to insert
        //
        HuffmanNode newNode = new HuffmanNode(element);

        if(root.isLeaf()) {
            //
            // If the root's a leaf, plant the tree.
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
     * Get all CharacterOccurrences in the order of insertion.
     *
     * @return a HuffmanCharacter array with ids
     */
    public HuffmanCharacter[] getAll() {
        //
        // Init result array
        //
        HuffmanCharacter[] result =
                new HuffmanCharacter[originalOccurrences.size()];

        for(int i = 0; i < result.length; i++) {
            //
            // Search for every character id - add full info to array
            //
            CharacterOccurrence currentChar = originalOccurrences.get(i);
            result[i] = get(currentChar.getCharacter());
        }
        
        return result;
    }

    /**
     * Search and return CharacterOccurrence from the tree basing on a character
     *
     * @param character a char searched in the tree
     * @return a corresponding CharacterOccurrence
     */
    public HuffmanCharacter get(char character) {
        return get(root, character, "");
    }

    /**
     * Search and return CharacterOccurrence from the tree basing on the id
     *
     * @param id a String id searched in the tree
     * @return a corresponding CharacterOccurrence
     */
    public HuffmanCharacter get(String id) {
        return get(root, '\n', id);
    }

    /**
     * Search for an OccurrenceIndex in a node using a character and id.
     *
     * @param node a start HuffmanNode
     * @param character a char being searched
     * @param id a String id being searched
     * @return a corresponding CharacterOccurrence
     */
    public HuffmanCharacter get(HuffmanNode node, char character, String id) {

        //
        // Default search results
        //
        HuffmanCharacter result = null;

        //
        // Search in the exsistent nodes
        //
        if(node != null) {

            if(!node.isLeaf()) {
                //
                // If not a leaf, search deeper.
                //
                result = get(node.getNode(NODE.LEFT.getValue()),
                        character, id);

                if(result == null) {
                    //
                    // If a left node search result is null, search right node
                    //
                    return get(node.getNode(NODE.RIGHT.getValue()), character,
                            id);

                } else {
                    //
                    // If found in left node, return it
                    //
                    return result;
                }

            } else if(node.value.getCharacter() == character
                    || node.id.equals(id)) {
                //
                // If found, return value
                //

                result = new HuffmanCharacter(node.value);
                result.id = node.id;
            }
        }

        //
        // Return search results
        //
        return result;
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

    //
    // Original occurrences for tree recreation
    //
    private ArrayList<CharacterOccurrence> originalOccurrences;
}
