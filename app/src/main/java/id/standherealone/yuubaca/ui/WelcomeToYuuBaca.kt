package id.standherealone.yuubaca.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import id.standherealone.yuubaca.MainActivity
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.databinding.ActivityWelcomeBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class WelcomeToYuuBaca : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    //This Welcome page in YuuBaca & grant premissions before use this app

    val PERMISSION_CALLBACK_CONSTANT = 100
    val REQUEST_PERMISSION_SETTING = 101
    val permissionsRequired = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.SET_DEBUG_APP,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.SET_WALLPAPER,
        Manifest.permission.VIBRATE
    )

    var btnCheckPermissions: TextView? = null
    var permissionStatus: SharedPreferences? = null
    var sentToSettings = false
    val VIDEO_NAME = "welcomevid_mobile.mp4"
    private var mVideoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportActionBar?.hide();
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kebijakan.setOnClickListener {
            this.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://luckytruedev.com/privacy_policy.html")
                )
            )
        }

        mVideoView = binding.videoView

        var videoFile = getFileStreamPath(VIDEO_NAME)
        if (!videoFile.exists()) {
            videoFile = copyVideoFile()
        }
        playVideo(videoFile)

        val selamatdatang: Boolean =
            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getBoolean(
                "selamatdatang",
                true
            )
        if (selamatdatang) {
            alertOneButton()
        }
    }

    fun alertOneButton() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE)
        btnCheckPermissions = binding.mulai
        btnCheckPermissions!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[0]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[1]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[2]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[3]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[4]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[5]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[6]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[7]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[8]
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[9]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[1]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[2]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[3]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[4]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[5]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[6]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[7]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[8]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this@WelcomeToYuuBaca,
                        permissionsRequired[9]
                    )
                ) {
                    //Tampilkan Informasi tentang mengapa Anda perlu izin
                    val builder =
                        AlertDialog.Builder(this@WelcomeToYuuBaca)
                    builder.setTitle("Butuh Beberapa Izin")
                    builder.setMessage("Aplikasi ini Membutuhkan Izin.")
                    builder.setPositiveButton(
                        "Izin"
                    ) { dialog, which ->
                        dialog.cancel()
                        ActivityCompat.requestPermissions(
                            this@WelcomeToYuuBaca,
                            permissionsRequired,
                            PERMISSION_CALLBACK_CONSTANT
                        )
                    }
                    builder.setNegativeButton(
                        "Batal"
                    ) { dialog, which -> dialog.cancel() }
                    builder.show()
                } else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                    val builder =
                        AlertDialog.Builder(this@WelcomeToYuuBaca)
                    builder.setTitle("Butuh Beberapa Izin")
                    builder.setMessage("Aplikasi ini Membutuhkan Izin.")
                    builder.setPositiveButton(
                        "Izin"
                    ) { dialog, which ->
                        dialog.cancel()
                        sentToSettings = true
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        @Suppress("DEPRECATION")
                        startActivityForResult(
                            intent,
                            REQUEST_PERMISSION_SETTING
                        )
                    }
                    builder.setNegativeButton(
                        "Batal"
                    ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this@WelcomeToYuuBaca,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }
                val editor = permissionStatus!!.edit()
                editor.putBoolean(permissionsRequired[0], true)
                editor.commit()
            } else {
                proceedAfterPermission()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }
            if (allgranted) {
                proceedAfterPermission()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[0]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[1]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[2]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[3]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[4]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[5]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[6]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[7]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[8]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[9]
                )
            ) {
                val builder = AlertDialog.Builder(this@WelcomeToYuuBaca)
                builder.setTitle("Butuh Beberapa Izin")
                builder.setMessage("Aplikasi ini Membutuhkan Izin.")
                builder.setPositiveButton("Izin") { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        this@WelcomeToYuuBaca,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }
                builder.setNegativeButton(
                    "Batal"
                ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                builder.show()
            } else {
                startActivity(Intent(this@WelcomeToYuuBaca, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                proceedAfterPermission()
            }
        }
    }

    private fun proceedAfterPermission() {}

    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(
                    this@WelcomeToYuuBaca,
                    permissionsRequired[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                proceedAfterPermission()
            }
        }
    }

    // Video Layout
    private fun playVideo(videoFile: File) {
        mVideoView!!.setVideoPath(videoFile.path)
        mVideoView!!.layoutParams = RelativeLayout.LayoutParams(-1, -1)
        mVideoView!!.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    private fun copyVideoFile(): File {
        try {
            val fos =
                openFileOutput(VIDEO_NAME, MODE_PRIVATE)
            val `in` = resources.openRawResource(R.raw.welcomevid_mobilex)
            val buff = ByteArray(1024)
            var len = 0
            while (`in`.read(buff).also { len = it } != -1) {
                fos.write(buff, 0, len)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val videoFile: File = getFileStreamPath(VIDEO_NAME)
        if (!videoFile.exists()) throw RuntimeException("Tidak Dapat Memutar Video")
        return videoFile
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView!!.stopPlayback()
    }

    // Video Layout

}