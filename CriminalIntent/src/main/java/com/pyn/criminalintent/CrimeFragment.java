package com.pyn.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText edtTitleField;
    private Button btnDate;
    private CheckBox cboxSolved;

    public CrimeFragment() {
    }

    public static CrimeFragment newInstance(String param1, String param2) {
        CrimeFragment fragment = new CrimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        edtTitleField = v.findViewById(R.id.crime_title);
        edtTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnDate = v.findViewById(R.id.crime_date);
        btnDate.setEnabled(false);

        cboxSolved = v.findViewById(R.id.crime_solved);
        cboxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
