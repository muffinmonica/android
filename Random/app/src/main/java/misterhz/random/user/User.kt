package misterhz.random.user

class User {
    var id: Int = 0;
    var nick: String? = null;
    var pass: String? = null;
    var score: Int = 0;

    constructor(id: Int, nick: String?, pass: String?, score: Int) {
        this.id = id
        this.nick = nick
        this.pass = pass
        this.score = score
    }
}