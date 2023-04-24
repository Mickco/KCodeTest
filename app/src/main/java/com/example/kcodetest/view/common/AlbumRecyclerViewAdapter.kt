package com.example.kcodetest.view.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kcodetest.R
import com.example.kcodetest.databinding.ItemAlbumBinding
import com.example.kcodetest.viewmodel.album.AlbumListItemUI

class AlbumRecyclerViewAdapter(private val onBookmarkClicked: (AlbumListItemUI) -> Unit) :
    RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumItemViewHolder>() {

    var uiList: List<AlbumListItemUI> = listOf()
        set(value) {
            val result =
                DiffUtil.calculateDiff(AlbumListDiffUtil(oldListInfo = field, newListInfo = value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumItemViewHolder {
        return AlbumItemViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumItemViewHolder, position: Int) {
        val item = uiList.getOrNull(position) ?: return
        val context = holder.itemView.context ?: return

        holder.binding.albumItemTitleTextView.text = item.albumText.getString(context)

        val bookmarkImage = when (item.isBookmarked) {
            true -> R.drawable.ic_bookmark_filled
            false -> R.drawable.ic_bookmark
        }
        holder.binding.albumItemBookmarkButton.setImageResource(bookmarkImage)
        holder.binding.albumItemBookmarkButton.setOnClickListener {
            onBookmarkClicked(item)
        }

        val imageUrl = item.imageUrl.getString(context)
        Glide
            .with(holder.binding.root)
            .load(imageUrl)
            .centerCrop()
            .error(R.drawable.ic_error)
            .thumbnail(
                Glide.with(holder.binding.root)
                    .load(imageUrl)
                    .override(SIZE_THUMBNAIL)
            )
            .into(holder.binding.albumItemAlbumImageView)

    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ABLUM_ITEM
    }

    override fun getItemCount(): Int {
        return uiList.size
    }


    inner class AlbumItemViewHolder(val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root)


    inner class AlbumListDiffUtil(
        private val oldListInfo: List<AlbumListItemUI>,
        private val newListInfo: List<AlbumListItemUI>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldListInfo.size
        }

        override fun getNewListSize(): Int {
            return newListInfo.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldListInfo.getOrNull(oldItemPosition)
            val new = newListInfo.getOrNull(newItemPosition)

            return old != null && new != null && old::class == new::class && old.collectionId == new.collectionId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldListInfo.getOrNull(oldItemPosition)
            val new = newListInfo.getOrNull(newItemPosition)
            return old == new
        }
    }

    companion object {
        const val TYPE_ABLUM_ITEM = 1
        const val SIZE_THUMBNAIL = 5
    }
}