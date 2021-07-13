package com.appliedrec.barcodedatamatcher;

import androidx.core.util.Pair;

import com.appliedrec.aamvabarcodeparser.AAMVABarcodeParser;
import com.appliedrec.aamvabarcodeparser.DocumentData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class DocumentFrontPageData implements BarcodeMatching {

    public final String firstName;
    public final String lastName;
    public final String address;
    public final Date dateOfIssue;
    public final Date dateOfBirth;
    public final Date dateOfExpiry;
    public final String documentNumber;
    private AAMVABarcodeParser parser;

    public DocumentFrontPageData(String firstName, String lastName, String address, Date dateOfIssue, Date dateOfBirth, Date dateOfExpiry, String documentNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dateOfIssue = dateOfIssue;
        this.dateOfBirth = dateOfBirth;
        this.dateOfExpiry = dateOfExpiry;
        this.documentNumber = documentNumber;
        this.parser = new AAMVABarcodeParser();
    }

    public AAMVABarcodeParser getParser() {
        return parser;
    }

    public void setParser(AAMVABarcodeParser parser) {
        this.parser = parser;
    }

    @Override
    public float matchBarcode(byte[] barcodeData) throws Exception {
        DocumentData data = this.getParser().parseData(barcodeData);
        ArrayList<Pair<String,String>> strings = new ArrayList<>();
        if (data.getFirstName() != null) {
            strings.add(new Pair<>(this.firstName, data.getFirstName()));
        }
        if (data.getLastName() != null) {
            strings.add(new Pair<>(this.lastName, data.getLastName()));
        }
        if (data.getDocumentNumber() != null) {
            strings.add(new Pair<>(this.documentNumber, data.getDocumentNumber()));
        }
        float stringScore = 0;
        for (Pair<String, String> pair : strings) {
            if (pair.second == null) {
                continue;
            }
            stringScore += StringMatcher.match(pair.first, pair.second);
        }
        stringScore /= strings.size();
        ArrayList<Pair<Date,String>> dateList = new ArrayList<>();
        if (data.getDateOfBirth() != null) {
            dateList.add(new Pair<>(this.dateOfBirth, data.getDateOfBirth()));
        }
        if (data.getDateOfIssue() != null) {
            dateList.add(new Pair<>(this.dateOfIssue, data.getDateOfIssue()));
        }
        if (data.getDateOfExpiry() != null) {
            dateList.add(new Pair<>(this.dateOfExpiry, data.getDateOfExpiry()));
        }
        HashMap<String,String> patternMap = new HashMap<>();
        patternMap.put("^\\d{1,2}\\s+[a-zA-Z]{3}\\s+\\d{4}$", "d MMM yyy");
        patternMap.put("^\\d{2}/\\d{2}/\\d{4}$", "dd/MM/yyyy");
        String pattern = "d MMM yyyy";
        for (Map.Entry<String,String> entry : patternMap.entrySet()) {
            if (data.getDateOfBirth() != null && data.getDateOfBirth().matches(entry.getKey())) {
                pattern = entry.getValue();
                break;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.CANADA);
        float dateScore = 0;
        for (Pair<Date, String> pair : dateList) {
            if (pair.first == null) {
                continue;
            }
            assert pair.second != null;
            Date date = dateFormat.parse(pair.second);
            if (date == null) {
                continue;
            }
            dateScore += date.compareTo(pair.first) == 0 ? 1 : 0;
        }
        dateScore /= dateList.size();
        return (stringScore + dateScore) / 2;
    }
}
