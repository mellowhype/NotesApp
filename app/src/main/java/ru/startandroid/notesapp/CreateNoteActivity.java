package ru.startandroid.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText mTitleText, mBodyText;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        sdf = new SimpleDateFormat();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mTitleText = findViewById(R.id.mTitleText);
        mBodyText = findViewById(R.id.mBodyText);
        Button btnCreate = findViewById(R.id.btnAdd);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String datetime = sdf.format(System.currentTimeMillis());
                bundle.putString(Database.COLUMN_TITLE, mTitleText.getText().toString());
                bundle.putString(Database.COLUMN_TEXT, mBodyText.getText().toString());
                bundle.putString(Database.COLUMN_DATE, datetime);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                if (mTitleText.getText().length()==0) {
                    Toast.makeText(CreateNoteActivity.this, R.string.titleemptytext, Toast.LENGTH_SHORT).show();
                    return;
                } else
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
