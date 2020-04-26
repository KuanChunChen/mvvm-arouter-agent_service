package k.c.common.lib.Util;

import android.os.Build;
import android.util.Log;

import java.io.IOException;

import CTOS.CtSystem;
import CTOS.CtSystemException;
import k.c.common.lib.logTool.LogTool;

import static k.c.common.lib.Util.CtmsModuleUtil.command;
import static k.c.common.lib.Util.CtmsModuleUtil.formatKernelVersion;
import static k.c.common.lib.Util.CtmsModuleUtil.readLine;
import static k.c.common.lib.constants.ModuleDefine.ID_ANDROID_LINUX_KERNEL;
import static k.c.common.lib.constants.ModuleDefine.ID_ANDROID_RELEASE;
import static k.c.common.lib.constants.ModuleDefine.ID_ANDROID_SDK;
import static k.c.common.lib.constants.ModuleDefine.ID_BIOS;
import static k.c.common.lib.constants.ModuleDefine.ID_BOOTSULD;
import static k.c.common.lib.constants.ModuleDefine.ID_CADRV_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_CAUSB_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_CIF_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_CLDRV_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_CLVWM_MP;
import static k.c.common.lib.constants.ModuleDefine.ID_CRADLE_MP;
import static k.c.common.lib.constants.ModuleDefine.ID_CRYPTO_HAL;
import static k.c.common.lib.constants.ModuleDefine.ID_EMVCL_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_EMV_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_KMS;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCABARCODE_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAEMVL2_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAETHERNET_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAFONT_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAFS_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAGSM_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAKMS_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCALCD_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAMODEM_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAPMODEM_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAPRT_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCARTC_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAUART_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAULDPM_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCAUSBH_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCLVW_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBCTOSAPI_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LIBTLS_SO;
import static k.c.common.lib.constants.ModuleDefine.ID_LINUX_KERNEL;
import static k.c.common.lib.constants.ModuleDefine.ID_LITTLE_KERNEL;
import static k.c.common.lib.constants.ModuleDefine.ID_ROOTFS;
import static k.c.common.lib.constants.ModuleDefine.ID_SAM_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_SECURITY_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_SYSUPD_KO;
import static k.c.common.lib.constants.ModuleDefine.ID_TMS;
import static k.c.common.lib.constants.ModuleDefine.ID_ULDPM;

public class GetModuleVersion {

    private static final String FILENAME_PROC_VERSION = "/proc/version";
    private static final String PERLOADER = "ro.boot.oem_pl_version";
    private static final String BOOTLOADER = "ro.boot.oem_bl_version";

    public static String getBootloaderVersion() {
        String bootloaderVersion = command("getprop", PERLOADER);
        return bootloaderVersion;
    }

    public static String getLittleKernelVersion() {
        String littleKernelVersion = command("getprop", BOOTLOADER);
        return littleKernelVersion;
    }

    public static String getLinuxKernelVersion() {
        String linuxKernelVersion = getFormattedKernelVersion().substring(0, getFormattedKernelVersion().length() - 1);
        return linuxKernelVersion;
    }

    public static String getFormattedKernelVersion() {
        try {
            return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

        } catch (IOException e) {
            Log.e("CTMS_SERVICE",
                    "IO Exception when getting kernel version for Device Info screen",
                    e);

            return "Unavailable";
        }
    }


    public static String getModuleVersion(String strName, int iStype) {


        CtSystem CTOSSystem = new CtSystem();
        String strReturn = "None";
        if (strName == null || strName == "" || strName.equals("")) {
            return strReturn;
        }


        switch (strName) {

            case ID_EMV_SO:
                try {
                    strReturn = CTOSSystem.getModuleVersion(iStype).substring(2,6);

                } catch (CtSystemException e) {
                    e.printStackTrace();
                    strReturn = "None";
                }
                break;
            case ID_EMVCL_SO:
                try {
                    strReturn = CTOSSystem.getModuleVersion(iStype).substring(2,6);

                } catch (CtSystemException e) {
                    e.printStackTrace();
                    strReturn = "None";
                }
                break;

            case ID_BOOTSULD:
            case ID_BIOS:
            case ID_CADRV_KO:
            case ID_CRYPTO_HAL:
            case ID_LINUX_KERNEL:
            case ID_SECURITY_KO:
            case ID_SYSUPD_KO:
            case ID_KMS:
            case ID_CAUSB_KO:
            case ID_LIBCAUART_SO:
            case ID_LIBCAUSBH_SO:
            case ID_LIBCAMODEM_SO:
            case ID_LIBCAETHERNET_SO:
            case ID_LIBCAFONT_SO:
            case ID_LIBCALCD_SO:
            case ID_LIBCAPRT_SO:
            case ID_LIBCARTC_SO:
            case ID_LIBCAULDPM_SO:
            case ID_LIBCAPMODEM_SO:
            case ID_LIBCAGSM_SO:
            case ID_LIBCAEMVL2_SO:
            case ID_LIBCAKMS_SO:
            case ID_LIBCAFS_SO:
            case ID_LIBCABARCODE_SO:
            case ID_CRADLE_MP:
            case ID_LIBTLS_SO:
            case ID_LIBCLVW_SO:
            case ID_LIBCTOSAPI_SO:
            case ID_SAM_KO:
            case ID_CLVWM_MP:
            case ID_ROOTFS:
            case ID_CIF_KO:
            case ID_CLDRV_KO:
            case ID_TMS:
            case ID_ULDPM:

                try {
                    strReturn = CTOSSystem.getModuleVersion(iStype);
                } catch (CtSystemException e) {
                    e.printStackTrace();
                    strReturn = "None";
                }
                break;
            default:
                strReturn = "None";
                break;

        }

        LogTool.d(strReturn);
        return strReturn;
    }




    public static String getAMEVersion(String strName) {
        String strReturn = "None";
        switch (strName) {

            case ID_ANDROID_SDK:
                strReturn = Build.VERSION.SDK;
                break;
            case ID_ANDROID_RELEASE:
                strReturn = Build.VERSION.RELEASE;
                break;
            case ID_LITTLE_KERNEL:
                strReturn = getLittleKernelVersion();
                break;
            case ID_ANDROID_LINUX_KERNEL:
                strReturn = getLinuxKernelVersion();
                break;
            default:
                strReturn = "None";
                break;
        }

        return strReturn;
    }


}
