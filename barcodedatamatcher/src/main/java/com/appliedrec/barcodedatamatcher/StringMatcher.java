package com.appliedrec.barcodedatamatcher;

import java.text.Normalizer;

public class StringMatcher {

    public static float match(String s1, String s2) throws Exception {
        String s1Norm = Normalizer.normalize(s1, Normalizer.Form.NFD);
        String s2Norm = Normalizer.normalize(s2, Normalizer.Form.NFD);
        return DamerauLevenshtein.distanceBetweenStrings(s1Norm.toLowerCase(), s2Norm.toLowerCase()).similarity;
    }
}
