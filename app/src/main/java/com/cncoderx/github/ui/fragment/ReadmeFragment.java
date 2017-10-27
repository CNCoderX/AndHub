package com.cncoderx.github.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.github.R;
import com.cncoderx.github.entites.Contents;
import com.cncoderx.github.netservice.IContentsService;
import com.cncoderx.github.utils.URLUtils;

import org.markdown4j.Markdown4jProcessor;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author cncoderx
 */

public class ReadmeFragment extends Fragment {
    private XWalkView xWalkView;

    public static ReadmeFragment create(String owner, String repo) {
        ReadmeFragment fragment = new ReadmeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("owner", owner);
        bundle.putString("repo", repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        View view = inflater.inflate(R.layout.fragment_readme, container, false);
        xWalkView = (XWalkView) view.findViewById(R.id.walk_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String owner = bundle.getString("owner");
            String repo = bundle.getString("repo");
            if (!TextUtils.isEmpty(owner) && !TextUtils.isEmpty(repo)) {
                getReadme(owner, repo);
            }
        }
    }

    private void getReadme(String owner, String repo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IContentsService service = retrofit.create(IContentsService.class);
        Call<Contents> call = service.getReadme(owner, repo);
        call.enqueue(new Callback<Contents>() {
            @Override
            public void onResponse(Call<Contents> call, Response<Contents> response) {
                if (response.isSuccessful()) {
                    Contents contents = response.body();
                    if (contents != null) {
                        byte[] bytes = Base64.decode(contents.getContent(), Base64.DEFAULT);
                        String text;
                        try {
                            text = new String(bytes, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            text = new String(bytes);
                        }
                        try {
                            String html = new Markdown4jProcessor().process(text);
                            xWalkView.load(null, html);
//                            tvReadme.setText(Html.fromHtml(html));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        xWalkView.load(contents.getUrl(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contents> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }
}
