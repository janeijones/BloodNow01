package zetrixweb.com.bloodnow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zetrixweb.com.bloodnow.R;



public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    Context context;

    public NotificationAdapter(Context context) {

        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(View itemView) {
            super(itemView);
        }
    }

}
