package id.standherealone.yuubaca.ui.home

import android.app.SearchManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: BukuAdapter
    private var shimmer: ShimmerFrameLayout? = null
    lateinit var searchView: SearchView
    lateinit var bukuList: List<Buku>

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

        bukuList = ArrayList<Buku>()
        val apiInterface = ApiBuku.create().getBuku()

        //apiInterface.enqueue( Callback<List<Buku>>())
        apiInterface.enqueue( object : Callback<List<Buku>> {
            override fun onResponse(call: Call<List<Buku>>?, response: Response<List<Buku>>?) {

//                if (response?.body() != null)
//                    recyclerAdapter.setBukuListItems(response.body()!!)

                bukuList = response!!.body()!!
                Log.d("TAG", "Response = $bukuList")
                recyclerAdapter.setBukuList(requireContext(), bukuList)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = requireActivity().getSystemService(AppCompatActivity.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(requireActivity().componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                recyclerAdapter.getFilter()?.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                recyclerAdapter.getFilter()?.filter(query)
                return false
            }
        })
//        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        shimmer!!.startShimmer()
    }

    override fun onPause() {
        shimmer!!.stopShimmer()
        super.onPause()
    }

//    override fun onBackPressed() {
//        if (!searchView.isIconified) {
//            searchView.isIconified = true
//            return
//        }
//        super.requireActivity().onBackPressed()
//    }

}