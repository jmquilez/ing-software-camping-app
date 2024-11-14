package es.unizar.eina.T213_camping.ui.reservas.adapters;

import android.app.Dialog;
import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;

/**
 * Adaptador para mostrar las parcelas disponibles para reservar.
 * Permite al usuario seleccionar parcelas y especificar su ocupación.
 */
public class AvailableParcelsAdapter extends ListAdapter<Parcela, AvailableParcelsAdapter.ViewHolder> {
    private Context context;
    private List<ParcelaOccupancy> addedParcels;
    private OnParcelSelectionChangedListener onParcelAddedListener;

    /**
     * Interfaz para notificar cambios en la selección de parcelas.
     */
    public interface OnParcelSelectionChangedListener {
        /**
         * Se llama cuando se añade o modifica una parcela seleccionada.
         * @param updatedAddedParcelsList Lista actualizada de parcelas añadidas
         * @param updatedAvailableParcelsList Lista actualizada de parcelas disponibles
         * @param whoCalled Identificador del componente que realizó el cambio
         */
        void onParcelSelectionChanged(List<ParcelaOccupancy> updatedAddedParcelsList,
                           List<Parcela> updatedAvailableParcelsList, String whoCalled);
    }

    /**
     * Constructor del adaptador.
     * @param context Contexto de la aplicación
     * @param addedParcels Lista inicial de parcelas ya añadidas
     * @param listener Listener para eventos de selección
     */
    public AvailableParcelsAdapter(Context context, List<ParcelaOccupancy> addedParcels, OnParcelSelectionChangedListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        // KEY: create a new ArrayList ref, since the underlying `addedParcels` is unmodifiable
        this.addedParcels = addedParcels == null ? new ArrayList<>() : new ArrayList<>(addedParcels);
        this.onParcelAddedListener = listener;
    }

    /**
     * Callback para calcular las diferencias entre elementos de la lista.
     * Ayuda a optimizar las actualizaciones del RecyclerView determinando
     * qué elementos han cambiado y cómo.
     */
    private static final DiffUtil.ItemCallback<Parcela> DIFF_CALLBACK = new DiffUtil.ItemCallback<Parcela>() {
        /**
         * Determina si dos elementos representan el mismo objeto.
         * En este caso, compara los nombres de las parcelas.
         * @param oldItem Parcela antigua
         * @param newItem Parcela nueva
         * @return true si representan la misma parcela
         */
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.getNombre().equals(newItem.getNombre());
        }

        /**
         * Determina si el contenido de dos elementos es el mismo.
         * Compara todos los campos relevantes de las parcelas.
         * @param oldItem Parcela antigua
         * @param newItem Parcela nueva
         * @return true si el contenido es idéntico
         */
        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_parcel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parcela parcel = getItem(position);
        holder.parcelName.setText(parcel.getNombre());
        holder.addButton.setOnClickListener(v -> showParcelDetailsDialog(parcel));
    }

    /**
     * Actualiza la lista de parcelas añadidas.
     * @param addedParcels Nueva lista de parcelas añadidas
     */
    public void updateAddedParcels(List<ParcelaOccupancy> addedParcels) {
        this.addedParcels = new ArrayList<>(addedParcels);
    }

    /*@Override
    public void submitList(List<Parcela> list) {
        if (list != null && addedParcels != null) {
            List<Parcela> filteredList = list.stream()
                    .filter(parcela -> addedParcels.stream()
                            .noneMatch(added -> added.getParcela().getNombre().equals(parcela.getNombre())))
                    .collect(Collectors.toList());
            super.submitList(filteredList);
        } else {
            super.submitList(list);
        }
    }*/

    /**
     * Muestra un diálogo para seleccionar el número de ocupantes de una parcela.
     * @param parcel Parcela seleccionada
     */
    private void showParcelDetailsDialog(Parcela parcel) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_parcel_details);

        TextView title = dialog.findViewById(R.id.dialog_parcel_title);
        NumberPicker occupantsPicker = dialog.findViewById(R.id.dialog_occupants_picker);
        MaterialButton removeButton = dialog.findViewById(R.id.dialog_remove_parcel_button);
        MaterialButton confirmButton = dialog.findViewById(R.id.dialog_confirm_changes_button);

        title.setText("PARCELA \"" + parcel.getNombre() + "\"");
        occupantsPicker.setMinValue(1);
        occupantsPicker.setMaxValue(parcel.getMaxOcupantes());
        occupantsPicker.setValue(1); // Default value for the number picker

        removeButton.setVisibility(View.GONE); // Hide remove button for available parcels

        // KEY: create a new list ref (getCurrentList() is unmodifiable)
        List<Parcela> currentList = new ArrayList<>(getCurrentList());

        Log.i("CURRENTLY_AVAILABLE_PARCELS_LIST", currentList.toString());

        confirmButton.setOnClickListener(v -> {
            currentList.remove(parcel);
            ParcelaOccupancy newBookedParcel = new ParcelaOccupancy(parcel, occupantsPicker.getValue());
            addedParcels.add(0, newBookedParcel); // NOTE: add first so the user can see the changes directly
            onParcelAddedListener.onParcelSelectionChanged(addedParcels, currentList, ReservationConstants.AVAILABLE_PARCELS_ADAPTER_CALLED);
            dialog.dismiss();
            submitList(currentList); // Refresh the list to remove the added parcel
        });

        dialog.show();
    }

    /**
     * ViewHolder para mantener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView parcelName;
        MaterialButton addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parcelName = itemView.findViewById(R.id.item_available_parcel_name);
            addButton = itemView.findViewById(R.id.item_available_parcel_add_button);
        }
    }
}
