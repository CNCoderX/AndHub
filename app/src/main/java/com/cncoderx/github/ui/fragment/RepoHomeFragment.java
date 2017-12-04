package com.cncoderx.github.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Repository;
import com.cncoderx.github.sdk.service.IContentsService;
import com.cncoderx.github.sdk.service.IRepositoryService;
import com.cncoderx.github.utils.CallbackFinal;
import com.cncoderx.github.utils.IntentExtra;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author cncoderx
 */
public class RepoHomeFragment extends Fragment {
    @BindView(R.id.tv_repo_detail_watched_count)
    TextView tvWatched;

    @BindView(R.id.tv_repo_detail_stared_count)
    TextView tvStared;

    @BindView(R.id.tv_repo_detail_forked_count)
    TextView tvForked;

    @BindView(R.id.wv_repo_detail_readme)
    WebView wvReadme;

    @BindView(R.id.tv_repo_detail_desc)
    TextView tvDesc;

    private String mOwner, mRepo;

    public static RepoHomeFragment create(String owner, String repo) {
        RepoHomeFragment fragment = new RepoHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_OWNER, owner);
        bundle.putString(IntentExtra.KEY_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_detail_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mOwner = getArguments().getString(IntentExtra.KEY_OWNER);
        mRepo = getArguments().getString(IntentExtra.KEY_REPO);

//        wvReadme.getSettings().setJavaScriptEnabled(true);
//        wvReadme.addJavascriptInterface(this, "Readme");
//        wvReadme.getSettings().setBlockNetworkImage(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            wvReadme.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
        getReadme();
    }

    private void updateUI() {
        IRepositoryService service = ServiceGenerator.create(IRepositoryService.class);
        Call<Repository> call = service.getRepository(mOwner, mRepo);
        call.enqueue(new CallbackFinal<Repository>() {
            @Override
            public void onSuccess(Repository repository) {
                tvWatched.setText(Integer.toString(repository.subscribersCount));
                tvStared.setText(Integer.toString(repository.starCount));
                tvForked.setText(Integer.toString(repository.forkCount));
                if (!TextUtils.isEmpty(repository.description)) {
                    tvDesc.setText(Html.fromHtml(repository.description));
                }
            }
        });
    }

    private static final String PAGE_START = "<!DOCTYPE html><html lang=\"en\"> <head> <title></title>" +
            "<meta charset=\"UTF-8\"> " +
            "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
            "<link href=\"markdown.css\" rel=\"stylesheet\"> </head> <body>";

    private static final String PAGE_END = "</body></html>";

    private void getReadme() {
        IContentsService service = ServiceGenerator.create(IContentsService.class);
        Call<ResponseBody> call = service.getReadme(mOwner, mRepo, null);
        call.enqueue(new CallbackFinal<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody body) {
                try {
                    String html = PAGE_START + body.string() + PAGE_END;
                    wvReadme.loadDataWithBaseURL("file:///android_asset/",
                            html, "text/html", "utf-8", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
