package com.example.loustics;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loustics.db.DatabaseClient;
import com.example.loustics.db.User;

public class AddUserActivity extends AppCompatActivity {

    // DATA
    private DatabaseClient mDb;

    // VIEW
    private EditText editTextNomView;
    private EditText editTextPrenomView;
    private EditText editTextFinishByView;
    private Button saveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Récupérer les vues
        editTextNomView = findViewById(R.id.username);
        editTextPrenomView = findViewById(R.id.password);
        saveView = findViewById(R.id.login);

        // Associer un événement au bouton save
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    private void saveTask() {

        // Récupérer les informations contenues dans les vues
        final String sNom = editTextNomView.getText().toString().trim();
        final String sPrenom = editTextPrenomView.getText().toString().trim();

        // Vérifier les informations fournies par l'utilisateur
        if (sNom.isEmpty()) {
            editTextNomView.setError("Nom requis");
            editTextNomView.requestFocus();
            return;
        }

        if (sPrenom.isEmpty()) {
            editTextPrenomView.setError("Prénom requis");
            editTextPrenomView.requestFocus();
            return;
        }

        /**
         * Création d'une classe asynchrone pour sauvegarder la tache donnée par l'utilisateur
         */
        class SaveUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {

                // creating a user
                User NewUser = new User();
                NewUser.setNom(sNom);
                NewUser.setPrenom(sPrenom);

                // adding to database
                mDb.getAppDatabase()
                        .taskDao()
                        .insert(NewUser);

                return NewUser;
            }

            @Override
            protected void onPostExecute(User NewUser) {
                super.onPostExecute(NewUser);

                // Quand la tache est créée, on arrête l'activité AddUserActivity (on l'enleve de la pile d'activités)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        SaveUser su = new SaveUser();
        su.execute();
    }

}