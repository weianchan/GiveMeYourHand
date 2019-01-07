package student.assignment.taruc.givemeyourhand;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPostFragment extends Fragment implements View.OnClickListener {

    private EditText mTitle, mContent, mAccount, mContact;
    private ImageView image1, image2, image3;
    private Button submitButton;
    private ProgressBar loadingBar;
    private static int SELECT_PICTURES_1 = 1;
    private static int SELECT_PICTURES_2 = 2;
    private static int SELECT_PICTURES_3 = 3;
    private String title, content, contact, account;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private Uri[] imageUriList = new Uri[3];

    private int count = 0;



    public UploadPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.upload_post_fragment, container, false);

        mTitle = view.findViewById(R.id.post_title);
        mContent = view.findViewById(R.id.post_content);
        mAccount = view.findViewById(R.id.post_bank_account);
        mContact = view.findViewById(R.id.post_contact_no);
        submitButton = view.findViewById(R.id.submit_button);
        loadingBar = view.findViewById(R.id.create_post_loading_bar);

        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);

        submitButton.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Picasso picasso = Picasso.get();

        if(resultCode == Activity.RESULT_OK && data!= null){

            if(requestCode == SELECT_PICTURES_1){
                Uri imageUri = data.getData();

                picasso.load(imageUri).fit().centerCrop()
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_image_24dp)
                        .into(image1);
                imageUriList[0] = imageUri;

            }
            if(requestCode == SELECT_PICTURES_2){
                Uri imageUri = data.getData();

                picasso.load(imageUri).fit().centerCrop()
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_image_24dp)
                        .into(image2);
                imageUriList[1] = imageUri;

            }
            if(requestCode == SELECT_PICTURES_3){
                Uri imageUri = data.getData();

                picasso.load(imageUri).fit().centerCrop()
                        .placeholder(R.drawable.ic_image_24dp)
                        .error(R.drawable.ic_image_24dp)
                        .into(image3);
                imageUriList[2] = imageUri;

            }



        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.image1:
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES_1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.image2:
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES_2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.image3:
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES_3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.submit_button:


                title = mTitle.getText().toString();
                content = mContent.getText().toString();
                contact = mContact.getText().toString();
                account = mAccount.getText().toString();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                if(checkValidation()){
                    loadingBar.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    final DatabaseReference dbRef = firebaseDatabase.getReference().child("Post").push();

                    for(int i=0 ; i < 3 ; i++){
                        if(imageUriList[i]!= null){
                            loadingBar.setVisibility(View.VISIBLE);
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            StorageReference imageRef = firebaseStorage.getReference().child("images/"+dbRef.getKey()+"_"+i+".jpg");
                            imageRef.putFile(imageUriList[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            dbRef.child("image"+(++count)).setValue(task.getResult().toString());
                                            Log.d("123", task.getResult().toString());
                                            loadingBar.setVisibility(View.GONE);
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        }
                                    });
                                }
                            });
                        }
                    }
                    HashMap hashMap = new HashMap();

                    hashMap.put("Title", title);
                    hashMap.put("Content", content);
                    hashMap.put("ContactNo", contact);
                    hashMap.put("AccountNo", account);
                    hashMap.put("Owner", firebaseAuth.getCurrentUser().getUid());
                    hashMap.put("UploadDate", currentDate);



                    dbRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadingBar.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getActivity(), "Your post created success.", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
        }
    }

    private boolean checkValidation(){


        Pattern phonePattern = Pattern.compile(global.phoneReg);


        Matcher pm = phonePattern.matcher(contact);

        if((pm.find() || contact.equals("")) && !title.equals("") && !content.equals("")){
            return true;
        }
        else if(title.equals("")){
            Toast.makeText(getActivity(), "Title cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(content.equals("")){
            Toast.makeText(getActivity(), "Content cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if (!pm.find()){
            Toast.makeText(getActivity(), "Invalid Contact No.",Toast.LENGTH_SHORT).show();
        }

        return false;

    }


}
