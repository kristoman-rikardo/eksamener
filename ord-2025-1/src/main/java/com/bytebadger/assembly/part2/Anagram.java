package com.bytebadger.assembly.part2;

// import java.util.HashMap;
import java.util.Map;

// Import relevant libraries

public class Anagram {

    /**
     * This method checks if two strings are anagrams.
     * 
     * Two strings are anagrams if they contain the same characters
     * in the same frequency, but possibly in a different order.
     * The method ignores case and spaces.
     * 
     * Empty strings cannot be anagrams.
     * Strings of different length cannot be anagrams.
     * 
     * Example:
     * The words "listen" and "silent" are anagrams.
     * 
     * @param str1 the first input string
     * @param str2 the second input string
     * @return a boolean value indicating whether the two strings are anagrams
     */
    public boolean isAnagram(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() < 1 || str2.length() < 1) return false;
        Map<Character, Integer> map1 = CharacterFrequency.countCharacterFrequency(str1); // implemented the CharacterFrequency first to easily use it within this class
        Map<Character, Integer> map2 = CharacterFrequency.countCharacterFrequency(str2);
        return (map1.equals(map2)); // if the maps are equal, all letters and its frequencies are equal
    }
}

