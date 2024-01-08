# AndroidPdfiumKit

[![](https://jitpack.io/v/aderan/AndroidPdfiumKit.svg)](https://jitpack.io/#aderan/AndroidPdfiumKit)

This repository combines [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer)
and [PdfiumAndroid](https://github.com/mshockwave/PdfiumAndroid) for displaying PDFs and acquiring related information.

## Introduction

This project started to extract text from PDFs, as [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer)
and [PdfiumAndroid](https://github.com/mshockwave/PdfiumAndroid) stopped receiving updates.
Submitting a PR for these projects became impractical, so a new maintenance-focused initiative was launched.

## Usage

Download the latest JAR or depend via Jitpack

```groovy
// settings.gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// build.gradle
dependencies {
    implementation "com.github.aderan.AndroidPdfiumKit:pdfium:0.1.2"
    implementation "com.github.aderan.AndroidPdfiumKit:pdfviewer:0.1.2"
}
```

## References

[pdfium-binaries](https://github.com/bblanchon/pdfium-binaries)

[pdfium-lib](https://github.com/paulocoutinhox/pdfium-lib)

[AndroidPdfViewer](https://github.com/mhiew/AndroidPdfViewer)

[PdfiumAndroidKt](https://github.com/johngray1965/PdfiumAndroidKt)

[pdfium google group](https://groups.google.com/g/pdfium)

[Apache PDFBox](https://pdfbox.apache.org/)