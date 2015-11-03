package censo.dito.co.censo.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import censo.dito.co.censo.ActDestallMaps;
import censo.dito.co.censo.Adapters.AppAdapterRuta;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.R;

public class FragmentRoute extends Fragment {

    private SwipeMenuListView mListView = null;
    private DBHelper mydb;
    AppAdapterRuta adapter;
    Activity activity;
    List<Route> rutas;

    public FragmentRoute() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        mydb =  new DBHelper(view.getContext());
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        menuListView();

        rutas = mydb.getRuta();
        adapter = new AppAdapterRuta(activity, rutas);
        adapter.notifyDataSetChanged();
        mListView.setAdapter(adapter);
    }

    private void menuListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Finalizar");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                //--
                //openItem.setIcon(R.drawable.ic_action_edit);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                deleteItem.setWidth(dp2px(90));

                deleteItem.setTitle("Iniciar");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //AddProductCar item = mAppList.get(position);
                switch (index) {
                    case 0:

                        if (!mydb.validateCanEndRoute(rutas.get(position).getId())) {
                            Toast.makeText(activity, "No es posible finalizar una ruta que no se ha iniciado", Toast.LENGTH_LONG).show();
                        }else {
                            Date date = new Date();
                            Route ruta = new Route();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                            ruta.setId(mydb.getRuta().get(position).getId());
                            ruta.setStart(dateFormat.format(date.getTime()));

                            mydb.updateRuta(ruta,2);

                            List<Route> rutas = mydb.getRuta();
                            adapter = new AppAdapterRuta(activity, rutas);
                            adapter.notifyDataSetChanged();
                            mListView.setAdapter(adapter);

                        }

                        break;
                    case 1:
                        // delete
                        //createDialog(position);
                        if (!mydb.validarInicioRuta(0, rutas.get(position).getId())){
                            Toast.makeText(activity, "No es posible iniciar una ruta que ya finalizó", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (mydb.validarInicioRuta(2)){
                            Date date = new Date();
                            Route ruta = new Route();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                            ruta.setId(mydb.getRuta().get(position).getId());
                            ruta.setStart(dateFormat.format(date));

                            mydb.updateRuta(ruta, 0);
                            List<Route> rutas = mydb.getRuta();
                            adapter = new AppAdapterRuta(activity, rutas);
                            adapter.notifyDataSetChanged();
                            mListView.setAdapter(adapter);
                        }else {
                            Toast.makeText(activity, "Ya tiene una Ruta en Ejecución", Toast.LENGTH_LONG).show();
                        }

                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!mydb.validarInicioRuta(2)){
                    //Abrir
                    Bundle bundle = new Bundle();
                    bundle.putInt("idRuta", rutas.get(position).getId());
                    startActivity(new Intent(getActivity(), ActDestallMaps.class).putExtras(bundle));
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    Toast.makeText(activity, "No Se tiene información de la ruta", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
