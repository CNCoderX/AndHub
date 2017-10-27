package com.cncoderx.github.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cncoderx.github.R;
import com.cncoderx.github.preference.SearchOptionsPreference;

/**
 * @author cncoderx
 */

public class SearchOptionsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_search_options, null);
        Spinner spSort = (Spinner) view.findViewById(R.id.sp_search_options_sort);
        Spinner spLang = (Spinner) view.findViewById(R.id.sp_search_options_lang);
        initSortSpinner(view, spSort);
        initLangSpinner(view, spLang);
        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .setTitle("搜索选项").create();
    }

    private void initSortSpinner(View view, Spinner spinner) {
        Context context = getActivity();
        String[] array = getResources().getStringArray(R.array.search_sort_options);
        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_text_layout, array);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new SearchOptionsPreference(parent.getContext()).setSortOption(position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int index = new SearchOptionsPreference(context).getSortOption();
        spinner.setSelection(index);
    }

    private void initLangSpinner(View view, Spinner spinner) {
        Context context = getActivity();
        String[] array = getResources().getStringArray(R.array.search_lang_options);
        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_text_layout, array);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new SearchOptionsPreference(parent.getContext()).setLangOption(position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int index = new SearchOptionsPreference(context).getLangOption();
        spinner.setSelection(index);
    }
}
