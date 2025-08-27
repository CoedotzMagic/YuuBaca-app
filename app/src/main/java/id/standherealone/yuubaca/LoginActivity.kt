@file:Suppress("DEPRECATION")

package id.standherealone.yuubaca

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import id.standherealone.yuubaca.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential: SignInCredential =
                oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Autentikasi gagal.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "ID Token tidak ditemukan.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal login: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fullscreen logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize One Tap
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false) // false = show all Google accounts
                    .build()
            )
            .setAutoSelectEnabled(true) // Auto sign-in if user already authorized
            .build()

        binding.signInButton.setOnClickListener {
            Toast.makeText(this, "Login ke YuuBaca", Toast.LENGTH_SHORT).show()
            startOneTapSignIn()
        }

        binding.kebijakan.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://coedotzmagic.com/privacy_policy".toUri()
                )
            )
        }
    }

    private fun startOneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    signInLauncher.launch(intentSenderRequest)
                } catch (e: Exception) {
                    Toast.makeText(this, "Gagal memulai sign-in: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(this, "One Tap tidak tersedia: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}