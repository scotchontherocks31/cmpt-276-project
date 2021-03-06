/*
 * Class: ViolationTXTIngester
 *
 * Class Description: The ViolationTXTIngester Class contains a .txt reader that fetches all types of violations from all_violations.txt file.
 *
 * Function:
 * 1. readViolationData(Context context): Given the context, the function fetches data from all_violations.txt and interprets it into a List of Violations.[Done]
 * 2. returnViolationByID(String id): Given the id, the function searches existing violations in violationList and return the designated violation, otherwise an empty violation will be returned.[Done]
 *
 * */

package ca.sfu.cmpt_276_project.CsvIngester;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.sfu.cmpt_276_project.Model.Violation;
import ca.sfu.cmpt_276_project.R;

public class ViolationTXTIngester {
    private List<Violation> violationList = new ArrayList<>();

    public void readViolationData(Context context) throws IOException, ParseException {
        InputStream restaurantDataInput = context.getResources().openRawResource
                (R.raw.all_violations);
        if (Locale.getDefault().getLanguage().equals("fr")){
            restaurantDataInput = context.getResources().openRawResource
                    (R.raw.all_violations_fr);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(restaurantDataInput,
                StandardCharsets.UTF_8));
        String inputLine = "";

        //reading and storing CSV data
        try {
            //skipping head lines
            reader.readLine();
            reader.readLine();

            while ((inputLine = reader.readLine()) != null) {
                String[] tokens = inputLine.split(",");
                Violation tempData = new Violation();
                tempData.setViolationNumber(tokens[0]);
                if (tokens[1].equals("Critical")|| tokens[1].equals("Critique")) {
                    tempData.setCritical(true);
                }
                tempData.setDescription(tokens[2]);
                violationList.add(tempData);
            }
        } catch (IOException e) {
            Log.wtf("Reading Activity", "Fatal Error when reading file on line" + inputLine, e);
            e.printStackTrace();
        }
    }//end of function

    //return a violation by tracking number
    public Violation returnViolationByID(String id) {
        Violation violation_to_return = new Violation();
        for (Violation violation : violationList
        ) {
            if (violation.getViolationNumber().equals(id))
                violation_to_return = violation;
        }
        return violation_to_return;
    }//end of function
}
