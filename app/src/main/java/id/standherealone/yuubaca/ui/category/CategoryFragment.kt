package id.standherealone.yuubaca.ui.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.standherealone.yuubaca.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel

        // Binding IPA
        binding.btnKategoriIpa.setOnClickListener {
            Toast.makeText(context,"Ini IPA ya Anjenk!", Toast.LENGTH_SHORT).show()
        }

        // Binding IPS
        binding.btnKategoriIps.setOnClickListener {
            Toast.makeText(context,"Ini IPS ya Anjenk!", Toast.LENGTH_SHORT).show()
        }

        // Binding SD
        binding.btnKategoriSd.setOnClickListener {
            Toast.makeText(context,"Ini SD ya Anjenk!", Toast.LENGTH_SHORT).show()
        }

        // Binding SMP
        binding.btnKategoriSmp.setOnClickListener {
            Toast.makeText(context,"Ini SMP ya Anjenk!", Toast.LENGTH_SHORT).show()
        }

        // Binding SMA
        binding.btnKategoriSma.setOnClickListener {
            Toast.makeText(context,"Ini SMA ya Anjenk!", Toast.LENGTH_SHORT).show()
        }
    }

}