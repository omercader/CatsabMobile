package com.example.catsabmobile;

import android.content.Context;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.OdooUser;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class OdooClientFactory {
    private static final OdooClientFactory instance = new OdooClientFactory();

    private OdooClient client;
    private OdooUser user;

    private OdooClientFactory(){}
    public static OdooClientFactory getInstance(){
        return instance;
    }

    public void connect(Context ctxt, String host, OdooConnectListener connListener){

        OdooClient.Builder bld = new OdooClient.Builder(ctxt);
        bld.setHost(host);
        bld.setConnectListener(connListener);
        this.client = bld.build();
    }

    public OdooClient getClient(){
        return this.client;
    }
    public OdooUser getUser(){ return this.user; }
    public void setUser(OdooUser user){ this.user = user; }
}
