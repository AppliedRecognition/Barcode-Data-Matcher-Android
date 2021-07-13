# Barcode Data Matcher

Match data collected from the front of ID cards against the barcode on the back

## Installation

1. Open your Android Studio project's **build.gradle** file and add the following entry:

    ```groovy
    allprojects {
        repositories {
            // ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
2. Open your module's **build.gradle** file and add the following dependency:

    ```groovy
    dependencies {
        implementation 'com.github.AppliedRecognition:Barcode-Data-Matcher-Android:v1.0.0'
    }
    ```
3. Sync your Android project.

## Usage

1. Create an instance of `DocumentFrontPageData`, filling in the values for first name, last name, document number, etc.
2. Get the raw PDF417 barcode data from a barcode scanner.
3. Execute the `matchBarcode()` method on the `DocumentFrontPageData` instance to get a match score.
4. The score ranges from 0 (not matching at all) to 1 (all elements from the front page match the barcode).

### Example

```java
DocumentFrontPageData frontPage = new DocumentFrontPageData("MICHAEL", "SAMPLE", "2300 WEST BROAD STREET\nRICHMOND, VA\n232690000", "2008/06/06", "1986/06/06", "2013/12/10", "T64235789");
byte[] barcodeData; // Obtain from a PDF417 barcode scanner
try {
  float score = frontPage.matchBarcode(barcodeData);
  // 0 = Nothing on the front page matches the barcode
  // 0.5 = Half of the information on the front page matches the barcode
  // 1 = Everything on the front page matches the barcode
} catch (Exception e) {
  // Matching failed
}
```
