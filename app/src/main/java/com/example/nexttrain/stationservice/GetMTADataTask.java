package com.example.nexttrain.stationservice;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;

public class GetMTADataTask
    extends AsyncTask<Void, Void, HashMap<Stops, List<Pair<Line, Integer>>>> {

  private StationDataManager stationDataManager = new StationDataManager();

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected HashMap<Stops, List<Pair<Line, Integer>>> doInBackground(Void... voids) {
    return stationDataManager.getArrivals();
  }
}
