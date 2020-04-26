package k.c.module.commonbusiness.model;

import java.util.Objects;

import k.c.module.commonbusiness.common.CommonConst;

public class UpdateDataModel extends BaseModel{
    /**
     * STATUS : 1
     * TYPE : Android App
     * NAME : c.k.marketplacedemo
     * VR : 1.0.4
     * PATH : /data/CTMS/Download/APK/13/marketplace_kotline_1_0_4.CAP
     * [{"STATUS":4,"TYPE":"Android App","NAME":"c.k.marketplacedemo","VR":"1.0.4","PATH":"\/data\/CTMS\/Download\/APK\/13\/marketplace_kotline_1_0_4.CAP"}]
     */
    public Integer id;
    public int status = CommonConst.UPDATE_STATUS_UNEXECUTE;
    public int type;
    public String name;
    public String packageName;
    public String versionName;
    public String versionCode;
    public String path = "";
    public int sType;
    public long size;
    public long activeTime;
    /**
     * 0:no result
     * 1:result success
     * 2:result fail
     */
    public int updateResult = CommonConst.UPDATE_RESULT_NO_RESULT;

    @Override
    public String getDiffKey() {
        return id + "";
    }

    @Override
    public void setDiffKey(String key) {

    }

    public boolean isSame(UpdateDataModel updateDataModel){
        return this.type == updateDataModel.type
                && Objects.equals(this.id, updateDataModel.id)
                && Objects.equals(this.packageName, updateDataModel.packageName)
                && Objects.equals(this.versionName, updateDataModel.versionName);

    }

    @Override
    public String toString() {
        return "UpdateDataModel{" +
                "id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", sType=" + sType + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", updateResult=" + updateResult +
                '}';
    }
}
