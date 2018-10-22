package greencode.ir.pillcci.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.Constants;

/**
 * Created by alireza on 5/27/18.
 */

public class PillAutoCompletePresenter extends RecyclerViewPresenter<String> {

    protected Adapter adapter;
    public ArrayList<String> pils = new ArrayList<>();
    public ArrayList<String> tempPils = new ArrayList<>();

    public PillAutoCompletePresenter(Context context) {
        super(context);

        String[] pills = Constants.allPills.split(",");
        String[] pills2 = Constants.allPills2.split("،");

        for(String p :pills2){
            pils.add(p);
        }

        for(String p :pills){
            pils.add(p);
        }




    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<String> all = pils;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<String> list = new ArrayList<>();
            for (String u : all) {
                if (u.toLowerCase().startsWith((String) query)) {
                    list.add(u);
                }
            }
            for (String u : all) {
                if (u.toLowerCase().contains((String) query)) {
                    if(!list.contains(u)) {
                        list.add(u);
                    }
                }
            }
            adapter.setData(list);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<String> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;

            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = ((TextView) itemView.findViewById(R.id.fullname));
            }
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 0 : data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_pill_complite, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {

                return;
            }
            final String pill = data.get(position);
            holder.fullname.setText(pill);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(pill);
                }
            });
        }
    }
}
