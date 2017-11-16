package com.example.usuario.red;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class ConexionDescarga extends AppCompatActivity implements View.OnClickListener {


    EditText direccion;
    Button conectar;
    WebView web;
    TextView tiempo;
    long inicio, fin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_descarga);
        iniciar();
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.edt_URL);
        conectar = (Button) findViewById(R.id.btn_conectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txv_Resultado);
    }


    @Override
    public void onClick(View v) {

    }
}




