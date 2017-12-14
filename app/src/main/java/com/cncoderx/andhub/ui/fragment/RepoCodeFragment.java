package com.cncoderx.andhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.andhub.R;
import com.cncoderx.andhub.utils.IntentExtra;

/**
 * @author cncoderx
 */
public class RepoCodeFragment extends BaseFragment {
    private RepoCodePathFragment mPathFragment;
    private RepoCodeListFragment mListFragment;

    public static RepoCodeFragment create(String owner, String repo) {
        RepoCodeFragment fragment = new RepoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_OWNER, owner);
        bundle.putString(IntentExtra.KEY_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.repo_detail_code_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mPathFragment = new RepoCodePathFragment();
        mPathFragment.setArguments(new Bundle(arguments));

        mListFragment = new RepoCodeListFragment();
        mListFragment.setArguments(new Bundle(arguments));

        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .add(R.id.code_container, mPathFragment)
                .add(R.id.code_container2, mListFragment)
                .commit();
    }

    public void update(String path) {
        mPathFragment.update(path);
        mListFragment.update(path);
    }
}
