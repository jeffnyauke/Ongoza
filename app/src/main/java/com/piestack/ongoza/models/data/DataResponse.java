package com.piestack.ongoza.models.data;

import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.piestack.ongoza.helper.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelConverter;
import org.parceler.ParcelPropertyConverter;
import org.parceler.Parcels;
import org.parceler.TypeRangeParcelConverter;

import io.realm.DataResponseRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = { DataResponseRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { DataResponse.class })

public class DataResponse extends RealmObject{


    @PrimaryKey
    private Integer id;

@SerializedName("partner")
@Expose
private RealmList<Partner> partner = null;
@SerializedName("support_theme")
@Expose
private RealmList<SupportTheme> supportTheme = null;
@SerializedName("county")
@Expose
private RealmList<County> county = null;
@SerializedName("modality")
@Expose
private RealmList<Modality> modality = null;
@SerializedName("internal_process")
@Expose
private RealmList<InternalProcess> internalProcess = null;
@SerializedName("pd")
@Expose
private RealmList<Pd> pd = null;
@SerializedName("sub_supporttheme")
@Expose
private RealmList<SubSupporttheme> subSupporttheme = null;
@SerializedName("support_mode")
@Expose
private RealmList<SupportMode> supportMode = null;



    public RealmList<Partner> getPartner() {
return partner;
}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public void setPartner(RealmList<Partner> partner) {
this.partner = partner;
}

public RealmList<SupportTheme> getSupportTheme() {
return supportTheme;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setSupportTheme(RealmList<SupportTheme> supportTheme) {
this.supportTheme = supportTheme;
}


public RealmList<County> getCounty() {
return county;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setCounty(RealmList<County> county) {
this.county = county;
}


public RealmList<Modality> getModality() {return modality;}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setModality(RealmList<Modality> modality) {
this.modality = modality;
}


public RealmList<InternalProcess> getInternalProcess() {
return internalProcess;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setInternalProcess(RealmList<InternalProcess> internalProcess) {
this.internalProcess = internalProcess;
}

public RealmList<Pd> getPd() {
return pd;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setPd(RealmList<Pd> pd) {
this.pd = pd;
}


public RealmList<SubSupporttheme> getSubSupporttheme() {
return subSupporttheme;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setSubSupporttheme(RealmList<SubSupporttheme> subSupporttheme) {
this.subSupporttheme = subSupporttheme;
}

public RealmList<SupportMode> getSupportMode() {
return supportMode;
}

    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setSupportMode(RealmList<SupportMode> supportMode) {
this.supportMode = supportMode;
}

}