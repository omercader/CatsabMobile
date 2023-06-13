package com.example.catsabmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oogbox.api.odoo.client.helper.data.OdooRecord;

public class AttachmentsCustomAdapter extends RecyclerView.Adapter<AttachmentsCustomAdapter.ViewHolder>{

    private OdooRecord[] localDataSet;


    public AttachmentsCustomAdapter(OdooRecord[] dataset){

        localDataSet = dataset;
    }

    @NonNull
    @Override
    public AttachmentsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachments_item_row, parent, false);


        return new AttachmentsCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsCustomAdapter.ViewHolder holder, int position) {

        holder.getFilenameView().setText(localDataSet[position].getString("name"));

    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView filenameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filenameView = itemView.findViewById(R.id.filenameView);

        }

        public TextView getFilenameView(){ return filenameView; }
    }
}
