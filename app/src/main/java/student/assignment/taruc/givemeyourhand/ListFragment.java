package student.assignment.taruc.givemeyourhand;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class ListFragment extends android.support.v4.app.Fragment {

    private View view;
    private RecyclerView recyclerView;

    private DatabaseReference postReference, idReference, commentReference;
    private Bitmap bitmap;
    private Uri path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);

        //recyclerView.setAdapter(customAdapter);

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

                holder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.commentTxt.getText().toString().equals("")){
                            Toast.makeText(getActivity(),"Empty Comment",Toast.LENGTH_SHORT);
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



                postReference.child(postID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild("image1") && dataSnapshot.hasChild("image2") && dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();
                            String secondImage = dataSnapshot.child("image2").getValue().toString();
                            String thirdImage = dataSnapshot.child("image3").getValue().toString();

                            //path = Uri.parse(firstImage);

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

                        String postTitle = dataSnapshot.child("Title").getValue().toString();
                        String postContent = dataSnapshot.child("Content").getValue().toString();
                        String postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                        String postContact = dataSnapshot.child("ContactNo").getValue().toString();

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
        TextView acc_label, contact_label, userName;
        EditText commentTxt;
        Button commentButton;




        public postViewHolder(View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            userName = itemView.findViewById(R.id.username);
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

        }
    }
}
