package ru.startandroid.notesapp;

import androidx.annotation.NonNull;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;

public class MainActivity extends ListActivity {

    private Database db;
    private Cursor cursor;
    private SimpleDateFormat sdf;

    private static final int ACTIVITY_CREATE=1;
    private static final int ACTIVITY_EDIT=2;
    private static final int DELETE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf = new SimpleDateFormat();
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(onClickListener);
        db = new Database(this);
        db.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        cursor = db.getAllData();
        startManagingCursor(cursor);
        String[] from = new String[] { Database.COLUMN_TITLE, Database.COLUMN_DATE };
        int[] to = new int[] { R.id.tvText, R.id.tvDate };
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        setListAdapter(scAdapter);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createNote();
        }
    };

    private void createNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = cursor;
        c.moveToPosition(position);
        Intent intent = new Intent(this, EditNoteActivity.class );
        intent.putExtra(Database.COLUMN_ID, id);
        intent.putExtra(Database.COLUMN_TITLE, c.getString(c.getColumnIndex(Database.COLUMN_TITLE)));
        intent.putExtra(Database.COLUMN_TEXT, c.getString(c.getColumnIndex(Database.COLUMN_TEXT)));
        startActivityForResult(intent, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    String title = extras.getString(Database.COLUMN_TITLE);
                    String text = extras.getString(Database.COLUMN_TEXT);
                    String datetime = extras.getString(Database.COLUMN_DATE);
                    db.addRec(title, text, datetime);
                    fillData();
                    break;
                case ACTIVITY_EDIT:
                    String editdatetime = sdf.format(System.currentTimeMillis());
                    long id = extras.getLong(Database.COLUMN_ID);
                    String editTitle = extras.getString(Database.COLUMN_TITLE);
                    String editText = extras.getString(Database.COLUMN_TEXT);
                    db.updateRec(id, editTitle, editText, editdatetime);
                    fillData();
                    break;
            }
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, R.string.delRec);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            db.delRec(acmi.id);
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}