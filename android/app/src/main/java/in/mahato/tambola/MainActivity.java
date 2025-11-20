package in.mahato.tambola;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {



   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = this;
        Button btnNew = findViewById(R.id.btnNewGame);
        Button btnContinue = findViewById(R.id.btnContinue);

        TextView textCopyright = findViewById(R.id.textCopyright);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String copyright = "© " + year + " Debasish Mahato.";
        textCopyright.setText(copyright);



        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GeneralUtil.isFireTv(ctx) && IsVoiceViewOnAmazonFireDevices()){

                    new AlertDialog.Builder(ctx)
                            .setTitle("Voice View Permission")
                            .setMessage("The Game Requires VoiceView Permission to call out Tambola Numbers.Please Enable VoiceView Permission from Settings.")
                            .setPositiveButton("Yes", (d, w) -> {
                                Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivity(i);
                                finish();
                            })
                            .setNegativeButton("No", (d,w)->{

                                Intent i = new Intent(MainActivity.this, GameActivity.class);
                                i.putExtra("NEW_GAME", true);
                                startActivity(i);
                                finish();
                            })
                            .show();



                }else {

                    Intent i = new Intent(MainActivity.this, GameActivity.class);
                    i.putExtra("NEW_GAME", true);
                    startActivity(i);
                    finish();
                }
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GeneralUtil.isFireTv(ctx) && IsVoiceViewOnAmazonFireDevices()){

                    new AlertDialog.Builder(ctx)
                            .setTitle("Voice View Permission")
                            .setMessage("The Game Requires VoiceView Permission to call out Tambola Numbers.Please Enable VoiceView Permission from Settings.")
                            .setPositiveButton("Yes", (d, w) -> {
                                Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivity(i);
                                finish();
                            })
                            .setNegativeButton("No", (d,w)->{

                                Intent i = new Intent(MainActivity.this, GameActivity.class);
                                i.putExtra("NEW_GAME", false);
                                startActivity(i);
                                finish();
                            })
                            .show();



                }else {

                    Intent i = new Intent(MainActivity.this, GameActivity.class);
                    i.putExtra("NEW_GAME", false);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public   boolean IsVoiceViewOnAmazonFireDevices(){

            AccessibilityManager am = (AccessibilityManager) this.getSystemService(ACCESSIBILITY_SERVICE);
            return am.isTouchExplorationEnabled();


    }


}