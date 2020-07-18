/*
* This is a testing activity to test singleton class and data consistency
*
* */

package ca.sfu.cmpt_276_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

import ca.sfu.cmpt_276_project.Model.InspectionData;
import ca.sfu.cmpt_276_project.Model.Restaurant;
import ca.sfu.cmpt_276_project.Model.RestaurantManager;
import ca.sfu.cmpt_276_project.WebScraper.*;

public class TestingActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private RestaurantManager restaurantManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            //System.out.println("not permitted");
            System.out.println(PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_testing);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //restaurantManager = RestaurantManager.getInstance();
        //SingletonTest();
        //WebTest();

        DownloadTest();
    }

    public void SingletonTest(){
        List<Restaurant> restaurantList = restaurantManager.getRestaurants();
        if (!restaurantManager.getRestaurants().isEmpty()) {
            int inspection_count = 0;
            for (Restaurant restaurant : restaurantManager.getRestaurants()) {
                restaurant.Display();
                if (restaurant.getInspectionDataList().size()!=0){
                    for (InspectionData inspection:restaurant.getInspectionDataList()
                         ) {
                        System.out.println("Days from inspection: "+ inspection.timeSinceInspection());

                    }
                }
                inspection_count += restaurant.getInspectionDataList().size();
            }
            System.out.println("Restaurant Count: "+restaurantList.size());
            System.out.println("Inspection Count: "+inspection_count);
        }
    }
    public void WebTest(){
        String url = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        WebScraper webScraper = new WebScraper();
        webScraper.setPd(this);
        webScraper.execute(url);
    }
    public void DownloadTest(){
        CSVDownloader csvDownloader = new CSVDownloader();
        csvDownloader.setFilename("iter2.csv");
        System.out.println(PackageManager.PERMISSION_DENIED);
        csvDownloader.execute("https://data.surrey.ca/dataset/3c8cb648-0e80-4659-9078-ef4917b90ffb/resource/0e5d04a2-be9b-40fe-8de2-e88362ea916b/download/restaurants.csv");
    }
}