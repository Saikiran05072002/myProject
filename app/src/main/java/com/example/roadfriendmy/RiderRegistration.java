package com.example.roadfriendmy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;


public class RiderRegistration extends AppCompatActivity {

    EditText fullname, licenceno, setpass, conpass, email;
    boolean passwordvisible;
    Button Nextbtn;
    TextView login;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    RiderInfo riderInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_registration);


        fullname = findViewById(R.id.userName);
        licenceno = findViewById(R.id.DrivingLicenceNo);
        setpass = findViewById(R.id.setpassword);
        conpass = findViewById(R.id.confirmpassword);
        email = findViewById(R.id.emailid);
        login = findViewById(R.id.logintx);
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("EmployeeInfo");

        // initializing our object
        // class variable.
        riderInfo = new RiderInfo();

        Nextbtn = findViewById(R.id.Nextbtn);

        setpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= setpass.getRight() - setpass.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = setpass.getSelectionEnd();
                        if (passwordvisible) {
                            setpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.hideeye, 0);

                            setpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible = false;
                        } else {
                            setpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);

                            setpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible = true;
                        }
                        setpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        conpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= conpass.getRight() - conpass.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = conpass.getSelectionEnd();
                        if (passwordvisible) {
                            conpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.hideeye, 0);

                            conpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible = false;
                        } else {
                            conpass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);

                            conpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible = true;
                        }
                        conpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderRegistration.this, RiderLogin.class);
                startActivity(intent);
            }
        });



        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String names = fullname.getText().toString();
                String emails = email.getText().toString();
                String licencenos = licenceno.getText().toString();
                String passwords = setpass.getText().toString();
                String conpasswords = conpass.getText().toString();

                boolean check = validateinfo(names, emails, licencenos, passwords, conpasswords);

                if (check == true) {
                    Toast.makeText(RiderRegistration.this, "Data is valid", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RiderRegistration.this, RiderLogin.class);
                    startActivity(intent);
                    addDatatoFirebase(riderInfo.RiderName, riderInfo.RiderEmailId, riderInfo.RiderLicence);
                } else {
                    Toast.makeText(RiderRegistration.this, "Data is invalid", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(emails)) {
                    Toast.makeText(RiderRegistration.this, "Enter Registered EmailId", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwords)) {
                    Toast.makeText(RiderRegistration.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(emails, passwords).addOnCompleteListener(RiderRegistration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RiderRegistration.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(RiderRegistration.this, "Registration Unsuccessfull", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            private void addDatatoFirebase(String ridernamei, String rideremaili, String riderlicencei) {
                riderInfo.setRiderName(ridernamei);
                riderInfo.setRiderEmailId(rideremaili);
                riderInfo.setRiderLicence(riderlicencei);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.setValue(riderInfo);
                        Toast.makeText(RiderRegistration.this, "data added", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RiderRegistration.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private Boolean validateinfo(String names, String emails, String licencenos, String passwords, String conpasswords) {
                if (names.length() == 0) {
                    fullname.requestFocus();
                    fullname.setError("Enter Your Name");
                    return false;
                } else if (!names.matches("[a-zA-Z]+")) {
                    fullname.requestFocus();
                    fullname.setError("Enter Alphabets Only");
                    return false;
                }
                if (emails.length() == 0) {
                    email.requestFocus();
                    email.setError("Enter Your EmailId");
                    return false;
                } else if (!emails.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.requestFocus();
                    email.setError("Enter Valid Email");
                    return false;
                }
                if (licencenos.length() == 0) {
                    licenceno.requestFocus();
                    licenceno.setError("Enter Your Driving Licence No");
                    return false;
                } else if (!licencenos.matches("[0-9]{10,13}$")) {
                    licenceno.requestFocus();
                    licenceno.setError("Enter Valid Driving Licence No");
                    return false;
                }
                if (passwords.length() <= 5) {
                    setpass.requestFocus();
                    setpass.setError("Password Should be more than 6 characters");
                    return false;
                } else if (!passwords.matches("[a-zA-Z0-9]+")) {
                    setpass.requestFocus();
                    setpass.setError("Password Should be contain Alphanumaric ");
                    return false;
                }
                if (!conpasswords.equals(passwords)) {
                    conpass.requestFocus();
                    conpass.setError("Confirm Password Should be equals to Set Password");
                    return false;
                } else {
                    return true;
                }
            }

        });

    }

    public class RiderInfo {
        private String RiderName;
        private String RiderEmailId;
        private String RiderLicence;

        public RiderInfo() {
        }
        public String getEmployeeName() {
            return RiderName;
        }
        public void setRiderName(String riderName) {
            this.RiderName = riderName;
        }
        public String getRiderEmailId() {
            return RiderEmailId;
        }
        public void setRiderEmailId(String riderEmailId) {
            this.RiderEmailId = riderEmailId;
        }
        public String getRiderLicence() {
            return RiderLicence;
        }
        public void setRiderLicence(String riderLicence) {
            this.RiderLicence = riderLicence;
        }
    }
}