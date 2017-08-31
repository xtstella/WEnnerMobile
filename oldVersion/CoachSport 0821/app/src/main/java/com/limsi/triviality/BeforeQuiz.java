package com.limsi.triviality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.triviality.QuizActivity;

/**
 * Created by Janjak on 10/07/2017.
 */
public class BeforeQuiz extends Activity{
    private Button Suivant;
    private String result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_quiz);
        Intent previous = getIntent();
        Bundle prevB = previous.getExtras();
        result = prevB.getString("result");
        Suivant = (Button) findViewById(R.id.button5);
        Suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previous = getIntent();
                Bundle prevB = previous.getExtras();
                result = prevB.getString("result");
                Intent intent = new Intent(BeforeQuiz.this, QuizActivity.class);
                Bundle b = new Bundle();
                b.putString("result", result);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }


        });
    }
}
