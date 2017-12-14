package com.cncoderx.andhub.switcher;

import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author cncoderx
 */
public abstract class ButtonSwitcher {
    final View mButton;
    private boolean isSelected;
    private OnSwitchListener mListener;

    public ButtonSwitcher(@NonNull View button) {
        mButton = button;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected()) {
                    unselect();
                } else {
                    select();
                }
            }
        });
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void begin() {
        Completable completable = isSelectedCall();
        completable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        isSelected = true;
                        notifySwitch();
                        mButton.setClickable(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        isSelected = false;
                        notifySwitch();
                        mButton.setClickable(true);
                    }
                });
        mButton.setClickable(false);
    }

    public void select() {
        Completable completable = getSelectCall();
        completable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        isSelected = true;
                        notifySwitch();
                        mButton.setClickable(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mButton.setClickable(true);
                    }
                });
        mButton.setClickable(false);
    }

    public void unselect() {
        Completable completable = getUnselectCall();
        completable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        isSelected = false;
                        notifySwitch();
                        mButton.setClickable(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mButton.setClickable(true);
                    }
                });
        mButton.setClickable(false);
    }

    void notifySwitch() {
        if (mListener != null) {
            if (isSelected) {
                mListener.switchOn(mButton);
            } else {
                mListener.switchOff(mButton);
            }
        }
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        mListener = listener;
    }

    @NonNull
    public abstract Completable isSelectedCall();

    @NonNull
    public abstract Completable getSelectCall();

    @NonNull
    public abstract Completable getUnselectCall();

    public interface OnSwitchListener {
        void switchOn(View button);
        void switchOff(View button);
    }
}
