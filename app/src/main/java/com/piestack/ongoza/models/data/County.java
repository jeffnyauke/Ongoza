package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.CountyRealmProxy;
import io.realm.DataResponseRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = { CountyRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { County.class })
public class County extends RealmObject {

@SerializedName("county_id")
@Expose
private String countyId;
@SerializedName("county_name")
@Expose
private String countyName;

public String getCountyId() {
return countyId;
}

public void setCountyId(String countyId) {
this.countyId = countyId;
}

public String getCountyName() {
return countyName;
}

public void setCountyName(String countyName) {
this.countyName = countyName;
}

}