package cc.spellchecker;

public class EditDistanceHelper {

    // method to calculate edit distance
    public static int calulateEditDistance(String x, String y) {
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
                        dp[i - 1][j - 1] + 1 - delta(x.charAt(i - 1), x.charAt(i - 1))
                );
            }
        }

        return dp[m][n];
    }

    // what is the delta of replacing Xi with Yj
    private static int delta(char x, char y) {
        return x == y ? 1 : 0;
    }

    // Method to find the minimum of three numbers
    public static int findMin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
