package com.jukaela.tabletest.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jukaela.tabletest.app.Misc.JsonAsyncTask;
import com.jukaela.tabletest.app.Models.ImageResponseObject;
import com.jukaela.tabletest.app.R;
import com.jukaela.tabletest.app.Misc.StableArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public ListView listView;
    private ArrayList<ImageResponseObject> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JsonAsyncTask(this).execute("");

        bindControls();
    }

    private void bindControls() {
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageResponseObject selectedFromList = dataSource.get(i);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedFromList.getImageUrl().toString()));
                startActivity(browserIntent);
            }
        });
    }

    public void setupListViewDataSource(ArrayList<ImageResponseObject> imageArray) {
        final ArrayList<String> list = new ArrayList<String>();

        dataSource = imageArray;

        for (ImageResponseObject tempObject : imageArray) {
            list.add(tempObject.getFullCaption());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
