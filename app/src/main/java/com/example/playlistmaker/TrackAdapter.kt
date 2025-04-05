package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class TrackAdapter(
    private val trackList: MutableList<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackNameTextView: TextView = itemView.findViewById(R.id.trackName)
        private val artistNameTextView: TextView = itemView.findViewById(R.id.artistName)
        private val trackTimeTextView: TextView = itemView.findViewById(R.id.trackTime)
        private val artworkImageView: ImageView = itemView.findViewById(R.id.artworkImage)

        fun bind(track: Track) {
            trackNameTextView.text = track.trackName
            artistNameTextView.text = track.artistName
            trackTimeTextView.text = formatTrackTime(track.trackTime)

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter()
                )
                .into(artworkImageView)

            itemView.setOnClickListener {
                onItemClick(track)
            }
        }

        private fun formatTrackTime(millis: Long): String {
            val seconds = (millis / 1000) % 60
            val minutes = (millis / 1000) / 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int = trackList.size

    fun updateTracks(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}
