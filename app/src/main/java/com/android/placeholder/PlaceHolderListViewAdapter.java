package com.android.placeholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.widgetplaceholder.R;

import java.util.List;

/**
 * Created by file template on 2020/12/31 in J1.
 * Do not forget to add author for this java !!!!
 *
 * @author
 */

/**
 * Created by wenjing.liu on 2020/12/31 in J1.
 *
 * @author wenjing.liu
 */
public class PlaceHolderListViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataSources;
    private LayoutInflater inflater;

    public PlaceHolderListViewAdapter(Context context, List<String> sources) {
        this.context = context;
        this.dataSources = sources;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSources.size();
    }

    @Override
    public String getItem(int position) {
        return dataSources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            holder = new ItemViewHolder();
            convertView = inflater.inflate(R.layout.item_place_holder_list_view, parent, false);

            holder.ivImage = convertView.findViewById(R.id.iv_image);
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.tvMessage = convertView.findViewById(R.id.tv_message);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(dataSources.get(position));
        holder.tvMessage.setText(position + ": 永定河发源于山西省宁武县管涔山，全河流经山西、内蒙古、河北、北京、天津五省市，在天津汇于海河，至塘沽注入渤海。是海河水系北系的最大河流，流域面积为 47016平方公里。");
        return convertView;
    }

    private class ItemViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvMessage;
    }
}
