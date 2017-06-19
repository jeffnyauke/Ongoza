package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.SupportModeRealmProxy;

@Parcel(implementations = { SupportModeRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { SupportMode.class })
public class SupportMode extends RealmObject {

@SerializedName("mode_id")
@Expose
private String modeId;
@SerializedName("mode_name")
@Expose
private String modeName;

public String getModeId() {
return modeId;
}

public void setModeId(String modeId) {
this.modeId = modeId;
}

public String getModeName() {
return modeName;
}

public void setModeName(String modeName) {
this.modeName = modeName;
}

}