package com.example.consumer_client.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.consumer_client.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private NotificationAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(NotificationAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notiTitle;
        TextView notiContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notiTitle=(TextView) itemView.findViewById(R.id.notiTitle);
            notiContent = (TextView) itemView.findViewById(R.id.notiContent);
        }
    }

    private ArrayList<NotificationItem> mList = null;

    public NotificationAdapter(ArrayList<NotificationItem> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_notification, parent, false);
        NotificationAdapter.ViewHolder vh = new NotificationAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationItem item = mList.get(position);
        holder.notiTitle.setText(item.getNotiTitle());
        holder.notiContent.setText(item.getNotiContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
