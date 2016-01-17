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
 * A class used for storing occurrence frequency of a character.
 * An implementation of huffmanencoding.TextTools.Interfaces.OccurrenceIndex
 *
 * @author cezary
 */
public class CharacterOccurrence implements OccurrenceIndex {

    public CharacterOccurrence(char character, double frequency) {
        //
        // Init fields needed for implementation
        //
        the_character = character;
        the_occurrence = frequency;
    }

    /**
     * Returns the character.
     * @return a char
     */
    public char getCharacter() {
        return the_character;
    }

    /**
     * Returns frequency of the character.
     *
     * @return the double occurrence of the char
     */
    public double getOccurence() {
        return the_occurrence;
    }

    //
    // Fields for methods implementation.
    //
    private char the_character;
    private double the_occurrence;
}
