package zetrixweb.com.bloodnow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;



public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder>{

    Context context;
    Map<String,RecordDonationPojo> hashMap;
    ArrayList<String> stringArrayList;
    RecordDonationPojo recordDonationPojo;

    public HistoryAdapter(Context context,  Map<String,RecordDonationPojo> hashMap) {
        this.context = context;
        this.recordDonationPojo = recordDonationPojo;
        this.hashMap = hashMap;
        stringArrayList = new ArrayList<>(hashMap.keySet());
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
       RecordDonationPojo map = hashMap.get(stringArrayList.get(position));

        holder.tvDate.setText("Date   : " + map.getDate());
        holder.tvPlace.setText("Place : " + map.getPlace());
        holder.tvCity.setText("City    : " + map.getCity());
        holder.type.setText("Type   : " + map.getType());

    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvPlace;
        TextView tvCity;
        TextView type;

        public viewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.rd_date);
            tvPlace = (TextView) itemView.findViewById(R.id.rd_place);
            tvCity = (TextView) itemView.findViewById(R.id.rd_city);
            type = (TextView) itemView.findViewById(R.id.rd_donationType);

        }
    }
}
