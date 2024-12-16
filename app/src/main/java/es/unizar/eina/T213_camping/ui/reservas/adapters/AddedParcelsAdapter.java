package es.unizar.eina.T213_camping.ui.reservas.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.utils.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;

/**
 * Adaptador para mostrar las parcelas ya añadidas a una reserva.
 * Permite gestionar la ocupación de cada parcela y eliminarlas de la reserva.
 */
public class AddedParcelsAdapter extends ListAdapter<ParcelaOccupancy, AddedParcelsAdapter.ViewHolder> {
    private final Context context;
    private final OnParcelSelectionChangedListener onParcelUpdatedListener;

    private List<Parcela> availableParcels;

    /**
     * Interfaz para notificar cambios en la selección de parcelas.
     */
    public interface OnParcelSelectionChangedListener {
        /**
         * Se llama cuando cambia la selección de parcelas.
         * @param updatedAddedList Lista actualizada de parcelas añadidas
         * @param updatedAvailableList Lista actualizada de parcelas disponibles
         * @param whoCalled Identificador del componente que realizó el cambio
         */
        void onParcelSelectionChanged(List<ParcelaOccupancy> updatedAddedList,
                             List<Parcela> updatedAvailableList, String whoCalled);
    }

    private static final DiffUtil.ItemCallback<ParcelaOccupancy> DIFF_CALLBACK = new DiffUtil.ItemCallback<ParcelaOccupancy>() {
        @Override
        public boolean areItemsTheSame(@NonNull ParcelaOccupancy oldItem, @NonNull ParcelaOccupancy newItem) {
            return oldItem.getParcela().getNombre().equals(newItem.getParcela().getNombre());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ParcelaOccupancy oldItem, @NonNull ParcelaOccupancy newItem) {
            return oldItem.equals(newItem);
        }
    };

    /**
     * Constructor del adaptador.
     * @param context Contexto de la aplicación
     * @param availableParcels Lista inicial de parcelas disponibles
     * @param listener Listener para eventos de cambio en la selección
     */
    public AddedParcelsAdapter(Context context, List<Parcela> availableParcels,
                               OnParcelSelectionChangedListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onParcelUpdatedListener = listener;
        // KEY: create a new ArrayList ref, since the underlying `availableParcels` is unmodifiable
        this.availableParcels = availableParcels == null ? new ArrayList<>() : new ArrayList<>(availableParcels);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_parcel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParcelaOccupancy addedParcel = getItem(position);
        holder.parcelName.setText(addedParcel.getParcela().getNombre());
        holder.editButton.setOnClickListener(v -> showParcelDetailsDialog(addedParcel));
    }

    /**
     * Muestra un diálogo para editar los detalles de una parcela añadida.
     * @param addedParcel Parcela añadida a editar
     */
    private void showParcelDetailsDialog(ParcelaOccupancy addedParcel) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_parcel_details);

        TextView title = dialog.findViewById(R.id.dialog_parcel_title);
        NumberPicker occupantsPicker = dialog.findViewById(R.id.dialog_occupants_picker);
        MaterialButton removeButton = dialog.findViewById(R.id.dialog_remove_parcel_button);
        MaterialButton confirmButton = dialog.findViewById(R.id.dialog_confirm_changes_button);

        title.setText(context.getString(R.string.dialog_parcel_title, addedParcel.getParcela().getNombre()));
        occupantsPicker.setValue(addedParcel.getOccupancy());
        occupantsPicker.setMaxValue(addedParcel.getParcela().getMaxOcupantes());
        occupantsPicker.setMinValue(1);

        List<ParcelaOccupancy> currentList = new ArrayList<>(getCurrentList());

        removeButton.setOnClickListener(v -> {
            // Available parcels array IS modified
            availableParcels.add(0, addedParcel.getParcela()); // NOTE: add first so the user can see the changes directly
            currentList.remove(addedParcel);
            onParcelUpdatedListener.onParcelSelectionChanged(currentList, availableParcels, ReservationConstants.ADDED_PARCELS_ADAPTER_CALLED);
            dialog.dismiss();
            submitList(currentList);
        });

        confirmButton.setOnClickListener(v -> {
            addedParcel.setOccupancy(occupantsPicker.getValue());
            onParcelUpdatedListener.onParcelSelectionChanged(currentList, availableParcels, ReservationConstants.ADDED_PARCELS_ADAPTER_CALLED);
            dialog.dismiss();
            submitList(currentList);
        });

        dialog.show();
    }

    /**
     * Actualiza la lista de parcelas disponibles.
     * @param updatedAvailableParcels Nueva lista de parcelas disponibles
     */
    public void updateAvailableParcels(List<Parcela> updatedAvailableParcels) {
        this.availableParcels = new ArrayList<>(updatedAvailableParcels);
    }

    /**
     * ViewHolder para mantener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView parcelName;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parcelName = itemView.findViewById(R.id.item_added_parcel_name);
            editButton = itemView.findViewById(R.id.item_added_parcel_edit_button);
        }
    }
}
