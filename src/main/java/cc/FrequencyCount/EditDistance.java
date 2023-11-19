package cc.FrequencyCount;

public class EditDistance {
    private static int getDistance(String s1, String s2) {
        // Levenshtein distance algorithm to calculate the distance between to strings

        // Get the lengths of the query and productFeature strings.
        int len1 = s1.length();
        int len2 = s2.length();

        // Create a 2D array to store edit distances according to the lengths of the query and productFeature strings
        int[][] distanceArray = new int[len1 + 1][len2 + 1];

        // Initialize the dynamic programming table.
        for (int n = 0; n <= len1; n++) {
            for (int m = 0; m <= len2; m++) {
                if (n == 0) {
                    // Insert all characters from productFeature string if query string is empty
                    distanceArray[n][m] = m;
                } else if (m == 0) {
                    // Insert all characters from query string at current index if the productFeature string is empty
                    distanceArray[n][m] = n;
                } else if (s1.charAt(n - 1) == s2.charAt(m - 1)) {
                    // If the current characters in query and productFeature same, the edit distance is the same as the characters from previous index
                    distanceArray[n][m] = distanceArray[n - 1][m - 1];
                } else {
                    // If the characters don't match, calculate the insertion, deletion and substitution costs
                    int insertCost = distanceArray[n][m - 1] + 1;
                    int deleteCost = distanceArray[n - 1][m] + 1;
                    int replaceCost = distanceArray[n - 1][m - 1] + 1;

                    // Calculate the minimum of the above costs
                    int minCost = Math.min(replaceCost, Math.min(insertCost, deleteCost));

                    // Insert the minimum cost at the current index
                    distanceArray[n][m] = minCost;
                }
            }
        }

        // Calculate the edit distance as a similarity score between 0 and 1.
        return distanceArray[len1][len2];

    }

    public static double getSimilarity (String a, String b) {
        int editDistance = getDistance(a, b);

        // Get the greater length of both strings
        int maxLength = Math.max(a.length(), b.length());
        double similarity = 1.0 - (double) editDistance / maxLength;

        // Return a non-negative similarity score
        return Math.max(0.0, similarity);
    }



}
