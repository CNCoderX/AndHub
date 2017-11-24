package com.cncoderx.github.switcher;

import android.support.annotation.NonNull;
import android.view.View;

import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.service.IGistService;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class GistStarSwitcher extends ButtonSwitcher {
    private final String id;
    private IGistService mService;

    public GistStarSwitcher(@NonNull View button, String id) {
        super(button);
        this.id = id;
        mService = ServiceGenerator.create(IGistService.class);
    }

    @NonNull
    @Override
    public Call<ResponseBody> isSelectedCall() {
        return mService.isStarred(id);
    }

    @NonNull
    @Override
    public Call<ResponseBody> getSelectCall() {
        return mService.star(id);
    }

    @NonNull
    @Override
    public Call<ResponseBody> getUnselectCall() {
        return mService.unstar(id);
    }
}
