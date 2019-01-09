package student.assignment.taruc.givemeyourhand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SettingFragment extends Fragment{

    public SettingFragment() {
    }

    String items[] = new String[]{"Version 1.0.0", "Tell your Friends about us"};
    ListView settingsList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment,container,false);
        settingsList = (ListView)view.findViewById(R.id.settings_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,items);

        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String temp = String.valueOf(adapterView.getItemAtPosition(i));
                Toast.makeText(getContext(),temp, Toast.LENGTH_SHORT).show();

            }
        });
        settingsList.setAdapter(arrayAdapter);

        return view;

    }
    public void onStart() {

        super.onStart();

    }
}
