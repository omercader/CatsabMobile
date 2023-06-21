package com.example.catsabmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

public class FraisDetailFragment extends Fragment {


    private TextView nameView;
    private TextView prodIdView;
    private TextView unitAmView;
    private TextView quantityView;
    private TextView dateView;
    private ImageView addAttachmentIconView;
    private ImageView attachmentIconView;

    private String fraisId;
    private int attachments;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frais_detail, container, false);


        nameView = view.findViewById(R.id.fraisDescription);
        prodIdView = view.findViewById(R.id.fraisProductId);
        unitAmView = view.findViewById(R.id.fraisUnitAmount);
        quantityView = view.findViewById(R.id.fraisQuantity);
        dateView = view.findViewById(R.id.fraisDate);
        addAttachmentIconView = view.findViewById(R.id.addAttachmentIconView);
        attachmentIconView = view.findViewById(R.id.attachmentsIconView);

        Button saveButton =  ((Button)view.findViewById(R.id.frais_save_butt));
        Button cancelButton = ((Button)view.findViewById(R.id.frais_cancel_butt));

        ImageView editIconView = view.findViewById(R.id.frais_edit_icon);
        editIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveButton.setVisibility(View.VISIBLE);
               cancelButton.setVisibility(View.VISIBLE);
               editIconView.setVisibility(View.INVISIBLE);
               nameView.setEnabled(true);
               prodIdView.setEnabled(true);
               unitAmView.setEnabled(true);
               quantityView.setEnabled(true);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = nameView.getText().toString();
                String unit_price = unitAmView.getText().toString();
                String quantity = quantityView.getText().toString();

                saveRecord(description,unit_price,quantity);


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editIconView.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                nameView.setEnabled(false);
                prodIdView.setEnabled(false);
                unitAmView.setEnabled(false);
                quantityView.setEnabled(false);
            }
        });

        addAttachmentIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add attachment camera activity
                Intent intent = new Intent(getActivity(),CameraActivity.class);
                intent.putExtra("frais_id",fraisId);
                startActivity(intent);

            }
        });


        return view;
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        fraisId = FraisDetailFragmentArgs.fromBundle(getArguments()).getFraisId();
        readRecord(fraisId);

        attachmentIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FraisDetailFragmentDirections.AttachmentsListAction action = FraisDetailFragmentDirections.attachmentsListAction().setFraisId(fraisId);
                Navigation.findNavController(view).navigate(action);
            }
        });

    }



    public void readRecord(String fieldId){

        OdooClient client = OdooClientFactory.getInstance().getClient();

        List<Integer> ids = Arrays.asList(Double.valueOf(fraisId).intValue());
        List<String> fields = Arrays.asList("id", "name","product_id","unit_amount","quantity","date","attachment_number");

        client.read("hr.expense", ids, fields, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();

                for(OdooRecord record: records) {

                    nameView.setText(record.getString("name"));
                    prodIdView.setText((String)record.getArray("product_id").get(1));
                    unitAmView.setText(Integer.toString(record.getInt("unit_amount")));
                    quantityView.setText(Integer.toString(record.getInt("quantity")));
                    dateView.setText(record.getString("date"));

                    //Attachments
                    attachments = record.getInt("attachment_number");
                    if(attachments > 0){
                        attachmentIconView.setVisibility(View.VISIBLE);
                    }

                    Log.v("Name:", record.getString("name"));
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.println(Log.ERROR,"ODOO","Error recovering read info");
                return super.onError(error);
            }
        });

    }



    public void saveRecord(String name, String unit_amnt, String quantity){

        OdooClient client = OdooClientFactory.getInstance().getClient();

        Integer[] ids = {Double.valueOf(fraisId).intValue()};
        OdooValues values = new OdooValues();
        values.put("name",name);
        values.put("unit_amount",Integer.valueOf(unit_amnt).intValue());
        values.put("quantity",Integer.valueOf(quantity).intValue());


        client.write("hr.expense", ids, values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {

                Toast.makeText(getContext(), "Record updated successfully", Toast.LENGTH_LONG).show();
                Log.println(Log.INFO,"ODOO","Record updated successfully");
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Log.println(Log.ERROR,"ODOO","Error recovering read info");
                return super.onError(error);
            }
        });

    }
}
