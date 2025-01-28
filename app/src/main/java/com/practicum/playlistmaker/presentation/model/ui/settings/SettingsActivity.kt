package com.practicum.playlistmaker.presentation.model.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.DARK_THEME_ENABLED
import com.practicum.playlistmaker.R


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backImageView = findViewById<MaterialToolbar>(R.id.back)
        val shareAppView = findViewById<MaterialTextView>(R.id.shareApp)
        val mailToSupport = findViewById<MaterialTextView>(R.id.mailToSupport)
        val userAgreement = findViewById<MaterialTextView>(R.id.userAgreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = getSharedPreferences(DARK_THEME_ENABLED, MODE_PRIVATE).getBoolean(
            DARK_THEME_ENABLED, false)

        backImageView.setNavigationOnClickListener{
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener(){ _, checked ->
            val sharedPreferences = getSharedPreferences(DARK_THEME_ENABLED, MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(DARK_THEME_ENABLED, checked).apply()
            (applicationContext as App).switchTheme(checked)
        }
        shareAppView.setOnClickListener {
            val link = getString(R.string.yandex_practicum_android_developer_url)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, link)
            intent.setType("text/plain")
            startActivity(intent)
        }

        mailToSupport.setOnClickListener {
            val mailRecipient  = getString(R.string.mailRecipient)
            val mailSubject = getString(R.string.mailSubject)
            val mailContent = getString(R.string.mailContent)
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailRecipient))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
            mailIntent.putExtra(Intent.EXTRA_TEXT, mailContent)

            if(mailIntent.resolveActivity(packageManager) != null){
                startActivity(Intent.createChooser(mailIntent, getString(R.string.chooseEmailClient)))
            } else{
                Toast.makeText(this, getString(R.string.askForEmailClientInstall), Toast.LENGTH_SHORT).show()
            }
        }

        userAgreement.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(getString(R.string.practicumOffer)))
            startActivity(intent)
        }

    }
}