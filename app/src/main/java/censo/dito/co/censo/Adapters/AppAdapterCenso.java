package censo.dito.co.censo.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import censo.dito.co.censo.Entity.Censu;
import censo.dito.co.censo.R;

public class AppAdapterCenso extends BaseAdapter {

    private Activity actx;
    List<Censu> elements;

    public AppAdapterCenso(Activity actx, List<Censu> elements){
        this.actx = actx;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Censu getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(actx, R.layout.item_censo, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.descripcion1.setText(elements.get(position).getDescriptionLine1());
        holder.descripcion2.setText(elements.get(position).getDescriptionLine2());

        return convertView;
    }

    class ViewHolder {
        public TextView descripcion1;
        public TextView descripcion2;

        public ViewHolder(View view) {
            descripcion1 = (TextView) view.findViewById(R.id.txtDes1);
            descripcion2 = (TextView) view.findViewById(R.id.txtDes2);
            view.setTag(this);
        }
    }
}
