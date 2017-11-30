package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

import static android.widget.Toast.LENGTH_LONG;

public class SubidaFicheroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText direccion;
    TextView resultado;

    Button conectar;
    WebView web;
    TextView tiempo;
    RequestQueue mRequestQueue;
    long inicio, fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida_fichero);
        iniciar();
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.edt_URL);
        resultado = (TextView) findViewById(R.id.txv_Resultado);
        conectar = (Button) findViewById(R.id.btn_conectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txv_Resultado);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

    }

    public final static String WEB = "http://192.168.2.171/upload.php";

    private void subida() {
        final AsyncHttpClient RestClient = new AsyncHttpClient();
        String fichero = direccion.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(SubidaFicheroActivity.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        params.add("usuario","123");
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            resultado.setText("Error en el fichero: " + e.getMessage());
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    //progreso.setCancelable(false);
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progreso.setMessage("Error de subida del fichero");

                    progreso.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    resultado.setText(responseString);
                    progreso.dismiss();
                }
            });
    }


    @Override
    public void onClick(View v) {
        if(v == conectar)
            subida();
    }
}
