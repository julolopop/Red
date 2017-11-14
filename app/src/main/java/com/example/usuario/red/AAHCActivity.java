package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.entity.mime.Header;

public class AAHCActivity extends AppCompatActivity implements View.OnClickListener {


    EditText direccion;
    Button conectar;
    WebView web;
    TextView tiempo;
    long inicio,fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahc);
        iniciar();
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.edt_URL);
        conectar = (Button) findViewById(R.id.btn_conectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txv_Resultado);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void onClick(View v) {
        if (v == conectar) {
            AAHC();
        }
    }

    private void AAHC() {
        final String texto = direccion.getText().toString();

        final ProgressDialog progreso = new ProgressDialog(AAHCActivity.this);
        inicio = System.currentTimeMillis();
        RestClient.get(texto, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, responseString, "text/html", "UTF-8", null);
                tiempo.setText("Completado"+(inicio-fin));
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                // called when response HTTP status is "200 OK"
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, responseString, "text/html", "UTF-8", null);
                tiempo.setText("Completado"+(inicio-fin));
            }

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
        });
    }


    public static class RestClient {
        private static final String BASE_URL = "";
        private static final int MAX_TIMEOUT = 10000;
        private static final int RETRIES = 3;
        private static final int TIMEOUT_BETWEEN_RETRIES = 5000;
        private static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        public static void get(String url, AsyncHttpResponseHandler responseHandler) {
            client.setTimeout(MAX_TIMEOUT);
            client.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);
            client.get(getAbsoluteUrl(url), responseHandler);
        }

        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.setTimeout(MAX_TIMEOUT);
            client.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.setTimeout(MAX_TIMEOUT);
            client.setMaxRetriesAndTimeout(RETRIES, TIMEOUT_BETWEEN_RETRIES);
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        private static String getAbsoluteUrl(String relativeUrl) {
            return BASE_URL + relativeUrl;
        }

        public static void cancelRequests(Context c, boolean flag) {
            client.cancelRequests(c, flag);
        }
    }
}
