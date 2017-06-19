package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.PartnerRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = { PartnerRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Partner.class })
public class Partner extends RealmObject{

@SerializedName("p_id")
@Expose
private String pId;
@SerializedName("p_name")
@Expose
private String pName;
@SerializedName("county_id")
@Expose
private String countyId;

public String getPId() {
return pId;
}

public void setPId(String pId) {
this.pId = pId;
}

public String getPName() {
return pName;
}

public void setPName(String pName) {
this.pName = pName;
}

public String getCountyId() {
return countyId;
}

public void setCountyId(String countyId) {
this.countyId = countyId;
}

}