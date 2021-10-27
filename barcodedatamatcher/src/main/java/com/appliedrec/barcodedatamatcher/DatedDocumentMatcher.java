package com.appliedrec.barcodedatamatcher;

import androidx.core.util.Pair;

import com.appliedrec.aamvabarcodeparser.DocumentData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatedDocumentMatcher extends BaseDocumentMatcher {

    public final Date dateOfIssue;
    public final Date dateOfBirth;
    public final Date dateOfExpiry;

    public DatedDocumentMatcher(Date dateOfIssue, Date dateOfBirth, Date dateOfExpiry) {
        this.dateOfIssue = dateOfIssue;
        this.dateOfBirth = dateOfBirth;
        this.dateOfExpiry = dateOfExpiry;
    }

    @Override
    public float matchDocumentData(DocumentData data) throws Exception {
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
        if (dateList.isEmpty()) {
            return 0;
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
            dateScore += dateFormat.format(date).equals(dateFormat.format(pair.first)) ? 1 : 0;
        }
        return dateScore / dateList.size();
    }
}
