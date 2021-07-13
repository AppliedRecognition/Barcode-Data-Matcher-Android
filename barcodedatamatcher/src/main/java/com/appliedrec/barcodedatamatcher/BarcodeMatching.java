package com.appliedrec.barcodedatamatcher;

public interface BarcodeMatching {

    float matchBarcode(byte[] data) throws Exception;
}
