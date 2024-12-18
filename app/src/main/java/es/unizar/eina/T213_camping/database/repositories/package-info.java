/**
 * Repositorios para la gestión de datos de la aplicación.
 * 
 * <h2>Repositorios disponibles:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.repositories.ParcelaRepository} - Gestión de parcelas:
 *     <ul>
 *       <li>Operaciones CRUD sobre parcelas</li>
 *       <li>Consultas de disponibilidad</li>
 *       <li>Filtrado y ordenación</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.repositories.ReservaRepository} - Gestión de reservas:
 *     <ul>
 *       <li>Creación y modificación de reservas</li>
 *       <li>Consultas por fecha y cliente</li>
 *       <li>Gestión de ocupación</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.repositories.ParcelaReservadaRepository} - Gestión de relaciones parcela-reserva:
 *     <ul>
 *       <li>Asignación de parcelas a reservas</li>
 *       <li>Control de disponibilidad temporal</li>
 *       <li>Gestión de ocupación múltiple</li>
 *       <li>Validación de fechas</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Características:</h2>
 * <ul>
 *   <li>Implementación del patrón Repository</li>
 *   <li>Abstracción de la fuente de datos</li>
 *   <li>Operaciones asíncronas</li>
 *   <li>Caché de datos</li>
 *   <li>Manejo de transacciones</li>
 * </ul>
 * 
 * <h2>Responsabilidades:</h2>
 * <ul>
 *   <li>Centralizar el acceso a datos</li>
 *   <li>Coordinar múltiples fuentes de datos</li>
 *   <li>Implementar lógica de negocio</li>
 *   <li>Gestionar el estado de la aplicación</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database.repositories; 