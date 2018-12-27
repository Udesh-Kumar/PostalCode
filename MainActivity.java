package com.example.cc.postalcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView out;
    Button search;
    double lat,lng; // Ye autocompletFragment me places ka lat,lng lene ke liye


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         out=findViewById(R.id.output);
         search=findViewById(R.id.find);




       PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                lat=place.getLatLng().latitude;
                lng=place.getLatLng().longitude;
                Toast.makeText(MainActivity.this, lat+" "+lng, Toast.LENGTH_SHORT).show();  //lat and lng show krane ke liye
            }

            @Override
            public void onError(Status status) {

            }

        });

        search.setOnClickListener(new View.OnClickListener() {  //api hit on button click
            @Override
            public void onClick(View v) {
                HashMap<String,String> data=new HashMap<>();

                data.put("latlng",lat+","+lng);
                data.put("key","AIzaSyDELpqMi27VwVMB44JliiQG3wSDAYEuG_c"); //

//                Api api=ApiClient.apiclient().create(Api.class);

//
//                Call<Map> call=api.Data

                com.example.cc.postalcode.Api api=ApiClient.apiclient().create(com.example.cc.postalcode.Api.class); //kiska code lena he he Api me se wo bhi check kerke
                Call<Map> call=api.Data(data);
                call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response){
                        Gson gson=new Gson();                           //Gson library is used to turn java obj into json formate(Serialization) && json to java object(Deserialization)
                        String json=gson.toJson(response.body());       //turn into the jsons objects



                        try {

                          JSONObject  jsonObject = new JSONObject(json);
                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("OK")){
                                JSONArray array=jsonObject.getJSONArray("results");
                                //out.setText(array.length());

                                JSONArray array1=array.getJSONObject(0).getJSONArray("address_components");


                                String data=array1.getJSONObject(array1.length()-1).getString("long_name");// -1 taaki last waale me se uthaye

                                Log.d( "onResponse: ", data);
                                out.setText(data);

                                //Isko hum attach debugger kerke bhi ker skte the
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }




                    @Override
                    public void onFailure(Call<Map> call, Throwable t) {

                    }
                });

            }
        });




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
//Asynctask manual passing he taaki null pointer excetion na aa jaye
//Register or login or Api hit kisi se bhi ho jayegi

// first we run the app then we add the debbugger or kya check krna usper click krenge uska data show hone lagega
//or evaluate krne ke liye select code and right click evaluate expression
