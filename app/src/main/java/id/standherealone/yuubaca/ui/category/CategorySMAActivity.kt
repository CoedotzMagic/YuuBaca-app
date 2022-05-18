package id.standherealone.yuubaca.ui.category

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.api.*
import id.standherealone.yuubaca.core.adapter.BukuAdapter
import id.standherealone.yuubaca.databinding.ActivityRecyclerviewBinding
import id.standherealone.yuubaca.model.Buku
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategorySMAActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerviewBinding
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: BukuAdapter
    private var shimmer: ShimmerFrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerview)
        shimmer = binding.shimmerHome
        recyclerAdapter = BukuAdapter(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = recyclerAdapter

        val apiInterface = ApiSMA.create().getBuku()

        //apiInterface.enqueue( Callback<List<Buku>>())
        apiInterface.enqueue(object : Callback<List<Buku>> {
            override fun onResponse(call: Call<List<Buku>>?, response: Response<List<Buku>>?) {

                if (response?.body() != null)
                    recyclerAdapter.setBukuListItems(response.body()!!)

                //shimmer
                shimmer!!.stopShimmer()
                shimmer!!.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Buku>>?, t: Throwable?) {
                val snackbar =
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.failed_load_data_api,
                        Snackbar.LENGTH_SHORT
                    )
                snackbar.show()

            }
        })
    }

    override fun onResume() {
        super.onResume()
        shimmer!!.startShimmer()
    }

    override fun onPause() {
        shimmer!!.stopShimmer()
        super.onPause()
    }

    override fun onBackPressed() {
        this.finish()
    }

}