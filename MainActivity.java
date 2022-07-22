/*
 * a simple SQL Lite database demo app
 * add a user to the database, and delete a user.
 * app can view all data in the database.
 */
package com.jah.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI elements, buttions, Edit Text, ListView
    private Button viewallbtn, addbtn;
    private EditText edname, edage;
    private Switch activesw;
    private ListView customerlistlv;

    private ArrayAdapter customarray;
    private DBHelper db ; // the DB


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the references to the buttons and other controls
        viewallbtn = findViewById(R.id.btn_viewAll);
        addbtn = findViewById(R.id.btn_add);

        // get the references to the EditText
        edname = findViewById(R.id.ed_name);
        edage = findViewById(R.id.et_Age);

        activesw = findViewById(R.id.sw_active);
        customerlistlv = findViewById(R.id.lv_customerList);

        db = new DBHelper(MainActivity.this); // init the database
        showAllCustomers(db); // show all customers in the db


        // button listeners
        viewallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "View all clicked", Toast.LENGTH_SHORT).show();
                DBHelper dbh = new DBHelper(MainActivity.this);
                List<CustomerModel> all = dbh.getAll();
                customarray = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, all);
                customerlistlv.setAdapter(customarray);
                //Toast.makeText(MainActivity.this, all.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // Add user button
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    // get the user input data
                    String name = edname.getText().toString();
                    String age = edage.getText().toString();
                    // create a Customer object with info
                    CustomerModel customer = new CustomerModel(name, Integer.parseInt(age), activesw.isChecked());
                    //Toast.makeText(MainActivity.this, customer.toString(), Toast.LENGTH_SHORT).show();
                    DBHelper db = new DBHelper(MainActivity.this); // create a database object
                    boolean success = db.addCustomer(customer); // add customer to db
                    // show a Toast message
                    Toast.makeText(MainActivity.this, "Status:" + success, Toast.LENGTH_LONG).show();
                    showAllCustomers(db);
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        // wire a click listener for the list view
        customerlistlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CustomerModel cm = (CustomerModel) adapterView.getItemAtPosition(position); // get the clicked item
                db.deleteCustomer(cm);
                showAllCustomers(db);
                Toast.makeText(MainActivity.this, "Deleted user " + cm.getId() + " " + cm.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showAllCustomers(DBHelper db2) {
        customarray = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, db2.getAll());
        customerlistlv.setAdapter(customarray);
    }
}
