# CS240 Spell Corrector

This repository contains a spelling corrector developed for BYU's CS240 course. The project focuses on building a spell checker capable of suggesting corrections for single-word input based on word frequency and edit distance. Code can be found under `src/spell`.

## Project Overview

The spelling corrector is a Java program that:
- Builds a dictionary from a text file, counting word occurrences to track frequency.
- Uses a Trie data structure to store words, making it efficient to check for and retrieve suggestions.
- Implements an edit distance algorithm to find the "most similar" word when a misspelling occurs.

## Trie Structure

The dictionary is implemented as a Trie where:
- Each node corresponds to a character, and each path from the root to a node represents a word.
- Nodes store a count of word occurrences from the text file.

The Trie supports the following edit distance types:
1. **Deletion**: Removing one character.
2. **Transposition**: Swapping two adjacent characters.
3. **Alteration**: Replacing one character.
4. **Insertion**: Adding one character.

## Key Functionalities

1. **Word Lookup**: Verifies if a word exists in the dictionary.
2. **Spelling Suggestion**: Suggests a similar word based on:
   - Edit distance (1 or 2).
   - Frequency (higher frequency preferred).
   - Alphabetical order (when frequency is the same).

3. **Trie Methods**: Implements standard methods like `toString()`, `hashCode()`, and `equals()` for Trie operations.
   

## Deliverables

This project provides:
- Classes implementing `ITrie`, `INode`, and `ISpellCorrector` interfaces.
- Main program that loads words, checks spelling, and provides suggestions.
