package com.hritik.asiainfo.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hritik.asiainfo.Adapters.CountryListAdapter;
import com.hritik.asiainfo.Database.AsianCountryDatabase;
import com.hritik.asiainfo.Database.DBInterface;
import com.hritik.asiainfo.Database.Tables.Borders;
import com.hritik.asiainfo.Database.Tables.Countries;
import com.hritik.asiainfo.Database.Tables.Languages;
import com.hritik.asiainfo.Dialog.DeleteDialog;
import com.hritik.asiainfo.Model.CountryData;
import com.hritik.asiainfo.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import static com.hritik.asiainfo.Constants.URLConstant.ASIA_URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loader;
    private ArrayList<CountryData> cList;
    private CountryListAdapter adapter;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private SharedPreferences sharedPreferences;
    private DBInterface dbInterface;
    private final Context context = this;
    private ConstraintLayout searchLayout;
    private boolean isSearchButtonClicked=false;
    private ConstraintLayout empty_img;

    public static AsianCountryDatabase myAppDatabase;

    void deleteAll(){
        DeleteDialog cdd=new DeleteDialog(context,dbInterface);
        cdd.show();

    }

    void filter(String text){
        ArrayList<CountryData> temp = new ArrayList();
        for(CountryData d: cList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    private void showList(Boolean offlineStatus){
        try {
            if (recyclerView==null || adapter==null){
                //Initialization of recyclerview and adapter
                recyclerView = (RecyclerView) findViewById(R.id.country_rv);
                adapter = new CountryListAdapter(this,this,cList,offlineStatus);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            }
            else{
                //Updates the list
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loader.setVisibility(View.INVISIBLE);
                    if (cList.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        empty_img.setVisibility(View.VISIBLE);
                    }


                }
            }, 2000);
        }
    }

    //Check for Internet Network for below Android Q
    private boolean haveNetwork(Context context){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            Log.i("COnnection IDS>>>>  ",info.getTypeName()+"   "+info.isConnected());
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
            {if (info.isConnected())
            {have_WIFI=true;}}
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
            {if (info.isConnected())
            {have_MobileData=true;}}
        }
        Log.i("Status Conn: ",""+(have_MobileData));
        return have_WIFI||have_MobileData;
    }

    //Check for Internet Network for above Android Q
    private boolean haveNetworkAPI29(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    }
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void retrieve_offlineList(){
        loader.setVisibility(View.VISIBLE);
        findViewById(R.id.empty_list_img).setVisibility(View.INVISIBLE);
        findViewById(R.id.country_rv).setVisibility(View.INVISIBLE);
        List<Countries> countries = dbInterface.getCountries();
        for(Countries country:countries){
            CountryData countryData = new CountryData();
            countryData.setName(country.getName());
            countryData.setCapital(country.getCapital());
            countryData.setRegion(country.getRegion());
            countryData.setFlag(country.getFlag());
            countryData.setSubRegion(country.getSubRegion());
            countryData.setPopulation(country.getPopulation());
            ArrayList<String> lang = new ArrayList<>(dbInterface.getCountryLanguagesName(String.valueOf(country.getCid())));
            ArrayList<String> bord = new ArrayList<>(dbInterface.getCountryBordersName(String.valueOf(country.getCid())));

            countryData.setLang_list(lang);
            countryData.setBord_list(bord);
            cList.add(countryData);


        }
        System.out.println("Hellow ew: "+cList.size());
        showList(true);
    }
    private void retrieve_list(){
        loader.setVisibility(View.VISIBLE);
        findViewById(R.id.empty_list_img).setVisibility(View.INVISIBLE);
        findViewById(R.id.country_rv).setVisibility(View.INVISIBLE);
        cList.clear();
        String URL = ASIA_URL;
        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isNoDataExist=false;
                try {
                    //Row creation
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("Length",jsonArray.length()+"");
                    for (int i=0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        CountryData countryData = new CountryData();
                        countryData.setName(object.getString("name"));
                        countryData.setCapital(object.getString("capital"));
                        countryData.setRegion(object.getString("region"));
                        countryData.setFlag(object.getString("flag"));
                        countryData.setSubRegion(object.getString("subregion"));
                        countryData.setPopulation(object.getString("population"));

                        ArrayList<String> langList = new ArrayList<String>();
                        JSONArray lang = object.getJSONArray("languages");
                        for (int j=0; j < lang.length(); j+=1){
                            JSONObject object1 = lang.getJSONObject(j);
                            String language_name = object1.getString("name");
                            langList.add(language_name);
                        }
                        countryData.setLang_list(langList);

                        ArrayList<String> bordList = new ArrayList<String>();
                        JSONArray bord = object.getJSONArray("borders");
                        //System.out.println(bord.length());
                        for (int j=0; j < bord.length(); j+=1){
                            String bordCountry = bord.getString(j);
                            bordList.add(bordCountry);
                        }
                        countryData.setBord_list(bordList);
                        cList.add(countryData);


                        if (sharedPreferences.getBoolean("offlineDataPresentStatus",false)){
                        }
                        else{
                            isNoDataExist=true;
                            Countries country = new Countries();
                            country.setName(object.getString("name"));
                            country.setCapital(object.getString("capital"));
                            country.setRegion(object.getString("region"));
                            country.setFlag(object.getString("flag"));
                            country.setSubRegion(object.getString("subregion"));
                            country.setPopulation(object.getString("population"));

                            int cid = (int) dbInterface.addCountry(country);

                            for (int j=0; j < lang.length(); j+=1){
                                JSONObject object1 = lang.getJSONObject(j);
                                String language_name = object1.getString("name");
                                Languages language = new Languages();
                                language.setCid(cid);
                                language.setName(language_name);
                                dbInterface.addLanguage(language);
                            }

                            //System.out.println(bord.length());
                            for (int j=0; j < bord.length(); j+=1){
                                String bordCountry = bord.getString(j);
                                Borders border = new Borders();
                                border.setCid(cid);
                                border.setName(bordCountry);
                                dbInterface.addBorder(border);
                            }
                        }
                    }
                    if (isNoDataExist){
                        sharedPreferences.edit().putBoolean("offlineDataPresentStatus",true).apply();
                    }

                    List<Countries> countries = dbInterface.getCountries();
                    System.out.println("Heelllsdo ::  "+countries.size());


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                finally {
                    showList(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empty_img=findViewById(R.id.empty_list_img);

        sharedPreferences= getSharedPreferences("MyShared",MODE_PRIVATE);
        searchLayout=findViewById(R.id.searchbarlayout);
        //searchLayout.setVisibility(View.INVISIBLE);
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), AsianCountryDatabase.class,"AsianCountries").allowMainThreadQueries().build();
        dbInterface =myAppDatabase.myDao();

        // Null checking of container

        requestQueue = Volley.newRequestQueue(this);
        //Initialize the list
        cList= new ArrayList<>();

        loader=findViewById(R.id.loader);
        searchBar=findViewById(R.id.search_et);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        boolean netFlag=false;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            netFlag=haveNetworkAPI29(this);
        } else{
            // do something for phones running an SDK before lollipop
            netFlag=haveNetwork(this);
        }
        if (!netFlag){
            retrieve_offlineList();
        }
        else {
            retrieve_list();
        }

    }

    void animateView(View view,float alpha,float yValues){
        ObjectAnimator move=ObjectAnimator.ofFloat(view, "translationY",yValues);
        move.setDuration(2500);
        ObjectAnimator alpha1=ObjectAnimator.ofFloat(view, "alpha",alpha);
        alpha1.setDuration(1000);
        AnimatorSet animatorSet =new AnimatorSet();
        animatorSet.play(alpha1).with(move);
        animatorSet.start();
    }

    void triggerSearchLayout(){
        if (isSearchButtonClicked){
            //searchLayout.setVisibility(View.INVISIBLE);
            animateView(searchLayout,0f,-20f);
            isSearchButtonClicked=false;
        }
        else{
            //searchLayout.setVisibility(View.VISIBLE);
            animateView(searchLayout,1f,20f);

            isSearchButtonClicked=true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbutton_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            deleteAll();
            return true;
        }
        if (id == R.id.search) {
            triggerSearchLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}