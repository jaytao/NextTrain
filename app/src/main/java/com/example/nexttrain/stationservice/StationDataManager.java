package com.example.nexttrain.stationservice;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.transit.realtime.GtfsRealtime.FeedMessage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class StationDataManager {

  private String apiKey = "";

  private final Map<Line, String> LINES =
      Map.of(
          Line.E, "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-ace",
          Line.R, "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-nqrw",
          Line.M, "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/nyct%2Fgtfs-bdfm");

  private FeedMessage getFeed(String api) {

    HttpsURLConnection urlConnection = null;
    try {
      URL url = null;
      url = new URL(api);
      urlConnection = (HttpsURLConnection) url.openConnection();
      urlConnection.setRequestProperty("x-api-key", apiKey);
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      return FeedMessage.parseFrom(in);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } finally {
      urlConnection.disconnect();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public HashMap<Stops, List<Pair<Line, Integer>>> getArrivals() {
    long tsNow = System.currentTimeMillis() / 1000;
    HashMap<Stops, List<Pair<Line, Integer>>> arrivals = new HashMap();
    arrivals.put(Stops.G21S, new ArrayList<>());
    arrivals.put(Stops.A31N, new ArrayList<>());
    arrivals.put(Stops.D14N, new ArrayList<>());

    LINES.forEach(
        (line, url) -> {
          FeedMessage feedMessage = getFeed(url);
          feedMessage.getEntityList().stream()
              .forEach(
                  entity -> {
                    if (entity.hasField(
                        entity.getDescriptorForType().findFieldByName("trip_update"))) {
                      String routeLine = entity.getTripUpdate().getTrip().getRouteId();
                      if (isLine(routeLine)) {
                        entity.getTripUpdate().getStopTimeUpdateList().stream()
                            .forEach(
                                stopTimeUpdate -> {
                                  String stopId = stopTimeUpdate.getStopId();
                                  if (isStop(stopId)) {
                                    long timeDiff = stopTimeUpdate.getArrival().getTime() - tsNow;
                                    arrivals
                                        .get(Stops.valueOf(stopId))
                                        .add(Pair.create(Line.valueOf(routeLine), (int) timeDiff));
                                  }
                                });
                      }
                    }
                  });
        });
    return arrivals;
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private boolean isLine(String s) {
    return Arrays.stream(Line.values()).anyMatch(e -> e.name().equals(s));
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private boolean isStop(String s) {
    return Arrays.stream(Stops.values()).anyMatch(e -> e.name().equals(s));
  }
}
