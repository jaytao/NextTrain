package com.example.nexttrain.ui.gallery;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nexttrain.R;
import com.example.nexttrain.databinding.FragmentGalleryBinding;
import com.example.nexttrain.stationservice.Stops;

import java.util.List;
import java.util.stream.Collectors;

public class GalleryFragment extends Fragment {

  private FragmentGalleryBinding binding;

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    return root;
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void onStart() {
    super.onStart();
    GalleryViewModel galleryViewModel =
        new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

    Stops stop = galleryViewModel.getStop().getValue();
    List<String> listViewDisplay;
    if (galleryViewModel.getArrivalsMap().getValue().get(stop).size() == 0) {
      listViewDisplay = List.of("No train updates found");
    } else {
      listViewDisplay =
          galleryViewModel.getArrivalsMap().getValue().get(stop).stream()
              .sorted((p1, p2) -> p1.second - p2.second)
              .map(
                  lineIntegerPair ->
                      String.format(
                          "%s:\t\t%s mins",
                          lineIntegerPair.first.toString(), lineIntegerPair.second / 60))
              .collect(Collectors.toList());
    }

    ArrayAdapter adapter =
        new ArrayAdapter<String>(getActivity(), R.layout.arrival_text_view, listViewDisplay);
    binding.arrivalsListView.setAdapter(adapter);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
