package student.assignment.taruc.givemeyourhand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        getSupportActionBar().hide();

        ImageView imageView = findViewById(R.id.full_screen_image);

        Intent intent = getIntent();

        String uri = intent.getExtras().get("image").toString();

        Picasso.get().load(uri).placeholder(R.drawable.ic_no_image).into(imageView);



    }
}
