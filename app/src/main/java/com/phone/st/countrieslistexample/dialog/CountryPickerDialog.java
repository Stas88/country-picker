package com.phone.st.countrieslistexample.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phone.st.countrieslistexampl.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by stassikorskyi on 13.05.15.
 */
public class CountryPickerDialog extends DialogFragment {

    public static final String TAG = CountryPickerDialog.class.getSimpleName();

    private static final String KEY_TITLE = "title";

    private AdapterView.OnItemClickListener onItemClickListener;
    private String onClickListener;
    private static Map<String,String> countriesMap;

    public CountryPickerDialog() {
    }

    public static CountryPickerDialog newInstance(String title, Map<String,String> coountriesMap) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        countriesMap = coountriesMap;
        CountryPickerDialog fragment = new CountryPickerDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public  CountryPickerDialog setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener1) {
        onItemClickListener = onItemClickListener1;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AlertDialog.Builder alertDialogBuilder = null;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(bundle.getString(KEY_TITLE));
        manageView(alertDialogBuilder);
        return alertDialogBuilder.create();
    }

    private void manageView(final AlertDialog.Builder alertDialogBuilder) {
        View rootView = getActivity().getLayoutInflater()
                .inflate(R.layout.country_picker_dialog, null);
        alertDialogBuilder.setView(rootView);
        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(alertDialogBuilder.getContext(),
                android.R.layout.simple_list_item_1, new ArrayList(countriesMap.keySet())));
        listView.setOnItemClickListener(onItemClickListener);
    }
}
