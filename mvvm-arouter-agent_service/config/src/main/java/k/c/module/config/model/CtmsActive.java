package k.c.module.config.model;


import com.google.gson.annotations.SerializedName;

public class CtmsActive {

    @SerializedName("Active")
    public Active active = new Active();
    @SerializedName("Switch")
    public Switch mSwitch = new Switch();
    @SerializedName("Install")
    public Install install = new Install();
    @SerializedName("Immediately")
    public Immediately immediately = new Immediately();
    @SerializedName("Postpone")
    public Postpone postpone = new Postpone();

    public class Active{
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
            return "Active{" +
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
        @SerializedName("action")
        public int action;

        @Override
        public String toString() {
            return "Switch{" +
                    "action=" + action +
                    '}';
        }
    }

    public class Install{
        @SerializedName("Lock")
        public int lock;

        @Override
        public String toString() {
            return "Install{" +
                    "lock=" + lock +
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

    public class Postpone{
        @SerializedName("minute")
        public int minute;

        @Override
        public String toString() {
            return "Postpone{" +
                    "minute=" + minute +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CtmsActive{" +
                "active=" + active +
                ", mSwitch=" + mSwitch +
                ", install=" + install +
                ", immediately=" + immediately +
                ", postpone=" + postpone +
                '}';
    }
}
