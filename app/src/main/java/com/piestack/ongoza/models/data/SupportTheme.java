package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.SupportThemeRealmProxy;

@Parcel(implementations = { SupportThemeRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { SupportTheme.class })
public class SupportTheme extends RealmObject {

@SerializedName("s_id")
@Expose
private String sId;
@SerializedName("s_name")
@Expose
private String sName;

public String getSId() {
return sId;
}

public void setSId(String sId) {
this.sId = sId;
}

public String getSName() {
return sName;
}

public void setSName(String sName) {
this.sName = sName;
}

}