package com.agusoft.actividad6;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;

public class Servicio extends Service {
    private Thread hilo;
    private boolean bandera = true;
    private int contador;
    public Servicio() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acceder();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bandera = false;
    }
    public void acceder(){
        Uri llamadas = Uri.parse("content://sms/inbox");
        ContentResolver cr = this.getContentResolver();

        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (bandera){
                        Cursor cursor = cr.query(llamadas,null,null,null,"date DESC LIMIT 5");
                        String fecha = null;
                        String contacto = null;
                        String mensaje = null;

                        StringBuilder resultado = new StringBuilder();
                        if (cursor.getCount()>0){
                            while(cursor.moveToNext()){
                                int fechaC = cursor.getColumnIndex(Telephony.Sms.DATE);
                                int contactoC = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                                int mensajeC = cursor.getColumnIndex(Telephony.Sms.BODY);
                                fecha = cursor.getString(fechaC);
                                contacto = cursor.getString(contactoC);
                                mensaje = cursor.getString(mensajeC);
                                resultado.append("Fecha: " + fecha + ", Contacto: " + contacto + ", Mensaje: " + mensaje);
                            }
                        }
                        Log.d("salida",resultado.toString());
                        Log.d("Contador","Numero: "+ contador);
                        contador++;
                        Thread.sleep(9000);
                    }
                }catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hilo.start();

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}