package misterhz.json

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import misterhz.json.adapter.ItemClickListener
import misterhz.json.adapter.PostAdapter
import misterhz.json.adapter.TodoAdapter
import misterhz.json.model.Data
import misterhz.json.model.Post
import misterhz.json.model.Todo
import misterhz.json.model.User

class PostsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        val user = intent.extras?.get("user") as User;
        var posts: ArrayList<Post>;
        var userPosts: ArrayList<Post>;
        var todos: ArrayList<Todo>;
        var userTodos: ArrayList<Todo>;

        var postAdapter: PostAdapter;
        var todoAdapter: TodoAdapter;
        var postClickListener: ItemClickListener;

        val postsRecyclerView = findViewById<RecyclerView>(R.id.user_posts);
        val todosRecyclerView = findViewById<RecyclerView>(R.id.user_todos);

        postsRecyclerView.layoutManager = LinearLayoutManager(this);
        todosRecyclerView.layoutManager = LinearLayoutManager(this);

        val returnButton = findViewById<Button>(R.id.to_users_posts);
        returnButton.setOnClickListener() {
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }

        Thread() {
            run {
                posts = Data.loadPosts();
                userPosts = Data.getUserPosts(user.id, posts);
                todos = Data.loadTodos();
                userTodos = Data.getUserTodos(user.id, todos);
            }
            runOnUiThread() {
                postAdapter = PostAdapter(userPosts);
                todoAdapter = TodoAdapter(userTodos);
                postClickListener = object : ItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        Thread() {
                            run {

                            }
                            runOnUiThread() {
                                val intent = Intent(applicationContext, PostCommentActivity::class.java);
                                intent.putExtra("post", postAdapter.getItem(position));
                                startActivity(intent);
                            }
                        }.start()
                    }
                }
                postAdapter.setClickListener(postClickListener);
                postsRecyclerView.adapter = postAdapter;

                todosRecyclerView.adapter = todoAdapter;
            }
        }.start();
    }
}