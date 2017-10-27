package com.cncoderx.github.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.entites.Contents;
import com.cncoderx.github.entites.Repository;
import com.cncoderx.github.netservice.IContentsService;
import com.cncoderx.github.netservice.IRepositoryService;
import com.cncoderx.github.ui.fragment.ReadmeFragment;
import com.cncoderx.github.utils.URLUtils;

import org.markdown4j.Markdown4jProcessor;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoDetailActivity extends BaseActivity {
    @BindView(R.id.tv_repo_detail_watched_count)
    TextView tvWatched;

    @BindView(R.id.tv_repo_detail_stared_count)
    TextView tvStared;

    @BindView(R.id.tv_repo_detail_forked_count)
    TextView tvForked;

    @BindView(R.id.wv_repo_detail_readme)
    XWalkView wvReadme;

    @BindView(R.id.tv_repo_detail_desc)
    TextView tvDesc;

    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        ButterKnife.bind(this);

        String owner = getIntent().getStringExtra("owner");
        String repo = getIntent().getStringExtra("repo");
        if (!TextUtils.isEmpty(owner) && !TextUtils.isEmpty(repo)) {
            getRepository(owner, repo);
            getReadme(owner, repo);
//            ReadmeFragment fragment = ReadmeFragment.create(owner, repo);
//            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
//            fm.replace(R.id.container, fragment);
//            fm.commit();
        }
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    private void getRepository(String owner, String repo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRepositoryService service = retrofit.create(IRepositoryService.class);
        Call<Repository> call = service.getRepository(owner, repo);
        call.enqueue(new Callback<Repository>() {
            @Override
            public void onResponse(Call<Repository> call, Response<Repository> response) {
                if (response.isSuccessful()) {
                    mRepository = response.body();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<Repository> call, Throwable t) {

            }
        });
    }

    private void updateUI() {
        if (mRepository == null)
            return;

        tvWatched.setText(Integer.toString(mRepository.getWatcherCount()));
        tvStared.setText(Integer.toString(mRepository.getStarCount()));
        tvForked.setText(Integer.toString(mRepository.getForkCount()));
        if (!TextUtils.isEmpty(mRepository.getDescription())) {
            tvDesc.setText(Html.fromHtml(mRepository.getDescription()));
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
                            wvReadme.load(null, html);
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
        if (wvReadme != null) {
            wvReadme.pauseTimers();
            wvReadme.onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wvReadme != null) {
            wvReadme.resumeTimers();
            wvReadme.onShow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wvReadme != null) {
            wvReadme.onDestroy();
        }
    }
}
