package com.example.catsabmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

public class FraisFragment extends Fragment {



    public FraisFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_frais, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fraisRecyclerView);

        OdooClient client = OdooClientFactory.getInstance().getClient();
        //Recuperem la llista de frais per a l'usuari autenticat
        ODomain domain = new ODomain();
        domain.add("name", "like", "a%");

        OdooFields fields = new OdooFields();
        fields.addAll("id","date","name","total_amount");

        int offset = 0;
        int limit = 80;

        String sorting = "name DESC";

        client.searchRead("hr.expense", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();

                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(new FraisCustomAdapter(records, new FraisCustomAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(OdooRecord item) {
                        Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                        Log.println(Log.INFO,"FRAIS","Item clickat!!");
                        FraisFragmentDirections.FraisDetailFragmentAction action = FraisFragmentDirections.fraisDetailFragmentAction().setFraisId(item.getString("id"));
                        Navigation.findNavController(view).navigate(action);

                    }
                }));

                for (OdooRecord record : records) {
                    Log.e(">>", record.getString("name"));
                }
            }
        });


        return view;
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}