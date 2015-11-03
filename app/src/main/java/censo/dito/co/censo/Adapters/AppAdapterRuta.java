package censo.dito.co.censo.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.R;

public class AppAdapterRuta extends BaseAdapter {

    private Activity actx;
    List<Route> elements;

    public AppAdapterRuta(Activity actx, List<Route> elements){
        this.actx = actx;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Route getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_rutas, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.nombre.setText(String.format("%s", elements.get(position).getDescription()));
        String state = null;
        if (elements.get(position).getState() == 0){
            state = "Finalizada";
        }else if (elements.get(position).getState() == 1){
            state = "Pendiente";
        }else if (elements.get(position).getState() == 2){
            state = "Inicializada";
        }

        holder.estado.setText(String.format("%s", state));

        if (elements.get(position).getState() == 2 || elements.get(position).getState() == 1){
            holder.estado.setTextColor(Color.RED);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView nombre;
        public TextView estado;

        public ViewHolder(View view) {
            nombre = (TextView) view.findViewById(R.id.txtNombreRuta);
            estado = (TextView) view.findViewById(R.id.txtEstado);
            view.setTag(this);
        }
    }

}
