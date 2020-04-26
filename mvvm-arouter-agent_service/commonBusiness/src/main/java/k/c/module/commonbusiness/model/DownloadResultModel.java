package k.c.module.commonbusiness.model;

public class DownloadResultModel {
    public int step;//0:onStart,1:onProgress,2:onFinish,3:onFailure
    public int id;
    public int statusCode;
    public int currentLength;
    public long size;
    public String path;

    public static DownloadResultModel buildOnStartModel(int id){
        DownloadResultModel downloadResultModel = new DownloadResultModel();
        downloadResultModel.step = 0;
        downloadResultModel.id = id;
        return downloadResultModel;
    }

    public static DownloadResultModel buildOnProgressModel(int currentLength){
        DownloadResultModel downloadResultModel = new DownloadResultModel();
        downloadResultModel.step = 1;
        downloadResultModel.currentLength = currentLength;
        return downloadResultModel;
    }

    public static DownloadResultModel buildOnFinishModel(int id, String path, long size){
        DownloadResultModel downloadResultModel = new DownloadResultModel();
        downloadResultModel.step = 2;
        downloadResultModel.id = id;
        downloadResultModel.path = path;
        downloadResultModel.size = size;
        return downloadResultModel;
    }

    public static DownloadResultModel buildOnFailureModel(int statusCode){
        DownloadResultModel downloadResultModel = new DownloadResultModel();
        downloadResultModel.step = 3;
        downloadResultModel.statusCode = statusCode;
        return downloadResultModel;
    }

    @Override
    public String toString() {
        return "DownloadResultModel{" +
                "step=" + step +
                ", id=" + id +
                ", statusCode=" + statusCode +
                ", currentLength=" + currentLength +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }
}
