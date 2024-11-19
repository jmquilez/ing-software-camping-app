package es.unizar.eina.T213_camping.utils;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.send.SendAbstraction;
import es.send.SendAbstractionImpl;
import es.send.SendImplementor;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.ParcelaOccupancy;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.ui.BaseActivity;
import es.unizar.eina.T213_camping.ui.reservas.ReservationConstants;
import es.unizar.eina.T213_camping.ui.reservas.gestion.ModifyReservationActivity;
import es.unizar.eina.T213_camping.ui.reservas.gestion.ParcelSelectionActivity;

import java.util.ArrayList;
import java.util.List;

public class ReservationUtils {

    // TODO: revise R.drawable
    public static void notifyClient(Context context, Activity activity) {
        SendAbstraction sender = new SendAbstractionImpl(activity, "SMS");
        
        String phoneNumber = "";
        String clientName = "";
        String entryDate = "";
        String departureDate = "";
        List<ParcelaOccupancy> parcelas = new ArrayList<>();
        
        if (activity instanceof ModifyReservationActivity) {
            ModifyReservationActivity act = (ModifyReservationActivity) activity;
            phoneNumber = act.getClientPhone();
            clientName = act.getClientName();
            entryDate = act.getCheckInDate();
            departureDate = act.getCheckOutDate();
            parcelas = act.getSelectedParcels();
        } else if (activity instanceof ParcelSelectionActivity) {
            ParcelSelectionActivity act = (ParcelSelectionActivity) activity;
            phoneNumber = act.getClientPhone();
            clientName = act.getClientName();
            entryDate = act.getCheckInDate();
            departureDate = act.getCheckOutDate();
            parcelas = act.getAddedParcels();
        }
        
        // Construir el mensaje
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("¡Hola ").append(clientName).append("!\n\n");
        messageBuilder.append("Detalles de su reserva:\n");
        messageBuilder.append("Fecha de entrada: ").append(entryDate).append("\n");
        messageBuilder.append("Fecha de salida: ").append(departureDate).append("\n");
        messageBuilder.append("Parcelas reservadas: ");
        
        // Añadir información de las parcelas
        if (parcelas != null && !parcelas.isEmpty()) {
            for (int i = 0; i < parcelas.size(); i++) {
                messageBuilder.append(parcelas.get(i).getParcela().getNombre());
                if (i < parcelas.size() - 1) {
                    messageBuilder.append(", ");
                }
            }
        }
        
        String message = messageBuilder.toString();
        
        try {
            sender.send(phoneNumber, message);
            Toast.makeText(context, "Mensaje enviado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
        }
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
