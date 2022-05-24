package id.standherealone.yuubaca.ui.baca

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.databinding.ActivityBacaBinding
import id.standherealone.yuubaca.databinding.ActivityDetailBinding


class BacaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBacaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBacaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Get Intent
        val i = this.intent

        //Receive Data
        val file = i.extras!!.getString("file")
        val name = i.extras!!.getString("title")

        // title bar
        title = "Baca Buku: $name"

        if (file != null) {
            binding.pdf.initializePDFDownloader(file, statusListener)
        }
        lifecycle.addObserver(PdfScope())
    }

    private val statusListener = object : MindevPDFViewer.MindevViewerStatusListener {
        override fun onStartDownload() {
        }

        override fun onPageChanged(position: Int, total: Int) {
        }

        override fun onProgressDownload(currentStatus: Int) {
        }

        override fun onSuccessDownLoad(path: String) {
            binding.pdf.fileInit(path)
        }

        override fun onFail(error: Throwable) {
        }

        override fun unsupportedDevice() {
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        this.finish()
    }
}