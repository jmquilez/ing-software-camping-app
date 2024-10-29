package es.unizar.eina.T213_camping.ui.reservas.listado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Reserva;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;

public class ReservationAdapter extends ListAdapter<Reserva, ReservationAdapter.ViewHolder> {

    private Context context;
    private String sortingCriteria;
    private OnReservationClickListener onReservationClickListener;

    public interface OnReservationClickListener {
        void onReservationClick(Reserva reserva);
    }

    public ReservationAdapter(Context context, String sortingCriteria, OnReservationClickListener listener) {
        super(new ReservationDiff());
        this.context = context;
        this.sortingCriteria = sortingCriteria;
        this.onReservationClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva reservation = getItem(position);
        holder.itemReservationId.setText(String.valueOf(reservation.getId()));

        switch (sortingCriteria) {
            case ReservationConstants.SORT_CLIENT_NAME:
                holder.itemReservationInfo.setText(reservation.getNombreCliente());
                break;
            case ReservationConstants.SORT_CLIENT_PHONE:
                holder.itemReservationInfo.setText(reservation.getTelefonoCliente());
                break;
            case ReservationConstants.SORT_ENTRY_DATE:
                holder.itemReservationInfo.setText("Entry: " + reservation.getFechaEntrada());
                break;
            default:
                holder.itemReservationInfo.setText(reservation.getNombreCliente());
                break;
        }

        holder.itemView.setOnClickListener(v -> onReservationClickListener.onReservationClick(reservation));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemReservationId;
        TextView itemReservationInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemReservationId = itemView.findViewById(R.id.item_reservation_id);
            itemReservationInfo = itemView.findViewById(R.id.item_reservation_info);
        }
    }

    public static class ReservationDiff extends DiffUtil.ItemCallback<Reserva> {
        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return areItemsTheSame(oldItem, newItem);
        }
    }
}
