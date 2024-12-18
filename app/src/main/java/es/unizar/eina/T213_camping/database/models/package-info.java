/**
 * Entidades y modelos de datos para la base de datos Room.
 * 
 * <h2>Entidades principales:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.models.Parcela} - Parcela del camping:
 *     <ul>
 *       <li>Información básica de parcelas</li>
 *       <li>Capacidad y características</li>
 *       <li>Estado y disponibilidad</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.models.Reserva} - Reserva de parcelas:
 *     <ul>
 *       <li>Datos del cliente</li>
 *       <li>Fechas de estancia</li>
 *       <li>Información de pago</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.models.ParcelaReservada} - Relación parcela-reserva:
 *     <ul>
 *       <li>Vinculación entre parcelas y reservas</li>
 *       <li>Gestión de ocupación</li>
 *       <li>Control de disponibilidad</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Características:</h2>
 * <ul>
 *   <li>Anotadas con {@code @Entity} para mapeo a tablas</li>
 *   <li>Claves primarias y foráneas</li>
 *   <li>Índices para optimización</li>
 *   <li>Validaciones de datos</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database.models; 