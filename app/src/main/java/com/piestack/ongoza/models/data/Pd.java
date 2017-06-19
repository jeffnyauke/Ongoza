package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.PdRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = { PdRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Pd.class })
public class Pd extends RealmObject{

@SerializedName("pd_id")
@Expose
private String pdId;
@SerializedName("pd_name")
@Expose
private String pdName;

public String getPdId() {
return pdId;
}

public void setPdId(String pdId) {
this.pdId = pdId;
}

public String getPdName() {
return pdName;
}

public void setPdName(String pdName) {
this.pdName = pdName;
}

}