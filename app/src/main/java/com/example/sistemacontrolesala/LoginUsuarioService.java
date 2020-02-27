package com.example.sistemacontrolesala;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginUsuarioService extends AsyncTask<String, Void, String> {
    ProgressDialog mDialog;
    Context context;

   /* protected void onPreExecute() {
        mDialog = ProgressDialog.show(context, "Aguarde", "Processando...", false, false);
    }*/

    @Override
    protected String doInBackground(String... strings) {
        String urlWS = "http://172.30.248.117:8080/ReservaDeSala/rest/usuario/login/";

        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlWS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", "secret");
            conn.setRequestProperty("email", strings[0]);
            conn.setRequestProperty("password", strings[1]);
            conn.setConnectTimeout(2000);

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Servidor nao responde";
        }
        return result.toString();
    }
}