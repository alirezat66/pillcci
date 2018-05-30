package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillUsage;

/**
 * Created by alireza on 5/21/18.
 */

public class TodayUsageAdapter extends RecyclerView.Adapter<TodayUsageAdapter.ViewHolder>  {
    ArrayList<PillUsage> list;
    Context context;
    ViewHolder viewHolder1;
    UsageInterface myInterface;


    public TodayUsageAdapter(Context context1, ArrayList<PillUsage> list,UsageInterface usageInterface) {
        this.list = list;
        context = context1;
        myInterface = usageInterface;
    }



    public int getUsedUsageCount(){
        int count = 0;
        for(PillUsage usage : list){
            if(usage.getState()==1){
                count++;
            }
        }
        return count;
    }



    public static interface UsageInterface {
        public void usageAct(PillUsage item);
        public void cancelAct(PillUsage item);
        public void jumpAct(PillUsage item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMedName, txtTime,txtCatName,txtdrName,txtunit,txtrepeatUsage,txtState,txtUse,txtCancel,txtJump;
        public LinearLayout lyEdit,lyCancel,catColor,lyAction;






        public ViewHolder(View v) {

            super(v);
            txtMedName =  v.findViewById(R.id.txtMedName);
            txtTime =  v.findViewById(R.id.txtUseTime);
            txtunit =v.findViewById(R.id.txtUnitUsage);
            txtrepeatUsage = v.findViewById(R.id.txtRepeatCount);
            txtdrName = v.findViewById(R.id.txtDrName);
            txtCatName = v.findViewById(R.id.txtCatName);
            txtState = v.findViewById(R.id.txtState);
            txtUse = v.findViewById(R.id.txtUse);
            txtCancel = v.findViewById(R.id.txtCancel);
            txtJump = v.findViewById(R.id.txtJump);
            lyEdit = v.findViewById(R.id.lyEdit);
            lyCancel = v.findViewById(R.id.lyCancel);
            lyAction = v.findViewById(R.id.actionLayout);
            catColor = v.findViewById(R.id.catColor);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_today_pils, parent, false));
        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PillUsage data = list.get(position);


        holder.txtMedName .setText(data.getPillName());
        holder.txtTime.setText(data.getTime());

        String[]times = data.getTime().split(":");
        int usageType = data.getState();
        int color;
        String state = "";
        if(usageType==0){
            holder.lyAction.setVisibility(View.VISIBLE);
            state="در انتظار مصرف";
            color = context.getResources().getColor(R.color.darkorange);
        }else if(usageType==1){
            holder.lyAction.setVisibility(View.GONE);
            state="مصرف شد.";
            color = context.getResources().getColor(R.color.teal);
        }else {
            holder.lyAction.setVisibility(View.GONE);

            state="لغو مصرف توسط کاربر";
            color = context.getResources().getColor(R.color.colorAccent);
        }
        holder.catColor.setBackgroundColor(data.getCatColor());
        if(times.length>2){
            holder.txtTime.setText(times[0]+":"+times[1]);
        }
        holder.txtState.setText(" | "+state);
        holder.txtState.setTextColor(color);

        holder.txtCatName.setText(data.getCatNme());
        holder.txtdrName.setText(data.getDrName());
        holder.txtunit.setText(data.getUnitAmount()+" "+data.getUnit());

        holder.txtrepeatUsage.setText(data.getCountPerDay());

        holder.txtUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.usageAct(data);
            }
        });
        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.cancelAct(data);
            }
        });
        holder.txtJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.jumpAct(data);
            }
        });


    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
