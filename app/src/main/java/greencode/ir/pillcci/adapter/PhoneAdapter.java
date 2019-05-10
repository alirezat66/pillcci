package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PhoneBook;

/**
 * Created by alireza on 5/21/18.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder>  {
    ArrayList<PhoneBook> list;
    Context context;
    ViewHolder viewHolder1;
    public onPhoneClick myListener;
    public PhoneAdapter(Context context1, ArrayList<PhoneBook> list,onPhoneClick myListener) {
        this.list = list;
        context = context1;
        this.myListener = myListener;
    }

    public void remove(PhoneBook phoneBook) {
        list.remove(phoneBook);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPhone,txtTell;
        ImageView imgLogo;
        CircleImageView imgUser;
        RelativeLayout lyEdit,lyDelete;
        public void bind(final PhoneBook item, final onPhoneClick listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        public ViewHolder(View v) {

            super(v);
            imgLogo = v.findViewById(R.id.img_logo);
            imgUser = v.findViewById(R.id.img_user);
            txtPhone =  v.findViewById(R.id.txtPhone);
            txtTell =  v.findViewById(R.id.txtNumber);
            lyEdit = v.findViewById(R.id.lyEdit);
            lyDelete = v.findViewById(R.id.lyDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_phone_new, parent, false));
        return viewHolder1;
    }

    public interface onPhoneClick{
        public void onItemClick(PhoneBook phoneBook);
        public void onItemEdit(PhoneBook phoneBook);
        public void onItemDelete(PhoneBook phoneBook);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PhoneBook data = list.get(position);
        holder.bind(list.get(position), myListener);
        holder.txtPhone.setText(data.getfName()+" "+data.getlName());
        String phone = "";
        if(data.getPhone().startsWith("00")){
            phone = data.getPhone().replaceFirst("00","+");
         }else {
            phone = data.getPhone();
        }
        holder.txtTell.setText(phone);
        holder.lyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onItemEdit(data);
            }
        });
        holder.lyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onItemDelete(data);
            }
        });
        if(data.isInitial()){
            holder.lyEdit.setVisibility(View.INVISIBLE);
            holder.lyDelete.setVisibility(View.INVISIBLE);
            if(position==0){
                holder.imgLogo.setImageResource(R.drawable.ambulance_icon);
            }else if(position==1) {
                holder.imgLogo.setImageResource(R.drawable.medicine_1_icon);
            }else {
                holder.imgLogo.setImageResource(R.drawable.no_image_person);
            }
        }else {
            holder.lyEdit.setVisibility(View.VISIBLE);
            holder.lyDelete.setVisibility(View.VISIBLE);
            if(data.getImg().equals("")) {
                holder.imgLogo.setBackground(context.getResources().getDrawable(R.drawable.no_image_person));
                holder.imgUser.setVisibility(View.INVISIBLE);
            }else {
                holder.imgLogo.setVisibility(View.INVISIBLE);
                holder.imgUser.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgUser.setImageBitmap(decodedByte);
            }
        }
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
