package com.cncoderx.andhub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cncoderx.andhub.BR;
import com.cncoderx.andhub.R;
import com.cncoderx.andhub.model.File;
import com.cncoderx.andhub.okhttp.ServiceGenerator;
import com.cncoderx.andhub.okhttp.service.IContentsService;
import com.cncoderx.andhub.ui.activity.CodeReviewActivity;
import com.cncoderx.andhub.ui.adapter.BindingAdapter;
import com.cncoderx.andhub.utils.IntentExtra;
import com.cncoderx.andhub.utils.Pagination;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * @author cncoderx
 */
public class RepoCodeListFragment extends RecyclerViewFragment implements OnItemClickListener {
    private BindingAdapter<File> mAdapter = new BindingAdapter<>(R.layout.item_file_list, BR.file);
    private Pagination<File> mPagination = new Pagination<>(mAdapter, new Pagination.Factory<File>() {
        IContentsService mService = ServiceGenerator.create(IContentsService.class);

        @Override
        public Single<List<File>> create(int pageIndex, int pageSize) {
            return mService.getContents(mOwner, mRepo, mPath, null, pageIndex, Integer.MAX_VALUE)
                            .map(new Function<List<File>, List<File>>() {
                                @Override
                                public List<File> apply(List<File> files) throws Exception {
                                    if (files.size() > 0) {
                                        Collections.sort(files, new Comparator<File>() {
                                            @Override
                                            public int compare(File o1, File o2) {
                                                if (o1.type == null ||
                                                        o2.type == null)
                                                    return 0;

                                                if ((o1.type != File.Type.dir) &&
                                                        (o2.type == File.Type.dir))
                                                    return 1;

                                                return -1;
                                            }
                                        });
                                    }
                                    return files;
                                }
                            });
        }
    });

    private String mOwner, mRepo, mPath;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOwner = getArguments().getString(IntentExtra.KEY_OWNER);
        mRepo = getArguments().getString(IntentExtra.KEY_REPO);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        setEmptyText(getString(R.string.no_files));
        mPagination.bind(getEmptyView());
        mPagination.setTransformer(this.<List<File>>bindToLifecycle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update("");
    }

    public void update(String path) {
        mPath = path;
        mPagination.bind(getRefreshLayout());
        mPagination.newPage();
        getRefreshLayout().setRefreshing(true);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {
        File file = mAdapter.get(position);
        File.Type type = file.type;
        if (type == File.Type.dir) {
            sendUpdateBroadcast(file.path);
        } else if (type == File.Type.symlink){
            sendUpdateBroadcast(file.target);
        } else {
            String url = file.downloadUrl;
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent(getActivity(), CodeReviewActivity.class);
                intent.putExtra(IntentExtra.KEY_NAME, file.name);
                intent.putExtra(IntentExtra.KEY_PATH, url);
                startActivity(intent);
            }
        }
    }

    public void sendUpdateBroadcast(String path) {
        ((RepoCodeFragment) getParentFragment()).update(path);
    }

}
