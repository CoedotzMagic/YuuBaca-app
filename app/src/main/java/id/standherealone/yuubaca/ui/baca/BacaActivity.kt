package id.standherealone.yuubaca.ui.baca

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mindev.mindev_pdfviewer.MindevPDFViewer
import com.mindev.mindev_pdfviewer.PdfScope
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.databinding.ActivityBacaBinding
import androidx.core.content.edit
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        // first run for attention
        val firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrunbuku", true)
        if (firstrun) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.title_baca_firstrun)
                .setMessage(R.string.isi_baca_firstrun)
                .setCancelable(false)
                .setPositiveButton("OK"
                ) { _: DialogInterface?, _: Int -> }
                .show()
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .edit {
                putBoolean("firstrunbuku", false)
            }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    private val statusListener = object : MindevPDFViewer.MindevViewerStatusListener {
        override fun onStartDownload() {
            binding.tvPages.text = "memuat buku"
        }

        override fun onPageChanged(position: Int, total: Int) {
            binding.tvPages.text = "${position + 1} / $total"
        }

        override fun onProgressDownload(currentStatus: Int) {
            binding.tvPages.text = "memuat buku : $currentStatus %"
        }

        override fun onSuccessDownLoad(path: String) {
            binding.pdf.fileInit(path)
            binding.pbLoading.visibility = View.GONE
        }

        override fun onFail(error: Throwable) {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Tidak Dapat Memuat Buku, Silahkan coba lagi nanti dan pastikan terhubung koneksi internet!",
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }

        override fun unsupportedDevice() {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Tidak Dapat Memuat Buku, Karena Perangkat Tidak Didukung!",
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }
    }

    override fun onDestroy() {
        binding.pdf.pdfRendererCore?.clear()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}