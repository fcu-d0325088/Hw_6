package com.example.D0325088.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_addnote;
    ListView lv_notes;
    SQLiteDatabase db;
    ArrayList<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_addnote = (Button)findViewById(R.id.btn_addnote);
        btn_addnote.setOnClickListener(addnote);

        lv_notes = (ListView)findViewById(R.id.lv_notes);
        lv_notes.setOnItemClickListener(iclick);
        lv_notes.setOnItemLongClickListener(ilclick);


    }


    protected void onPause() {
        super.onPause();
        db.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        titlelist = NoteDB.getTitleList(db);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titlelist);
        lv_notes.setAdapter(adapter);
    }


    View.OnClickListener addnote = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", -1);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener iclick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener ilclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v,
                                       int position, long id) {
            String title = titlelist.get(position);
            NoteDB.delNote(db, title);
            titlelist = NoteDB.getTitleList(db);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (MainActivity.this,
                            android.R.layout.simple_list_item_1, titlelist);
            lv_notes.setAdapter(adapter);
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
