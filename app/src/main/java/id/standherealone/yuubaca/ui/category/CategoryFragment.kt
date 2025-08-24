package id.standherealone.yuubaca.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.standherealone.yuubaca.databinding.FragmentCategoryBinding

@Suppress("DEPRECATION")
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
            val ipa = Intent(context, CategoryIPAActivity::class.java)
            startActivity(ipa)
        }

        // Binding IPS
        binding.btnKategoriIps.setOnClickListener {
            val ips = Intent(context, CategoryIPSActivity::class.java)
            startActivity(ips)
        }

        // Binding SD
        binding.btnKategoriSd.setOnClickListener {
            val sd = Intent(context, CategorySDActivity::class.java)
            startActivity(sd)
        }

        // Binding SMP
        binding.btnKategoriSmp.setOnClickListener {
            val smp = Intent(context, CategorySMPActivity::class.java)
            startActivity(smp)
        }

        // Binding SMA
        binding.btnKategoriSma.setOnClickListener {
            val sma = Intent(context, CategorySMAActivity::class.java)
            startActivity(sma)
        }

        // Binding Umum
        binding.btnKategoriUmum.setOnClickListener {
            val umum = Intent(context, CategoryUMUMActivity::class.java)
            startActivity(umum)
        }
    }

}