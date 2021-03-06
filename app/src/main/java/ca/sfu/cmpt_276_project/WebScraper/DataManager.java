/*Basic Behavior on stored data
* */
package ca.sfu.cmpt_276_project.WebScraper;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DataManager extends Activity{
    private final static String restaurant_url = "http://data.surrey.ca/api/3/action/package_show?id=restaurants";
    private final static String inspection_url = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
    private final static String restaurant_filename = "restaurants_itr2.csv";
    private final static String restaurant_update_date_local = "restaurants_date_local.txt";
    private final static String inspection_filename = "inspectionreports_itr2.csv";
    private final static String inspection_update_date_local = "inspectionreports_date_local.txt";
    private final static String directory_path = android.os.Environment.getExternalStorageDirectory() + "/Download/";
    private String restaurant_csv_url = "";
    private String inspection_csv_url = "";
    private Date restaurant_latest_update = null;
    private Date inspection_latest_update = null;


    public DataStatus checkForUpdates() throws ExecutionException, InterruptedException, ParseException, IOException {
        if (!(checkFileExistence(restaurant_filename) && checkFileExistence(inspection_filename))) {
            return DataStatus.NOT_EXIST;
        }
        else {
            restaurant_latest_update = readLocalDate(restaurant_update_date_local);
            inspection_latest_update = readLocalDate(inspection_update_date_local);
            WebScraper restaurantData = new WebScraper();
            String fetched_res_date = restaurantData.execute(restaurant_url).get()[1];
            Date restaurant_date_on_server;
            restaurant_date_on_server = dateParser(fetched_res_date);
            //System.out.println("restaurant_date_on_server:" + restaurant_date_on_server);

            WebScraper inspectionData = new WebScraper();
            String fetched_ins_date = inspectionData.execute(inspection_url).get()[1];
            Date inspection_date_on_server;
            inspection_date_on_server = dateParser(fetched_ins_date);
            //System.out.println("inspection_latest_update:" + inspection_date_on_server);

            Date currentTime = new Date();
            long time_diff_res = currentTime.getTime()-restaurant_latest_update.getTime();
            long hour_diff_res = TimeUnit.HOURS.convert(time_diff_res, TimeUnit.MILLISECONDS);
            long timeDiff_ins = currentTime.getTime()-inspection_latest_update.getTime();
            long hour_diff_ins = TimeUnit.HOURS.convert(timeDiff_ins, TimeUnit.MILLISECONDS);
            if (hour_diff_res >=20 && hour_diff_ins>=20){
                return DataStatus.OUT_OF_DATE;
            }else if (hour_diff_res>=20){
                return DataStatus.OUT_OF_DATE;
            }else if(hour_diff_ins>=20){
                return DataStatus.OUT_OF_DATE;
            }
            //To test it with older data on server, change before to after
            else if (restaurant_latest_update.before(restaurant_date_on_server)&&inspection_latest_update.before(inspection_date_on_server)){
                return DataStatus.OUT_OF_DATE;
            }else {
                if (restaurant_latest_update.before(restaurant_date_on_server)) {
                    return DataStatus.OUT_OF_DATE;
                }
                if (inspection_latest_update.before(inspection_date_on_server)) {
                    return DataStatus.OUT_OF_DATE;
                }
            }
        }
        return DataStatus.UP_TO_DATE;
    }

    public boolean checkFileExistence(String filename){
        File dummyFile = new File(directory_path+filename);
        return dummyFile.exists();
    }

    public void downloadAll(Context context) throws ExecutionException, InterruptedException, IOException {
        downloadList(context,restaurant_url,restaurant_filename,restaurant_csv_url);
        downloadList(context,inspection_url,inspection_filename,inspection_csv_url);
    }

    public void downloadList(Context context,String url,String filename,String csv_url) throws ExecutionException, InterruptedException, IOException {
        WebScraper web_data = new WebScraper();
        String[] result = web_data.execute(url).get();
        csv_url = result[0];
        System.out.println("csvurl:" + url);
        System.out.println("downloading " + filename);
        CSVDownloader restaurantData_downloader = new CSVDownloader(filename, context);
        restaurantData_downloader.execute(csv_url);
        Date currentTimeStamp = new Date();
        currentTimeStamp.getTime();
        if (filename.equals(restaurant_filename)){
            updateLocalDate(restaurant_update_date_local,currentTimeStamp.toString());
        }else{
            updateLocalDate(inspection_update_date_local,currentTimeStamp.toString());
        }
    }

    public void updateLocalDate(String filename,String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(directory_path+filename));
        writer.write(data);
        writer.close();
        System.out.println("current_time_stamp_saved: "+ data);
    }

    public Date dateParser(String date) throws ParseException {
        date = date.replace("T"," ");
        String[] dummydate = date.split("\\." );
        Date result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dummydate[0]);
        return result;
    }

    public Date readLocalDate(String filename) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(directory_path + filename));
        String input = reader.readLine();
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(input);
        System.out.println(input);
        System.out.println(date);
        reader.close();
        return date;
    }

    /**
     * Getters
     */

    public String getRestaurant_filename() {
        return restaurant_filename;
    }

    public String getInspection_filename() {
        return inspection_filename;
    }

    public Date getRestaurant_latest_update() {
        return restaurant_latest_update;
    }

    public Date getInspection_latest_update() {
        return inspection_latest_update;
    }

    public String getDirectory_path() {
        return directory_path;
    }

    public String getRestaurant_url() {
        return restaurant_url;
    }

    public String getInspection_url() {
        return inspection_url;
    }

    public String getRestaurant_csv_url() {
        return restaurant_csv_url;
    }

    public String getInspection_csv_url() {
        return inspection_csv_url;
    }


}
