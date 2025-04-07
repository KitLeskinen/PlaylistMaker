package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.practicum.playlistmaker.Creator.Creator

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.factory(application, Creator.providePreferencesInteractor(this))
    }

    private lateinit var binding: ActivitySettingsBinding

    private fun loading(darkThemeEnabled: Boolean) {
        binding.themeSwitcher.isChecked = darkThemeEnabled
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.getState().observe(this) { state ->
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
            finish()
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

            if (mailIntent.resolveActivity(packageManager) != null) {
                startActivity(
                    Intent.createChooser(
                        mailIntent,
                        getString(R.string.chooseEmailClient)
                    )
                )
            } else {
                Toast.makeText(
                    this,
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
}