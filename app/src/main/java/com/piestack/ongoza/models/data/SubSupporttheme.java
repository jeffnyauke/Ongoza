package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.SubSupportthemeRealmProxy;

@Parcel(implementations = { SubSupportthemeRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { SubSupporttheme.class })
public class SubSupporttheme extends RealmObject {

@SerializedName("sub_id")
@Expose
private String subId;
@SerializedName("sub_name")
@Expose
private String subName;
@SerializedName("s_id")
@Expose
private String sId;

public String getSubId() {
return subId;
}

public void setSubId(String subId) {
this.subId = subId;
}

public String getSubName() {
return subName;
}

public void setSubName(String subName) {
this.subName = subName;
}

public String getSId() {
return sId;
}

public void setSId(String sId) {
this.sId = sId;
}

}