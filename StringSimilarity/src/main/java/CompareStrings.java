import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CompareStrings {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter first string: ");
        String first = scanner.nextLine();
        System.out.print("enter second string: ");
        String sec = scanner.nextLine();
        compareStrings(first, sec);
    }

    private static void compareStrings(String first, String sec) {
        float ld = computeLevenshteindist(new StringBuffer(first), new StringBuffer(sec));
        double dc = computeDiceCoefficient(first, sec);
        System.out.println("Similarity by Levenshtein dist: " + ld/(Math.max(first.length(), sec.length())));
        System.out.println("Similarity by Dice Coefficient: " + dc);
    }

    //to get the minimum of three numbers
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    //computes Levenshtein dist
    private static int computeLevenshteindist(CharSequence lhs, CharSequence rhs) {

        int[][] dist = new int[lhs.length() + 1][rhs.length() + 1];

        // for all i and j, d[i,j] will hold the Levenshtein dist between

        // source prefixes can be transformed into empty string by dropping all characters
        for (int i = 0; i <= lhs.length(); i++)
            dist[i][0] = i;

        // target prefixes can be reached from empty source prefix by inserting every character
        for (int j = 1; j <= rhs.length(); j++)
            dist[0][j] = j;

        //calculating minimum cost for deletion, insertion, substitution
        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                dist[i][j] = minimum(
                        dist[i - 1][j] + 1,
                        dist[i][j - 1] + 1,
                        dist[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));
            //summation cost for complete string
        return dist[lhs.length()][rhs.length()];
    }

    private static double computeDiceCoefficient(String s1, String s2)
    {
        // Verifying the input:
        if (s1 == null || s2 == null)
            return 0;
        //check to catch identical objects:
        if (s1.equals(s2))
            return 1;
        // avoid exception for single character searches
        if (s1.length() < 2 || s2.length() < 2)
            return 0;

        // Create the bigrams for string s1:
        final int n = s1.length()-1;
        final int[] s1Pairs = new int[n];
        for (int i = 0; i <= n; i++)
            if (i == 0)
                s1Pairs[i] = s1.charAt(i) << 16;
            else if (i == n)
                s1Pairs[i-1] |= s1.charAt(i);
            else
                s1Pairs[i] = (s1Pairs[i-1] |= s1.charAt(i)) << 16;

        // Create the bigrams for string s2:
        final int m = s2.length()-1;
        final int[] s2Pairs = new int[m];
        for (int i = 0; i <= m; i++)
            if (i == 0)
                s2Pairs[i] = s2.charAt(i) << 16;
            else if (i == m)
                s2Pairs[i-1] |= s2.charAt(i);
            else
                s2Pairs[i] = (s2Pairs[i-1] |= s2.charAt(i)) << 16;

        // Sort the bigram lists:
        Arrays.sort(s1Pairs);
        Arrays.sort(s2Pairs);

        // Count the matches:
        int matches = 0, i = 0, j = 0;
        while (i < n && j < m)
        {
            if (s1Pairs[i] == s2Pairs[j])
            {
                matches += 2;
                i++;
                j++;
            }
            else if (s1Pairs[i] < s2Pairs[j])
                i++;
            else
                j++;
        }
        return (double)matches/(n+m);
    }
}
