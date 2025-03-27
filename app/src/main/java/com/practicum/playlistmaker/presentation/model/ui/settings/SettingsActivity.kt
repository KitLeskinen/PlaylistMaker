package com.practicum.playlistmaker.presentation.model.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator.Creator

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    private lateinit var binding:ActivitySettingsBinding

    private val preferencesInteractor = Creator.providePreferencesInteractor(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.themeSwitcher.isChecked = preferencesInteractor.getThemePreferences()

        binding.backImageView.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener() { _, checked ->

            preferencesInteractor.saveThemePreferences(checked)

            (applicationContext as App).switchTheme(checked)
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