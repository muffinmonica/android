package misterhz.json

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import misterhz.json.adapter.CommentAdapter
import misterhz.json.model.Comment
import misterhz.json.model.Data
import misterhz.json.model.Post

class PostCommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment);

        var comments: ArrayList<Comment>;
        var postComments: ArrayList<Comment>;

        var commentAdapter: CommentAdapter;
        val commentsRecyclerView = findViewById<RecyclerView>(R.id.post_comments);

        commentsRecyclerView.layoutManager = LinearLayoutManager(this);

        val post = intent.extras?.get("post") as Post;
        val postView = findViewById<TextView>(R.id.post);
        postView.text = post.text;

        val returnButton = findViewById<Button>(R.id.to_users_comments);
        returnButton.setOnClickListener() {
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }

        Thread() {
            run {
                comments = Data.loadComments();
                postComments = Data.getPostComments(post.id, comments);
            }
            runOnUiThread() {
                commentAdapter = CommentAdapter(postComments);
                commentsRecyclerView.adapter = commentAdapter;
            }
        }.start();
    }
}