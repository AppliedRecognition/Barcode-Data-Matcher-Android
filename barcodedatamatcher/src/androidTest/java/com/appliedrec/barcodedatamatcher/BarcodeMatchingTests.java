package com.appliedrec.barcodedatamatcher;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BarcodeMatchingTests {

    @Test
    public void testStringDistance() throws Exception {
        assertEquals(3, DamerauLevenshtein.distanceBetweenStrings("one", "two").steps);
    }

    @Test
    public void testIIN() throws Exception {
        HashMap<String,String> iins = new HashMap<>();
        iins.put("1", "636028");
        iins.put("2", "636000");
        iins.put("3", "636015");
        iins.put("4", "636012");
        Iterator<Map.Entry<String,String>> it = iins.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> entry = it.next();
            byte[] data = getBarcodeData(entry.getKey());
            String iin = AAMVABarcodeHelper.getInstance().getIIN(data);
            assertEquals(entry.getValue(), iin);
        }
    }

    @Test
    public void testAAMVAVersion() throws Exception {
        HashMap<String,Integer> versions = new HashMap<>();
        versions.put("1", null);
        versions.put("2", 8);
        versions.put("3", 3);
        versions.put("4", 3);
        Iterator<Map.Entry<String,Integer>> it = versions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Integer> entry = it.next();
            byte[] data = getBarcodeData(entry.getKey());
            Integer version = AAMVABarcodeHelper.getInstance().getAAMVASpecVersion(data);
            if (entry.getValue() == null) {
                assertNull(version);
            } else {
                assertEquals(entry.getValue(), version);
            }
        }
    }

    @Test
    public void testONDLMatching() throws Exception {
        DocumentFrontPageData frontPage = createFrontPage("ABDULAH,M", "ABBOTT", "5619 LAKEHILL CRES\nBURLINGTON, ON\nL7N 1B9", "G0463-64356-00901", "1960/09/01", "2020/09/01", "2015/08/28");
        float score = match("4", frontPage);
        assertEquals(1, score, 0);
    }

    @Test
    public void testVADLMatching() throws Exception {
        DocumentFrontPageData frontPage = createFrontPage("MICHAEL", "SAMPLE", "2300 WEST BROAD STREET\nRICHMOND, VA\n232690000", "T64235789", "1986/06/06", "2013/12/10", "2008/06/06");
        float score = match("2", frontPage);
        assertEquals(1, score, 0);
    }

    @Test
    public void testTXDLMatching() throws Exception {
        DocumentFrontPageData frontPage = createFrontPage("STAN CONSTANTINE", "OGRADY", "4367 LEWISHAME AVE\nHIGHLAND PARK, TX\n752050000", "36114825", "1964/05/09", "2019/05/09", "2015/01/23");
        float score = match("3", frontPage);
        assertEquals(1, score, 0);
    }

    @Test
    public void testBCDLMatching() throws Exception {
        DocumentFrontPageData frontPage = createFrontPage("ROBERTO N", "BRONSTON", "942 WILDHOOD LANE\nWEST VANCOUVER BC  V7S 2H7", "1070412", "1927/04/01", "2020/04/31", null);
        float score = match("1", frontPage);
        assertEquals(1, score, 0);
    }

    private float match(String barcodeNumber, DocumentFrontPageData frontPage) throws Exception {
        byte[] data = getBarcodeData(barcodeNumber);
        return frontPage.matchBarcode(data);
    }

    private DocumentFrontPageData createFrontPage(String firstName, String lastName, String address, String documentNumber, String dateOfBirth, String dateOfExpiry, String dateOfIssue) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return new DocumentFrontPageData(firstName, lastName, address, dateOfIssue != null ? dateFormat.parse(dateOfIssue) : null, dateOfBirth != null ? dateFormat.parse(dateOfBirth) : null, dateOfExpiry != null ? dateFormat.parse(dateOfExpiry) : null, documentNumber);
    }

    private byte[] getBarcodeData(String name) throws Exception {
        try (InputStream inputStream = InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("barcode_data/"+name+".txt")) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[256];
                int read;
                while ((read = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    outputStream.write(buffer, 0, read);
                }
                return outputStream.toByteArray();
            }
        }
    }
}