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

    public HuffmanNode() {
        this.value = new NodeValue(0);
    }

    public HuffmanNode(OccurrenceIndex value) {
        this.value = value;
    }

    public boolean isLeaf() {
        return (nodes[0] == null) && (nodes[1] == null);
    }

    public void setNode(int node, HuffmanNode value) {
        this.nodes[node] = value;
        sumValues();
    }

    public HuffmanNode getNode(int node) {
        return this.nodes[node];
    }

    public double compareTo(HuffmanNode that) {
        return this.value.getOccurrence() - that.value.getOccurrence();
    }

    private void sumValues() {
        double sum = 0;
        for(HuffmanNode node : nodes) {
            if(node != null) {
                sum += node.value.getOccurrence();
            }
        }

        this.value = new NodeValue(sum);
    }

    public OccurrenceIndex value;

    private HuffmanNode[] nodes = new HuffmanNode[2];
}
