package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.piestack.ongoza.helper.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.CountyResponseRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.ReportResponseRealmProxy;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { CountyResponseRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { CountyResponse.class })
public class CountyResponse extends RealmObject{


@SerializedName("county")
@Expose
private RealmList<County> county = null;



    public RealmList<County> getReport() {
return county;
}
    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setReport(RealmList<County> county) {
this.county = county;
}

}