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


        Button btnNew = findViewById(R.id.btnNewGame);
        Button btnContinue = findViewById(R.id.btnContinue);

        TextView textCopyright = findViewById(R.id.textCopyright);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String copyright = "© " + year + " Debasish Mahato.";
        textCopyright.setText(copyright);


        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("NEW_GAME", true);
                startActivity(i);
                finish();

            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("NEW_GAME", false);
                startActivity(i);
                finish();

            }
        });
    }


}