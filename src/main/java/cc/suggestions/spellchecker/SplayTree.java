package cc.suggestions.spellchecker;

import java.util.ArrayList;
import java.util.List;


public class SplayTree {
    Node root;

    // Right Rotate
    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Left Rotate
    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Splay operation - bringing x to root
    private Node splay(Node root, String key) {
        // Base cases
        if (root == null || root.key.equals(key))
            return root;

        // Key lies in left subtree
        if (key.compareTo(root.key) < 0) {
            // Key is not in tree, so we're done
            if (root.left == null) return root;

            // Zig-Zig (Left Left)
            if (key.compareTo(root.left.key) < 0) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            }
            // Zig-Zag (Left Right)
            else if (key.compareTo(root.left.key) > 0) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }

            // Second rotation for Zig-Zig or one rotation for Zig-Zag
            return (root.left == null) ? root : rightRotate(root);
        } else { // Key lies in right subtree
            // Key is not in tree, so we're done
            if (root.right == null) return root;

            // Zag-Zig (Right Left)
            if (key.compareTo(root.right.key) < 0) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            }
            // Zag-Zag (Right Right)
            else if (key.compareTo(root.right.key) > 0) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }

            // Second rotation for Zag-Zag or one rotation for Zag-Zig
            return (root.right == null) ? root : leftRotate(root);
        }
    }

    // Insert operation - inserts and splays to root
    public void insert(String key) {
        root = insert(root, key);
    }

    private Node insert(Node root, String key) {
        // Simple Binary Search Tree insert
        if (root == null) return new Node(key);

        // Splay
        root = splay(root, key);

        // Insert new key
        if (key.compareTo(root.key) < 0) {
            Node temp = new Node(key);
            temp.right = root;
            temp.left = root.left;
            root.left = null;
            root = temp;
        } else if (key.compareTo(root.key) > 0) {
            Node temp = new Node(key);
            temp.left = root;
            temp.right = root.right;
            root.right = null;
            root = temp;
        } else // it's a duplicate, do not insert it
            return root;

        return root;
    }

    // NORMAL SPLAY TREE

    // Checking if a word exists in the tree
    public boolean isWordInDictionary(String word) {
        root = splay(root, word);
        return root != null && root.key.equals(word);
    }

    // Method to find resembling words
    public List<String> findResemblingWords(String word) {
        List<String> resemblingWords = new ArrayList<>();
        findResemblingWordsRecursive(root, word, resemblingWords);
        return resemblingWords;
    }


    private static final int MAX_DISTANCE = 2;  // Maximum allowed Distance for suggestions

    // Recursive method to traverse the tree and find words starting with the given prefix
    private void findResemblingWordsRecursive(Node node, String word, List<String> words) {
        // Base case
        if (node == null) {
            return;
        }

//        System.out.printf("RU %s <-> %s :: %d\n", node.key, word, calulateEditDistance(node.key, word));
//        System.out.printf("RA %s <-> %s :: %d\n", node.key, word, getDistance(node.key, word));

        // If the Distance between the node's key and word is below the threshold, add it to suggestions
        if (calulateEditDistance(node.key, word) <= MAX_DISTANCE) {
            words.add(node.key);
        }

        // Recur on the left and right subtree
        findResemblingWordsRecursive(node.left, word, words);
        findResemblingWordsRecursive(node.right, word, words);
    }

    // method to calculate edit distance
    public int calulateEditDistance(String x, String y) {
        int m = x.length();
        int n = y.length();

        int[][] dp = new int[m + 1][n + 1];

        //length of X
        // correct version from slide
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i;
        }
//        System.out.println(dp);

        //length of Y
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j;
        }
//        System.out.println(dp);


        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = findMin(
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1,
                        dp[i - 1][j - 1] + 1 - delta(x.charAt(i - 1), y.charAt(j - 1))
                );
            }
        }

        return dp[m][n];
    }

    // what is the delta of replacing Xi with Yj
    private int delta(char x, char y) {
        return x == y ? 1 : 0;
    }

    // Method to find the minimum of three numbers
    public int findMin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
