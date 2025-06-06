package es.unizar.eina.T213_camping.ui.reservas;

/**
 * Constantes comunes para la gestión de reservas.
 * Centraliza los límites y restricciones usados en múltiples clases.
 */
public class ReservationConstants {
    /**
     * Clave para identificar el tipo de operación en los Intents.
     */
    public static final String OPERATION_TYPE = "OPERATION_TYPE";

    /**
     * Operación para insertar una nueva reserva.
     */
    public static final String OPERATION_INSERT = "OPERATION_INSERT";

    /**
     * Operación para actualizar una reserva existente.
     */
    public static final String OPERATION_UPDATE = "OPERATION_UPDATE";

    /**
     * Operación para eliminar una reserva existente.
     */
    public static final String OPERATION_DELETE = "OPERATION_DELETE";

    /**
     * Operación para notificar al cliente sobre su reserva.
     */
    public static final String OPERATION_NOTIFY_CLIENT = "OPERATION_NOTIFY_CLIENT";

    /**
     * Criterio de ordenación por nombre del cliente.
     */
    public static final String SORT_CLIENT_NAME = "SORT_CLIENT_NAME";

    /**
     * Criterio de ordenación por teléfono del cliente.
     */
    public static final String SORT_CLIENT_PHONE = "SORT_CLIENT_PHONE";

    /**
     * Criterio de ordenación por fecha de entrada.
     */
    public static final String SORT_ENTRY_DATE = "SORT_ENTRY_DATE";

    /**
     * Identificador único de la reserva.
     */
    public static final String RESERVATION_ID = "RESERVATION_ID";

    /**
     * Nombre del cliente asociado a la reserva.
     */
    public static final String CLIENT_NAME = "CLIENT_NAME";

    /**
     * Número de teléfono del cliente.
     */
    public static final String CLIENT_PHONE = "CLIENT_PHONE";

    /**
     * Precio de la reserva en euros.
     */
    public static final String RESERVATION_PRICE = "RESERVATION_PRICE";

    /**
     * Fecha de entrada de la reserva.
     */
    public static final String ENTRY_DATE = "ENTRY_DATE";

    /**
     * Fecha de salida de la reserva.
     */
    public static final String DEPARTURE_DATE = "DEPARTURE_DATE";

    /**
     * Lista de parcelas seleccionadas para la reserva.
     */
    public static final String SELECTED_PARCELS = "SELECTED_PARCELS";

    /**
     * Indicador de que el adaptador de parcelas añadidas ha iniciado una actualización.
     * Utilizado para prevenir actualizaciones recursivas.
     */
    public static final String ADDED_PARCELS_ADAPTER_CALLED = "ADDED_PARCELS_ADAPTER_CALLED";

    /**
     * Indicador de que el adaptador de parcelas disponibles ha iniciado una actualización.
     * Utilizado para prevenir actualizaciones recursivas.
     */
    public static final String AVAILABLE_PARCELS_ADAPTER_CALLED = "AVAILABLE_PARCELS_ADAPTER_CALLED";

    /**
     * Validation constraints (matching repository)
     */
    public static final int MAX_CLIENT_NAME_LENGTH = 40;
    public static final int PHONE_LENGTH = 9;
    public static final double MIN_PRICE = 0.0;
    public static final double MAX_PRICE = 99999.0;
    public static final int MIN_ID = 1;
    public static final int MAX_ID = 10000;
}
