package lxn240002;

public class Timer {
    long startTime, endTime, elapsedTime, memAvailable, memUsed;
    boolean ready;// Indicate whether the timer has been stopped

    // Constructor
    public Timer() {
        startTime = System.currentTimeMillis();
        ready = false;// Timer is running
    }

    // Restart the timer
    public void start() {
        startTime = System.currentTimeMillis();
        ready = false;// Timer is reset
    }

    public Timer end() {
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;// Calculate elapsed time
        memAvailable = Runtime.getRuntime().totalMemory();// Total memory
        memUsed = memAvailable - Runtime.getRuntime().freeMemory();// Memory use
        ready = true;
        return this;
    }
    // Returns time in millisecond
    public long duration() { if(!ready) { end(); }  return elapsedTime; }

    // Returns in bytes
    public long memory()   { if(!ready) { end(); }  return memUsed; }

    public String toString() {
        if(!ready) { end(); }
        return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
    }

}
