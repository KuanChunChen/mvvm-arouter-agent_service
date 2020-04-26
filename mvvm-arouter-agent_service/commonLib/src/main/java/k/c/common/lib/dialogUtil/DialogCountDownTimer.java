package k.c.common.lib.dialogUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import k.c.common.lib.CommonLib;
import k.c.common.lib.logTool.LogTool;

/**
 * Created by kongx on 2016/8/31.
 */
public class DialogCountDownTimer extends CountDownTimer {

    private static String COUNTDOWN_TIME_XML_NAME = "DialogCountDownTimer";

    public static String TIME_KEY = "times";

    public static String DESTORY_TIME_KEY = "destoryTime";

    public static long DEF_TIME = 0x0;

	public long times = 0;

	private long destoryTime = 0;

	private long createTime = 0;

    CountDownTimeListener listener;

    public DialogCountDownTimer(CountDownTimeListener listener) {
        super(60000, 1000);
        this.listener = listener;
    }

    public DialogCountDownTimer(long countDownInterval, CountDownTimeListener listener) {
        super(CommonLib.getAppContext().getSharedPreferences(COUNTDOWN_TIME_XML_NAME, Context.MODE_PRIVATE).getLong(TIME_KEY + listener.getClazzName(), DEF_TIME), countDownInterval);
        this.listener = listener;
    }

    public DialogCountDownTimer(long millisInFuture, long countDownInterval, CountDownTimeListener listener) {
        super(millisInFuture, countDownInterval);
        this.listener = listener;
    }

    //invoke with onCreate，use it to calculate the second of the once again enter.
    public void onCreateComputeTimes(){
        SharedPreferences sp = CommonLib.getAppContext().getSharedPreferences(COUNTDOWN_TIME_XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        createTime = System.currentTimeMillis();
        destoryTime = sp.getLong(DESTORY_TIME_KEY + listener.getClazzName(), DEF_TIME);
        long lastTimes = sp.getLong(TIME_KEY + listener.getClazzName(), DEF_TIME);
        if((createTime - destoryTime) < lastTimes){
            times = lastTimes - (createTime - destoryTime);
        }else{
            times = 0;
        }
        edit.putLong(TIME_KEY + listener.getClazzName(), times);
        edit.apply();
    }

    //invoke with onDestroy，use it to save the second on the exit page.
    public void onDestroyComputeTimes(){
        destoryTime = System.currentTimeMillis();
        SharedPreferences sp = CommonLib.getAppContext().getSharedPreferences(COUNTDOWN_TIME_XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(TIME_KEY + listener.getClazzName(), times);
        edit.putLong(DESTORY_TIME_KEY + listener.getClazzName(), destoryTime);
        edit.apply();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        LogTool.d("millisUntilFinished == %s", millisUntilFinished);
        times = millisUntilFinished;
        listener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        listener.onFinish();
    }

    /**
     * use it that whether goon the second of the once again enter.
     * @return boolean
     */
	public boolean needCountDownTimes(){
		if(times > 0){
			return true;
		}
		return false;
	}

    /**
     * reset the count time, if the time is 0, then will not countdown time.
     */
	public static void resetMyCountTime(){
        SharedPreferences sp = CommonLib.getAppContext().getSharedPreferences(COUNTDOWN_TIME_XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.apply();
	}

    public interface CountDownTimeListener {
		void onTick(long millisUntilFinished);//current second

		void onFinish();//countdown time has finish

        String getClazzName();//the serialized of the view
	}
}
