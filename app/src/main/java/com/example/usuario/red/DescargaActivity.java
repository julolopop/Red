package com.example.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.File;

import cz.msebera.android.httpclient.Header;

import static android.widget.Toast.LENGTH_LONG;

public class DescargaActivity extends AppCompatActivity implements View.OnClickListener {

    EditText texto;
    Button botonImagen,botonFichero;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);
        texto = (EditText) findViewById(R.id.edt_URL);
        botonImagen = (Button) findViewById(R.id.btn_image);
        botonFichero = (Button) findViewById(R.id.btn_fichero);
        botonImagen.setOnClickListener(this);
        botonFichero.setOnClickListener(this);
        imagen = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        String url = texto.getText().toString();
        if (v == botonImagen)
            descargaImagen(url);
        if (v == botonFichero)
            descargaFichero(url);

    }
    private void descargaFichero(final String url) {
        //usar FileAsyncHttpResponseHandler
        final ProgressDialog progreso = new ProgressDialog(this);

        AsyncHttpClient client = new AsyncHttpClient();
        File fichero = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        client.get(url, new FileAsyncHttpResponseHandler(fichero) {
            @Override
            public void onStart() {
                // called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                //progreso.setCancelable(false);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        AAHCActivity.RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progreso.setMessage("Error de descarga del fichero");
                Toast.makeText(DescargaActivity.this,"Error", LENGTH_LONG).show();
                progreso.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progreso.setMessage("Descargando en : "+file.getAbsolutePath());

                Toast.makeText(DescargaActivity.this, file.getAbsolutePath(), LENGTH_LONG).show();

                progreso.dismiss();
            }
        });

    }
    private void descargaImagen(String url) {
        //utilizar OkHttp3
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imagen);
 }
}
