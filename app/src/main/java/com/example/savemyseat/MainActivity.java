package com.example.savemyseat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;


    //defining views
    EditText editTextCompID, editTextCompName, editTextLocation, editTextCompPhone;
    RatingBar ratingBar;
    Spinner spinnerCompType;
    ProgressBar progressBar;
    ListView listViewCompanies;
    Button buttonAddUpdate;


    //we will use this list to display Company in listview
    List<Company> companyList;

    //as the same button is used for create and update
    //we need to track whether it is an update or create operation
    //for this we have this boolean
    boolean isUpdating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCompID = (EditText) findViewById(R.id.editTextCompID);
        editTextCompName = (EditText) findViewById(R.id.editTextCompName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        spinnerCompType = (Spinner) findViewById(R.id.spinnerCompType);
        editTextCompPhone = (EditText) findViewById(R.id.editTextCompPhone);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listViewCompanies = (ListView) findViewById(R.id.listViewCompanies);

        companyList = new ArrayList<>();


        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if it is updating
                if (isUpdating) {
                    //calling the method update Company
                    //method is commented becuase it is not yet created
                    updateCompany();
                } else {
                    //if it is not updating
                    //that means it is creating
                    //so calling the method create company
                    createCompany();
                }
            }
        });

        //calling the method read heroes to read existing heros from the database
        //method is commented because it is not yet created
        readCompanies();
    }



    private void createCompany() {
        String compName = editTextCompName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String compPhone = editTextCompPhone.getText().toString().trim();

        int rating = (int) ratingBar.getRating();

        String compType = spinnerCompType.getSelectedItem().toString();


        //validating the inputs
        if (TextUtils.isEmpty(compName)) {
            editTextCompName.setError("Please enter company name");
            editTextCompName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            editTextLocation.setError("Please enter location");
            editTextLocation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(compPhone)) {
            editTextCompPhone.setError("Please enter phone number");
            editTextCompPhone.requestFocus();
            return;
        }

        //if validation passes

        HashMap<String, String> params = new HashMap<>();
        params.put("compName", compName);
        params.put("location", location);
        params.put("rating", String.valueOf(rating));
        params.put("compType", compType);
        params.put("compPhone", compPhone);


        //Calling the create hero API
        PerformNetworkRequest request = new PerformNetworkRequest(CompanyApi.URL_CREATE_COMPANY, params, CODE_POST_REQUEST);
        request.execute();
    }


    private void readCompanies() {
        PerformNetworkRequest request = new PerformNetworkRequest(CompanyApi.URL_READ_COMPANIES, null, CODE_GET_REQUEST);
        request.execute();
    }



    private void updateCompany() {
        String compID = editTextCompID.getText().toString();
        String compName = editTextCompName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();


        int rating = (int) ratingBar.getRating();

        String compType = spinnerCompType.getSelectedItem().toString();

        String compPhone = editTextCompPhone.getText().toString().trim();


        if (TextUtils.isEmpty(compName)) {
            editTextCompName.setError("Please enter company name");
            editTextCompName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            editTextLocation.setError("Please enter location");
            editTextLocation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            editTextCompPhone.setError("Please enter phone number");
            editTextCompPhone.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("compID", compID);
        params.put("compName", compName);
        params.put("location", location);
        params.put("rating", String.valueOf(rating));
        params.put("compType", compType);
        params.put("compPhone", compPhone);



        PerformNetworkRequest request = new PerformNetworkRequest(CompanyApi.URL_UPDATE_COMPANY, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Add");

        editTextCompName.setText("");
        editTextLocation.setText("");
        ratingBar.setRating(0);
        spinnerCompType.setSelection(0);
        editTextCompPhone.setText("");

        isUpdating = false;
    }



    private void deleteCompany(int compID) {
        PerformNetworkRequest request = new PerformNetworkRequest(CompanyApi.URL_DELETE_COMPANY + compID, null, CODE_GET_REQUEST);
        request.execute();
    }


    private void refreshCompanyList(JSONArray companies) throws JSONException {
        //clearing previous heroes
        companyList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < companies.length(); i++) {
            //getting each hero object
            JSONObject obj = companies.getJSONObject(i);

            //adding the company to the list
            companyList.add(new Company(
                    obj.getInt("compID"),
                    obj.getString("compName"),
                    obj.getString("location"),
                    obj.getInt("rating"),
                    obj.getString("compType"),
                    obj.getInt("compPhone")
            ));
        }

        //creating the adapter and setting it to the listview
        CompanyAdapter adapter = new CompanyAdapter(companyList);
        listViewCompanies.setAdapter(adapter);
    }





    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {


        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshing the companyList after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    refreshCompanyList(object.getJSONArray("companies"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }


    class CompanyAdapter extends ArrayAdapter<Company> {

        //our hero list
        List<Company> companyList;


        //constructor to get the list
        public CompanyAdapter(List<Company> companyList) {
            super(MainActivity.this, R.layout.layout_company_list, companyList);
            this.companyList = companyList;
        }


        //method returning list item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_company_list, null, true);

            //getting the textview for displaying name
            TextView textViewCompName = listViewItem.findViewById(R.id.textViewCompName);

            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Company company = companyList.get(position);

            textViewCompName.setText(company.getCompName());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //so when it is updating we will
                    //make the isUpdating as true
                    isUpdating = true;

                    //we will set the selected hero to the UI elements
                    editTextCompID.setText(String.valueOf(company.getCompID()));
                    editTextCompName.setText(company.getCompName());
                    editTextLocation.setText(company.getLocation());
                    ratingBar.setRating(company.getRating());
                    spinnerCompType.setSelection(((ArrayAdapter<String>) spinnerCompType.getAdapter()).getPosition(company.getCompType()));
                    editTextCompPhone.setText(String.valueOf(company.getCompPhone()));

                    //we will also make the button text to Update
                    buttonAddUpdate.setText("Update");
                }
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Delete " + company.getCompName())
                            .setMessage("Are you sure you want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the hero
                                    //method is commented because it is not yet created
                                    deleteCompany(company.getCompID());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }
    }




}