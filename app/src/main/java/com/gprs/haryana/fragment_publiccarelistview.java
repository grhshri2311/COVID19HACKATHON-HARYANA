package com.gprs.haryana;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;


public class fragment_publiccarelistview extends Fragment {


    ArrayList<String> state, link,state1,link1;
    SearchView searchView;
    ListView listView;
    CustomPubliccareAdapter customPubliccareAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_publichealthcarelistview, container, false);

        searchView = view.findViewById(R.id.search);
        listView = view.findViewById(R.id.list);

        state=new ArrayList<>();
        link=new ArrayList<>();


        state.add(getString(R.string.AndamanandNicobarIsland));
        state.add(getString(R.string.andhrapradesh));
        state.add(getString(R.string.arunachalpradesh));
        state.add(getString(R.string.assam));
        state.add(getString(R.string.Bihar));
        state.add(getString(R.string.Chandigarh));
        state.add(getString(R.string.Chhattisgarh));
        state.add(getString(R.string.DadraNagarHaveliDamanandDiu));
        state.add(getString(R.string.Delhi));
        state.add(getString(R.string.Goa));
        state.add(getString(R.string.Gujarat));
        state.add(getString(R.string.Haryana));
        state.add(getString(R.string.Himachalpradesh));
        state.add(getString(R.string.JammuandKashmir));
        state.add(getString(R.string.Jharkhand));
        state.add(getString(R.string.Karnataka));
        state.add(getString(R.string.Kerala));
        state.add(getString(R.string.Ladakh));
        state.add(getString(R.string.Lakshadweep));
        state.add(getString(R.string.Madhyapradesh));
        state.add(getString(R.string.Manipur));
        state.add(getString(R.string.Meghalaya));
        state.add(getString(R.string.Mizoram));
        state.add(getString(R.string.Nagaland));
        state.add(getString(R.string.Odisha));
        state.add(getString(R.string.Puducherry));
        state.add(getString(R.string.Punjab));
        state.add(getString(R.string.Rajasthan));
        state.add(getString(R.string.Sikkim));
        state.add(getString(R.string.Tamilnadu));
        state.add(getString(R.string.Telangana));
        state.add(getString(R.string.Tripura));
        state.add(getString(R.string.Uttarpradesh));
        state.add(getString(R.string.Uttarakhand));
        state.add(getString(R.string.Westbengal));

        link.add("https://dhs.andaman.gov.in/NewEvents/249.jpeg");
        link.add("http://hmfw.ap.gov.in/COVID-19%20IEC/COVID-19%20Hospitals.pdf");
        link.add("http://nrhmarunachal.gov.in/covid_19_IEC.html");
        link.add("https://nhm.assam.gov.in/portlet-innerpage/dedicated-covid-hospitals");
        link.add("http://statehealthsocietybihar.org/");
        link.add("http://chdcovid19.in/");
        link.add("http://cghealth.nic.in/cghealth17/");
        link.add("http://dnh.nic.in/Docs/COVID19/COVID19Health_Fac08052020.pdf");
        link.add("https://coronabeds.jantasamvad.org/");
        link.add("https://nhm.goa.gov.in/corona-virus-important-links-iec/");
        link.add("https://nrhm.gujarat.gov.in/cir-noti-covid-19.htm");
        link.add("http://nhmharyana.gov.in/page.aspx?id=208");
        link.add("http://www.nrhmhp.gov.in/content/covid-health-facilities");
        link.add("https://www.jknhm.com/covidfacilities.php");
        link.add("http://jrhms.jharkhand.gov.in/FileUploaded%20By%20User/DCovid_Hospitals.pdf");
        link.add("https://karunadu.karnataka.gov.in/hfw/nhm/pages/home.aspx");
        link.add("http://arogyakeralam.gov.in/2020/03/25/guidelines/");
        link.add("https://leh.nic.in/notice/covid19-hospital/");
        link.add("https://cdn.s3waas.gov.in/s358238e9ae2dd305d79c2ebc8c1883422/uploads/2020/05/2020051228-1.pdf");
        link.add("https://www.nhmmp.gov.in/CovidInformation.aspx");
        link.add("https://arogya.maharashtra.gov.in/1177/Dedicated-COVID-Facilities-Status");
        link.add("http://nrhmmanipur.org/?page_id=2602");
        link.add("http://meghalayaonline.gov.in/covid/images/materials/hospitals.pdf");
        link.add("http://nhmmizoram.org/page?id=202");
        link.add("http://nhmnagaland.in/Notification_file_path/Dedicated%20COVID%20Hospitals%20in%20Nagaland.pdf");
        link.add("https://statedashboard.odisha.gov.in/");
        link.add("https://health.py.gov.in/sites/default/files/NEET-PDF/Categorisation%20of%20covid%20facilities.pdf");
        link.add("http://pbhealth.gov.in/");
        link.add("http://rajswasthya.nic.in/PDF/COvid%20Facility%20Rajasthan.pdf");
        link.add("https://www.covid19sikkim.org/");
        link.add("https://stopcorona.tn.gov.in/%e0%ae%ae%e0%af%81%e0%ae%95%e0%af%8d%e0%ae%95%e0%ae%bf%e0%ae%af-%e0%ae%a4%e0%ae%95%e0%ae%b5%e0%ae%b2%e0%af%8d%e0%ae%95%e0%ae%b3%e0%af%8d/");
        link.add("https://www.chfw.telangana.gov.in/covid_hospitals.html");
        link.add("http://tripuranrhm.gov.in/home/0905202001.pd");
        link.add("https://updgmh-covid19.maps.arcgis.com/apps/opsdashboard/index.html#/bfc888151feb48928a4f6885ca20e83c");
        link.add("https://health.uk.gov.in/pages/view/102-dedicated-covid-facilities-in-state");
        link.add("https://www.wbhealth.gov.in/uploaded_files/corona/Notification___Revised___67_Covid_Hospital___30.04_.2020_.pdf");

        state1=new ArrayList<>(state);
        link1=new ArrayList<>(link);
        customPubliccareAdapter = new CustomPubliccareAdapter(getActivity(), state1, link1);
        listView.setAdapter(customPubliccareAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                state1.clear();
                link1.clear();

                int i;
                for (i = 0; i < state.size(); i++) {
                        if (state.get(i).toLowerCase().contains(query.toLowerCase())) {
                           state1.add(state.get(i));
                            link1.add(link.get(i));
                        }
                }
                if (state1.size() != 0) {
                    customPubliccareAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), state1.size() + " results found", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), i + "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return view;
    }
}
