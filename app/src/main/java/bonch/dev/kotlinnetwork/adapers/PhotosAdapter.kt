package bonch.dev.kotlinnetwork.adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.models.Photo
import com.bumptech.glide.Glide

class PhotosAdapter(val list: List<Photo>, val context: Context) :
    RecyclerView.Adapter<PhotosAdapter.ItemPhotosHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPhotosHolder {
        return ItemPhotosHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_photo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemPhotosHolder, position: Int) {
        val photo = list[position]
        holder.bind(photo)
    }

    class ItemPhotosHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        private val titlePhotosTextView = itemview.findViewById<TextView>(R.id.item_user_nickname)
        private val imagePhotosImageView = itemview.findViewById<ImageView>(R.id.item_photo_image)

        fun bind(photo: Photo) {
            titlePhotosTextView.text = photo.title

            Glide.with(itemView)
                .load(photo.url)
                .into(imagePhotosImageView)
        }
    }
}

