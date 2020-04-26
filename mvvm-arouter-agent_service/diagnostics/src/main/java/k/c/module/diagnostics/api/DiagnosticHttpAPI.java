package k.c.module.diagnostics.api;

import k.c.module.diagnostics.model.DiagnosticData;
import k.c.module.diagnostics.model.DiagnosticResult;
import k.c.module.http.RetrofitResultObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static k.c.module.http.RetrofitClient.http;

public class DiagnosticHttpAPI {

    public static void uploadDignosticData(DiagnosticData diagnosticData, RetrofitResultObserver<DiagnosticResult> result) {

        http(DiagnosticAPI.class).uploadDiagnosticData(diagnosticData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result);
    }
}
