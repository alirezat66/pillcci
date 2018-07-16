package greencode.ir.pillcci.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.objects.PillShelf;

/**
 * Created by alireza on 5/21/18.
 */

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder>  {
    ArrayList<PillShelf> list;
    ArrayList<PillShelf> fullPill;
    Activity context;
    ViewHolder viewHolder1;
    PillListInterface myInterface;




    public PillAdapter(Activity context1, ArrayList<PillShelf> list,PillListInterface myInterface) {
        this.list = list;
        this.fullPill=list;
        context = context1;
        this.myInterface = myInterface;
    }


    public void filter(ArrayList<PillShelf>pillShelves){
        list=pillShelves;
        notifyDataSetChanged();
    }
    public ArrayList<PillShelf>getFullPill(){
        return fullPill;
    }

    public void update(ArrayList<PillShelf> pillShelves) {
        list = pillShelves;
        notifyDataSetChanged();
    }

    public interface PillListInterface{
        public void onDelete(PillShelf item);
        public void onDetail(PillShelf item, ImageView imgLogo,TextView txtMedName);
        public void onStop(PillShelf item);
        public void start(PillShelf data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMedName,txtUseTime,txtStop;
        public CardView catColor,mainLay;
        CircleImageView imgLogo;
        LinearLayout lyDelete,lyStop;


        public ViewHolder(View v) {

            super(v);
            imgLogo = v.findViewById(R.id.imgLogo);
            txtMedName =  v.findViewById(R.id.txtMedName);
            txtUseTime =  v.findViewById(R.id.txtUseTime);
            catColor = v.findViewById(R.id.catColor);
            mainLay = v.findViewById(R.id.mainLay);
            lyDelete = v.findViewById(R.id.lyDelete);
            lyStop = v.findViewById(R.id.lyStop);
            txtStop = v.findViewById(R.id.txtStop);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_pil, parent, false));

        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final PillShelf data = list.get(position);




        holder.txtMedName.setText(data.getName() );
        holder.txtUseTime.setText("دسته : "+data.getCatName());
        holder.txtUseTime.setTextColor(data.getCatColer());
        holder.catColor.setCardBackgroundColor(data.getCatColer());
        if (!data.getImg().equals("")) {
            byte[] decodedString = Base64.decode(data.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgLogo.setImageBitmap(decodedByte);
        }else {
            holder.imgLogo.setImageResource(R.drawable.pill_avator);
        }

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onDetail(data,holder.imgLogo,holder.txtMedName);
            }
        });
        if(data.getState()==1){
            holder.txtStop.setText("توقف مصرف");

        }else {
            holder.txtStop.setText("شروع مصرف");
            if(data.getUsageType()==4){
                holder.txtStop.setTextColor(context.getResources().getColor(R.color.grayText));
            }else {
                holder.txtStop.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

            }
        }

        holder.lyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onDelete(data);
            }
        });
        holder.lyStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getState()==1) {
                    myInterface.onStop(data);
                }else {
                    myInterface.start(data);
                }
            }
        });

    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
