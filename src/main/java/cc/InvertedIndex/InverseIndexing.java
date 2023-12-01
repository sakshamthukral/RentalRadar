package cc.InvertedIndex;

import cc.utils.FileReader;

import java.nio.file.Path;
import java.util.*;

public class InverseIndexing {
    private final Set<String> stopWords;
    private TernaryNode root;

    public InverseIndexing() {
        root = null;
        stopWords = new HashSet<>();
    }

    public void addDocument(String parentPath, String documentName) {
        FileReader
                .readFile(parentPath, documentName, FileReader.TYPE.WORDS)
                .forEach(word -> {
                    if (!stopWords.contains(word)) // Check if the word is a stop word
                        root = insert(root, word.toCharArray(), 0, Path.of(parentPath, documentName).toString()); // TODO check if any other option
                });
    }

    private TernaryNode insert(TernaryNode node, char[] word, int index, String documentName) {
        char c = word[index];

        if (node == null) {
            node = new TernaryNode(c);
        }

        if (c < node.character) {
            node.left = insert(node.left, word, index, documentName);
        } else if (c > node.character) {
            node.right = insert(node.right, word, index, documentName);
        } else {
            if (index < word.length - 1) {
                node.mid = insert(node.mid, word, index + 1, documentName);
            } else {
                node.documents.add(documentName);
            }
        }

        return node;
    }

    public Set<String> search(String query) {
        String[] queryWords = query.split("\\s+");
        Set<String> result = null;

        for (String queryWord : queryWords) {
            queryWord = queryWord.toLowerCase();
            if (root != null) {
                TernaryNode node = search(root, queryWord.toCharArray(), 0);
                if (node != null) {
                    if (result == null) {
                        // Initialize intersection with the first set of documents
                        result = new HashSet<>(node.documents);
                    } else {
                        // keep all the document that are on the passed Set
                        result.retainAll(node.documents);
                    }
                }
            }
        }

        return result != null ? result : Collections.emptySet();
    }

    private TernaryNode search(TernaryNode node, char[] word, int index) {
        if (node == null) {
            return null;
        }

        char c = word[index];

        if (c < node.character) {
            return search(node.left, word, index);
        } else if (c > node.character) {
            return search(node.right, word, index);
        } else {
            if (index < word.length - 1) {
                return search(node.mid, word, index + 1);
            } else {
                return node;
            }
        }
    }

    // Set to store stop words
    public void addStopWords(String... words) {
        stopWords.addAll(Arrays.asList(words));
    }
}
