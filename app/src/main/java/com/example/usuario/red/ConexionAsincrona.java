package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

public class ConexionAsincrona extends AppCompatActivity implements View.OnClickListener {

    EditText direccion;
    RadioButton radioJava, radioApache;
    Button conectar;
    WebView web;
    TextView tiempo;


    public static final String JAVA = "JAVA";
    public static final String APACHE = "APACHE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_asincrona);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conectar:
                establecerConexion();
                break;
        }
    }

    private void establecerConexion() {
        String url = direccion.getText().toString();
        String modoConexion = "";

        if (radioJava.isChecked()) {
            modoConexion = JAVA;
        } else {
            modoConexion = APACHE;
        }

        new ConectarAsincrona().execute(url, modoConexion);

    }


    class ConectarAsincrona extends AsyncTask<String, Integer, Resultado> {

        private ProgressDialog progreso;
        private Resultado resultado;

        public ConectarAsincrona() {
            resultado = new Resultado();
        }

        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(ConexionAsincrona.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando...");
            progreso.setCancelable(true
            );
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    ConectarAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }

        @Override
        protected Resultado doInBackground(String... cadena) {

            try {
                if (cadena[1].equals(JAVA))
                    resultado = Conexiones.conectarJava(cadena[0]);
                if (cadena[1].equals(APACHE))
                    resultado = Conexiones.conectarApache(cadena[0]);

            } catch (Exception e) {
                resultado = null;
                cancel(true);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Resultado result) {
            progreso.setMessage("Conexion exitosa");
            if (resultado.getCodigo()) {
                web.loadDataWithBaseURL(null, resultado.getContenido(), "text/html", "UTF-8", null);
                tiempo.setText("Completado");
            } else {
                web.loadDataWithBaseURL(null, resultado.getMensaje(), "text/html", "UTF-8", null);
                tiempo.setText("Completado");
            }
            progreso.dismiss();
        }

        @Override
        protected void onCancelled() {
            progreso.setMessage("Conexion cancelada");
            progreso.dismiss();
        }
    }
}