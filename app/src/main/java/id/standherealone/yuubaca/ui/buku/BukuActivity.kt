package id.standherealone.yuubaca.ui.buku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.api.ApiBuku
import id.standherealone.yuubaca.core.adapter.BukuAdapter
import id.standherealone.yuubaca.model.Buku

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlinx.android.synthetic.main.activity_recyclerview.*

class BukuActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: BukuAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerAdapter = BukuAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter


        val apiInterface = ApiBuku.create().getBuku()

        //apiInterface.enqueue( Callback<List<Buku>>())
        apiInterface.enqueue( object : Callback<List<Buku>>{
            override fun onResponse(call: Call<List<Buku>>?, response: Response<List<Buku>>?) {

                if(response?.body() != null)
                    recyclerAdapter.setBukuListItems(response.body()!!)
            }

            override fun onFailure(call: Call<List<Buku>>?, t: Throwable?) {

            }
        })
    }
}