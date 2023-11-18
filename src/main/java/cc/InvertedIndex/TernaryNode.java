package cc.InvertedIndex;

import java.util.HashSet;
import java.util.Set;

public class TernaryNode {
    char character;
    TernaryNode left;
    TernaryNode mid;
    TernaryNode right;
    Set<String> documents;

    TernaryNode(char c) {
        character = c;
        documents = new HashSet<>();
    }
}
