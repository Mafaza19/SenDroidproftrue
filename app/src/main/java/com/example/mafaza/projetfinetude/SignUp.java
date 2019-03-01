package com.example.mafaza.projetfinetude;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;

public class SignUp extends AppCompatActivity implements OnClickListener {

    // profile pic
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;
    private TextInputEditText ema;
    private TextInputEditText password;
    private Button Sign;
    private static final String TAG = "";
    private ProgressBar progressBar;


    //Declare an instance of FirebaseAuth
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // profile pic
        findViewById(R.id.browse_button).setOnClickListener(this);


        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        ema =findViewById(R.id.email);
        password = findViewById(R.id.password);
        Sign = findViewById(R.id.button);
        progressBar =(findViewById(R.id.progressBar2));




    }

    // profile pic
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);


        intent.addCategory(Intent.CATEGORY_OPENABLE);


        intent.setType("image/*");


        startActivityForResult(intent, REQUEST_ID);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_ID && resultCode == Activity.RESULT_OK) {
            try {
                stream = getContentResolver().openInputStream(data.getData());


                Bitmap original = BitmapFactory.decodeStream(stream);
                ((ImageView) findViewById(R.id.image_holder)).setImageBitmap(Bitmap.createScaledBitmap(original,
                        original.getWidth() / HALF, original.getHeight() / HALF, true));
            } catch (Exception e) {
                e.printStackTrace();


            }


            if (stream != null) {
                try {
                    stream.close();


                } catch (Exception e) {
                    e.printStackTrace();


                }


            }


        }


    }
    // profile pic End


    public void SignUpUser(View v) {

        String Email = ema.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Write your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            if (task.isSuccessful()) {
                                //User is successfully registered and logged in
                                //start Profile Activity here
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(SignUp.this, "registration successful",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), SignedIn.class));
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Couldn't register, try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}
