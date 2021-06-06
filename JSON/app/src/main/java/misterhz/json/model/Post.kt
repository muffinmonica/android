package misterhz.json.model

import java.io.Serializable

class Post : Serializable{
    var userId: Int = 0;
    var id: Int = 0;
    lateinit var text: String;

    constructor(userId: Int, id: Int, text: String) {
        this.userId = userId;
        this.id = id;
        this.text = text;
    };

    constructor()
}