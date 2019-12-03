package com.example.searchforimages.view.adapter

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchforimages.R
import com.example.searchforimages.model.ImagePost
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.example.searchforimages.utils.extension.inflate
import com.example.searchforimages.utils.extension.showToast
import kotlinx.android.synthetic.main.list_item_image_post.view.*

class ImagePostAdapter(private var items: List<ImagePost>) :
    RecyclerView.Adapter<ImagePostAdapter.ViewHolder>() {

    private val onClickSubject = PublishSubject.create<ImageItem>()
    val clickStream: Observable<ImageItem> get() = onClickSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_image_post))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position]) { onClickSubject.onNext(it) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setImages(items: List<ImagePost>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addImages(items: List<ImagePost>) {
        this.items = this.items.plus(items)
        notifyDataSetChanged()
    }

    fun getImagesList(): List<ImagePost> {
        return items
    }

    fun resetItems() {
        items = emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ImagePost, click: (ImageItem) -> Unit = {}) {
            itemView.setOnClickListener(null)
            Glide.with(itemView.context).clear(itemView.image_view)
            Glide.with(itemView.context)
                .load(item.images?.first()?.link)
                .centerCrop()
                .into(itemView.image_view)

            itemView.image_view.setOnClickListener {
                click.invoke(
                    ImageItem(
                        itemView.image_view,
                        item
                    )
                )
            }
            itemView.link_button.setOnClickListener {
                val clipboard =
                    itemView.context.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link", item.images?.first()?.link)
                clipboard.setPrimaryClip(clip)
                itemView.context.showToast("Copied")
            }

            itemView.score_text.text = item.score.toString()
            itemView.title_text.text = item.title
        }
    }

    data class ImageItem(val imageView: View, val data: ImagePost)
}