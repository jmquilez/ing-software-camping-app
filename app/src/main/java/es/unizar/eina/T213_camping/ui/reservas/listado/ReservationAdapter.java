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

/**
 * Adaptador para mostrar reservas en un RecyclerView.
 * Gestiona la visualización y actualización de la lista de reservas,
 * permitiendo diferentes criterios de ordenación.
 */
public class ReservationAdapter extends ListAdapter<Reserva, ReservationAdapter.ViewHolder> {

    private Context context;
    private String sortingCriteria;
    private OnReservationClickListener onReservationClickListener;

    /**
     * Interfaz para manejar los eventos de clic en las reservas.
     */
    public interface OnReservationClickListener {
        /**
         * Se llama cuando se hace clic en una reserva.
         * @param reserva Reserva seleccionada
         */
        void onReservationClick(Reserva reserva);
    }

    /**
     * Constructor del adaptador.
     * @param context Contexto de la aplicación
     * @param sortingCriteria Criterio inicial de ordenación
     * @param listener Listener para eventos de clic
     */
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

    /**
     * ViewHolder para mantener las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemReservationId;
        TextView itemReservationInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemReservationId = itemView.findViewById(R.id.item_reservation_id);
            itemReservationInfo = itemView.findViewById(R.id.item_reservation_info);
        }
    }

    /**
     * Clase para calcular las diferencias entre elementos de la lista.
     * Ayuda a optimizar las actualizaciones del RecyclerView.
     */
    public static class ReservationDiff extends DiffUtil.ItemCallback<Reserva> {
        /**
         * Determina si dos elementos representan la misma reserva.
         * @param oldItem Reserva antigua
         * @param newItem Reserva nueva
         * @return true si tienen el mismo ID
         */
        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Determina si el contenido de dos elementos es el mismo.
         * @param oldItem Reserva antigua
         * @param newItem Reserva nueva
         * @return true si todos los campos son iguales
         */
        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
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
