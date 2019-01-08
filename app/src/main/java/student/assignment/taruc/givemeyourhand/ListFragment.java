package student.assignment.taruc.givemeyourhand;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ListFragment extends android.support.v4.app.Fragment {

    private View view;
    private RecyclerView recyclerView;
    private static final String TAG = "PostRecycleView:";
    private static final int R1_ID = 8456;
    private static final int R2_ID = 9704;
    private static final int R3_ID = 4136;

    private DatabaseReference postReference, idReference, commentReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postReference = FirebaseDatabase.getInstance().getReference().child("Post");
        idReference = FirebaseDatabase.getInstance().getReference().child("User");
        commentReference = FirebaseDatabase.getInstance().getReference().child("Comment");

        return view;
    }

    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<OurData>()
                .setQuery(postReference, OurData.class)
                .build();

        FirebaseRecyclerAdapter<OurData, postViewHolder> adapter
                = new FirebaseRecyclerAdapter<OurData, postViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final postViewHolder holder, final int position, @NonNull OurData model)
            {
                final String postID = getRef(position).getKey();

                DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Like");

                likeRef.orderByChild("Post").equalTo(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long count = dataSnapshot.getChildrenCount();
                        Log.d(TAG, "Number of data:" + count);
                        holder.like.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                FirebaseDatabase.getInstance().getReference().child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(dataSnapshot.hasChild(postID + currentUser)){
                            Log.d(TAG, "Set to red");
                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked_24dp,0,0,0);
                            holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked_24dp,0,0,0);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                holder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.commentTxt.getText().toString().equals("")){
                            Toast.makeText(getActivity(),"Empty comment",Toast.LENGTH_SHORT);
                        }
                        else{

                            DatabaseReference newComment = commentReference.push();
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                            String currentDate = s.format(new Date());
                            String comment = holder.commentTxt.getText().toString();

                            HashMap hashMap = new HashMap();
                            hashMap.put("Content", comment);
                            hashMap.put("Post", postID);
                            hashMap.put("Owner", currentUser.getUid());
                            hashMap.put("Date", currentDate);

                            newComment.setValue(hashMap);
                            holder.commentTxt.setText("");
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        }

                    }
                });

                holder.location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity().getApplicationContext(), MapActivity.class);
                        intent.putExtra("Location", holder.location.getText().toString());
                        startActivity(intent);
                    }
                });

                holder.viewCommentBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(getActivity().getApplicationContext(), commentActivity.class);
                        intent.putExtra("postId", postID);
                        startActivity(intent);
                    }
                });

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(postID + currentUser)){
                                    Log.d(TAG, "Remove like");
                                    FirebaseDatabase.getInstance().getReference().child("Like").child(postID + currentUser).removeValue();
                                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_24dp,0,0,0);
                                    holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_24dp,0,0,0);
                                    holder.like.setText(String.valueOf(Long.parseLong(holder.like.getText().toString())-1));
                                }
                                else{
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("Post", postID);
                                    hashMap.put("User", currentUser);
                                    FirebaseDatabase.getInstance().getReference().child("Like").child(postID + currentUser).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "liked");
                                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked_24dp,0,0,0);
                                            holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked_24dp,0,0,0);
                                            holder.like.setText(String.valueOf(Long.parseLong(holder.like.getText().toString())+1));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                holder.reportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder newBuilder = new AlertDialog.Builder(getActivity());
                        final LinearLayout layout = new LinearLayout(getActivity());
                        layout.setOrientation(LinearLayout.VERTICAL);
                        RadioGroup radioGroup = new RadioGroup(getActivity());
                        final EditText text = new EditText(getActivity());
                        text.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        text.setVisibility(View.GONE);
                        final RadioButton r1 = new RadioButton(getActivity());
                        r1.setText("Inaccurate Information");
                        r1.setId(R1_ID);
                        final RadioButton r2 = new RadioButton(getActivity());
                        r2.setText("Fake Information");
                        r2.setId(R2_ID);
                        final RadioButton r3 = new RadioButton(getActivity());
                        r3.setText("Other");
                        r3.setId(R3_ID);
                        layout.addView(radioGroup);
                        layout.addView(text);
                        radioGroup.addView(r1);
                        radioGroup.addView(r2);
                        radioGroup.addView(r3);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                RadioButton rb = radioGroup.findViewById(R3_ID);
                                if(i == R3_ID){
                                    if(rb.isChecked()){
                                        text.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        text.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });

                        newBuilder.setView(layout);
                        newBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Report").child(postID + currentUser);
                                HashMap hashMap = new HashMap();
                                hashMap.put("Post",postID);
                                hashMap.put("User",currentUser);


                                if(r3.isChecked()){
                                    String txt = text.getText().toString();
                                    hashMap.put("Reason", txt);
                                }
                                else if(r2.isChecked()){
                                    hashMap.put("Reason", r2.getText().toString());
                                }
                                else{
                                    hashMap.put("Reason", r1.getText().toString());
                                }

                                ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(),"Report is sent out. Please wait for approve", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                });


                postReference.child(postID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild("image1") && dataSnapshot.hasChild("image2") && dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();
                            String secondImage = dataSnapshot.child("image2").getValue().toString();
                            String thirdImage = dataSnapshot.child("image3").getValue().toString();

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            Picasso.get().load(secondImage).placeholder(R.drawable.ic_no_image).into(holder.image2);
                            Picasso.get().load(thirdImage).placeholder(R.drawable.ic_no_image).into(holder.image3);

                        }
                        else if(dataSnapshot.hasChild("image1") && !dataSnapshot.hasChild("image2") && !dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            holder.image2.setVisibility(View.GONE);
                            holder.image3.setVisibility(View.GONE);
                        }
                        else if(dataSnapshot.hasChild("image1") && dataSnapshot.hasChild("image2") && !dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();
                            String secondImage = dataSnapshot.child("image2").getValue().toString();

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            Picasso.get().load(secondImage).placeholder(R.drawable.ic_no_image).into(holder.image2);
                            holder.image3.setVisibility(View.GONE);
                        }
                        else
                        {
                            holder.image1.setVisibility(View.GONE);
                            holder.image2.setVisibility(View.GONE);
                            holder.image3.setVisibility(View.GONE);
                        }

                        String owner = dataSnapshot.child("Owner").getValue().toString();
                        idReference.child(owner).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("Username"))
                                {
                                    String username = dataSnapshot.child("Username").getValue().toString();
                                    holder.userName.setText(username);
                                    if(dataSnapshot.hasChild("ProfilePic"))
                                    {
                                        String profile = dataSnapshot.child("ProfilePic").getValue().toString();
                                        Picasso.get().load(profile).placeholder(R.drawable.ic_person_black_24dp).into(holder.profilePic);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        String date = dataSnapshot.child("UploadDate").getValue().toString();
                        String postTitle = dataSnapshot.child("Title").getValue().toString();
                        String postContent = dataSnapshot.child("Content").getValue().toString();
                        String postAcc = "";
                        String postContact = "";
                        String location = "";
                        if(dataSnapshot.hasChild("AccountNo")){
                            postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                        }
                        if(dataSnapshot.hasChild("ContactNo")){
                            postContact = dataSnapshot.child("ContactNo").getValue().toString();
                        }
                        if(dataSnapshot.hasChild("Location")){
                            location = dataSnapshot.child("Location").getValue().toString();

                        }



                        if(!location.equals("")){
                            holder.location.setText(location);
                        }
                        else{
                            holder.location.setVisibility(View.GONE);
                            holder.locationLabel.setVisibility(View.GONE);
                        }


                        holder.datePost.setText(date);
                        holder.title.setText(postTitle);
                        holder.content.setText(postContent);

                        if(postAcc.equals("") && postContact.equals(""))
                        {
                            holder.contact_label.setVisibility(View.GONE);
                            holder.contact.setVisibility(View.GONE);
                            holder.acc_label.setVisibility(View.GONE);
                            holder.bankAcc.setVisibility(View.GONE);
                        }
                        else if(postAcc.equals(""))
                        {
                            holder.acc_label.setVisibility(View.GONE);
                            holder.bankAcc.setVisibility(View.GONE);
                            holder.contact.setText(postContact);
                        }
                        else if(postContact.equals(""))
                        {
                            holder.contact_label.setVisibility(View.GONE);
                            holder.contact.setVisibility(View.GONE);
                            holder.bankAcc.setText(postAcc);
                        }
                        else
                        {
                            holder.bankAcc.setText(postAcc);
                            holder.contact.setText(postContact);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail, parent, false);

                postViewHolder viewHolder = new postViewHolder(postView);

                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class postViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, content, bankAcc, contact;
        ImageView image1, image2, image3, profilePic;
        TextView acc_label, contact_label, userName, locationLabel, location;
        EditText commentTxt;
        Button commentButton, viewCommentBtn, likeButton, reportButton;
        TextView datePost, like;


        public postViewHolder(View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            userName = itemView.findViewById(R.id.username);
            datePost = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.textDesc);
            content = itemView.findViewById(R.id.content);
            bankAcc = itemView.findViewById(R.id.bank_acc);
            contact = itemView.findViewById(R.id.contactNo);
            image1 = itemView.findViewById(R.id.first);
            image2 = itemView.findViewById(R.id.second);
            image3 = itemView.findViewById(R.id.third);
            acc_label = itemView.findViewById(R.id.accNoLabel);
            contact_label = itemView.findViewById(R.id.contactLabel);
            commentTxt = itemView.findViewById(R.id.textComment);
            commentButton = itemView.findViewById(R.id.comment_confirm);
            viewCommentBtn = itemView.findViewById(R.id.comment_btn);
            locationLabel = itemView.findViewById(R.id.location_label);
            location = itemView.findViewById(R.id.location);
            like = itemView.findViewById(R.id.like);
            likeButton = itemView.findViewById(R.id.like_btn);
            reportButton = itemView.findViewById(R.id.report_btn);


        }
    }
}
