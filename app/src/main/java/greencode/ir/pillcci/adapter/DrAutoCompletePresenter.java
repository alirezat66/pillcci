package greencode.ir.pillcci.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;

/**
 * Created by alireza on 5/27/18.
 */

public class DrAutoCompletePresenter extends RecyclerViewPresenter<String> {

    protected Adapter adapter;
    public ArrayList<String> drNames = new ArrayList<>();
    public DrAutoCompletePresenter(Context context) {
        super(context);
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        drNames = new ArrayList<>(database.pillObjectDao().getAllDrNames());





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
        List<String> all = drNames;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<String> list = new ArrayList<>();
            for (String u : all) {
                if (u.toLowerCase().contains(query) ||
                        u.toLowerCase().contains(query)) {
                    list.add(u);

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

            }else {
                onViewShown();
                holder.root.setVisibility(View.VISIBLE);
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



}
