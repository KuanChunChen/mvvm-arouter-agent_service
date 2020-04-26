package k.c.module.commonbusiness.po;

public class EnableConfig {

    public boolean ctmsEnable;
    public boolean bootConnectEnable;
    public long connectTime;
    public long leaveTime;

    @Override
    public String toString() {
        return "EnableConfig{" +
                "ctmsEnable=" + ctmsEnable +
                ", bootConnectEnable=" + bootConnectEnable +
                ", connectTime=" + connectTime +
                ", leaveTime=" + leaveTime +
                '}';
    }
}
