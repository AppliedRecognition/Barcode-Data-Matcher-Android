package com.appliedrec.barcodedatamatcher;

import androidx.collection.ArraySet;

import com.appliedrec.aamvabarcodeparser.AAMVABarcodeParser;
import com.appliedrec.aamvabarcodeparser.DocumentData;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MultiMatcher implements BarcodeMatching {

    private ArraySet<BarcodeMatching> matchers = new ArraySet<>();
    private AAMVABarcodeParser parser = new AAMVABarcodeParser();

    public MultiMatcher() {
    }

    public AAMVABarcodeParser getParser() {
        return parser;
    }

    public void setParser(AAMVABarcodeParser parser) {
        this.parser = parser;
    }

    public MultiMatcher(BarcodeMatching[] matchers) {
        this.matchers.addAll(Arrays.asList(matchers));
    }

    public MultiMatcher(Set<BarcodeMatching> matchers) {
        this.matchers.addAll(matchers);
    }

    public MultiMatcher(List<BarcodeMatching> matchers) {
        this.matchers.addAll(matchers);
    }

    public BarcodeMatching[] getMatchers() {
        BarcodeMatching[] matchersArray = new BarcodeMatching[matchers.size()];
        matchers.toArray(matchersArray);
        return matchersArray;
    }

    public void addMatcher(BaseDocumentMatcher matcher) {
        this.matchers.add(matcher);
    }

    public void removeMatcher(BaseDocumentMatcher matcher) {
        this.matchers.remove(matcher);
    }

    @Override
    public float matchBarcode(byte[] data) throws Exception {
        if (matchers.isEmpty()) {
            return 0;
        }
        DocumentData documentData = getParser().parseData(data);
        float score = 0;
        for (BarcodeMatching matcher : matchers) {
            if (matcher instanceof BaseDocumentMatcher) {
                score += ((BaseDocumentMatcher)matcher).matchDocumentData(documentData);
            } else {
                score += matcher.matchBarcode(data);
            }
        }
        return score / matchers.size();
    }
}
