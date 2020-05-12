package com.example.test_mysql;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


@SuppressWarnings("deprecation")
public class Recycler_ViewAdapter_all_users extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<List_All_Users> mList;
    private Context context;


    public Recycler_ViewAdapter_all_users(List<List_All_Users> mList, Context context) {

        this.mList = mList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case 1: {
                View v = inflater.inflate(R.layout.row_item_all_users, viewGroup, false);
                viewHolder = new MenuItemViewHolder(v);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        final List_All_Users model = mList.get(holder.getAdapterPosition());

        switch (holder.getItemViewType()) {
            case 1: {
                final MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;

                menuItemHolder.Text_UserName.setText(model.getUser_name());
                menuItemHolder.Text_Email.setText(model.getEmail());
                menuItemHolder.Text_DateTime_All_Users.setText(model.getRegDate());

                if (MainActivity.Local_UserKey.equals(model.getUserKey())) {
                    menuItemHolder.Row_Card_View.setCardBackgroundColor(Color.parseColor("#2F00BCD4"));
                }

                try {
                    String UserAvatar = MainActivity.Main_Link + "Images/" + model.getAvatar_img();
                    Picasso.with(context.getApplicationContext()).load(UserAvatar)
                            .error(R.drawable.avatar)
                            .placeholder(R.drawable.avatar).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .into(menuItemHolder.ImgAvatar);


                } catch (Exception e) {
                }


                menuItemHolder.Row_Card_View.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.UserKey = model.getUserKey();
                        MainActivity.UserName = model.getUser_name();
                        MainActivity.UserEmail = model.getEmail();
                        MainActivity.UserAvatar = model.getAvatar_img();
                        context.startActivity(new Intent(context, User_Profile.class));
                    }
                });


                break;
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public CardView Row_Card_View;
        public CircleImageView ImgAvatar;
        public TextView Text_UserName;
        public TextView Text_Email;
        public TextView Text_DateTime_All_Users;


        public MenuItemViewHolder(View view) {
            super(view);
            Row_Card_View = view.findViewById(R.id.cardView_All_Users);
            ImgAvatar = view.findViewById(R.id.Img_Avatar);
            Text_UserName = view.findViewById(R.id.Text_UserName);
            Text_Email = view.findViewById(R.id.Text_Email);
            Text_DateTime_All_Users = view.findViewById(R.id.Text_RegDate);

        }
    }

//END
}
