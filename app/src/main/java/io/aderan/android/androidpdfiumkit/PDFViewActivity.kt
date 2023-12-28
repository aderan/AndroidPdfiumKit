package io.aderan.android.androidpdfiumkit

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.shockwave.pdfium.PdfDocument
import io.aderan.android.androidpdfiumkit.theme.AndroidPdfiumKitTheme

class PDFViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPdfiumKitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PDFViewScreen()
                }
            }
        }
    }

    companion object {
        val TAG = PDFViewActivity::class.java.simpleName
        const val SAMPLE_FILE = "sample.pdf"
    }
}

@Composable
fun PDFViewScreen() {
    var pageNumber by remember { mutableStateOf(0) }
    var pageCount by remember { mutableStateOf(0) }
    var pdfFileName by remember { mutableStateOf<String?>(null) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var title by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        uri = it
    }

    Box {
        PDFViewer(
            modifier = Modifier.fillMaxSize(),
            uri = uri,
            onPageChange = { page, pageCount ->
                pageNumber = page
            },
            onLoadCompleted = { pdf, pageCount ->
                loadComplete(pdf, pageCount)
            },
            onError = { page, throwable ->
                errorMessage = "Cannot load page $page: ${throwable.localizedMessage}"
            }
        )

        OutlinedButton(
            onClick = { launcher.launch(listOf("application/pdf").toTypedArray()) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Pick PDF")
        }
    }
}

@Composable
fun PDFViewer(
    modifier: Modifier,
    uri: Uri?,
    onPageChange: (Int, Int) -> Unit,
    onLoadCompleted: (pdfView: PDFView, Int) -> Unit,
    onError: (Int, Throwable) -> Unit
) {
    val context = LocalContext.current
    val pdfView = remember(context) { PDFView(context, null) }

    AndroidView(
        factory = { context ->
            pdfView
        },
        modifier = modifier,
        update = { view ->
            displayPdf(view, uri, onPageChange, { onLoadCompleted(view, it) }, onError)
        }
    )
}

fun displayPdf(
    pdfView: PDFView,
    uri: Uri?,
    onPageChange: (Int, Int) -> Unit,
    onLoadCompleted: (Int) -> Unit,
    onError: (Int, Throwable) -> Unit
) {
    pdfView.setBackgroundColor(Color.LTGRAY)
    if (uri != null) {
        displayFromUri(pdfView, uri, onPageChange, onLoadCompleted, onError)
    } else {
        displayFromAsset(
            pdfView,
            PDFViewActivity.SAMPLE_FILE,
            onPageChange,
            onLoadCompleted,
            onError
        )
    }
}

@Suppress("SameParameterValue")
private fun displayFromAsset(
    pdfView: PDFView,
    assetFileName: String,
    onPageChange: (Int, Int) -> Unit,
    onLoadCompleted: (Int) -> Unit,
    onError: (Int, Throwable) -> Unit
) {
    pdfView.fromAsset(assetFileName)
        // .defaultPage(pageNumber)
        .onPageChange(onPageChange)
        .enableAnnotationRendering(true)
        .onLoad(onLoadCompleted)
        .scrollHandle(DefaultScrollHandle(pdfView.context))
        .spacing(10) // in dp
        .onPageError(onError)
        .pageFitPolicy(FitPolicy.BOTH)
        .load()
}

private fun displayFromUri(
    pdfView: PDFView,
    uri: Uri,
    onPageChange: (Int, Int) -> Unit,
    onLoadCompleted: (Int) -> Unit,
    onError: (Int, Throwable) -> Unit
) {
    pdfView.fromUri(uri)
        //.defaultPage(pageNumber)
        .onPageChange(onPageChange)
        .enableAnnotationRendering(true)
        .onLoad(onLoadCompleted)
        .scrollHandle(DefaultScrollHandle(pdfView.context))
        .spacing(10) // in dp
        .onPageError(onError)
        .load()
}


fun loadComplete(pdfView: PDFView, nbPages: Int) {
    val meta = pdfView.documentMeta
    Log.e(PDFViewActivity.TAG, "title = " + meta.title)
    Log.e(PDFViewActivity.TAG, "author = " + meta.author)
    Log.e(PDFViewActivity.TAG, "subject = " + meta.subject)
    Log.e(PDFViewActivity.TAG, "keywords = " + meta.keywords)
    Log.e(PDFViewActivity.TAG, "creator = " + meta.creator)
    Log.e(PDFViewActivity.TAG, "producer = " + meta.producer)
    Log.e(PDFViewActivity.TAG, "creationDate = " + meta.creationDate)
    Log.e(PDFViewActivity.TAG, "modDate = " + meta.modDate)
    printBookmarksTree(pdfView.tableOfContents, "-")
}

private fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
    for (b in tree) {
        Log.e(PDFViewActivity.TAG, String.format("%s %s, p %d", sep, b.title, b.pageIdx))
        if (b.hasChildren()) {
            printBookmarksTree(b.children, "$sep-")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PDFViewActivityPreview() {
    AndroidPdfiumKitTheme {
        PDFViewScreen()
    }
}