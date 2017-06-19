package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.ModalityRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = { ModalityRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Modality.class })
public class Modality extends RealmObject {

@SerializedName("modality_id")
@Expose
private String modalityId;
@SerializedName("modality_name")
@Expose
private String modalityName;

public String getModalityId() {
return modalityId;
}

public void setModalityId(String modalityId) {
this.modalityId = modalityId;
}

public String getModalityName() {
return modalityName;
}

public void setModalityName(String modalityName) {
this.modalityName = modalityName;
}

}