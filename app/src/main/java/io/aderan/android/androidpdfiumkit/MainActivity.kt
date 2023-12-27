package io.aderan.android.androidpdfiumkit

import android.content.Context
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.shockwave.pdfium.PdfiumCore
import io.aderan.android.androidpdfiumkit.theme.AndroidPdfiumKitTheme
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPdfiumKitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    TextButton(onClick = { runTestPdf(context) }) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

private fun runTestPdf(context: Context) {
    Thread {
        val file = FileUtil.fileFromAsset(context, "ocr.pdf")

        val core = PdfiumCore(context)
        val document =
            core.newDocument(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
        val pageCount = core.getPageCount(document)

        val path = FileUtil.getInternalFilePath(context, UUID.randomUUID().toString() + ".txt")
        println("PdfiumCore test $pageCount")
        println("PdfiumCore test $path")

        val startTime = System.currentTimeMillis()
        core.openPage(document, 0, pageCount - 1)
        for (i in 0 until pageCount) {
            FileUtil.writeStringToFile(path, "--- Page $i ---\n")
            FileUtil.writeStringToFile(path, core.getPageText2(document, i))
        }
        val endTime = System.currentTimeMillis()
        println("PdfiumCore test cost time: ${endTime - startTime}ms")
        core.closeDocument(document)
    }.start()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidPdfiumKitTheme {
        Greeting("Android")
    }
}