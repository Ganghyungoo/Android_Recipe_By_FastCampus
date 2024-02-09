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
import com.test.youtubeappproject.databinding.ItemVideoHeaderBinding
import com.test.youtubeappproject.dataclass.VideoItem


class PlayerVideoAdapter(private val context: Context, private val onClick: (VideoItem) -> Unit) :
    ListAdapter<VideoItem, RecyclerView.ViewHolder>(diffUtil) {

    inner class HeaderViewHolder(private val binding: ItemVideoHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VideoItem) {
            binding.titleTextView.text = item.title
            binding.subTitleTextView.text = context.getString(
                R.string.header_sub_title_video_info, item.viewCount, item.dateText
            )
            binding.channelNameTextView.text = item.channelName
            binding.channelLogoImageView

            Glide.with(binding.channelLogoImageView)
                .load(item.channelThumb)
                .circleCrop()
                .into(binding.channelLogoImageView)
        }
    }

    inner class VideoViewHolder(private val itemVideoBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoBinding.root) {
        fun bind(item: VideoItem) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER){
            HeaderViewHolder(ItemVideoHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            VideoViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            (holder as HeaderViewHolder).bind(currentList[position])
        } else {
            (holder as VideoViewHolder).bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_VIDEO
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_VIDEO = 1

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