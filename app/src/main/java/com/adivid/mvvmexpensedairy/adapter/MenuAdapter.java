package com.adivid.mvvmexpensedairy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.model.Menu;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<Menu> {

    private List<Menu> list;
    private Context context;
    private int resource;

    public MenuAdapter(@NonNull Context context, int resource, List<Menu> list) {
        super(context, resource);
        this.list = list;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.menu_list_item, null, false);

        ImageView imageView = view.findViewById(R.id.iv_menu_icon);
        TextView textViewMenuName = view.findViewById(R.id.menu_name);

        Menu menu = list.get(position);

        imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                menu.getImage(), null));
        textViewMenuName.setText(menu.getMenu_name());

        return view;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
