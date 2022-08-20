package com.example.nexttrain;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nexttrain.databinding.ActivityMainBinding;
import com.example.nexttrain.stationservice.GetMTADataTask;
import com.example.nexttrain.stationservice.Line;
import com.example.nexttrain.stationservice.Stops;
import com.example.nexttrain.ui.gallery.GalleryFragment;
import com.example.nexttrain.ui.gallery.GalleryViewModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration mAppBarConfiguration;
  private ActivityMainBinding binding;
  private GalleryViewModel galleryViewModel;
  private NavHostFragment navHostFragment;

  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());

    setSupportActionBar(binding.appBarMain.toolbar);

    binding.appBarMain.fabRefresh.setOnClickListener(view -> refreshData());

    navHostFragment =
        (NavHostFragment)
            getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
    refreshData();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
        || super.onSupportNavigateUp();
  }

  public void refreshData() {
    try {
      HashMap<Stops, List<Pair<Line, Integer>>> arrivalMap = new GetMTADataTask().execute().get();
      galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
      galleryViewModel.setArrivalsMap(arrivalMap);

      binding.appBarMain.toolbar.setTitle(
          String.format("Last Updated: %s", dtf.format(LocalTime.now())));

      Fragment frag = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();

      if (frag instanceof GalleryFragment) {
        navHostFragment.getChildFragmentManager().beginTransaction().detach(frag).commit();
        navHostFragment.getChildFragmentManager().beginTransaction().attach(frag).commit();
      }
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
