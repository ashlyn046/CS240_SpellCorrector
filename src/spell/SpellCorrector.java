package spell;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Trie trie; //UPDATE? may not want spellcorrector to HAVE a trie...?

    //constructor
    public SpellCorrector() {
        trie = new Trie();
    }

    public Trie getTrie() { //UPDATE MAY NOT WANT
        return trie;
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {

        //creating a file object containing the dictionary file
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file); //creating a scanner object and putting in the file
        //reads in , default delimiter is spaces and newlines

        //creating an instance of our Trie CHANGED
        //Trie trie = new Trie();

        while (scanner.hasNext()) {
            //reading in the next word
            String currstrraw = scanner.next();
            //converting it to lowercase
            String currstr = currstrraw.toLowerCase();

            trie.add(currstr);

            //this code is a check
            //System.out.println(currstr);
            //i++;
        }

        //UPDATE CHECKS

        /*
        //printing trie, don't actually do this!
        System.out.println(trie);

        //checking equals UPDATE DONT KEEP
        //creating a NEW instance of our Trie
        File file2 = new File(dictionaryFileName);
        Scanner scanner2 = new Scanner(file2);

        Trie trie2 = new Trie();

        while(scanner2.hasNext()){
            //reading in the next word
            String currstrraw2 = scanner2.next();
            //converting it to lowercase
            String currstr2 = currstrraw2.toLowerCase();

            trie2.add(currstr2);

            //this code is a check
            //System.out.println(currstr);
            //i++;
        }
        System.out.println(trie2);

        System.out.println(trie.equals(trie2));
        System.out.println(trie.hashCode());
        System.out.println(trie2.hashCode());

        Trie trie3 = new Trie();
        System.out.println(trie3.hashCode());

         */


    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (trie.find(inputWord) != null) {
            return inputWord;
        }

        //added for failed test case but not neccessary??
        else if(inputWord == ""){
            return null;
        }

        else {
            Map<String, Integer> dist1 = new HashMap<String, Integer>();
            Map<String, Integer> dist2 = new HashMap<String, Integer>();

            //adding all of the edit distance 1 words to the map
            dist1.putAll(deletion(inputWord));
            dist1.putAll(transposition(inputWord));
            dist1.putAll(alteration(inputWord));
            dist1.putAll(insertion(inputWord));

            //System.out.println("dist1: " + dist1);

            //if there is only one in the set, then return that one as the suggestion
            if(case1(dist1) != null){
                return case1(dist1);
            }

            //if there is more than one word in the set, keeping only those with the max number of occurrences
            if(case2(dist1) != null){
                return case2(dist1);
            }

            //if there are no words, calculating the dist 2 set
            for (String word : dist1.keySet()) {
                dist2.putAll(deletion(word));
                dist2.putAll(transposition(word));
                dist2.putAll(alteration(word));
                dist2.putAll(insertion(word));
            }
            //System.out.println("dist2: " + dist2);

            //if there is only one in the set, then return that one as the suggestion
            if(case1(dist2) != null){
                return case1(dist2);
            }

            //if there is more than one word in the set, keeping only those with the max number of occurrences
            if(case2(dist2) != null){
                return case2(dist2);
            }

            //if there are no words, output error
            return null;
        }
    }

    public String case1(Map<String, Integer> dist){
        if(dist.keySet().size() == 0){
            return null;
        }
        //if there is only one in the set, then return that one as the suggestion

        //creating a new hashmap with only those that actualy occur
        Map<String, Integer> inmap = new HashMap<String, Integer>();
        for (String word : dist.keySet()) {
            if(dist.get(word) != 0){
                inmap.put(word, dist.get(word));
            }
        }
        //System.out.println("inmap: " + inmap);

        if(inmap.keySet().size() == 1){
            for (String word : inmap.keySet()) {
                //System.out.println("only 1");
                return word;
            }
        }
        return null;
    }

    public String case2(Map<String,Integer> dist) {
        if(dist.keySet().size() == 0){
            return null;
        }

        //keeping only the ones we want
        Map<String, Integer> inmap = new HashMap<String, Integer>();
        for (String word : dist.keySet()) {
            if(dist.get(word) != 0){
                inmap.put(word, dist.get(word));
            }
        }

        Integer max = 0;
        Map<String, Integer> maxmap = new HashMap<String, Integer>();

        //finding the max
        for (String word : inmap.keySet()) {
            if (inmap.get(word) > max) {
                max = inmap.get(word);
                //System.out.println("max dist: " + inmap.get(word));
            }
        }

        //keeping only those with the max
        for (String word : inmap.keySet()) {
            if (inmap.get(word) == max) {
                maxmap.put(word, inmap.get(word));
            }
        }

        //if there is only one in the set, then return that one as the suggestion
        if (maxmap.keySet().size() == 1) {
            for (String word : maxmap.keySet()) {
                return word;
            }
        }

        //if there is more than one, then return the alphabetically sorted first
        //BUG THIS COULD BE WRONG IF ITS NOT AUTO ALPHA UPDATE
        List<String> list = new ArrayList<String>(maxmap.keySet());
        Collections.sort(list);
        Integer i = 0;
        if (maxmap.keySet().size() != 1) {
            for (String word : list) {
                if (i == 0) {
                    return word;
                }
                i++;
            }
        }
        return null;
    }

    public Map deletion(String str) {
        //returns a map with all the words IN THE TRIE of deletion dist 1 from input word and number of occurrences

        //this is going to be a map of all the distance 1 words and the number of times they occur in the dictionary
        Map<String, Integer> deletionmap = new HashMap<String, Integer>();
        String currstr = "";

        //This code successfully adds all the deletion distance 1 words to the map that are in the set
        for (int i = 0; i < str.length(); i++) {
            String substr = "" + str.charAt(i);
            currstr = str.replaceFirst(substr, "");

            //this code adds it to the map with value number of times it is in the set
            if (trie.find(currstr) != null) {
                deletionmap.put(currstr, trie.find(currstr).getValue());
            }
            else{
                deletionmap.put(currstr, 0);
            }
        }
        //System.out.println("deletemap: " + deletionmap);

        /*
        Deletion Distance: A string s has a deletion distance 1 from another string t if and only if t is
        equal to s with one character removed. The only strings that are a deletion distance of 1 from “bird”
        are “ird”, “brd”, “bid”, and “bir”. Note that if a string s has a deletion distance of 1 from another
        string t then |s| = |t| -1. Also, there are exactly |t| strings that are a deletion distance of 1 from t.
        The dictionary may contain 0 to n of the strings one deletion distance from t.
         */
        return deletionmap;
}

    public Map transposition(String str){
        //returns a map with all the words IN THE TRIE of transposition dist 1 from input word and number of occurrences

        //this is going to be a map of all the distance 1 words and the number of times they occur in the dictionary
        Map<String, Integer> transmap = new HashMap<String, Integer>();
        String currstr;

        for(int i = 0; i < str.length() - 1; i++){
            //creating a temp to store the value at the first index
            char[] temparray = str.toCharArray();
            char tempchar = temparray[i];

            //swapping the values at the indexes
            temparray[i] = temparray[i+1];
            temparray[i+1] = tempchar;

            currstr = new String(temparray);

            if (trie.find(currstr) != null) {
                transmap.put(currstr, trie.find(currstr).getValue());
            }
            else{
                transmap.put(currstr, 0);
            }
        }
        //System.out.println("transmap" + transmap);
          /*
        Transposition Distance: A string s has a transposition distance 1 from another string t if and only if t
        is equal to s with two adjacent characters transposed. The only strings that are a transposition Distance
        of 1 from “house” are “ohuse”, “huose”, “hosue” and “houes”. Note that if a string s has a transposition
        distance of 1 from another string t then |s| = |t|. Also, there are exactly |t| - 1 strings that are a
        transposition distance of 1 from t. The dictionary may contain 0 to n of the strings one transposition
        distance from t.
         */
        return transmap;
    }

    public Map alteration(String str){ //i think this code works, but possible bug!
        //returns a map with all the words IN THE TRIE of alteration dist 1 from input word and number of occurrences

        //this is going to be a map of all the distance 1 words and the number of times they occur in the dictionary
        Map<String, Integer> altmap = new HashMap<String, Integer>();
        String currstr;

        //this increments through the characters in a string
        for(int i = 0; i < str.length(); i++){
            char[] temparray = str.toCharArray();

            for(int j = 0; j < 26; j++){
                temparray[i] = (char) (j + 'a');

                currstr = new String(temparray);

                if (trie.find(currstr) != null) {
                    altmap.put(currstr, trie.find(currstr).getValue());
                }
                else{
                    altmap.put(currstr, 0);
                }
            }
        }
        //System.out.println("altmap: " + altmap);


        /*
        Alteration Distance: A string s has an alteration distance 1 from another string t if and only if t is
        equal to s with exactly one character in s replaced by a lowercase letter that is not equal to the
        original letter. The only strings that are an alternation distance of 1 from “top” are “aop”, “bop”, …,
        “zop”, “tap”, “tbp”, …, “tzp”, “toa”, “tob”, …, and “toz”. Note that if a string s has an alteration
        distance of 1 from another string t then |s| = |t|. Also, there are exactly 25* |t| strings that are an
        alteration distance of 1 from t. The dictionary may contain 0 to n of the strings one alteration distance
        from t.
         */
        return altmap;
    }

    public Map insertion(String str){ //i think this wors... but possible update bug
        //returns a map with all the words IN THE TRIE of insertion dist 1 from input word and number of occurrences

        //this is going to be a map of all the distance 1 words and the number of times they occur in the dictionary
        Map<String, Integer> insmap = new HashMap<String, Integer>();
        String currstr;

        //this increments through the characters in a string
        for(int i = 0; i < str.length() + 1; i++){
            StringBuilder tempstr;

            for (char c = 'a'; c <= 'z'; c++) {
                tempstr = new StringBuilder(str);

                tempstr.insert(i, c);
                currstr = tempstr.toString();

                if (trie.find(currstr) != null) {
                    insmap.put(currstr, trie.find(currstr).getValue());
                }
                else{
                    insmap.put(currstr, 0);
                }
            }
        }
        //System.out.println("insmap: " + insmap);

        /*
        Insertion Distance: A string s has an insertion distance 1 from another string t if and only if t has a
        deletion distance of 1 from s. The only strings that are an insertion distance of 1 from “ask” are “aask”,
        “bask”, “cask”, … “zask”, “aask”, “absk”, “acsk”, … “azsk”, “asak”, “asbk”, “asck”, … “aszk”, “aska”,
        “askb”, “askc”, … “askz”. Note that if a string s has an insertion distance of 1 from another string t
        then |s| = |t|+1. Also, there are exactly 26* (|t| + 1) strings that are an insertion distance of 1 from t.
        The dictionary may contain 0 to n of the strings one insertion distance from t.
         */
        return insmap;
    }
}
