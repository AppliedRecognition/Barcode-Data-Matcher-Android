package com.appliedrec.barcodedatamatcher;

import java.util.HashMap;

public class DamerauLevenshtein {

    public static StringDistance distanceBetweenStrings(String s1, String s2) throws Exception {
        if (s1.equals(s2)) {
            return new StringDistance(0, 1);
        }
        int inf = s1.length() + s2.length();
        HashMap<Character,Integer> distanceMatrix = new HashMap<>();
        for (int i=0; i<s1.length(); i++) {
            distanceMatrix.put(s1.charAt(i), 0);
        }
        for (int i=0; i<s2.length(); i++) {
            distanceMatrix.put(s2.charAt(i), 0);
        }
        int[][] h = new int[s1.length()+2][s2.length()+2];
        for (int i=0; i<=s1.length(); i++) {
            h[i+1][0] = inf;
            h[i+1][1] = i;
        }
        for (int i=0; i<=s2.length(); i++) {
            h[0][i+1] = inf;
            h[1][i+1] = i;
        }
        for (int i=1; i<=s1.length(); i++) {
            int db = 0;
            for (int j=1; j<=s2.length(); j++) {
                int i1 = distanceMatrix.get(s2.charAt(j-1));
                int j1 = db;
                int cost = 1;
                if (s1.charAt(i-1) == s2.charAt(j-1)) {
                    cost = 0;
                    db = j;
                }
                int[] vals = new int[]{
                        h[i][j]+cost,
                        h[i+1][j]+1,
                        h[i][j+1]+1,
                        h[i1][j1]+(i-i1-1)+1+(j-j1-1)
                };
                int minVal = Integer.MAX_VALUE;
                for (int val : vals) {
                    if (val < minVal) {
                        minVal = val;
                    }
                }
                h[i+1][j+1] = minVal;
            }
            distanceMatrix.put(s1.charAt(i-1), i);
        }
        int distance = h[s1.length()+1][s2.length()+1];
        float similarity = 1 - (float)distance / (float)Math.max(s1.length(), s2.length());
        return new StringDistance(distance, similarity);
    }
}
