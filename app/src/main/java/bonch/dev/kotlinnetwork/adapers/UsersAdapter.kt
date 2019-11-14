
package bonch.dev.kotlinnetwork.adapers
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bonch.dev.kotlinnetwork.R
import bonch.dev.kotlinnetwork.models.User

class UsersAdapter (val list : ArrayList<User>, val context : Context) :
    RecyclerView.Adapter<UsersAdapter.ItemUsersHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUsersHolder {
        return ItemUsersHolder (
            LayoutInflater.from(context)
                .inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemUsersHolder, position: Int) {
        val user = list[position]
        holder.bind(user)
    }

    class ItemUsersHolder (itemview : View) : RecyclerView.ViewHolder(itemview) {

        private val userNickTextView = itemview.findViewById<TextView>(R.id.item_user_nickname)
        private val userNameTextView = itemview.findViewById<TextView>(R.id.item_user_name)
        private val userMailTextView = itemview.findViewById<TextView>(R.id.item_user_mail)

        fun bind (user : User) {
            userNickTextView.text = user.username
            userNameTextView.text = user.name
            userMailTextView.text = user.email
        }
    }
}

