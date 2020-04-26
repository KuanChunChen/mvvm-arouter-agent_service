package k.c.module.commonbusiness.po;

public class TriggerConfig {

        public int scheduleYear;
        public int scheduleMonth;
        public int scheduleDay;
        public int scheduleHour;
        public int scheduleMinute;
        public int scheduleSecond;
        public int scheduleTimezone;
        public long scheduleUnixtime;

        public int switchTrigger;

        public int immediateExecute;

        public boolean pollingEnable;
        public int pollingCycle;

    @Override
    public String toString() {
        return "TriggerConfig{" +
                "scheduleYear=" + scheduleYear +
                ", scheduleMonth=" + scheduleMonth +
                ", scheduleDay=" + scheduleDay +
                ", scheduleHour=" + scheduleHour +
                ", scheduleMinute=" + scheduleMinute +
                ", scheduleSecond=" + scheduleSecond +
                ", scheduleTimezone=" + scheduleTimezone +
                ", scheduleUnixtime=" + scheduleUnixtime +
                ", switchTrigger=" + switchTrigger +
                ", immediateExecute=" + immediateExecute +
                ", pollingEnable=" + pollingEnable +
                ", pollingCycle=" + pollingCycle +
                '}';
    }
}
