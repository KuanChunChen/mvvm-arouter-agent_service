package k.c.module.update.vendor.common;

import k.c.common.lib.logTool.LogTool;
import k.c.module.update.constants.Constants;
import k.c.module.update.vendor.thired.s1e.VendorPackageS1E;

public class VendorPackageAgent {

    /**
     * select the vendor package
     */
    public static VendorPackageInterface selectVendorPackage() {
//        String vendorPackageName = BuildConfig.VENDOR_PACKAGE;
        String vendorPackageName = Constants.VENDOR_PACKAGE_NAME_S1E;
        switch (vendorPackageName){
            case Constants.VENDOR_PACKAGE_NAME_S1E:
                LogTool.d("Vendor package packageName is S1E");
                return new VendorPackageS1E();
            default:
                LogTool.d("Vendor package packageName is default S1E");
                return new VendorPackageS1E();
        }
    }

    /**
     * Get the vendor package packageName
     */
    public static boolean isS1EVendorPackage(){
//        String vendorPackageName = BuildConfig.VENDOR_PACKAGE;
        String vendorPackageName = Constants.VENDOR_PACKAGE_NAME_S1E;
        switch (vendorPackageName){
            case Constants.VENDOR_PACKAGE_NAME_S1E:
                LogTool.d("is S1E Vendor Package");
                return true;
            default:
                LogTool.d("isn't S1E Vendor Package");
                return false;
        }
    }

}
