package id.standherealone.yuubaca.ui.detail

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.standherealone.yuubaca.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GET INTENT
        val i = this.intent

        //RECEIVE DATA
        val images = i.extras!!.getString("images")
        val name = i.extras!!.getString("title")
        val author = i.extras!!.getString("author")
        val sipnosis = i.extras!!.getString("sipnosis")
        val file = i.extras!!.getString("file")

        //BIND DATA
        binding.thumbnail.setImageURI(Uri.parse(images))
        binding.title.text = name
        binding.author.text = author
        binding.descbook.text = sipnosis

        Picasso.get()
            .load(images) //.resize(1200, 800)                     // optional
            .into(binding.thumbnail)

        binding.buttonBaca.setOnClickListener{
            Toast.makeText(this, file, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        this.finish()
    }
}