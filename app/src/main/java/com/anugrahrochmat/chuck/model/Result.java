package com.anugrahrochmat.chuck.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * Result class constructor
     */
    public Result(String id, String url, String value){
        this.id = id;
        this.url = url;
        this.value = value;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Parcelling
     */
    public Result(Parcel in){
        this.id = in.readString();
        this.url = in.readString();
        this.value = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.url);
        parcel.writeString(this.value);
    }
}
