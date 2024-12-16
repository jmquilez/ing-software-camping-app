package es.unizar.eina.T213_camping.utils;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.send.SendAbstraction;
import es.send.SendAbstractionImpl;
import es.send.SendImplementor;
import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.utils.ModelUtils.ParcelaOccupancy;
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
     * @param activity Actividad actual
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
                DialogUtils.showErrorDialog(context, context.getString(R.string.error_invalid_dates));
                return;
            }
            
            String error = DateUtils.validateDates(entryDate, departureDate);
            if (error != null) {
                DialogUtils.showErrorDialog(context, context.getString(R.string.error_departure_after_entry));
                return;
            }
        }
        
        DialogUtils.showChoiceDialog(
            context,
            context.getString(R.string.notify_method_title),
            context.getString(R.string.notify_method_message),
            context.getString(R.string.notify_method_whatsapp),
            context.getString(R.string.notify_method_sms),
            () -> sendMessage(context, activity, "WHATSAPP"),
            () -> sendMessage(context, activity, "SMS")
        );
    }

    /**
     * Envía un mensaje al cliente utilizando el método especificado (SMS o WhatsApp).
     * Construye el mensaje con los detalles de la reserva y lo envía usando el implementador correspondiente.
     * 
     * @param context Contexto de la aplicación
     * @param activity Actividad desde la que se envía el mensaje
     * @param method Método de envío ("SMS" o "WHATSAPP")
     */
    private static void sendMessage(Context context, Activity activity, String method) {
        SendAbstraction sender = new SendAbstractionImpl(activity, method);
        
        String phoneNumber = "";
        String clientName = "";
        List<ParcelaOccupancy> parcelas = new ArrayList<>();

        // To make sure dates aren't null
        Date entryDate = new Date();
        Date departureDate = new Date();
        
        if (activity instanceof ModifyReservationActivity) {
            ModifyReservationActivity act = (ModifyReservationActivity) activity;
            phoneNumber = act.getClientPhone();
            clientName = act.getClientName();
            parcelas = act.getSelectedParcels();
            entryDate = act.getCheckInDate();
            departureDate = act.getCheckOutDate();
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
        messageBuilder.append(String.format(context.getString(R.string.reservation_greeting), clientName))
                     .append("\n\n")
                     .append(context.getString(R.string.reservation_details))
                     .append("\n")
                     .append(String.format(context.getString(R.string.reservation_entry_date), 
                         DateUtils.formatDate(entryDate)))
                     .append("\n")
                     .append(String.format(context.getString(R.string.reservation_departure_date), 
                         DateUtils.formatDate(departureDate)))
                     .append("\n")
                     .append(context.getString(R.string.reservation_parcels))
                     .append(" ");
        
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
            Toast.makeText(context, R.string.notify_sending, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, R.string.notify_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra un diálogo de confirmación para eliminar una reserva.
     * @param context Contexto de la aplicación
     * @param currentActivity Actividad actual
     */
    public static void deleteReservation(Context context, AppCompatActivity currentActivity) {
        DialogUtils.showConfirmationDialog(
            context, 
            context.getString(R.string.delete_reservation_title),
            context.getString(R.string.delete_reservation_message),
            R.drawable.ic_delete_confirm,
            () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, ReservationConstants.OPERATION_DELETE);
                currentActivity.setResult(RESULT_OK, resultIntent);
                currentActivity.finish();
            }
        );
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
                                        List<ParcelaOccupancy> selectedParcels,
                                        boolean isUpdate) {
        double price = PriceUtils.calculateReservationPrice(entryDate, departureDate, selectedParcels);
        
        DialogUtils.showConfirmationDialog(
            activity,
            activity.getString(R.string.confirm_changes_title),
            String.format(Locale.getDefault(), 
                activity.getString(R.string.confirm_reservation_message),
                activity.getString(isUpdate ? R.string.confirm_reservation_update 
                                     : R.string.confirm_reservation_create),
                price),
            R.drawable.ic_confirm_reservation,
            () -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ReservationConstants.RESERVATION_ID, reservationId);
                resultIntent.putExtra(ReservationConstants.CLIENT_NAME, clientName);
                resultIntent.putExtra(ReservationConstants.CLIENT_PHONE, clientPhone);
                resultIntent.putExtra(ReservationConstants.ENTRY_DATE, entryDate.getTime());
                resultIntent.putExtra(ReservationConstants.DEPARTURE_DATE, departureDate.getTime());
                resultIntent.putParcelableArrayListExtra(ReservationConstants.SELECTED_PARCELS, 
                    new ArrayList<>(selectedParcels));
                resultIntent.putExtra(ReservationConstants.OPERATION_TYPE, 
                    isUpdate ? ReservationConstants.OPERATION_UPDATE : ReservationConstants.OPERATION_INSERT);
                resultIntent.putExtra(ReservationConstants.RESERVATION_PRICE, price);
                
                activity.setResult(RESULT_OK, resultIntent);
                activity.finish();
            }
        );
    }
}
