package k.c.common.lib.Util;

import CTOS.CtCtms;

public class KernelUtil {



    public static int ChangeSystemPanelPassword(String oldFirstPassword, int ofPwdSize, String oldSecondPassword, int osPwdSize, String newFirstPassword, int fpwdSize, String newSecondPassword, int spwdSize){

        CtCtms ctCtms = new CtCtms();
        int status = ctCtms.ChangeSystemPanelPassword(
                oldFirstPassword, ofPwdSize,
                oldSecondPassword, osPwdSize,
                newFirstPassword, fpwdSize,
                newSecondPassword, spwdSize);

        return status;
    }

    public static int updatePRMFile(String prmPath, String savePath) {

        CtCtms ctCtms = new CtCtms();
        int status = ctCtms.updatePRMFile(prmPath, savePath);
        return status;
    }
}
