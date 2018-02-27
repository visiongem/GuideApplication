package com.pyn.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by pengyanni on 2018/2/9.
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
//    private int currentPosition = -1;

    public enum ITEM_TYPE {
        ITEM_TYPE_STANDARD,
        ITEM_TYPE_POLICE
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
//            if (currentPosition != -1) {
//                mAdapter.notifyItemChanged(currentPosition);
//            } else {
            mAdapter.notifyDataSetChanged();
//            }
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDate;
        private Crime mCrime;
        private ImageView imgSolved;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            tvTitle = itemView.findViewById(R.id.crime_title);
            tvDate = itemView.findViewById(R.id.crime_date);
            imgSolved = itemView.findViewById(R.id.crime_solved);

        }

        public void bind(Crime crime) {
            mCrime = crime;
            tvTitle.setText(mCrime.getTitle());
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
            tvDate.setText(dateFormat.format(mCrime.getDate()));
            imgSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
//            currentPosition = this.getAdapterPosition();
            startActivity(intent);
        }
    }

    private class CrimePoliceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDate;
        private Button btnPolice;
        private Crime mCrime;
        private ImageView imgSolved;

        public CrimePoliceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            tvTitle = itemView.findViewById(R.id.crime_title);
            tvDate = itemView.findViewById(R.id.crime_date);
            btnPolice = itemView.findViewById(R.id.crime_police);
            imgSolved = itemView.findViewById(R.id.crime_solved);
            btnPolice.setVisibility(View.VISIBLE);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            tvTitle.setText(mCrime.getTitle());
            tvDate.setText(mCrime.getDate().toString());
            imgSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
//            currentPosition = this.getAdapterPosition();
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == ITEM_TYPE.ITEM_TYPE_STANDARD.ordinal()) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new CrimeHolder(layoutInflater, parent);
            } else if (viewType == ITEM_TYPE.ITEM_TYPE_POLICE.ordinal()) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new CrimePoliceHolder(layoutInflater, parent);
            }
            return null;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            } else if (holder instanceof CrimePoliceHolder) {
                ((CrimePoliceHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemViewType(int position) {

            if (mCrimes.get(position).isRequiresPolice()) {
                return ITEM_TYPE.ITEM_TYPE_POLICE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_TYPE_STANDARD.ordinal();
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
