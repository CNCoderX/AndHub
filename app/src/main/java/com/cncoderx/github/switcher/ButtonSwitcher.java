package com.cncoderx.github.switcher;

import android.support.annotation.NonNull;
import android.view.View;

import com.cncoderx.github.utils.CallbackFinal;

import okhttp3.ResponseBody;
import retrofit2.Call;

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
        Call<ResponseBody> call = isSelectedCall();
        call.enqueue(new CallbackFinal<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody body) {
                isSelected = true;
                notifySwitch();
            }

            @Override
            public void onFailure(ResponseBody body) {
                isSelected = false;
                notifySwitch();
            }

            @Override
            public void onPostResponse() {
                mButton.setClickable(true);
            }
        });
        mButton.setClickable(false);
    }

    public void select() {
        Call<ResponseBody> call = getSelectCall();
        call.enqueue(new CallbackFinal<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody body) {
                isSelected = true;
                notifySwitch();
            }

            @Override
            public void onPostResponse() {
                mButton.setClickable(true);
            }
        });
        mButton.setClickable(false);
    }

    public void unselect() {
        Call<ResponseBody> call = getUnselectCall();
        call.enqueue(new CallbackFinal<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody body) {
                isSelected = false;
                notifySwitch();
            }

            @Override
            public void onPostResponse() {
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
    public abstract Call<ResponseBody> isSelectedCall();

    @NonNull
    public abstract Call<ResponseBody> getSelectCall();

    @NonNull
    public abstract Call<ResponseBody> getUnselectCall();

    public interface OnSwitchListener {
        void switchOn(View button);
        void switchOff(View button);
    }
}
