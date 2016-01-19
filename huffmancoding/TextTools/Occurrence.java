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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class for handling CharacterOccurrence operations
 * @author cezary
 */
public class Occurrence {

    /**
     * This method sorts given array of CharacterOccurrence
     *
     * @param occurrence CharacterOccurrence array to sort
     */
    public static void sort(CharacterOccurrence[] occurrence) {
        //
        // Make a separate array of frequencies
        //
        double[] frequencies = new double[occurrence.length];

        for(int i = 0; i < frequencies.length; i++) {
            frequencies[i] = occurrence[i].getOccurrence();
        }

        //
        // Sort the frequencies
        //
        Arrays.sort(frequencies);

        //
        // Create a source ArrayList we'll be using to get elements from
        //
        ArrayList<CharacterOccurrence> temp =
                new ArrayList<CharacterOccurrence>(Arrays.asList(occurrence));

        //
        // Put sorted elements in the source array
        //
        for(int i = 0; i < frequencies.length; i++) {

            for(int j = 0; j < temp.size(); j++) {

                if(temp.get(j).getOccurrence() == frequencies[i]) {
                    //
                    // If the CharacterOccurrence is matched, put it on its
                    // place and remove from the source array to prevent
                    // multiple records
                    //
                    occurrence[i] = temp.get(j);
                    temp.remove(j);
                    break;
                }
            }
        }
    }
}
