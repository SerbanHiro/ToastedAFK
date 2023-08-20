package me.serbob.toastedafk.Classes;

public class PlayerStats {
    private int defaultAfkTime;
    private int afkTimer;
    private int levelTimer;
    private float expTimer;
    private boolean xpEnabled;
    private int timeoutTimes;
    public PlayerStats(int defaultAfkTime,
                       int afkTimer,
                       int levelTimer,
                       float expTimer,
                       boolean xpEnabled,
                       int timeoutTimes) {
        this.defaultAfkTime = defaultAfkTime;
        this.afkTimer = afkTimer;
        this.levelTimer = levelTimer;
        this.expTimer = expTimer;
        this.xpEnabled = xpEnabled;
        this.timeoutTimes=timeoutTimes;
    }
    public int getDefaultAfkTime() {
        return defaultAfkTime;
    }
    public int getAfkTimer() {
        return afkTimer;
    }
    public void setAfkTimer(int timeLeft) {
        afkTimer=timeLeft;
    }
    public int getLevelTimer() {
        return levelTimer;
    }
    public float getExpTimer() {
        return expTimer;
    }
    public boolean isXpEnabled() {
        return xpEnabled;
    }
    public int getTimeoutTimes() {
        return timeoutTimes;
    }
    public void setTimeoutTimes(int timeoutTimesToSet) {
        timeoutTimes = timeoutTimes + timeoutTimesToSet;
    }
}
