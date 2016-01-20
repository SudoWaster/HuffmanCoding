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
 * OccurrenceIndex implementation used just for storing current node value
 *
 * @author cezary
 */
public class NodeValue implements OccurrenceIndex {

    /**
     * Create a NodeValue (with a value)
     *
     * @param value a double - value of the node
     */
    public NodeValue(double value) {
        this.value = value;
    }

    /**
     * Return dummy character - we don't need this.
     *
     * @return null char
     */
    public char getCharacter() {
        return '\0';
    }

    /**
     * Return node value
     *
     * @return a double representing value
     */
    public double getOccurrence() {
        return value;
    }

    //
    // The value of the node
    //
    public double value = 0;
}
