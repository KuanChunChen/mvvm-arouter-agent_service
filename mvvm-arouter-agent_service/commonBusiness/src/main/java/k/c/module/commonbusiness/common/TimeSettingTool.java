package k.c.module.commonbusiness.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import k.c.common.lib.logTool.LogTool;
import k.c.module.commonbusiness.CommonSingle;
import k.c.module.commonbusiness.po.ActiveConfig;
import k.c.module.commonbusiness.po.EnableConfig;
import k.c.module.commonbusiness.po.TriggerConfig;

public class TimeSettingTool {

    public static void setConnectTime(long connectTime){
        EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
        if (connectTime != 0){
            enableConfig.connectTime = connectTime / 1000;
        }
        CommonSingle.getInstance().setEnableConfig(enableConfig);
    }

    public static void setLeaveTime(long leaveTime){
        EnableConfig enableConfig = CommonSingle.getInstance().getEnableConfig();
        if (leaveTime != 0){
            enableConfig.leaveTime = leaveTime / 1000;
        }
        CommonSingle.getInstance().setEnableConfig(enableConfig);
    }

    public static void setActiveTime(long activeTime){
        String[] time = timeFormat(activeTime).split("\\.");
        ActiveConfig activeConfig = CommonSingle.getInstance().getActiveConfig();
        activeConfig.activeYear = Integer.parseInt(time[0]);
        activeConfig.activeMonth = Integer.parseInt(time[1]);
        activeConfig.activeDay = Integer.parseInt(time[2]);
        activeConfig.activeHour = Integer.parseInt(time[3]);
        activeConfig.activeMinute = Integer.parseInt(time[4]);
        activeConfig.activeSecond = Integer.parseInt(time[5]);
        activeConfig.activeUnixtime = activeTime;
        CommonSingle.getInstance().setActiveConfig(activeConfig);
        LogTool.d(activeConfig.toString());
    }

    public static void setTriggerTime(long triggerTime){
        String[] time = timeFormat(triggerTime).split("\\.");
        TriggerConfig triggerConfig = CommonSingle.getInstance().getTriggerConfig();
        triggerConfig.scheduleYear = Integer.parseInt(time[0]);
        triggerConfig.scheduleMonth = Integer.parseInt(time[1]);
        triggerConfig.scheduleDay = Integer.parseInt(time[2]);
        triggerConfig.scheduleHour = Integer.parseInt(time[3]);
        triggerConfig.scheduleMinute = Integer.parseInt(time[4]);
        triggerConfig.scheduleSecond = Integer.parseInt(time[5]);
        triggerConfig.scheduleUnixtime = triggerTime;
        CommonSingle.getInstance().setTriggerConfig(triggerConfig);
        LogTool.d(triggerConfig.toString());
    }

    public static String timeFormat(long time){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return sDateFormat.format(new Date(time * 1000));
    }
}
