package com.appliedrec.barcodedatamatcher;

import com.appliedrec.aamvabarcodeparser.DocumentData;

import java.util.Locale;

public class NumberedDocumentMatcher extends BaseDocumentMatcher {

    public final String documentNumber;

    public NumberedDocumentMatcher(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public float matchDocumentData(DocumentData documentData) throws Exception {
        if (documentData.getDocumentNumber() == null) {
            return 0;
        }
        return StringMatcher.match(documentNumber.replaceAll("\\W+", " "), documentData.getDocumentNumber().replaceAll("\\W+", " "));
    }
}
