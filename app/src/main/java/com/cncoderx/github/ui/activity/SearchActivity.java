package com.cncoderx.github.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.github.R;
import com.cncoderx.github.entites.Repositories;
import com.cncoderx.github.entites.Repository;
import com.cncoderx.github.netservice.ISearchService;
import com.cncoderx.github.preference.SearchOptionsPreference;
import com.cncoderx.github.ui.adapter.RepositoryAdapter;
import com.cncoderx.github.ui.dialog.SearchOptionsDialog;
import com.cncoderx.github.utils.URLUtils;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends BaseActivity implements OnLoadMoreListener {
    @BindView(R.id.iv_search_header_icon)
    ImageView ivHeaderIcon;

    @BindView(R.id.iv_search_button)
    ImageView ivSearchBtn;

    @BindView(R.id.et_search_key)
    EditText etSearchKey;

    @BindView(R.id.rv_search_list)
    RecyclerView rvSearchList;

    @BindView(R.id.ll_search_empty_layout)
    LinearLayout llEmptyView;

    @BindView(R.id.tv_search_empty_text)
    TextView tvEmptyView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private RepositoryAdapter mAdapter;

    private class RequestParam {
        String key;
        String sort;
        String order;
        int page = 1;
        int pageSize = 30;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mAdapter = new RepositoryAdapter();

        RecyclerViewHelper.setLinearLayout(rvSearchList);
        RecyclerViewHelper.setAdapter(rvSearchList, mAdapter);
        RecyclerViewHelper.setDivider(rvSearchList,
                ContextCompat.getDrawable(this, R.drawable.list_divider),
                getResources().getDimensionPixelOffset(R.dimen.divider_height));
        RecyclerViewHelper.setLoadMoreListener(rvSearchList, this);
        RecyclerViewHelper.setOnItemClickListener(rvSearchList, new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                Repositories.Item item = mAdapter.get(position);
                String[] fullName = item.getFullName().split("/");
                Intent intent = new Intent(SearchActivity.this, RepoDetailActivity.class);
                intent.putExtra("owner", fullName[0]);
                intent.putExtra("repo", fullName[1]);
                startActivity(intent);
            }
        });

        refreshLayout.setEnabled(false);
    }

    @OnClick(R.id.iv_search_button)
    public void onSearch(View view) {
        String key = etSearchKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            etSearchKey.setText("");
            etSearchKey.requestFocus();
            return;
        }
        final RequestParam param = new RequestParam();
        String keyOption = getKeyOption();
        String[] sortAndOrder = getSortAndOrder();
        param.key = key + keyOption;
        param.sort = sortAndOrder[0];
        param.order = sortAndOrder[1];
        search(param, null);
        rvSearchList.setTag(param);
        refreshLayout.setRefreshing(true);

        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private String[] getSortAndOrder() {
        int option = new SearchOptionsPreference(this).getSortOption();
        switch (option) {
            case 1:
                return new String[]{"stars", "desc"};
            case 2:
                return new String[]{"stars", "asc"};
            case 3:
                return new String[]{"forks", "desc"};
            case 4:
                return new String[]{"forks", "asc"};
            case 5:
                return new String[]{"updated", "desc"};
            case 6:
                return new String[]{"updated", "asc"};
            default:
                return new String[]{"", ""};
        }
    }

    private String getKeyOption() {
        int option = new SearchOptionsPreference(this).getLangOption();
        String[] languages = getResources().getStringArray(R.array.search_lang_options);
        if (option < 0
                || languages[option].equals("Any Language")
                || languages[option].equals("Other Language")) {
            return "";
        } else {
            return String.format(Locale.US, "+language:%s", languages[option]);
        }
    }

    @OnClick(R.id.iv_search_menu)
    public void onMenuClick(View view) {
        new SearchOptionsDialog().show(getFragmentManager(), "Options");
    }

    @Override
    public void load(RecyclerView recyclerView, ILoadingView view) {
        RequestParam param = (RequestParam) recyclerView.getTag();
        search(param, view);
    }

    private void search(final RequestParam param, @Nullable final ILoadingView view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ISearchService service = retrofit.create(ISearchService.class);
        Call<Repositories> call = service.search(param.key, param.sort,
                param.order, param.page, param.pageSize);
        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                boolean isCompleted = false;
                if (response.isSuccessful()) {
                    Repositories repositories = response.body();
                    if (repositories != null) {
                        if (view == null && mAdapter.size() > 0) {
                            mAdapter.clear();
                        }
                        List<Repositories.Item> data = repositories.getItems();
                        mAdapter.addAll(data);
                        isCompleted = repositories.getTotalCount() == mAdapter.size();
                    }
                    param.page++;
                }
                if (view != null) {
                    if (isCompleted) {
                        view.end();
                    } else {
                        view.hidden();
                    }
                }
                onPostResponse();
            }

            @Override
            public void onFailure(Call<Repositories> call, Throwable t) {
                t.printStackTrace();
                if (view != null) {
                    view.hidden();
                }
                onPostResponse();
            }
        });
        if (view != null) {
            view.show();
        }
    }

    private void onPostResponse() {
        String key = etSearchKey.getText().toString().trim();
        if (mAdapter.size() == 0) {
            tvEmptyView.setText(getString(R.string.search_no_result, key));
            llEmptyView.setVisibility(View.VISIBLE);
        } else {
            llEmptyView.setVisibility(View.GONE);
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
