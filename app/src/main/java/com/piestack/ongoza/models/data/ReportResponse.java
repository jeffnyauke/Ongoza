package com.piestack.ongoza.models.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.piestack.ongoza.helper.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.DataResponseRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.ReportRealmProxy;
import io.realm.ReportResponseRealmProxy;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { ReportResponseRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { ReportResponse.class })
public class ReportResponse extends RealmObject{

    @PrimaryKey
    private Integer id = 0;

@SerializedName("report")
@Expose
private RealmList<Report> report = null;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public RealmList<Report> getReport() {
return report;
}
    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setReport(RealmList<Report> report) {
this.report = report;
}

}