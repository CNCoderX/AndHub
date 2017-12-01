package com.cncoderx.github.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author cncoderx
 */
public class SearchResult<T> {
    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("incomplete_results")
    public boolean incompleteResults;

    public List<T> items;
}
