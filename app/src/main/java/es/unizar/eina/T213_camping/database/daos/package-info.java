/**
 * Objetos de Acceso a Datos (DAOs) para la base de datos Room.
 * 
 * <h2>DAOs disponibles:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.daos.ParcelaDao} - Operaciones sobre parcelas:
 *     <ul>
 *       <li>Inserción y actualización de parcelas</li>
 *       <li>Consulta de parcelas disponibles</li>
 *       <li>Eliminación de parcelas</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.daos.ReservaDao} - Operaciones sobre reservas:
 *     <ul>
 *       <li>Gestión de reservas</li>
 *       <li>Consultas por fecha</li>
 *       <li>Operaciones CRUD</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.daos.ParcelaReservadaDao} - Relación parcelas-reservas:
 *     <ul>
 *       <li>Gestión de ocupación</li>
 *       <li>Consultas de disponibilidad</li>
 *       <li>Relaciones entre entidades</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Características:</h2>
 * <ul>
 *   <li>Anotados con {@code @Dao} para integración con Room</li>
 *   <li>Métodos asíncronos para operaciones de base de datos</li>
 *   <li>Consultas optimizadas mediante SQL</li>
 *   <li>Soporte para LiveData y Flow</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database.daos; 