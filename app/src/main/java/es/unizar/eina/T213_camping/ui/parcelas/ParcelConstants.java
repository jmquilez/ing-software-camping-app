package es.unizar.eina.T213_camping.ui.parcelas;

/**
 * Constantes utilizadas en la gestión de parcelas.
 * Define las claves para operaciones, criterios de ordenación y detalles de parcelas
 * que se utilizan en la comunicación entre actividades y en la interfaz de usuario.
 */
public class ParcelConstants {
    /**
     * Clave para identificar el tipo de operación en los Intents.
     */
    public static final String OPERATION_TYPE = "OPERATION_TYPE";

    /**
     * Operación de inserción de una nueva parcela.
     */
    public static final String OPERATION_INSERT = "OPERATION_INSERT";

    /**
     * Operación de actualización de una parcela existente.
     */
    public static final String OPERATION_UPDATE = "OPERATION_UPDATE";

    /**
     * Operación de eliminación de una parcela.
     */
    public static final String OPERATION_DELETE = "OPERATION_DELETE";

    /**
     * Operación de actualización de una parcela con cambio de nombre.
     */
    public static final String OPERATION_UPDATE_WITH_NAME = "OPERATION_UPDATE_WITH_NAME";

    /**
     * Criterio de ordenación por identificador (nombre) de la parcela.
     */
    public static final String SORT_ID = "SORT_ID";

    /**
     * Criterio de ordenación por número máximo de ocupantes.
     */
    public static final String SORT_MAX_OCCUPANTS = "SORT_MAX_OCCUPANTS";

    /**
     * Criterio de ordenación por precio por persona.
     */
    public static final String SORT_EUR_PERSONA = "SORT_EUR_PERSONA";

    /**
     * Clave para el nombre de la parcela en los Intents.
     */
    public static final String PARCEL_NAME = "PARCEL_NAME";

    /**
     * Clave para el nombre anterior de la parcela en los Intents.
     */
    public static final String OLD_PARCEL_NAME = "OLD_PARCEL_NAME";

    /**
     * Clave para el número máximo de ocupantes en los Intents.
     */
    public static final String MAX_OCCUPANTS = "MAX_OCCUPANTS";

    /**
     * Clave para el precio por persona en los Intents.
     */
    public static final String PRICE_PER_PERSON = "PRICE_PER_PERSON";

    /**
     * Clave para la descripción de la parcela en los Intents.
     */
    public static final String DESCRIPTION = "DESCRIPTION";

    /**
     * Límites de validación para los campos de parcelas
     */
    public static final int MAX_NAME_LENGTH = 60;
    public static final int MAX_DESCRIPTION_LENGTH = 300;
    public static final int MAX_OCCUPANTS_NUM = 999;
    public static final double MAX_PRICE = 999.0;
    public static final int MIN_OCCUPANTS = 1;
    public static final double MIN_PRICE = 0.0;
    public static final int MAX_PARCELS = 100;
}
