package com.limsi.triviality;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.*;
import java.nio.charset.Charset;

public class ResultActivity extends Activity {
	int seuilFortProbleme = 30;
	int seuilFortEmotion = 30;
	int seuilFortSoutien = 30;
	int seuilFaibleProbleme = 15;
	int seuilFaibleEmotion = 15;
	int seuilFaibleSoutien = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		//get text view
		TextView t=(TextView)findViewById(R.id.textResult);
		//get score
		Bundle b = getIntent().getExtras();
        int R1= b.getInt("R1");
        int R2= b.getInt("R2");
        int R3= b.getInt("R3");
        String result;
		result = b.getString("result") + R1 + "," +  R2 + "," + R3;
		System.out.println(result);




		if (R2 < seuilFortProbleme && R2 >= seuilFaibleProbleme) {t.setText(getResources().getString(R.string.CopingProblemeFaible));}
		else if (R2 < seuilFaibleProbleme) {t.setText(getResources().getString(R.string.CopingProblemeFort));}
        else if (R1 < seuilFortEmotion && R1 >= seuilFaibleEmotion) {t.setText(getResources().getString(R.string.CopingEmotionFaible));}
        else if (R1 > seuilFortEmotion) {t.setText(getResources().getString(R.string.CopingEmotionFort));}
        else if (R3 < seuilFortSoutien && R3 >= seuilFaibleSoutien) {t.setText(getResources().getString(R.string.SoutienSocialFaible));}
        else if (R3 > seuilFortSoutien) {t.setText(getResources().getString(R.string.SoutienSocialFort));}
        else {t.setText("Pas de problèmes de stress \nValeur de R1: " + R1 + "\nValeur de R2: " + R2 + "\nValeur de R3: " + R3);}
        System.out.println(R1 +","+ R2+"," + R3);


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_result, menu);
		return true;
	}
}
