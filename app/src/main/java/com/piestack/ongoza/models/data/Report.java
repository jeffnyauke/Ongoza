package com.piestack.ongoza.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.ReportRealmProxy;

@Parcel(implementations = { ReportRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { Report.class })
public class Report extends RealmObject {

@SerializedName("id")
@Expose
private String id;
@SerializedName("p_id")
@Expose
private String pId;
@SerializedName("s_id")
@Expose
private String sId;
@SerializedName("sub_id")
@Expose
private String subId;
@SerializedName("mode_id")
@Expose
private String modeId;
@SerializedName("modality_id")
@Expose
private String modalityId;
@SerializedName("pd_id")
@Expose
private String pdId;
@SerializedName("oip_id")
@Expose
private String oipId;
@SerializedName("travel")
@Expose
private String travel;
@SerializedName("hours_ip")
@Expose
private String hoursIp;
@SerializedName("outcome")
@Expose
private String outcome;
@SerializedName("report_id")
@Expose
private String reportId;
@SerializedName("county_id")
@Expose
private String countyId;
@SerializedName("county_name")
@Expose
private String countyName;
@SerializedName("name")
@Expose
private String name;
@SerializedName("p_name")
@Expose
private String pName;
@SerializedName("s_name")
@Expose
private String sName;
@SerializedName("sub_name")
@Expose
private String subName;
@SerializedName("mode_name")
@Expose
private String modeName;
@SerializedName("modality_name")
@Expose
private String modalityName;
@SerializedName("pd_name")
@Expose
private String pdName;
@SerializedName("oip_name")
@Expose
private String oipName;
@SerializedName("dor")
@Expose
private String dor;

@SerializedName("e_needs")
@Expose
private String e_needs;

@SerializedName("exchange")
@Expose
private Boolean exchange;

    private boolean isImportant;
    private boolean isRead;
    private int color = -1;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getPId() {
return pId;
}

public void setPId(String pId) {
this.pId = pId;
}

public String getSId() {
return sId;
}

public void setSId(String sId) {
this.sId = sId;
}

public String getSubId() {
return subId;
}

public void setSubId(String subId) {
this.subId = subId;
}

public String getModeId() {
return modeId;
}

public void setModeId(String modeId) {
this.modeId = modeId;
}

public String getModalityId() {
return modalityId;
}

public void setModalityId(String modalityId) {
this.modalityId = modalityId;
}

public String getPdId() {
return pdId;
}

public void setPdId(String pdId) {
this.pdId = pdId;
}

public String getOipId() {
return oipId;
}

public void setOipId(String oipId) {
this.oipId = oipId;
}

public String getTravel() {
return travel;
}

public void setTravel(String travel) {
this.travel = travel;
}

public String getHoursIp() {
return hoursIp;
}

public void setHoursIp(String hoursIp) {
this.hoursIp = hoursIp;
}

public String getOutcome() {
return outcome;
}

public void setOutcome(String outcome) {
this.outcome = outcome;
}

public String getReportId() {
return reportId;
}

public void setReportId(String reportId) {
this.reportId = reportId;
}

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

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getPName() {
return pName;
}

public void setPName(String pName) {
this.pName = pName;
}

public String getSName() {
return sName;
}

public void setSName(String sName) {
this.sName = sName;
}

public String getSubName() {
return subName;
}

public void setSubName(String subName) {
this.subName = subName;
}

public String getModeName() {
return modeName;
}

public void setModeName(String modeName) {
this.modeName = modeName;
}

public String getModalityName() {
return modalityName;
}

public void setModalityName(String modalityName) {
this.modalityName = modalityName;
}

public String getPdName() {
return pdName;
}

public void setPdName(String pdName) {
this.pdName = pdName;
}

public String getOipName() {
return oipName;
}

public void setOipName(String oipName) {
this.oipName = oipName;
}

public String getDor() {
return dor;
}

public void setDor(String dor) {
this.dor = dor;
}

public String getE_needs() {
        return e_needs;
}

public void setE_needs(String e_needs) {
        this.e_needs = e_needs;
    }

    public Boolean getExchange() {
        return exchange;
    }

    public void setExchange(Boolean exchange) {
        this.exchange = exchange;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}