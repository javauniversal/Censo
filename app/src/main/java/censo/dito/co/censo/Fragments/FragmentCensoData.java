package censo.dito.co.censo.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.HashMap;

import censo.dito.co.censo.Activities.ActDetalleCenso;
import censo.dito.co.censo.Adapters.AppAdapterCenso;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.ListCensu;
import censo.dito.co.censo.R;
import censo.dito.co.censo.Services.ServiceData;

public class FragmentCensoData extends Fragment {

    private SwipeMenuListView mListView = null;
    private DBHelper mydb;
    private AppAdapterCenso adapter;
    private Activity activity;
    private int mParam1;
    private ListCensu loginResponse;

    public static FragmentCensoData newInstance(Bundle bundle) {
        FragmentCensoData fragment = new FragmentCensoData();
        fragment.setArguments(bundle);
        return fragment;
    }

    public FragmentCensoData() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("idRuta");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_censo_data, container, false);
        mydb =  new DBHelper(view.getContext());
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        menuListView();
        servicesCensoData();
    }

    private void servicesCensoData(){

        HashMap<String, Object> postParameters = new HashMap<String, Object>();
        //postParameters.put("userId", getLoginRequest().getUser().getId());
        postParameters.put("routeId", mParam1);
        String jsonParameters = new Gson().toJson(postParameters);

        ServiceData serviceData = new ServiceData(getActivity(), String.format("%1$s%2$s", getString(R.string.url_administration), "GetCensuData"), jsonParameters);
        serviceData.PostClick();
        parseJSON(serviceData.dataServices);

    }

    private void menuListView() {
        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());
        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("xml", loginResponse.get(position).getData());

                startActivity(new Intent(getActivity(), ActDetalleCenso.class).putExtras(bundle));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    private void parseJSON(String json) {

        try {
            if (!json.equals("\"No hay datos!\"")){
                Gson gson = new Gson();
                loginResponse = gson.fromJson(json, ListCensu.class);

                adapter = new AppAdapterCenso(activity, loginResponse);
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
            }else{
                Toast.makeText(getActivity(), json, Toast.LENGTH_SHORT).show();
            }
        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }



}
