package com.example.catsabmobile;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oogbox.api.odoo.client.helper.data.OdooRecord;

public class FraisCustomAdapter extends RecyclerView.Adapter<FraisCustomAdapter.ViewHolder> {

    private OdooRecord[] localDataSet;
    private OnItemClickListener localListener;

    public interface OnItemClickListener {
        void onItemClick(OdooRecord item);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView fraisNameTView;
        private final TextView fraisValueTView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fraisNameTView = (TextView) itemView.findViewById(R.id.itemName);
            fraisValueTView = (TextView) itemView.findViewById(R.id.itemValue);

        }

        public TextView getFraisNameTView(){
            return fraisNameTView;
        }

        public TextView getFraisValueTView(){
            return fraisValueTView;
        }
    }

    public FraisCustomAdapter(OdooRecord[] dataSet, OnItemClickListener listener){
        localDataSet = dataSet;
        localListener = listener;
    }

    @NonNull
    @Override
    public FraisCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FraisCustomAdapter.ViewHolder holder, int position) {

        holder.getFraisNameTView().setText(localDataSet[position].getString("name"));
        holder.getFraisValueTView().setText(localDataSet[position].getString("total_amount"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localListener.onItemClick(localDataSet[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }


}
