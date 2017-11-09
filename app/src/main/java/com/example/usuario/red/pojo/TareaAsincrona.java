package com.example.usuario.red.pojo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.example.usuario.red.MainActivity;

/**
 * Created by usuario on 9/11/17.
 */

public class TareaAsincrona extends AsyncTask<String, Void, String> {


    private ProgressDialog progreso;
    private Context context;


    public static  final String JAVA = "JAVA";
    public static  final String APACHE = "APACHE";
    long inicio ,fin;


    public TareaAsincrona(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        progreso = new ProgressDialog(context);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando . . .");
        progreso.setCancelable(true);
        progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                TareaAsincrona.this.cancel(true);
            }
        });
        progreso.show();
    }

    protected Resultado doInBackground(String... cadena) {
        Resultado resultado;
        int i = 1;
        try {
// operaciones en el hilo secundario

            publishProgress(i++);
            if (cadena[0].equals(JAVA)){
                resultado = Conexiones.conectarJava(cadena[1]);
            }else {

                resultado = Conexiones.conectarJava(cadena[2]);
            }

        } catch (Exception e) {
            Log.e("HTTP", e.getMessage(), e);
            resultado = null;
            cancel(true);
        }
        return resultado;
    }

    protected void onProgressUpdate(Integer... progress) {
        progreso.setMessage("Conectando " + Integer.toString(progress[0]));
    }

    protected void onPostExecute(Resultado result) {
        progreso.dismiss();
// mostrar el resultado

    }

    protected void onCancelled() {
        progreso.dismiss();
// mostrar cancelación

    }
}