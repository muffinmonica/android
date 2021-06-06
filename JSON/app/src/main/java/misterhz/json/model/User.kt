package misterhz.json.model

import java.io.Serializable

public class User : Serializable {
    var id: Int = 0;
    lateinit var name: String;
    lateinit var username: String;
    lateinit var email: String;
    var tasksToDo: Int = 0;
    var postsCount: Int = 0;

    constructor(name: String, username: String, email: String, tasksToDo: Int, postsCount: Int) {
        this.name = name
        this.username = username
        this.email = email
        this.tasksToDo = tasksToDo
        this.postsCount = postsCount
    }
    constructor();
}