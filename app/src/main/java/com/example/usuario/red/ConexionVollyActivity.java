package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.red.pojo.OkHttp3Stack;

import java.io.UnsupportedEncodingException;

import okhttp3.OkHttpClient;

public class ConexionVollyActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MyTag";
    EditText direccion;
    Button conectar;
    WebView web;
    TextView tiempo;
    RequestQueue mRequestQueue;
    long inicio,fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_volly);
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
    public void onClick(View view) {
        String url;
        if (view == conectar) {
            url = direccion.getText().toString();
            makeRequest(url);
        }
    }

    public void makeRequest(String url) {
        final String enlace = url;
        // Instantiate the RequestQueue.
        //mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final ProgressDialog progreso = new ProgressDialog(ConexionVollyActivity.this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando...");
        progreso.setCancelable(true
        );
        progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
               mRequestQueue.cancelAll(TAG);
            }
        });
        progreso.show();
        inicio = System.currentTimeMillis();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fin = System.currentTimeMillis();
                        progreso.dismiss();
                        web.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                        tiempo.setText("Completado"+(inicio-fin));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensaje = "Error";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError)
                            mensaje = "Timeout Error: " + error.getMessage();
                        else {
                            NetworkResponse errorResponse = error.networkResponse;
                            if (errorResponse != null && errorResponse.data != null)
                                try {
                                    mensaje = "Error: " + errorResponse.statusCode + " " + "\n" + new
                                            String(errorResponse.data, "UTF-8");
                                    Log.e("Error", mensaje);
                                    web.loadDataWithBaseURL(null, mensaje, "text/html", "UTF-8", null);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                        }
                        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

                    }
                });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    public static class MySingleton {
        private static MySingleton mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        private MySingleton(Context context) {
            mCtx = context;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized MySingleton getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new MySingleton(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                //mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new
                        OkHttp3Stack(new OkHttpClient()));
            }
            return mRequestQueue;
        }
    }



}

