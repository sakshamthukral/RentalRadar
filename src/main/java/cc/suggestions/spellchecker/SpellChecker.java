package cc.suggestions.spellchecker;
import java.util.List;

class SpellChecker {
    SplayTree dictionaryTree = new SplayTree();

    // Load a word into the splay tree
    public void loadWordIntoDictionary(String word) {
        dictionaryTree.insert(word);
    }

    // Check if the word is spelled correctly
    public boolean isSpelledCorrectly(String word) {
        return dictionaryTree.isWordInDictionary(word);
    }

    // Suggest words starting with the given prefix
    public List<String> suggestWords(String prefix) {
        return dictionaryTree.findResemblingWords(prefix);
    }
}
