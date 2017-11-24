package com.cncoderx.github.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.github.R;
import com.cncoderx.github.utils.IntentExtra;

/**
 * @author cncoderx
 */
public class RepoDetailCodeFragment extends Fragment {
    private RepoDetailCodePathFragment mPathFragment;
    private RepoDetailCodeListFragment mListFragment;

    public static RepoDetailCodeFragment create(String owner, String repo) {
        RepoDetailCodeFragment fragment = new RepoDetailCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentExtra.KEY_OWNER, owner);
        bundle.putString(IntentExtra.KEY_REPO, repo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_detail_code, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mPathFragment = new RepoDetailCodePathFragment();
        mPathFragment.setArguments(new Bundle(arguments));

        mListFragment = new RepoDetailCodeListFragment();
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        IntentFilter filter = new IntentFilter(ACTION);
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
//    }
//
//    public static final String ACTION = RepoDetailCodeFragment.class.getName() + ".ACTION";
//
//    BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String path = intent.getStringExtra(IntentExtra.KEY_PATH);
//            update(path);
//        }
//    };
}
