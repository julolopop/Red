package com.example.usuario.red;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.usuario.red.pojo.Conexiones;
import com.example.usuario.red.pojo.Resultado;
import com.example.usuario.red.pojo.TareaAsincrona;

public class ConexionAsincrona extends AppCompatActivity implements View.OnClickListener {

    EditText direccion;
    RadioButton radioJava,radioApache;
    Button conectar;
    WebView web;
    TextView tiempo;


    public static  final String JAVA = "JAVA";
    public static  final String APACHE = "APACHE";
    long inicio ,fin;
    String tipo;

    TareaAsincrona tareaAsincrona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_asincrona);
    }
    private void iniciar() {
        direccion = (EditText) findViewById(R.id.edt_URL);
        radioJava = (RadioButton) findViewById(R.id.rdb_java);
        radioApache = (RadioButton) findViewById(R.id.rdb_apache);
        conectar = (Button) findViewById(R.id.btn_conectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txv_Resultado);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    @Override
    public void onClick(View v) {
        String texto = direccion.getText().toString();
        long inicio, fin;
        Resultado resultado;
        if (v == conectar) {
            inicio = System.currentTimeMillis();
            if (radioJava.isChecked())
                tipo = JAVA;
            else
                tipo = APACHE;


            tareaAsincrona = new TareaAsincrona(this);
            tareaAsincrona.execute(tipo,texto);
            tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
