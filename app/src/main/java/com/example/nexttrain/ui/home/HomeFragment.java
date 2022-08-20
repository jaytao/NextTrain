package com.example.nexttrain.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.nexttrain.R;
import com.example.nexttrain.databinding.FragmentHomeBinding;
import com.example.nexttrain.stationservice.Stops;
import com.example.nexttrain.ui.gallery.GalleryViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    return root;
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    List<Stops> stops = List.of(Stops.values());

    ArrayAdapter adapter =
        new ArrayAdapter<String>(
            getActivity(),
            R.layout.arrival_text_view,
            stops.stream().map(x -> x.getName()).collect(Collectors.toList()));
    binding.listviewStations.setAdapter(adapter);
    binding.listviewStations.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Stops stop = stops.get(position);
            GalleryViewModel galleryViewModel =
                new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);
            galleryViewModel.setStop(stop);

            Navigation.findNavController(view).navigate(R.id.nav_gallery);
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
