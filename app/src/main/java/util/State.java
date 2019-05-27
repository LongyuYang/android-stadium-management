package util;

public class State {
    static private State state = new State();
    private User user = null;
    static public State getState(){
        return state;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User u){
        user = u;
    }
}
