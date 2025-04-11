package Server.model;

public class Player {

    private int playerID;

    private String username;

    private String password;

    private int wins;

    private int loses;

    private String createdAt;
    //more can be added if needed

    public Player(){}

    public Player(int pID, String u, String p, int s, int l, String cA) {
        this.playerID = pID;
        this.username = u;
        this.password = p;
        this.wins = s;
        this.loses = l;
        this.createdAt = cA;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() { return loses; }

    public void setLoses(int loses) { this.loses = loses; }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object obj) {
        Player another = (Player) obj;
        return this.username.equals(another.getUsername());
    }

    @Override
    public String toString() {
        return playerID +", "+ username + ", " + wins;
    }
}

