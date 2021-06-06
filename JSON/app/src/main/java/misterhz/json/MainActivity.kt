package misterhz.json

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import misterhz.json.adapter.ItemClickListener
import misterhz.json.adapter.UserAdapter
import misterhz.json.model.Data
import misterhz.json.model.Post
import misterhz.json.model.Todo
import misterhz.json.model.User
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        var userAdapter: UserAdapter;
        var userClickListener: ItemClickListener;

        var users: ArrayList<User>;
        var posts: ArrayList<Post>;
        var todos: ArrayList<Todo>;

        val recyclerView = findViewById<RecyclerView>(R.id.user_recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this);

        Thread() {
            run {
                users = Data.loadUsers();
                posts = Data.loadPosts();
                todos = Data.loadTodos();
                for(user in users) {
                    user.postsCount = Data.getUserPosts(user.id, posts).size;
                    user.tasksToDo = Data.getUserTodos(user.id, todos).size;
                }
            }
            runOnUiThread() {
                userAdapter = UserAdapter(users);
                userClickListener = object : ItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        Thread() {
                            run {

                            }
                            runOnUiThread() {
                                val intent = Intent(applicationContext, PostsActivity::class.java);
                                intent.putExtra("user", userAdapter.getItem(position));
                                startActivity(intent);
                            }
                        }.start()
                    }
                }
                userAdapter.setClickListener(userClickListener);
                recyclerView.adapter = userAdapter;
            }
        }.start();
    }
}