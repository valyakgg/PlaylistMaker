package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackName = intent.getStringExtra("TRACK_NAME") ?: ""
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: ""
        val trackTime = intent.getLongExtra("TRACK_TIME", 0)
        val artworkUrl100 = intent.getStringExtra("ARTWORK_URL")
        val collectionName = intent.getStringExtra("COLLECTION_NAME")
        val releaseDate = intent.getStringExtra("RELEASE_DATE")
        val primaryGenreName = intent.getStringExtra("PRIMARY_GENRE")
        val country = intent.getStringExtra("COUNTRY")

        val buttonBack = findViewById<ImageButton>(R.id.button_back)
        val coverImage = findViewById<ImageView>(R.id.cover_image)
        val trackNameView = findViewById<TextView>(R.id.track_name)
        val artistNameView = findViewById<TextView>(R.id.artist_name)
        val durationView = findViewById<TextView>(R.id.track_duration)
        val albumNameView = findViewById<TextView>(R.id.album_name)
        val genreView = findViewById<TextView>(R.id.genre)
        val countryView = findViewById<TextView>(R.id.country)
        val releaseYearView = findViewById<TextView>(R.id.release_year)
        val trackProgress = findViewById<TextView>(R.id.track_progress)

        trackNameView.text = trackName
        artistNameView.text = artistName
        durationView.text = formatTime(trackTime)
        genreView.text = primaryGenreName
        countryView.text = country
        releaseYearView.text = getFormattedReleaseYear(releaseDate)
        trackProgress.text = "00:00"

        if (!collectionName.isNullOrBlank()) {
            albumNameView.text = collectionName
            albumNameView.visibility = View.VISIBLE
        } else {
            albumNameView.visibility = View.GONE
        }

        Glide.with(this)
            .load(getCoverArtwork(artworkUrl100))
            .placeholder(R.drawable.placeholder)
            .into(coverImage)

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / 60000
        val seconds = (millis % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getFormattedReleaseYear(date: String?): String {
        return date?.takeIf { it.length >= 4 }?.substring(0, 4) ?: ""
    }

    private fun getCoverArtwork(url: String?): String? {
        return url?.replaceAfterLast('/', "512x512bb.jpg")
    }
}
