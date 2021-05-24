package com.gcoen.navgo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    HorizontalScrollView scrollView;
    ImageView imageView28;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView28 = findViewById(R.id.img_28);
        scrollView = findViewById(R.id.zoom_linear_layout);
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
        String[] locationArray = new String[]{"-------Choose Destination----", "1 :Principal Cabin", "2 :Room", "3 :Chemistry Lab","4 :Office" , "5 :Room", "6 :HOD Cabin", "7 :Boys Toilet", "8 :Room", "9 :Room", "10 :Workshop HOD", "11 :Room", "12 :Girls Toilet", "13 :Workshop Lab1", "14 :Workshop Lab2", "15 :Civil Lab", "16 :Mechanic Lab", "17 :Civil HOD", "18 :Auditorium","19 :Boys Toilet","20 :Girls Toilet","21 :CSE HOD","22 :Lab","23 :Room","24 :Room","25 :Room","26 :Room","27 :Toilet","28 :Room","29 :Room","30 :Lab","31 :Lab","32 :Staff","33 :Server","34 :Staff","35 :Lab","36 :Lab","37 :Room","38 :Entry Gate1","39 :Main Gate","40 :Entry Gate","41 :Parking1","42 :Parking2","43 :Ground","44 :Mess","45 :Girls Hostel","46 :Canteen","47 :Boys Hostel"};
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
                selectedLocationIdDest = "Q" + roomNumber[0];
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

                ArrayList<View> childArray = new ArrayList<>();
                if(shortestPath!=null){
                    for (String s : shortestPath) {
                        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                            View child = relativeLayout.getChildAt(i);
                            if (s.equals(child.getTag())) {
                                childArray.add(child);
                                Log.d(TAG, "shortest_childArray: " + child.getTag());

                            }
                        }
                    }
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
                if(shortestPath!=null){
                    for (String s : shortestPath) {
                        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                            View child = relativeLayout.getChildAt(i);
                            if (s.equals(child.getTag())) {
                                childArray.add(child);
                                Log.d(TAG, "shortest_childArray: " + child.getTag());

                            }
                        }
                    }
                }

                //Get coordinates of nodes
                if (childArray.size() != 0) {
                    for (int j = 0; j < childArray.size()-1; j++) {
//                        childArray.get(j).setVisibility(View.VISIBLE);
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
                            GraphicOverlay.Graphic textGraphic = new Graphics(graphicOverlay, (float) location_curr[0], (float) location_curr[1], (float) location_dest[0], (float) location_dest[1]);
                            graphicOverlay.add(textGraphic);
                        }
                    }
                }
                //Scroll the map when path is drawn
                if(childArray.size()!=0){
                    int[] location_curr = new int[2];
                    childArray.get(childArray.size()-1).getLocationOnScreen(location_curr);
                    scrollView.scrollTo(location_curr[0],location_curr[1]);
                }

            }
        });

    }



    public void goToScanner(View view){
        Intent intent = new Intent(this,ScannedBarcodeActivity.class);
        startActivity(intent);
    }
    public void exitFromApp(View view){
        finish();
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
        NodeWeighted v19 = new NodeWeighted(18, "Q19");
        NodeWeighted v20 = new NodeWeighted(9, "Q20");
        NodeWeighted v21 = new NodeWeighted(20, "Q21");
        NodeWeighted v22 = new NodeWeighted(21, "Q22");
        NodeWeighted v23 = new NodeWeighted(22, "Q23");
        NodeWeighted v24 = new NodeWeighted(23, "Q24");
        NodeWeighted v25 = new NodeWeighted(24, "Q25");
        NodeWeighted v26 = new NodeWeighted(25, "Q26");
        NodeWeighted v27 = new NodeWeighted(26, "Q27");
        NodeWeighted v28 = new NodeWeighted(27, "Q28");
        NodeWeighted v29 = new NodeWeighted(28, "Q29");
        NodeWeighted v30 = new NodeWeighted(29, "Q30");
        NodeWeighted v31 = new NodeWeighted(30, "Q31");
        NodeWeighted v32 = new NodeWeighted(31, "Q32");
        NodeWeighted v33 = new NodeWeighted(32, "Q33");
        NodeWeighted v34 = new NodeWeighted(33, "Q34");
        NodeWeighted v35 = new NodeWeighted(34, "Q35");
        NodeWeighted v36 = new NodeWeighted(35, "Q36");
        NodeWeighted v37 = new NodeWeighted(36, "Q37");
        /////////
        NodeWeighted v38 = new NodeWeighted(37, "Q38");
        NodeWeighted v39 = new NodeWeighted(38, "Q39");
        NodeWeighted v40 = new NodeWeighted(39, "Q40");
        NodeWeighted v41 = new NodeWeighted(40, "Q41");
        NodeWeighted v42 = new NodeWeighted(41, "Q42");
        NodeWeighted v43 = new NodeWeighted(42, "Q43");
        NodeWeighted v44 = new NodeWeighted(43, "Q44");
        NodeWeighted v45 = new NodeWeighted(44, "Q45");
        NodeWeighted v46 = new NodeWeighted(45, "Q46");
        NodeWeighted v47 = new NodeWeighted(46, "Q47");
        //////////////////
        NodeWeighted v48 = new NodeWeighted(47, "Q48");   //30,31
        NodeWeighted v49 = new NodeWeighted(48, "Q49");   //34,35
        NodeWeighted v50 = new NodeWeighted(49, "Q50");   //stairs
        NodeWeighted v51 = new NodeWeighted(50, "Q51");   //boysHostel,stairs
        NodeWeighted v52 = new NodeWeighted(51, "Q52");   //17,exit
        NodeWeighted v53 = new NodeWeighted(52, "Q53");   //17,18
        NodeWeighted v54 = new NodeWeighted(53, "Q54");   //18,1
        NodeWeighted v55 = new NodeWeighted(54, "Q55");   //1,2
        NodeWeighted v56 = new NodeWeighted(55, "Q56");   //7,8
        NodeWeighted v57 = new NodeWeighted(56, "Q57");   //11,12
        NodeWeighted v58 = new NodeWeighted(57, "Q58");   //9,10
        NodeWeighted v59 = new NodeWeighted(58, "Q59");   //9,10,canteen
        NodeWeighted v60 = new NodeWeighted(59, "Q60");   //Parking gate ,mess
        NodeWeighted v61 = new NodeWeighted(60, "Q61");   //parking1,7,8
        NodeWeighted v62 = new NodeWeighted(61, "Q62");   //boysHostel,stairs,16
        NodeWeighted v63 = new NodeWeighted(62, "Q63");   //Annexgate,mainGate


        vertices = new NodeWeighted[]{ v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19,v20, v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v31, v32, v33, v34, v35, v36, v37, v38, v39, v40, v41, v42, v43, v44, v45, v46, v47, v48, v49, v50, v51, v52, v53, v54, v55, v56, v57, v58, v59, v60 ,v61, v62, v63};

        // Our addEdge method automatically adds Nodes as well.
        // The addNode method is only there for unconnected Nodes,
        // if we wish to add any
        //Main Building
        graphWeighted.addEdge(v1,v55, 1);
        graphWeighted.addEdge(v1, v54, 2);
        graphWeighted.addEdge(v2, v55, 1);
        graphWeighted.addEdge(v2, v3, 2);
        graphWeighted.addEdge(v3, v2, 2);
        graphWeighted.addEdge(v4, v55, 4);
        graphWeighted.addEdge(v4, v8, 4);
        graphWeighted.addEdge(v5,v6, 2);
        graphWeighted.addEdge(v6, v5, 2);
        graphWeighted.addEdge(v6, v7, 2);
        graphWeighted.addEdge(v7, v6, 2);
        graphWeighted.addEdge(v7, v61, 3);
        graphWeighted.addEdge(v7, v56, 1);
        graphWeighted.addEdge(v8, v56, 1);
        graphWeighted.addEdge(v8, v9, 2);
        graphWeighted.addEdge(v8, v4, 4);
        graphWeighted.addEdge(v9, v8, 2);
        graphWeighted.addEdge(v9, v58, 3);
        graphWeighted.addEdge(v10, v58, 3);
        graphWeighted.addEdge(v10, v11, 2);
        graphWeighted.addEdge(v11,v10, 2);
        graphWeighted.addEdge(v11,v57, 1);
        graphWeighted.addEdge(v12, v57, 1);
        graphWeighted.addEdge(v12, v13, 2);
        graphWeighted.addEdge(v13, v12, 2);
        graphWeighted.addEdge(v13, v14, 2);
        graphWeighted.addEdge(v14, v13, 2);
        graphWeighted.addEdge(v15, v57, 4);
        graphWeighted.addEdge(v15, v53, 4);
        graphWeighted.addEdge(v16, v17, 2);
        graphWeighted.addEdge(v17, v16, 2);
        graphWeighted.addEdge(v17, v53, 1);
        graphWeighted.addEdge(v17, v52, 2);
        graphWeighted.addEdge(v18, v53, 1);
        graphWeighted.addEdge(v18, v54, 2);
        //Main Building mid points
        graphWeighted.addEdge(v54, v1, 2);
        graphWeighted.addEdge(v54, v18, 2);
        graphWeighted.addEdge(v54, v39, 5);
        graphWeighted.addEdge(v55, v1, 1);
        graphWeighted.addEdge(v55, v2, 1);
        graphWeighted.addEdge(v55, v4, 4);
        graphWeighted.addEdge(v56, v7, 1);
        graphWeighted.addEdge(v56, v8, 1);
        graphWeighted.addEdge(v58, v9, 3);
        graphWeighted.addEdge(v58, v10, 3);
        graphWeighted.addEdge(v58, v59, 3);
        graphWeighted.addEdge(v57, v11, 1);
        graphWeighted.addEdge(v57, v12, 1);
        graphWeighted.addEdge(v57, v15, 4);
        graphWeighted.addEdge(v53, v18, 1);
        graphWeighted.addEdge(v53, v52, 3);
        graphWeighted.addEdge(v53, v17, 1);
        graphWeighted.addEdge(v53, v15, 4);
        graphWeighted.addEdge(v52, v17, 2);
        graphWeighted.addEdge(v52, v53, 3);
        graphWeighted.addEdge(v52, v51, 4);

        //between main building and annexe building
        graphWeighted.addEdge(v51, v52, 4);
        graphWeighted.addEdge(v51, v50, 4);
        graphWeighted.addEdge(v51, v63, 4);
        graphWeighted.addEdge(v51, v62, 10);

        //Annex Building
        graphWeighted.addEdge(v50, v37, 2);
        graphWeighted.addEdge(v50, v19, 2);
        graphWeighted.addEdge(v50, v20, 2);
        graphWeighted.addEdge(v50, v51, 4);
        graphWeighted.addEdge(v37, v50, 2);
        graphWeighted.addEdge(v37, v36, 2);
        graphWeighted.addEdge(v37, v21, 3);
        graphWeighted.addEdge(v36, v37, 2);
        graphWeighted.addEdge(v36, v35, 2);
        graphWeighted.addEdge(v36, v22, 3);
        graphWeighted.addEdge(v36, v35, 2);
        graphWeighted.addEdge(v35, v36, 2);
        graphWeighted.addEdge(v35, v49, 1);
        graphWeighted.addEdge(v34, v49, 1);
        graphWeighted.addEdge(v34, v33, 2);
        graphWeighted.addEdge(v33, v34, 2);
        graphWeighted.addEdge(v33, v32, 2);
        graphWeighted.addEdge(v33, v23, 3);
        graphWeighted.addEdge(v32, v33, 2);
        graphWeighted.addEdge(v32, v24, 3);
        graphWeighted.addEdge(v32, v31, 2);
        graphWeighted.addEdge(v31, v48, 1);
        graphWeighted.addEdge(v31, v32, 2);
        graphWeighted.addEdge(v30, v48, 1);
        graphWeighted.addEdge(v30, v29, 2);
        graphWeighted.addEdge(v30, v25, 3);
        graphWeighted.addEdge(v29, v30, 2);
        graphWeighted.addEdge(v29, v28, 2);
        graphWeighted.addEdge(v28, v26, 2);
        graphWeighted.addEdge(v28, v27, 3);
        graphWeighted.addEdge(v27, v28, 3);
        graphWeighted.addEdge(v26, v27, 2);
        graphWeighted.addEdge(v26, v25, 2);
        graphWeighted.addEdge(v25, v48, 1);
        graphWeighted.addEdge(v25, v26, 2);
        graphWeighted.addEdge(v24, v48, 3);
        graphWeighted.addEdge(v24, v23, 2);
        graphWeighted.addEdge(v24, v32, 3);
        graphWeighted.addEdge(v28, v26, 2);
        graphWeighted.addEdge(v23, v24, 2);
        graphWeighted.addEdge(v23, v33, 3);
        graphWeighted.addEdge(v23, v49, 3);
        graphWeighted.addEdge(v22, v49, 3);
        graphWeighted.addEdge(v22, v21, 2);
        graphWeighted.addEdge(v22, v36, 3);
        graphWeighted.addEdge(v21, v22, 2);
        graphWeighted.addEdge(v21, v20, 2);
        graphWeighted.addEdge(v21, v37, 3);
        graphWeighted.addEdge(v20, v21, 2);
        graphWeighted.addEdge(v20, v19, 1);
        graphWeighted.addEdge(v20, v50, 2);
        graphWeighted.addEdge(v19, v20, 1);
        graphWeighted.addEdge(v19, v50, 2);
        //Annex Building mid points
        graphWeighted.addEdge(v48, v30, 1);
        graphWeighted.addEdge(v48, v31, 1);
        graphWeighted.addEdge(v48, v25, 1);
        graphWeighted.addEdge(v48, v24, 3);
        graphWeighted.addEdge(v49, v34, 1);
        graphWeighted.addEdge(v49, v35, 1);
        graphWeighted.addEdge(v49, v22, 3);
        //surrounding spots
        graphWeighted.addEdge(v47, v62, 4);
        graphWeighted.addEdge(v47, v46, 4);
        graphWeighted.addEdge(v46, v47, 4);
        graphWeighted.addEdge(v46, v44, 4);
        graphWeighted.addEdge(v46, v59, 4);
        graphWeighted.addEdge(v44, v45, 2);
        graphWeighted.addEdge(v44, v46, 4);
        graphWeighted.addEdge(v44, v60, 6);
        graphWeighted.addEdge(v41, v61, 2);
        graphWeighted.addEdge(v41, v60, 2);
        graphWeighted.addEdge(v43, v42, 2);
        graphWeighted.addEdge(v42, v43, 2);
        graphWeighted.addEdge(v42, v60, 4);
        graphWeighted.addEdge(v40, v60, 4);
        graphWeighted.addEdge(v40, v39, 5);
        graphWeighted.addEdge(v39, v40, 5);
        graphWeighted.addEdge(v39, v54, 4);
        graphWeighted.addEdge(v39, v63, 4);
        graphWeighted.addEdge(v38, v63, 4);
        //Surounding mid points
        graphWeighted.addEdge(v62, v51, 10);
        graphWeighted.addEdge(v62, v47, 4);
        graphWeighted.addEdge(v59, v46, 4);
        graphWeighted.addEdge(v59, v58, 3);
        graphWeighted.addEdge(v61, v41, 2);
        graphWeighted.addEdge(v61, v7, 3);
        graphWeighted.addEdge(v60, v41, 2);
        graphWeighted.addEdge(v60, v40, 4);
        graphWeighted.addEdge(v60, v42, 4);
        graphWeighted.addEdge(v63, v39, 4);
        graphWeighted.addEdge(v63, v38, 4);
        graphWeighted.addEdge(v63, v51, 4);


    }


}