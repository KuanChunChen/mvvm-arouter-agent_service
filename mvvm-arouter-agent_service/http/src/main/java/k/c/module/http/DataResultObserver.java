package k.c.module.http;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class DataResultObserver<T> implements Observer<Response<ResponseBody>> {

    public static final String TAG = "DataResultObserver";

    public DataResultObserver() {}

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        DataResultException resultException;

        //non-200 error
        if(e instanceof HttpException){
            HttpException httpError =(HttpException) e;
            resultException = new DataResultException(httpError.code(),"non-200 HTTP ERROR",httpError);
//            LogTool.e(TAG,"Retrofit Http Error");
            onFailure(resultException);
        }
        //network error
        else if(e instanceof IOException){
            resultException = new DataResultException(0,"NETWORK ERROR",e);
//            LogTool.e(TAG,"Retrofit Network Error");
            onFailure(resultException);
        }

        //json parse error
        else if (e instanceof UnknownFormatConversionException || e instanceof JsonIOException || e instanceof JsonParseException){
            resultException = new DataResultException(DataResultException.PARSE_ERROR,"PARSE ERROR",e);
//            LogTool.e(TAG, "Retrofit Parse Error");
            onFailure(resultException);
        }
        //unknown error
        else {
            returnUnKnow(e);
        }
    }

    private void returnUnKnow(Throwable e) {
//        LogTool.d(e);
        DataResultException resultException = new DataResultException(DataResultException.UNKNOWN,"UNKNOWN ERROR",e);
//        ToastUtils.showShort("UnKnow error");
        onFailure(resultException);
    }

    @Override
    public void onNext(Response<ResponseBody> responseBodyResponse) {
        if(responseBodyResponse == null) {
            onFailure(new DataResultException(DataResultException.PARSE_ERROR, "Return null responseBodyResponse from server"));
            return;
        }
        ResponseBody responseBody = responseBodyResponse.body();
        if(responseBody == null){
            onFailure(new DataResultException(DataResultException.PARSE_ERROR, "Return null responseBody from server"));
            return;
        }

        byte[] bytes;
        try{
            bytes = responseBody.bytes();
        }catch (IOException e){
            onFailure(new DataResultException(DataResultException.PARSE_ERROR, "Return IOException"));
            e.printStackTrace();
//            LogTool.e(TAG, "Parse responseBody Error");
            return;
        }

        byte[] decodeBytes = Base64.decode(bytes, Base64.DEFAULT);
        String response = new String(decodeBytes);
        Result result = new Gson().fromJson(response, Result.class);
        if(result.code == null){
            onFailure(new DataResultException(DataResultException.PARSE_ERROR, "Return error result from server"));
            return;
        }
        if(Constants.Http.SUCCESS == result.code) {
            try {
                onSuccess((T)result.value);
            }
            catch (Exception e){
                e.printStackTrace();
                onError(new DataResultException(result.code, result.msg));
            }
        }
        else {
            onResultCode(result.code, result);
        }
    }

    /**
     * call this when getting non SUCCESS code
     */
    protected void onResultCode(int resultCode, Result result){
//        LogTool.d("server result [" + resultCode + "]");
        if (!TextUtils.isEmpty(result.msg)) {
//            LogTool.d("[" + resultCode + "]" + result.msg);
        }
    }

    public void onFailure(DataResultException e){
//        LogTool.d(e.toString());
        e.printStackTrace();
    }

    public abstract void onSuccess(T  data);


    public static class DataResultException extends RuntimeException {
        public int code;
        public String msg;

        public static final int UNKNOWN = 1000;
        public static final int PARSE_ERROR = 1001;

        public DataResultException(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public DataResultException(int code, String msg, Throwable e){
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "http code = "+code+"\n cause = "+msg +"\n"+super.toString();
        }
    }
}
