package fcm.test.sockettest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fcm.test.sockettest.R;
import fcm.test.sockettest.model.data.Message;
import fcm.test.sockettest.model.data.ViewType;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Message> messages = new ArrayList<>();

    Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType == ViewType.MY_MESSAGE) {
            View view = inflater.inflate(R.layout.item_mine, parent, false);
            return new MineMessageViewHolder(view);
        }else if(viewType == ViewType.USER_MESSAGE) {
            View view = inflater.inflate(R.layout.item_not_mine, parent, false);
            return new UserMessageViewHolder(view);
        }else {
            View view = inflater.inflate(R.layout.item_info, parent, false);
            return new InfoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name;
        String message;

        name = messages.get(position).getName();
        message = messages.get(position).getMessage();

        if(holder instanceof MineMessageViewHolder) {
            ((MineMessageViewHolder) holder).message.setText(message);
            ((MineMessageViewHolder) holder).user_name.setText(name);
        }
        if(holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).user_name.setText(name);
            ((UserMessageViewHolder) holder).message.setText(message);
        }
        if(holder instanceof InfoViewHolder) {
            ((InfoViewHolder) holder).message.setText(messages.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getViewType();
    }

    public void addItem(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        TextView message;

        public InfoViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.info_txt);
        }
    }

    public class MineMessageViewHolder extends RecyclerView.ViewHolder {

        TextView message, user_name;

        public MineMessageViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.mine_msg);
            user_name = itemView.findViewById(R.id.mine_txt);
        }
    }

    public class UserMessageViewHolder extends RecyclerView.ViewHolder {

        TextView message, user_name;

        public UserMessageViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.user_msg);
            user_name = itemView.findViewById(R.id.user_txt);
        }
    }
}
