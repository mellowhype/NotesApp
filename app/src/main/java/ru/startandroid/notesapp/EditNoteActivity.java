package ru.startandroid.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mTitleText = (EditText) findViewById(R.id.mTitleText);
        mBodyText = (EditText) findViewById(R.id.mBodyText);

        Button btnSave = (Button) findViewById(R.id.btnEdit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(Database.COLUMN_TITLE);
            String text = extras.getString(Database.COLUMN_TEXT);
            mRowId = extras.getLong(Database.COLUMN_ID);
            mTitleText.setText(title);
            mBodyText.setText(text);
        }

            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Database.COLUMN_ID, mRowId);
                    bundle.putString(Database.COLUMN_TITLE, mTitleText.getText().toString());
                    bundle.putString(Database.COLUMN_TEXT, mBodyText.getText().toString());
                    Intent mIntent = new Intent();
                    mIntent.putExtras(bundle);
                    if (mTitleText.getText().length()==0) {
                        Toast.makeText(EditNoteActivity.this, R.string.titleemptytext, Toast.LENGTH_SHORT).show();
                        return;
                    } else
                    setResult(RESULT_OK, mIntent);
                    finish();
                }
            });
        }
    }

