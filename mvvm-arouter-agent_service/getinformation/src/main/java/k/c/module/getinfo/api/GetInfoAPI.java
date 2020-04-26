package k.c.module.getinfo.api;

import k.c.module.getinfo.model.dataexchange.DataExchangeInfo;
import k.c.module.getinfo.model.dataexchange.DataExchangeResult;
import k.c.module.getinfo.model.getinfo.GetInfoRequest;
import k.c.module.getinfo.model.getinfo.GetInfoResult;
import k.c.module.http.Result;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetInfoAPI {

    @POST("/gw/GetInfo.php")
    Observable<Result<GetInfoResult>> getInfo(@Body GetInfoRequest info);

    @POST("/gw/DataExchange.php")
    Observable<Result<DataExchangeResult>> dataExchange(@Body DataExchangeInfo info);

}
