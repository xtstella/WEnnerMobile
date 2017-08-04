package com.limsi.coachsport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ConnexionActivity extends Activity {
    private EditText Identifiant, Password;
    private Button Connexion, Creation;
    private TextView Erreur;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Erreur = (TextView)findViewById(R.id.textViewErreur);
        Identifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        Password = (EditText) findViewById(R.id.editTextPassword);
        Connexion=(Button)findViewById(R.id.buttonConnexion);
        Creation=(Button)findViewById(R.id.buttonCreation);
        Connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Identifiant.getText().toString().equals("") || Password.getText().toString().equals("")){ // Vérifier si ID dans la bd)
                    System.out.print(Identifiant);
                    Erreur.setText("Identifiant ou Mot de Passe incorrect");
                }
                else{
                    Intent intent = new Intent(ConnexionActivity.this, AppActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        Creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, CreationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}