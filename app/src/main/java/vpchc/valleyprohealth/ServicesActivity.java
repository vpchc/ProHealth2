package vpchc.valleyprohealth;

import org.vpchc.valleyprohealth.R;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ServicesActivity extends AppCompatActivity {

    String locations[];
    String categories[];
    String availableServices[]={};

    private boolean dentalCheck = false;
    private int selectionServicesLocation;
    private int selectionServicesCategory;

    private Spinner spinnerServicesLocations;
    private Spinner spinnerServicesCategories;

    Dialog servicesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarServices);
        setSupportActionBar(toolbar);

        //Sets company logo text to custom font due to it being unavailable natively
        String fontPath = "fonts/franklinGothicMedium.ttf";
        TextView titleText = (TextView) findViewById(R.id.title_services);
        Typeface customTitleText = Typeface.createFromAsset(getAssets(), fontPath);
        titleText.setTypeface(customTitleText);

        //Back button listener
        View buttonBack = findViewById(R.id.backButtonServices);
        buttonBack.setOnClickListener(servicesListener);

        locations = getResources().getStringArray(R.array.vpchc_locations2);
        categories = getResources().getStringArray(R.array.services_categories);

        spinnerServicesLocations = (Spinner)findViewById(R.id.spinnerServicesLocations);
        ArrayAdapter<String> adapterServicesLocations = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.fancy_spinner_item,locations);
        adapterServicesLocations.setDropDownViewResource(R.layout.fancy_spinner_dropdown);
        spinnerServicesLocations.setAdapter(adapterServicesLocations);

        spinnerServicesCategories= (Spinner)findViewById(R.id.spinnerServicesCategories);
        ArrayAdapter<String> adapterServicesCategories= new ArrayAdapter<String>(getApplicationContext(),
                R.layout.fancy_spinner_item,categories);
        adapterServicesCategories.setDropDownViewResource(R.layout.fancy_spinner_dropdown);
        spinnerServicesCategories.setAdapter(adapterServicesCategories);

        //Sets the preferred location
        SharedPreferences pref = getSharedPreferences("prefLocation", MODE_PRIVATE);
        int locationPref = pref.getInt("prefLocation", 0);
        if (locationPref == 6) {
            locationPref = 0;//This sets MSBHC to no preference due to no location section for it
        }
        spinnerServicesLocations.setSelection(locationPref);
        if(locationPref == 0){
            spinnerServicesCategories.setVisibility(View.GONE);
        }

        spinnerServicesLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        spinnerServicesCategories.setSelection(0);
                        spinnerServicesCategories.setVisibility(View.GONE);
                        break;
                    case 1:
                        servicesCategoriesChange(1);
                        selectionServicesLocation = 1;
                        break;
                    case 2:
                        servicesCategoriesChange(1);
                        selectionServicesLocation = 2;
                        break;
                    case 3:
                        servicesCategoriesChange(0);
                        selectionServicesLocation = 3;
                        break;
                    case 4:
                        servicesCategoriesChange(0);
                        selectionServicesLocation = 4;
                        break;
                    case 5:
                        servicesCategoriesChange(0);
                        selectionServicesLocation = 5;
                        break;
                    case 6:
                        servicesCategoriesChange(0);
                        selectionServicesLocation = 6;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinnerServicesCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        spinnerServicesCategories.setSelection(0);
                        break;
                    case 1:
                        selectionServicesCategory = 1;
                        availableServices = getResources().getStringArray(R.array.BehavioralHealth);
                        servicesPopup();
                        spinnerServicesCategories.setSelection(0);
                        break;
                    case 2:
                        if(dentalCheck){
                            selectionServicesCategory = 2;
                            availableServices = getResources().getStringArray(R.array.Dental);
                        }else{
                            selectionServicesCategory = 2;
                            availableServices = getResources().getStringArray(R.array.PatientSupport);
                        }
                        servicesPopup();
                        spinnerServicesCategories.setSelection(0);
                        break;
                    case 3:
                        if(dentalCheck){
                            selectionServicesCategory = 3;
                            availableServices = getResources().getStringArray(R.array.PatientSupport);
                        }else{
                            selectionServicesCategory = 3;
                            if(selectionServicesLocation==5) {
                                availableServices = getResources().getStringArray(R.array.PrimaryCare2);
                            }else{
                                availableServices = getResources().getStringArray(R.array.PrimaryCare1);
                            }
                        }
                        servicesPopup();
                        spinnerServicesCategories.setSelection(0);
                        break;
                    case 4:
                        selectionServicesCategory = 4;
                        if(selectionServicesLocation==5) {
                            availableServices = getResources().getStringArray(R.array.PrimaryCare2);
                        }else{
                            availableServices = getResources().getStringArray(R.array.PrimaryCare1);
                        }
                        servicesPopup();
                        spinnerServicesCategories.setSelection(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    private void servicesCategoriesChange(int choice){
    /*
	    Arguments:   None
	    Description: Changes the services category listing to show or remove dental depending on
	                 the location.
	    Returns:     Nothing
    */

        if(choice == 0){
            categories  = getResources().getStringArray(R.array.services_categories);
            dentalCheck = false;
        }else{
            categories  = getResources().getStringArray(R.array.services_categories2);
            dentalCheck = true;
        }

        spinnerServicesCategories = (Spinner) findViewById(R.id.spinnerServicesCategories);
        final ArrayAdapter<String> adapterServicesCategories = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.fancy_spinner_item, categories);
        adapterServicesCategories.setDropDownViewResource(R.layout.fancy_spinner_dropdown);
        spinnerServicesCategories.setAdapter(adapterServicesCategories);
        spinnerServicesCategories.setVisibility(View.VISIBLE);
        spinnerServicesCategories.setSelection(0);
    }

    private boolean servicesPopup(){
    /*
	    Arguments:   choice(0 - dismiss dialog, 1 - create a dialog)
	    Description: Displays or dismisses a dialog with selected type of services listed
	    Returns:     true
    */
        //Initialize the dialog
        int layoutID = getResources().getIdentifier("dialog_services", "layout", this.getPackageName());
        int closeID = getResources().getIdentifier("buttonDialogCloseServices", "id", this.getPackageName());
        int titleID = getResources().getIdentifier("servicesTitleText", "id", this.getPackageName());
        int subTitleID = getResources().getIdentifier("servicesSubTitleText", "id", this.getPackageName());
        int[] IDs = new int[] {layoutID,closeID,titleID,subTitleID};
        String[] titleText = new String[] {categories[selectionServicesCategory], locations[selectionServicesLocation]};
        servicesDialog = DialogSetup.dialogCreate(servicesDialog, this, IDs, titleText, 2);

        //Populate list of services based on type of service chosen
        LinearLayout servicesContent = (LinearLayout) servicesDialog.findViewById(R.id.servicesContent);
        for(int i = 0;i < availableServices.length;i++){
            TextView serviceToAdd = new TextView(this);
            serviceToAdd.setText(availableServices[i]);
            serviceToAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.dialog_content));
            serviceToAdd.setTextColor(Color.parseColor("#000000"));
            servicesContent.addView(serviceToAdd);
            if(i != 0){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)serviceToAdd.getLayoutParams();
                params.setMargins(0, 50, 0, 0);
                serviceToAdd.setLayoutParams(params);
            }
        }
        return true;
    }

    private View.OnClickListener servicesListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButtonServices:
                    finish();
                    v.setSelected(true);
                    break;
                default:
                    break;
            }
        }
    };
}