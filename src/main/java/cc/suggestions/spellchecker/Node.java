package cc.suggestions.spellchecker;

public class Node {
    String key;
    Node left, right;

    public Node(String item) {
        key = item;
        left = right = null;
    }
}
