package id.standherealone.yuubaca.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.api.ApiBuku
import id.standherealone.yuubaca.core.adapter.BukuAdapter
import id.standherealone.yuubaca.databinding.FragmentHomeBinding
import id.standherealone.yuubaca.model.Buku
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION", "WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
class HomeFragmentBackup : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: BukuAdapter
    private var shimmer: ShimmerFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        recyclerView = binding.recyclerview
        shimmer = binding.shimmerHome
        recyclerAdapter = BukuAdapter(requireContext())
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = recyclerAdapter


        val apiInterface = ApiBuku.create().getBuku()

        //apiInterface.enqueue( Callback<List<Buku>>())
        apiInterface.enqueue( object : Callback<List<Buku>> {
            override fun onResponse(call: Call<List<Buku>>?, response: Response<List<Buku>>?) {

                if(response?.body() != null)
                    recyclerAdapter.setBukuListItems(response.body()!!)

                //shimmer
                shimmer!!.stopShimmer()
                shimmer!!.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Buku>>?, t: Throwable?) {
                val snackbar = Snackbar.make(view, getString(R.string.failed_load_data_api),
                    Snackbar.LENGTH_LONG).setAction("OK", null)
                val snackbarView = snackbar.view
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
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

}