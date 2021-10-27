package com.appliedrec.barcodedatamatcher;

import com.appliedrec.aamvabarcodeparser.DocumentData;

public class AddressedDocumentMatcher extends BaseDocumentMatcher {

    public final String address;

    public AddressedDocumentMatcher(String address) {
        this.address = address;
    }

    @Override
    public float matchDocumentData(DocumentData data) throws Exception {
        if (data.getAddress() == null) {
            return 0;
        }
        return StringMatcher.match(this.address.replaceAll("[\n,]", " "), data.getAddress().replaceAll("[\n,]", " "));
    }
}
