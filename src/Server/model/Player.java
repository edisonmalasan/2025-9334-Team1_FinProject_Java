package Server.model;

public class Player {

    private int playerID;

    private String username;

    private String password;

    private int score;
    //more can be added if needed

    public Player(){}

    public Player(int pID, String u, String p, int s) {
        this.playerID = pID;
        this.username = u;
        this.password = p;
        this.score = s;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        Player another = (Player) obj;
        return this.username.equals(another.getUsername());
    }

    @Override
    public String toString() {
        return playerID +", "+ username + ", " + score;
    }
}

