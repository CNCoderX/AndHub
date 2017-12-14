package com.cncoderx.andhub.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.databinding.RepoDetailHomeFragmentBinding;
import com.cncoderx.andhub.model.Repository;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IContentsService;
import com.cncoderx.andhub.okhttp.service.IRepositoryService;
import com.cncoderx.andhub.utils.IntentExtra;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author cncoderx
 */
public class RepoHomeFragment extends BaseFragment {
    private RepoDetailHomeFragmentBinding mBinding;
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.repo_detail_home_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mOwner = getArguments().getString(IntentExtra.KEY_OWNER);
        mRepo = getArguments().getString(IntentExtra.KEY_REPO);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newServiceCall();
    }

    private void newServiceCall() {
        ServiceGenerator.create(IRepositoryService.class)
            .getRepository(mOwner, mRepo)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(new Consumer<Repository>() {
                @Override
                public void accept(Repository repository) throws Exception {
                    mBinding.setRepository(repository);
                }
            })
            .observeOn(Schedulers.io())
            .flatMap(new Function<Repository, Single<ResponseBody>>() {
                @Override
                public Single<ResponseBody> apply(Repository repository) throws Exception {
                    return ServiceGenerator.create(IContentsService.class).getReadme(mOwner, mRepo, null);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ResponseBody>() {
                @Override
                public void accept(ResponseBody body) throws Exception {
                    try {
                        String html = PAGE_START + body.string() + PAGE_END;
                        mBinding.webView.loadDataWithBaseURL("file:///android_asset/",
                                html, "text/html", "utf-8", null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private static final String PAGE_START = "<!DOCTYPE html><html lang=\"en\"> <head> <title></title>" +
            "<meta charset=\"UTF-8\"> " +
            "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
            "<link href=\"markdown.css\" rel=\"stylesheet\"> </head> <body>";

    private static final String PAGE_END = "</body></html>";
}
