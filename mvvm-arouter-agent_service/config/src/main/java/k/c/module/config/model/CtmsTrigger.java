package k.c.module.config.model;


import com.google.gson.annotations.SerializedName;

public class CtmsTrigger {

    @SerializedName("Schedule")
    public Schedule schedule = new Schedule();
    @SerializedName("Switch")
    public Switch mSwitch = new Switch();

    @SerializedName("Immediately")
    public Immediately immediately = new Immediately();
    @SerializedName("Polling")
    public Polling polling = new Polling();

    public class Schedule{
        @SerializedName("year")
        public int year;
        @SerializedName("month")
        public int month;
        @SerializedName("day")
        public int day;
        @SerializedName("hour")
        public int hour;
        @SerializedName("minute")
        public int minute;
        @SerializedName("second")
        public int second;
        @SerializedName("timezone")
        public int timezone;
        @SerializedName("unixtime")
        public long unixtime;

        @Override
        public String toString() {
            return "Schedule{" +
                    "year=" + year +
                    ", month=" + month +
                    ", day=" + day +
                    ", hour=" + hour +
                    ", minute=" + minute +
                    ", second=" + second +
                    ", timezone=" + timezone +
                    ", unixtime=" + unixtime +
                    '}';
        }
    }

    public class Switch{
        @SerializedName("trigger")
        public int trigger;

        @Override
        public String toString() {
            return "Switch{" +
                    "trigger=" + trigger +
                    '}';
        }
    }



    public class Immediately{
        @SerializedName("execute")
        public int execute;

        @Override
        public String toString() {
            return "Immediately{" +
                    "execute=" + execute +
                    '}';
        }
    }

    public class Polling{
        @SerializedName("Enable")
        public int enable;

        @SerializedName("Cycle")
        public int cycle;

        @Override
        public String toString() {
            return "Polling{" +
                    "enable=" + enable +
                    ", cycle=" + cycle +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CtmsTrigger{" +
                "schedule=" + schedule +
                ", mSwitch=" + mSwitch +
                ", immediately=" + immediately +
                ", polling=" + polling +
                '}';
    }
}

