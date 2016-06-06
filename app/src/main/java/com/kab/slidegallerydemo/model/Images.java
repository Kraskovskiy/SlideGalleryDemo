package com.kab.slidegallerydemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Suxx on 01.06.16.
 */
public class Images {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("index")
    @Expose
    private int index;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The index
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param index
     * The index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
