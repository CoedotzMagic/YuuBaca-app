package id.standherealone.yuubaca.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.standherealone.yuubaca.BuildConfig
import id.standherealone.yuubaca.R
import id.standherealone.yuubaca.databinding.FragmentSettingBinding
import id.standherealone.yuubaca.ui.feedback.EasyFeedback
import androidx.core.net.toUri

@Suppress("DEPRECATION")
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        // TODO: Use the ViewModel

        binding.lytTermPolicies.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://coedotzmagic.com/privacy_policy".toUri()
                )
            )
        }

        binding.lytTellFriends.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, requireActivity().getString(R.string.app_name))
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Baca buku perpustakaan gratis dan lengkap hanya di YuuBaca!\n\nUnduh Sekarang!\nhttp://play.google.com/store/apps/details?id=id.standherealone.yuubaca")
            requireActivity().startActivity(Intent.createChooser(sharingIntent, "Bagikan Melalui"))
        }

        binding.lytRateThis.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    ("http://play.google.com/store/apps/details?id=" + requireActivity().packageName).toUri()
                )
            )
        }

        binding.lytVerApp.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    ("http://play.google.com/store/apps/details?id=" + requireActivity().packageName).toUri()
                )
            )
        }

        binding.lytOpensource.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/CoedotzMagic/YuuBaca-app".toUri()
                )
            )
        }

        binding.buildVersion.text = BuildConfig.VERSION_NAME

        binding.btnReport.setOnClickListener {
            EasyFeedback.Builder(requireContext())
                .withEmail("reporter@coedotzmagic.com")
                .withSystemInfo()
                .build()
                .start()
        }
    }

}