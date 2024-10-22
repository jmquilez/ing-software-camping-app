package es.unizar.eina.T213_camping.ui.reservas.adapters;

import android.app.Dialog;
import android.content.Context;
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
import es.unizar.eina.T213_camping.db.models.ParcelaOccupancy;

public class AddedParcelsAdapter extends ListAdapter<ParcelaOccupancy, AddedParcelsAdapter.ViewHolder> {
    private Context context;
    private OnParcelUpdatedListener onParcelUpdatedListener;
    private boolean showRemoveButton = true;

    public interface OnParcelUpdatedListener {
        void onParcelUpdated(List<ParcelaOccupancy> updatedList);
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

    public AddedParcelsAdapter(Context context, OnParcelUpdatedListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onParcelUpdatedListener = listener;
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

    public void setShowRemoveButton(boolean show) {
        showRemoveButton = show;
    }

    // TODO: merge with the other adapter
    private void showParcelDetailsDialog(ParcelaOccupancy addedParcel) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_parcel_details);

        TextView title = dialog.findViewById(R.id.dialog_parcel_title);
        NumberPicker occupantsPicker = dialog.findViewById(R.id.dialog_occupants_picker);
        MaterialButton removeButton = dialog.findViewById(R.id.dialog_remove_parcel_button);
        MaterialButton confirmButton = dialog.findViewById(R.id.dialog_confirm_changes_button);

        title.setText("PARCELA \"" + addedParcel.getParcela().getNombre() + "\"");
        occupantsPicker.setValue(addedParcel.getOccupancy());
        occupantsPicker.setMaxValue(addedParcel.getParcela().getMaxOcupantes());
        occupantsPicker.setMinValue(1);

        // Set visibility of remove button
        removeButton.setVisibility(showRemoveButton ? View.VISIBLE : View.GONE);

        removeButton.setOnClickListener(v -> {
            List<ParcelaOccupancy> currentList = new ArrayList<>(getCurrentList());
            currentList.remove(addedParcel);
            onParcelUpdatedListener.onParcelUpdated(currentList);
            dialog.dismiss();
        });

        confirmButton.setOnClickListener(v -> {
            addedParcel.setOccupancy(occupantsPicker.getValue());
            List<ParcelaOccupancy> currentList = new ArrayList<>(getCurrentList());
            onParcelUpdatedListener.onParcelUpdated(currentList);
            dialog.dismiss();
        });

        dialog.show();
    }

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
