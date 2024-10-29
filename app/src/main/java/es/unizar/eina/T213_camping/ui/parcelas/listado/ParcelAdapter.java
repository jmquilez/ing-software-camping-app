package es.unizar.eina.T213_camping.ui.parcelas.listado;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.parcelas.ParcelConstants;

public class ParcelAdapter extends ListAdapter<Parcela, ParcelAdapter.ViewHolder> {

    private Context context;
    private String sortingCriteria;
    private OnParcelClickListener onParcelClickListener;

    public interface OnParcelClickListener {
        void onParcelClick(Parcela parcela);
    }

    public ParcelAdapter(Context context, String sortingCriteria, OnParcelClickListener listener) {
        // TODO: pass parcelDiff as parameter
        super(new ParcelDiff());
        this.context = context;
        this.sortingCriteria = sortingCriteria;
        this.onParcelClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("CREATING VIEW_HOLDER", "ok");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parcela parcela = getItem(position);
        holder.itemParcelName.setText(parcela.getNombre());

        Log.w("PARCEL_ADAPTER", "onBindViewHolder, maxOcupantes parcela: " + parcela.getMaxOcupantes());

        switch (sortingCriteria) {
            case ParcelConstants.SORT_ID:
                holder.itemParcelAdditionalInfo.setText("ID: " + parcela.getNombre());
                break;
            case ParcelConstants.SORT_MAX_OCCUPANTS:
                holder.itemParcelAdditionalInfo.setText("Max Occupants: " + parcela.getMaxOcupantes());
                break;
            case ParcelConstants.SORT_EUR_PERSONA:
                holder.itemParcelAdditionalInfo.setText("Price per Person: €" + String.format("%.2f", parcela.getEurPorPersona()));
                break;
            default:
                holder.itemParcelAdditionalInfo.setText(parcela.getNombre());
                break;
        }

        holder.itemView.setOnClickListener(v -> onParcelClickListener.onParcelClick(parcela));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemParcelName;
        TextView itemParcelAdditionalInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemParcelName = itemView.findViewById(R.id.item_parcel_name);
            itemParcelAdditionalInfo = itemView.findViewById(R.id.item_parcel_additional_info);
        }
    }

    public static class ParcelDiff extends DiffUtil.ItemCallback<Parcela> {
        // TODO: compare by reference? => NO
        // KEY: See https://jermainedilao.medium.com/demystifying-diffutil-itemcallback-class-8c0201cc69b1
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            Log.d("ARE_ITEMS_THE_SAME", "maxOld: " + oldItem.getMaxOcupantes() + ", maxNew: " + newItem.getMaxOcupantes() + ", areEquals: " + oldItem.equals(newItem));
            return oldItem.getNombre().equals(newItem.getNombre());
        }

        /* TODO, CHECK: with a wrong implementation of "areContentsTheSame", the ViewHolder is not properly
         *   updated but in the next iteration (next time an update is made by the user) it seems like the
         *   system has indeed recorded the changes, why? Looks like it caches the "newItem" as the "oldItem"
         *   for the next time that "areContentsTheSame" is called, no matter whether the last call to
         *   "areContentsTheSame" returned true or false (no matter whether the ViewHolder was re-rendered or not).
         *   Make diagram. */
        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            Log.d("ARE_CONTENTS_THE_SAME", "maxOld: " + oldItem.getMaxOcupantes() + ", maxNew: " + newItem.getMaxOcupantes() + ", areEquals: " + oldItem.equals(newItem));
            return oldItem.equals(newItem);
        }
    }

    public void updateSortingCriteria(String newSortingCriteria) {
        // TODO: when modifying a single Parcela, just call "notifyItemChanged(i)"
        this.sortingCriteria = newSortingCriteria;
        notifyItemRangeChanged(0, getItemCount());
    }
}
