package misterhz.json.model;

public class Comment {
    var postId: Int = 0;
    lateinit var text : String;

    constructor(text: String) {
        this.text = text
    };

    constructor();
}
