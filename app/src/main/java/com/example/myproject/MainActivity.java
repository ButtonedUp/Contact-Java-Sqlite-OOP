package com.example.myproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText idEditText, nameEditText, phoneEditText, emailEditText;
    private Button addDataButton, displayAllDataButton, updateDataButton, deleteDataButton, deleteAllDataButton;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHelper = new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        idEditText = findViewById(R.id.IdEditTextID);
        nameEditText = findViewById(R.id.nameEditTextID);
        phoneEditText = findViewById(R.id.phoneEditTextID);
        emailEditText = findViewById(R.id.emailEditTextID);
        addDataButton = findViewById(R.id.addDataButtonID);
        displayAllDataButton = findViewById(R.id.displayAllDataButtonID);
        updateDataButton = findViewById(R.id.updateDataButtonID);
        deleteDataButton = findViewById(R.id.deleteDataButtonID);
        deleteAllDataButton = findViewById(R.id.deleteAllDataButtonID);

        addDataButton.setOnClickListener(this);
        displayAllDataButton.setOnClickListener(this);
        updateDataButton.setOnClickListener(this);
        deleteDataButton.setOnClickListener(this);
        deleteAllDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String id = idEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (v.getId() == R.id.addDataButtonID) {
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Name and Phone are mandatory", Toast.LENGTH_SHORT).show();
                return;
            }

            long rowId = myDatabaseHelper.insertData(name, phone, email);
            if (rowId == -1) {
                Toast.makeText(this, "Error inserting data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Row " + rowId + " inserted successfully", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.displayAllDataButtonID) {
            Cursor cursor = null;
            try {
                cursor = myDatabaseHelper.displayAllData();
                if (cursor.getCount() == 0) {
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                    myDatabaseHelper.deleteAllData();
                    return;
                }

                StringBuilder stringBuffer = new StringBuilder();
                while (cursor.moveToNext()) {
                    stringBuffer.append("ID: ").append(cursor.getString(0)).append("\n");
                    stringBuffer.append("Name: ").append(cursor.getString(1)).append("\n");
                    stringBuffer.append("Phone: ").append(cursor.getString(2)).append("\n");
                    stringBuffer.append("Email: ").append(cursor.getString(3)).append("\n\n");
                }

                showData("Contact List", stringBuffer.toString());

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (v.getId() == R.id.updateDataButtonID) {
            if (id.isEmpty()) {
                Toast.makeText(this, "Enter ID to update data", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = myDatabaseHelper.updateData(id, name, phone, email);
            if (isUpdated) {
                Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed. Check ID", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.deleteDataButtonID) {
            int value = myDatabaseHelper.deleteData(id);
            if (value > 0) {
                Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Delete failed. Check ID", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.deleteAllDataButtonID) {
            myDatabaseHelper.deleteAllData();
            Toast.makeText(this, "All data deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void showData(String title, String resultSet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(resultSet);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
