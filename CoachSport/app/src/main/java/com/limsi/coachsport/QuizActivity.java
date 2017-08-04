package com.limsi.coachsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class QuizActivity extends Activity {
	private List<Question> quesList;
	int R1 = 0;
	int R2 = 0;
	int R3 = 0;
    private int qid=0;
    private Question currentQ;
    private TextView txtQuestion;
    private RadioButton rda, rdb, rdc, rdd;
    private RadioGroup radioGroup;
    private Button butNext;
    private ProgressBar progressBar;
    private String result;
    private long startTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		quesList = com.limsi.coachsport.Question.addQuestions(getResources().getStringArray(R.array.Questions), getResources().getStringArray(R.array.Reponse), "0");
		for (int i = 0; i < quesList.size(); i++){
			if (i == 6 || i == 8 || i == 9 || i == 10  || i == 14  || i == 18  || i == 19  || i == 23  || i == 27 || i == 31 ){
				quesList.get(i).setRESULT("2");
			}
			if (i == 11 || i == 12 || i == 13 || i == 16  || i == 20  || i == 25  || i == 28  || i == 30  || i == 32 ){
				quesList.get(i).setRESULT("1");
			}
			if (i == 7 || i == 15 || i == 17 || i == 21  || i == 22  || i == 24  || i == 26  || i == 29 ){
				quesList.get(i).setRESULT("3");
			}
		}
		currentQ=quesList.get(qid);
		Intent previous = getIntent();
		Bundle prevB = previous.getExtras();
		result = prevB.getString("result");
		txtQuestion=(TextView)findViewById(R.id.textView1);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.clearCheck();
		rda=(RadioButton)findViewById(R.id.radio0);
		rdb=(RadioButton)findViewById(R.id.radio1);
		rdc=(RadioButton)findViewById(R.id.radio2);
		rdd=(RadioButton)findViewById(R.id.radio3);
		progressBar=(ProgressBar)findViewById(R.id.progressBar1);
		progressBar.setMax(quesList.size());
		butNext=(Button)findViewById(R.id.button1);
		setQuestionView();
		startTime = System.currentTimeMillis();



		butNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(radioGroup.getCheckedRadioButtonId() != -1){
					long endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					startTime = System.currentTimeMillis();
			        RadioButton answer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
					result = result + answer.getText() + "," + totalTime + ",";
                switch(currentQ.getCATEGORY()){
					case "0":
						break;
                    case "1":
                        if(answer.getText().equals("Non")) {R1 += 1;}
                        if(answer.getText().equals("Plutôt non")) {R1 += 2;}
                        if(answer.getText().equals("Plutôt oui")) {R1 += 3;}
                        if(answer.getText().equals("Oui")) {R1 += 4;}
                        break;

                    case "2":
						if(answer.getText().equals("Non")) {R2 += 1;}
						if(answer.getText().equals("Plutôt non")) {R2 += 2;}
						if(answer.getText().equals("Plutôt oui")) {R2 += 3;}
						if(answer.getText().equals("Oui")) {R2 += 4;}
                        break;


                    case "3":
						if(answer.getText().equals("Non")) {R3 += 1;}
						if(answer.getText().equals("Plutôt non")) {R3 += 2;}
						if(answer.getText().equals("Plutôt oui")) {R3 += 3;}
						if(answer.getText().equals("Oui")) {R3 += 4;}
                        break;


                }
				if(qid<quesList.size()-1){
                    qid += 1;
					currentQ=quesList.get(qid);
                    setQuestionView();
                    progressBar.incrementProgressBy(1);
					radioGroup.clearCheck();


				}else{

					Intent intent = new Intent(QuizActivity.this, AfterQuiz.class);
					Bundle b = new Bundle();
					b.putInt("R1", R1);
					b.putInt("R2", R2);
					b.putInt("R3", R3);
					b.putString("result", result);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
	}
	private void setQuestionView()
	{
		txtQuestion.setText(currentQ.getQUESTION());
		rda.setText(currentQ.getOPTA());
		rdb.setText(currentQ.getOPTB());
		rdc.setText(currentQ.getOPTC());
		rdd.setText(currentQ.getOPTD());
	}
}
