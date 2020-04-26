package k.c.module.login.api;

import k.c.module.http.Result;
import k.c.module.login.model.login.LoginInfo;
import k.c.module.login.model.login.LoginResult;
import k.c.module.login.model.logout.LogoutInfo;
import k.c.module.login.model.logout.LogoutResult;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginAPI {

    @Headers({
            "Accept: */*",
            "Connection: Keep-Alive",
            "Content-Type: application/x-www-form-urlencoded"

    })
    @POST("/gw/Login.php")
    Observable<Result<LoginResult>> postLogin(@Body LoginInfo info);

    @POST("/gw/Logout.php")
    Observable<Result<LogoutResult>> logout(@Body LogoutInfo info, @Header("cookie") String sessionId);
}

