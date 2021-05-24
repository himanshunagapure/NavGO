package com.gcoen.navgo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "abc";
    Spinner spDestination;
    String qrID=null;
    RelativeLayout relativeLayout;
    View currentLocation;
    String selectedLocationIdDest;
    String [] shortestPath;

    GraphicOverlay graphicOverlay;
    Button btnGetPath;
    View currentLocImg, destinationLocImg;
    NodeWeighted[] vertices;
    GraphWeighted graphWeighted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetPath = findViewById(R.id.btn_get_path);
        relativeLayout = findViewById(R.id.relativeLayout);
        spDestination = findViewById(R.id.sp_destination_loc);
        graphicOverlay = findViewById(R.id.graphic_overlay);

        //Get location value from Qrcode scanning
        Intent intent = getIntent();
        if (intent != null) {
            qrID = intent.getExtras().get("current_location").toString();
        } else {
            Toast.makeText(this, "Location not detected.Scan again", Toast.LENGTH_SHORT).show();
        }

        //Qrcode value comparison with position: Get current Location
        getCurrentLocation(qrID);

        //Spinner
        String[] locationArray = new String[]{"-------Choose Destination----", "Room 1", "Room 2", "Room 3", "Room 4", "Lab1-Room 5", "Lab2-Room 6", "Room 7", "Room 8", "Room 9", "Room 10", "Toilet-Room 11", "Room 12", "HOD Office-Room 13", "Room 14", "Room 15", "Room 16", "Room 17", "Toilet-Room 18"};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(locationArray));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestination.setAdapter(arrayAdapter);

        getResourceData();

        ///////////////////////////////////////////////////////////

        spDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected Destination: " + selectedLocation, Toast.LENGTH_LONG).show();
                String[] roomNumber = selectedLocation.split(" ");
                selectedLocationIdDest = "Q" + roomNumber[roomNumber.length - 1];
                Log.i("Location selected", selectedLocationIdDest);
                getDestination(selectedLocationIdDest);

                NodeWeighted currentPos = null;
                NodeWeighted destinationPos = null;
                for (NodeWeighted v : vertices) {
                    if (v.getName().equals(qrID)) {
                        currentPos = v;
                    }
                }

                for (NodeWeighted v : vertices) {
                    if (v.getName().equals(selectedLocationIdDest)) {
                        destinationPos = v;
                    }
                }

                if (currentPos!=null && destinationPos!=null) {
                    graphWeighted.resetNodesVisited();
                    String path = graphWeighted.DijkstraShortestPath(currentPos, destinationPos);
                    Log.d(TAG, "shortest_path: " + path);
                    shortestPath = path.split(" ");
                    Log.d(TAG, "shortest_path2: " + Arrays.toString(shortestPath));
                }else {
                    Toast.makeText(MainActivity.this, "Select Destination", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnGetPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphicOverlay.clear();
                ArrayList<View> childArray = new ArrayList<>();

                for (int j = 0; j < shortestPath.length ; j++) {
                    for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                        View child = relativeLayout.getChildAt(i);
                        if (shortestPath[j].equals(child.getTag())) {
                            childArray.add(child);
                            Log.d(TAG, "shortest_childArray: " + child.getTag());

                        }
                    }
                }

                if (childArray.size() != 0) {
                    for (int j = 0; j < childArray.size()-1; j++) {
                        childArray.get(j).setVisibility(View.VISIBLE);
                        Log.d(TAG, "shortest_path3: " + Arrays.toString(shortestPath));
                        int[] location_curr = new int[2];
                        childArray.get(j).getLocationOnScreen(location_curr);
                        Log.d(TAG, "curr_0: " + location_curr[0]);
                        Log.d(TAG, "curr_1: " + location_curr[1]);
                        Log.d(TAG, "shortest_tag1: " + childArray.get(j).getTag());
                        int[] location_dest = new int[2];

                        childArray.get(j+1).getLocationOnScreen(location_dest);
                        Log.d(TAG, "curr_0: " + location_dest[0]);
                        Log.d(TAG, "curr_1: " + location_dest[1]);
                        Log.d(TAG, "shortest_path4: " + Arrays.toString(shortestPath));
                        Log.d(TAG, "shortest_tag2: " + childArray.get(j+1).getTag());
                        if (location_curr.length != 0 && location_dest.length != 0) {
                            GraphicOverlay.Graphic textGraphic = new OcrGraphic(graphicOverlay, (float) location_curr[0], (float) location_curr[1], (float) location_dest[0], (float) location_dest[1]);
                            graphicOverlay.add(textGraphic);
                        }
                    }
                }
            }
        });

    }



    public void goToScanner(View view){
        Intent intent = new Intent(this,ScannedBarcodeActivity.class);
        startActivity(intent);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void getCurrentLocation(String ID){
        for(int i = 0; i < relativeLayout.getChildCount(); i++) {
            View child = relativeLayout.getChildAt(i);
            Log.d(TAG, "onCreate: "+child);

            if(child.getTag().equals(String.valueOf(ID))){
                child.setVisibility(View.VISIBLE);
                currentLocation = child;
                currentLocImg = child;

            }else {
                child.setVisibility(View.INVISIBLE);
            }
            Log.d(TAG, "onCreate_noID: "+child.getTag());

        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void getDestination(String ID){
        for(int i = 0; i < relativeLayout.getChildCount(); i++) {

            View child = relativeLayout.getChildAt(i);

            if(child.getTag().equals(String.valueOf(ID))) {

                if(child == currentLocation){
                    Toast.makeText(this,"Destination cannot be same as current Location",Toast.LENGTH_SHORT).show();
                }else{

                    Glide
                            .with(this)
                            .load(R.drawable.ic_destination)
                            .centerCrop()
                            .into((ImageView) child);

                    child.setVisibility(View.VISIBLE);

                    destinationLocImg = child;

                    graphicOverlay.clear();
                }
            }else {
                child.setVisibility(View.INVISIBLE);
            }
            currentLocation.setVisibility(View.VISIBLE);

        }
    }

    private void getResourceData() {
        graphWeighted = new GraphWeighted(true);
        NodeWeighted v1 = new NodeWeighted(0, "Q1");
        NodeWeighted v2 = new NodeWeighted(1, "Q2");
        NodeWeighted v3 = new NodeWeighted(2, "Q3");
        NodeWeighted v4 = new NodeWeighted(3, "Q4");
        NodeWeighted v5 = new NodeWeighted(4, "Q5");
        NodeWeighted v6 = new NodeWeighted(5, "Q6");
        NodeWeighted v7 = new NodeWeighted(6, "Q7");
        NodeWeighted v8 = new NodeWeighted(7, "Q8");
        NodeWeighted v9 = new NodeWeighted(8, "Q9");
        NodeWeighted v10 = new NodeWeighted(9, "Q10");
        NodeWeighted v11 = new NodeWeighted(10, "Q11");
        NodeWeighted v12 = new NodeWeighted(11, "Q12");
        NodeWeighted v13 = new NodeWeighted(12, "Q13");
        NodeWeighted v14 = new NodeWeighted(13, "Q14");
        NodeWeighted v15 = new NodeWeighted(14, "Q15");
        NodeWeighted v16 = new NodeWeighted(15, "Q16");
        NodeWeighted v17 = new NodeWeighted(16, "Q17");
        NodeWeighted v18 = new NodeWeighted(17, "Q18");

        vertices = new NodeWeighted[]{ v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18};

        // Our addEdge method automatically adds Nodes as well.
        // The addNode method is only there for unconnected Nodes,
        // if we wish to add any
        graphWeighted.addEdge(v1,v2, 2);
        graphWeighted.addEdge(v1, v18, 3);
        graphWeighted.addEdge(v1, v17, 2);
        graphWeighted.addEdge(v2, v1, 2);
        graphWeighted.addEdge(v2, v3, 2);
        graphWeighted.addEdge(v2, v16, 2);
        graphWeighted.addEdge(v3, v2, 2);
        graphWeighted.addEdge(v3, v4, 1);
//        graphWeighted.addEdge(v3, v16, 3);
        graphWeighted.addEdge(v4, v3, 1);
        graphWeighted.addEdge(v4, v5, 3);
//        graphWeighted.addEdge(v4, v15, 3);
        graphWeighted.addEdge(v5, v4, 2);
        graphWeighted.addEdge(v5, v6, 2);
        graphWeighted.addEdge(v5,v15, 2);
        graphWeighted.addEdge(v6, v5, 2);
        graphWeighted.addEdge(v6, v7, 2);
        graphWeighted.addEdge(v6, v14, 2);
        graphWeighted.addEdge(v7, v6, 2);
        graphWeighted.addEdge(v7, v8, 1);
//        graphWeighted.addEdge(v7, v14, 3);
        graphWeighted.addEdge(v8, v7, 1);
        graphWeighted.addEdge(v8, v9, 2);
//        graphWeighted.addEdge(v8, v13, 3);
        graphWeighted.addEdge(v9, v8, 2);
        graphWeighted.addEdge(v9, v10, 2);
        graphWeighted.addEdge(v9, v13, 2);
        graphWeighted.addEdge(v10, v9, 2);
        graphWeighted.addEdge(v10, v11, 3);
        graphWeighted.addEdge(v10, v12, 2);
        graphWeighted.addEdge(v11,v10, 3);
        graphWeighted.addEdge(v11,v12, 2);
        graphWeighted.addEdge(v12, v11, 2);
        graphWeighted.addEdge(v12, v13, 2);
        graphWeighted.addEdge(v12, v10, 2);
        graphWeighted.addEdge(v13, v12, 2);
        graphWeighted.addEdge(v13, v14, 1);
        graphWeighted.addEdge(v13, v9, 2);
//        graphWeighted.addEdge(v13, v8, 3);
        graphWeighted.addEdge(v14, v13, 1);
        graphWeighted.addEdge(v14, v15, 2);
//        graphWeighted.addEdge(v14, v7, 3);
        graphWeighted.addEdge(v15, v14, 2);
        graphWeighted.addEdge(v15, v16, 1);
        graphWeighted.addEdge(v15, v5, 2);
//        graphWeighted.addEdge(v15, v4, 3);
        graphWeighted.addEdge(v16, v15, 1);
        graphWeighted.addEdge(v16, v17, 2);
//        graphWeighted.addEdge(v16, v3, 3);
        graphWeighted.addEdge(v16, v2, 2);
        graphWeighted.addEdge(v17, v16, 2);
        graphWeighted.addEdge(v17, v18, 2);
        graphWeighted.addEdge(v17, v1, 2);
        graphWeighted.addEdge(v18, v1, 3);

    }
}