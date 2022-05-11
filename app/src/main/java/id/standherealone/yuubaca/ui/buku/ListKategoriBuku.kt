package id.standherealone.yuubaca.ui.buku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.standherealone.yuubaca.databinding.ListKategoriBinding

class ListKategoriBuku : AppCompatActivity() {

    private lateinit var binding: ListKategoriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}