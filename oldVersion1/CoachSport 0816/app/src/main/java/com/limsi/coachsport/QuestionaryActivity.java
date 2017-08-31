package com.limsi.coachsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

public class QuestionaryActivity extends Activity {
    private Button next;
    private SeekBar stressMoyen;
    private SeekBar stressMax;
    private Spinner anneeThese;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionary);
        next=(Button)findViewById(R.id.button1);

        stressMoyen=(SeekBar)findViewById(R.id.seekBar1);
        stressMax=(SeekBar)findViewById(R.id.seekBar);
        anneeThese=(Spinner)findViewById(R.id.spinner1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previous = getIntent();
                String anneeT = anneeThese.getSelectedItem().toString();
                Integer valeurStressMoyen = stressMoyen.getProgress();
                Integer valeurStressMax = stressMax.getProgress();
                result = anneeT + "," + valeurStressMoyen + "," + valeurStressMax + ",";
                System.out.println(result);
                Intent intent = new Intent(QuestionaryActivity.this, BeforeQuiz.class);
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