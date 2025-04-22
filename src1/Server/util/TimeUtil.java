package Server.util;

public class TimeUtil implements Runnable{

    private int id;

    final private long duration;

    private long currentTime;

    public TimeUtil(int id, long timerDuration) {
        this.id = id;
        this.duration = timerDuration;
        currentTime = timerDuration;

    }

    public long getCurrentTime() {
        return currentTime;
    }

    public int getID() {
        return id;
    }

    public void reset() {
        currentTime = duration;
    }

    @Override
    public void run() {
        //timer logic to be added
    }
}
