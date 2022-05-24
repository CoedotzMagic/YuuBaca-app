package id.standherealone.yuubaca.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.standherealone.yuubaca.databinding.ActivityDetailBinding
import id.standherealone.yuubaca.ui.baca.BacaActivity

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
        binding.thumbnail.setImageURI(Uri.parse(images))
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
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        // disini gw harus balikin lagi datanya & pake putextra
        // biar pas balik2 datanya masih ada, karena passing data
        this.finish()
    }
}