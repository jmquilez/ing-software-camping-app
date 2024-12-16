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

/**
 * Adaptador para mostrar parcelas en un RecyclerView.
 * Gestiona la visualización y actualización de la lista de parcelas.
 */
public class ParcelAdapter extends ListAdapter<Parcela, ParcelAdapter.ViewHolder> {

    private Context context;
    private String sortingCriteria;
    private OnParcelClickListener onParcelClickListener;

    /**
     * Interfaz para manejar los eventos de clic en las parcelas.
     */
    public interface OnParcelClickListener {
        /**
         * Se llama cuando se hace clic en una parcela.
         * @param parcela Parcela seleccionada
         */
        void onParcelClick(Parcela parcela);
    }

    /**
     * Constructor del adaptador.
     * @param context Contexto de la aplicación
     * @param sortingCriteria Criterio inicial de ordenación
     * @param listener Listener para eventos de clic
     */
    public ParcelAdapter(Context context, String sortingCriteria, OnParcelClickListener listener) {
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
                holder.itemParcelAdditionalInfo.setText(
                    context.getString(R.string.parcel_list_id, parcela.getNombre()));
                break;
            case ParcelConstants.SORT_MAX_OCCUPANTS:
                holder.itemParcelAdditionalInfo.setText(
                    context.getString(R.string.parcel_list_max_occupants, parcela.getMaxOcupantes()));
                break;
            case ParcelConstants.SORT_EUR_PERSONA:
                holder.itemParcelAdditionalInfo.setText(
                    context.getString(R.string.parcel_list_price_per_person, parcela.getEurPorPersona()));
                break;
            default:
                holder.itemParcelAdditionalInfo.setText(parcela.getNombre());
                break;
        }

        holder.itemView.setOnClickListener(v -> onParcelClickListener.onParcelClick(parcela));
    }

    /**
     * ViewHolder para mantener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemParcelName;
        TextView itemParcelAdditionalInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemParcelName = itemView.findViewById(R.id.item_parcel_name);
            itemParcelAdditionalInfo = itemView.findViewById(R.id.item_parcel_additional_info);
        }
    }

    /**
     * Clase para calcular las diferencias entre elementos de la lista.
     * Ayuda a optimizar las actualizaciones del RecyclerView.
     */
    public static class ParcelDiff extends DiffUtil.ItemCallback<Parcela> {
        // KEY: See https://jermainedilao.medium.com/demystifying-diffutil-itemcallback-class-8c0201cc69b1
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            Log.d("ARE_ITEMS_THE_SAME", "maxOld: " + oldItem.getMaxOcupantes() + ", maxNew: " + newItem.getMaxOcupantes() + ", areEquals: " + oldItem.equals(newItem));
            return oldItem.getNombre().equals(newItem.getNombre());
        }

        /* NOTE: with a wrong implementation of "areContentsTheSame", the ViewHolder is not properly
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

    /**
     * Actualiza el criterio de ordenación y notifica los cambios.
     * @param newSortingCriteria Nuevo criterio de ordenación
     */
    public void updateSortingCriteria(String newSortingCriteria) {
        this.sortingCriteria = newSortingCriteria;
        notifyItemRangeChanged(0, getItemCount());
    }
}
