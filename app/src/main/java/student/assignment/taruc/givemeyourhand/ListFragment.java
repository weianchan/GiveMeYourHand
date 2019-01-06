package student.assignment.taruc.givemeyourhand;

import android.app.Fragment;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ListFragment extends android.support.v4.app.Fragment {

    private View view;
    private  RecyclerView recyclerView;

    private DatabaseReference postReference, idReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);


        //recyclerView.setAdapter(customAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth.getInstance();
        postReference = FirebaseDatabase.getInstance().getReference().child("Post");
        idReference = FirebaseDatabase.getInstance().getReference().child("User");

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
                String postID = getRef(position).getKey();



                postReference.child(postID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild("image1") && dataSnapshot.hasChild("image2") && dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();
                            String secondImage = dataSnapshot.child("image2").getValue().toString();
                            String thirdImage = dataSnapshot.child("image3").getValue().toString();

                            String owner = dataSnapshot.child("Owner").getValue().toString();
                            String postTitle = dataSnapshot.child("Title").getValue().toString();
                            String postContent = dataSnapshot.child("Content").getValue().toString();
                            String postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                            String postContact = dataSnapshot.child("ContactNo").getValue().toString();

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

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            Picasso.get().load(secondImage).placeholder(R.drawable.ic_no_image).into(holder.image2);
                            Picasso.get().load(thirdImage).placeholder(R.drawable.ic_no_image).into(holder.image3);
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
                        else if(dataSnapshot.hasChild("image1") && !dataSnapshot.hasChild("image2") && !dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();

                            String owner = dataSnapshot.child("Owner").getValue().toString();
                            String postTitle = dataSnapshot.child("Title").getValue().toString();
                            String postContent = dataSnapshot.child("Content").getValue().toString();
                            String postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                            String postContact = dataSnapshot.child("ContactNo").getValue().toString();

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

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            holder.image2.setVisibility(View.GONE);
                            holder.image3.setVisibility(View.GONE);
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
                        else if(dataSnapshot.hasChild("image1") && dataSnapshot.hasChild("image2") && !dataSnapshot.hasChild("image3"))
                        {
                            String firstImage = dataSnapshot.child("image1").getValue().toString();
                            String secondImage = dataSnapshot.child("image2").getValue().toString();

                            String owner = dataSnapshot.child("Owner").getValue().toString();
                            String postTitle = dataSnapshot.child("Title").getValue().toString();
                            String postContent = dataSnapshot.child("Content").getValue().toString();
                            String postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                            String postContact = dataSnapshot.child("ContactNo").getValue().toString();

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

                            Picasso.get().load(firstImage).placeholder(R.drawable.ic_no_image).into(holder.image1);
                            Picasso.get().load(secondImage).placeholder(R.drawable.ic_no_image).into(holder.image2);
                            holder.image3.setVisibility(View.GONE);

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
                        else
                        {
                            String owner = dataSnapshot.child("Owner").getValue().toString();
                            String postTitle = dataSnapshot.child("Title").getValue().toString();
                            String postContent = dataSnapshot.child("Content").getValue().toString();
                            String postAcc = dataSnapshot.child("AccountNo").getValue().toString();
                            String postContact = dataSnapshot.child("ContactNo").getValue().toString();

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

                            holder.image1.setVisibility(View.GONE);
                            holder.image2.setVisibility(View.GONE);
                            holder.image3.setVisibility(View.GONE);
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
        ImageView image1, image2, image3;
        TextView acc_label, contact_label;
        TextView userName;
        ImageView profilePic;

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

        }
    }
}
