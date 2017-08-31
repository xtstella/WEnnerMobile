package com.limsi.triviality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.triviality.ResultActivity;
import com.limsi.coachsport.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AfterQuiz extends Activity{
    private Button Suivant;
    private String result;
    private int R1,R2,R3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_quiz);
        Intent previous = getIntent();
        Bundle prevB = previous.getExtras();
        result = prevB.getString("result");
        int R1= prevB.getInt("R1");
        int R2= prevB.getInt("R2");
        int R3= prevB.getInt("R3");
        result = result + R1 + "," + R2 + "," + R3;

        final File path =
                Environment.getExternalStoragePublicDirectory
                        (

                                Environment.DIRECTORY_DCIM + "/dataProfile/"
                        );

        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
            System.out.println("dossier cree");
        }

        final File file = new File(path, "data.csv");

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(result);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            System.out.println("ça marche");
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
            System.out.println("ça marche pas");
        }
        Suivant = (Button) findViewById(R.id.button5);
        Suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AfterQuiz.this, ResultActivity.class);
                Bundle b = new Bundle();
                //b.putInt("R1", R1);
                //b.putInt("R2", R2);
                //b.putInt("R3", R3);
                b.putString("result", result);
                b.putString("result", result);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }


        });
    }
}
