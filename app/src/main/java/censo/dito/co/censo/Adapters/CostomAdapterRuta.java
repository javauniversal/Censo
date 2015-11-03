package censo.dito.co.censo.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Ruta;
import censo.dito.co.censo.R;

public class CostomAdapterRuta extends BucketListAdapterRuta {

    private Activity mActivity;
    private DBHelper mydb;

    public CostomAdapterRuta(Activity ctx, List<Ruta> elements) {
        super(ctx, elements);
        this.mActivity = ctx;
        mydb = new DBHelper(ctx);
    }

    @Override
    protected View getBucketElement(int position, final Ruta currentElement) {

        final ViewHolder2 holder;
        View bucketElement;
        LayoutInflater inflater = mActivity.getLayoutInflater();
        bucketElement = inflater.inflate(R.layout.item_rutas, null);
        holder = new ViewHolder2(bucketElement);
        bucketElement.setTag(holder);

        holder.nombre.setText(String.format("%s", currentElement.get_nombre()));
        holder.estado.setText(String.format("%s", currentElement.get_estadoRuta()));

        return bucketElement;
    }

    class ViewHolder2 {
        public TextView nombre;
        public TextView estado;

        ViewHolder2(View row){
            nombre = (TextView) row.findViewById(R.id.txtNombreRuta);
            estado = (TextView) row.findViewById(R.id.txtEstado);
        }
    }

}
