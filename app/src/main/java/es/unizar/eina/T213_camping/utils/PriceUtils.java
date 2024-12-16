package es.unizar.eina.T213_camping.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.unizar.eina.T213_camping.R;
import es.unizar.eina.T213_camping.database.models.Parcela;
import es.unizar.eina.T213_camping.utils.ModelUtils.ParcelaOccupancy;
import android.widget.TextView;
import java.util.Locale;

/**
 * Utilidades para el cálculo y gestión de precios de reservas.
 * Proporciona métodos para:
 * - Calcular precios totales de reservas
 * - Actualizar displays de precios
 * - Gestionar formato de precios
 */
public class PriceUtils {
    
    /**
     * Calcula el precio total de una reserva.
     * El precio se calcula como: (precio por persona) * (número de ocupantes) *
     * (número de días)
     * para cada parcela reservada.
     * 
     * @param checkInDate  Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @param parcelas     Lista de parcelas con su ocupación
     * @return Precio total de la reserva
     */
    public static double calculateReservationPrice(Date checkInDate, Date checkOutDate,
            List<ParcelaOccupancy> parcelas) {
        if (checkInDate == null || checkOutDate == null || parcelas == null || parcelas.isEmpty()) {
            return 0.0;
        }

        // Calculate number of days
        long diffInMillies = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // Add 1 because if you check in on Monday and check out on Tuesday, that's 1
        // day,
        // but the difference in days would be 0
        days = days + 1;

        if (days <= 0)
            return 0.0;

        // Sum price for each parcel
        double totalPrice = 0.0;
        for (ParcelaOccupancy parcelaOcc : parcelas) {
            Parcela parcela = parcelaOcc.getParcela();
            int occupants = parcelaOcc.getOccupancy();

            // Price = (euros per person) * (number of occupants) * (number of days)
            double parcelPrice = parcela.getEurPorPersona() * occupants * days;

            totalPrice += parcelPrice;
        }

        return totalPrice;
    }

    /**
     * Actualiza el TextView que muestra el precio con el precio calculado.
     * 
     * @param priceDisplay TextView donde mostrar el precio
     * @param checkInDate  Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @param parcelas     Lista de parcelas con su ocupación
     */
    public static void updatePriceDisplay(TextView priceDisplay, Date checkInDate,
            Date checkOutDate, List<ParcelaOccupancy> parcelas) {
        if (priceDisplay == null)
            return;

        double price = calculateReservationPrice(checkInDate, checkOutDate, parcelas);
        priceDisplay.setText(String.format(Locale.getDefault(), 
            priceDisplay.getContext().getString(R.string.price_total), price));
    }
}