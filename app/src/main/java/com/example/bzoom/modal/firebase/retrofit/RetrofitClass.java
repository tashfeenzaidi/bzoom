package com.example.bzoom.modal.firebase.retrofit;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.bzoom.Car;
import com.example.bzoom.CarType;
import com.example.bzoom.Constants;
import com.example.bzoom.MainActivity;
import com.example.bzoom.OwnerBelongings;
import com.example.bzoom.R;
import com.example.bzoom.ReasonEndRide;
import com.example.bzoom.Utility.General;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.owner.Owner;
import com.example.bzoom.modules.map.Complaint;
import com.example.bzoom.modules.map.GoogleDirectionApi;
import com.example.bzoom.modules.map.GoogleDistanceMatrix;
import com.example.bzoom.modules.map.Rating;
import com.example.bzoom.modules.map.chauffeur.Chauffeur;
import com.example.bzoom.modules.map.driver.Driver;
import com.example.bzoom.modules.map.driver.Vehical;
import com.example.bzoom.modules.map.rider.Rider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class RetrofitClass {
    static String res;
    static String userID;
    private static String rideClass;
    Retrofit retrofit;
    static String role;
    public static String baseUrl = "http://www.thebzoom.com/api";
    private static boolean isExist;
    static boolean numberExist ;
    public RetrofitClass(){

    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static void postRest(RequestBody formBody,String endPoint ) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();

//                RequestBody formBody = new FormBody.Builder()
//                        .add("email", "admin@admin.com")
//                        .add("password", "password")
//                        .build();

                Request request = new Request.Builder()
                        .url(baseUrl+endPoint)
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        // Do something with the response.
                        res = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String email =  data.getString("email");
                            String password =  data.getString("password");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

    }

    public static List<CarType> getRest(String endPoint  ) {

        List<CarType> carTypes = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                // Do something with the response.
                res = response.body().string();
                try {


                    JSONObject jsonResponse = new JSONObject(res);
                    JSONArray cast = jsonResponse.getJSONArray("data");
                    CarType.carTypes = cast;
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        CarType carType = new CarType(actor.getInt("id"),actor.getString("icon"),254,"7mim",actor.getString("name"),actor.getString("topic_name"));
                        carTypes.add(carType);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return carTypes;
    }

    public static void postSignUp(String endPoint ) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", General.getUserName())
                .add("contact_number", General.getUserNumber())
                .add("email", General.getUserEmail())
                .add("token", General.getUserToken())
                .add("password", General.getUserPassword())
                .add("u_id", General.getUserUid())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject data = jsonObject.getJSONObject("data");
                    General.setUserid( data.getString("id"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String postLogin(String endPoint ) {
        OkHttpClient client = new OkHttpClient();

        String pass= General.getUserPassword();
        String email= General.getUserEmail();String role= General.getRole();
        RequestBody formBody = new FormBody.Builder()
                .add("email", General.getUserEmail())
                .add("password", General.getUserPassword())
                .add("role", General.getRole())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();
                if (!res.equals("not fount")){
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject data = jsonObject.getJSONObject("data");
                        res = data.getString("id");
                        General.setUserid(res);
                        General.setUserName( data.getString("name"));
                        General.setUserNumber( data.getString("contact_number"));
                        General.setUserEmail( data.getString("email"));
                        General.setImageUrl( data.getString("image_name"));
                        General.setImageUrl( data.getString("image_name"));
                        General.setImagePath( data.getString("image_path"));
                        General.setUserTopicName( data.getString("topic_name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static boolean postEmail(String endPoint ) {


        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("email", General.getUserEmail())
                        .build();

                Request request = new Request.Builder()
                        .url(baseUrl+endPoint)
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        // Do something with the response.
                        res = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(res);
                            JSONObject cast = jsonResponse.getJSONObject("body");
                            if (cast.getString("exist").equals("true")){
                                isExist = true;
                            }else {
                                isExist = false;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();


                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();
    return isExist;
    }

    public static String postRide(String endPoint ){

        OkHttpClient client = new OkHttpClient();
        String isCab;
        boolean success = false;
        if (Ride.isIsCab()){
            isCab = "1";
        }else {
            isCab = "0";
        }


        String pickLat =  Ride.getLat();
        String pickLon=  Ride.getLon();
        String dropLat =  Ride.getLatDrop();
        String dropLon =  Ride.getLonDrop();
        String customer_id =  General.getUserid();
        String v_type =  String.valueOf(CarType.activeCarId);
        RequestBody formBody = new FormBody.Builder()
                .add("pick_latitude", Ride.getLat())
                .add("pick_longitude", Ride.getLon())
                .add("drop_latitude", Ride.getLatDrop())
                .add("drop_longitude", Ride.getLonDrop())
                .add("customer_id", General.getUserid())
                .add("is_cab",isCab )
                .add("v_type", String.valueOf(CarType.activeCarId))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            rideClass = response.body().string();

            if (response.isSuccessful()){
                // Do something with the response.
                JSONObject jsonObject = new JSONObject(rideClass);
                JSONObject data = jsonObject.getJSONObject("data");
                rideClass = data.getString("id");
                Ride.setRideID(rideClass);
                Ride.jsonObject = data;
                success = true;
            }else {
                res = "not fount";
                success = false;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return rideClass;
    }

    public static boolean verifyEmail(String endPoint , String email) {

        OkHttpClient client = new OkHttpClient();

        role = General.getRole();
        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("role", role)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("body");
                    if (cast.getString("exist").equals("true")){
                        isExist = true;
                    }else {
                        isExist = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static void updateUid(String endPoint, String id ) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("u_id", General.getUserUid())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+id)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject data = jsonObject.getJSONObject("data");
                    General.setUserid( data.getString("id"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static boolean postPhoneNumber(String endPoint ) {
        numberExist = false;
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("number", General.getUserNumber())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("body");
                    if (cast.getString("exist").equals("true")){
                        numberExist = true;
                    }else {
                        numberExist = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberExist;
    }

    public static boolean getDriver(String endPoint,String id ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+id)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                isExist = true;
                // Do something with the response.
                res = response.body().string();
                try {


                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject data1 = jsonResponse.getJSONObject("data");
                    Driver.driver = data1;
                    Driver.id = data1.getInt("id");
                    Driver.carType = data1.getInt("vt_id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    return isExist;
    }

    public static ArrayList<Car> getDriverVehicalData(String endPoint,String id ) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+id)
                .get()
                .build();
        ArrayList<Car> cars = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                // Do something with the response.
                res = response.body().string();
                try {

                    JSONObject jsonResponse = new JSONObject(res);


                    if (endPoint.equals("/get-owner-vehicals")){
                        JSONArray carList = jsonResponse.getJSONArray("data");

                        for (int i=0;i<carList.length();i++){
                            JSONObject car = carList.getJSONObject(i);
                            Car vehical = new Car(
                                    car.getString("company_name"),
                                    2.5f,
                                    car.getString("plate_number"),
                                    car.getString("color"),
                                    1,
                                    car.getString("model"),
                                    car.getInt("id"),
                                    car.getInt("vt_id"),
                                    "img");
                            cars.add(vehical);
                        }

                        Car.cars = new ArrayList<>();
                        Car.cars.addAll(cars);
                        return cars;

                    }else {
                        JSONObject data1 = jsonResponse.getJSONObject("data");
                        Vehical.vehicalData = data1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public static boolean driverRideAccept(String endPoint ) {
        int pickLat = 0;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;

        if (General.getRole().equals("chauffeur")){
            pickLat = AvailableCar.activeVehicalId;
        }else {
            try {
                pickLat =  Vehical.vehicalData.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String custor_id =  String.valueOf(pickLat);
        String custor_i =  General.getUserid();
        String v_ype = Ride.getRideID();
        String pickLon=  GoogleDirectionApi.getDistance().replace("m", "");
        String dropLat = GoogleDirectionApi.getDuration().replace("min", "");
        String customer_id =  String.valueOf(Driver.getDriverLat());
        String v_type =  String.valueOf(Driver.getDriverLon());
        formBody = new FormBody.Builder()
                .add("vehical_id", String.valueOf(pickLat))
                .add("driver_id", General.getUserid())
                .add("ride_id",Ride.getRideID())
                .add("driver_lat",String.valueOf(Driver.getDriverLat()) )
                .add("driver_lon",String.valueOf(Driver.getDriverLon()))
                .add("driver_distance", GoogleDirectionApi.getDistance().replace("km", "").trim())
                .add("driver_time",     GoogleDirectionApi.getDuration().replace("mins", "").trim())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                isExist =true;
                // Do something with the response.
                res = response.body().string();
                try {

                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject data1 = jsonResponse.getJSONObject("data");
                    Ride.jsonObject = data1;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                isExist =false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return isExist;
    }

    public static void driverOnTheWay(String endPoint ) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("ride_id",Ride.jsonObject.getString("id"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void driverRideStart(String endPoint ) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("ride_id",Ride.jsonObject.getString("id"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            res = response.body().string();
            try {


                JSONObject jsonResponse = new JSONObject(res);
                JSONObject data1 = jsonResponse.getJSONObject("data");
                Ride.jsonObject = data1;


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void driverArrived(String endPoint ) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("ride_id",Ride.jsonObject.getString("id"))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void collectedAmount(String endPoint ) {

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("ride_id",Ride.jsonObject.getString("id"))
                    .add("price",String.valueOf(Ride.getFair()))
                    .add("collected_amount",String.valueOf( Ride.getAmountCollected()))
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getReasons(String endPoint,String isCab, String role  ) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+isCab+"/"+role)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            isExist = false;

            if (response.isSuccessful()){

                List<String> allNames = new ArrayList<String>();
                // Do something with the response.
                res = response.body().string();
                try {

                    isExist = true;
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONArray cast = jsonResponse.getJSONArray("data");
                    ReasonEndRide.reasonEndRides = cast;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean rideCancel(String endPoint,int status, String bodyTitle) {

        isExist= false;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;

            formBody = new FormBody.Builder()
                    .add("user_id",String.valueOf(General.getUserid()))
                    .add("ride_id",String.valueOf(Ride.getRideID()))
                    .add("status", String.valueOf(status))
                    .add("body_title", bodyTitle)
                    .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                isExist = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean rideRating(String endPoint) {

        isExist= false;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;

        formBody = new FormBody.Builder()
                .add("available_id",String.valueOf(AvailableCar.activeAvailableId))
                .add("user_id",String.valueOf(General.getUserid()))
                .add("rating",String.valueOf(Rating.getRating()))
                .add("comment",String.valueOf(Rating.getComment()))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                isExist = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean normalRideRating(String endPoint) {

        isExist= false;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("ride_id",String.valueOf(Ride.getRideID()))
                .add("user_id",String.valueOf(General.getUserid()))
                .add("rating",String.valueOf(Rider.getRatingBar()))
                .add("comment",String.valueOf(Rider.getComment()))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                // Do something with the response.
                isExist = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean rideEnd(String endPoint) {

        isExist= false;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = null;

        formBody = new FormBody.Builder()
                .add("ride_id",       String.valueOf(Ride.getRideID()))
                .add("reason_id",     String.valueOf(ReasonEndRide.getId()))
                .add("rider_time",    GoogleDirectionApi.getDistance())
                .add("rider_distance",GoogleDirectionApi.getDuration())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                res = response.body().string();
                // Do something with the response.
                isExist = true;
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    Ride.jsonObject = jsonResponse.getJSONObject("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean postVehical(String endPoint ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();

        String pickLat =  Ride.getLat();
        String pickLon=  Ride.getLon();
        String as=  Ride.getEndDate()  ;
        String sd=  Ride.getEndTime()  ;
        String df=  Ride.getStartDate();
        String fg=  Ride.getStartTime();
        RequestBody formBody = new FormBody.Builder()
                .add("vehical_id", String.valueOf(Car.activeCarId))
                .add("vehical_pickup_long",Ride.getLon())
                .add("vehical_pickup_lat", Ride.getLat())
                .add("end_date",  Ride.getEndDate()  )
                .add("end_time",  Ride.getEndTime()  )
                .add("start_date",Ride.getStartDate())
                .add("start_time",Ride.getStartTime())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                isExist=true;
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("data");
                    JSONObject vehical = cast.getJSONObject("vehical");
                    JSONObject user = vehical.getJSONObject("user");
                    AvailableCar.activeVehicalId =  cast.getInt("vehical_id");
                    AvailableCar.activeAvailableId = cast.getInt("id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean bookCar(String endPoint ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();

//        String pickLat =  String.valueOf( MainActivity.currentLocation.getLongitude());
//        String pickLon=  String.valueOf( MainActivity.currentLocation.getLatitude());
//        String as=  General.getUserid();
//        String sd=  Ride.getEndTime() ;
//        String df= Ride.getEndDate();
        RequestBody formBody = new FormBody.Builder()
                .add("available_id", String.valueOf(AvailableCar.activeAvailableId))
                .add("chauffeur_id",General.getUserid())
                .add("chauffeur_lat", String.valueOf( MainActivity.currentLocation.getLatitude()))
                .add("chauffeur_date_time",  Ride.getEndDate())
                .add("chauffeur_end_time",  Ride.getEndTime())
                .add("chauffeur_long",String.valueOf( MainActivity.currentLocation.getLongitude()))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                isExist=true;
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("data");
                    AvailableCar.activeVehicalId =  cast.getInt("vehical_id");
                    AvailableCar.activeAvailableId = cast.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean getProfile(String endPoint,int id ,String role) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+id)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                isExist = true;
                // Do something with the response.
                res = response.body().string();
                try {


                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject data = jsonResponse.getJSONObject("data");
                    if (role.equals("chauffeur")){

                        Chauffeur.setName(data.getString("name"));
                        Chauffeur.setNumber(data.getString("contact_number"));
                        Chauffeur.setRating(data.getString("rating"));
                        Chauffeur.setProfilePic(data.getString("image_path"));
                    }else if (role.equals("owner")){
                        Owner.setName(data.getString("name"));
                        Owner.setNumber(data.getString("contact_number"));
                        Owner.setRating(data.getString("rating"));
                        Owner.setProfilePic(data.getString("image_path"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean notification(String endPoint,String bodyTitle ,int status ) {

        isExist = false;
        int id =AvailableCar.activeAvailableId;
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("available_id", String.valueOf(AvailableCar.activeAvailableId))
                .add("body_title",bodyTitle)
                .add("status", String.valueOf(status))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                isExist=true;
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("data");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static ArrayList<OwnerBelongings> getAgreementByVehical(String endPoint, int id ,int isReturn) {

        boolean returnStatus;
        if (isReturn == 0){
            returnStatus = true;
        }else {
            returnStatus = false;
        }
        ArrayList<OwnerBelongings> belongings = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+id+"/"+isReturn)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                // Do something with the response.
                res = response.body().string();
                try {


                    JSONObject jsonResponse = new JSONObject(res);
                    JSONArray cast = jsonResponse.getJSONArray("data");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        OwnerBelongings ownerBelongings = new OwnerBelongings();
                        ownerBelongings.setId(actor.getInt("id"));
                        ownerBelongings.setStatus(returnStatus);
                        ownerBelongings.setName(actor.getString("clause"));
                        belongings.add(ownerBelongings);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return belongings;
    }

    public static boolean postComplaint(String endPoint, String title ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();

        int owner = 0;
        if (Complaint.isIsOwner()){
            owner = 1;
        }


        String pickLat = String.valueOf(Complaint.getAvailableId())  ;
        String isOwner = String.valueOf(owner)       ;
        String as      = String.valueOf(Complaint.getDescription())  ;
        String sd      = String.valueOf(Complaint.clauseList)        ;

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("available_id", String.valueOf(Complaint.getAvailableId()))
                .add("is_owner",     String.valueOf(owner))
                .add("body_title",     title)
                .add("description",  String.valueOf(Complaint.getDescription()));

        int index = 0;
        for (int item: Complaint.clauseList) {
            String clause = "clause_id["+index+"]";
            formBuilder.add(clause, String.valueOf(item));
            index++;
        }
        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()){
                isExist=true;
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean getDirection(double startLat, double startLon, double endLat, double endLon ) {


        // Origin of route
        String str_origin = "origin=" + startLat + "," + startLon;

        // Destination of route
        String str_dest = "destination=" + endLat + "," + endLon;

        // Sensor enabled
        String sensor = "sensor=false&alternatives=false&units=metric&mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String MY_API_KEY = "AIzaSyCsG2tYn1NyAAM9vR_IXoIR67ATe8iKZK0" ;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        String data = "";
        isExist = false;
        try {
            data =  downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }



            if (!data.equals("")){
                isExist=true;
                // Do something with the response.

                try {
                    JSONObject jsonResponse = new JSONObject(data);
                    JSONArray route = jsonResponse.getJSONArray("routes");
                    JSONObject object = route.getJSONObject(0);
                    JSONArray legs = object.getJSONArray("legs");
                    JSONObject legsObjects = legs.getJSONObject(0);

//get the distance
                    JSONObject distance = legsObjects.getJSONObject("distance");
                    GoogleDirectionApi.setDistanceText( distance.getString("text"));
                    double dist = distance.getDouble("value");
                    GoogleDirectionApi.setDistance(String.valueOf(dist/1000));

//get the time
                    JSONObject time = legsObjects.getJSONObject("duration");
                    GoogleDirectionApi.setDurationText(time.getString("text"));
                    long durat = time.getLong("value");
                    GoogleDirectionApi.setDuration(String.valueOf(durat/60));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        return isExist;
    }

    @SuppressLint("LongLogTag")
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static boolean getEstimation(String endPoint,String distance, String time, String vType, boolean isCab) {

        int iscab = 0;
        if (isCab){
            iscab=1;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+distance+"/"+time+"/"+vType+"/"+iscab)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            isExist = false;

            if (response.isSuccessful()){


                // Do something with the response.
                res = response.body().string();

                    isExist = true;
                    Ride.setFair(Double.parseDouble(res));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean getAmount(String endPoint,String rideId) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+rideId)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            isExist = false;

            if (response.isSuccessful()){
                // Do something with the response.
                res = response.body().string();

                isExist = true;
                Ride.setFair(Double.parseDouble(res));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean getDriverHomeScreen(String endPoint,String id ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl+endPoint+"/"+id)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){

                isExist = true;
                // Do something with the response.
                res = response.body().string();
                try {


                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject data1 = jsonResponse.getJSONObject("body");
                    Driver.totalMonth = data1.getLong("total_months");
                    Driver.totalReview = data1.getLong("total_review");
                    Driver.totalRides = data1.getLong("total_rider");
                    Driver.totalEarned = data1.getLong("earned");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return isExist;
    }

    public static boolean chauffeurCancelBooking(String endPoint,int status, String bodyTitle ) {

        isExist = false;
        OkHttpClient client = new OkHttpClient();

//        String pickLat =  String.valueOf( MainActivity.currentLocation.getLongitude());
//        String pickLon=  String.valueOf( MainActivity.currentLocation.getLatitude());
//        String as=  General.getUserid();
//        String sd=  Ride.getEndTime() ;
//        String df= Ride.getEndDate();
        RequestBody formBody = new FormBody.Builder()
                .add("available_id", String.valueOf(AvailableCar.activeAvailableId))
                .add("status", String.valueOf(status))
                .add("body_title", bodyTitle)
                .add("user_id",  General.getUserid())
                .build();

        Request request = new Request.Builder()
                .url(baseUrl+endPoint)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                isExist=true;
                // Do something with the response.
                res = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(res);
                    JSONObject cast = jsonResponse.getJSONObject("data");
                    AvailableCar.activeVehicalId =  cast.getInt("vehical_id");
                    AvailableCar.activeAvailableId = cast.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean getDistanceMatrix(double startLat, double startLon, double endLat, double endLon ) {

//https://maps.googleapis.com/maps/api/distancematrix/
// json?units=imperial&origins=24.933916,
// 67.100169&destinations=31.569688, 74.327749
// &key=AIzaSyCsG2tYn1NyAAM9vR_IXoIR67ATe8iKZK0



//https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=24.92592960734395,67.10300624370575&destinations=24.9196618378489,67.0931363850832&key=AIzaSyCsG2tYn1NyAAM9vR_IXoIR67ATe8iKZK0
        // Origin of route
        String str_origin = "origins=" + startLat + "," + startLon;

        // Destination of route
        String str_dest = "destinations=" + endLat + "," + endLon;

        // Sensor enabled
        String units = "units=imperial";

        // Building the parameters to the web service
        String parameters =units+ "&" +str_origin + "&" + str_dest;

        // Output format
        String output = "json";

        // Building the url to the web service
        String MY_API_KEY = "AIzaSyCsG2tYn1NyAAM9vR_IXoIR67ATe8iKZK0" ;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        String data = "";
        isExist = false;
        try {
            data =  downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (!data.equals("")){
            isExist=true;
            // Do something with the response.

            try {
                JSONObject jsonResponse = new JSONObject(data);
                JSONArray des = jsonResponse.getJSONArray("destination_addresses");
                GoogleDistanceMatrix.destination = (String) des.get(0);
                GoogleDistanceMatrix.origin = (String) jsonResponse.getJSONArray("origin_addresses").get(0);
                JSONArray row = jsonResponse.getJSONArray("rows");
                JSONObject rowIndex = (JSONObject) row.get(0);
                JSONArray elements = rowIndex.getJSONArray("elements");
                JSONObject elementsIndex = (JSONObject) elements.get(0);

                JSONObject duration = (JSONObject) elementsIndex.get("duration");
                GoogleDistanceMatrix.duration =duration.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return isExist;
    }

}
