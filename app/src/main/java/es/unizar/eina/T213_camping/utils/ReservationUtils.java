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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utilidades para la gestión de reservas.
 * Proporciona métodos para realizar operaciones comunes sobre reservas:
 * - Notificación al cliente
 * - Eliminación de reservas
 * - Confirmación de cambios
 */
public class ReservationUtils {

    /**
     * Muestra un diálogo de confirmación para notificar al cliente.
     * @param context Contexto de la aplicación
     * @param currentActivity Actividad actual
     */
    public static void notifyClient(Context context, Activity activity) {
        // Validate dates first
        Date entryDate = null;
        Date departureDate = null;
        
        if (activity instanceof ModifyReservationActivity) {
            ModifyReservationActivity act = (ModifyReservationActivity) activity;
            entryDate = act.getCheckInDate();
            departureDate = act.getCheckOutDate();
            
            // Validate dates before proceeding
            if (entryDate == null || departureDate == null) {
                DialogUtils.showErrorDialog(context, "Las fechas no son válidas");
                return;
            }
            
            String error = DateUtils.validateDates(entryDate, departureDate);
            if (error != null) {
                DialogUtils.showErrorDialog(context, "La fecha de salida debe ser posterior a la de entrada");
                return;
            }
        }
        
        SendAbstraction sender = new SendAbstractionImpl(activity, "SMS");
        
        String phoneNumber = "";
        String clientName = "";
        List<ParcelaOccupancy> parcelas = new ArrayList<>();
        
        if (activity instanceof ModifyReservationActivity) {
            ModifyReservationActivity act = (ModifyReservationActivity) activity;
            phoneNumber = act.getClientPhone();
            clientName = act.getClientName();
            parcelas = act.getSelectedParcels();
        } else if (activity instanceof ParcelSelectionActivity) {
            ParcelSelectionActivity act = (ParcelSelectionActivity) activity;
            phoneNumber = act.getClientPhone();
            clientName = act.getClientName();
            parcelas = act.getAddedParcels();
            entryDate = act.getCheckInDate();
            departureDate = act.getCheckOutDate();
        }
        
        // Construir el mensaje
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("¡Hola ").append(clientName).append("!\n\n");
        messageBuilder.append("Detalles de su reserva:\n");
        messageBuilder.append("Fecha de entrada: ").append(DateUtils.formatDate(entryDate)).append("\n");
        messageBuilder.append("Fecha de salida: ").append(DateUtils.formatDate(departureDate)).append("\n");
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

    /**
     * Muestra un diálogo de confirmación para eliminar una reserva.
     * @param context Contexto de la aplicación
     * @param currentActivity Actividad actual
     */
    public static void deleteReservation(Context context, AppCompatActivity currentActivity) {
        DialogUtils.showConfirmationDialog(context, "Eliminar reserva", "¿Está seguro de que desea eliminar esta reserva?", R.drawable.ic_delete_confirm, () -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_DELETE);
            currentActivity.setResult(RESULT_OK, resultIntent);
            currentActivity.finish();
        });
    }

    /**
     * Muestra un diálogo de confirmación para guardar los cambios en una reserva.
     * @param activity Actividad actual
     * @param reservationId ID de la reserva
     * @param clientName Nombre del cliente
     * @param clientPhone Teléfono del cliente
     * @param entryDate Fecha de entrada
     * @param departureDate Fecha de salida
     * @param selectedParcels Lista de parcelas seleccionadas
     */
    public static void confirmReservation(BaseActivity activity, long reservationId,
                                          String clientName, String clientPhone,
                                          Date entryDate, Date departureDate,
                                          List<ParcelaOccupancy> selectedParcels) {
        double price = PriceUtils.calculateReservationPrice(entryDate, departureDate, selectedParcels);
        
        DialogUtils.showConfirmationDialog(
            activity,
            "Confirmar cambios",
            String.format(Locale.getDefault(), 
                "¿Está seguro de que desea guardar los cambios en esta reserva?\n\nPrecio total: %.2f€", 
                price),
            R.drawable.ic_confirm_reservation,
            () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ReservationConstants.RESERVATION_ID, reservationId);
                resultIntent.putExtra(ReservationConstants.CLIENT_NAME, clientName);
                resultIntent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhone);
                resultIntent.putExtra(ReservationConstants.ENTRY_DATE, entryDate.getTime());
                resultIntent.putExtra(ReservationConstants.DEPARTURE_DATE, departureDate.getTime());
                
                // NOTE: See https://stackoverflow.com/questions/51805648/unchecked-cast-java-io-serializable-to-java-util-arraylist
                resultIntent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, 
                    new ArrayList<>(selectedParcels));
                resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, 
                    ReservationConstants.OPERATION_UPDATE);
                resultIntent.putExtra(ReservationConstants.RESERVATION_PRICE, price);
                
                activity.setResult(RESULT_OK, resultIntent);
                activity.finish();
            }
        );
    }
}
