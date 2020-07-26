package com.gprs.haryana;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class fragment_labsfortestlistview extends Fragment {


    Spinner spinner;
    CheckBox gov, priv;
    ListView listView;
    ArrayList<String> states;
    ArrayAdapter<String> dataAdapter;
    TextView tot, govtot, privtot;
    HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> hashMaps;
    String t, g, p;
    ArrayList<Pair<String, Pair<String, String>>> labs;
    CustomLabsAdapter customLabsAdapter;
    private String stateselected = "";
    Boolean privat = true, govt = true;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_labsfortestinglistview, container, false);

        spinner = view.findViewById(R.id.selectstate);
        gov = view.findViewById(R.id.gov);
        priv = view.findViewById(R.id.priv);
        listView = view.findViewById(R.id.labs);

        labs = new ArrayList<>();

        hashMaps = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();


        tot = view.findViewById(R.id.tot);
        govtot = view.findViewById(R.id.govtotal);
        privtot = view.findViewById(R.id.privtotal);

        states = new ArrayList<>();
        states.add("Select State");
        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, R.id.txt_bundle, states);
        spinner.setAdapter(dataAdapter);
        spinner.setGravity(11);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!states.get(position).equals("Select State")) {
                    stateselected = states.get(position);
                    Toast.makeText(getActivity(), stateselected, Toast.LENGTH_LONG).show();
                    getstate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customLabsAdapter = new CustomLabsAdapter(getActivity(), labs);
        listView.setAdapter(customLabsAdapter);

        gov.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                govt = isChecked;

                if (!stateselected.isEmpty()) {
                    getstate();
                } else
                    getall();
            }
        });

        priv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                privat = isChecked;

                if (!stateselected.isEmpty()) {
                    getstate();
                } else
                    getall();

            }
        });


        String jsonString = "{\n" +
                "  \"Total\":{\n" +
                "   \"Government laboratories\":\"428\",\n" +
                "   \"Private laboratories\":\"182\",\n" +
                "   \"Real-Time RT PCR for COVID-19\":\"452 (Govt: 303 + Private: 149)\",\n" +
                "   \"TrueNat Test for COVID-19\" : \"104 (Govt: 99 + Private: 05)\",\n" +
                "   \"CBNAAT Test for COVID-19\" : \"54 (Govt: 26 + Private: 28)\",\n" +
                "   \"Total No. of Labs\" : \"610\"\n" +
                "  },\n" +
                "  \"1\":{\n" +
                "    \"Names of States\":\"Andhra Pradesh\",\n" +
                "    \"Test Category\":{\n" +
                "      \"RT-PCR\":{\n" +
                "        \"Names of Government Institutes\":[\"Sri Venkateswara Institute of Medical Sciences, Tirupati\",\"Sri Venkateswara Medical College, Tirupati\",\"Rangaraya Medical College, Kakinada\",\"Sidhartha Medical College, Vijaywada\",\"Govt. Medical College, Ananthpur\",\"Guntur Medical College, Guntur\",\"Rajiv Gandhi Institute of Medical Sciences, Kadapa\" ,\"Andhra Medical College, Visakhapatnam\",\"Govt. Kurnool Medical College, Kurnool\",\"Govt. Medical College, Srikakulam\",\"AIIMS, Mangalagiri\"],\n" +
                "        \"Names of Private Institutes\":[]\n" +
                "            },\n" +
                "      \"TrueNat\":{\n" +
                "        \"Names of Government Institutes\":[\"Damien TB Research Centre, Nellore\",\"SVRR Govt. General Hospital, Tirupati\",\"Community Health Centre, Gadi Veedhi Saluru, Vizianagaram\",\"Community Health Centre, Bhimavaram, West Godavari District\",\"Community Health Centre, Patapatnam\",\"Community Health Center, Nandyal, Banaganapalli, Kurnool\",\"GSL Medical College & General Hospital, Rajahnagram, East Godavari District\",\"District Hospital, Madnapalle, Chittoor District\",\"District Hospital, APVVP,Pulivendula, Kadapa District\",\"District Hospital, Rajahmundry, East Godavari District\",\"District Hospital, Noonepalli, Nandyal, Kurnool\",\"District Hospital, Anakapalli, Vishakhapatnam\",\"District Hospital, Hindupur, Anantpur\",\"District Hospital, Proddatur\",\"District Hospital, Machlipatnam\",\"District Hospital, Atmakur\",\"District Hospital, Markapur\",\"District Hospital, Tekkali\",\"Area Hospital, Rampachodavaram, East Godavari District\",\"Area Hospital, Palamaner, Chittoor\",\"Area Hospital, Amalapuram, East Godavari District\",\"Area Hospital, Adoni, Kurnool\",\"Area Hospital, Chirala\",\"Area Hospital, Kandukuru\",\"Area Hospital, Narsipatnam\",\"Area Hospital, Parvathipuram\",\"Area Hospital, Tadepalligudem\",\"Area Hospital, Kavali\",\"Area Hospital, Tenali\",\"Area Hospital, Narasaraopet, Guntur\",\"Area Hospital, Macheria, Guntur\",\"Area Hospital, Kadiri\",\"Area Hospital, Gandhinagar, Nuzividu\",\"ACSR Govt. Medical College, Nellore\",\"Rural Development Trust, Bathalpalli\",\"Govt. General Hospital, Guntur\",\"Govt. General Hospital/ RIMS, Ongole\",\"DST Lab Govt. Chest Hospital, Vishakhapatnam\",\"District Hospital, Vizianagram\",\"District Hospital, Chittoor\",\"District Hospital, Eluru\",\"SHAR Hospital, Sriharikota, Nellore\"],\n" +
                "        \"Names of Private Institutes\":[]\n" +
                "            },\n" +
                "      \"CB NAAT\":{\n" +
                "        \"Names of Government Institutes\":[],\n" +
                "        \"Names of Private Institutes\":[]\n" +
                "            }\n" +
                "      }\n" +
                "  },\n" +
                "\n" +
                "  \"2\":{\n" +
                "  \"Names of States\":\"Arunachal Pradesh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Tomo Riba Institute of Health & Medical Sciences, Naharlagun\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory, Directorate of Health Sciences, Naharlagun\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"3\":{\n" +
                "  \"Names of States\":\"Assam\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Gauhati Medical College, Guwahati\",\"Regional Medical Research Center, Dibrugarh\",\"Jorhat Medical College, Jorhat\",\"Silchar Medical College, Silchar\",\"Fakkhruddin Ali Ahmed Medical College, Barpeta\",\"Tezpur Medical College, Tezpur\",\"Assam Medical College, Dibrugarh\",\"CSIR North East Institute of Science and Technology (NEIST), Jorhat\",\"*Defence Research Laboratory, Tezpur\",\"Diphu Medical College, Karbi Anglong\"],\n" +
                "      \"Names of Private Institutes\":[\"Ultracare Diagnostic Centre, Dept of Lab Services, Ashok Path, Survey, Beltola, Guwahati\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Molecular Testing LAb, GNRC Lab services,GNRC Hospitals, Dispur\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"4\":{\n" +
                "  \"Names of States\":\"Bihar\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Rajendra Memorial Research Institute of Medical Sciences, Patna\",\"Indira Gandhi Institute Medical Sciences, Patna\",\"Patna Medical College, Patna\",\"Darbhanga Medical College, Darbhanga\",\"SKMCH, Muzaffarpur\",\"All India Institute of Medical Sciences, Patna\"],\n" +
                "      \"Names of Private Institutes\":[\"Narayan Medical College, Sasaram\",\"Saral Pathlab Pvt. Ltd, 55B, Sector O,Sachiwalaya Colony, Kankarbagh, Patna\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Anugrah Narayan Magadh Medical College (ANMMC), Gaya\",\"Vardhman Institute of Medical Sciences, Pawapuri, Nalanda\",\"District Hospital, Siwan\",\"District Hospital, Rohtas\",\"JanNayak Karpoori Thakur Medical College and Hospital, Madhepura\",\"Government Medical College, Bettiah\",\"District Hospital, Purnia\",\"District Hospital, Katihar\",\"District Hospital, East Champaran\",\"District Hospital, Madhubani\",\"District Hospital, Buxar\",\"District Hospital, Khagaria\",\"District Hospital, Begusarai\",\"District Hospital, Banka, Bihar\",\"District Hospital, Gopalganj, Bihar\",\"District Hospital, Aurangabad, Bihar\",\"District Hospital, Jehanabad, Bihar\",\"District Hospital, Nawada, Bihar\",\"District Hospital, Arwal\",\"District Hospital, Saharsa, Bihar\",\"District Hospital, Supaul, Bihar\",\"District Hospital, Kaimur, Bihar\",\"District Hospital, Saran, Bihar\",\"District Hospital, Bhojpur, Bihar\",\"District Hospital, Kishanganj, Bihar\",\"District Hospital, Jamui, Bihar\",\"District Hospital, Lakhisarai, Bihar\",\"District Hospital, Munger\",\"District Hospital, Sheikhpura\",\"District Hospital, Vaishali\",\"District Hospital, Araria\",\"District Hospital, Samastipur\",\"District Hospital, Sheohar\",\"District Hospital, Sitamarhi\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Jawaharlal Nehru Medical College, Bhagalpur\"],\n" +
                "      \"Names of Private Institutes\":[\"Sen Diagnostics Pvt. Ltd., Budh Marg, Patna\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"5\":{\n" +
                "  \"Names of States\":\"Chandigarh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Post Graduate Institute of Medical Education & Research\",\"Govt. Medical College\",\"Institute of Microbial Technology\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"6\":{\n" +
                "  \"Names of States\":\"Chhattisgarh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"All India Institute of Medical Sciences, Raipur\",\"Late Baliram Kashyap M Govt. Medical College, Jagdalpur\",\"JNM Medical College, Raipur\",\"Late Shri Lakhi Ram Agrawal Memorial Govt. Medical College, Raigarh\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory, Lalpur, Raipur\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"7\":{\n" +
                "  \"Names of States\":\"Delhi\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"All India Institute Medical Sciences\",\"Lady Hardinge Medical College\",\"National Centre for Disease Control\",\"Ram Manohar Lohia Hospital\",\"Institute of Liver & Biliary Sciences\",\"Army Hospital Research & Referral\",\"Maulana Azad Medical College\",\"Vardhman Mahavir Medical College & Safdarjung Hospital\",\"University College of Medical Sciences\",\"Army Base Hospital\",\"IGIB, CSIR, New Delhi\",\"Rajiv Gandhi Super Speciality Hospital, Taharpur, Delhi\",\"Vallabhbhai Patel Chest Institute (VPCI), Delhi\",\"Defence Institute of Physiology & Allied Sciences (DIPAS), DRDO, Delhi\"],\n" +
                "      \"Names of Private Institutes\":[\"Lal Path Labs, Block -E, Sector 18, Rohini, Delhi\",\"Dr Dangs Lab, C-2/1, Safadarjung Development Area, New-Delhi\",\"Laboratory Services, Indraprastha Apollo Hospitals, Sarita Vihar, New Delhi\",\"Max Lab, Max Super Spciality Hospital, Saket, New-Delhi\",\"Sir Ganga Ram Hospital Clinical Lab Services, Sir Ganga Ram Hospital, Delhi\",\"Oncquest Labs Ltd, 3-Factory Road, New-Delhi\",\"Prognosis Laboratories, 515-16, Sector 19, Dwarka\",\"City X-Ray & Scan Clinic Pvt Ltd, 4B/18, Tilak Nagar, New-Delhi\",\"Lifeline Laboratory, H-11, Green Park Extension, New-Delhi\",\"Dept of Lab Services, Dr. B.L. Kapur Memorial Hospital, 5, Pusa Road, New-Delhi\",\"Dept of Laboratory Services, Action Cancer Hospital, A-4, Paschim Vihar (East), New-Delhi\",\"Star Imaging & Path Lab Pvt Ltd, 4B/4, Tilak Nagar, New Delhi\",\"Genestrings Diagnostic Centre Pvt Ltd, 3, MMTC, Geetanjali Enclave, New Delhi\",\"Sterling Accuris Diagnostics, A divison of Sterling Accuris Wellness Pvt Ltd, C-65, Block C, Phase I, Okhla, New Delhi\",\"CRL Diagnostics Pvt Ltd, Plot No 10, Avtar Enclave, Opposite Metro Pillar 227, Paschim Vihar, Rohtak Road, New Delhi\",\"Dept of Lab Medicine, HCMCT, Manipal Hospital, Main Road, Sector 6, Dwarka, New Delhi\",\". Gen-X Diagnostics, 2/6, Sarvapriya Vihar, \",\"Noble Diagnostic Centre, WZ-409C, Janak Park, Hari Nagar, Opposite DDU Hospital\",\"Mahajan Imaging Pvt Ltd, E-19, Defence Colony, New-Delhi\",\"Dept of Lab Sciences, Rajiv Gandhi Cancer Institute & Research Centre, Sector 5, Rohini\",\"Metropolis Healthcare Ltd. E21, Block-B1, Mohan Cooperative Industrial Estate, South East Delhi\",\"Gagan Pathology & Imaging Pvt Ltd F-26/21-22, Near Ayodhya Chowk,Sector 7, Rohini\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"ESIC Hospital, Basaidarapur \"],\n" +
                "      \"Names of Private Institutes\":[\"Aakash Path Lab, Aakash Healthcare & Super speciality Hospital, Road No 201, Sector 3,Dwarka\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"State TB Training and Demonstration Centre, Jawaharlal Nehru Marg, Delhi Gate\",\"Department of Microbiology, National Institute of TB and Respiratory Diseases (NITRD), New Delhi\",\". Northern Railway Central Hospital, New Delhi\"],\n" +
                "      \"Names of Private Institutes\":[\"Dr P Bhasin Path Labs (P) Ltd, S 13 Greater Kailash Part 1\",\"Venkateshwar Hospital, sector- 18A, Dwarka\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"8\":{\n" +
                "  \"Names of States\":\"Gujarat\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"BJ Medical College, Ahmedabad\",\"MP Shah Govt Medical College, Jamnagar\",\"Govt. Medical College, Surat\",\"Govt. Medical College, Bhavnagar\",\"Govt. Medical College, Vadodara\",\"Govt. Medical College, Rajkot\",\"NHL Medical College, Ahmedabad\",\"GMERS Medical College, Ahmedabad\",\"GMERS Medical College, Gandhinagar\",\"GMERS Medical College, Valsad\",\"National Institute of Occupational Health, Ahmedabad\",\"Gujarat Cancer & Research Institute, Ahmedabad\",\"Surat Municipal Institute of Medical Education & Research (SMIMER), Surat\",\"GMERS Medical College and Hospital, Dharpur-Patan, Gujarat\",\"Gujarat Adani Institute of Medical Sciences, Bhuj\",\"Gujarat Biotechnology Research Center, Gandhinagar\",\"Gujarat Forensic Sciences University, Gandhinagar\",\"GMERS Medical College, Gotri, Vadodara\",\"GMERS Medical College, Himmatnagar\",\"Dr. H.L. Trivedi Institute of Transplantation Services, Ahmedabad\",\"GMERS Medical College, Junagadh\"],\n" +
                "      \"Names of Private Institutes\":[\"Unipath Specialty laboratory limited ,102,Sanoma Plaza, Opposite Parimal Garden, Besides JMC House, Ellisbridge, Ahmedabad\",\"Supratech Micropath Laboratory & Research Institute Pvt Ltd, Kedar, Ahmedabad\",\"SN GeneLab Pvt Ltd, President Plaza –A, Near Mahavir Hospital, Nanpura, Surat\",\"Pangenomics International Pvt Ltd, Ellis Bridge, Ahmedabad\",\"Dept of Lab Medicine, Zydus Hospitals & Healthcare Research Pvt Ltd, ZydusHospital Road, Hebatpur, Off S.G. Highway,Thaltej, Ahmedabad\",\"Toprani Advanced Lab Systems, Suflam, 10, Haribhakti Colony, Race Course, Vadodra\",\"Dept of Lab Medicine, Apollo Hospitals International Ltd, 1, Bhat, GIDC Estate, Ahmedabad\",\"Divine lab, B 201/202, Mangalkirti Apartment, Fatehgunj, Vadodra\",\"Green Cross Genetics Lab Pvt Ltd,104,Sears Towers, Ahmedabad\",\"Sunflower Laboratory, Helmet Circle, Rudra Arcade, Drive In Road, Manav Mandir, MemNagar, Ahmedabad\",\"Parul Institute of Medical Sciences & Research (PIMSR), Vadodara\",\"Dhiraj Hospital, Smt. B.K. Shah Medical Institute & Research Centre, Vadodara\",\"Gujarat Pathology Lab & Diagnostic Centre, 101/102, Span Trade Centre, Paldi, Ahmedabad\",\"Scientific Diagnostic Centre Pvt.Ltd, 3,Venunand Raw House, Gulbai Tekra Road, Ellisebridge, Ahmedabad\",\"Salvus Bioresearch Solutions, 2nd Floor, Earth Retail, Science City Circle, S.P. Ring Road,Ahmedabad\",\"Maharaj Agrasen Medical College, Agroha, Hisar\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Apex Diagnostics - 12 Jacaranda Marg , Dlf City Phase - 2 , Gurgaon\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"AMC MET Medical College and Hospital, Ahmedabad\",\"GCS Medical College and Hospital, Ahmedabad\",\"AMC MET Medical College and Hospital, Ahmedabad\"],\n" +
                "      \"Names of Private Institutes\":[\"Dept of Lab Medicine, Bhailal Amin Gen Hospital, Alembic Road, Gorwa, Vadodra\",\"Microcare Lab & TRC ,105 ,Manthan Point, Unapani Road, Surat\",\"Speciality Microtech Laboratory, 121 Akshar Arcade, Navrangpura, Ahmedabad\",\"Sterling Accuris Diagnostics, Sterling Hospital, Phase 2, 1st Floor, Race Course Circle (West), Vadodra\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"9\":{\n" +
                "  \"Names of States\":\"Goa\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Goa Medical College, Goa\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"North District Hospital, Mapusa\",\"Subdistrict Hospital, Ponda\",\"South Goa District Hospital (Hospicio Hospital), Margao\",\"Sub- District Hospital, Chicalim, Vasco Da Gama\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"10\":{\n" +
                "  \"Names of States\":\"Haryana\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Pt. B.D. Sharma Post Graduate Inst. Of Med. Sciences, Rohtak\",\"Command Hospital, Chandimandir\",\"BPS Govt. Medical College, Sonipat\",\"ESIC Hospital, Faridabad\",\"Kalpana Chawla Govt. Medical College, Karnal\",\"Government Civil Hospital, Panchkula\",\"ICAR-National Research Centre on Equines, Hisar\",\"Translational Health Science & Technology Institute, Faridabad\",\"SHKM, Govt. Medical College, Mewat\",\"Maharaj Agrasen Medical College,Agroha, Hisar\"],\n" +
                "      \"Names of Private Institutes\":[\"Strand Life Sciences, A-17, Sector 34, Gurugram\",\"SRL Limited, GP26, Sector 18, Gurugram\",\"Modern Diagnostic & Research Centre-Lab, 363-364/4, JAwahar Nagar. Gurgaon\",\"Core Diagnostics Pvt Ltd, Udyog Vihar Phase-3, Gurgaon\",\"MolQ Laboratory, Plot 28,29; Sector 18(P), Electronic city, Udyog Vihar, Phase IV, Gurgaon\",\"Pathkind Diagnostics Pvt Ltd, Plot 55-56, Phase 4, Udyog Vihar, Sec 18, Gurgaon\",\"Department of Pathology and Laboratory Medicine, Medanta-The  Medicity, Sector 38, Gurgaon\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Apex Diagnostics - 12 Jacaranda Marg , Dlf City Phase - 2 , Gurgaon\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"IRL, Haryana Govt. Public Health Laboratory, Karnal\",\"Maharaj Agrasen Medical College, Agroha, Hisar\",\"District Civil Hospital, Ambala\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"11\":{\n" +
                "  \"Names of States\":\"Himachal Pradesh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Indira Gandhi Medical College, Shimla\",\"Dr. Rajendra Prasad Govt. Medical College, Tanda\",\"Central Research Institute, Kasauli\",\"Shri Lal Bahadur Shastri Govt. Medical College, Mandi\",\"CSIR Institute of Himalayan Bioresource Technology, Palampur\",\"Dr. Yashwant Singh Parmar Government Medical College, Nahan\",\"Pt. JLN Government Medical College and Hospital, Chamba\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Zonal Hospital, Mandi\",\" Dr. Radhakrishnan Government Medical College, Hamirpur\",\"Regional Hospital, Una\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"12\":{\n" +
                "  \"Names of States\":\"Jammu & Kashmir\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Govt. Medical College, Jammu\",\"Command Hospital (NC) Udhampur\",\"Sher-i-Kashmir Institute of Medical Sciences, Srinagar\",\"Govt. Medical College, Srinagar\",\"Sheri Kashmir Institute of Medical Science Medical College, Bemina, Srinagar\",\"CSIR Indian Institute of Integrative Medicine (IIIM), Jammu\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory, Chest Disease Hospital, Dalgate, Srinagar\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"13\":{\n" +
                "  \"Names of States\":\"Jharkhand\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"MGM Medical College & Hospital, Jamshedpur\",\"Rajendra Institute of Medical Sciences, Ranchi\",\"Patliputra Medical College & Hospital, Dhanbad\",\"Itki Aarogyashala, Ranchi\"],\n" +
                "      \"Names of Private Institutes\":[\"Tata Main Hospital (Dept of Pathology), Tata Steel, Bistupur, Jamshedpur\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"District Hospital, Bokaro\",\"District Hospital, Chatra\",\"District Hospital, Deoghar\",\"District Hospital, Dumka\",\"District Hospital, Garhwa\",\"District Hospital, Giridih\",\"District Hospital, Godda\",\"District Hospital, Hazaribag\",\"District Hospital, Kodarma\",\"District Hospital, Lathehar\",\"District Hospital, Pakur\",\"District Hospital, Palamu\",\"District Hospital, Pashchimi Singhbhum\",\"District Hospital, Ranchi\",\"District Hospital, Sahibganj\",\"District Hospital, Dhanbad\",\"District Hospital, Gumla\",\"District Hospital, Jamtara\",\"District Hospital, Khunti\",\"District Hospital, Lohardaga\",\"District Hospital, Purbi Singhbum\",\"District Hospital, Ramgarh\",\"District Hospital, Saraikela Kharsawan\",\"District Hospital, Simdega\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Military Hospital, Namkum, Ranchi\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"14\":{\n" +
                "  \"Names of States\":\"Karnataka\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Hassan Inst. Of Med. Sciences, Hassan\",\"Mysore Medical College & Research Institute, Mysore\",\"Shivamogga Institute of Medical Sciences, Shivamogga\",\"Command Hospital (Air Force), Bengaluru\",\"Bangalore Medical College & Research Institute, Bengaluru\",\"National Institute of Virology, Bangalore Field Unit, Bengaluru\",\"Indian Institute of Science, Bengaluru (Department of Biochemistry, Centre for Infectious Disease Research)\",\"Gulbarga Institute of Medical Sciences, Gulbarga\",\"Vijaynagar Institute of Medical Sciences, Bellary\",\"National Institute of Mental Health and Neuro-Sciences, Bangalore\",\"Wenlock District Hospital, Mangalore\",\"Karnataka Institute of Medical Sciences, Hubli\",\"National Institute of Traditional Medicine, Belagavi\",\"Dharwad Institute of Mental Health & Neurosciences, Dharwad\",\"Kidwai Memorial Institute of Oncology, Bengaluru\",\"Instem, Bengaluru\",\"Mandya Institute of Medical Sciences (MIMS), Mandya\",\"Chamarajanagar Institute of Medical Sciences (CIMS), Chamarajanagar District\",\"Gadag Institute of Medical Sciences, Gadag\",\"Kodagu Institute of Medical Sciences (KOIMS), Kodagu District\"],\n" +
                "      \"Names of Private Institutes\":[\"Neuberg Anand Reference Laboratory, Anand Tower, #54, Bowring Hospital Road, Bengaluru\",\"Cancyte Technologies Pvt Ltd, Sri Shankara Research Centre, Bengaluru\",\"Central Diagnostic Lab, Vydehi Institute of Medical Sciences and Research Centre, #82, E.P.I.P. Area, Whitefield, Bengaluru\",\"Syngene International Limited, Biocon Park, SEZ, Bommasandra Industrial Area, Phase IV, Bommasandra-Jigani Link Road, Bengaluru\",\"Department of Lab Medicine, Narayana Hrudayalaya Ltd, 258/A, Bommasandra Industrial Area, Hosur Road, Bengaluru\",\"Aster Clinical Lab LLP, No 24, Venkatappa Road, Tasker Town, Vasanthanagar, Bangalore\",\"Microbiological Lab, 22-D 3, KIADB Industrial Area, 1st Phase, Kumbalagidu, Bengaluru\",\"Yenepoya Medical College Hospital Lab, Nithyananda Nagar, Derlakatte, Mangaluru\",\"Hybrinomics Life Science and diagnostics LLP, Site No 50, Maruthi Township, B. Hanumanthanagar, Bileshivale, Doddagubbi Post, Bengaluru\",\"Shamanur Shivashankarappa Institute of Medical Sciences and Research Centre (SSIMSRC), Davangere\",\"XCyton Diagnostics Pvt Ltd – Molecular Diagnostic Services, #449, 10th Cross, 4th phase, Peenya, Bengaluru\",\"St. Johns Medical College and Hospital, Bangalore\",\"Kasturba Medical College Manipal\",\"Father Muller's Medical College, Mangalore\",\"JJM Medical College (JJMMC), Davangere\",\"Manipal Hospital, Bangalore\",\"Sakra World Hospital Lab Services, Devarabeesanahalli VArthur Hobli, Bengaluru\",\"Kasturba Medical College, Mangalore\",\"KS Hegde Medical Academy (KSHEMA), Mangalore\",\"Kempegowda Institute of Medical Sciences,Bangalore\",\"Lab Services, Apollo Hospitals, 154/11, Bannerghatta Road, Bengaluru\",\"United Hospitals, Kalburgi\",\"Mediclu Diagnostics, Bangalore\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Public health Laboratory, Chikkaballapur District Hospital, Chikkaballapur\",\"Public Health Laboratory, Chikmagalur District Hospital, Chikmagalur\",\"Udupi District Hospital, Udupi\",\"Yadgiri District Hospital, Yadgiri\",\"Chitradurga District Hospital, Chitradurga\",\"Raichur Institute of Medical Sciences, Raichur\",\"Bidar Institute of Medical Sciences, Bidar\",\"Koppal Institute of Medical Sciences, Koppal\",\"District Public Health Laboratory, Kolar District\",\"District Public Health Laboratory, Chamarajanagar District\",\"Bangalore Bruhat Mahanagara Palike (BBMP) Health Centre (Fever Clinic),Adugodi, Bangalore\",\"District Public Health Laboratory,Ramanagara District\",\"Jayadeva Institute of Cardiac Sciences, Bangalor\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Tumkur District Hospital, Tumkur\",\"Vijayapura District Hospital, Vijayapura\",\"Belgaum Institute of Medical Sciences, Belgaum\",\"Bagalkot District Hospital, Bagalkot\",\"Karwar Institute of Medical Sciences, Karwar\",\"Bagalkot District Hospital, Bagalkot\"],\n" +
                "      \"Names of Private Institutes\":[\"Lab Services, Apollo Hospitals, 154/11, Bannerghatta Road, Bengaluru\",\"KLES Dr Prabhakar Kore Hospital & MRC Hi Tech Lab, NehruNagar, Belgaum, Belagavi\",\"Dept of Pathology & Lab Medicine, Aster CMI Hospital, #43/2, NH 7, International Airport Rd, Sahakar Nagar, Bengaluru\",\"Dept of Lab Medicine, Vikram Hospital Pvt Ltd, No 71/1, Millers Road, Bengaluru\",\"Department of Laboratory Medicine and Pathology, Columbia Asia Referral Hospital,Yeshwantpur, #26/4, Brigade Gateway, Malleshwaram West, Bengaluru\",\"SRL Limited at Fortis hospitals, 154/9,Bannerghatta Main Road, Bengaluru\",\"Trident Diagnostics & Healthcare Pvt.Ltd, #313, 2nd main Jagajyothi Nagara, 80 feet outer Ring Road, Kenchanapura Cross bus stop, Nagadevanahalli, Bengaluru\",\"NMR Diagnostics Pvt. Ltd, Maratha Mandal Building, P.B.Road, Dharwad\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"15\":{\n" +
                "  \"Names of States\":\"Kerala\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"National Institute of Virology, Field Unit, Allapuzzha\",\"Govt. Medical College, Thiruvanathapuram\",\"Govt. Medical College, Kozhikode\",\"Govt. Medical College, Thrissur\",\"Rajiv Gandhi Center for Biotechnology, Thiruvanathapuram\",\"Sree Chitra Tirunal Institute of Medical Sciences, Thiruvanathapuram\",\"State Public Health Laboratory, Trivandrum\",\"Inter University, Kottayam\",\"212. Malabar Cancer Center, Thalassery\",\"213. Central University of Kerala, Periye, Kasaragod\",\"Govt. Medical College, Ernakulum\",\"Govt. Medical College, Manjeri\",\"Govt. Medical College, Kottayam\",\"Govt. Medical College, Kannur\",\"Indian Institute of Science Education and Research (IISER), Thiruvananthapuram\",\"MVR Cancer Centre & Research\n" +
                "Institute, CP 13/516 B, C, Vellalaserri NIT (via), Poolacode, Kozhikode\"],\n" +
                "      \"Names of Private Institutes\":[\"DDRC SRL Diagnostics Pvt Ltd, Panampilly Nagar, Ernakulam\",\"MIMS Lab Services, Govindapuram, Kozhikode\",\"Lab Services of Amrita Institute of Medical Sciences & Research Centre, AIMS-Ponekkara, Kochi\",\"Dane Diagnostics Pvt Ltd, 18/757 (1), RC Road, Palakkad\",\"Medivision Scan & Diagnostic Research Centre Pvt Ltd, Sreekandath Road, Kochi\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Regional Public Health Laboratory,Pathanamthitta\",\"Government Medical College Hospital,Kollam\",\"Government TD Medical College, Alappuzha\",\"District Public Health Laboratory, Wayanad\",\"District TB Centre, Palakkad\",\"INHS Sanjivani, Koch\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Regional Cancer Centre, Thiruvananthapuram\"],\n" +
                "      \"Names of Private Institutes\":[\"Dept of Pathology and Lab Medicine, Aster Medcity, Aster DM Healthcare Ltd, Kutty Sahib Road, Kothad, Cochin\",\"NIMS Medicity, Department of Laboratory Medicine, Aralumoodu, Neyyattinkara, Thiruvananthapuram\",\"Rajagiri Hospital Laboratory Services, Rajagiri Hospital, Chunangamvely, Aluva\",\" Micro Health LAbs, MPS Tower, Kozhikode\",\"Microbiology Section, Department Of Laboratory Medicine, Kerala Instittute Of Medical Sciences, Anamukham, Anayara P.O. Thiruvananthapuram\",\"Believers Church Medical College Laboratory, St Thomas Nagar, Kuttapuzha P.O., Thiruvalla\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"16\":{\n" +
                "  \"Names of States\":\"Maharashtra\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"National Institute of Virology, Pune\",\"Seth GS Medical College & KEM Hospital, Mumbai\",\"Kasturba Hospital for Infectious Diseases, Mumbai\",\"National Institute of Virology Field Unit, Mumbai\",\"Armed Forces Medical College, Pune\",\"BJ Medical College, Pune\",\"Command Hospital (SC), Pune\",\"Indira Gandhi Govt. Medical College, Nagpur\",\"All India Institute of Medical Sciences, Nagpur\",\"Govt. Medical College, Nagpur\",\"Nagpur Veterinary College, MAFSU, Nagpur\",\"Intermediate Reference Laboratory, Nagpur\",\"Grant Medical College & Sir JJ Hospital, Mumbai\",\"Govt. Medical College, Aurangabad\",\"V. M. Government Medical College, Solapur\",\"Haffkine Institute, Mumbai\",\"Shree Bhausaheb Hire Govt. Medical College, Dhule\",\"Government Medical College, Miraj\",\"Govt. Medical College, Akola\",\"National Institute for Research on Reproductive Health, Mumbai\",\"Rajiv Gandhi Medical College & CSM Hospital, Kalwa, Thane, Mumbai\",\"ICMR-National AIDS Research Institute, Pune\",\"Swami Ramanand Teerth Marathwada University, Nanded\",\"Mahatma Gandhi Institute of Medical Sciences, Sevagram, Wardha\",\"Vilasrao Deshmukh Govt. Institute of Medical Sciences, Latur\",\"INHS Ashvini, Mumba\",\"Tata Memorial Centre ACTREC, Mumbai\",\"*Tata Memorial Hospital, Mumbai\",\"National Centre for Cell Sciences, Pune\",\"National Environmental Engineering Research Institute, Nagpur\",\"Sant Gadge Baba Amravati University, Amravati\",\"RCSM Govt. Medical College, Kolhapur\",\"Model Rural Health Research Unit (MRHRU), Sub District Hospital, Agar, Dahanu, Palghar\",\"Indian Institute of Science Education and Research (IISER), Pune\",\"Government Medical College, Jalgaon\",\"District General Hospital, Ahmednagar\",\"Government Medical College, Baramati\",\"Govt. Medical College, Chandrapur\",\"Govt. Medical College, Yavatmal\",\"Swami Ramanand Teerth Rural Government Medical College, Ambajogai\",\"Government Medical College, Gondia\",\"Agharkar Research Institute, Pune\",\"District General Hospital, Ratnagiri\",\"Dr. Shankarrao Chavan Govt. Medical College, Nanded\"],\n" +
                "      \"Names of Private Institutes\":[\"Thyrocare Technologies Limited, D37/1, TTC MIDC, Turbhe, Navi Mumbai\",\"Suburban Diagnostics (India) Pvt. Ltd., 306, 307/T, 3rd Floor, Sunshine Bld., Andheri (W), Mumbai\",\"Metropolis Healthcare Ltd, Unit No. 409-416, 4th Floor, Commercial Building-1, Kohinoor Mall, Mumbai\",\"Sunflower Lab & Diagnostic Center, Keshav Kunj, Marve Road, Malad West, Mumbai\",\"Aditya Birla Memorial Hospital – Laboratory, Aditya Birla Marg, Chinchwad, Pune\",\"Dr. Jariwala Lab & Diagnostics LLP, 1st Floor, Rasraj Heights, Rokadia Lane, Off Mandpeshwar Road, Borivli (W), Mumbai\",\"Sir H.N. Reliance Foundation Hospital and Research Centre, Molecular Medicine, Reliance Life Sciences Pvt. Ltd., R-282, TTC Industrial Area, Rabale, Navi Mumbai\",\"Rajiv Gandhi Medical College & CSM Hospital, Kalwa, Thane, Mumbai\",\"Suburban Diagnostics (India) Pvt. Ltd., 306, 307/T, 3rd Floor, Sunshine Bld., Andheri (W), Mumbai\",\"Metropolis Healthcare Ltd, Unit No. 409-416, 4th Floor, Commercial Building-1, Kohinoor Mall, Mumbai\",\"ICMR-National AIDS Research Institute, Pune\",\"Swami Ramanand Teerth Marathwada University, Nanded\",\"SRL Limited, Prime Square Building, Plot No 1, Gaiwadi Industrial Estate, SV Road, Goregaon, Mumbai\",\"A.G. Diagnostics Pvt Ltd, Nayantara Building, Pune\",\"Kokilaben Dhirubhai Ambani Hospital Laboratory, Four Bungalows, Mumbai\",\"InfeXn Laboratories Private Limited, A/131, Therelek Compound, Road No 23, Wagle Industrial Estate, Thane (W)\",\"iGenetic Diagnostics Pvt Ltd, Krislon House, Andheri East, Mumbai\",\"Sahyadri Speciality Labs, Plot No 54, S.No. 89-90, Lokmanya Colony, Kothrud, Pune\",\"Metropolis Healthcare Limited, Construction House, 796/189-B, Bhandarkar Institute Road, Pune\",\"SRL Diagnostics – Dr. Avinash Phadke (SRL Diagnostics Pvt Ltd), Mahalaxmi Engineering Estate, 2nd Floor, L.J. Cross Road No 1, KJ Khilnani High School, Mahim (West), Mumbai\",\"Department of Laboratory Medicine – P.D. Hinduja National Hospital and Medical Research Centre, Veer Savarkar Marg, Mahim, Mumbai\",\"Vaidya Lab Thane, Unit of Millenium Special Lab Pvt Ltd, Odyssey Park, 2nd Floor, 201, Raghunath Nagar, Wagle Estate, Thane\",\"Genepath Diagnostics India Pvt Ltd, 4th Floor, Above Phadke Hospital, Pune\",\"Daignostic Molecular Laboratory, Dept of Microbiology, Dr. Vasantrao Pawar Medical College Hospital & Research Centre, Vasantdada Nagar, Adgaon, Nashik\",\"Dept of Lab Medicine, Dr. Balabhai Nanavati Hospital, Swami Vivekananda Road, Mumbai\",\"Krsnaa Diagnostics Pvt Ltd, Lt. Jayabai Nanasaheb Sutar Maternity Home, Pune\",\"Dhruv Pathology and Molecular Diagnostic Lab, Third Floor, Aditya Enclave, Central Bazaar Road, Ramdaspeth, Nagpur\",\"Dept of Molecular Biology & Genetics, Krishna Institute of Medical Sciences, Karad, Satara\",\"Lab Services, Ayugen Biosciences Pvt Ltd, 562/1, Shivajinagar, Pune\",\"MGM Medical College and Hospital, Navi Mumbai\",\"Ruby Hall Clinic, Dept of Laboratory, Grant Medical Foundation, 40, Sassoon Road, Pune\",\"D. Y. Patil Medical College, Kolhapur\",\"Metropolis Healthcare Limited, Shop No. 1, Ground Floor, Ahilya Building, Savarkar Marg, Thane West, Thane\",\"Molecular Diagnostic Laboratory, Department of Pathology, LMMF's Deenanath Mangeshkar Hospital and Research Center, Erandwane, Pune\",\"Bharati Vidyapeeth (Deemed to beUniversity) Medical College, Pune\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Civil Surgeon, District Hospital, Parbhani\",\"District Hospital, Satara\",\"Daga Memorial Government Women Hospital, Nagpur\",\"Navi Mumbai Municipal Corporation General Hospital, F.R.U. Vashi\",\"District General Hospital, Gadchiroli\",\"Darasha Maternity Home, Solapur Municipal Corporation, Solapur\",\"District Hospital, Amravati\",\"Sub district Hospital, Shahapur, Thane\"],\n" +
                "      \"Names of Private Institutes\":[\"Qualilife Diagnostics, Balaji Arcade, 1st Floor, 544/A, Netaji Subhash Road, Mulund (W), Mumbai\",\"Rural Medical College, Pravara Institute of Medical Sciences, Loni, Ahmednagar\",\"Clinicare Speciality Laboratory Pvt. Ltd.,Lata Mangeshkar Hospital, Nagpur\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory, Pune\",\"Government Medical College, Chandrapur\",\"Government Medical College, Baramati\",\"Government Medical College, Yavatmal\"],\n" +
                "      \"Names of Private Institutes\":[\"Sunflower Lab & Diagnostic Center, Keshav Kunj, Marve Road, Malad West, Mumbai\",\"Aditya Birla Memorial Hospital – Laboratory, Aditya Birla Marg, Chinchwad, Pune\",\"Dr. Jariwala Lab & Diagnostics LLP, 1st Floor, Rasraj Heights, Rokadia Lane, Off Mandpeshwar Road, Borivli (W), Mumbai\",\"Su-Vishwas Diagnostic Lab, 1st floor Midas height, Ramdaspeth, Nagpur\",\"Jupiter Lifeline Hospitals Limited, Pune\",\"Kingsway Hospitals ((A Unit of SPANV Medisearch Lifesciences Pvt Ltd), 44,Kingsway Road, Nagpur\",\" Jupiter Hospital, Eastern Express Highway, Thane West\",\"Dr Ajay Shah's Pathology Lab & Microbiology Reference Center, 1st Floor,Amidrashti, Opposite Manav Kalyan Kendra, S V Road, Dahisar East. Mumbai\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"17\":{\n" +
                "  \"Names of States\":\"Madhya Pradesh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"All India Institute of Medical Sciences, Bhopal\",\"ICMR-National Institute for Research on Tribal Health, Jabalpur\",\"Mahatma Gandhi Memorial Medical College, Indore\",\"Gandhi Medical College, Bhopal\",\"Bhopal Memorial Hospital & research Centre, Bhopal\",\"Gajra Raja Medical College, Gwalior\",\"Bundelkhand Medical College, Sagar\",\"SS Medical College, Rewa\",\"Defence Research & Development Organization, Gwalior\",\"ICAR-NIHSAD, Bhopal\",\"Government Medical College, Ratlam\",\"Netaji Subhash Chandra Bose Medical College, Jabalpur\",\"Government Medical College, Khandwa\",\"Atal Bihari Vajpayee Government Medical College (ABVGMC), Vidisha\",\"IISER, Bhopal\"],\n" +
                "      \"Names of Private Institutes\":[\"Chirayu Medical College & Hospital, Bhopal Indore Highway, Bhaisakhedi, Bhopal\",\"Central Research Lab, R D Gardi Medical College, Surasa, Ujjain\",\"Central Lab, MZ 117-118, Yeshwant Plaza, Indore\",\"Lab Medicine, Bansal Hospital, A unit of Ayushman Medical Diagnostics Pvt Ltd, C-sector Shahpura, Bhopal\",\"Sri Aurobindo Institute of Medical Sciences, Indore\",\"L. N. Medical College & J. K. Hospital, Bhopal\",\"Chirayu Medical College & Hospital, Bhopal Indore Highway, Bhaisakhedi, Bhopal\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"District Tuberculosis Centre, Bhopal\",\"District Hospital Gwalior\",\"District Hospital Morena\",\"District Hospital Chhatarpur\",\"District Hospital Damoh\",\"District Hospital Sagar\",\"District Hospital Tikamgarh\",\"District Hospital, Mandsaur\",\"District Hospital, Neemuch\",\"District Tuberculosis Centre, Indore\",\"District Hospital Chhindwara\",\"District Hospital, Jabalpur\",\"District Hospital, Shahdol\",\"District Hospital, Burhanpur\",\"District Hospital, Singrauli\",\"District Tuberculosis Centre, Raisen\",\"District Hospital, Bhind\",\"District Hospital, Datia\",\"District Hospital, Sheopur\",\"District Hospital, Shivpuri\",\"District Hospital, Alirazpur\",\"District Hospital, Barwani\",\"District Hospital, Dhar\",\"District Hospital, Jhabua\",\"District Hospital, Khargone\",\"District Hospital, Agarmalwa\",\"District Hospital, Shajapur\",\"District Hospital, Ujjain\",\"District Hospital, Dindori\",\"District Tuberculosis Centre, Mandla\",\"District Hospital, Seoni\",\"District Hospital, Satna\",\"District Hospital, Sidhi\",\"District Hospital, Singrauli\",\"District Hospital, Umaria\",\"District Hospital, Panna\",\"District Hospital, Rewa\",\"District Hospital, Harda\",\"JSR Hospital, Itarsi\",\"CHC, Pipariya\",\"District Hospital, Rajgarh\",\"District Hospital, Sehore\",\"District Hospital, Vidisha\",\"District Hospital, Ashok Nagar\",\"District Hospital, Dewas\",\"District Hospital, Guna\",\"District Hospital, Khandwa\"],\n" +
                "      \"Names of Private Institutes\":[\"Sampurna Sodani Diagnostic Clinic, LG-1, Morya Centre, 16/1, Race Course Road, Indore\",\"Central Pathology Laboratory, People's Hospital, Bhanpur, Bhopal-462037\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"18\":{\n" +
                "  \"Names of States\":\"Manipur\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Jawaharlal Nehru Institute of Med. Sciences, Imphal-East, Manipur\",\"Regional Institute of Medical Sciences, Imphal\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"19\":{\n" +
                "  \"Names of States\":\"Meghalaya\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"North Eastern Indira Gandhi Regional Institute of Health & Medical Sciences, Shillong, Meghalaya\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Pasteur Institute, Shillong\",\"Civil Hospital, Tura\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"20\":{\n" +
                "  \"Names of States\":\"Mizoram\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Zoram Medical College\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Civil Hospital, Lunglei\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"21\":{\n" +
                "  \"Names of States\":\"Nagaland\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"State Referral BSL-3 Laboratory, Naga Hospital, Kohima\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Imkongliba Memorial District Hospital, Mokokchung\",\"District Hospital, Mon\",\"District Hospital, Dimapur, Nagaland\",\"CHC, Jalunkie, Nagaland\",\"District Hospital, Zunheboto\",\"District Hospital, Wokha\",\"District Hospital, Tuensang\",\"District Hospital, Phek\",\"District Hospital, Kiphire\",\"District Hospital, Longleng\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"22\":{\n" +
                "  \"Names of States\":\"Odisha\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"ICMR-Regional Medical Research Centre, Bhubaneshwar (High-throughput Laboratory)\",\"All India Institute of Medical Sciences, Bhubaneshwar\",\"SCB Medical College and Hospital, Cuttack\",\"MKCG Medical College, Berhampur\",\"Ispat General Hospital, Rourkela\",\"Veer Surendra Sai institute of Medical Science & Research, Sambalpur\",\"Institute of Life Sciences, Bhubaneshwar\",\"ICAR- International Centre for Foot and Mouth Disease, Khordha, Odisha\",\"Indian Institute of Science Education and Research (IISER), Berhampur\",\"Bhima Bhoi Medical College and Hospital, Bolangir\",\"Pandit Raghunath Murmu Medical College, Baripada\"],\n" +
                "      \"Names of Private Institutes\":[\"Dept of Lab Services, Apollo Hospitals, Bhubaneswar\",\"IMS & SUM Hospital, Bhubaneswar\",\"InDNA Life Sciences Pvt Ltd, 2nd Floor, KIIT TBI, Bhubaneswar\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"SLN Medical College and Hospital, Koraput\",\"Tribal Field Unit of RMRC, Bhawanipatna, Kalahandi\",\"Tribal Field Unit of RMRC, Rayagada\",\"Fakir Mohan Medical College and Hospital, Balasore\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Central Lab, AMRI Hospitals, Plot No 1, Khandagiri, Bhubaneswar\",\"Kalinga Institute of Medical Science (KIMS),Dept. of Microbiology, Bhubaneswar\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"23\":{\n" +
                "  \"Names of States\":\"Puducherry\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Jawaharlal Institute of Postgraduate Medical Education & Research, Puducherry\",\"Indira Gandhi Medical College, Puducherry\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Pondicherry Institute of Medical Sciences, Ganapathichettikulam, Kalapet, Puducherry\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory, Government Hospital for Chest Diseases, Gorimedu, Puducherry\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"24\":{\n" +
                "  \"Names of States\":\"Punjab\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Govt. Medical College, Amritsar\",\"Govt. Medical College, Patiala\",\"Guru Gobind Singh Medical University, Faridkot\"],\n" +
                "      \"Names of Private Institutes\":[\"Tuli Diagnostic Centre, Majitha Road, Amritsar\",\"Christian Medical College, Ludhiana\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"District Hospital, Barnala, Punjab\",\"District Hospital, Jalandhar, Punjab\",\"District Hospital, Ludhiana, Punjab\",\"District Hospital, Mansa, Punjab\",\"District Hospital, Pathankot, Punjab\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"IRL, Patiala\"],\n" +
                "      \"Names of Private Institutes\":[\"Department of Microbiology, Dayanand Medical College & Hospital, Tagore Nagar, Civil Lines, Ludhiana\",\"Dr Bhasin Path labs , A 96,97,98, Ranjit Avenue , Amritsar\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"25\":{\n" +
                "  \"Names of States\":\"Rajasthan\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Sawai Man Singh Medical College, Jaipur\",\"Rajasthan University of Health Sciences Medical College, Jaipur\",\"Dr. Sampurnan and Medical College, Jodhpur\",\"Jhalawar Medical College, Jhalawar\",\"RNT Medical College, Udaipur\",\"SP Medical College, Bikaner\",\"All India Institute of Medical Sciences, Jodhpu\",\"JLN Medical College, Ajmer\",\"Govt. Medical College, Kota\",\"National Institute for Implementation Research on Non-Communicable Diseases, Jodhpur\",\"RVRS Govt. Medical College, Bhilwara\",\"Government Medical College, Dungarpur\",\"Pandit Deendayal Upadhyaya Medical College, Churu\",\"Government Medical College, Bharatpur\",\"Government Medical College, Sikar\",\"Government Medical College, Barmer\",\"Government Medical College, Pali\",\"Government BDK District Hospital, Jhunjhunu\",\"Jaipur National University Institute for Medical Sciences and Research Centre, Jaipur\",\"Military Hospital, Jaipur\",\"District Hospital, Sirohi\"],\n" +
                "      \"Names of Private Institutes\":[\"Central Lab, The Mahatma Gandhi University of Medical Sciences and Technology, RIICO Institution Area, Sitapura, Tonk Road, Jaipur\",\"Dr. B Lal Clinical Lab Pvt Ltd, 6-E, Malviya Industrial Area, Malviya Nagar, Jaipur\",\"Brig T.K. Narayanan Dept of Pathology, Santokaba Durlabhji Memorial Hospital Cum Medical Research Institute, Jaipur (TruNat and RTPCR)\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"26\":{\n" +
                "  \"Names of States\":\"Tamil Nadu\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"King Institute of Preventive Medicine & Research, Chennai\",\"Madras Medical College, Chennai\",\"Stanley Medical College, Chennai\",\"Govt. Kilpauk Medical College, Chennai\",\"National Institute for Research in Tuberculosis, Chennai\",\"State Public Health Laboratory, Chennai\",\"ICMR-National Institute of Epidemiology, Chennai\",\"Dr. MGR Medical University, Chennai\",\"Dr. ALM PG Institute of Basic Medical Sciences, Chennai\",\"Govt. Medical College & Hospital, Omandurar Govt. Estate, Chennai\",\"Govt. Theni Medical College, Theni\",\"Tirunelveli Medical College, Tirunelveli\",\"Govt. Medical College, Thiruvarur\",\"Kumar Mangalam Govt. Medical College, Salem\",\"Coimbatore Medical College, Coimbatore\",\"Govt. Medical College & ESIC Hospital, Coimbatore\",\"Govt. Medical College, Villupuram\",\"Madurai Medical College, Madurai\",\"K A P Viswanatham Govt. Medical College, Trichy\",\"Perundurai Medical College, Perundurai\",\"Govt. Dharmapuri Medical College, Dharmapuri\",\"Govt. Medical College, Vellore\",\"Thanjavur Medical College, Thanjavur\",\"Kanyakumari Govt. Medical College, Nagercoil\",\"Govt. Thoothukudi Medical College, Thoothukudi\",\"Institute of Vector Control & Zoonoses, Hosur\",\"Pasteur Institute of India, Coonoor\",\"Rajah Muthiah Medical College, Chidambaram\",\"Government Medical College, Karur\",\"Govt. Tiruvannamalai Medical College & Hospital, Tiruvannamalai\",\"Chengalpattu Government Medical College, Kancheepuram\",\"Government Medical College and Hospital, Pudukkottai\",\"Government Shivagangai Medical College, Shivagangai\",\"Government District Headquarters Hospital, Virrudhu Nagar\",\"Government District Headquarters Hospital, Ramanathapuram\",\"Government District Headquarters Hospital, Ariyalur\",\"Government District Headquarters Hospital, Tiruppur\",\"Government Kallakruichi Hospital, Kallakurichi\",\"Government District Headquarters Hospital, Tiruvallur\",\"Govt District Headquarters Hospital, Namakkal\",\"Central Leather Research Institute, Adyar, Chennai\",\"Government Headquarters Hospital, Dindigul\",\"C.D Hospital, Greater Chennai Corporation, Chennai\"],\n" +
                "\n" +
                "\n" +
                "      \"Names of Private Institutes\":[\"Dept. of Clinical Virology, CMC, Vellore\",\"Department of Laboratory Services, Apollo Hospitals Enterprise Ltd, Chennai\",\"Neuberg Ehrlich Lab Pvt Ltd, 46-48 Masilamani Road, Balaji Nagar, Chennai\",\"Sri Ramachandra Medical College & Research Institute, Porur, Chennai\",\"Microbiology Lab, Veerakeralam Road, Coimbatore\",\"YRG CARE, Taramani, Chennai\",\"Hitech Diagnostic Centre- A Unit of Dr. Ganesan’s Hitech Diagnostic Centre PVt Ltd, Poonamallee High Road, Chennai\",\"PSG Hospitals Diagnostic Centre, Avinashi Road, Peelamedu, Coimbatore\",\"Medall Healthcare Pvt Ltd, 17, Race View Colony, 2nd street, Race Course Road, Guindy, Chennai\",\"Meenakshi Labs Madurai A unit of Sunmed Healthcare Pvt Ltd, 2nd Floor, Meenakshi Mission Hospital and Research Centre, Madurai\",\"Metropolis Healthcare Limited, No 3, Jaganathan Road, Nungambakkam, Chennai\",\"Clinical Lab Services, Dr. Rela Institute & Medical Centre, #7, CLC Works Rd, Shankar Nagar, Chennai\",\"Dept. of Clinical Virology, CMC, Vellore\",\"Doctors’ Diagnostic Centre, 123/1, Puthur High Road, Thiruchirapalli\",\"Molecular Testing, Institute of Lab Medicine, Kovai Medical Center & Hospital, 99, Avinashi Road, Coimbatore\",\"Chettinad Hospital and Research Institute, Chengalpattu\",\"Nu-Med Labs, 15A, Nellukara Street, Kanchipuram\",\"BioLine Laboratory, 43B-1, Cowley Brown Road, RS Puram, Coimbatore\",\"Premier Health Center, Crescent Court, Ground Floor No 963, Poonamallee High Road, Purasawalkam, Chennai\",\"Dept of Lab Medicine, Royalcare Super Speciality Hospital Ltd, 1/520, L&T Road, Neelambur, Coimbatore\",\"VRR Diagnostics, #87, Burkit Road, T. Nagar, Chennai\",\"Lab Services, Apollo Speciality Hospitals, P 3, KK Nagar East 1st Street, Madurai\",\"Orbito Asia diagnostics, Puliyakulam road, Coimbatore\",\"Lifecell International Pvt Ltd, No 26, Vandalur – Kelambakkam Main Road, Keelakottaiyur, Chennai\",\"Vivek Laboratories, 1159, K.P. Road, Nagercoil, Kanniyakumari\",\"Krsnaa Diagnostics Pvt Ltd, Krsnaa Diagnostics Coimbatore Medical College & Hospital, No 1619 A, Trichy Road,Coimbatore\",\"Central Laboratory, Sree Balaji Medical College and Hospital, Chennai\",\"Aarthi Scans and Labs. No 60, 100 feet road, Vadapalani, Chennai\",\"Balaji Medical Centre, Old No 18,New No 4,Jagadeeswaran Street, T.Nagar, Chennai\",\"Anderson Diagnostics and Labs, Kilpauk\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Karpaga Vinayaga Institute of Medical Sciences, Madhuranthgam Chengalpet\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Government Hospital of Thoracic Medicine, Tambaram Sanatorium, Chennai\"],\n" +
                "      \"Names of Private Institutes\":[\"MIOT Hospitals – Dept of Lab Medicine, 4/112, Mount Poonamallee Road, Manapakkam, Chennai\",\"Madras Medical Mission Clinical Lab Services, 4-A, Dr. J. Jayalalitha Nagar, Mogappair East, Chennai\",\"Clinical Lab Services, Sundaram Medical Foundation, Dr. Rangarajan Memorial Hospital, 9C, 4th Avenue, Shanthi Colony, Anna Nagar, Chennai\",\"VHS Lab Services, VHS Hospital, Rajiv Gandhi Salai, Taramani, Chennai\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"27\":{\n" +
                "  \"Names of States\":\"Telangana\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Gandhi Medical College, Secunderabad\",\"Osmania Medical College, Hyderabad\",\"Sir Ronald Ross of Tropical & Communicable Diseases, Hyderabad\",\"Nizam’s Institute of Medical Sciences, Hyderabad\",\"Institute of Preventive Medicine, Hyderabad\",\"ESIC Medical College, Hyderabad\",\"Kakatiya Medical College, Warangal\",\"Centre for Cellular & Molecular Biology, Hyderabad\",\"Centre for DNA Fingerprinting & Diagnostics, Hyderabad\"],\n" +
                "      \"Names of Private Institutes\":[\"Laboratory Services, Apollo Hospitals, 6th Floor, Health Street Building, Jubilee Hills, Hyderabad\",\"Vijaya Diagnostic Centre Pvt Ltd, Street No 19, Himayath Nagar, Hyderabad\",\"Vimta Labs Ltd, Plot No 142, Phase 2, IDA Cherlapally, Hyderabad\",\"Apollo Health and Lifestyle Limited, Diagnostic Labortory, Bowenpally, Secunderabad\",\"Dr. Remedies Labs Private Ltd, A3, Titus Plaza, Sharma Commercial Complex, Punjagutta, Hyderabad\",\"Pathcare Labs Pvt Ltd, Medchal, Hyderabad\",\"American Institute of Pathology And Lab Sciences Pvt Ltd, Citizens Hospital, Serilingampally, Hyderabad\",\"Medcis Pathlabs India Pvt Ltd, Plot No 16 & 17, Swathi Plaza, Anand Nagar, New Bowenpally, Secunderabad\",\"Department of Lab Medicine, Yashoda Hospital, 9th Floor, 1-1-156 & 157, Alexander Road, Secunderabad\",\"Biognosys Technologies (India) Pvt Ltd, #8-148/174/11, NRI Colony, Near Aleap Industrial Area, Medchal, Malkajgiri\",\"Tenet Diagnostics, Plot No 51, Kineta Towers, Journalist Colony, Road No 3, Banjara Hills, Hyderabad\",\"AIG Hospitals, Survey No 136, Plot No 2/3/4/5, 1, Mindspace Rd, Gachibowli, Hyderabad\",\"Cell Correct Diagnostics, Virinchi Hospitals, Road No 1, Banjara Hills, Hyderabad\",\"Krishna Institute of Medical Sciences Ltd, Dept of Lab Services, 1-8-31/1, Minister Road, Secunderabad\",\"MAPMYGENOME India Ltd, Royal Demeure, Plot No 12/2, Sector-1, HUDA TEchno Enclave, Madhapur, Hyderabad\",\"LEPRA Society-Blue Peter Public Health and Research Centre, Cherlapally, Near TEC Building, Hyderabad\",\"Lucid Medical Diagnostics Pvt Ltd, Plot No 203,203A, Vasavi Nagar, Karkhana,Secunderabad\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Dept of Lab Medicine, Star Hospital, A Unit of UniMed Healthcare Pvt Ltd, 8-2-594/B, Road No 10, Banjara Hills, Hyderabad\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Rajiv Gandhi Institute of Medical Sciences, Adilabad\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"28\":{\n" +
                "  \"Names of States\":\"Tripura\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Government Medical College, Agartala\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"29\":{\n" +
                "  \"Names of States\":\"Uttar Pradesh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"King George Medical University, Lucknow\",\"Institute of Medical Sciences, Banaras Hindu University, Varanasi\",\"Jawaharlal Nehru Medical College, Aligarh\",\"Command Hospital, Lucknow\",\"Lala Lajpat Rai Memorial Medical College, Meerut\",\"Sanjay Gandhi Post Graduate Institute, Lucknow\",\"MLN Medical College, Allahabad\",\"Uttar Pradesh University of Medical Sciences (Formerly Uttar Pradesh RIMS), Saifai\",\"MLB Medical College, Jhansi\",\"Regional Medical Research Centre, Gorakhpur\",\"SN Medical College, Agra\",\"National JALMA Institute for Leprosy & Other Mycobacterial Diseases, Agra\",\"RML Hospital, Lucknow\",\"Govt. Institute of Medical Sciences, Noida\",\"GSVM Medical College, Kanpur\",\"National Institute of Biologicals, Noida (High-throughput Laboratory)\",\"BRD Medical College, Gorakhpur\",\"Indian Institute of Toxicology Research, Lucknow\",\"Birbal Sahni Institute of Palaeosciences, Lucknow\",\"Central Drug Research Institute, Lucknow\",\"ICAR- Indian Veterinary Research Institute (IVRI), Izatnagar\",\"Super specialty PediatricHospital and Postgraduate Teaching Institute (SSPHPGTI), Noida\",\"College of Veterinary Sciences and Animal Husbandry, UP Pt. Deen Dayal Upadhyaya Pashu Chikitsa Vishwavidyalaya evam Go Anusandhan Sansthan, Mathura\"],\n" +
                "      \"Names of Private Institutes\":[\"RML Mehrotra Pathology Pvt Ltd, Nirala Nagar, Lucknow\",\"Dept of Lab Medicine, Jaypee Hospital, Sector 128, Noida\",\"Central Lab, Sharda Hospital, Plot no 32,34, Knowledge Park-III, Greater Noida\",\"RML Mehrotra Pathology Pvt Ltd, Nirala Nagar, Lucknow\",\"Medical Testing Lab, Yashoda Superspeciality Hospital, H-1, 24, 26-27, Kaushambi, Ghaziabad\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"District Combined Hospital, District Balrampur\",\"District Combined Hospital, District Basti\",\"Sarojani Naidu Samarak Hospital,District Firozabad\",\"Babu Eswar Saran District Hospital,Gonda\",\"Amar Shahid late Uma Nath Singh District Hospital, Jaunpur\",\"District Hospital, Bahraich\",\"District Male Hospital, Ayodhya\",\"District Combined Hospital, Mahrajganj\",\"District Femal Hospital, Muzaffarnagar\",\"District Male Hospital, Pratapgarh\",\"District Hospital, Ballia\",\"Pt.Ramprasad Bismil, District Combined Hospital Shahjahanpur\",\"Bairister Yusuf Emam, Divisional Hospital, Mirzapur\",\"Mahatama Gandhi, District Combined Hospital, Siddharth Nagar\",\"Balrampur Hospital, Lucknow\",\"Malkhan Singh Joint District Hospital,Aligarh\",\"District Hospital, Ambedkar Nagar\",\"Pt. Din Dayal Upahadhy, District Combined Hospital, Moradabad\",\"Motilal Nehru Divisional Hospital,Prayagraj\",\"Divisional Hospital, Azamgarh\",\"District Male Hospital, Budaun\",\"Babu Mohan Singh, Joint District Hospital, Deoria\",\"Bheemrao Ambedkar Multi Superspeciality District Hospital, G. B Nagar\",\"Netaji Subhash Chandra Bose District Hospital, Gorakhpur\",\"Rana Devi Madhav Singh, District Hospital, Raebareli\",\"District Hospital, Sultanpur\",\"Maharana Pratap Joint District Hospital,Bareily\",\"Rafi Ahmad Kidwai memorial District Hospital, Barabanki\",\"District Hospital, Etah\",\"M.M.G District Hospital, Ghaziabad\",\"Joint District Hospital, Manjhanpur,Kaushambi\",\"District Male Hospital, Sitapur\",\"District Male Hospital, Kheri\",\"Babu Banarasi Das govt. District Hospital, Bulandsahar\",\"District Hospital, Agra\",\"Joint District Hospital, Amethi\",\"District Hospital, Baghpat\",\"District Male & Eye Hospital, Padrauna,Kushinagar\",\"U.H.M District Male Hospital, Kanpur Nagar\",\"P.L. Sharma District Hospital, Meerut\",\"Shadilal Memorial Community Health Centre, Shamli\",\" Uma Shankar Dixit Joint District Hospital, Unnao\",\"S.S.P.G District Hospital, Varanasi\",\"100 Beded District Hospital, Aauraiya\",\"District Hospital, Banda\",\"District Combined Hospital, Bijnor\",\"Pt. Kamlapati Tripathi Joint District Hospital,Chandauli\",\"Joint District Hospital, Chitrakoot\",\"Dr. Bheemrao Ambedkar District Hospital,Etawah\",\"Dr. Ram Manohar Lohia District Hospital,Farrukabad\",\"District Hospital, Fatehpur\",\"Dr. Mukhtar Ansari District Hospital, Ghazipur\",\"Deewan Satrughan District Hospital,Hamirpur\",\"District Hospital, Hapur\",\"Pt. Ramdayal Trivedi District Hospital, Hardoi\",\"Bangla District Hospital, Hathras\",\"Joint District Hospital, J.P Nagar\",\"District Male Hospital, Jalaun\",\"Divisional District Hospital, Jhansi\",\"100 Beded District Hospital, Kannauj\",\"Joint District Hospital, Akbarpur, Kanpur Dehat\",\"Joint District Hospital, Kanshiram Nagar\",\"Manywar Kanshiram Joint District Hospital,Lalitpur\",\"District Hospital, Mahoba\",\"Maharaja Tez Singh District Hospital,Mainpur\",\"Mahrishi Dayanad Saraswati District Hospital,Mathura\",\"District Hospital, Ghazipur Tiraha, Mau\",\"Joint District Hospital, Pilibhit\",\"District Hospital, Rampur\",\"Seth Baldev Das District Hospital, Saharanpur\",\"Joint District Hospital, Manjhanpur, Sambhal\",\"Joint District Hospital, Sant Kabir Nagar\",\"Maharaja Balwant District Hospital,Santravidas Nagar\",\"Joint District Hospital, Bhinga, Shrawasti\",\"Joint District Hospital, Robertsganj,Sonbhadra\",\"12 Air Force Hospital, Gorakhpur\"],\n" +
                "      \"Names of Private Institutes\":[\"Heritage Speciality Lab, Lanka, Varanasi\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[\"Gian Life Care Ltd, 7/216(6), Swaroop Nagar, Kanpur\",\"Dept of Microbiology, Apollomedics Super Speciality Hospitals, KBC-31, Sector B, LDA Colony, Kanpur Road, Lucknow\",\"Scientific Pathology Agra, Durga Commercial Complex, Hariparwat Delhi Gate, Agra\",\"Department of Lab Sciences, Regency Hospital, A-2, Sarvodaya Nagar, Kanpur\",\"Charakdhar Diagnostics Pvt.Ltd. Basement, 292/05 Tulsidas Marg, Chowk, Near King George Medical University, Lucknow\n" +
                "\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"30\":{\n" +
                "  \"Names of States\":\"Uttarakhand\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Govt. Medical College, Haldwani\",\"All India Institute of Medical Sciences, Rishikesh\",\"Doon Medical College, Dehradun\",\"Veer Chandra Singh Garhwali Govt. Institute of Medical Science & Research, Srinagar, Pauri, Garhwal\",\"CSIR- Indian Institute of Petroleum, Dehradun\",\"ICAR- Indian Veterinary Research Institute (IVRI), Mukteswar\"],\n" +
                "      \"Names of Private Institutes\":[\"Dr. Ahuja’s Pathology and Imaging Centre, 7-B, Astley Hall, Dehradun\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Mela Hospital, Haridwar\",\"JLN Hospital, Rudrapur, USNagar\",\"District Hospital, Pithoragarh\",\"District Hospital, Uttarkashi\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"31\":{\n" +
                "  \"Names of States\":\"West Bengal\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"ICMR-National Institute of Cholera & Enteric Diseases, Kolkata\",\"Institute of Post Graduate Medical Education & Research, Kolkata\",\"Midnapore Medical College, Midnapore\",\"North Bengal Medical College, Darjeeling\",\"School of Tropical Medicine, Kolkata\",\"Malda Medical College & Hospital, Malda\",\"Command Hospital, Kolkata\",\"Chittaranjan National Cancer Institute, Kolkata\",\"R.G. Kar Medical College & Hospital, Kolkata\",\"Murshidabad Medical College, Behrampore, Murshidabad\",\"Nil Ratan Sircar Medical College, Kolkata\",\"Bankura Medical College, Bankura\",\"Suri Sadar Hospital, Birbhum\",\"Medical College, Kolkata\",\"College of Medicine and JNM Hospital,Kalyani, Nadia\"],\n" +
                "      \"Names of Private Institutes\":[\"Apollo Gleneagles Hospitals, 58 Canal Circular Road, Kolkata\",\"Tata Medical Center, Rajarhat, Kolkata\",\"Suraksha Diagnostic Pvt Ltd (Dept of Lab Services), 12/1, Premises No 02/0327, DG Block (Newtown), Action Area 1D, Newtown, Kolkata\",\"Dr. Lal Pathlabs Ltd – Kolkata Reference lab, Plot No CB-31/1, Premises No 031-0199, Street No 199, Action Area 1C, Newtown, Kolkata\",\"Dept of Lab Services, Medica Superspeciality Hospital, 127, Mukundpur, E.M. Bypass, Kolkata\",\"Remedy Life Care, Plot 6, Nani Gopal Roy Chowdhury Avenue, Entally, Padmapukur On Main Road, Kolkata\",\"Shri Ramkrishna Institute of Medical Sciences & Sanaka Hospitals, Malandighi, P.S. Kanksha, Durgapur\",\"Laboratory Services, Peerless Hospitex Hospital & Research Centre, 360,Panchasayar, Kolkata\",\"Desun Reference Lab - A Unit of Desun Healthcare and Research Institute Limited(Department of Laboratory Services), S-16,Kasba Industrial Estate, Phase III, Kolkata\"]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Calcutta National Medical College and Hospital, Kolkata\",\"Raiganj Government Medical College and Hospital, Raiganj\",\"Falakata SSH Hospital, District Alipurduar\",\"Jhargram District Hospital, Jhargram District\",\"Rampurhat Govt. Medical College and Hospital, Rampurhat, Birbhum\",\"Diamond Harbour Government Medical College and Hospital, Diamond Harbour\",\"Asansol District Hospital, Asansol\",\"Balurghat District Hospital, Dakshin Dinajpur\",\"Jangipur Sub-Divisional Hospital, Murshidabad\",\"Uluberia Sub Divisional Hospital, Howrah\",\"Serampore Walsh Sub-divisional Hospital, Hugli\",\"Biswa Bangla Krirangan SARI/COVID Hospital, Jalpaiguri\",\"Chanchal Sub-divisional Hospital (SDH),Malda\",\"Purulia Government Medical College and Hospital, Purulia \",\"M R Bangur District Hospital, Kolkata\",\"Cooch Behar Government Medical College, Cooch Behar\",\"Alipurduar District Hospital, Alipurduar\"],\n" +
                "      \"Names of Private Institutes\":[\"Anandaloke Sonoscan Centre Pvt.Ltd., 3/3 Hill Cart Road, Near Mahananda Bridge, Siliguri\"]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"Burdwan Medical College, Burdwan\",\"College of Medicine and Sagore Dutta Hospital, Kolkata\"],\n" +
                "      \"Names of Private Institutes\":[\"Dept of Lab Medicine, AMRI Hospital, 38/1A, Gariahat Road, Kolkata\",\"Laboratory Services, Peerless Hospitex Hospital & Research Centre, 360, Panchasayar, Kolkata\",\"AMRI Hospitals, Dept of Lab Medicine, JC 16-17, Sector III, Salt Lake City, Kolkata\",\"The Calcutta Medical Research Institute (Dept of Pathology), 7/2 Diamond Harbour Raod, Kolkata\",\"Dept of Lab Medicine, AMRI Hospital, 38/1A, Gariahat Road, Kolkata\"]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"32\":{\n" +
                "  \"Names of States\":\"Andaman & Nicobar Islands\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"ICMR-Regional Medical Research Centre, Port Blair\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"A&N Islands Institute of Medical Sciences, Port Blair\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[\"GB Pant Hospital, Port Blair\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"33\":{\n" +
                "  \"Names of States\":\"Dadra & Nagar Haveli\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Shri Vinoba Bhave Civil Hospital, Silvassa\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  },\n" +
                "  \"34\":{\n" +
                "  \"Names of States\":\"Sikkim\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Virus Research and Diagnostic Laboratory, STNM Hospital, Gangtok\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Intermediate Reference Laboratory,STNM Hospital, Gangtok\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  }\n" +
                "  ,\n" +
                "  \"35\":{\n" +
                "  \"Names of States\":\"Leh-Ladakh\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[\"Sonam Nurboo Memorial Hospital, Leh\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  }\n" +
                "  ,\n" +
                "  \"36\":{\n" +
                "  \"Names of States\":\"Lakshadweep\",\n" +
                "  \"Test Category\":{\n" +
                "    \"RT-PCR\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"TrueNat\":{\n" +
                "      \"Names of Government Institutes\":[\"Lakshadweep Medical Store, Kochi\",\"Indira Gandhi Hospital, Kavaratti Island\",\"Rajiv Gandhi Specialty Hospital, Agatti\"],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          },\n" +
                "    \"CB NAAT\":{\n" +
                "      \"Names of Government Institutes\":[],\n" +
                "      \"Names of Private Institutes\":[]\n" +
                "          }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        try {
            JSONObject object = new JSONObject(jsonString);
            Iterator<String> keys = object.keys();

            String key = keys.next();
            JSONObject totobj = object.getJSONObject(key);

            t = tot.getText() + totobj.optString("Total No. of Labs");
            g = govtot.getText() + totobj.optString("Government laboratories");
            p = privtot.getText() + totobj.optString("Private laboratories");
            tot.setText(t);
            govtot.setText(g);
            privtot.setText(p);


            while (keys.hasNext()) {
                key = keys.next();
                JSONObject object1 = object.getJSONObject(key);
                String state = object1.optString("Names of States");
                hashMaps.put(state, new HashMap<String, HashMap<String, ArrayList<String>>>());
                hashMaps.get(state).put("1", new HashMap<String, ArrayList<String>>());
                hashMaps.get(state).put("2", new HashMap<String, ArrayList<String>>());
                hashMaps.get(state).put("3", new HashMap<String, ArrayList<String>>());
                hashMaps.get(state).put("1", new HashMap<String, ArrayList<String>>());
                hashMaps.get(state).put("2", new HashMap<String, ArrayList<String>>());
                hashMaps.get(state).put("3", new HashMap<String, ArrayList<String>>());

                object1 = object1.getJSONObject("Test Category");

                hashMaps.get(state).get("1").put("gov", new ArrayList<String>());
                hashMaps.get(state).get("1").put("priv", new ArrayList<String>());
                hashMaps.get(state).get("2").put("gov", new ArrayList<String>());
                hashMaps.get(state).get("2").put("priv", new ArrayList<String>());
                hashMaps.get(state).get("3").put("gov", new ArrayList<String>());
                hashMaps.get(state).get("3").put("priv", new ArrayList<String>());

                JSONArray RT_PCRgov = object1.getJSONObject("RT-PCR").getJSONArray("Names of Government Institutes");
                JSONArray RT_PCRpriv = object1.getJSONObject("RT-PCR").getJSONArray("Names of Private Institutes");
                JSONArray TrueNatgov = object1.getJSONObject("TrueNat").getJSONArray("Names of Government Institutes");
                JSONArray TrueNatpriv = object1.getJSONObject("TrueNat").getJSONArray("Names of Private Institutes");
                JSONArray CBNAATgov = object1.getJSONObject("CB NAAT").getJSONArray("Names of Government Institutes");
                JSONArray CBNAATpriv = object1.getJSONObject("CB NAAT").getJSONArray("Names of Private Institutes");

                for (int i = 0; i < RT_PCRgov.length(); i++) {
                    hashMaps.get(state).get("1").get("gov").add((String) RT_PCRgov.get(i));
                }
                for (int i = 0; i < RT_PCRpriv.length(); i++) {
                    hashMaps.get(state).get("1").get("priv").add((String) RT_PCRpriv.get(i));
                }
                for (int i = 0; i < TrueNatgov.length(); i++) {
                    hashMaps.get(state).get("2").get("gov").add((String) TrueNatgov.get(i));
                }
                for (int i = 0; i < TrueNatpriv.length(); i++) {
                    hashMaps.get(state).get("2").get("priv").add((String) TrueNatpriv.get(i));
                }
                for (int i = 0; i < CBNAATgov.length(); i++) {
                    hashMaps.get(state).get("3").get("gov").add((String) CBNAATgov.get(i));
                }
                for (int i = 0; i < CBNAATpriv.length(); i++) {
                    hashMaps.get(state).get("3").get("priv").add((String) CBNAATpriv.get(i));
                }

                states.add(state);
            }
            dataAdapter.notifyDataSetChanged();
            getall();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    void getall() {

        labs.clear();
        int t = 0, g = 0, p = 0;

        for (Map.Entry e : hashMaps.entrySet()) {
            if (govt) {
                ArrayList gov = hashMaps.get(e.getKey()).get("1").get("gov");
                for (int i = 0; i < gov.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("RT-PCR", "Government"))));
                    g++;
                }
                gov = hashMaps.get(e.getKey()).get("2").get("gov");
                for (int i = 0; i < gov.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("TrueNat", "Government"))));
                    g++;
                }
                gov = hashMaps.get(e.getKey()).get("3").get("gov");
                for (int i = 0; i < gov.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("CB NAAT", "Government"))));
                    g++;
                }
            }

            if (privat) {
                ArrayList priv = hashMaps.get(e.getKey()).get("1").get("priv");
                for (int i = 0; i < priv.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("RT-PCR", "Private"))));
                    p++;
                }

                priv = hashMaps.get(e.getKey()).get("2").get("priv");
                for (int i = 0; i < priv.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("TrueNat", "Private"))));
                    p++;
                }

                priv = hashMaps.get(e.getKey()).get("3").get("priv");
                for (int i = 0; i < priv.size(); i++) {
                    labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("CB NAAT", "Private"))));
                    p++;
                }
            }


        }
        customLabsAdapter.notifyDataSetChanged();
        t = p + g;

        tot.setText("Total No. of Labs : " + t);
        privtot.setText("Private laboratories : " + p);
        govtot.setText("Government laboratories : " + g);

    }

    void getstate() {

        labs.clear();
        int t = 0, g = 0, p = 0;


        for (Map.Entry e : hashMaps.entrySet()) {
            if (e.getKey().equals(stateselected)) {
                if (govt) {
                    ArrayList gov = hashMaps.get(e.getKey()).get("1").get("gov");
                    for (int i = 0; i < gov.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("RT-PCR", "Government"))));
                        g++;
                    }
                    gov = hashMaps.get(e.getKey()).get("2").get("gov");
                    for (int i = 0; i < gov.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("TrueNat", "Government"))));
                        g++;
                    }
                    gov = hashMaps.get(e.getKey()).get("3").get("gov");
                    for (int i = 0; i < gov.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) gov.get(i), new Pair<String, String>("CB NAAT", "Government"))));
                        g++;
                    }
                }

                if (privat) {
                    ArrayList priv = hashMaps.get(e.getKey()).get("1").get("priv");
                    for (int i = 0; i < priv.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("RT-PCR", "Private"))));
                        p++;
                    }

                    priv = hashMaps.get(e.getKey()).get("2").get("priv");
                    for (int i = 0; i < priv.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("TrueNat", "Private"))));
                        p++;
                    }

                    priv = hashMaps.get(e.getKey()).get("3").get("priv");
                    for (int i = 0; i < priv.size(); i++) {
                        labs.add((new Pair<String, Pair<String, String>>((String) priv.get(i), new Pair<String, String>("CB NAAT", "Private"))));
                        p++;
                    }
                }
                break;
            }

        }
        customLabsAdapter.notifyDataSetChanged();
        t = p + g;

        tot.setText("Total No. of Labs : " + t);
        privtot.setText("Private laboratories : " + p);
        govtot.setText("Government laboratories : " + g);

    }


}

