package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.InternalProcessRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = { InternalProcessRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { InternalProcess.class })
public class InternalProcess extends RealmObject {

@SerializedName("oip_id")
@Expose
private String oipId;
@SerializedName("oip_name")
@Expose
private String oipName;

public String getOipId() {
return oipId;
}

public void setOipId(String oipId) {
this.oipId = oipId;
}

public String getOipName() {
return oipName;
}

public void setOipName(String oipName) {
this.oipName = oipName;
}

}