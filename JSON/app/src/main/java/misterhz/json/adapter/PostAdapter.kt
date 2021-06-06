package misterhz.json.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import misterhz.json.R
import misterhz.json.model.Post


class PostAdapter(private val data: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var mClickListener: ItemClickListener? = null;

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_adapter_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.text.text = data[position].text;
    }

    override fun getItemCount() = data.size;

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener;
    }

    fun getItem(id: Int): Post? {
        return data[id];
    }


    inner class ViewHolder(row: View): RecyclerView.ViewHolder(row), View.OnClickListener{

        public val text: TextView;

        init {
            text = row.findViewById<TextView>(R.id.post_id);
            row.setOnClickListener(this);
        }

        override fun onClick(v: View?) {
            mClickListener?.onItemClick(v, adapterPosition);
        }
    }
}