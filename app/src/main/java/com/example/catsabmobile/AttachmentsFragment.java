package com.example.catsabmobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

public class AttachmentsFragment extends Fragment {

    private String fraisId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attachments, container, false);

        fraisId = FraisDetailFragmentArgs.fromBundle(getArguments()).getFraisId();
        int intFraisId = Double.valueOf(fraisId).intValue();

        RecyclerView recyclerView = view.findViewById(R.id.attachmentsRecyclerView);

        OdooClient client = OdooClientFactory.getInstance().getClient();
        //Recuperem la llista de frais per a l'usuari autenticat
        ODomain domain = new ODomain();
        domain.add("res_id", "=", String.valueOf(intFraisId));

        OdooFields fields = new OdooFields();
        fields.addAll("id","name");

        int offset = 0;
        int limit = 80;

        String sorting = "name DESC";

        client.searchRead("ir.attachment", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {

                OdooRecord[] records = result.getRecords();
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(new AttachmentsCustomAdapter(records));


            }
        });


        return view;
    }


}
