package com.appliedrec.barcodedatamatcher;

import androidx.annotation.NonNull;

import com.appliedrec.aamvabarcodeparser.AAMVABarcodeParser;
import com.appliedrec.aamvabarcodeparser.DocumentData;

public abstract class BaseDocumentMatcher implements BarcodeMatching {

    private AAMVABarcodeParser parser = new AAMVABarcodeParser();

    public AAMVABarcodeParser getParser() {
        return parser;
    }

    public void setParser(@NonNull AAMVABarcodeParser parser) {
        this.parser = parser;
    }

    public abstract float matchDocumentData(DocumentData documentData) throws Exception;

    @Override
    public final float matchBarcode(byte[] data) throws Exception {
        return matchDocumentData(getParser().parseData(data));
    }
}
