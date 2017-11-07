package com.example.usuario.red;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.usuario.red.pojo.Conexiones;
import com.example.usuario.red.pojo.Resultado;

public class ConexionHTTP extends AppCompatActivity implements View.OnClickListener {

    EditText direccion;
    RadioButton radioJava,radioApache;
    Button conectar;
    WebView web;
    TextView tiempo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_http);
        iniciar();
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
                resultado = Conexiones.conectarJava(texto);
            else
                resultado = Conexiones.conectarApache(texto);
            fin = System.currentTimeMillis();
            if (resultado.getCodigo())
                web.loadDataWithBaseURL(null, resultado.getContenido(),"text/html", "UTF-8", null);
            else
                web.loadDataWithBaseURL(null, resultado.getMensaje(),"text/html", "UTF-8", null);
            tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
