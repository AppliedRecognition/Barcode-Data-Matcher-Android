package com.appliedrec.barcodedatamatcher;

import androidx.core.util.Pair;

import com.appliedrec.aamvabarcodeparser.DocumentData;

import java.util.ArrayList;

public class NamedDocumentMatcher extends BaseDocumentMatcher {

    public final String firstName;
    public final String lastName;
    public final String fullName;

    public NamedDocumentMatcher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName+" "+lastName;
    }

    public NamedDocumentMatcher(String fullName) {
        this.firstName = null;
        this.lastName = null;
        this.fullName = fullName;
    }

    @Override
    public float matchDocumentData(DocumentData documentData) throws Exception {
        ArrayList<Pair<String,String>> stringPairs = new ArrayList<>();
        if (documentData.getFirstName() != null && firstName != null) {
            stringPairs.add(new Pair<>(documentData.getFirstName(), firstName));
        }
        if (documentData.getLastName() != null && lastName != null) {
            stringPairs.add(new Pair<>(documentData.getLastName(), lastName));
        }
        if (firstName == null && lastName == null && fullName != null && documentData.getFirstName() != null && documentData.getLastName() != null) {
            stringPairs.add(new Pair<>(documentData.getFirstName()+" "+documentData.getLastName(), fullName));
        }
        if (stringPairs.isEmpty()) {
            return 0;
        }
        float stringScore = 0;
        for (Pair<String, String> pair : stringPairs) {
            stringScore += StringMatcher.match(pair.first.replaceAll("\\W+", " "), pair.second.replaceAll("\\W+", " "));
        }
        return stringScore / stringPairs.size();
    }
}
