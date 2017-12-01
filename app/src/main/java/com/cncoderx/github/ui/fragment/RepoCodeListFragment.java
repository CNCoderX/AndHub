package com.cncoderx.github.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.sdk.ServiceGenerator;
import com.cncoderx.github.sdk.model.Contents;
import com.cncoderx.github.sdk.service.IContentsService;
import com.cncoderx.github.ui.activity.TextFileReviewActivity;
import com.cncoderx.github.ui.adapter.FileListAdapter;
import com.cncoderx.github.utils.IntentExtra;
import com.cncoderx.github.utils.ListCallback;
import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author cncoderx
 */
public class RepoCodeListFragment extends RecyclerViewFragment implements OnItemClickListener {
    private FileListAdapter mListAdapter = new FileListAdapter();
    private SortedListCallback mCallback = new SortedListCallback(mListAdapter);

    private String mOwner, mRepo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOwner = getArguments().getString(IntentExtra.KEY_OWNER);
        mRepo = getArguments().getString(IntentExtra.KEY_REPO);
        setAdapter(mListAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_files));
        mCallback.setEmptyView(getEmptyView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update("");
    }

    public void update(String path) {
        IContentsService service = ServiceGenerator.create(IContentsService.class);
        Call<List<Contents>> call = service.getContents(mOwner, mRepo, path, null);
        mCallback.queueTo(call, true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        Contents contents = mListAdapter.get(position);
        String type = contents.type;
        if (type != null) {
            if (type.equals(Contents.TYPE_DIRECTION)) {
                sendUpdateBroadcast(contents.path);
            } else if (type.equals(Contents.TYPE_SYMLINK)){
                sendUpdateBroadcast(contents.target);
            } else {
                String url = contents.downloadUrl;
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(getActivity(), TextFileReviewActivity.class);
                    intent.putExtra(IntentExtra.KEY_NAME, contents.name);
                    intent.putExtra(IntentExtra.KEY_PATH, url);
                    startActivity(intent);
                }
            }
        }
    }

    public void sendUpdateBroadcast(String path) {
        ((RepoCodeFragment) getParentFragment()).update(path);
//        Intent intent = new Intent(RepoDetailCodeFragment.ACTION);
//        intent.putExtra(IntentExtra.KEY_PATH, path);
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private static class SortedListCallback extends ListCallback<Contents> {

        SortedListCallback(@NonNull ObjectAdapter<Contents> adapter) {
            super(adapter, false);
        }

        @Override
        public void onResponse(Call<List<Contents>> call, Response<List<Contents>> response) {
            // 按文件类型排列顺序，文件夹在前
            if (response.isSuccessful()) {
                List<Contents> objects = response.body();
                if (objects != null && objects.size() > 0) {
                    Collections.sort(objects, new Comparator<Contents>() {
                        @Override
                        public int compare(Contents o1, Contents o2) {
                            if (o1.type == null ||
                                    o2.type == null)
                                return 0;

                            if (!o1.type.equals(Contents.TYPE_DIRECTION) &&
                                    o2.type.equals(Contents.TYPE_DIRECTION))
                                return 1;

                            return -1;
                        }
                    });
                }
            }
            super.onResponse(call, response);
        }
    }
}
