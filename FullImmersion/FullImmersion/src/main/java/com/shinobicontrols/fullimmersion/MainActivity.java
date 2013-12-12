package com.shinobicontrols.fullimmersion;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity {

    private List<ActivityInstantiator> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityInstantiator[] activityArray = {
                new ActivityInstantiator("Lean Back", LeanBackActivity.class),
                new ActivityInstantiator("Immersive", ImmersiveActivity.class)
        };
        activityList = Arrays.asList(activityArray);

        setListAdapter(new ArrayAdapter<ActivityInstantiator>(this,
                android.R.layout.simple_list_item_1,
                activityList));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ActivityInstantiator activityInstantiator = activityList.get(position);
        Intent intent = activityInstantiator.createIntent(this);
        startActivity(intent);
    }
}
