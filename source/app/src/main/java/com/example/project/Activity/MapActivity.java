package com.example.project.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.example.project.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private MapView mapView;
    private ImageView btnBack;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng current;
    private Polyline currentPolyline;


    // Danh sách chi nhánh ngân hàng (sample)
    private List<LatLng> bankBranches = Arrays.asList(
            new LatLng(10.762622, 106.660172),
            new LatLng(10.765421, 106.682321),
            new LatLng(10.768912, 106.690412),
            new LatLng(10.768129, 105.359208),
            new LatLng(10.805765, 105.342501),
            new LatLng(10.708750, 105.378025),
            new LatLng(10.729691, 106.699949)

    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        btnBack = findViewById(R.id.btnBack);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        btnBack.setOnClickListener(v -> finish());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Kiểm tra quyền
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001);
            return;
        }

        gMap.setMyLocationEnabled(true);
        // Lấy vị trí hiện tại
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        current = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.clear();
                        gMap.addMarker(new MarkerOptions().position(current).title("Vị trí hiện tại"));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15f));

                        // Hiển thị các chi nhánh
                        addBankMarkers();

                        // Tìm chi nhánh gần nhất
                        LatLng nearest = getNearestBranch(current);

                        // Vẽ đường đi ngắn nhất
                        if (nearest != null) {
                            drawRoute(current, nearest);
                        }
                    }
                });



    }

    // Thêm marker chi nhánh
    private void addBankMarkers() {
        for (LatLng branch : bankBranches) {
            gMap.addMarker(new MarkerOptions()
                    .position(branch)
                    .title("Ngân hàng DNS"));
        }
    }

    // Tìm chi nhánh gần nhất
    private LatLng getNearestBranch(LatLng user) {
        LatLng nearest = null;
        double minDist = Double.MAX_VALUE;

        for (LatLng branch : bankBranches) {
            double dist = distance(user.latitude, user.longitude, branch.latitude, branch.longitude);
            if (dist < minDist) {
                minDist = dist;
                nearest = branch;
            }
        }

        return nearest;
    }

    // Hàm tính khoảng cách
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // vị trí Đồng tháp
//    private void drawRoute(LatLng origin, LatLng dest) {
//
//        // Xóa polyline cũ nếu có
//        if (currentPolyline != null) {
//            currentPolyline.remove();
//        }
//
//        // ================== DANH SÁCH ĐIỂM BẺ LÁI ==================
//        List<LatLng> path = new ArrayList<>();
//
//        // Điểm bắt đầu (vị trí hiện tại)
//        path.add(origin);
//
//        // ====== CÁC ĐIỂM BẺ LÁI THEO HƯỚNG ĐẾN 10.768129, 105.359208 ======
//        path.add(new LatLng(10.754296, 105.349441));  // cua 1
//        path.add(new LatLng(10.754471, 105.350669));  // cua 2
//        path.add(new LatLng(10.759621, 105.359903));  // cua 3
//        path.add(new LatLng(10.760120, 105.361571));  // cua 4
//
//        // Điểm đích
//        path.add(dest);
//
//        // ================== VẼ POLYLINE ==================
//        PolylineOptions options = new PolylineOptions()
//                .addAll(path)
//                .width(14)
//                .color(Color.BLUE)
//                .geodesic(true);
//
//        currentPolyline = gMap.addPolyline(options);
//    }






    // Google Directions API: Vẽ đường đi

   //  vị trí tdtu
    private void drawRoute(LatLng origin, LatLng dest) {

        // Xóa polyline cũ
        if (currentPolyline != null) {
            currentPolyline.remove();
        }

        // ====== TẠO 1 ĐƯỜNG ĐI GOM GÓC (GIẢ LẬP) ======
        List<LatLng> path = new ArrayList<>();

        path.add(origin);  // điểm bắt đầu = vị trí giả định

        // Các điểm bẻ lái do bạn cung cấp
        path.add(new LatLng(10.731239, 106.699722));
        path.add(new LatLng(10.731567, 106.699688));

        path.add(new LatLng(10.731606, 106.700035));
        path.add(new LatLng(10.730139, 106.700203));
        path.add(new LatLng(10.729900, 106.700120));

        path.add(dest); // điểm đến

        // Polyline
        PolylineOptions options = new PolylineOptions()
                .addAll(path)
                .width(14)
                .color(Color.BLUE)
                .geodesic(true);

        currentPolyline = gMap.addPolyline(options);
    }





    private class FetchURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray routes = jsonObject.getJSONArray("routes");

                JSONObject route = routes.getJSONObject(0);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String encoded = poly.getString("points");

                List<LatLng> list = decodePolyline(encoded);

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(list)
                        .width(14)
                        .color(Color.BLUE)
                        .geodesic(true);

                gMap.addPolyline(polylineOptions);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Decode polyline từ Google Directions
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }

        return poly;
    }

    // Xử lý lifecycle MapView
    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override protected void onPause() { mapView.onPause(); super.onPause(); }
    @Override protected void onDestroy() { mapView.onDestroy(); super.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    // Xử lý quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(gMap);
        }
    }

}
