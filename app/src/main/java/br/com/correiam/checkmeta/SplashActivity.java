package br.com.correiam.checkmeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;


public class SplashActivity extends Activity {
    static final int TIME_SPLASH = 1000;

    private SharedPreferences sharedPref;
    private Boolean keepConnected = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        sharedPref = context.getSharedPreferences("checkmeta_config", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPref = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                keepConnected = sharedPref.getBoolean("keepConnected", false);
                Log.d("SharedPreference", "KeepConnected: "+ keepConnected);
                if(keepConnected){
                    Long idUser = sharedPref.getLong("idUser", -1);
                    Log.d("SharedPreference", "idUser: "+ idUser);

                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("ID", idUser);
                    SplashActivity.this.startActivity(intent);

                    SplashActivity.this.finish();
                }
                else {
                    Intent splash = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(splash);
                    SplashActivity.this.finish();
                }
            }
        }, TIME_SPLASH);
    }


}
