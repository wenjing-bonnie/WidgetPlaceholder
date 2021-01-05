package com.android.placeholder;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.placeholder.holder.PlaceHolder;
import com.android.widgetplaceholder.R;

import java.util.List;

/**
 * Created by wenjing.liu on 2020/12/31 in J1.
 *
 * @author wenjing.liu
 */
public class PlaceHolderListViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> dataSources;
    private LayoutInflater inflater;
    private PlaceHolder placeHolder;
    private boolean isCalled = false;

    public PlaceHolderListViewAdapter(Context context, List<String> sources, PlaceHolder holder) {
        this.context = context;
        this.dataSources = sources;
        this.placeHolder = holder;
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

    /**
     * 其实ListView在加载预占位UI的时候，我只需要修改该item的背景就可以了，所以仍在外面
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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
        String message;
        if (position % 2 == 0) {
            message = String.format("第%d行: 永定河发源于山西省宁武县管涔山，全河流经山西、内蒙古、河北、北京、天津五省市，在天津汇于海河，至塘沽注入渤海", position);
        } else {
            message = String.format("第%d行：救荒本草是哪个朝代朱棣编纂的一部专著", position);
        }
        holder.tvMessage.setText(message);

        placeHolder.startPlaceHolderChild(R.id.lv_test_place_holder, position,
                holder.tvTitle, holder.tvMessage, holder.ivImage);
        return convertView;
    }


    private class ItemViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvMessage;
    }
}
