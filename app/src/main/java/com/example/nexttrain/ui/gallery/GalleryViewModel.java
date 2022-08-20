package com.example.nexttrain.ui.gallery;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nexttrain.stationservice.Line;
import com.example.nexttrain.stationservice.Stops;

import java.util.HashMap;
import java.util.List;

public class GalleryViewModel extends ViewModel {

  private final MutableLiveData<String> mText;
  private final MutableLiveData<HashMap<Stops, List<Pair<Line, Integer>>>> arrivalsMap;
  private final MutableLiveData<Stops> stop;

  public GalleryViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is gallery fragment");
    arrivalsMap = new MutableLiveData<>();
    stop = new MutableLiveData<>();
  }

  public LiveData<String> getText() {
    return mText;
  }

  public LiveData<HashMap<Stops, List<Pair<Line, Integer>>>> getArrivalsMap() {
    return arrivalsMap;
  }

  public void setArrivalsMap(HashMap<Stops, List<Pair<Line, Integer>>> arrivalsMap) {
    this.arrivalsMap.setValue(arrivalsMap);
  }

  public void setStop(Stops stop) {
    this.stop.setValue(stop);
  }

  public LiveData<Stops> getStop() {
    return stop;
  }
}
