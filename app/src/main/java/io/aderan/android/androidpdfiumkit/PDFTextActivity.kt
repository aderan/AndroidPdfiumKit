package io.aderan.android.androidpdfiumkit

import android.content.Context
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.MODE_READ_ONLY
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.shockwave.pdfium.PdfiumCore
import io.aderan.android.androidpdfiumkit.theme.AndroidPdfiumKitTheme
import java.util.UUID

class PDFTextActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPdfiumKitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PDFTextScreen()
                }
            }
        }
    }
}

@Composable
fun PDFTextScreen() {
    val context = LocalContext.current

    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = { extractTextPdf(context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(text = "Extract Text")
        }
    }
}

private fun extractTextPdf(context: Context) {
    Thread {
        val file = Utils.fileFromAsset(context, "sample.pdf")
        val core = PdfiumCore(context)

        val document = core.newDocument(ParcelFileDescriptor.open(file, MODE_READ_ONLY))
        val pageCount = core.getPageCount(document)

        val path = Utils.getInternalFilePath(context, "${UUID.randomUUID()}.txt")
        println("PdfiumCore test $pageCount")
        println("PdfiumCore test $path")

        val startTime = System.currentTimeMillis()
        // TODO toIndex is exclusive
        core.openPage(document, 0, pageCount - 1)

        for (i in 0 until pageCount) {
            Utils.writeStringToFile(path, "--- Page $i ---\n")
            Utils.writeStringToFile(path, "${core.getPageText(document, i)}\n")
        }

        val endTime = System.currentTimeMillis()
        println("PdfiumCore test cost time: ${endTime - startTime}ms")
        core.closeDocument(document)
    }.start()
}