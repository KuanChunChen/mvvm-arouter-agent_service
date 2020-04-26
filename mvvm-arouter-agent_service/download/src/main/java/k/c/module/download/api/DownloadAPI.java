package k.c.module.download.api;

import k.c.module.download.model.DownloadHandshakeInfo;
import k.c.module.download.model.DownloadHandshakeResult;
import k.c.module.http.Result;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DownloadAPI {

    @POST("/gw/DownloadHS.php")
    Observable<Result<DownloadHandshakeResult>> downloadHS(@Body DownloadHandshakeInfo info);

    @GET
    Observable<Response<ResponseBody>> downloadFile(@Url String url, @Header("Range") String range);
}
