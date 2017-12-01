package com.cncoderx.github.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.cncoderx.github.R;
import com.cncoderx.github.accounts.GitAccount;
import com.cncoderx.github.accounts.GitAccountManager;
import com.cncoderx.github.ui.adapter.AccountDropListAdapter;
import com.cncoderx.recyclerviewhelper.RecyclerViewHelper;

/**
 * @author cncoderx
 */
public class AccountListActivity extends RecyclerViewActivity {
    private AccountDropListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GitAccount[] accounts = GitAccountManager.getGitAccounts();
        mAdapter = new AccountDropListAdapter(accounts);
        setAdapter(mAdapter);

        View header = LayoutInflater.from(this).inflate(
                R.layout.item_space_layout, getRecyclerView(), false);
        RecyclerViewHelper.addHeaderView(getRecyclerView(), header);

        View footer = LayoutInflater.from(this).inflate(
                R.layout.item_space_layout, getRecyclerView(), false);
        RecyclerViewHelper.addFooterView(getRecyclerView(), footer);
    }
}
