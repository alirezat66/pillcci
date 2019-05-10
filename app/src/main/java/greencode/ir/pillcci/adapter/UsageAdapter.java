package greencode.ir.pillcci.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillUsage;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

/**
 * Created by alireza on 5/21/18.
 */

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder>  {
    ArrayList<PillUsage> list;
    Context context;
    ViewHolder viewHolder1;



    public UsageAdapter(Context context1, ArrayList<PillUsage> list) {
        this.list = list;
        context = context1;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTime, txtUnit,txtDate,txtName;






        public ViewHolder(View v) {

            super(v);
            txtTime =  v.findViewById(R.id.txtNextTime);
            txtName =  v.findViewById(R.id.txtMedName);
            txtUnit =v.findViewById(R.id.txtNextDoze);
            txtDate = v.findViewById(R.id.txtNextDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_medician, parent, false));

        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PillUsage data = list.get(position);


        holder.txtTime.setText(data.getTime());

        String[]times = data.getTime().split(":");
        if(times.length>2){
            holder.txtTime.setText(times[0]+":"+times[1]);
        }

        holder.txtUnit.setText(data.getUnitAmount()+" " +data.getUnit());
        holder.txtName.setText(data.getPillName());
        PersianDate calendar = new PersianDate(data.getUsageTime());
        PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
        holder.txtDate.setText(pdformater1.format(calendar));


       /* if(data.getState().equals("online")){
            holder.img.setBorderColor(Color.parseColor("#14af14"));
            holder.img.setBorderWidth(4);


        }else {
            holder.img.setBorderColor(Color.parseColor("#ff0000"));
            holder.img.setBorderWidth(2);
            holder.img.setBorderWidth(0);
        }*/


    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
