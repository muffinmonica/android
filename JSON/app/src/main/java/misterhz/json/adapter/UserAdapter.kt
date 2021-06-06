package misterhz.json.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import misterhz.json.R
import misterhz.json.model.User


class UserAdapter(private val data: ArrayList<User>) :
        RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var mClickListener: ItemClickListener? = null;

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.user_adapter_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = data[position].name;
        viewHolder.username.text = data[position].username;
        viewHolder.email.text = data[position].email;
        viewHolder.posts.text = data[position].postsCount.toString();
        viewHolder.todos.text = data[position].tasksToDo.toString();
    }

    override fun getItemCount() = data.size;

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener;
    }

    fun getItem(id: Int): User? {
        return data[id];
    }


    inner class ViewHolder(row: View): RecyclerView.ViewHolder(row), View.OnClickListener{

        public val todos: TextView;
        public val posts: TextView;
        public val email: TextView;
        public val username: TextView;
        public val name: TextView;

        init {
            name = row.findViewById<TextView>(R.id.name_id);
            username = row.findViewById<TextView>(R.id.username_id);
            email = row.findViewById<TextView>(R.id.email_id);
            posts = row.findViewById<TextView>(R.id.posts_id);
            todos = row.findViewById<TextView>(R.id.todos_id); // maybe LinearLayout as well

            row.setOnClickListener(this);
        }

        override fun onClick(v: View?) {
            mClickListener?.onItemClick(v, adapterPosition);
        }
    }

}

