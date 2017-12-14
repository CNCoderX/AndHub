package com.cncoderx.andhub.switcher;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IGistService;

import io.reactivex.Completable;

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
    public Completable isSelectedCall() {
        return mService.isStarred(id);
    }

    @NonNull
    @Override
    public Completable getSelectCall() {
        return mService.star(id);
    }

    @NonNull
    @Override
    public Completable getUnselectCall() {
        return mService.unstar(id);
    }

    @BindingAdapter({"star_switcher_id", "star_switcher_on", "star_switcher_off"})
    public static void setStarSwitcher(final TextView view, String id, final String on, final String off) {
        GistStarSwitcher switcher = new GistStarSwitcher(view, id);
        switcher.setOnSwitchListener(new ButtonSwitcher.OnSwitchListener() {
            @Override
            public void switchOn(View button) {
                view.setSelected(true);
                view.setText(on);
            }

            @Override
            public void switchOff(View button) {
                view.setSelected(false);
                view.setText(off);
            }
        });
        switcher.begin();
    }

}
