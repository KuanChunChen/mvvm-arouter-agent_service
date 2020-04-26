package k.c.module.diagnostics.api;

import k.c.module.diagnostics.model.DiagnosticData;
import k.c.module.diagnostics.model.DiagnosticResult;
import k.c.module.http.Result;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DiagnosticAPI {

    @POST("/gw/Diagnostics.php")
    Observable<Result<DiagnosticResult>> uploadDiagnosticData(@Body DiagnosticData diagnosticData);
}
