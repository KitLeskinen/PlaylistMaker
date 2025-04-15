package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


import com.practicum.playlistmaker.R

import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.medialibrary.ui.MediaLibraryFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loading(darkThemeEnabled: Boolean) {
        binding.themeSwitcher.isChecked = darkThemeEnabled
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SettingsState.Loading -> loading(state.darkThemeEnabled)

            }
        }


        binding.themeSwitcher.setOnCheckedChangeListener() { switcherView, isChecked ->
            if (switcherView.isPressed) {
                viewModel.saveThemePreferences(isChecked)
                viewModel.applyTheme(isChecked)
            }
        }


        binding.backImageView.setNavigationOnClickListener {
           // finish()
        }


        binding.shareApp.setOnClickListener {
            val link = getString(R.string.yandex_practicum_android_developer_url)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, link)
            intent.setType("text/plain")
            startActivity(intent)
        }

        binding.mailToSupport.setOnClickListener {
            val mailRecipient = getString(R.string.mailRecipient)
            val mailSubject = getString(R.string.mailSubject)
            val mailContent = getString(R.string.mailContent)
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailRecipient))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
            mailIntent.putExtra(Intent.EXTRA_TEXT, mailContent)

            if (mailIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(
                    Intent.createChooser(
                        mailIntent,
                        getString(R.string.chooseEmailClient)
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.askForEmailClientInstall),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.userAgreement.setOnClickListener() {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(getString(R.string.practicumOffer)))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}