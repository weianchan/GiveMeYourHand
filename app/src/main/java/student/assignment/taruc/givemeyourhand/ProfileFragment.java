package student.assignment.taruc.givemeyourhand;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView username, userEmail, userPhone;
    private ImageView userProfile;
    private Button changeProfile, changeInfo, changePassword;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ProgressBar loadingBar;
    View view;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.profile_username);
        userEmail = view.findViewById(R.id.profile_user_email);
        userPhone = view.findViewById(R.id.profile_user_phone);
        userProfile = view.findViewById(R.id.profile_image);

        changeProfile = view.findViewById(R.id.change_profile_btn);
        changeInfo = view.findViewById(R.id.change_info_btn);
        changePassword = view.findViewById(R.id.change_password_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        changePassword.setOnClickListener(this);
        changeInfo.setOnClickListener(this);
        changeProfile.setOnClickListener(this);

        loadingBar = view.findViewById(R.id.profile_loading_bar);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadingBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadingBar.bringToFront();
        username.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());
        if(currentUser.getPhotoUrl()!=null){
            Picasso.get().load(currentUser.getPhotoUrl()).fit().centerCrop()
                    .placeholder(R.drawable.ic_image_24dp)
                    .error(R.drawable.ic_image_24dp)
                    .into(userProfile);
        }

        firebaseDatabase.getReference().child("User").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPhone.setText(dataSnapshot.child("Phone").getValue(String.class));
                loadingBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_info_btn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new UpdateInfo()).commit();
                break;
            case R.id.change_password_btn:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("your old password:");

                final EditText input =new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());

                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadingBar.setVisibility(View.VISIBLE);
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loadingBar.bringToFront();
                        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), input.getText().toString());

                        currentUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            loadingBar.setVisibility(View.GONE);
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            final AlertDialog.Builder newBuilder = new AlertDialog.Builder(getActivity());
                                            LinearLayout layout = new LinearLayout(getActivity());
                                            layout.setOrientation(LinearLayout.VERTICAL);
                                            newBuilder.setTitle("Enter Your new password");
                                            final EditText password = new EditText(getActivity());
                                            password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                            password.setHint("new password");
                                            layout.addView(password);
                                            final EditText confirmPassword = new EditText(getActivity());
                                            confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                            confirmPassword.setHint("Confirm Password");
                                            layout.addView(confirmPassword);
                                            newBuilder.setView(layout);

                                            newBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    loadingBar.setVisibility(View.VISIBLE);
                                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    loadingBar.bringToFront();
                                                    if(password.getText().toString().equals(confirmPassword.getText().toString())){
                                                        currentUser.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    loadingBar.setVisibility(View.GONE);
                                                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                    Toast.makeText(getActivity(),"Password updated", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(getActivity(),"Error password not updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        Toast.makeText(getActivity(), "Error confirm password must same as password", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                            newBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });

                                            newBuilder.show();

                                        }
                                        else{
                                            Toast.makeText(getActivity(), "Error password incorrect", Toast.LENGTH_SHORT).show();

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
            case R.id.change_profile_btn:
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        loadingBar.setVisibility(View.VISIBLE);
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loadingBar.bringToFront();
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK&& requestCode == 1 && data!= null){


            Uri imageUri = data.getData();
            StorageReference imageRef = firebaseStorage.getReference().child("users/"+currentUser.getUid()+".jpg");
            imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isComplete() && task.isSuccessful()){
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                currentUser.updateProfile(profileUpdate);
                                Picasso.get().load(uri).fit().centerCrop()
                                        .placeholder(R.drawable.ic_image_24dp)
                                        .error(R.drawable.ic_image_24dp)
                                        .into(userProfile);

                                NavigationView navigationView = view.getRootView().findViewById(R.id.nav_view);
                                View header = navigationView.getHeaderView(0);
                                ImageView navProfile = header.findViewById(R.id.current_user_profile);
                                Picasso.get().load(uri).fit().centerCrop()
                                        .placeholder(R.drawable.ic_image_24dp)
                                        .error(R.drawable.ic_image_24dp)
                                        .into(navProfile);

                                loadingBar.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                            }
                        });


                    }
                }
            });





        }
    }
}
