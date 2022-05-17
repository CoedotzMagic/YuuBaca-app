package id.standherealone.yuubaca.ui.feedback

import android.Manifest
import id.standherealone.yuubaca.core.components.SystemLog.extractLogToString
import id.standherealone.yuubaca.core.components.DeviceInfo.getAllDeviceInfo
import androidx.appcompat.app.AppCompatActivity
import id.standherealone.yuubaca.core.components.SystemLog
import android.os.Bundle
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.ui.feedback.FeedbackActivity
import id.standherealone.yuubaca.core.components.DeviceInfo
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.os.StrictMode.VmPolicy
import android.os.StrictMode
import android.os.Build
import android.content.pm.PackageManager
import android.content.Intent
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import id.standherealone.yuubaca.core.components.Utils
import id.standherealone.yuubaca.databinding.ActivityFeedbackBinding
import id.standherealone.yuubaca.databinding.ActivityMainBinding
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList

class FeedbackActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFeedbackBinding

    var LOG_TO_STRING = extractLogToString()
    private var editText: EditText? = null
    private var emailId: String? = null
    private val REQUEST_APP_SETTINGS = 321
    private val REQUEST_PERMISSIONS = 123
    private var deviceInfo: String? = null
    private var withInfo = false
    private val PICK_IMAGE_REQUEST = 125
    private var realPath: String? = null
    private var selectedImageView: ImageView? = null
    private var selectContainer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    private fun init() {
        editText = findViewById(R.id.editText)
        val info = findViewById<TextView>(R.id.info_legal)
        val selectImage = findViewById<FrameLayout>(R.id.selectImage)
        val submitSuggestion = findViewById<Button>(R.id.submitSuggestion)
        selectedImageView = findViewById(R.id.selectedImageView)
        selectContainer = findViewById(R.id.selectContainer)
        submitSuggestion.setOnClickListener(this)
        selectImage.setOnClickListener(this)
        emailId = intent.getStringExtra(KEY_EMAIL)
        withInfo = intent.getBooleanExtra(KEY_WITH_INFO, false)
        deviceInfo = getAllDeviceInfo(this, false)
        if (withInfo) {
            val infoFeedbackStart: CharSequence =
                resources.getString(R.string.info_fedback_legal_start)
            val deviceInfo =
                SpannableString(resources.getString(R.string.info_fedback_legal_system_info))
            deviceInfo.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    AlertDialog.Builder(this@FeedbackActivity)
                        .setTitle(R.string.info_fedback_legal_system_info)
                        .setMessage(this@FeedbackActivity.deviceInfo)
                        .setPositiveButton(R.string.Ok) { dialog, which -> dialog.dismiss() }
                        .show()
                }
            }, 0, deviceInfo.length, 0)
            val infoFeedbackAnd: CharSequence = resources.getString(R.string.info_fedback_legal_and)
            val systemLog =
                SpannableString(resources.getString(R.string.info_fedback_legal_log_data))
            systemLog.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    AlertDialog.Builder(this@FeedbackActivity)
                        .setTitle(R.string.info_fedback_legal_log_data)
                        .setMessage(LOG_TO_STRING)
                        .setPositiveButton(R.string.Ok) { dialog, which -> dialog.dismiss() }
                        .show()
                }
            }, 0, systemLog.length, 0)
            val infoFeedbackEnd: CharSequence =
                resources.getString(R.string.info_fedback_legal_will_be_sent, appLabel)
            val finalLegal = TextUtils.concat(
                infoFeedbackStart,
                deviceInfo,
                infoFeedbackAnd,
                systemLog,
                infoFeedbackEnd
            ) as Spanned
            info.text = finalLegal
            info.movementMethod = LinkMovementMethod.getInstance()
        } else info.visibility = View.GONE
    }

    fun selectImage() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasWriteContactsPermission =
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS
                )
                return
            } else  //already granted
                selectPicture()
        } else {
            //normal process
            selectPicture()
        }
    }

    private fun selectPicture() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        realPath = null
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        @Suppress("DEPRECATION")
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.select_picture_title)
            ), PICK_IMAGE_REQUEST
        )
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.Ok, okListener)
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                selectPicture()
            } else {
                // Permission Denied
                showMessageOKCancel(
                    java.lang.String.valueOf(R.string.permissionsgambar)
                ) { dialog: DialogInterface?, which: Int -> goToSettings() }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun goToSettings() {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        @Suppress("DEPRECATION")
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                selectPicture()
            } else {
                showMessageOKCancel(
                    java.lang.String.valueOf(R.string.permissionsgambar)
                ) { dialog: DialogInterface?, which: Int -> goToSettings() }
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            realPath = Utils.getPath(this, data.data!!)
            selectedImageView!!.setImageBitmap(
                Utils.decodeSampledBitmap(
                    realPath,
                    selectedImageView!!.width, selectedImageView!!.height
                )
            )
            selectContainer!!.visibility = View.GONE
            Toast.makeText(this, getString(R.string.click_again), Toast.LENGTH_SHORT).show()
        }
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun hasPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                permission
            )
        ) return false
        return true
    }

    fun sendEmail(body: String?) {
        val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.feedback_mail_subject, appLabel)
        )
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        val uris = ArrayList<Uri>()
        if (withInfo) {
            val deviceInfoUri =
                createFileFromString(deviceInfo, getString(R.string.file_name_device_info))
            uris.add(deviceInfoUri)
            val logUri =
                createFileFromString(LOG_TO_STRING, getString(R.string.file_name_device_log))
            uris.add(logUri)
        }
        if (realPath != null) {
            val uri = FileProvider.getUriForFile(
                this,
                applicationContext
                    .packageName + ".provider", File(realPath)
            )
            //Uri uri = Uri.parse("file://" + realPath);
            uris.add(uri)
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(
            Utils.createEmailOnlyChooserIntent(
                this,
                emailIntent,
                getString(R.string.send_feedback_two)
            )
        )
    }

    private fun createFileFromString(text: String?, name: String): Uri {
        val file = File(externalCacheDir, name)
        //create the file if it didn't exist
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        try {
            //BufferedWriter for performance, false to overrite to file flag
            val buf = BufferedWriter(FileWriter(file, false))
            buf.write(text)
            buf.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return FileProvider.getUriForFile(
            this,
            applicationContext
                .packageName + ".provider", file
        )
    }

    override fun onClick(view: View) {
        if (view.id == R.id.submitSuggestion) {
            val suggestion = editText!!.text.toString()
            if (suggestion.trim { it <= ' ' }.isNotEmpty()) {
                sendEmail(suggestion)
                finish()
            } else editText!!.error = getString(R.string.please_write)
        } else if (view.id == R.id.selectImage) selectImage()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    val appLabel: String
        get() = resources.getString(R.string.app_name)

    override fun onBackPressed() {
        finish()
    }

    companion object {
        const val KEY_WITH_INFO = "with_info"
        const val KEY_EMAIL = "email"
    }
}