package student.assignment.taruc.givemeyourhand;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class commentActivity extends AppCompatActivity {

    private DatabaseReference commentRef, userRef;
    private RecyclerView commentRecyclerView;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_comment);

        postId = getIntent().getExtras().get("postId").toString();

        commentRef = FirebaseDatabase.getInstance().getReference().child("Comment");
        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        commentRecyclerView = (RecyclerView) findViewById(R.id.listComment);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    protected void onStart()
    {
        super.onStart();
        Query query = commentRef.orderByChild("Post").equalTo(postId);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        final FirebaseRecyclerAdapter <Comment, CommentsViewHolder> adapter;
        adapter = new FirebaseRecyclerAdapter<Comment, CommentsViewHolder>(options)
{

    @Override
    protected void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position, @NonNull final Comment model) {
        String commentId = getRef(position).getKey();
        commentRef.child(commentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String owner = dataSnapshot.child("Owner").getValue().toString();
                userRef.child(owner).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String user = dataSnapshot.child("Username").getValue().toString();
                        holder.username.setText(user + " ");
                        String userProfile = dataSnapshot.child("ProfilePic").getValue().toString();
                        Picasso.get().load(userProfile).placeholder(R.drawable.ic_person_black_24dp).into(holder.profile);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.commentDate.setText(" " + model.getDate());
                holder.commentText.setText(model.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post, parent, false);

        CommentsViewHolder viewHolder = new CommentsViewHolder(postView);

        return viewHolder;

    }
};
        commentRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, commentDate, commentText;
        ImageView profile;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.comment_username);
            commentDate = itemView.findViewById(R.id.comment_date);
            commentText = itemView.findViewById(R.id.comment_txt);
            profile = itemView.findViewById(R.id.profile_pic);

        }


    }
}
