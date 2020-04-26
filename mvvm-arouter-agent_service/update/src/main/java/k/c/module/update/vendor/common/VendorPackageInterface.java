package k.c.module.update.vendor.common;




public interface VendorPackageInterface {

    /**
     * set the callback
     * @param vendorApiResult callback
     */
    void setCallback(VendorApiResult vendorApiResult);

    /**
     * start install app
     *
     * @param path apk path
     */
    void installStart(String path);

    /**
     * set the prm callback
     * @param vendorApiResult callback
     */
    void setPrmCallback(VendorApiResult vendorApiResult);

    /**
     * start install parameter file
     *
     * @param path prm path
     */
    void installPrmStart(String path);

    /**
     * start uninstall app
     * @param packageName app packageName
     */
    void uninstallStart(String packageName);

    /**
     * reboot terminal api
     */
    void rebootStart();

    /**
     * get the vendor package packageName
     */
    String getVendorPackageName();

    /**
     * Launch default app
     */
    void launchDefaultApp();

    /**
     * is the default app
     */
    boolean isDefaultApp(String packageName);
}
