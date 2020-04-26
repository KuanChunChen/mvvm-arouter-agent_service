package k.c.module.config.api;

import k.c.module.config.model.ReportSystemResult;
import k.c.module.config.model.ReportSystemSettingData;
import k.c.module.config.model.RequestInfo;
import k.c.module.config.model.ReportAndroidSettingData;
import k.c.module.config.model.SettingResult;
import k.c.module.config.model.SystemSettingInfo;
import k.c.module.config.model.SystemSettingResult;
import k.c.module.http.Result;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SettingConfigAPI {

    @POST("/gw/ConfigSetting.php")
    Observable<Result<SettingResult>> getConfigSetting(@Body RequestInfo info);

    @POST("/gw/AndroidReport.php")
    Observable<Result<SettingResult>> reportAndroidSetting(@Body ReportAndroidSettingData info);

    @POST("/gw/SystemSetting.php")
    Observable<Result<SystemSettingResult>> getSystemSetting(@Body SystemSettingInfo systemSettingInfo);

    @POST("/gw/Report.php")
    Observable<Result<ReportSystemResult>> reportSystemSetting(@Body ReportSystemSettingData reportSystemSettingData);
}
