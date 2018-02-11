package com.pyn.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by pengyanni on 2018/2/9.
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

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

    /**
     * 更新UI
     */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDate;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            tvTitle = itemView.findViewById(R.id.crime_title);
            tvDate = itemView.findViewById(R.id.crime_date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            tvTitle.setText(mCrime.getTitle());
            tvDate.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class CrimePoliceHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDate;
        private Button btnPolice;
        private Crime mCrime;

        public CrimePoliceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            tvTitle = itemView.findViewById(R.id.crime_title);
            tvDate = itemView.findViewById(R.id.crime_date);
            btnPolice = itemView.findViewById(R.id.crime_police);
            btnPolice.setVisibility(View.VISIBLE);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            tvTitle.setText(mCrime.getTitle());
            tvDate.setText(mCrime.getDate().toString());
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
