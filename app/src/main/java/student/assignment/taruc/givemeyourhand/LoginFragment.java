package student.assignment.taruc.givemeyourhand;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View view;

    private EditText email, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox showPassword;

    private static final  String TAG = "SignInActivity: ";

    private FirebaseAuth mAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);

        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_btn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.create_account);
        showPassword = view.findViewById(R.id.show_hide_password);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                }
                else{
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
            }
        });

        return view;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_btn:
                if(checkValidation()){
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Login Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }

                break;
            case R.id.forgot_password:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please enter your email address:");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String emailAddress = input.getText().toString();

                        mAuth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(),"Reset Password Email sent to yout email address", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getActivity(),"Invalid Email Address", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();

                break;
            case R.id.create_account:

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_fragmentContainer, new SignUpFragment(),global.SIGNUP_FRAGMENT).commit();
                break;
        }

    }

    private boolean checkValidation(){
        String emailTxt = email.getText().toString();
        String passwordTxt = email.getText().toString();

        Pattern p = Pattern.compile(global.regEx);

        Matcher m = p.matcher(emailTxt);

        if(emailTxt.equals("") || passwordTxt.equals("")){
            Toast.makeText(getActivity(), "No empty Field", Toast.LENGTH_SHORT).show();
        }
        else if(!m.find())
            Toast.makeText(getActivity(), "Invalid Email address", Toast.LENGTH_SHORT).show();
        else {

            return true;
        }
        return false;

    }
}
