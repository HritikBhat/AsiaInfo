package com.hritik.asiainfo.Adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.hritik.asiainfo.Model.CountryData;
import com.hritik.asiainfo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import static com.hritik.asiainfo.Constants.FILEConstant.IMAGES_FOLDER_NAME;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>{
    private ArrayList<CountryData> cList,cListFiltered;
    Context context;
    Activity activity;
    SharedPreferences sharedPreferences;
    Boolean offlineStatus;

    // RecyclerView recyclerView;
    public CountryListAdapter(Activity activity,Context context,ArrayList<CountryData> cList,Boolean offlineStatus) {
        this.activity=activity;
        this.cList = cList;
        this.cListFiltered = cList;
        this.context= context;
        this.offlineStatus=offlineStatus;
        Log.i("Array check",""+cList.size());
    }

    /*
    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

     */

    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.country_row_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    private String joinArrayString(ArrayList<String> arr){
        StringBuilder text = new StringBuilder();
        if(arr.size()==0){
            return "None";
        }
        for(String s:arr){
            text.append(s+", ");
        }
        return text.toString().substring(0,text.length()-2);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        CountryData content = cListFiltered.get(position);
        holder.name_tv.setText(content.getName());
        holder.capital_tv.setText("Capital: " +content.getCapital());
        holder.subRegion_tv.setText("SubRegion: "+content.getSubRegion());
        holder.region_tv.setText("Region: "+content.getRegion());
        holder.population_tv.setText("Population: "+content.getPopulation());

        String languages = joinArrayString(content.getLang_list());
        String borders = joinArrayString(content.getBord_list());

        holder.lang_tv.setText("Languages: "+languages);
        holder.bord_tv.setText("Borders: "+borders);


        Uri uri = Uri.parse(content.getFlag());
        GlideToVectorYou.justLoadImage(activity, uri, holder.flag_img);

    }

    public void updateList(ArrayList<CountryData> list){
        cListFiltered = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_tv,capital_tv,region_tv,subRegion_tv,population_tv,lang_tv,bord_tv;
        public ImageView flag_img;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name_tv = itemView.findViewById(R.id.cname_textview);
            this.flag_img = itemView.findViewById(R.id.cimgview);
            this.capital_tv = itemView.findViewById(R.id.capital_textview);
            this.subRegion_tv = itemView.findViewById(R.id.sregion_textview);
            this.region_tv = itemView.findViewById(R.id.region_textview);
            this.population_tv = itemView.findViewById(R.id.population_textview);
            this.lang_tv = itemView.findViewById(R.id.languages_tv);
            this.bord_tv = itemView.findViewById(R.id.border_tv);
        }
    }
}

