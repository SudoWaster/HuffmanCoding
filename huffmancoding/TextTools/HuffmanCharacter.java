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

package huffmancoding.TextTools;

import huffmancoding.TextTools.Interfaces.OccurrenceIndex;

/**
 * HuffmanCharacter is a CharacterOccurrence extension allowing to use id with
 * the character information. Used in BST search results to encode text.
 *
 * @author cezary
 */
public class HuffmanCharacter extends CharacterOccurrence {

    /**
     * Create from an existing CharacterOccurrence
     *
     * @param occurrence a CharacterOccurrence to extend
     */
    public HuffmanCharacter(OccurrenceIndex occurrence) {
        super(occurrence.getCharacter(), occurrence.getOccurrence());
    }

    /**
     * Returns its id in BST.
     *
     * @return a String id
     */
    public String getID() {
        return id;
    }

    /**
     * Returns its id, but using byte.
     *
     * @return a byte id
     */
    public byte getByte() {
        return Byte.parseByte(id, 2);
    }

    /**
     * Returns its byte id length - used for consolidating bytes
     *
     * @return an int length
     */
    public int getByteLength() {
        return id.length();
    }

    /**
     * ID in BST
     */
    public String id;
}
