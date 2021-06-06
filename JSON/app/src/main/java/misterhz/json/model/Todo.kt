package misterhz.json.model

import java.io.Serializable

class Todo : Serializable{
    var userId: Int = 0;
    lateinit var text: String;
    var isCompleted: Boolean = true;

    constructor(userId: Int, text: String, isCompleted: Boolean) {
        this.userId = userId
        this.text = text
        this.isCompleted = isCompleted
    }

    constructor()
}