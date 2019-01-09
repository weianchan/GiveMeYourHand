package student.assignment.taruc.givemeyourhand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        final String[] languages = {"English(Default)", "Chinese(Simplifies)"};
        ListView languageList;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, languages);
        languageList = (ListView)findViewById(R.id.language_list);
        languageList.setAdapter(arrayAdapter);

        languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    String lg = String.valueOf(adapterView.getItemAtPosition(i));
                    Toast.makeText(LanguageActivity.this, lg,Toast.LENGTH_SHORT).show();
                }
                else if(i == 1)
                {
                    String lg = String.valueOf(adapterView.getItemAtPosition(i));
                    Toast.makeText(LanguageActivity.this, lg,Toast.LENGTH_SHORT).show();
                }
                else
                {

                }
            }
        });


    }
}
