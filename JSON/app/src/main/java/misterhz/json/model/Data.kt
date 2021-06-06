package misterhz.json.model

import org.json.JSONArray
import java.net.URL

public class Data {

    companion object {

        fun loadUsers(): ArrayList<User> {
            val url = "https://jsonplaceholder.typicode.com/users/";
            val response = URL(url).readText();
            val jsonArray = JSONArray(response);
            val usersList = ArrayList<User>();
            val size = jsonArray.length();
            for(i in 0 until size) {
                val jsonObject = jsonArray.getJSONObject(i);
                val user = User();
                user.id = jsonObject.getInt("id");
                user.name = jsonObject.getString("name");
                user.username = jsonObject.getString("username");
                user.email = jsonObject.getString("email");
                user.tasksToDo = 0 // TODO change to real task count
                user.postsCount = 0 // TODO change to real post count
                usersList.add(user);
            }
            return usersList;
        }

        fun loadPosts(): ArrayList<Post> {
            val url = "https://jsonplaceholder.typicode.com/posts/";
            val response = URL(url).readText();
            val jsonArray = JSONArray(response);
            val postsList = ArrayList<Post>();
            val size = jsonArray.length();
            for(i in 0 until size) {
                val jsonObject = jsonArray.getJSONObject(i);
                val post = Post();
                post.userId = jsonObject.getInt("userId");
                post.id = jsonObject.getInt("id");
                post.text = jsonObject.getString("body");
                postsList.add(post);
            }
            return postsList;
        }

        fun loadTodos(): ArrayList<Todo> {
            val url = "https://jsonplaceholder.typicode.com/todos/";
            val response = URL(url).readText();
            val jsonArray = JSONArray(response);
            val todosList = ArrayList<Todo>();
            val size = jsonArray.length();
            for(i in 0 until size) {
                val jsonObject = jsonArray.getJSONObject(i);
                val todo = Todo();
                todo.userId = jsonObject.getInt("userId");
                todo.text = jsonObject.getString("title");
                todo.isCompleted = jsonObject.getBoolean("completed");
                todosList.add(todo);
            }
            return todosList;
        }

        fun loadComments(): ArrayList<Comment> {
            val url = "https://jsonplaceholder.typicode.com/comments";
            val response = URL(url).readText();
            val jsonArray = JSONArray(response);
            val commentsList = ArrayList<Comment>();
            val size = jsonArray.length();
            for(i in 0 until size) {
                val jsonObject = jsonArray.getJSONObject(i);
                val comment = Comment();
                comment.postId = jsonObject.getInt("postId");
                comment.text = jsonObject.getString("body");
                commentsList.add(comment);
            }
            return commentsList;
        }

        fun getUserPosts(userId: Int, posts: ArrayList<Post>): ArrayList<Post> {
            return posts.filter { it.userId == userId } as ArrayList<Post>;
        }

        fun getUserTodos(userId: Int, todos: ArrayList<Todo>): ArrayList<Todo> {
            return todos.filter { it.userId == userId && !it.isCompleted } as ArrayList<Todo>;
        }

        fun getPostComments(postId: Int, comments: ArrayList<Comment>): ArrayList<Comment> {
            return comments.filter { it.postId == postId } as ArrayList<Comment>;
        }
    }
}