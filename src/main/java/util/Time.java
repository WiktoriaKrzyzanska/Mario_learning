package util;

public class Time {
    public static float timeStarted = System.nanoTime(); //start of application
    public static float getTime(){return (float)((System.nanoTime() - timeStarted) * 1E-9);}
    //time elapsed since start of app
}
