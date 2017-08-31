package com.example.triviality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.limsi.coachsport.R;

public class CreationActivity extends Activity {
    private EditText Identifiant, Password, Confirm;
    private Button Valider;
    private TextView Erreur;
    private Profil ProfilUser;
    private String result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        Erreur = (TextView) findViewById(R.id.textViewErreurCreation);
        Identifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        Password = (EditText) findViewById(R.id.editTextPassword);
        Confirm = (EditText) findViewById(R.id.editTextValide);
        Valider = (Button) findViewById(R.id.buttonCreation);
        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Password.getText().toString().equals(Confirm.getText().toString())) {
                    System.out.println(Password.getText().toString());
                    System.out.println(Confirm.getText().toString());
                    Erreur.setText("Erreur de saisie du Mot de Passe");
                } else if (Identifiant.getText().toString().equals("") || Password.getText().toString().equals("")) {
                    Erreur.setText("Identifiant ou Mot de Passe vide");
                } else {
                    ProfilUser = new Profil(Identifiant.getText().toString(), Password.getText().toString());
                    result = ProfilUser.getUsername() + "," + ProfilUser.getPassword() + ",";
                    Intent intent = new Intent(CreationActivity.this, StartActivity.class);
                    intent.putExtra("result", result);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }
}