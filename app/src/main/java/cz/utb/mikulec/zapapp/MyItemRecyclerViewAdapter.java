package cz.utb.mikulec.zapapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.utb.mikulec.zapapp.HistoryFragment.OnListFragmentInteractionListener;
//import cz.utb.mikulec.zapapp.dummy.DummyContent.DummyItem;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Firma> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Firma> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIcView.setText(String.valueOf(holder.mItem.getIC()));
        holder.mNazevView.setText(holder.mItem.getNazev());
        holder.mMestoView.setText(holder.mItem.getMesto());

        Log.i("IČ:", String.valueOf(holder.mItem.getIC()));
        Log.i("Město:", String.valueOf(holder.mItem.getMesto()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIcView;
        public final TextView mNazevView;
        public final TextView mMestoView;
        public Firma mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIcView = (TextView) view.findViewById(R.id.history_ic);
            mNazevView = (TextView) view.findViewById(R.id.history_nazev);
            mMestoView = (TextView) view.findViewById(R.id.history_mesto);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNazevView.getText() + "'";
        }
    }
}
