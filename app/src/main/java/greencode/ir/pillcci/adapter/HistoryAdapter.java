package greencode.ir.pillcci.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillUsage;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/21/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>  {
    List<PillUsage> list;
    List<PillUsage>fullList;
    Context context;
    ViewHolder viewHolder1;
    UsageInterface myInterface;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public static interface UsageInterface {

        public void EditAct(PillUsage item);

    }

    public List<PillUsage> getFullPill(){
        return fullList;
    }
    public void filter(List<PillUsage>filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
    public HistoryAdapter(Context context1, List<PillUsage> list,UsageInterface myInterface) {
        this.list = list;
        this.fullList=list;
        context = context1;
        this.myInterface = myInterface;
    }

    public void update(ArrayList<PillUsage> temp) {
        this.list = temp;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMedName, txtUnitCount,txtTime,txtCatName,txtdrName,txtunit,txtrepeatUsage,txtState,txtUse,txtCancel,txtJump;
        public CardView catColor;
        LinearLayout lyEdit;
        public ExpansionLayout expansionLayout;

        public ViewHolder(View v) {
            super(v);
            expansionLayout = v.findViewById(R.id.expansionLayout);

            txtMedName =  v.findViewById(R.id.txtMedName);
            txtUnitCount =  v.findViewById(R.id.unitCount);
            txtTime =  v.findViewById(R.id.txtUseTime);
            txtunit =v.findViewById(R.id.txtUnitUsage);
            txtrepeatUsage = v.findViewById(R.id.txtRepeatCount);
            txtdrName = v.findViewById(R.id.txtDrName);
            txtCatName = v.findViewById(R.id.txtCatName);
            txtState = v.findViewById(R.id.txtState);
            txtUse = v.findViewById(R.id.txtUse);
            txtCancel = v.findViewById(R.id.txtCancel);
            txtJump = v.findViewById(R.id.txtJump);
            catColor = v.findViewById(R.id.catColor);
            lyEdit = v.findViewById(R.id.lyEdit);
        }
        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        viewHolder1 = new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_pil_history, parent, false));

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PillUsage data = list.get(position);
        expansionsCollection.add(holder.getExpansionLayout());
        holder.expansionLayout.collapse(true);

        holder.txtMedName .setText(data.getPillName());


        String[]times = data.getTime().split(":");
        int usageType = data.getState();
        int color;
        String state = "";
        if(usageType==0){
            PersianDate pdate = new PersianDate(data.getUsageTime());
            String usedTime = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() ;
            holder.txtTime.setText(usedTime);
            state= "در انتظار مصرف" + " (" +   (pdate.getHour()>9?pdate.getHour():("0"+pdate.getHour())) + ":" + (pdate.getMinute()>9?pdate.getMinute():("0"+pdate.getMinute())) + ")";
            color = context.getResources().getColor(R.color.darkorange);
        }else if(usageType==1){
            state="مصرف شد";
            PersianDate pdate = new PersianDate(data.getUsedTime());

            String usedTime = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay();
            state = state  + " (" +   (pdate.getHour()>9?pdate.getHour():("0"+pdate.getHour())) + ":" + (pdate.getMinute()>9?pdate.getMinute():("0"+pdate.getMinute())) + ")";
            holder.txtTime.setText(usedTime);
            color = context.getResources().getColor(R.color.teal);
        }else if(usageType==2) {
            PersianDate pdate = new PersianDate(data.getUsageTime());
            String usedTime = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay();
            holder.txtTime.setText(usedTime);

            state="مصرف نشد";
            state = state  + " (" +   (pdate.getHour()>9?pdate.getHour():("0"+pdate.getHour())) + ":" + (pdate.getMinute()>9?pdate.getMinute():("0"+pdate.getMinute())) + ")";

            color = context.getResources().getColor(R.color.colorAccent);
        }else {
            PersianDate pdate = new PersianDate(data.getUsageTime());
            String usedTime = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() ;
            holder.txtTime.setText(usedTime);
            state="نامشخص";
            state = state  + " (" +   (pdate.getHour()>9?pdate.getHour():("0"+pdate.getHour())) + ":" + (pdate.getMinute()>9?pdate.getMinute():("0"+pdate.getMinute())) + ")";

            color = context.getResources().getColor(R.color.colorAccent);
        }
        holder.catColor.setCardBackgroundColor(data.getCatColor());
        if(times.length>2){
            holder.txtTime.setText(times[0]+":"+times[1]);
        }
        holder.txtState.setText(" | "+state);
        holder.txtState.setTextColor(color);

        holder.txtCatName.setText(data.getCatNme());
        holder.txtdrName.setText(data.getDrName());
        try {
            double count = Double.parseDouble(data.getUnitAmount());
            if(count-(int)count==0) {
                holder.txtunit.setText((int)count + " " + data.getUnit());
            }else {
                holder.txtunit.setText(data.getUnitAmount()+" "+data.getUnit());
            }

        }catch (NumberFormatException ex){
            holder.txtunit.setText(data.getUnitAmount()+" "+data.getUnit());

            ex.printStackTrace();
        }
        holder.txtrepeatUsage.setText(data.getCountPerDay());

        holder.lyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.EditAct(data);
            }
        });

    }


    @Override
    public int getItemCount() {

        return list.size();
    }

}
