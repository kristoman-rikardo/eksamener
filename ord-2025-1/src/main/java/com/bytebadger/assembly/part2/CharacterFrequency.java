package com.bytebadger.assembly.part2;

import java.util.HashMap;
import java.util.Map;

// Import relevant libraries


public class CharacterFrequency {

    /**
     * This method counts the frequency of each character in a given string.
     * 
     * @return a Map with characters as keys and their frequencies as values
     */

    public static Map<Character, Integer> countCharacterFrequency(String input) {
        if (input == null || input.length() < 1) return new HashMap<>();
        char[] chars = input.toLowerCase().toCharArray();
        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : chars) {
            if (c != ' ') { // made some modifications and assumptions that _characters_ is the real characters, not spaces which made it work for the Anagram class.
                if (map.containsKey(c)) map.put(c, map.get(c) + 1); // putting one more in the value if it exists
                else {map.put(c, 1);} // counting 1 if it did not exist-
            }
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(countCharacterFrequency("aab bcc cd")); // main method to test
    }
}
