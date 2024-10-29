package es.unizar.eina.T213_camping.utils;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import java.util.ArrayList;
import java.util.List;

public class ReservationUtils {

    // TODO: revise R.drawable
    public static void notifyClient(Context context, AppCompatActivity currentActivity) {
        DialogUtils.showConfirmationDialog(context, "Notificar al cliente", "¿Está seguro de que desea notificar al cliente?", R.drawable.ic_notify, () -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_NOTIFY_CLIENT);
            currentActivity.setResult(RESULT_OK, resultIntent);
            currentActivity.finish();
        });
    }

    public static void deleteReservation(Context context, AppCompatActivity currentActivity) {
        DialogUtils.showConfirmationDialog(context, "Eliminar reserva", "¿Está seguro de que desea eliminar esta reserva?", R.drawable.ic_delete_confirm, () -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_DELETE);
            currentActivity.setResult(RESULT_OK, resultIntent);
            currentActivity.finish();
        });
    }

    public static void confirmReservation(BaseActivity activity, long reservationId, String clientName,
                                          String clientPhone, String entryDate, String departureDate,
                                          @NonNull List<ParcelaOccupancy> selectedParcels) {
        DialogUtils.showConfirmationDialog(
            activity,
            "Confirmar cambios",
            "¿Está seguro de que desea guardar los cambios en esta reserva?",
            R.drawable.ic_confirm_reservation,
            () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ReservationConstants.RESERVATION_ID, reservationId);
                resultIntent.putExtra(ReservationConstants.CLIENT_NAME, clientName);
                resultIntent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhone);
                resultIntent.putExtra(ReservationConstants.ENTRY_DATE, entryDate);
                resultIntent.putExtra(ReservationConstants.DEPARTURE_DATE, departureDate);
                // resultIntent.putExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(selectedParcels));
                // NOTE: See https://stackoverflow.com/questions/51805648/unchecked-cast-java-io-serializable-to-java-util-arraylist
                resultIntent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, new ArrayList<>(selectedParcels));
                resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_UPDATE);
                
                activity.setResult(RESULT_OK, resultIntent);
                activity.finish();
            }
        );
    }
}
