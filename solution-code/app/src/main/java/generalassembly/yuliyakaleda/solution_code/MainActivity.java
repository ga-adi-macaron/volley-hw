package generalassembly.yuliyakaleda.solution_code;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import generalassembly.yuliyakaleda.solution_code.model.WalmartItem;
import generalassembly.yuliyakaleda.solution_code.model.WalmartRootObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String API_KEY = "35s8a6nuab3wydbn99nb777h";
    private static final String API_END_POINT = "http://api.walmartlabs.com/v1/search";
    private static final String QUERY_URL = API_END_POINT + "?apiKey=" + API_KEY + "&query=";
    private static final String URL_TEA = QUERY_URL + "tea";
    private static final String URL_CEREAL = QUERY_URL + "cereal";
    private static final String URL_CHOCOLATE = QUERY_URL + "chocolate";

    private List<WalmartItem> mWalmartItems;
    private RecyclerView.Adapter mAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mWalmartItems = new ArrayList<>(); // initialize as an empty list - will be populated by AsyncTasks
        mAdapter = new WalmartItemRecyclerView(mWalmartItems);
        recyclerView.setAdapter(mAdapter);

        // Set up buttons
        Button cerealButton = (Button) findViewById(R.id.cereal);
        Button chocolateButton = (Button) findViewById(R.id.chocolate);
        Button teaButton = (Button) findViewById(R.id.tea);

        cerealButton.setOnClickListener(this);
        teaButton.setOnClickListener(this);
        chocolateButton.setOnClickListener(this);

        // Set up the Volley RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // If there are any requests in the request queue, cancel them
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onClick(View v) {
        // Check for network connectivity before trying to make API call
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            switch (v.getId()) {
                case R.id.cereal:
                    makeWalmartApiCall(URL_CEREAL);
                    break;
                case R.id.chocolate:
                    makeWalmartApiCall(URL_CHOCOLATE);
                    break;
                case R.id.tea:
                    makeWalmartApiCall(URL_TEA);
                    break;
            }
        } else {
            Toast.makeText(this, R.string.network_connection_check, Toast.LENGTH_LONG).show();
        }
    }

    private void makeWalmartApiCall(String url) {

        // Define a Volley request - in this case a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,                     // HTTP request type
                url,                                    // URL for the request
                null,                                   // JSON Object to send with the request
                new Response.Listener<JSONObject>() {   // Listener for when a response is rec'd
                    @Override
                    public void onResponse(JSONObject response) {
                        WalmartRootObject root = new Gson().fromJson(response.toString(),
                                WalmartRootObject.class);
                        mWalmartItems.clear();
                        mWalmartItems.addAll(root.getItems());
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {          // Listener for when an error occurs
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyUserOfProblem();
                        Log.d(TAG, "onErrorResponse: request failed with status " +
                                error.networkResponse.statusCode);
                        error.printStackTrace();
                    }
                }
        );

        // Set a tag on the new request so it can be cancelled later by referencing the tag
        jsonObjectRequest.setTag(TAG);

        // Cancel any ongoing requests and add the new one to the queue
        mRequestQueue.cancelAll(TAG);
        mRequestQueue.add(jsonObjectRequest);
    }

    private void notifyUserOfProblem() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "There was a problem retrieving network data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
