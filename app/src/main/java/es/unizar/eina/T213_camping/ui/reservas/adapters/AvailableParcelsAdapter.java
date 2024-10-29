package es.unizar.eina.T213_camping.ui.reservas.adapters;

import android.app.Dialog;
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

public class AvailableParcelsAdapter extends ListAdapter<Parcela, AvailableParcelsAdapter.ViewHolder> {
    private Context context;
    private List<ParcelaOccupancy> addedParcels;
    private OnParcelAddedListener onParcelAddedListener;

    public interface OnParcelAddedListener {
        void onParcelAdded(List<ParcelaOccupancy> updatedList);
    }

    public AvailableParcelsAdapter(Context context, List<ParcelaOccupancy> addedParcels, OnParcelAddedListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.addedParcels = addedParcels;
        this.onParcelAddedListener = listener;
    }

    private static final DiffUtil.ItemCallback<Parcela> DIFF_CALLBACK = new DiffUtil.ItemCallback<Parcela>() {
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.getNombre().equals(newItem.getNombre());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return areItemsTheSame(oldItem, newItem);
        }
    };

    @Override
    public void submitList(List<Parcela> list) {
        if (list != null && addedParcels != null) {
            List<Parcela> filteredList = list.stream()
                    .filter(parcela -> addedParcels.stream()
                            .noneMatch(added -> added.getParcela().getNombre().equals(parcela.getNombre())))
                    .collect(Collectors.toList());
            super.submitList(filteredList);
        } else {
            super.submitList(null);
        }
    }

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

        confirmButton.setOnClickListener(v -> {
            ParcelaOccupancy newBookedParcel = new ParcelaOccupancy(parcel, occupantsPicker.getValue());
            addedParcels.add(newBookedParcel);
            onParcelAddedListener.onParcelAdded(new ArrayList<>(addedParcels));
            dialog.dismiss();
            submitList(getCurrentList()); // Refresh the list to remove the added parcel
        });

        dialog.show();
    }

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
