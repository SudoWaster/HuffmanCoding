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

import huffmancoding.TextTools.Interfaces.OccurrenceIndex;

/**
 * A node implementation for a Huffman binary tree
 * 
 * @author cezary
 */
public class HuffmanNode {

    /**
     * Default constructor, creates a branch node.
     */
    public HuffmanNode() {
        this.value = new NodeValue(0);
    }

    /**
     * Constructor used for creating a leaf node.
     *
     * @param value an OccurrenceIndex that is a node value
     */
    public HuffmanNode(OccurrenceIndex value) {
        this.value = value;
    }

    /**
     * Identifies itself as a leaf or not.
     *
     * @return a boolean representing identification of a leaf node
     */
    public boolean isLeaf() {
        return (nodes[0] == null) && (nodes[1] == null);
    }

    /**
     * Sets a node's node value (inserts itself a node).
     *
     * @param node an int representing side of the node
     * @param value a HuffmanNode to insert
     */
    public void setNode(int node, HuffmanNode value) {

        this.nodes[node] = value;
        updateValue();
    }

    /**
     * Returns a subnode.
     *
     * @param node an int representing side of the node
     * @return a requested HuffmanNode
     */
    public HuffmanNode getNode(int node) {
        return this.nodes[node];
    }

    /**
     * Updates a node value with a sum of its nodes' values. Used with branch
     * nodes.
     *
     */
    private void updateValue() {
        //
        // init sum
        //
        double sum = 0;

        for(HuffmanNode node : nodes) {
            //
            // Go through the nodes and sum them
            //
            if(node != null) {
                sum += node.value.getOccurrence();
            }
        }

        //
        // Update node's value
        //
        this.value = new NodeValue(sum);
    }

    //
    // Node qualities
    //
    public OccurrenceIndex value;
    public String id;

    //
    // Subnodes
    //
    private HuffmanNode[] nodes = new HuffmanNode[2];
}
