package com.appliedrec.barcodedatamatcher;

import java.util.Date;

public class DocumentFrontPageData extends MultiMatcher {

    public DocumentFrontPageData(String firstName, String lastName, String address, Date dateOfIssue, Date dateOfBirth, Date dateOfExpiry, String documentNumber) {
        if (firstName != null && lastName != null) {
            addMatcher(new NamedDocumentMatcher(firstName, lastName));
        }
        if (address != null) {
            addMatcher(new AddressedDocumentMatcher(address));
        }
        if (documentNumber != null) {
            addMatcher(new NumberedDocumentMatcher(documentNumber));
        }
        if (dateOfBirth != null && dateOfIssue != null && dateOfExpiry != null) {
            addMatcher(new DatedDocumentMatcher(dateOfIssue, dateOfBirth, dateOfExpiry));
        }
    }
}
