/**
 * Database components for the T213 Camping application.
 * 
 * <h2>Package Contents:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.models} - Data models:
 *     <ul>
 *       <li>Parcela - Camping plot entity</li>
 *       <li>Reserva - Reservation entity</li>
 *       <li>ParcelaReservada - Reserved plot entity</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.daos} - Data Access Objects:
 *     <ul>
 *       <li>ParcelaDao - Plot operations</li>
 *       <li>ReservaDao - Reservation operations</li>
 *       <li>ParcelaReservadaDao - Reserved plot operations</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.repositories} - Repositories:
 *     <ul>
 *       <li>Business logic implementation</li>
 *       <li>Data operations coordination</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.converters} - Type converters for Room database</li>
 * </ul>
 * 
 * <h2>Database Features:</h2>
 * <ul>
 *   <li>Room persistence library integration</li>
 *   <li>SQLite database management</li>
 *   <li>Entity relationships handling</li>
 *   <li>Data type conversion</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database;