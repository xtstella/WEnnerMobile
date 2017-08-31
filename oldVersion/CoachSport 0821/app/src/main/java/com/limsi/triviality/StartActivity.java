package com.limsi.triviality;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.example.triviality.*;

import static java.lang.System.in;

public class StartActivity extends Activity {
    private Button next;
    private SeekBar stressMoyen;
    private SeekBar stressMax;
    private Spinner anneeThese;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        next=(Button)findViewById(R.id.button1);

        stressMoyen=(SeekBar)findViewById(R.id.seekBar1);
        stressMax=(SeekBar)findViewById(R.id.seekBar);
        anneeThese=(Spinner)findViewById(R.id.spinner1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previous = getIntent();
                Bundle prevB = previous.getExtras();
                String anneeT = anneeThese.getSelectedItem().toString();
                Integer valeurStressMoyen = stressMoyen.getProgress();
                Integer valeurStressMax = stressMax.getProgress();
                result = prevB.getString("result") + anneeT + "," + valeurStressMoyen + "," + valeurStressMax + ",";
                System.out.println(result);
                Intent intent = new Intent(StartActivity.this, com.example.triviality.BeforeQuiz.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }
}
