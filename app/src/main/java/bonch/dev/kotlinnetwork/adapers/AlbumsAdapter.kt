package bonch.dev.kotlinnetwork.adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.models.Album

class AlbumsAdapter(val list: ArrayList<Album>, val context: Context) :
    RecyclerView.Adapter<AlbumsAdapter.ItemAlbumsHolder>() {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAlbumsHolder {
        return ItemAlbumsHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_album, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemAlbumsHolder, position: Int) {
        val album = list[position]
        holder.bind(album)
    }

    inner class ItemAlbumsHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        private val titleAlbumTextView = itemview.findViewById<TextView>(R.id.item_user_nickname)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

        fun bind(album: Album) {
            titleAlbumTextView.text = album.title

        }

    }

     fun removeAlbum(position: Int) {
        list.removeAt(position)
         notifyDataSetChanged()
    }
}

