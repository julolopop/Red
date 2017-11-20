package com.example.usuario.red;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class DescargaActivity extends AppCompatActivity implements View.OnClickListener {

    EditText texto;
    Button botonImagen;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);
        texto = (EditText) findViewById(R.id.edt_URL);
        botonImagen = (Button) findViewById(R.id.btn_image);
        botonImagen.setOnClickListener(this);
        imagen = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        String url = texto.getText().toString();
        if (v == botonImagen)
            descargaImagen(url);
    }

    private void descargaImagen(String url) {
        //utilizar OkHttp3
        /*Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imagen);*/

         OkHttpDownloader client =  new OkHttpDownloader();

                Picasso picasso = new Picasso.with(this)
                        .load(url)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder_error)
                        .into(imagen);
    }
}
