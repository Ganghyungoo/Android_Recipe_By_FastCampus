package com.test.youtubeappproject.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.youtubeappproject.R
import com.test.youtubeappproject.databinding.ItemVideoBinding
import com.test.youtubeappproject.dataclass.VideoItem

class VideoAdapter(private val context:Context, private val onClick: (item:VideoItem) -> Unit): ListAdapter<VideoItem, VideoAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val itemVideoBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoBinding.root) {
        fun binding(item: VideoItem) {
            itemVideoBinding.titleTextView.text = item.title
            itemVideoBinding.subTitleTextView.text = context.getString(R.string.sub_title_video_info,item.channelName,item.viewCount,item.dateText)
            //영상 썸네일
            Glide.with(itemVideoBinding.videoThumbnailImageView)
                .load(item.thumb)
                .into(itemVideoBinding.videoThumbnailImageView)
            //채널 로고
            Glide.with(itemVideoBinding.channelLogoImageView)
                .load(item.channelThumb)
                .circleCrop()
                .into(itemVideoBinding.channelLogoImageView)

            itemVideoBinding.root.setOnClickListener {
                onClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}