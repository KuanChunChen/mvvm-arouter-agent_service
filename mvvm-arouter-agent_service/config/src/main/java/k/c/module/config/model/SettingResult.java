package k.c.module.config.model;

import com.google.gson.annotations.SerializedName;

public class SettingResult {
    @SerializedName("Setting")
    private Setting setting;

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}