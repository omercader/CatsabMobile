package com.example.catsabmobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.catsabmobile.databinding.FragmentFirstBinding;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.AuthError;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.listeners.AuthenticateListener;
import oogbox.api.odoo.client.listeners.OdooConnectListener;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui posarem la lògica per a connectar amb Odoo
                String user_id = binding.userId.getText().toString();
                String password = binding.passwordId.getText().toString();

                AuthenticateListener loginCallback = new AuthenticateListener() {
                    @Override
                    public void onLoginSuccess(OdooUser user) {
                        Log.println(Log.INFO,"ODOO","Usuari connectat");
                        OdooClientFactory.getInstance().setUser(user);
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    }

                    @Override
                    public void onLoginFail(AuthError error) {
                        Log.println(Log.ERROR,"ODOO","Authentication error");

                    }
                };

                OdooClientFactory.getInstance().connect(getContext(), "https://catsab.net", new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        Log.println(Log.INFO,"ODOO","Connected to Odoo server");
                    }
                });

                OdooClientFactory.getInstance().getClient().authenticate(user_id,password,"Catsab",loginCallback);




            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}