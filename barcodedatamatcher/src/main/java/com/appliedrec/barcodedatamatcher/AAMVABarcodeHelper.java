package com.appliedrec.barcodedatamatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appliedrec.aamvabarcodeparser.AAMVABarcodeParser;

import java.nio.charset.Charset;
import java.util.Arrays;

public class AAMVABarcodeHelper {

    private final static AAMVABarcodeHelper INSTANCE = new AAMVABarcodeHelper();

    public static AAMVABarcodeHelper getInstance() {
        return INSTANCE;
    }

    @Nullable
    public Integer getAAMVASpecVersion(@NonNull byte[] data) {
        if (data.length < 18 || data[0] != 0x40) {
            return null;
        }
        String str = new String(Arrays.copyOfRange(data, 15, 17), Charset.forName("UTF-8"));
        return Integer.parseInt(str);
    }

    @Nullable
    public String getIIN(@NonNull byte[] data) {
        if (data.length < 16) {
            return null;
        }
        if (data[0] == 0x40) {
            return new String(Arrays.copyOfRange(data, 9, 15), Charset.forName("UTF-8"));
        }
        try {
            return new AAMVABarcodeParser().parseData(data).getValueForKey("IIN");
        } catch (Exception e) {
            return null;
        }
    }
}
