/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AnagramDictionary {

    // variable for various game preferences
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private int currentWordLength = DEFAULT_WORD_LENGTH;

    // array list to store the words
    private ArrayList<String> wordList = new ArrayList<>();
    // set to temporarily store all values with the min number of anagrams
    private HashSet<String> wordSet = new HashSet<>();
    // map to store each sorted word along with its anagrams
    private HashMap<String, ArrayList<String>> wordMap = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        // get a buffered reader to read the file
        BufferedReader in = new BufferedReader(reader);
        // initialize variable to store each line
        String line;
        // initialize a string to store each word
        String word;
        // while there are still words to read
        while((line = in.readLine()) != null) {
            // trim the word read
            word = line.trim();
            // add the word to our list
            wordList.add(word);
        }
        loadWordMap();
    }

    public boolean isGoodWord(String word, String base) {
        // if the word is in the dictionary and is not a substring
        if(!word.contains(base)) //&& getAnagramsWithOneMoreLetter(base).contains(word)
            return true;
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        // get the anagrams for the sorted string
        return wordMap.get(stringSort(targetWord));
    }

    public HashMap<String, ArrayList<String>> loadWordMap() {
        // use a temp variable to store the sorted words
        String temp;
        // go through all the words in wordList
        for(String regularWord : wordList) {
            // set the temp variable to be the sorted string
            temp = stringSort(regularWord);
            // if the word is not in the hashMap
            if(!wordMap.containsKey(temp))
                // add a new entry
                wordMap.put(temp, new ArrayList<String>());
            // add the element (regardless as a new entry was made or exists)
            wordMap.get(temp).add(regularWord);
            // if the word is a possible starter word
            if(getAnagrams(temp).size() >= MIN_NUM_ANAGRAMS) {
                // add it to a set
                wordSet.add(regularWord);
            }
        }
        // return the hashMap for reference
        return wordMap;
    }

    public String stringSort(String word) {
        // convert the string to an array of chars
        char[] wordChars = word.toCharArray();
        // sort the array (alphabetically)
        Arrays.sort(wordChars);
        // turn the chars back into a string
        String sortedWord = new String(wordChars);
        // return the alphabetical string
        return sortedWord;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        // put the word in alphabetical order
        word = stringSort(word);
        // result arrayList to store the anagrams
        ArrayList<String> result = new ArrayList<>();
        // temp variable to store word length
        int tempLength;
        // go through the key set
        for(String sortedWord : wordMap.keySet()) {
            // reset the counter for each word
            tempLength = 0;
            // if the anagram is one greater in length
            if (sortedWord.length() == word.length() + 1) {
                // go through the words letters
                for (int i = 0; i < sortedWord.length(); i++) {
                    // if the letters match
                    if (word.charAt(tempLength) == sortedWord.charAt(i))
                        // increment the counter
                        tempLength++;
                    // if the counter is the size of the word being searched
                    if (tempLength == word.length()) {
                        // then add all their anagrams to the result
                        result.addAll(getAnagrams(sortedWord));
                        break;
                    }
                }
            }
        }
        // return the list
        return result;
    }

    public String pickGoodStarterWord() {
        // initialize the possible starting words into a list
        ArrayList<String> randomWord = new ArrayList<String>(wordSet);
        // random value for indexing a random start value
        int random = (int) (Math.random() * randomWord.size());
        // get the word at the index (random value that was generated)
        String tempWord = randomWord.get(random);

        // keep searching, by changing the random value, until the length matches
        while(tempWord.length() != currentWordLength) {
            // get a new value
            random = (int) (Math.random() * randomWord.size());
            // get the new word with that value
            tempWord = randomWord.get(random);
        }
        // if the word length is too high
        if(currentWordLength >= MAX_WORD_LENGTH)
            // reset the game
            currentWordLength = DEFAULT_WORD_LENGTH;
        // otherwise
        else
            // increase difficulty
            currentWordLength++;
        // return the correct word
        return randomWord.get(random);
    }
}
