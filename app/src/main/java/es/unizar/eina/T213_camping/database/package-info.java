/**
 * Componentes de base de datos para la aplicación T213 Camping.
 * 
 * <h2>Contenido del Paquete:</h2>
 * <ul>
 *   <li>{@link es.unizar.eina.T213_camping.database.models} - Modelos de datos:
 *     <ul>
 *       <li>Parcela - Entidad de parcela de camping</li>
 *       <li>Reserva - Entidad de reserva</li>
 *       <li>ParcelaReservada - Entidad de parcela reservada</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.daos} - Objetos de Acceso a Datos:
 *     <ul>
 *       <li>ParcelaDao - Operaciones sobre parcelas</li>
 *       <li>ReservaDao - Operaciones sobre reservas</li>
 *       <li>ParcelaReservadaDao - Operaciones sobre parcelas reservadas</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.repositories} - Repositorios:
 *     <ul>
 *       <li>Implementación de lógica de negocio</li>
 *       <li>Coordinación de operaciones de datos</li>
 *     </ul>
 *   </li>
 *   <li>{@link es.unizar.eina.T213_camping.database.converters} - Conversores de tipos para la base de datos Room</li>
 * </ul>
 * 
 * <h2>Características de la Base de Datos:</h2>
 * <ul>
 *   <li>Integración con la biblioteca de persistencia Room</li>
 *   <li>Gestión de base de datos SQLite</li>
 *   <li>Manejo de relaciones entre entidades</li>
 *   <li>Conversión de tipos de datos</li>
 * </ul>
 */
package es.unizar.eina.T213_camping.database;