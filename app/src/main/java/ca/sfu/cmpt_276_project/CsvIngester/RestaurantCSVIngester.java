/*
* Class: RestaurantCSVIngester
*
* Class Description: The csvImporter Class contains several csv readers that dedicated for fetching data from the provided .csv files.
*
* Functionality:
* 1. readRestaurantList(Context context): Given the context, the function fetches data from restaurants_itr1.csv and interprets it into a List of Restaurants.[Done]
* 2. readInspectionReports(Context context): Given the context, the function fetches data from rinspectionreports_itr1.csv and binds data with List of Restaurants.[Undone]
* 3. readViolations(Context context): Given the context, the function fetches data from all_violations.txt and binds data with List of Restaurants.[Undone]
* 4. csvIngester(Context context): Given the context, the function calls functions above automatically, and return a List of Restaurants that each contains full information.[Undone]
*
* */

package ca.sfu.cmpt_276_project.CsvIngester;
import android.content.Context;
import android.util.Log;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import ca.sfu.cmpt_276_project.Model.*;
import ca.sfu.cmpt_276_project.R;

public class RestaurantCSVIngester {

    private List<Restaurant> restaurantList = new ArrayList<>();

    public void readRestaurantList(Context context) throws IOException {
        InputStream restaurantDataInput = context.getResources().openRawResource
                                            (R.raw.restaurants_itr1);

        BufferedReader reader = new BufferedReader(new InputStreamReader(restaurantDataInput,
                                                    Charset.forName("UTF-8")));
        String inputLine = "";

        //reading and storing CSV data
        try{
            reader.readLine();
            while((inputLine = reader.readLine())!=null){
                String[] tokens = inputLine.split(",");
                Restaurant dummy_restaurant = new Restaurant();
                dummy_restaurant.setTrackNumber(tokens[0]);
                dummy_restaurant.setRestaurantName(tokens[1]);
                dummy_restaurant.setPhysicalAddress(tokens[2]);
                dummy_restaurant.setPhysicalCity(tokens[3]);
                dummy_restaurant.setFacType(tokens[4]);
                dummy_restaurant.setLatitude(Double.parseDouble(tokens[5]));
                dummy_restaurant.setLongitude(Double.parseDouble(tokens[6]));

                restaurantList.add(dummy_restaurant);
            }
        }catch (IOException e){
            Log.wtf("Reading Activity","Fatal Error when reading file on line" +inputLine,e);
            e.printStackTrace();
        }

        //Display restaurant list
        for (Restaurant res:restaurantList
             ) {
            res.Display();
        }
        System.out.println(this.restaurantList.size());//Debugging purposes

    }//end of function
}
