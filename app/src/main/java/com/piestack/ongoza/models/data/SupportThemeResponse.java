package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.piestack.ongoza.helper.RealmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.realm.CountyResponseRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.SupportThemeResponseRealmProxy;

@Parcel(implementations = { SupportThemeResponseRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { SupportThemeResponse.class })
public class SupportThemeResponse extends RealmObject{


@SerializedName("stheme")
@Expose
private RealmList<SupportTheme> supportThemes = null;

    public RealmList<SupportTheme> getSupportTheme() {
return supportThemes;
}
    @ParcelPropertyConverter(RealmListParcelConverter.class)
public void setReport(RealmList<SupportTheme> supportThemes) {
this.supportThemes = supportThemes;
}

}