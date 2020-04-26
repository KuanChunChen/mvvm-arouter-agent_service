package k.c.module.commonbusiness.po;

public class ActiveConfig {
    public int activeYear;
    public int activeMonth;
    public int activeDay;
    public int activeHour;
    public int activeMinute;
    public int activeSecond;
    public int activeTimezone;
    public long activeUnixtime;

    public int switchAction;

    public int installLock;

    public int immediatelyExecute;

    public int postponeMinute;

    @Override
    public String toString() {
        return "ActiveConfig{" +
                "activeYear=" + activeYear +
                ", activeMonth=" + activeMonth +
                ", activeDay=" + activeDay +
                ", activeHour=" + activeHour +
                ", activeMinute=" + activeMinute +
                ", activeSecond=" + activeSecond +
                ", activeTimezone=" + activeTimezone +
                ", activeUnixtime=" + activeUnixtime +
                ", switchAction=" + switchAction +
                ", installLock=" + installLock +
                ", immediatelyExecute=" + immediatelyExecute +
                ", postponeMinute=" + postponeMinute +
                '}';
    }
}
