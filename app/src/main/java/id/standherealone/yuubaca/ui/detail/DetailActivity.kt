package id.standherealone.yuubaca.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.standherealone.yuubaca.databinding.ActivityDetailBinding
import id.standherealone.yuubaca.ui.baca.BacaActivity
import androidx.core.net.toUri

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //GET INTENT
        val i = this.intent

        //RECEIVE DATA
        val images = i.extras!!.getString("images")
        val name = i.extras!!.getString("title")
        val author = i.extras!!.getString("author")
        val sipnosis = i.extras!!.getString("sipnosis")
        val file = i.extras!!.getString("file")

        // Set title bar
        title = name

        //BIND DATA
        binding.thumbnail.setImageURI(images?.toUri())
        binding.title.text = name
        binding.author.text = author
        binding.descbook.text = sipnosis

        Picasso.get()
            .load(images)
            .resize(600, 900)
            .into(binding.thumbnail)

        binding.buttonBaca.setOnClickListener{
            val intent = Intent(this, BacaActivity::class.java)
            intent.putExtra("file", file)
            intent.putExtra("title", name)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}