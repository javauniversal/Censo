package censo.dito.co.censo.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.List;

import censo.dito.co.censo.Entity.DrawerItem;
import censo.dito.co.censo.Entity.DrawerMenu;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DrawerItem> drawerItems;

    private OnItemClickListener listener;

    private DisplayImageOptions options1;

    private ImageLoader imageLoader1;

    private Activity activity;

    public DrawerItemAdapter(List<DrawerItem> drawerItems, Activity activity) {
        this.drawerItems = drawerItems;
        this.activity = activity;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (DrawerItem.Type.values()[viewType]) {
            case HEADER:
                View headerRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);

                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity).build();
                imageLoader1 = ImageLoader.getInstance();
                imageLoader1.init(config);
                //Setup options for ImageLoader so it will handle caching for us.
                options1 = new DisplayImageOptions.Builder()
                        .cacheInMemory()
                        .cacheOnDisc()
                        .build();

                CircleImageView profileImage = (CircleImageView) headerRootView.findViewById(R.id.profile_image);

                ImageLoadingListener listener = new ImageLoadingListener(){
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                    }
                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                        // TODO Auto-generated method stub
                    }
                };

                imageLoader1.displayImage(LoginResponse.getLoginRequest().getUser().getPhotoURL(), profileImage, options1, listener);

                TextView numberUser = (TextView) headerRootView.findViewById(R.id.txtNumberUser);
                numberUser.setText(LoginResponse.getLoginRequest().getUser().getFullName());
                return new HeaderViewHolder(headerRootView);
            case DIVIDER:
                View dividerRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_divider, parent, false);
                return new DividerViewHolder(dividerRootView);
            case MENU:
                View menuRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu, parent, false);
                return new MenuViewHolder(menuRootView);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onClick(view, position);
            }
        });
        DrawerItem drawerItem = drawerItems.get(position);
        switch (drawerItem.getType()) {
            case MENU:
                MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
                DrawerMenu drawerMenu = (DrawerMenu) drawerItem;
                menuViewHolder.itemTextView.setText(drawerMenu.getText());
                menuViewHolder.itemTextView.setCompoundDrawablesWithIntrinsicBounds(drawerMenu.getIconRes(), 0, 0, 0);
                break;

        }
    }

    public String getItemIdger(int position) {

        DrawerItem drawerItem = drawerItems.get(position);
        DrawerMenu drawerMenu = (DrawerMenu) drawerItem;
        return drawerMenu.getText();

    }


    @Override
    public int getItemViewType(int position) {
        return drawerItems.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View rootView) {
            super(rootView);
        }
    }

    private static class DividerViewHolder extends RecyclerView.ViewHolder {

        public DividerViewHolder(View rootView) {
            super(rootView);
        }
    }

    private static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTextView;

        public MenuViewHolder(View rootView) {
            super(rootView);
            itemTextView = (TextView) rootView.findViewById(R.id.item);

        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}