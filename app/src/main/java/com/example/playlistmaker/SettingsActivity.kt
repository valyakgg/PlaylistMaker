package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)

        settingsBackButton.setOnClickListener {
            finish()

        }
        findViewById<TextView>(R.id.textView_share_the_app).setOnClickListener {
            shareApp()
        }

        findViewById<TextView>(R.id.textView_write_to_support).setOnClickListener {
            writeToSupport()
        }

        findViewById<TextView>(R.id.textView_user_agreement).setOnClickListener {
            openUserAgreement()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
    }

    private fun writeToSupport() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
        }

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_email_client)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.no_email_clients_found), Toast.LENGTH_SHORT).show()
        }
    }


    private fun openUserAgreement() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
        startActivity(intent)
    }
    }
