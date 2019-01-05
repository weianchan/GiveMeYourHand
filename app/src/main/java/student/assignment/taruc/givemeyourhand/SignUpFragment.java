package student.assignment.taruc.givemeyourhand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment implements View.OnClickListener {


    private View view;
    private EditText username, email, phone, password, confirmPassword;
    private CheckBox tnC;
    private Button signUpBtn, closeBtn;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private static final String TAG = "SignInActivity: ";
    private ImageView profile;
    private Uri profileUri;
    FirebaseUser user;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.signup_layout, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        username = view.findViewById(R.id.full_name);
        email = view.findViewById(R.id.user_email);
        phone = view.findViewById(R.id.mobile_number);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        tnC = view.findViewById(R.id.terms_conditions);
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        closeBtn = view.findViewById(R.id.close_button);
        profile = view.findViewById(R.id.profile);

        signUpBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        profile.setOnClickListener(this);



        return view;


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.sign_up_btn:

                if(checkValidation()){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();

                                        if(profileUri!=null){
                                            StorageReference imageRef = firebaseStorage.getReference().child("users/"+user.getUid()+".jpg");

                                            imageRef.putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if(task.isComplete() && task.isSuccessful()){
                                                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(final Uri uri) {
                                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                                        .setPhotoUri(uri)
                                                                        .setDisplayName(username.getText().toString())
                                                                        .build();

                                                                user.updateProfile(profileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "createUserWithEmail:success");
                                                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
                                                                        HashMap info = new HashMap();
                                                                        info.put("Username", username.getText().toString());
                                                                        info.put("Phone", phone.getText().toString());
                                                                        info.put("ProfilePic",uri.toString());
                                                                        myRef.setValue(info);

                                                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                                                        getActivity().finish();
                                                                    }
                                                                });

                                                            }
                                                        });


                                                    }
                                                }
                                            });

                                        }
                                        else{
                                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(username.getText().toString())
                                                    .build();
                                            user.updateProfile(profileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "createUserWithEmail:success");
                                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
                                                    HashMap info = new HashMap();
                                                    info.put("Username", username.getText().toString());
                                                    info.put("Phone", phone.getText().toString());
                                                    myRef.setValue(info);

                                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                                    getActivity().finish();

                                                }
                                            });

                                        }


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
                break;
            case R.id.close_button:{
                Toast.makeText(getActivity(), "Exit",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_fragmentContainer, new LoginFragment(),global.LOGIN_FRAGMENT).commit();

                break;
            }
            case R.id.profile:{
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }



    private boolean checkValidation(){

        String usernameTxt = username.getText().toString();
        String emailTxt = email.getText().toString();
        String phoneTxt = phone.getText().toString();
        String passwordTxt = password.getText().toString();
        String confirmPasswordTxt = confirmPassword.getText().toString();

        Pattern emailPattern = Pattern.compile(global.regEx);
        Pattern phonePattern = Pattern.compile(global.phoneReg);

        Matcher em = emailPattern.matcher(emailTxt);
        Matcher pm = phonePattern.matcher(phoneTxt);

        if(usernameTxt.equals("") || emailTxt.equals("")
                || phoneTxt.equals("") || passwordTxt.equals("")
                || confirmPasswordTxt.equals("")){
            Toast.makeText(getActivity(), "All fields are required.",Toast.LENGTH_SHORT).show();
        } else if (!em.find())
            Toast.makeText(getActivity(), "Invalid Email",Toast.LENGTH_SHORT).show();
        else if(!pm.find())
            Toast.makeText(getActivity(), "Invalid Mobile Phone Number",Toast.LENGTH_SHORT).show();
        else if(passwordTxt.length() < 8){
            Toast.makeText(getActivity(), "Password Must at least 8 characters or digits",Toast.LENGTH_SHORT).show();
        }
        else if(!passwordTxt.equals(confirmPasswordTxt)){
            Toast.makeText(getActivity(), "Confirm Password must same with password",Toast.LENGTH_SHORT).show();

        }
        else if(!tnC.isChecked()){
            Toast.makeText(getActivity(), "Please select the terms and conditions",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Sign Up",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Picasso picasso = Picasso.get();
        if(resultCode == Activity.RESULT_OK&& requestCode == 1 && data!= null){

            Uri imageUri = data.getData();
            picasso.load(imageUri).fit().centerCrop()
                    .placeholder(R.drawable.ic_image_24dp)
                    .error(R.drawable.ic_image_24dp)
                    .into(profile);
            profileUri = imageUri;


        }

    }


}
